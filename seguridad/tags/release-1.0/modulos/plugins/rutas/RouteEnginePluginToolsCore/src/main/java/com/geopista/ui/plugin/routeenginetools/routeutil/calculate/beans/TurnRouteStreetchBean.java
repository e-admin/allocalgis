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
