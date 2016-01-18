/**
 * IdBienInmueble.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
