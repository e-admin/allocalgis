/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 14-oct-2004 by juacas
 *
 * 
 */
package com.geopista.feature;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public abstract class PatternBasedDomain extends Domain
{

	 /**
	 * Constructor. Inicializa el patrón que define la lógica del
	 * dominio y una descripción.
	 * @param pattern
	 * @param Description
	 */
	public PatternBasedDomain(String pattern, String Description) {
		super(pattern, Description);
		setPattern(pattern);

	}
public PatternBasedDomain()
{}
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
	public boolean validate(Feature feature, String name, Object value)
	{
		if (value==null)value=feature.getAttribute(name);
//		if (value==null) 
//			{
//			if (!isNullable())
//				setLastErrorMessage(Domain.NOT_NULLABLE_MSG);
//			return isNullable();
//			}
	return validateLocal(feature,name,value);
	}
	abstract protected boolean validateLocal(Feature feature, String name, Object value);
}
