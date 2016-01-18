/**
 * OrderToMPT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;



/*
 * Ordena los atributos para el formato especificadop por el MPT
 */
public class OrderToMPT {
	
		public OrderToMPT() {
		}
/**
 * Devuelve un array con los nombre de los atributos de object en el orden que se han definido
 * @param object
 * @return
 * @throws IllegalAccessException 
 * @throws IllegalArgumentException 
 */
		public ArrayList getOrder(Object object) throws IllegalArgumentException, IllegalAccessException {
			Field[] order_aux = null;
			ArrayList order=null;
			order_aux=object.getClass().getDeclaredFields();			
			order=new ArrayList<Integer>(order_aux.length);
			for (int i = 0; i < order_aux.length; i++) {
					order.add(i, order_aux[i].get(object));		
				
			}
			return order;
		}
		
		/*public static void main(String args[]) throws IllegalArgumentException, IllegalAccessException
		{
			V_alumbrado_bean pruebaBean= new V_alumbrado_bean();
			pruebaBean.setMunicipio("002");
			OrderToMPT ordenacion=new OrderToMPT();
			ArrayList campos=ordenacion.getOrder(pruebaBean);
			String row=LocalGISEIELServerUtils.getRowToMPT("2011", "|",campos);
			System.out.println(row);
		}*/
		
	
		
//		/*
//		 * Devuelve una cadena de elementos, iniciada por fase y seguida de los atributos de entrada
//		 * 	separados por separator
//		 */
//
//		public String getRowToMPT(String fase,String separator,Object entrada){
//			String cadena=null;
//			if(entrada instanceof V_cap_agua_nucleo_bean){
//				entrada
//				cadena=fase+separator+getProvincia()+separator+getMunicipio()+separator+getEntidad()
//				+separator+getNucleo()+separator+getClave()+separator+getC_provinc()+separator+
//				getC_municip()+separator+getOrden_capt();
//			}
//
//			return cadena;
//		}

}



