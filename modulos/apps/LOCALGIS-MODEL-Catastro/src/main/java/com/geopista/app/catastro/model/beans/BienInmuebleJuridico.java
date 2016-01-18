/**
 * BienInmuebleJuridico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Titular;

public class BienInmuebleJuridico implements Serializable
{
    
    private BienInmuebleCatastro bienInmueble = new BienInmuebleCatastro();
    private Expediente datosExpediente = new Expediente();
    private ArrayList lstTitulares = new ArrayList();
    private ArrayList lstComBienes; //= new ArrayList();
    
    public static final String TIPO_MOVIMIENTO_VARIACIONES_TITULARIDAD = "I";
    public static final String TIPO_MOVIMIENTO_FINAL ="F";
    public static final String TIPO_MOVIMIENTO_ALTA ="A";
    public static final String TIPO_MOVIMIENTO_BAJA ="B";
    public static final String TIPO_MOVIMIENTO_MODIF ="M";

    public String TIPO_MOVIMIENTO = TIPO_MOVIMIENTO_FINAL;
    public String TIPO_MOVIMIENTO_DELETE;
    
    /**
     * booleano que indica si el bien inmueble se ha eliminado del expediente
     * en la pantalla de edición-
     */
    private boolean delete = false;
    
    // marca si el elemento ha sido modificado,borrado o es nuevo
    // para activar los campos de edicion en la informacion catastral
    private boolean elementoModificado;
    
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
     * @return Returns the datosExpediente.
     */
    public Expediente getDatosExpediente()
    {
        return datosExpediente;
    }
    /**
     * @param expediente The datosExpediente to set.
     */
    public void setDatosExpediente(Expediente expediente)
    {
        this.datosExpediente = expediente;
    }    
    
    /**
     * @return Returns the lstTitulares.
     */
    public ArrayList getLstTitulares()
    {
        return lstTitulares;
    }
    /**
     * @param lstTitulares The lstTitulares to set.
     */
    public void setLstTitulares(ArrayList lstTitulares)
    {
        this.lstTitulares = lstTitulares;
    }
    /**
     * @return Returns the lstComBienes.
     */
    public ArrayList getLstComBienes()
    {
        return lstComBienes;
    }
    /**
     * @param lstComBienes The lstComBienes to set.
     */
    public void setLstComBienes(ArrayList lstComBienes)
    {
        this.lstComBienes = lstComBienes;
    }
    
    
    
    
  /*  public boolean isDelete() {
		return delete;
	}
	*/
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	
	public Boolean getDelete() {
		return new Boolean(isDelete());
	}
    
    public boolean isDelete() {
    	
    	if (TIPO_MOVIMIENTO_DELETE!=null){
    		if( TIPO_MOVIMIENTO_DELETE.equals("B")){
    			delete = true;
    		}
    		else{
    			delete = false;
    		}
    	}
		return delete;
	}
	
    public void setTIPO_MOVIMIENTO_DELETE(String TIPO_MOVIMIENTO_DELETE)
    {
        this.TIPO_MOVIMIENTO_DELETE = TIPO_MOVIMIENTO_DELETE;
    }

    public String getTIPO_MOVIMIENTO_DELETE()
    {
        return TIPO_MOVIMIENTO_DELETE;
    }
    
    public String getTIPO_MOVIMIENTO() {
		return TIPO_MOVIMIENTO;
		//return "F";
	}



	public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
	}
	
	public BienInmuebleJuridico clone(BienInmuebleJuridico bij)
    {
    	BienInmuebleJuridico bijNuevo = new BienInmuebleJuridico();
    	
    	bijNuevo.setBienInmueble(bij.getBienInmueble().clone(bij.getBienInmueble()));
    	
    	bijNuevo.setLstComBienes(bij.getLstComBienes());
    	bijNuevo.setTIPO_MOVIMIENTO(bij.getTIPO_MOVIMIENTO());
    	if (bij.getLstTitulares() != null)
    	{
    		ArrayList lstTitulares = new ArrayList();
    		for (int i=0;i<bij.getLstTitulares().size();i++)
    		{
    			Titular titular = (Titular) bij.getLstTitulares().get(i);
    			Titular titularActual = titular.clone(titular);
    			
    			lstTitulares.add(titularActual);
    		}
    		bijNuevo.setLstTitulares(lstTitulares);
    	}
    	
    	
    	return bijNuevo;
    }
    
    public boolean isElementoModificado() {
		return elementoModificado;
	}

	public void setElementoModificado(boolean elementoModificado) {
		this.elementoModificado = elementoModificado;
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
	
	public Boolean isElemModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) || 
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF) ||
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public Boolean isBienInmuebleJurElim(){
		Boolean elementoBaja = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			elementoBaja= true;
		}
		return elementoBaja;
	}
	
	public Boolean isBienSinTitularidad(){
		Boolean isBienSinTitularidad = false;
		if(getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_ALTA) ||
				getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_BAJA) ||
				getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_MODIFICADO) ||
				getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_VARIACIONES_TITULARIDAD)){
				
				isBienSinTitularidad= true;
				
				if(getLstTitulares() != null && !getLstTitulares().isEmpty()){
					for(int i=0; i<getLstTitulares().size(); i++){
						Titular titular = (Titular)getLstTitulares().get(i);
						if(titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) ||
								titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_BAJA) ||
								titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF)){
							isBienSinTitularidad = false;
						}
						
					}
					
				}
			
			//isBienSinTitularidad = true;
		}
	
		
		return isBienSinTitularidad;
	}
	
	
	public Boolean isBienConTitularidad(){
		Boolean isBienTitularidad = false;
		if(!getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_ALTA) &&
				!getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_BAJA) &&
				!getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_MODIFICADO) ||
				!getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_VARIACIONES_TITULARIDAD)){
			if(getLstTitulares() != null && !getLstTitulares().isEmpty()){
				for(int i=0; i<getLstTitulares().size(); i++){
					Titular titular = (Titular)getLstTitulares().get(i);
					if(titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) ||
							titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_BAJA) ||
							titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF)){
						isBienTitularidad = true;
					}
					
				}
				
			}
		}
		return isBienTitularidad;
	}
	
	public Boolean isBienConModificacionesComunidadBienes(){
		Boolean isBienComunidad = false;

		if(getLstComBienes() != null && !getLstComBienes().isEmpty()){
			for(int i=0; i<getLstComBienes().size(); i++){
				ComunidadBienes comunidad = (ComunidadBienes)getLstComBienes().get(i);
				if(comunidad.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) ||
						comunidad.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_BAJA) ||
						comunidad.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF)){
					isBienComunidad = true;
				}
				
			}
		}
		return isBienComunidad;
	}
	
	public Boolean isBienConModificacionesTitularidad(){
		Boolean isBienTitularidad = false;
		
		if(getLstTitulares() != null && !getLstTitulares().isEmpty()){
			for(int i=0; i<getLstTitulares().size(); i++){
				Titular titular = (Titular)getLstTitulares().get(i);
				if(titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) ||
						titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_BAJA) ||
						titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF)){
					isBienTitularidad = true;
				}
				
			}
			
		}
		return isBienTitularidad;
	}
	
	public Boolean isBienExpTitularidadVariaciones(){
		// un expediente de tipo titularidad y orientado a variaciones
		Boolean isBienExpTitularidadVariaciones = false;
		if(getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_VARIACIONES_TITULARIDAD)){
			if(getLstTitulares() != null && !getLstTitulares().isEmpty()){
				for(int i=0; i<getLstTitulares().size(); i++){
					Titular titular = (Titular)getLstTitulares().get(i);
					if(titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_VARIACIONES_TITULARIDAD)){
						isBienExpTitularidadVariaciones = true;
					}
					
				}
				
			}
		}
		
		return isBienExpTitularidadVariaciones;
	}
}
