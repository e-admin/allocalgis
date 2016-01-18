package com.geopista.ui.plugin.external;

public class Query {

	private int dataSourceId;
	private int queryId;
	private String name;
	private String text;
	private String internalLayer;
	private String internalAttribute;
	private String externalTable;
	private String externalColumn;
	
	
	public int getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(int dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public int getQueryId() {
		return queryId;
	}
	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getInternalLayer() {
		return internalLayer;
	}
	public void setInternalLayer(String internalLayer) {
		this.internalLayer = internalLayer;
	}
	public String getInternalAttribute() {
		return internalAttribute;
	}
	public void setInternalAttribute(String internalAttribute) {
		this.internalAttribute = internalAttribute;
	}
	public String getExternalTable() {
		return externalTable;
	}
	public void setExternalTable(String externalTable) {
		this.externalTable = externalTable;
	}
	public String getExternalColumn() {
		return externalColumn;
	}
	public void setExternalColumn(String externalColumn) {
		this.externalColumn = externalColumn;
	}


}
