package com.geopista.app.eiel.beans;

public interface IFilterEIEL {

	
	public abstract String getFilterSQL();
	public abstract String getFilterSQLByFeature(Object feature);
	public abstract String getNombreTablaAlfanumerica();
	public abstract String getNombreTablaAlfanumericaUsos();
}
