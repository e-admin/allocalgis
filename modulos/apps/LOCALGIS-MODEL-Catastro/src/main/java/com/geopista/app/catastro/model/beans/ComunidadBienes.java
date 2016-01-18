/**
 * ComunidadBienes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

import com.geopista.app.AppContext;


public class ComunidadBienes extends Persona implements Serializable {
   
    private String complementoTitularidad;
    private String fechaAlteracion;
    
    public static final String TIPO_MOVIMIENTO_FINAL ="F";
    public static final String TIPO_MOVIMIENTO_ALTA ="A";
    public static final String TIPO_MOVIMIENTO_BAJA ="B";
    public static final String TIPO_MOVIMIENTO_MODIF ="M";
    
    public String TIPO_MOVIMIENTO = TIPO_MOVIMIENTO_FINAL;
    
    private BienInmuebleCatastro bienInmueble;
    
    public ComunidadBienes()
    {
        
    }  
    public ComunidadBienes(String nifCB)
    {
        super(nifCB);
    }  
    
    
    /**
     * @return Returns the bienInmueble.
     */
    public BienInmuebleCatastro getBienInmueble()
    {
        return bienInmueble;
    }
    
    /**
     * @param bienInmueble The bienInmueble to set.
     */
    public void setBienInmueble(BienInmuebleCatastro bienInmueble)
    {
        this.bienInmueble = bienInmueble;
    }
    
    /**
     * @return Returns the complementoTitularidad.
     */
    public String getComplementoTitularidad()
    {
        return complementoTitularidad;
    }
    
    /**
     * @param complementoTitularidad The complementoTitularidad to set.
     */
    public void setComplementoTitularidad(String complementoTitularidad)
    {
        this.complementoTitularidad = complementoTitularidad;
    }
    
    
    /**
     * @return Returns the fechaAlteracion.
     */
    public String getFechaAlteracion()
    {
        return fechaAlteracion;
    }
    
    /**
     * @param fechaAlteracion The fechaAlteracion to set.
     */
    public void setFechaAlteracion(String fechaAlteracion)
    {
        this.fechaAlteracion = fechaAlteracion;
    }
    
    
    public boolean equals(Object o)
    {
        if (!(o instanceof ComunidadBienes) || o==null
                || ((ComunidadBienes)o).getNif()==null)
            return false;
        
        if (((ComunidadBienes)o).getNif().equalsIgnoreCase(this.getNif()))
            return true;
        else 
            return false;
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
    
	public String getTIPO_MOVIMIENTO() {
			return TIPO_MOVIMIENTO;
		//return "F";
	}

	public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
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
	
	public boolean isCatastroTemporalExpVariaciones(){
		boolean isCatTemporalExpVariaciones = false;
		if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
			//si el expediente es de variaciones
			if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
				boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
				if(isCatTemporal){
					isCatTemporalExpVariaciones = true;
				}
			}
		}
		return isCatTemporalExpVariaciones;
	}
}
