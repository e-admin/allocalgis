/**
 * ColumnsBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.ws.georreferenciaexterna.server.beans;

public class ColumnsBean {

	public String name="";
	public String type="";
	public String value="";
	
	public String getName(){
		return name;
	}
	
	public void setName(String nombre){
		name=nombre;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String tipo){
		type=tipo;
	}
	
	public String getValue(){
		return value;
	}
	
	public void setValue(String valor){
		value=valor;
	}	
}
