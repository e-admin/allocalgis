/*
 * Created on 29-jul-2004
 */
package com.geopista.style.sld.ui.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.DefaultTableModel;

/**
 * Tabla de Temáticos para estilos SLD
 * @author Enxenio S.L.
 */
public class ThematicTableModel extends DefaultTableModel {
    /**
     * Constructor
     */
	public ThematicTableModel() {
		super();
		setRangeModel(false);
		_isRange = false;
		_styles = new ArrayList();
	}
	
    /**
     * 
     * @param columnIndex 
     * @return 
     */
	public Class getColumnClass(int columnIndex) {
		return types [columnIndex];
	}

    /**
     * 
     * @param rowIndex 
     * @param columnIndex 
     * @return 
     */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return canEdit [columnIndex];
	}
	
    /**
     * 
     * @param isRange 
     */
	public void setRangeModel(boolean isRange) {
		Object[][] dataVector;
		String[] columnNames;
		int rowCount;
		
		_isRange = isRange;
		if (isRange) {
			types = new Class [] {Color.class, java.lang.Object.class, java.lang.Object.class};
			canEdit = new boolean [] {true, true, true};
			columnNames = new String[]{"Color", "Mínimo", "Máximo"};
			setColumnIdentifiers(columnNames);
		}
		else {
			types = new Class [] {Color.class, java.lang.Object.class};
			canEdit = new boolean [] {true, true};
			columnNames = new String[]{"Color", "Valor"};
			setColumnIdentifiers(columnNames); 
		}
	}

    /**
     * 
     * @param data 
     */
	public void addRow(Object[] data) {
		super.addRow(new Object[]{data[0], data[1], data[2]});
		_styles.add(data[3]);
	}

    /**
     * 
     * @param row 
     */
	public void removeRow(int row) {
		super.removeRow(row);
		_styles.remove(row);
	}
	
    /**
     * 
     * @param rowIndex 
     * @return 
     */
	public HashMap getStyle(int rowIndex) {
		return (HashMap)_styles.get(rowIndex);
	}
	
    /**
     * 
     * @return 
     */
	public boolean isCorrect() {
		if (_isRange) 
			return checkValues(1) && checkValues(2);
		else
			return checkValues(1);		
	}
	
	private boolean checkValues(int column) {

		for (int i=0; i< getRowCount(); i++) {
			Object aValue = getValueAt(i,column);
			if (!(aValue instanceof String))
				return false;
			else if (((String)aValue).equals(""))
				return false;  		
		}
		return true;
	}

	private Class[] types;
	private boolean[] canEdit;
	private boolean _isRange;
	private List _styles;

}
