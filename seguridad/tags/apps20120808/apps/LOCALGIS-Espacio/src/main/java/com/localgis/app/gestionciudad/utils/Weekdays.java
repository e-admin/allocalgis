package com.localgis.app.gestionciudad.utils;

import java.util.ArrayList;
import java.util.Collection;

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
	
	public static Collection<Weekdays> weekdaysToCollection(){
		ArrayList<Weekdays> list = new ArrayList<Weekdays>();
		for(int i=0; i < Weekdays.values().length; i++){
			list.add(Weekdays.values()[i]);
		}
		return list;
	}
	
}
