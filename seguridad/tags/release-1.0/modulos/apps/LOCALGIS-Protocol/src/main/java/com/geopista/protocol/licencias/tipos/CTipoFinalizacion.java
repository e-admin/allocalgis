package com.geopista.protocol.licencias.tipos;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:05 $
 *          $Name:  $
 *          $RCSfile: CTipoFinalizacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CTipoFinalizacion implements java.io.Serializable {

	private int idFinalizacion;
	private String descripcion;
	private String observacion;

	public CTipoFinalizacion() {
	}

	public CTipoFinalizacion(int idFinalizacion, String descripcion, String observacion) {
		this.idFinalizacion = idFinalizacion;
		this.descripcion = descripcion;
		this.observacion = observacion;
	}


	public int getIdFinalizacion() {
		return idFinalizacion;
	}

	public void setIdFinalizacion(int idFinalizacion) {
		this.idFinalizacion = idFinalizacion;
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
