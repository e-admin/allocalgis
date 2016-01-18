/**
 * ApplicationContext.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;

import java.awt.Window;
import java.security.acl.AclNotFoundException;
import java.security.acl.Permission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.util.Blackboard;
//import com.geopista.editor.IGeopistaEditor;
//import com.geopista.server.administradorCartografia.IAdministradorCartografiaClient;
//import com.vividsolutions.jump.workbench.model.Task;

/**
 * @author juacas
 *
 */
public interface ApplicationContext
{
	
	
	/**
	 * Averigua si el usuario está conectado en este momento.
	 * @return
	 */
	public abstract boolean isLogged();

	public abstract boolean isOnlyLogged();

	public abstract void setUrl(String url);
	
    public abstract boolean isPartialLogged();
    
    public abstract void setPartialLogged(boolean partialLogged);
	
	/**
	 * Define el perfil por defecto de la aplicación. Con ese identificador
	 * se obtiene el ACL para consultar los permisos.
	 * @param idProfile
	 * @throws AclNotFoundException
	 */
	public abstract void setProfile(String idProfile) throws AclNotFoundException;
	/**
	 * Chequea el permiso con el ACL especificado para el usuario representado en este
	 * contexto de aplicación.
	 * 
	 * @param permission
	 * @param idProfile si es null se utiliza el que se haya fijado por defecto con
	 * 			la llamada a setProfile
	 * @see setProfile
	 */
	public abstract boolean checkPermission(Permission permission, String idProfile);
	/**
	 * Crea una instancia del visor Editor con el mapa previamente
	 * almacenado en Appcontext
	 * @param id
	 * @return
	 */
	//public abstract IGeopistaEditor getSharedEditor(String id);
	public GeometryFactory getGeometryFactory();
	public GeometryFactory getGeometryFactory(int precision);
	/**
	 * @return Returns the editor.
	 */
	//public abstract IGeopistaEditor getEditor(String id);
	/**
	 * 
	 * @param editor
	 */
	//public void setEditor(String id, IGeopistaEditor editor);
	/**
	 * 
	 * @param editor
	 */
	public void removeEditor(String id);	
	
	
	
	/**
	 * Devuelve un objeto Blackboard qeu sirve de almacen para
	 * la comunicación entre diversas partes de la aplición
	 * @return Blackboard inicializado
	 */
	public abstract Blackboard getBlackboard();

  /**
	 * REgistra listeners para los eventos relativos al contexto de aplicacion
	 * @param listener
	 * @see GeopistaEvent
	 * @see AppContextListener
	 */
	public abstract void addAppContextListener(AppContextListener listener);
	public abstract void removeAppContextListener(AppContextListener listener);
	
	public abstract boolean isOnline();
	/**
	 * Pasa las credenciales del usuario 
	 *
	 */
	public abstract void login();
	public abstract void logout();
	/**
	 * Devuelve una conexión compartida a la base de datos.
	 * @return
	 * @throws SQLException
	 */
	public abstract Connection getConnection() throws SQLException;
	/**
	 * Devuelve el Frame de la aplicacion para referencia de dialogos y otros.
	 * @return Returns the _parentFrame.
	 */
	public abstract JFrame getMainFrame();
	/**
	 * @param frame The _parentFrame to set.
	 */
	public abstract void setMainFrame(JFrame frame);
	
	
	/**
	 * 
	 * @param id Identificación del mapa guardado
	 * @return
	 */
	//public abstract Task getMap(String id);
	/**
	 * Guarda una referencia al mapa para reutilizarlo entre componentes y ahorrar tiempo de carga
	 * @param id Identificación del mapa guardado
	 * @param map The map to set.
	 */
	//public abstract void storeMap(String id, Task map);
	//public abstract void clearMap(String id);
	/**
	 * Traducción del término para el Locale y recurso actual
	 * @see com.geopista.util.ApplicationContext#getI18nString(java.lang.String)
	 */
	public abstract String getI18nString(String resourceId);

	/**
	 * Cambia el locale por defecto del contexto.
	 * Se inicializa automaticamente a partir de la clave 
	 * geopista.user.locale
	 * @param locale
	 */
	public abstract void setLocale(Locale locale);
	
	
	public abstract void closeConnection(Connection conn, PreparedStatement ps,
			Statement st, ResultSet rs);
	/* (non-Javadoc)
	 * @see com.geopista.security.IConnection#disconnect()
	 */public abstract void disconnect();
	/* (non-Javadoc)
	 * @see com.geopista.security.IConnection#connect()
	 */public abstract void connect();
//	/**
//	 * Obtiene un acceso al servidor de cartografia
//	 * @return
//	 */
//	public abstract IAdministradorCartografiaClient getClient();
	/**
	 * Version de un solo parámetro que devuelve null por defecto.
	 * Obtiene la propiedad de configuracion para la aplicacion actual
	 * Busca primero en las preferencias del usuario local.
	 * Despues busca en la configuración general.
	 * Realiza la sustitución de patrones fijados:
	 *
	 * @param host_ip_key2
	 * @param string
	 * @return
	 * @see AppContext.substitutions
	 * @see AppContext.getString(String,String)
	 */
	public abstract String getString(String paramId);
	/**
	 * Obtiene la propiedad de configuracion para la aplicacion actual
	 * Busca primero en las preferencias del usuario local.
	 * Despues busca en la configuración general.
	 * Realiza la sustitución de patrones fijados:
	 *
	 * @param host_ip_key2
	 * @param string
	 * @return
	 * @see AppContext.substitutions
	 */
	public abstract String getString(String paramId, String defaultString);
	
	
	public abstract String getPath(String paramId);
	
	public abstract String getPath(String paramId, String defaultString);
	
//	/**
//	 * Fija una preferencia local de este usuario
//	 * Usa un registro local para configuraciones
//	 * @param key
//	 * @param value
//	 */
//	public abstract void setUserPreference(String key, String value);
//	/**
//	 * Obtiene la preferencia del usuario. Si no se ha fijado antes se extrae
//	 * de la configuración de sistema y se fija para futuras consultas.
//	 * 
//	 * @param key
//	 * @param defaultValue
//	 * @param tryToCreate indica si hay que copiar la propiedad de configuraciones generales
//	 * @return
//	 */
//	public abstract String getUserPreference(String key, String defaultValue,
//			boolean tryToCreate);

	/**
	 * @param windowAncestor
	 */
	public abstract void setMainWindow(Window windowAncestor);
	public abstract Window getMainWindow();

	/**
	 * @return
	 */
	public abstract ResourceBundle getRB();
	/**
	 * Allows an application to use ApplicationContext with custom resource files
	 * @param resource
	 */
	public abstract void loadI18NResource(String resource);

    public abstract void loadI18NResource(String resource, Locale locale);

    public ResourceBundle getI18NResource();
    public String getDefaultProfile();
    public void setEditor(String id, Object editor);
    public Object getEditor(String id);
    public void storeMap(String id, Object map);
    public Object getMap(String id);
    public void clearMap(String id);
    
    public String getDecrytedPassword() throws Exception;
    
}