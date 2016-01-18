/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * JSQLTablesPane.java
 * 
 * Created on 21 maggio 2003, 19.03
 *
 */

package it.businesslogic.ireport.gui;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
/**
 *
 * @author  Administrator
 */
public class JSQLTablesPane extends javax.swing.JDesktopPane {
    
    boolean repainting = false;
    private Graphics2D offscreen = null;
    private BufferedImage offscreenImage = null;
    /** Creates a new instance of JSQLTablesPane */
    public JSQLTablesPane() {
        
        Dimension offscreenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        offscreenImage = new java.awt.image.BufferedImage(offscreenDimension.width, offscreenDimension.height, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE);
        
        offscreen = GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(offscreenImage);
        offscreen.setBackground(this.getBackground());
        offscreen.setColor(Color.BLACK);
        this.setDoubleBuffered(false);
        
    }
    
    public void paint(Graphics g)
    {
        if (repainting) return;
        repainting = true;
        //this.repaint();
        //super.paint(g);
        if (offscreen == null) return;
        offscreen.clearRect(0,0, this.getWidth(), this.getHeight());
        JInternalFrame[] frames = this.getAllFrames();
        // Draw a line between two fields...
        offscreen.drawLine( frames[0].getLocation().x+frames[0].getWidth()/2,
                    frames[0].getLocation().y+frames[0].getHeight()/2,
                    frames[1].getLocation().x+frames[1].getWidth()/2,
                    frames[1].getLocation().y+frames[1].getHeight()/2);
        
        super.paintChildren(offscreen);
        g.drawImage( offscreenImage,0,0,(ImageObserver)this);
        repainting = false;
    }
    
     public void repaint()
    {
        paint( this.getGraphics() );
    }
     /*
    public void repaint(int x,int y, int width, int height)
    {
        super.repain
        //paint(this.getGraphics() );
    }
      **/
}
