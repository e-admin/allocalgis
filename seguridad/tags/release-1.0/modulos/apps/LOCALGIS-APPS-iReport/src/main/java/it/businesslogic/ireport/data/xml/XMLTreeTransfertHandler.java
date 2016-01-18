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
 * TreeTransfertHandler.java
 * 
 * Created on 15 settembre 2004, 2.19
 *
 */

package it.businesslogic.ireport.data.xml;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.gui.library.*;
import it.businesslogic.ireport.gui.MainFrame;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceMotionListener;
import javax.swing.*;
import javax.swing.tree.*;
import org.w3c.dom.Node;
/**
 *
 * @author  Administrator
 */
public class XMLTreeTransfertHandler extends javax.swing.TransferHandler  
//iR20 implements DragSourceMotionListener, TimingTarget 
{
    XMLFieldMappingEditor xmlEditor = null;

    public XMLFieldMappingEditor getXmlEditor() {
        return xmlEditor;
    }

    public void setXmlEditor(XMLFieldMappingEditor xmlEditor) {
        this.xmlEditor = xmlEditor;
    }
    
    /** Creates a new instance of TreeTransfertHandler 
     * @param xmlEditor 
     */
    public XMLTreeTransfertHandler(XMLFieldMappingEditor xmlEditor) {
        super();
        this.xmlEditor = xmlEditor;
    }
    
    public int getSourceActions(JComponent c) 
    {
        return COPY_OR_MOVE;
        
    }
    
    protected Transferable createTransferable(JComponent c) 
    {
//iR20         DragSource.getDefaultDragSource().addDragSourceMotionListener(this);
//iR20         glassPane = (GhostGlassPane) MainFrame.getMainInstance().getGlassPane();
//iR20         glassPane.setImage(imgRect.getImage(), false);
        
        if (c instanceof JTree)
        {
            JTree tree = (JTree)c;
            TreePath path = tree.getLeadSelectionPath();
	    DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
            
            JRField field = getXmlEditor().createField(path, true);
            
            return new it.businesslogic.ireport.gui.dnd.TransferableObject(field);           
        }
        
        return new it.businesslogic.ireport.gui.dnd.TransferableObject(c);
    }
    
    
//iR20     GhostGlassPane glassPane = null;
//iR20     final public ImageIcon imgRect = new ImageIcon(DockPanel.class.getResource("/it/businesslogic/ireport/icons/palette/rectangle.png"));
    

    

//iR20     public void dragMouseMoved(DragSourceDragEvent e) {
//iR20         
//iR20         Component c = e.getDragSourceContext().getComponent();
//iR20 
//iR20         if (to == null) {
//iR20             to = e.getDragSourceContext().getTrigger().getDragOrigin();
//iR20             
//iR20             //java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)ie;
//iR20             //Point p = me.getLocationOnScreen();
//iR20             to = SwingUtilities.convertPoint(e.getDragSourceContext().getTrigger().getComponent(), to, glassPane);
//iR20             //to.x += 12;
//iR20             //to.y -= 2;
//iR20             //to = p;
//iR20         }
//iR20         
//iR20         Point p = (Point) e.getLocation().clone();
//iR20         //SwingUtilities.convertPointToScreen(p, c);
//iR20         glassPane.moveImageTo(p);
//iR20         if (!glassPane.isVisible())
//iR20         {
//iR20             glassPane.setVisible(true);
//iR20         }
//iR20     }
    
    
//iR20     public void exportDone(JComponent source, Transferable data, int action) {
//iR20        
//iR20             DragSource.getDefaultDragSource().removeDragSourceMotionListener(this);
//iR20             boolean accepted = (action != NONE);
//iR20             from = glassPane.getPoint();
//iR20             glassPane.setAccepted( accepted );
//iR20             new TimingController(300, this).start();
//iR20              
//iR20     }
    
    
    
//iR20     Point from;
//iR20     Point to;
//iR20     
//iR20     public void begin() {
//iR20         
//iR20     }
    
//iR20     public void end() {
//iR20         from = null;
//iR20         to = null;
//iR20         glassPane.setImage(null, false);
//iR20     }

//iR20     public void timingEvent(long l, long l0, float f) {
//iR20         
//iR20         if (glassPane.isAccepted()) {
//iR20             
//iR20             glassPane.moveImageTo( glassPane.getPoint(),  f);
//iR20         } else {
//iR20             Point newP = new Point();
//iR20             newP.x = from.x + (int)((to.x - from.x) * f);
//iR20             newP.y = from.y + (int)((to.y - from.y) * f);
//iR20             glassPane.moveImageTo(newP, f);
//iR20         }
//iR20     }
    
}
