package com.geopista.app.ocupaciones;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.print.*;
import java.text.*;
import java.lang.reflect.Method;
import java.util.*;

import com.geopista.app.utilidades.xml.GeopistaTranslationInfo;
import com.geopista.app.utilidades.xml.GeopistaTranslator;
import com.geopista.ui.GeopistaLayerNamePanel;
import com.geopista.model.GeopistaLayerTreeModel;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 28-abr-2005
 * Time: 15:56:49
 */
public class GeopistaPrintableOcupaciones extends javax.swing.JPanel implements Printable{
    private static Logger logger = Logger.getLogger(GeopistaPrintableOcupaciones.class);

    public final static int FICHA_ADMINISTRADOR = 0;
    public final static int FICHA_CONTAMINANTE_ACTIVIDAD = 1;
    public final static int FICHA_CONTAMINANTE_ARBOLADO = 2;
    public final static int FICHA_CONTAMINANTE_VERTEDERO = 3;
    public final static int FICHA_ACTIVIDAD_CONSULTA = 4;
    public final static int FICHA_ACTIVIDAD_MODIFICACION = 5;
    public final static int FICHA_LICENCIAS_CONSULTA = 6;
    public final static int FICHA_LICENCIAS_MODIFICACION = 7;
    public final static int FICHA_LICENCIAS_OBRA_MENOR_CONSULTA = 8;
    public final static int FICHA_LICENCIAS_OBRA_MENOR_MODIFICACION = 9;
    public final static int FICHA_LICENCIAS_PLANOS = 10;
    public final static int FICHA_OCUPACIONES_CONSULTA = 11;
    public final static int FICHA_OCUPACIONES_MODIFICACION = 12;
    public final static int FICHA_OCUPACIONES_PLANOS = 13;
    public final static int FICHA_OCUPACIONES_UTILIDADES = 14;
    public final static int FICHA_CONTAMINANTE_PLANOS = 15;
    public final static int FICHA_LICENCIAS_ACTIVIDAD_CONSULTA = 16;
    public final static int FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION = 17;

    public static final int supertitulo=0;
    public static final int titulo=1;
    public static final int detalle=2;
    private int derecha=0;
    private int pagina=0;
    private int altura=0;
    private Object objeto;
    private Class clase;
    private Image plano;
    private GeopistaTranslator traductor;
    private int tipoFicha = -1;

    private ResourceBundle rbI18N = null;

