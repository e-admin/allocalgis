/*
 * Package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 21 oct. 2004
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

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;

/**
 * @author FOUREAU_C
 */

public class Page {
	/**
	 * 
	 */
	private static final int	PRINTER_DPI	= 72;
	private static final int	PIXEL_PER_PICA	= 72;
	private PrintLayoutFrame parent;

	private int largeur;

	private int hauteur;

	private int largeurSansMarge;

	private int hauteurSansMarge;

	private int top;

	private int left;

	private PageForPrint print;

	private PageDrawOnScreen drawOnScreen;

	private List graphicElements = new ArrayList();

	private int pageStyle=0;

	public Page(){
	}

	public Page(PrintLayoutFrame plf) {
		parent = plf;
		setSize(plf.getPageFormat());
		print = new PageForPrint(plf);
		//		//test
		//JFrame fr=new JFrame("TEst print");
		//fr.getContentPane().add(print);
		//		fr.setSize(400,600);
		//		fr.show();
		drawOnScreen = new PageDrawOnScreen();
	}

	public void posGraphicElement(PrintLayoutFrame plf){
		parent = plf;
		resize(plf.getPageFormat());
		drawOnScreen.posGraphicElement(plf);
	}

	public JLayeredPane getPageForPrint() {
		return print;
	}

	public JPanel getPageDrawOnScreen() {
		return drawOnScreen;
	}

	public JLayeredPane getCenter(){
		return drawOnScreen.getCenter();
	}

	public void setSize(PageFormat pf) {
		/*
		 * largeur = Conversion.SoixanteDouxième_Inch_To_Pixel(pf.getWidth());
		 * hauteur = Conversion.SoixanteDouxième_Inch_To_Pixel(pf.getHeight());
		 * largeurSansMarge = Conversion.SoixanteDouxième_Inch_To_Pixel(pf
		 * .getImageableWidth()); hauteurSansMarge =
		 * Conversion.SoixanteDouxième_Inch_To_Pixel(pf .getImageableHeight());
		 * top = Conversion.SoixanteDouxième_Inch_To_Pixel(pf.getImageableY());
		 * left = Conversion.SoixanteDouxième_Inch_To_Pixel(pf.getImageableX());
		 */
		largeur = (int) pf.getWidth()*PRINTER_DPI/PIXEL_PER_PICA;
		hauteur = (int) pf.getHeight()*PRINTER_DPI/PIXEL_PER_PICA;
		largeurSansMarge = (int) pf.getImageableWidth()*PRINTER_DPI/PIXEL_PER_PICA;
		hauteurSansMarge = (int) pf.getImageableHeight()*PRINTER_DPI/PIXEL_PER_PICA;
		top = (int) pf.getImageableY()*PRINTER_DPI/PIXEL_PER_PICA;
		left = (int) pf.getImageableX()*PRINTER_DPI/PIXEL_PER_PICA;
	}

	public void resize(PageFormat pf) {
		setSize(pf);
		drawOnScreen.resize();
		for (int i = 0; i < graphicElements.size(); i++) {

			((GraphicElements) graphicElements.get(i)).resize(largeurSansMarge,
					print.getWidth(), hauteurSansMarge, print.getHeight(),
					drawOnScreen.getCenter().getWidth(), drawOnScreen
					.getCenter().getHeight());
		}
		print.resize();
	}

	public void remove(GraphicElements ge){
		graphicElements.remove(ge);
		drawOnScreen.remove(ge);
		print.remove(ge);
	}



	public class PageDrawOnScreen extends JPanel {
		private Marge topMargin = new Marge();

		private Marge leftMargin = new Marge();

		private Marge bottomMargin = new Marge();

		private Marge rightMargin = new Marge();

		private PrintableZone center = new PrintableZone();

		public PageDrawOnScreen() {
			setBackground(Color.WHITE);
			resize();
		}

		public void posGraphicElement(PrintLayoutFrame plf){
			center.PosGraphicElement(plf);
		}

