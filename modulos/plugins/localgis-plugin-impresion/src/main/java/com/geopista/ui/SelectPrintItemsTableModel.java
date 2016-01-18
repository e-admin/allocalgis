/**
 * SelectPrintItemsTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;


public class SelectPrintItemsTableModel extends DefaultTableModel {

	public static String[] columnNames = {"Sel.", "Valor"};
	
	private List datos = new ArrayList();


	public SelectPrintItemsTableModel() {		
		new DefaultTableModel(columnNames, 0);
	}

	public static void setColumnNames(String[] colNames) {
		columnNames= colNames;
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public boolean isCellEditable(int row, int col) {
		boolean editable = false;
		if (col == 0)       
			editable = true;
		return editable;
	}
	
	
	public List getListaDatos () {
		return this.datos;
	}
	public void clearListaDatos() {
		this.datos.clear();
	}
	public Object getDatos(int pos) {
		return datos.get(pos);
	}
	public void addDatos(Object datos) {
		this.datos.add(datos);
	}
	public Object removeDatos(int pos) {
		Object dato = getDatos(pos);
		this.datos.remove(pos);
		return dato;
	}
	public void updateDatos(int pos, Object datos) {
		this.datos.set(pos, datos);
	}
}
