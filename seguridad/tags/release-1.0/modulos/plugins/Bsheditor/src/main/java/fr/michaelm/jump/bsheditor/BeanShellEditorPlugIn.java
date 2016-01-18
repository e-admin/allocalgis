package fr.michaelm.jump.bsheditor;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.ImageIcon;

import fr.michaelm.bsheditor.BeanShellEditor;
import fr.michaelm.bsheditor.images.IconLoader;

public class BeanShellEditorPlugIn extends AbstractPlugIn {
    
    public void initialize(PlugInContext context) {
        context.getFeatureInstaller().addMainMenuItem(this,
                                                      new String[] { "Scripting" },
                                                      getName(),
                                                      false,
                                                      null,
                                                      null);
        
        ApplicationContext appContext=AppContext.getApplicationContext();
		
		context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(appContext.getString("CalculateFeature.category")).addPlugIn(
				getIcon(), this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());
    
    }
                                                      
    public boolean execute(PlugInContext context) throws Exception {
        Map map = new HashMap();
        map.put("wc", context.getWorkbenchContext());
        BeanShellEditor e = new BeanShellEditor(map, null);
        return true;
    }
    
	public ImageIcon getIcon() {
		return IconLoader.icon("Script16.gif");
	}
	
	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

//		return new MultiEnableCheck()
//		.add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
//		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
//		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
//		.add(checkFactory.createAtLeastNLayersMustExistCheck(2));

		return new MultiEnableCheck();
	}

}
