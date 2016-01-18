package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import java.util.Collection;



import com.geopista.protocol.document.Documentable;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 07-sep-2006
 * Time: 15:38:56
 */
public class BienPreAltaBean implements Serializable {
	private static final long	serialVersionUID	= 3546643200656945977L;
	
    private long id=-1;
    private String nombre;
    private String descripcion;
    private long idMunicipio;
    private long tipo;
    private Date fechaAdquisicion;
    private double costeAdquisicion;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public long getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public void setTipo(long tipo) {
		this.tipo = tipo;
	}
	public long getTipo() {
		return tipo;
	}
	public Date getFechaAdquisicion() {
		return fechaAdquisicion;
	}
	public void setFechaAdquisicion(Date fechaAdquisicion) {
		this.fechaAdquisicion = fechaAdquisicion;
	}
	public double getCosteAdquisicion() {
		return costeAdquisicion;
	}
	public void setCosteAdquisicion(double costeAdquisicion) {
		this.costeAdquisicion = costeAdquisicion;
	}
    
}
