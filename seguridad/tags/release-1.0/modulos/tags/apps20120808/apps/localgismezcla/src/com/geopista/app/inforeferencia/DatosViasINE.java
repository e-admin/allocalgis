/*
 * Created on 17-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.inforeferencia;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DatosViasINE
{
    String codigoViaINE = null;
    String idMunicipio = null;
    String nombreCorto = null;
    String nombreVia = null;
    String posicionVia = null;
    String tipoVia = null;
    String idVia = null;
    String nombreOficial=null;
    
    String normalized=null;
	private boolean asociada;
    
    /**
     * @return Returns the codigoViaINE.
     */
    public String getCodigoViaINE()
    {
        return codigoViaINE;
    }
    /**
     * @param codigoViaINE The codigoViaINE to set.
     */
    public void setCodigoViaINE(String codigoViaINE)
    {
        this.codigoViaINE = codigoViaINE.trim();
    }
    /**
     * @return Returns the idMunicipio.
     */
    public String getIdMunicipio()
    {
        return idMunicipio;
    }
    /**
     * @param idMunicipio The idMunicipio to set.
     */
    public void setIdMunicipio(String idMunicipio)
    {
        this.idMunicipio = idMunicipio.trim();
    }
    /**
     * @return Returns the nombreCorto.
     */
    public String getNombreCorto()
    {
        return nombreCorto;
    }
    /**
     * @param nombreCorto The nombreCorto to set.
     */
    public void setNombreCorto(String nombreCorto)
    {
        this.nombreCorto = nombreCorto.trim();
    }
    /**
     * @return Returns the nombreVia.
     */
    public String getNombreVia()
    {
        return nombreVia;
    }
    /**
     * @param nombreVia The nombreVia to set.
     */
    public void setNombreVia(String nombreVia)
    {
        this.nombreVia = nombreVia.trim();
    }
    /**
     * @return Returns the posicionVia.
     */
    public String getPosicionVia()
    {
        return posicionVia;
    }
    /**
     * @param posicionVia The posicionVia to set.
     */
    public void setPosicionVia(String posicionVia)
    {
        this.posicionVia = posicionVia.trim();
    }
    /**
     * @return Returns the tipoVia.
     */
    public String getTipoVia()
    {
        return tipoVia;
    }
    /**
     * @param tipoVia The tipoVia to set.
     */
    public void setTipoVia(String tipoVia)
    {
        this.tipoVia = tipoVia.trim();
    }
    /**
     * @return Returns the idVia.
     */
    public String getIdVia()
    {
        return idVia;
    }
    /**
     * @param idVia The idVia to set.
     */
    public void setIdVia(String idVia)
    {
        if(idVia==null) this.idVia=null;
        else 
            this.idVia = idVia.trim();
    }
    
    public String getNombreOficial(){
    	return nombreOficial;
    }
    
    public void setNombreOficial(String nombreOficial){
    	this.nombreOficial=nombreOficial;
    }
    
    public String getNormalized(){
    	return normalized;
    }
    
    public void setNormalized(String normalized){
    	this.normalized=normalized;
    }
	public void setAsociada(boolean asociada) {
		this.asociada=asociada;		
	}
	public boolean getAsociada(){
		return asociada;
	}
    
}
