package com.geopista.protocol.licencias;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CViaNotificacion.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CViaNotificacion implements java.io.Serializable{

	private int idViaNotifiacion;
	private String observacion;

	public CViaNotificacion() {
	}

	public CViaNotificacion(int idViaNotifiacion, String observacion) {
		this.idViaNotifiacion = idViaNotifiacion;
		this.observacion = observacion;
	}


	public int getIdViaNotificacion() {
		return idViaNotifiacion;
	}

	public void setIdViaNotificacion(int idViaNotifiacion) {
		this.idViaNotifiacion = idViaNotifiacion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
}
