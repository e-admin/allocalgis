/**
 * LocalGisBasicLineGraphGenerator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.route.graph.build;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.uva.geotools.graph.build.GraphBuilder;
import org.uva.geotools.graph.build.line.LineGraphGenerator;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.route.graph.build.line.GeographicLinearFeatureGraphGenerator;
import org.uva.route.graph.structure.geographic.GeographicNode;
import org.uva.route.util.GeographicNodeUtil;

import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.localgis.route.datastore.LocalGISResultSet;
import com.localgis.route.graph.build.dynamic.LocalGISGraphBuilder;
import com.localgis.route.graph.build.dynamic.LocalGISGraphGenerator;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.feature.Feature;


/**
 * Generator a partir de features locales
 * Delega en {@link LocalGISGraphGenerator} para generar elementos a partir de {@link LocalGISResultSet}
 * @author javieraragon
 *
 */
public class LocalGisBasicLineGraphGenerator extends GeographicLinearFeatureGraphGenerator implements LineGraphGenerator, LocalGisGraphGenerator
{
    static final Log LOG = LogFactory.getLog(LocalGisBasicLineGraphGenerator.class);
    private LocalGISGraphGenerator delegateGenerator;
	
	
	public LocalGisBasicLineGraphGenerator(GraphBuilder graphBuilder){
		super();
		this.delegateGenerator=new LocalGISGraphGenerator(graphBuilder);
		this.setGraphBuilder(graphBuilder);
	}

    /* (non-Javadoc)
     * @see com.localgis.route.graph.build.LocalGisGraphGenerator#add(java.lang.Object, int, int, int, com.vividsolutions.jump.feature.Feature)
     */
    @Override
    public ILocalGISEdge add(Geometry geometryObject, int idFeature, int idLayer, int geometrySRID, Feature feature)
    {
		LineString lineStringFromGeometry = null;
		ILocalGISEdge lgDynamicEdge = null;
		
		// Indicamos el numero de decimales para truncar, para que el generador de red sea menos estricto
		int places = 3;
		
	if (geometryObject != null)
	    {
		// FIX No se debe sobre-simplificar a un linestring lineal!
		lineStringFromGeometry = NetworkModuleUtil.getEdgeLineStringFromGeometry((Geometry) geometryObject);

		if (lineStringFromGeometry != null && !lineStringFromGeometry.isEmpty())
		    {
			lgDynamicEdge = addElement(lineStringFromGeometry, idFeature, idLayer, geometrySRID, places, feature);
			return lgDynamicEdge;
		    }
	    }
	throw new IllegalArgumentException("Not Valid Geometry To Generate Graph:" + geometryObject);
	}



    /**
     * Crea un edge a partir de una feature y la geometría suministrada
     * 
     * @param ls
     *            {@link LineString} sintÃ©tica que representa el Edge. Puede no coincidir con la de la Feature original
     * @param idFeature
     * @param idLayer
     * @param geometrySRID
     * @param places
     *            decimales significativos para creación de coordenadas de nodos TODO el número no es igual en mÃ©tricas que en geogrÃ¡ficas
     * @param feature
     * @return
     */
    private ILocalGISEdge addElement(LineString ls, int idFeature, int idLayer, int geometrySRID, int places, Feature feature)
    {
		GeographicNode n1, n2;
		
		CoordinateReferenceSystem crs= getDefaultCRS();
				
		try {
			
			crs = CRS.decode("EPSG:" + Integer.toString(geometrySRID));
			
		} catch (NoSuchAuthorityCodeException e) {
			crs= getDefaultCRS();
		LOG.warn("Using default CRS. Referencing Error:" + e.getLocalizedMessage());
		} catch (FactoryException e) {
			crs= getDefaultCRS();
		LOG.warn("Using default CRS. Referencing Error:" + e.getLocalizedMessage());
		}
		
	Point p = roundPoint(ls.getStartPoint(), places);
	n1 = createGeographicNode(p, geometrySRID, crs);
		// look up second node and create if necessary
	p = roundPoint(ls.getEndPoint(), places);
	n2 = createGeographicNode(p, geometrySRID, crs);

		// create edge and set underlying object
	LocalGISGraphBuilder graphBuilder = (LocalGISGraphBuilder) getGraphBuilder();
	LocalGISDynamicEdge lgDynamicEdge = (LocalGISDynamicEdge) graphBuilder.buildEdge(n1, n2, -1,-1, idFeature, idLayer, ls.getLength(), feature);
	lgDynamicEdge.setGeometry(ls); // almacena la geometría sintÃ©tica que la ha generado.
				     // TODO la geometría estÃ¡ mejor representada en feature
	lgDynamicEdge.setFeature(feature);
	getGraphBuilder().addEdge(lgDynamicEdge);
		
	return lgDynamicEdge;
	}

    private GeographicNode createGeographicNode(Point p, int geometrySRID, CoordinateReferenceSystem crs)
    {
	GeographicNode n2;
	if ((n2 = (GeographicNode) coord2Node.get(p.getCoordinate())) == null)
	    {
		n2 = (GeographicNode) getGraphBuilder().buildNode();
		n2.setPosition(GeographicNodeUtil.createPoint(p.getCoordinate(), crs));

		getGraphBuilder().addNode(n2);
		p.setSRID(geometrySRID);
		coord2Node.put(p.getCoordinate(), n2);
	    }
	return n2;
    }

	protected Point roundPoint(Point point, int places){
		if (places != 0){
			Point p = null;
			
			Double x = truncate(point.getX(), places);
			Double y = truncate(point.getY(), places);
			
			GeometryFactory geometryFactory = new GeometryFactory();
			
			p = geometryFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(x,y));
			p.setSRID(point.getSRID());
			return p;
		}
		else
			return point;
	}
	
	protected double truncate(double value, int places) {
	    double multiplier = Math.pow(10, places);
	    return Math.floor(multiplier * value) / multiplier;
	}

	@Override
	public Graphable add(Object obj)
	{
	    if (obj instanceof LocalGISResultSet)// delegate to LocalGISGraphGenerator
		{
		    return delegateGenerator.add(obj); 
		}
	    else 
		{ // serÃ¡ un LineString o similar
		    return super.add(obj);
		}
	}
}
