/**
 * LayerFamilyTreeModelListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layerfamily;


/**
 * Escucha los eventos producidos en el árbol de layerfamilies
 * 
 * @author cotesa
 *
 */

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class LayerFamilyTreeModelListener implements TreeModelListener 
{   
    /**
     * Captura el evento que se lanza al modificar los nodos del árbol
     */
    public void treeNodesChanged(TreeModelEvent e) 
    {
        DefaultMutableTreeNode node;
        node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
        /*
         * If the event lists children, then the changed
         * node is the child of the node we've already
         * gotten.  Otherwise, the changed node and the
         * specified node are the same.
         */
        try 
        {
            int index = e.getChildIndices()[0];
            node = (DefaultMutableTreeNode)(node.getChildAt(index));
        } 
        catch (NullPointerException exc) 
        {
            
        }

    }
    
    public void treeNodesInserted(TreeModelEvent e)
    {
    }
    
    public void treeNodesRemoved(TreeModelEvent e) 
    {
    }
    
    public void treeStructureChanged(TreeModelEvent e) 
    {
    }
}