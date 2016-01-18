package com.geopista.app.catastro.model.beans;

import java.io.Serializable;


public class IdBienInmueble implements Serializable {
    private String idBienInmueble;
    private ReferenciaCatastral parcelaCatastral;
    private String numCargo;
    private String digControl1;
    private String digControl2;
    
    /**
     * Constructor por defecto
     */
    public IdBienInmueble()
    {
    	
    }
    
    public IdBienInmueble(IdBienInmueble idBI)
    {
        this.digControl1 = idBI.getDigControl1();
        this.digControl2 = idBI.getDigControl2();
        this.numCargo = idBI.getNumCargo();
        this.parcelaCatastral = new ReferenciaCatastral(idBI.getParcelaCatastral().getRefCatastral());
        this.idBienInmueble = parcelaCatastral.getRefCatastral() + numCargo + digControl1 + digControl2;
    }
    
    /**
     * Obtiene el identificador completo del bien inmueble
     * @return String con el identificador catastral del bien inmueble
     */
    public String getIdBienInmueble()
    {
        if (parcelaCatastral!=null 
                && numCargo !=null
                && digControl1 !=null
                && digControl2 !=null)
            idBienInmueble = parcelaCatastral.getRefCatastral() + numCargo + digControl1 + digControl2;
        
        return idBienInmueble;
        
    }
    
    public void setIdBienInmueble(String idBienInmueble) {
        if (idBienInmueble.trim().length()==20)
        {
            setParcelaCatastral(idBienInmueble.substring(0,14));
            setNumCargo(idBienInmueble.substring(14,18));
            setDigControl1(idBienInmueble.substring(18,19));
            setDigControl2(idBienInmueble.substring(19,20));
        }
        else if(idBienInmueble.trim().length()==18){
            setParcelaCatastral(idBienInmueble.substring(0,14));
            setNumCargo(idBienInmueble.substring(14,18));
        }
        else if(idBienInmueble.trim().length()==14){
            setParcelaCatastral(idBienInmueble.substring(0,14));
        }
        
        this.idBienInmueble = idBienInmueble;
    }
    
    
    
    /**
     * @return Returns the digControl1.
     */
    public String getDigControl1()
    {
        return digControl1;
    }
    /**
     * @param digControl1 The digControl1 to set.
     */
    public void setDigControl1(String digControl1)
    {
        this.digControl1 = digControl1;
    }
    /**
     * @return Returns the digControl2.
     */
    public String getDigControl2()
    {
        return digControl2;
    }
    /**
     * @param digControl2 The digControl2 to set.
     */
    public void setDigControl2(String digControl2)
    {
        this.digControl2 = digControl2;
    }
    /**
     * @return Returns the numCargo.
     */
    public String getNumCargo()
    {
        return numCargo;
    }
    /**
     * @param numCargo The numCargo to set.
     */
    public void setNumCargo(String numCargo)
    {
        this.numCargo = numCargo;
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
