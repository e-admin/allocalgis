package com.geopista.app.eiel.models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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
