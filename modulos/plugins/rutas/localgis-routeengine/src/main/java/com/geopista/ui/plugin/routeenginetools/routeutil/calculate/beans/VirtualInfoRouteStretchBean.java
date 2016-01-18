/**
 * VirtualInfoRouteStretchBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans;

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
				if (stretchBean.getGeometries()!=null && !stretchBean.getGeometries().isEmpty()
//					&& this.getGeometries().size()==0
					)
				    {
					this.addAllGeometriesToCollection(stretchBean.getGeometries());	
				}

				if (stretchBean.getEdges()!=null && !stretchBean.getEdges().isEmpty())
				{
					this.addAllEdgesToCollection(stretchBean.getEdges());
				}

//				if (stretchBean.getLenthStreetMeters() > 0){
//					this.setLengthStreetMeters(stretchBean.getLenthStreetMeters() + this.getLenthStreetMeters());
//				}
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
