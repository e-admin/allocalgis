/**
 * SigmServlet.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.server.sigm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Unmarshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.app.sigm.SigmConstants;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.administradorCartografia.SystemMapException;
import com.localgis.server.sigm.SigmConnection;
import com.localgis.server.SessionsContextShared;
import com.localgis.web.core.model.Procedure;
import com.localgis.web.core.model.ProcedureDefaults;
import com.localgis.web.core.model.ProcedureProperty;
import com.localgis.web.core.model.ProcedurePropertyKey;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndValue;


/**
 * @author david.caaveiro
 * @company SATEC
 * @date 13-12-2012
 * @version 1.0
 * @ClassComments Servlet SiGM
 */
public class SigmServlet extends LoggerHttpServlet
{
  	
  	/**
	 * Logger
	 */
	private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(SigmServlet.class);
	
	/**
	 * Variables
	 */
    public int numeroServlet = 0;

    /**
     * doGet
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

	/**
	 * doPost
     * @throws ServletException
     * @throws IOException
	 */   
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	super.doPost(request);
    	logger.info("[SigmServlet : doPost ]: Entrando en servlet sigm");
        ObjectOutputStream oos=null;
        try{
            JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
            
            PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
	    	
            Sesion userSesion = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

            ACQuery query= new ACQuery();
            
            if(!org.apache.commons.fileupload.DiskFileUpload.isMultipartContent(request)){
                /* -- PostMethod --  */
                //System.out.println(request.getParameter("mensajeXML"));
                ACMessage abQuery= (ACMessage)Unmarshaller.unmarshal(ACMessage.class,new StringReader(request.getParameter("mensajeXML")));
                ObjectInputStream ois= new ObjectInputStream(new ByteArrayInputStream(abQuery.getQuery()));
                query= (ACQuery)ois.readObject();
            }else{
                /** -- MultiPartPost -- */
               // Create a new file upload handler
                org.apache.commons.fileupload.DiskFileUpload upload= new org.apache.commons.fileupload.DiskFileUpload();
                List items= upload.parseRequest(request);

                // Process the uploaded items
                Iterator iter= items.iterator();

                while (iter.hasNext()) {
                    org.apache.commons.fileupload.FileItem item= (org.apache.commons.fileupload.FileItem) iter.next();

                    if (item.isFormField()) {
                       if (item.getFieldName().equalsIgnoreCase("mensajeXML")){
                           ACMessage abQuery=(ACMessage)Unmarshaller.unmarshal(ACMessage.class,new StringReader(item.getString("ISO-8859-1")));
                           ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(abQuery.getQuery()));
                           query=(ACQuery)ois.readObject();

                           logger.debug("MENSAJE XML:"+item.getString("ISO-8859-1"));
                       }
                    }
                }
            }                   	

