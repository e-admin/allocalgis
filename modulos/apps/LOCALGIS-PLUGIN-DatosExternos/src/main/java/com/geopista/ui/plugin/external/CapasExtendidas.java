/**
 * CapasExtendidas.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;


public class CapasExtendidas {

	int id_layer;
	String nombreTraducido;
	ExternalDataSource externalDS;
	int id_tabla;
	String nombreTabla;
	String fetchQuery;
	
	public CapasExtendidas() {
		super();
	}
	
	public int getId_layer() {
		return id_layer;
	}
	
	public void setId_layer(int id_layer) {
		this.id_layer = id_layer;
	}
	
	public String getNombreTraducido() {
		return nombreTraducido;
	}
	
	public void setNombreTraducido(String nombreTraducido) {
		this.nombreTraducido = nombreTraducido;
	}

	public ExternalDataSource getExternalDS() {
		return externalDS;
	}

	public void setExternalDS(ExternalDataSource externalDS) {
		this.externalDS = externalDS;
	}

	public int getId_tabla() {
		return id_tabla;
	}

	public void setId_tabla(int id_tabla) {
		this.id_tabla = id_tabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getFetchQuery() {
		return fetchQuery;
	}

	public void setFetchQuery(String fetchQuery) {
		this.fetchQuery = fetchQuery;
	}
	
}
