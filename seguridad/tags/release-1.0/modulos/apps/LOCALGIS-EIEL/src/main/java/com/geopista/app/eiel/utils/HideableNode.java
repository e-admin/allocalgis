package com.geopista.app.eiel.utils;



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




