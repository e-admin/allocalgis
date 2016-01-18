package com.geopista.ui.plugin.external;

import com.geopista.ui.plugin.external.ExternalDataSource;

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
