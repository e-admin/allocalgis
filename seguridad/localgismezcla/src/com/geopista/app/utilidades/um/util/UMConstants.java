/**
 * @(#) UMConstants.java	1.0	99/05/27
 *
 * This code is designed for JDK1.2 and JDK1.1.5
 * Use tab spacing 4. Follow JavaDoc convention while coding.
 * Mail any suggestions or bugs to unicman@iname.com
 */
package	com.geopista.app.utilidades.um.util;

import	java.text.DateFormatSymbols;

/**
 * This is a interface containing all the constants needed for the package
 * 'um'. For using the constants in this interface just implement this
 * interface.
 *
 * @author	UnicMan
 * @version	1.0 05/27/99
 */
public interface UMConstants
{
/*
	public static final String	MONTHS[] =
	{
		"January",	"February",	"March",	"April",		"May",
		"June",		"July",		"August",	"September",	"October",
		"November",	"December"
	};

	public static final String	WEEKS[] =
	{
		"Sunday",	"Monday",	"Tuesday",	"Wednesday",	"Thursday",
		"Friday",	"Saturday"
	};
*/

	public static final DateFormatSymbols	dfSymb	= new DateFormatSymbols();

	public static final String	MONTHS[]		= dfSymb.getMonths();

	public static final String	AMPM[]			= dfSymb.getAmPmStrings();

	public static final String	WEEKS[]			= dfSymb.getWeekdays();

	public static final char	SHORT_DATE_SEP	= '/';

	public static final char	NORMAL_DATE_SEP	= ',';

	public static final char	TIME_SEP		= ':';

}
