package com.geopista.server.alfresco;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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
import com.localgis.security.model.LocalgisJAASUserPrincipal;
import com.localgis.server.SessionsContextShared;

import org.eclipse.jetty.util.security.Password;

import admcarApp.PasarelaAdmcar;

import com.geopista.feature.GeopistaFeature;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Lote;
import com.geopista.server.database.CPoolDatabase;
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
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(AlfrescoDocumentConnection.class);
    
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
        	Node rootNode = null;
        	String municipalityName = getMunicipalityById(idMunicipality);
        	if(municipalityName != null){
        		AlfrescoKey rootKey = new AlfrescoKey(AlfrescoManagerUtils.getRootPathProperty(), AlfrescoKey.KEY_PATH);	  
        		AlfrescoKey rootMunicipalityKey = new AlfrescoKey(AlfrescoManagerUtils.getRootPathProperty() + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(municipalityName), AlfrescoKey.KEY_PATH);
        		AlfrescoKey rootAppKey = new AlfrescoKey(AlfrescoManagerUtils.getRootPathProperty() + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(municipalityName) + AlfrescoManagerUtils.NODE_PATH_DELIMITER + ISO9075.encode(appName), AlfrescoKey.KEY_PATH);
        		if(jaasUserPrincipal != null){   
        			AlfrescoManagerImpl.getInstance().setAdminCredential();
        			//Se comprueba la existencia del usuario autenticado en LocalGIS y si no existe se crea
        			String username = jaasUserPrincipal.getName();
        			Password password = (Password) jaasUserPrincipal.getSubject().getPrivateCredentials().iterator().next();
	        		if(username != null && password != null && AlfrescoManagerImpl.getInstance().addUser(username, password.toString()) != null){
		        		//Se comprueba la existencia de la carpeta correspondiente al municipio y si no existe se crea
		        		if(!AlfrescoManagerImpl.getInstance().existsNode(rootMunicipalityKey)){
		        			AlfrescoManagerImpl.getInstance().addDirectoryFromParent(rootKey, municipalityName);
		        			//Se elimina del directorio la herencia de permisos del padre
		        			AlfrescoManagerImpl.getInstance().setInheritPermission(rootMunicipalityKey, false);	        					
		        		}
		        		
		        		if(AlfrescoManagerImpl.getInstance().existsNode(rootMunicipalityKey)){        			
		            		//Se comprueba la existencia del grupo correspondiente al municipio y si no existe se crea
		        			//if(AlfrescoManagerImpl.getInstance().addGroup(municipalityName)){
		        				//Se comprueba si el usuario pertenece al grupo y si no se añade
		        				//if(AlfrescoManagerImpl.getInstance().addUserToGroup(municipalityName, session.getUserPrincipal().getName())){
		        					//Se añade el grupo a la carpeta con permiso total
					        		//if(AlfrescoManagerImpl.getInstance().addAccessToGroup(rootMunicipalityKey, municipalityName, AlfrescoAccessControlManager.ACE_PERMISSION_CONSUMER)){
		        					if(AlfrescoManagerImpl.getInstance().addAccessToUser(rootMunicipalityKey, username, AlfrescoAccessControlManager.ACE_PERMISSION_CONSUMER)){
				            			//Se comprueba la existencia de la carpeta correspondiente a la aplicación documental y si no existe se crea
						            	if(!AlfrescoManagerImpl.getInstance().existsNode(rootAppKey))
						        			AlfrescoManagerImpl.getInstance().addDirectoryFromParent(rootMunicipalityKey, appName);
				
						            	if(AlfrescoManagerImpl.getInstance().existsNode(rootAppKey)){
						            		//Se comprueba la existencia del grupo correspondiente al municipio y la aplicacion documental y si no existe se crea
						            	//	if(AlfrescoManagerImpl.getInstance().addGroup(municipalityName + "_" + appName)){
						            			//Se comprueba si el usuario pertenece al grupo y si no se añade
						            			//if(AlfrescoManagerImpl.getInstance().addUserToGroup(municipalityName + "_" + appName, session.getUserPrincipal().getName())){
									       			//Se añade el grupo a la carpeta con permiso total
							            			//if(AlfrescoManagerImpl.getInstance().addAllAccessToGroup(new AlfrescoKey(rootApp, AlfrescoKey.KEY_PATH), municipalityName + "_" + appName)){	            			
						            		if(AlfrescoManagerImpl.getInstance().addAllAccessToUser(rootAppKey, username)){
							            		rootNode = AlfrescoManagerImpl.getInstance().getNode(rootAppKey);	 
							            	}
						            	}
		        					}
						} 	
		        		canUserAuth(username, password.toString());
	        		}
        		}
        	}
            oos.writeObject((Node) rootNode);            
        }catch(Exception e){
            logger.error("initializeRelativeDirectoryPathAndAccess: ", e);
            oos.writeObject(null);
            //oos.writeObject(new AlfrescoException(e));
            throw e;
        }
    }
        
    //BORRAR
