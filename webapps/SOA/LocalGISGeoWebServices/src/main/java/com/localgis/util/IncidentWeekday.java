/**
 * IncidentWeekday.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.util;

public enum IncidentWeekday {
	MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY;
	public static IncidentWeekday getIncidentByString(String weekday){
		if(weekday.toUpperCase().equals("L"))
			return MONDAY;
		if(weekday.toUpperCase().equals("M"))
			return TUESDAY;
		if(weekday.toUpperCase().equals("X"))
			return WEDNESDAY;
		if(weekday.toUpperCase().equals("J"))
			return THURSDAY;
		if(weekday.toUpperCase().equals("V"))
			return FRIDAY;
		if(weekday.toUpperCase().equals("S"))
			return SATURDAY;
		if(weekday.toUpperCase().equals("D"))
			return SUNDAY;
		return null;
	}
}
