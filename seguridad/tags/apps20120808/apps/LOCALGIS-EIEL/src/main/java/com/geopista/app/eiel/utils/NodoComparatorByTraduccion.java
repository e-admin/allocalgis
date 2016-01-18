package com.geopista.app.eiel.utils;

import java.util.Comparator;

import com.geopista.app.eiel.beans.filter.LCGNodoEIEL;

public class NodoComparatorByTraduccion implements Comparator {
	public int compare(Object o1, Object o2) {
		if (o1 instanceof LCGNodoEIEL && o2 instanceof LCGNodoEIEL){
			LCGNodoEIEL b1 = (LCGNodoEIEL)o1;
			LCGNodoEIEL b2 = (LCGNodoEIEL)o2;
			String traduccion1=b1.getTraduccion();
			String traduccion2=b2.getTraduccion();
			
			if (traduccion1.compareTo(traduccion2)>0)
				return 1;
			else if (traduccion1.compareTo(traduccion2)<0)
				return -1;
			else
				return 0;
        	   			    	
		}	 
    	return 0;
	}
}
