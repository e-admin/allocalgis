/**
 * LocalGISStreetResultSet.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.datastore;

import java.util.ArrayList;
import java.util.Iterator;

import org.uva.routeserver.street.Incident;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.localgis.route.graph.structure.basic.LocalGISTurnImpedance;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author rubengomez
 * Extension de ResultSet para almacenar el identificador de layer y de feature para poder asociarlo al Edge
 */
public class LocalGISStreetResultSet extends LocalGISResultSet{
	private double nominalMaxSpeed;
	private ArrayList<Incident> incidents;
	private StreetTrafficRegulation StreetTrafficRegulation;
	private LocalGISTurnImpedance turnImpedanceNodeA;
	private LocalGISTurnImpedance turnImpedanceNodeB;
	private double width = 100;	//Anchura
	private double transversalSlope = 0;	//Pendiente transversal
	private double longitudinalSlope = 0;	//Pendiente longitudinal
	private boolean irregularPaving = false;	//Pavimento irregular
	private boolean bPMRGraph = false;
	private String sType;
	private String sEdgeType;
	private int relatedToId;
	private int calculatedSide;
	private Geometry geom;

	public Geometry getGeom() {
		return geom;
	}
	public void setGeom(Geometry geom) {
		this.geom = geom;
	}
	public int getCalculatedSide() {
		return calculatedSide;
	}
	public void setCalculatedSide(int calculatedSide) {
		this.calculatedSide = calculatedSide;
	}
	public int getRelatedToId() {
		return relatedToId;
	}
	public void setRelatedToId(int relatedToId) {
		this.relatedToId = relatedToId;
	}
	public boolean isbPMRGraph() {
		return bPMRGraph;
	}
	public void setbPMRGraph(boolean bPMRGraph) {
		this.bPMRGraph = bPMRGraph;
	}
	
	public String getsType() {
		return sType;
	}

	public void setsType(String sType) {
		this.sType = sType;
	}
	
	public String getsEdgeType() {
		return sEdgeType;
	}

	public void setsEdgeType(String sEdgeType) {
		this.sEdgeType = sEdgeType;
	}
	
	public LocalGISTurnImpedance getTurnImpedanceNodeA() {
		return turnImpedanceNodeA;
	}
	public void setTurnImpedanceNodeA(LocalGISTurnImpedance turnImpedance) {
		this.turnImpedanceNodeA = turnImpedance;
	}
	public LocalGISTurnImpedance getTurnImpedanceNodeB() {
		return turnImpedanceNodeB;
	}
	public void setTurnImpedanceNodeB(LocalGISTurnImpedance turnImpedance) {
		this.turnImpedanceNodeB = turnImpedance;
	}
	public StreetTrafficRegulation getStreetTrafficRegulation() {
		return StreetTrafficRegulation;
	}
	public void setStreetTrafficRegulation(
			StreetTrafficRegulation streetTrafficRegulation) {
		StreetTrafficRegulation = streetTrafficRegulation;
	}
	public void setNominalMaxSpeed(double nominalMaxSpeed){
		this.nominalMaxSpeed = nominalMaxSpeed;
	}
	public double getNominalMaxSpeed(){
		return this.nominalMaxSpeed;
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
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getTransversalSlope() {
		return transversalSlope;
	}
	public void setTransversalSlope(double transversalSlope) {
		this.transversalSlope = transversalSlope;
	}
	public double getLongitudinalSlope() {
		return longitudinalSlope;
	}
	public void setLongitudinalSlope(double longitudinalSlope) {
		this.longitudinalSlope = longitudinalSlope;
	}
	public boolean isIrregularPaving() {
		return irregularPaving;
	}
	public void setIrregularPaving(boolean irregularPaving) {
		this.irregularPaving = irregularPaving;
	}
}
