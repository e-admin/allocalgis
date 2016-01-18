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
 * JDraggableTabbedPane.java
 * 
 * Created on January 26, 2006, 9:16 AM
 *
 */

package it.businesslogic.ireport.gui.docking;

import it.businesslogic.ireport.gui.MainFrame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.MouseAdapter;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.*;

/**
 *
 * @author gtoffoli
 */
public class JDraggableTabbedPane extends ExtendedTabbedPane implements LanguageChangedListener {

    private javax.swing.JMenuItem jMenuItemMerge;
    private javax.swing.JMenuItem jMenuItemUnmerge;
    private javax.swing.JPopupMenu jPopupMenu;



    private DockingContainer dockingContainer = null;
    private int position = 0;

    /** Creates a new instance of JDraggableTabbedPane */
    public JDraggableTabbedPane() {
        super();
        it.businesslogic.ireport.util.I18n.setCurrentLocale( System.getProperty("Language"), System.getProperty("Country") );
        this.dragSource = DragSource.getDefaultDragSource();
        this.dgListener = new DGListener();
        this.dsListener = new DSListener((int)(1000*Math.random())) ;
        this.dtListener = new GenericDragTargetListener();

        // component, ops, listener, accepting
        this.dropTarget = new DropTarget(this,
            this.acceptableActions,
            this.dtListener,
        true);

        // component, action, listener
        this.dragSource.createDefaultDragGestureRecognizer(
        this,
        this.dragAction,
        this.dgListener);

        jPopupMenu = new JPopupMenu();

        jMenuItemMerge = new javax.swing.JMenuItem();
        jMenuItemMerge.setText("Merge panels");
        jMenuItemMerge.setEnabled(true);
        jMenuItemMerge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMergeActionPerformed(evt);
            }
        });

        jPopupMenu.add(jMenuItemMerge);

        jMenuItemUnmerge = new javax.swing.JMenuItem();
        jMenuItemUnmerge.setText("Separate panel");
        jMenuItemUnmerge.setEnabled(true);
        jMenuItemUnmerge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUnmergeActionPerformed(evt);
            }
        });

        jPopupMenu.add(jMenuItemUnmerge);

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onMouseClicked(evt);
            }
        });

        this.setOpaque(true);

        I18n.addOnLanguageChangedListener( this );

    }

    private void jMenuItemUnmergeActionPerformed(java.awt.event.ActionEvent evt) {

        PanelView pv = new PanelView( JDraggableTabbedPane.this.getTitleAt( JDraggableTabbedPane.this.getSelectedIndex() ),
                                      JDraggableTabbedPane.this.getSelectedComponent(),
                                      JDraggableTabbedPane.this.getPosition(),
                                      JDraggableTabbedPane.this.isClosable( JDraggableTabbedPane.this.getSelectedIndex() )
                                      );
        getDockingContainer().moveComponent(pv, pv.getPosition(), DockingContainer.INSERT_MODE_NEWPOSITION);
    }



    private void jMenuItemMergeActionPerformed(java.awt.event.ActionEvent evt) {
        getDockingContainer().mergePosition( this.getPosition() );
    }

    public void onMouseClicked(java.awt.event.MouseEvent evt)
    {
        if (evt.getButton() == evt.BUTTON3 && evt.getClickCount() == 1) {
            this.jPopupMenu.show(this, evt.getPoint().x, evt.getPoint().y);
        }
    }

   /**
   * DGListener
   * a listener that will start the drag.
   * has access to top level's dsListener and dragSource
   * @see java.awt.dnd.DragGestureListener
   * @see java.awt.dnd.DragSource
   * @see java.awt.datatransfer.StringSelection
   */
  class DGListener implements DragGestureListener {
    /**
     * Start the drag if the operation is ok.
     * uses java.awt.datatransfer.StringSelection to transfer
     * the label's data
     * @param e the event object
     */
    public void dragGestureRecognized(DragGestureEvent e) {

      // if the action is ok we go ahead
      // otherwise we punt
      if((e.getDragAction() & JDraggableTabbedPane.this.dragAction) == 0)
	return;

      // get the label's text and put it inside a Transferable
      // Transferable transferable = new StringSelection( DragLabel.this.getText() );
      if (JDraggableTabbedPane.this.getSelectedIndex() < 0) return;

      PanelView pv = new PanelView( JDraggableTabbedPane.this.getTitleAt( JDraggableTabbedPane.this.getSelectedIndex() ),
                                    JDraggableTabbedPane.this.getSelectedComponent(),
                                    JDraggableTabbedPane.this.getPosition(),
                                    JDraggableTabbedPane.this.isClosable( JDraggableTabbedPane.this.getSelectedIndex() ));
      pv.setDockingContainer( JDraggableTabbedPane.this.getDockingContainer());
      Transferable transferable = new PanelTransferable( pv);

      // now kick off the drag
      try {
	// initial cursor, transferrable, dsource listener
	e.startDrag(DragSource.DefaultCopyNoDrop,
	  transferable,
	  JDraggableTabbedPane.this.dsListener);

	// or if dragSource is a variable
	// dragSource.startDrag(e, DragSource.DefaultCopyDrop, transferable, dsListener);


	// or if you'd like to use a drag image if supported

	/*
	  if(DragSource.isDragImageSupported() )
	  // cursor, image, point, transferrable, dsource listener
	  e.startDrag(DragSource.DefaultCopyDrop, image, point, transferable, dsListener);
	*/

      }catch( InvalidDnDOperationException idoe ) {
      }
    }
  }

  /**
   * DSListener
   * a listener that will track the state of the DnD operation
   *
   * @see java.awt.dnd.DragSourceListener
   * @see java.awt.dnd.DragSource
   * @see java.awt.datatransfer.StringSelection
   */
  class DSListener implements DragSourceListener {

      public int myId = 0;
     public  DSListener(int id)
     {
         super();
         myId = id;
     }

    /**
     * @param e the event
     */
    public void dragDropEnd(DragSourceDropEvent e) {

        if (GenericDragTargetListener.lastDp != null)
        {
            GenericDragTargetListener.lastDp.repaint();
            GenericDragTargetListener.lastDp = null;
        }
    }

    /**
     * @param e the event
     */
    public void dragEnter(DragSourceDragEvent e) {
      DragSourceContext context = e.getDragSourceContext();
      //intersection of the users selected action, and the source and target actions
      int myaction = e.getDropAction();
      if( (myaction & JDraggableTabbedPane.this.dragAction) != 0) {
	context.setCursor(DragSource.DefaultCopyDrop);
      } else {
	context.setCursor(DragSource.DefaultCopyNoDrop);
      }
    }
    /**
     * @param e the event
     */
    public void dragOver(DragSourceDragEvent e) {
      DragSourceContext context = e.getDragSourceContext();
      int sa = context.getSourceActions();
      int ua = e.getUserAction();
      int da = e.getDropAction();
      int ta = e.getTargetActions();

    }
    /**
     * @param e the event
     */
    public void dragExit(DragSourceEvent e) {
    }

    /**
     * for example, press shift during drag to change to
     * a link action
     * @param e the event
     */
    public void dropActionChanged (DragSourceDragEvent e) {
      DragSourceContext context = e.getDragSourceContext();
      context.setCursor(DragSource.DefaultCopyNoDrop);
    }
  }




  private DragSource dragSource;
  private DragGestureListener dgListener;
  private DragSourceListener dsListener;
  private DropTarget dropTarget;
  private DropTargetListener dtListener;
  private int dragAction = DnDConstants.ACTION_MOVE;
  private int acceptableActions = DnDConstants.ACTION_MOVE;

    public DockingContainer getDockingContainer() {
        return dockingContainer;
    }

    public void setDockingContainer(DockingContainer dockingContainer) {
        this.dockingContainer = dockingContainer;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    //added by Felix Firgau on April 24th 2006
        public void applyI18n(){
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
          jMenuItemMerge.setText(it.businesslogic.ireport.util.I18n.getString("mergePanels","Merge panels"));
          jMenuItemUnmerge.setText(it.businesslogic.ireport.util.I18n.getString("unmergePanel","Seperate panel"));
        }

        public void languageChanged(LanguageChangedEvent evt) {
          applyI18n();
        }
    //Modification end
}