            SigmConnection sigmConnection = new SigmConnection();                
            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));

            Hashtable params = query.getParams();
            switch (query.getAction())
            {
                case SigmConstants.ACTION_GET_INFO_ALL:
                {
                	sigmConnection.getInfoAll(oos, SigmConstants.SIGM_URL, (Integer)params.get(SigmConstants.KEY_ENTITY_ID), (String)params.get(SigmConstants.KEY_LAYER_NAME), (String)params.get(SigmConstants.KEY_FEATURE_ID));
                    break;
                    
                }         
                case SigmConstants.ACTION_GET_SEARCH_ALL:
                {
                	sigmConnection.getSearchAll(oos, SigmConstants.SIGM_URL, (Integer)params.get(SigmConstants.KEY_ENTITY_ID), (String)params.get(SigmConstants.KEY_FEATURE_TYPE), (PropertyAndValue [])params.get(SigmConstants.KEY_SEARCH_PROPERTY_AND_VALUE));
                    break;
                    
                }     
                case SigmConstants.ACTION_GET_INFO_BY_PRIMARY_KEY:
                {
                	sigmConnection.getInfoByPrimaryKey(oos, SigmConstants.SIGM_URL, (Integer)params.get(SigmConstants.KEY_ENTITY_ID), (String)params.get(SigmConstants.KEY_LAYER_NAME), (String)params.get(SigmConstants.KEY_FEATURE_ID), (String)params.get(SigmConstants.KEY_PROPERTY));
                    break;
                    
                }    
                case SigmConstants.ACTION_GET_PROPERTY_AND_NAME_KEY:
                {
                	sigmConnection.getPropertyAndName(oos, (String)params.get(SigmConstants.KEY_LAYER_NAME));
                    break;                    
                }   
                case SigmConstants.ACTION_GET_ALL_PROCEDURES:
                {
                	sigmConnection.getAllProcedures(oos);
                    break;                    
                }   
                case SigmConstants.ACTION_INSERT_PROCEDURE:
                {
                	sigmConnection.insertProcedure(oos, (Procedure)params.get(SigmConstants.KEY_PROCEDURE));
                    break;                    
                }   
                case SigmConstants.ACTION_UPDATE_PROCEDURE:
                {
                	sigmConnection.updateProcedure(oos, (Procedure)params.get(SigmConstants.KEY_PROCEDURE));
                    break;                    
                }   
                case SigmConstants.ACTION_DELETE_PROCEDURE:
                {
                	sigmConnection.deleteProcedure(oos, (String)params.get(SigmConstants.KEY_PROCEDURE_ID));
                    break;                    
                }   
                case SigmConstants.ACTION_GET_PROCEDURE_DEFAULTS:
                {
                	sigmConnection.getProcedureDefaults(oos, (String)params.get(SigmConstants.KEY_PROCEDURE_ID));
                    break;                    
                }  
                case SigmConstants.ACTION_INSERT_PROCEDURE_DEFAULTS:
                {
                	sigmConnection.insertProcedureDefaults(oos, (ProcedureDefaults)params.get(SigmConstants.KEY_PROCEDURE_DEFAULTS));
                    break;                    
                }   
                case SigmConstants.ACTION_UPDATE_PROCEDURE_DEFAULTS:
                {
                	sigmConnection.updateProcedureDefaults(oos, (ProcedureDefaults)params.get(SigmConstants.KEY_PROCEDURE_DEFAULTS));
                    break;                    
                }   
                case SigmConstants.ACTION_DELETE_PROCEDURE_DEFAULTS:
                {
                	sigmConnection.deleteProcedureDefaults(oos, (String)params.get(SigmConstants.KEY_PROCEDURE_ID));
                    break;                    
                }   
                case SigmConstants.ACTION_GET_PROCEDURE_PROPERTIES:
                {
                	sigmConnection.getProcedureProperties(oos, (String)params.get(SigmConstants.KEY_PROCEDURE_ID));
                    break;                    
                } 
                case SigmConstants.ACTION_GET_PROCEDURE_PROPERTIES_MAP:
                {
                	sigmConnection.getProcedurePropertiesMap(oos, (String)params.get(SigmConstants.KEY_PROCEDURE_ID));
                    break;                    
                } 
                case SigmConstants.ACTION_INSERT_PROCEDURE_PROPERTY:
                {
                	sigmConnection.insertProcedureProperty(oos, (ProcedureProperty)params.get(SigmConstants.KEY_PROCEDURE_PROPERTY));
                    break;                    
                }   
                case SigmConstants.ACTION_UPDATE_PROCEDURE_PROPERTY:
                {
                	sigmConnection.updateProcedureProperty(oos, (ProcedureProperty)params.get(SigmConstants.KEY_PROCEDURE_PROPERTY));
                    break;                    
                }  
                case SigmConstants.ACTION_DELETE_PROCEDURE_PROPERTY:
                {
                	sigmConnection.deleteProcedureProperty(oos, (ProcedurePropertyKey)params.get(SigmConstants.KEY_PROCEDURE_PROPERTY_ID));
                    break;                    
                } 
                case SigmConstants.ACTION_INSERT_PROCEDURE_PROPERTIES:
                {
                	sigmConnection.insertProcedureProperties(oos, (HashMap<String,ProcedureProperty>)params.get(SigmConstants.KEY_PROCEDURE_PROPERTIES_MAP));
                    break;                    
                }   
                case SigmConstants.ACTION_UPDATE_PROCEDURE_PROPERTIES:
                {
                	sigmConnection.updateProcedureProperties(oos, (String)params.get(SigmConstants.KEY_PROPERTY_ID),  (HashMap<String,ProcedureProperty>)params.get(SigmConstants.KEY_PROCEDURE_PROPERTIES_MAP));
                    break;                    
                }  
                case SigmConstants.ACTION_DELETE_PROCEDURE_PROPERTIES:
                {
                	sigmConnection.deleteProcedureProperties(oos, (String)params.get(SigmConstants.KEY_PROCEDURE_ID));
                    break;                    
                }   
            }
        }
	    catch(PermissionException pe){
	        try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
	    }catch(SystemMapException pe){
	        try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
	    }/** MultiPartPost */catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException pe){
	        try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
	    }catch(Exception e){
	        e.printStackTrace();
	        log(e.getMessage());
	    }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        e.printStackTrace(pw);
	        try{oos.writeObject(new ACException(e));}catch(Exception ex){};
	    }finally{
	        try{oos.close();}catch(Exception e){};
	    }
//        catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException pe){
//            try{
//                oos.writeObject(new ACException(pe));
//            }
//            catch(Exception ex){};
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            log(e.getMessage());
//        }/** java.lang.OutOfMemoryError */
//        catch (java.lang.Error e){
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            try{
//                oos.writeObject(new ACException(e));
//            }
//            catch(Exception ex){};
//        }
//        finally{
//            try{oos.close();}catch(Exception e){};
//        }
    }   
    
}
