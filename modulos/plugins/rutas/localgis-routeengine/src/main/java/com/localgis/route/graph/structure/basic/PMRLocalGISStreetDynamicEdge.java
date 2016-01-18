/**
 * PMRLocalGISStreetDynamicEdge.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicEdge;

import com.vividsolutions.jump.feature.Feature;

import es.uva.idelab.route.algorithm.SidewalkEdge;


/**
 * Clase que extiende un LocalGISStreetDynamicEdge, para crear el concepto de aceras
 */
public class PMRLocalGISStreetDynamicEdge extends LocalGISStreetDynamicEdge implements SidewalkEdge,ILocalGISEdge
{
//	PavementProperties properties = new PavementProperties();
	
	public PMRLocalGISStreetDynamicEdge(Node nA, Node nB, int idNetworkNodeA,int idNetworkNodeB, UniqueIDGenerator uidGenerator, Feature feature) {
		super(nA, nB, idNetworkNodeA, idNetworkNodeB,uidGenerator,feature);
	}
	
	public PMRLocalGISStreetDynamicEdge(Node nodeA, Node nodeB, int idNetworkNodeA,int idNetworkNodeB,int idFeature,
			int idLayer, double length,UniqueIDGenerator uidGenerator, Feature feature)
	{
		super(nodeA,nodeB, idNetworkNodeA, idNetworkNodeB, idFeature,	idLayer, length, uidGenerator,feature);
	}
	
	
	public PMRLocalGISStreetDynamicEdge(DynamicEdge e, int idNetworkNodeA,int idNetworkNodeB,int idFeature,
			int idLayer, double length,UniqueIDGenerator uidGenerator, Feature feature) {
		super(e, idNetworkNodeA, idNetworkNodeB,idFeature, idLayer, length,uidGenerator,feature);
	}
	
	public PMRLocalGISStreetDynamicEdge(Edge e, int idNetworkNodeA,int idNetworkNodeB, int side, Node nodeA, Node nodeB,UniqueIDGenerator uidGenerator, Feature feature) {
		super(nodeA, nodeB,idNetworkNodeA, idNetworkNodeB,uidGenerator,  feature);
        relatedTo = e;
        if (e !=  null)
        	this.setRelatedToId(e.getID());
        setSide(side);
    	this.setWidth(2);
    	this.setTransversalSlope(0);
    	this.setLongitudinalSlope(0);
    	this.setEdgeType("EDGE");
    	// Copies impedances from relatedTo
    	if (e instanceof LocalGISStreetDynamicEdge)
    	{
    		LocalGISStreetDynamicEdge edgeWithImpedances = ((LocalGISStreetDynamicEdge)e);
    		this.setCost(((LocalGISStreetDynamicEdge) e).getCost());
    	}
	}

	private double width; 	
	private double transversalSlope;
	private double longitudinalSlope;
	private boolean irregularPavement;
	private String description;
	private Edge relatedTo;
	private int side;
	public void setSide(int side) {
		this.side = side;
	}

	private String sEdgeType;
	private double obstacleHeight;
	
	private int relatedToId;
	
	public int getRelatedToId() {
		return relatedToId;
	}
	public void setRelatedToId(int relatedToId) {
		this.relatedToId = relatedToId;
	}
	public int getCalculatedSide() {
		return getSide();
	}
	public void setCalculatedSide(int calculatedSide){
		setSide(calculatedSide);
	}
	public double getObstacleHeight() {
		return obstacleHeight;
	}
	public void setObstacleHeight(double obstacleHeight) {
		this.obstacleHeight = obstacleHeight;
	}
	
	public String getsEdgeType() {
		return sEdgeType;
	}

	public void setEdgeType(String sEdgeType) {
		this.sEdgeType = sEdgeType;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getLongitudinalSlope() {
		return longitudinalSlope;
	}
	public void setLongitudinalSlope(double longitudinalSlope) {
		this.longitudinalSlope = longitudinalSlope;
	}
	public double getTransversalSlope() {
		return transversalSlope;
	}
	public void setTransversalSlope(double transversalSlope) {
		this.transversalSlope = transversalSlope;
	}
	public boolean getIrregularPavement() {
		return irregularPavement;
	}
	public void setIrregularPavement(boolean irregularPavement) {
		this.irregularPavement = irregularPavement;
	}

	public double getCost(Node from, double walkerImpulse)
	{
		return getImpedance(from).getCost(walkerImpulse);
	}
    public Edge getRelatedTo()
    {
        return relatedTo;
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(super.toString()))).append("->").append(relatedTo).toString();
    }
    public int getSide()
    {
        return side;
    }

    @Override
    /**
     * configura las propiedades adicionales de los PMR
     * @see EdgesFeatureCollections
     */
    public boolean setAttribute(String attributeName, Object attribute)
    {
	if (attribute==null)
	    return false;
	boolean result= super.setAttribute(attributeName, attribute);
	if (result==true)
	    return true;
	if( "anchuraAcera".equals(attributeName))
	    this.setWidth(Double.parseDouble(attribute.toString()));
	else
	if("pendienteTransversal".equals(attributeName))
	    this.setTransversalSlope(Double.parseDouble(attribute.toString()));
	else
	if("pendienteLongitudinal".equals(attributeName))
	    this.setLongitudinalSlope(Double.parseDouble(attribute.toString()));
	else
	if("ejeRelacionadoConId".equals(attributeName))
	    this.setRelatedToId(Integer.parseInt(attribute.toString()));
	else
	if("tipoEje".equals(attributeName))
	    this.setEdgeType(attribute.toString());
	else
	if("alturaObstaculo".equals(attributeName))
	    this.setObstacleHeight(Double.parseDouble(attribute.toString()));
	else
	if("tipoPasoCebra".equals(attributeName))
	    return false;// TODO asignar
	else
	if("ladoAcera".equals(attributeName))
	    {
		if (this instanceof ZebraDynamicEdge) // TODO revisar jerarquía de herencia aquí.
		    {
			ZebraDynamicEdge zebra = (ZebraDynamicEdge) this;
			zebra.setType(attribute.toString());
		    }
	    }
	else
	return false;
	
	return true;
    }

}
