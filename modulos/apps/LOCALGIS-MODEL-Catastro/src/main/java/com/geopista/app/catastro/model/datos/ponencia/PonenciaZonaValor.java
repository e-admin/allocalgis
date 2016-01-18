/**
 * PonenciaZonaValor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.datos.ponencia;

public class PonenciaZonaValor extends Ponencia
{
    
    private String codZonaValor;
    private TipoValor importesZonaValor;
    private Float valorUnitario;
    private Float valorZonaVerde;
    private Float valorEquipamientos;
    private Float valorSinDesarrollar;
    
    
    public PonenciaZonaValor()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * @return Returns the codZonaValor.
     */
    public String getCodZonaValor()
    {
        return codZonaValor;
    }
    
    
    /**
     * @param codZonaValor The codZonaValor to set.
     */
    public void setCodZonaValor(String codZonaValor)
    {
        this.codZonaValor = codZonaValor;
    }
    
    
    /**
     * @return Returns the importesZonaValor.
     */
    public TipoValor getImportesZonaValor()
    {
        return importesZonaValor;
    }
    
    
    /**
     * @param importesZonaValor The importesZonaValor to set.
     */
    public void setImportesZonaValor(TipoValor importesZonaValor)
    {
        this.importesZonaValor = importesZonaValor;
    }
    
    
    /**
     * @return Returns the valorEquipamientos.
     */
    public Float getValorEquipamientos()
    {
        return valorEquipamientos;
    }
    
    
    /**
     * @param valorEquipamientos The valorEquipamientos to set.
     */
    public void setValorEquipamientos(Float valorEquipamientos)
    {
        this.valorEquipamientos = valorEquipamientos;
    }
    
    
    /**
     * @return Returns the valorSinDesarrollar.
     */
    public Float getValorSinDesarrollar()
    {
        return valorSinDesarrollar;
    }
    
    
    /**
     * @param valorSinDesarrollar The valorSinDesarrollar to set.
     */
    public void setValorSinDesarrollar(Float valorSinDesarrollar)
    {
        this.valorSinDesarrollar = valorSinDesarrollar;
    }
    
    
    /**
     * @return Returns the valorUnitario.
     */
    public Float getValorUnitario()
    {
        return valorUnitario;
    }
    
    
    /**
     * @param valorUnitario The valorUnitario to set.
     */
    public void setValorUnitario(Float valorUnitario)
    {
        this.valorUnitario = valorUnitario;
    }
    
    
    /**
     * @return Returns the valorZonaVerde.
     */
    public Float getValorZonaVerde()
    {
        return valorZonaVerde;
    }
    
    
    /**
     * @param valorZonaVerde The valorZonaVerde to set.
     */
    public void setValorZonaVerde(Float valorZonaVerde)
    {
        this.valorZonaVerde = valorZonaVerde;
    }
    
}
