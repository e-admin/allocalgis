package com.geopista.app.catastro.model.beans;


import java.io.Serializable;
import java.util.ArrayList;

import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosConstruccion;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosConstruccion;

public class ConstruccionCatastro implements Serializable
{
	public String TIPO_MOVIMIENTO;
	
    private String idConstruccion;
    
    private Expediente datosExpediente = new Expediente();
    private String tipoMovimiento;
    
    private String codDelegacionMEH;
    private String codMunicipio;
    private ReferenciaCatastral refParcela = new ReferenciaCatastral("");
    
    /**
     * Número de orden dell bien inmueble fiscal. (Número del Cargo al que se imputa
     * el valor de la construcción dentro de la parcela catastral). Dato no consignado
     * en caso de elementos comunes.
     */
    private String numOrdenBienInmueble;
    /**
     * Número de orden del elemento de construcción.
     */
    private String numOrdenConstruccion;
    
    
    
    private DireccionLocalizacion domicilioTributario = new DireccionLocalizacion();
    private DatosFisicosConstruccion datosFisicos = new DatosFisicosConstruccion();
    private DatosEconomicosConstruccion datosEconomicos = new DatosEconomicosConstruccion();
    
    private ArrayList lstRepartos = new ArrayList();
    
    private FincaCatastro finca;
    
    /**
     * Constructor por defecto
     *
     */
    public ConstruccionCatastro()
    {
    }
    
    
    
    
    public String getTIPO_MOVIMIENTO() {
		return "F";
	}




