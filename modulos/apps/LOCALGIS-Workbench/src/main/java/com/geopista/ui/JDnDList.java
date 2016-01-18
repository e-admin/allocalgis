/**
 * JDnDList.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class JDnDList extends JList 
implements DragSourceListener, 
   DragGestureListener,
      DropTargetListener
{
    
    
    int overIndex ;
    boolean dragging; 
    int[] selectedIndicies;
    DragSource dragSource;
    DropTarget dropTarget;
    private LayerNameTabbedPanel panel;
    
public JDnDList( DnDListModel model, LayerNameTabbedPanel panel )
{
super( model );
this.panel=panel;
// Configure ourselves to be a drag source
  dragSource = new DragSource();
  dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_MOVE, this);

// Configure ourselves to be a drop target
  dropTarget = new DropTarget( this, this );
  
  
  this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
}



public void dragEnter(DragSourceDragEvent dsde)
{
    // TODO Auto-generated method stub
    
}

public void dragExit(DragSourceEvent dse)
{
    // TODO Auto-generated method stub
    
}

public void dragOver(DragSourceDragEvent dsde)
{
    // TODO Auto-generated method stub
    
}

public void dropActionChanged(DragSourceDragEvent dsde)
{
    // TODO Auto-generated method stub
    
}

public void dragGestureRecognized(DragGestureEvent dge) 
{
  this.selectedIndicies = this.getSelectedIndices();
Object[] selectedObjects = this.getSelectedValues();
  if( selectedObjects.length > 0 )
  {
 StringBuffer sb = new StringBuffer();
 for( int i=0; i<selectedObjects.length; i++ ) {
 sb.append( selectedObjects[ i ].toString() + "\n" );
 }

    // Build a StringSelection object that the Drag Source
    // can use to transport a string to the Drop Target
    StringSelection text = new StringSelection( sb.toString() ); 

    // Start dragging the object 
    this.dragging = true;
    dragSource.startDrag( dge, DragSource.DefaultMoveDrop, text, this );
  }
}


public void dragExit(DropTargetEvent dte) {
    this.overIndex = -1;
  }
  public void dragEnter(DropTargetDragEvent dtde) {
      if(this.dragging==false)
      {
      dtde.rejectDrag();
      return;
      }
      this.overIndex = this.locationToIndex( dtde.getLocation() );
    this.setSelectedIndex( this.overIndex );
  }
  public void dragOver(DropTargetDragEvent dtde) {
      if(this.dragging==false)
          {
          dtde.rejectDrag();
          return;
          }
    // See who we are over...
    int overIndex = this.locationToIndex( dtde.getLocation() );
    if( overIndex != -1 && overIndex != this.overIndex )
    {
      // If the value has changed from what we were previously over
      // then change the selected object to the one we are over; this 
      // is a visual representation that this is where the drop will occur
      this.overIndex = overIndex;
      this.setSelectedIndex( this.overIndex );
    }
  }

  public void dragDropEnd(DragSourceDropEvent dsde) {
      this.dragging = false;
    }

public void drop(DropTargetDropEvent dtde) {
    try 
    {
      Transferable transferable = dtde.getTransferable();
      if( transferable.isDataFlavorSupported( DataFlavor.stringFlavor ) )
      {
        dtde.acceptDrop( DnDConstants.ACTION_MOVE );

   // Find out where the item was dropped
   int newIndex = this.locationToIndex( dtde.getLocation() );

   // Get the items out of the transferable object and build an
   // array out of them...
        String s = ( String )transferable.getTransferData( DataFlavor.stringFlavor );
   //StringTokenizer st = new StringTokenizer( s );
   ArrayList items = new ArrayList();
   items.add(s);
   /*while( st.hasMoreTokens() )
   {
     items.add( st.nextToken() );
   }*/
   DnDListModel model = ( DnDListModel )this.getModel();

        // If we are dragging from our this to our list them move the items,
        // otherwise just add them...
        if( this.dragging )
        {
          //model.itemsMoved( newIndex, items );
          model.itemsMoved( newIndex, this.selectedIndicies);
          int oldindex=this.selectedIndicies[0]; //OJO
          panel.treeLayerNamePanel.getLayerManager().movePositionLayer(oldindex,newIndex);
        }
        else
        {
       //   model.insertItems( newIndex, items );
            throw new UnsupportedOperationException("No se puede arrastrar desde mapas distintos.");
        }
       
   // Update the selected indicies
   int[] newIndicies = new int[ items.size() ];
   for( int i=0; i<items.size(); i++ )
   {
     newIndicies[ i ] = newIndex + i;
   }
   this.clearSelection();
   this.setSelectedIndices( newIndicies );

        // Reset the over index
        this.overIndex = -1;

        dtde.getDropTargetContext().dropComplete( true );
        
     
      }
      else
      {
        dtde.rejectDrop();
      }
    }
    catch( IOException exception )
    {
      exception.printStackTrace();
      System.err.println( "Exception" + exception.getMessage());
      dtde.rejectDrop();
    }
    catch( UnsupportedFlavorException ufException )
    {
      ufException.printStackTrace();
      System.err.println( "Exception" + ufException.getMessage());
      dtde.rejectDrop();
    }
  }

public void dropActionChanged(DropTargetDragEvent dtde)
{
    // TODO Auto-generated method stub
    
}


}
