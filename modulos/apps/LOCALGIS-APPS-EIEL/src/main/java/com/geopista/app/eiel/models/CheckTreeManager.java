/**
 * CheckTreeManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class CheckTreeManager extends MouseAdapter implements TreeSelectionListener{  
	   private CheckTreeSelectionModel selectionModel; 
	    private JTree tree = new JTree(); 
	    int hotspot = new JCheckBox().getPreferredSize().width; 
	 
	    public CheckTreeManager(JTree tree){ 
	        this.tree = tree; 
	        selectionModel = new CheckTreeSelectionModel(tree.getModel()); 
	        tree.setCellRenderer(new CheckTreeCellRenderer(tree.getCellRenderer(), selectionModel)); 
	        tree.addMouseListener(this); 
	        selectionModel.addTreeSelectionListener(this); 
	    } 
	 
	    public void mouseClicked(MouseEvent me){ 
	        TreePath path = tree.getPathForLocation(me.getX(), me.getY()); 
	        if(path==null) 
	            return; 
	        if(me.getX()>tree.getPathBounds(path).x+hotspot) 
	            return; 
	 
	        boolean selected = selectionModel.isPathSelected(path, true); 
	        selectionModel.removeTreeSelectionListener(this); 
	 
	        try{ 
	            if(selected) 
	                selectionModel.removeSelectionPath(path); 
	            else 
	                selectionModel.addSelectionPath(path); 
	        } finally{ 
	            selectionModel.addTreeSelectionListener(this); 
	            tree.treeDidChange(); 
	        } 
	    } 
	 
	    public CheckTreeSelectionModel getSelectionModel(){ 
	        return selectionModel; 
	    } 
	 
	    public void valueChanged(TreeSelectionEvent e){ 
	        tree.treeDidChange(); 
	    } 
}
