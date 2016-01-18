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
 * JBGList.java
 * 
 * Created on March 22, 2006, 4:27 PM
 *
 */

package it.businesslogic.ireport.gui.style;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author gtoffoli
 */
public class JBGTree extends JTree {
    
    private boolean showLibrary = true;
    /**
     * 109 x 106
     */
    public static Image backGround =  (new javax.swing.ImageIcon(JBGTree.class.getResource("/it/businesslogic/ireport/icons/styles/library.png"))).getImage() ; 
    public static Image backGroundDocument =  (new javax.swing.ImageIcon(JBGTree.class.getResource("/it/businesslogic/ireport/icons/styles/document.png"))).getImage() ; 
    /** Creates a new instance of JBGList */
    public JBGTree() {
        super();
        setOpaque(false);
        this.setCellRenderer(new StyleCellRenderer());
        
    }
    
    public Object getSelectedItem()
    {
        if (getSelectionPath() != null &&
            getSelectionPath().getLastPathComponent() instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)getSelectionPath().getLastPathComponent();
            return selectedNode.getUserObject();
        }
        
        return null;
    }
    
    public void paint(Graphics g)
    {
        
        Rectangle r = this.getVisibleRect();
        g.setColor(Color.WHITE);
        g.fillRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(),(int)r.getHeight());
        g.drawImage( (isShowLibrary()) ? backGround : backGroundDocument,  (int)r.getX()+(int)r.getWidth()-109,(int)r.getY()+(int)r.getHeight()-106,109,106,this);
        super.paint(g);
        
    }

    public boolean isShowLibrary() {
        return showLibrary;
    }

    public void setShowLibrary(boolean newShowLibrary) {
        if (showLibrary == newShowLibrary) return;
        this.showLibrary = newShowLibrary;
        this.repaint();
    }
    
}
