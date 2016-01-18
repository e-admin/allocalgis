/**
 * TrafficRegulationViewPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.trafficregulationplugin;

import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.ui.plugin.routeenginetools.routeutil.routelabelstyle.RouteArrowLineStyle;
import com.geopista.ui.plugin.routeenginetools.trafficregulationplugin.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.Viewport;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class TrafficRegulationViewPlugIn extends TrafficRegulationEditPlugIn{

	private boolean trafficViewModifierButtonAdded = false;
	private static AppContext aplication = (AppContext) AppContext.getApplicationContext();
	private PlugInContext context = null;

	public boolean execute(PlugInContext context) throws Exception {
		this.context = context;
		return super.execute(context);
	}

	@Override
	public void initialize(PlugInContext context) throws Exception {
		super.initialize(context);
		this.context = context;
 
		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}

	@Override
	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("regulacionTrafico","routeengine.trafficregulation.viewiconfile"));
	}

	@Override
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!trafficViewModifierButtonAdded  )
		{
			//			toolbox.addToolBar();
			TrafficRegulationViewPlugIn explode = new TrafficRegulationViewPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			trafficViewModifierButtonAdded = true;
		}
	}

	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {
		// TODO Auto-generated method stub
		this.setMonitor(monitor);
		this.getMonitor().report(I18N.get("regulacionTrafico","routeengine.trafficregulation.savingdatainfomessage"));


		String netName="";
		if(getSelectedLayer().getName().startsWith("Arcos-")){
			netName = getSelectedLayer().getName().split("-")[1];
		} else{
			netName = getSelectedLayer().getName();
		}
		System.out.println(netName);

		RouteArrowLineStyle line = new RouteArrowLineStyle.BiDirect(
				(Viewport)context.getLayerViewPanel().getViewport(),
				(Graphics2D) ((LayerViewPanel)context.getLayerViewPanel()).getGraphics());
		if (getSelectedLayer().getStyle(RouteArrowLineStyle.class) != null){
			getSelectedLayer().removeStyle(getSelectedLayer().getStyle(RouteArrowLineStyle.class));
		}
		getSelectedLayer().addStyle(line);



		ArrayList<GeopistaFeature> listaFeatures = this.getSelectedsArrayListStreetFeature();
		
		for (int i = 0; i < listaFeatures.size(); i++ ){
			
			monitor.report("Modificando visulaizacion tramo " + i + " de " + listaFeatures.size());
			
			GeopistaFeature feat = listaFeatures.get(i);
			if (feat!= null){
				if (feat.getSchema().hasAttribute("pintadaRegulacionTrafico") && 
						feat.getAttribute("pintadaRegulacionTrafico") != null &&
						feat.getAttribute("pintadaRegulacionTrafico") instanceof Integer){
					Integer painted = (Integer) feat.getAttribute("pintadaRegulacionTrafico");
					if (painted!=null && painted == 0){
						painted = 1;
					}else{
						painted = 0;
					}
					try{
						feat.setAttribute("pintadaRegulacionTrafico", painted);
					}catch (Exception e) {
					}
				}
			}
		}
		
		context.getLayerViewPanel().repaint();
	}

}
