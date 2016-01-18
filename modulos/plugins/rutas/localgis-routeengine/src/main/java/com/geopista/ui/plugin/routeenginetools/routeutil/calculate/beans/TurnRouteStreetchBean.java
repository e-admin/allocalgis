/**
 * TurnRouteStreetchBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans;

import org.uva.geotools.graph.structure.Node;

/**
 * @author javieraragon
 *
 */
public class TurnRouteStreetchBean extends InfoRouteStretchBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7570955248115119506L;

	private int turnAngle = -1;
	private Node turnNode = null;
	private String description = "";
	
	
	
	public TurnRouteStreetchBean(Node node, String description){
		this.turnNode = node;
		this.description = description;
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
	
	/**
	 * @return the turnNode
	 */
	public Node getTurnNode() {
		return turnNode;
	}
	
	/**
	 * @param turnNode the turnNode to set
	 */
	public void setTurnNode(Node turnNode) {
		this.turnNode = turnNode;
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

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
