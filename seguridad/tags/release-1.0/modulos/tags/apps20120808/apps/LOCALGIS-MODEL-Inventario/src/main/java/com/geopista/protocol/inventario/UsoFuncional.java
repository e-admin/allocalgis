package com.geopista.protocol.inventario;

import java.util.Date;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 23-ago-2006
 * Time: 10:09:12
 * To change this template use File | Settings | File Templates.
 */
public class UsoFuncional  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id=-1;
    private long idBien=-1;
    private String uso;
    private double superficie=-1;
    private Date fecha;
    private long revision=-1;

    public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
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

    public long getIdBien() {
        return idBien;
    }

    public void setIdBien(long idBien) {
        this.idBien = idBien;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

}
