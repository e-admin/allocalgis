/**
 * GeneratePavementPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.pavementfactory;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.pavementfactory.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.geopista.util.ApplicationContext;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * Plugin para la generacion de aceras para personas de movilidad reducida de forma grafica
 * @author miriamperez
 *
 */

public class GeneratePavementPlugIn extends ThreadedBasePlugIn 
{

	private PlugInContext context = null;
	private boolean pavementButtonAdded = false;
	private String CALLEJEROPMR = "CallejeroPMR";
	private static Logger LOGGER = Logger.getLogger(GeneratePavementPlugIn.class);
	private double distance = 0.05;
	private LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
	private TaskMonitor monitor;
	private GeneratePavementDialog dialog;

	public boolean execute(PlugInContext context) throws Exception {
		this.context= context; 
//		this.monitor = new TaskMonitorDialog(context.getWorkbenchFrame(), context.getWorkbenchFrame());
		
		 GeopistaLayer layer = (GeopistaLayer) context.getLayerManager().getLayer(GeneratePavementDialog.ACERASPMR_SYSTEMLAYERNAME);
		    if (layer == null){
				JOptionPane.showMessageDialog(context.getWorkbenchFrame(), I18N.get("genred", "CargarCapaAceras"));
				return true;
			}
		dialog = new GeneratePavementDialog(context.getWorkbenchFrame(),"",  context, getDescription(),false,true);
		dialog.setVisible(true);
		if (!dialog.wasOKPressed()) return false;
//		if ("".equals(dialog.getSelectedNetwork()))
//		    return false;
//		Network network = networkMgr.getNetwork(dialog.getSelectedNetwork());
		new TaskMonitorManager().execute(this,this.context);
		
		return false;
	}
	public void run(TaskMonitor monitor, PlugInContext runContext)throws Exception
	{
		try
		{
		    this.monitor=monitor;
			NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
			Network network= NetworkModuleUtil.getSubNetwork(networkMgr, dialog.getRedSeleccionada(), dialog.getSubredseleccionada());
			createPavementLayer(network);
		}catch (IOException e) {
			// TODO: handle exception
			if(e.getMessage()!=null && e.getMessage().contains("No se han grabado los cambios de memoria"))
			{
				JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), e.getMessage());
			}
		}
	}

	private String getDescription()
	{
	    return "Genera las Features que representan la red y asigna en cada arco los identificadores de las Features para uso futuro en las aplicaciones de enrutado. " +
	   		"Si la capa de destino es \""+GeneratePavementDialog.ACERASPMR_SYSTEMLAYERNAME+"\" se actualizara automaticamente la informacion el LocalGIS. " +
	   		"Si se usa otra capa los cambios no seran guardados automaticamente.";
	}


	/**
	 * Inserto las features tipo acera en la capa "aceras" de LocalGIS
	 * @throws IOException 
	 * @throws  
	 * @throws SQLException 
	 * 
	 */
	private List createPavementLayer(Network network) throws SQLException, IOException
	{
	    Graph graph = network.getGraph();
	    
	    GeopistaLayer layer = (GeopistaLayer) dialog.getLayer(GeneratePavementDialog.TARGET_LAYER_FIELDNAME);
	    if (layer == null){
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), I18N.get("genred", "CargarCapaAceras"));
			return null;
		}
	   if (JOptionPane.CANCEL_OPTION== JOptionPane.showConfirmDialog(context.getWorkbenchFrame(), "La capa "+layer.getName()+" se vaciará de todo su contenido antes de cargarse con las nuevas features.","Confimación",JOptionPane.OK_CANCEL_OPTION))
	       {
		   return null;
	       }
		monitor.report("Creando en el sistema la red de aceras en la capa: "+layer.getName());
		monitor.allowCancellationRequests();
		int idMunicipio = AppContext.getIdMunicipio();
		context.getLayerManager().setFiringEvents(false);
		if (!layer.isLocal() && layer.getName().equals(GeneratePavementDialog.ACERASPMR_SYSTEMLAYERNAME))
		    {
			
        		//Se borran las aceras anteriores del municipio si estas existieran
			String sql = "DELETE FROM aceraspmr WHERE id_municipio ="+idMunicipio;
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			Connection conn = connectionFactory.getConnection();
			nDAO.updateQuery(sql, conn);
			
		    }// TODO BUG Borrar mediante el UpdateManagerPlugin de forma automatica
	try{
	    	int indexMunicipio = layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("id_municipio");
		int indexGeometry = layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("GEOMETRY");
		
		monitor.report("Vaciando la capa receptora: "+layer.getName());
		List features = layer.getFeatureCollectionWrapper().getFeatures();
		List featuresToDelete = new ArrayList();
		for (Object feat : features)
		    {
			Object attribute = ((Feature)feat).getAttribute(indexMunicipio);
			if (Integer.parseInt(attribute.toString())==idMunicipio)
			    {
				featuresToDelete.add(feat);
			    }
		    }
		monitor.report("Eliminando las features generadas en la capa.");
		layer.getFeatureCollectionWrapper().removeAll(featuresToDelete);
		context.getLayerManager().setFiringEvents(true);
		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
		
//BUG JPC Coge dirtyedges?		Iterator<Edge> itEdges = ((AllInMemoryManager)((DynamicGraph)graph).getMemoryManager()).getUpdateMonitor().getDirtyEdges().iterator();
		int cont = 1;
		GeopistaFeature featureBase = new BasicFeature(layer.getFeatureCollectionWrapper().getFeatureSchema());
		List featuresList = new ArrayList();
		List<Edge> edgesWithFeaturesList = new ArrayList<Edge>();
		int progress=1;
		Collection<Edge> edges = graph.getEdges();
		int total=edges.size();
	
		
		
		for(Edge edge:edges)
		    {
			monitor.report(progress, total, "Procesando arco "+progress+" de "+total);
			if (monitor.isCancelRequested())
			    {
				context.getWorkbenchGuiComponent().warnUser("Cancelado por el usuario.");
				return null;
			    }
			PMRLocalGISStreetDynamicEdge pmrEdge = (PMRLocalGISStreetDynamicEdge)edge;
			
			LineString pavementGeom = (LineString) NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(pmrEdge, context);
			pmrEdge.setGeometry(pavementGeom);
			if (pavementGeom!=null)
			{
//			LineString lineString = factory.createLineString(new Coordinate[]{((XYNode)edge.getNodeA()).getCoordinate(),((XYNode)edge.getNodeB()).getCoordinate()});
			GeopistaFeature feature = (GeopistaFeature)featureBase.clone();
			feature.setGeometry(pavementGeom);
			feature.setNew(true); // TODO JPC BUG Â¿porque es setNew(false) ?
			feature.setLayer(layer);
			feature.setAttribute(indexMunicipio, idMunicipio);
			feature.setAttribute(indexGeometry, pavementGeom);
			
			featuresList.add(feature);
			pmrEdge.setIdLayer(feature.getLayer().getId_LayerDataBase());
			pmrEdge.setFeature(feature);
			
			edgesWithFeaturesList.add(edge);
			}
			else
			{
				LOGGER.warn("A sidewalk without Geometry was found!"+edge);
			}
		}
		

	monitor.report("Guardando todas las features generadas en la capa.");
	layer.getFeatureCollectionWrapper().addAll(featuresList);
	
		progress=1;
		int i = 0;
//BUG hay que procesar todos los edges		itEdges = ((AllInMemoryManager)((DynamicGraph)graph).getMemoryManager()).getUpdateMonitor().getDirtyEdges().iterator();
	// Asume que la secuencia de features se mantiene tras la actualizacion de los Ids por parte del servidor.

		for(Edge edge:edgesWithFeaturesList)
		    {
			monitor.report(progress, total, "Asignando nuevos identificadores "+progress+" de "+total);
//			if (monitor.isCancelRequested())
//			    {
//				context.getWorkbenchGuiComponent().warnUser("Cancelado por el usuario.");
//				return null;
//			    }
			PMRLocalGISStreetDynamicEdge pmrEdge = (PMRLocalGISStreetDynamicEdge)edge;
			Geometry geometry = NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(pmrEdge, context);
			if (geometry!=null)// misma comprobacion que en el bucle de generacion más arriba
			{
				GeopistaFeature feature = (GeopistaFeature)featuresList.get(i);
				if (feature.isTempID()) // usa id nativo
				    pmrEdge.setIdFeature(feature.getID());
				else
				    pmrEdge.setIdFeature(Integer.valueOf(feature.getSystemId()));
				i++;
			}
		}
		monitor.report("Red actualizada. Inicio del guardado automatico de la red si está vinculada a un almacen de datos.");

		NetworkModuleUtil.addDirtyGraphablesIsSupported(graph, edgesWithFeaturesList);
		if (graph instanceof DynamicGraph)
		    {
			((DynamicGraph) graph).commit();
		    }
		return edgesWithFeaturesList;
		
	}catch(IllegalArgumentException ex)
	{
	    JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "ERROR EN EL PROCESO. Posible causa: La capa destino debe contener al menos una columna \"id_municipio\" de tipo INTEGER.");
	    return null;
	}
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