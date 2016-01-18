/**
 * DesignDesktop.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.legend;

import javax.swing.JPanel;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class DesignDesktop extends JPanel
{

	/**
	 * 
	 */
	public DesignDesktop() {
		super();
		initialize();
	}
void initialize()
{
	setLayout(null);
}
void add(EditablePanel edPnl)
{
	super.add(edPnl);
}
void BringToFront(EditablePanel edPnl)
{
	remove(edPnl);
	add(edPnl);
}
}
