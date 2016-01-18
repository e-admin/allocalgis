/*
 * Package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 27 oct. 2004
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

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.legendeFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElementsListener;

import com.geopista.ui.legend.LegendPanel;
import com.geopista.ui.plugin.print.elements.ForPrintLegendPanel;
import com.geopista.ui.plugin.print.elements.LegendConfigDialog;
import com.geopista.ui.plugin.print.elements.MapFrame;
import com.geopista.ui.plugin.print.elements.OnScreenLegendPanel;

/**
 * @author FOUREAU_C
 */
public class LegendFrame extends GraphicElements {
	
	public int linkedToMapNumber=0; // 0 means not linked
	public int firstMapIndex = 0;
	
	public PrintLayoutFrame parent;

	public Point topLeftCorner = new Point();

	public Point topRightCorner = new Point();

	public Point BottomLeftCorner = new Point();

	public Point BottomRightCorner = new Point();

	private boolean select = false;

	private Police titleMapLegendFont = new Police(new Font("Arial", Font.BOLD,
			12), Color.BLACK, false, false);

	private Police labelMapLegendFont = new Police();

	private OnScreenLegendPanel onScreen;

	private OnScreenLegendPanel forPrint;

	private LegendFrameListener listener;

	private List layers;

	public LegendFrame(PrintLayoutFrame plf) {
		parent = plf;
		layers = plf.getPlugInContext().getLayerManager().getLayers();
		listener = new LegendFrameListener(this, parent);
//		onScreen = new LegendFrameOnScreen();
		onScreen = new OnScreenLegendPanel(this);
		onScreen.addMouseListener(listener);
		onScreen.addMouseMotionListener(listener);
		//   forPrint = new LegendFrameForPrint();
		forPrint=new ForPrintLegendPanel(this);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		
		MapFrame mf = getFirstMap();
		linkedToMapNumber = firstMapIndex;
		
		setValeursSpec();
	}

	public LegendFrame(){
		listener = new LegendFrameListener(this,null);
		onScreen = new OnScreenLegendPanel(this);
		forPrint = new ForPrintLegendPanel(this);
		onScreen.addMouseListener(listener);
		onScreen.addMouseMotionListener(listener);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}

	public JComponent getGraphicElementsOnScreen() {
		return onScreen;
	}

	public JComponent getGraphicElementsForPrint() {
		return forPrint;
	}

