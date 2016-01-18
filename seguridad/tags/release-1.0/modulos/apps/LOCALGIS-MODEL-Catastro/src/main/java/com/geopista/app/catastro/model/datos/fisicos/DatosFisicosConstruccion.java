package com.geopista.app.catastro.model.datos.fisicos;

import java.io.Serializable;


public class DatosFisicosConstruccion implements Serializable
{  
    /**
     * Año de Antigüedad efectiva en geopista (Para la obtención del valor catastral).
     */
    private Integer anioAntiguedad;
    /**
     * Año de Reforma en caso de existir.
     */
    private Integer anioReforma;
    
    /**
     * Código de unidad constructiva
     */
    private String codUnidadConstructiva;
    
    /**
     * Datos físicos. Código de destino según codificación DGC.
     */
    private String codDestino = "";
    
    
    /**
     * Puede tomar los valores: S o N.
     */
    private Boolean localInterior;
    /**
     * Puede tomar los valores: R, O, E, I o blanco.
     * Indicador tio de reforma si l hubiera a efectos de clacular el coeficiente
     * corrector por antigüedad.
     */
    private String tipoReforma;
    
    /**
     * Superficie imputable al local situada en otras plantas, en metros cuadrados, a
     * título informativo.
     */
    private Long supImputableLocal;
    /**
     * Superficie de porches y terrazas del local en metros cuadrados
     */
    private Long supTerrazasLocal;
    /**
     * Superficie total del local incluyendo porches y terrazas en metros cuadrados.
     */
    private Long supTotal;
    
    
    /**
     * Constructor por defecto
     *
     */
    public DatosFisicosConstruccion()
    {
        
    }
    
    
    /**
     * @return Returns the anioAntiguedad.
     */
    public Integer getAnioAntiguedad()
    {
        return anioAntiguedad;
    }
    
    
    /**
     * @param anioAntiguedad The anioAntiguedad to set.
     */
    public void setAnioAntiguedad(Integer anioAntiguedad)
    {
        this.anioAntiguedad = anioAntiguedad;
    }
    
    
    /**
     * @return Returns the anioReforma.
     */
    public Integer getAnioReforma()
    {
        return anioReforma;
    }
    
    
    /**
     * @param anioReforma The anioReforma to set.
     */
    public void setAnioReforma(Integer anioReforma)
    {
        this.anioReforma = anioReforma;
    }
    
    
    /**
     * @return Returns the codDestino.
     */
    public String getCodDestino()
    {
        return codDestino;
    }
    
    
    /**
     * @param codDestino The codDestino to set.
     */
    public void setCodDestino(String codDestino)
    {
        this.codDestino = codDestino;
    }

    /**
    * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
    * */
    public String getLocalInterior()
    {
        if(localInterior!=null&&localInterior.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    /**
     * @return Returns the localInterior.
     */
    public Boolean isLocalInterior()
    {
        return localInterior;
    }
    
    
    /**
     * @param localInterior The localInterior to set.
     */
    public void setLocalInterior(Boolean localInterior)
    {
        this.localInterior = localInterior;
    }
    
    
    /**
     * @return Returns the supImputableLocal.
     */
    public Long getSupImputableLocal()
    {
        return supImputableLocal;
    }
    
    
    /**
     * @param supImputableLocal The supImputableLocal to set.
     */
    public void setSupImputableLocal(Long supImputableLocal)
    {
        this.supImputableLocal = supImputableLocal;
    }
    
    
    /**
     * @return Returns the supTerrazasLocal.
     */
    public Long getSupTerrazasLocal()
    {
        return supTerrazasLocal;
    }
    
    
    /**
     * @param supTerrazasLocal The supTerrazasLocal to set.
     */
    public void setSupTerrazasLocal(Long supTerrazasLocal)
    {
        this.supTerrazasLocal = supTerrazasLocal;
    }
    
    
    /**
     * @return Returns the supTotal.
     */
    public Long getSupTotal()
    {
        return supTotal;
    }
    
    
    /**
     * @param supTotal The supTotal to set.
     */
    public void setSupTotal(Long supTotal)
    {
        this.supTotal = supTotal;
    }
    
    
    /**
     * @return Returns the tipoReforma.
     */
    public String getTipoReforma()
    {
        return tipoReforma;
    }
    
    
    /**
     * @param tipoReforma The tipoReforma to set.
     */
    public void setTipoReforma(String tipoReforma)
    {
        this.tipoReforma = tipoReforma;
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
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }
    
}
