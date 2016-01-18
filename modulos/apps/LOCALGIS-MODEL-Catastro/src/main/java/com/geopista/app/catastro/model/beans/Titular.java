/**
 * Titular.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

import com.geopista.app.AppContext;


public class Titular extends Persona implements Serializable {       
    private String nifConyuge; 
    private String complementoTitularidad;
    private String fechaAlteracion;
    private String nifCb;
    
    private Derecho derecho = new Derecho();
    
 
    public static final String TIPO_MOVIMIENTO_VARIACIONES_TITULARIDAD = "I";
    public static final String TIPO_MOVIMIENTO_FINAL ="F";
    public static final String TIPO_MOVIMIENTO_ALTA ="A";
    public static final String TIPO_MOVIMIENTO_BAJA ="B";
    public static final String TIPO_MOVIMIENTO_MODIF ="M";
    
    public String TIPO_MOVIMIENTO = TIPO_MOVIMIENTO_FINAL;
    
    // marca si el elemento ha sido modificado,borrado o es nuevo
    // para activar los campos de edicion en la informacion catastral
    private boolean elementoModificado;
    
    /**
     * Constructor por defecto
     */
    public Titular()
    {
        
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
    
    
    
    /**
     * @return Returns the nifConyuge.
     */
    public String getNifConyuge()
    {
        return nifConyuge;
    }
    
    /**
     * @param nifConyuge The nifConyuge to set.
     */
    public void setNifConyuge(String nifConyuge)
    {
        this.nifConyuge = nifConyuge;
    }
       
    /**
     * @return Returns the nifCb.
     */
    public String getNifCb()
    {
        return nifCb;
    }


    /**
     * @param nifCb The nifCb to set.
     */
    public void setNifCb(String nifCb)
    {
        this.nifCb = nifCb;
    }


    /**
     * @return Returns the derecho.
     */
    public Derecho getDerecho() {
        return derecho;
    }
    
    /**
     * @param derecho The derecho to set.
     */
    public void setDerecho(Derecho derecho) {
        this.derecho = derecho;
    }
    
    public Titular clone(Titular titular)
    {
    	Titular titularActual = new Titular();
    	
    	titularActual.setAusenciaNIF(titular.getAusenciaNIF());
    	//titularActual.setBienInmueble(titular.getBienInmueble().clone(titular.getBienInmueble()));
    	titularActual.setCodEntidadMenor(titular.getCodEntidadMenor());
    	titularActual.setComplementoTitularidad(titular.getComplementoTitularidad());
    	titularActual.setDerecho(titular.getDerecho());
    	titularActual.setDomicilio(titular.getDomicilio());
    	titularActual.setExpediente(titular.getExpediente());
    	titularActual.setFechaAlteracion(titular.getFechaAlteracion());
    	titularActual.setNif(titular.getNif());
    	titularActual.setNifCb(titular.getNifCb());
    	titularActual.setNifConyuge(titular.getNifConyuge());
    	titularActual.setRazonSocial(titular.getRazonSocial());
    	
    	titularActual.setTIPO_MOVIMIENTO(titular.getTIPO_MOVIMIENTO());
    	
    	return titularActual;
    }
    
    public boolean isElementoModificado() {
		return elementoModificado;
	}

	public void setElementoModificado(boolean elementoModificado) {
		this.elementoModificado = elementoModificado;
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
