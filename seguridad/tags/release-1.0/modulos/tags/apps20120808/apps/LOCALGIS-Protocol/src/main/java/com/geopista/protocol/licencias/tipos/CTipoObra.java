package com.geopista.protocol.licencias.tipos;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:05 $
 *          $Name:  $
 *          $RCSfile: CTipoObra.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CTipoObra implements java.io.Serializable{

	private int idTipoObra;
	private String descripcion;
	private String observacion;

	public CTipoObra() {
	}

	public CTipoObra(int idTipoObra, String descripcion, String observacion) {
		this.idTipoObra = idTipoObra;
		this.descripcion = descripcion;
		this.observacion = observacion;
	}

	public int getIdTipoObra() {
		return idTipoObra;
	}

	public void setIdTipoObra(int idTipoObra) {
		this.idTipoObra = idTipoObra;
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
}
