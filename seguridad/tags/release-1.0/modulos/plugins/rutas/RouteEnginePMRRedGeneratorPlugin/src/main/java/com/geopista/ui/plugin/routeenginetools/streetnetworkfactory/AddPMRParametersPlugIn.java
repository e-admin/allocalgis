package com.geopista.ui.plugin.routeenginetools.streetnetworkfactory;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.managers.AllInMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.AddPMRParametersDialog;
import com.geopista.ui.plugin.routeenginetools.pavementfactory.GeneratePavementDialog;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.weighter.PMRProperties;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * Plugin para la generacion de aceras para personas de movilidad reducida
 * @author miriamperez
 *
 */

public class AddPMRParametersPlugIn extends AbstractPlugIn {

	private PlugInContext context = null;
	private boolean addPMRParametersButtonAdded = false;

	private static Logger LOGGER = Logger.getLogger(AddPMRParametersPlugIn.class);
	private PMRProperties pmrPropertiesFeatures;
	private Network subRed = null;
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	public boolean execute(PlugInContext context) throws Exception {
		GeneratePavementDialog dialogNetwork = new GeneratePavementDialog(context.getWorkbenchFrame(), "", context);
		if (!dialogNetwork.wasOKPressed()) return false;
		if (dialogNetwork.getNombreRedTextField().getText().equals("")) return false;
		NetworkManager networkMgr = FuncionesAuxiliares.getNetworkManager(context);
		Network network = networkMgr.getNetwork(dialogNetwork.getNombreRedTextField().getText());
		
		if (network == null){
			JOptionPane.showMessageDialog(null, I18N.get("genred","IntroducirNombreValido"), "", JOptionPane.ERROR_MESSAGE); 
			return false;
		}

		if(context.getLayerViewPanel() == null)
			return false;
		this.context = context;

		List selectedFeatures = (List)context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
		int n = selectedFeatures.size();
		if (n == 0){
			JOptionPane.showMessageDialog(null, I18N.get("genred","debe.seleccionar.features"));
			return false;
		}

		for (int i=0;i<n;i++){
			Feature feature = (Feature)selectedFeatures.get(i);
			if (!feature.getSchema().hasAttribute("idEje")){
				JOptionPane.showMessageDialog(null, "Debe seleccionar un eje de la red");
				return false;
			}
			if (feature.getAttribute("idEje") == null){
				JOptionPane.showMessageDialog(null, "Debe volver a recargar la red");
				return false;
			}
			DynamicGraph graph = (DynamicGraph)networkMgr.getNetwork(network.getName()).getGraph();
			PMRLocalGISStreetDynamicEdge edge = (PMRLocalGISStreetDynamicEdge)graph.getEdge((Integer)feature.getAttribute("idEje"));
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			Connection conn = connectionFactory.getConnection();
			LocalGISNetworkDAO dao =  new LocalGISNetworkDAO();
			dao.getStreetData(network.getName(), edge, conn);
			PMRProperties pmrProperties = new PMRProperties();
			pmrProperties.setMaxLongitudinalSlope(edge.getLongitudinalSlope());
			pmrProperties.setMaxTransversalSlope(edge.getTransversalSlope());
			pmrProperties.setMinWidth(edge.getWidth());
			pmrProperties.setObstacleHeight(edge.getObstacleHeight());
			AddPMRParametersDialog dialog = new AddPMRParametersDialog(context.getWorkbenchFrame(), "", context, pmrProperties);
			pmrPropertiesFeatures = dialog.getPmrProperties();
			if (pmrPropertiesFeatures == null) return false;
			((PMRLocalGISStreetDynamicEdge)edge).setLongitudinalSlope(pmrPropertiesFeatures.getMaxLongitudinalSlope());
			((PMRLocalGISStreetDynamicEdge)edge).setTransversalSlope(pmrPropertiesFeatures.getMaxTransversalSlope());
			((PMRLocalGISStreetDynamicEdge)edge).setWidth(pmrPropertiesFeatures.getMinWidth());
			((PMRLocalGISStreetDynamicEdge)edge).setObstacleHeight(pmrPropertiesFeatures.getObstacleHeight());
			feature.setAttribute("anchuraAcera", pmrPropertiesFeatures.getMinWidth());
			feature.setAttribute("pendienteTransversal", pmrPropertiesFeatures.getMaxTransversalSlope());
			feature.setAttribute("pendienteLongitudinal", pmrPropertiesFeatures.getMaxLongitudinalSlope());
			feature.setAttribute("alturaObstaculo", pmrPropertiesFeatures.getObstacleHeight());
			dao.updateStreetData(network.getName(), edge, conn);				
		}
		return false;
	}


    public Icon getIcon() {
        return IconLoader.icon("loadManualParameters.gif");
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
		if (!addPMRParametersButtonAdded  )
		{
			AddPMRParametersPlugIn explode = new AddPMRParametersPlugIn();
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			addPMRParametersButtonAdded = true;
		}
	}
}