package com.geopista.protocol.inventario;

import java.util.Date;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 07-ago-2006
 * Time: 13:05:58
 * To change this template use File | Settings | File Templates.
 */
public class Observacion implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String descripcion;
    Date fecha;
    long id=-1;
    long idBien=-1;
    Date fechaUltimaModificacion;
    long revisionActual = -1;
    
    public Observacion(){
    	
    }
    
    public Observacion(String descripcion){
    	this.fecha=new Date();
    	this.descripcion=descripcion; 
    }

    public long getRevisionActual() {
		return revisionActual;
	}

	public void setRevisionActual(long revisionActual) {
		this.revisionActual = revisionActual;
	}

	public Date getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public long getIdBien() {
        return idBien;
    }

    public void setIdBien(long idBien) {
        this.idBien = idBien;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
