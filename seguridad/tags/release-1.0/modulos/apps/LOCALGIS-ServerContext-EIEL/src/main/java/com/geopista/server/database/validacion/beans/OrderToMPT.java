package com.geopista.server.database.validacion.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.geopista.server.utils.LocalGISEIELServerUtils;


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
		
		public static void main(String args[]) throws IllegalArgumentException, IllegalAccessException
		{
			V_alumbrado_bean pruebaBean= new V_alumbrado_bean();
			pruebaBean.setMunicipio("002");
			OrderToMPT ordenacion=new OrderToMPT();
			ArrayList campos=ordenacion.getOrder(pruebaBean);
			String row=LocalGISEIELServerUtils.getRowToMPT("2011", "|",campos);
			System.out.println(row);
		}
		
	
		
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



