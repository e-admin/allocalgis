package com.geopista.app.eiel.plugin.unmarkfamilylayers;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.model.LayerFamily;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class UnmarkFamilyLayersPlugIn extends ThreadedBasePlugIn{
	
	private static String sUrlPrefix="http://localhost:8081/geopista/";
	private static AdministradorCartografiaClient acClient =new AdministradorCartografiaClient (sUrlPrefix+"AdministradorCartografiaServlet");
	private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();		
	
	public UnmarkFamilyLayersPlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.app.eiel.plugin.unmarkfamilylayers.languages.UnmarkFamilyLayersi18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("UnmarkFamilyLayers",bundle2);
    }
	
	public String getName(){    	
    	return "Unmark Family Layers";
    }
	
	public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {		
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()            
            .add(new EnableCheck() {
            	public String check(JComponent component) {
            		Collection layerFamilyCollection = workbenchContext.getLayerNamePanel().getSelectedCategories();

            		for (Iterator iterLayerFamily = layerFamilyCollection.iterator(); iterLayerFamily.hasNext();){

            			Object object = iterLayerFamily.next();

            			if (object instanceof LayerFamily){

            				LayerFamily layerFamily = (LayerFamily) object;
            				
            				for (int index = 0; index<layerFamily.getLayerables().size();index++){
            					Object obj = layerFamily.getLayerables().get(index);				

            					if (obj instanceof Layer){

            						Layer layer = (Layer) obj;
            						if (layer.isVisible())
            						return null;
            					}
            				}				
            			}

            		}
            		return "Selección correcta";
            	}
            });            
	}
	
	public void initialize(PlugInContext context) throws Exception {

		JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
	       .getGuiComponent()
	       .getLayerNamePopupMenu();
		   
	       FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	       featureInstaller.addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
	                .getGuiComponent().getCategoryPopupMenu(), this, GeopistaFunctionUtils.i18n_getname(this
	                .getName()), false, null, this.createEnableCheck(context.getWorkbenchContext()));
	   }
	
	public boolean execute(PlugInContext context) throws Exception {
		return true;
	}
	
//	public String traduccion (String nom){
//		String trad="";		
//		trad=acClient.traduccionLayers(nom);
//		return trad;
//	}
	
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception
	{
		Collection layerFamilyCollection = context.getLayerNamePanel().getSelectedCategories();

		for (Iterator iterLayerFamily = layerFamilyCollection.iterator(); iterLayerFamily.hasNext();){

			Object object = iterLayerFamily.next();

			if (object instanceof LayerFamily){

				LayerFamily layerFamily = (LayerFamily) object;
				
				for (int index = 0; index<layerFamily.getLayerables().size();index++){
					Object obj = layerFamily.getLayerables().get(index);				
				
					if (obj instanceof Layer){

						Layer layer = (Layer) obj;
						layer.setVisible(false);
					}
				}
			
			}
		}

	}
}

