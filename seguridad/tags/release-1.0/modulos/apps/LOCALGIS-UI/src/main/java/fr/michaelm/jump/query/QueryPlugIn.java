package fr.michaelm.jump.query;

import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
//import com.vividsolutions.jump.workbench.ui.MenuNames;

import fr.michaelm.jump.query.QueryDialog;
import fr.michaelm.jump.query.I18NPlug;

/**
 * QueryPlugIn
 * @author Michaël MICHAUD
 * @version 0.1.0 (4 Dec 2004)
 */ 
public class QueryPlugIn extends AbstractPlugIn {
    static QueryDialog queryDialog;
	public static String pluginname = "query";
	
    public void initialize(PlugInContext context) {
		I18NPlug.setPlugInRessource(pluginname, "language.query");
		
		String pluginCategory = AppContext.getApplicationContext().getString("GeopistaSearchByAttributes.category");
		context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(pluginCategory)
				.addPlugIn(this.getIcon(), this,
						createEnableCheck(context.getWorkbenchContext()),
						context.getWorkbenchContext());
//		if (I18NPlug.jumpi18n == true) {
//		    context.getFeatureInstaller().addMainMenuItem(this,
//		        new String[]
//	            {MenuNames.TOOLS, 
//		 	    I18NPlug.get(pluginname, "jump.query.menu")},
//		        I18NPlug.get(pluginname, "jump.query.menuitem"), false, null, null);
//		}
//		else 
		{
			context.getFeatureInstaller().addMainMenuItem(this,
				new String[] 
				{"Tools", AppContext.getApplicationContext().getI18nString("SimpleQuery")},
				AppContext.getApplicationContext().getI18nString("SimpleQuery"),
			    false, null, QueryPlugIn.createEnableCheck(context.getWorkbenchContext()));
		}    	   
    }
                                                      
    public boolean execute(PlugInContext context) throws Exception {
//        if (queryDialog==null) {
            queryDialog = new QueryDialog(context);
//        }
//        queryDialog.initUI();
        return false;
    }
    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck()).add(
                checkFactory.createAtLeastNLayersMustExistCheck(1));

    }
    
    public ImageIcon getIcon() {
        return IconLoader.icon("Attribute.gif");
  }
    
}
