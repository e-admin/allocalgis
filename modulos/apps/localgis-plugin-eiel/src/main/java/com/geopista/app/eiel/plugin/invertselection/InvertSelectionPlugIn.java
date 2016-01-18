/**
 * InvertSelectionPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.plugin.invertselection;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class InvertSelectionPlugIn extends ThreadedBasePlugIn{
	
	private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();		
	
	public InvertSelectionPlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.app.eiel.plugin.invertselection.languages.InvertSelectioni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("InvertSelection",bundle2);
    }
	
	public String getName(){
    	return "Invert Selection";  
    }
	
	public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {		
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()            
            .add(new EnableCheck() {
            	public String check(JComponent component) {            		
            		Layerable[]  layerables=((Layerable[]) (workbenchContext.getLayerNamePanel()).getLayerManager().getLayerables(Layerable.class).toArray(new Layerable[] {  }));
            		for (int h=0;h<layerables.length;h++){            			
            				if (layerables[h].isVisible() == false || layerables[h].isVisible() == true) {
            					return null;
                			}
            			}            		      		
            		return "El número de capas seleccionadas es incorrecto.";
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
	public void seleccionar (Layerable[] selectedLayers){
		 for (int i = 0; i < selectedLayers.length; i++) {
			 	Layerable layerables=(Layerable) selectedLayers[i];
			 	if (layerables.isVisible() == true) {
					layerables.setVisible(!layerables.isVisible());
				} else if ((layerables.isVisible() == false)) {
					layerables.setVisible(!layerables.isVisible());
				}
	        }
	}
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {
		seleccionar((Layerable[]) (context.getLayerNamePanel()).getLayerManager().getLayerables(Layerable.class).toArray(new Layerable[] {  }));
    }
}
