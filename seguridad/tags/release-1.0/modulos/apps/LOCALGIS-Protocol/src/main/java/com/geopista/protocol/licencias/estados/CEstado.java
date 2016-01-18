package com.geopista.protocol.licencias.estados;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:05 $
 *          $Name:  $
 *          $RCSfile: CEstado.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CEstado implements java.io.Serializable{

	private int idEstado;
	private String descripcion;
	private String observacion;
	private int step;
    private long idPermiso= -1;
    private String descPermiso;

	public CEstado() {
	}

    public CEstado(int idEstado)
    {
        this.idEstado=idEstado;
    }
	public CEstado(int idEstado, String descripcion, String observacion, int step) {
		this.idEstado = idEstado;
		this.descripcion = descripcion;
		this.observacion = observacion;
		this.step = step;
	}

	public int getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

    public long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getDescPermiso() {
        return descPermiso;
    }

    public void setDescPermiso(String descripcion) {
        this.descPermiso = descripcion;
    }


}
