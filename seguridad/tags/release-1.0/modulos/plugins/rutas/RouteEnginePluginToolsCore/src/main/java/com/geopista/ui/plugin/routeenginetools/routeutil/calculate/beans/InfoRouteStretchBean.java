/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.uva.geotools.graph.structure.Edge;

import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author javieraragon
 *
 */
public class InfoRouteStretchBean implements Serializable, RouteStretchInterface{
	
	
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -1015287502007442041L;
	
	
	private String typeStreet = null;
	private String nameStreet = null;
	private long lenthStreetMeters = 0;
	private Collection<Geometry> geometries = null;
	private Collection<Edge> edges = null;
	private int turnAngle = 0;
	
	
	public InfoRouteStretchBean(){
		this.typeStreet = "";
		this.nameStreet = "";
		this.lenthStreetMeters = 0;
		this.geometries = new ArrayList<Geometry>();
		this.edges = new ArrayList<Edge>();
	}
	
	/**
	 * @return the typeStreet
	 */
	public String getTypeStreet() {
		return typeStreet;
	}
	/**
	 * @param typeStreet the typeStreet to set
	 */
	public void setTypeStreet(String typeStreet) {
		this.typeStreet = typeStreet;
	}
	/**
	 * @return the nameStreet
	 */
	public String getNameStreet() {
		return nameStreet;
	}
	/**
	 * @param nameStreet the nameStreet to set
	 */
	public void setNameStreet(String nameStreet) {
		this.nameStreet = nameStreet;
	}
	/**
	 * @return the lenthStreetMeters
	 */
	public long getLenthStreetMeters() {
		return lenthStreetMeters;
	}
	
	
	/**
	 * @param lenthStreetMeters the lenthStreetMeters to set
	 */
	public void setLenthStreetMeters(long lenthStreetMeters) {
		if (lenthStreetMeters == 0){
			lenthStreetMeters = 1;
		}
		
		this.lenthStreetMeters = lenthStreetMeters;
	}
	
	
	/**
	 * @return the geometries
	 */
	public ArrayList<Geometry> getGeometries() {
		if (this.geometries != null){
			return new ArrayList<Geometry>(this.geometries);
		} else{
			return new ArrayList<Geometry>();
		}
	}
	
	
	/**
	 * @param geometries the geometries to set
	 */
	public void setGeometries(Collection<Geometry> geometries) {
		this.geometries = geometries;
	}
	
	
	/**
	 * @return the edges
	 */
	public Collection<Edge> getEdges() {
		if (this.edges != null){
			return edges;
		} else{
			return new ArrayList<Edge>();
		}
	}
	/**
	 * @param edges the edges to set
	 */
	public void setEdges(Collection<Edge> edges) {
		this.edges = edges;
	}


	@Override
	public String buildHtmlStringRouteStretchInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildStringRouteStretchInformation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addGeometryToCollection(Geometry geometry){
		if (geometries == null){
			geometries = new ArrayList<Geometry>();
		}
		
		if (geometry != null && !geometry.isEmpty()){
			geometries.add(geometry);
		}
	}
	
	public void addAllGeometriesToCollection(Collection<Geometry> newGeometries){
		if (geometries == null){
			geometries = new ArrayList<Geometry>();
		}
		
		if (newGeometries!=null && !newGeometries.isEmpty()){
			geometries.addAll(newGeometries);
		}
	}
	
	
	
	
	public void addEdgeToCollection(Edge edge){
		if (edges == null){
			edges = new ArrayList<Edge>();
		}
		
		if (edge != null){
			edges.add(edge);
		}
	}
	
	public void addAllEdgesToCollection(Collection<Edge> newEdges){
		if (edges == null){
			edges = new ArrayList<Edge>();
		}
		
		if (newEdges!=null && !newEdges.isEmpty()){
			edges.addAll(newEdges);
		}
	}

	/**
	 * @return the turnAngle
	 */
	public int getTurnAngle() {
		return turnAngle;
	}

	/**
	 * @param turnAngle the turnAngle to set
	 */
	public void setTurnAngle(int turnAngle) {
		this.turnAngle = turnAngle;
	}

	public boolean hasStreetEdges() {
		if (this.edges!=null && !this.edges.isEmpty()){
			Iterator<Edge> edgesIt = edges.iterator();
			while(edgesIt.hasNext()){
				Edge edge = edgesIt.next();
				if (edge!=null && edge instanceof LocalGISStreetDynamicEdge){
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	
	
	

}
