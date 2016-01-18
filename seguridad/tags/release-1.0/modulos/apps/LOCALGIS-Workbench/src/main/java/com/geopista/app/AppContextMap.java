/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 * 
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * For more information, contact:
 * 
 * 
 * www.geopista.com
 * 
 * Created on 11-jun-2004 by juacas
 * 
 *  
 */
package com.geopista.app;

import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.acl.AclNotFoundException;
import java.security.acl.Permission;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.utilidades.PasswordManager;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.IConnection;
import com.geopista.security.ISecurityManager;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.IAdministradorCartografiaClient;
import com.geopista.ui.dialogs.LoginDialog;
import com.geopista.ui.dialogs.MunicipioDialog;
import com.geopista.util.ApplicationContext;
import com.geopista.util.I18NUtils;
import com.geopista.util.SecurityManagerProxy;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 
 * 
 * Información compartida en el transcurso de una ejecución de la aplicación.
 * Este contexto de aplicación lo utilizan las aplicaciones para comunicarse con sus partes.
 * Debe incluirse este contexto de aplicación en el contexto del editor para que los plugins
 * tengan acceso a los servicios de acceso a Geopista.
 * TODO: Asegurarse que el componente Editor puede incorporar el contexto de aplicación dentro del blackboard de su workbenchcontexto.
 *@author juacas
 **/
public class AppContextMap extends AppContext
{	
    
    /**
     * Obtiene un acceso al servidor de cartografia
     * @return
     */
    public IAdministradorCartografiaClient getClient()
    {
        String sUrlPrefix = getString(GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA);
        return new AdministradorCartografiaClient(sUrlPrefix);
    }

    /**
     * Inicializa una instancia de {@link AppContextMap} como {@link AppContext} de la aplicación.
     * Contiene métodos adicionales de utilidad para los clientes de mapas, y que no son necesarios 
     * para los servidores.
     * 
     * @see AppContextMap
     * @see ApplicationContext
     * @see AppContext 
     */
	public static void initAppContextMap() 
	{
		AppContext.hearbeat=false;
    	AppContext.setApplicationContext(new AppContextMap());
    	((AppContext)AppContext.getApplicationContext()).initHeartBeat();
	}
    
    
}