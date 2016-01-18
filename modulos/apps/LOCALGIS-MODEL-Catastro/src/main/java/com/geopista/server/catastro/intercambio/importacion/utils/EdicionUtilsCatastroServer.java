/**
 * EdicionUtilsCatastroServer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.intercambio.importacion.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.geopista.app.catastro.model.beans.ImagenCatastro;

public class EdicionUtilsCatastroServer {

    /**
     ** Rellena una cadena de caracteres S hasta un número N de caracteres con caracteres C
     ** a la izquierda (true) o la derecha (false)
     **/
    public static String paddingString ( String s, int n, char c , boolean paddingLeft  ) {
        if (s==null)
            return null;
        
        StringBuffer str = new StringBuffer(s);
        int strLength  = str.length();
        if ( n > 0 && n > strLength ) {
            for ( int i = 0; i <= n ; i ++ ) {
                if ( paddingLeft ) {
                    if ( i < n - strLength ) str.insert( 0, c );
                }
                else {
                    if ( i > strLength ) str.append( c );
                }
            }
        }
        return str.toString();
    }
    public static String getImagenCatastroXML(ArrayList lstImagenes){
    	
    	String lstImagenesXML = "<limg>";
    	for (Iterator itImagenes = lstImagenes.iterator();itImagenes.hasNext();){

    		ImagenCatastro imagen = (ImagenCatastro)itImagenes.next();
    		lstImagenesXML = lstImagenesXML + "<img>";
    		
    		lstImagenesXML = lstImagenesXML + "<nom>" + imagen.getNombre() + "</nom>";
    		lstImagenesXML = lstImagenesXML + "<frmt>" + imagen.getExtension() + "</frmt>";
    		lstImagenesXML = lstImagenesXML + "<tdo>" + imagen.getTipoDocumento() + "</tdo>";
    		lstImagenesXML = lstImagenesXML + "<foto>" + imagen.getFoto() + "</foto>";
    		
    		lstImagenesXML = lstImagenesXML + "</img>";

    	}
    	lstImagenesXML = lstImagenesXML + "</limg>";
    	
    	return lstImagenesXML;
    }
    
    public static String doubleToStringWithFactor(double d, int factor)
    {
    	
    	NumberFormat form = new DecimalFormat("##");
			
    	 String s =form.format(d*factor);
        //String s = String.valueOf(d*factor);
        if(s.contains(".")){
        	 s=s.substring(0, s.indexOf("."));
        }
       
       
        
        return s;
    }
    public static String floatToStringWithFactor(float f, int factor)
    {
    	NumberFormat form = new DecimalFormat("##");
		
   	 	String s =form.format(f*factor);
       // String s = String.valueOf(f*factor);
        
        s=s.substring(0, s.indexOf("."));
        
        return s;
    }
    
    public static double strToDouble (String value)
    {
        double d = 0;
        if (value.length()!=0)
            d = Double.parseDouble(value);
        
        return d;
    }
    
    public static float strToFloat (String value)
    {
        float f = 0;
        if (value.length()!=0)
            f = Float.parseFloat(value);
        
        return f;
    }
}
