package com.geopista.app.reports.maps.vo;

public class SelectedMapVO {
	
	int idMap;
	String nombre;
	
	public int getIdMap() {
		return idMap;
	}
	public void setIdMap(int idMap) {
		this.idMap = idMap;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
    public String toString() {
        return this.nombre;
    }

	
}
