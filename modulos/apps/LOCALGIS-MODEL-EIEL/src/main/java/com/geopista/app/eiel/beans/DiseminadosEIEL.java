/**
 * DiseminadosEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class DiseminadosEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public DiseminadosEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_mun_diseminados","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_mun_diseminados","getCodINEMunicipio"));
	}	

	public Hashtable getIdentifyFields() {		
		Hashtable fields = new Hashtable();		
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);				
		return fields;
	}

	
	private Integer padron = null;
	private Integer poblacionEstacional = null;
	private Integer plazasHoteleras = null;
	private Integer plazasCasasRurales = null;
	private Integer viviendasTotales = null;
	
	private Integer estadoRevision = null;
	private Date fechaRevision = null;
	/* Abastecimiento */
	private Integer consumoInvierno = null;
	private Integer consumoVerano = null;
	private Integer fuentesControladas = null;
	private Integer fuentesNoControladas = null;
	private Integer longitudAbastecimiento = null;
	private Integer longDeficitariaAbast = null;
	private Integer viviendasExcesoPresion = null;
	private Integer viviendasDefectoPresion = null;
	private Integer viviendasConAbastecimiento = null;
	private Integer viviendasSinAbastecimiento = null;
	private Integer viviendasDeficitAbast = null;
	private Integer poblacionResidenteDefAbast = null;
	private Integer poblacionEstacionalDefAbast = null;
	private Integer vivendasAbastecimientoAuto = null;
	private Integer poblacionResidenteAbastAuto = null;
	private Integer poblacionEstacionalAbastAuto = null;
	private Integer viviendasDefAbastAuto = null;
	private Integer poblacionResidenteDefAbastAuto = null;
	private Integer poblacionEstacionalDefAbastAuto = null;
	/* Saneamiento */
	private Integer caudalDesaguado = null;
	private Integer caudalTratado = null;
	private Integer longitudSaneamiento = null;
	private Integer longDeficitariaSaneam = null;
	private Integer viviendasConSaneamiento = null;
	private Integer viviendasSinSaneamiento = null;
	private Integer viviendasDefSaneam = null;
	private Integer poblacionEstacionalDefSaneam = null;
	private Integer poblacionResidenteDefSaneam = null;	
	private Integer viviendasSaneamientoAuto = null;
	private Integer poblacionEstacionalSaneamAuto = null;
	private Integer poblacionResidenteSaneamAuto = null;
	private Integer viviendasDeficitSaneamAuto = null;
	private Integer poblacionResidenteDefSaneamAuto = null;
	private Integer poblacionEstacionalDefSaneamAuto = null;
	/* Recogida de Basura */
	private Float TmBasura = null;
	private Integer contenedores = null;
	private Integer viviendasSinBasura = null;
	private Integer poblacionResidenteSinBasura = null;
	private Integer poblacionEstacionalSinBasura = null;
	private Integer plantillaLimpieza = null;
	/* Alumbrado */
	private Integer puntosLuz = null;
	private Integer viviendasSinAlumbrado = null;
	private Integer longDeficitariaAlumbrado = null;


	private VersionEiel version = null;
	
	public String getCodINEProvincia() {
		return codINEProvincia;
	}

	public void setCodINEProvincia(String codINEProvincia) {
		this.codINEProvincia = codINEProvincia;
	}

	public String getCodINEMunicipio() {
		return codINEMunicipio;
	}

	public void setCodINEMunicipio(String codINEMunicipio) {
		this.codINEMunicipio = codINEMunicipio;
	}

	public Integer getPadron() {
		return padron;
	}

	public void setPadron(Integer padron) {
		this.padron = padron;
	}

	public Integer getPoblacionEstacional() {
		return poblacionEstacional;
	}

	public void setPoblacionEstacional(Integer poblacionEstacional) {
		this.poblacionEstacional = poblacionEstacional;
	}

	public Integer getPlazasHoteleras() {
		return plazasHoteleras;
	}

	public void setPlazasHoteleras(Integer plazasHoteleras) {
		this.plazasHoteleras = plazasHoteleras;
	}

	public Integer getPlazasCasasRurales() {
		return plazasCasasRurales;
	}

	public void setPlazasCasasRurales(Integer plazasCasasRurales) {
		this.plazasCasasRurales = plazasCasasRurales;
	}

	public Integer getViviendasTotales() {
		return viviendasTotales;
	}

	public void setViviendasTotales(Integer viviendasTotales) {
		this.viviendasTotales = viviendasTotales;
	}

	public Integer getEstado() {
		return estadoRevision;
	}

	public void setEstado(Integer estado) {
		estadoRevision = estado;
	}

	public Date getFecha() {
		return fechaRevision;
	}

	public void setFecha(Date fecha) {
		fechaRevision = fecha;
	}

	public Integer getConsumoInvierno() {
		return consumoInvierno;
	}

	public void setConsumoInvierno(Integer consumoInvierno) {
		this.consumoInvierno = consumoInvierno;
	}

	public Integer getConsumoVerano() {
		return consumoVerano;
	}

	public void setConsumoVerano(Integer consumoVerano) {
		this.consumoVerano = consumoVerano;
	}

	public Integer getFuentesNoControladas() {
		return fuentesNoControladas;
	}

	public void setFuentesNoControladas(Integer fuentesNoControladas) {
		this.fuentesNoControladas = fuentesNoControladas;
	}

	public Integer getLongitudAbastecimiento() {
		return longitudAbastecimiento;
	}

	public void setLongitudAbastecimiento(Integer longitudAbastecimiento) {
		this.longitudAbastecimiento = longitudAbastecimiento;
	}

	public Integer getLongDeficitariaAbast() {
		return longDeficitariaAbast;
	}

	public void setLongDeficitariaAbast(Integer longDeficitariaAbast) {
		this.longDeficitariaAbast = longDeficitariaAbast;
	}

	public Integer getViviendasExcesoPresion() {
		return viviendasExcesoPresion;
	}

	public void setViviendasExcesoPresion(Integer viviendasExcesoPresion) {
		this.viviendasExcesoPresion = viviendasExcesoPresion;
	}

	public Integer getViviendasDefectoPresion() {
		return viviendasDefectoPresion;
	}

	public void setViviendasDefectoPresion(Integer viviendasDefectoPresion) {
		this.viviendasDefectoPresion = viviendasDefectoPresion;
	}

	public Integer getViviendasConAbastecimiento() {
		return viviendasConAbastecimiento;
	}

	public void setViviendasConAbastecimiento(Integer viviendasConAbastecimiento) {
		this.viviendasConAbastecimiento = viviendasConAbastecimiento;
	}

	public Integer getViviendasSinAbastecimiento() {
		return viviendasSinAbastecimiento;
	}
	
	public Integer getContenedores() {
		return contenedores;
	}

	public void setContenedores(Integer contenedores) {
		this.contenedores = contenedores;
	}

	public void setViviendasSinAbastecimiento(Integer viviendasSinAbastecimiento) {
		this.viviendasSinAbastecimiento = viviendasSinAbastecimiento;
	}

	public Integer getViviendasDeficitAbast() {
		return viviendasDeficitAbast;
	}

	public void setViviendasDeficitAbast(Integer viviendasDeficitAbast) {
		this.viviendasDeficitAbast = viviendasDeficitAbast;
	}

	public Integer getPoblacionResidenteDefAbast() {
		return poblacionResidenteDefAbast;
	}

	public void setPoblacionResidenteDefAbast(Integer poblacionResidenteDefAbast) {
		this.poblacionResidenteDefAbast = poblacionResidenteDefAbast;
	}

	public Integer getPoblacionEstacionalDefAbast() {
		return poblacionEstacionalDefAbast;
	}

	public void setPoblacionEstacionalDefAbast(Integer poblacionEstacionalDefAbast) {
		this.poblacionEstacionalDefAbast = poblacionEstacionalDefAbast;
	}

	public Integer getVivendasAbastecimientoAuto() {
		return vivendasAbastecimientoAuto;
	}

	public void setVivendasAbastecimientoAuto(Integer vivendasAbastecimientoAuto) {
		this.vivendasAbastecimientoAuto = vivendasAbastecimientoAuto;
	}

	public Integer getPoblacionResidenteAbastAuto() {
		return poblacionResidenteAbastAuto;
	}

	public void setPoblacionResidenteAbastAuto(Integer poblacionResidenteAbastAuto) {
		this.poblacionResidenteAbastAuto = poblacionResidenteAbastAuto;
	}

	public Integer getPoblacionEstacionalAbastAuto() {
		return poblacionEstacionalAbastAuto;
	}

	public void setPoblacionEstacionalAbastAuto(Integer poblacionEstacionalAbastAuto) {
		this.poblacionEstacionalAbastAuto = poblacionEstacionalAbastAuto;
	}

	public Integer getFuentesControladas() {
		return fuentesControladas;
	}

	public void setFuentesControladas(Integer fuentesControladas) {
		this.fuentesControladas = fuentesControladas;
	}

	public Integer getViviendasDefAbastAuto() {
		return viviendasDefAbastAuto;
	}

	public void setViviendasDefAbastAuto(Integer viviendasDefAbastAuto) {
		this.viviendasDefAbastAuto = viviendasDefAbastAuto;
	}

	public Integer getPoblacionResidenteDefAbastAuto() {
		return poblacionResidenteDefAbastAuto;
	}

	public void setPoblacionResidenteDefAbastAuto(
			Integer poblacionResidenteDefAbastAuto) {
		this.poblacionResidenteDefAbastAuto = poblacionResidenteDefAbastAuto;
	}

	public Integer getPoblacionEstacionalDefAbastAuto() {
		return poblacionEstacionalDefAbastAuto;
	}

	public void setPoblacionEstacionalDefAbastAuto(
			Integer poblacionEstacionalDefAbastAuto) {
		this.poblacionEstacionalDefAbastAuto = poblacionEstacionalDefAbastAuto;
	}

	public Integer getCaudalDesaguado() {
		return caudalDesaguado;
	}

	public void setCaudalDesaguado(Integer caudalDesaguado) {
		this.caudalDesaguado = caudalDesaguado;
	}

	public Integer getCaudalTratado() {
		return caudalTratado;
	}

	public void setCaudalTratado(Integer caudalTratado) {
		this.caudalTratado = caudalTratado;
	}

	public Integer getLongitudSaneamiento() {
		return longitudSaneamiento;
	}

	public void setLongitudSaneamiento(Integer longitudSaneamiento) {
		this.longitudSaneamiento = longitudSaneamiento;
	}

	public Integer getLongDeficitariaSaneam() {
		return longDeficitariaSaneam;
	}

	public void setLongDeficitariaSaneam(Integer longDeficitariaSaneam) {
		this.longDeficitariaSaneam = longDeficitariaSaneam;
	}

	public Integer getViviendasConSaneamiento() {
		return viviendasConSaneamiento;
	}

	public void setViviendasConSaneamiento(Integer viviendasConSaneamiento) {
		this.viviendasConSaneamiento = viviendasConSaneamiento;
	}

	public Integer getViviendasSinSaneamiento() {
		return viviendasSinSaneamiento;
	}

	public void setViviendasSinSaneamiento(Integer viviendasSinSaneamiento) {
		this.viviendasSinSaneamiento = viviendasSinSaneamiento;
	}

	public Integer getViviendasDefSaneam() {
		return viviendasDefSaneam;
	}

	public void setViviendasDefSaneam(Integer viviendasDefSaneam) {
		this.viviendasDefSaneam = viviendasDefSaneam;
	}

	public Integer getPoblacionEstacionalDefSaneam() {
		return poblacionEstacionalDefSaneam;
	}

	public void setPoblacionEstacionalDefSaneam(Integer poblacionEstacionalDefSaneam) {
		this.poblacionEstacionalDefSaneam = poblacionEstacionalDefSaneam;
	}

	public Integer getPoblacionResidenteDefSaneam() {
		return poblacionResidenteDefSaneam;
	}

	public void setPoblacionResidenteDefSaneam(Integer poblacionResidenteDefSaneam) {
		this.poblacionResidenteDefSaneam = poblacionResidenteDefSaneam;
	}

	public Integer getViviendasSaneamientoAuto() {
		return viviendasSaneamientoAuto;
	}

	public void setViviendasSaneamientoAuto(Integer viviendasSaneamientoAuto) {
		this.viviendasSaneamientoAuto = viviendasSaneamientoAuto;
	}

	public Integer getPoblacionEstacionalSaneamAuto() {
		return poblacionEstacionalSaneamAuto;
	}

	public void setPoblacionEstacionalSaneamAuto(
			Integer poblacionEstacionalSaneamAuto) {
		this.poblacionEstacionalSaneamAuto = poblacionEstacionalSaneamAuto;
	}

	public Integer getPoblacionResidenteSaneamAuto() {
		return poblacionResidenteSaneamAuto;
	}

	public void setPoblacionResidenteSaneamAuto(Integer poblacionResidenteSaneamAuto) {
		this.poblacionResidenteSaneamAuto = poblacionResidenteSaneamAuto;
	}

	public Integer getViviendasDeficitSaneamAuto() {
		return viviendasDeficitSaneamAuto;
	}

	public void setViviendasDeficitSaneamAuto(Integer viviendasDeficitSaneamAuto) {
		this.viviendasDeficitSaneamAuto = viviendasDeficitSaneamAuto;
	}

	public Integer getPoblacionResidenteDefSaneamAuto() {
		return poblacionResidenteDefSaneamAuto;
	}

	public void setPoblacionResidenteDefSaneamAuto(
			Integer poblacionResidenteDefSaneamAuto) {
		this.poblacionResidenteDefSaneamAuto = poblacionResidenteDefSaneamAuto;
	}

	public Integer getPoblacionEstacionalDefSaneamAuto() {
		return poblacionEstacionalDefSaneamAuto;
	}

	public void setPoblacionEstacionalDefSaneamAuto(
			Integer poblacionEstacionalDefSaneamAuto) {
		this.poblacionEstacionalDefSaneamAuto = poblacionEstacionalDefSaneamAuto;
	}

	public Float getTmBasura() {
		return TmBasura;
	}

	public void setTmBasura(Float tmBasura) {
		TmBasura = tmBasura;
	}

	public Integer getViviendasSinBasura() {
		return viviendasSinBasura;
	}

	public void setViviendasSinBasura(Integer viviendasSinBasura) {
		this.viviendasSinBasura = viviendasSinBasura;
	}

	public Integer getPoblacionResidenteSinBasura() {
		return poblacionResidenteSinBasura;
	}

	public void setPoblacionResidenteSinBasura(Integer poblacionResidenteSinBasura) {
		this.poblacionResidenteSinBasura = poblacionResidenteSinBasura;
	}

	public Integer getPoblacionEstacionalSinBasura() {
		return poblacionEstacionalSinBasura;
	}

	public void setPoblacionEstacionalSinBasura(Integer poblacionEstacionalSinBasura) {
		this.poblacionEstacionalSinBasura = poblacionEstacionalSinBasura;
	}

	public Integer getPlantillaLimpieza() {
		return plantillaLimpieza;
	}

	public void setPlantillaLimpieza(Integer plantillaLimpieza) {
		this.plantillaLimpieza = plantillaLimpieza;
	}

	public Integer getPuntosLuz() {
		return puntosLuz;
	}

	public void setPuntosLuz(Integer puntosLuz) {
		this.puntosLuz = puntosLuz;
	}

	public Integer getViviendasSinAlumbrado() {
		return viviendasSinAlumbrado;
	}

	public void setViviendasSinAlumbrado(Integer viviendasSinAlumbrado) {
		this.viviendasSinAlumbrado = viviendasSinAlumbrado;
	}

	public Integer getLongDeficitariaAlumbrado() {
		return longDeficitariaAlumbrado;
	}

	public void setLongDeficitariaAlumbrado(Integer longDeficitariaAlumbrado) {
		this.longDeficitariaAlumbrado = longDeficitariaAlumbrado;
	}


	/**
	 * @param version the version to set
	 */
	public void setVersion(VersionEiel version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public VersionEiel getVersion() {
		return version;
	}
	
	@Override
	public String toString() {
		return "DiseminadosEIEL [codINEMunicipio="+codINEMunicipio+"]";
	}	
	
}
