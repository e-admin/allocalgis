/*
 * 
 * Created on 13-ago-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print.elements;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.Page;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElementsListener;

import com.geopista.ui.plugin.scalebar.GeopistaScaleBarRenderer;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ScaleBarFrame extends GraphicElements
{
	public int linkedToMapNumber=0; // 0 means not linked
	private ScaleBarFrameListener	listener;
	private ScaleBarFrameOnScreen	onScreen;
	private ScaleBarFrameForPrint   forPrint;
	public class ScaleBarFrameOnScreen extends JComponent {
		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long	serialVersionUID	= 4050486716059890224L;
		ImageIcon icon=null;
		private GeopistaScaleBarRenderer render;

		public ScaleBarFrameOnScreen() {
			super();

			setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
			addMouseListener(listener);
			addMouseMotionListener(listener);
		}
		public void setIcon(ImageIcon icon)
		{
			this.icon=icon;
		}

		public void setRenderer (GeopistaScaleBarRenderer render)
		{
			this.render = render;
		}

		public void paint (Graphics g)
		{
			super.paint(g);

			if (render!=null)
			{
				render.setHeight(this.getHeight());
				render.setTransparency(getTransparency());
				render.setPaintingArea(isPaintingArea());
				render.setIncrementCount(getIncrementCount());
				render.setIdealIncrement(this.getWidth()/getIncrementCount());
				MapFrame mf=getMap(linkedToMapNumber);
				double scale;
				if (mf==null)
					scale=parent.getPlugInContext().getLayerViewPanel().
					getViewport().getScale();
				else
					scale=mf.getScale();
				if (true) //TODO: implement styles
				{
					render.setStyle(1);
				}
				else
				{
					render.setStyle(2);
				}
				
				render.paint((Graphics2D) g,scale);
				if (linkedToMapNumber>0)
				{	
					//render.paint((Graphics2D) g,scale, mf.getEscala());
					
					String label="->Map:"+linkedToMapNumber;
					LineMetrics lm=g.getFontMetrics().getLineMetrics(label,g);
					Rectangle2D rect=g.getFontMetrics().getStringBounds(label,g);
					int x=(int) ((getWidth()-rect.getWidth())/2);
					int y=(int) ((getHeight()-rect.getHeight())/2);
					g.setColor(Color.GRAY);
					g.fillRoundRect(x-3+2,(int)(y+6-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);
					g.setColor(Color.WHITE);
					g.fillRoundRect(x-3,(int)(y+3-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);
					g.setColor(Color.gray);
					g.drawRoundRect(x-3,(int)(y+3-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);

					g.drawString(label,x,y);
				}
				else
				{
					//render.paint((Graphics2D) g,scale);					
				}				
			}
		}
	}

	public class ScaleBarFrameForPrint extends JComponent {

		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long	serialVersionUID	= 3834877987673158195L;
		private GeopistaScaleBarRenderer render;

		public ScaleBarFrameForPrint() {
			super();

		}

		public void print(Graphics arg0)
		{
			// TODO Auto-generated method stub
			super.print(arg0);
		}
		protected void printComponent(Graphics arg0)
		{
			// TODO Auto-generated method stub
			super.printComponent(arg0);
		}

		public void setRenderer (GeopistaScaleBarRenderer render)
		{
			this.render = render;
		}

		public void paint (Graphics g)
		{
			if (render!=null)
			{
				render.setHeight(this.getHeight());
				render.setWidth(this.getWidth());
				render.setTransparency(getTransparency());
				render.setPaintingArea(isPaintingArea());
				render.setIncrementCount(getIncrementCount());
				render.setIdealIncrement(this.getWidth()/getIncrementCount());
				double scale;
				MapFrame mf=getMap(linkedToMapNumber);
				if (mf==null)
					scale=parent.getPlugInContext().getLayerViewPanel().
					getViewport().getScale();
				else
					scale=mf.getScaleForPrint();

				render.paint((Graphics2D) g,scale);
			}
		}

		public void fixerDimensions() {
			switch (parent.getZoomActif()) {
			case PrintLayoutFrame.PAGE_ENTIERE:
				super
				.setBounds(
						(int) ((onScreen.getBounds().getX() * parent
								.getPrintLayoutPreviewPanel()
								.getPreviewPanel().getPage()
								.getPageForPrint().getWidth()) / ((Page.PageDrawOnScreen) parent
										.getPrintLayoutPreviewPanel()
										.getPreviewPanel().getPage()
										.getPageDrawOnScreen()).getCenter()
										.getWidth()),
										(int) ((onScreen.getBounds().getY() * parent
												.getPrintLayoutPreviewPanel()
												.getPreviewPanel().getPage()
												.getPageForPrint().getHeight()) / ((Page.PageDrawOnScreen) parent
														.getPrintLayoutPreviewPanel()
														.getPreviewPanel().getPage()
														.getPageDrawOnScreen()).getCenter()
														.getHeight()),
														(int) ((onScreen.getBounds().getWidth() * parent
																.getPrintLayoutPreviewPanel()
																.getPreviewPanel().getPage()
																.getPageForPrint().getWidth()) / ((Page.PageDrawOnScreen) parent
																		.getPrintLayoutPreviewPanel()
																		.getPreviewPanel().getPage()
																		.getPageDrawOnScreen()).getCenter()
																		.getWidth()),
																		(int) ((onScreen.getBounds().getHeight() * parent
																				.getPrintLayoutPreviewPanel()
																				.getPreviewPanel().getPage()
																				.getPageForPrint().getHeight()) / ((Page.PageDrawOnScreen) parent
																						.getPrintLayoutPreviewPanel()
																						.getPreviewPanel().getPage()
																						.getPageDrawOnScreen()).getCenter()
																						.getHeight()));
				break;
			case PrintLayoutFrame.LARGEUR_PAGE:
				super.setBounds((int) onScreen.getBounds().getX(),
						(int) onScreen.getBounds().getY(), (int) onScreen
						.getBounds().getWidth(), (int) onScreen
						.getBounds().getHeight());
				break;
			}
			super.setLocation((int) super.getBounds().getX(), (int) super
					.getBounds().getY());
			super.repaint();
		}
	}

	public class ScaleBarFrameListener extends GraphicElementsListener {
		public ScaleBarFrameListener(GraphicElements ge, PrintLayoutFrame plf) {
			super(ge, plf);
		}

		public void mouseClicked(MouseEvent e)
		{
			if (SwingUtilities.isLeftMouseButton(e))
			{
				if (e.getClickCount() == 2)
				{
					ScaleBarConfigDialog dlg = new ScaleBarConfigDialog(getParent(),ScaleBarFrame.this);

					dlg.setVisible(true);

					repaint();
				}
				else{
					if (parent==null)return;
					if (parent.getSelectedComponent() != null) {
						parent.getSelectedComponent().setSelected(false);
					}
					ge.setSelected(true);
					parent.setSelectedComponent(ge);
					parent.validate();
				}
			}
		}
	}
	
	public ScaleBarFrame(PrintLayoutFrame plf) {
		setParent(plf);

//		northSymbol = createImageIcon(
//		"/reso/jumpPlugIn/printLayoutPlugIn/images/North.gif",
//		"NorthSymbol");
		listener = new ScaleBarFrameListener(this, parent);
		onScreen = new ScaleBarFrameOnScreen();
		forPrint = new ScaleBarFrameForPrint();
		initValeursSpec(plf);
	}
	/**
	 * 
	 */
	public ScaleBarFrame()
	{
		listener = new ScaleBarFrameListener(this, parent);
		onScreen = new ScaleBarFrameOnScreen();
		forPrint = new ScaleBarFrameForPrint();
	}
	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#isSelected()
	 */
	public boolean isSelected() {
		return getSelect();
	}

	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#setSelected(boolean)
	 */
	public void setSelected(boolean select) {
		setSelect(select);
		if (isSelected()) {
			onScreen.setBorder(selectedBorder);
		} else {
			onScreen.setBorder(null);
		}
	}

	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#getGraphicElementsOnScreen()
	 */

	public JComponent getGraphicElementsOnScreen() {
		return onScreen;
	}

	public JComponent getGraphicElementsForPrint() {
		return forPrint;
	}

	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#initCornerPoint()
	 */
	public void initCornerPoint() {
		topLeftCorner = new Point(0, 0);
		topRightCorner = new Point((int) onScreen.getBounds().getWidth(), 0);
		BottomLeftCorner = new Point(0, (int) onScreen.getBounds().getHeight());
		BottomRightCorner = new Point((int) onScreen.getBounds().getWidth(),
				(int) onScreen.getBounds().getHeight());
	}
	private Point topLeftCorner = new Point();

	private Point topRightCorner = new Point();

	private Point BottomLeftCorner = new Point();

	private Point BottomRightCorner = new Point();
	private int	transparency=255;
	private boolean	paintingArea=false;
	private int	increment_count=3;
	private int	scale=MapFrame.CURRENT_EXTENT; //@see MapFrame setExtentType
	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#getCornerPoint()
	 */

	public Point[] getCornerPoint() {
		Point[] points = new Point[4];
		points[0] = topLeftCorner;
		points[1] = topRightCorner;
		points[2] = BottomLeftCorner;
		points[3] = BottomRightCorner;
		return points;
	}


	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#fixerDimensions(int, int, int, int, int, int)
	 */
	public void fixerDimensions(int x, int y, int w, int h, int facteur1,
			int facteur2) {
		onScreen.setBounds(x, y, w, h);
		onScreen.setLocation(x, y);
		forPrint.fixerDimensions();
//		resizeIcon();
		initCornerPoint();
		repaint();
	}

	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#repaint()
	 */
	public void repaint() {
//		resizeIcon();
		onScreen.repaint();
		forPrint.repaint();
	}

	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#refreshForPrintBounds()
	 */
	public void refreshForPrintBounds() {
		forPrint.fixerDimensions();
	}

	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#resize(int, int, int, int, int, int)
	 */
	public void resize(int newForPrintWidth, int oldForPrintWidth,
			int newForPrintHeight, int oldForPrintHeight, int widthOnScreen,
			int heightOnScreen) {
		forPrint
		.setBounds(
				(int) ((forPrint.getBounds().getX() * newForPrintWidth) / oldForPrintWidth),
				(int) ((forPrint.getBounds().getY() * newForPrintHeight) / oldForPrintHeight),
				(int) ((forPrint.getBounds().getWidth() * newForPrintWidth) / oldForPrintWidth),
				(int) ((forPrint.getBounds().getHeight() * newForPrintHeight) / oldForPrintHeight));
		forPrint.setLocation((int) forPrint.getBounds().getX(), (int) forPrint
				.getBounds().getY());
		switch (parent.getZoomActif()) {
		case PrintLayoutFrame.PAGE_ENTIERE:
			onScreen
			.setBounds(
					(int) ((forPrint.getBounds().getX() * widthOnScreen) / newForPrintWidth),
					(int) ((forPrint.getBounds().getY() * heightOnScreen) / newForPrintHeight),
					(int) ((forPrint.getBounds().getWidth() * widthOnScreen) / newForPrintWidth),
					(int) ((forPrint.getBounds().getHeight() * heightOnScreen) / newForPrintHeight));
			onScreen.setLocation((int) onScreen.getBounds().getX(),
					(int) onScreen.getBounds().getY());
			break;
		case PrintLayoutFrame.LARGEUR_PAGE:
			onScreen.setBounds((int) forPrint.getBounds().getX(),
					(int) forPrint.getBounds().getY(), (int) forPrint
					.getBounds().getWidth(), (int) forPrint.getBounds()
					.getHeight());
			onScreen.setLocation((int) forPrint.getBounds().getX(),
					(int) forPrint.getBounds().getY());
			break;
		}
//		resizeIcon();

initCornerPoint();
repaint();
	}

	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#setBorder(javax.swing.border.Border)
	 */
	public void setBorder(Border border){
		onScreen.setBorder(border);
	}

	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#setValeursSpec()
	 */
	public void setValeursSpec() {
		setBorder(null);
		onScreen.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		onScreen.validate();
	}

	/* (non-Javadoc)
	 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements#initValeursSpec(reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame)
	 */
	public void initValeursSpec(PrintLayoutFrame plf)
	{
		parent=plf;
		//render=new GeopistaScaleBarRenderer(plf.getPlugInContext().getLayerViewPanel());

		onScreen.setRenderer(new GeopistaScaleBarRenderer(plf.getPlugInContext().getLayerViewPanel()));
		forPrint.setRenderer(new GeopistaScaleBarRenderer(plf.getPlugInContext().getLayerViewPanel()));


	}
	public int getTopLeftCornerx(){
		return(topLeftCorner.x);
	}

	public void setTopLeftCornerx(int n){
		topLeftCorner.x = n;
	}

	public int getTopLeftCornery(){
		return(topLeftCorner.y);
	}

	public void setTopLeftCornery(int n){
		topLeftCorner.y = n;
	}

	public int getTopRightCornerx(){
		return(topRightCorner.x);
	}

	public void setTopRightCornerx(int n){
		topRightCorner.x = n;
	}

	public int getTopRightCornery(){
		return(topRightCorner.y);
	}

	public void setTopRightCornery(int n){
		topRightCorner.y = n;
	}

	public int getBottomLeftCornerx(){
		return(BottomLeftCorner.x);
	}

	public void setBottomLeftCornerx(int n){
		BottomLeftCorner.x = n;
	}

	public int getBottomLeftCornery(){
		return(BottomLeftCorner.y);
	}

	public void setBottomLeftCornery(int n){
		BottomLeftCorner.y = n;
	}

	public int getBottomRightCornerx(){
		return(BottomRightCorner.x);
	}

	public void setBottomRightCornerx(int n){
		BottomRightCorner.x = n;
	}

	public int getBottomRightCornery(){
		return(BottomRightCorner.y);
	}

	public void setBottomRightCornery(int n){
		BottomRightCorner.y = n;
	}

	public int getPrintHeight(){
		return(forPrint.getHeight());
	}

	public void setPrintHeight(int h){
		int w = forPrint.getWidth();
		Dimension d = new Dimension();
		d.setSize(w,h);
		forPrint.setSize(d);
	}

	public int getPrintWidth(){
		return(forPrint.getWidth());
	}

	public void setPrintWidth(int w){
		int h = forPrint.getHeight();
		Dimension d = new Dimension();
		d.setSize(w,h);
		forPrint.setSize(d);
	}
	public int getPrintX(){
		return(forPrint.getX());
	}

	public void setPrintX(int x){
		int y = forPrint.getY();
		Point p = new Point();
		p.setLocation(x,y);
		forPrint.setLocation(p);
	}
	public int getPrintY(){
		return(forPrint.getY());
	}

	public void setPrintY(int y){
		int x = forPrint.getX();
		Point p = new Point();
		p.setLocation(x,y);
		forPrint.setLocation(p);
	}

	public int getScreenHeight(){
		return(onScreen.getHeight());
	}

	public void setScreenHeight(int h){
		int w = onScreen.getWidth();
		Dimension d = new Dimension();
		d.setSize(w,h);
		onScreen.setSize(d);
	}

	public int getScreenWidth(){
		return(onScreen.getWidth());
	}

	public void setScreenWidth(int w){
		int h = onScreen.getHeight();
		Dimension d = new Dimension();
		d.setSize(w,h);
		onScreen.setSize(d);
	}
	public int getScreenX(){
		return(onScreen.getX());
	}

	public void setScreenX(int x){
		int y = onScreen.getY();
		Point p = new Point();
		p.setLocation(x,y);
		onScreen.setLocation(p);
	}
	public int getScreenY(){
		return(onScreen.getY());
	}

	public void setScreenY(int y){
		int x = onScreen.getX();
		Point p = new Point();
		p.setLocation(x,y);
		onScreen.setLocation(p);
	}

	public int getIncrementCount()
	{
		return increment_count;
	}
	public int getTransparency()
	{
		return transparency;
	}
	public boolean isPaintingArea()
	{
		return paintingArea;
	}
	public void setIncrementCount(int increment_count)
	{
		this.increment_count=increment_count;
	}
	public void setPaintingArea(boolean paintArea)
	{
		this.paintingArea=paintArea;
	}
	public void setTransparency(int transparency)
	{
		this.transparency=transparency;

	}
	private MapFrame getMap(int number)
	{
		List elem=parent.getPage().getGraphicElement();
		int cont=1;
		for (Iterator iter = elem.iterator(); iter.hasNext();)
		{
			GraphicElements element = (GraphicElements) iter.next();
			if (element instanceof MapFrame)
			{
				if(cont==number)return (MapFrame) element;
				cont++;
			}


		}
		return null;
	}
	public int getLinkedToMapNumber()
	{
		return linkedToMapNumber;
	}
	public void setLinkedToMapNumber(int linkedToMapNumber)
	{
		//render=new GeopistaScaleBarRenderer(plf.getPlugInContext().getLayerViewPanel());
		MapFrame mf = getMap(linkedToMapNumber);
		onScreen.setRenderer(new GeopistaScaleBarRenderer(mf.getLayerViewPanelOnScreen()));
		forPrint.setRenderer(new GeopistaScaleBarRenderer(mf.getLayerViewPanelForPrint()));


		this.linkedToMapNumber = linkedToMapNumber;
	}
}
