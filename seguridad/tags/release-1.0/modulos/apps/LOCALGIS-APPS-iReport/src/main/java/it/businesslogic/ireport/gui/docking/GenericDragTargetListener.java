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
 * GenericDragTargetListener.java
 * 
 * Created on March 16, 2006, 10:59 PM
 *
 */

package it.businesslogic.ireport.gui.docking;

import it.businesslogic.ireport.gui.MainFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;


  /**
   * GenericDragTargetListener
   * a listener that tracks the state of the operation
   * @see java.awt.dnd.DropTargetListener
   * @see java.awt.dnd.DropTarget
   */
 public class GenericDragTargetListener implements DropTargetListener {

    public static JDraggableTabbedPane lastDp = null;
    
    public static final int POSITION_UP = 0;
    public static final int POSITION_CENTER = 1;
    public static final int POSITION_BOTTOM = 2;
    public static final Color COLOR = new java.awt.Color(255,0,0,128);
    
    private int lastPosition = -1;
    
    public static final BasicStroke STROKE = new BasicStroke(3);
    /**
     * start "drag under" feedback on component
     * invoke acceptDrag or rejectDrag based on isDragOk
     */
    public void dragEnter(DropTargetDragEvent e) {
        handleMotion(e);
      e.acceptDrag(e.getDropAction());        
    }

    /**
     * continue "drag under" feedback on component
     * invoke acceptDrag or rejectDrag based on isDragOk
     */
    public void dragOver(DropTargetDragEvent e) {
        
         handleMotion(e);
          
      e.acceptDrag(e.getDropAction()); 
    }
    
    public void handleMotion(DropTargetDragEvent dtde)
    {
        DropTargetContext context = dtde.getDropTargetContext();
      
      //Transferable tr = dtde.getTransferable();
      // JDK 1.4.2 workaround Bug Id  4248542 
        DropTargetDropEvent tempDTDropEvent = new  
        DropTargetDropEvent(dtde.getDropTargetContext(),
                dtde.getLocation(), 0, 0);
        Transferable tr = tempDTDropEvent.getTransferable();

        
      DataFlavor[] df = tr.getTransferDataFlavors();

      if (df[0].getHumanPresentableName().equals("it.businesslogic.ireport.gui.docking.PanelView"))
      {
          JDraggableTabbedPane dp = lookForParentDraggablePane( dtde.getDropTargetContext().getComponent() );
          //System.out.println( "Sono in!!" + dtde.getLocation() + " ->" + dp);
          
          if (lastDp != dp)
          {
              if (lastDp != null) lastDp.repaint();
              lastDp = dp;
              lastPosition = -1;
          }
          if (dp != null)
          {
              Graphics2D g = (Graphics2D)dp.getGraphics();
              int height = dp.getHeight();
              if (dtde.getLocation()!= null)
              {
                  try {
               
                  int position = (int)dtde.getLocation().getY();
                  
                  if (position <= height/3)
                  {
                      if (lastPosition != POSITION_UP)
                      {
                        lastPosition = POSITION_UP;
                        dp.paint(g);
                        //g.setClip( dp.getBounds() );
                        g.setStroke(STROKE);
                        g.setColor( COLOR );
                        g.drawRect(1,1,dp.getWidth()-4, (height/3)-6);
                      }
                  }
                  else if ( (position <= (height/3) * 2))
                  {
                      if (lastPosition != POSITION_CENTER)
                      {
                        lastPosition = POSITION_CENTER;
                        dp.paint(g);
                        //g.setClip( dp.getBounds() );
                        g.setStroke(STROKE);
                        g.setColor( COLOR );
                        g.drawRect(1,1,dp.getWidth()-4, height-6);
                      }
                  }
                  else if ( lastPosition != POSITION_BOTTOM)
                  {
                      if (lastPosition != POSITION_BOTTOM)
                      {
                        lastPosition = POSITION_BOTTOM;
                        dp.paint(g);
                        g.setStroke(STROKE);
                        g.setColor( COLOR );
                        g.drawRect(1,height-(height/3),dp.getWidth()-4, (height/3)-4);
                      }
                  }
                  } catch (Exception ex){
                      
                  }
              }
          }
      }
    }
    
    public void dropActionChanged(DropTargetDragEvent e) {
    }
    
    public void dragExit(DropTargetEvent e) {
        //if (lastDp != null) lastDp.repaint();
        //lastDp = null;
    }

    public JDraggableTabbedPane lookForParentDraggablePane(Component component)
    {
        if (component == null) return null;
        if (component instanceof JDraggableTabbedPane) return (JDraggableTabbedPane)component;
        return lookForParentDraggablePane(component.getParent());
        
    }
    /**
     * perform action from getSourceActions on
     * the transferrable
     * invoke acceptDrop or rejectDrop
     * invoke dropComplete
     * if its a local (same JVM) transfer, use StringTransferable.localStringFlavor
     * find a match for the flavor
     * check the operation
     * get the transferable according to the chosen flavor
     * do the transfer
     */
    public void drop(DropTargetDropEvent dtde) {
        
        try {
           
            DropTargetContext context = dtde.getDropTargetContext();

            Transferable tr = dtde.getTransferable();

            DataFlavor[] df = tr.getTransferDataFlavors();

            if (tr.isDataFlavorSupported (DataFlavor.javaFileListFlavor))
            {
                dtde.acceptDrop ( DnDConstants.ACTION_COPY_OR_MOVE);
                java.util.List fileList = (java.util.List)tr.getTransferData(DataFlavor.javaFileListFlavor);
            
                MainFrame.getMainInstance().openFiles(fileList);
            }
            else if (df[0].getHumanPresentableName().equals("it.businesslogic.ireport.gui.docking.PanelView"))
            {                               
                java.awt.datatransfer.DataFlavor myFlavor = new java.awt.datatransfer.DataFlavor(it.businesslogic.ireport.gui.docking.PanelView.class, it.businesslogic.ireport.gui.docking.PanelView.class.getName());
                it.businesslogic.ireport.gui.docking.PanelView panelView = (it.businesslogic.ireport.gui.docking.PanelView)tr.getTransferData( myFlavor );
                
                // Look for the parent draggable
                JDraggableTabbedPane dp = lastDp;
                
                if (dp != null)
                {
                
                    if (dp.getDockingContainer() == panelView.getDockingContainer())
                    {
                        int containerPosition = dp.getPosition();
                        if (panelView.getDockingContainer().getPanelCount(panelView.getPosition()) == 1 && dp.getPosition() == panelView.getPosition())
                        {
                            // Nothing to do
                        }
                        if (dp.getTabCount() > 1 && dp.getPosition() == panelView.getPosition())
                        {
                            if (lastPosition == POSITION_UP)
                            {
                                dp.getDockingContainer().moveComponent(panelView, containerPosition);
                            }
                            if (lastPosition == POSITION_BOTTOM)
                            {
                               dp.getDockingContainer().moveComponent(panelView, containerPosition+1);
                            }
                        }
                        else if (dp.getPosition() != panelView.getPosition())
                        {
                            if (lastPosition == POSITION_UP)
                            {
                                if (containerPosition-1 >= 0 &&
                                    panelView.getPosition() < containerPosition) containerPosition = containerPosition-1;
                                dp.getDockingContainer().moveComponent(panelView, containerPosition);
                            }
                            if (lastPosition == POSITION_BOTTOM)
                            {
                               if (panelView.getPosition() != containerPosition+1 || panelView.getDockingContainer().getPanelCount(panelView.getPosition())>0)
                               { 
                                    if (panelView.getPosition() > containerPosition) containerPosition +=1;
                                    dp.getDockingContainer().moveComponent(panelView, containerPosition);
                               }
                            }
                            if (lastPosition == POSITION_CENTER)
                            {
                               if (panelView.getPosition() >= containerPosition || panelView.getDockingContainer().getPanelCount(panelView.getPosition()) == 1)
                               {
                                 if (containerPosition-1 >= 0) containerPosition = containerPosition-1;
                               }
                               else
                               {
                                   
                               }
                               dp.getDockingContainer().moveComponent(panelView, containerPosition, DockingContainer.INSERT_MODE_SHAREDPOSTION);
                            }
                        }                      
                    }
                    else
                    {
                        int containerPosition = dp.getPosition();
                        panelView.getDockingContainer().removePanel(panelView.getComponent());
                        if (lastPosition == POSITION_UP)
                        {
                            dp.getDockingContainer().insertPanel(containerPosition, panelView.getName(), panelView.getComponent(), DockingContainer.INSERT_MODE_NEWPOSITION, panelView.isClosable());
                        }
                        if (lastPosition == POSITION_BOTTOM)
                        {
                           dp.getDockingContainer().insertPanel(containerPosition+1, panelView.getName(), panelView.getComponent(), DockingContainer.INSERT_MODE_NEWPOSITION, panelView.isClosable());
                        }
                        if (lastPosition == POSITION_CENTER)
                        {
                           //if (containerPosition-1 >= 0) containerPosition = containerPosition-1;
                           dp.getDockingContainer().insertPanel(containerPosition, panelView.getName(), panelView.getComponent(), DockingContainer.INSERT_MODE_SHAREDPOSTION, panelView.isClosable());
                        }
                        
                        
                        
                    }
                }
               /* else
                {
                    if (dtde.getDropTargetContext().getComponent() instanceof DockingContainer)
                    {
                        DockingContainer dockContainer = (DockingContainer)(dtde.getDropTargetContext().getComponent());
                        panelView.getDockingContainer().removePanel(panelView.getComponent());
                        dockContainer.insertPanel(0, panelView.getName(), panelView.getComponent());
                    }
                }
                */
            }
            
            context.dropComplete(true);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

  }
