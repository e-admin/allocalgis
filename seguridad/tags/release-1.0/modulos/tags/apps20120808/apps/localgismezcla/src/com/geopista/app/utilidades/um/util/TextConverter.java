/**
 * @(#) TextConverter.java	1.0	99/05/26
 *
 * This code is designed for JDK1.2 and JDK1.1.5
 * Use tab spacing 4. Follow JavaDoc convention while coding.
 * Mail any suggestions or bugs to unicman@iname.com
 */
package	com.geopista.app.utilidades.um.util;

import	com.geopista.app.utilidades.um.util.UMConstants;
import	java.util.Date;
import	java.util.Calendar;
import	java.util.StringTokenizer;

/**
 * All the conversion methods for converting text to some data are in this
 * class. Some/all of them are static methods which can be used without
 * instantiating the class.
 *
 * @author	UnicMan
 * @version	1.0 05/26/99
 */
public class TextConverter
	implements	UMConstants
{
	/**
	 * Converts string in short date format to date. This method internally
	 * calls getShortCalendar and creates Date object from the Calendar
	 * object. This method is provided for backward compatibility.
	 *
	 * @param	szText	date in string format
	 * @return	Date object with converted date
	 */
	public static Date getShortDate( String szText )
	{
		Calendar	cDate	= getShortCalendar(szText);
		if( cDate != null )
			return	cDate.getTime();
		else
			return	null;
	}
	
	/**
	 * Converts string in normal date format to date. This method internally
	 * calls getNormalCalendar and creates Date object from the Calendar
	 * object. This method is provided for backward compatibility.
	 *
	 * @param	szText	date in string format
	 * @return	Date object with converted date
	 */
	public static Date getNormalDate( String szText )
	{
		Calendar	cDate	= getNormalCalendar(szText);
		if( cDate != null )
			return	cDate.getTime();
		else
			return	null;
	}
	
	/**
	 * Converts string in short date format to date. Converts the given string
	 * in date if it is in valid SHORT date format i.e. (MM/DD/YYYY). If the
	 * string is not valid, this function will return null object. The string is
	 * manually analysed. DateFormat is not used, because even if wrong date
	 * (e.g. 33/3/99) is given, its doesn't give error.
	 *
	 * @param	szText	date in string format
	 * @return	Calendar object with converted date
	 */
	public static Calendar getShortCalendar( String szText )
	{
		StringTokenizer	stFields	= new StringTokenizer(szText,""+SHORT_DATE_SEP);

		// There should be only three fields viz. MM,DD,YYYY ....
		if( stFields.countTokens() < 3 )
			return	null;

		String	szMonth	= stFields.nextToken();
		String	szDate	= stFields.nextToken();
		String	szYear	= stFields.nextToken();
		int		iMonth	= -1;
		int		iDate	= -1;
		int		iYear	= -1;
		
		try
		{
			iYear	= Integer.parseInt(szYear);
			if( iYear < 0 )
				return	null;

			// If only two digits of year are specified ...
			if( iYear < 100 )
			{
				// Use the current year ...
				iYear += Calendar.getInstance().get(Calendar.YEAR) / 100 * 100;
			}
		}
		catch( Exception e )	{	return null;	}
		try
		{
			iMonth	= Integer.parseInt(szMonth);
			if( iMonth < 1 || iMonth > 12 )
				return	null;
			iMonth--;
		}
		catch( Exception e )	{	return null;	}
		try
		{
			iDate	= Integer.parseInt(szDate);
			if( iDate < 1 || iDate > getDaysInMonth(iYear,iMonth) )
				return	null;
		}
		catch( Exception e )	{	return null;	}

		// Create the Calendar object with the converted values ...
		Calendar	cDate	= Calendar.getInstance();
		cDate.clear();
		cDate.set( iYear, iMonth, iDate );
		return	cDate;
	}

	/**
	 * Converts string in normal date format to date. Converts the given string
	 * in date if it is in valid date format i.e. (MMM DD, YYYY / DD MMM, YYYY /
	 * MMM DD YYYY / DD MMM YYYY). If the string is not valid, this function
	 * will return null object. The string is manually analysed. DateFormat
	 * is not used.
	 *
	 * @param	szText	date in string format
	 * @return	Calendar object with converted date
	 */
	public static Calendar getNormalCalendar( String szText )
	{
		// Substitute it with spaces so that it won't be included in fields
		// (e.g. 'DD,') ...
		szText = szText.replace( NORMAL_DATE_SEP, ' ' );	

		StringTokenizer	stFields	= new StringTokenizer(szText," ");

		// There should be only three fields viz. MMM,DD,YYYY ....
		if( stFields.countTokens() < 3 )
			return	null;

		String	szMonth	= stFields.nextToken();
		String	szDate	= stFields.nextToken();
		String	szYear	= stFields.nextToken();
		int		iMonth	= -1;
		int		iDate	= -1;
		int		iYear	= -1;
		
		// Month can't start with digit,
		// so if digit is found assume that the string is in format (DD MMM,
		// YYYY)...
		if( Character.isDigit(szMonth.charAt(0)) )
		{
			String	szSwap	= szMonth;
			szMonth			= szDate;
			szDate			= szSwap;
		}
		
		try
		{
			iYear	= Integer.parseInt(szYear);
			if( iYear < 0 )
				return	null;

			// If only two digits of year are specified ...
			if( iYear < 100 )
			{
				// Use the current year ...
				iYear += Calendar.getInstance().get(Calendar.YEAR) / 100 * 100;
			}
		}
		catch( Exception e )	{	return null;	}
		try
		{
			// Disqualify month specification with less than 3 chars ...
			if( szMonth.length() < 3 )
				return	null;
			szMonth = szMonth.toUpperCase();
			for( iMonth=0;
				iMonth < MONTHS.length &&
				!MONTHS[iMonth].toUpperCase().startsWith( szMonth );
				iMonth++
			);
			if( iMonth >= MONTHS.length )
				return	null;
		}
		catch( Exception e )	{	return null;	}
		try
		{
			iDate	= Integer.parseInt(szDate);
			if( iDate < 1 || iDate > getDaysInMonth(iYear,iMonth) )
				return	null;
		}
		catch( Exception e )	{	return null;	}

		// Create the Calendar object with the converted values ...
		Calendar	cDate	= Calendar.getInstance();
		cDate.clear();
		cDate.set( iYear, iMonth, iDate );
		return	cDate;
	}

	/**
	 * Converts string to time. It assumes that the specified time is
	 * referring 24 hour time. The format of time is 'HH:MM:SS.SS'. Minutes,
	 * Seconds and milli-seconds are optional.
	 *
	 * @param	szTime	time in string format
	 * @return	Calndar object with converted time
	 */
	public static Calendar getBase24Time( String szTime )
	{
		StringTokenizer	stFields	= new StringTokenizer( szTime, ""+TIME_SEP );

		// At least HH should be specified ....
		if( stFields.countTokens() < 1 )
			return	null;

		String	szHour	= stFields.nextToken();
		String	szMinute= "";
		String	szSecond= "";
		int		iHour	= -1;
		int		iMinute	= 0;
		int		iSecond	= 0;
		int		iMilli	= 0;
		
		if( stFields.hasMoreTokens() )
			szMinute = stFields.nextToken();
		if( stFields.hasMoreTokens() )
			szSecond = stFields.nextToken();
		
		try
		{
			iHour	= Integer.parseInt(szHour);
			if( iHour < 0 || iHour > 23 )
				return	null;
		}
		catch( Exception e )	{	return null;	}
		try
		{
			if( szMinute.length() != 0 )
			{
				iMinute	= Integer.parseInt(szMinute);
				if( iMinute < 0 || iMinute > 59 )
					return	null;
			}
		}
		catch( Exception e )	{	return null;	}
		try
		{
			if( szSecond.length() != 0 )
			{
				float	fSec	= (new Float(szSecond)).floatValue();
				iSecond	= (int)fSec;
				if( iSecond < 0 || iSecond > 59 )
					return	null;
				iMilli	= (int)((fSec*1000) % 1000);
			}
		}
		catch( Exception e )	{	return null;	}

		// Create the Calendar object with the converted values ...
		Calendar	cTime	= Calendar.getInstance();
		cTime.clear();
		cTime.set( 0,0,0, iHour, iMinute, iSecond );
		if( iMilli != 0 )
			cTime.set( Calendar.MILLISECOND, iMilli );
		return	cTime;
	}

	/**
	 * Converts string to time. It assumes that the specified time is
	 * referring 12 hour time. The format of time is 'HH:MM:SS.SS xx'. Minutes,
	 * Seconds, and milli-seconds are optional. If AM/PM is not
	 * specified or something other than AM/PM is specified, null is returned.
	 *
	 * @param	szTime	time in string format
	 * @return	Calndar object with converted time
	 */
	public static Calendar getBase12Time( String szTime )
	{
		int		iIndex	= szTime.length()-1;
		boolean	bAM		= true;

		// Extract AM/PM if specified ...
		for(
			;	iIndex > 0 &&
				Character.isLetter(szTime.charAt(iIndex))
			;	iIndex-- );
			
		if( iIndex < szTime.length()-1 )
		{
			char	cAMPM	= Character.toUpperCase(szTime.charAt( iIndex+1 ));
			if( cAMPM == 'P' )
				bAM	= false;
			else if( cAMPM != 'A' )
				return	null;
			szTime	= szTime.substring( 0, iIndex+1 );
		}
		else
			return	null;
		
		StringTokenizer	stFields	= new StringTokenizer( szTime, ""+TIME_SEP );

		// At least HH should be specified ....
		if( stFields.countTokens() < 1 )
			return	null;

		String	szHour	= stFields.nextToken();
		String	szMinute= "";
		String	szSecond= "";
		int		iHour	= -1;
		int		iMinute	= 0;
		int		iSecond	= 0;
		int		iMilli	= 0;
		
		if( stFields.hasMoreTokens() )
			szMinute = stFields.nextToken();
		if( stFields.hasMoreTokens() )
			szSecond = stFields.nextToken();
		
		try
		{
			iHour	= Integer.parseInt(szHour);
			if( iHour < 0 || iHour > 12 )
				return	null;

			if( iHour == 12 )
			{
				if( bAM )
					iHour	+= 12;
			}
			else if( !bAM )
				iHour	+= 12;
		}
		catch( Exception e )	{	return null;	}
		try
		{
			if( szMinute.length() != 0 )
			{
				iMinute	= Integer.parseInt(szMinute);
				if( iMinute < 0 || iMinute > 59 )
					return	null;
			}
		}
		catch( Exception e )	{	return null;	}
		try
		{
			if( szSecond.length() != 0 )
			{
				float	fSec	= (new Float(szSecond)).floatValue();
				iSecond	= (int)fSec;
				if( iSecond < 0 || iSecond > 59 )
					return	null;
				iMilli	= (int)((fSec*1000) % 1000);
			}
		}
		catch( Exception e )	{	return null;	}

		// Create the Calendar object with the converted values ...
		Calendar	cTime	= Calendar.getInstance();
		cTime.clear();
		cTime.set( 0,0,0, iHour, iMinute, iSecond );
		if( iMilli != 0 )
			cTime.set( Calendar.MILLISECOND, iMilli );
		return	cTime;
	}

    /** charo */
    public static Calendar getBaseHMTime( String szTime )
    {
        StringTokenizer	stFields	= new StringTokenizer( szTime, ""+TIME_SEP );

        // At least HH should be specified ....
        if( stFields.countTokens() < 1 )
            return	null;

        String	szHour	= stFields.nextToken();
        String	szMinute= "";
        int		iHour	= -1;
        int		iMinute	= 0;

        if( stFields.hasMoreTokens() )
            szMinute = stFields.nextToken();

        try
        {
            iHour	= Integer.parseInt(szHour);
            if( iHour < 0 || iHour > 23 )
                return	null;
        }
        catch( Exception e )	{	return null;	}
        try
        {
            if( szMinute.length() != 0 )
            {
                iMinute	= Integer.parseInt(szMinute);
                if( iMinute < 0 || iMinute > 59 )
                    return	null;
            }
        }
        catch( Exception e )	{	return null;	}
        // Create the Calendar object with the converted values ...
        Calendar	cTime	= Calendar.getInstance();
        cTime.clear();
        cTime.set( 0,0,0, iHour, iMinute);
        return	cTime;
    }


    public static String toBaseHMString( Calendar cTime )
    {
        String szTime="";
        if (cTime.get(Calendar.HOUR_OF_DAY) == 0)
            szTime+="00";
        else if ((cTime.get(Calendar.HOUR_OF_DAY)) > 0 && (cTime.get(Calendar.HOUR_OF_DAY) <=9))
            szTime+="0"+cTime.get(Calendar.HOUR_OF_DAY);
        else szTime+= cTime.get(Calendar.HOUR_OF_DAY);

        szTime+= TIME_SEP;

        if (cTime.get(Calendar.MINUTE) == 0)
            szTime+= "00";
        else if ((cTime.get(Calendar.MINUTE) >= 0) && (cTime.get(Calendar.MINUTE)<=9))
            szTime+= "0"+cTime.get(Calendar.MINUTE);
        else szTime	+= cTime.get(Calendar.MINUTE);

        return	szTime;
    }




	/**
	 * Returns date in string format. The string format this method uses
	 * is short i.e. MM/DD/YYYY.
	 *
	 * @param	cDate	date to convert
	 * @return	date in string format
	 */
	public static String toShortString( Calendar cDate )
	{
		String	szDate	= "" + (cDate.get(Calendar.MONTH)+1);
		szDate	+= SHORT_DATE_SEP;
		szDate	+= cDate.get(Calendar.DAY_OF_MONTH);
		szDate	+= SHORT_DATE_SEP; 
		szDate	+= cDate.get(Calendar.YEAR);
		return	szDate;
	}
	
	/**
	 * Returns date in string format. The string format this method uses
	 * is normal i.e. MMMMM DD, YYYY.
	 *
	 * @param	cDate	date to convert
	 * @return	date in string format
	 */
	public static String toNormalString( Calendar cDate )
	{
		return	MONTHS[ cDate.get(Calendar.MONTH) ] + " " +
				cDate.get(Calendar.DAY_OF_MONTH) + NORMAL_DATE_SEP + " " +
				cDate.get(Calendar.YEAR);
	}

	/**
	 * Returns time in string format. The string format this method uses is 24
	 * based i.e. (HH:MM:SS.SS)
	 *
	 * @param	cTime	time to convert
	 * @return	time in string format
	 */
	public static String toBase24String( Calendar cTime )
	{
		String	szTime	= "" + cTime.get(Calendar.HOUR_OF_DAY);

		szTime	+= TIME_SEP;
		szTime	+= cTime.get(Calendar.MINUTE);
		szTime	+= TIME_SEP;
		szTime	+= (cTime.get(Calendar.SECOND) +
					cTime.get(Calendar.MILLISECOND) / 1000.0);

		return	szTime;
	}

	/**
	 * Returns time in string format. The string format this method uses is 12
	 * based i.e. (HH:MM:SS.SS xx) where 'xx' is AM/PM. Also this method will
	 * show only non-zero seconds.
	 *
	 * @param	cTime	time to convert
	 * @return	time in string format
	 */
	public static String toBase12String( Calendar cTime )
	{
		String	szTime	= "" + cTime.get(Calendar.HOUR);

		szTime	+= TIME_SEP;
		szTime	+= cTime.get(Calendar.MINUTE);
		if( cTime.get(Calendar.SECOND) != 0 )
		{
			szTime	+= TIME_SEP;
			szTime	+= (cTime.get(Calendar.SECOND) +
						cTime.get(Calendar.MILLISECOND) / 1000.0);
		}

		szTime	+= AMPM[cTime.get(Calendar.AM_PM)];

		return	szTime;
	}

	/**
	 * Returns the number of days in a perticular month of a year. This method
	 * returns no. of actual days that will be/was in the specified year and
	 * month. It considers leap year also.
	 *
	 * @param	iYear	year
	 * @param	iMonth	month
	 * @return	number of days in the month
	 */
	public static int getDaysInMonth( int iYear, int iMonth )
	{
		switch( iMonth )
		{
		case 3:
		case 5:
		case 8:
		case 10:
			return	30;
		case 1:		// February
			return	((iYear%4)==0 || (iYear%200)==0) ? 29 : 28;
		default:
			return	31;
		}
	}
}
