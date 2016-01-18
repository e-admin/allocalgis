/**
 * OutLineNode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.deletenetworksplugin.dialog;

import javax.swing.tree.*;

public class OutLineNode extends DefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4414106201005087635L;

	private boolean areChildrenDefined = false;
	private int outlineNum;
	private int numChildren;

	public OutLineNode(int outlineNum, int numChildren) {
		this.outlineNum = outlineNum;
		this.numChildren = numChildren;
	}

	public boolean isLeaf() {
		return(false);
	}

	public int getChildCount() {
		if (!areChildrenDefined)
			defineChildNodes();
		return(super.getChildCount());
	}

	private void defineChildNodes() {
		// You must set the flag before defining children if you
		// use "add" for the new children. Otherwise you get an infinite
		// recursive loop, since add results in a call to getChildCount.
		// However, you could use "insert" in such a case.
		areChildrenDefined = true;
		for(int i=0; i<numChildren; i++)
			add(new OutLineNode(i+1, numChildren));
	}

	public String toString() {
		TreeNode parent = getParent();
		if (parent == null)
			return(String.valueOf(outlineNum));
		else
			return(parent.toString() + "." + outlineNum);
	}
}
