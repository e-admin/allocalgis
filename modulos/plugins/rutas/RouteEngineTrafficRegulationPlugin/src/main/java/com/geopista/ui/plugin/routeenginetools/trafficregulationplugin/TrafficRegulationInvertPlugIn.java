/**
 * TrafficRegulationInvertPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.trafficregulationplugin;

import java.awt.Graphics2D;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.routelabelstyle.RouteArrowLineStyle;
import com.geopista.ui.plugin.routeenginetools.trafficregulationplugin.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.Viewport;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class TrafficRegulationInvertPlugIn extends TrafficRegulationEditPlugIn{

	private boolean trafficDirectionInvertModifierButtonAdded = false;
	private static AppContext aplication = (AppContext) AppContext.getApplicationContext();


	public boolean execute(PlugInContext context) throws Exception {
		return super.execute(context);
	}

	@SuppressWarnings("static-access")
	@Override
	public void initialize(PlugInContext context) throws Exception {
		super.initialize(context);

		
		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}

	@Override
	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("regulacionTrafico","routeengine.trafficregulation.reverticonfile"));
	}

	@Override
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!trafficDirectionInvertModifierButtonAdded  )
		{
			//			toolbox.addToolBar();
			TrafficRegulationInvertPlugIn explode = new TrafficRegulationInvertPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			trafficDirectionInvertModifierButtonAdded = true;
		}
	}

	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {
		this.setMonitor(monitor);
		this.getMonitor().report(I18N.get("regulacionTrafico","routeengine.trafficregulation.savingdatainfomessage"));

		// Manejar el trafficRegulation
		if (this.getTrafficRegualtion().equals(StreetTrafficRegulation.DIRECT)){
			this.setTrafficRegualtion(StreetTrafficRegulation.INVERSE);
		} else if (this.getTrafficRegualtion().equals(StreetTrafficRegulation.INVERSE)){
			this.setTrafficRegualtion(StreetTrafficRegulation.DIRECT);
		} else if (this.getTrafficRegualtion().equals(StreetTrafficRegulation.FORBIDDEN)
				|| (this.getTrafficRegualtion().equals(StreetTrafficRegulation.BIDIRECTIONAL))){
			this.setTrafficRegualtion(StreetTrafficRegulation.DIRECT);
		}

		saveTrafficRegulationInformation(context, monitor);

	}

	/**
	 * @param context
	 * @param monitor 
	 * @throws IOException
	 */
	private void saveTrafficRegulationInformation(PlugInContext context, TaskMonitor monitor) throws IOException {
		String netName="";
		if(getSelectedLayer().getSystemId().startsWith("Arcos-")){
			String[] a = getSelectedLayer().getSystemId().split("-");
			netName = a[1];
			System.out.println(a.length);
			for (int i=2; i < a.length; i++){
				netName = netName + "-" + a[i];
			}
		} else{
			netName = getSelectedLayer().getSystemId();
		}

		ArrayList<GeopistaFeature> 	listaFeatures = this.getSelectedsArrayListStreetFeature();
		ArrayList<Integer> 			listaEdgeIds = this.getSelectedsArrayListEdgesIds();
		
		for (int i=0; i < listaFeatures.size(); i++){
			
			monitor.report("Modificando Direccion tramo " + i + " de " + listaEdgeIds.size());
			
			Edge selectedEdge = searchEdgeByIdAndNetworkName(context, netName, listaEdgeIds.get(i));

			if (selectedEdge instanceof LocalGISStreetDynamicEdge){
				//Se actualiza el StreetEdge
				((LocalGISStreetDynamicEdge)selectedEdge).setTrafficRegulation(
						getTrafficRegualtion());


				RouteArrowLineStyle line = new RouteArrowLineStyle.BiDirect(
						(Viewport)context.getLayerViewPanel().getViewport(),
						(Graphics2D) ((LayerViewPanel)context.getLayerViewPanel()).getGraphics());
				if (this.getSelectedLayer().getStyle(RouteArrowLineStyle.class) != null){
					getSelectedLayer().removeStyle(getSelectedLayer().getStyle(RouteArrowLineStyle.class));
				}
				getSelectedLayer().addStyle(line);

				GeopistaFeature feat = listaFeatures.get(i);

				if (feat!= null){
					
					feat.setAttribute(I18N.get("regulacionTrafico","routeengine.trafficregulation.trafficregulationattributename"), getTrafficRegualtion().toString());
					
					if (feat.getSchema().hasAttribute("pintadaRegulacionTrafico") && 
							feat.getAttribute("pintadaRegulacionTrafico") != null &&
							feat.getAttribute("pintadaRegulacionTrafico") instanceof Integer){
						try{
							feat.setAttribute("pintadaRegulacionTrafico", 1);
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}

			NetworkManager nManager = NetworkModuleUtilWorkbench.getNetworkManager(context);
			Network actualNetwork = nManager.getNetwork(netName);

			// intentamos insercion en la base de datos de la velocidad de la vï¿½a
			Category categoryLayer = context.getLayerManager().getCategory(getSelectedLayer());
			if (!categoryLayer.getName().equals("")){
				if(actualNetwork.getGraph() instanceof DynamicGraph && ((DynamicGraph)actualNetwork.getGraph()).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager){
					RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
					LocalGISNetworkDAO lnDAO = new LocalGISNetworkDAO();
					Connection connection = null;
					try{
						connection = routeConnection.getConnection();
						lnDAO.updateStreetData(netName, (LocalGISStreetDynamicEdge)selectedEdge, connection);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						ConnectionUtilities.closeConnection(connection);
					}
				}
			}
		}
	}

}
