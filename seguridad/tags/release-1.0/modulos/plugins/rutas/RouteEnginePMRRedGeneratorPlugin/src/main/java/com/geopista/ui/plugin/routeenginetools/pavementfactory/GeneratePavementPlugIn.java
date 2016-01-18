package com.geopista.ui.plugin.routeenginetools.pavementfactory;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.managers.AllInMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.pavementfactory.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

import es.uva.idelab.route.algorithm.BlocksAlgorithm;
import es.uva.idelab.route.algorithm.GeometrySideWalkFactory;
import es.uva.idelab.route.algorithm.SidewalkEdge;

/**
 * Plugin para la generacion de aceras para personas de movilidad reducida de forma gráfica
 * @author miriamperez
 *
 */

public class GeneratePavementPlugIn extends AbstractPlugIn {

	private PlugInContext context = null;
	private boolean pavementButtonAdded = false;
	private String CALLEJEROPMR = "CallejeroPMR";
	private static Logger LOGGER = Logger.getLogger(GeneratePavementPlugIn.class);
	private double distance = 0.05;
	private LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();

	public boolean execute(PlugInContext context) throws Exception {
		this.context= context; 
		GeneratePavementDialog dialog = new GeneratePavementDialog(context.getWorkbenchFrame(), "", context);
		if (!dialog.wasOKPressed()) return false;
		if (dialog.getNombreRedTextField().getText().equals("")) return false;
		NetworkManager networkMgr = FuncionesAuxiliares.getNetworkManager(context);
		Network network = networkMgr.getNetwork(dialog.getNombreRedTextField().getText());
		Graph graph = network.getGraph();
		createPavementLayer(graph);
		return false;
	}


	/**
	 * Inserto las features tipo acera en la capa "aceras" de LocalGIS
	 * @param context
	 * @param linegenerator
	 */
	private List createPavementLayer(Graph graph){
		GeopistaLayer layer = (GeopistaLayer)this.context.getLayerManager().getLayer("aceraspmr");
		if (layer == null){
			JOptionPane.showMessageDialog(null, I18N.get("genred", "CargarCapaAceras"));
			return null;
		}
		
		//Se borran las aceras anteriores del municipio si éstas existieran
		try{
			String sql = "DELETE FROM aceraspmr WHERE id_municipio ="+AppContext.getIdMunicipio();
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			Connection conn = connectionFactory.getConnection();
			nDAO.updateQuery(sql, conn);	
		}catch(Exception e){
			LOGGER.error(e);
		}
		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
		Iterator<Edge> itEdges = ((AllInMemoryManager)((DynamicGraph)graph).getMemoryManager()).getUpdateMonitor().getDirtyEdges().iterator();
		int indexMunicipio = layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("id_municipio");
		int indexGeometry = layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("GEOMETRY");
		int cont = 1;
		GeopistaFeature featureBase = new BasicFeature(layer.getFeatureCollectionWrapper().getFeatureSchema());
		List featuresList = new ArrayList();
		List edgesWithFeaturesList = new ArrayList();
		while (itEdges.hasNext()){
			PMRLocalGISStreetDynamicEdge edge = (PMRLocalGISStreetDynamicEdge)itEdges.next();
			
			LineString pavementGeom = (LineString) edge.getGeom();
			
			if (pavementGeom!=null)
			{
//			LineString lineString = factory.createLineString(new Coordinate[]{((XYNode)edge.getNodeA()).getCoordinate(),((XYNode)edge.getNodeB()).getCoordinate()});
			GeopistaFeature feature = (GeopistaFeature)featureBase.clone();
			feature.setGeometry(pavementGeom);
			feature.setNew(false);
			feature.setLayer(layer);
			feature.setAttribute(indexMunicipio, AppContext.getIdMunicipio());
			feature.setAttribute(indexGeometry, pavementGeom);
			featuresList.add(feature);
			edge.setIdLayer(feature.getLayer().getId_LayerDataBase());
			edgesWithFeaturesList.add(edge);
			}
			else
			{
				LOGGER.warn("A sidewalk without Geometry was found!"+edge);
			}
		}
		layer.getFeatureCollectionWrapper().addAll(featuresList);
		int i = 0;
		itEdges = ((AllInMemoryManager)((DynamicGraph)graph).getMemoryManager()).getUpdateMonitor().getDirtyEdges().iterator();
		while (itEdges.hasNext()){
			PMRLocalGISStreetDynamicEdge edge = (PMRLocalGISStreetDynamicEdge)itEdges.next();
			if (edge.getGeom()!=null)
			{
				GeopistaFeature feature = (GeopistaFeature)featuresList.get(i);
				edge.setIdFeature(Integer.valueOf(feature.getSystemId()));
				i++;
			}
		}
		return edgesWithFeaturesList;
	}

	public Icon getIcon() {
        return IconLoader.icon("generatePavement.gif");
    }

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.genredplugin.language.RouteEngine_GenRedi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("genred",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!pavementButtonAdded  )
		{
			GeneratePavementPlugIn explode = new GeneratePavementPlugIn();
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			pavementButtonAdded = true;
		}
	}
}