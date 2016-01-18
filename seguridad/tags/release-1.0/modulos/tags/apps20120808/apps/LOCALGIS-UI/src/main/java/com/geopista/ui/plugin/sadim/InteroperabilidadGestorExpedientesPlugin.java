
package com.geopista.ui.plugin.sadim;


import java.awt.Desktop;
import java.net.URI;
import java.security.acl.AclNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;



public class InteroperabilidadGestorExpedientesPlugin extends AbstractPlugIn
{
	private static String ACL_EDITOR_GIS="EditorGIS";
	private static String PERMISO_CONEXION_GESTOR_EXPEDIENTES="localgis.conexion.gestion.expediente";
	private static String URL_CONEXION_GESTOR_EXPEDIENTES="http://pamod-pre.c.ovd.interhost.com:8080/integracion_expedientes/menu_expedientes.htm";

	private ApplicationContext appContext=AppContext.getApplicationContext();
	private String toolBarCategory="toolBarGeopistaPlugIn";



	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
	{

		return new MultiEnableCheck().add(isAdminUserCheck());
	}



	public static EnableCheck isAdminUserCheck()
	{
		return new EnableCheck()
		{
			public String check(JComponent component)
			{
				try
				{

					ApplicationContext appContext=AppContext.getApplicationContext();
					GeopistaPermission adminPermission=new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_ADMINITRACION);
					if (appContext.checkPermission(adminPermission,"Administracion")==false)
					{
						return (appContext.getI18nString("sadim.InteroperabilidadGestorExpedientesPlugin.sinPermisos"));
					}

					if (!tieneUsuarioPermisos()) return (appContext.getI18nString("sadim.InteroperabilidadGestorExpedientesPlugin.sinPermisos"));

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				return null;
			}
		};
	}



	public String getName()
	{
		return appContext.getI18nString("sadim.InteroperabilidadGestorExpedientesPlugin.description");
	}



	private static boolean tieneUsuarioPermisos() throws Exception
	{
		GeopistaPrincipal principal=com.geopista.security.SecurityManager.getPrincipal();
		GeopistaAcl acl=com.geopista.security.SecurityManager.getPerfil(ACL_EDITOR_GIS);

		try
		{
			Enumeration<GeopistaPermission> permisos=acl.getPermissions(principal);
			
			if (permisos==null) return(false);
			
			Hashtable permisoshash=new Hashtable<String, String>();
			while (permisos.hasMoreElements())
			{
				GeopistaPermission geopistaPermission=(GeopistaPermission) permisos.nextElement();
				String permissionName=geopistaPermission.getName();
				if (!permisoshash.containsKey(permissionName))
				{
					permisoshash.put(permissionName,"");
				}
			}

			if (permisoshash.containsKey(PERMISO_CONEXION_GESTOR_EXPEDIENTES)) return true;

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return false;
	}



	public boolean execute(PlugInContext context) throws Exception
	{

		if (!tieneUsuarioPermisos())
		{
			JOptionPane.showMessageDialog(null,appContext.getI18nString("sadim.InteroperabilidadGestorExpedientesPlugin.sinPermisos"),appContext.getI18nString("sadim.InteroperabilidadGestorExpedientesPlugin.error"),JOptionPane.OK_OPTION);
			return true;
		}

		if (Desktop.isDesktopSupported())
		{
			Desktop desktop=Desktop.getDesktop();
			desktop.browse(new URI(URL_CONEXION_GESTOR_EXPEDIENTES));
		}
		else
			JOptionPane.showMessageDialog(null,appContext.getI18nString("sadim.InteroperabilidadGestorExpedientesPlugin.noNavegador")+" '"+URL_CONEXION_GESTOR_EXPEDIENTES+"'",appContext.getI18nString("sadim.InteroperabilidadGestorExpedientesPlugin.error"),JOptionPane.OK_OPTION);

		return true;
	}



	public void initialize(PlugInContext context) throws Exception
	{

		String pluginCategory=appContext.getString(toolBarCategory);
		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(getIcon(),this,createEnableCheck(context.getWorkbenchContext()),context.getWorkbenchContext());

		Properties props=new Properties();
		/*props.load(ClassLoader.getSystemResourceAsStream(this.getClass().getName().replaceAll("\\.","/")+".properties"));*/

		props.load(this.getClass().getResourceAsStream("/com/geopista/ui/plugin/sadim/InteroperabilidadGestorExpedientesPlugin.properties"));
		
		ACL_EDITOR_GIS=props.get("acl_editor_gis").toString();
		PERMISO_CONEXION_GESTOR_EXPEDIENTES=props.get("permiso_conexion_gestor_expedientes").toString();
		URL_CONEXION_GESTOR_EXPEDIENTES=props.get("url_conexion_gestor_expedientes").toString();
	}



	public ImageIcon getIcon()
	{
		return IconLoader.icon("interoperabilidadgestorexpedientes.gif");
	}
}