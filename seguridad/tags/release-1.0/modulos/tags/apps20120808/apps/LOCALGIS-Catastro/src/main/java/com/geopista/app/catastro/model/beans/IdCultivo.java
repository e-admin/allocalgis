package com.geopista.app.catastro.model.beans;

import java.io.Serializable;


public class IdCultivo implements Serializable {
    private String idCultivo;
    
    private ReferenciaCatastral parcelaCatastral = new ReferenciaCatastral("");
    
    /**
     * Número de orden del bien inmueble fiscal (Número del Cargo al que se imputa el valor
     * del cultivo dentro de la parcela catastral). 
     * Dato no consignado en caso de elementos comunes
     */
    private String numOrden;
    
    private String califCultivo;
    
    
    /**
     * Constructor por defecto
     */
    public IdCultivo()
    {
        
    }
    
    /**
     * Obtiene el identificador completo del bien inmueble
     * @return String con el identificador catastral del bien inmueble
     */
    public String getIdCultivo()
    {
        if (parcelaCatastral!=null 
                && numOrden !=null
                && califCultivo !=null
        )
            idCultivo = parcelaCatastral.getRefCatastral() + numOrden + califCultivo ;
        
        return idCultivo;  
    }
    
    public void setIdBienInmueble(String idBienInmueble)
    {
        if (idBienInmueble.trim().length()==19)
        {
            setParcelaCatastral(idBienInmueble.substring(0,14));
            setNumOrden(idBienInmueble.substring(14,4));
            setCalifCultivo(idBienInmueble.substring(18,1));            
        }
    }
    
    
    /**
     * @param idCultivo The idCultivo to set.
     */
    public void setIdCultivo(String idCultivo)
    {
        this.idCultivo = idCultivo;
    }
    /**
     * @return Returns the califCultivo.
     */
    public String getCalifCultivo()
    {
        return califCultivo;
    }
    /**
     * @param califCultivo The califCultivo to set.
     */
    public void setCalifCultivo(String califCultivo)
    {
        this.califCultivo = califCultivo;
    }
    
    /**
     * @return Returns the numOrden.
     */
    public String getNumOrden()
    {
        return numOrden;
    }
    /**
     * @param numOrden The numOrden to set.
     */
    public void setNumOrden(String numCargo)
    {
        this.numOrden = numCargo;
    }
    /**
     * @return Returns the parcelaCatastral.
     */
    public ReferenciaCatastral getParcelaCatastral()
    {
        return parcelaCatastral;
    }
    /**
     * @param parcelaCatastral The parcelaCatastral to set.
     */
    public void setParcelaCatastral(String parcelaCatastral)
    {
    	this.parcelaCatastral = new ReferenciaCatastral(parcelaCatastral);
    }    
}
