/**
 * LeerRedDeLocalPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.leerreddelocal;


import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.uva.geotools.graph.structure.Graph;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.routeenginetools.leerreddelocal.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilToolsDraw;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToLoadFromDataLocalStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class LeerRedDeLocalPlugIn extends ReadNetworkAbstractPlugin
{
	Graph graph;
	

	 public String getName(){
	    	return "LeerRedDeLocal";
	    }
	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("leerredlocal","routeengine.leerred.iconfile"));
	}
	
	private boolean isStreetNetwork(String networkName) {
		boolean resultado = false;


		return resultado;
	}

	private int getIdNetwork(String networkName) {

		return 0;
	}

	
	protected PanelToLoadFromDataLocalStore createPanel(PlugInContext context)
	{
		return new PanelToLoadFromDataLocalStore(context.getWorkbenchContext());
	}
	protected Layer createLayer(DynamicGraph graph, String subredName, PlugInContext context) throws Exception
	{
		if (graph != null){
		    return NetworkModuleUtilToolsDraw.createGraphLayer(graph, context, subredName, "Red de fichero.");
		} else{
			JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(),
			"Error al leer Red del Fichero. No se ha encontrado ninguna Red en el sistema");
			return null;
		}
	
	}
	public void initialize(PlugInContext context) throws Exception
	{
	
		Locale loc=I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.leerreddelocal.language.RouteEngine_LeerRedLocali18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("leerredlocal",bundle);
	
		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(
				this.aplicacion.getI18nString("ui.MenuNames.TOOLS.ROUTEENGINEADMINISTRATION")).addPlugIn(
						this.getIcon(),
						this,
						createEnableCheck(context.getWorkbenchContext()),
						context.getWorkbenchContext());
	
	}
	@Override
	 protected void linkGraphToStoreAndCreateLayer(String networkName, TaskMonitor monitor, PlugInContext context) throws Exception
	{
	monitor.allowCancellationRequests();
	monitor.report("Cargando grafo de fichero local");
	
	Network net=linkGraphToStore(true,false,networkName, monitor, context);
	/*
	 * Ahora aqui representaremos del grafo sus arcos
	 */
	if (net!=null)
	    {
		monitor.report(I18N.get("jump.workbench.ui.plugin.datastore.AddDatastoreLayerPlugIn.Creating-layer"));
		createLayer((DynamicGraph) net.getGraph(),  networkName, context);
		context.getWorkbenchGuiComponent().warnUser("Se ha cargado desde el disco la red :"+net.getName());
	    }
	else
	    {
		context.getWorkbenchGuiComponent().warnUser("No se han cargado redes.");
	    }
	}
	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
	    {
	    	EnableCheckFactory checkFactory = new EnableCheckFactory(
	    			workbenchContext);
	    	return new MultiEnableCheck()
	    	.add(
	    			checkFactory
	    			.createWindowWithLayerManagerMustBeActiveCheck())
	    			.add(
	    					checkFactory
	    					.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
	    					.add(checkFactory.createTaskWindowMustBeActiveCheck());
	    }
}