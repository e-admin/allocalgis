package com.geopista.ui.plugin.routeenginetools.streetnetworkfactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.geotools.geometry.jts.JTSUtils;
import org.geotools.referencing.CRS;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.geographic.GeographicNode;

import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
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

    public Hashtable<Integer, GeopistaFeature> getCache() {
		return cache;
	}

	public void setCache(Hashtable<Integer, GeopistaFeature> cache) {
		this.cache = cache;
	}

	public PMRSideWalkFactory( PlugInContext context)
    {
        super();
        this.context = context;
        String sRespuesta = "";
        List layersList = this.context.getLayerManager().getLayers();
    	int n = layersList.size();
        Object[] namesArray = new Object[n];
    	for (int i=0;i<n;i++){
    		namesArray[i] = ((Layer)layersList.get(i)).getName();    		
    	}
        if (sRespuesta ==null || sRespuesta.equals("")){
        	sRespuesta=(String)JOptionPane.showInputDialog(
        					this.context.getWorkbenchFrame(),
        					I18N.get("genred","routeengine.capaBase"),
                            "Capas",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            namesArray,
                            null);
        }
        if (sRespuesta !=null && !sRespuesta.equals("")){
        	Layer layer = null;
        	for (int i=0;i<n;i++){
        		if (namesArray[i].equals(sRespuesta)){
        			layer = ((Layer)layersList.get(i));
        			break;
        		}
        		
        	}
	        List features = (((GeopistaLayer)layer).getFeatureCollectionWrapper()).getFeatures();
	        cache= new Hashtable<Integer, GeopistaFeature>();
	        for (Object feat : features) {
	        	GeopistaFeature geofeat = ((GeopistaFeature)feat);
				cache.put(Integer.valueOf(geofeat.getSystemId()), geofeat);
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
    * Implement a fake linestring generator to prove that geometry treatment pass the tests
    */
    @Override
    protected LineString getLineStringFor(Edge edge)
    {
/*    	Coordinate pos1=JTSUtils.directPositionToCoordinate(((GeographicNode)edge.getNodeA()).getPosition().getDirectPosition());
        Coordinate pos2=JTSUtils.directPositionToCoordinate(((GeographicNode)edge.getNodeB()).getPosition().getDirectPosition());
    	Coordinate pos12=new Coordinate((pos1.x+pos2.x)/2,(pos1.y+pos2.y)/2);

    	LineString ls=geomFact.createLineString(new Coordinate[]{pos1,pos12,pos2});
    	return ls;*/
    	GeopistaFeature feature = cache.get(((LocalGISStreetDynamicEdge)edge).getIdFeature());
    	LineString lineString = (LineString) feature.getGeometry();
    	Coordinate coordNodeA = ((XYNode)edge.getNodeA()).getCoordinate();
    	Coordinate coordNodeB = ((XYNode)edge.getNodeB()).getCoordinate();
    	double distanceNodeA = lineString.getCoordinates()[0].distance(coordNodeA);
    	double distanceNodeB = lineString.getCoordinates()[0].distance(coordNodeB);
    	if (distanceNodeA < distanceNodeB)
    		return lineString;
    	else
    		return (LineString)lineString.reverse();
   	}

    @Override
    protected double getDisplacementFor(SidewalkEdge edge1)
    {
    	return 1;
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
        SidewalkEdge sidewalkEdge=new PMRLocalGISStreetDynamicEdge(relatedto, side ,sidewalkNodeA,sidewalkNodeB, edgesUidGenerator);
        
        sidewalkNodeA.add(sidewalkEdge);
        sidewalkNodeB.add(sidewalkEdge);

        try {
			correctGeometries(sidewalkEdge);
		} catch (Exception e) {
			logger.warn("Hay geometrias que no se pueden tratar para generar la representación gráfica: "+sidewalkEdge);
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
}

