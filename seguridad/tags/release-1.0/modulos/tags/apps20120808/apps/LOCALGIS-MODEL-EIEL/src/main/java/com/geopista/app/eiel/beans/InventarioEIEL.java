package com.geopista.app.eiel.beans;

import java.io.Serializable;

public class InventarioEIEL extends FilterEIEL implements Serializable{
	
	private String nombreEIEL;
	private String estadoEIEL;
	private String gestionEIEL;
	private String vistaEIEL;
	private String unionClaveEIEL;
	private Integer idBien;
	private Integer epigInventario;
	private String titularidadMunicipal;
	private Integer idMunicipio;
	
	
	public Integer getIdBien() {
		return idBien;
	}
	public void setIdBien(Integer idBien) {
		this.idBien = idBien;
	}
	public Integer getEpigInventario() {
		return epigInventario;
	}
	public void setEpigInventario(Integer epigInventario) {
		this.epigInventario = epigInventario;
	}
	public String getTitularidadMunicipal() {
		return titularidadMunicipal;
	}
	public void setTitularidadMunicipal(String titularidadMunicipal) {
		this.titularidadMunicipal = titularidadMunicipal;
	}
	public Integer getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getUnionClaveEIEL() {
		return unionClaveEIEL;
	}
	public void setUnionClaveEIEL(String unionClaveEIEL) {
		this.unionClaveEIEL = unionClaveEIEL;
	}
	public String getNombreEIEL() {
		return nombreEIEL;
	}
	public void setNombreEIEL(String nombreEIEL) {
		this.nombreEIEL = nombreEIEL;
	}
	public String getEstadoEIEL() {
		return estadoEIEL;
	}
	public void setEstadoEIEL(String estadoEIEL) {
		this.estadoEIEL = estadoEIEL;
	}
	public String getGestionEIEL() {
		return gestionEIEL;
	}
	public void setGestionEIEL(String gestionEIEL) {
		this.gestionEIEL = gestionEIEL;
	}
	public String getVistaEIEL() {
		return vistaEIEL;
	}
	public void setVistaEIEL(String vistaEIEL) {
		this.vistaEIEL = vistaEIEL;
	}
	

}
