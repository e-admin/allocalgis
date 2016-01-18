package com.geopista.ui.plugin.sadim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.protocol.control.ISesion;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.ui.dialogs.GeopistaNumerosPoliciaDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.io.DriverProperties;
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
					GeopistaPermission adminPermission = new com.geopista.security.GeopistaPermission(
							com.geopista.security.GeopistaPermission.VER_ADMINITRACION);
					if (appContext.checkPermission(adminPermission,
							"Administracion") == false) {						
						return (appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.description"));
					}

					if (!tieneUsuarioPermisos())
						return (appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.description"));

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