/**
 * 
 */
package com.localgis.app.gestionciudad;

import java.text.SimpleDateFormat;
import java.util.Hashtable;

import com.geopista.security.GeopistaPrincipal;

/**
 * @author javieraragon
 *
 */
public class ConstantessLocalGISObraCivil {

	public static GeopistaPrincipal principal;
	@SuppressWarnings("unchecked")
	public static Hashtable permisos= new Hashtable();

	public static int IDMunicipioCompleto= 34083;
	public static String Provincia = null;
	public static String Municipio = null;

	public static String idProvincia = null;
	public static String idMunicipio = null;

	public static int MAPA_GESTIONESPACIOPUBLICO = 150;    
	
	public static String NOMBRE_RED = "Callejero";

	
	public static SimpleDateFormat DateFormat = new SimpleDateFormat("dd-MM-yyyy");
}
