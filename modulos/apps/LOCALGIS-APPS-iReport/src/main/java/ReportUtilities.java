/**
 * ReportUtilities.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * ReportUtilities.java
 *
 * Created on August 25, 2006, 12:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author gtoffoli
 */
import java.util.Date;
import java.util.GregorianCalendar;

public class ReportUtilities
{
        
	public static Date getFirstDayOfMonth(java.util.Date day)
	{
		if (day == null) day = new Date();
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime( day );
                gc.set( GregorianCalendar.DAY_OF_MONTH, 1);
                gc.set( GregorianCalendar.HOUR, 0);
                gc.set( GregorianCalendar.MINUTE, 0);
                gc.set( GregorianCalendar.SECOND, 0);
                
                return gc.getTime();
	}
        
        public static Date getLastDayOfMonth(java.util.Date day)
	{
		if (day == null) day = new Date();
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime( day );
                int lastMonthDay = gc.getActualMaximum( GregorianCalendar.DAY_OF_MONTH );
                gc.set( GregorianCalendar.DAY_OF_MONTH, lastMonthDay);
                gc.set( GregorianCalendar.HOUR, 23);
                gc.set( GregorianCalendar.MINUTE, 59);
                gc.set( GregorianCalendar.SECOND, 59);
                return gc.getTime();
	}
        
        public static Date getFirstDayOfQuarter(java.util.Date day)
	{
                if (day == null) day = new Date();
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime( day );
                
                // Check in wich quarter we are....
                int currentMonth = gc.get(GregorianCalendar.MONTH);
                if (currentMonth <= GregorianCalendar.MARCH) // From January to March
                {
                    gc.set( gc.get(GregorianCalendar.YEAR), 
                            GregorianCalendar.JANUARY,
                            1);
                }
                else if (currentMonth <= GregorianCalendar.JUNE) // From April to June
                {
                    gc.set( gc.get(GregorianCalendar.YEAR), 
                            GregorianCalendar.APRIL,
                            1);
                }
                else if (currentMonth <= GregorianCalendar.SEPTEMBER) // From July to September
                {
                    gc.set( gc.get(GregorianCalendar.YEAR), 
                            GregorianCalendar.JULY,
                            1);
                }
                else //if (currentMonth <= GregorianCalendar.MARCH) // From Octoner to December
                {
                    gc.set( gc.get(GregorianCalendar.YEAR), 
                            GregorianCalendar.OCTOBER,
                            1);
                }
                return getFirstDayOfMonth( gc.getTime());
	}
        
        public static Date getLastDayOfQuarter(java.util.Date day)
	{
                if (day == null) day = new Date();
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime( day );
                
                // Check in wich quarter we are....
                int currentMonth = gc.get(GregorianCalendar.MONTH);
                if (currentMonth <= GregorianCalendar.MARCH) // From January to March
                {
                    gc.set( gc.get(GregorianCalendar.YEAR), 
                            GregorianCalendar.MARCH,
                            1);
                }
                else if (currentMonth <= GregorianCalendar.JUNE) // From April to June
                {
                    gc.set( gc.get(GregorianCalendar.YEAR), 
                            GregorianCalendar.JUNE,
                            1);
                }
                else if (currentMonth <= GregorianCalendar.SEPTEMBER) // From July to September
                {
                    gc.set( gc.get(GregorianCalendar.YEAR), 
                            GregorianCalendar.SEPTEMBER,
                            1);
                }
                else //if (currentMonth <= GregorianCalendar.MARCH) // From Octoner to December
                {
                    gc.set( gc.get(GregorianCalendar.YEAR), 
                            GregorianCalendar.DECEMBER,
                            1);
                }
                return getLastDayOfMonth(gc.getTime());
	}
        

}
