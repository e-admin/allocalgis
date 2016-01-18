/**
 * WeekdayIncidentComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.util;

import java.util.Comparator;

public class WeekdayIncidentComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		if(o1 instanceof IncidentWeekday && o2 instanceof IncidentWeekday){
			IncidentWeekday firstIncident = (IncidentWeekday)o1;
			IncidentWeekday lastIncident = (IncidentWeekday)o2;
			if(firstIncident.equals(IncidentWeekday.MONDAY))
				return -1;
			else if(firstIncident.equals(IncidentWeekday.TUESDAY) && !lastIncident.equals(IncidentWeekday.MONDAY))
				return -1;
			else if(firstIncident.equals(IncidentWeekday.WEDNESDAY) && 
						!(  lastIncident.equals(IncidentWeekday.MONDAY) || 
							lastIncident.equals(IncidentWeekday.TUESDAY
				)))
				return -1;
			else if(firstIncident.equals(IncidentWeekday.THURSDAY) && 
					!(   lastIncident.equals(IncidentWeekday.MONDAY) || 
						lastIncident.equals(IncidentWeekday.TUESDAY) || 
						lastIncident.equals(IncidentWeekday.WEDNESDAY)
					)
				)
				return -1;
			else if(firstIncident.equals(IncidentWeekday.FRIDAY) && 
					(   lastIncident.equals(IncidentWeekday.SATURDAY) ||
						lastIncident.equals(IncidentWeekday.SUNDAY)
					)
				)
				return -1;
			else if(firstIncident.equals(IncidentWeekday.SATURDAY) && 
					lastIncident.equals(IncidentWeekday.SUNDAY))
				return -1;
		}
		return 1;
	}

}
