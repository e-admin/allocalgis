/**
 * DatosBaseLiquidableBien.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