    private void  pintarPiePagina(Graphics2D g2d,PageFormat format)
    {
        //Pintamos el pie de pagina
        Element piePagina= GeopistaTranslator.recuperarHijo(traductor.getRoot(), "piepagina");
        Element piePagina2= GeopistaTranslator.recuperarHijo(traductor.getRoot(), "piepaginaI18N");
        double moverY=0;
        if (piePagina!=null)
        {
           moverY=(format.getImageableHeight()-altura-20);
           g2d.translate(-derecha,moverY);
           g2d.drawLine(0,0, new Double(format.getImageableWidth()).intValue(),0);

           String sPiePagina = GeopistaTranslator.recuperarHoja(traductor.getRoot(), "piepagina");
           derecha=0;
           if (sPiePagina!=null)
           {
                int alturaOld=altura;
                altura=new Double(format.getImageableHeight()-20).intValue();
                printEncabezado(g2d,format,-1,sPiePagina);
                altura=alturaOld;
           }
           g2d.translate(0,-moverY);

        }
        else if (piePagina2!=null)
        {
           moverY=(format.getImageableHeight()-altura-20);
           g2d.translate(-derecha,moverY);
           g2d.drawLine(0,0, new Double(format.getImageableWidth()).intValue(),0);

           String sPiePagina = GeopistaTranslator.recuperarHoja(traductor.getRoot(), "piepaginaI18N");
           String sI18N = rbI18N.getString(sPiePagina);
           derecha=0;
           if (sPiePagina!=null)
           {
                int alturaOld=altura;
                altura=new Double(format.getImageableHeight()-20).intValue();
                printEncabezado(g2d,format,-1,sI18N);//sPiePagina);
                altura=alturaOld;
           }
           g2d.translate(0,-moverY);

        }
    }
    private void  pintarEncabezadoPagina(Graphics2D g2d,PageFormat format)
    {
         Element encabezado= GeopistaTranslator.recuperarHijo(traductor.getRoot(), "encabezado");
         Element encabezado2= GeopistaTranslator.recuperarHijo(traductor.getRoot(), "encabezadoI18N");
         if (encabezado!=null)
        {
            String sEncabezado = GeopistaTranslator.recuperarHoja(traductor.getRoot(), "encabezado");
            if (sEncabezado!=null)
            {
                g2d.translate(format.getImageableX(), format.getImageableY());
                printEncabezado(g2d,format,-1,sEncabezado);
                g2d.drawLine(0, 0
                          , new Double(format.getImageableWidth()).intValue(),0);
                g2d.translate(0, 10);
                altura+=10;
            }
            else
            {
                g2d.translate(format.getImageableX(), format.getImageableY());
                g2d.drawLine(new Double(format.getImageableX()).intValue(), new Double(format.getImageableY()-10).intValue()
                     , new Double(format.getImageableWidth()-derecha).intValue(),new Double(format.getImageableY()-10).intValue());
                g2d.translate(0, 10);
                altura+=10;
            }
        }
        else if (encabezado2!=null)

       {
           String sEncabezado = GeopistaTranslator.recuperarHoja(traductor.getRoot(), "encabezadoI18N");
            String sI18N = rbI18N.getString(sEncabezado);
           if (sEncabezado!=null)
           {
               g2d.translate(format.getImageableX(), format.getImageableY());
               printEncabezado(g2d,format,-1,sI18N);
               g2d.drawLine(0, 0
                         , new Double(format.getImageableWidth()).intValue(),0);
               g2d.translate(0, 10);
               altura+=10;
           }
           else
           {
               g2d.translate(format.getImageableX(), format.getImageableY());
               g2d.drawLine(new Double(format.getImageableX()).intValue(), new Double(format.getImageableY()-10).intValue()
                    , new Double(format.getImageableWidth()-derecha).intValue(),new Double(format.getImageableY()-10).intValue());
               g2d.translate(0, 10);
               altura+=10;
           }
       }
       else
         g2d.translate(format.getImageableX(), format.getImageableY());
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
    public int printEncabezado(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.PLAIN, 8);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font, true);
    }
    public int printDetalle(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.PLAIN, 10);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena,detalle,font);
    }
    public int printTitulo(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.BOLD, 12);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font);
    }
    public int printPie(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
   {
       Font font = new Font("Verdana", Font.PLAIN, 8);
       if (g2d!=null) g2d.setFont(font);
       return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font);
   }

    public int printTituloDetalle(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        if ((cadena!=null)&&(cadena.indexOf(":")<0)) cadena+=":";
        Font font = new Font("Verdana", Font.BOLD, 10);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena+" ", titulo,font );
    }
    public int printSuperTitulo(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.BOLD, 14);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font);
    }
    public int printSaltoLinea(Graphics2D g2d, PageFormat pageFormat, int pageIndex)
    {
        Font font = new Font("Verdana", Font.BOLD, 12);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, " ", supertitulo,font);
    }
    public int printCabecera(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.BOLD, 16);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font);
    }

    public int printString (Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena, int tipo, Font dataFont)
    {
        return printString(g2d, pageFormat, pageIndex, cadena,  tipo,  dataFont, false);
    }
    public int printString (Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena, int tipo, Font dataFont, boolean bPiePagina)
    {
          if (tipo==supertitulo|| tipo==titulo)
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
    public void init()
    {
         derecha=0;
         pagina=0;
         altura=0;
    }

    public int getPaginasDocumento() {
        return pagina;
    }
    public void setAltura(int iAltura)
    {
        altura=iAltura;
    }

    public void printObjeto(String urlXml, Object objeto, Class clase, LayerViewPanel layerViewPanel, int tipoFicha) throws Exception
    {
        this.tipoFicha = tipoFicha;
              int altoPanel = layerViewPanel.getViewport().getPanel().getHeight();
              int anchoPanel = layerViewPanel.getViewport().getPanel().getWidth();
              Image image = layerViewPanel.createImage(anchoPanel, altoPanel);

              GeopistaLayerNamePanel legendPanel = new GeopistaLayerNamePanel(
                   layerViewPanel,new GeopistaLayerTreeModel(layerViewPanel),layerViewPanel.getRenderingManager(),
                   new HashMap());

              Graphics g = image.getGraphics();
              layerViewPanel.getViewport().getPanel().printAll(g);
              printObjeto(urlXml, objeto , clase, image, tipoFicha);
    }

    public void printObjeto(String urlXml, Object objeto, Class clase, Image plano, int tipoFicha) throws Exception
    {
        this.tipoFicha = tipoFicha;
            this.objeto=objeto;
            this.plano=plano;
            this.clase=clase;

             ClassLoader cl =(new GeopistaPrintableOcupaciones()).getClass().getClassLoader();
            java.net.URL url=cl.getResource(urlXml);

            traductor= new  GeopistaTranslator(new GeopistaTranslationInfo(url,false));
            traductor.parsear();


          PrinterJob printerJob=PrinterJob.getPrinterJob();
          //Imprimo en formato A$
          PageFormat format = new PageFormat();
          format.setOrientation(PageFormat.PORTRAIT);
          Paper paper = format.getPaper();
//          paper.setSize(587, 842); // Svarer til A4
          paper.setSize(594.936, 841.536); // A4
//          paper.setImageableArea(44, 52, 500, 738);
          paper.setImageableArea(44, 52, 506, 738);
          format.setPaper(paper);
          //Obtengo un vector con los graficos a imprimir
          //printPanel(null, format, -1);
          int paginas = 9999;
          logger.warn("Se van a imprimir: "+paginas+" pagina/s");
          //Relleno los gráficos
          Book book = new Book();
          book.append(this,format,paginas);

          printerJob.setPageable(book);
          boolean doPrint=printerJob.printDialog();
          if (doPrint)
          {
              try{ printerJob.print();}catch (Exception e){
                  logger.error("Excepcion al imprimir "+e.toString());
             }
          }
    }

    public String getNombreInspector(String idInspector)
    {
        // TODO: en la llamada de Operacionescontaminantes.java se llama a un servlet. ¿De dónde obtengo los datos?
/*           if (listaInspectores==null || idInspector==null) return null;
           for (Enumeration e=listaInspectores.elements();e.hasMoreElements();)
           {
               com.geopista.protocol.contaminantes.Inspector aux=(com.geopista.protocol.contaminantes.Inspector) e.nextElement();
               if (idInspector.equalsIgnoreCase(aux.getId()))
                   return (aux.getNombre()+" "+aux.getApellido1()+" "+aux.getApellido2());
           }
*/           return "";
    }
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		// TODO Auto-generated method stub
		return 0;
	}



}
