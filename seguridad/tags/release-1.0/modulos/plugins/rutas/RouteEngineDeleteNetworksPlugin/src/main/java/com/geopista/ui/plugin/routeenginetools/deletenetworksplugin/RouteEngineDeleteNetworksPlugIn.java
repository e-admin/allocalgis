/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.deletenetworksplugin;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.deletenetworksplugin.dialog.DeleteNetworksDialog;
import com.geopista.ui.plugin.routeenginetools.deletenetworksplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * @author javieraragon
 *
 */
public class RouteEngineDeleteNetworksPlugIn extends AbstractPlugIn{
	
	private PlugInContext context = null;
	private boolean deleteNetwroksButtonAdded = false ;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	DeleteNetworksDialog dialog = null;
	
	public boolean execute(PlugInContext context) throws Exception {
		this.context = context;
		
		this.dialog = new DeleteNetworksDialog(this.context, 
				I18N.get("delnetworks","routeengine.deletenetworks.dialogtitle"));
		
		if (dialog.wasOKPressed()){
			return false;
		}
		
		return false;
	}

//	@Override
//	public void run(TaskMonitor monitor, PlugInContext context)
//			throws Exception {
//		// TODO Auto-generated method stub
//	}
	
	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("delnetworks","routeengine.deletenetworks.iconfile"));
	}
	
	public void initialize(PlugInContext context) throws Exception {
		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.deletenetworksplugin.language.RouteEngine_DeleteNetworksi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("delnetworks",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
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
		.add(checkFactory.createExactlyNFeaturesMustBeSelectedCheck(1));
	}
	
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!deleteNetwroksButtonAdded )
		{
//			toolbox.addToolBar();
			RouteEngineDeleteNetworksPlugIn explode = new RouteEngineDeleteNetworksPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			deleteNetwroksButtonAdded = true;
		}
	}

}
