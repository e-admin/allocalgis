package com.geopista.app.utilidades.filteredText;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @(#) IntegerNumberFormat.java	1.0	99/07/19
 *
 * This code is designed for JDK1.1 & JDK1.2
 * Use tab spacing 4. Follow JavaDoc convention while coding.
 * Mail any suggestions or bugs to unicman@iname.com
 */

/**
 * This class is used to validate and show numbers. This class can be used by
 * any number adapters for validations.
 *
 * @author	UnicMan
 * @version	1.0 07/19/99
 */
public class IntegerNumberFormat
	implements	UMNumberFormat
{
	DecimalFormat	df	= new DecimalFormat( "#,##0" );

	/**
	 * Converts string to Integer number. This method will convert number in
	 * NNNN / N,NNN format to integer. The (+)ve or (-)ve sign can be
	 * specified in the string. String with decimal point will not be accepted
	 * by this method.
	 *
	 * @param	szNumber	number in string format
	 *
	 * @return	Long integer
	 */
	public Number parseNumber( String szNumber )
		throws	ParseException
	{
		Number	number	= df.parse( szNumber );

		if( number instanceof Long )
			return	(Long)number;
		else
			throw	new ParseException( "Number is not Long.", 0 );
	}

	/**
	 * Converts Integer number to string format. This method will convert the
	 * number to N,NNN,NNN format.
	 *
	 * @param	nNumber	long integer number (positive / negative)
	 */
	public String formatNumber( Number nNumber )
	{
		return	df.format( nNumber.longValue() );
	}
}
