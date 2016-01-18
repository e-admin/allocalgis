/**
 * GeopistaCommonUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;

import java.io.File;



/**
 * Funciones útiles
 */
public class GeopistaCommonUtils
{
	
	  /**
     * 
     * @param cadena 
     * @param longitud 
     * @return 
     */
	public static String completarConCeros(String cadena, int longitud){

		while (cadena.length()< longitud){
			cadena = '0' + cadena;
		}
		return cadena;
	}
	
	/**
	 * Método para determinar si el fichero existe
	 * 
	 * @param String
	 *            fich, ruta del fichero a comprobar que sea correcto
	 * @return boolean true si es correcto, false en caso contrario
	 */
	public static boolean revisarDirectorio(String fich) {
		try {
			File directorio = new File(fich);
			return directorio.exists();

		} catch (Exception ex) {
			return false;
		}
	}
}