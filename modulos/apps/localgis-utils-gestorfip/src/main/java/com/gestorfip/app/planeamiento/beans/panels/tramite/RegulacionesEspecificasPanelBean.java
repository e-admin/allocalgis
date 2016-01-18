/**
 * RegulacionesEspecificasPanelBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.panels.tramite;

import java.util.Arrays;



public class RegulacionesEspecificasPanelBean {
	
	private int id;
	private int orden;
	private String nombre;
	private String texto;
	private int madre;
	private int determinacion;
	
	private boolean nueva = false;
	private boolean eliminada = false;
	private boolean modificada = false;
	
	private RegulacionesEspecificasPanelBean[] lstRegulacionesEspecificas ;
	
	public boolean isModificada() {
		return modificada;
	}

	public void setModificada(boolean modificada) {
		this.modificada = modificada;
	}
	
	public boolean isNueva() {
		return nueva;
	}

	public void setNueva(boolean regulacionNueva) {
		this.nueva = regulacionNueva;
	}

	public boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(boolean regulacionEliminada) {
		this.eliminada = regulacionEliminada;
	}

	
	
	public RegulacionesEspecificasPanelBean[] getLstRegulacionEspecifica() {
		return lstRegulacionesEspecificas;
	}

	public void setLstRegulacionEspecifica(
			RegulacionesEspecificasPanelBean[] regulacionesEspecificas) {
		this.lstRegulacionesEspecificas = regulacionesEspecificas;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getOrden() {
		return orden;
	}
	
	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getMadre() {
		return madre;
	}
	
	public void setMadre(int madre) {
		this.madre = madre;
	}
	
	public int getDeterminacion() {
		return determinacion;
	}
	
	public void setDeterminacion(int determinacion) {
		this.determinacion = determinacion;
	}

	public void addRegulacionEspecificaHija(RegulacionesEspecificasPanelBean regulacionEspecificaHija) {
		
		if(lstRegulacionesEspecificas == null){
			
			lstRegulacionesEspecificas = new RegulacionesEspecificasPanelBean[1];
			lstRegulacionesEspecificas[0] = regulacionEspecificaHija;
		
		}
		else{
			lstRegulacionesEspecificas = (RegulacionesEspecificasPanelBean[]) Arrays.copyOf(lstRegulacionesEspecificas, 
					lstRegulacionesEspecificas.length+1);
		
			lstRegulacionesEspecificas[lstRegulacionesEspecificas.length-1] = regulacionEspecificaHija;
		}

	}
	
	public static RegulacionesEspecificasPanelBean[] clone (RegulacionesEspecificasPanelBean[] lstRegExpecifica){
		
		RegulacionesEspecificasPanelBean[] lstClone = null;
		for(int h=0; h<lstRegExpecifica.length; h++){
			if(lstRegExpecifica[h] != null){
				RegulacionesEspecificasPanelBean nueva = new  RegulacionesEspecificasPanelBean(); 
				nueva.setId(lstRegExpecifica[h].getId());
				nueva.setOrden(lstRegExpecifica[h].getOrden());
				nueva.setNombre(lstRegExpecifica[h].getNombre());
				nueva.setTexto(lstRegExpecifica[h].getTexto());
				nueva.setMadre(lstRegExpecifica[h].getMadre());
				nueva.setDeterminacion(lstRegExpecifica[h].getDeterminacion());
				
				if(lstRegExpecifica[h].getLstRegulacionEspecifica() != null && 
						lstRegExpecifica[h].getLstRegulacionEspecifica().length != 0){
					
						nueva.setLstRegulacionEspecifica(
								clone(lstRegExpecifica[h].getLstRegulacionEspecifica()));
				}
							
				if(lstClone == null){
	    			
					lstClone = new RegulacionesEspecificasPanelBean[1];
					lstClone[0] = (RegulacionesEspecificasPanelBean)nueva;
	    		
	    		}
	    		else{
	    			lstClone = (RegulacionesEspecificasPanelBean[]) Arrays.copyOf(lstClone, 
	    					lstClone.length+1);
	    		
	    			lstClone[lstClone.length-1] = (RegulacionesEspecificasPanelBean)nueva;
	    		}
			}
		}
	
		
		return lstClone;
	}
}
