/*
 * Package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 25 oct. 2004
 *
 * Développé dans le cadre du Projet APARAD 
 *  (Laboratoire Reso UMR ESO 6590 CNRS / Bassin Versant du Jaudy-Guindy-Bizien)
 *    Responsable : Erwan BOCHER
 *    Développeurs : Céline FOUREAU, Olivier BEDEL
 *
 * olivier.bedel@uhb.fr ou olivier.bedel@yahoo.fr
 * erwan.bocher@uhb.fr ou erwan.bocher@free.fr
 * celine.foureau@uhb.fr ou celine.foureau@wanadoo.fr
 * 
 * Ce package hérite de la licence GPL de JUMP. Il est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. (http://www.cecill.info)
 * 
 */

package com.geopista.ui.plugin.print.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.font.LineMetrics;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.Page;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.Page.PrintableZone;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElementsListener;

import com.geopista.model.GeopistaLayer;
import com.geopista.ui.renderer.UncachedLayerRenderer;
import com.geopista.util.UtilsPrintPlugin;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelContext;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;

/**
 * @author FOUREAU_C
 */
public class MapFrame extends GraphicElements {

	public static final int	FULL_EXTENT	= 1;

	public static final int	CURRENT_EXTENT	= 2;
	public static final int	CUSTOM_EXTENT	= 0;

	private Point topLeftCorner = new Point();

	private Point topRightCorner = new Point();

	private Point BottomLeftCorner = new Point();

	private Point BottomRightCorner = new Point();



	private MapFrameOnScreen onScreen;

	private MapFrameForPrint forPrint;

	private MapFrameListener listener;

	private LineBorder	border;

	private int	borderThickness;

	private Color	borderColor;

	private int	extentType	= CURRENT_EXTENT; // 1 = full extent  2= current extent
	private int resizedOnView = -1;

	public MapFrame(PrintLayoutFrame plf){

		parent = plf;
		
		listener = new MapFrameListener(this, parent);
		onScreen = new MapFrameOnScreen(parent.getPlugInContext().getLayerManager(), parent.getLayerViewPanelContext(),this);
		forPrint = new MapFrameForPrint(parent.getPlugInContext().getLayerManager(), parent.getLayerViewPanelContext());
	}

	public MapFrame(){
		listener = new MapFrameListener(this, null);
		onScreen = new MapFrameOnScreen(this);
		forPrint = new MapFrameForPrint();
	}

	public boolean isSelected() {
		return getSelect();
	}

	public void setSelected(boolean select) {
		setSelect(select);
		if (isSelected()) {
			onScreen.setBorder(selectedBorder);
		} else {
			onScreen.setBorder(getBorder());
		}
	}

	public JComponent getGraphicElementsOnScreen() {
		return onScreen;
	}

	public JComponent getGraphicElementsForPrint() {
		return forPrint;
	}

	public void initCornerPoint() {
		topLeftCorner = new Point(0, 0);
		topRightCorner = new Point((int) onScreen.getBounds().getWidth(), 0);
		BottomLeftCorner = new Point(0, (int) onScreen.getBounds().getHeight());
		BottomRightCorner = new Point((int) onScreen.getBounds().getWidth(),
				(int) onScreen.getBounds().getHeight());
	}

	public Point[] getCornerPoint() {
		Point[] points = new Point[4];
		points[0] = topLeftCorner;
		points[1] = topRightCorner;
		points[2] = BottomLeftCorner;
		points[3] = BottomRightCorner;
		return points;
	}
	public void fixerDimensions(int x, int y, int w, int h, int facteur1,
			int facteur2) {
		onScreen.setBounds(x, y, w, h);
		onScreen.setLocation(x, y);
		forPrint.fixerDimensions();
		//initZoom(); //modif obedel differe dans repaint
		initCornerPoint();

		repaint();
	}

