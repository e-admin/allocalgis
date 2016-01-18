/**
 * ComboRender.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.style.sld.ui.impl;

import java.awt.Component;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Objeto que mostrara la celda de la tabla
 */
public class ComboRender extends DefaultTableCellRenderer implements TableCellRenderer {
    /**
     * Combo que se modificará para mostrar los datos
     */
	JComboBox JComboComponente;
    /**
     * Hash que contiene los keys y los values de los datos del Dominio
     */
	HashMap inValores2;

    /**
     * Constructor
     * @param ModeloCombo 
     * @param inValores 
     */
	public ComboRender(JComboBox ModeloCombo, HashMap inValores) {
		super();
		this.JComboComponente = ModeloCombo;
		this.inValores2 = inValores;
	}

    /**
     * Metodo abstracto de la interfaz
     * @param tabla 
     * @param valor 
     * @param isSelected 
     * @param hasFocus 
     * @param fila 
     * @param columna 
     * @return 
     */
	public Component getTableCellRendererComponent(JTable tabla, Object valor,
			boolean isSelected, boolean hasFocus, int fila, int columna) {

		for (int i = 0; i < (inValores2.size()); i++) {
			Object key = new Object();
			Object valor2 = new Object();
			
			key = ((JComboBox) this.JComboComponente).getModel().getElementAt(i);
			valor2 = inValores2.get(key.toString());
			
			
			String valorString=null;
			if (valor!=null)
				valorString=valor.toString();
			if (valor != null && valorString.equals(valor2)) {
				this.setText(key.toString());
				break;
			}
			if (valor == null && valor2.equals("")) {
				this.setText(key.toString());
				break;
			}
		}

		if (isSelected || hasFocus)
		{
			
		}
		else
		{
			
		}

		Vector vTextoSeleccionado = new Vector();
		vTextoSeleccionado.add(this.getText());
		JComboBox ComboSeleccionado = new JComboBox(vTextoSeleccionado);
		return ComboSeleccionado;
	}
}