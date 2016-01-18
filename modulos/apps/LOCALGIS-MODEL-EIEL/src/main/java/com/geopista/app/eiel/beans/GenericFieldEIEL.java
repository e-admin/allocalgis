/**
 * GenericFieldEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;

public class GenericFieldEIEL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4711286707856974570L;
	String nombrecampo;
	int type; //From CampoFiltro
	
	public GenericFieldEIEL(){
									
	}
	

	public GenericFieldEIEL(String nombrecampo, int type) {
		super();
		this.nombrecampo = nombrecampo;
		this.type = type;
	}


	public String getNombrecampo() {
		return nombrecampo;
	}

	public void setNombrecampo(String nombrecampo) {
		this.nombrecampo = nombrecampo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
	
	

}
