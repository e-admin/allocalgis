/**
 * HideableTreeModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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