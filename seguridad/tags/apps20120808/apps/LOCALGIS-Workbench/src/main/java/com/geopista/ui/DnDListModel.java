package com.geopista.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;

import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;

public class DnDListModel extends DefaultListModel
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1204091898678043046L;
    Vector items = new Vector();
    /**
     * Inserts a collection of items before the specified index
     */
    public void insertItems( int index, Collection objects )
    {
      // Handle the case where the items are being added to the end of the list
      if( index == -1 )
      {
        // Add the items
        for( Iterator i = objects.iterator(); i.hasNext(); )
        {
          Object item = i.next();
          addItem( item );
        }
//      Tell the list to update itself
        this.fireContentsChanged( this, this.items.size() - 1-objects.size() , this.items.size() - 1 );
      }
      else
      {
          int index0=index;
        // Insert the items
        for( Iterator i = objects.iterator(); i.hasNext(); )
        {
          Object item = i.next();
          add( index++, item );
        }
//      Tell the list to update itself
        this.fireContentsChanged( this, index0 , index - 1 );
    
      }
      
      

   
    }
    
    public void fireContentsChanged(Object source,
            int index0,
            int index1)
    {
        super.fireContentsChanged(source,index0,index1);
    }
    
    
    private void addItem(Object item)
    {
        items.add(item);
    }
    
    public void add(int index, Object item)
    {
        items.insertElementAt(item, index);
        fireIntervalAdded(this, index, index);
    }

    public void itemsMoved( int newIndex, int[] indicies )
    {
      // Copy the objects to a temporary ArrayList
      ArrayList objects = new ArrayList();
      for( int i=0; i<indicies.length; i++ )
      {
        objects.add( this.items.get( indicies[ i ] ) );
      }

      // Delete the objects from the list
      for( int i=indicies.length-1; i>=0; i-- )
      {
        this.items.remove( indicies[ i ] );
      }

      // Insert the items at the new location
      insertItems( newIndex, objects );
    }

    public Object getElementAt(int index)
    {
        return items.elementAt(index);
    }

    public int getSize()
    {
        return items.size();
    }

    public boolean removeElement(Object item)
    {
        int index = indexOf(item);
        boolean rv = items.removeElement(item);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return rv;
        
    }

    public int indexOf(Object item)
    {
        int index=0;
     for (Iterator iter = items.iterator(); iter.hasNext();)
    {
         Layerable layer = (Layerable) iter.next();
        if (layer==item)  return index;
        index++;
    }
     return -1;
    }

    
}
