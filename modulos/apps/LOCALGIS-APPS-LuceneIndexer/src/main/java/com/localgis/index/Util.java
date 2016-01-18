/**
 * Util.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.index;

import java.util.Arrays;

public class Util {
	
	/**
	 * Comprueba si la cadena item está permitida por la lista definida en la cadena (separada por comas)
	 * list
	 * @param list
	 * @param item
	 * @return
	 */
	public static boolean included(String list, String item){
		list=list.replaceAll(" ", "");
		
		String[] ls=list.split("[,;]");
	    //log.debug("Comprobando lista "+list +":"+Arrays.asList(ls));
		
		if (Arrays.asList(ls).contains(item) || Arrays.asList(ls).contains("ALL"))
			return true;
		else return false;
	}
	
	/**
	 * Dada una expresión de un parámetro de entrada parametro=valor, retorna el valor
	 * @param parameterString
	 * @return
	 */
	public static String getArgumentTokenValue(String parameterString){
		int pos=parameterString.indexOf('=');
		return parameterString.substring(pos+1);
	}

}
