/**
 * CapasExtendidasPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class CapasExtendidasPlugin extends AbstractPlugIn {
	
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    private Blackboard blackboard  = aplicacion.getBlackboard();
	
	public void initialize(PlugInContext context) throws Exception {
	      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	      featureInstaller.addMainMenuItem(this,
	            new String[] { aplicacion.getI18nString("Tools"), aplicacion.getI18nString("Datos Externos") },
	            aplicacion.getI18nString("ConfigureQueryExternalDataSource.capasExtendidas"), false, null,
	            null);
	}
	
	public boolean execute(PlugInContext context) throws Exception {

        if(!aplicacion.isLogged())
        {
             aplicacion.setProfile("Geopista");
             aplicacion.login();
        }

        if(aplicacion.isLogged())
        {
        	if (!isEmptyCapasExtendidas()) {
	        	CapasExtendidasDialog capasExtendidasDialog = new CapasExtendidasDialog(context.getWorkbenchFrame(),aplicacion.getI18nString("ConfigureQueryExternalDataSource.capasExtendidas.titulo"),true);
	        	GUIUtil.centreOnWindow(capasExtendidasDialog);
	        	capasExtendidasDialog.setVisible(true);
        	}
        	else {
        		JOptionPane.showMessageDialog(context.getWorkbenchFrame(), aplicacion.getI18nString("ConfigureQueryExternalDataSource.capasExtendidas.error.contenido"), aplicacion.getI18nString("ConfigureQueryExternalDataSource.capasExtendidas.error.titulo"), JOptionPane.INFORMATION_MESSAGE);
        	}
        }

		return true;
	}
	
	private boolean isEmptyCapasExtendidas() {
		return false;

	}
	
}
