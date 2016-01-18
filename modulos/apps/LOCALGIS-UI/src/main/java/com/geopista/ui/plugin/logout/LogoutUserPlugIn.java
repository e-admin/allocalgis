/**
 * LogoutUserPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.logout;

import javax.swing.JFrame;

import com.geopista.app.AppContext;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
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
