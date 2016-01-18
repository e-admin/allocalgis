package com.geopista.style.sld.ui.impl;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;


/**
 * Clase que implementa el interfaz TableCellEditor para que muestre 
 * celdas con un editor tipo JComboBox
 * @author miguel
 */
public class ComboEditor extends JComboBox implements TableCellEditor {
    /**
     * Componente grafico que se usara como editor
     */
	JComboBox JComboComponente;
    /**
     * Hash con los keys y los values de los Dominios
     */
	HashMap inValores2;

	private LinkedList suscriptores = new LinkedList();

    /**
     * Constructor // Crea un editor para tablas con los valores pasados en el hash
     * @param comboBox 
     * @param inValores 
     */
	public ComboEditor(JComboBox comboBox, HashMap inValores) {
		super(new JComboBox(comboBox.getModel()).getModel());
		this.inValores2 = inValores;
		this.JComboComponente = comboBox;
		this.setEditable(false);
		this.setEnabled(true);

		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				editado(true);
			}
		});

		this.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {;}
			public void focusLost(FocusEvent e) {
				editado(false);
			}
		});

	}


    /**
     * addCellEditorListener
     * @param l 
     */
	public void addCellEditorListener(CellEditorListener l) {
		suscriptores.add(l);
	}


    /**
     * cancelCellEditing
     */
	public void cancelCellEditing() {
	}


    /**
     * Indica si la celda es editable
     * @param anEvent 
     * @return 
     */
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

    /**
     * removeCellEditorListener
     * @param l 
     */
	public void removeCellEditorListener(CellEditorListener l) {
		suscriptores.remove(l);
	}

    /**
     * shouldSelectCell
     * @param anEvent 
     * @return 
     */
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

    /**
     * stopCellEditing
     * @return 
     */
	public boolean stopCellEditing() {
		return false;
	}

    /**
     * editado
     * @param cambiado 
     */
	protected void editado(boolean cambiado) {
		ChangeEvent evento = new ChangeEvent(this);
		int i;
		for (i = 0; i < suscriptores.size(); i++) {
			CellEditorListener aux = (CellEditorListener) suscriptores.get(i);
			if (cambiado) {
				aux.editingStopped(evento);
			} else {
				aux.editingCanceled(evento);
			}
		}

	}

	////
	
    /**
     * devuelve el valor seleccionado del Combo
     * @return 
     */
	public Object getCellEditorValue() {
		Object seleccionado = this.getSelectedItem();

		if ((seleccionado.toString()).equals(null)) {
			return new String(" ");
		} else {
			Object a;
			a = inValores2.get(seleccionado.toString());
			return new String(a.toString());
		}

	}

    /**
     * metodo abstracto del interfaz
     * @param table 
     * @param value 
     * @param isSelected 
     * @param row 
     * @param column 
     * @return 
     */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		
		//this.setEditable(false);
		if (isSelected == false) {
			editado(false);
			return null;
		}

		if (value == null || value.equals("")) {
			this.setSelectedIndex(0);
			return this;
		}

		Object key = new Object();
		Object valor = new Object();
	
		for (int i = 0; i < (inValores2.size()); i++) {
			key = ((JComboBox) this.JComboComponente).getModel().getElementAt(i);
			valor = inValores2.get(key.toString());

			if (value.equals(valor)) {
				this.setEditable(true);
				this.setSelectedItem(key);
				this.setEditable(false);
				return this;
			}
		}
		return this;
	}
}
