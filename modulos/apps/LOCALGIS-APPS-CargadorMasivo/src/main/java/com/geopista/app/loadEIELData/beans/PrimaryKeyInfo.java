/**
 * PrimaryKeyInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
