/**
 * CConstantesPlaneamiento.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.planeamiento;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.geopista.editor.GeopistaEditor;
import com.geopista.security.GeopistaPrincipal;

public class CConstantesPlaneamiento {

	 /** Usuario de login */
    public static GeopistaPrincipal principal;

	public static final String idApp="Planeamiento";
	public static String selectedSubApp="0";    

	public static int IdMunicipio = 34083;
	public static String Provincia = null;
	public static String Municipio = null;

	public static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public static String Locale = "es_ES";

	public static boolean LOCK = false;

	public static String searchValue="";
	public static String calendarValue="";
	
    public static GeopistaEditor geopistaEditor = null;
  
    /** Base de Datos */
    public static String DriverClassName= "";
    public static String ConnectionInfo= "";
    public static String DBUser= "";
    public static String DBPassword= "";

    /** Ayuda */
    public static String helpSetHomeID= "planeamientoIntro";   
    
}