	public void refreshForPrintBounds() {
		forPrint.fixerDimensions();

	}

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
			onScreen/*setBounds(0,0, 325, 175);*/
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
		initCornerPoint();
		initZoom(); // modif obedel -> differe dans repaint
		//repaint();
	}
	// modif obedel pour conservation du zoom courant de la tache
	public void initZoom(){
		//try {
			//try {
				//onScreen.getViewport().zoomToFullExtent();
				//forPrint.getViewport().zoomToFullExtent();
				//} catch (NoninvertibleTransformException e) {
					//    e.printStackTrace();
					//}
//				Viewport oldViewport = parent.getPlugInContext().getLayerViewPanel().getViewport();
//		Envelope mapEnvelope = oldViewport.getEnvelopeInModelCoordinates(); 
//		onScreen.getViewport().zoom(mapEnvelope);
//		forPrint.getViewport().zoom(mapEnvelope);
		onScreen.setExtent();
		forPrint.setExtent();
		//onScreen.getViewport().update();//.initialize(oldViewport.getScale(),oldViewport.getOriginInModelCoordinates());
		//forPrint.getViewport().update();//.initialize(oldViewport.getScale(),oldViewport.getOriginInModelCoordinates());
//		} 
//		catch (NoninvertibleTransformException e) {
//		// Exception handling
//		e.printStackTrace();
//		}
		onScreen.repaint();
		forPrint.repaint();
	}



	public void setBorder(Border border) {
		this.border = (LineBorder) border;
		if (border != null){
			borderThickness = ((LineBorder) this.getBorder()).getThickness();
			borderColor = ((LineBorder) this.getBorder()).getLineColor();
		}
		onScreen.setBorder(border);
		forPrint.setBorder(border);
		repaint();
	}

	/**
	 * @return
	 */
	private LineBorder getBorder()
	{

		return border;
	}

	public Color getBorderColor() {
		return borderColor;
	}


	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		this.border = (LineBorder) BorderFactory.createLineBorder(borderColor,borderThickness);
//		if (onScreen!=null)
//		onScreen.setBorder(this.border);
//		if (forPrint!=null)
//		forPrint.setBorder(this.border);

	}

	public int getBorderThickness() {
		return borderThickness;
	}

	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		this.border = (LineBorder) BorderFactory.createLineBorder(borderColor,borderThickness);
