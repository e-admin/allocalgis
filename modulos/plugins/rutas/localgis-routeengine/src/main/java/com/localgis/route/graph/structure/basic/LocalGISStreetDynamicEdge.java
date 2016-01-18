/**
 * LocalGISStreetDynamicEdge.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;


import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.impedance.EdgeImpedance;
import org.uva.route.graph.structure.impedance.SimpleImpedance;
import org.uva.routeserver.street.BasicStreet;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;

/**
 * @author rubengomez
 * Clase que extiende un DynamicEdge, para agregar los campos idLayer e idFeature
 */
@SuppressWarnings("serial")
public class LocalGISStreetDynamicEdge extends BasicStreet implements ILocalGISEdge
{

	private int idFeature; 	
	private int idLayer;
	private int idNetworkNodeA;
	private int idNetworkNodeB;
	
	private double edgeLength;


	private boolean isCloned = false;
	private boolean isTreated = false;
    protected Geometry geom;
    
    protected transient Feature feature;
    
    public LocalGISStreetDynamicEdge(Node nA, Node nB, int idNetworkNodeA,int idNetworkNodeB,int idFeature, int idLayer, double edgeLength, UniqueIDGenerator uidGenerator, Feature feature) {

   	super(nA, nB, uidGenerator);
   		this.idFeature = idFeature;
   		this.idLayer = idLayer;
   		this.edgeLength = edgeLength;
   		this.idNetworkNodeA=idNetworkNodeA;
   		this.idNetworkNodeB=idNetworkNodeB;
   	this.feature = feature;
   	}

	public LocalGISStreetDynamicEdge(Node nA, Node nB, int idNetworkNodeA,int idNetworkNodeB, UniqueIDGenerator uidGenerator, Feature feature)
	{
		super(nA, nB, uidGenerator);
		this.setFeature(feature);
		this.idNetworkNodeA=idNetworkNodeA;
   		this.idNetworkNodeB=idNetworkNodeB;
	}

	public LocalGISStreetDynamicEdge(DynamicEdge e, int idNetworkNodeA,int idNetworkNodeB, int idFeature, int idLayer, double length, Feature feature)
	{
		// TODO Auto-generated constructor stub
		super(e.getNodeB(), e.getNodeA(), new SequenceUIDGenerator());
		this.idFeature = idFeature;
		this.idLayer = idLayer;
		this.edgeLength = length;
		this.idNetworkNodeA=idNetworkNodeA;
   		this.idNetworkNodeB=idNetworkNodeB;
		setFeature(feature);
	}
	
	
	public LocalGISStreetDynamicEdge(DynamicEdge e, int idNetworkNodeA,int idNetworkNodeB,int idFeature,
			int idLayer, double length,UniqueIDGenerator uidGenerator,Feature feature)
	{
		super(e.getNodeB(), e.getNodeA(),uidGenerator);
		this.idFeature = idFeature;
		this.idLayer = idLayer;
		this.edgeLength = length;
		this.idNetworkNodeA=idNetworkNodeA;
   		this.idNetworkNodeB=idNetworkNodeB;
		this.setID(uidGenerator.getEdgeUniqueID());
		setFeature(feature);
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
		this.setImpedance(this.getNodeA(),
				new SimpleImpedance(impedanceAToB));
	}
	
	@Override
	public void setImpedanceBToA(double impedanceBToA) {
		this.setImpedance(this.getNodeB(),
				new SimpleImpedance(impedanceBToA));
	}
	
	@Override
	public void setImpedanceAToB(EdgeImpedance impedanceAToB) {
		this.setImpedance(this.getNodeA(),
				impedanceAToB);
	}
	
	@Override
	public void setImpedanceBToA(EdgeImpedance impedanceBToA) {
		this.setImpedance(this.getNodeB(),
				impedanceBToA);
	}
	
	@Override
	public void setImpedanceBidirecccional(double impedance) {
		this.setImpedance(this.getNodeB(),
				new SimpleImpedance(impedance));
		this.setImpedance(this.getNodeA(), 
				new SimpleImpedance(impedance));
	}
	
	@Override
	public void setImpedanceBidirecccional(EdgeImpedance impedance) {
		this.setImpedance(this.getNodeB(),
				impedance);
		this.setImpedance(this.getNodeA(),
				impedance);
	}

	@Override
	public double getEdgeLength() {
		return this.edgeLength;
	}

	@Override
	public void setEdgeLength(double length) {
		this.edgeLength = length;
	}

	@Override
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
	
	/**
	 * @return the isCloned
	 */
	public boolean isCloned() {
		return isCloned;
	}

	/**
	 * @param isCloned the isCloned to set
	 */
	public void setCloned(boolean isCloned) {
		this.isCloned = isCloned;
	}
	
	public boolean isTreated() {
		return isTreated;
	}

	public void setTreated(boolean isTreated) {
		this.isTreated = isTreated;
	}

    @Override
    public void setGeometry(Geometry geom)
	{
	this.geom = geom;
	}

    @Override
    public void setFeature(Feature feature)
	{
	this.feature = feature;

	}

    @Override
    public Geometry getGeometry()
	{
	return geom;
	}

    @Override
	public Feature getFeature()
	{
	return feature;
	}

    @Override
    public boolean setAttribute(String attributeName, Object attribute)
    {
	if (attribute==null)
	    return false;
//	try
//	    {
		if ("coste".equals(attributeName))
		   return false;// Deprecated Usar Impedancias A->B y B->A this.setCost(Double.parseDouble(attribute.toString()));
		else
		    if ("impedanciaAB".equals(attributeName))
			{
			    double value=Double.parseDouble(attribute.toString());
			    this.setImpedanceAToB(value);
			}
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
		if("regulacionTrafico".equals(attributeName))
		    {
			this.setTrafficRegulation(StreetTrafficRegulation.valueOf(attribute.toString()));
		    }
		else
		if ("maxVelocidadNominal".equals(attributeName))
		    this.setNominalMaxSpeed(Double.parseDouble(attribute.toString()));
		else
		if("pintadaRegulacionTrafico".equals(attributeName))
		    {
			return false; // esta propiedad compleja no se puede fijar
		    }
		else

		return false;
//	    }
//	    catch (NumberFormatException e)
//	    {
//		//Ignora errores
//		return false;
//	    }catch(IllegalArgumentException e)
//	    {
//		return false;
//	    }
	
	return true;
    }
	
	
}
