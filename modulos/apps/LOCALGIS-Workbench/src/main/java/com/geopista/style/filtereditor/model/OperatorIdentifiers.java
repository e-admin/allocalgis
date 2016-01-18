/**
 * OperatorIdentifiers.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model;

/**
 * @author enxenio s.l.
 *
 */
public class OperatorIdentifiers {
	

	// spatial operations
	public static final int DWITHIN	   = 10;
	public static final int BEYOND     = 8;
	public static final int BBOX       = 9;

	// comparison operations
	public static final int PROPERTYISEQUALTO              = 100;
	public static final int PROPERTYISLESSTHAN             = 101;
	public static final int PROPERTYISGREATERTHAN          = 102;
	public static final int PROPERTYISLESSTHANOREQUALTO    = 103;
	public static final int PROPERTYISGREATERTHANOREQUALTO = 104;
	public static final int PROPERTYISLIKE                 = 105;
	public static final int PROPERTYISNULL                 = 106;
	public static final int PROPERTYISBETWEEN              = 107;

	// logical operations
	public static final int AND = 200;
	public static final int OR  = 201;
	public static final int NOT = 202;

	public static final int UNKNOWN = -1;

	// expressions
	public static final int EXPRESSION_PROPERTYNAME = 1;
	public static final int EXPRESSION_LITERAL = 2;
}
