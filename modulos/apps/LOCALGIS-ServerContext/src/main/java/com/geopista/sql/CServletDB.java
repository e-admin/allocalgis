/**
 * CServletDB.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

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

public class CServletDB extends LoggerHttpServlet {
	Logger logger = Logger.getLogger(CServletDB.class);

	public int numeroServlet = 0;

	private boolean ACTIVATE_QUERY_CACHE = false;
	HashMap queryCache = new HashMap();

	public void init(ServletConfig config) throws ServletException

	{
		super.init(config);

		/*
		 * Integer ins=(Integer)
		 * config.getServletContext().getAttribute("running_instances");
		 * 
		 * if (ins==null)
		 * 
		 * ins=new Integer (0);
		 * 
		 * config.getServletContext().setAttribute("running_instances", new
		 * Integer(ins.intValue()+1));
		 * 
		 * System.out.println("Instantiated a new "+config.getServletName()+
		 * " object. Total:"+ins);
		 */

		ServletContextListener.numero++;
		numeroServlet = ServletContextListener.numero;

		// System.out.println("Instantiated a new "+config.getServletName()+" object. Total:"+ServletContextListener.numero);

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Authentication Successful!");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request);
		PreparedStatement psSQL = null;
		ResultSet rsSQL = null;
		ResultSet rsRet = null;
		Connection connection = null;
		try {

			if (request.getParameter("test") != null)
				return;
			// System.out.println("Conexiones:"+CPoolDatabase.getNumeroConexiones());
			// request.getReader();
			// GEOPISTAPreparedStatement
			// ps=(GEOPISTAPreparedStatement)Unmarshaller.unmarshal(GEOPISTAPreparedStatement.class,request.getReader());
			PoolPS commitPS = null;
			GEOPISTAPreparedStatement ps = null;
			if (!org.apache.commons.fileupload.DiskFileUpload
					.isMultipartContent(request)) {
				/* -- PostMethod -- */
				request.getReader();
				try {
					ps = (GEOPISTAPreparedStatement) Unmarshaller.unmarshal(
							GEOPISTAPreparedStatement.class,
							new StringReader(request
									.getParameter(EnviarSeguro.mensajeXML)));
				} catch (Exception e) {
					commitPS = (PoolPS) Unmarshaller.unmarshal(
							PoolPS.class,
							new StringReader(request
									.getParameter(EnviarSeguro.mensajeXML)));
				}
				// System.out.println("Escribe servlet numero:" + numeroServlet
				// + " POST con PS: "+ps.toString());
			} else {
				/** -- MultiPartPost -- */
				String stream = null;
				// Create a new file upload handler
				org.apache.commons.fileupload.DiskFileUpload upload = new org.apache.commons.fileupload.DiskFileUpload();
				List items = upload.parseRequest(request);

				// Process the uploaded items
				Iterator iter = items.iterator();

				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();

					String fieldName = item.getFieldName();
					if (item.isFormField()) {
						if (fieldName.equalsIgnoreCase("mensajeXML")) {
							stream = item.getString("ISO-8859-1");
							// logger.info("MENSAJE XML:"+item.getString("ISO-8859-1"));
							// System.out.println("CServletDB.doPost mensajeXML="+item.getString("ISO-8859-1"));
						}
					}
				}
				if (stream != null && !(stream.trim().equals(""))) {
					StringReader sw = new StringReader(stream);
					try {
						ps = (GEOPISTAPreparedStatement) Unmarshaller
								.unmarshal(GEOPISTAPreparedStatement.class, sw);
						
						//Realizamos una adaptacion para ExportImportUtils.java. Determinadas SQL son muy largas y provocan 
						//problemas con el unmarshall. Parece un error del castor (Ver ejemplo del main en la parte inferior)
						if (ps.getQueryId().startsWith("##")){
							String sql=CServletDB.getSql(stream);
							ps.setQueryId(sql.substring(2));
						}
							
					} catch (Exception e) {
						commitPS = (PoolPS) Unmarshaller.unmarshal(
								PoolPS.class, new StringReader(stream));
					}
				} else {
					throw new Exception("El stream es null");
				}
			}
			if (ps != null)
				connection = execute(ps, connection, psSQL, rsSQL, rsRet,
						response, request);
			else
				connection = execute(commitPS, connection, psSQL, rsSQL, rsRet,
						response, request);
			// System.out.println("Escribe servlet numero:" + numeroServlet +
			// " MULTIPART con PS: "+ps.toString());
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (Exception ex) {
			}
			;
			logger.error("Error al ejecutar la query:" + e);
		} finally {
			try {
				rsSQL.close();
			} catch (Exception e) {
			}
			;
			try {
				psSQL.close();
			} catch (Exception e) {
			}
			;
			try {
				if (connection != null) {
					if (!connection.isClosed())
						CPoolDatabase.releaseConexion();
					connection.close();
				}
			}

			catch (Exception e) {
				e.printStackTrace();
			}
			;
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
	            	  if (sStatement.equals("getEntidadesSortedByIdEntidadNotAssigned"))
	  	            	return null;
	  	            
	            	
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
				//logger.info("Ejecutando query Filled: "+((org.enhydra.jdbc.standard.StandardXAPreparedStatement)psSQL).ps.toString());
				logger.info("Ejecutando query Filled: "+psSQL.toString());
				System.out.println("Executing query Filled:"+psSQL.toString());
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
			HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		try {
			new InitialContext();
			connection = CPoolDatabase.getConnection();
			String oldIdQuery = "";
			String sStatement = "";

			// autocommit a false para que guarde en el pool
			connection.setAutoCommit(false);

			int numQueries = alPs.getPool().size();
			// se recorren las queries para ver si todas tienen permiso
			for (Iterator it = alPs.iterator(); it.hasNext();) {
				GEOPISTAPreparedStatement ps = (GEOPISTAPreparedStatement) it
						.next();
				logger.info("Buscando Query: " + ps.getQueryId());
				if (oldIdQuery == null || !oldIdQuery.equals(ps.getQueryId())) {
					oldIdQuery = ps.getQueryId();
					psSQL = connection
							.prepareStatement("select query_catalog.query, query_catalog.acl, usrgrouperm.def from query_catalog, usrgrouperm where usrgrouperm.idperm = query_catalog.idperm and query_catalog.id=?");
					psSQL.setString(1, ps.getQueryId());
					rsSQL = psSQL.executeQuery();
					rsSQL.next();
					sStatement = rsSQL.getString("query");
					long iAcl = rsSQL.getLong("acl");
					String perm = rsSQL.getString("def");
					logger.info("Ejecutando query: " + sStatement);
					rsSQL.close();
					psSQL.close();

					if (!checkPermission(sesion(request), iAcl, perm,
							CPoolDatabase.getConnection())) {
						throw new PermissionException(
								"No puede ejecutar la operación "
										+ ps.getQueryId()
										+ " sin tener el permiso" + " " + perm);
					}
				}
			}

			int ejecutados = 0;
			for (Iterator it = alPs.iterator(); it.hasNext();) {
				GEOPISTAPreparedStatement ps = (GEOPISTAPreparedStatement) it
						.next();
				logger.info("Buscando Query: " + ps.getQueryId());
				if (oldIdQuery == null || !oldIdQuery.equals(ps.getQueryId())) {
					oldIdQuery = ps.getQueryId();
					psSQL = connection
							.prepareStatement("select query_catalog.query, query_catalog.acl, usrgrouperm.def from query_catalog, usrgrouperm where usrgrouperm.idperm = query_catalog.idperm and query_catalog.id=?");
					psSQL.setString(1, ps.getQueryId());
					rsSQL = psSQL.executeQuery();
					rsSQL.next();
					sStatement = rsSQL.getString("query");
					long iAcl = rsSQL.getLong("acl");
					String perm = rsSQL.getString("def");
					logger.info("Ejecutando query: " + sStatement);
					rsSQL.close();
					psSQL.close();

					// if
					// (!checkPermission(sesion(request),iAcl,perm,CPoolDatabase.getConnection()))
					// continue;

				}

				psSQL = connection.prepareStatement(sStatement);
				Object[] oData = (Object[]) (new ObjectInputStream(
						new ByteArrayInputStream(ps.getParams())).readObject());
				for (int i = 0; i < oData.length; i++)
					psSQL.setObject(i + 1, oData[i]);

				try // Se intenta ejecutar todas las sentencias
				{
					if (psSQL.execute())
						// REFACTORIZACION ORACLE rsRet=new
						// GEOPISTAResultSet(null,psSQL.getResultSet());
						rsRet = new GEOPISTAResultSet(null,
								psSQL.getResultSet(),
								!CPoolDatabase.isPostgres(connection));
					else {
						connection.commit();
						rsRet = new GEOPISTAResultSet(psSQL.getUpdateCount());
					}
					ejecutados++;
				} catch (Exception e) {
					e.printStackTrace();
				}
				;
			}

			// Si el número de queries ejecutadas no coincide con las que se
			// pidieron, no se hace el commit
			// y se envía una excepción
			if (ejecutados != numQueries) {
				response.setHeader("Transfer-encoding", "chunked");
				response.setHeader("Content-Encoding", "gzip");

				OutputStream out = response.getOutputStream();
				ObjectOutputStream out1 = new ObjectOutputStream(
						new GZIPOutputStream(out));
				ACException ace = new ACException(
						"No se han podido ejecutar todas las queries del pool");
				out1.writeObject(ace);

				PrintWriter outpw = new PrintWriter(new GZIPOutputStream(out1),
						false);
				Marshaller.marshal(new Exception(), outpw);
				outpw.close();

				try {
					out1.flush();
					out1.close();
					out.flush();
				} catch (Exception excep) {
					excep.printStackTrace();
				}
				throw ace;
			} else {
				connection.commit();
				rsRet = new GEOPISTAResultSet(ejecutados);
				response.setHeader("Transfer-encoding", "chunked");
				response.setHeader("Content-Encoding", "gzip");
				OutputStream out1 = response.getOutputStream();
				PrintWriter out = new PrintWriter(new GZIPOutputStream(out1),
						false);
				Marshaller.marshal(rsRet, out);
				out.close();
			}

			return connection;
		} catch (Exception e) {

			response.setHeader("Transfer-encoding", "chunked");
			response.setHeader("Content-Encoding", "gzip");

			OutputStream out = response.getOutputStream();
			ObjectOutputStream out1 = new ObjectOutputStream(
					new GZIPOutputStream(out));
			out1.writeObject(new ACException(e));

			PrintWriter outpw = new PrintWriter(new GZIPOutputStream(out1),
					false);
			Marshaller.marshal(new Exception(), outpw);
			outpw.close();

			try {
				out1.flush();
				out1.close();
				out.flush();
			} catch (Exception excep) {
				excep.printStackTrace();
			}
			try {
				connection.close();
				CPoolDatabase.releaseConexion();
				// connection.rollback();
			} catch (Exception ex) {
			}
			;

			throw e;

			/*
			 * e.printStackTrace();
			 * 
			 * try{connection.rollback();}catch(Exception ex){};
			 * try{rsSQL.close();}catch(Exception ex){};
			 * try{psSQL.close();}catch(Exception ex){};
			 * try{connection.close();CPoolDatabase
			 * .releaseConexion();}catch(Exception ex){}; throw e;
			 */
		}
	}

	private ISesion sesion(HttpServletRequest request) {
		ISesion sRet = null;
		JAASUserPrincipal userPrincipal = (JAASUserPrincipal) request
				.getUserPrincipal();
		if (userPrincipal != null) {
			String sIdSesion = userPrincipal.getName();

			PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared
					.getContextShared().getSharedAttribute(
							this.getServletContext(), "UserSessions");

			sRet = PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
		}
		return sRet;
	}

	private boolean checkPermission(ISesion sSesion, long lACL, String sPerm,
			Connection con) throws Exception {
		logger.debug("Buscando el acl:" + lACL);
		GeopistaAcl acl = SesionUtils_LCGIII.getPerfil(sSesion, lACL, con);
		if (acl == null)
			return false;
		return acl.checkPermission(new GeopistaPermission(sPerm));
	}
	
	public static String getSql(String xml) throws Exception{
		
		//ByteArrayInputStream inputstream=new ByteArrayInputStream(xml.getBytes("ISO-8859-1"));
		ByteArrayInputStream inputstream=new ByteArrayInputStream(xml.getBytes("UTF-8"));
		SAXBuilder builder = new SAXBuilder(false);
		Document docNew = builder.build(inputstream);
		Element rootElement = docNew.getRootElement();
		Element elemento = (Element)rootElement.getChild("query-id");		
		return elemento.getText();
		
	}

	public static void main(String args[]){
		try {
			//Este String provoca que que el Unmarshaller falle, el sql se carga incorrectamte. Para evitar problemas en lugar
			//de utilizar el UnMarhaller utilizamos la lectura de sax.
			String stream="<?xml version=\"1.0\" encoding=\"UTF-8\"?><GEOPISTAPreparedStatement max-rows=\"0\" result-set-concurrency=\"1007\" closed=\"false\" fetch-direction=\"1000\" update-count=\"-1\" max-field-size=\"0\" fetch-size=\"-1\" poolable=\"false\" query-timeout=\"0\" result-set-type=\"1004\"><connection closed=\"false\" auto-commit=\"true\" read-only=\"false\" xsi:type=\"java:com.geopista.sql.GEOPISTAConnection\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/><query-id>insert into \"mdl_IGI\" (\"id\", \"id_municipio\", \"GEOMETRY\", \"codigo\", \"nombre\", \"sup_oficial\", \"superficie\", \"per_oficial\", \"perimetro\", \"declaracion\", \"observaciones\", \"tipo\", \"estilo\", \"GEOMETRYold50\") values (nextval('seq_mdl_igi'), 33001, GeometryFromText('MULTIPOLYGON(((-5.62039675262 43.51917812194,-5.62041076267 43.51916103099,-5.62039424615 43.51916600198,-5.62039037685 43.51914808262,-5.62042261038 43.51908881683,-5.62047048026 43.51900668214,-5.62047935828 43.51898847034,-5.62050849172 43.51890226341,-5.62053219769 43.51876215666,-5.62052327757 43.51866331667,-5.62049213187 43.51852446822,-5.62046329752 43.5183675586,-5.62041883045 43.51824252185,-5.62036335629 43.51812223948,-5.62034557154 43.51807762716,-5.62032873163 43.51797446673,-5.62034166376 43.51795165997,-5.62037564952 43.51793287222,-5.62056280778 43.51795108903,-5.62088672061 43.51800218412,-5.62100048693 43.51802208407,-5.62101922287 43.51802615621,-5.62105881777 43.51802974967,-5.62110420681 43.51803321023,-5.62114708671 43.51804123042,-5.621184945 43.51804936579,-5.62134274685 43.51806825484,-5.62155579293 43.51810388401,-5.62159500162 43.51810748628,-5.62165024243 43.51811522254,-5.6217116637 43.51812281699,-5.62199250335 43.51816139142,-5.62233959484 43.51821195038,-5.62252753046 43.51823915015,-5.62277843291 43.51827390793,-5.62287597303 43.5182851744,-5.62295401163 43.51831039469,-5.62296101249 43.51832258797,-5.62304310699 43.51833493385,-5.623038337 43.51836475645,-5.62235976296 43.51858392086,-5.62171007719 43.51879313986,-5.62081449584 43.51908146795,-5.62071523716 43.51910085666,-5.62069143135 43.51910663201,-5.62067831554 43.51910981397,-5.62039675262 43.51917812194)),((-5.62147685868 43.5204057279,-5.62147321921 43.52040453861,-5.62146414516 43.52041383975,-5.62140024016 43.52038964467,-5.62131611871 43.5203500664,-5.62114182716 43.52027122849,-5.62099200575 43.52020884643,-5.62088963075 43.52016294252,-5.62086877623 43.52015906813,-5.62084965328 43.52015500489,-5.62081453569 43.52012433784,-5.62080955125 43.52012164152,-5.62076276198 43.52008408779,-5.62072727911 43.52004841365,-5.62068678162 43.52001917865,-5.62066089048 43.51999276052,-5.62045181153 43.51972743517,-5.62042745608 43.5196739695,-5.62032155578 43.5195143257,-5.62029753884 43.51946054961,-5.62029597176 43.5194596503,-5.62025301812 43.51941921719,-5.62021282314 43.5193754328,-5.62020964196 43.51937196751,-5.62018315486 43.51934311519,-5.62023603865 43.51932713592,-5.62023772681 43.51932662467,-5.620214082 43.51927368131,-5.62029924952 43.51924921763,-5.6204713241 43.51920475214,-5.620654211 43.51915103417,-5.62083806178 43.51909279181,-5.62105763815 43.51902022344,-5.62129614171 43.51894722022,-5.62149717894 43.51888408036,-5.62169223009 43.51882557969,-5.62192397051 43.51874822832,-5.62199349394 43.51872862442,-5.62216054059 43.51867526774,-5.62231233066 43.51862676303,-5.62244678382 43.51869570893,-5.62246842608 43.51872222424,-5.62247866779 43.5187354952,-5.62248832831 43.5187442775,-5.6224995339 43.51875302426,-5.62255072483 43.51877435915,-5.62261775674 43.51880433442,-5.62265175649 43.51882156193,-5.62265771749 43.51882494197,-5.622680638 43.51883643864,-5.62270472788 43.51884957826,-5.62271765484 43.51885841,-5.62272185267 43.51886199098,-5.62272756135 43.5188651047,-5.62273371473 43.51886716978,-5.62274326178 43.51887037375,-5.62274538198 43.51887119194,-5.62275568723 43.51887320015,-5.62276727914 43.51888193801,-5.62276951507 43.51888453547,-5.62277219931 43.51888682522,-5.62277666018 43.51889283609,-5.62277867979 43.51889518235,-5.62278199221 43.51890104133,-5.62278224033 43.51890142854,-5.62278249335 43.51890192776,-5.62278873026 43.5189129597,-5.62279955687 43.51893972331,-5.62281232202 43.51898445052,-5.62281793206 43.51900683184,-5.62282624248 43.51902014714,-5.62283957462 43.51903334705,-5.62284769008 43.51904216477,-5.62285754567 43.51905544464,-5.62286933265 43.51906868008,-5.62303858172 43.51920885879,-5.62303677617 43.51921023028,-5.62303711339 43.51921050918,-5.62293928571 43.5192842848,-5.6227574507 43.51942240719,-5.62271189428 43.5194864816,-5.62269509626 43.51950037336,-5.62265879623 43.51952821893,-5.62264312814 43.51954011519,-5.62263915922 43.519543708,-5.6226384833 43.51954432784,-5.62262385001 43.51956053546,-5.62261407142 43.51956920478,-5.6226132921 43.51957022134,-5.62260937322 43.51958337804,-5.62259856079 43.51959263031,-5.62258770359 43.51959644445,-5.62258352095 43.51959861521,-5.62258087685 43.51959985391,-5.62256940167 43.51960680589,-5.6225527949 43.51961619117,-5.62253889225 43.51962551439,-5.62252846604 43.51963475785,-5.62252461033 43.51965285445,-5.62252210481 43.51966641805,-5.62251477245 43.51968459452,-5.62250608638 43.51969830003,-5.62250049053 43.51971193459,-5.6225078406 43.51973877806,-5.62251190548 43.51976119481,-5.62251577548 43.51977911407,-5.62251465718 43.51978254383,-5.62251476562 43.51978504573,-5.62249730749 43.51983575431,-5.62247584269 43.51990158568,-5.62236175671 43.52006177581,-5.62226504954 43.52021256275,-5.62213954761 43.52032349236,-5.62213873628 43.52032387409,-5.62213827687 43.52032428202,-5.62209235225 43.52034569789,-5.6220288948 43.52037555461,-5.62202824848 43.52037559102,-5.62202761206 43.52037588785,-5.62202452844 43.52037709965,-5.62189308965 43.52038353979,-5.62173002445 43.52038385922,-5.62170717818 43.52039060619,-5.62166018954 43.52040652736,-5.62161521918 43.52041478898,-5.6216029038 43.5204175854,-5.62159673658 43.52042000911,-5.62154965635 43.52042311055,-5.62154357391 43.52042351127,-5.62149757833 43.52041190919,-5.62149099443 43.52041041035,-5.62147685868 43.5204057279)))', 4258), '0', 'CARBAYERA DE EL TRAGAMÓN', null, null, null, null, null, null, null, 3, GeometryFromText('MULTIPOLYGON(((-5.6189767476195 43.5202620686715,-5.61899075728899 43.5202449786718,-5.61897424135579 43.5202499491775,-5.61897037248024 43.5202320304828,-5.61900260553044 43.520172767657,-5.61905047449614 43.5200906371839,-5.61905935237295 43.5200724263014,-5.61908848577552 43.5199862234367,-5.61911219266199 43.5198461228236,-5.61910327425808 43.5197472866827,-5.61907213176133 43.5196084433801,-5.61904330077344 43.5194515397013,-5.61899883725745 43.5193265072582,-5.61894336707325 43.5192062289323,-5.61892558366112 43.519161618108,-5.61890874586064 43.5190584616209,-5.61892167781918 43.5190356559765,-5.6189556624195 43.5190168696129,-5.61914281260134 43.5190350888628,-5.61946671121401 43.5190861875116,-5.6195804724805 43.5191060885548,-5.61959920764632 43.5191101608514,-5.61963880081316 43.5191137549187,-5.6196841878726 43.5191172161289,-5.61972706585579 43.5191252366954,-5.61976492247851 43.5191333723801,-5.61992271748962 43.5191522634269,-5.62013575423026 43.519187894835,-5.62017496121158 43.519191497572,-5.62023019961112 43.519199234493,-5.62029161817752 43.5192068296542,-5.62057244555305 43.5192454073912,-5.62091952191333 43.5192959703004,-5.62110744933548 43.5193231721823,-5.6213583407804 43.5193579328837,-5.62145587669126 43.5193692006196,-5.62153391170692 43.5193944212433,-5.62154091205306 43.5194066141072,-5.62162300295111 43.5194189609416,-5.62161823281596 43.5194487821916,-5.6209396841065 43.5196679259191,-5.62029002261141 43.5198771250986,-5.6193944746708 43.5201654258694,-5.6192952199216 43.5201848120813,-5.61927141501821 43.52019058683,-5.6192582997398 43.5201937684173,-5.6189767476195 43.5202620686715)),((-5.62005679176802 43.5214896430905,-5.62005315254074 43.5214884538086,-5.62004407872098 43.5214977543988,-5.61998017667945 43.5214735591936,-5.61989605934857 43.5214339810808,-5.61972177613316 43.5213551433917,-5.61957196178435 43.521292761334,-5.6194695917001 43.5212468574821,-5.61944873805088 43.5212429829408,-5.61942961595359 43.5212389194933,-5.61939450025391 43.5212082530904,-5.61938951605719 43.5212055568306,-5.61934272926181 43.5211680038209,-5.61930724837082 43.5211323305483,-5.61926675302578 43.5211030960307,-5.6192408633468 43.5210766784771,-5.61903179667938 43.5208113603856,-5.61900744297074 43.5207578965055,-5.61890154925714 43.5205982574044,-5.61887753406727 43.520544483075,-5.61887596712861 43.5205435837832,-5.61883301578485 43.520503151556,-5.61879282311105 43.5204593683445,-5.61878964207298 43.5204559031308,-5.61876315649503 43.5204270514649,-5.61881603829189 43.5204110738037,-5.6188177264404 43.5204105625768,-5.61879408333302 43.5203576210404,-5.61887924756709 43.5203331597846,-5.61905131558014 43.5202886990518,-5.61923419564962 43.5202349865132,-5.61941803946244 43.520176749743,-5.61963760767748 43.5201041880668,-5.61987610233518 43.5200311919884,-5.62007713201033 43.519968058242,-5.62027217580658 43.5199095632627,-5.6205039076012 43.5198322191252,-5.62057342835651 43.5198126172397,-5.62074046875475 43.5197592655887,-5.62089225316873 43.5197107654929,-5.6210266998153 43.5197797108731,-5.62104834077706 43.519806225492,-5.62105858187025 43.5198194960912,-5.62106824186616 43.519828278176,-5.62107944691846 43.5198370248156,-5.62113063537482 43.5198583597376,-5.62119766410892 43.5198883349258,-5.62123166218979 43.5199055623297,-5.62123762291361 43.5199089423215,-5.62126054232195 43.5199204389217,-5.62128463099679 43.5199335784477,-5.62129755730841 43.5199424100031,-5.62130175493937 43.5199459908795,-5.62130746327536 43.5199491045618,-5.62131361643489 43.5199511696822,-5.62132316302116 43.5199543737021,-5.62132528305267 43.5199551918986,-5.62133558786755 43.5199572001679,-5.62134717924619 43.5199659379176,-5.62134941502823 43.5199685353493,-5.62135209909538 43.5199708250447,-5.62135655969139 43.5199768356982,-5.62135857922505 43.5199791819012,-5.62136189137817 43.519985040682,-5.62136213953797 43.5199854278746,-5.62136239252575 43.5199859271488,-5.62136862901374 43.519996958705,-5.6213794547988 43.5200237213918,-5.62139221884441 43.5200684469831,-5.62139782834503 43.5200908275022,-5.62140613823961 43.5201041423773,-5.62141946958399 43.5201173420425,-5.62142758461992 43.5201261595429,-5.62143743958704 43.5201394389662,-5.62144922588505 43.5201526740768,-5.6216184660427 43.5202928499521,-5.62161666050113 43.5202942214124,-5.62161699774572 43.5202945003462,-5.62151917306215 43.5203682711678,-5.62133734377811 43.5205063848224,-5.62129178836602 43.52057045579,-5.62127499088216 43.5205843466933,-5.62123869201338 43.5206121905251,-5.62122302438261 43.5206240859505,-5.62121905556439 43.5206276785885,-5.62121837974487 43.5206282983944,-5.62120374677075 43.5206445051345,-5.62119396853996 43.5206531739013,-5.62119318916979 43.520654190435,-5.62118927029566 43.5206673464645,-5.62117845820925 43.5206765981961,-5.62116760135827 43.5206804120305,-5.62116341888428 43.5206825826492,-5.62116077486371 43.5206838211595,-5.62114930006805 43.5206907726506,-5.62113269393806 43.5207001572994,-5.6211187916855 43.5207094799168,-5.62110836584979 43.5207187227808,-5.62110451001079 43.5207368186106,-5.62110200439938 43.5207503816215,-5.62109467213287 43.5207685572026,-5.6210859862075 43.5207822619934,-5.62108039043445 43.5207958958939,-5.62108773982614 43.5208227383556,-5.62109180421888 43.5208451543332,-5.62109567376742 43.5208630729371,-5.62109455547816 43.5208665025136,-5.62109466385847 43.5208690042682,-5.62107720582484 43.5209197104912,-5.62105574099412 43.5209855387854,-5.62094165755101 43.5211457203537,-5.62084495240539 43.5212964994894,-5.62071945421541 43.5214074223935,-5.62071864288779 43.5214078041084,-5.62071818350371 43.5214082119796,-5.62067226048121 43.5214296261753,-5.62060880530542 43.5214594806101,-5.62060815899511 43.5214595170483,-5.62060752258498 43.5214598138095,-5.62060443906744 43.5214610255485,-5.62047300574739 43.5214674631225,-5.62030994726078 43.52146777975,-5.62028710188171 43.521474526009,-5.62024011504971 43.5214904456601,-5.6201951464169 43.5214987061521,-5.6201828315435 43.5215015023118,-5.62017666448599 43.5215039257674,-5.62012958616426 43.5215070263273,-5.62012350400995 43.5215074268941,-5.6200775105483 43.5214958245159,-5.62007092693103 43.5214943255556,-5.62005679176802 43.5214896430905)))', 4258));</query-id><params>rO0ABXVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAA=</params></GEOPISTAPreparedStatement>";
			StringReader sw = new StringReader(stream);
			GEOPISTAPreparedStatement ps=(GEOPISTAPreparedStatement)Unmarshaller.unmarshal(GEOPISTAPreparedStatement.class,sw);
			int indiceErroneo=ps.getQueryId().lastIndexOf("GeometryFromText");
			System.out.println("PASO1:"+ps.getQueryId().substring(indiceErroneo,indiceErroneo+100));
			//PASO1:GeometryFromText('MULTIPOLYGON(((-5.618976747619543.5202620686715,-5.61899075728899 43.5202449786718
			//El resultado es esto que como se puede ver, junta las geometrias y hace que sean incorrectas.
			
			String sql=CServletDB.getSql(stream);
			ps.setQueryId(sql);
			indiceErroneo=ps.getQueryId().lastIndexOf("GeometryFromText");
			System.out.println("PASO1:"+ps.getQueryId().substring(indiceErroneo,indiceErroneo+100));
			
			
			//

		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
		} 	
		
	}


}
// get the compressed content
/*
 * byte[] compressedBytes = compressedContent.toByteArray(); ObjectOutputStream
 * oos=new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));
 * Marshaller.marshal(rsRet,new ByteArrayOutputStream(oos)); oos.close();
 * //Marshaller.marshal(rsRet,response.getWriter());
 */

