package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 23-ago-2006
 * Time: 10:09:12
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class Aprovechamiento  implements Serializable{
    private long id=-1;
    private long idBien=-1;
    private String aprovechamiento;
    private double superficie=-1;
    private Date fecha;
	private long revision;

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

    public long getIdBien() {
        return idBien;
    }

    public void setIdBien(long idBien) {
        this.idBien = idBien;
    }

    public String getAprovechamiento() {
        return aprovechamiento;
    }

    public void setAprovechamiento(String aprovechamiento) {
        this.aprovechamiento = aprovechamiento;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

	public void setRevision(long revision) {
		this.revision = revision;
		
	}
	public long getRevision(){
		return this.revision;
	}

}
