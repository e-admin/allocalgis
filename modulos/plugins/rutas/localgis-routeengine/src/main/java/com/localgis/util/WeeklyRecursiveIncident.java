/**
 * WeeklyRecursiveIncident.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.naming.ConfigurationException;

public class WeeklyRecursiveIncident implements Serializable{
	
	private ArrayList<IncidentWeekday> weekdayIncidents;
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
	public WeeklyRecursiveIncident(String weeklyRecursiveIncident) throws ConfigurationException{
		//Generar a partir de un formato estandar [LMXVD](12:00:00-13:30:00) el objeto cargado
		//TODO: Controlar que pueda llegar un [1L]
		weekdayIncidents = new ArrayList<IncidentWeekday>();
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
			end = new GregorianCalendar();
			start = setTime(timeStart,start);
			end = setTime(timeEnd,end);
			setWeekdayIncidents(weekdays);
		}
		
 
	}
	private void setWeekdayIncidents(String weekdays) {
		for(int i = 1;i<=weekdays.length();i++){
			IncidentWeekday incident = IncidentWeekday.getIncidentByString(weekdays.substring(i-1,i));
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
	private String getWeekdaysString() {
		Collections.sort(weekdayIncidents,new WeekdayIncidentComparator());
		Iterator<IncidentWeekday> it = weekdayIncidents.iterator();
		String result = "";
		while(it.hasNext()){
			IncidentWeekday wday = it.next();
			if(wday.equals(IncidentWeekday.MONDAY))
				result+="L";
			else if(wday.equals(IncidentWeekday.TUESDAY))
				result+="M";
			else if(wday.equals(IncidentWeekday.WEDNESDAY))
				result+="X";
			else if(wday.equals(IncidentWeekday.THURSDAY))
				result+="J";
			else if(wday.equals(IncidentWeekday.FRIDAY))
				result+="V";
			else if(wday.equals(IncidentWeekday.SATURDAY))
				result+="S";
			else if(wday.equals(IncidentWeekday.SUNDAY))
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
		Iterator<IncidentWeekday> it = weekdayIncidents.iterator();
		boolean coincidence = false;
		while(it.hasNext() && !coincidence){
			IncidentWeekday iWeekDay = it.next();
			if(
					(dayOfWeek == GregorianCalendar.MONDAY && iWeekDay.equals(IncidentWeekday.MONDAY)) ||
					(dayOfWeek == GregorianCalendar.TUESDAY && iWeekDay.equals(IncidentWeekday.TUESDAY)) ||
					(dayOfWeek == GregorianCalendar.WEDNESDAY && iWeekDay.equals(IncidentWeekday.WEDNESDAY)) ||
					(dayOfWeek == GregorianCalendar.THURSDAY && iWeekDay.equals(IncidentWeekday.THURSDAY)) ||
					(dayOfWeek == GregorianCalendar.FRIDAY && iWeekDay.equals(IncidentWeekday.FRIDAY)) ||
					(dayOfWeek == GregorianCalendar.SATURDAY && iWeekDay.equals(IncidentWeekday.SATURDAY)) ||
					(dayOfWeek == GregorianCalendar.SUNDAY && iWeekDay.equals(IncidentWeekday.SUNDAY))
			
			){
				coincidence = true;
			}
		}
		return coincidence;
	}
	public boolean isActive(){
		return isActive(new Date(System.currentTimeMillis()));
	}
	public ArrayList<IncidentWeekday> getWeekdayIncidents() {
		return weekdayIncidents;
	}
	public void setWeekdayIncidents(ArrayList<IncidentWeekday> weekdayIncidents) {
		this.weekdayIncidents = weekdayIncidents;
	}
}
