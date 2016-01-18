/**
 * JComboBoxFeatures.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;

import javax.swing.JComboBox;

public class JComboBoxFeatures extends JComboBox {

	public JComboBoxFeatures(Object[] lista,
			java.awt.event.ActionListener accion) {
		this(lista, accion, true);
	}

	public JComboBoxFeatures(Object[] lista,
			java.awt.event.ActionListener accion, boolean conBlanco) {
		super();
		if (lista != null) {
			for (int i = 0; i < lista.length; i++) {
                //GeopistaFeature feature = (GeopistaFeature) lista[i];
                //String nombre = "FID:"+feature.getSystemId();			
				//this.addItem(nombre);
				this.addItem(lista[i]);
			}
		}
	}

	public void setSelected(int id) {
	}

	public Object getSelected() {
		return null;
	}

	public void removeAllItems() {
	}

}
