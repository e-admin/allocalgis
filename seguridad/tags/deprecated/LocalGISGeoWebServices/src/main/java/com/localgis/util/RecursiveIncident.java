package com.localgis.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.naming.ConfigurationException;

public class RecursiveIncident implements Serializable{
	
	private RecursiveIncidentType incidentType;
	private GregorianCalendar start;
	private GregorianCalendar end;
	private ArrayList<WeeklyRecursiveIncident> weeklyRecursiveIncident;
	
	
	
	/**
	 * @return the incidentType
	 */
	public RecursiveIncidentType getIncidentType() {
		return incidentType;
	}

	/**
	 * @param incidentType the incidentType to set
	 */
	public void setIncidentType(RecursiveIncidentType incidentType) {
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
	public ArrayList<WeeklyRecursiveIncident> getWeeklyRecursiveIncident() {
		return weeklyRecursiveIncident;
	}

	/**
	 * @param weeklyRecursiveIncident the weeklyRecursiveIncident to set
	 */
	public void setWeeklyRecursiveIncident(
			ArrayList<WeeklyRecursiveIncident> weeklyRecursiveIncident) {
		this.weeklyRecursiveIncident = weeklyRecursiveIncident;
	}

	public RecursiveIncident(){
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
		Iterator<WeeklyRecursiveIncident> it = weeklyRecursiveIncident.iterator();
		String result = "";
		while(it.hasNext()){
			WeeklyRecursiveIncident incident = it.next();
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
		if(incidentType.equals(RecursiveIncidentType.YEARLY)){
			String dateStart = buildMonthStringCalendar(start);
			String dateEnd = buildMonthStringCalendar(end);
			String timeStart = buildTimeCalendar(start);
			String timeEnd = buildTimeCalendar(end);
			date = dateStart+"-"+dateEnd;
			time = timeStart+"-"+timeEnd;
		}else if(incidentType.equals(RecursiveIncidentType.MONTHLY)){
			String dateStart = buildDayStringCalendar(start);
			String dateEnd = buildDayStringCalendar(end);
			String timeStart = buildTimeCalendar(start);
			String timeEnd = buildTimeCalendar(end);
			date = dateStart+"-"+dateEnd;
			time = timeStart+"-"+timeEnd;
		}else if(incidentType.equals(RecursiveIncidentType.DAILY)){
			String timeStart = buildTimeCalendar(start);
			String timeEnd = buildTimeCalendar(end);
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
	public RecursiveIncident(String recursiveIncident) throws ConfigurationException{

		weeklyRecursiveIncident = new ArrayList<WeeklyRecursiveIncident>();
		//Formato inicial [recursivo](HH:MM:SS-HH:MM:SS),[recursivo](HH:MM:SS-HH:MM:SS),[semanal](HH:MM:SS-HH:MM:SS)
		String[] incidents = recursiveIncident.split(",");
		for(int i = 0;i<incidents.length;i++){
			String period = incidents[i];
			String[] times = period.substring(period.indexOf("(")+1,period.indexOf(")")).split("-");
			if(period.contains("/")){
				incidentType = RecursiveIncidentType.YEARLY;
				//Formato [MM/DD-MM/DD](HH:MM:SS-HH:MM:SS) Periodicidad anual
				String[] dates = period.substring(1,period.indexOf("]")).split("-");
				start = new GregorianCalendar();
				end = new GregorianCalendar();
				buildYearlyCalendar(dates[0],times[0],start);
				if(dates.length == 1)
					buildYearlyCalendar(dates[0],times[0],end);
				else
					buildYearlyCalendar(dates[1],times[1],end);
			}else if(period.contains("-") && !period.contains("[-]") && !(period.toUpperCase().contains("L") ||
					period.toUpperCase().contains("M") ||
					period.toUpperCase().contains("X") ||
					period.toUpperCase().contains("J") ||
					period.toUpperCase().contains("V") ||
					period.toUpperCase().contains("S") ||
					period.toUpperCase().contains("D")
					)){
			//Formato [DD-DD](HH:MM:SS-HH:MM:SS) Periodicidad mensual
				incidentType = RecursiveIncidentType.MONTHLY;
				String[] dates = period.substring(1,period.indexOf("]")).split("-");
				start = new GregorianCalendar();
				end = new GregorianCalendar();
				buildMonthlyCalendar(dates[0],times[0],start);
				buildMonthlyCalendar(dates[1],times[1],end);
			}else if(period.contains("[-]")){
				incidentType = RecursiveIncidentType.DAILY;
				//Formato [](HH:MM:SS-HH:MM:SS) Periodicidad diaria
				start = new GregorianCalendar();
				end = new GregorianCalendar();
				buildDailyCalendar(times[0],start);
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
				weeklyRecursiveIncident.add(new WeeklyRecursiveIncident(period));
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
			if(incidentType.equals(RecursiveIncidentType.YEARLY))
				isActive = evalYearly(actualCalendar);
			if(incidentType.equals(RecursiveIncidentType.MONTHLY))
				isActive = evalMonthly(actualCalendar);
			if(incidentType.equals(RecursiveIncidentType.DAILY))
				isActive = evalDaily(actualCalendar);
			if(isActive){
				isActive = evalWeekly(actualCalendar);
			}
		}else
			isActive = evalWeekly(actualCalendar);
		return isActive;
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
			Iterator<WeeklyRecursiveIncident> it = weeklyRecursiveIncident.iterator();
			while(it.hasNext()){
				WeeklyRecursiveIncident incident = it.next();
				isActive = incident.isActive(actualCalendar.getTime());
			}
		}
		return isActive;
	}
}
