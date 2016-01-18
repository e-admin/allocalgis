package com.geopista.editor;

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
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.JUMPWorkbench;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

public class GeopistaContext extends WorkbenchContext implements ApplicationContext
{

      private GeopistaEditor geoEditor;
      private ApplicationContext _appContext= AppContext.getApplicationContext();
      
      public GeopistaContext(){
    	  
      }
      
      public GeopistaContext(GeopistaEditor geoEditor)
      {
        this.geoEditor = geoEditor;
        _appContext.setEditor("default",geoEditor);
        if (_appContext.getMainFrame()==null)
        	_appContext.setMainFrame((JFrame)SwingUtilities.getAncestorOfClass(JFrame.class,geoEditor));
      }

      public ErrorHandler getErrorHandler() {
            return null;//GeopistaEditor.this;
      }

        public LayerNamePanel getLayerNamePanel() {
            return geoEditor.getLayerNamePanel();
        }

        public LayerViewPanel getLayerViewPanel() {
            return geoEditor.getLayerViewPanel();
        }

        public ILayerManager getLayerManager() {
            return geoEditor.getLayerManager();
        }

        public PlugInContext createPlugInContext() {
        return new PlugInContext(this,  getTask(), 
        		geoEditor ,getLayerNamePanel(),
				getLayerViewPanel());
        }

        public WorkBench getIWorkbench() {
           return geoEditor;
        }

        public JUMPWorkbench getWorkbench() {
        return geoEditor;
        }
    
        

        public Task getTask() {
            return geoEditor.getTask();
        }
        public Blackboard getBlackboard() {
          return geoEditor.getBlackboard();
        }
	public ApplicationContext getAppContext()
	{
		return _appContext;
	}
	public void clearMap(String id)
	{
		_appContext.clearMap(id);
	}
	public Connection getConnection() throws SQLException
	{
		return _appContext.getConnection();
	}
	public GeopistaEditor getEditor(String id)
	{
		return (GeopistaEditor)_appContext.getEditor(id);
	}
	public ITask getMap(String id)
	{
		return (ITask)_appContext.getMap(id);
	}
	public JFrame getMainFrame()
	{
		return _appContext.getMainFrame();
	}
	public boolean isLogged()
	{
		return _appContext.isLogged();
	}
	public boolean isOnlyLogged()
	{
		return _appContext.isOnlyLogged();
	}
	public void login()
	{
		_appContext.login();
	}
    public boolean isPartialLogged() {
    	return _appContext.isPartialLogged();
    }
    public void setPartialLogged(boolean partialLogged){
    	_appContext.setPartialLogged(partialLogged);
    }
	public void setEditor(String id,Object editor)
	{
		_appContext.setEditor(id, editor);
	}
	public void setMainFrame(JFrame frame)
	{
		_appContext.setMainFrame(frame);
	}
	public void storeMap(String id, Object map)
	{
		_appContext.storeMap(id, map);
	}

	/* (non-Javadoc)
	 * @see com.geopista.util.ApplicationContext#getString(java.lang.String)
	 */
	public String getString(String resourceId)
	{
		return _appContext.getString(resourceId);
	}
	public String getI18nString(String resourceId)
	{
		return _appContext.getI18nString(resourceId);
	}

	public void closeConnection(Connection conn, PreparedStatement ps, Statement st, ResultSet rs)
	{
		_appContext.closeConnection(conn, ps, st, rs);
	}
	public boolean checkPermission(Permission permission,
			String idProfile)
	{
		return _appContext.checkPermission(permission, idProfile);
	}
	public void setProfile(String idProfile) throws AclNotFoundException
	{
		_appContext.setProfile(idProfile);
	}
	public void addAppContextListener(AppContextListener listener)
	{
		_appContext.addAppContextListener(listener);
	}
	public void connect()
	{
		_appContext.connect();
	}
	public void disconnect()
	{
		_appContext.disconnect();
	}
/*	public IAdministradorCartografiaClient getClient()
	{
		return _appContext.getClient();
	}*/
	public String getString(String paramId, String defaultString)
	{
		return _appContext.getString(paramId, defaultString);
	}
	public String getUserPreference(String key, String defaultValue,
			boolean tryToCreate)
	{
		return _appContext.getUserPreference(key, defaultValue, tryToCreate);
	}
	public boolean isOnline()
	{
		return _appContext.isOnline();
	}
	public void logout()
	{
		_appContext.logout();
	}
	public void removeAppContextListener(AppContextListener listener)
	{
		_appContext.removeAppContextListener(listener);
	}
	public void setLocale(Locale locale)
	{
		_appContext.setLocale(locale);
	}
	public void setUserPreference(String key, String value)
	{
		_appContext.setUserPreference(key, value);
	}
	
	public String getPath(String paramId)
	{
	    return getPath(paramId, null);
	}
	
	public String getPath(String paramId, String defaultString)
	{
	    return _appContext.getPath(paramId, defaultString);
	}

	/* (non-Javadoc)
	 * @see com.geopista.util.ApplicationContext#getSharedEditor(java.lang.String)
	 */
	public GeopistaEditor getSharedEditor(String id)
	{
		
        Task map = (Task)getMap(id);
        if (map==null)
            return new GeopistaEditor();
        else
            return new GeopistaEditor((ITask) map);
	}

	
	/* (non-Javadoc)
	 * @see com.geopista.util.ApplicationContext#getMainWindow()
	 */
	public Window getMainWindow()
	{
	
	return SwingUtilities.getWindowAncestor(getMainFrame());
	}

	/* (non-Javadoc)
	 * @see com.geopista.util.ApplicationContext#getRB()
	 */
	public ResourceBundle getRB()
	{
	
	return getAppContext().getRB();
	}

    public void loadI18NResource(String resource)
    {
        _appContext.loadI18NResource(resource);
        
    }

	public void setMainWindow(Window windowAncestor)
	{
	_appContext.setMainWindow(windowAncestor);
	}


    public void loadI18NResource(String resource, Locale locale){
        _appContext.loadI18NResource(resource,  locale);// internacionalizacion
    }

    public ResourceBundle getI18NResource(){
        return _appContext.getI18NResource();
    }


    public String getDefaultProfile(){
    	return _appContext.getDefaultProfile();
    }

	public GeometryFactory getGeometryFactory(){
		return _appContext.getGeometryFactory();
	}

	public GeometryFactory getGeometryFactory(int precision){
		return _appContext.getGeometryFactory(precision);
	}
}