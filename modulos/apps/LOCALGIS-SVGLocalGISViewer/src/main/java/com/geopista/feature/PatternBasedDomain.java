/**
 * PatternBasedDomain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;

import org.satec.sld.SVG.SVGNodeFeature;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public abstract class PatternBasedDomain extends Domain
{

	public PatternBasedDomain(String name, String Description) {
		super(name, Description);
	}

	/**
	 * Constructor. Inicializa el patrón que define la lógica del
	 * dominio y una descripción.
	 * @param pattern
	 * @param Description
	 */
	public PatternBasedDomain(String name, String Description, String pattern) {
		super(name, Description);
		setPattern(pattern);
	}

	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#setPattern(java.lang.String)
	 */
	public void setPattern(String pattern) {
		// TODO define pattern format and tokenize it to extract aprox length
		super.setPattern(pattern);
		setAproxLenght(10);
	}

	/* Returns the String representation of this domain default value.
	 * A pattern domain don't specifies a default value
	 * @see com.geopista.feature.Domain#getRepresentation()
	 */
	public String getRepresentation() {
		/*
		 * TODO: Trim pattern to get the representation String as literal or format
		 */
		return getDescription();
	}
	public boolean validate(SVGNodeFeature feature, String name, Object value)
	{
		if (value==null)value=feature.getAttribute(name);
//		if (value==null) 
//		{
//		if (!isNullable())
//		setLastErrorMessage(Domain.NOT_NULLABLE_MSG);
//		return isNullable();
//		}
		return validateLocal(feature,name,value);
	}
	abstract protected boolean validateLocal(SVGNodeFeature feature, String name, Object value);
}