		public void resize() {
			removeAll();


			switch (parent.getZoomActif()) {
			case PrintLayoutFrame.PAGE_ENTIERE:
				switch (parent.getPageFormat().getOrientation()) {
				case PageFormat.LANDSCAPE://width > height
					topMargin.setPreferredSize(new Dimension(800,
							(int) ((top * 800) / largeur)));
					leftMargin.setPreferredSize(new Dimension(
							(int) ((left * 800) / largeur),
							(int) ((hauteurSansMarge * 800) / largeur)));
					center.reSize(800, largeur);
					rightMargin
					.setPreferredSize(new Dimension(
							(int) (((largeur - (left + largeurSansMarge)) * 800) / largeur),
							(int) ((hauteurSansMarge * 800) / largeur)));
					bottomMargin
					.setPreferredSize(new Dimension(
							800,
							(int) ((((hauteur - (top + hauteurSansMarge)) * 800) / largeur))));
					super.setPreferredSize(new Dimension(800,
							(int) ((hauteur * 800) / largeur)));
					break;
				case PageFormat.PORTRAIT: //height > width
					topMargin.setPreferredSize(new Dimension(
							(int) ((largeur * 600) / hauteur),
							(int) ((top * 600) / hauteur)));
					leftMargin.setPreferredSize(new Dimension(
							(int) ((left * 600) / hauteur),
							(int) ((hauteurSansMarge * 600) / hauteur)));
					center.reSize(600, hauteur);
					rightMargin
					.setPreferredSize(new Dimension(
							(int) ((((largeur - (left + largeurSansMarge)) * 600) / hauteur)),
							(int) ((hauteurSansMarge * 600) / hauteur)));
					bottomMargin
					.setPreferredSize(new Dimension(
							(int) ((largeur * 600) / hauteur),
							(int) (((hauteur - (top + hauteurSansMarge)) * 600) / hauteur)));
					super.setPreferredSize(new Dimension(
							(int) ((largeur * 600) / hauteur), 600));
					break;
				}
				break;
			case PrintLayoutFrame.LARGEUR_PAGE:
				setPreferredSize(new Dimension(largeur, hauteur));
				topMargin.setPreferredSize(new Dimension(largeur, top));
				leftMargin.setPreferredSize(new Dimension(left,
						hauteurSansMarge));
				center.reSize(largeurSansMarge, hauteurSansMarge);
				rightMargin
				.setPreferredSize(new Dimension(
						(largeur - (left + largeurSansMarge)),
						hauteurSansMarge));
				bottomMargin.setPreferredSize(new Dimension(largeur,
						(hauteur - (top + hauteurSansMarge))));
				break;
			}


			setSize(this.getPreferredSize());
			topMargin.setSize(topMargin.getPreferredSize());
			leftMargin.setSize(leftMargin.getPreferredSize());
			center.setSize(center.getPreferredSize());
			rightMargin.setSize(rightMargin.getPreferredSize());
			bottomMargin.setSize(bottomMargin.getPreferredSize());

			setLayout(new BorderLayout());
			add(topMargin, BorderLayout.NORTH);
			add(leftMargin, BorderLayout.WEST);
			add(center, BorderLayout.CENTER);
			add(rightMargin, BorderLayout.EAST);
			add(bottomMargin, BorderLayout.SOUTH);
			repaint();
		}

		public JLayeredPane getCenter() {
			return center;
		}

		public void remove(GraphicElements ge){
			for(int i =0 ; i < getCenter().getComponents().length ; i++){
				if(getCenter().getComponent(i).equals(ge.getGraphicElementsOnScreen())){
					getCenter().remove(i); 
				}
			}
			repaint();
		}

		private class Marge extends JPanel {
			public Marge() {
				setBackground(Color.WHITE);
			}
		}
	}

	public class PageForPrint extends JLayeredPane implements Printable {
		private PrintLayoutFrame	parent;

		public PageForPrint(PrintLayoutFrame plf) {
			this.parent=plf;
			super.setBackground(Color.WHITE);
			super.setPreferredSize(new Dimension(largeurSansMarge, hauteurSansMarge));
			super.setSize(getPreferredSize());
		}

		public void resize() {
			super.resize(largeurSansMarge, hauteurSansMarge);
			repaint();
		}

		public void remove(GraphicElements ge){
			for(int i =0 ; i < getComponents().length ; i++){
				if(getComponent(i).equals(ge.getGraphicElementsForPrint())){
					remove(i); 
				}
			}
			repaint();
		}

		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
			//parent.getPrinterJob().setJobName("JAJAJA");
			//TODO:
			//setJobName("");
			
