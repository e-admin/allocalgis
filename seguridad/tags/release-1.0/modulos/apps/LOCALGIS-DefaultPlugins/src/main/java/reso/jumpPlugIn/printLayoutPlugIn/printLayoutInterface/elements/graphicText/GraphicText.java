/*
 * Package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 22 oct. 2004
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

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.graphicText;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import reso.jumpPlugIn.printLayoutPlugIn.I18NPlug;
import reso.jumpPlugIn.printLayoutPlugIn.PrintLayoutPlugIn;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.Page;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElementsListener;

import com.geopista.app.AppContext;
import com.geopista.ui.components.MultiLineLabelUI;
import com.vividsolutions.jump.util.Blackboard;

/**
 * @author FOUREAU_C
 */

public class GraphicText extends GraphicElements {
  

    public Point topLeftCorner = new Point();

    public Point topRightCorner = new Point();

    public Point BottomLeftCorner = new Point();

    public Point BottomRightCorner = new Point();

    private String text = I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.YourText");

    private Font font = new Font("Arial", Font.PLAIN, 12);

    private Color fontColor = Color.BLACK;

    private Color borderColor = Color.GRAY;

    private int borderThickness = 1;

    private Border border = BorderFactory.createLineBorder(borderColor,
            borderThickness);

    private Color backgroundColor = null;

    private int verticalAlignment = JLabel.CENTER;

    private int horizontalAlignment = JLabel.CENTER;

    private boolean isOpaque = false;

    private boolean isUnderline = false;

    private boolean isUpperCase = false;

   

    private GraphicTextOnScreen onScreen;

    private GraphicTextForPrint forPrint;

    private GraphicTextListener listener;

    private Font font2;

