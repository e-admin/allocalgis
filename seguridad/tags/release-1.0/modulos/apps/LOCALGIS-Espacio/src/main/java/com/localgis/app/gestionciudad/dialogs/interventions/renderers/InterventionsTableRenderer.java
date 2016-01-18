package com.localgis.app.gestionciudad.dialogs.interventions.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class InterventionsTableRenderer extends JLabel implements TableCellRenderer {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3680604489867056896L;

	
	public InterventionsTableRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
		
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}
		if(value instanceof Integer){
			if ((Integer)value == 1){
				setBackground(Color.RED);
			}else if((Integer)value < 5){
				setBackground(Color.ORANGE);
			}
		}
		
		this.setText(value.toString());
		
		return this;
	}

}
