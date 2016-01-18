package com.localgis.app.gestionciudad.dialogs.interventions.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class PriorityComboBoxRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3229419536784020216L;

	public PriorityComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// TODO Auto-generated method stub
		
		  if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }

		  if (value instanceof Integer) {
			  switch ((Integer)value) {
			  case 10:
				  this.setText(value + " - Sin Prioridad");
				  break;
			  case 9:
				  this.setText(value + "  - Prioridad Mínima");
				  break;
			  case 8:
				  this.setText(value + "  - Prioridad Muy Baja");
				  break;
			  case 7:
				  this.setText(value + "  - Prioridad Baja");
				  break;
			  case 6:
				  this.setText(value + "  - Prioridad Media/Baja");
				  break;
			  case 5:
				  this.setText(value + "  - Prioridad Media");
				  break;
			  case 4:
				  this.setText(value + "  - Prioridad Media/Alta");
				  break;
			  case 3:
				  this.setText(value + "  - Prioridad Alta");
				  break;
			  case 2:
				  this.setText(value + "  - Prioridad Muy Alta");
				  break;
			  case 1:
				  this.setText(value + "  - Prioridad Máxima");
				  break;

			  default:
				  break;
			  }
			  if (((Integer)value) == 10){
				 
			  }
			  
		  }
		  
		return this;
	}

	
	

}
