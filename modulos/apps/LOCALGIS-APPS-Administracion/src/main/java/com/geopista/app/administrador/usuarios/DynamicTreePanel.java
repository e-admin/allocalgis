/**
 * DynamicTreePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @author david.caaveiro
 *
 */
public class DynamicTreePanel extends JPanel {
	
    private JTree tree;

    public DynamicTreePanel(DefaultTreeModel model) {
    	init(model);    	
    }
    
    private void init(DefaultTreeModel model){
    	tree = getTree(model);
    	
    	DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)getTree().getCellRenderer();
    	Icon openIcon = new ImageIcon("src/com/geopista/app/alfresco/ui/image/folder_open.png"); 
    	//Icon leafIcon = new ImageIcon("src/com/geopista/app/alfresco/ui/image/folder_close.png");
    	Icon closedIcon = new ImageIcon("src/com/geopista/app/alfresco/ui/image/folder_close.png"); 
    	renderer.setOpenIcon(openIcon);
    	//renderer.setLeafIcon(leafIcon);
    	renderer.setLeafIcon(openIcon);
    	renderer.setClosedIcon(closedIcon);
    	
        setLayout(new BorderLayout());
        add(getTree(), BorderLayout.CENTER);
    }
    
    private JTree getTree(DefaultTreeModel model) {
    	if(tree == null){
    		tree = new JTree(model) {         	
                public String convertValueToText(Object value, boolean selected,
                                                 boolean expanded, boolean leaf, int row,
                                                 boolean hasFocus) {
                	//if(((TreeNode)value).getAllowsChildren())
                	return ((TreeNode)value).toString();
                }                           
        	};      
        	//invisibilizar directorio raiz
        	tree.setRootVisible( false ); 
        	tree.setRequestFocusEnabled(true);
        	tree.setShowsRootHandles( true );
            tree.putClientProperty( "JTree.lineStyle", "Angled" );
    	}
    	return tree;
    }
    
    public JTree getTree() {    	
    	return tree;
    }
      
}


