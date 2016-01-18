package com.geopista.app.layerutil.layer.controls;
import it.businesslogic.ireport.gui.fonts.CheckBoxListEntry;

import java.awt.Color;
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