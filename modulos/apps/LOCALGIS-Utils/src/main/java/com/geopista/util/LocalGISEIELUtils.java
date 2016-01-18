/**
 * LocalGISEIELUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalGISEIELUtils {

	
	/**
	 * Metodo que convierte una fecha en formato Date a una fecha en formato String de la forma DD/MM/AAAA
	 *
	 * @param fecha La fecha que se desea convertir.
	 * @return String El resultado.
	 * */
	public static String formatFecha(Date fecha)
	{
		if (fecha == null)
		{
			return "";
		}
		return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
	}
	
	/*
	 * Devuelve values en el mismo orden separados por separator e inserta como primer valor la fase
	 * Si algún valor es null no se inserta.
	 */
	
	public static String getRowToMPT(String fase,String separator,ArrayList values){
		String cadena="";
		
		cadena=fase+separator;
		for (int i = 0; i < values.size()-1; i++) {
			if(values.get(i)!=null){
				if(values.get(i) instanceof java.lang.Double){
				
					cadena+=Double.toString((Double)values.get(i)).replace(".", ",")+separator;
				}else
					cadena+=values.get(i)+separator;		
			}else
				cadena+=separator;
		}
		if(values.get(values.size()-1)!=null)
			if(values.get(values.size()-1) instanceof java.lang.Double){
				
				cadena+=Double.toString((Double)values.get(values.size()-1)).replace(".", ",");
			}else
				cadena+=values.get(values.size()-1);	
		
		return cadena;
	}
	
	public static double redondear(double numero,int digitos)
	{
	      int cifras=(int) Math.pow(10,digitos);
	      return Math.rint(numero*cifras)/cifras;
	}
	

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

}