	public void fixerDimensions(int x, int y, int w, int h, int facteur1,
			int facteur2) {
		onScreen.setBounds(x, y, w, h);
		onScreen.setLocation(x, y);
		forPrint.fixerDimensions();
		initCornerPoint();
		repaint();
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
			onScreen.setLabelMapLegendFont(heightOnScreen, newForPrintHeight);
			onScreen.setTitleMapLegendFont(heightOnScreen, newForPrintHeight);
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
			onScreen.setLabelMapLegendFont();
			onScreen.setTitleMapLegendFont();
			onScreen.setBounds((int) forPrint.getBounds().getX(),
					(int) forPrint.getBounds().getY(), (int) forPrint
					.getBounds().getWidth(), (int) forPrint.getBounds()
					.getHeight());
			onScreen.setLocation((int) forPrint.getBounds().getX(),
					(int) forPrint.getBounds().getY());
			break;
		}
		initCornerPoint();
		repaint();
	}

	public boolean isSelected() {
		return select;
	}

	public void setSelected(boolean select) {
		this.select = select;
		if (select) {
			onScreen.setBorder(selectedBorder);
		} else {
			setBorder(null);
		}
		repaint();
	}

	public void setBorder(Border border) {
		onScreen.setBorder(border);
	}

	public void repaint() {
		onScreen.repaint();
		forPrint.repaint();
	}
	public void paint(Graphics g)
	{
		String label="->Map:"+linkedToMapNumber;
		LineMetrics lm=g.getFontMetrics().getLineMetrics(label,g);
		Rectangle2D rect=g.getFontMetrics().getStringBounds(label,g);
		int x=(int) ((getScreenWidth()-rect.getWidth())/2);
		int y=(int) ((getScreenHeight()-rect.getHeight())/2);
		
		g.setColor(Color.GRAY);
		g.fillRoundRect(x-3+2,(int)(y+6-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);
		g.setColor(Color.WHITE);
		g.fillRoundRect(x-3,(int)(y+3-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);
		g.setColor(Color.gray);
		g.drawRoundRect(x-3,(int)(y+3-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);

		g.drawString(label,x,y);
	}

	public Police getLabelMapLegendFont() {
		return labelMapLegendFont;
	}

	public void setLabelMapLegendFont(Police labelMapLegendFont) {
		this.labelMapLegendFont = labelMapLegendFont;
		onScreen
		.setLabelMapLegendFont(onScreen.getHeight(), forPrint.getHeight());
		forPrint.setLabelMapLegendFont();
		repaint();
	}

	public Police getTitleMapLegendFont() {
		return titleMapLegendFont;
	}

	public void setTitleMapLegendFont(Police titleMapLegendFont) {
		this.titleMapLegendFont = titleMapLegendFont;
		onScreen
		.setTitleMapLegendFont(onScreen.getWidth(), forPrint.getWidth());
		forPrint.setTitleMapLegendFont();
		repaint();
	}

//	private class LegendFrameOnScreen extends JPanel {
//	public LegendFrameOnScreen() {
//	int pos = 1;
//setBackground(Color.WHITE);
//setLayout(new GridBagLayout());
//if (layers != null){
//	for (int i = 0; i < layers.size(); i++) {
//	if (((Layer) layers.get(i)).isVisible()) {
//	add(new MapLegend((Layer) layers.get(i)),
//		new GridBagConstraints(1, pos, 1, 1, 0.0, 0.0,
//		GridBagConstraints.WEST,
//GridBagConstraints.REMAINDER, new Insets(1,
//		0, 1, 0), 0, 0));
//pos++;
//}
//}
//}
//this.setLabelMapLegendFont(1, 2);
//this.setTitleMapLegendFont(1, 2);
//this.addMouseListener(listener);
//this.addMouseMotionListener(listener);
//this.repaint();
//}

//public void setValeursSpec(){
//	int pos = 1;
//if (layers != null){
//	for (int i = 0; i < layers.size(); i++) {
//	if (((Layer) layers.get(i)).isVisible()) {
//	add(new MapLegend((Layer) layers.get(i)),
//	new GridBagConstraints(1, pos, 1, 1, 0.0, 0.0,
//	GridBagConstraints.WEST,
//	GridBagConstraints.REMAINDER, new Insets(1,
//	0, 1, 0), 0, 0));
//	pos++;
//	}
//	}
//	}
//	}
//	public void setLabelMapLegendFont(int facteur1, int facteur2) {
//	if (((labelMapLegendFont.getFont().getSize() * facteur1) / facteur2) < 8) {
//	for (int i = 0; i < getComponents().length; i++) {
//	Component[] components = ((MapLegend) getComponent(i))
//	.getMapLegendComponents();
//	for (int j = 0; j < components.length; j++) {
//	if (components[j] instanceof LabelMapLegend) {
//	((LabelMapLegend) components[j]).getLibelle()
//	.setPolice(
//	new Police(new Font(
//	labelMapLegendFont
//	.getFont()
//	.getName(),
//	labelMapLegendFont
//	.getFont()
//	.getStyle(), 8),
//	labelMapLegendFont
//	.getColor(),
//	labelMapLegendFont
//	.getUnderline(),
//	labelMapLegendFont
//	.getUpperCase()));
//	}
//	}
//	}
//	} else {
//	for (int i = 0; i < getComponents().length; i++) {
//	Component[] components = ((MapLegend) getComponent(i))
//	.getMapLegendComponents();
//	for (int j = 0; j < components.length; j++) {
//	if (components[j] instanceof LabelMapLegend) {
//	((LabelMapLegend) components[j])
//	.getLibelle()
//	.setPolice(
//	new Police(
//	new Font(
//	labelMapLegendFont
//	.getFont()
//	.getName(),
//	labelMapLegendFont
//	.getFont()
//	.getStyle(),
//	((labelMapLegendFont
//	.getFont()
//	.getSize() * facteur1) / facteur2)),
//	labelMapLegendFont
//	.getColor(),
//	labelMapLegendFont
//	.getUnderline(),
//	labelMapLegendFont
//	.getUpperCase()));
//	}
//	}
//	}
//	}
//	}

//	public void setTitleMapLegendFont(int facteur1, int facteur2) {
//	if (((titleMapLegendFont.getFont().getSize() * facteur1) / facteur2) < 8) {
//	for (int i = 0; i < getComponents().length; i++) {
//	Component[] components = ((MapLegend) getComponent(i))
//	.getMapLegendComponents();
//	for (int j = 0; j < components.length; j++) {
//	if (components[j] instanceof TitleMapLegend) {
//	((TitleMapLegend) components[j])
//	.setPolice(new Police(new Font(
//	titleMapLegendFont.getFont()
//	.getName(),
//	titleMapLegendFont.getFont()
//	.getStyle(), 8),
//	titleMapLegendFont.getColor(),
//	titleMapLegendFont.getUnderline(),
//	titleMapLegendFont.getUpperCase()));
//	}
//	}
//	}
//	} else {
//	for (int i = 0; i < getComponents().length; i++) {
//	Component[] components = ((MapLegend) getComponent(i))
//	.getMapLegendComponents();
//	for (int j = 0; j < components.length; j++) {
//	if (components[j] instanceof TitleMapLegend) {
//	((TitleMapLegend) components[j])
//	.setPolice(new Police(
//	new Font(
//	titleMapLegendFont
//	.getFont()
//	.getName(),
//	titleMapLegendFont
//	.getFont()
//	.getStyle(),
//	((titleMapLegendFont
//	.getFont()
//	.getSize() * facteur1) / facteur2)),
//	titleMapLegendFont.getColor(),
//	titleMapLegendFont.getUnderline(),
//	titleMapLegendFont.getUpperCase()));
//	}
//	}
//	}
//	}
//	}

//	public void setLabelMapLegendFont() {
//	for (int i = 0; i < getComponents().length; i++) {
//	Component[] components = ((MapLegend) getComponent(i))
//	.getMapLegendComponents();
//	for (int j = 0; j < components.length; j++) {
//	if (components[j] instanceof LabelMapLegend) {
//	((LabelMapLegend) components[j]).getLibelle()
//	.setPolice(labelMapLegendFont);
//	}
//	}
//	}
//	}

//	public void setTitleMapLegendFont() {
//	for (int i = 0; i < getComponents().length; i++) {
//	Component[] components = ((MapLegend) getComponent(i))
//	.getMapLegendComponents();
//	for (int j = 0; j < components.length; j++) {
//	if (components[j] instanceof TitleMapLegend) {
//	((TitleMapLegend) components[j])
//	.setPolice(titleMapLegendFont);
//	}
//	}
//	}
//	}
//	}

//	private class LegendFrameForPrint extends JPanel {
//	public LegendFrameForPrint() {
//	setBackground(Color.WHITE);
//	setLayout(new GridBagLayout());
//	if (layers != null){
//	for (int i = 0; i < layers.size(); i++) {
//	if (((Layer) layers.get(i)).isVisible()) {
//	add(new MapLegend((Layer) layers.get(i)),
//	new GridBagConstraints(1, i, 1, 1, 0.0, 0.0,
//	GridBagConstraints.WEST,
//	GridBagConstraints.NONE, new Insets(0, 0,
//	0, 0), 0, 0));
//	}
//	}
//	}
//	setLabelMapLegendFont();
//	setTitleMapLegendFont();
//	}

//	void setValeursSpec(){
//	if (layers != null){
//	for (int i = 0; i < layers.size(); i++) {
//	if (((Layer) layers.get(i)).isVisible()) {
//	add(new MapLegend((Layer) layers.get(i)),
//	new GridBagConstraints(1, i, 1, 1, 0.0, 0.0,
//	GridBagConstraints.WEST,
//	GridBagConstraints.NONE, new Insets(0, 0,
//	0, 0), 0, 0));
//	}
//	}
//	}
//	}

//	public void setLabelMapLegendFont() {
//	for (int i = 0; i < getComponents().length; i++) {
//	Component[] components = ((MapLegend) getComponent(i))
//	.getMapLegendComponents();
//	for (int j = 0; j < components.length; j++) {
//	if (components[j] instanceof LabelMapLegend) {
//	((LabelMapLegend) components[j]).getLibelle()
//	.setPolice(labelMapLegendFont);
//	}
//	}
//	}
//	}

//	public void setTitleMapLegendFont() {
//	for (int i = 0; i < getComponents().length; i++) {
//	Component[] components = ((MapLegend) getComponent(i))
//	.getMapLegendComponents();
//	for (int j = 0; j < components.length; j++) {
//	if (components[j] instanceof TitleMapLegend) {
//	((TitleMapLegend) components[j])
//	.setPolice(titleMapLegendFont);
//	}
//	}
//	}
//	}

//	public void fixerDimensions() {
//	switch (parent.getZoomActif()) {
//	case PrintLayoutFrame.PAGE_ENTIERE:
//	super
//	.setBounds(
//	(int) ((onScreen.getBounds().getX() * parent
//	.getPrintLayoutPreviewPanel()
//	.getPreviewPanel().getPage()
//	.getPageForPrint().getWidth()) / ((Page.PageDrawOnScreen) parent
//	.getPrintLayoutPreviewPanel()
//	.getPreviewPanel().getPage()
//	.getPageDrawOnScreen()).getCenter()
//	.getWidth()),
//	(int) ((onScreen.getBounds().getY() * parent
//	.getPrintLayoutPreviewPanel()
//	.getPreviewPanel().getPage()
//	.getPageForPrint().getHeight()) / ((Page.PageDrawOnScreen) parent
//	.getPrintLayoutPreviewPanel()
//	.getPreviewPanel().getPage()
//	.getPageDrawOnScreen()).getCenter()
//	.getHeight()),
//	(int) ((onScreen.getBounds().getWidth() * parent
//	.getPrintLayoutPreviewPanel()
//	.getPreviewPanel().getPage()
//	.getPageForPrint().getWidth()) / ((Page.PageDrawOnScreen) parent
//	.getPrintLayoutPreviewPanel()
//	.getPreviewPanel().getPage()
//	.getPageDrawOnScreen()).getCenter()
//	.getWidth()),
//	(int) ((onScreen.getBounds().getHeight() * parent
//	.getPrintLayoutPreviewPanel()
//	.getPreviewPanel().getPage()
//	.getPageForPrint().getHeight()) / ((Page.PageDrawOnScreen) parent
//	.getPrintLayoutPreviewPanel()
//	.getPreviewPanel().getPage()
//	.getPageDrawOnScreen()).getCenter()
//	.getHeight()));
//	break;
//	case PrintLayoutFrame.LARGEUR_PAGE:
//	super.setBounds((int) onScreen.getBounds().getX(),
//	(int) onScreen.getBounds().getY(), (int) onScreen
//	.getBounds().getWidth(), (int) onScreen
//	.getBounds().getHeight());
//	break;
//	}
//	super.setLocation((int) super.getBounds().getX(), (int) super
//	.getBounds().getY());
//	super.repaint();
//	}
//	}

	private class LegendFrameListener extends GraphicElementsListener {
		public LegendFrameListener(GraphicElements ge, PrintLayoutFrame plf) {
			super(ge, plf);
		}

		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (e.getClickCount() == 2) {
					LegendPanel pan=((OnScreenLegendPanel)ge.getGraphicElementsOnScreen()).borrowLegendPanel();
					LegendConfigDialog dlg=new LegendConfigDialog(pan, LegendFrame.this);
					((OnScreenLegendPanel)ge.getGraphicElementsOnScreen()).restoreLegendPanel();
					OnScreenLegendPanel lp=((OnScreenLegendPanel)ge.getGraphicElementsOnScreen());
					int symh=dlg.getSymbolHeight();// tamaño aparente en pantalla
					linkedToMapNumber = dlg.getLinkedMap();
					
					MapFrame mf = getMap(linkedToMapNumber);
					
					lp.cleanLegendPanel();
					lp.getLegendPanel().initializeFromLayers(mf.getLayerViewPanelOnScreen().getLayerManager().getLayers(), 
							mf.getExtent());
					ForPrintLegendPanel plp=((ForPrintLegendPanel)ge.getGraphicElementsForPrint());
					plp.cleanLegendPanel();
					plp.getLegendPanel().initializeFromLayers(mf.getLayerViewPanelOnScreen().getLayerManager().getLayers(), 
							mf.getExtent());
					plp.getLegendPanel().setAutoLayout(lp.getLegendPanel().isAutoLayout());

					plp.fixerDimensions();

				} else {
					if (SwingUtilities.isLeftMouseButton(e)) {
						if (parent.getSelectedComponent() != null) {
							parent.getSelectedComponent().setSelected(false);
						}
						ge.setSelected(true);
						parent.setSelectedComponent(ge);
					}
				}
				repaint();
			}
		}
		public void mouseDragged(MouseEvent e)
		{
			// TODO Auto-generated method stub
			super.mouseDragged(e);

		}
		public void mouseReleased(MouseEvent e)
		{
			// TODO Auto-generated method stub
			super.mouseReleased(e);
			ge.repaint();
		}
		public void mouseMoved(MouseEvent e)
		{

			super.mouseMoved(e);

		}
		
		public void paint (Graphics g)
		{
			String label="->Map:"+linkedToMapNumber;
			LineMetrics lm=g.getFontMetrics().getLineMetrics(label,g);
			Rectangle2D rect=g.getFontMetrics().getStringBounds(label,g);
			int x=(int) ((getScreenWidth()-rect.getWidth())/2);
			int y=(int) ((getScreenHeight()-rect.getHeight())/2);
			
			g.setColor(Color.GRAY);
			g.fillRoundRect(x-3+2,(int)(y+6-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);
			g.setColor(Color.WHITE);
			g.fillRoundRect(x-3,(int)(y+3-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);
			g.setColor(Color.gray);
			g.drawRoundRect(x-3,(int)(y+3-rect.getHeight()),(int)rect.getWidth()+6,(int)rect.getHeight(),5,5);

			g.drawString(label,x,y);
			
		}
		
		
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

	public void setValeursSpec(){
		if (parent==null)return;
		
		layers = parent.getPlugInContext().getLayerManager().getLayers();
		if (linkedToMapNumber!=0 && getMap(linkedToMapNumber)!=null)
		{			
			double escala = getMap(linkedToMapNumber).getExtent();
			onScreen.setValeursSpec(escala);
			forPrint.setValeursSpec(escala);
		}	
		else
		{
			onScreen.setValeursSpec();
			forPrint.setValeursSpec();
		}
		
		repaint();		
	}

	public void initValeursSpec(PrintLayoutFrame plf){
		parent=plf;
		layers = plf.getPlugInContext().getLayerManager().getLayers();
//		onScreen.getLegendPanel().initializeFromLayers(getLayers());
	}
	
	public List getLayers()
	{
		return layers;
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
		//onScreen.setRenderer(new GeopistaLegendRenderer());
		//forPrint.setRenderer(new GeopistaLegendRenderer());
		
		this.linkedToMapNumber = linkedToMapNumber;
	}
	
	private MapFrame getFirstMap()
	{				
		List elem=parent.getPage().getGraphicElement();
		int cont=0;
		for (Iterator iter = elem.iterator(); iter.hasNext();)
		{
			cont++;
			GraphicElements element = (GraphicElements) iter.next();
			if (element instanceof MapFrame)
			{
				firstMapIndex = cont;
				return (MapFrame)element;				
			}	
		}
		return null;
	}	
	
}