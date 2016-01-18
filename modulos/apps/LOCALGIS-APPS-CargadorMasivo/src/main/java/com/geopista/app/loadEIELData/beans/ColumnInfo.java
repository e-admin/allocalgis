/**
 * ColumnInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
