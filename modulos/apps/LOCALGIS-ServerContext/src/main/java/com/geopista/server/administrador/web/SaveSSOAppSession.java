/**
 * SaveSSOAppSession.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administrador.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exolab.castor.xml.Marshaller;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.CPoolDatabase;

public class SaveSSOAppSession extends LoggerHttpServlet {
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(SaveSSOAppSession.class);

	private final static String AppCentralizadorSSO = "CentralizadorSSO"; 
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.error("INICIO SaveSSOAppSession:");
		super.doPost(request);
		try {				
			Marshaller.marshal(saveSSOAppSession(request.getParameter("mensajeXML"),request.getParameter(EnviarSeguro.IdApp)), response.getWriter());
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Excepción al guardar histórico de sesión SSO:" + sw.toString());
			try {
				Marshaller.marshal(new CResultadoOperacion(false,
						"Excepción al guardar histórico de sesión SSO:" + e.toString()),
						response.getWriter());
			} catch (Exception ex) {
			}
		}
	}
	
	public static CResultadoOperacion saveSSOAppSession(String sIdSesion, String sAppDef){
		 Connection connection = null;
			PreparedStatement preparedStatement = null;
	        ResultSet rs = null;
	        if(!sAppDef.equals(AppCentralizadorSSO)){
		    	try {
					connection = CPoolDatabase.getConnection();
					if (connection == null) {
						logger.warn("No se puede obtener la conexión");
					    return new CResultadoOperacion(false, "No se puede obtener la conexión");
					}
					 preparedStatement = connection.prepareStatement("Select appid from appgeopista where Upper(def)=Upper(?)");
			         preparedStatement.setString(1,sAppDef);
			         rs=preparedStatement.executeQuery();
			         if (rs.next()){
			            String sIdApp=rs.getString(1);
			            rs.close();
		            	try{
		            		preparedStatement = connection.prepareStatement("insert into session_app (id,access_order,appid) values (?,Coalesce((select (max(access_order)+1) from session_app where id=?),1),?)");
		            		preparedStatement.setString(1, sIdSesion);
		            		preparedStatement.setString(2, sIdSesion);
		            		preparedStatement.setLong(3, Long.parseLong(sIdApp));          		
		            		preparedStatement.execute();
		            		preparedStatement.close();
		            	}	catch(Exception ex){
		            		logger.warn("Ha habido un error al grabar el historico de sesion de SSO",ex);	            	
		            	}
			        }
		            connection.commit();
		            connection.close();
		            CPoolDatabase.releaseConexion();
		        } catch (Exception ex) {
		              try {rs.close();} catch (Exception ex2) {}
		              try {preparedStatement.close();} catch (Exception ex2) {}
		              try {connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
		            StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					ex.printStackTrace(pw);
					logger.error("Exception: " + sw.toString());
					return new CResultadoOperacion(false, "Exception al grabar el histórico de sesión de SSO: " + sw.toString()+ " Sesion:"+sIdSesion);
		    	}
		        return new CResultadoOperacion(true,"Operación ejecutar saveSSOAppSession realizada con exito");
	        }
	        return new CResultadoOperacion(true,"CentralizadorSSO");
	}
	
	
}
