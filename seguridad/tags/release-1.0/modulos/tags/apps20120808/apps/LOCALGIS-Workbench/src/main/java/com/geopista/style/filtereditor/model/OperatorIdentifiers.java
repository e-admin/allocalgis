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
