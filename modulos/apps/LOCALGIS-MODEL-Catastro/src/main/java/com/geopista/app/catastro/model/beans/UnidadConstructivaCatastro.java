/**
 * UnidadConstructivaCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;


import java.io.Serializable;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosUC;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosUC;

public class UnidadConstructivaCatastro implements Serializable
{
	public static final String TIPO_MOVIMIENTO_FINAL ="F";
    public static final String TIPO_MOVIMIENTO_ALTA ="A";
    public static final String TIPO_MOVIMIENTO_BAJA ="B";
    public static final String TIPO_MOVIMIENTO_MODIF ="M";
    
	public String TIPO_MOVIMIENTO = TIPO_MOVIMIENTO_FINAL ;
	 
	
    /**
     * IDentificador de la UC: referencia catastral + número de la uc
     */
    private String idUnidadConstructiva;
    /**
     * Tipo de movimiento
     */
    private int tipoRegistro;
    /**
     * Datos del expediente
     */
    private Expediente datosExpediente = new Expediente();
    
    private String tipoUnidad;
    /**
     * Parcela catastral
     */
    private ReferenciaCatastral refParcela;
    /**
     * Código de la uc
     */
    private String codUnidadConstructiva;
    
    
    private DireccionLocalizacion dirUnidadConstructiva = new DireccionLocalizacion();
    private DatosFisicosUC datosFisicos = new DatosFisicosUC();
    private DatosEconomicosUC datosEconomicos = new DatosEconomicosUC();
    
    
    /**
     * Código de delegación
     */
    private String codDelegacionMEH;
    /**
     * Código de municipio
     */
    private String codMunicipioDGC;
    
    // marca si el elemento ha sido modificado,borrado o es nuevo
    // para activar los campos de edicion en la informacion catastral
    private boolean elementoModificado;
    
    /**
     * Constructor por defecto
     *
     */
    public UnidadConstructivaCatastro()
    {
        
    }
    
    
    
    public String getTIPO_MOVIMIENTO() {
		//return "F";
    	return TIPO_MOVIMIENTO;
	}

    public UnidadConstructivaCatastro clone (UnidadConstructivaCatastro unidadConstructiva){
    	
    	UnidadConstructivaCatastro unidadConstructivaNueva = new UnidadConstructivaCatastro();
    	
    	unidadConstructivaNueva.setCodDelegacionMEH(unidadConstructiva.getCodDelegacionMEH());
    	unidadConstructivaNueva.setCodMunicipioDGC(unidadConstructiva.getCodMunicipioDGC());
    	unidadConstructivaNueva.setCodUnidadConstructiva(unidadConstructiva.getCodUnidadConstructiva());
    	
    	if(unidadConstructiva.getDatosEconomicos() != null){
	    	unidadConstructivaNueva.setDatosEconomicos(new DatosEconomicosUC());
	    	unidadConstructivaNueva.getDatosEconomicos().setCodViaPonencia(unidadConstructiva.getDatosEconomicos().getCodViaPonencia());
	    	unidadConstructivaNueva.getDatosEconomicos().setCoefCargasSingulares(unidadConstructiva.getDatosEconomicos().getCoefCargasSingulares());
	    	unidadConstructivaNueva.getDatosEconomicos().setCorrectorConservacion(unidadConstructiva.getDatosEconomicos().getCorrectorConservacion());
	    	unidadConstructivaNueva.getDatosEconomicos().setCorrectorDepreciacion(unidadConstructiva.getDatosEconomicos().isCorrectorDepreciacion());
	    	unidadConstructivaNueva.getDatosEconomicos().setCorrectorLongFachada(unidadConstructiva.getDatosEconomicos().isCorrectorLongFachada());
	    	unidadConstructivaNueva.getDatosEconomicos().setCorrectorNoLucrativo(unidadConstructiva.getDatosEconomicos().isCorrectorNoLucrativo());
	    	unidadConstructivaNueva.getDatosEconomicos().setCorrectorSitEspeciales(unidadConstructiva.getDatosEconomicos().isCorrectorSitEspeciales());
	    	unidadConstructivaNueva.getDatosEconomicos().setNumFachadas(unidadConstructiva.getDatosEconomicos().getNumFachadas());
	    	unidadConstructivaNueva.getDatosEconomicos().setTramoPonencia(unidadConstructiva.getDatosEconomicos().getTramoPonencia());
	    	unidadConstructivaNueva.getDatosEconomicos().setZonaValor(unidadConstructiva.getDatosEconomicos().getZonaValor());
    	}
    	
    	if(unidadConstructiva.getDatosExpediente() != null){
	    	unidadConstructivaNueva.setDatosExpediente(new Expediente());
	    	unidadConstructivaNueva.getDatosExpediente().setAnnoExpedienteAdminOrigenAlteracion(unidadConstructiva.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion());
	    	unidadConstructivaNueva.getDatosExpediente().setAnnoExpedienteGerencia(unidadConstructiva.getDatosExpediente().getAnnoExpedienteGerencia());
	    	unidadConstructivaNueva.getDatosExpediente().setAnnoProtocoloNotarial(unidadConstructiva.getDatosExpediente().getAnnoProtocoloNotarial());
	    	unidadConstructivaNueva.getDatosExpediente().setCodigoDescriptivoAlteracion(unidadConstructiva.getDatosExpediente().getCodigoDescriptivoAlteracion());
	    	unidadConstructivaNueva.getDatosExpediente().setCodigoEntidadRegistroDGCOrigenAlteracion(unidadConstructiva.getDatosExpediente().getCodigoEntidadRegistroDGCOrigenAlteracion());
	    	unidadConstructivaNueva.getDatosExpediente().setCodigoINEmunicipio(unidadConstructiva.getDatosExpediente().getCodigoINEmunicipio());
	    	unidadConstructivaNueva.getDatosExpediente().setCodNotaria(unidadConstructiva.getDatosExpediente().getCodNotaria());
	    	unidadConstructivaNueva.getDatosExpediente().setCodPoblacionNotaria(unidadConstructiva.getDatosExpediente().getCodPoblacionNotaria());
	    	unidadConstructivaNueva.getDatosExpediente().setCodProvinciaNotaria(unidadConstructiva.getDatosExpediente().getCodProvinciaNotaria());
	    	unidadConstructivaNueva.getDatosExpediente().setDescripcionAlteracion(unidadConstructiva.getDatosExpediente().getDescripcionAlteracion());
	    	unidadConstructivaNueva.getDatosExpediente().setDireccionPresentador(unidadConstructiva.getDatosExpediente().getDireccionPresentador());
    	}
    	
    	if(unidadConstructiva.getDatosExpediente().getEntidadGeneradora() != null){
    		unidadConstructivaNueva.getDatosExpediente().setEntidadGeneradora( new EntidadGeneradora());
	    	unidadConstructivaNueva.getDatosExpediente().getEntidadGeneradora().setCodigo(unidadConstructiva.getDatosExpediente().getEntidadGeneradora().getCodigo());
	    	unidadConstructivaNueva.getDatosExpediente().getEntidadGeneradora().setDescripcion(unidadConstructiva.getDatosExpediente().getEntidadGeneradora().getDescripcion());
	    	unidadConstructivaNueva.getDatosExpediente().getEntidadGeneradora().setIdEntidadGeneradora(unidadConstructiva.getDatosExpediente().getEntidadGeneradora().getIdEntidadGeneradora());
	    	unidadConstructivaNueva.getDatosExpediente().getEntidadGeneradora().setNombre(unidadConstructiva.getDatosExpediente().getEntidadGeneradora().getNombre());
	    	unidadConstructivaNueva.getDatosExpediente().getEntidadGeneradora().setTipo(unidadConstructiva.getDatosExpediente().getEntidadGeneradora().getTipo());
    	}
    	
    	unidadConstructivaNueva.getDatosExpediente().setExisteDeclaracionAlteracion(unidadConstructiva.getDatosExpediente().isExisteDeclaracionAlteracion());
    	unidadConstructivaNueva.getDatosExpediente().setExistenciaInformacionGrafica(unidadConstructiva.getDatosExpediente().getExistenciaInformacionGrafica());
    	unidadConstructivaNueva.getDatosExpediente().setFechaAlteracion(unidadConstructiva.getDatosExpediente().getFechaAlteracion());
    	unidadConstructivaNueva.getDatosExpediente().setFechaDeCierre(unidadConstructiva.getDatosExpediente().getFechaDeCierre());
    	unidadConstructivaNueva.getDatosExpediente().setFechaMovimiento(unidadConstructiva.getDatosExpediente().getFechaMovimiento());
    	unidadConstructivaNueva.getDatosExpediente().setFechaRegistro(unidadConstructiva.getDatosExpediente().getFechaRegistro());
    	unidadConstructivaNueva.getDatosExpediente().setHoraMovimiento(unidadConstructiva.getDatosExpediente().getHoraMovimiento());
    	unidadConstructivaNueva.getDatosExpediente().setIdEstado(unidadConstructiva.getDatosExpediente().getIdEstado());
    	unidadConstructivaNueva.getDatosExpediente().setIdExpediente(unidadConstructiva.getDatosExpediente().getIdExpediente());
    	unidadConstructivaNueva.getDatosExpediente().setIdMunicipio(unidadConstructiva.getDatosExpediente().getIdMunicipio());
    	unidadConstructivaNueva.getDatosExpediente().setIdTecnicoCatastro(unidadConstructiva.getDatosExpediente().getIdTecnicoCatastro());
    	unidadConstructivaNueva.getDatosExpediente().setInfoDocumentoOrigenAlteracion(unidadConstructiva.getDatosExpediente().getInfoDocumentoOrigenAlteracion());
    	unidadConstructivaNueva.getDatosExpediente().setListaReferencias(unidadConstructiva.getDatosExpediente().getListaReferencias());
    	unidadConstructivaNueva.getDatosExpediente().setM_Direccion(unidadConstructiva.getDatosExpediente().getM_Direccion());
    	unidadConstructivaNueva.getDatosExpediente().setNifPresentador(unidadConstructiva.getDatosExpediente().getNifPresentador());
    	unidadConstructivaNueva.getDatosExpediente().setNombreCompletoPresentador(unidadConstructiva.getDatosExpediente().getNombreCompletoPresentador());
    	unidadConstructivaNueva.getDatosExpediente().setNumBienesInmueblesCaractEsp(unidadConstructiva.getDatosExpediente().getNumBienesInmueblesCaractEsp());
    	unidadConstructivaNueva.getDatosExpediente().setNumBienesInmueblesRusticos(unidadConstructiva.getDatosExpediente().getNumBienesInmueblesRusticos());
    	unidadConstructivaNueva.getDatosExpediente().setNumBienesInmueblesUrbanos(unidadConstructiva.getDatosExpediente().getNumBienesInmueblesUrbanos());
    	unidadConstructivaNueva.getDatosExpediente().setNumeroExpediente(unidadConstructiva.getDatosExpediente().getNumeroExpediente());
    	unidadConstructivaNueva.getDatosExpediente().setProtocoloNotarial(unidadConstructiva.getDatosExpediente().getProtocoloNotarial());
    	unidadConstructivaNueva.getDatosExpediente().setReferenciaExpedienteAdminOrigen(unidadConstructiva.getDatosExpediente().getReferenciaExpedienteAdminOrigen());
    	unidadConstructivaNueva.getDatosExpediente().setReferenciaExpedienteGerencia(unidadConstructiva.getDatosExpediente().getReferenciaExpedienteGerencia());
    	unidadConstructivaNueva.getDatosExpediente().setTipoDeIntercambio(unidadConstructiva.getDatosExpediente().getTipoDeIntercambio());
    	unidadConstructivaNueva.getDatosExpediente().setTipoDocumentoOrigenAlteracion(unidadConstructiva.getDatosExpediente().getTipoDocumentoOrigenAlteracion());
    	unidadConstructivaNueva.getDatosExpediente().setTipoExpediente(unidadConstructiva.getDatosExpediente().getTipoExpediente());
    	
    	if(unidadConstructiva.getDatosFisicos() != null){
	    	unidadConstructivaNueva.setDatosFisicos(new DatosFisicosUC());
	    	unidadConstructivaNueva.getDatosFisicos().setAnioConstruccion(unidadConstructiva.getDatosFisicos().getAnioConstruccion());
	    	unidadConstructivaNueva.getDatosFisicos().setIndExactitud(unidadConstructiva.getDatosFisicos().getIndExactitud());
	    	unidadConstructivaNueva.getDatosFisicos().setLongFachada(unidadConstructiva.getDatosFisicos().getLongFachada());
	    	unidadConstructivaNueva.getDatosFisicos().setSupOcupada(unidadConstructiva.getDatosFisicos().getSupOcupada());
    	}
    	
    	unidadConstructivaNueva.setDirUnidadConstructiva(new DireccionLocalizacion());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setApartadoCorreos(unidadConstructiva.getDirUnidadConstructiva().getApartadoCorreos());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setBloque(unidadConstructiva.getDirUnidadConstructiva().getBloque());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setCodigoMunicipioDGC(unidadConstructiva.getDirUnidadConstructiva().getCodigoMunicipioDGC());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setCodigoPostal(unidadConstructiva.getDirUnidadConstructiva().getCodigoPostal());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setCodigoVia(unidadConstructiva.getDirUnidadConstructiva().getCodigoVia());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setCodMunOrigenAgregacion(unidadConstructiva.getDirUnidadConstructiva().getCodMunOrigenAgregacion());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setCodParaje(unidadConstructiva.getDirUnidadConstructiva().getCodParaje());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setCodParcela(unidadConstructiva.getDirUnidadConstructiva().getCodParcela());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setCodPoligono(unidadConstructiva.getDirUnidadConstructiva().getCodPoligono());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setCodZonaConcentracion(unidadConstructiva.getDirUnidadConstructiva().getCodZonaConcentracion());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setDireccionNoEstructurada(unidadConstructiva.getDirUnidadConstructiva().getDireccionNoEstructurada());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setDistrito(unidadConstructiva.getDirUnidadConstructiva().getDistrito());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setEscalera(unidadConstructiva.getDirUnidadConstructiva().getEscalera());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setIdLocalizacion(unidadConstructiva.getDirUnidadConstructiva().getIdLocalizacion());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setIdVia(unidadConstructiva.getDirUnidadConstructiva().getIdVia());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setKilometro(unidadConstructiva.getDirUnidadConstructiva().getKilometro());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setMunicipioINE(unidadConstructiva.getDirUnidadConstructiva().getMunicipioINE());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setNombreEntidadMenor(unidadConstructiva.getDirUnidadConstructiva().getNombreEntidadMenor());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setNombreMunicipio(unidadConstructiva.getDirUnidadConstructiva().getNombreMunicipio());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setNombreParaje(unidadConstructiva.getDirUnidadConstructiva().getNombreParaje());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setNombreProvincia(unidadConstructiva.getDirUnidadConstructiva().getNombreProvincia());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setNombreVia(unidadConstructiva.getDirUnidadConstructiva().getNombreVia());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setPlanta(unidadConstructiva.getDirUnidadConstructiva().getPlanta());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setPrimeraLetra(unidadConstructiva.getDirUnidadConstructiva().getPrimeraLetra());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setPrimerNumero(unidadConstructiva.getDirUnidadConstructiva().getPrimerNumero());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setProvinciaINE(unidadConstructiva.getDirUnidadConstructiva().getProvinciaINE());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setPuerta(unidadConstructiva.getDirUnidadConstructiva().getPuerta());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setSegundaLetra(unidadConstructiva.getDirUnidadConstructiva().getSegundaLetra());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setSegundoNumero(unidadConstructiva.getDirUnidadConstructiva().getSegundoNumero());
    	unidadConstructivaNueva.getDirUnidadConstructiva().setTipoVia(unidadConstructiva.getDirUnidadConstructiva().getTipoVia());

    	unidadConstructivaNueva.setRefParcela(new ReferenciaCatastral(unidadConstructiva.getRefParcela().getRefCatastral()));
    	unidadConstructivaNueva.getRefParcela().setRefCatastral(unidadConstructiva.getRefParcela().getRefCatastral());
    	unidadConstructivaNueva.getRefParcela().setRefCatastral1(unidadConstructiva.getRefParcela().getRefCatastral1());
    	unidadConstructivaNueva.getRefParcela().setRefCatastral2(unidadConstructiva.getRefParcela().getRefCatastral2());

    	unidadConstructivaNueva.setTIPO_MOVIMIENTO(unidadConstructiva.getTIPO_MOVIMIENTO());
    	unidadConstructivaNueva.setTipoRegistro(unidadConstructiva.getTipoRegistro());
    	unidadConstructivaNueva.setTipoUnidad(unidadConstructiva.getTipoUnidad());
    	
    	
    	
    	return unidadConstructivaNueva;
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



	/**
     * @return Returns the datosEconomicos.
     */
    public DatosEconomicosUC getDatosEconomicos()
    {
        return datosEconomicos;
    }
    
    /**
     * @param datosEconomicos The datosEconomicos to set.
     */
    public void setDatosEconomicos(DatosEconomicosUC datosEconomicos)
    {
        this.datosEconomicos = datosEconomicos;
    }
    
    /**
     * @return Returns the datosFisicos.
     */
    public DatosFisicosUC getDatosFisicos()
    {
        return datosFisicos;
    }
    
    /**
     * @param datosFisicos The datosFisicos to set.
     */
    public void setDatosFisicos(DatosFisicosUC datosFisicos)
    {
        this.datosFisicos = datosFisicos;
    }
    
    /**
     * @return Returns the dirUnidadConstructiva.
     */
    public DireccionLocalizacion getDirUnidadConstructiva()
    {
        return dirUnidadConstructiva;
    }
    
    /**
     * @param dirUnidadConstructiva The dirUnidadConstructiva to set.
     */
    public void setDirUnidadConstructiva(DireccionLocalizacion dirUnidadConstructiva)
    {
        this.dirUnidadConstructiva = dirUnidadConstructiva;
    }
    
    /**
     * @return Returns the idUnidadConstructiva.
     */
    public String getIdUnidadConstructiva()
    {
        return idUnidadConstructiva;
    }
    
    /**
     * @param idUnidadConstructiva The idUnidadConstructiva to set.
     */
    public void setIdUnidadConstructiva(String idUnidadConstructiva)
    {
        this.idUnidadConstructiva = idUnidadConstructiva;
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
    
    
    /**
     * @return Returns the codUnidadConstructiva.
     */
    public String getCodUnidadConstructiva()
    {
        return codUnidadConstructiva;
    }
    
    /**
     * @param codUnidadConstructiva The codUnidadConstructiva to set.
     */
    public void setCodUnidadConstructiva(String codUnidadConstructiva)
    {
        this.codUnidadConstructiva = codUnidadConstructiva;
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
    public int getTipoRegistro()
    {
        return tipoRegistro;
    }
    
    /**
     * @param tipoRegistro The tipoRegistro to set.
     */
    public void setTipoRegistro(int tipoRegistro)
    {
        this.tipoRegistro = tipoRegistro;
    }
    
    /**
     * @return Returns the tipoUnidad.
     */
    public String getTipoUnidad()
    {
        return tipoUnidad;
    }
    
    /**
     * @param tipoUnidad The tipoUnidad to set.
     */
    public void setTipoUnidad(String tipoUnidad)
    {
        this.tipoUnidad = tipoUnidad;
    }
    
    /**
     * @return Returns the codDelegacionMEH.
     */
    public String getCodDelegacionMEH()
    {
        return codDelegacionMEH;
    }
    /**
     * @param codDelegacionMEH The codDelegacionMEH to set.
     */
    public void setCodDelegacionMEH(String codDelegacionMEH)
    {
        this.codDelegacionMEH = codDelegacionMEH;
    }
    /**
     * @return Returns the codMunicipioDGC.
     */
    public String getCodMunicipioDGC()
    {
        return codMunicipioDGC;
    }
    /**
     * @param codMunicipioDGC The codMunicipioDGC to set.
     */
    public void setCodMunicipioDGC(String codMunicipioDGC)
    {
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
	
	public Boolean isUCAltaModif(){
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean ucAltaModif = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) || 
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF)){
			ucAltaModif= true;
		}
		return ucAltaModif;
	}
	
	public Boolean isUCElim() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean ucElim = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			ucElim = true;
		}
		return ucElim;
	}
}
