/**
 * LocalGISWarning.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

import com.localgis.app.gestionciudad.utils.RecursiveWarning;

@SuppressWarnings("serial")
public class LocalGISWarning implements Serializable {
	
	
	
	
	public static int PATH_DISABLED = 1;
	public static int PATH_CLOSED_TO_VEHICLES = 2;
	private String description;
	private GregorianCalendar start;
	private GregorianCalendar end;
	private int incidentType;
	private RecursiveWarning recursiveIncident = null;
	private String pattern;
	
	public RecursiveWarning getRecursivencident(){
		return recursiveIncident;
	}
	
	public void setRecursiveIncident(RecursiveWarning recursiveIncident){
		this.recursiveIncident = recursiveIncident;
	}
	
	public LocalGISWarning(int incidentType){
		this.incidentType = incidentType;
	}
	public LocalGISWarning(){
		
	}
	public String toString(){
		String resultGeneral = "["+getCalendarDateString(start)+"-"+getCalendarDateString(end)+"]("+getCalendarTimeString(start)+"-"+getCalendarTimeString(end)+")";
		if(resultGeneral.equals("[-](-)"))
			resultGeneral = "";
		if(recursiveIncident != null)
			resultGeneral +=  recursiveIncident.toString();
		return resultGeneral;
	}
	private String getCalendarTimeString(GregorianCalendar date) {
		String result = "";
		if(date != null){
			result = rebuild(date.get(GregorianCalendar.HOUR_OF_DAY),2)+":"+rebuild(date.get(GregorianCalendar.MINUTE),2)+":"+rebuild(date.get(GregorianCalendar.SECOND),2);
		}
		return result;
	}
	private String getCalendarDateString(GregorianCalendar date) {
		String result = "";
		if(date != null){
			result = rebuild(date.get(GregorianCalendar.YEAR),4)+"/"+rebuild(date.get(GregorianCalendar.MONTH)+1,2)+"/"+rebuild(date.get(GregorianCalendar.DAY_OF_MONTH),2);
		}
		return result;
		
	}
	private String rebuild(int date, int length) {
		String result = date + "";
		while(result.length()<length){
			result = "0"+result;
		}
		return result;
	}
	private void buildCalendar(String dates, String times,
			GregorianCalendar actualCalendar) {
		String year = dates.split("/")[0];
		String month = dates.split("/")[1];
		String day = dates.split("/")[2];
		String hour = times.split(":")[0];
		String min = times.split(":")[1];
		String sec = times.split(":")[2];
		setCalendar(year,month,day,hour,min,sec,actualCalendar);
		
	}
	private void setCalendar(String year, String month,
			String day, String hour, String min, String sec,GregorianCalendar calendar) {
			calendar.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(min),Integer.parseInt(sec));
	}
	public LocalGISWarning(String data) throws ConfigurationException{
		if(data != null && data.equals(""))
			data = "[-](-)";
		pattern = data;
		String[] incidents = data.split(",");
		for(int i = 0;i<incidents.length && recursiveIncident == null;i++){
			String incident = incidents[i];
			String dates = incident.substring(1, incident.indexOf("]"));
			String times = incident.substring(incident.indexOf("(")+1,incident.indexOf(")"));
			if(dates.contains("-")){
				//puede ser de formato "DD/MM/YY-DD/MM/YY"
				String[] date = dates.split("-");
				String[] time = times.split("-");
				if(dates.equals("-")){
					if(time.length > 0)
						recursiveIncident = new RecursiveWarning(data);
				}
				else if(date.length == 2 &&(date[0].split("/").length == 3 || date[1].split("/").length == 3)){
					if(date[0].split("/").length == 3){
						start = new GregorianCalendar();
						buildCalendar(date[0], time[0], start);
					}
					if(date.length == 2 && date[1].split("/").length == 3){
						end = new GregorianCalendar();
						buildCalendar(date[1], time[1], end);
					}
				}else if(date.length == 1 && date[0].split("/").length == 3){
					start = new GregorianCalendar();
					buildCalendar(date[0], time[0], start);
				}else{
					recursiveIncident = new RecursiveWarning(data);
				}	
			}else{
				recursiveIncident = new RecursiveWarning(data);
			}
			data = data.substring(incident.length(),data.length());
			if(data.startsWith(","))
				data = data.replaceFirst(",", "");
		}
		
	}
	public boolean isActive(){
		
		return isActive(new Date(System.currentTimeMillis()));
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public void analizePattern(){
		if(pattern != null){
			//Analiza patrón del tipo:
			//[DD/MM/YYYY-DD/MM/YYYY](HH:MM:SS-HH:MM:SS)
			//sino, evalua si lo relanza a tipo recursivo.
		}
	}
	public boolean isActive(Date date){
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		boolean isActive = false;
		if(start == null)
			isActive = true;
		else{
			LOGGER.debug("Comparando si la fecha de inicio de la cita : " + start.getTime()+ " es anterior que la actual : " + date);
			if(start.before(calendar))
				isActive = true;
		}
		if(isActive){
			if(end == null)
				isActive = true;
			else{
				LOGGER.debug("Comparando si la fecha de fin de la cita : " + end.getTime()+ " es posterior que la actual : " + date);
				if(end.after(calendar))
					isActive = true;
				else
					isActive = false;
			}	
			
		}
		if(isActive){
			if(recursiveIncident != null)
				isActive = recursiveIncident.isActive(date);
		}
		// En el resto de los casos debe ser verdadero.
		return isActive;
	}
	public int getIncidentType() {
		return incidentType;
	}
	public void setIncidentType(int incidentType) {
		this.incidentType = incidentType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateStart() {
		if(start != null)
			return start.getTime();
		return null;
	}
	public void setDateStart(Date dateStart) {
		start = new GregorianCalendar();
		start.setTime(dateStart);
	}
	public Date getDateEnd() {
		if(end != null)
			return end.getTime();
		return null;
	}
	public void setDateEnd(Date dateEnd) {
		end = new GregorianCalendar();
		end.setTime(dateEnd);
	}
	public boolean equals(Object o) {
		if(o instanceof LocalGISWarning){
			if(((LocalGISWarning) o).getIncidentType() == this.getIncidentType())
				return true;
		}
		
		return false;
	}
	private static Logger LOGGER = Logger.getLogger(LocalGISWarning.class);

	public GregorianCalendar getNextCalendarWarning(GregorianCalendar actualCalendar) {
		
		//GregorianCalendar calendar = (GregorianCalendar)start.clone();

		if(start != null && actualCalendar.after(start) && recursiveIncident == null){
			return null;
		}
		if(start != null && actualCalendar.before(start)){
			actualCalendar = (GregorianCalendar)start.clone();	
		}
		if(recursiveIncident != null)
			actualCalendar = recursiveIncident.getNextWarning(actualCalendar); 
		return actualCalendar;
	}
}
