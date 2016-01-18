package com.geopista.ui.plugin.routeenginetools.deletenetworksplugin.dialog;

import javax.swing.JTree;

public class DynamicTree {
 
	JTree tree = null;
	
  public DynamicTree(int n) {
    tree = new JTree();   
  }
  
  public boolean addElement(Object element){
	  return true;
  }
}

