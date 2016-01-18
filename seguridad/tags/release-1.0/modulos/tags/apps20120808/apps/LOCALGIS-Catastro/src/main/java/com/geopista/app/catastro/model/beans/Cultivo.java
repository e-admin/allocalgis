package com.geopista.app.catastro.model.beans;

import java.util.ArrayList;
import java.io.Serializable;


public class Cultivo implements Serializable
{
	public String TIPO_MOVIMIENTO;
	 
    /**
     * Id del cultivo
     */
    private IdCultivo idCultivo = new IdCultivo();
    /**
     * Datos relativos al expediente
     */
    private Expediente datosExpediente = new Expediente();
    
    /**
     * Código de la delegación
     */
    private String codDelegacionMEH;
    
    /**
     * Código de municipio según DGC
     */
    private String codMunicipioDGC;
    /**
     * Naturaleza del suelo
     */
    private String tipoSuelo;
    
    
    /**
     * Código de la subparcela 
     */
    private String codSubparcela;
    
    /**
     * Tipo de subparcela
     */
    private String tipoSubparcela;
    /**
     * Superficie de la subparcela
     */
    private Long superficieParcela = new Long(0); 
    
   
    /**
     * Denominación de la clase de cultivo
     */
    private String denominacionCultivo;
    
    /**
     * Intensidad productiva
     */
    private Integer intensidadProductiva;
    /**
     * Código de bonificacion
     */
    private String codBonificacion;
    
    /**
     * Año de fin de la bonificación
     */
    private Integer ejercicioFinBonificacion = null;
    /**
     * Valor catastral
     */
    private Double valorCatastral = new Double(0);
    
    /**
     * Código de modalidad de reparto, que se informará solamente si el cultivo es elemento
     * común. La composición del código es la siguiente:
     * Primer carácter: a todos o a alguno (T/A),
     * segundo carácter: cargos (C), 
     * tercer carácter: proporción del reparto, por partes iguales, por superficie de los locales, 
     * por coeficientes de propiedad, o por coeficientes específicamente determinados (1/2/3/4)
     */
    private String codModalidadReparto;
    
    private ArrayList lstRepartos = new ArrayList();
    
    /**
     * Constructor por defecto
     *
     */
    public Cultivo()
    {
        
    }    
    
    
    /**
     * @return Returns the codBonificacion.
     */
    public String getCodBonificacion()
    {
        return codBonificacion;
    }
    
    
    
    
    public String getTIPO_MOVIMIENTO() {
		return "F";
	}


