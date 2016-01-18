/**
 * ScientificNumberFormat.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades.filteredText;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @(#) ScientificNumberFormat.java	1.0	99/07/27
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
 * @version	1.0 07/27/99
 */
public class ScientificNumberFormat
	implements	UMNumberFormat
{
	DecimalFormat	df	= null;

	/**
	 * Default constructor. By default this constructor makes precision of
	 * showing real numbers 6, so fraction to the digit 6 will be shown.
	 */
	public ScientificNumberFormat()
	{
		df	= new DecimalFormat( "#0.00E0" );
	}

	/**
	 * Constructor with custom precision. Using this precision you can control
	 * how many digits you want to show after the decimal point.
	 *
	 * @param	iPrecision	Number of digits to show after decimal point
	 */
	public ScientificNumberFormat( int iPrecision )
	{
		StringBuffer	szBuffer	= new StringBuffer( iPrecision + 1 );

		if( iPrecision == 0 )
			df	= new DecimalFormat( "#0E0" );
		else
		{
			for( ; iPrecision > 0; iPrecision-- )
				szBuffer.append( '0' );

			df	= new DecimalFormat( "#0." + szBuffer + "E0" );
		}
	}

	/**
	 * Converts string to Real number. This method will convert number in
	 * NNNN.NNN / N,NNN.NNN format to real. The (+)ve or (-)ve sign can be
	 * specified in the string.
	 *
	 * @param	szNumber	number in string format
	 *
	 * @return	Float value
	 */
	public Number parseNumber( String szNumber )
		throws	ParseException
	{
		Number	nNumber	= df.parse( szNumber );

		if( nNumber instanceof Double || nNumber instanceof Float )
			return	nNumber;
		else if( nNumber instanceof Long )
			return	new Double( nNumber.longValue() );
		else
			throw	new ParseException( "Number is not Real.", 0 );
	}

	/**
	 * Converts float number to string format. This method will convert the
	 * number to N,NNN,NNN.NNN format.
	 *
	 * @param	nNumber	real number (positive / negative)
	 */
	public String formatNumber( Number nNumber )
	{
		return	df.format( nNumber.floatValue() );
	}
}
