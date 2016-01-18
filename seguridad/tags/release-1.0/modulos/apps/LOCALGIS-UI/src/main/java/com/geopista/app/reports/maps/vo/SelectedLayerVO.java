package com.geopista.app.reports.maps.vo;

public class SelectedLayerVO {
	
	private int idCapa;
	private String nombre;
	private String traduccion;
	
	 
	public SelectedLayerVO() {
		super();
	}

	
	public SelectedLayerVO(String nombre,String traduccion) {
		super();
		this.nombre = nombre;
		this.traduccion= traduccion;
	}

	public int getIdCapa() {
		return idCapa;
	}
	
	public void setIdCapa(int idCapa) {
		this.idCapa = idCapa;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTraduccion() {
		return traduccion;
	}
	
	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
	}
	
    public String toString() {
        return traduccion;
    }
}
