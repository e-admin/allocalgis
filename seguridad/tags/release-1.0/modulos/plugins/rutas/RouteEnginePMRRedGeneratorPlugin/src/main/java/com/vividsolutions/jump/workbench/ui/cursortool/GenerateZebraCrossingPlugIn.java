package com.vividsolutions.jump.workbench.ui.cursortool;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;

import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;


public class GenerateZebraCrossingPlugIn extends AbstractPlugIn{ 
	
	NetworkManager networkMgr;
	
	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.genredplugin.language.RouteEngine_GenRedi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("genred",bundle);
		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}
    
    public Icon getIcon() {
        return IconLoader.icon("cebra.gif");
    }
    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);        
        return new MultiEnableCheck()
                        .add(checkFactory.createAtLeastNLayersMustExistCheck(1));        
    }
        

	public void addButton(final ToolboxDialog toolbox)
	{
/*		GenerateZebraCrossingPlugIn explode = new GenerateZebraCrossingPlugIn();                 
		toolbox.addPlugIn(explode, null, explode.getIcon());
		toolbox.finishAddingComponents();
		toolbox.validate();*/
	}




	public boolean execute(PlugInContext ctxt) throws Exception {
        List<Layer> layersList = ctxt.getLayerManager().getLayers();
        int n = layersList.size();
        int i = 0;
        String sSubRedName = "";
        for(i=0;i<n;i++){
        	if (layersList.get(i).getName().startsWith("Nodos-")){
        		String[] trozos = layersList.get(i).getName().split("-");
        		if (trozos.length == 2)
        			sSubRedName = trozos[1];
        		break;
        	}
        }
		this.networkMgr = FuncionesAuxiliares.getNetworkManager(ctxt);
		Network subRed = null;
		String sRed = "";
		Iterator redesIterator = networkMgr.getNetworks().keySet().iterator();
		while (redesIterator.hasNext()){
			sRed = (String)redesIterator.next();
			Iterator subredesIterator = networkMgr.getNetwork(sRed).getSubnetworks().keySet().iterator();
			while (subredesIterator.hasNext()){
				String sSubRed = (String)subredesIterator.next();
				if (sSubRed.equals(sSubRedName))
					subRed = networkMgr.getNetwork(sRed).getSubNetwork(sSubRedName);
			}
		}
		if (subRed == null)
			subRed = networkMgr.getNetwork(sRed);
		ToolboxDialog toolbox = new ToolboxDialog(ctxt.getWorkbenchContext());
		EnableCheckFactory checkFactory = new EnableCheckFactory(toolbox.getContext());
		ctxt.getLayerViewPanel().setCurrentCursorTool(new GenerateZebraCrossingTool(subRed));

		return false;

	}
}
