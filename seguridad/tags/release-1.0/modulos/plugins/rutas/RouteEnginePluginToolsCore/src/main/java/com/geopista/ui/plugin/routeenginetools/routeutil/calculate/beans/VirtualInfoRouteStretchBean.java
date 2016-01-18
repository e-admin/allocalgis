package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans;

import java.util.Iterator;

import org.uva.geotools.graph.structure.Edge;

import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;

public class VirtualInfoRouteStretchBean extends InfoRouteStretchBean {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2113832935734185894L;
	
	
	private VirtualNodeInfo virtualNodeInfo = null;
	private long degreeOrientation = -1;
	private CardinalDirections stretchCardinalDirection = null;
	 
	
	public VirtualInfoRouteStretchBean(VirtualNodeInfo nodeInfo){
		this.virtualNodeInfo = nodeInfo;
	}
	
	
	/**
	 * @return the virtualNodeInfo
	 */
	public VirtualNodeInfo getVirtualNodeInfo() {
		return virtualNodeInfo;
	}
	/**
	 * @param virtualNodeInfo the virtualNodeInfo to set
	 */
	public void setVirtualNodeInfo(VirtualNodeInfo virtualNodeInfo) {
		this.virtualNodeInfo = virtualNodeInfo;
	}
	/**
	 * @return the degreeOrientation
	 */
	public long getDegreeOrientation() {
		return degreeOrientation;
	}
	/**
	 * @param degreeOrientation the degreeOrientation to set
	 */
	public void setDegreeOrientation(long degreeOrientation) {
		this.degreeOrientation = degreeOrientation;
	}
	/**
	 * @return the stretchCardinalDirection
	 */
	public CardinalDirections getStretchCardinalDirection() {
		return stretchCardinalDirection;
	}
	/**
	 * @param stretchCardinalDirection the stretchCardinalDirection to set
	 */
	public void setStretchCardinalDirection(
			CardinalDirections stretchCardinalDirection) {
		this.stretchCardinalDirection = stretchCardinalDirection;
	}


	public boolean addInfoRouteStretchBean(InfoRouteStretchBean stretchBean) {
		if (stretchBean!=null){
			try{
				if (stretchBean.getGeometries()!=null && !stretchBean.getGeometries().isEmpty()){
					this.addAllGeometriesToCollection(stretchBean.getGeometries());	
				}

				if (stretchBean.getEdges()!=null && !stretchBean.getEdges().isEmpty()){
					this.addAllEdgesToCollection(stretchBean.getEdges());
				}

				if (stretchBean.getLenthStreetMeters() > 0){
					this.setLenthStreetMeters(stretchBean.getLenthStreetMeters() + this.getLenthStreetMeters());
				}
			}catch (Exception e) {
				return false;
			}
			return true;
		}
				
		return false;
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
	
	@Override
	public boolean hasStreetEdges() {
		boolean resultado = super.hasStreetEdges();
		if (resultado){
			return resultado;
		} else{
			if (this.virtualNodeInfo.getEdge() instanceof LocalGISStreetDynamicEdge){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
}
