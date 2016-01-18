/**
 * LCGIII_GeopistaUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.enhydra.jdbc.standard.StandardXAPreparedStatement;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.dbtable.ColumnDB;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.sql.GEOPISTAPreparedStatement;

public class LCGIII_GeopistaUtil {

	/**
	 * Logger for this class
	 */
	private static final Log logger	= LogFactory.getLog(LCGIII_GeopistaUtil.class);

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	 /**
     * 
     * @param ps 
     */
	public static void generarSQL (Statement ps)
	{
		boolean genSQL = new Boolean(aplicacion.getString("geopista.generarsql")).booleanValue();

		if (genSQL)
		{   
			try
			{              
				String rutaFich = aplicacion.getString("ruta.base.mapas");
				File fichLog = new File (rutaFich, "BDqueries.sql");
				OutputStream out = new FileOutputStream(fichLog, true);
				String stQuery = new String(); 

				if (ps instanceof GEOPISTAPreparedStatement)
				{

					PreparedStatement s = null;
					ResultSet r = null;
					java.sql.Connection conn=  aplicacion.getConnection();
					conn.setAutoCommit(false);            

					s = conn.prepareStatement("getQueryById");
					s.setString(1, ((GEOPISTAPreparedStatement)ps).getQueryId());                
					r = s.executeQuery();

					if (r.next())
					{
						stQuery= r.getString(1);
						Object[] oData=(Object[])(new ObjectInputStream(new ByteArrayInputStream(((GEOPISTAPreparedStatement)ps).getParams())).readObject());


						for (int i=0;i<oData.length;i++)
						{
							String cadSustitucion = new String();

							if (oData[i]!=null)
							{
								cadSustitucion = oData[i].toString();
								cadSustitucion = cadSustitucion.replaceAll("\\?", "\\!");

								if (oData[i] instanceof String)
									cadSustitucion = "'"+cadSustitucion+"'";
							}
							else
							{
								cadSustitucion="null";
							}
							stQuery = stQuery.replaceFirst("\\?", cadSustitucion);

						}  
						stQuery= stQuery.replaceAll("\\!", "\\?") +";\n";
						out.write(stQuery.getBytes());                      

					}
				}
				else if (ps instanceof StandardXAPreparedStatement)
				{   
					java.sql.Connection conex=CPoolDatabase.getConnection();
					if (((org.enhydra.jdbc.core.CoreConnection)conex).con instanceof org.postgresql.PGConnection)
					{
						stQuery = (((StandardXAPreparedStatement)ps).sql)+ ";\n";;
						out.write(stQuery.getBytes());                     

					}
					conex.close();
					CPoolDatabase.releaseConexion();               
				}
				else if (ps instanceof org.postgresql.PGStatement)
				{   
					java.sql.Connection conex=CPoolDatabase.getConnection();
					stQuery = ((org.postgresql.PGStatement)ps)+ ";\n";;
					out.write(stQuery.getBytes());                     
					conex.close();
					CPoolDatabase.releaseConexion();               
				}
				
				out.close();
			}
			catch (SQLException ex)
			{
				logger.error(ex);
				//ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));                  
				return;
			}

			catch (Exception ex)
			{            
				ex.printStackTrace();
				return;
			}            
		}
	}


	/**
     * 
     * @param s 
     */
	  public static void generarSQL (String stQuery)
	  {
	      boolean genSQL = new Boolean(aplicacion.getString("geopista.generarsql")).booleanValue();
	      
	      if (genSQL)
	      {  
	          try
	          {
	              String rutaFich = aplicacion.getString("ruta.base.mapas");
	              File fichLog = new File (rutaFich, "BDqueries.sql");
	              OutputStream out =  new FileOutputStream(fichLog, true);
	              stQuery = stQuery.replaceAll(";", ";\n");
	                  
	              out.write(stQuery.getBytes());
	              out.close();
	          } catch (FileNotFoundException e)
	          {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	          }
	          catch (IOException e)
	          {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	          }
	          
	          catch (Exception ex)
	          {            
	              ex.printStackTrace();
	              return;

	          }
	          
	      }
	  }
	  
	  /**
	     * Traduce el tipo de datos devuelto por el catálogo de PostgreSQL al tipo de datos
	     * utilizado en GeoPISTA
	     * @param type Tipo de datos del catálogo de PostgreSQL
	     * @return Tipo de datos de GeoPISTA
	     */
	    public static String translateBDtoComboItem(String type)
	    {
	        if (type.equalsIgnoreCase("int4"))
	            return ColumnDB.COL_INTEGER;
	        else if (type.equalsIgnoreCase("bpchar"))
	            return ColumnDB.COL_CHAR;
	        else if (type.equalsIgnoreCase("bool"))
	            return ColumnDB.COL_BOOLEAN;
	        else 
	            return type.toUpperCase();
	        
	    }  
	
}
