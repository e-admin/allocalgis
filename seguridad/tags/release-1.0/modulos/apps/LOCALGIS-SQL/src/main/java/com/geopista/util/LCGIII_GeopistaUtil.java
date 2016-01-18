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
	
}
