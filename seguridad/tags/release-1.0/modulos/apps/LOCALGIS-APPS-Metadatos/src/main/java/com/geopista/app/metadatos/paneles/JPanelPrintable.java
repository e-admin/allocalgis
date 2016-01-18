package com.geopista.app.metadatos.paneles;

import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;


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
 * Date: 14-sep-2004
 * Time: 12:31:29
 */
public abstract class JPanelPrintable extends javax.swing.JPanel {

    public static final int supertitulo=0;
    public static final int titulo=1;
    public static final int detalle=2;
    private static int derecha=0;
    private static int pagina=0;
    private static int altura=0;

    public abstract int printPanel(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException ;

    public int printDetalle(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.PLAIN, 10);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena,detalle,font);
    }
    public int printTitulo(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        if ((cadena!=null)&&(cadena.indexOf(":")<0)) cadena+=":";
        Font font = new Font("Verdana", Font.BOLD, 10);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena+" ", titulo,font );
    }
    public int printSuperTitulo(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.BOLD, 12);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font);
    }

    public int printString (Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena, int tipo, Font dataFont)
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
            if (altura+layout.getAscent()+layout.getDescent() + layout.getLeading()+20>pageFormat.getImageableHeight()) //pasamos de pagina
            {
                 pagina++;
                 altura=0;
           }
            pen.y += layout.getAscent();
            float dx = layout.isLeftToRight()? 0 :
                    (wrappingWidth - layout.getAdvance());
            if (pagina==pageIndex)
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
            if (pagina==pageIndex)
            {
                 if (g2d!=null) g2d.translate(0,pen.y);
            }
           altura+=pen.y;
       }

       return Printable.PAGE_EXISTS;
     }
    public static void init()
    {
         derecha=0;
         pagina=0;
         altura=0;
    }

    public static int getPaginasDocumento() {
        return pagina;
    }
    public static void setAltura(int iAltura)
    {
        altura=iAltura;
    }


}
