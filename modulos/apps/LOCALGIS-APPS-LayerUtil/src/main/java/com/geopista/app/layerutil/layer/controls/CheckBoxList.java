/**
 * CheckBoxList.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.layerutil.layer.controls;


import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CheckBoxList extends JList {
	
  public CheckBoxList(DefaultListModel listModel) {
    super();

    setModel(listModel);
    
    setCellRenderer(new CheckboxCellRenderer());

    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
	        int index = locationToIndex(e.getPoint());
	
	        if (index != -1) {
	          Object obj = getModel().getElementAt(index);
	          if (obj instanceof JCheckBox) {
	            JCheckBox checkbox = (JCheckBox) obj;
	
	            checkbox.setSelected(!checkbox.isSelected());
	            repaint();
	          }
	        }
	      }
	   }
    );    
    
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  @SuppressWarnings("unchecked")
  public java.util.List getCheckedIdexes() {
        java.util.List list = new java.util.ArrayList();
        DefaultListModel dlm = (DefaultListModel) getModel();
        for (int i = 0; i < dlm.size(); ++i) {
          Object obj = getModel().getElementAt(i);
          if (obj instanceof JCheckBox) {
            JCheckBox checkbox = (JCheckBox) obj;
            if (checkbox.isSelected()) {
              list.add(new Integer(i));
            }
          }
        }
    return list;
  }
  
  	@SuppressWarnings("unchecked")
  	public int getNumChecked() {
	  	int numChecked = 0;
	  	DefaultListModel dlm = (DefaultListModel) getModel();
        for (int i = 0; i < dlm.size(); ++i) {
          Object obj = getModel().getElementAt(i);
          if (obj instanceof JCheckBox) {
            JCheckBox checkbox = (JCheckBox) obj;
            if (checkbox.isSelected()) {
            	numChecked++;
            }
          }
        }
        return numChecked;
  	}

  @SuppressWarnings("unchecked")
  public java.util.List getCheckedItems() {
    java.util.List list = new java.util.ArrayList();
    DefaultListModel dlm = (DefaultListModel) getModel();
    for (int i = 0; i < dlm.size(); ++i) {
      Object obj = getModel().getElementAt(i);
      if (obj instanceof JCheckBox) {
        JCheckBox checkbox = (JCheckBox) obj;
        if (checkbox.isSelected()) {
          list.add(checkbox);
        }
      }
    }
    return list;
  }
}

class CheckboxCellRenderer extends DefaultListCellRenderer {
  protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

  public Component getListCellRendererComponent(JList list, Object value, int index,
      boolean isSelected, boolean cellHasFocus) {
    if (value instanceof JCheckBox) {
    	JCheckBox checkbox = (JCheckBox) value;
      checkbox.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
      checkbox.setEnabled(isEnabled());
      checkbox.setFont(getFont());
      checkbox.setFocusPainted(false);
      checkbox.setBorderPainted(true);
      checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder")
          : noFocusBorder);

      return checkbox;
    } else {
      return super.getListCellRendererComponent(list, value.getClass().getName(), index,
          isSelected, cellHasFocus);
    }
  }
}