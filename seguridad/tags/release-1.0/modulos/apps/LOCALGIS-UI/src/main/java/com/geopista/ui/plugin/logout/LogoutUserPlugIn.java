package com.geopista.ui.plugin.logout;

import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.geopista.app.AppContext;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class LogoutUserPlugIn extends AbstractPlugIn{

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	
    public String getName() {
        return aplicacion.getI18nString("LogoutUser");
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        
        try {
			SecurityManager.logout();
			SSOAuthManager.clearRegistrySesion();
			final JFrame desktop = (JFrame) context.getWorkbenchFrame();

		}  catch (Exception e) {
			//e.printStackTrace();
			SSOAuthManager.clearRegistrySesion();			
		}
        return true;
    }

        
    
    public void initialize(PlugInContext context) throws Exception
    {
    	
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(this, aplicacion.getI18nString("File"),
            GeopistaUtil.i18n_getname(this.getName()) + "...", null, null);
    }
    /*public ImageIcon getIcon() {
        return IconLoader.icon("Nuevo_mapa.GIF");
    }*/
}
