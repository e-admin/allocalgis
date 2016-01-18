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


