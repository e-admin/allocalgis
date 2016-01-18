/**
 * GestionCiudadMapPrinter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.printsearch.printer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import org.apache.log4j.Logger;

import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

/**
 * @author javieraragon
 *
 */


public class GestionCiudadMapPrinter extends javax.swing.JPanel implements Printable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5895276652731606268L;

	private static Logger logger = Logger.getLogger(GestionCiudadMapPrinter.class);

	private int derecha=0;
	private int pagina=0;
	private int altura=0;
	public static final int titulo=1;

	private Image plano;

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
	throws PrinterException {
		// TODO Auto-generated method stub

		Graphics2D g2d = (Graphics2D)graphics;

		printTitulo(g2d, pageFormat, pageIndex, "Titulo del Mapa");

		printPlano(g2d, pageFormat, pageIndex, 200, 200);
		
		
		g2d.drawString("Hola",	10, 10);
		return Printable.PAGE_EXISTS;
	}

	public boolean setMapaGestionCiudad(LayerViewPanel layerViewPanel){

		int altoPanel = layerViewPanel.getViewport().getPanel().getHeight();
		int anchoPanel = layerViewPanel.getViewport().getPanel().getWidth();
		Image image = layerViewPanel.createImage(anchoPanel, altoPanel);

		plano = image;
		return true;
	}


	public void printPlano(Graphics2D g2d, PageFormat pageFormat, int pageIndex, double ancho, double largo)
	{
		if (plano!=null)
		{
			if ((derecha>0)&&g2d!=null)
			{
				g2d.translate(-derecha,0);
				derecha=0;
			}
			if (ancho>pageFormat.getImageableWidth()-4) ancho=pageFormat.getImageableWidth()-4;
			if (largo>pageFormat.getImageableHeight()-4) largo=pageFormat.getImageableHeight()-4;
			//encuadramos la pagina en el rectangulo
			if ((ancho/plano.getWidth(this)> (largo/plano.getHeight(this))))
				ancho=(plano.getWidth(this)*largo)/plano.getHeight(this);
			else
				largo=(plano.getHeight(this)*ancho)/plano.getWidth(this);
			if ((altura+largo+4)>pageFormat.getImageableHeight())
			{
				//pintarPiePagina(g2d,pageFormat);
				pagina++;
				altura=0;
			}

			if (pagina==pageIndex)
			{
				try
				{
					Shape s = new Rectangle(0,altura,new Double(ancho+4).intValue(),new Double(largo+4).intValue());
					g2d.setPaint(Color.black);
					g2d.draw(s);
					g2d.drawImage(plano,2,altura+2, new Double(ancho).intValue(),new Double(largo).intValue(),this);
					g2d.translate(0,altura+new Double(largo).intValue()+6);
				}catch(Exception e)
				{
					logger.error("Error al pintar el plano:",e);
				}
			}
			setAltura(altura+new Double(largo).intValue()+6);
		}
	}

	public int printTitulo(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
	{
		Font font = new Font("Verdana", Font.BOLD, 12);
		if (g2d!=null) g2d.setFont(font);
		return printString ( g2d, pageFormat,  pageIndex, cadena, 0,font);
	}



	public int printString (Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena, int tipo, Font dataFont)
	{
		return printString(g2d, pageFormat, pageIndex, cadena,  tipo,  dataFont, false);
	}


	public int printString (Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena, int tipo, Font dataFont, boolean bPiePagina)
	{
		if (tipo==0|| tipo==titulo)
		{
			if ((derecha>0)&&g2d!=null)
			{
				g2d.translate(-derecha,0);
				derecha=0;
			}
		}
		if (cadena==null || cadena.length()<=0)
			cadena="  ";
		if (g2d!=null)
			System.out.println("PAGINA: "+pageIndex+" GRAFICO X: "+g2d.getTransform().getTranslateX()+" -  Y:"+g2d.getTransform().getTranslateY()+" ALTURA:"+altura+" CADENA:"+cadena);

		int imover=0;
		if (g2d!=null) g2d.setPaint(Color.black);
		Point2D.Float pen = new Point2D.Float();
		AttributedCharacterIterator charIterator = new AttributedString(cadena,dataFont.getAttributes()).getIterator();
		LineBreakMeasurer measurer;
		if (g2d!=null)
			measurer= new LineBreakMeasurer(charIterator,g2d.getFontRenderContext());
		else
			measurer= new LineBreakMeasurer(charIterator,new FontRenderContext(new AffineTransform(),false,false));

		float wrappingWidth = (float) pageFormat.getImageableWidth()-derecha;
		int bSegunda=1;
		boolean bQuitaMargen=false;
		while (measurer.getPosition() < charIterator.getEndIndex()) {
			if (bSegunda==2 && derecha>0)
			{
				bQuitaMargen=true;
				if (g2d!=null) g2d.translate(-derecha,0);
				wrappingWidth+=derecha;

			}

			bSegunda++;
			TextLayout layout = measurer.nextLayout(wrappingWidth);
			if ((altura+layout.getAscent()+layout.getDescent() + layout.getLeading()+20>pageFormat.getImageableHeight())
					&& !bPiePagina) //pasamos de pagina
					{
				//pintarPiePagina(g2d,pageFormat);
				pagina++;
				altura=0;
					}
			pen.y += layout.getAscent();
			float dx = layout.isLeftToRight()? 0 :
				(wrappingWidth - layout.getAdvance());
			if (pagina==pageIndex || bPiePagina)
				if (g2d!=null)layout.draw(g2d, pen.x + dx, pen.y);

			pen.y += layout.getDescent() + layout.getLeading();
			imover+=layout.getBounds().getBounds2D().getWidth();

		}
		if (bQuitaMargen)
		{
			if (g2d!=null) g2d.translate(derecha,0);
		}
		if (tipo==titulo)
		{
			imover+=10;
			if (g2d!=null) g2d.translate(imover,0);
			derecha+=imover;
		}
		else
		{
			if (pagina==pageIndex || bPiePagina)
			{
				if (g2d!=null) g2d.translate(0,pen.y);
			}
			altura+=pen.y+4;
		}

		return Printable.PAGE_EXISTS;
	}

	public void printMap(){
		PrinterJob printerJob=PrinterJob.getPrinterJob();
		//Imprimo en formato A$
		PageFormat format = new PageFormat();
		format.setOrientation(PageFormat.PORTRAIT);
		Paper paper = format.getPaper();
		//      paper.setSize(587, 842); // Svarer til A4
		paper.setSize(594.936, 841.536); // A4
		//      paper.setImageableArea(44, 52, 500, 738);
		paper.setImageableArea(44, 52, 506, 738);
		format.setPaper(paper);
		//Obtengo un vector con los graficos a imprimir
		//printPanel(null, format, -1);
		int paginas = 9999;
		logger.warn("Se van a imprimir: "+paginas+" pagina/s");
		//Relleno los gráficos
		Book book = new Book();
		book.append(this,format,1);

		printerJob.setPageable(book);
		printerJob.setPrintable(this);
		boolean doPrint=printerJob.printDialog();
		if (doPrint)
		{
			try{ printerJob.print();}catch (Exception e){
				logger.error("Excepcion al imprimir "+e.toString());
			}
		}
	}
	

	  


	/**
	 * @return the derecha
	 */
	public int getDerecha() {
		return derecha;
	}

	/**
	 * @param derecha the derecha to set
	 */
	public void setDerecha(int derecha) {
		this.derecha = derecha;
	}

	/**
	 * @return the pagina
	 */
	public int getPagina() {
		return pagina;
	}

	/**
	 * @param pagina the pagina to set
	 */
	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	/**
	 * @return the altura
	 */
	public int getAltura() {
		return altura;
	}

	/**
	 * @param altura the altura to set
	 */
	public void setAltura(int altura) {
		this.altura = altura;
	}
	
	

}
