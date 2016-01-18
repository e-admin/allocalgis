package com.geopista.protocol.inventario;

import java.util.Comparator;
import java.util.StringTokenizer;


/**
 * Clase necesaria para realizar una ordenación por
 * número de intentario en el table model
 * @date: 21-12-2010
 * @author: angeles
 *
 */

public class NumInventario{
	//Para añadir la búsqueda especial por numero de inventario
    public static final Comparator NUM_INVENTARIO_COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
        	
        	String cadena1=(((NumInventario)o1).valor).replaceAll("_", ".");
        	String cadena2=(((NumInventario)o2).valor).replaceAll("_", ".");
        	StringTokenizer stk1 = new StringTokenizer(cadena1,".");
        	StringTokenizer stk2 = new StringTokenizer(cadena2,".");
        	//StringTokenizer stk1 = new StringTokenizer(o1.toString(),".");
        	//StringTokenizer stk2 = new StringTokenizer(o2.toString(),".");
    		for (;stk1.countTokens()<stk2.countTokens()?stk1.hasMoreTokens():stk2.hasMoreTokens();){
    			String token1 = stk1.nextToken();
    			String token2 = stk2.nextToken();
    			try{
    				long num1=new Long(token1).longValue();
    				long num2=new Long(token2).longValue();
    				if (num1<num2)return -1;
    				if (num1>num2)return 1;
    			}catch (Exception ex){
    				return token1.compareTo(token2);
    			}
    		}
            return stk1.countTokens()<stk2.countTokens()?-1:1;
        }
    };

	private String valor;
	public NumInventario(String valor){
		this.valor=valor;
	}
	public String toString(){
		return valor;
	}
	public static void main(String[] arg){
		NumInventario str1= new NumInventario("1.1.2.10");
		NumInventario str2= new NumInventario("1.1.10");
		System.out.println("Comparando: "+NUM_INVENTARIO_COMPARATOR.compare(str1, str2));
		
		
	}

}
