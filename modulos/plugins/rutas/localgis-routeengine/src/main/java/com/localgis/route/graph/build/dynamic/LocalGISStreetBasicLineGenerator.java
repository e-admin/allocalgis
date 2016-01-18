/**
 * LocalGISStreetBasicLineGenerator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.build.dynamic;

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
import com.localgis.route.graph.build.LocalGisBasicLineGraphGenerator;
import com.localgis.route.graph.build.LocalGisGraphGenerator;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jump.feature.Feature;

/**
 * 
 * @author juacas
 */

public class LocalGISStreetBasicLineGenerator extends GeographicLinearFeatureGraphGenerator implements LineGraphGenerator, LocalGisGraphGenerator
{
    static final Log LOG = LogFactory.getLog(LocalGisBasicLineGraphGenerator.class);
    private LocalGISStreetGraphGenerator delegateGenerator;
    
	public LocalGISStreetBasicLineGenerator(GraphBuilder graphBuilder){
		super();
		this.delegateGenerator=new LocalGISStreetGraphGenerator(graphBuilder);
		this.setGraphBuilder(graphBuilder);
	}

    public ILocalGISEdge add(Geometry geometryObject, int idFeature, int idLayer, int srid, Feature feature)
    {
		// TODO Auto-generated method stub
		LineString lineStringFromGeometry = NetworkModuleUtil.getEdgeLineStringFromGeometry( geometryObject);
		
		// Indicamos el numero de decimales para truncar, para que el generador de red sea menos estricto
		int places = 2;

		// FIX JPC ReutilizaciÛn de m√©todos. Uso de linestring no simplificados.
//		if (geometryObject != null){
//			if (geometryObject instanceof MultiLineString){
//			// TODO BUG concatenar Linestrings
//				MultiLineString mls=(MultiLineString) geometryObject;
//				if (!(mls.isEmpty())) {
//					Coordinate[] coordmls= null;
//					//add ls built for the first and last coordinate.
//					Coordinate c0=mls.getGeometryN(0).getCoordinate();
//					Coordinate c1=mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates()[1];
//					Coordinate c2=mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates()[
//					                                                                          mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates().length -1
//					                                                                          ];
//					coordmls=new Coordinate[] {c0, c2};
//					CoordinateSequence seq= new CoordinateArraySequence(coordmls);
//					lineStringFromGeometry=new LineString(seq, new GeometryFactory());
//					
//				}else{
//					return null;
//				}
//
//			} else if (geometryObject instanceof LineString){
//				lineStringFromGeometry = (LineString) geometryObject;
//				
//			
//			}
//			
//				else if (geometryObject instanceof Polygon){
//				Polygon mls=(Polygon) geometryObject;
//				if (!(mls.isEmpty())) {
//					Coordinate[] coordmls= null;
//					//add ls built for the first and last coordinate.
//					Coordinate c0=mls.getGeometryN(0).getCoordinate();
//					Coordinate c2=mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates()[
//					                       mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates().length -1
//					                       ];
//					coordmls=new Coordinate[] {c0, c2};
//					CoordinateSequence seq= new CoordinateArraySequence(coordmls);
//					lineStringFromGeometry=new LineString(seq, new GeometryFactory());
//					lgDynamicEdge = addElement(lineStringFromGeometry, idFeature, idLayer, geometrySRID);
//
//				}
//			else{
//				throw new Exception("Not Valid Geometry To Generate Graph");
//			}	
//		}
//		}
		if (lineStringFromGeometry!=null  && !lineStringFromGeometry.isEmpty())
		    {
			ILocalGISEdge lgDynamicEdge = addElement(lineStringFromGeometry, idFeature, idLayer, srid, places, feature);
			return lgDynamicEdge;
		    }
		else
		    {
			return null;
		    }
	}

