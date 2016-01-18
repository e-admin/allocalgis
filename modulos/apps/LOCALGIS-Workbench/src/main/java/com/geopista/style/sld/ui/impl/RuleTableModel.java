/**
 * RuleTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 29-jul-2004
 */
package com.geopista.style.sld.ui.impl;


import javax.swing.table.DefaultTableModel;

import org.deegree.graphics.sld.Symbolizer;

/**
 * @author Enxenio S.L.
 */
public class RuleTableModel extends DefaultTableModel {

	public RuleTableModel() {
		super(new Object[][]{}, new String[] {"Estilo", "Nombre", "Condición"});
	}
	
	public Class getColumnClass(int columnIndex) {
		return types [columnIndex];
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return canEdit [columnIndex];
	}

	Class[] types = new Class [] {Symbolizer.class, java.lang.String.class, java.lang.String.class};
	boolean[] canEdit = new boolean [] {false, false, false};
}
