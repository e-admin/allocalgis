/**
 * OperacionesConFechasObraCivil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.utils;

import java.util.GregorianCalendar;

public class OperacionesConFechasObraCivil {


	/**
	 * Con la fecha dada como parametro devuelve un estring con el retraso con la fecha de hoy.
	 * El formato del retraso es por ejemplo: "2 semanas", "4 meses", "1 año".
	 * @param fechaCaducada
	 * @return
	 */
	public static String obtenerDuracionVencimiento(GregorianCalendar fechaCaducada) {
		GregorianCalendar today = new GregorianCalendar();
 		if (today!=null && fechaCaducada!= null){
			if (fechaCaducada.compareTo(today) > 0){
				return "Fecha Posterior";
			} else if (fechaCaducada.compareTo(today) < 0){
				long difference = today.getTimeInMillis() - fechaCaducada.getTimeInMillis();
				return imprimirDiferencia(fechaCaducada, difference);
			} else{
				return "Hoy";
			}
		} else{
			return "NULL";
		}

	}

	@SuppressWarnings("static-access")
	public static String imprimirDiferencia (GregorianCalendar fechainicial, long diferencia){
		

		long anios = Math.round((diferencia/(1000*60*60*24))) / 365;
		if (anios > 0){
			diferencia = diferencia - (anios * 365 * 24 * 60 * 60 * 1000);
		}
		
		double minutos = diferencia / (1000 * 60);
		long horas = (long) (minutos / 60);
		long dias = horas/24;
		//Calcular meses...

		//Crear vector para almacenar los diferentes dias maximos segun correponda
		String[] mesesAnio = new String[12];
		mesesAnio[0] = "31";

		//validacion de los años bisiestos
		if (fechainicial.isLeapYear(fechainicial.YEAR)){mesesAnio[1] = "29";}else{mesesAnio[1] = "28";}
		mesesAnio[2] = "31";
		mesesAnio[3] = "30";
		mesesAnio[4] = "31";
		mesesAnio[5] = "30";
		mesesAnio[6] = "31";
		mesesAnio[7] = "31";
		mesesAnio[8] = "30";
		mesesAnio[9] = "31";
		mesesAnio[10] = "30";
		mesesAnio[11] = "31";
		int diasRestantes = (int) dias;

		//variable almacenará el total de meses que hay en esos dias
		int totalMeses = 0;
		int mesActual = fechainicial.MONTH;
		//Restar los dias de cada mes desde la fecha de ingreso hasta que ya no queden sufcientes dias para 

		// completar un mes.

		for (int i=0; i<=11; i++ ){

			//Validar año, si sumando 1 al mes actual supera el fin de año, 
			// setea la variable a principio de año 
			if ((mesActual+1)>=12){
				mesActual = i;
			}

			//Validar que el numero de dias resultantes de la resta de las 2 fechas, menos los dias
			//del mes correspondiente sea mayor a cero, de ser asi totalMeses aumenta,continuar hasta 
			//que ya nos se cumpla.
			if ((diasRestantes -Integer.parseInt(mesesAnio[mesActual]))>=0){
				totalMeses ++;
				diasRestantes = diasRestantes- Integer.parseInt(mesesAnio[mesActual]);
				mesActual ++;
			}else{
				break;
			}
		}

		//Resto de horas despues de sacar los dias
		horas = horas % 24;
		String salida ="";

		if (anios > 0){
			if (anios > 1){
				salida = salida+  String.valueOf(anios)+" años";
			} else{
				salida = salida+  String.valueOf(anios)+" año";
			}
		}

		if (totalMeses > 0){
			if (salida.length() > 0){
				if (diasRestantes >0){
					salida = salida + ", ";
				} else {
					salida = salida + " y ";
				}
			}
			if (totalMeses > 1)
				salida = salida+  String.valueOf(totalMeses)+" Meses";
			else
				salida = salida+  String.valueOf(totalMeses)+" Mes";
		}
		
		if (diasRestantes > 0){
			if (salida.length() > 0){
				salida = salida + " y ";
			}
			if (diasRestantes > 1)
				salida = salida+  String.valueOf(diasRestantes)+" Días";
			else
				salida = salida+  String.valueOf(diasRestantes)+" Día";
		}
		// IMPRIMIR HORAS;
		//salida =salida +String.valueOf(horas)+":"+String.valueOf(minuto)+":"+String.valueOf(segundos)+".";
		if (salida.equals("")){
			return "HOY";
		}
		return salida;
	}

}
