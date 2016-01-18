package com.geopista.client.alfresco;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Constantes del Alfresco
 */
public class AlfrescoConstants {
	
	/**
	 * Constantes (Aplicaciones)
	 */
	public static final String APP_GENERAL = "General";
	public static final String APP_INVENTORY = "Inventario";
	public static final String APP_MAJORWORKLICENSE = "Licencia de Obra Mayor";
	public static final String APP_MINORWORKLICENSE = "Licencia de Obra Menor";
	public static final String APP_ACTIVITYLICENSE = "Licencia de Actividad";
	public static final String APP_NONQUALIFIEDACTIVITYLICENSE = "Licencia de Actividad No Cualificada";
	public static final String APP_OCUPATIONLICENSE = "Licencia de Ocupaciones";
	public static final String APP_CEMENTERY = "Cementerio";
	
	/**
	 * Constantes (Acciones del servlet)
	 */
	public static final int ACTION_GET_DOCUMENTS = 0;
	public static final int ACTION_ADD_GROUP = 1;
	public static final int ACTION_GET_TREE_DIRECTORIES = 2;
	public static final int ACTION_GET_CHILD_FILES = 3;
	public static final int ACTION_ADD_FILE_FROM_PARENT = 4;
	public static final int ACTION_RETURN_FILE = 5;
	public static final int ACTION_ADD_DIRECTORY_FROM_PARENT = 6;
	public static final int ACTION_GET_NODE = 7;
	public static final int ACTION_ADD_USER_TO_GROUP = 8;
	public static final int ACTION_ADD_ACCESS_TO_GROUP = 9;
	public static final int ACTION_ADD_VERSION = 10;
	public static final int ACTION_INITIALIZE_RELATIVE_DIRECTORY_PATH_AND_ACCESS = 11;
	public static final int ACTION_SET_USER_CREDENTIALS = 12;
	public static final int ACTION_GET_MUNICPALITY_NAME = 13;
	public static final int ACTION_SET_PASSWORD = 14;
	public static final int ACTION_MOVE_NODE = 15;
	public static final int	ACTION_UPDATE_DOCUMENT_UUID = 16;
	public static final int	ACTION_RENAME_NODE = 17;
	public static final int	ACTION_GET_PARENT_NODE = 18;
	public static final int	ACTION_GET_PARENT_NODES = 19;
	
	/**
	 * Constantes (Claves de los parámetros)
	 */
	public static final int KEY_NAME = 0;
	public static final int KEY_USER = 1;
	public static final int KEY_GROUP = 2;
	public static final int KEY_ACCESS_ROLE = 3;
	public static final int KEY_TREE_NODE = 4;
	public static final int KEY_ALFRESCOKEY = 5;
	public static final int KEY_FILE = 6;
	public static final int KEY_BYTE_CONTENT = 7;
	public static final int KEY_COMMENTS = 8;
	public static final int KEY_DIRECTORY = 9;
	public static final int KEY_ID_MUNICIPALITY = 10;
	public static final int KEY_APP = 11;
	public static final int KEY_ID_SESSION = 12;
	public static final int KEY_PASS = 13;
	public static final int KEY_PARENT_ALFRESCOKEY = 14;
	public static final int KEY_OLD_UUID = 15;
	public static final int KEY_NEW_UUID = 16;
	public static final int KEY_ELEMENT = 17;

}
