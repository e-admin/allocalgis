/**
 * ConstantesEspacioPublico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.app.gestionciudad;

import java.text.SimpleDateFormat;
import java.util.Hashtable;

import com.geopista.security.GeopistaPrincipal;

/**
 * @author javieraragon
 *
 */
public class ConstantesEspacioPublico {

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
