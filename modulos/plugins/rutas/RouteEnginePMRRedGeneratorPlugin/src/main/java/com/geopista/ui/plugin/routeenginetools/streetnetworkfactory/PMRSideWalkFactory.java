/**
 * PMRSideWalkFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.streetnetworkfactory;

import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.geotools.referencing.CRS;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;

import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

import es.uva.idelab.route.algorithm.GeometrySideWalkFactory;
import es.uva.idelab.route.algorithm.SidewalkEdge;

public class PMRSideWalkFactory extends GeometrySideWalkFactory
{
    private PlugInContext context = null;
    GeometryFactory geomFact=new GeometryFactory();
    private SequenceUIDGenerator              uidGenerator;
    private SequenceUIDGenerator              edgesUidGenerator; // for legibility use different sequences
    private static Logger logger = Logger.getLogger(PMRSideWalkFactory.class);
    Hashtable<Integer, GeopistaFeature> cache;
    double displacement=2;

    public Hashtable<Integer, GeopistaFeature> getCache() {
		return cache;
	}

	public void setCache(Hashtable<Integer, GeopistaFeature> cache) {
		this.cache = cache;
	}

	public PMRSideWalkFactory(Layer layer, PlugInContext context)
    {
        super();
        this.context = context;
       
        if (layer !=null)
            {
        	
	        FeatureCollectionWrapper featureCollection = ((GeopistaLayer)layer).getFeatureCollectionWrapper();
		List features = featureCollection.getFeatures();
	        cache= new Hashtable<Integer, GeopistaFeature>();
	        
	        // identifica capa de localgis de grafos
	        // busca campo entero featureId
	        boolean hasFeatureId=false;
	        int attIndex=0;
	        try
	        {
	        attIndex = featureCollection.getFeatureSchema().getAttributeIndex("idFeature");
		if (featureCollection.getFeatureSchema().getAttributeType(attIndex).equals(AttributeType.INTEGER))
		    hasFeatureId=true;
	        }catch (IllegalArgumentException e)// no existe ese atributo
	        {
	            // ignora
	        }
	        
	        for (Object feat : features) {
	        	GeopistaFeature geofeat = ((GeopistaFeature)feat);
	        	 Integer intId;
	        	
	        	 if (geofeat.isTempID())
	        	    {
	        		
	        		if (hasFeatureId) // es una capa de representación de redes
	        		    {
	        			intId=((Integer)geofeat.getAttribute(attIndex));
	        		    }
	        		else
	        		    {
	        			intId=geofeat.getID();
	        		    }
	        	    }
	        	else
			    {
				intId = Integer.valueOf(geofeat.getSystemId());  
			    }
	        	cache.put(intId, geofeat);
			}
        }else{
			cache = null;
        }
    }

    public PMRSideWalkFactory(PlugInContext context, boolean minimal)
    {
        super();
        this.context = context;
    }

    /**
     * Obtiene las features asociadas como fuente de geometrías al edge origen del procesado
    */
    @Override
    protected LineString getLineStringFor(Edge edge)
    {
	LineString lineString=(LineString) NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(edge, context);
//    	Feature feature = cache.get(((LocalGISStreetDynamicEdge)edge).getIdFeature());
//    	if (feature==null)
//    	    {
//    		feature= NetworkModuleUtilWorkbench.findFeatureForEdge(edge, context);
//    	    }
//    	LineString lineString;
//    	if (feature==null)
//    	    {
//    		lineString=(LineString) NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(edge, context);
//    	    }
//    	else
//    	    {
//    	    lineString = (LineString) feature.getGeometry();
//    	    }

    	return NetworkModuleUtil.checkLineStringOrientation(edge,lineString);
   	}

    @Override
    protected double getDisplacementFor(SidewalkEdge edge1)
    {
    	return getDisplacement();
    }

	public CoordinateReferenceSystem getCRS()
	{
		CoordinateReferenceSystem refSys = null;
		try{
			CoordinateSystem coodSys = context.getLayerManager().getCoordinateSystem();
			refSys = CRS.decode("EPSG:"+String.valueOf(coodSys.getEPSGCode()));
		}catch(Exception e){
			logger.debug(e.getMessage());
		}
		return refSys;
	}


    @Override
    public SidewalkEdge createSidewalkInstance(Edge relatedto, Node sidewalkNodeA, Node sidewalkNodeB, int side)
    {
    	
    	if (sidewalkNodeA==null) // create node
        {
    		Point point=((DynamicGeographicNode)relatedto.getNodeA()).getPosition();
            sidewalkNodeA= new DynamicGeographicNode(point, uidGenerator);
        }
        if (sidewalkNodeB==null) // create node
        {
        	Point point=((DynamicGeographicNode)relatedto.getNodeB()).getPosition();
            sidewalkNodeB= new DynamicGeographicNode(point, uidGenerator);
        }
        SidewalkEdge sidewalkEdge=new PMRLocalGISStreetDynamicEdge(relatedto,-1,-1,side ,sidewalkNodeA,sidewalkNodeB, edgesUidGenerator,null);
        if (!sidewalkNodeA.getEdges().contains(sidewalkEdge))
            sidewalkNodeA.add(sidewalkEdge);
        if (!sidewalkNodeB.getEdges().contains(sidewalkEdge))
            sidewalkNodeB.add(sidewalkEdge);

        try {
			correctGeometries(sidewalkEdge);
		} catch (Exception e) {
			logger.warn("Hay geometrias que no se pueden tratar para generar la representacion gráfica: "+sidewalkEdge);
		}
        return sidewalkEdge;
    }

    public void setUIDGenerator(SequenceUIDGenerator ids)
    {
    	this.uidGenerator=ids;
    }

    public void setUIDGeneratorForEdges(SequenceUIDGenerator edges_ids)
    {
    	this.edgesUidGenerator=edges_ids;
    }

    public double getDisplacement()
    {
        return displacement;
    }

    public void setDisplacement(double displacement)
    {
        this.displacement = displacement;
    }
}

