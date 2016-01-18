package com.geopista.app.eiel;



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
