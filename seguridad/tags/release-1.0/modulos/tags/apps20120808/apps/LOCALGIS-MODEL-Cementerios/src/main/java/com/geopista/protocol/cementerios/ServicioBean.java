package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;


/**
 * Clase que implementa un objeto de tipo Servicio
 */
public class ServicioBean extends ElemCementerioBean implements Serializable{
	
	private String consideracion;
	private Date plazo_concesion;
	private int cuota;
	private int canon_mantenimiento;
	private int estado;
	private Date fecha_modificado;
	
	
	public String getConsideracion() {
		return consideracion;
	}
	public void setConsideracion(String consideracion) {
		this.consideracion = consideracion;
	}
	public Date getPlazo_concesion() {
		return plazo_concesion;
	}
	public void setPlazo_concesion(Date plazo_concesion) {
		this.plazo_concesion = plazo_concesion;
	}
	public int getCuota() {
		return cuota;
	}
	public void setCuota(int cuota) {
		this.cuota = cuota;
	}
	public int getCanon_mantenimiento() {
		return canon_mantenimiento;
	}
	public void setCanon_mantenimiento(int canon_mantenimiento) {
		this.canon_mantenimiento = canon_mantenimiento;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public Date getFecha_modificado() {
		return fecha_modificado;
	}
	public void setFecha_modificado(Date fecha_modificado) {
		this.fecha_modificado = fecha_modificado;
	}

}
