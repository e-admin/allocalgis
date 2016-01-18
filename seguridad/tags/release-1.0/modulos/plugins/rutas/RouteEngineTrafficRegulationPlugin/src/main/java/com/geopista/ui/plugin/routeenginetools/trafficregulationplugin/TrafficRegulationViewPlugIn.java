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
				context.getLayerViewPanel().getViewport(),
				(Graphics2D) context.getLayerViewPanel().getGraphics());
		if (getSelectedLayer().getStyle(RouteArrowLineStyle.class) != null){
			getSelectedLayer().removeStyle(getSelectedLayer().getStyle(RouteArrowLineStyle.class));
		}
		getSelectedLayer().addStyle(line);



		ArrayList<GeopistaFeature> listaFeatures = this.getSelectedsArrayListStreetFeature();
		
		for (int i = 0; i < listaFeatures.size(); i++ ){
			
			monitor.report("Modificando visulaización tramo " + i + " de " + listaFeatures.size());
			
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
