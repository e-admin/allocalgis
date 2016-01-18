/**
 * IconTableHeaderRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.interventions.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.localgis.app.gestionciudad.dialogs.interventions.images.IconLoader;
import com.vividsolutions.jump.I18N;

public class IconTableHeaderRenderer extends JLabel implements TableCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6173920142751431021L;


	public Component getTableCellRendererComponent(JTable table, 
			Object obj,boolean isSelected, boolean hasFocus, int row, int column) {

		setIcon(IconLoader.icon(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.table.priorityIcon")));

//		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		setHorizontalAlignment(JLabel.CENTER);
		setToolTipText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.table.priority"));
		return this;
	}

}
