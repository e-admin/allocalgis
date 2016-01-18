/**
 * ElementEntity.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg.domain;

public class ElementEntity {
	private String id;
	private String name;
	private String numero;
	private String type;
	private String posX;
	private String posY;
	private String geometria;
	private String county;
	private String town;
	private boolean exactResult;

	
	public ElementEntity() {
		//Empty constructor
		this.numero = "";
	}

	public String getGeometria() {
		return this.geometria;
	}

	public void setGeometria(String geometria) {
		this.geometria = geometria;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosX() {
		return this.posX;
	}

	public void setPosX(String posX) {
		this.posX = posX;
	}

	public String getPosY() {
		return this.posY;
	}

	public void setPosY(String posY) {
		this.posY = posY;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCounty() {
		return this.county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTown() {
		return this.town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String sNumero) {
		this.numero = sNumero;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isExactResult() {
		return exactResult;
	}

	public void setExactResult(boolean exactResult) {
		this.exactResult = exactResult;
	}
}
