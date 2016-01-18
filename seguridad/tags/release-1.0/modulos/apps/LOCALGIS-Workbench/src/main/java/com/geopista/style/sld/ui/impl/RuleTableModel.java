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
