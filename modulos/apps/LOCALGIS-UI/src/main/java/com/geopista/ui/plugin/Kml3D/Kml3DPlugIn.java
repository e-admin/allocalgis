/**
 * Kml3DPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.Kml3D.dialogs.EstilosKml3DDialogo;
import com.geopista.ui.plugin.Kml3D.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/**
 * Clase que implementa el Plugin para la configuracion de los estilos para la representacion
 * de las capas en 3D (WorldWind)
 * 
 * @author David Vicente
 *
 */
public class Kml3DPlugIn extends AbstractPlugIn
{
	public static String name = "Kml3DPlugIn";
	
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private String toolBarCategory = "Kml3DPlugIn.category";
	private EstilosKml3DDialogo dialogo = null;
	
    
	
	/**
	 * Inicializa el plugin
	 */
    public void initialize(PlugInContext context) throws Exception
    {
        String pluginCategory = aplicacion.getString(toolBarCategory);
	    Locale loc=Locale.getDefault();
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.Kml3D.languages.Kml3DPlugIni18n",loc,this.getClass().getClassLoader());    	
		
	    I18N.plugInsResourceBundle.put(this.getName(),bundle2);

	     //Agregamos la herramienta a la barra de iconos
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
    			this,
    			null,
//    			this.createEnableCheck(context.getWorkbenchContext()),
    			context.getWorkbenchContext());
        
        
		 FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	      
	     JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
	                                                        .getGuiComponent()
	                                                        .getLayerNamePopupMenu();
	     
	     //Agregamos la herramienta al menú contextual.
	     featureInstaller.addPopupMenuItem(layerNamePopupMenu,
	            this, aplicacion.getI18nString(this.getName()) + "...", false,
	            GUIUtil.toSmallIcon(this.getIcon()),null);
//	            this.createEnableCheck(context.getWorkbenchContext()));
    }
    
    
    
    /**
     * Metodo que se lanza cuando se pulsa el boton
     */
    public boolean execute(final PlugInContext context) throws Exception
    {
    	try
    	{
//    		if(this.dialogo == null)
    		{
    			this.dialogo = new EstilosKml3DDialogo("CapaPrueba 1");
    		}
    		
    		this.dialogo.setVisible(true);
//	        reportNothingToUndoYet(context);	        	        
    	}
    	catch(Exception e)
    	{
            String msg = I18N.get("ChangeStyles3DPlugIn", "Error");
          	JOptionPane.showMessageDialog(null, msg);
    	}
    	
        return true;
    }
    

    
    
	public ImageIcon getIcon() {
		return IconLoader.icon("kml.png");
	}
	
	public String getName(){
		return name;
	}
	
	public MultiEnableCheck createEnableCheck(
			final WorkbenchContext workbenchContext) {
			EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

			return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
										 .add(checkFactory.createExactlyOneLayerMustBeSelectedCheck());
		}
	
}

