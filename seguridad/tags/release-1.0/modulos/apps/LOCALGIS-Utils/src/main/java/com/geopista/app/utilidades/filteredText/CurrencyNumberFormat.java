package com.geopista.app.utilidades.filteredText;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @(#) CurrencyNumberFormat.java	1.0	99/07/22
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
 * @version	1.0 07/22/99
 */
public class CurrencyNumberFormat
	implements	UMNumberFormat
{
	DecimalFormat	df	= null;
	DecimalFormat	dfR	= null;	// Had to create separate, as
								// currency sign is a must in
								// actual currency format.

	/**
	 * Default constructor. By default this constructor makes precision of
	 * showing real numbers 6, so fraction to the digit 6 will be shown.
	 */
	public CurrencyNumberFormat()
	{
		df	= new DecimalFormat( "¤#,##0.00" );
		dfR	= new DecimalFormat( "#,##0.00" );
	}

	/**
	 * Constructor with custom precision. Using this precision you can control
	 * how many digits you want to show after the decimal point.
	 *
	 * @param	iPrecision	Number of digits to show after decimal point
	 */
	public CurrencyNumberFormat( int iPrecision )
	{
		StringBuffer	szBuffer	= new StringBuffer( iPrecision + 1 );

		if( iPrecision == 0 )
		{
			df	= new DecimalFormat( "¤#,##0" );
			dfR	= new DecimalFormat( "#,##0" );
		}
		else
		{
			for( ; iPrecision > 0; iPrecision-- )
				szBuffer.append( '0' );

			df	= new DecimalFormat( "¤#,##0." + szBuffer );
			dfR	= new DecimalFormat( "#,##0." + szBuffer );
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
		Number	nNumber	= null;

		try
		{	nNumber	= df.parse( szNumber );	}
		catch( ParseException e )
		{	nNumber	= dfR.parse( szNumber );	}

		if( nNumber instanceof Double || nNumber instanceof Float )
			return	nNumber;
		else if( nNumber instanceof Long )
			return	new Double( nNumber.longValue() );
		else
			throw	new ParseException( "Number is not valid.", 0 );
	}

	/**
	 * Converts float number to string format. This method will convert the
	 * number to N,NNN,NNN.NNN format.
	 *
	 * @param	nNumber	real number (positive / negative)
	 */
	public String formatNumber( Number nNumber )
	{
		return	df.format( nNumber.doubleValue() );
	}
}
