package com.geopista.app.catastro.model.beans;


import java.io.Serializable;

import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosSuelo;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosSuelo;

public class SueloCatastro implements Serializable
{    
	
	 public String TIPO_MOVIMIENTO;
	 
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
    
    /**
     * Constructor por defecto
     *
     */
    public SueloCatastro()
    {
        
    }    
    
    public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
	}
        
    public String getTIPO_MOVIMIENTO() {
		return "F";
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


	
    
}
