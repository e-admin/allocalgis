/**
 * DateUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.sicalwin.util;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtils {
	

	/*
	 * ajustarLongitud es una función que ajusta el tamaño de la cadena pasada
	 * por valor a 2 y rellenando de 0 por la izquierda en el caso de que la
	 * longitud sea inferior a 2.
	 */
	public static String ajustarLongitud(String cadena) {
		if (cadena.length() == 1)
			return "0" + cadena;
		else
			return cadena;
	}
	
	public static String formateaFecha(GregorianCalendar calendario) {
		// Obtener la fecha, que debe coincidir con la enviada en el campo fecha
		// de Sec

		// GregorianCalendar calendario = new GregorianCalendar();
		// calendario.setTime(fecha);

		// Obtenemos el CREATED
		StringBuffer created = new StringBuffer();
		created.append(calendario.get(Calendar.YEAR));
		created.append(ajustarLongitud(calendario.get(Calendar.MONTH) + 1 + ""));
		created.append(ajustarLongitud(calendario.get(Calendar.DAY_OF_MONTH)
				+ ""));
		created.append(ajustarLongitud(calendario.get(Calendar.HOUR_OF_DAY)
				+ ""));
		created.append(ajustarLongitud(calendario.get(Calendar.MINUTE) + ""));
		created.append(ajustarLongitud(calendario.get(Calendar.SECOND) + ""));
		return created.toString();
	}
	

	public static String formateaFloat(Float numero) {

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(false);

		String floatFormateado = nf.format(numero);

		return floatFormateado;
	}


}
