/**
 * WeekdayComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.utils;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public class WeekdayComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		if(o1 instanceof Weekdays && o2 instanceof Weekdays){
			Weekdays firstIncident = (Weekdays)o1;
			Weekdays lastIncident = (Weekdays)o2;
			if(firstIncident.equals(Weekdays.MONDAY))
				return -1;
			else if(firstIncident.equals(Weekdays.TUESDAY) && !lastIncident.equals(Weekdays.MONDAY))
				return -1;
			else if(firstIncident.equals(Weekdays.WEDNESDAY) && 
						!(  lastIncident.equals(Weekdays.MONDAY) || 
							lastIncident.equals(Weekdays.TUESDAY
				)))
				return -1;
			else if(firstIncident.equals(Weekdays.THURSDAY) && 
					!(   lastIncident.equals(Weekdays.MONDAY) || 
						lastIncident.equals(Weekdays.TUESDAY) || 
						lastIncident.equals(Weekdays.WEDNESDAY)
					)
				)
				return -1;
			else if(firstIncident.equals(Weekdays.FRIDAY) && 
					(   lastIncident.equals(Weekdays.SATURDAY) ||
						lastIncident.equals(Weekdays.SUNDAY)
					)
				)
				return -1;
			else if(firstIncident.equals(Weekdays.SATURDAY) && 
					lastIncident.equals(Weekdays.SUNDAY))
				return -1;
		}
		return 1;
	}

}