			/**
			 * Soporte para multipagina
			 */
			int pages = parent.getExtentManager().getExtentCount();
			if (pageIndex > pages-1) {
				return (NO_SUCH_PAGE);
			} else {
				int currentExtent=parent.getExtentManager().getCurrentExtent();
				if (currentExtent!=pageIndex)
				{
					parent.getExtentManager().setCurrentExtent(pageIndex);
					parent.setMapsExtents();
				}
				Graphics2D g2d = (Graphics2D) graphics;
				g2d.translate(pageFormat.getImageableX(), pageFormat
						.getImageableY());
				// Turn off double buffering
				RepaintManager currentManager = 
						RepaintManager.currentManager(this);
				currentManager.setDoubleBufferingEnabled(false);
				currentManager.markCompletelyDirty(this);
				printAll(g2d);
				// Turn double buffering back on
				currentManager.setDoubleBufferingEnabled(true);
				return (PAGE_EXISTS);
			}
		}
	}

	public class PrintableZone extends JLayeredPane implements MouseListener,
	MouseMotionListener, IPrintableZone {
		double scaleFactor=0;

		public PrintableZone() {
			setBackground(Color.WHITE);
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		private int largeurComposant, hauteurComposant;

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#getScaleFactor()
		 */
		@Override
		public double getScaleFactor ()
		{
			if (scaleFactor == 0)
			{
				double numerador = Double.valueOf(Page.this.drawOnScreen.getCenter().getWidth());
				double denominador = Double.valueOf(Page.this.print.getWidth());
				scaleFactor = numerador/denominador;
			}
			return scaleFactor;

		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#setScaleFactor(double)
		 */
		@Override
		public void setScaleFactor (double scaleFactor)
		{
			this.scaleFactor = scaleFactor;

		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#getPageStyle()
		 */
		@Override
		public int getPageStyle(){
			return pageStyle;
		}



		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			if (parent.getAjout() != null) {
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if (parent.getAjout() != null) {
				parent.getAjout().getGraphicElementsOnScreen().setBounds(
						new Rectangle(e.getPoint()));
				parent.getAjout().getGraphicElementsForPrint().setBounds(
						new Rectangle(e.getPoint()));
				add(parent.getAjout().getGraphicElementsOnScreen(), 0);
				print.add(parent.getAjout().getGraphicElementsForPrint(), 0);
				graphicElements.add(parent.getAjout());
			}
		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#PosGraphicElement(reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame)
		 */
		@Override
		public void PosGraphicElement(Object plObject){
			PrintLayoutFrame plf = (PrintLayoutFrame)plObject;
			if (parent.getAjout() != null){
				parent.getAjout().initValeursSpec(plf);
				add(parent.getAjout().getGraphicElementsOnScreen(),0);
				print.add(parent.getAjout().getGraphicElementsForPrint(),0);
				moveToFront(parent.getAjout().getGraphicElementsOnScreen());
				moveToFront(parent.getAjout().getGraphicElementsForPrint());

				parent.getAjout().setParent(plf);
				((GraphicElements) parent.getAjout()).fixerDimensions(
						(int) parent.getAjout().getGraphicElementsOnScreen()
						.getBounds().getX(), (int) parent.getAjout()
						.getGraphicElementsOnScreen().getBounds()
						.getY(), (int) parent.getAjout()
						.getGraphicElementsOnScreen().getBounds()
						.getWidth(), (int) parent.getAjout()
						.getGraphicElementsOnScreen().getBounds()
						.getHeight(), (int) largeurSansMarge,
						(int) getWidth());
				graphicElements.add(parent.getAjout());
				parent.getAjout().setValeursSpec();

				parent.getAjout().repaint();
				parent.initAjout();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				repaint();
			}
		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			if (parent.getAjout() != null) {
				parent.getAjout().setBorder(null);
				parent.initAjout();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#mouseDragged(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			if (parent.getAjout() != null) {
				if (e.getPoint().getX() > parent.getAjout()
						.getGraphicElementsOnScreen().getBounds().getX()) {
					largeurComposant = (int) (e.getPoint().getX() - parent
							.getAjout().getGraphicElementsOnScreen()
							.getBounds().getX());
					if (e.getPoint().getY() > parent.getAjout()
							.getGraphicElementsOnScreen().getBounds().getY()) {
						hauteurComposant = (int) (e.getPoint().getY() - parent
								.getAjout().getGraphicElementsOnScreen()
								.getBounds().getY());
					} else {
						hauteurComposant = (int) (parent.getAjout()
								.getGraphicElementsOnScreen().getBounds()
								.getY() - e.getPoint().getY());
					}
				} else {
					largeurComposant = (int) (parent.getAjout()
							.getGraphicElementsOnScreen().getBounds().getX() - e
							.getPoint().getX());
					if (e.getPoint().getY() < parent.getAjout()
							.getGraphicElementsOnScreen().getBounds().getY()) {
						hauteurComposant = (int) (parent.getAjout()
								.getGraphicElementsOnScreen().getBounds()
								.getY() - e.getPoint().getY());
					} else {
						hauteurComposant = (int) (e.getPoint().getY() - parent
								.getAjout().getGraphicElementsOnScreen()
								.getBounds().getY());
					}
				}
				parent.getAjout().getGraphicElementsOnScreen().setBounds(
						(int) parent.getAjout().getGraphicElementsOnScreen()
						.getBounds().getX(),
						(int) parent.getAjout().getGraphicElementsOnScreen()
						.getBounds().getY(), largeurComposant,
						hauteurComposant);

				((GraphicElements) parent.getAjout()).fixerDimensions(
						(int) parent.getAjout().getGraphicElementsOnScreen()
						.getBounds().getX(), (int) parent.getAjout()
						.getGraphicElementsOnScreen().getBounds()
						.getY(), (int) parent.getAjout()
						.getGraphicElementsOnScreen().getBounds()
						.getWidth(), (int) parent.getAjout()
						.getGraphicElementsOnScreen().getBounds()
						.getHeight(), (int) largeurSansMarge,
						(int) getWidth());
				repaint();
			}
		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#mouseMoved(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
		}

		/* (non-Javadoc)
		 * @see reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone#reSize(int, int)
		 */
		@Override
		public void reSize(int facteur1, int facteur2) {

			switch (parent.getZoomActif()) {
			case PrintLayoutFrame.PAGE_ENTIERE:

				pageStyle = PrintLayoutFrame.PAGE_ENTIERE;

				setPreferredSize(new Dimension(
						(int) ((largeurSansMarge * facteur1) / facteur2),
						(int) ((hauteurSansMarge * facteur1) / facteur2)));
				break;
			case PrintLayoutFrame.LARGEUR_PAGE:

				pageStyle = PrintLayoutFrame.LARGEUR_PAGE;				

				setPreferredSize(new Dimension(largeurSansMarge,
						hauteurSansMarge));
				break;
			}
			setSize(getPreferredSize());
			repaint();
		}
	}

	public int getLargeur(){
		return(largeur);
	}

	public void setLargeur(int i){
		largeur = i;
	}

	public int getHauteur(){
		return(hauteur);
	}

	public void setHauteur(int i){
		hauteur = i;
	}

	public int getLargeurSansMarge(){
		return(largeurSansMarge);
	}

	public void setLargeurSansMarge(int i){
		largeurSansMarge = i;
	}

	public int getHauteurSansMarge(){
		return(hauteurSansMarge);
	}

	public void setHauteurSansMarge(int i){
		hauteurSansMarge = i;
	}

	public int getTop(){
		return(top);
	}

	public void setTop(int i){
		top = i;
	}

	public int getLeft(){
		return(left);
	}

	public void setLeft(int i){
		left = i;
	}
	/**
	 * Return an ordered list of elements. Order is determined by position on JLayeredPane
	 * @return
	 */
	public List getGraphicElement(){
		if (drawOnScreen!=null )
		{
			TreeMap list= new TreeMap();
			for (Iterator iter = graphicElements.iterator(); iter.hasNext();)
			{
				GraphicElements element = (GraphicElements) iter.next();
				list.put(new Integer(-getCenter().getPosition(element.getGraphicElementsOnScreen())), element );
			}
			//			return(graphicElements);
			return new ArrayList(list.values());
		}
		else
			return graphicElements;
	}

	public void addGraphicElement(GraphicElements elem) {
		graphicElements.add(elem);
	}

	public void addGraphicElement(GraphicElements elem,PrintLayoutFrame plf) {
		graphicElements.add(elem);
	}

	/**
	 * @param gesel
	 */
	public void toFront(GraphicElements gesel)
	{
		// cambia los componentes
		int zorder= drawOnScreen.getCenter().getLayer(gesel.getGraphicElementsOnScreen());
		int pos= drawOnScreen.getCenter().getPosition(gesel.getGraphicElementsOnScreen());
		// cambia en la lista de componentes
		//drawOnScreen.getCenter().setLayer(gesel.getGraphicElementsOnScreen(),zorder+1,0);
		drawOnScreen.getCenter().moveToFront(gesel.getGraphicElementsOnScreen());
		drawOnScreen.getCenter().validate();
		print.moveToFront(gesel.getGraphicElementsForPrint());
		gesel.getGraphicElementsForPrint().validate();
	}

	/**
	 * 
	 */
	public void toBack(GraphicElements gesel)
	{
		int zorder= drawOnScreen.getCenter().getLayer(gesel.getGraphicElementsOnScreen());
		int pos= drawOnScreen.getCenter().getPosition(gesel.getGraphicElementsOnScreen());
		//drawOnScreen.getCenter().setLayer(gesel.getGraphicElementsOnScreen(),zorder<=0?0:zorder-1,0);
		drawOnScreen.getCenter().moveToBack(gesel.getGraphicElementsOnScreen());
		drawOnScreen.getCenter().validate();
		print.moveToBack(gesel.getGraphicElementsForPrint());
		gesel.getGraphicElementsForPrint().validate();
	}
}