/**
 * COperacionesALP.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import com.geopista.app.alptolocalgis.beans.OperacionAlp;
import com.geopista.server.administradorCartografia.ACException;

public class COperacionesALP {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesALP.class);

	private static boolean safeClose(ResultSet rs, Statement statement, Connection connection) {
		
		try {
			connection.commit();
		} catch (Exception ex2) {
		}
		try {
			rs.close();
		} catch (Exception ex2) {
		}
		try {
			statement.close();
		} catch (Exception ex2) {
		}
		try {
			connection.close();
			CPoolDatabase.releaseConexion();
		} catch (Exception ex2) {
		}

		return true;
	}

	private static boolean safeClose(ResultSet rs, Statement statement, PreparedStatement preparedStatement, Connection connection) {

		try {
			rs.close();
		} catch (Exception ex2) {
		}
		try {
			statement.close();
		} catch (Exception ex2) {
		}
		try {
			preparedStatement.close();
		} catch (Exception ex2) {
		}
		try {
			connection.close();
			CPoolDatabase.releaseConexion();
		} catch (Exception ex2) {
		}

		return true;
	}
	
	public void getOperaciones(ObjectOutputStream oos, String idMunicipio)throws Exception
    {
       try
       {
    	   for(Iterator it= getOperaciones(idMunicipio).iterator();it.hasNext();){
    		   OperacionAlp operacion = (OperacionAlp)it.next();
    		   oos.writeObject(operacion);
    	   }
       }
       catch(Exception e)
       {
           logger.error("getUsuarios: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
	
	public void getOperaciones(ObjectOutputStream oos, String filtro, String idMunicipio)throws Exception
    {
       try
       {
    	   for(Iterator it= getOperaciones(filtro, idMunicipio).iterator();it.hasNext();){
    		   OperacionAlp operacion = (OperacionAlp)it.next();
    		   oos.writeObject(operacion);
    	   }
       }
       catch(Exception e)
       {
           logger.error("getUsuarios: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
	
	public void getIdMapa(ObjectOutputStream oos, String nombreMapa, String idEntidad)throws Exception
    {
       try
       {
    	   Integer idMapa = getIdMapa(nombreMapa,idEntidad);
    	   oos.writeObject(idMapa);
    	  
       }
       catch(Exception e)
       {
           logger.error("getUsuarios: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
	
	public void removeOperacion(ObjectOutputStream oos, Integer idOperacion)throws Exception
    {
       try
       {
    	   if (idOperacion != null){
    		   removeOperacion(idOperacion);
    	   }
       }
       catch(Exception e)
       {
           logger.error("getUsuarios: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
	
	public ArrayList getOperaciones() {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		ArrayList lstOperaciones = null;
		
		try {
			
			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new ArrayList();
			}
			
			statement = connection.prepareStatement("select id, xml, tipo_operacion, fecha_operacion from alp_temporal");
			rs = statement.executeQuery();

			lstOperaciones = new ArrayList();
			while (rs.next()) {

				OperacionAlp operacionAlp = new OperacionAlp();
				operacionAlp.setIdOperacion(new Integer(rs.getInt("id")));
				operacionAlp.setXml(rs.getString("xml"));
				operacionAlp.setTipoOperacion(rs.getString("tipo_operacion"));
				operacionAlp.setFechaOperacion(rs.getDate("fecha_operacion"));
				lstOperaciones.add(operacionAlp);

			}
			safeClose(rs, statement, connection);

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

		}

		return lstOperaciones;
	}
	
	public ArrayList getOperaciones(String filtro, String idMunicipio) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		ArrayList lstOperaciones = null;
		
		try {
			
			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new ArrayList();
			}
			
			statement = connection.prepareStatement("select id, xml, tipo_operacion, " +
					"fecha_operacion from alp_temporal where idmunicipio=" 
					+ idMunicipio + " and tipo_operacion='" + filtro + "'");
			rs = statement.executeQuery();

			lstOperaciones = new ArrayList();
			while (rs.next()) {

				OperacionAlp operacionAlp = new OperacionAlp();
				operacionAlp.setIdOperacion(new Integer(rs.getInt("id")));
				operacionAlp.setXml(rs.getString("xml"));
				operacionAlp.setTipoOperacion(rs.getString("tipo_operacion"));
				operacionAlp.setFechaOperacion(rs.getDate("fecha_operacion"));
				lstOperaciones.add(operacionAlp);

			}
			safeClose(rs, statement, connection);

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

		}

		return lstOperaciones;
	}
	
	public Integer getIdMapa(String nombreMapa, String idEntidad) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Integer idMapa = null;
		
		try {
			
			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}
			
			statement = connection.prepareStatement("select m.id_map " +
		            "from maps m, dictionary d " +
		            "where m.id_name=d.id_vocablo and d.traduccion='" + nombreMapa +
		            "' and (id_entidad = 0  or id_entidad=" + idEntidad + ")");
			rs = statement.executeQuery();
			
			while (rs.next()) {

				idMapa = new Integer(rs.getInt("id_map"));			

			}
			safeClose(rs, statement, connection);

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

		}

		return idMapa;
	}
	
	public ArrayList removeOperacion(Integer idOperacion) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;
		ArrayList lstOperaciones = null;
		
		try
        {
            String queryUpdate = "delete from alp_temporal where id=" + idOperacion ;
            connection = CPoolDatabase.getConnection();
            statement= connection.prepareStatement(queryUpdate);
            statement.execute();
            connection.commit();
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            try{
            	statement.close();
            }catch(Exception e){};
            try{
            	connection.close();
            }catch(Exception e){};
        }

		return lstOperaciones;
	}
	
	public ArrayList getOperaciones(String idMunicipio) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		ArrayList lstOperaciones = null;
		
		try {
			
			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new ArrayList();
			}
			
			statement = connection.prepareStatement("select id, xml, tipo_operacion, " +
					"fecha_operacion from alp_temporal where idmunicipio=" 
					+ idMunicipio);
			rs = statement.executeQuery();

			lstOperaciones = new ArrayList();
			while (rs.next()) {

				OperacionAlp operacionAlp = new OperacionAlp();
				operacionAlp.setIdOperacion(new Integer(rs.getInt("id")));
				operacionAlp.setXml(rs.getString("xml"));
				operacionAlp.setTipoOperacion(rs.getString("tipo_operacion"));
				operacionAlp.setFechaOperacion(rs.getDate("fecha_operacion"));
				lstOperaciones.add(operacionAlp);

			}
			safeClose(rs, statement, connection);

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

		}

		return lstOperaciones;
	}

}