	public PMRLocalGISStreetDynamicEdge addPMR(Object geometryObject, int idFeature,
			int idLayer, int srid,Feature feature) throws Exception {
		// TODO Auto-generated method stub
		LineString lineStringFromGeometry = null;
		PMRLocalGISStreetDynamicEdge lgDynamicEdge = null;
		if (geometryObject != null){
			if (geometryObject instanceof MultiLineString){
				MultiLineString mls=(MultiLineString) geometryObject;
				if (!(mls.isEmpty())) {
					Coordinate[] coordmls= null;
					//add ls built for the first and last coordinate.
					Coordinate c0=mls.getGeometryN(0).getCoordinate();
					Coordinate c1=mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates()[1];
					Coordinate c2=mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates()[
					                                                                          mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates().length -1
					                                                                          ];
					coordmls=new Coordinate[] {c0, c2};
					CoordinateSequence seq= new CoordinateArraySequence(coordmls);
					lineStringFromGeometry=new LineString(seq, new GeometryFactory());
					lgDynamicEdge = addPMRElement(lineStringFromGeometry, idFeature, idLayer, srid,feature);

				}else{
					return null;
				}

			} else if (geometryObject instanceof LineString){
				lineStringFromGeometry = (LineString) geometryObject;
				if (!lineStringFromGeometry.isEmpty()){
					lgDynamicEdge = addPMRElement(lineStringFromGeometry, idFeature, idLayer, srid,feature);
				} else {
					return null;
				}
			}
//				else if (geometryObject instanceof Polygon){
//				Polygon mls=(Polygon) geometryObject;
//				if (!(mls.isEmpty())) {
//					Coordinate[] coordmls= null;
//					//add ls built for the first and last coordinate.
//					Coordinate c0=mls.getGeometryN(0).getCoordinate();
//					Coordinate c2=mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates()[
//					                       mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates().length -1
//					                       ];
//					coordmls=new Coordinate[] {c0, c2};
//					CoordinateSequence seq= new CoordinateArraySequence(coordmls);
//					lineStringFromGeometry=new LineString(seq, new GeometryFactory());
//					lgDynamicEdge = addElement(lineStringFromGeometry, idFeature, idLayer, geometrySRID);
//
//				}
			else{
				throw new Exception("Not Valid Geometry To Generate Graph");
			}
		}
		//		}

		return lgDynamicEdge;
	}

	private ILocalGISEdge addElement(LineString ls, int idFeature, int idLayer, int geometrySRID, int places, Feature feature)
	{
		GeographicNode n1, n2;
		CoordinateReferenceSystem crs= getDefaultCRS();

		try {

			crs = CRS.decode("EPSG:" + Integer.toString(geometrySRID));

		} catch (NoSuchAuthorityCodeException e) {
			crs= getDefaultCRS();
		} catch (FactoryException e) {
			crs= getDefaultCRS();
		}

		//Point p= ls.getStartPoint();
		Point p = roundPoint(ls.getStartPoint(), places);
		// look up first node and create if necessary
		if ((n1 = (GeographicNode) coord2Node.get(p.getCoordinate())) == null) {
			n1 = (GeographicNode) getGraphBuilder().buildNode();

			/** 
			 * JPC Workaround!! JTSUtils.jtsToGo1 fails to find JTSGeometryFactory in Geotools registry
			 *n1.setPosition((org.opengis.geometry.primitive.Point) JTSUtils.jtsToGo1(p, crs));
			 *TODO: investigate effect!
			 */
			n1.setPosition(GeographicNodeUtil.createPoint(p.getCoordinate(), crs));

			getGraphBuilder().addNode(n1);
			p.setSRID(geometrySRID);
			coord2Node.put(p.getCoordinate(), n1);
		}

		// look up second node and create if necessary

		p = roundPoint(ls.getEndPoint(), places);
		if ((n2 = (GeographicNode) coord2Node.get(p.getCoordinate())) == null) {
			n2 = (GeographicNode) getGraphBuilder().buildNode();
			n2.setPosition(GeographicNodeUtil.createPoint(p.getCoordinate(), crs));

			getGraphBuilder().addNode(n2);
			p.setSRID(geometrySRID);
			coord2Node.put(p.getCoordinate(), n2);
		}

		// create edge and set underlying object
		LocalGISStreetGraphBuilder graphBuilder = (LocalGISStreetGraphBuilder)getGraphBuilder();
		ILocalGISEdge lgStreetDynamicEdge =  (ILocalGISEdge) graphBuilder.buildEdge(n1, n2,-1,-1,idFeature, idLayer, ls.getLength(),feature);
		lgStreetDynamicEdge.setGeometry(ls); // almacena la geometrÌa sint√©tica que la ha generado.
		     // TODO la geometrÌa est√° mejor representada en feature
		lgStreetDynamicEdge.setFeature(feature);

		getGraphBuilder().addEdge(lgStreetDynamicEdge);

		return lgStreetDynamicEdge;
	}

