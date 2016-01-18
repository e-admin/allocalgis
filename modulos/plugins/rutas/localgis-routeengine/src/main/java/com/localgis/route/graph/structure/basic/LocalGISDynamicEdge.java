/**
 * LocalGISDynamicEdge.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;

import java.util.HashSet;
import java.util.Set;

import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.impedance.EdgeImpedance;
import org.uva.route.graph.structure.impedance.SimpleImpedance;
import org.uva.routeserver.street.Incident;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;

/**
 * @author rubengomez Clase que extiende un DynamicEdge, para agregar los campos idLayer e idFeature y {@link LocalGISDynamicEdge#feature}
 */
@SuppressWarnings("serial")
public class LocalGISDynamicEdge extends DynamicEdge implements ILocalGISEdge{

	private int idFeature; 	
	private int idLayer;	
	private double edgeLength;
	private int idNetworkNodeA;
	private int idNetworkNodeB;

	//TODO: Introducir los incidents en el DynamicEdge, como efectos generales de cualquier ruta. Se debe hacer en el RouteEngine.
	Set<Incident> incidents = new HashSet<Incident>();
    /**
     * Optional feature if it can be located in the workspace
     * 
     * @see {@link #idFeature} {@link #idLayer}
     *
     */

    protected transient Feature feature;
    /**
     * Optional geometry for representing and browsing spatially
     */
    private Geometry geom;
	
	/**
	 * @return the incidents
	 */
	public Set<Incident> getIncidents()
	{
		return incidents;
	}
	/**
	 * put a new incident to this street
	 */
	public void putIncident(Incident incident)
	{
		getIncidents().add(incident);
	}
	
	


    public LocalGISDynamicEdge(Node nA, Node nB, UniqueIDGenerator uidGenerator) {
		super(nA, nB, uidGenerator);
	}
	
    public LocalGISDynamicEdge(Node nA, Node nB,int networkA,int networkB, int idFeature, int idLayer, double edgeLength, UniqueIDGenerator uidGenerator, Feature feature) {

	super(nA, nB, uidGenerator);
		this.idFeature = idFeature;
		this.idLayer = idLayer;
		this.edgeLength = edgeLength;
		this.idNetworkNodeA=networkA;
		this.idNetworkNodeB=networkB;
	this.feature = feature;
	}

	public double getEdgeLength() {
		return edgeLength;
	}
	
	public void setEdgeLength(double edgeLength) {
		this.edgeLength = edgeLength;
	}
	
	public int getIdFeature() {
		return idFeature;
	}

	public void setIdFeature(int idFeature) {
		this.idFeature = idFeature;
	}

	public int getIdLayer() {
		return idLayer;
	}

	public void setIdLayer(int idLayer) {
		this.idLayer = idLayer;
	}
	
	@Override
	public void setImpedanceAToB(double impedanceAToB) {
		this.setImpedance(this.getNodeA(),new SimpleImpedance(impedanceAToB));
	}
	
	@Override
	public void setImpedanceBToA(double impedanceBToA) {
		this.setImpedance(this.getNodeB(),new SimpleImpedance(impedanceBToA));
	}
	
	@Override
	public void setImpedanceAToB(EdgeImpedance impedanceAToB) {
		this.setImpedance(this.getNodeA(),impedanceAToB);
	}
	
	@Override
	public void setImpedanceBToA(EdgeImpedance impedanceBToA) {
		this.setImpedance(this.getNodeB(),impedanceBToA);
	}
	
	@Override
	public void setImpedanceBidirecccional(double impedance) {
		this.setImpedance(this.getNodeB(),new SimpleImpedance(impedance));
		this.setImpedance(this.getNodeA(),new SimpleImpedance(impedance));
	}
	
	@Override
	public void setImpedanceBidirecccional(EdgeImpedance impedance) {
		this.setImpedance(this.getNodeB(),impedance);
		this.setImpedance(this.getNodeA(),impedance);
	}
	public int getIdNetworkNodeA() {
		return idNetworkNodeA;
	}
	
	public void setIdNetworkNodeA(int idNetworkNodeA) {
		this.idNetworkNodeA = idNetworkNodeA;
	}

	public void setIdNetworkNodeB(int idNetworkNodeB) {
		this.idNetworkNodeB = idNetworkNodeB;
	}

	@Override
	public int getIdNetworkNodeB() {
		return idNetworkNodeB;
	}

    @Override
    public Feature getFeature()
    {
	return feature;
    }

    @Override
    public void setFeature(Feature feature)
    {
	this.feature = feature;
    }

    @Override
    public void setGeometry(Geometry geom)
    {
	this.geom = geom;
    }

    @Override
    public Geometry getGeometry()
    {
	return geom;
    }
    @Override
    public boolean setAttribute(String attributeName, Object attribute)
    {
	try
	    {
		if ("coste".equals(attributeName))
		    this.setCost(Double.parseDouble(attribute.toString()));
		else
		    if ("impedanciaAB".equals(attributeName))
			this.setImpedanceAToB(Double.parseDouble(attribute.toString()));
		    else
		if( "impedanciaBA".equals(attributeName))
		    this.setImpedanceBToA(Double.parseDouble(attribute.toString()));
		else
		if("costeEjeDinamico".equals(attributeName))
		    {
			return false;//??this.setco(Double.parseDouble(attribute.toString()));
		    }
		else
		if( "longitudEje".equals(attributeName))
		    this.setEdgeLength(Double.parseDouble(attribute.toString()));
		else
		if("idFeature".equals(attributeName))
		    this.setIdFeature(Integer.parseInt(attribute.toString()));
		else
		if("idCapa".equals(attributeName))
		    this.setIdLayer(Integer.parseInt(attribute.toString()));
		else
		return false;
	    } catch (NumberFormatException e)
	    {
		//Ignora errores
		return false;
	    }
	
	return true;
    }
}
