/**
 * ReferenciaCatastral.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

public class ReferenciaCatastral implements Serializable {
    
	/**
	 * Primer código de la referencia catastral. 7 dígitos.
	 */
    private String refCatastral1;
    
    /**
     * Segundo código de la referencia catastral. 7 dígitos.
     */
    private String refCatastral2;
    
    /**
     * Identificador único de la finca = Referencia Catastral, 14 dígitos.
     * refCatastral = refCatastral1 + refCatastral2
     */
    private String refCatastral;
    
    /**
     * Tamaño que debe tener la referencia catastral de la finca.
     */
    private static int TAM_REFERENCIACATASTRAL = 14;
    
    
    /**
     * Constructor de la Referencia catastral. Si la referencia que llega por parámetro tiene 
     * un tamaño inferior a 14, se rellena con ceros hasta completarlo.
     * @param refCatastral String con la referencia.
     */
    public ReferenciaCatastral(String refCatastral) {
        if (refCatastral!=null){
            //rellena el numero de finca registral con 0's delante hasta completar su tamaño
            StringBuffer str = new StringBuffer(refCatastral.replaceAll(" ",""));
            int strLength  = str.length();
            for ( int i = 0; i <= TAM_REFERENCIACATASTRAL ; i ++ ) {
                if (i < TAM_REFERENCIACATASTRAL - strLength) 
                    str.insert( 0, '0' );
            }
            refCatastral = str.toString();
            
            this.refCatastral = refCatastral;	        
            this.refCatastral1 = refCatastral.substring(0, 7);	        
            this.refCatastral2 = refCatastral.substring(7,14);
        }
    }
    
    /**
     * Constructor de la referencia catastral a partir de los dos códigos de la misma. 
     * @param refCatastral1 String con el primer código de la referencia.
     * @param refCatastral2 String con el segundo código de la referencia.
     */
    public ReferenciaCatastral(String refCatastral1, String refCatastral2) {
        setRefCatastral1(refCatastral1.replaceAll(" ",""));
        setRefCatastral2(refCatastral2.replaceAll(" ",""));        
        this.refCatastral = this.refCatastral1 + this.refCatastral2;
    }
    
    /**
     * Devuelve la referencia catastral.
     * @return String con la referencia.
     */
    public String getRefCatastral() {
        return refCatastral;
    }
    
    /**
     * Guarda la referencia catastral. Si el tamaño de la referencia es menor que 14 se rellena
     * con ceros hasta completarlo.
     * @param refCatastral String con la referencia.
     */
    public void setRefCatastral(String refCatastral) {
        if (refCatastral!=null){
            //rellena con 0's delante hasta completar su tamaño
            StringBuffer str = new StringBuffer(refCatastral.replaceAll(" ",""));
            int strLength  = str.length();
            for ( int i = 0; i <= TAM_REFERENCIACATASTRAL ; i ++ ) {
                if (i < TAM_REFERENCIACATASTRAL - strLength) 
                    str.insert( 0, '0' );
            }
            refCatastral = str.toString();
            
            this.refCatastral = refCatastral;           
            this.refCatastral1 = refCatastral.substring(0, 7);          
            this.refCatastral2 = refCatastral.substring(7,14);
        }
    }
    
    /**
     * Devuelve el primer código de la referencia catastral. 
     * @return String con el primer código de la referencia.
     */
    public String getRefCatastral1() {
        return refCatastral1;
    }
    
    
    /**
     * Guarda el primer código de la referencia catastral. Si el tamaño de la referencia es 
     * inferior a 7 se rellena con ceros hasta completar su tamaño.
     * @param refCatastral1 String con el primer código de la referencia.
     */
    public void setRefCatastral1(String refCatastral1) {
        
        if (refCatastral1!=null){
            //rellena con 0's delante hasta completar su tamaño
            StringBuffer str = new StringBuffer(refCatastral1.replaceAll(" ",""));
            int strLength  = str.length();
            for ( int i = 0; i <= TAM_REFERENCIACATASTRAL/2 ; i ++ ) {
                if (i < TAM_REFERENCIACATASTRAL/2 - strLength) 
                    str.insert( 0, '0' );
            }
            this.refCatastral1 = str.toString(); 
        }
    }
    
    /**
     * Devuelve el segundo código de la referencia catastral.
     * @return String con el segundo código de la referencia.
     */
    public String getRefCatastral2() {
        return refCatastral2;
    }
    
    /**
     * Se guarda el segundo código de la referencia catastral. Si el tamaño es inferior a 7, se rellena
     * con ceros hasta completarlo.
     * @param refCatastral2 String con el segundo código de la referencia.
     */
    public void setRefCatastral2(String refCatastral2) {
        
        if (refCatastral2!=null){
            //rellena con 0's delante hasta completar su tamaño
            StringBuffer str = new StringBuffer(refCatastral2.replaceAll(" ",""));
            int strLength  = str.length();
            for ( int i = 0; i <= TAM_REFERENCIACATASTRAL/2 ; i ++ ) {
                if (i < TAM_REFERENCIACATASTRAL/2 - strLength) 
                    str.insert( 0, '0' );
            }                  
            
            this.refCatastral2 = str.toString();  
        }
    }
    
    public boolean equals (Object o)
    {
        if (!(o instanceof ReferenciaCatastral))
            return false;
        
        if (((ReferenciaCatastral)o).getRefCatastral().equalsIgnoreCase(this.getRefCatastral()))
            return true;
        else
            return false;
    }
}