	public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
	}




	/**
     * @return Returns the datosEconomicos.
     */
    public DatosEconomicosConstruccion getDatosEconomicos()
    {
        return datosEconomicos;
    }
    
    /**
     * @param datosEconomicos The datosEconomicos to set.
     */
    public void setDatosEconomicos(DatosEconomicosConstruccion datosEconomicos)
    {
        this.datosEconomicos = datosEconomicos;
    }
    
    /**
     * @return Returns the datosFisicos.
     */
    public DatosFisicosConstruccion getDatosFisicos()
    {
        return datosFisicos;
    }
    
    /**
     * @param datosFisicos The datosFisicos to set.
     */
    public void setDatosFisicos(DatosFisicosConstruccion datosFisicos)
    {
        this.datosFisicos = datosFisicos;
    }
    
    /**
     * @return Returns the domicilioTributario.
     */
    public DireccionLocalizacion getDomicilioTributario()
    {
        return domicilioTributario;
    }
    
    /**
     * @param domicilioTributario The domicilioTributario to set.
     */
    public void setDomicilioTributario(DireccionLocalizacion domicilioTributario)
    {
        this.domicilioTributario = domicilioTributario;
    }
    
    /**
     * @return Returns the idConstruccion.
     */
    public String getIdConstruccion()
    {
        return idConstruccion;
    }
    
    /**
     * @param idConstruccion The idConstruccion to set.
     */
    public void setIdConstruccion(String idConstruccion)
    {
        this.idConstruccion = idConstruccion;
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
     * @return Returns the codMunicipio.
     */
    public String getCodMunicipio()
    {
        return codMunicipio;
    }
    
    /**
     * @param codMunicipio The codMunicipio to set.
     */
    public void setCodMunicipio(String codMunicipio)
    {
        this.codMunicipio = codMunicipio;
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
     * @return Returns the tipoMovimiento.
     */
    public String getTipoMovimiento()
    {
        return tipoMovimiento;
    }
    
    /**
     * @param tipoMovimiento The tipoMovimiento to set.
     */
    public void setTipoMovimiento(String tipoMovimiento)
    {
        this.tipoMovimiento = tipoMovimiento;
    }
    
    
    
    /**
     * @return Returns the numOrdenBienInmueble.
     */
    public String getNumOrdenBienInmueble()
    {
        return numOrdenBienInmueble;
    }
    
    /**
     * @param numOrdenBienInmueble The numOrdenBienInmueble to set.
     */
    public void setNumOrdenBienInmueble(String numOrdenBienInmueble)
    {
        this.numOrdenBienInmueble = numOrdenBienInmueble;
    }
    
    /**
     * @return Returns the numOrdenConstruccion.
     */
    public String getNumOrdenConstruccion()
    {
        return numOrdenConstruccion;
    }
    
    /**
     * @param numOrdenConstruccion The numOrdenConstruccion to set.
     */
    public void setNumOrdenConstruccion(String numOrdenConstruccion)
    {
        this.numOrdenConstruccion = numOrdenConstruccion;
    }
    
    /**
     * @return Returns the finca.
     */
    public FincaCatastro getFinca()
    {
        return finca;
    }

    /**
     * @param finca The finca to set.
     */
    public void setFinca(FincaCatastro finca)
    {
        this.finca = finca;
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




    /**
     * @return Returns the lstRepartos.
     */
    public ArrayList getLstRepartos()
    {
        return lstRepartos;
    }




    /**
     * @param lstRepartos The lstRepartos to set.
     */
    public void setLstRepartos(ArrayList lstRepartos)
    {
        this.lstRepartos = lstRepartos;
    }
    
    
    public ConstruccionCatastro clone(ConstruccionCatastro construccion)
    {
    	ConstruccionCatastro construccionNueva = new ConstruccionCatastro();
    	
    	construccionNueva.setCodDelegacionMEH(construccion.getCodDelegacionMEH());
    	construccionNueva.setCodMunicipio(construccion.getCodMunicipio());
    	
    	construccionNueva.setDatosEconomicos(new DatosEconomicosConstruccion());
    	construccionNueva.getDatosEconomicos().setCodCategoriaPredominante(construccion.getDatosEconomicos().getCodCategoriaPredominante());
    	construccionNueva.getDatosEconomicos().setCodModalidadReparto(construccion.getDatosEconomicos().getCodModalidadReparto());
    	construccionNueva.getDatosEconomicos().setCodTipoValor(construccion.getDatosEconomicos().getCodTipoValor());
    	construccionNueva.getDatosEconomicos().setCodUsoPredominante(construccion.getDatosEconomicos().getCodUsoPredominante());
    	construccionNueva.getDatosEconomicos().setCorrectorApreciación(construccion.getDatosEconomicos().getCorrectorApreciación());
    	construccionNueva.getDatosEconomicos().setCorrectorVivienda(construccion.getDatosEconomicos().isCorrectorVivienda());
    	
    	construccionNueva.setDatosFisicos(new DatosFisicosConstruccion());
    	construccionNueva.getDatosFisicos().setAnioAntiguedad(construccion.getDatosFisicos().getAnioAntiguedad());
    	construccionNueva.getDatosFisicos().setAnioReforma(construccion.getDatosFisicos().getAnioReforma());
    	construccionNueva.getDatosFisicos().setCodDestino(construccion.getDatosFisicos().getCodDestino());
    	construccionNueva.getDatosFisicos().setCodUnidadConstructiva(construccion.getDatosFisicos().getCodUnidadConstructiva());
    	construccionNueva.getDatosFisicos().setLocalInterior(construccion.getDatosFisicos().isLocalInterior());
    	construccionNueva.getDatosFisicos().setSupImputableLocal(construccion.getDatosFisicos().getSupImputableLocal());
    	construccionNueva.getDatosFisicos().setSupTerrazasLocal(construccion.getDatosFisicos().getSupTerrazasLocal());
    	construccionNueva.getDatosFisicos().setSupTotal(construccion.getDatosFisicos().getSupTotal());
    	construccionNueva.getDatosFisicos().setTipoReforma(construccion.getDatosFisicos().getTipoReforma());
    	
    	construccionNueva.setDomicilioTributario(new DireccionLocalizacion());
    	construccionNueva.getDomicilioTributario().setApartadoCorreos(construccion.getDomicilioTributario().getApartadoCorreos());
    	construccionNueva.getDomicilioTributario().setBloque(construccion.getDomicilioTributario().getBloque());
    	construccionNueva.getDomicilioTributario().setCodigoMunicipioDGC(construccion.getDomicilioTributario().getCodigoMunicipioDGC());
    	construccionNueva.getDomicilioTributario().setCodigoPostal(construccion.getDomicilioTributario().getCodigoPostal());
    	construccionNueva.getDomicilioTributario().setCodigoVia(construccion.getDomicilioTributario().getCodigoVia());
    	construccionNueva.getDomicilioTributario().setCodMunOrigenAgregacion(construccion.getDomicilioTributario().getCodMunOrigenAgregacion());
    	construccionNueva.getDomicilioTributario().setCodParaje(construccion.getDomicilioTributario().getCodParaje());
    	construccionNueva.getDomicilioTributario().setCodParcela(construccion.getDomicilioTributario().getCodParcela());
    	construccionNueva.getDomicilioTributario().setCodPoligono(construccion.getDomicilioTributario().getCodPoligono());
    	construccionNueva.getDomicilioTributario().setCodZonaConcentracion(construccion.getDomicilioTributario().getCodZonaConcentracion());
    	construccionNueva.getDomicilioTributario().setDireccionNoEstructurada(construccion.getDomicilioTributario().getDireccionNoEstructurada());
    	construccionNueva.getDomicilioTributario().setDistrito(construccion.getDomicilioTributario().getDistrito());
    	construccionNueva.getDomicilioTributario().setEscalera(construccion.getDomicilioTributario().getEscalera());
    	construccionNueva.getDomicilioTributario().setIdLocalizacion(construccion.getDomicilioTributario().getIdLocalizacion());
    	construccionNueva.getDomicilioTributario().setIdVia(construccion.getDomicilioTributario().getIdVia());
    	construccionNueva.getDomicilioTributario().setKilometro(construccion.getDomicilioTributario().getKilometro());
    	construccionNueva.getDomicilioTributario().setMunicipioINE(construccion.getDomicilioTributario().getMunicipioINE());
    	construccionNueva.getDomicilioTributario().setNombreEntidadMenor(construccion.getDomicilioTributario().getNombreEntidadMenor());
    	construccionNueva.getDomicilioTributario().setNombreMunicipio(construccion.getDomicilioTributario().getNombreMunicipio());
    	construccionNueva.getDomicilioTributario().setNombreParaje(construccion.getDomicilioTributario().getNombreParaje());
    	construccionNueva.getDomicilioTributario().setNombreProvincia(construccion.getDomicilioTributario().getNombreProvincia());
    	construccionNueva.getDomicilioTributario().setNombreVia(construccion.getDomicilioTributario().getNombreVia());
    	construccionNueva.getDomicilioTributario().setPlanta(construccion.getDomicilioTributario().getPlanta());
    	construccionNueva.getDomicilioTributario().setPrimeraLetra(construccion.getDomicilioTributario().getPrimeraLetra());
    	construccionNueva.getDomicilioTributario().setPrimerNumero(construccion.getDomicilioTributario().getPrimerNumero());
    	construccionNueva.getDomicilioTributario().setProvinciaINE(construccion.getDomicilioTributario().getProvinciaINE());
    	construccionNueva.getDomicilioTributario().setPuerta(construccion.getDomicilioTributario().getPuerta());
    	construccionNueva.getDomicilioTributario().setSegundaLetra(construccionNueva.getDomicilioTributario().getSegundaLetra());
    	construccionNueva.getDomicilioTributario().setSegundoNumero(construccion.getDomicilioTributario().getSegundoNumero());
    	construccionNueva.getDomicilioTributario().setTipoVia(construccion.getDomicilioTributario().getTipoVia());
    	    	
    	construccionNueva.setIdConstruccion(construccion.getIdConstruccion());
    	construccionNueva.setNumOrdenBienInmueble(construccion.getNumOrdenBienInmueble());
    	construccionNueva.setNumOrdenConstruccion(construccion.getNumOrdenConstruccion());
    	construccionNueva.setRefParcela(construccion.getRefParcela());
    	
    	return construccionNueva;
    }
    
    
}
