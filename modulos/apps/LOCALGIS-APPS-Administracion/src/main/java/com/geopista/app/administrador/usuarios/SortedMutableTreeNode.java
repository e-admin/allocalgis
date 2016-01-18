/**
 * SortedMutableTreeNode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
