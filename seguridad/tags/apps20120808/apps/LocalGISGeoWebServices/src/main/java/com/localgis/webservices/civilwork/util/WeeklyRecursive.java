package com.localgis.webservices.civilwork.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.naming.ConfigurationException;

@SuppressWarnings("serial")
public class WeeklyRecursive implements Serializable{
	
	public GregorianCalendar getStart() {
		return start;
	}
	public void setStart(GregorianCalendar start) {
		this.start = start;
	}
	public GregorianCalendar getEnd() {
		return end;
	}
	public void setEnd(GregorianCalendar end) {
		this.end = end;
	}
	private ArrayList<Weekdays> weekdayIncidents;
	private GregorianCalendar start;//horas:minutos:segundos
	private GregorianCalendar end;//horas:minutos:segundos

	 
	public String toString(){
		String pattern = "";
		try {
			pattern += buildWeeklyRecursiveIncidentString();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pattern;
	}
	public WeeklyRecursive(String weeklyRecursiveIncident) throws ConfigurationException{
		//Generar a partir de un formato estándar [LMXVD](12:00:00-13:30:00) el objeto cargado
		//TODO: Controlar que pueda llegar un [1L]
		weekdayIncidents = new ArrayList<Weekdays>();
		String weekdays = weeklyRecursiveIncident.substring(1,weeklyRecursiveIncident.indexOf("]"));
		String times = weeklyRecursiveIncident.substring(weeklyRecursiveIncident.indexOf("(")+1,weeklyRecursiveIncident.indexOf(")"));
		if(times.indexOf("-")==-1 ){
			//if(weekdays.length() > 1)
				throw new ConfigurationException("WeeklyRecursiveIncident with StartDate or End date null needs only one IncidentWeekday");
			/*start = new GregorianCalendar();
			start = setTime(times,start);
			setWeekdayIncidents(weekdays);*/
		}else{
			String timeStart = times.substring(0,times.indexOf("-"));
			String timeEnd = times.substring(times.indexOf("-")+1,times.length());
			start = new GregorianCalendar();
			start = setTime(timeStart,start);
			if(timeEnd != null && timeEnd.length() > 0){
				end = new GregorianCalendar();
				end = setTime(timeEnd,end);
			}
			setWeekdayIncidents(weekdays);
		}
		
 
	}
	private void setWeekdayIncidents(String weekdays) {
		for(int i = 1;i<=weekdays.length();i++){
			Weekdays incident = Weekdays.getIncidentByString(weekdays.substring(i-1,i));
			if(incident != null)
				weekdayIncidents.add(incident);
		}	
	}
	private GregorianCalendar setTime(String times,GregorianCalendar calendar) {
		// formato recibido HH:MM:SS
		String[] time = times.split(":");
		calendar.set(2010, 1, 1, Integer.parseInt(time[0]),Integer.parseInt(time[1]), Integer.parseInt(time[2]));
		return calendar;
	}
	public String buildWeeklyRecursiveIncidentString() throws ConfigurationException{
		if(weekdayIncidents.size() == 0)
			throw new ConfigurationException("WeeklyRecursiveIncident needs at least one IncidentWeekday");
		
		String weekdays = "["+getWeekdaysString()+"]";
		String period = "("+getPeridodString()+")";
		return weekdays+period;
	}
	private String getPeridodString() throws ConfigurationException {
		String startTime =  getPeriod(start);
		String endTime =  getPeriod(end);
		if(startTime.equals("") && endTime.equals(""))
			throw new ConfigurationException("WeeklyRecursiveIncident needs at least one StartDate or EndDate");
		if(startTime.equals("")){
			if(weekdayIncidents.size()!=1)
				throw new ConfigurationException("WeeklyRecursiveIncident with StartDate or End date null needs only one IncidentWeekday");
			return getPeriod(end);
		}
		if(endTime.equals("")){
			if(weekdayIncidents.size()!=1)
				throw new ConfigurationException("WeeklyRecursiveIncident with StartDate or End date null needs only one IncidentWeekday");			
			return getPeriod(start);
		}
		return getPeriod(start)+"-"+getPeriod(end);
	}
	private String getPeriod(GregorianCalendar period){
		if(period == null)
			return "";
		return format(period.get(GregorianCalendar.HOUR_OF_DAY),2)+":" + format(period.get(GregorianCalendar.MINUTE),2) + ":" + format(period.get(GregorianCalendar.SECOND),2);
	}
	private String format(int date, int length) {
		String result = date+"";
		while(result.length()<length)
			result = "0"+result;
		return result;
	}
	@SuppressWarnings("unchecked")
	private String getWeekdaysString() {
		Collections.sort(weekdayIncidents,new WeekdayComparator());
		Iterator<Weekdays> it = weekdayIncidents.iterator();
		String result = "";
		while(it.hasNext()){
			Weekdays wday = it.next();
			if(wday.equals(Weekdays.MONDAY))
				result+="L";
			else if(wday.equals(Weekdays.TUESDAY))
				result+="M";
			else if(wday.equals(Weekdays.WEDNESDAY))
				result+="X";
			else if(wday.equals(Weekdays.THURSDAY))
				result+="J";
			else if(wday.equals(Weekdays.FRIDAY))
				result+="V";
			else if(wday.equals(Weekdays.SATURDAY))
				result+="S";
			else if(wday.equals(Weekdays.SUNDAY))
				result+="D";
		}
		return result;
	}
	public boolean isActive(Date date){
		boolean isActive = false;
		GregorianCalendar actualCalendar = new GregorianCalendar();
		actualCalendar.setTime(date);
		int dayOfWeek = actualCalendar.get(GregorianCalendar.DAY_OF_WEEK);
		//Si esta activo para el dia de la semana, evaluar hora minutos y segundos
		if(dayOfWeekCoincidence(dayOfWeek)){
			if(start == null && end == null)
				isActive = true;
			else if(start != null && end == null){
				isActive = evalDates(start,actualCalendar);
			}else{
				if(evalDates(start,actualCalendar))
						isActive = evalDates(actualCalendar,end);
			}
		}
		return isActive;
	}
	private boolean evalDates(GregorianCalendar before, GregorianCalendar after) {
		boolean result = false;
		if(	
				before.get(GregorianCalendar.HOUR_OF_DAY) < after.get(GregorianCalendar.HOUR_OF_DAY) ||
				(
						before.get(GregorianCalendar.HOUR_OF_DAY) == after.get(GregorianCalendar.HOUR_OF_DAY) &&
						before.get(GregorianCalendar.MINUTE) < after.get(GregorianCalendar.MINUTE)
				)||(
						before.get(GregorianCalendar.HOUR_OF_DAY) == after.get(GregorianCalendar.HOUR_OF_DAY) &&
						before.get(GregorianCalendar.MINUTE) == after.get(GregorianCalendar.MINUTE) &&
						before.get(GregorianCalendar.SECOND) <= after.get(GregorianCalendar.SECOND)
				)
				
		  ){
			result = true;
		}
		return result;
	}
	private boolean dayOfWeekCoincidence(int dayOfWeek) {
		Iterator<Weekdays> it = weekdayIncidents.iterator();
		boolean coincidence = false;
		while(it.hasNext() && !coincidence){
			Weekdays iWeekDay = it.next();
			if(
					(dayOfWeek == GregorianCalendar.MONDAY && iWeekDay.equals(Weekdays.MONDAY)) ||
					(dayOfWeek == GregorianCalendar.TUESDAY && iWeekDay.equals(Weekdays.TUESDAY)) ||
					(dayOfWeek == GregorianCalendar.WEDNESDAY && iWeekDay.equals(Weekdays.WEDNESDAY)) ||
					(dayOfWeek == GregorianCalendar.THURSDAY && iWeekDay.equals(Weekdays.THURSDAY)) ||
					(dayOfWeek == GregorianCalendar.FRIDAY && iWeekDay.equals(Weekdays.FRIDAY)) ||
					(dayOfWeek == GregorianCalendar.SATURDAY && iWeekDay.equals(Weekdays.SATURDAY)) ||
					(dayOfWeek == GregorianCalendar.SUNDAY && iWeekDay.equals(Weekdays.SUNDAY))
			
			){
				coincidence = true;
			}
		}
		return coincidence;
	}
	public boolean isActive(){
		return isActive(new Date(System.currentTimeMillis()));
	}
	public ArrayList<Weekdays> getWeekdayIncidents() {
		return weekdayIncidents;
	}
	public void setWeekdayIncidents(ArrayList<Weekdays> weekdayIncidents) {
		this.weekdayIncidents = weekdayIncidents;
	}
}
