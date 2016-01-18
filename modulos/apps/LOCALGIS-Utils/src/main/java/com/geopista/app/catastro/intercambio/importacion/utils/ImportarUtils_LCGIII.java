/**
 * ImportarUtils_LCGIII.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.utils;


public class ImportarUtils_LCGIII
{
	
    public static final String HTML_ROJO="<p><font face=SansSerif size=3 color=#ff0000><b>";
    public static final String HTML_VERDE="<p><font face=SansSerif size=3 color=#009900><b>";
    public static final String HTML_NUEVO_PARRAFO="<p><font face=SansSerif size=3>";
    public static final String HTML_FIN_PARRAFO="</b></font></p>";
    public static final String HTML_SALTO="<BR>";
    public static final boolean BORDERS_OFF = false;
    public static final boolean BORDERS_ON = true; 
    
    public final static String LAST_IMPORT_DIRECTORY = "lastImportDirectory";
    public final static String FILE_TO_IMPORT ="fileToImport";
    public final static String FILE_TYPE ="fileType";
    public static final String FILE_TXT_MULTILINE = "txtMultiline";

    /**
     * Convierte un InputStream en String
     * @param is
     * @return
     */
	//LCGIII.Desplazado del paquete LOCALGIS-Catastro
    public String parseISToString(java.io.InputStream is){
        java.io.DataInputStream din = new java.io.DataInputStream(is);
        StringBuffer sb = new StringBuffer();
        try{
            String line = null;
            while((line=din.readLine()) != null){
                sb.append(line+"\n");
            }
        }catch(Exception ex){
            ex.getMessage();
        }finally{
            try{
                is.close();
            }catch(Exception ex){}
        }
        return sb.toString();
    }
    
    /**
     * Convierte un String en in InputStream
     * @param xml
     * @return
     */
    public static java.io.InputStream parseStringToIS(String xml){
        if(xml==null) return null;
        xml = xml.trim();
        java.io.InputStream in = null;
        try{
            in = new java.io.ByteArrayInputStream(xml.getBytes("UTF-8"));
        }catch(Exception ex){
        }
        return in;
    }
    
}

	
