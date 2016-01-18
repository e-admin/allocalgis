package com.geopista.sql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.SesionUtils_LCGIII;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.util.ServletContextListener;
import com.localgis.server.SessionsContextShared;


public class CServletDB extends LoggerHttpServlet{
	Logger logger = Logger.getLogger(CServletDB.class);

	public int numeroServlet = 0;

	private boolean ACTIVATE_QUERY_CACHE=false;
	HashMap queryCache=new HashMap();
	
	public void init(ServletConfig config) throws ServletException

	{
		super.init(config);

		/*   Integer ins=(Integer) config.getServletContext().getAttribute("running_instances");

    if (ins==null)

        ins=new Integer (0);

    config.getServletContext().setAttribute("running_instances", new Integer(ins.intValue()+1));

    System.out.println("Instantiated a new "+config.getServletName()+" object. Total:"+ins);
		 */
		
		ServletContextListener.numero ++;
		numeroServlet = ServletContextListener.numero;

		// System.out.println("Instantiated a new "+config.getServletName()+" object. Total:"+ServletContextListener.numero);

	}

	public void doGet(HttpServletRequest request,HttpServletResponse response)  throws ServletException, IOException{
		response.getWriter().append("Authentication Successful!");  
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response)  
	throws ServletException, IOException {
        super.doPost(request);
		PreparedStatement psSQL=null;
		ResultSet rsSQL=null;
		ResultSet rsRet=null;
		Connection connection=null;
		try {
			

			if (request.getParameter("test")!=null)
				return;
			//System.out.println("Conexiones:"+CPoolDatabase.getNumeroConexiones());
			//request.getReader();
			//GEOPISTAPreparedStatement ps=(GEOPISTAPreparedStatement)Unmarshaller.unmarshal(GEOPISTAPreparedStatement.class,request.getReader());
			PoolPS commitPS=null;
			GEOPISTAPreparedStatement ps=null;
			if(!org.apache.commons.fileupload.DiskFileUpload.isMultipartContent(request))
			{
				/* -- PostMethod --  */
				request.getReader();
				try
				{
					ps=(GEOPISTAPreparedStatement)Unmarshaller.unmarshal(GEOPISTAPreparedStatement.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));                    
				}
				catch(Exception e)
				{
					commitPS= (PoolPS)Unmarshaller.unmarshal(PoolPS.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
				}
				//System.out.println("Escribe servlet numero:" + numeroServlet + " POST con PS: "+ps.toString());
			}
			else
			{
				/** -- MultiPartPost -- */
				String stream=null;
				// Create a new file upload handler
				org.apache.commons.fileupload.DiskFileUpload upload= new org.apache.commons.fileupload.DiskFileUpload();
				List items= upload.parseRequest(request);

				// Process the uploaded items
				Iterator iter = items.iterator();

				while (iter.hasNext())
				{
					FileItem item = (FileItem) iter.next();

					String fieldName = item.getFieldName();
					if (item.isFormField())
					{
						if (fieldName.equalsIgnoreCase("mensajeXML"))
						{
							stream  = item.getString("ISO-8859-1");
							//logger.info("MENSAJE XML:"+item.getString("ISO-8859-1"));
							//System.out.println("CServletDB.doPost mensajeXML="+item.getString("ISO-8859-1"));
						}
					}
				}
				if(stream!=null && !(stream.trim().equals("")))
				{
					StringReader sw = new StringReader(stream);
					try
					{
						ps=(GEOPISTAPreparedStatement)Unmarshaller.unmarshal(GEOPISTAPreparedStatement.class,sw);
					}
					catch(Exception e)
					{
						commitPS= (PoolPS)Unmarshaller.unmarshal(PoolPS.class,new StringReader(stream));
					}
				}
				else
				{
					throw new Exception("El stream es null");
				}
			}
			if (ps!=null)
				connection=execute(ps, connection,psSQL, rsSQL,rsRet,response, request);
			else
				connection=execute(commitPS, connection,psSQL, rsSQL,rsRet,response, request);
			//System.out.println("Escribe servlet numero:" + numeroServlet + " MULTIPART con PS: "+ps.toString());
		} catch (Exception e) {
			try{connection.rollback();}catch(Exception ex){};
			logger.error("Error al ejecutar la query:"+e);
		}  finally{
			try{rsSQL.close();}catch(Exception e){};
			try{psSQL.close();}catch(Exception e){};
			try{
				if (connection!=null){
					if (!connection.isClosed())
						CPoolDatabase.releaseConexion();			
					connection.close();
				}
			}
				
			catch(Exception e)
			{
				e.printStackTrace();                
			};
		}
	}
	public Connection execute(GEOPISTAPreparedStatement ps, Connection connection,
			PreparedStatement psSQL, ResultSet rsSQL, ResultSet rsRet,
			HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		try
		{
			//Marshaller.marshal(ps,new OutputStreamWriter(System.out));
			InitialContext context=new InitialContext();
			//DataSource datasource =(DataSource)context.lookup("geopista");
			//connection = datasource.getConnection();
			connection=CPoolDatabase.getConnection();
			logger.info("Buscando Query: "+ps.getQueryId());
			
			String sStatement=null;
			if (queryCache.containsKey(ps.getQueryId())){
				logger.info("Query existe en la cache:");
				sStatement=(String)queryCache.get(ps.getQueryId());
			}
			else{
	            psSQL=connection.prepareStatement("select query from query_catalog where id=?");
				psSQL.setString(1,ps.getQueryId());
	            rsSQL=psSQL.executeQuery();
	           
	            if (rsSQL.next())
	            	sStatement=rsSQL.getString("query");
	            else{
	            	sStatement = ps.getQueryId();
	            	logger.error("La query:"+ps.getQueryId()+" no existe en la Base de Datos");
	            }
	            
	            logger.info("Ejecutando query: "+sStatement);
	            
	            if (sStatement!=null){
	            	if (ACTIVATE_QUERY_CACHE)
	            		queryCache.put(ps.getQueryId(), sStatement);
	            }
	            
				rsSQL.close();
				psSQL.close();
			}
			Object[] oData=(Object[])(new ObjectInputStream(new ByteArrayInputStream(ps.getParams())).readObject());
            int i = 0;
            for (i=0;i<oData.length;i++){
            	if (sStatement.indexOf("#")<0) break;
            		
            	String value = (String)oData[i];
            	value.replaceAll(" ", ""); //Se hace para evitar parametros extraños con espacios en medio;
            	sStatement = sStatement.replaceFirst("#", value);
            }
           
            psSQL=connection.prepareStatement(sStatement);
            if (sStatement.indexOf("?")>0){
            	int k=1;
            	for (int j=i;j<oData.length;j++){
            		psSQL.setObject(k,oData[j]);
            		k++;
            	}
            }
            
            try {
				logger.info("Ejecutando query Filled: "+((org.enhydra.jdbc.standard.StandardXAPreparedStatement)psSQL).ps.toString());
			} catch (Exception e) {}
			if (psSQL.execute())
				//REFACTORIZACION ORACLE rsRet=new GEOPISTAResultSet(null,psSQL.getResultSet());
			rsRet=new GEOPISTAResultSet(null,psSQL.getResultSet(), !CPoolDatabase.isPostgres(connection));
			else{

				connection.commit();
				rsRet=new GEOPISTAResultSet(psSQL.getUpdateCount());
			}
			response.setHeader("Transfer-encoding","chunked");
			response.setHeader("Content-Encoding", "gzip");
			/*    ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Marshaller.marshal(rsRet,new OutputStreamWriter(baos));
            // prepare a gzip stream
            ByteArrayOutputStream compressedContent = new ByteArrayOutputStream();
            GZIPOutputStream gzipstream = new GZIPOutputStream(compressedContent);
            byte[] bytes = baos.toByteArray();
            gzipstream.write(bytes);
            gzipstream.finish();
            response.getWriter().print(compressedContent.toByteArray());*/

			OutputStream out1 = response.getOutputStream();
			PrintWriter out = new PrintWriter(new GZIPOutputStream(out1), false);
			Marshaller.marshal(rsRet,out);
			out.close();
			return connection;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			   
			response.setHeader("Transfer-encoding","chunked");
			response.setHeader("Content-Encoding", "gzip");
			
			OutputStream out = response.getOutputStream();
			ObjectOutputStream out1 = new ObjectOutputStream(new GZIPOutputStream(out));
			out1.writeObject(new ACException(e));
			
			PrintWriter outpw = new PrintWriter(new GZIPOutputStream(out1), false);
			Marshaller.marshal(new Exception(),outpw);
			outpw.close();		
						
			try
			{
				out1.flush();
				out1.close();	
				out.flush();
			}
			catch(Exception excep)
			{
				excep.printStackTrace();
			}			
			try
			{
				connection.rollback();
			}
			catch(Exception ex){};			
			
			throw e;
		}
		finally
		{			
			try
			{
				connection.close();
				CPoolDatabase.releaseConexion();
			}
			catch(Exception ex){};
			
			try{rsSQL.close();}catch(Exception ex){};
			try{psSQL.close();}catch(Exception ex){};			
		}
	}

