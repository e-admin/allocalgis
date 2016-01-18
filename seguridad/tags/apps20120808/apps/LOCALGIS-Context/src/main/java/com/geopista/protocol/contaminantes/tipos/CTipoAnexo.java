package com.geopista.protocol.contaminantes.tipos;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:56 $
 *          $Name:  $
 *          $RCSfile: CTipoAnexo.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CTipoAnexo implements java.io.Serializable{

	private int idTipoAnexo;
	private String descripcion;
	private String observacion;

	public CTipoAnexo() {
	}

	public CTipoAnexo(int idTipoAnexo, String descripcion, String observacion) {
		this.idTipoAnexo = idTipoAnexo;
		this.descripcion = descripcion;
		this.observacion = observacion;
	}

	public int getIdTipoAnexo() {
		return idTipoAnexo;
	}

	public void setIdTipoAnexo(int idTipoAnexo) {
		this.idTipoAnexo = idTipoAnexo;
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
