package com.geopista.protocol.licencias.tipos;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:05 $
 *          $Name:  $
 *          $RCSfile: CTipoTramitacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CTipoTramitacion implements java.io.Serializable{

	private int idTramitacion;
	private String descripcion;
	private String observacion;
	private String plazoEntrega;

	public CTipoTramitacion() {
	}

	public CTipoTramitacion(int idTramitacion, String descripcion, String observacion,  String plazoEntrega) {
		this.idTramitacion = idTramitacion;
		this.descripcion = descripcion;
		this.observacion = observacion;
		this.plazoEntrega = plazoEntrega;
	}


	public int getIdTramitacion() {
		return idTramitacion;
	}

	public void setIdTramitacion(int idTramitacion) {
		this.idTramitacion = idTramitacion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPlazoEntrega() {
		return plazoEntrega;
	}

	public void setPlazoEntrega(String plazoEntrega) {
		this.plazoEntrega = plazoEntrega;
	}




}
