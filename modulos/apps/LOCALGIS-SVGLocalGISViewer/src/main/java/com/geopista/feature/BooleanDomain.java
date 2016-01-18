/**
 * BooleanDomain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;

import org.satec.sld.SVG.SVGNodeFeature;

/**
 * Un dominio Boolean representa dos posibles valores true/false
 * Su representación en el sistema será 1/0 para facilitar el
 * ajuste a enteros.
 * 
 * El pattern se ignora.
 * 
 * @author juacas
 *
 */
public class BooleanDomain extends PatternBasedDomain {

	/**
	 * 
	 * @param name
	 * @param Description
	 */
	public BooleanDomain(String name, String Description) {
		super(name, Description);
		setAproxLenght(1);
	}

	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getType()
	 */
	public int getType() {
		return Domain.BOOLEAN;
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#validateLocal(com.vividsolutions.jump.feature.Feature, java.lang.String, java.lang.String)
	 */
	protected boolean validateLocal(SVGNodeFeature feature, String Name, Object Value) {

		if (Value==null)
			Value=feature.getAttribute(Name);
		String strVal = Value.toString();
		return "0".equals(strVal) || "1".equals(strVal);

		//return ((Comparable) Value).compareTo(1)==0 || ((Comparable) Value).compareTo("0")==0;

	}

}
