/**
 * AlfrescoRepositoryManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.implementations;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLAddAspect;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLDelete;
import org.alfresco.webservice.types.CMLMove;
import org.alfresco.webservice.types.CMLUpdate;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;

import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.interfaces.AlfrescoRepositoryManager;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase que gestiona el repositorio de directorios y documentos de Alfresco (los métodos requieren autenticación)
 */
public class AlfrescoRepositoryManagerImpl implements AlfrescoRepositoryManager {
	
	/**
     * Variables
     */
	private static AlfrescoRepositoryManagerImpl instance = new AlfrescoRepositoryManagerImpl();
	   
	/**
     * Constructor
     */
	protected AlfrescoRepositoryManagerImpl() {
	}
	
	/**
     * Solicita la instancia única de la clase (Singleton)
     * @return AlfrescoRepositoryManagerImpl
     */
	public static AlfrescoRepositoryManagerImpl getInstance(){
		return instance;
	}
	
	/**
	 * Solicita todos los nodos referenciados por la clave unívoca
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return Node []: Array de nodos de Alfresco
	 */
	public Node [] getAllNodes(AlfrescoKey key) throws RepositoryFault, RemoteException{
		return WebServiceFactory.getRepositoryService().get(AlfrescoManagerUtils.getPredicate(key));
	}
	
	/**
	 * Solicita el nodo referenciado por la clave unívoca
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return Node: Nodo de Alfresco
	 */
	public Node getNode(AlfrescoKey key) throws RepositoryFault, RemoteException{
		Node[] nodes = getAllNodes(key);
		if(nodes != null && nodes.length > 0)
			return nodes[0];
		return null;
	}	
	
	/**
	 * Solicita el resultado de la existencia de un nodo por clave unívoca y nombre
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @param name: Nombre del nodo
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return boolean: Resultado de la existencia de un nodo por clave unívoca y nombre
	 */
	public boolean existsNodeFromName(AlfrescoKey parentKey, String name) throws RepositoryFault, RemoteException{
		QueryResult queryResult = getChildNodes(parentKey);
		if (queryResult != null
				&& queryResult.getResultSet().getTotalRowCount() > 0) {
			ResultSetRow[] rows = queryResult.getResultSet().getRows();
			for (ResultSetRow row : rows) {
				ResultSetRowNode resultSetRowNode = row.getNode();
				Node[] noderesult = null;
				noderesult = getAllNodes(new AlfrescoKey(resultSetRowNode.getId(), AlfrescoKey.KEY_UUID));
				for (Node node : noderesult) {			
					if(AlfrescoManagerUtils.getPropertyFromNode(node, Constants.PROP_NAME).equals(name))
						return true;
				}
			}
		}
		return false;
	}	
	
	/**
	 * Solicita un resultado de consulta con los nodos hijos de un directorio
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return QueryResult: QueryResult con los nodos hijos de un directorio
	 */
	public QueryResult getChildNodes(AlfrescoKey key) throws RepositoryFault, RemoteException {
		return WebServiceFactory.getRepositoryService().queryChildren(AlfrescoManagerUtils.getReference(key));
	}
	
	/**
	 * Solicita un resultado de consulta con los nodos padre de un nodo
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return QueryResult: QueryResult con los nodos padre de un nodo
	 */
	public QueryResult getAllParentNode(AlfrescoKey key) throws RepositoryFault, RemoteException {
		return WebServiceFactory.getRepositoryService().queryParents(AlfrescoManagerUtils.getReference(key));		
	}
	
	/**
	 * Solicita el nodo referenciado por la clave unívoca
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return Node: Nodo de Alfresco
	 */
	public Node getParentNode(AlfrescoKey key) throws RepositoryFault, RemoteException{
		QueryResult queryResult = getAllParentNode(key);
		if (queryResult != null
				&& queryResult.getResultSet().getTotalRowCount() > 0) {
			ResultSetRow[] rows = queryResult.getResultSet().getRows();
			for (ResultSetRow row : rows) {
				ResultSetRowNode resultSetRowNode = row.getNode();
				Node[] noderesult = null;
				noderesult = getAllNodes(new AlfrescoKey(resultSetRowNode.getId(), AlfrescoKey.KEY_UUID));
				for (Node node : noderesult) {			
					return node;
				}
			}
		}
		return null;
	}	
	
	/**
	 * Solicita el nodo referenciado por la clave unívoca
	 * @param key: Clave unívoca de un nodo de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return Node: Nodo de Alfresco
	 */
	public ArrayList<Node> getParentNodes(AlfrescoKey key) throws RepositoryFault, RemoteException{
		ArrayList<Node> parentNodes = new ArrayList<Node>();
		QueryResult queryResult = getAllParentNode(key);
		if (queryResult != null
				&& queryResult.getResultSet().getTotalRowCount() > 0) {
			ResultSetRow[] rows = queryResult.getResultSet().getRows();
			for (ResultSetRow row : rows) {
				ResultSetRowNode resultSetRowNode = row.getNode();
				Node[] noderesult = null;
				noderesult = getAllNodes(new AlfrescoKey(resultSetRowNode.getId(), AlfrescoKey.KEY_UUID));
				for (Node node : noderesult) {			
					parentNodes.add(node);
				}
			}
		}
		return parentNodes;
	}	
	
