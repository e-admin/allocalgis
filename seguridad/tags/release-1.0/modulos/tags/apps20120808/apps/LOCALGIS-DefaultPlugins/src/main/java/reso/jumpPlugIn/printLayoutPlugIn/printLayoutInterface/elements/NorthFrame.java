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

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.geopista.ui.plugin.print.elements.NorthIconChooserDialog;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.Page;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;

/**
 * @author FOUREAU_C
 */
public class NorthFrame extends GraphicElements {
   

    private Point topLeftCorner = new Point();

    private Point topRightCorner = new Point();

    private Point BottomLeftCorner = new Point();

    private Point BottomRightCorner = new Point();

    protected ImageIcon northSymbol;
protected int northIconNumber=0;

    protected NorthFrameOnScreen onScreen;

    protected NorthFrameForPrint forPrint;

    protected GraphicElementsListener listener;

    public NorthFrame(){
        northSymbol = (ImageIcon) NorthIconChooserDialog.northSymbols[northIconNumber];
       
        listener = new NorthFrameListener(this, parent);
        onScreen = new NorthFrameOnScreen();
        forPrint = new NorthFrameForPrint();
   }
    
    public NorthFrame(PrintLayoutFrame plf) {
        setParent(plf);
        northSymbol = createImageIcon(
                "/reso/jumpPlugIn/printLayoutPlugIn/images/North.gif",
                "NorthSymbol");
        listener = new NorthFrameListener(this, parent);
        onScreen = new NorthFrameOnScreen();
        forPrint = new NorthFrameForPrint();
    }

    public boolean isSelected() {
        return getSelect();
    }

    public void setSelected(boolean select) {
        setSelect(select);
        if (isSelected()) {
            onScreen.setBorder(selectedBorder);
        } else {
            onScreen.setBorder(null);
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
        resizeIcon();
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
        resizeIcon();
        initCornerPoint();
        repaint();
    }

    private static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = NorthFrame.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void resizeIcon() {
        Image icon;
        if (onScreen.getHeight() != 0 && onScreen.getWidth() != 0) {
            if (onScreen.getHeight() > onScreen.getWidth()) {
                int sc = (int) ((onScreen.getHeight() * northSymbol
                        .getIconWidth()) / northSymbol
                        .getIconHeight());
                icon = northSymbol.getImage().getScaledInstance(
                                (int) ((onScreen.getHeight() * northSymbol
                                        .getIconWidth()) / northSymbol
                                        .getIconHeight()),
                                onScreen.getHeight(), Image.SCALE_SMOOTH);
            } else {
                icon = northSymbol.getImage().getScaledInstance(
                                onScreen.getWidth(),
                                (int) ((onScreen.getWidth() * northSymbol
                                        .getIconHeight()) / northSymbol
                                        .getIconWidth()), Image.SCALE_SMOOTH);
            }
            //onScreen.setIcon(new ImageIcon(icon));
        }
        if (forPrint.getHeight() != 0 && forPrint.getWidth() != 0) {
            if (forPrint.getHeight() > forPrint.getWidth()) {
                icon = northSymbol.getImage().getScaledInstance(
                                (int) ((forPrint.getHeight() * northSymbol
                                        .getIconWidth()) / northSymbol
                                        .getIconHeight()),
                                forPrint.getHeight(), Image.SCALE_SMOOTH);
            } else {
                icon = northSymbol.getImage().getScaledInstance(
                                forPrint.getWidth(),
                                (int) ((forPrint.getWidth() * northSymbol
                                        .getIconHeight()) / northSymbol
                                        .getIconWidth()), Image.SCALE_SMOOTH);
            }
           // forPrint.setIcon(new ImageIcon(icon));
        }
    }

    public void repaint() {
        resizeIcon();
        onScreen.repaint();
        forPrint.repaint();
    }

    
    public class NorthFrameOnScreen extends JComponent {
    ImageIcon icon=null;
        public NorthFrameOnScreen() {
            super();
            icon=northSymbol;
            setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
            addMouseListener(listener);
            addMouseMotionListener(listener);
        }
        public void setIcon(ImageIcon icon)
        {
        this.icon=icon;
        }
        public void paint (Graphics g)
		{
		super.paint(g);
		int cw=this.getWidth();
		int ch=this.getHeight();
		int iw=icon.getIconWidth();
		int ih=icon.getIconHeight();
		// Calcula tamaño en funcion del ratio
		double ratioW=((double)cw)/iw;
		double ratioH=((double)ch)/ih;
		double ratio=Math.min(ratioW,ratioH);
		int h=(int) (ih*ratio);
		int w=(int) (iw*ratio);
		g.drawImage(icon.getImage(),cw/2-w/2,ch/2-h/2,w,h,this);
		//g.drawString("Pos:"+((JLayeredPane)getParent()).getLayer(this),10,10);
		
		}
   }

    public class NorthFrameForPrint extends JComponent {
    ImageIcon icon=null;
        public NorthFrameForPrint() {
            super();
            setIcon(northSymbol);
        }
        public void setIcon(ImageIcon icon)
        {
        this.icon=icon;
        }
        public void paint (Graphics g)
		{
		// Calcula tamaño en funcion del ratio
		int cw=this.getWidth();
		int ch=this.getHeight();
		int iw=icon.getIconWidth();
		int ih=icon.getIconHeight();
		// Calcula tamaño en funcion del ratio
		double ratioW=((double)cw)/iw;
		double ratioH=((double)ch)/ih;
		double ratio=Math.min(ratioW,ratioH);
		int h=(int) (ih*ratio);
		int w=(int) (iw*ratio);
		g.drawImage(icon.getImage(),cw/2-w/2,ch/2-h/2,w,h,this);
		//g.draw3DRect(cw/2-w/2,ch/2-h/2,w,h,true);
		//g.drawString("Pos:"+((JLayeredPane)getParent()).getLayer(this),10,10);
		
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

    public class NorthFrameListener extends GraphicElementsListener {
        public NorthFrameListener(GraphicElements ge, PrintLayoutFrame plf) {
            super(ge, plf);
        }
        
        public void mouseClicked(MouseEvent e)
		{
		if (SwingUtilities.isLeftMouseButton(e))
			{
			if (e.getClickCount() == 2)
				{
				NorthIconChooserDialog dlg = new NorthIconChooserDialog(getParent());
				dlg.setNorthIcon(northIconNumber);
				dlg.setVisible(true);
				if (dlg.isOkpressed())
					{
					
						
						setNorthIconNumber(dlg.getNumberIconSelected());
					
					}
				repaint();
				}
			else
				{
				super.mouseClicked(e);
				}
			}

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

    public void setParent(PrintLayoutFrame plf) {
       parent = plf;
      if (listener!=null)
      	listener.setFrame(plf);
     }
    
    public void setBorder(Border border){
        onScreen.setBorder(border);
    }

    public void setValeursSpec() {
        setBorder(null);
        onScreen.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        onScreen.validate();
    }

    public void initValeursSpec(PrintLayoutFrame plf){
        
    }
    
public int getNorthIconNumber()
{
return northIconNumber;
}
public void setNorthIconNumber(int northIconNumber)
{
this.northIconNumber = northIconNumber;
northSymbol=(ImageIcon) NorthIconChooserDialog.northSymbols[northIconNumber];
onScreen.setIcon(northSymbol);
forPrint.setIcon(northSymbol);
}
}
