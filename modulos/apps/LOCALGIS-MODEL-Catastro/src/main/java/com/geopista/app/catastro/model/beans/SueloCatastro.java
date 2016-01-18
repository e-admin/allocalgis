/**
 * SueloCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;


import java.io.Serializable;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosSuelo;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosSuelo;

public class SueloCatastro implements Serializable
{    
	public static final String TIPO_MOVIMIENTO_FINAL ="F";
    public static final String TIPO_MOVIMIENTO_ALTA ="A";
    public static final String TIPO_MOVIMIENTO_BAJA ="B";
    public static final String TIPO_MOVIMIENTO_MODIF ="M";
	
	 public String TIPO_MOVIMIENTO = TIPO_MOVIMIENTO_FINAL;
	 
    /**
     * IDentificador del suelo: referencia catastral + numero de orden
     */
    private String idSuelo;
    
    /**
     * Tipo de movimiento
     */
    private Integer tipoRegistro;
    /**
     * Datos de expediente
     */
    private Expediente datosExpediente = new Expediente();
    
    /**
     * Referencia de la finca
     */
    private ReferenciaCatastral refParcela;
    /**
     * Número de orden del elemento del suelo (subparcela)
     */
    private String numOrden;
    /**
     * Datos físicos del suelo
     */
    private DatosFisicosSuelo datosFisicos = new DatosFisicosSuelo();
    
    /**
     * Datos económicos del suelo
     */
    private DatosEconomicosSuelo datosEconomicos = new DatosEconomicosSuelo();
    
    /**
     * Código de la delegación
     */
    private String codDelegacion;
    
    /**
     * Código del municipio según la DGC
     */    
    private String codMunicipioDGC;
    
 // marca si el elemento ha sido modificado,borrado o es nuevo
    // para activar los campos de edicion en la informacion catastral
    private boolean elementoModificado;
    
    /**
     * Constructor por defecto
     *
     */
    public SueloCatastro()
    {
        
    }    
    
    public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
		
		if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
			//expediente de variaciones
			if(TIPO_MOVIMIENTO. equals(TIPO_MOVIMIENTO_ALTA) || 
					TIPO_MOVIMIENTO. equals(TIPO_MOVIMIENTO_MODIF)){
				//se marca que el elemento esta modificado, por lo tanto es editable
				setElementoModificado(true);
				
			}
			else if(TIPO_MOVIMIENTO. equals(TIPO_MOVIMIENTO_BAJA)){
				setElementoModificado(false);
			}
		}
	}
        
    public String getTIPO_MOVIMIENTO() {
		//return "F";
    	return TIPO_MOVIMIENTO;
	}


	/**
     * @return Returns the datosEconomicos.
     */
    public DatosEconomicosSuelo getDatosEconomicos()
    {
        return datosEconomicos;
    }
    
    /**
     * @param datosEconomicos The datosEconomicos to set.
     */
    public void setDatosEconomicos(DatosEconomicosSuelo datosEconomicos)
    {
        this.datosEconomicos = datosEconomicos;
    }
    
    /**
     * @return Returns the datosFisicos.
     */
    public DatosFisicosSuelo getDatosFisicos()
    {
        return datosFisicos;
    }
    
    /**
     * @param datosFisicos The datosFisicos to set.
     */
    public void setDatosFisicos(DatosFisicosSuelo datosFisicos)
    {
        this.datosFisicos = datosFisicos;
    }
    
    /**
     * @return Returns the idSuelo.
     */
    public String getIdSuelo()
    {
        return idSuelo;
    }
    
    /**
     * @param idSuelo The idSuelo to set.
     */
    public void setIdSuelo(String idSuelo)
    {
        this.idSuelo = idSuelo;
    }
    
    
    /**
     * @return Returns the numOrden.
     */
    public String getNumOrden()
    {
        return numOrden;
    }
    
    /**
     * @param numOrden The numOrden to set.
     */
    public void setNumOrden(String numOrden)
    {
        this.numOrden = numOrden;
    }    
    
    
    /**
     * @return Returns the refParcela.
     */
    public ReferenciaCatastral getRefParcela()
    {
        return refParcela;
    }
    
    /**
     * @param refParcela The refParcela to set.
     */
    public void setRefParcela(ReferenciaCatastral refParcela)
    {
        this.refParcela = refParcela;
    }
    
    
    /**
     * @return Returns the tipoRegistro.
     */
    public Integer getTipoRegistro()
    {
        return tipoRegistro;
    }
    
    /**
     * @param tipoRegistro The tipoRegistro to set.
     */
    public void setTipoRegistro(Integer tipoRegistro)
    {
        this.tipoRegistro = tipoRegistro;
    }
    
    /**
     * @return Returns the datosExpediente.
     */
    public Expediente getDatosExpediente()
    {
        return datosExpediente;
    }
    
    /**
     * @param datosExpediente The datosExpediente to set.
     */
    public void setDatosExpediente(Expediente datosExpediente)
    {
        this.datosExpediente = datosExpediente;
    }
    
    
    public String getCodDelegacion() {
		return codDelegacion;
	}

	public void setCodDelegacion(String codDelegacion) {
		this.codDelegacion = codDelegacion;
	}

	public String getCodMunicipioDGC() {
		return codMunicipioDGC;
	}

	public void setCodMunicipioDGC(String codMunicipioDGC) {
		this.codMunicipioDGC = codMunicipioDGC;
	}

	/**
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }


    public SueloCatastro clone (SueloCatastro sueloCatastro){
    	
    	SueloCatastro sueloCatastroNuevo = new SueloCatastro();
    	
    	sueloCatastroNuevo.setCodDelegacion(sueloCatastro.getCodDelegacion());
    	sueloCatastroNuevo.setCodMunicipioDGC(sueloCatastro.getCodMunicipioDGC());
    	
    	if(sueloCatastro.getDatosEconomicos() != null){
    		sueloCatastroNuevo.setDatosEconomicos(new DatosEconomicosSuelo());
    		sueloCatastroNuevo.getDatosEconomicos().setCodTipoValor(sueloCatastro.getDatosEconomicos().getCodTipoValor());
    		sueloCatastroNuevo.getDatosEconomicos().setCodTramoPonencia(sueloCatastro.getDatosEconomicos().getCodTramoPonencia());
    		sueloCatastroNuevo.getDatosEconomicos().setCodViaPonencia(sueloCatastro.getDatosEconomicos().getCodViaPonencia());
    		sueloCatastroNuevo.getDatosEconomicos().setCoefAprecDeprec(sueloCatastro.getDatosEconomicos().getCoefAprecDeprec());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorAprecDeprec(sueloCatastro.getDatosEconomicos().getCorrectorAprecDeprec());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorCargasSingulares(sueloCatastro.getDatosEconomicos().getCorrectorCargasSingulares());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorDeprecFuncional(sueloCatastro.getDatosEconomicos().isCorrectorDeprecFuncional());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorDesmonte(sueloCatastro.getDatosEconomicos().isCorrectorDesmonte());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorFormaIrregular(sueloCatastro.getDatosEconomicos().isCorrectorFormaIrregular());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorInedificabilidad(sueloCatastro.getDatosEconomicos().isCorrectorInedificabilidad());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorLongFachada(sueloCatastro.getDatosEconomicos().isCorrectorLongFachada());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorNoLucrativo(sueloCatastro.getDatosEconomicos().isCorrectorNoLucrativo());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorSitEspeciales(sueloCatastro.getDatosEconomicos().isCorrectorSitEspeciales());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorSupDistinta(sueloCatastro.getDatosEconomicos().isCorrectorSupDistinta());
    		sueloCatastroNuevo.getDatosEconomicos().setCorrectorVPO(sueloCatastro.getDatosEconomicos().isCorrectorVPO());
    		sueloCatastroNuevo.getDatosEconomicos().setNumFachadas(sueloCatastro.getDatosEconomicos().getNumFachadas());
    		sueloCatastroNuevo.getDatosEconomicos().setZonaUrbanistica(sueloCatastro.getDatosEconomicos().getZonaUrbanistica());
    		sueloCatastroNuevo.getDatosEconomicos().setZonaValor(sueloCatastro.getDatosEconomicos().getZonaValor());
    	}
    	
    	if(sueloCatastro.getDatosExpediente() != null){
    		sueloCatastroNuevo.setDatosExpediente(new Expediente());
    		sueloCatastroNuevo.getDatosExpediente().setAnnoExpedienteAdminOrigenAlteracion(sueloCatastro.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion());
    		sueloCatastroNuevo.getDatosExpediente().setAnnoExpedienteGerencia(sueloCatastro.getDatosExpediente().getAnnoExpedienteGerencia());
    		sueloCatastroNuevo.getDatosExpediente().setAnnoProtocoloNotarial(sueloCatastro.getDatosExpediente().getAnnoProtocoloNotarial());
    		sueloCatastroNuevo.getDatosExpediente().setCodigoDescriptivoAlteracion(sueloCatastro.getDatosExpediente().getCodigoDescriptivoAlteracion());
    		sueloCatastroNuevo.getDatosExpediente().setCodigoEntidadRegistroDGCOrigenAlteracion(sueloCatastro.getDatosExpediente().getCodigoEntidadRegistroDGCOrigenAlteracion());
    		sueloCatastroNuevo.getDatosExpediente().setCodigoINEmunicipio(sueloCatastro.getDatosExpediente().getCodigoINEmunicipio());
    		sueloCatastroNuevo.getDatosExpediente().setCodNotaria(sueloCatastro.getDatosExpediente().getCodNotaria());
    		sueloCatastroNuevo.getDatosExpediente().setCodPoblacionNotaria(sueloCatastro.getDatosExpediente().getCodPoblacionNotaria());
    		sueloCatastroNuevo.getDatosExpediente().setCodProvinciaNotaria(sueloCatastro.getDatosExpediente().getCodProvinciaNotaria());
    		sueloCatastroNuevo.getDatosExpediente().setDescripcionAlteracion(sueloCatastro.getDatosExpediente().getDescripcionAlteracion());
    		
    		if(sueloCatastro.getDatosExpediente().getDireccionPresentador() != null){
    			sueloCatastroNuevo.getDatosExpediente().setDireccionPresentador(new DireccionLocalizacion());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setApartadoCorreos(sueloCatastro.getDatosExpediente().getDireccionPresentador().getApartadoCorreos());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setBloque(sueloCatastro.getDatosExpediente().getDireccionPresentador().getBloque());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setCodigoMunicipioDGC(sueloCatastro.getDatosExpediente().getDireccionPresentador().getCodigoMunicipioDGC());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setCodigoPostal(sueloCatastro.getDatosExpediente().getDireccionPresentador().getCodigoPostal());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setCodigoVia(sueloCatastro.getDatosExpediente().getDireccionPresentador().getCodigoVia());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setCodMunOrigenAgregacion(sueloCatastro.getDatosExpediente().getDireccionPresentador().getCodMunOrigenAgregacion());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setCodParaje(sueloCatastro.getDatosExpediente().getDireccionPresentador().getCodParaje());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setCodParcela(sueloCatastro.getDatosExpediente().getDireccionPresentador().getCodParcela());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setCodPoligono(sueloCatastro.getDatosExpediente().getDireccionPresentador().getCodPoligono());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setCodZonaConcentracion(sueloCatastro.getDatosExpediente().getDireccionPresentador().getCodZonaConcentracion());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setDireccionNoEstructurada(sueloCatastro.getDatosExpediente().getDireccionPresentador().getDireccionNoEstructurada());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setDistrito(sueloCatastro.getDatosExpediente().getDireccionPresentador().getDistrito());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setEscalera(sueloCatastro.getDatosExpediente().getDireccionPresentador().getEscalera());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setIdLocalizacion(sueloCatastro.getDatosExpediente().getDireccionPresentador().getIdLocalizacion());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setIdVia(sueloCatastro.getDatosExpediente().getDireccionPresentador().getIdVia());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setKilometro(sueloCatastro.getDatosExpediente().getDireccionPresentador().getKilometro());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setMunicipioINE(sueloCatastro.getDatosExpediente().getDireccionPresentador().getMunicipioINE());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setNombreEntidadMenor(sueloCatastro.getDatosExpediente().getDireccionPresentador().getNombreEntidadMenor());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setNombreMunicipio(sueloCatastro.getDatosExpediente().getDireccionPresentador().getNombreMunicipio());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setNombreParaje(sueloCatastro.getDatosExpediente().getDireccionPresentador().getNombreParaje());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setNombreProvincia(sueloCatastro.getDatosExpediente().getDireccionPresentador().getNombreProvincia());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setNombreVia(sueloCatastro.getDatosExpediente().getDireccionPresentador().getNombreVia());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setPlanta(sueloCatastro.getDatosExpediente().getDireccionPresentador().getPlanta());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setPrimeraLetra(sueloCatastro.getDatosExpediente().getDireccionPresentador().getPrimeraLetra());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setPrimerNumero(sueloCatastro.getDatosExpediente().getDireccionPresentador().getPrimerNumero());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setProvinciaINE(sueloCatastro.getDatosExpediente().getDireccionPresentador().getProvinciaINE());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setPuerta(sueloCatastro.getDatosExpediente().getDireccionPresentador().getPuerta());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setSegundaLetra(sueloCatastro.getDatosExpediente().getDireccionPresentador().getSegundaLetra());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setSegundoNumero(sueloCatastro.getDatosExpediente().getDireccionPresentador().getSegundoNumero());
    			sueloCatastroNuevo.getDatosExpediente().getDireccionPresentador().setTipoVia(sueloCatastro.getDatosExpediente().getDireccionPresentador().getTipoVia());
    		}
    		
    		if(sueloCatastro.getDatosExpediente().getEntidadGeneradora() != null){
    			sueloCatastroNuevo.getDatosExpediente().setEntidadGeneradora(new EntidadGeneradora());
    			sueloCatastroNuevo.getDatosExpediente().getEntidadGeneradora().setCodigo(sueloCatastro.getDatosExpediente().getEntidadGeneradora().getCodigo());
    			sueloCatastroNuevo.getDatosExpediente().getEntidadGeneradora().setDescripcion(sueloCatastro.getDatosExpediente().getEntidadGeneradora().getDescripcion());
    			sueloCatastroNuevo.getDatosExpediente().getEntidadGeneradora().setIdEntidadGeneradora(sueloCatastro.getDatosExpediente().getEntidadGeneradora().getIdEntidadGeneradora());
    			sueloCatastroNuevo.getDatosExpediente().getEntidadGeneradora().setNombre(sueloCatastro.getDatosExpediente().getEntidadGeneradora().getNombre());
    			sueloCatastroNuevo.getDatosExpediente().getEntidadGeneradora().setTipo(sueloCatastro.getDatosExpediente().getEntidadGeneradora().getTipo());
    		}
    		
    		sueloCatastroNuevo.getDatosExpediente().setExistenciaInformacionGrafica(sueloCatastro.getDatosExpediente().getExistenciaInformacionGrafica());
    		sueloCatastroNuevo.getDatosExpediente().setFechaAlteracion(sueloCatastro.getDatosExpediente().getFechaAlteracion());
    		sueloCatastroNuevo.getDatosExpediente().setFechaDeCierre(sueloCatastro.getDatosExpediente().getFechaDeCierre());
    		sueloCatastroNuevo.getDatosExpediente().setFechaMovimiento(sueloCatastro.getDatosExpediente().getFechaMovimiento());
    		sueloCatastroNuevo.getDatosExpediente().setFechaRegistro(sueloCatastro.getDatosExpediente().getFechaRegistro());
    		sueloCatastroNuevo.getDatosExpediente().setHoraMovimiento(sueloCatastro.getDatosExpediente().getHoraMovimiento());
    		sueloCatastroNuevo.getDatosExpediente().setIdEstado(sueloCatastro.getDatosExpediente().getIdEstado());
    		sueloCatastroNuevo.getDatosExpediente().setIdExpediente(sueloCatastro.getDatosExpediente().getIdExpediente());
    		sueloCatastroNuevo.getDatosExpediente().setIdMunicipio(sueloCatastro.getDatosExpediente().getIdMunicipio());
    		sueloCatastroNuevo.getDatosExpediente().setIdTecnicoCatastro(sueloCatastro.getDatosExpediente().getIdTecnicoCatastro());
    		sueloCatastroNuevo.getDatosExpediente().setInfoDocumentoOrigenAlteracion(sueloCatastro.getDatosExpediente().getInfoDocumentoOrigenAlteracion());
    		sueloCatastroNuevo.getDatosExpediente().setListaReferencias(sueloCatastro.getDatosExpediente().getListaReferencias());
    		sueloCatastroNuevo.getDatosExpediente().setM_Direccion(sueloCatastro.getDatosExpediente().getM_Direccion());
    		sueloCatastroNuevo.getDatosExpediente().setNifPresentador(sueloCatastro.getDatosExpediente().getNifPresentador());
    		sueloCatastroNuevo.getDatosExpediente().setNombreCompletoPresentador(sueloCatastro.getDatosExpediente().getNombreCompletoPresentador());
    		sueloCatastroNuevo.getDatosExpediente().setNumBienesInmueblesCaractEsp(sueloCatastro.getDatosExpediente().getNumBienesInmueblesCaractEsp());
    		sueloCatastroNuevo.getDatosExpediente().setNumBienesInmueblesRusticos(sueloCatastro.getDatosExpediente().getNumBienesInmueblesRusticos());
    		sueloCatastroNuevo.getDatosExpediente().setNumBienesInmueblesUrbanos(sueloCatastro.getDatosExpediente().getNumBienesInmueblesUrbanos());
    		sueloCatastroNuevo.getDatosExpediente().setNumeroExpediente(sueloCatastro.getDatosExpediente().getNumeroExpediente());
    		sueloCatastroNuevo.getDatosExpediente().setProtocoloNotarial(sueloCatastro.getDatosExpediente().getProtocoloNotarial());
    		sueloCatastroNuevo.getDatosExpediente().setReferenciaExpedienteAdminOrigen(sueloCatastro.getDatosExpediente().getReferenciaExpedienteAdminOrigen());
    		sueloCatastroNuevo.getDatosExpediente().setReferenciaExpedienteGerencia(sueloCatastro.getDatosExpediente().getReferenciaExpedienteGerencia());
    		sueloCatastroNuevo.getDatosExpediente().setTipoDeIntercambio(sueloCatastro.getDatosExpediente().getTipoDeIntercambio());
    		sueloCatastroNuevo.getDatosExpediente().setTipoDocumentoOrigenAlteracion(sueloCatastro.getDatosExpediente().getTipoDocumentoOrigenAlteracion());
    		sueloCatastroNuevo.getDatosExpediente().setTipoExpediente(sueloCatastro.getDatosExpediente().getTipoExpediente());
    	}

    	if(sueloCatastro.getDatosFisicos() != null){
    		sueloCatastroNuevo.setDatosFisicos(new DatosFisicosSuelo());
    		sueloCatastroNuevo.getDatosFisicos().setFondo(sueloCatastro.getDatosFisicos().getFondo());
    		sueloCatastroNuevo.getDatosFisicos().setLongFachada(sueloCatastro.getDatosFisicos().getLongFachada());
    		sueloCatastroNuevo.getDatosFisicos().setSupOcupada(sueloCatastro.getDatosFisicos().getSupOcupada());
    		sueloCatastroNuevo.getDatosFisicos().setTipoFachada(sueloCatastro.getDatosFisicos().getTipoFachada());
    		
    	}
    	
    	sueloCatastroNuevo.setIdSuelo(sueloCatastro.getIdSuelo());
    	sueloCatastroNuevo.setNumOrden(sueloCatastro.getNumOrden());
    	
    	if(sueloCatastro.getRefParcela() != null){
    		sueloCatastroNuevo.setRefParcela(new ReferenciaCatastral(sueloCatastro.getRefParcela().getRefCatastral()));
    		sueloCatastroNuevo.getRefParcela().setRefCatastral(sueloCatastro.getRefParcela().getRefCatastral());
    		sueloCatastroNuevo.getRefParcela().setRefCatastral1(sueloCatastro.getRefParcela().getRefCatastral1());
    		sueloCatastroNuevo.getRefParcela().setRefCatastral2(sueloCatastro.getRefParcela().getRefCatastral2());

    	}
    	sueloCatastroNuevo.setTIPO_MOVIMIENTO(sueloCatastro.getTIPO_MOVIMIENTO());
    	sueloCatastroNuevo.setTipoRegistro(sueloCatastro.getTipoRegistro());
    	
    	return sueloCatastroNuevo;
    	
    }
    public boolean isElementoModificado() {
		return elementoModificado;
	}

	public void setElementoModificado(boolean elementoModificado) {
		this.elementoModificado = elementoModificado;
	}
	
	public boolean isCatastroTemporal(){
		
		if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
			boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
			if(isCatTemporal){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
		//return true;
	}
	public boolean isNotCatastroTemporal(){
		if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
			boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
			if(isCatTemporal){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return true;
		}
		
		//return false;
	}
    
	public Boolean isElemModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) || 
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF) ||
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Boolean isSueloAltaModif(){
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean sueloAltaModif = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) || 
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF)){
			sueloAltaModif= true;
		}
		return sueloAltaModif;
	}
	
	public Boolean isSueloElim() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean sueloElim = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			sueloElim = true;
		}
		return sueloElim;
	}
}