//		onScreen.setBorder(this.border);
//		forPrint.setBorder(this.border);
//		repaint();
	}

	public void repaint() {
		//modif obedel -> simplification : appel au recalage du zoom vant chaque trace
		initZoom();
		onScreen.repaint();
		forPrint.repaint();
	}

	public class MapFrameOnScreen extends LayerViewPanel implements IMapFrameOnScreen {      
		private MapFrame mapFrame;
		private double escala = 0;
		private boolean reescalado = false;
		
		
		public MapFrameOnScreen(LayerManager layerManager, LayerViewPanelContext context, MapFrame mapframe) {
			super(layerManager, context);
			this.mapFrame=mapframe;
			
			
			String scale = (String)parent.getExtentManager().getParameters().get(UtilsPrintPlugin.PARAM_CONFIG_ID_ESCALA);
			extentType = (scale != null)? Integer.parseInt(scale) : -1;
			//superRepaint();
			super.setBorder(BorderFactory.createLineBorder(Color.GRAY));

			//setExtent();
			addMouseListener(listener);
			addMouseMotionListener(listener);
		}		

		public MapFrameOnScreen(MapFrame mf){		
			this.mapFrame = mf;
		}
		
		private void setExtent()
		{
			if (parent== null)
				return;
			
			ExtentManager extentMgr = parent.getExtentManager();
			Envelope envelope = extentMgr.getEnvelope(extentType, MapFrame.this);
			try
			{
				Envelope env=getViewport().getEnvelopeInModelCoordinates();
				if (!env.equals(envelope)){
					//Para los puntos el valor de 0,03 da igual internamente dentro de este
					//metodo se pone 15
					getViewport().zoom(EnvelopeUtil.bufferByFraction(envelope, 0.03));
					//getViewport().zoom(envelope);
				}
			}
			catch (NoninvertibleTransformException e)
			{
				e.printStackTrace();
			}
		}
		
		/* (non-Javadoc)
		 * @see com.geopista.ui.plugin.print.elements.IMapFrameOnScreen#getNormalizedScale(double)
		 */
		@Override
		public double getNormalizedScale(double scale) {		
			if (escala == 0 || reescalado)
			{
				System.out.println("escala es: " + scale ); 
				
				escala = scale;
				setReescalado(false);
			}
			
			double alto = ((PrintLayoutFrame)mapFrame.getParent()).getPage().getHauteur();
			double altoPx = ((PrintLayoutFrame)mapFrame.getParent()).getPrintLayoutPreviewPanel().getViewport().getPreferredSize().getHeight();
			double ppm = ((alto/72) * 2.54 / 100)/altoPx; //altoPx / ((alto/72) * 2.54 / 100);
			
			return getViewport().getNormalizedScaleForPPM(escala, ppm);				
		}
		
		/* (non-Javadoc)
		 * @see com.geopista.ui.plugin.print.elements.IMapFrameOnScreen#initValeursSpec2(reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame, com.geopista.ui.plugin.print.elements.MapFrame)
		 */
		
		public void initValeursSpec2(PrintLayoutFrame plf, MapFrame mapFrame){
			super.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			addMouseListener(listener);
			addMouseMotionListener(listener);
		}
		
		/* (non-Javadoc)
		 * @see com.geopista.ui.plugin.print.elements.IMapFrameOnScreen#setReescalado(boolean)
		 */
		@Override
		public void setReescalado (boolean reescalado)
		{
			this.reescalado = reescalado;
		}
		
		/* (non-Javadoc)
		 * @see com.geopista.ui.plugin.print.elements.IMapFrameOnScreen#getEscala()
		 */
		@Override
		public double getEscala ()
		{
			return escala;
		}

		/* (non-Javadoc)
		 * @see com.geopista.ui.plugin.print.elements.IMapFrameOnScreen#paint(java.awt.Graphics)
		 */
		@Override
		public void paint (Graphics g)
		{
			double scale = super.getViewport().getScale();
			super.paint(g);
			super.getViewport().setScale(scale);

			int number = getMapNumber();
			String label="Map:"+number;
			LineMetrics lm=g.getFontMetrics().getLineMetrics(label,g);
			Rectangle2D rect=g.getFontMetrics().getStringBounds(label,g);
			g.setColor(Color.GRAY);
			g.fillRoundRect(10-3+2,(int)(13+3+4-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);
			g.setColor(Color.WHITE);
			g.fillRoundRect(10-3,(int)(13+3-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);
			g.setColor(Color.gray);
			g.drawRoundRect(10-3,(int)(13+3-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);

			g.drawString(label,10,13);

			// Draw handlers for resizing
			if (mapFrame!=null && mapFrame.isSelected())
			{
				g.setColor(Color.BLACK);

				g.drawRect(0, 0, GraphicElements._RESIZE_HANDLER_SIZE, GraphicElements._RESIZE_HANDLER_SIZE);
				g.drawRect(this.getWidth()-GraphicElements._RESIZE_HANDLER_SIZE-1, 0, GraphicElements._RESIZE_HANDLER_SIZE, GraphicElements._RESIZE_HANDLER_SIZE);
				g.drawRect(0, this.getHeight()-GraphicElements._RESIZE_HANDLER_SIZE-1, GraphicElements._RESIZE_HANDLER_SIZE, GraphicElements._RESIZE_HANDLER_SIZE);
				g.drawRect(this.getWidth()-GraphicElements._RESIZE_HANDLER_SIZE-1, this.getHeight()-GraphicElements._RESIZE_HANDLER_SIZE-1, GraphicElements._RESIZE_HANDLER_SIZE, GraphicElements._RESIZE_HANDLER_SIZE);
			}
		}

		/* (non-Javadoc)
		 * @see com.geopista.ui.plugin.print.elements.IMapFrameOnScreen#setResizedOnView(int)
		 */
		@Override
		public void setResizedOnView(int zoomActif) {
			resizedOnView = zoomActif;
			
		}
		/* (non-Javadoc)
		 * @see com.geopista.ui.plugin.print.elements.IMapFrameOnScreen#getResizedOnView()
		 */
		@Override
		public int getResizedOnView ()
		{
			return resizedOnView;
		}
	}

	private class MapFrameForPrint extends LayerViewPanel{
		public MapFrameForPrint(LayerManager layerManager, LayerViewPanelContext context) {
			super(layerManager, context);
			superRepaint();
		}

		int printcalls=0;
		private int	lastWidth;

		public void printComponent(Graphics g)
		{
			if (false)
			{
				super.printComponent(g);
				return;
			}
//			System.out.println("Map:"+getMapNumber()+" Printing Band #"+printcalls++ +":"+g.getClipBounds());
//			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//			RenderingHints.VALUE_ANTIALIAS_ON);
			//super.paintComponent(g);
//			erase((Graphics2D) g);

			// this.erase((Graphics2D) g);

			//List layersToPrint = this.getLayerManager().getVisibleLayers(false);
			List layersToPrint = this.getLayerManager().getOrderLayers();

			ArrayList layersReversed = new ArrayList(layersToPrint);
			Collections.reverse(layersReversed);
			for (Iterator iter = layersReversed.iterator(); iter.hasNext();)
			{
				Object obj =  iter.next();

				if (obj instanceof GeopistaLayer)
				{
					Renderer uncached=new UncachedLayerRenderer((GeopistaLayer)obj, this);
					uncached.copyTo((Graphics2D) g);
				}
				else
				{
					if (obj != null){
						Renderer renderer=getRenderingManager().getRenderer(obj);
					//renderer.clearImageCache();
						renderer.copyTo((Graphics2D) g);
					}
				}
			}
			((Graphics2D)g).setStroke(new BasicStroke(1));
			g.drawLine(g.getClipBounds().x,g.getClipBounds().y-1,g.getClipBounds().width+g.getClipBounds().x,g.getClipBounds().y-1);
		}

		public MapFrameForPrint(){

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
			case PrintLayoutFrame.LARGEUR_PAGE :
				super
				.setBounds(
						(int) onScreen.getBounds().getX(),
						(int) onScreen.getBounds().getY(),
						(int) onScreen.getBounds().getWidth(),
						(int) onScreen.getBounds().getHeight());
				break;
			}
			super.setLocation((int) super.getBounds().getX(), (int) super
					.getBounds().getY());
			repaint();


			//  initZoom();

			super.repaint();
		}
		private void setExtent()
		{	
			if (parent== null)return;
			ExtentManager extentMgr = parent.getExtentManager();

			Envelope envelope = extentMgr.getEnvelope(extentType, MapFrame.this);
			try
			{
				Envelope env=getViewport().getEnvelopeInModelCoordinates();
				if (!env.equals(envelope))
					getViewport().zoom(envelope);
			}
			catch (NoninvertibleTransformException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class MapFrameListener extends GraphicElementsListener {
		public MapFrameListener(GraphicElements ge, PrintLayoutFrame plf) {
			super(ge, plf);
		}
		public void mouseReleased(MouseEvent e) {
			// initZoom();

		}
		public void mouseClicked(MouseEvent e)
		{
			if (SwingUtilities.isLeftMouseButton(e))
			{
				if (e.getClickCount() == 2)
				{
					MapFrameConfigDialog dlg = new MapFrameConfigDialog(getParent());
					dlg.setExtentType(extentType);
					dlg.setPanelBorder(border);
					dlg.setVisible(true);

					if (dlg.isOkpressed())
					{
						setBorder(dlg.getBorder());
						extentType=dlg.getExtentType();
						
						onScreen.setReescalado(true);
						int view = MapFrame.this.getParent().getZoomActif();
						onScreen.setResizedOnView (view);
						
						((PrintableZone)onScreen.getParent()).setScaleFactor(0);
						//MapFrame.this.getParent().getPage().set;
						//((PrintableZone)MapFrame.this.getParent().getPlugInContext().getLayerViewPanel()).setScaleFactor(0);
						
						//Actualizar escalas segun cambios realizados en el mapa
						List elements = MapFrame.this.getParent().getPage().getGraphicElement();
						for (Iterator iter = elements.iterator(); iter.hasNext();) {
							GraphicElements element = (GraphicElements) iter.next();
							if (element instanceof ScaleBarFrame) 
								((ScaleBarFrame) element).repaint();
						}
						
						
						//getPageStyle()==PrintLayoutFrame.PAGE_ENTIERE
						System.out.println("reescalado true");
					}
					onScreen.invalidate();
					initZoom();
					repaint();
				}
				else {
					if (SwingUtilities.isLeftMouseButton(e)) {
						if (parent.getSelectedComponent() != null) {
							parent.getSelectedComponent().setSelected(false);
						}
						ge.setSelected(true);
						parent.setSelectedComponent(ge);
					}
				}
			}
		}
	}
	
	public void initValeursSpec(PrintLayoutFrame plf,Point[] points) {
//		this.setFrame(plf);
//		setCorner(points);
	}

	public int getMapNumber()
	{
		List elemd= getParent().getPage().getGraphicElement();
		int cont=1;
		for (Iterator iter = elemd.iterator(); iter.hasNext();)
		{
			GraphicElements element = (GraphicElements) iter.next();
			if (element==this) return cont;
			else
				if (element instanceof MapFrame)
					cont++;
		}
		return 0;
	}

	public void setValeursSpec() {
		onScreen.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		onScreen.validate();
		setBorder(border);
	}

	public void initValeursSpec(PrintLayoutFrame plf) {
		parent=plf;
		onScreen.init(plf.getPlugInContext().getLayerManager(), plf.getLayerViewPanelContext());
		onScreen.initValeursSpec2(plf,this);
		forPrint.init(plf.getPlugInContext().getLayerManager(), plf.getLayerViewPanelContext());

		onScreen.setBorder(this.border);

		forPrint.setBorder(this.border);
		
		//Establecer extentType segun escala seleccionada
		String scale = (String)parent.getExtentManager().getParameters().get(UtilsPrintPlugin.PARAM_CONFIG_ID_ESCALA);
		int scaleSelect = (scale != null)? Integer.parseInt(scale) : -1;
		if (scaleSelect == -1)
			extentType = MapFrame.FULL_EXTENT;
		else if (scaleSelect == -2)
			extentType = MapFrame.CURRENT_EXTENT;
		else 
			extentType = scaleSelect;
		
		initZoom();
		
		// listener.initValeursSpec(plf,this.getCornerPoint());
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

	public int getCurrentExtent()
	{
		return extentType;
	}	
	
	public double getExtent()
	{
		double extent = 0;
		if (getCurrentExtent() == MapFrame.FULL_EXTENT || getCurrentExtent() == MapFrame.CURRENT_EXTENT) {			
			extent = onScreen.getNormalizedScale(onScreen.getViewport().getScale());
		}
		else {
			extent = getCurrentExtent();
		}
		return extent;			
	}
	
	
	public boolean isFullExtent()
	{
		return extentType==MapFrame.FULL_EXTENT;
	}
	
	public void setFullExtent(boolean fullExtent)
	{
		if (fullExtent)
			this.extentType = MapFrame.FULL_EXTENT;
		initZoom();
	}
	
	public double getScaleForPrint()
	{
		return forPrint.getViewport().getScale();
	}
	
	public double getScale()
	{
		return onScreen.getViewport().getScale();
	}
	
	public LayerViewPanel getLayerViewPanelForPrint()
	{
		return forPrint;
	}
	public LayerViewPanel getLayerViewPanelOnScreen()
	{
		return onScreen;
	}
	
	public double getEscala()
	{
		return ((MapFrameOnScreen)onScreen).getEscala();
	}
	public int getResizedOnView ()
	{
		return ((MapFrameOnScreen)onScreen).getResizedOnView();
	}
	
}