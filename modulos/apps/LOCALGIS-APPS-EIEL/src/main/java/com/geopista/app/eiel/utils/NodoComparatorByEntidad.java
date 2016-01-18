/**
 * NodoComparatorByEntidad.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.utils;

import java.util.Comparator;

import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;

public class NodoComparatorByEntidad implements Comparator<Object> {
	public int compare(Object o1, Object o2) {
		if (o1 instanceof LCGNucleoEIEL && o2 instanceof LCGNucleoEIEL){
			LCGNucleoEIEL b1 = (LCGNucleoEIEL)o1;
			LCGNucleoEIEL b2 = (LCGNucleoEIEL)o2;
			String codEntidad1=b1.getCodentidad();
			String codEntidad2=b2.getCodentidad();

			if (codEntidad1.compareTo(codEntidad2)>0)
				return 1;
			else if (codEntidad1.compareTo(codEntidad2)<0)
				return -1;
			else
				return 0;
        	   			    	
		}	 
    	return 0;
	}
}