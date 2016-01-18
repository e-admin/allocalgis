/**
 * AlfrescoDocumentConnection.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.alfresco;

import java.io.File;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.rpc.ServiceException;

import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.ISO9075;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.eclipse.jetty.util.security.Password;

import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Lote;
import com.geopista.server.database.COperacionesAdministrador;
import com.geopista.server.database.COperacionesMunicipios;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.utils.alfresco.beans.DocumentBean;
import com.geopista.utils.alfresco.beans.FeatureBean;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.beans.AlfrescoNode;
import com.geopista.utils.alfresco.manager.implementations.AlfrescoManagerImpl;
import com.geopista.utils.alfresco.manager.interfaces.AlfrescoAccessControlManager;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase con las operaciones de Alfresco
 */
public class AlfrescoDocumentConnection{
	
	/**
	 * Logger
	 */
    private static org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(AlfrescoDocumentConnection.class);
    
    /**
     * Constructor
     */
    public AlfrescoDocumentConnection(){    	
    }
    
    /**
     * Devuelve el resultado de la inicialización de la ruta relativa de directorios
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param idSession: Identificador de la sesión activa del usuario autenticado en LocalGIS
     * @param idMunicipality: Identificador de municipio
     * @param appName: Nombre de la aplicación
     * @throws Exception
     */
    public void initializeRelativeDirectoryPathAndAccess(ObjectOutputStream oos, JAASUserPrincipal jaasUserPrincipal, String idMunicipality, String appName) throws Exception{ 	
    	try{	
    		Node rootNode = initializeRelativeDirectoryPathAndAcces(jaasUserPrincipal, idMunicipality, appName);
            oos.writeObject((Node) rootNode);            
        }catch(Exception e){
            logger.error("initializeRelativeDirectoryPathAndAccess: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }

    /**
     * Devuelve el nodo raíz resultado de la inicialización de la ruta relativa de directorios
     * @param idSession: Identificador de la sesión activa del usuario autenticado en LocalGIS
     * @param idMunicipality: Identificador de municipio
     * @param appName: Nombre de la aplicación
     * @return Node: Devuelve el nodo raíz 
     * @throws Exception
     */
	public static Node initializeRelativeDirectoryPathAndAcces(JAASUserPrincipal jaasUserPrincipal, String idMunicipality, String appName) throws Exception{
		Node rootNode = null;	 
		try{   
	        	String municipalityName = COperacionesMunicipios.getMunicipalityById(idMunicipality);
	        	if(municipalityName != null){
	        		AlfrescoKey rootKey = new AlfrescoKey(AlfrescoManagerUtils.getRootPathProperty(), AlfrescoKey.KEY_PATH);	  
	        		AlfrescoKey rootMunicipalityKey = new AlfrescoKey(AlfrescoManagerUtils.getRootPathProperty() + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(municipalityName), AlfrescoKey.KEY_PATH);
	        		AlfrescoKey rootAppKey = new AlfrescoKey(AlfrescoManagerUtils.getRootPathProperty() + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(municipalityName) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(appName), AlfrescoKey.KEY_PATH);
	        		if(jaasUserPrincipal != null){   
	        			AlfrescoManagerImpl.getInstance().setAdminCredential();
	        			//Se comprueba la existencia del usuario autenticado en LocalGIS y si no existe se crea
	        			String username =  jaasUserPrincipal.getSubject().getPrincipals().iterator().next().getName();
	        			Password password = new Password(COperacionesAdministrador.getUserPassword(username).getDescripcion());
		        		if(username != null && password != null && AlfrescoManagerImpl.getInstance().addUser(username, password.toString()) != null){
			        		//Se comprueba la existencia de la carpeta correspondiente al municipio y si no existe se crea
		        			boolean existsNode = AlfrescoManagerImpl.getInstance().existsNode(rootMunicipalityKey);
			        		if(!existsNode){
			        			AlfrescoManagerImpl.getInstance().addDirectoryFromParent(rootKey, municipalityName);
			        			//Se elimina del directorio la herencia de permisos del padre
			        			AlfrescoManagerImpl.getInstance().setInheritPermission(rootMunicipalityKey, false);	        
			        			existsNode = true;
			        		}
			        		
			        		if(existsNode){        			
			            		if(AlfrescoManagerImpl.getInstance().addAccessToUser(rootMunicipalityKey, username, AlfrescoAccessControlManager.ACE_PERMISSION_CONSUMER)){
					            	//Se comprueba la existencia de la carpeta correspondiente a la aplicación documental y si no existe se crea
			            			if(!AlfrescoManagerImpl.getInstance().existsNode(rootAppKey)){
			            				AlfrescoManagerImpl.getInstance().addDirectoryFromParent(rootMunicipalityKey, appName);			            				
			            			}
			            			if(AlfrescoManagerImpl.getInstance().addAllAccessToUser(rootAppKey, username)){
			            				rootNode = AlfrescoManagerImpl.getInstance().getNode(rootAppKey);
			            			}
			            		}
							} 	
			        		canUserAuth(username, password.toString());
		        		}
	        		}
	        	}	    
		 }
	     catch(RepositoryFault ex){
	    	 logger.error("Exception:",ex);
	     }
		 catch(Exception ex){
			 throw ex;
		 }
	     return rootNode;
	}

    /**
     * Devuelve un node de Alfresco
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param key: Clave unívoca de un node de Alfresco
     * @throws Exception
     */
    public void getNode(ObjectOutputStream oos, AlfrescoKey key) throws Exception{     	
        try{           
            oos.writeObject((Node) AlfrescoManagerImpl.getInstance().getNode(key));
        }catch(RepositoryFault e){        	  
              oos.writeObject(null);
        }catch(Exception e){
            logger.error("getNode: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }
    
    /**
     * Devuelve el árbol de directorios hijos del nodo padre incluido él mismo 
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param parentNode: Nodo padre (directorio)
     * @throws Exception
     */
    public void getTreeDirectories(ObjectOutputStream oos, DefaultMutableTreeNode parentNode) throws Exception{     	
        try{           
        	getTreeDirectories(parentNode);
            oos.writeObject((DefaultMutableTreeNode) parentNode);            
        }catch(Exception e){
            logger.error("getTreeDirectories: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }
        
    /**
     * Devuelve un nodo padre de un nodo referenciado por la clave unívoca
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param key: Clave unívoca de un nodo de Alfresco
     * @throws Exception
     */
    public void getParentNode(ObjectOutputStream oos, AlfrescoKey key) throws Exception{     	
        try{           
            oos.writeObject((Node) AlfrescoManagerImpl.getInstance().getParentNode(key));
        }catch(RepositoryFault e){        	  
              oos.writeObject(null);
        }catch(Exception e){
            logger.error("getParentNode: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }    
    
    /**
     * Devuelve un listado de nodos padre de un nodo referenciado por la clave unívoca
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param key: Clave unívoca de un nodo de Alfresco
     * @throws Exception
     */
    public void getParentNodes(ObjectOutputStream oos, AlfrescoKey key) throws Exception{     	
        try{           
            oos.writeObject((ArrayList<Node>) AlfrescoManagerImpl.getInstance().getParentNodes(key));
        }catch(RepositoryFault e){        	  
              oos.writeObject(null);
        }catch(Exception e){
            logger.error("getParentNodes: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }    
    
    
    /**
     * Devuelve el modelo de tabla de los documentos contenidos en el nodo padre
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param parentKey: Clave unívoca de Alfresco del nodo padre
     * @param element: Elementos LocalGIS seleccionados
     * @throws Exception
     */
    public void getChildFiles(ObjectOutputStream oos, AlfrescoKey parentKey, Object [] elements) throws Exception{     	
        try{     
            oos.writeObject((DefaultTableModel) getChildFiles(parentKey, elements));            
        }catch(Exception e){
            logger.error("getChildFiles: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }    

    /**
     * Añade un documento a un directorio padre
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param key: Clave unívoca del nodo de alfresco
     * @param file: Documento a añadir
     * @throws Exception
     */
    public void addFileFromParent(ObjectOutputStream oos, AlfrescoKey key, DocumentBean file, String fileName) throws Exception{   
        try{     
            oos.writeObject((Node) AlfrescoManagerImpl.getInstance().addFileFromParent(key, file, fileName));            
        }catch(Exception e){
            logger.error("addFileFromParent: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }
    
    /**
     * Devuelve un documento
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param key: Clave unívoca del nodo de alfresco
     * @param name: Nombre del documento
     * @throws Exception
     */
    public void returnFile(ObjectOutputStream oos, AlfrescoKey key) throws Exception{       	
        try{     
        	//String tempFilePath = System.getProperty("java.io.tmpdir") + AlfrescoUtils.getPropertyFromNode(AlfrescoManagerImpl.getInstance().getNode(key), Constants.PROP_NAME);        	
        	//String tempFilePath = System.getProperty("java.io.tmpdir") + AlfrescoManagerUtils.getPropertyFromNode(AlfrescoManagerImpl.getInstance().getNode(key), Constants.PROP_NAME) + "_" + (new SimpleDateFormat("yyyy_MM_dd hh.mm")).format(new Date());        	
        	String tempFilePath = AlfrescoManagerUtils.getTempDirProperty() + AlfrescoManagerUtils.getPropertyFromNode(AlfrescoManagerImpl.getInstance().getNode(key), Constants.PROP_NAME) + "_" + (new SimpleDateFormat("yyyy_MM_dd hh.mm")).format(new Date());        	
        	logger.info("AlfrescoDocumentConnection - returnFile: " + tempFilePath);   
        	if(AlfrescoManagerImpl.getInstance().downloadFile(key, tempFilePath)){
        		File file = new File(tempFilePath);
        		DocumentBean document = new DocumentBean();		
        		document.setFileName(file.getName());
                document.setContent(AlfrescoManagerUtils.readFile(file));
                MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
                document.setMimeType(mimeTypesMap.getContentType(file));  
        		oos.writeObject(document);            
        	}
        }catch(Exception e){
            logger.error("downloadFile: ", e);            
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }
    
    /**
     * Añade un directorio a un directorio padre
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param key: Clave unívoca del nodo de alfresco
     * @param directoryName: Nombre del directorio a añadir
     * @throws Exception
     */
    public void addDirectoryFromParent(ObjectOutputStream oos, AlfrescoKey key, String directoryName) throws Exception{   
        try{     
            oos.writeObject((Node) AlfrescoManagerImpl.getInstance().addDirectoryFromParent(key, directoryName));            
        }catch(Exception e){
            logger.error("addDirectoryFromParent: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }
    
    /**
     * Crea una versión de un documento
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @param content: Array de bytes con el contenido del documento
	 * @param content: Formato del documento
     * @param directoryName: Nombre del directorio a añadir
     * @throws Exception
     */
    public void addVersion(ObjectOutputStream oos, AlfrescoKey key, byte [] content, String comments) throws Exception{   
        try{     
            oos.writeObject((Node) AlfrescoManagerImpl.getInstance().addVersion(key, content, comments));            
        }catch(Exception e){
            logger.error("addVersion: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    } 
    
    /**
     * Cambia la contraseña de un usuario
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param userName: Nombre del usuario
     * @param newPassword: Nueva contraseña del usuario
     * @throws Exception
     */
    public void setPassword(ObjectOutputStream oos, String userName, String newPassword) throws Exception{   
        try{     
            oos.writeObject((Boolean) AlfrescoManagerImpl.getInstance().setPassword(userName, newPassword));            
        }catch(Exception e){
            logger.error("setPassword: ", e);
            oos.writeObject(false);
            //oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    /**
     * Mueve un nodo de Alfresco a un nuevo directorio
	 * @param newParentKey: Clave unívoca del nuevo directorio padre
	 * @param key: Clave unívoca del nodo de Alfresco a mover
	 * @throws Exception
     */
    public void moveNode(ObjectOutputStream oos, AlfrescoKey newParentKey, AlfrescoKey key) throws Exception{   
        try{     
            oos.writeObject((Boolean) AlfrescoManagerImpl.getInstance().moveNode(newParentKey, key));            
        }catch(Exception e){
            logger.error("moveNode: ", e);
            oos.writeObject(false);
            //oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    /**
     * Renombra un nodo
	 * @param key: Clave unívoca del nodo
	 * @param newName: Nuevo nombre del nodo de Alfresco
	 * @throws Exception
     */
    public void renameNode(ObjectOutputStream oos, AlfrescoKey key, String newName) throws Exception{   
        try{     
            oos.writeObject((Boolean) AlfrescoManagerImpl.getInstance().renameNode(key, newName));            
        }catch(Exception e){
            logger.error("moveNode: ", e);
            oos.writeObject(false);
            //oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    /**
     * Elimina un nodo
	 * @param key: Clave unívoca del nodo
	 * @throws Exception
     */
    public void removeNode(ObjectOutputStream oos, AlfrescoKey key) throws Exception{   
        try{     
            oos.writeObject((Boolean) AlfrescoManagerImpl.getInstance().removeNode(key));            
        }catch(Exception e){
            logger.error("moveNode: ", e);
            oos.writeObject(false);
            //oos.writeObject(new ACException(e));
            throw e;
        }
    }

    /**
     * Actualiza el identificador de un documento 
	 * @param oldUuid: Clave unívoca del documento
	 * @param newUuid: Nueva clave unívoca del documento
	 * @throws Exception
     */
    public void updateDocumentUuid(ObjectOutputStream oos, String oldUuid, String newUuid) throws Exception{   
    	Connection conn = null; 
    	boolean transaction = false;

    	String uSqlD = "UPDATE DOCUMENTO SET ID_DOCUMENTO=? WHERE ID_DOCUMENTO=?";
        String uSqlAF = "UPDATE ANEXOFEATURE SET ID_DOCUMENTO=? WHERE ID_DOCUMENTO=?";

        PreparedStatement ps = null;
		try {			
            if (conn == null){
                conn = CPoolDatabase.getConnection();
                conn.setAutoCommit(false);
            }

            ps = conn.prepareStatement(uSqlD);
            ps.setString(1, new String(newUuid)); 
            ps.setString(2, new String(oldUuid)); 
            if (!transaction) ps.executeUpdate();
            conn.commit();
            ps.close();
            
            ps = conn.prepareStatement(uSqlAF);
            ps.setString(1, new String(newUuid));  
            ps.setString(2, new String(oldUuid)); 
            if (!transaction) ps.executeUpdate();
            conn.commit();
            ps.close();
                        
            oos.writeObject(true);   
		}catch(Exception e){
            logger.error("updateDocumentUuid: ", e);
            oos.writeObject(false);
            throw e;
        }
		finally{
            try{ps.close();}catch(Exception e){};
            try{if (!transaction){
                conn.close();CPoolDatabase.releaseConexion();
            }}catch(Exception e){};
        }
    }    
    
    /**
     * Devuelve el nombre de un municipio
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param key: Clave unívoca del nodo de alfresco
     * @param file: Documento a añadir
     * @throws Exception
     */
    public void getMunicipalityName(ObjectOutputStream oos, String idMunicipality) throws Exception{   
        try{     
            oos.writeObject((String) COperacionesMunicipios.getMunicipalityById(idMunicipality));            
        }catch(Exception e){
            logger.error("getMuniciaplityName: ", e);
            oos.writeObject(null);
            //oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    /**
     * Devuelve el valor de una propiedad
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param propertyName: Nombre de la propiedad
     * @throws Exception
     */
    public void getProperty(ObjectOutputStream oos, String propertyName) throws Exception{   
        try{     
            oos.writeObject((String) AlfrescoManagerUtils.getProperties(propertyName));            
        }catch(Exception e){
            logger.error("getProperty: ", e);
            oos.writeObject(null);
            //oos.writeObject(new ACException(e));
            throw e;
        }
    }
                   
    /**
     * Comprueba que le usuario puede autenticarse correctamente
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param idSession: Identificador de la sesión activa del usuario autenticado en LocalGIS
     * @param idMunicipality: Identificador de municipio
     * @param appName: Nombre de la aplicación
     * @throws Exception
     */
    private static void canUserAuth(String userName, String password) throws Exception{  
	    for(int i=0; i<3; i++){
	    	try{
			    AlfrescoManagerImpl.getInstance().setUserCredential(userName, password);    	
			    AlfrescoManagerImpl.getInstance().existsNode(new AlfrescoKey(AlfrescoManagerUtils.getRootPathProperty(),AlfrescoKey.KEY_PATH));
			    return;
		    }catch(AuthenticationFault e){
		    	AlfrescoManagerImpl.getInstance().setAdminCredential();
		    	AlfrescoManagerImpl.getInstance().setPassword(userName, password);
		    	AlfrescoManagerImpl.getInstance().setUserCredential(userName, password);
		    }
	    }
	    throw new Exception("Error de autenticación del usuario");
    }
           
    /**
     * Solicita el árbol de directorios hijos del nodo padre incluido él mismo 
     * @param parentNode: Nodo padre (directorio)
     * @throws Exception
     */
    private void getTreeDirectories(DefaultMutableTreeNode parentNode) throws RemoteException, ServiceException{
	    if(parentNode != null && parentNode.getUserObject() != null){
			String uuid = ((AlfrescoNode) parentNode
					.getUserObject()).getUuid();
			QueryResult queryResult = AlfrescoManagerImpl.getInstance().getChildNodes(new AlfrescoKey(uuid, AlfrescoKey.KEY_UUID));
			if (queryResult != null
					&& queryResult.getResultSet().getTotalRowCount() > 0) {
				ResultSetRow[] rows = queryResult.getResultSet().getRows();
				for (ResultSetRow row : rows) {
					ResultSetRowNode resultSetRowNode = row.getNode();					
					if (AlfrescoManagerUtils.isDirectory(resultSetRowNode)) {	
						try {
							AlfrescoNode nodeDirectory = new AlfrescoNode(AlfrescoManagerUtils.getPropertyFromNode(row, Constants.PROP_NAME), resultSetRowNode.getId());
							DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
							childNode.setUserObject(nodeDirectory);
							parentNode.add(childNode);
							getTreeDirectories(childNode);						
						} catch (Exception ex1) {
							System.out
									.println("Error al obntener la referencia al contenido ");
						}
					}
				}
	
			}
		}
    }
    
    /**
     * Solicita el modelo de tabla con los documentos contenidos en el nodo padre
     * @param parentKey: Clave unívoca de Alfresco del nodo padre (directorio)
     * @param elements: Elementos LocalGIS seleccionados
     * @throws Exception
     * @return DefaultTableModel: Modelo de tabla con los documentos contenidos en el nodo padre
     */
    private DefaultTableModel getChildFiles(AlfrescoKey parentKey, Object [] elements) throws RemoteException, ServiceException{
    	QueryResult queryResult = AlfrescoManagerImpl.getInstance().getChildNodes(parentKey);		
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Nombre");
		tableModel.addColumn(" ");
		if (queryResult != null
				&& queryResult.getResultSet().getTotalRowCount() > 0) {				
			ResultSetRow[] rows = queryResult.getResultSet().getRows();
			for (ResultSetRow resultSetRow : rows) {

				ResultSetRowNode resultSetNode = resultSetRow.getNode();
			
				if (AlfrescoManagerUtils.isFile(resultSetNode)) {						
					AlfrescoNode nodeDirectory = new AlfrescoNode(AlfrescoManagerUtils.getPropertyFromNode(resultSetRow, Constants.PROP_NAME), resultSetNode.getId());
					tableModel.addRow(new Object[] { nodeDirectory , getAnnexationStatus(nodeDirectory.getUuid(), elements)});
				
				}
			}

		}	
		return tableModel;
    }
    
    /**
     * Devuelve el color de anexado relacionado con el anexo de un documento a la propia feature, a alguna o a ninguna
     * @param uuid: Clave unívoca de un nodo Alfresco
     * @param elements: Elementos LocalGIS seleccionados
     * @throws SQLException
     * @return Color: Color de anexado
     */
    private Integer getAnnexationStatus(String uuid, Object [] elements){
    	if(uuid != null && elements != null){
    		String sSqlOwnAnnexed = null;
    		String sSqlAnnexed = "SELECT d.id_documento FROM documento d WHERE d.id_documento='" + uuid + "'";
    		if(elements[0] instanceof FeatureBean){ //Geometrias
    			sSqlOwnAnnexed = "SELECT d.id_documento FROM documento d, anexofeature af WHERE d.id_documento='" + uuid + "' AND d.id_documento=af.id_documento AND af.id_layer='" + ((FeatureBean)elements[0]).getIdLayer() + "' AND af.id_feature='" + ((FeatureBean)elements[0]).getIdFeature() + "'";
    		}
    		else if(elements[0] instanceof BienBean){ //Bien Inventario
    			sSqlOwnAnnexed = "SELECT d.id_documento FROM documento d, anexo_inventario ai WHERE d.id_documento='" + uuid + "' AND d.id_documento=ai.id_documento AND ai.id_bien='" + ((BienBean)elements[0]).getId() + "'";
    		}
    		else if(elements[0] instanceof BienRevertible){
    			sSqlOwnAnnexed = "SELECT d.id_documento FROM documento d, anexo_bien_revertible abr WHERE d.id_documento='" + uuid + "' AND d.id_documento=abr.id_documento AND abr.id_bien_revertible='" + ((BienRevertible)elements[0]).getId() + "'";
    		}
    		else if(elements[0] instanceof Lote){
    			sSqlOwnAnnexed = "SELECT d.id_documento FROM documento d, anexo_lote al WHERE d.id_documento='" + uuid + "' AND d.id_documento=al.id_documento AND al.id_lote='" + ((Lote)elements[0]).getId_lote() + "'";
    		}
//    		else if(elements[0] instanceof CAnexo) //Licencias
//    			sSqlOwnAnnexed = "SELECT sl.id_solicitud FROM solicitud_licencia sl, anexo a WHERE sl.id_solicitud='" + uuid + "' AND sl.id_solicitud=a.id_solicitud AND a.id_anexo='" + ((CAnexo)elements[0]).getIdAnexo() + "'";
    		else if(elements[0] instanceof String || elements[0] instanceof Long){ //Licencias
    			String idSolicitud;
    			if(elements[0] instanceof Long){
    				idSolicitud = String.valueOf(elements[0]);
    			}
    			else{
    				idSolicitud = (String) elements[0];
    			}
    			sSqlOwnAnnexed = "SELECT sl.id_solicitud FROM solicitud_licencia sl, anexo a WHERE sl.id_solicitud='" + idSolicitud + "' AND sl.id_solicitud=a.id_solicitud AND a.id_anexo='" + uuid + "'";
    			sSqlAnnexed = "SELECT a.id_anexo FROM anexo a WHERE a.id_anexo='" + uuid + "'";      
    		}
    		else if(elements[0] instanceof ElemCementerioBean){ //Cementerio
    			//sSqlOwnAnnexed = "SELECT d.id_documento FROM cementerio.documento d, cementerio.anexo_feature a WHERE a.id_documento=d.id_documento AND d.id_documento=" + uuid + " AND d.id_documento=a.id_documento AND a.id_layer='" + ((ElemCementerioBean)elements[0]).getIdLayers()[0] + "' AND a.id_feature='" + ((ElemCementerioBean)elements[0]).getIdFeatures()[0] + "'";
    			sSqlOwnAnnexed = "SELECT d.id_documento FROM cementerio.documento d WHERE  d.id_documento='" + uuid + "'";
    			sSqlAnnexed = "SELECT cd.id_documento FROM cementerio.documento cd WHERE cd.id_documento='" + uuid + "')";
        	}

    		
		    Connection conn = null;
   		    PreparedStatement ps = null;
   		    ResultSet rs = null;
   		    try{
   		    	if(sSqlOwnAnnexed != null){
	   		        conn = CPoolDatabase.getConnection();
	  		        ps = conn.prepareStatement(sSqlOwnAnnexed);	       
	  		        rs = ps.executeQuery();	
	  		        while (rs.next())
	  		        	return 2;
	  		        	//return Color.green;  		        
	  		        conn.close();
	  		        ps.close();
	  		        rs.close();
	   		    }
   		    			   //AÑADIR OTROS cementerio.documento y solicitud_licencia
   		    	if(sSqlAnnexed != null){
	   		    	conn= CPoolDatabase.getConnection();
		   		    ps= conn.prepareStatement(sSqlAnnexed);	       
		   		    rs= ps.executeQuery();	
		   		    while (rs.next())
		   		    	return 1;
		   		    	//return Color.orange;   	
   		    	}
   		    	
   		    } catch (SQLException e) {
				System.out.println(e);			
   			}finally{
   		        try{rs.close();}catch(Exception e){};
   		        try{ps.close();}catch(Exception e){};
   		        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
   		    }
   	    }
    	return 0;
    	//return Color.red;
    }
    
}
