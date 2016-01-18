/**
 * CheckTreeCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.geopista.server.database.validacion.beans.ValidacionesMPTBean;
import com.geopista.server.database.validacion.beans.ValidacionesPorCuadrosMPTBean;

public class CheckTreeCellRenderer extends JPanel implements TreeCellRenderer{ 
	 private CheckTreeSelectionModel selectionModel; 
	    private TreeCellRenderer delegate; 
	    private TristateCheckBox checkBox = new TristateCheckBox(); 
	 
	    public CheckTreeCellRenderer(TreeCellRenderer delegate, CheckTreeSelectionModel selectionModel){ 
	        this.delegate = delegate; 
	        this.selectionModel = selectionModel; 
	        setLayout(new BorderLayout()); 
	        setOpaque(false); 
	        checkBox.setOpaque(false); 
	    } 
	 
	 
	    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus){ 
	        Component renderer = delegate.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus); 
	        JLabel label = new JLabel();
	        TreePath path = tree.getPathForRow(row); 
	        if(path!=null){ 
	            if(selectionModel.isPathSelected(path, true)) 
	            	
	                checkBox.setState(TristateCheckBox.SELECTED); 
	            else 
	                checkBox.setState(selectionModel.isPartiallySelected(path) ? null : TristateCheckBox.NOT_SELECTED); 
	        } 
	        removeAll(); 
	               
	        
	        if (row>=0)
	        {
	        	DefaultMutableTreeNode node = (DefaultMutableTreeNode)value; 
	        	
	        	if(node.getUserObject() instanceof ValidacionesMPTBean){
	        		ValidacionesMPTBean valMPTBean = (ValidacionesMPTBean)node.getUserObject();
	        		label.setText(valMPTBean.getNombre()+" - ["+ valMPTBean.getTabla()+"]" );
	        	}
	        	else if(node.getUserObject() instanceof ValidacionesPorCuadrosMPTBean){
	        		label.setText(((ValidacionesPorCuadrosMPTBean)node.getUserObject()).getNombre());
	        	}
	        }
	        add(checkBox, BorderLayout.WEST); 

	     
	       // add(renderer, BorderLayout.CENTER); 
	        add(label, BorderLayout.EAST);
	        return this; 
	    } 
}
