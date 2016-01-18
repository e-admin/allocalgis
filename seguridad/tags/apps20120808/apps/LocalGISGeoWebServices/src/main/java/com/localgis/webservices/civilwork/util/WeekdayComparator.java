package com.localgis.webservices.civilwork.util;

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