    public GraphicText(PrintLayoutFrame plf) {
        parent = plf;
        listener = new GraphicTextListener(this, parent);
        onScreen = new GraphicTextOnScreen();
        forPrint = new GraphicTextForPrint();
    }
    public GraphicText() {
        listener = new GraphicTextListener(this, null);
        onScreen = new GraphicTextOnScreen();
        forPrint = new GraphicTextForPrint();
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
            onScreen.setFont(heightOnScreen, newForPrintHeight);
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
            onScreen.setFont(font);
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

    public void setFont(Font f) {
        font = f;
        onScreen.setFont(onScreen.getWidth(), forPrint.getWidth());
        forPrint.setFont(font);
        repaint();
    }

    public Font getFont() {
        return font;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        onScreen.setHorizontalAlignment(horizontalAlignment);
        forPrint.setHorizontalAlignment(horizontalAlignment);
        repaint();
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        onScreen.setVerticalAlignment(verticalAlignment);
        forPrint.setVerticalAlignment(verticalAlignment);
        repaint();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        onScreen.setBackground(backgroundColor);
        forPrint.setBackground(backgroundColor);
        repaint();
    }

    public boolean getOpaque() {
        return(isOpaque);
    }
    
    public void setOpaque(boolean backgroundIsOpaque) {
        this.isOpaque = backgroundIsOpaque;
        onScreen.setOpaque(backgroundIsOpaque);
        forPrint.setOpaque(backgroundIsOpaque);
        repaint();
    }

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
        if (border != null){
            borderThickness = ((LineBorder) this.getBorder()).getThickness();
            borderColor = ((LineBorder) this.getBorder()).getLineColor();
        }
        onScreen.setBorder(border);
        forPrint.setBorder(border);
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }
    

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        this.border = BorderFactory.createLineBorder(borderColor,borderThickness);
        onScreen.setBorder(this.border);
        forPrint.setBorder(this.border);
        repaint();
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
        this.border = BorderFactory.createLineBorder(borderColor,borderThickness);
        onScreen.setBorder(this.border);
        forPrint.setBorder(this.border);
        repaint();
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        onScreen.setForeground(fontColor);
        forPrint.setForeground(fontColor);
        repaint();
    }

    public boolean getUpperCase() {
        return isUpperCase;
    }
    
    public void setUpperCase(boolean isUpperCase) {
        this.isUpperCase = isUpperCase;
       
        setText(text);
        repaint();
    }

    public boolean getUnderline() {
        return isUnderline;
    }

    public void setUnderline(boolean isUnderline) {
        this.isUnderline = isUnderline;
        if (isUnderline) {
            text = "<html><u>" + text + "</u></html>";
        } else {
            text = text.replaceAll("<html><u>", "");
            text = text.replaceAll("</u></html>", "");
        }
        setText(text);
        repaint();
    }

    public String getText() {
        return text;
    }
    Map substitutions=null;
	private Map getSubsMap()
	{
	if (substitutions==null)
		{
		substitutions=new Hashtable();
		substitutions.put("%Mun%", com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_MUNICIPALITYNAME);
		substitutions.put("%Title%", com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_MAPTITLE);
		substitutions.put("%Description%", com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_MAPDESCRIPTION);
		substitutions.put("%Page%", com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_CURRENTPAGE);
		substitutions.put("%PageCount%", com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_PAGECOUNT);
		}
	return substitutions;
	}
	/**
	 * Sustituye las claves con los valores del Blackboard del sistema
	 * 
	 * @param value
	 * @param substitutionMap
	 * @return
	 */
	public String substitutions(String value, Map substitutionMap)
	{
	if (value==null) return null;
	Blackboard bb;
	PrintLayoutFrame parent=getParent();
	//if (parent==null)
		bb=AppContext.getApplicationContext().getBlackboard();
//		else
//		bb=parent.getPlugInContext().getWorkbenchContext().getBlackboard();
	Iterator subst=substitutionMap.entrySet().iterator();
	
	while (subst.hasNext()) // Make all substitutions
		{
		try
			{
			Map.Entry element = (Map.Entry) subst.next();
			Object obj = bb.get((String) element.getValue());
			String newValue;
			if (obj instanceof ArrayList) 
				newValue = (String) ((ArrayList) obj).get(0);
			else 
				newValue = (String) obj;
			if (newValue != null) value = value.replaceAll((String) element
					.getKey(), newValue);
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
		}
		
		return value;
	}
		
	/**
     * Make string substitutions of following keys from the AppContextBlackboard
     * <br>
     * <ul>
     * <li> "%Mun%",
     * com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_MUNICIPALITYNAME
     * </li>
     * <li> "%Title%",
     * com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_MAPTITLE
     * </li>
     * <li> "%Description%",
     * com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_MAPSUBTITLE
     * </li>
     * </ul>
     * <br>
     * @see GraphicText#getSubsMap()
     * @param text Text to set
     */
    public void setText(String text) {
        this.text = text;
        text=substitutions(text,getSubsMap());
        onScreen.setText(isUpperCase?text.toUpperCase():text);
        forPrint.setText(isUpperCase?text.toUpperCase():text);
        repaint();
    }

    public boolean isSelected() {
        return getSelect();
    }

    public void setSelected(boolean select) {
        setSelect(select);
        if (select) {
            onScreen.setBorder(selectedBorder);
        } else {
            setBorder(border);
        }
        repaint();
    }

    public void repaint() {
        onScreen.repaint();
        forPrint.repaint();
    }

    private class GraphicTextOnScreen extends JLabel {
        public GraphicTextOnScreen() {
            super(text);
           
            super.setFont(new Font(font.getName(), font.getStyle(), 8));
            super.setForeground(fontColor);
            super.setBorder(border);
            super.setOpaque(isOpaque);
            super.setHorizontalAlignment(horizontalAlignment);
            super.setVerticalAlignment(verticalAlignment);
            addMouseListener(listener);
            addMouseMotionListener(listener);
            setUI(new MultiLineLabelUI());
        }
        public void paint (Graphics g)
		{
		super.paint(g);
//		g.drawString("Pos:"+((JLayeredPane)getParent()).getLayer(this),10,10);
		
		}
        public void setFont(int facteur1, int facteur2) {
            if (((font.getSize() * facteur1) / facteur2) < 8) {
                super.setFont(new Font(font.getName(), font.getStyle(), 8));
            } else {
                super.setFont(new Font(font.getName(), font.getStyle(), ((font
                        .getSize() * facteur1) / facteur2)));
            }
        }
    }

    private class GraphicTextForPrint extends JLabel {
        public GraphicTextForPrint() {
            super(text);
            super.setFont(font);
            super.setForeground(fontColor);
            super.setBorder(border);
            super.setOpaque(isOpaque);
            super.setHorizontalAlignment(horizontalAlignment);
            super.setVerticalAlignment(verticalAlignment);
            setUI(new MultiLineLabelUI());
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

    private class GraphicTextListener extends GraphicElementsListener {
        public GraphicTextListener(GraphicElements ge, PrintLayoutFrame plf) {
            super(ge, plf);
        }

        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (e.getClickCount() == 2) {
                    new TextStyleChooserDialog((GraphicText) ge);
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
    }

    public void setParent(PrintLayoutFrame plf) {
        parent = plf;
        listener.setFrame(plf);
     }
     
     public void setValeurs(PrintLayoutFrame plf,Border border) {
         setParent(plf);
         setBorder(border);
         onScreen.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
         onScreen.validate();
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

     public String getFontName(){
         return(font.getName());
     }
     
     public void setFontName(String fn){
         int style = font.getStyle();
         int size = font.getSize();
         Font nf = new Font(fn, style, size);
		 font = nf;
         onScreen.setFont(onScreen.getWidth(), forPrint.getWidth());
         forPrint.setFont(font);
     }
     
     public int getFontStyle(){
         return(font.getStyle());
     }
     
     public void setFontStyle(int st){
         String name = font.getName();
         int size = font.getSize();
         Font nf = new Font(name,st,size);
         font = nf;
         onScreen.setFont(onScreen.getWidth(), forPrint.getWidth());
         forPrint.setFont(font);
     }
     
     public int getFontSize(){
         return(font.getSize());
     }

     public void setFontSize(int s){
         String name = font.getName();
         int style = font.getStyle();
         Font nf = new Font(name,style,s);
         font = nf;
         onScreen.setFont(onScreen.getWidth(), forPrint.getWidth());
         forPrint.setFont(font);
     }

     public void setValeursSpec(){
       setText(text);  
     }
     
     public void initValeursSpec(PrintLayoutFrame plf){
         
     }
}
