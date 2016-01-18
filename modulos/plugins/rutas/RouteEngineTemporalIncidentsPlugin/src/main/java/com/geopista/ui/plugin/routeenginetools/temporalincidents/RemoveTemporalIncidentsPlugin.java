/**
 * RemoveTemporalIncidentsPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.temporalincidents;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.temporalincidents.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.util.IdEdgeNetworkBean;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class RemoveTemporalIncidentsPlugin extends ThreadedBasePlugIn{
	
	private static AppContext app = (AppContext) AppContext.getApplicationContext();
	private boolean buttonAdded;
	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.temporalincidents.language.RouteEngine_TemporalIncidentsi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("temporalIncidents",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}
	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("temporalIncidents","routeengine.temporalincidents.iconfile.remove"));
	}
	
	public String getName(){
    	String name = I18N.get("temporalIncidents","routeengine.temporalincidents.plugintitle.remove");
    	return name;
    }

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		MiEnableCheckFactory checkFactory = new MiEnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		.add(checkFactory.createBlackBoardMustBeElementsCheck());
	}
	
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		
		/*if(app.getBlackboard().get("temporalincidents") != null)
			app.getBlackboard().put("temporalincidents",new ArrayList<IdEdgeNetworkBean>());
		
		JOptionPane.showMessageDialog(app.getMainFrame(),I18N.get("temporalIncidents","routeengine.temporalincidents.remove.removed"));*/
		
	}
	public boolean execute(PlugInContext context)throws Exception {
		if(context.getLayerViewPanel() == null){
			return false;
		}
		try{
			if(app.getBlackboard().get("temporalincidents") != null)
				app.getBlackboard().put("temporalincidents",new ArrayList<IdEdgeNetworkBean>());
		
		//JOptionPane.showMessageDialog(app.getMainFrame(),I18N.get("temporalIncidents","routeengine.temporalincidents.remove.removed"));
		}catch(Exception e){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("temporalIncidents","routeengine.temporalincidents.remove.error"));
			return false;
		}
		JOptionPane.showMessageDialog(context.getWorkbenchFrame(), I18N.get("temporalIncidents","routeengine.temporalincidents.remove.removed"));
		context.getLayerViewPanel().getContext().setStatusMessage(I18N.get("temporalIncidents","routeengine.temporalincidents.remove.removed"));
		return true;
	}
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!buttonAdded )
		{
//			toolbox.addToolBar();
			RemoveTemporalIncidentsPlugin explode = new RemoveTemporalIncidentsPlugin();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			buttonAdded = true;
		}
	}
}
