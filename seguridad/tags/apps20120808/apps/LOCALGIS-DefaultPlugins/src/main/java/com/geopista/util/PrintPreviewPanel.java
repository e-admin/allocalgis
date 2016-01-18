/*
 * * The GEOPISTA project is a set of tools and applications to manage
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 18-jun-2004 by juacas
 *
 * 
 */
package com.geopista.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.geopista.app.AppContext;

/**
 *Panel para visualizar un objeto Printable o un Book
 *Incluye botones para avanzar o retroceder páginas en un Book.
 *
 *  @author juacas
 *@see Printable, Book
 *
 */
public class PrintPreviewPanel extends JPanel
{
	  /**
    Constructs a print preview dialog.
    @param p a Printable
    @param pf the page format
    @param pages the number of pages in p
 */
 public PrintPreviewPanel(Printable p, PageFormat pf,
    int pages)
 {  
    Book book = new Book();
    book.append(p, pf, pages);
    layoutUI(book);
 }

 /**
    Constructs a print preview dialog.
    @param b a Book
 */
 public PrintPreviewPanel(Book b)
 {  
    layoutUI(b);
 }

 /**
    Lays out the UI of the dialog.
    @param book the book to be previewed
 */
 public void layoutUI(Book book)
 {  
   // setSize(WIDTH, HEIGHT);

//Iniciamos la ayuda
            try {
                String helpHS="ayuda.hs";
                HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
                HelpBroker hb = hs.createHelpBroker();
                hb.enableHelpKey(this,"planeamientoGenerarMapaPlaneamiento05",hs); 
          }catch (Exception excp){
          }                        
 //fin de la ayuda
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    setName(aplicacion.getI18nString("printPanel02.Name"));
    Container contentPane = this;
    contentPane.setLayout(new BorderLayout());
    canvas = new PrintPreviewCanvas(book);
    contentPane.add(canvas, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();

  
    JButton previousButton = new JButton(I18NUtils.i18n_getname("Anterior"));
    buttonPanel.add(previousButton);
    previousButton.addActionListener(new
       ActionListener()
       {
          public void actionPerformed(ActionEvent event)
          {
             canvas.flipPage(-1);
          }
       });
    JButton nextButton = new JButton(I18NUtils.i18n_getname("Siguiente"));
    buttonPanel.add(nextButton);
    nextButton.addActionListener(new
       ActionListener()
       {
          public void actionPerformed(ActionEvent event)
          {
             canvas.flipPage(1);
          }
       });

//    JButton closeButton = new JButton("Close");
//    buttonPanel.add(closeButton);
//    closeButton.addActionListener(new
//       ActionListener()
//       {
//          public void actionPerformed(ActionEvent event)
//          {
//             setVisible(false);
//          }
//       });

    if (book.getNumberOfPages()>1)
    	contentPane.add(buttonPanel, BorderLayout.SOUTH);
 }

 private PrintPreviewCanvas canvas;

 private static final int WIDTH = 300;
 private static final int HEIGHT = 300;
}

/**
 The canvas for displaying the print preview.
*/
class PrintPreviewCanvas extends JPanel
{ 
 /**
    Constructs a print preview canvas.
    @param b the book to be previewed
 */
 public PrintPreviewCanvas(Book b)
 {  
    book = b;
    currentPage = 0;
 }

 public void paintComponent(Graphics g)
 {  

    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    PageFormat pageFormat = book.getPageFormat(currentPage);

    double xoff; // x offset of page start in window
    double yoff; // y offset of page start in window
    double scale; // scale factor to fit page in window
    double px = pageFormat.getWidth();
    double py = pageFormat.getHeight();
    double sx = getWidth() - 1;
    double sy = getHeight() - 1;
    if (px / py < sx / sy) // center horizontally
    {  
       scale = sy / py;
       xoff = 0.5 * (sx - scale * px);
       yoff = 0;
    }
    else // center vertically
    {  
       scale = sx / px;
       xoff = 0;
       yoff = 0.5 * (sy - scale * py);
    }
    g2.translate((float)xoff, (float)yoff);
    g2.scale((float)scale, (float)scale);

    // draw page outline (ignoring margins)
    Rectangle2D page = new Rectangle2D.Double(0, 0, px, py);
    g2.setPaint(Color.black);
    g2.fillRect(10,10,(int)px+10,(int)py+10);
    g2.setPaint(Color.white);
    g2.fill(page);
    g2.setPaint(Color.black);
    g2.draw(page);

    Printable printable = book.getPrintable(currentPage);
    try
    {  
       printable.print(g2, pageFormat, currentPage);
    }
    catch (PrinterException exception)
    {  
       g2.draw(new Line2D.Double(0, 0, px, py));
       g2.draw(new Line2D.Double(0, px, 0, py));
    }
 }

 /**
    Flip the book by the given number of pages.
    @param by the number of pages to flip by. Negative
    values flip backwards.
 */
 public void flipPage(int by)
 {  
    int newPage = currentPage + by;
    if (0 <= newPage && newPage < book.getNumberOfPages())
    {  
       currentPage = newPage;
       repaint();
    }
 }
 

 private Book book;
 private int currentPage;

}
