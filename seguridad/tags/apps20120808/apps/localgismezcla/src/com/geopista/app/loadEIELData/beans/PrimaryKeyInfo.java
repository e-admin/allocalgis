package com.geopista.app.loadEIELData.beans;

public class PrimaryKeyInfo {

	String name;
	String characteristic;
		
	public PrimaryKeyInfo() {
		super();
	}
	
	public PrimaryKeyInfo(String name, String characteristic) {
		super();
		this.name = name;
		this.characteristic = characteristic;
	}
	
	public PrimaryKeyInfo(PrimaryKeyInfo primaryKey) {
		super();
		this.name = primaryKey.getName().toString();
		this.characteristic = primaryKey.getCharacteristic().toString();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}
	
	@Override
	public String toString() {
		return "{" + getName() + "," + getCharacteristic() + "}";
	}	
	
}
