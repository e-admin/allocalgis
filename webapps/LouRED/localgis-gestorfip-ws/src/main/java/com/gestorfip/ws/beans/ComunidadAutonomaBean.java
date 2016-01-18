package com.gestorfip.ws.beans;

/**
 * @author davidriou
 * Bean para representar una comunidad autonoma, conteniendo:
 * - Nombre
 * - Id
 */
public class ComunidadAutonomaBean {

	String nombreCA;
	int idCA;

	public String getNombreCA() {
		return nombreCA;
	}

	public void setNombreCA(String nombreCA) {
		this.nombreCA = nombreCA;
	}

	public int getIdCA() {
		return idCA;
	}

	public void setIdCA(int idCA) {
		this.idCA = idCA;
	}

}
