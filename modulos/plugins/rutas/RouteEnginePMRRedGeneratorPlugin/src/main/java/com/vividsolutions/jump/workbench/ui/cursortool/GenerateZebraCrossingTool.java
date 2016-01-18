/**
 * GenerateZebraCrossingTool.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.cursortool;

import java.awt.Cursor;
import java.awt.geom.NoninvertibleTransformException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.geographic.GeographicVirtualNode;
import org.uva.route.network.Network;
import org.uva.routeserver.managers.AllInMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.PMRStreetNetworkFactoryPlugIn;
import com.geopista.util.ConnectionUtilities;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.AbstractVectorLayerFinder;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.UndoableCommand;

public class GenerateZebraCrossingTool extends VectorTool{

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Network subRed;
	private SequenceUIDGenerator sequenceEdges = new SequenceUIDGenerator();	
	private int edgeId;
    private GraphGenerator newLinegenerator;
	private static Logger LOGGER = Logger.getLogger(PMRStreetNetworkFactoryPlugIn.class);
	

	protected GenerateZebraCrossingTool(Network subRed) {
		super();
		this.subRed = subRed;
	newLinegenerator = NetworkModuleUtilWorkbench.getLocalGISStreetGraphGenerator();
		AllInMemoryManager memmgr = new AllInMemoryManager();
		memmgr.setGraph(subRed.getGraph());
		DynamicGraph dynamicGraph = new DynamicGraph(memmgr);		
		newLinegenerator.getGraphBuilder().importGraph(dynamicGraph);
	}

	
	public String getName(){
	    return aplicacion.getI18nString("GenerarPasoCebra");
	}
	
	public Icon getIcon() {
	    return  com.geopista.ui.images.IconLoader.icon("dinamico_clip.gif");
	}
	
	public Cursor getCursor() {
	    return createCursor(com.geopista.ui.images.IconLoader.icon("dinamico_clip_cursor.gif").getImage());
	}


    protected AbstractVectorLayerFinder createVectorLayerFinder(LayerManagerProxy layerManagerProxy) {
        return new ZebraVectorLayerFinder(layerManagerProxy, getPanel());
    }
    
    protected UndoableCommand createCommand() throws NoninvertibleTransformException {
        final AbstractVectorLayerFinder vectorLayerFinder =
            createVectorLayerFinder(getPanel());
        final boolean vectorLayerExistedOriginally = vectorLayerFinder.getLayer() != null;
        final LineString lineString = lineString(getModelSource(), getModelDestination());
        GeopistaLayer layer = (GeopistaLayer)this.getPanel().getLayerManager().getLayer("aceraspmr");
		GeopistaFeature geopistaFeature = new BasicFeature(layer.getFeatureCollectionWrapper().getFeatureSchema());
		geopistaFeature.setGeometry(lineString);
		geopistaFeature.setLayer(layer);
		layer.getFeatureCollectionWrapper().add(geopistaFeature);
		List edgesToAppend = new ArrayList();
		writeToDatabase((DynamicGraph)newLinegenerator.getGraph(),edgesToAppend);
		return new UndoableCommand(getName()) {
            private Feature vector;
            private boolean vectorLayerVisibleOriginally;
            public void execute() {
                if (!vectorLayerExistedOriginally) {
                    vectorLayerFinder.createLayer();
                }
                if (vector == null) {
                    //Cache the vector because (1) we don't want to create a new feature
                    //when redo is pressed. [Jon Aquino]
                    vector = feature(lineString, vectorLayerFinder.getLayer(), this);
                }
                vectorLayerFinder.getLayer().getFeatureCollectionWrapper().add(vector);
                vectorLayerVisibleOriginally = vectorLayerFinder.getLayer().isVisible();
                vectorLayerFinder.getLayer().setVisible(true);
            }
            public void unexecute() {
                vectorLayerFinder.getLayer().setVisible(vectorLayerVisibleOriginally);
                vectorLayerFinder.getLayer().getFeatureCollectionWrapper().remove(vector);
                if (!vectorLayerExistedOriginally) {
                    getPanel().getLayerManager().remove(vectorLayerFinder.getLayer());
                }
            }
        };
    }

    protected TaskComponent getTaskFrame() {
        return (TaskComponent) SwingUtilities.getAncestorOfClass(TaskComponent.class,
           (JComponent) getPanel());
    }
    
    /**
     * Hallo el nodo mas cercano de la subred a una coordenada
     * @param coordinate
     * @return
     */
    private Node nearestNode(Coordinate coordinate){
    	Iterator nodesIterator = subRed.getGraph().getNodes().iterator();
    	double distance = Double.MAX_VALUE;
    	Node candidateNode = null;
    	while (nodesIterator.hasNext()){
    		Node node = (Node) nodesIterator.next();
    		if (node instanceof GeographicVirtualNode) continue;
    		Coordinate nodeCoordinate = ((XYNode)node).getCoordinate();
    		if (distance > coordinate.distance(nodeCoordinate)){
    			distance = coordinate.distance(nodeCoordinate);
    			candidateNode = node;
    		}
    	}
    	return candidateNode;
    }
    
    /**
     * Crea un nuevo edge para simular el paso de cebra
     * @param node1
     * @param node2
     * @return
     */
    private Edge createEdge (Node node1, Node node2){
    	try {
			SequenceUIDGenerator sequenceEdge = new SequenceUIDGenerator();
			LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
			sequenceEdge.setSeq(nDAO.getNextDatabaseIdEdge(AppContext.getApplicationContext().getConnection()));
			ZebraDynamicEdge newEdge = new ZebraDynamicEdge("CON REBAJE",node1, node2,-1,-1, sequenceEdge,null);
	    	newEdge.setIdLayer(((PMRLocalGISStreetDynamicEdge)node1.getEdges().get(0)).getIdLayer());
	    	newEdge.setWidth(200);
	    	newEdge.setTransversalSlope(0);
	    	newEdge.setLongitudinalSlope(0);
	    	node1.add(newEdge);
	    	node2.add(newEdge);
	    	newLinegenerator.getGraphBuilder().addEdge(newEdge);
	    	// TODO no sustituir el graph sino anexar nuevo edge
	    	
	    	AllInMemoryManager memmgr = new AllInMemoryManager();
			DynamicGraph dynamicGraph = new DynamicGraph(memmgr);		
			memmgr.setGraph(newLinegenerator.getGraph());
		
		//subRed.setGraph(dynamicGraph);
		// BUG JPC reusa graph si es posible
		if (subRed.getGraph() instanceof DynamicGraph)
		    {
			DynamicGraph graph = (DynamicGraph) subRed.getGraph();
			graph.getMemoryManager().appendGraph(dynamicGraph);
		    }
		return newEdge;
    	}catch(Exception e){
    		LOGGER.error(e);
    		return null;
    	}
    }
    
    protected LineString lineString(Coordinate source, Coordinate destination)
    throws NoninvertibleTransformException {
    	Node sourceNode = nearestNode(source);
    	Node destinationNode = nearestNode(destination);
    	Edge edge = createEdge(sourceNode,destinationNode);
    	
		return geometryFactory.createLineString(new Coordinate[] { ((XYNode)sourceNode).getCoordinate(), ((XYNode)destinationNode).getCoordinate() });
    }
    
	
	private void writeToDatabase(DynamicGraph graph, List edgesToAppend){
		DynamicGraph resultGraph = null;
		LocalGISRouteReaderWriter db = null;
		Connection conn = null;
		try {

			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
			db = new LocalGISStreetRouteReaderWriter(routeConnection, true);
			db.setNetworkName(this.subRed.getName());
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			conn = connectionFactory.getConnection();
			if (graph != null) {
				Iterator<Edge> itEdge = edgesToAppend.iterator();
				while (itEdge.hasNext()){
					Edge e = itEdge.next();
					if (e instanceof LocalGISStreetDynamicEdge){
						db.writeEdge(e,conn);
						((LocalGISStreetRouteReaderWriter)db).writeStreetData(e, conn);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}finally{
			ConnectionUtilities.closeConnection(conn);
		}

	}
	
	
}