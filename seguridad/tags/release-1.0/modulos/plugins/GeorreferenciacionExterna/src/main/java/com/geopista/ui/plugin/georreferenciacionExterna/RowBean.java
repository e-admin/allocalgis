package com.geopista.ui.plugin.georreferenciacionExterna;

public class RowBean {
	public ColumnsBean[] getColumns() {
		return columns;
	}
	public void setColumns(ColumnsBean[] columns) {
		this.columns = columns;
	}
	public String getGeometryWkt() {
		return geometryWkt;
	}
	public void setGeometryWkt(String geometryWkt) {
		this.geometryWkt = geometryWkt;
	}
	private ColumnsBean[] columns;
	private String geometryWkt;
}
