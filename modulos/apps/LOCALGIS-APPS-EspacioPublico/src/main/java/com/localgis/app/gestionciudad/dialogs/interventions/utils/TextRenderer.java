/**
 * TextRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.utils;

import java.awt.Component;
import java.awt.TextArea;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * @author javieraragon
 *
 */
public class TextRenderer extends TextArea implements TableCellRenderer{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8375206650209301561L;

	public TextRenderer(){
		super("",2,20,
		TextArea.SCROLLBARS_VERTICAL_ONLY); 
			}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column){
		append((value == null) ? "": value.toString());
		return this;
	}
}