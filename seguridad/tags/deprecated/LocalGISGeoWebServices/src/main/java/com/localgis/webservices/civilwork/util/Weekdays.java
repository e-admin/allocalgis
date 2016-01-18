package com.localgis.webservices.civilwork.util;

public enum Weekdays {
	MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY;
	public static Weekdays getIncidentByString(String weekday){
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