	/**
	 * Crea un nodo dentro de un nodo padre
	 * @param parentKey: Clave unívoca de un nodo padre de Alfresco
	 * @param childName: Nombre del nodo hijo a crear
	 * @param type: Tipo de nodo (TYPE_FOLDER (directorio) o TYPE_CONTENT (documento))  
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return Node: Nodo añadido
	 */
	public Node addFromParent(AlfrescoKey parentKey, String childName, String type) throws RepositoryFault, RemoteException{
		ParentReference parentReference = AlfrescoManagerUtils.getParentReferenceFromParent(parentKey, childName);
		NamedValue[] properties = new NamedValue[] { Utils.createNamedValue(Constants.PROP_NAME, childName) };
		String id=String.valueOf(System.currentTimeMillis());
		CMLCreate cmlCreate = new CMLCreate(id, parentReference, null, null, null, type,
				properties);
		CML cml = new CML();
		cml.setCreate(new CMLCreate[] { cmlCreate });
		UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cml);
		Reference refrenceResult = result[0].getDestination();
		return getNode(new AlfrescoKey(refrenceResult.getPath(),AlfrescoKey.KEY_PATH));
	}
	
	/**
	 * Añade un directorio a un nodo padre
	 * @param parentKey: Clave unívoca de un nodo padre de Alfresco
	 * @param childName: Nombre del directorio hijo a crear
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return Node: Directorio añadido
	 */
	public Node addDirectoryFromParent(AlfrescoKey parentKey, String directoryName) throws RepositoryFault, RemoteException{
		return addFromParent(parentKey, directoryName, Constants.TYPE_FOLDER);	
	}
	
	/**
	 * Añade un documento a un nodo padre
	 * @param parentKey: Clave unívoca de un nodo padre de Alfresco
	 * @param childName: Nombre del documento hijo a crear
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return Node: Documento añadido
	 */
	public Node addFileFromParent(AlfrescoKey parentKey, String childName) throws RepositoryFault, RemoteException{
		return addFromParent(parentKey, childName, Constants.TYPE_CONTENT);	
	}
	
	/**
	 * Asigna un documento la característica de versionado
	 * @param key: Clave unívoca del documento de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 */
	public void setVersionable(AlfrescoKey key) throws RepositoryFault, RemoteException{
		CMLAddAspect addAspect = new CMLAddAspect(Constants.ASPECT_VERSIONABLE, null, AlfrescoManagerUtils.getPredicate(key), null);
		CML cml = new CML();
		cml.setAddAspect(new CMLAddAspect[] { addAspect });
		WebServiceFactory.getRepositoryService().update(cml);
	}
	
	/**
	 * Mueve un nodo de Alfresco a un nuevo directorio
	 * @param parentReference: Referencia al directorio padre
	 * @param key: Clave unívoca del documento de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return boolean: Resultado del movimiento del nodo de Alfresco
	 */
	public boolean moveNode(ParentReference parentReference, AlfrescoKey key) throws RepositoryFault, RemoteException{
		CMLMove cmlMove = new CMLMove();
		cmlMove.setTo(parentReference);
		cmlMove.setWhere(AlfrescoManagerUtils.getPredicate(key));		
		CMLUpdate cmlUpdate = new CMLUpdate();
		//NamedValue[] properties = new NamedValue[]{Utils.createNamedValue(Constants.PROP_NAME, ISO9075.encode(parentReference.getChildName().replace("{" +Constants.NAMESPACE_CONTENT_MODEL + "}"), ""))};
		NamedValue[] properties = new NamedValue[]{Utils.createNamedValue(Constants.PROP_NAME, parentReference.getChildName().replace("{" +Constants.NAMESPACE_CONTENT_MODEL + "}", ""))};
		cmlUpdate.setProperty(properties);
		cmlUpdate.setWhere(AlfrescoManagerUtils.getPredicate(key));
		CML cml = new CML();		
        cml.setMove(new CMLMove[]{cmlMove});
        cml.setUpdate(new CMLUpdate[]{cmlUpdate});		
        UpdateResult [] result = WebServiceFactory.getRepositoryService().update(cml);
        if(result != null && result.length>0)
        	return true;
        return false;
	}
	
	/**
	 * Mueve un nodo de Alfresco a un nuevo directorio
	 * @param newParentPath: Ruta del nuevo directorio padre
	 * @param key: Clave unívoca del documento de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return boolean: Resultado del movimiento del nodo de Alfresco
	 */
	public boolean moveNode(AlfrescoKey newParentKey, AlfrescoKey key) throws RepositoryFault, RemoteException{
        return moveNode(AlfrescoManagerUtils.getParentReferenceFromParent(newParentKey, AlfrescoManagerUtils.getPropertyFromNode(getNode(key),Constants.PROP_NAME)), key);
	}
	
	/**
	 * Renombra un nodo
	 * @param key: Clave unívoca del nodo
	 * @param newName: Nuevo nombre del nodo de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return boolean: Resultado del renombrado del nodo de Alfresco
	 */
	public boolean renameNode(AlfrescoKey key, String newName) throws RepositoryFault, RemoteException{
		return moveNode(AlfrescoManagerUtils.getParentReferenceFromParent(new AlfrescoKey(getAllParentNode(key).getResultSet().getRows(0).getNode().getId(),AlfrescoKey.KEY_UUID), newName), key);
	}
	
	/**
	 * Elimina un nodo de Alfresco
	 * @param key: Clave unívoca del documento de Alfresco
	 * @throws RepositoryFault
	 * @throws RemoteException
	 * @return boolean: Resultado de la eliminiación del nodo de Alfresco
	 */
	public boolean removeNode(AlfrescoKey key) throws RepositoryFault, RemoteException{
		CMLDelete delete = new CMLDelete();
		delete.setWhere(AlfrescoManagerUtils.getPredicate(key));
		CML cml = new CML();
		cml.setDelete(new CMLDelete[] { delete });
        UpdateResult [] result = WebServiceFactory.getRepositoryService().update(cml);
        if(result != null && result.length>0)
        	return true;
        return false;
	}
	
}
