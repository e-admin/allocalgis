package com.geopista.protocol.inventario;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 10-ago-2006
 * Time: 10:23:32
 * To change this template use File | Settings | File Templates.
 */
public class ReferenciaCatastral implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id=-1;
    private String refCatastral;
    private String descripcion;
    private long idBien=-1;
    private long revision=-1;

    public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRefCatastral() {
        return refCatastral;
    }

    public void setRefCatastral(String refCatastral) {
        this.refCatastral = refCatastral;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getIdBien() {
        return idBien;
    }

    public void setIdBien(long idBien) {
        this.idBien = idBien;
    }
}
