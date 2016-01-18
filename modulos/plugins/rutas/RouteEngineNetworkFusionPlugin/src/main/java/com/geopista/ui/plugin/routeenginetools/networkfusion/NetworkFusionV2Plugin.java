/**
 * NetworkFusionV2Plugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networkfusion;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.uva.geotools.graph.structure.Graph;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.networkfusion.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilToolsCore;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToSelectMemoryNetworks;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

import edu.emory.mathcs.backport.java.util.Arrays;

public class NetworkFusionV2Plugin extends ThreadedBasePlugIn{
	
	private static final String TARGET_NETWORK_FIELDNAME = "Red destino";
	private static final String SOURCE_NETWORK_FIELDNAME = "Red origen";
	private static final String RENUMBERING_YESNO_FIELDNAME = "Renumerar elementos origen antes de copiarlos o mantener (Anexar/Mezclar)";
	private static AppContext app = (AppContext) AppContext.getApplicationContext();
	private boolean buttonAdded;
	private MultiInputDialog dialog;
	private PlugInContext context;
	public void initialize(PlugInContext context) throws Exception {
	    this.context=context;
		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.networkfusion.language.RouteEngine_NetworkFusioni18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("networkFusion",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}
	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("networkFusion","routeengine.networkfusionv2.iconfile"));
	}
	
	public String getName(){
    	String name = I18N.get("networkFusion","routeengine.networkfusionv2.plugintitle");
    	return name;
    }
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!buttonAdded )
		{ 
			toolbox.addPlugIn(this, null, this.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			buttonAdded = true;
		}
	}
	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		MiEnableCheckFactory checkFactory = new MiEnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		.add(checkFactory.createBlackBoardMustBeElementsCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1))
		.add(checkFactory.createNetworksMustBeLoadedCheck());
	}
	@Override
	
	public boolean execute(PlugInContext context)throws Exception 
	{
	NetworkManager manager = NetworkModuleUtilWorkbench.getNetworkManager(context);
	dialog = new MultiInputDialog(context.getWorkbenchFrame(), getName(), true);
	dialog.setSideBarDescription(getDescription());
	dialog.setSideBarImage(getIcon());
	dialog.addRow(SOURCE_NETWORK_FIELDNAME,new JLabel("Red origen"),new PanelToSelectMemoryNetworks(context),null,"");
	dialog.addEnableChecks(SOURCE_NETWORK_FIELDNAME, Arrays.asList(new EnableCheck[]{NetworkModuleUtilToolsCore.createEnableNetworkSelectedCheck(dialog,SOURCE_NETWORK_FIELDNAME)}));
	dialog.addRow(TARGET_NETWORK_FIELDNAME,new JLabel("Red destino"),new PanelToSelectMemoryNetworks(context),null,"");
	dialog.addEnableChecks(TARGET_NETWORK_FIELDNAME, Arrays.asList(new EnableCheck[]{NetworkModuleUtilToolsCore.createEnableNetworkSelectedCheck(dialog,TARGET_NETWORK_FIELDNAME)}));
	dialog.addCheckBox(RENUMBERING_YESNO_FIELDNAME, false);
	dialog.setVisible(true);
        if (dialog.wasOKPressed())
            {
        	 TaskMonitorManager taskMonitorManager = new TaskMonitorManager();
        	 taskMonitorManager.execute((ThreadedPlugIn) this, context);
        	 context.getWorkbenchFrame().setStatusMessage("Elementos copiados de red origen a destino. ");
            }
        return true;
	}
	private String getDescription()
	{
	   return "Fusiona los elementos de un grafo origen en el de destino. La red origen se elimina de memoria.";
	}
	@Override
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception
	{
	   // Copia elementos y elimina red
	    PanelToSelectMemoryNetworks sourceNetField = (PanelToSelectMemoryNetworks)dialog.getComponent(SOURCE_NETWORK_FIELDNAME);
	    PanelToSelectMemoryNetworks targetNetField = (PanelToSelectMemoryNetworks)dialog.getComponent(TARGET_NETWORK_FIELDNAME);
	    NetworkManager netMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
	    Network sourceNet = sourceNetField.getSelectedNetwork();
	    Network targetNet = targetNetField.getSelectedNetwork();
	    Graph sourceGraph = sourceNet.getGraph();
	    DynamicGraph targetGraph = (DynamicGraph) targetNet.getGraph();
	    if (dialog.getBoolean(RENUMBERING_YESNO_FIELDNAME))
		{
		    monitor.report("Renumerando elementos");
		    SequenceUIDGenerator uid = NetworkModuleUtil.renumberElements(targetGraph,sourceGraph);
		}
	    monitor.report("AÃ±adiendo elementos a la red destino");
	    targetGraph.getMemoryManager().appendGraph(sourceGraph);
	    monitor.report("Eliminando red origen.");
	    netMgr.detachNetwork(sourceNet.getName());
	}
	
	protected void processActions()
	{
	    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
	    final PlugInContext runContext = this.context;
	    progressDialog.setTitle("TaskMonitorDialog.Wait");
	    progressDialog.report("");
	    progressDialog.addComponentListener(new ComponentAdapter()
	    {
	    	public void componentShown(ComponentEvent e)
	    	{   
	    		new Thread(new Runnable()
	    		{
	    			public void run()
	    			{
	    				try
	    				{
	    				    NetworkFusionV2Plugin.this.run(progressDialog, runContext);
	    				} catch (Exception e)
					    {
						// TODO Auto-generated catch block
						e.printStackTrace();
					    } 							
	    				finally
	    				{
	    					progressDialog.setVisible(false);
	    				}
	    			}
	    		}).start();
	    	}
	    });
	    GUIUtil.centreOnWindow(progressDialog);
	    progressDialog.setVisible(true);
	}	
}
