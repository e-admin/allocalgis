package com.geopista.app.catastro.model.beans;


import java.io.Serializable;

import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosUC;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosUC;

public class UnidadConstructivaCatastro implements Serializable
{
	 public String TIPO_MOVIMIENTO;
	
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
    
    /**
     * Constructor por defecto
     *
     */
    public UnidadConstructivaCatastro()
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
    
    
    
}