//    public void addGroup(ObjectOutputStream oos, String groupName) throws Exception{ 	
//        try{           
//            oos.writeObject((Boolean)AlfrescoManagerImpl.getInstance().addGroup(groupName));            
//        }catch(Exception e){
//            logger.error("addGroup: ", e);
//            oos.writeObject(new AlfrescoException(e));
//            throw e;
//        }
//    }
//    

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
     * Devuelve el modelo de tabla de los documentos contenidos en el nodo padre
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param parentNode: Nodo padre (directorio)
     * @param element: Elementos LocalGIS seleccionados
     * @throws Exception
     */
    public void getChildFiles(ObjectOutputStream oos, DefaultMutableTreeNode parentNode, Object [] elements) throws Exception{     	
        try{     
            oos.writeObject((DefaultTableModel) getChildFiles(parentNode, elements));            
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
    public void addFileFromParent(ObjectOutputStream oos, AlfrescoKey key, File file, String fileName) throws Exception{   
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
        	String tempFilePath = System.getProperty("java.io.tmpdir") + AlfrescoManagerUtils.getPropertyFromNode(AlfrescoManagerImpl.getInstance().getNode(key), Constants.PROP_NAME) + "_" + (new SimpleDateFormat("yyyy_MM_dd hh.mm")).format(new Date());        	
        	if(AlfrescoManagerImpl.getInstance().downloadFile(key, tempFilePath))
        		oos.writeObject(new File(tempFilePath));            
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
    
    
    //BORRAR - Llamada interna
//    public void addUserToGroup(ObjectOutputStream oos, String groupName, String userName) throws Exception{   
//        try{     
//            oos.writeObject((Boolean) AlfrescoManagerImpl.getInstance().addUserToGroup(groupName, userName));            
//        }catch(Exception e){
//            logger.error("addUserToGroup: ", e);
//            oos.writeObject(new AlfrescoException(e));
//            throw e;
//        }
//    } 
    
    //BORRAR - Llamada interna
//    public void addAccessToGroup(ObjectOutputStream oos, AlfrescoKey key, String groupName,
//			String accessRole) throws Exception{   
//        try{     
//            oos.writeObject((Boolean) AlfrescoManagerImpl.getInstance().addAccessToGroup(key, groupName, accessRole));            
//        }catch(Exception e){
//            logger.error("addAccessToGroup: ", e);
//            oos.writeObject(new AlfrescoException(e));
//            throw e;
//        }
//    } 
    
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
            ps.executeUpdate();
            if (!transaction) conn.commit();
            ps.close();
            
            ps = conn.prepareStatement(uSqlAF);
            ps.setString(1, new String(newUuid));  
            ps.setString(2, new String(oldUuid)); 
            ps.executeUpdate();
            if (!transaction) conn.commit();
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
            oos.writeObject((String) getMunicipalityById(idMunicipality));            
        }catch(Exception e){
            logger.error("getMuniciaplityName: ", e);
            oos.writeObject(null);
            //oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    /**
     * Devuelve el nombre del municipio coincidente con el identificador dado
     * @param idMunicipality: Identificador de Municipio     
     * @throws SQLException 
     */
    private String getMunicipalityById(String idMunicipality) throws SQLException{	    
	    if(idMunicipality!= null && isINE(idMunicipality)){
		    String sSQL = "SELECT nombreoficial FROM municipios WHERE id_provincia='" + idMunicipality.substring(0,2) + "' AND id_ine='" +  idMunicipality.substring(2,5) + "'";
		    Connection conn=null;
		    PreparedStatement ps= null;
		    ResultSet rs= null;
		    try{
		        conn= CPoolDatabase.getConnection();
		        ps= conn.prepareStatement(sSQL);	       
		        rs= ps.executeQuery();	
		        String municipalityName = null;
		        while (rs.next()){
		        	municipalityName = rs.getString("nombreoficial");
		        }	
		        return municipalityName;
			}finally{
		        try{rs.close();}catch(Exception e){};
		        try{ps.close();}catch(Exception e){};
		        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		    }
	    }
	    return null;
	}
           
    /**
     * Comprueba que le usuario puede autenticarse correctamente
     * @param oos: ObjectOutputStream de respuesta a la solicitud del cliente
     * @param idSession: Identificador de la sesión activa del usuario autenticado en LocalGIS
     * @param idMunicipality: Identificador de municipio
     * @param appName: Nombre de la aplicación
     * @throws Exception
     */
    private void canUserAuth(String userName, String password) throws Exception{  
	    for(int i=0; i<3; i++){
	    	try{
			    AlfrescoManagerImpl.getInstance().setUserCredential(userName, password);    	
			    AlfrescoManagerImpl.getInstance().existsNode(new AlfrescoKey("",AlfrescoKey.KEY_UUID));
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
     * Comprueba si la cadena pasada como parámetro contiene un código INE
     * @param idMunicipality: Identificador de municipio
     * @throws Exception
     */
    private boolean isINE(String idMunicipality){  
       try{  
    	  if(idMunicipality.length()==5){
	          Integer.parseInt(idMunicipality);  
	          return true;  
    	  }
       }  
       catch(Exception e){}  
       return false;  
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
				//PARA MOSTRAR ORDENADOS 
				ResultSetRow[] rows = queryResult.getResultSet().getRows();
				for (ResultSetRow row : rows) {
					ResultSetRowNode resultSetRowNode = row.getNode();
					
					if (AlfrescoManagerUtils.isDirectory(resultSetRowNode)) {
	
						// Comprobamos que podemos ver que el nodo hijo existe y
						// que el nombre coincida con el que estemos buscando.
						Node[] noderesult = null;
						try {
							noderesult = AlfrescoManagerImpl.getInstance()
									.getAllNodes(new AlfrescoKey(resultSetRowNode.getId(), AlfrescoKey.KEY_UUID));
							AlfrescoNode nodeDirectory = null;
							String propUuid = resultSetRowNode.getId();
							for (Node node : noderesult) {			
								String propName = AlfrescoManagerUtils
										.getPropertyFromNode(node,
												Constants.PROP_NAME);
											 if (propName != null) {
										nodeDirectory = new AlfrescoNode(
												propName, propUuid);
										DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
										childNode.setUserObject(nodeDirectory);
										parentNode.add(childNode);
										getTreeDirectories(childNode);
									}
							}
						} catch (Exception ex1) {
							System.out
									.println("Error al obntener la referencia al contenido ");
						}
					}
				}
	
			} else {
				System.out.println("The query returned no results");
	
			}
		}
    }
    
    /**
     * Solicita el modelo de tabla con los documentos contenidos en el nodo padre
     * @param parentNode: Nodo padre (directorio)
     * @param elements: Elementos LocalGIS seleccionados
     * @throws Exception
     * @return DefaultTableModel: Modelo de tabla con los documentos contenidos en el nodo padre
     */
    private DefaultTableModel getChildFiles(DefaultMutableTreeNode parentNode, Object [] elements) throws RemoteException, ServiceException{
    	QueryResult queryResult = null;
		String uuid = ((AlfrescoNode) parentNode
				.getUserObject()).getUuid();
			queryResult = AlfrescoManagerImpl.getInstance().getChildNodes(new AlfrescoKey(uuid, AlfrescoKey.KEY_UUID));		
		DefaultTableModel tableModel = new DefaultTableModel();
		if (queryResult != null
				&& queryResult.getResultSet().getTotalRowCount() > 0) {				
			ResultSetRow[] rows = queryResult.getResultSet().getRows();
			for (ResultSetRow resultSetRow : rows) {

				ResultSetRowNode resultSetNode = resultSetRow.getNode();

				if (AlfrescoManagerUtils.isFile(resultSetNode)) {						
					if(tableModel.getColumnCount() == 0){
						tableModel.addColumn("Nombre");
						tableModel.addColumn("");
					}
					
					// Comprobamos que podemos ver que el nodo hijo existe y
					// que el nombre coincida con el que estemos buscando.
					Node[] noderesult = null;
					try {
						noderesult = AlfrescoManagerImpl.getInstance()
								.getAllNodes(new AlfrescoKey(resultSetNode.getId(), AlfrescoKey.KEY_UUID));
						AlfrescoNode nodeDirectory = null;
						
						String propUuid = resultSetNode.getId();						
						for (Node node : noderesult) {							
							String propName = AlfrescoManagerUtils
									.getPropertyFromNode(node,
											Constants.PROP_NAME);
							nodeDirectory = new AlfrescoNode(propName,
									propUuid);
							//PONER CONSULTA GENERAL O POR FICHERO A LA BD PARA SABER SI EXISTE ANEXO O NO
							tableModel
									.addRow(new Object[] { nodeDirectory , getAnnexionStatus(nodeDirectory.getUuid(), elements)});
						}
					} catch (Exception ex1) {
						System.out
								.println("Error al obntener la referencia al contenido ");
					}
				}
			}

		} else {
			System.out.println("The query returned no results");
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
    private Color getAnnexionStatus(String uuid, Object [] elements) throws SQLException{
    	if(uuid != null && elements != null){
    		String sSqlOwnAnnexed = null;
    		if(elements[0] instanceof FeatureBean)
    			sSqlOwnAnnexed = "SELECT d.id_documento FROM documento d, anexofeature af WHERE d.id_documento='" + uuid + "' AND d.id_documento=af.id_documento AND af.id_layer='" + ((FeatureBean)elements[0]).getIdLayer() + "' AND af.id_feature='" + ((FeatureBean)elements[0]).getIdFeature() + "'";
    		else if(elements[0] instanceof BienBean)
    			sSqlOwnAnnexed = "SELECT d.id_documento FROM documento d, anexo_inventario ai WHERE d.id_documento='" + uuid + "' AND d.id_documento=ai.id_documento AND ai.id_bien='" + ((BienBean)elements[0]).getId() + "'";
    		else if(elements[0] instanceof BienRevertible)
    			sSqlOwnAnnexed = "SELECT d.id_documento FROM documento d, anexo_bien_revertible abr WHERE d.id_documento='" + uuid + "' AND d.id_documento=abr.id_documento AND abr.id_bien_revertible='" + ((BienRevertible)elements[0]).getId() + "'";
    		else if(elements[0] instanceof Lote)
    			sSqlOwnAnnexed = "SELECT d.id_documento FROM documento d, anexo_lote al WHERE d.id_documento='" + uuid + "' AND d.id_documento=al.id_documento AND al.id_lote='" + ((Lote)elements[0]).getId_lote() + "'";
    		
    		String sSqlAnnexed = "SELECT id_documento FROM documento WHERE id_documento='" + uuid + "'";
		    Connection conn = null;
   		    PreparedStatement ps = null;
   		    ResultSet rs = null;
   		    try{
   		    	if(sSqlOwnAnnexed != null){
	   		        conn = CPoolDatabase.getConnection();
	  		        ps = conn.prepareStatement(sSqlOwnAnnexed);	       
	  		        rs = ps.executeQuery();	
	  		        while (rs.next())
	  		        	return Color.green;  		        
	  		        conn.close();
	  		        ps.close();
	  		        rs.close();
	   		    }
  		          		        
   		        conn= CPoolDatabase.getConnection();
   		        ps= conn.prepareStatement(sSqlAnnexed);	       
   		        rs= ps.executeQuery();	
   		        while (rs.next())
   		        	return Color.orange;   		        
   		        
   			}finally{
   		        try{rs.close();}catch(Exception e){};
   		        try{ps.close();}catch(Exception e){};
   		        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
   		    }
   	    }
    	return Color.red;
    }
    
}
