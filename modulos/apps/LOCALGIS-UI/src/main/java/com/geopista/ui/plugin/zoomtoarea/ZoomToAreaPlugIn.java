/**
 * ZoomToAreaPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.zoomtoarea;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.zoomtoarea.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class ZoomToAreaPlugIn extends AbstractPlugIn{

	private boolean selectZoomToAreaAdded = false;
	
	public ZoomToAreaPlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.zoomtoarea.languages.ZoomToAreaPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("ZoomToAreaPlugIn",bundle2);
    }
	
	public Icon getIcon() {
        return IconLoader.icon("ZoomToArea.gif");
    }
    
    public String getName(){
    	return "ZoomToArea";
    }
        
    public void initialize(final PlugInContext context) throws Exception {    	

    	//Código necesario para incluir el PlugIn en la barra de herramientas
    	
    	context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar().addPlugIn(this.getIcon(),
            this,
            this.createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());   

    	////////////////////////////////////////////////////////////////
    	
//    	GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
//    	geopistaEditingPlugIn.addAditionalPlugIn(this);

    }  
    
    public void addButton(final ToolboxDialog toolbox)
    {
        if (!selectZoomToAreaAdded)
        {
            toolbox.addToolBar();            
            ZoomToAreaPlugIn zoomToArea = new ZoomToAreaPlugIn();                 
            toolbox.addPlugIn(zoomToArea, null, zoomToArea.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectZoomToAreaAdded = true;
        }
    } 
    
    public boolean execute(PlugInContext context) throws Exception {
    	
        ZoomToAreaDialog zoomToAreaPanel = new ZoomToAreaDialog(context);
    	
        if (context.getLayerViewPanel()!=null){
        	context.getLayerViewPanel().repaint();
        }
        
        return true;
    }
    

	public MultiEnableCheck createEnableCheck(
	        final WorkbenchContext workbenchContext) {
	        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

	        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
	                                     .add(checkFactory.createAtLeastNLayersMustExistCheck(
	                1));
	    }


}
