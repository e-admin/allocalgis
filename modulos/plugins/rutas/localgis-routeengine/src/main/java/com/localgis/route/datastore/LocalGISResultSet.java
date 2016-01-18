/**
 * LocalGISResultSet.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.datastore;

import java.util.ArrayList;
import java.util.Iterator;

import org.uva.route.datastore.RouteResultSet;
import org.uva.routeserver.street.Incident;

/**
 * @author rubengomez
 * Extension de ResultSet para almacenar el identificador de layer y de feature para poder asociarlo al Edge
 */
public class LocalGISResultSet extends RouteResultSet{
	private int idFeature;
	private int idLayer;
	private ArrayList<Incident> incidents;
	private double impedanceAToB;
	private double impedanceBToA;
	private double length;
	
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public int getIdLayer() {
		return idLayer;
	}
	public void setIdLayer(int idLayer) {
		this.idLayer = idLayer;
	}
	public int getIdFeature() {
		return idFeature;
	}
	public void setIdFeature(int idFeature) {
		this.idFeature = idFeature;
	}
	public double getImpedanceAToB() {
		return impedanceAToB;
	}
	public void setImpedanceAToB(double impedanceAToB) {
		this.impedanceAToB = impedanceAToB;
	}
	public double getImpedanceBToA() {
		return impedanceBToA;
	}
	public void setImpedanceBToA(double impedanceBToA) {
		this.impedanceBToA = impedanceBToA;
	}
	public void setIncident(Incident incident){
		if(incidents == null)
			incidents = new ArrayList<Incident>();
		incidents.add(incident);
	}
	
	public ArrayList<Incident> getIncidents(){
		return this.incidents;
	}
	public Iterator<Incident> getIncidentIterator(){
		return this.incidents.iterator();
	}
	
}
