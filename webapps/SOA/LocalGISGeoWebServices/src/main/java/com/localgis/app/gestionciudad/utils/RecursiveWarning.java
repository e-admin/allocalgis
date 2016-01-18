/**
 * RecursiveWarning.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.naming.ConfigurationException;

@SuppressWarnings("serial")
public class RecursiveWarning implements Serializable{
	
	private RecursiveType incidentType;
	private GregorianCalendar start;
	private GregorianCalendar end;
	private ArrayList<WeeklyRecursive> weeklyRecursiveIncident;
	
	
	
	/**
	 * @return the incidentType
	 */
	public RecursiveType getIncidentType() {
		return incidentType;
	}

	/**
	 * @param incidentType the incidentType to set
	 */
	public void setIncidentType(RecursiveType incidentType) {
		this.incidentType = incidentType;
	}

	/**
	 * @return the start
	 */
	public GregorianCalendar getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(GregorianCalendar start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public GregorianCalendar getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(GregorianCalendar end) {
		this.end = end;
	}

	/**
	 * @return the weeklyRecursiveIncident
	 */
	public ArrayList<WeeklyRecursive> getWeeklyRecursiveIncident() {
		return weeklyRecursiveIncident;
	}

	/**
	 * @param weeklyRecursiveIncident the weeklyRecursiveIncident to set
	 */
	public void setWeeklyRecursiveIncident(
			ArrayList<WeeklyRecursive> weeklyRecursiveIncident) {
		this.weeklyRecursiveIncident = weeklyRecursiveIncident;
	}

	public RecursiveWarning(){
		super();
	}
	
	public String toString(){
		String result = buildRecursiveIncidentString();
		if(weeklyRecursiveIncident.size()>0){
			if(result.length()>0)
				result+=",";
			result += buildWeeklyIncidentsString() ;
		}
		return result;
		
	}
	private String buildWeeklyIncidentsString(){
		Iterator<WeeklyRecursive> it = weeklyRecursiveIncident.iterator();
		String result = "";
		while(it.hasNext()){
			WeeklyRecursive incident = it.next();
			try {
				result += incident.buildWeeklyRecursiveIncidentString();
				if(it.hasNext())
					result+=",";
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	private String buildRecursiveIncidentString() {
		String date = "";
		String time = "";
		if(incidentType == null) return "";
		if(incidentType.equals(RecursiveType.YEARLY)){
			String dateStart = buildMonthStringCalendar(start);
			String dateEnd = "";
			String timeEnd = "";
			if(end != null){
				dateEnd = buildMonthStringCalendar(end);
				timeEnd = buildTimeCalendar(end);
			}
			String timeStart = buildTimeCalendar(start);
			
			date = dateStart+"-"+dateEnd;
			time = timeStart+"-"+timeEnd;
		}else if(incidentType.equals(RecursiveType.MONTHLY)){
			String dateStart = buildDayStringCalendar(start);
			String timeStart = buildTimeCalendar(start);
			String dateEnd = "";
			String timeEnd = "";
			if(end != null){
				dateEnd = buildDayStringCalendar(end);
				timeEnd = buildTimeCalendar(end);
			}
			date = dateStart+"-"+dateEnd;
			time = timeStart+"-"+timeEnd;
		}else if(incidentType.equals(RecursiveType.DAILY)){
			String timeStart = buildTimeCalendar(start);
			String timeEnd = "";
			if(end != null)
				timeEnd = buildTimeCalendar(end);
			date = "-";
			time = timeStart+"-"+timeEnd;
		}else{
			return "";
		}
		return "["+date+"]("+ time +")";
	}
	private String buildMonthStringCalendar(GregorianCalendar calendar) {
		String month = format(calendar.get(GregorianCalendar.MONTH)+1);
		String day = format(calendar.get(GregorianCalendar.DAY_OF_MONTH));
		return month+"/"+day;
	}
	private String buildDayStringCalendar(GregorianCalendar calendar) {
		return format(calendar.get(GregorianCalendar.DAY_OF_MONTH));
	}
	private String buildTimeCalendar(GregorianCalendar calendar){
		String hour = format(calendar.get(GregorianCalendar.HOUR_OF_DAY));
		String min = format(calendar.get(GregorianCalendar.MINUTE));
		String sec = format(calendar.get(GregorianCalendar.SECOND));
		return hour+":"+min+":"+sec;
	}
	private String format(int i) {
		String result = Integer.toString(i);
		if(result.length()<2)
			result = "0"+result;
		return result;
	}
	public RecursiveWarning(String recursiveIncident) throws ConfigurationException{

		weeklyRecursiveIncident = new ArrayList<WeeklyRecursive>();
		//Formato inicial [recursivo](HH:MM:SS-HH:MM:SS),[recursivo](HH:MM:SS-HH:MM:SS),[semanal](HH:MM:SS-HH:MM:SS)
		String[] incidents = recursiveIncident.split(",");
		for(int i = 0;i<incidents.length;i++){
			String period = incidents[i];
			String[] times = period.substring(period.indexOf("(")+1,period.indexOf(")")).split("-");
			if(period.contains("/")){
				incidentType = RecursiveType.YEARLY;
				//Formato [MM/DD-MM/DD](HH:MM:SS-HH:MM:SS) Periodicidad anual
				String[] dates = period.substring(1,period.indexOf("]")).split("-");
				start = new GregorianCalendar();
				buildYearlyCalendar(dates[0],times[0],start);
				if(dates.length == 2){
					end = new GregorianCalendar();
					buildYearlyCalendar(dates[1],times[1],end);
				}
			}else if(period.contains("-") && !period.contains("[-]") && !(period.toUpperCase().contains("L") ||
					period.toUpperCase().contains("M") ||
					period.toUpperCase().contains("X") ||
					period.toUpperCase().contains("J") ||
					period.toUpperCase().contains("V") ||
					period.toUpperCase().contains("S") ||
					period.toUpperCase().contains("D")
					)){
			//Formato [DD-DD](HH:MM:SS-HH:MM:SS) Periodicidad mensual
				incidentType = RecursiveType.MONTHLY;
				String[] dates = period.substring(1,period.indexOf("]")).split("-");
				start = new GregorianCalendar();
				end = new GregorianCalendar();
				buildMonthlyCalendar(dates[0],times[0],start);
				if(dates.length == 2)
					buildMonthlyCalendar(dates[1],times[1],end);
			}else if(period.contains("[-]")){
				incidentType = RecursiveType.DAILY;
				//Formato [](HH:MM:SS-HH:MM:SS) Periodicidad diaria
				start = new GregorianCalendar();
				end = new GregorianCalendar();
				buildDailyCalendar(times[0],start);
				if(times.length == 2)
					buildDailyCalendar(times[1],end);
				//Si es diaria, sólo se almacena el tipo diario
			}else if(period.toUpperCase().contains("L") ||
					period.toUpperCase().contains("M") ||
					period.toUpperCase().contains("X") ||
					period.toUpperCase().contains("J") ||
					period.toUpperCase().contains("V") ||
					period.toUpperCase().contains("S") ||
					period.toUpperCase().contains("D")
					){
				//Formato [LMXVSD](HH:MM:SS-HH:MM:SS) Periodicidad semanal
				incidentType =RecursiveType.WEEKLY;
				weeklyRecursiveIncident.add(new WeeklyRecursive(period));
			}else{
				throw new ConfigurationException("Format not supported : " + period);
			}
				
		}
		
	}
	private void buildYearlyCalendar(String dates, String times,
			GregorianCalendar actualCalendar) {
		String month = dates.split("/")[0];
		String day = dates.split("/")[1];
		String hour = times.split(":")[0];
		String min = times.split(":")[1];
		String sec = times.split(":")[2];
		setCalendar(null,month,day,hour,min,sec,actualCalendar);
		
	}
	private void buildMonthlyCalendar(String dates, String times,
			GregorianCalendar actualCalendar) {
		String day = dates;
		String hour = times.split(":")[0];
		String min = times.split(":")[1];
		String sec = times.split(":")[2];
		setCalendar(null,null,day,hour,min,sec,actualCalendar);
		
	}
	private void buildDailyCalendar(String times,
			GregorianCalendar actualCalendar) {
		String hour = times.split(":")[0];
		String min = times.split(":")[1];
		String sec = times.split(":")[2];
		setCalendar(null,null,null,hour,min,sec,actualCalendar);
		
	}
	private void setCalendar(String year, String month,
			String day, String hour, String min, String sec,GregorianCalendar calendar) {
		if(year == null)
			year = "2000";
		if(month == null)
			month = "1";
		if(day == null)
			day = "1";
		calendar.set(Integer.parseInt(year),
				Integer.parseInt(month)-1,
				Integer.parseInt(day),
				Integer.parseInt(hour),
				Integer.parseInt(min),
				Integer.parseInt(sec));
	}
	public boolean isActive(){
		return isActive(new Date(System.currentTimeMillis()));
	}
	public boolean isActive(Date date) {
		// es que es semanal para siempre
		GregorianCalendar actualCalendar = new GregorianCalendar();
		actualCalendar.setTime(date);
		boolean isActive = false;
		if(incidentType != null){
			if(incidentType.equals(RecursiveType.YEARLY))
				isActive = evalYearly(actualCalendar);
			if(incidentType.equals(RecursiveType.MONTHLY))
				isActive = evalMonthly(actualCalendar);
			if(incidentType.equals(RecursiveType.DAILY))
				isActive = evalDaily(actualCalendar);
			if(isActive){
				isActive = evalWeekly(actualCalendar);
			}
		}else
			isActive = evalWeekly(actualCalendar);
		return isActive;
	}
	public GregorianCalendar getNextWarning(GregorianCalendar actualCalendar) {
		// es que es semanal para siempre
		GregorianCalendar calendar = null;
		if(incidentType != null){
			if(incidentType.equals(RecursiveType.YEARLY)){
				calendar = new GregorianCalendar(
						actualCalendar.get(GregorianCalendar.YEAR),
						start.get(GregorianCalendar.MONTH),
						start.get(GregorianCalendar.DAY_OF_MONTH),
						start.get(GregorianCalendar.HOUR_OF_DAY),
						start.get(GregorianCalendar.MINUTE),
						start.get(GregorianCalendar.SECOND)
						);
				if(actualCalendar.after(calendar)){
					calendar.add(GregorianCalendar.YEAR, 1);
				}
			}
			if(incidentType.equals(RecursiveType.MONTHLY)){
				calendar = new GregorianCalendar(
						actualCalendar.get(GregorianCalendar.YEAR),
						actualCalendar.get(GregorianCalendar.MONTH),
						start.get(GregorianCalendar.DAY_OF_MONTH),
						start.get(GregorianCalendar.HOUR_OF_DAY),
						start.get(GregorianCalendar.MINUTE),
						start.get(GregorianCalendar.SECOND)
						);
				if(evalMonthly(actualCalendar)){
					calendar.add(GregorianCalendar.MONTH, 1);
				}
			}
			if(incidentType.equals(RecursiveType.DAILY)){
				
				calendar = new GregorianCalendar(
						actualCalendar.get(GregorianCalendar.YEAR),
						actualCalendar.get(GregorianCalendar.MONTH),
						actualCalendar.get(GregorianCalendar.DAY_OF_MONTH),
						start.get(GregorianCalendar.HOUR_OF_DAY),
						start.get(GregorianCalendar.MINUTE),
						start.get(GregorianCalendar.SECOND)
						);
				if(evalDaily(actualCalendar)){
						calendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
				}
			}
		}
		if(calendar == null){
			calendar = actualCalendar;
		}
		if(weeklyRecursiveIncident != null && weeklyRecursiveIncident.size()>0){
			Iterator<WeeklyRecursive> it = weeklyRecursiveIncident.iterator();
			//TODO: Evaluar el caso de que se obtengan varios incidentes semanales en vez de uno acumulado 
			if(it.hasNext()){
				WeeklyRecursive weekly = it.next();
				GregorianCalendar weeklyCalendar = weekly.getStart();
				Iterator<Weekdays> weekdays  = weekly.getWeekdayIncidents().iterator();
				while(weekdays.hasNext()){
					Weekdays weekDay = weekdays.next();
					if(getWeekdayCalendar(weekDay) == calendar.get(GregorianCalendar.DAY_OF_WEEK)){
						GregorianCalendar weekdayCalendar = new GregorianCalendar(
								actualCalendar.get(GregorianCalendar.YEAR),
								actualCalendar.get(GregorianCalendar.MONTH),
								actualCalendar.get(GregorianCalendar.DAY_OF_MONTH),
								weeklyCalendar.get(GregorianCalendar.HOUR_OF_DAY),
								weeklyCalendar.get(GregorianCalendar.MINUTE),
								weeklyCalendar.get(GregorianCalendar.SECOND)
								);
						if(weekdayCalendar.after(calendar)){
							calendar = weekdayCalendar;
							return calendar;
						}
					}
					if(getWeekdayCalendar(weekDay) > calendar.get(GregorianCalendar.DAY_OF_WEEK)){
						GregorianCalendar weekdayCalendar = new GregorianCalendar(
								actualCalendar.get(GregorianCalendar.YEAR),
								actualCalendar.get(GregorianCalendar.MONTH),
								actualCalendar.get(GregorianCalendar.DAY_OF_MONTH),
								weeklyCalendar.get(GregorianCalendar.HOUR_OF_DAY),
								weeklyCalendar.get(GregorianCalendar.MINUTE),
								weeklyCalendar.get(GregorianCalendar.SECOND)
								);
						while(weekdayCalendar.get(GregorianCalendar.DAY_OF_WEEK) != getWeekdayCalendar(weekDay))
							weekdayCalendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
						return weekdayCalendar;
					}
					if(!weekdays.hasNext()){
						GregorianCalendar weekdayCalendar = new GregorianCalendar(
								actualCalendar.get(GregorianCalendar.YEAR),
								actualCalendar.get(GregorianCalendar.MONTH),
								actualCalendar.get(GregorianCalendar.DAY_OF_MONTH),
								weeklyCalendar.get(GregorianCalendar.HOUR_OF_DAY),
								weeklyCalendar.get(GregorianCalendar.MINUTE),
								weeklyCalendar.get(GregorianCalendar.SECOND)
								);
						while(weekdayCalendar.get(GregorianCalendar.DAY_OF_WEEK) != getWeekdayCalendar(weekly.getWeekdayIncidents().get(0)))
							weekdayCalendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
						return weekdayCalendar;
					}
				}
			}
		}
		return calendar;
	}
	private int getWeekdayCalendar(Weekdays weekDay) {
		int i = 0;
		if(weekDay.equals(Weekdays.MONDAY))
			i = GregorianCalendar.MONDAY;
		if(weekDay.equals(Weekdays.TUESDAY))
			i = GregorianCalendar.TUESDAY;
		if(weekDay.equals(Weekdays.WEDNESDAY))
			i = GregorianCalendar.WEDNESDAY;
		if(weekDay.equals(Weekdays.THURSDAY))
			i = GregorianCalendar.THURSDAY;
		if(weekDay.equals(Weekdays.FRIDAY))
			i = GregorianCalendar.FRIDAY;
		if(weekDay.equals(Weekdays.SATURDAY))
			i = GregorianCalendar.SATURDAY;
		if(weekDay.equals(Weekdays.SUNDAY))
			i = GregorianCalendar.SUNDAY;
		return i;
	}

	private boolean evalYearly(GregorianCalendar actualCalendar) {
		boolean isActive = false;
		if(start == null)
			isActive = true;
		else{
			isActive = evalYearly(start,actualCalendar);
		}
		if(isActive){
			if(end == null)
				isActive = true;
			else
				isActive = evalYearly(actualCalendar,end);
		}
		return isActive;
	}
	private boolean evalYearly(GregorianCalendar startCalendar,GregorianCalendar endCalendar){
		boolean isActive = false;
		if(
				startCalendar.get(GregorianCalendar.MONTH)<endCalendar.get(GregorianCalendar.MONTH) || (
				startCalendar.get(GregorianCalendar.MONTH)==endCalendar.get(GregorianCalendar.MONTH) &&
					evalMonthly(startCalendar,endCalendar)
				)
			)
			isActive = true;
		return isActive;
	}
	private boolean evalMonthly(GregorianCalendar actualCalendar) {
		boolean isActive = false;
		if(start == null)
			isActive = true;
		else{
			isActive = evalMonthly(start,actualCalendar);
		}
		if(isActive){
			if(end == null)
				isActive = true;
			else
				isActive = evalMonthly(actualCalendar,end);
		}
		return isActive;
		
	}
	private boolean evalMonthly(GregorianCalendar startCalendar,GregorianCalendar endCalendar){
		boolean isActive = false;
		if(
				startCalendar.get(GregorianCalendar.DAY_OF_MONTH)<endCalendar.get(GregorianCalendar.DAY_OF_MONTH) || (
				startCalendar.get(GregorianCalendar.DAY_OF_MONTH)==endCalendar.get(GregorianCalendar.DAY_OF_MONTH) &&
					evalDaily(startCalendar,endCalendar)
				)
			)
			isActive = true;
		return isActive;
	}
	private boolean evalDaily(GregorianCalendar actualCalendar){
		boolean isActive = false;
		if(start == null)
			isActive = true;
		else{
			isActive = evalDaily(start,actualCalendar);
		}
		if(isActive){
			if(end == null)
				isActive = true;
			else
				isActive = evalDaily(actualCalendar,end);
		}
		return isActive;
	}
	private boolean evalDaily(GregorianCalendar startCalendar,GregorianCalendar endCalendar){
		boolean isActive = false;
		if((startCalendar.get(GregorianCalendar.HOUR_OF_DAY) < endCalendar.get(GregorianCalendar.HOUR_OF_DAY)) || 
			  (
					  startCalendar.get(GregorianCalendar.HOUR_OF_DAY) == endCalendar.get(GregorianCalendar.HOUR_OF_DAY) &&
					  startCalendar.get(GregorianCalendar.MINUTE) < endCalendar.get(GregorianCalendar.MINUTE)
			  ) || (
					  startCalendar.get(GregorianCalendar.HOUR_OF_DAY) == endCalendar.get(GregorianCalendar.HOUR_OF_DAY) &&
					  startCalendar.get(GregorianCalendar.MINUTE) == endCalendar.get(GregorianCalendar.MINUTE) &&
					  startCalendar.get(GregorianCalendar.SECOND) <= endCalendar.get(GregorianCalendar.SECOND)
			  		)){
					isActive = true;	
			}
		return isActive;
	}
	private boolean evalWeekly(GregorianCalendar actualCalendar){
		boolean isActive = false;
		if(weeklyRecursiveIncident.size() == 0)
			return true;
		else{
			Iterator<WeeklyRecursive> it = weeklyRecursiveIncident.iterator();
			while(it.hasNext()){
				WeeklyRecursive incident = it.next();
				isActive = incident.isActive(actualCalendar.getTime());
			}
		}
		return isActive;
	}
}
