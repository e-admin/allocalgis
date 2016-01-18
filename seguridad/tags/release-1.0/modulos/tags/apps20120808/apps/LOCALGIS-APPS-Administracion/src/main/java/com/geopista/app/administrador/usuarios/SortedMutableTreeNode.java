package com.geopista.app.administrador.usuarios;

import java.util.Collections;
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class SortedMutableTreeNode extends DefaultMutableTreeNode{
	
	public SortedMutableTreeNode() {
		super();
	}

	public SortedMutableTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public SortedMutableTreeNode(Object userObject) {
		super(userObject);
	}

	
	public void insert(MutableTreeNode newChild, int childIndex)    {
	     super.insert(newChild, childIndex);
	     Collections.sort(this.children, nodeComparator);
	}
	  
	protected Comparator nodeComparator = new Comparator () {
	     
	     public int compare(Object o1, Object o2) {
	         return o1.toString().compareToIgnoreCase(o2.toString());
	     }
	  
	     
	     @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	     public boolean equals(Object obj)    {
	         return false;
	     }
	  
	     
	     public int hashCode() {
	         int hash = 7;
	         return hash;
	     }
	};
	
}
