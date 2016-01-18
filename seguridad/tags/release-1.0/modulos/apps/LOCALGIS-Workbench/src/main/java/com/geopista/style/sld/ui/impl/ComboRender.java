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

			if (valor != null && valor.equals(valor2)) {
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