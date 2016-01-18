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
