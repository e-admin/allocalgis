package com.geopista.utils.alfresco.manager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.Constants;

import com.geopista.utils.alfresco.config.AlfrescoPropertiesStore;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase con utilidades de Alfresco
 */
public class AlfrescoManagerUtils {

	/**
	 * Constantes
	 */
	private static final Store STORE = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
	private static final String SPACE_CODE = "_x0020_";
	private static final String PROP_ALFRESCO_ACTIVE = "alfresco.active";
	private static final String PROP_URL = "alfresco.url";
	private static final String PROP_ADMIN_NAME = "alfresco.adminName";
	private static final String PROP_ADMIN_PASS = "alfresco.adminPass";
	private static final String PROP_ROOT_PATH = "alfresco.rootPath";	
	public static final String NODE_PATH_DELIMITER = "/cm:";
	
	/**
	 * Recupera una propiedad de un nodo
	 * @param node: Nodo de Alfresco
	 * @param propertyName: Nombre de la propiedad
	 * @return String: El valor de la propiedad en el nodo
	 */
	public static String getPropertyFromNode(Node node, String propertyName){
		if(node != null){
			for(NamedValue namedValue : node.getProperties()){
				if(namedValue.getName().endsWith(propertyName))
					return namedValue.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Comprueba la existencia mediante comparación de un objeto en un array de objetos
	 * @param array: Array de objetos
	 * @param value: Objeto a comparar
	 * @return boolean: Existe una coincidencia
	 */
	public static boolean isInArray(Object [] array, Object value){
		if(array != null && value != null){
			for(Object arrayValue : array){
				if(arrayValue.equals(value))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Transforma el contenido de un fichero a un array de bytes
	 * @param file: Fichero a transformar
	 * @return byte []: Array de bytes
	 * @throws IOException
	 */
	public static byte [] stringToByteArray(File file) throws IOException
	{
		FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fis.read(bytes);
        
        return bytes;

	}
	
	/**
	 * Comprueba si el nodo dado es un directorio
	 * @param node: Nodo de Alfresco (ResultSetRowNode)
	 * @return boolean: Es un directorio
	 */
	public static boolean isDirectory(ResultSetRowNode node){
		return node.getType().equals(Constants.TYPE_FOLDER);
	}
	
	/**
	 * Comprueba si el nodo dado es un directorio
	 * @param node: Nodo de Alfresco (Node)
	 * @return boolean: Es un directorio
	 */
	public static boolean isDirectory(Node node){
		return node.getType().equals(Constants.TYPE_FOLDER);
	}
	
	/**
	 * Comprueba si el nodo dado es un fichero
	 * @param node: Nodo de Alfresco (ResultSetRowNode)
	 * @return boolean: Es un fichero
	 */
	public static boolean isFile(ResultSetRowNode node){
		return node.getType().equals(Constants.TYPE_CONTENT);
	}
	
	/**
	 * Comprueba si el nodo dado es un fichero
	 * @param node: Nodo de Alfresco (Node)
	 * @return boolean: Es un fichero
	 */
	public static boolean isFile(Node node){
		return node.getType().equals(Constants.TYPE_CONTENT);
	}
	
	/**
	 * Devuelve una referencia de Alfresco 
	 * @param key: Clave unívoca
	 * @return Reference: Referencia de Alfresco
	 */
	public static Reference getReference(AlfrescoKey key){
		if(key.getKeyType()==AlfrescoKey.KEY_UUID)
			return new Reference(STORE, key.getKey(), null);
		else if(key.getKeyType()==AlfrescoKey.KEY_PATH)
			return new Reference(STORE, null, key.getKey().replace(" ", SPACE_CODE));
		return null;
	}
		
	/**
	 * Devuelve un predicado dada una referencia
	 * @param reference: Referencia de Alfresco
	 * @return Predicate: Predicado de Alfresco
	 */
	public static Predicate getPredicateFromReference(Reference reference){
		return new Predicate(new Reference[]{reference}, STORE, null);
	}
	
	/**
	 * Devuelve un Predicado de Alfresco
	 * @param key: Clave unívoca
	 * @return Predicate: Predicado de Alfresco
	 */
	public static Predicate getPredicate(AlfrescoKey key){
		return new Predicate(new Reference[]{AlfrescoManagerUtils.getReference(key)}, STORE, null);
	}
	
	/**
	 * Devuelve la referencia padre de Alfresco dada una clave y un nombre
	 * @param parentkey: Clave unívoca
	 * @param childName: Nombre del hijo
	 * @return ParentReference: Referencia Padre de Alfresco
	 */
	public static ParentReference getParentReferenceFromParent(AlfrescoKey parentkey, String childName){
		if(parentkey.getKeyType()==AlfrescoKey.KEY_UUID)
			return new ParentReference(STORE, parentkey.getKey(), null, 
					Constants.ASSOC_CONTAINS, Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, childName));			
		else if(parentkey.getKeyType()==AlfrescoKey.KEY_PATH)	
			return new ParentReference(STORE, null, parentkey.getKey(), 
					Constants.ASSOC_CONTAINS, Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, childName));	
		return null;
	}
	
	/**
	 * Comprueba si el documento es de Alfresco
	 * @param uuid: Identificador unívoco del documento
	 * @param municipalityId: Identificador del municipio
	 * @return Boolean: Resultado de la comprobacion
	 */
	public static Boolean isAlfrescoUuid(String uuid, String municipalityId){
		return !uuid.startsWith(municipalityId + "_");
	}
	
	/**
	 * Recupera el valor de una propiedad
	 * @param propertyName: Nombre de la propiedad
	 * @return String: Valor de la propiedad
	 */
	private static String getProperties(String propertyName){
		return AlfrescoPropertiesStore.getAlfrescoPropertiesStore().getString(propertyName);
	}
	
	/**
	 * Recupera el valor de la url del api de Alfresco
	 * @return String: Valor de la url del api de Alfresco
	 */
	public static String getUrlProperty(){
		return getProperties(PROP_URL);
	}
	
	/**
	 * Recupera el valor de la activación de Alfresco como gestor documental
	 * @return String: Valor de la activación de Alfresco como gestor documental
	 */
	public static Boolean isAlfrescoActive(){
		return Boolean.valueOf(getProperties(PROP_ALFRESCO_ACTIVE));
	}
	
	/**
	 * Recupera el valor del nombre del usuario administrador
	 * @return String: Valor del nombre del usuario administrador
	 */
	public static String getAdminNameProperty(){
		return getProperties(PROP_ADMIN_NAME);
	}
	
	/**
	 * Recupera el valor de la contraseña del usuario administrador
	 * @return String: Valor de la contraseña del usuario administrador
	 */
	public static String getAdminPassProperty(){
		return getProperties(PROP_ADMIN_PASS);
	}
	
	/**
	 * Recupera el valor de la ruta raíz
	 * @return String: Valor de la ruta raíz
	 */
	public static String getRootPathProperty(){
		return getProperties(PROP_ROOT_PATH);
	}
	
}
