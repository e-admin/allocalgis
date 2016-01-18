/**
 * NotePageSelectComboBoxRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 *
 */
public class NotePageSelectComboBoxRenderer extends JLabel implements ListCellRenderer {
	
	private static final long serialVersionUID = 3229419536784020216L;
	
	public NotePageSelectComboBoxRenderer() {
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
			  this.setText(Integer.toString(((Integer)value) + 1));			  
		  }
		  
		return this;
	}

}
