/**
 * CheckBoxNodeEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.beans;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

public class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {

	  private CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();

	  private ChangeEvent changeEvent = null;

	  private JTree tree;

	  public CheckBoxNodeEditor(JTree tree) {
	    this.tree = tree;
	  }

	  public Object getCellEditorValue() {
	    JCheckBox checkbox = renderer.getLeafRenderer();
	    CheckBoxNode checkBoxNode = new CheckBoxNode(checkbox.getText(),
	        checkbox.isSelected(), checkbox.isEnabled());
	    return checkBoxNode;
	  }

	  public boolean isCellEditable(EventObject event) {
	    boolean returnValue = false;
	    if (event instanceof MouseEvent) {
	      MouseEvent mouseEvent = (MouseEvent) event;
	      TreePath path = tree.getPathForLocation(mouseEvent.getX(),
	          mouseEvent.getY());
	      if (path != null) {
	        Object node = path.getLastPathComponent();
	        if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
	          DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
	          Object userObject = treeNode.getUserObject();
	          if((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode)){
	        	  return ((CheckBoxNode) userObject).isEnabled();
	          }
	        }
	      }
	    }
	    return returnValue;
	  }

	  public Component getTreeCellEditorComponent(JTree tree, Object value,
	      boolean selected, boolean expanded, boolean leaf, int row) {

	    Component editor = renderer.getTreeCellRendererComponent(tree, value,
	        true, expanded, leaf, row, true);

	    // editor always selected / focused
	    ItemListener itemListener = new ItemListener() {
	      public void itemStateChanged(ItemEvent itemEvent) {
	        if (stopCellEditing()) {
	          fireEditingStopped();
	        }
	      }
	    };
	    if (editor instanceof JCheckBox) {
	      ((JCheckBox) editor).addItemListener(itemListener);
	    }

	    return editor;
	  }
	}
