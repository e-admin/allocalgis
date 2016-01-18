package com.geopista.ui.dialogs;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.JComboBox;

import com.geopista.app.document.DocumentPanel;
import com.geopista.feature.GeopistaFeature;

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
