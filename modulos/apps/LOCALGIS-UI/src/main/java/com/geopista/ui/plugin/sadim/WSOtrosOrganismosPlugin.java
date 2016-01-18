/**
 * WSOtrosOrganismosPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.sadim;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class WSOtrosOrganismosPlugin extends AbstractPlugIn {

	private static String ACL_EDITOR_GIS="EditorGIS";
	private static String PERMISO_CONEXION_WS_OTROSORGANISMOS="localgis.conexion.ws.otros_organismos";
	
	private ApplicationContext appContext = AppContext.getApplicationContext();
	private String toolBarCategory = "toolBarGeopistaPlugIn";
	private Properties props = new Properties();
	
	private static boolean verificarPermisos=true;

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {

		return new MultiEnableCheck().add(isAdminUserCheck());

	}

	public static EnableCheck isAdminUserCheck() {
		return new EnableCheck() {
			public String check(JComponent component) {
				try {

					ApplicationContext appContext = AppContext
							.getApplicationContext();
					if (SecurityManager.isCanceled()){
						return (appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.description"));
					}
					
				
					if (!verificarPermisos) 
						return null;
						
					GeopistaPermission adminPermission = new com.geopista.security.GeopistaPermission(
							com.geopista.security.GeopistaPermission.VER_ADMINITRACION);
					if (appContext.checkPermission(adminPermission,
							"Administracion") == false) {		
						verificarPermisos=false;
						return (appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.description"));
					}

					if (!tieneUsuarioPermisos()){
						verificarPermisos=false;
						return (appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.description"));
					}
					
					verificarPermisos=false;

				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}
		};
	}
 	

	public String getName() {
		return appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.description");
	}
	
	
	private static boolean tieneUsuarioPermisos() throws Exception
    {
		GeopistaPrincipal principal=com.geopista.security.SecurityManager.getPrincipal();
		GeopistaAcl acl=com.geopista.security.SecurityManager.getPerfil(ACL_EDITOR_GIS);

		Enumeration<GeopistaPermission> permisos=acl.getPermissions(principal);

		if (permisos==null) return(false);

		Hashtable permisoshash= new Hashtable<String, String>();
				
        while (permisos.hasMoreElements())
        {
        	GeopistaPermission geopistaPermission = (GeopistaPermission) permisos.nextElement();
        	String permissionName = geopistaPermission.getName();
            if (!permisoshash.containsKey(permissionName))
            {
            	permisoshash.put(permissionName, "");
            }            
        }
        
        if (permisoshash.containsKey(PERMISO_CONEXION_WS_OTROSORGANISMOS))
            return true;
        
        
        return false;
    }
	

	public boolean execute(PlugInContext context) throws Exception {
			
		if (!tieneUsuarioPermisos())
		{
			JOptionPane.showMessageDialog(null, appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.description"), appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.error"), JOptionPane.OK_OPTION);
			return true;
		}
		
	    JDialog d = new WSOtrosOrganismosDialog(context, appContext.getMainFrame(), props);
		
		return true;
	}

	public void initialize(PlugInContext context) throws Exception {

		String pluginCategory = appContext.getString(toolBarCategory);
		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench()
				.getGuiComponent()).getToolBar(pluginCategory).addPlugIn(
				getIcon(), this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());
		
		/*props.load(ClassLoader
				.getSystemResourceAsStream(this.getClass().getName().replaceAll("\\.", "/")+".properties"));*/
		
		props.load(this.getClass().getResourceAsStream("/com/geopista/ui/plugin/sadim/WSOtrosOrganismosPlugin.properties"));


		ACL_EDITOR_GIS = props.get("acl_editor_gis").toString();
		PERMISO_CONEXION_WS_OTROSORGANISMOS = props.get(
				"permiso_conexion_ws_otrosorganismos").toString();
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("wsotrosorganismos.gif");
	}
}