	public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
	}


	/**
     * @param codBonificacion The codBonificacion to set.
     */
    public void setCodBonificacion(String codBonificacion)
    {
        this.codBonificacion = codBonificacion;
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
     * @return Returns the codModalidadReparto.
     */
    public String getCodModalidadReparto()
    {
        return codModalidadReparto;
    }
    
    
    
    /**
     * @param codModalidadReparto The codModalidadReparto to set.
     */
    public void setCodModalidadReparto(String codModalidadReparto)
    {
        this.codModalidadReparto = codModalidadReparto;
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
     * @return Returns the codSubparcela.
     */
    public String getCodSubparcela()
    {
        return codSubparcela;
    }
    
    
    
    /**
     * @param codSubparcela The codSubparcela to set.
     */
    public void setCodSubparcela(String codSubparcela)
    {
        this.codSubparcela = codSubparcela;
    }
    
    
    
    /**
     * @return Returns the denominacionCultivo.
     */
    public String getDenominacionCultivo()
    {
        return denominacionCultivo;
    }
    
    
    
    /**
     * @param denominacionCultivo The denominacionCultivo to set.
     */
    public void setDenominacionCultivo(String denominacionCultivo)
    {
        this.denominacionCultivo = denominacionCultivo;
    }
    
    
    
    /**
     * @return Returns the ejercicioFinBonificacion.
     */
    public Integer getEjercicioFinBonificacion()
    {
        return ejercicioFinBonificacion;
    }
    
    
    
    /**
     * @param ejercicioFinBonificacion The ejercicioFinBonificacion to set.
     */
    public void setEjercicioFinBonificacion(Integer ejercicioFinBonificacion)
    {
        this.ejercicioFinBonificacion = ejercicioFinBonificacion;
    }
    
    
    
    /**
     * @return Returns the intensidadProductiva.
     */
    public Integer getIntensidadProductiva()
    {
        return intensidadProductiva;
    }
    
    
    
    /**
     * @param intensidadProductiva The intensidadProductiva to set.
     */
    public void setIntensidadProductiva(Integer intensidadProductiva)
    {
        this.intensidadProductiva = intensidadProductiva;
    }
    
    
    
    /**
     * @return Returns the superficieParcela.
     */
    public Long getSuperficieParcela()
    {
        return superficieParcela;
    }
    
    
    
    /**
     * @param superficieParcela The superficieParcela to set.
     */
    public void setSuperficieParcela(Long superficieParcela)
    {
        this.superficieParcela = superficieParcela;
    }
    
    
    
    /**
     * @return Returns the tipoSubparcela.
     */
    public String getTipoSubparcela()
    {
        return tipoSubparcela;
    }
    
    
    
    /**
     * @param tipoSubparcela The tipoSubparcela to set.
     */
    public void setTipoSubparcela(String tipoSubparcela)
    {
        this.tipoSubparcela = tipoSubparcela;
    }
    
    
    
    /**
     * @return Returns the tipoSuelo.
     */
    public String getTipoSuelo()
    {
        return tipoSuelo;
    }
    
    
    
    /**
     * @param tipoSuelo The tipoSuelo to set.
     */
    public void setTipoSuelo(String tipoSuelo)
    {
        this.tipoSuelo = tipoSuelo;
    }
    
    /**
     * @return Returns the idCultivo.
     */
    public IdCultivo getIdCultivo()
    {
        return idCultivo;
    }
    
    
    
    /**
     * @param idCultivo The idCultivo to set.
     */
    public void setIdCultivo(IdCultivo idCultivo)
    {
        this.idCultivo = idCultivo;
    }
    
    
    /**
     * @return Returns the valorCatastral.
     */
    public Double getValorCatastral()
    {
        return valorCatastral;
    }
    
    
    
    /**
     * @param valorCatastral The valorCatastral to set.
     */
    public void setValorCatastral(Double valorCatastral)
    {
        this.valorCatastral = valorCatastral;
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
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return idCultivo.hashCode();
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        return idCultivo.equals(obj);
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return idCultivo.toString();
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
    
    public Cultivo clone(Cultivo cultivo)
    {
    	Cultivo cultivoNuevo = new Cultivo();
    	
    	cultivoNuevo.setCodBonificacion(cultivo.getCodBonificacion());
    	cultivoNuevo.setCodDelegacionMEH(cultivo.getCodDelegacionMEH());
    	cultivoNuevo.setCodModalidadReparto(cultivo.getCodModalidadReparto());
    	cultivoNuevo.setCodMunicipioDGC(cultivo.getCodMunicipioDGC());
    	cultivoNuevo.setCodSubparcela(cultivo.getCodSubparcela());
    	cultivoNuevo.setDenominacionCultivo(cultivo.getDenominacionCultivo());
    	cultivoNuevo.setEjercicioFinBonificacion(cultivo.getEjercicioFinBonificacion());
    	
    	cultivoNuevo.setIdCultivo(new IdCultivo());
    	cultivoNuevo.getIdCultivo().setCalifCultivo(cultivo.getIdCultivo().getCalifCultivo());
    	cultivoNuevo.getIdCultivo().setNumOrden(cultivo.getIdCultivo().getNumOrden());
    	cultivoNuevo.getIdCultivo().setParcelaCatastral(cultivo.getIdCultivo().getParcelaCatastral().getRefCatastral());
    	
    	 	
    	cultivoNuevo.setIntensidadProductiva(cultivo.getIntensidadProductiva());
    	cultivoNuevo.setSuperficieParcela(cultivo.getSuperficieParcela());
    	cultivoNuevo.setTipoSubparcela(cultivo.getTipoSubparcela());
    	cultivoNuevo.setTipoSuelo(cultivo.getTipoSuelo());
    	cultivoNuevo.setValorCatastral(cultivo.getValorCatastral());
    	
    	return cultivoNuevo;
    }
    
}
