package com.geopista.protocol.licencias.tipos;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:05 $
 *          $Name:  $
 *          $RCSfile: CTipoLicencia.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CTipoLicencia implements java.io.Serializable{

	private int idTipolicencia;
	private String descripcion;
	private String observacion;

	public CTipoLicencia() {
	}

	public CTipoLicencia(int idTipolicencia, String descripcion, String observacion) {
		this.idTipolicencia = idTipolicencia;
		this.descripcion = descripcion;
		this.observacion = observacion;
	}

	public int getIdTipolicencia() {
		return idTipolicencia;
	}

	public void setIdTipolicencia(int idTipolicencia) {
		this.idTipolicencia = idTipolicencia;
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
