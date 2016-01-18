package com.geopista.protocol.licencias;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CReferenciaCatastral.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CReferenciaCatastral implements java.io.Serializable {

	private String referenciaCatastral;
	private String tipoVia;
	private String nombreVia;
	private String primerNumero;
	private String primeraLetra;

	private String bloque;
	private String escalera;
	private String planta;
	private String puerta;
    private Double area;
    private String cpostal;

	public CReferenciaCatastral() {
	}

	public CReferenciaCatastral(String referenciaCatastral, String tipoVia, String nombreVia, String primerNumero, String primeraLetra, String bloque, String escalera, String planta, String puerta) {
		this.referenciaCatastral = referenciaCatastral;
		this.tipoVia = tipoVia;
		this.nombreVia = nombreVia;
		this.primerNumero = primerNumero;
		this.primeraLetra = primeraLetra;
		this.bloque = bloque;
		this.escalera = escalera;
		this.planta = planta;
		this.puerta = puerta;
	}


	public String getReferenciaCatastral() {
		return referenciaCatastral;
	}

	public void setReferenciaCatastral(String referenciaCatastral) {
		this.referenciaCatastral = referenciaCatastral;
	}

	public String getTipoVia() {
		return tipoVia;
	}

	public void setTipoVia(String tipoVia) {
		this.tipoVia = tipoVia;
	}

	public String getNombreVia() {
		return nombreVia;
	}

	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
	}

	public String getPrimerNumero() {
		return primerNumero;
	}

	public void setPrimerNumero(String primerNumero) {
		this.primerNumero = primerNumero;
	}

	public String getPrimeraLetra() {
		return primeraLetra;
	}

	public void setPrimeraLetra(String primeraLetra) {
		this.primeraLetra = primeraLetra;
	}

	public String getBloque() {
		return bloque;
	}

	public void setBloque(String bloque) {
		this.bloque = bloque;
	}

	public String getEscalera() {
		return escalera;
	}

	public void setEscalera(String escalera) {
		this.escalera = escalera;
	}

	public String getPlanta() {
		return planta;
	}

	public void setPlanta(String planta) {
		this.planta = planta;
	}

	public String getPuerta() {
		return puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getCPostal() {
        return cpostal;
    }

    public void setCPostal(String cpostal) {
        this.cpostal = cpostal;
    }



}
