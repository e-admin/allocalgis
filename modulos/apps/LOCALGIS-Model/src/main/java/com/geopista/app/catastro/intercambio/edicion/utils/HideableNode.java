/**
 * HideableNode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.utils;
/*Example modified from http://www.crionics.com/products/opensource/faq/swing_ex/JTableExamples2.html
 */


import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class HideableNode extends DefaultMutableTreeNode {
    
    protected boolean isVisible;
    
    public HideableNode() {
        this(null);
    }
    
    public HideableNode(Object userObject) {
        this(userObject, true, true);
    }
    
    public HideableNode(Object userObject, boolean allowsChildren
            , boolean isVisible) {
        super(userObject, allowsChildren);
        this.isVisible = isVisible;
    }
    
    public TreeNode getChildAt(int index,boolean filterIsActive) {
        if (! filterIsActive) {
            return super.getChildAt(index);
        }
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        
        int realIndex = -1;
        int visibleIndex = -1;
        Enumeration menum = children.elements();
        while (menum.hasMoreElements()) {
            HideableNode node = (HideableNode)menum.nextElement();
            if (node.isVisible()) {
                visibleIndex++;
            }
            realIndex++;
            if (visibleIndex == index) {
                return (TreeNode)children.elementAt(realIndex);
            }
        }
        
        throw new ArrayIndexOutOfBoundsException("index unmatched");
        
    }
    
    public int getChildCount(boolean filterIsActive) {
        if (! filterIsActive) {
            return super.getChildCount();
        }
        if (children == null) {
            return 0;
        }
        
        int count = 0;
        Enumeration menum = children.elements();
        while (menum.hasMoreElements()) {
            HideableNode node = (HideableNode)menum.nextElement();
            if (node.isVisible()) {
                count++;
            }
        }
        
        return count;
    }
    
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
}




