/**
 * AlfrescoDocumentServlet.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.alfresco;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.administradorCartografia.GeopistaConnection;
import com.geopista.server.administradorCartografia.NewSRID;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.administradorCartografia.SystemMapException;
import com.geopista.utils.alfresco.beans.DocumentBean;
import com.geopista.utils.alfresco.config.AlfrescoPropertiesStore;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.localgis.server.SessionsContextShared;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Servlet Alfresco
 */
public class AlfrescoDocumentServlet extends LoggerHttpServlet {
    
	/**
	 * Logger
	 */
	private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(AlfrescoDocumentServlet.class);
    
	/**
	 * Variables
	 */
	GeopistaConnection geoConn;
    private static NewSRID srid = null;
    
    public void init(){    	
    	SessionsContextShared.getContextShared().setSharedAttribute(this.getServletContext(),WebAppConstants.ALFRESCO_WEBAPP_NAME,
    			"ConfigAlfresco", AlfrescoPropertiesStore.getAlfrescoPropertiesStore().getMap());
    }
    

    /**
     * doGet
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

	/**
	 * doPost
     * @throws ServletException
     * @throws IOException
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	super.doPost(request);
        ObjectOutputStream oos=null;
        
        try{
            ACQuery query = new ACQuery();

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
            
            AlfrescoDocumentConnection geoConn = new AlfrescoDocumentConnection();
            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));
            //De aquí hay que obtener los parametros necesarios
            Hashtable params = query.getParams();
            switch (query.getAction()){
//		          case AlfrescoConstants.ACTION_ADD_GROUP:
//		        	  geoConn.addGroup(oos, (String)params.get(AlfrescoConstants.KEY_GROUP));
//		        	  break;
//		          case AlfrescoConstants.ACTION_ADD_USER_TO_GROUP:
//		        	  geoConn.addUserToGroup(oos, (String)params.get(AlfrescoConstants.KEY_GROUP),(String)params.get(AlfrescoConstants.KEY_USER));
//		        	  break;  
//		          case AlfrescoConstants.ACTION_ADD_ACCESS_TO_GROUP:
//		        	  geoConn.addAccessToGroup(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY),(String)params.get(AlfrescoConstants.KEY_GROUP), (String)params.get(AlfrescoConstants.KEY_ACCESS_ROLE));
//		        	  break;    
		          case AlfrescoConstants.ACTION_GET_NODE:
		        	  geoConn.getNode(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY));
		        	  break;      
		          case AlfrescoConstants.ACTION_GET_TREE_DIRECTORIES:
		        	  geoConn.getTreeDirectories(oos, (DefaultMutableTreeNode)params.get(AlfrescoConstants.KEY_TREE_NODE));
		        	  break;	
		          case AlfrescoConstants.ACTION_GET_CHILD_FILES:
		        	  geoConn.getChildFiles(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY), (Object []) params.get(AlfrescoConstants.KEY_ELEMENT));
		        	  break;	
		          case AlfrescoConstants.ACTION_ADD_FILE_FROM_PARENT:
		        	  geoConn.addFileFromParent(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY), (DocumentBean)params.get(AlfrescoConstants.KEY_FILE), (String)params.get(AlfrescoConstants.KEY_NAME));
		        	  break;
		          case AlfrescoConstants.ACTION_RETURN_FILE:
		        	  geoConn.returnFile(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY));
		        	  break;
		          case AlfrescoConstants.ACTION_ADD_DIRECTORY_FROM_PARENT:
		        	  geoConn.addDirectoryFromParent(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY), (String)params.get(AlfrescoConstants.KEY_DIRECTORY));
		        	  break;   
		          case AlfrescoConstants.ACTION_ADD_VERSION:
		        	  geoConn.addVersion(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY), (byte [])params.get(AlfrescoConstants.KEY_BYTE_CONTENT), (String)params.get(AlfrescoConstants.KEY_COMMENTS));
		        	  break;   
		          case AlfrescoConstants.ACTION_INITIALIZE_RELATIVE_DIRECTORY_PATH_AND_ACCESS:
		        	  geoConn.initializeRelativeDirectoryPathAndAccess(oos, (JAASUserPrincipal) request.getUserPrincipal(), (String)params.get(AlfrescoConstants.KEY_ID_MUNICIPALITY), (String)params.get(AlfrescoConstants.KEY_APP));
		        	  break;     		
		          case AlfrescoConstants.ACTION_GET_MUNICPALITY_NAME:
		        	  geoConn.getMunicipalityName(oos, (String)params.get(AlfrescoConstants.KEY_ID_MUNICIPALITY));
		        	  break;   
		          case AlfrescoConstants.ACTION_SET_PASSWORD:
		        	  geoConn.setPassword(oos, (String)params.get(AlfrescoConstants.KEY_USER), (String)params.get(AlfrescoConstants.KEY_PASS));
		        	  break;  
		          case AlfrescoConstants.ACTION_MOVE_NODE:
		        	  geoConn.moveNode(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_PARENT_ALFRESCOKEY), (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY));
		        	  break;  
		          case AlfrescoConstants.ACTION_UPDATE_DOCUMENT_UUID:
		        	  geoConn.updateDocumentUuid(oos, (String)params.get(AlfrescoConstants.KEY_OLD_UUID), (String)params.get(AlfrescoConstants.KEY_NEW_UUID));
		        	  break;  
		          case AlfrescoConstants.ACTION_RENAME_NODE:
		        	  geoConn.renameNode(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY), (String)params.get(AlfrescoConstants.KEY_NAME));
		        	  break;  		        	  
		          case AlfrescoConstants.ACTION_REMOVE_NODE:
		        	  geoConn.removeNode(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY));
		        	  break;  
		          case AlfrescoConstants.ACTION_GET_PARENT_NODE:
		        	  geoConn.getParentNode(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY));
		        	  break;  
		          case AlfrescoConstants.ACTION_GET_PARENT_NODES:
		        	  geoConn.getParentNodes(oos, (AlfrescoKey)params.get(AlfrescoConstants.KEY_ALFRESCOKEY));
		        	  break;  
		          case AlfrescoConstants.ACTION_GET_PROPERTY:
		        	  geoConn.getProperty(oos, (String)params.get(AlfrescoConstants.KEY_PROPERTY));
			          break;  
			      }
        }catch(PermissionException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }catch(SystemMapException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }/** MultiPartPost */catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }catch(Exception e){
            e.printStackTrace();
            log(e.getMessage());
        }/** java.lang.OutOfMemoryError */
        catch (java.lang.Throwable ex){
        	logger.error("Exception Throwable",ex);
        	ex.printStackTrace();
            try{oos.writeObject(new ACException(ex));}catch(Exception ex2){};
        }/*catch (java.lang.Error e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            try{oos.writeObject(new ACException(e));}catch(Exception ex){};
        }*/finally{
            try{oos.close();}catch(Exception e){};
        }
    }
    
}

