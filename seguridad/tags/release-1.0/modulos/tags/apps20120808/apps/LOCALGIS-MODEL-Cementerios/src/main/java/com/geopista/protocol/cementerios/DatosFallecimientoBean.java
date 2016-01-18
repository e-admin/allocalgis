package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;


/**
 * Clase que implementa un objeto de tipo datosFallecimiento
 */
@SuppressWarnings("serial")
public class DatosFallecimientoBean extends ElemCementerioBean implements Serializable{
	
	private String lugar;
	private String poblacion;
	private String causa_fundamental;
	private String causa_inmediata;
	private Date fecha;
	private String medico;
	private String numColegiado;
	private String Registro_civil;
	private String referencia_fallecimiento;
	
	private int id_datosFallecimiento;
	
	
	public int getId_datosFallecimiento() {
		return id_datosFallecimiento;
	}
	public void setId_datosFallecimiento(int id_datosFallecimiento) {
		this.id_datosFallecimiento = id_datosFallecimiento;
	}
	public String getReferencia_fallecimiento() {
		return referencia_fallecimiento;
	}
	public void setReferencia_fallecimiento(String referencia_fallecimiento) {
		this.referencia_fallecimiento = referencia_fallecimiento;
	}
	public String getRegistro_civil() {
		return Registro_civil;
	}
	public void setRegistro_civil(String registro_civil) {
		Registro_civil = registro_civil;
	}
	public String getMedico() {
		return medico;
	}
	public void setMedico(String medico) {
		this.medico = medico;
	}
	public String getNumColegiado() {
		return numColegiado;
	}
	public void setNumColegiado(String numColegiado) {
		this.numColegiado = numColegiado;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public String getCausa_fundamental() {
		return causa_fundamental;
	}
	public void setCausa_fundamental(String causa_fundamental) {
		this.causa_fundamental = causa_fundamental;
	}
	public String getCausa_inmediata() {
		return causa_inmediata;
	}
	public void setCausa_inmediata(String causa_inmediata) {
		this.causa_inmediata = causa_inmediata;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}
