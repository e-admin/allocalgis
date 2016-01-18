package com.geopista.app.catastro.model.datos.economicos;

import java.io.Serializable;


public class DatosBaseLiquidableBien implements Serializable
{   
    private Integer ejercicioRevTotal;
    private Integer ejercicioRevParcial;
    private Integer periodoTotal;
    
    /**
     * Ejercicio de efectos IBI del valor catastral, en caso de 
     * procedimientos de valoracion colectiva total posterior a 2002
     */
    private Integer ejercicioIBI;
    
    /**
     * Procedencia del valor base en caso de valoración colectiva posterior a  1997.
     */
    private String procedenciaValorBase;
    
    /**
     * Importe, en caso de existir, del valor base expresado en céntimos de euro, en
     * caso de revisiones totales o valoraciones colectivas totales posteriores a 1997
     */
    private Double valorBase;
    
    
    /**
     * Valor Catastral en el ejercicio de efectos IBI, expresado en céntimos de euro,
     * en caso de procedimientos de valoración colectiva total posterior a 2002.
     */
    private Double valorCatastralIBI;
    
    
    /**
     * Constructor por defecto
     *
     */
    public DatosBaseLiquidableBien()
    {
        super();
    }
    
    
    /**
     * @return Returns the ejercicioRevParcial.
     */
    public Integer getEjercicioRevParcial()
    {
        return ejercicioRevParcial;
    }
    
    /**
     * @param ejercicioRevParcial The ejercicioRevParcial to set.
     */
    public void setEjercicioRevParcial(Integer ejercicioRevParcial)
    {
        this.ejercicioRevParcial = ejercicioRevParcial;
    }
    
    /**
     * @return Returns the ejercicioRevTotal.
     */
    public Integer getEjercicioRevTotal()
    {
        return ejercicioRevTotal;
    }
    
    /**
     * @param ejercicioRevTotal The ejercicioRevTotal to set.
     */
    public void setEjercicioRevTotal(Integer ejercicioRevTotal)
    {
        this.ejercicioRevTotal = ejercicioRevTotal;
    }
    
    /**
     * @return Returns the periodoTotal.
     */
    public Integer getPeriodoTotal()
    {
        return periodoTotal;
    }
    
    /**
     * @param periodoTotal The periodoTotal to set.
     */
    public void setPeriodoTotal(Integer periodoTotal)
    {
        this.periodoTotal = periodoTotal;
    }
    
    /**
     * @return Returns the procedenciaValorBase.
     */
    public String getProcedenciaValorBase()
    {
        return procedenciaValorBase;
    }
    
    /**
     * @param procedenciaValorBase The procedenciaValorBase to set.
     */
    public void setProcedenciaValorBase(String procedenciaValorBase)
    {
        this.procedenciaValorBase = procedenciaValorBase;
    }
    
    /**
     * @return Returns the valorBase.
     */
    public Double getValorBase()
    {
        return valorBase;
    }
    
    /**
     * @param valorBase The valorBase to set.
     */
    public void setValorBase(Double valorBase)
    {
        this.valorBase = valorBase;
    }
    
    
    /**
     * @return Returns the valorCatastralIBI.
     */
    public Double getValorCatastralIBI()
    {
        return valorCatastralIBI;
    }
    
    /**
     * @param valorCatastralIBI The valorCatastralIBI to set.
     */
    public void setValorCatastralIBI(Double valorCatastralIBI)
    {
        this.valorCatastralIBI = valorCatastralIBI;
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
     * @return Returns the ejercicioIBI.
     */
    public Integer getEjercicioIBI()
    {
        return ejercicioIBI;
    }
    
    /**
     * @param ejercicioIBI The ejercicioIBI to set.
     */
    public void setEjercicioIBI(Integer ejercicioIBI)
    {
        this.ejercicioIBI = ejercicioIBI;
    }
    
}
