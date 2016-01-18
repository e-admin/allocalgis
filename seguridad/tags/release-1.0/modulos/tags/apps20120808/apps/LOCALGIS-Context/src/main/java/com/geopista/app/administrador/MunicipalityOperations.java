/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.administrador;


/**
 * Definicion de metodos para la realizacion de operaciones sobre base de 
 * datos
 * 
 * @author cotesa
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.layerutil.dbtable.LoginDBDialog;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.protocol.administrador.Entidad;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class MunicipalityOperations
{
    /**
     * Conexion a base de datos
     */
    public Connection conn = null;
    /**
     * Conexion a base de datos sin pasar por el administrador de cartografia
     */
    public static Connection directConn = null;
    
    /**
     * Contexto de la aplicacion
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    /**
     * Blackboard para intercambio de variables entre distintas zonas de la aplicacion
     */
    private Blackboard Identificadores = aplicacion.getBlackboard();
    /**
     * Locale que identifica el idioma del usuario
     */
    private String locale = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);
    public static Set familiasModificadas = new HashSet(); 
    //private static final String pass = aplicacion.getUserPreference("conexion.pass","",false);
    
    private static final int NO_RESULTS=-1;
    
    /**
     * Constructor por defecto
     *
     */
    public MunicipalityOperations()
    {   
    } 
    
    public static void directConnect()
    {
        try
        {
            directConn = getDirectConnection();
        }
        catch(Exception e)
        { 
            e.printStackTrace();
        }     
    }
    
    /**
     * Obtiene una conexion a la base de datos
     * @return Conexion a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException
    {        
        Connection con=  aplicacion.getConnection();
        con.setAutoCommit(false);
        return con;
    }  
    
    
    /**
     * Obtiene una conexion directa a la base de datos, sin utilizar el administrador de 
     * cartografia
     * @return Conexion a la base de datos
     * @throws SQLException 
     */
    private static Connection getDirectConnection () throws SQLException
    {   
        if (directConn==null
                && aplicacion.getBlackboard().get("DirectConnection")==null)
        {    
            LoginDBDialog dial = new LoginDBDialog(aplicacion.getMainFrame());
            if (aplicacion.getMainFrame()==null)
                GUIUtil.centreOnScreen(dial);
            else
                GUIUtil.centreOnWindow(dial);
            
            dial.show();
            
            directConn = aplicacion.getJDBCConnection(dial.getLogin(), dial.getPassword());
            aplicacion.getBlackboard().put("USER_BD", dial.getLogin());
            aplicacion.getBlackboard().put("PASS_BD", dial.getPassword());
            aplicacion.getBlackboard().put("DirectConnection", directConn);        
            
            directConn.setAutoCommit(true);
        } 
        else if (aplicacion.getBlackboard().get("DirectConnection")!=null)
        {
            directConn = (Connection)aplicacion.getBlackboard().get("DirectConnection");  
            if (directConn.isClosed())
                directConn = aplicacion.getJDBCConnection(aplicacion.getBlackboard().get("USER_BD").toString(),
                        aplicacion.getBlackboard().get("PASS_BD").toString());
        }            
        else if (directConn.isClosed() && aplicacion.getBlackboard().get("USER_BD")!=null
                && aplicacion.getBlackboard().get("PASS_BD") !=null)
        {
            directConn = aplicacion.getJDBCConnection(aplicacion.getBlackboard().get("USER_BD").toString(),
                    aplicacion.getBlackboard().get("PASS_BD").toString());
        }
        return directConn;
    }  
    
 
    /**
     * A partir de una lista con Object[] en cada posicion, creo un lista de clases Municipio
     */
    public List construirListaMunicipios (List lista){
    	List listFinal = new ArrayList();
    	int n = lista.size();
    	for (int i=0;i<n;i++){
    		Object[] elemento = (Object[])lista.get(i);
    		if (elemento.length==3)
    			listFinal.add(new com.geopista.app.catastro.model.beans.Municipio((String)elemento[0],(String)elemento[1],(String)elemento[2]));
    		else
    			listFinal.add(new com.geopista.app.catastro.model.beans.Municipio((String)elemento[0],(String)elemento[1],(String)elemento[2],(String)elemento[3]));
    	}
    	return listFinal;
    }

    public List getMunicipiosEntidad(String idEntidad) throws DataException{
        List alList = new ArrayList();
		try{
	        PreparedStatement s = null;
	        ResultSet r = null;
	        s = conn.prepareStatement("getMunicipiosEntidad");
	        s.setString(1, idEntidad);
	        r = s.executeQuery();  
	        while (r.next()){
		        com.geopista.app.catastro.model.beans.Municipio municipio=new com.geopista.app.catastro.model.beans.Municipio();
	            municipio.setId(r.getInt("id"));
	            municipio.setNombreOficial(r.getString("nombreoficial"));
	            municipio.setSrid(r.getString("srid"));
	            alList.add(municipio);
	        }
	        if (s!=null) s.close();
	        if (r!= null) r.close(); 
		}catch(SQLException e){
            throw new DataException(e);
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
	            throw new DataException(e);
			}
	        return alList;
		}
    }

    /**
     * A partir de una lista con Object[] en cada posicion, creo un lista de clases Entidad
     */
    public List construirListaEntidades (List lista){
    	List listFinal = new ArrayList();
    	int n = lista.size();
    	for (int i=0;i<n;i++){
    		Object[] elemento = (Object[])lista.get(i);
    		listFinal.add(new Entidad((String)elemento[0],(String)elemento[1], (String)elemento[2]));
    	}
    	return listFinal;
    }
	
    
}


