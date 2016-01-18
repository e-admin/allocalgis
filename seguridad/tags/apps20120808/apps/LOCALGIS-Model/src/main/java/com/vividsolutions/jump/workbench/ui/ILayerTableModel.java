package com.vividsolutions.jump.workbench.ui;

public interface ILayerTableModel {

	public abstract Object getValueAt(int rowIndex);

	public abstract void setValueAt(Object value, int rowIndex);

}