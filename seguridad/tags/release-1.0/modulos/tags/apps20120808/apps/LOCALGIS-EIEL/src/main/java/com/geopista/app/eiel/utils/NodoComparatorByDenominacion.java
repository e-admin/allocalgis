package com.geopista.app.eiel.utils;

import java.util.Comparator;

import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;

public class NodoComparatorByDenominacion implements Comparator<Object> {
	public int compare(Object o1, Object o2) {
		if (o1 instanceof LCGNucleoEIEL && o2 instanceof LCGNucleoEIEL){
			LCGNucleoEIEL b1 = (LCGNucleoEIEL)o1;
			LCGNucleoEIEL b2 = (LCGNucleoEIEL)o2;
			String denominacion1=b1.getDenominacion();
			String denominacion2=b2.getDenominacion();
			
			//Los blancos al final
			if (denominacion1.equals(""))
				return 1;
			if (denominacion1.compareTo(denominacion2)>0)
				return 1;
			else if (denominacion1.compareTo(denominacion2)<0)
				return -1;
			else
				return 0;
        	   			    	
		}	 
    	return 0;
	}
}