package com.geopista.app.eiel.utils;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class HideableTreeModel extends DefaultTreeModel {
    
    protected boolean filterIsActive;
    
    public HideableTreeModel(TreeNode root) {
        this(root, false);
    }
    
    public HideableTreeModel(TreeNode root, boolean asksAllowsChildren) {
        this(root, false, false);
    }
    
    public HideableTreeModel(TreeNode root, boolean asksAllowsChildren
            ,boolean filterIsActive) {
        super(root, asksAllowsChildren);
        this.filterIsActive = filterIsActive;
    }
    
    public void activateFilter(boolean newValue) {
        filterIsActive = newValue;
    }
    
    public boolean isActivatedFilter() {
        return filterIsActive;
    }
    
    public Object getChild(Object parent, int index) {
        if (filterIsActive) {
            if (parent instanceof HideableNode) {
                return ((HideableNode)parent).getChildAt(index,filterIsActive);
            }
        }
        return ((TreeNode)parent).getChildAt(index);
    }
    
    public int getChildCount(Object parent) {
        if (filterIsActive) {
            if (parent instanceof HideableNode) {
                return ((HideableNode)parent).getChildCount(filterIsActive);
            }
        }
        return ((TreeNode)parent).getChildCount();
    }
    
}