	public Connection execute(PoolPS alPs, Connection connection,
			PreparedStatement psSQL, ResultSet rsSQL, ResultSet rsRet,
			HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		try
		{
			new InitialContext();
			connection=CPoolDatabase.getConnection();
			String oldIdQuery="";
			String sStatement="";
			
			//autocommit a false para que guarde en el pool
			connection.setAutoCommit(false);
			
			int numQueries = alPs.getPool().size();
			//se recorren las queries para ver si todas tienen permiso
			for (Iterator it=alPs.iterator();it.hasNext();)
			{
				GEOPISTAPreparedStatement ps=(GEOPISTAPreparedStatement)it.next();
				logger.info("Buscando Query: " + ps.getQueryId());
				if (oldIdQuery==null || !oldIdQuery.equals(ps.getQueryId()))
				{
					oldIdQuery=ps.getQueryId();
					psSQL=connection.prepareStatement("select query_catalog.query, query_catalog.acl, usrgrouperm.def from query_catalog, usrgrouperm where usrgrouperm.idperm = query_catalog.idperm and query_catalog.id=?");
					psSQL.setString(1,ps.getQueryId());
					rsSQL=psSQL.executeQuery();
					rsSQL.next();
					sStatement=rsSQL.getString("query");
					long iAcl = rsSQL.getLong("acl");
					String perm = rsSQL.getString("def");
					logger.info("Ejecutando query: " + sStatement);
					rsSQL.close();
					psSQL.close();

					if (!checkPermission(sesion(request),iAcl,perm,CPoolDatabase.getConnection()))
					{
						throw new PermissionException("No puede ejecutar la operación " + ps.getQueryId()+" sin tener el permiso"+" " + perm);
					}
				}
			}
			
			int ejecutados=0;
			for (Iterator it=alPs.iterator();it.hasNext();)
			{
				GEOPISTAPreparedStatement ps=(GEOPISTAPreparedStatement)it.next();
				logger.info("Buscando Query: " + ps.getQueryId());
				if (oldIdQuery==null || !oldIdQuery.equals(ps.getQueryId()))
				{
					oldIdQuery=ps.getQueryId();
					psSQL=connection.prepareStatement("select query_catalog.query, query_catalog.acl, usrgrouperm.def from query_catalog, usrgrouperm where usrgrouperm.idperm = query_catalog.idperm and query_catalog.id=?");
					psSQL.setString(1,ps.getQueryId());
					rsSQL=psSQL.executeQuery();
					rsSQL.next();
					sStatement=rsSQL.getString("query");
					long iAcl = rsSQL.getLong("acl");
					String perm = rsSQL.getString("def");
					logger.info("Ejecutando query: " + sStatement);
					rsSQL.close();
					psSQL.close();

					//if (!checkPermission(sesion(request),iAcl,perm,CPoolDatabase.getConnection()))
					//	continue;

				}
				
				psSQL=connection.prepareStatement(sStatement);
				Object[] oData=(Object[])(new ObjectInputStream(new ByteArrayInputStream(ps.getParams())).readObject());
				for (int i=0;i<oData.length;i++)
					psSQL.setObject(i+1,oData[i]);
				
				try //Se intenta ejecutar todas las sentencias
				{
					if (psSQL.execute())
						//REFACTORIZACION ORACLE rsRet=new GEOPISTAResultSet(null,psSQL.getResultSet());
						rsRet=new GEOPISTAResultSet(null,psSQL.getResultSet(), !CPoolDatabase.isPostgres(connection));
					else{
						//connection.commit();
						rsRet=new GEOPISTAResultSet(psSQL.getUpdateCount());
					}
					ejecutados++;
				}catch(Exception e){e.printStackTrace();};
			}
			
			// Si el número de queries ejecutadas no coincide con las que se pidieron, no se hace el commit
			// y se envía una excepción			
			if (ejecutados!= numQueries)
			{
				response.setHeader("Transfer-encoding","chunked");
				response.setHeader("Content-Encoding", "gzip");
				
				OutputStream out = response.getOutputStream();
				ObjectOutputStream out1 = new ObjectOutputStream(new GZIPOutputStream(out));
				ACException ace = new ACException("No se han podido ejecutar todas las queries del pool"); 
				out1.writeObject(ace);
				
				PrintWriter outpw = new PrintWriter(new GZIPOutputStream(out1), false);
				Marshaller.marshal(new Exception(),outpw);
				outpw.close();		
							
				try
				{
					out1.flush();
					out1.close();	
					out.flush();
				}
				catch(Exception excep)
				{
					excep.printStackTrace();
				}
				throw ace;
			}
			else
			{
				connection.commit();
				rsRet=new GEOPISTAResultSet(ejecutados);
				response.setHeader("Transfer-encoding","chunked");
				response.setHeader("Content-Encoding", "gzip");
				OutputStream out1 = response.getOutputStream();
				PrintWriter out = new PrintWriter(new GZIPOutputStream(out1), false);
				Marshaller.marshal(rsRet,out);
				out.close();				
			}
			
			
			return connection;
		}
		catch(Exception e)
		{
			
			response.setHeader("Transfer-encoding","chunked");
			response.setHeader("Content-Encoding", "gzip");
			
			OutputStream out = response.getOutputStream();
			ObjectOutputStream out1 = new ObjectOutputStream(new GZIPOutputStream(out));
			out1.writeObject(new ACException(e));
			
			PrintWriter outpw = new PrintWriter(new GZIPOutputStream(out1), false);
			Marshaller.marshal(new Exception(),outpw);
			outpw.close();		
						
			try
			{
				out1.flush();
				out1.close();	
				out.flush();
			}
			catch(Exception excep)
			{
				excep.printStackTrace();
			}			
			try
			{
				connection.close();
				CPoolDatabase.releaseConexion();
				//connection.rollback();
			}
			catch(Exception ex){};			
			
			throw e;
			
			/*
			e.printStackTrace();

			try{connection.rollback();}catch(Exception ex){};
			try{rsSQL.close();}catch(Exception ex){};
			try{psSQL.close();}catch(Exception ex){};
			try{connection.close();CPoolDatabase.releaseConexion();}catch(Exception ex){};
			throw e;
			*/
		}
	}

	private ISesion sesion(HttpServletRequest request){
		ISesion sRet=null;
		JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
		if (userPrincipal!=null){
			String  sIdSesion= userPrincipal.getName();
			
			PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
	    	
			sRet=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
		}
		return sRet;
	}

	private boolean checkPermission(ISesion sSesion,long lACL,String sPerm,Connection con) throws Exception{
		logger.debug("Buscando el acl:"+lACL);
		GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sSesion,lACL,con);
		if (acl==null) return false;
		return acl.checkPermission(new GeopistaPermission(sPerm));
	}

}
//get the compressed content
/*        byte[] compressedBytes = compressedContent.toByteArray();
                        ObjectOutputStream oos=new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));
                        Marshaller.marshal(rsRet,new ByteArrayOutputStream(oos));
                        oos.close();
            //Marshaller.marshal(rsRet,response.getWriter()); */


