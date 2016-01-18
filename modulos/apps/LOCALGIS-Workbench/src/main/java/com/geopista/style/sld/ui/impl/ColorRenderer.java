/**
 * ColorRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/* 
 * ColorRenderer.java (compiles with releases 1.2, 1.3, and 1.4) is used by 
 * TableDialogEditDemo.java.
 */
package com.geopista.style.sld.ui.impl;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

/**
 * @author Enxenio S.L.
 */
public class ColorRenderer extends JLabel
						   implements TableCellRenderer {
	Border unselectedBorder = null;
	Border selectedBorder = null;
	boolean isBordered = true;

	public ColorRenderer(boolean isBordered) {
		this.isBordered = isBordered;
		setOpaque(true); //MUST do this for background to show up.
	}

	public Component getTableCellRendererComponent(
							JTable table, Object color,
							boolean isSelected, boolean hasFocus,
							int row, int column) {
		Color newColor = (Color)color;
		setBackground(newColor);
		if (isBordered) {
			if (isSelected) {
				if (selectedBorder == null) {
					selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
											  table.getSelectionBackground());
				}
				setBorder(selectedBorder);
			} else {
				if (unselectedBorder == null) {
					unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
											  table.getBackground());
				}
				setBorder(unselectedBorder);
			}
		}
        
		setToolTipText("RGB value: " + newColor.getRed() + ", "
									 + newColor.getGreen() + ", "
									 + newColor.getBlue());
		return this;
	}
}