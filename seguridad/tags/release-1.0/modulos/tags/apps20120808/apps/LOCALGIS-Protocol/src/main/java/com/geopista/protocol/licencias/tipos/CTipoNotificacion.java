package com.geopista.protocol.licencias.tipos;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:05 $
 *          $Name:  $
 *          $RCSfile: CTipoNotificacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CTipoNotificacion implements java.io.Serializable {

	private int idTipoNotificacion;
	private String descripcion;
	private String observacion;

	public CTipoNotificacion() {
	}

	public CTipoNotificacion(int idTipoNotificacion, String descripcion, String observacion) {
		this.idTipoNotificacion = idTipoNotificacion;
		this.descripcion = descripcion;
		this.observacion = observacion;
	}


	public int getIdTipoNotificacion() {
		return idTipoNotificacion;
	}

	public void setIdTipoNotificacion(int idTipoNotificacion) {
		this.idTipoNotificacion = idTipoNotificacion;
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
