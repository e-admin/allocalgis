package com.geopista.app.reports.maps.vo;

public class SelectedColumnVO {
	
	private String atributo;
	private int idColumna;
	private String nombre;
	private int idTabla;
	private String nombreTabla;
	
	public int getIdColumna() {
		return idColumna;
	}
	
	public void setIdColumna(int idColumna) {
		this.idColumna = idColumna;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getIdTabla() {
		return idTabla;
	}
	
	public void setIdTabla(int idTabla) {
		this.idTabla = idTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}
	
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String toString() {
        return nombre;
    }

	public String getAtributo() {
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
}
