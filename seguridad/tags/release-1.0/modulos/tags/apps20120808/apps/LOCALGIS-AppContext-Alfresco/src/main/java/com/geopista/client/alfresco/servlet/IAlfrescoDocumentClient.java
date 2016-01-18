package com.geopista.client.alfresco.servlet;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.net.URLEncoder;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.alfresco.webservice.types.Node;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.exolab.castor.xml.Marshaller;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 */
public interface IAlfrescoDocumentClient {

	/**
	 * Constantes
	 */
    public static final String	MENSAJE_XML	= "mensajeXML";
     
    /**
     * Metodos abstractos
     */
    public abstract DefaultMutableTreeNode getTreeDirectories(DefaultMutableTreeNode rootNode) throws Exception;     
    public abstract DefaultTableModel getChildFiles(DefaultMutableTreeNode rootNode, Object [] elements) throws Exception;     
    public abstract Node initializeRelativeDirectoryPathAndAccess(String idMunicipality, String appName) throws Exception;
    public abstract Boolean setPassword(String userName, String newPassword) throws Exception;
    public abstract Boolean moveNode(AlfrescoKey newParentKey, AlfrescoKey key) throws Exception;
    public abstract Boolean renameNode(AlfrescoKey key, String newName) throws Exception;
    public abstract Boolean updateDocumentUuid(String oldUuid, String newUuid) throws Exception;
//    public abstract Boolean addGroup(String groupName) throws Exception;     
//    public abstract Boolean addUserToGroup(String groupName, String userName) throws Exception;
//    public abstract Boolean addAccessToGroup(AlfrescoKey key, String groupName, String accessRole) throws Exception;
    public abstract Node addVersion(AlfrescoKey key, byte[] content, String comments) throws Exception;
    public abstract Boolean downloadFile(AlfrescoKey key, String path, String name) throws Exception;
    public abstract Node addFileFromParent(AlfrescoKey key, File file, String fileName) throws Exception;         
    public abstract Node addDirectoryFromParent(AlfrescoKey key, String directoryName) throws Exception;
    public abstract Node getNode(AlfrescoKey key) throws Exception;
    public abstract Node getNode(int action, Hashtable<Integer, Object> params) throws Exception;
    public abstract String getMunicipalityName(String idMunicipality) throws Exception;
}
