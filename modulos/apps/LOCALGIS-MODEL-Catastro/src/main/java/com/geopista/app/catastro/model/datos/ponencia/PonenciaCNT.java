/**
 * PonenciaCNT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.datos.ponencia;

import java.sql.Date;

public class PonenciaCNT extends Ponencia {

	private String IDentificador;
	private String codPonenciaCNT;

	private String tipoPonencia;
	private Integer anioEfectos;
	private Integer anioAprobacionTotal;
	private Integer anioEfectosTotal;
	private Integer anioNormas;
	private Integer anioCuadroMarco;
	private String aplicFormula;   

	private String estadoPonencia;
	// private String fechaPonencia;
	// private int boe;
	// private String fechaBOP;
	// private int boeIncremento;
	// private String textoLibre;
	// private String fechaCierre;
	// private String sigca;

	private Float infraedificacion;
	private Integer antiguedad;
	private String sinDesarrollar;
	private String ruinoso;

	// private Float incrementoMedio;
	// private Double vCatastralAnterior;
	// private Double vCatastralNuevo;
	// private Float incRustica;

	private String propVertical;
	private Date fechaCierreRevision;

	public Date getFechaCierreRevision() {
		return fechaCierreRevision;
	}

	public void setFechaCierreRevisiona(Date fechaCierreRevision) {
		this.fechaCierreRevision = fechaCierreRevision;
	}

	public String getEstadoPonencia() {
		return estadoPonencia;
	}

	public void setEstadoPonencia(String estadoPonencia) {
		this.estadoPonencia = estadoPonencia;
	}

	/**
	 * @return Returns the anioAprobacionTotal.
	 */
	public Integer getAnioAprobacionTotal() {
		return anioAprobacionTotal;
	}

	/**
	 * @param anioAprobacionTotal
	 *            The anioAprobacionTotal to set.
	 */
	public void setAnioAprobacionTotal(Integer anioAprobacionTotal) {
		this.anioAprobacionTotal = anioAprobacionTotal;
	}

	/**
	 * @return Returns the anioCuadroMarco.
	 */
	public Integer getAnioCuadroMarco() {
		return anioCuadroMarco;
	}

	/**
	 * @param anioCuadroMarco
	 *            The anioCuadroMarco to set.
	 */
	public void setAnioCuadroMarco(Integer anioCuadroMarco) {
		this.anioCuadroMarco = anioCuadroMarco;
	}

	/**
	 * @return Returns the anioEfectos.
	 */
	public Integer getAnioEfectos() {
		return anioEfectos;
	}

	/**
	 * @param anioEfectos
	 *            The anioEfectos to set.
	 */
	public void setAnioEfectos(Integer anioEfectos) {
		this.anioEfectos = anioEfectos;
	}

	/**
	 * @return Returns the anioEfectosTotal.
	 */
	public Integer getAnioEfectosTotal() {
		return anioEfectosTotal;
	}

	/**
	 * @param anioEfectosTotal
	 *            The anioEfectosTotal to set.
	 */
	public void setAnioEfectosTotal(Integer anioEfectosTotal) {
		this.anioEfectosTotal = anioEfectosTotal;
	}

	/**
	 * @return Returns the anioNormas.
	 */
	public Integer getAnioNormas() {
		return anioNormas;
	}

	/**
	 * @param anioNormas
	 *            The anioNormas to set.
	 */
	public void setAnioNormas(Integer anioNormas) {
		this.anioNormas = anioNormas;
	}

	/**
	 * @return Returns the antiguedad.
	 */
	public Integer getAntiguedad() {
		return antiguedad;
	}

	/**
	 * @param antiguedad
	 *            The antiguedad to set.
	 */
	public void setAntiguedad(Integer antiguedad) {
		this.antiguedad = antiguedad;
	}

	/**
	 * @return Returns the aplicFormula.
	 */
	public String getAplicFormula() {
		return aplicFormula;
	}

	/**
	 * @param aplicFormula
	 *            The aplicFormula to set.
	 */
	public void setAplicFormula(String aplicFormula) {
		this.aplicFormula = aplicFormula;
	}

	/**
	 * @return Returns the boe.
	 */
	/*
	 * public int getBoe() { return boe; }
	 */

	/**
	 * @param boe
	 *            The boe to set.
	 */
	/*
	 * public void setBoe(int boe) { this.boe = boe; }
	 */

	/**
	 * @return Returns the boeIncremento.
	 */
	/*
	 * public int getBoeIncremento() { return boeIncremento; }
	 */

	/**
	 * @param boeIncremento
	 *            The boeIncremento to set.
	 */
	/*
	 * public void setBoeIncremento(int boeIncremento) { this.boeIncremento =
	 * boeIncremento; }
	 */

	/**
	 * @return Returns the codPonenciaCNT.
	 */
	public String getCodPonenciaCNT() {
		return codPonenciaCNT;
	}

	/**
	 * @param codPonenciaCNT
	 *            The codPonenciaCNT to set.
	 */
	public void setCodPonenciaCNT(String codPonenciaCNT) {
		this.codPonenciaCNT = codPonenciaCNT;
	}

	/**
	 * @return Returns the estadoPonencia.
	 */
	/*
	 * public String getEstadoPonencia() { return estadoPonencia; }
	 */

	/**
	 * @param estadoPonencia
	 *            The estadoPonencia to set.
	 */
	/*
	 * public void setEstadoPonencia(String estadoPonencia) {
	 * this.estadoPonencia = estadoPonencia; }
	 */

	/**
	 * @return Returns the fechaBOP.
	 */
	/*
	 * public String getFechaBOP() { return fechaBOP; }
	 */

	/**
	 * @param fechaBOP
	 *            The fechaBOP to set.
	 */
	/*
	 * public void setFechaBOP(String fechaBOP) { this.fechaBOP = fechaBOP; }
	 */

	/**
	 * @return Returns the fechaCierre.
	 */
	/*
	 * public String getFechaCierre() { return fechaCierre; }
	 */

	/**
	 * @param fechaCierre
	 *            The fechaCierre to set.
	 */
	/*
	 * public void setFechaCierre(String fechaCierre) { this.fechaCierre =
	 * fechaCierre; }
	 */

	/**
	 * @return Returns the fechaPonencia.
	 */
	/*
	 * public String getFechaPonencia() { return fechaPonencia; }
	 */

	/**
	 * @param fechaPonencia
	 *            The fechaPonencia to set.
	 */
	/*
	 * public void setFechaPonencia(String fechaPonencia) { this.fechaPonencia =
	 * fechaPonencia; }
	 */

	/**
	 * @return Returns the incrementoMedio.
	 */
	/*
	 * public Float getIncrementoMedio() { return incrementoMedio; }
	 */

	/**
	 * @param incrementoMedio
	 *            The incrementoMedio to set.
	 */
	/*
	 * public void setIncrementoMedio(Float incrementoMedio) {
	 * this.incrementoMedio = incrementoMedio; }
	 */

	/**
	 * @return Returns the incRustica.
	 */
	/*
	 * public Float getIncRustica() { return incRustica; }
	 */

	/**
	 * @param incRustica
	 *            The incRustica to set.
	 */
	/*
	 * public void setIncRustica(Float incRustica) { this.incRustica =
	 * incRustica; }
	 */

	/**
	 * @return Returns the infraedificacion.
	 */
	public Float getInfraedificacion() {
		return infraedificacion;
	}

	/**
	 * @param infraedificacion
	 *            The infraedificacion to set.
	 */
	public void setInfraedificacion(Float infraedificacion) {
		this.infraedificacion = infraedificacion;
	}

	/**
	 * @return Returns the propVertical.
	 */
	public String getPropVertical() {
		return propVertical;
	}

	/**
	 * @param propVertical
	 *            The propVertical to set.
	 */
	public void setPropVertical(String propVertical) {
		this.propVertical = propVertical;
	}

	/**
	 * @return Returns the ruinoso.
	 */
	public String getRuinoso() {
		return ruinoso;
	}

	/**
	 * @param ruinoso
	 *            The ruinoso to set.
	 */
	public void setRuinoso(String ruinoso) {
		this.ruinoso = ruinoso;
	}

	/**
	 * @return Returns the sigca.
	 */
	/*
	 * public String getSigca() { return sigca; }
	 */

	/**
	 * @param sigca
	 *            The sigca to set.
	 */
	/*
	 * public void setSigca(String sigca) { this.sigca = sigca; }
	 */

	/**
	 * @return Returns the sinDesarrollar.
	 */
	public String getSinDesarrollar() {
		return sinDesarrollar;
	}

	/**
	 * @param sinDesarrollar
	 *            The sinDesarrollar to set.
	 */
	public void setSinDesarrollar(String sinDesarrollar) {
		this.sinDesarrollar = sinDesarrollar;
	}

	/**
	 * @return Returns the textoLibre.
	 */
	/*
	 * public String getTextoLibre() { return textoLibre; }
	 */

	/**
	 * @param textoLibre
	 *            The textoLibre to set.
	 */
	/*
	 * public void setTextoLibre(String textoLibre) { this.textoLibre =
	 * textoLibre; }
	 */

	/**
	 * @return Returns the tipoPonencia.
	 */
	public String getTipoPonencia() {
		return tipoPonencia;
	}

	/**
	 * @param tipoPonencia
	 *            The tipoPonencia to set.
	 */
	public void setTipoPonencia(String tipoPonencia) {
		this.tipoPonencia = tipoPonencia;
	}

	/**
	 * @return Returns the vCatastralAnterior.
	 */
	/*
	 * public Double getVCatastralAnterior() { return vCatastralAnterior; }
	 */

	/**
	 * @param catastralAnterior
	 *            The vCatastralAnterior to set.
	 */
	/*
	 * public void setVCatastralAnterior(Double catastralAnterior) {
	 * vCatastralAnterior = catastralAnterior; }
	 */

	/**
	 * @return Returns the vCatastralNuevo.
	 */
	/*
	 * public Double getVCatastralNuevo() { return vCatastralNuevo; }
	 */

	/**
	 * @param catastralNuevo
	 *            The vCatastralNuevo to set.
	 */
	/*
	 * public void setVCatastralNuevo(Double catastralNuevo) { vCatastralNuevo =
	 * catastralNuevo; }
	 */

	public PonenciaCNT() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the iDentificador
	 */
	public String getIDentificador() {
		return IDentificador;
	}

	/**
	 * @param dentificador
	 *            the iDentificador to set
	 */
	public void setIDentificador(String dentificador) {
		IDentificador = dentificador;
	}

}
