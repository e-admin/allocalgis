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