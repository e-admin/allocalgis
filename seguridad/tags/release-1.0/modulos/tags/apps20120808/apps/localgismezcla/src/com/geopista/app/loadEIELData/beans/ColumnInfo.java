package com.geopista.app.loadEIELData.beans;

public class ColumnInfo {

	String originName;
	String destinyName;
	String value;
	String type;
	String characteristic;
		
	public ColumnInfo() {
		super();
	}
	
	public ColumnInfo(String originName, String destinyName, String value, String type,
			String characteristic) {
		super();
		this.originName = originName;
		this.destinyName = destinyName;
		this.value = value;
		this.type = type;
		this.characteristic = characteristic;
	}
	
	public ColumnInfo(ColumnInfo column) {
		super();
		this.originName = column.getOriginName().toString();
		this.destinyName = column.getDestinyName().toString();
		this.value = column.getValue().toString();
		this.type = column.getType().toString();
		this.characteristic = column.getCharacteristic().toString();
	}
	
	public String getOriginName() {
		return originName;
	}
	
	public void setOriginName(String originName) {
		this.originName = originName;
	}
		
	public String getDestinyName() {
		return destinyName;
	}

	public void setDestinyName(String destinyName) {
		this.destinyName = destinyName;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCharacteristic() {
		return characteristic;
	}
	
	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}

	@Override
	public String toString() {
		return "{" + getOriginName() + "," + getDestinyName() + "," + getValue() + "," + getType() + "," + getCharacteristic() + "}";
	}	
	
}
