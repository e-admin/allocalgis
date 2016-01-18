/*
 * Created on 17-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.inforeferencia;

import java.util.ArrayList;

public class DatosViasCatastro{
    
    String nombreCatastro = null;
    String idVia = null;
    String codigoIne = null;
    String nombreViaIne = null;
    String posicionTipoViaIne = null;
    String tipoViaIne = null;
    
    String normalized=null;

	private ArrayList indiceCoincidencias=new ArrayList();
	private boolean inicialmenteAsociada;

    
    public String getNombreCatastro() {
		return nombreCatastro;
	}
    
	public void setNombreCatastro(String nombreCatastro) {
		if (nombreCatastro==null)
			this.nombreCatastro = null;
		else
			this.nombreCatastro = nombreCatastro.trim();
	}
	
	public String getCodigoIne() {
		return codigoIne;
	}
	
	public void setCodigoIne(String codigoIne) {
		if (codigoIne==null)
			this.codigoIne = null;
		else			
			this.codigoIne = codigoIne.trim();
	}
	
	public String getNombreViaIne() {
		return nombreViaIne;
	}
	
	public void setNombreViaIne(String nombreViaIne) {
		if (nombreViaIne==null)
			this.nombreViaIne = null;
		else
			this.nombreViaIne = nombreViaIne.trim();
	}
	
	public String getPosicionTipoViaIne() {
		return posicionTipoViaIne;
	}
	
	public void setPosicionTipoViaIne(String posicionTipoViaIne) {
		if (posicionTipoViaIne==null)
			this.posicionTipoViaIne = null;
		else
			this.posicionTipoViaIne = posicionTipoViaIne.trim();
	}
	
	public String getTipoViaIne() {
		return tipoViaIne;
	}
	
	public void setTipoViaIne(String tipoViaIne) {
		if (tipoViaIne==null)
			this.tipoViaIne = null;
		else
			this.tipoViaIne = tipoViaIne.trim();
	}
	
    public String getIdVia(){
        return idVia;
    }

    public void setIdVia(String idVia){
        if(idVia==null) 
        	this.idVia=null;
        else 
            this.idVia = idVia.trim();
    }
    
    public String getNormalized(){
    	return normalized;
    }
    
    public void setNormalized(String normalized){
    	this.normalized=normalized;
    }
    

	public void setIndiceCoincidencias(ArrayList indiceCoincidencias) {
		this.indiceCoincidencias=indiceCoincidencias;	
	}
	public ArrayList getIndiceCoincidencias(){
		return indiceCoincidencias;
	}

	public void setInicialmenteAsociada(boolean inicialmenteAsociada) {
		this.inicialmenteAsociada=inicialmenteAsociada;		
	}
	public boolean getInicialmenteAsociada(){
		return this.inicialmenteAsociada;
	}
}
