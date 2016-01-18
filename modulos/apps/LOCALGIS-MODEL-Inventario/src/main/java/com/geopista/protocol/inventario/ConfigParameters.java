/**
 * ConfigParameters.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

public class ConfigParameters {
	
	
	public static int DEFAULT_LIMIT=20;
	public static int MAX_LIMIT=20;
	private int limit=-1;
	private int offset=0;
	private boolean busquedaIndividual=false;
	
	public void setLimit(int limit) {
		this.limit=limit;		
	}
	public int getLimit(){
		return limit;
	}
	
	public void setOffset(int offset) {
		this.offset=offset;		
	}
	
	public int getOffset(){
		return offset;
	}
	public void setBusquedaIndividual(boolean busquedaIndividual) {
		this.busquedaIndividual = busquedaIndividual;
	}
	public boolean isBusquedaIndividual() {
		return busquedaIndividual;
	}

}