	private PMRLocalGISStreetDynamicEdge addPMRElement(LineString ls, int idFeature, int idLayer, int geometrySRID, Feature feature)
	{
		GeographicNode n1, n2;

		PMRLocalGISStreetDynamicEdge lgStreetDynamicEdge = null;

		CoordinateReferenceSystem crs= getDefaultCRS();

		try {

			crs = CRS.decode("EPSG:" + Integer.toString(geometrySRID));

		} catch (NoSuchAuthorityCodeException e) {
			crs= getDefaultCRS();
		} catch (FactoryException e) {
			crs= getDefaultCRS();
		}

		Point p= ls.getStartPoint();

		// look up first node and create if necessary
		if ((n1 = (GeographicNode) coord2Node.get(p.getCoordinate())) == null) {
			n1 = (GeographicNode) getGraphBuilder().buildNode();

			/** 
			 * JPC Workaround!! JTSUtils.jtsToGo1 fails to find JTSGeometryFactory in Geotools registry
			 *n1.setPosition((org.opengis.geometry.primitive.Point) JTSUtils.jtsToGo1(p, crs));
			 *TODO: investigate effect!
			 */
			n1.setPosition(GeographicNodeUtil.createPoint(p.getCoordinate(), crs));

			getGraphBuilder().addNode(n1);
			p.setSRID(geometrySRID);
			coord2Node.put(p.getCoordinate(), n1);
		}

		// look up second node and create if necessary
		p=ls.getEndPoint();
		if ((n2 = (GeographicNode) coord2Node.get(p.getCoordinate())) == null) {
			n2 = (GeographicNode) getGraphBuilder().buildNode();
			n2.setPosition(GeographicNodeUtil.createPoint(p.getCoordinate(), crs));

			getGraphBuilder().addNode(n2);
			p.setSRID(geometrySRID);
			coord2Node.put(p.getCoordinate(), n2);
		}

		// create edge and set underlying object
		
		LocalGISStreetGraphBuilder localGISStreetGraphBuilder = (LocalGISStreetGraphBuilder)getGraphBuilder();
		lgStreetDynamicEdge = (PMRLocalGISStreetDynamicEdge) localGISStreetGraphBuilder.buildPMREdge(n1, n2,-1,-1,idFeature, idLayer, ls.getLength(),feature);
		lgStreetDynamicEdge.setGeometry(ls); // almacena la geometrÌa sint√©tica que la ha generado.
		     // TODO la geometrÌa est√° mejor representada en feature
		lgStreetDynamicEdge.setFeature(feature);	

		getGraphBuilder().addEdge(lgStreetDynamicEdge);

		return lgStreetDynamicEdge;
	}

	@Override
	public Graphable add(Object obj)
	{
	    if (obj instanceof LocalGISResultSet)// delegate to LocalGISGraphGenerator
		{
		    
		    return delegateGenerator.add(obj); 
		}
	    else 
		{ // ser√° un LineString o similar
		    return super.add(obj);
		}
	}
	
	private Point roundPoint(Point point, int places){
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
	
	private double truncate(double value, int places) {
	    double multiplier = Math.pow(10, places);
	    return Math.floor(multiplier * value) / multiplier;
	}
}
