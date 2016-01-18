/**
 * ReadNetworkAbstractPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.leerreddelocal;

import java.awt.Component;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.Icon;

import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeutil.DialogForDataStorePlugin;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil.Operations;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilToolsCore;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToLoadFromDataStore;
import com.geopista.ui.plugin.routeenginetools.routeutil.VentanaError;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelDialog;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public abstract class ReadNetworkAbstractPlugin extends DialogForDataStorePlugin
{

    protected Layer capadebase;
    protected boolean saveRedToStoreButtonAdded;
    protected boolean bPMRGraph = false;
    protected static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private OKCancelDialog dialog;

   
    public boolean execute(final PlugInContext context)
    {
    
    	OKCancelDialog dlg = getDialog(context);
    	dlg.setVisible(true);
    
    	return dlg.wasOKPressed();
    }

    protected double RedondearVelocidad(double speed)
    {
    	try{
    		return Math.round(speed*Math.pow(10,2))/Math.pow(10,2);
    	}catch (Exception e) {
    		return speed;
    	}
    }

    public abstract Icon getIcon();
   

    public ReadNetworkAbstractPlugin() {
	super();
    }

    protected Network linkGraphToStore(boolean toFile,boolean overwrite,String networkName, TaskMonitor monitor, PlugInContext context) throws IOException
    {   
    
    	NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
    
    	Network selectedNetwork = networkMgr.getNetwork(networkName);
    	if (selectedNetwork != null)
    	    {
    		VentanaError msg = new VentanaError(context);
    		msg.addText("Una red con ese nombre existe en memoria. Â¿Sobreescribir?");
    		msg.setVisible();
    		if (!msg.OK())
    		    {
    			context.getWorkbenchGuiComponent().warnUser("Cancelado por el usuario");
    			return null;
    		    }
    
    		networkMgr.detachNetwork(networkName);
    	    }
    // TODO Usar propiedad para marcar la red como PMR COMPROBAR
    //	bPMRGraph = false;
    //		
    //	if (netWorkName.endsWith("PMR"))
    //			bPMRGraph = true;	
    	HashMap<String, Object> netProperties = new HashMap<String, Object>();
    	DynamicGraph newGraph = NetworkModuleUtil.getNewInMemoryGraph(null);
    	Network newNet = NetworkModuleUtil.addNewNetwork(networkMgr, networkName, newGraph, netProperties);
    	NetworkModuleUtilToolsCore.linkNetworkToStore(networkName, newNet, Operations.LOAD, toFile, monitor); // cargará automáticamente el contenido
    
    	Collection<Edge> edges = newGraph.getEdges(); // forzar carga del modelo.
    
    	// emparejar con features locales
            pairGraphWithFeatures(newGraph, context, monitor);
    	// JPC: FIX usa estructura reader-writer estándar
    	// if (!graph.getEdges().isEmpty())
    	// {
    	// // FIXED graph.getEdges().toArray()[0] crea un array innecesario
    	// Edge firstEdge = graph.getEdges().iterator().next();
    	// // TODO esta heurística es arriesgada y poco flxible mejor comprobar GraphBuilders y Generators
    	// if (firstEdge instanceof ILocalGISEdge)
    	// {
    	// graphGenerator = NetworkModuleUtil.getBasicLineGraphGenerator();
    	// } else if (firstEdge instanceof ILocalGISEdge)
    	// {
    	// graphGenerator = NetworkModuleUtil.getLocalGISStreetGraphGenerator();
    	// }
    	// }
    
    //	db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);
    
    	return newNet;
    
        }

    public void pairGraphWithFeatures(DynamicGraph newGraph, PlugInContext context, TaskMonitor monitor)
    {
        Collection<Edge> edges=newGraph.getEdges();
        int size = edges.size();
        int i=1;
        for (Edge edge : edges)
            {		
        	if (monitor!=null)
        	    monitor.report(i, size, "Buscando en el escritorio geometría detallada para arco "+i+"de "+size);
        	NetworkModuleUtilWorkbench.findFeatureForEdge(edge, context);
        	i++;
            }
    }

    protected abstract Layer createLayer(DynamicGraph graph, String subredSelected, PlugInContext context) throws Exception;

    public void addButton(final ToolboxDialog toolbox)
    {
    	if (!saveRedToStoreButtonAdded)
    	{
    		toolbox.addToolBar();
    		//LeerRedDeLocalPlugIn explode = new LeerRedDeLocalPlugIn();
    		toolbox.addPlugIn(this, null, this.getIcon());
    		toolbox.finishAddingComponents();
    		toolbox.validate();
    		saveRedToStoreButtonAdded = true;
    	}
    }

    private OKCancelDialog getDialog(PlugInContext context)
    {
    	dialog = createDialog(context);
    	return dialog;
    }

    @Override
    public void run(TaskMonitor monitor, PlugInContext context)
    {
    	// TODO Auto-generated method stub
    	try {
    	    String networkName=((PanelToLoadFromDataStore) dialog.getCustomComponent()).getSubredSelected().trim();
    	    linkGraphToStoreAndCreateLayer(networkName, monitor, context);
    
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	} catch (Throwable e) {
    		throw new RuntimeException(e);
    	}
    }

    private OKCancelDialog createDialog(PlugInContext context)
    {
    
    	WithOutConnectionPanel conPannel = createPanel(context);
    	OKCancelDialog dialog = new OKCancelDialog(context.getWorkbenchFrame(),
    			getName(),
    			true,
    			conPannel,
    			new OKCancelDialog.Validator() {
    		public String validateInput(Component component) {
    			return ((WithOutConnectionPanel) component).validateInput();
    		}
    	});
    	dialog.pack();
    	GUIUtil.centreOnWindow(dialog);
    
    	return dialog;
    }
   
}