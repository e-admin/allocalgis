/**
 * TimeStampComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;
import com.geopista.app.inventario.component.FechaInventario;
/**
 * Clase necesaria para realizar una ordenación por fecha
 * 
 * @date: 21-12-2010
 * @author: angeles
 * 
 */

public class TimeStampComparator {
	// Para añadir la búsqueda especial por numero de inventario
	public static final Comparator DATE_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {

			DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date date1 = null;
			Date date2 = null;
			try {
				date1 = ((FechaInventario)o1).getFecha();
				date2 = ((FechaInventario)o2).getFecha();
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			 if (date1 == null && date2 == null) {
                 return  0;
             } else if (date1 == null) {
                 return -1;
             } else if (date2 == null) {
                 return 1;
             } 
             else
            	 return date1.compareTo(date2);
			
			 /*if(date1.compareTo(date2)>0){
        		return 1;
        	}else if(date1.compareTo(date2)<0){
        		System.out.println("Date1 is before Date2");
        	}else if(date1.compareTo(date2)==0){
        		System.out.println("Date1 is equal to Date2");
        	}else{
        		System.out.println("How to get here?");
        	}
			
			//System.out.println("Comparando:"+o1+ " con "+ o2+ " Resultado:"+Pdate.compareTo(Qdate));
			return Pdate.compareTo(Qdate) > 0 ? 1 : 0;*/

		}
	};

	private String valor;

	public TimeStampComparator(String valor) {
		this.valor = valor;
	}

	public String toString() {
		return valor;
	}

	public static void main(String[] arg) {
		/*
		 * NumInventario str1= new NumInventario("1.1.2.10"); NumInventario
		 * str2= new NumInventario("1.1.10");
		 * System.out.println("Comparando: "+NUM_INVENTARIO_COMPARATOR
		 * .compare(str1, str2));
		 */

	}

}
