/**
 * DocumentClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import com.geopista.app.AppContext;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.IGeopistaLayer;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 24-abr-2006
 * Time: 14:33:00
 */

public class DocumentClient implements IDocumentClient {
    /**
	 * Logger for this class
	 */
	private static final Log logger= LogFactory.getLog(DocumentClient.class);

    private static String sUrl=null;

    public DocumentClient(String sUrl){
        this.sUrl=sUrl;
    }

    private static Object readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException,ACException{
       Object oRet=null;
       oRet=ois.readObject();
       if (oRet instanceof ACException){
           throw (ACException)oRet;
       }

       return oRet;
   }

    /** Devuelva todos los documentos (DocumentBean) del sistema que no son imagenes */
    public Collection getDocuments() throws Exception{
        return getDocuments(DocumentBean.DOC_CODE);
    }

    /** Devuelva todos los documentos (DocumentBean) del sistema que no son imagenes */
    public Collection getTexts() throws Exception{
           return getDocuments(DocumentBean.TEXT_CODE);
    }

    /** Devuelva todos los documentos (DocumentBean) del sistema que son imagenes */
    public Collection getImages() throws Exception{
        return getDocuments(DocumentBean.IMG_CODE);
    }
     /** Devuelva todos los documentos (DocumentBean) del sistema que son imagenes */
    public Collection get(char tipo) throws Exception{
        return getDocuments(tipo);
    }
    /** Devuelva todos los documentos (DocumentBean) del sistema */
    private Collection getDocuments(char is_imgdoctext) throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_DOCUMENTS);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_IS_IMGDOCTEXT, new String(""+is_imgdoctext));
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            DocumentBean documentBean= null;
            for(;;){
                documentBean= (DocumentBean)readObject(ois);
                cRet.add(documentBean);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getDocuments()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }

    /** 
     * Devuelve todos los documentos cuyo nombre y tamanio coincidan
     * 
     * @param is_imgdoctext
     * @return
     * @throws Exception
     */
    public Collection findDocuments(DocumentBean document) throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_FIND_DOCUMENTS);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            DocumentBean documentBean= null;
            for(;;){
                documentBean= (DocumentBean)readObject(ois);
                cRet.add(documentBean);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getDocuments()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }

    /** Devuelva un listado de Documentos (DocumentBean) asociados a una feature.
     *  Cada DocumentBean tendra todos los datos del documento almacenados en la
     *  base de datos excepto el contenido */
    public Collection getAttachedDocuments(GeopistaFeature feature) throws Exception{
        return getAttached(feature, DocumentBean.DOC_CODE);
    }

    public Collection getAttachedImages(GeopistaFeature feature) throws Exception{
        return getAttached(feature, DocumentBean.IMG_CODE);
    }

    public Collection getAttachedTexts(GeopistaFeature feature) throws Exception{
        return getAttached(feature, DocumentBean.TEXT_CODE);
    }
    /** Devuelva un listado de Documentos (DocumentBean) asociados a una feature.
     *  Cada DocumentBean tendra todos los datos del documento almacenados en la
     *  base de datos excepto el contenido */
    private Collection getAttached(GeopistaFeature feature, char is_imgdoctext) throws Exception{
    	    	
        Collection cRet=null;
        
        if (feature.getLayer()!= null && feature.getLayer() instanceof IGeopistaLayer && !((IGeopistaLayer)feature.getLayer()).isLocal()){

        	ACQuery query= new ACQuery();
        	query.setAction(Const.ACTION_GET_ATTACHED_DOCUMENTS);
        	Hashtable params= new Hashtable();
        	params.put(Const.KEY_ID_LAYER, feature.getLayer().getSystemId());
        	params.put(Const.KEY_ID_FEATURE, feature.getSystemId());
        	params.put(Const.KEY_IS_IMGDOCTEXT, new String(""+is_imgdoctext));
        	if (SecurityManager.getIdMunicipio()!=null)
            	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
           
        	query.setParams(params);
        	StringWriter swQuery= new StringWriter();
        	ByteArrayOutputStream baos= new ByteArrayOutputStream();
        	new ObjectOutputStream(baos).writeObject(query);
        	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        	InputStream in= enviarDocuments(sUrl, swQuery.toString());
        	ObjectInputStream ois= new ObjectInputStream(in);
        	cRet= new ArrayList();
        	try{
        		DocumentBean documentBean= null;
        		for(;;){
        			documentBean= (DocumentBean)readObject(ois);

        			cRet.add(documentBean);
        		}
        	}catch(OptionalDataException ode){
        		if (ode.eof!=true)
        			logger.error("getAttachedDocuments(GeopistaFeature feature)" + ode.getMessage(), ode);
        	}catch (EOFException ee){
        	}finally{
        		try{ois.close();}catch(Exception e){};
        	}
        }

        return cRet;
    }

    /** Devuelve el contenido del documento. */
    public byte[] getAttachedByteStream(DocumentBean document) throws Exception{
        byte[] content= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_ATTACHED_BYTESTREAM);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              content= (byte[])readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getAttachedByteStream(DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return content;
    }
    
    /**
     * Solicita un documento al servidor
	 * @param key: Clave unívoca de un nodo padre de Alfresco
	 * @throws Exception
     * @return DocumentBean: Contenido del documento solicitado
     */
	public com.geopista.utils.alfresco.beans.DocumentBean getAttachedByteStreamFromAlfresco(AlfrescoKey key) throws Exception {
		ACQuery query = new ACQuery();
		query.setAction(AlfrescoConstants.ACTION_RETURN_FILE);
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ALFRESCOKEY, key);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = enviarDocuments(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);		
		return ((com.geopista.utils.alfresco.beans.DocumentBean) readObject(ois));	
//		return new FileInputStream(
//				(File) readObject(ois));
	}
    
    public static void sendFile(File file, String path, String name) throws Exception{	
	    ACQuery query = new ACQuery();
		query.setAction(Const.ACTION_SEND_FILE);
		Hashtable params = new Hashtable();
		if(SecurityManager.getIdMunicipio() != null)
			params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
		if(SecurityManager.getIdEntidad() != null)
			params.put(Const.KEY_ID_ENTIDAD, SecurityManager.getIdEntidad());
		params.put(Const.KEY_PATH, path);
		params.put(Const.KEY_REPORT_NAME, name);
		params.put(Const.KEY_DOCUMENT_FILE, file.getName());	
		
		DocumentBean document=new DocumentBean();
		document.setContent(readFile(file));

		params.put(Const.KEY_DOCUMENT, document);	
		
		
		
		
		query.setParams(params);    	
		StringWriter swQuery= new StringWriter();
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in= enviarDocuments(sUrl, swQuery.toString());
		ObjectInputStream ois= new ObjectInputStream(in);
		try{
			ois.close();
		}catch(Exception e){};	
    }
    
    public ArrayList getDirectories() throws Exception{
    	ArrayList cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_DIR);
        Hashtable params= new Hashtable();
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
        if (SecurityManager.getIdEntidad()!=null)
        	params.put(Const.KEY_ID_ENTIDAD, SecurityManager.getIdEntidad());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            String dir = null;
            for(;;){
                dir = (String)readObject(ois);
                cRet.add(dir);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getDocuments()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }
    
    public ArrayList getFiles(String path) throws Exception{
    	ArrayList cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_FILES);
        Hashtable params= new Hashtable();
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
        if (SecurityManager.getIdEntidad()!=null)
        	params.put(Const.KEY_ID_ENTIDAD, SecurityManager.getIdEntidad());
        params.put(Const.KEY_PATH, path);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            String dir = null;
            for(;;){
                dir = (String)readObject(ois);
                cRet.add(dir);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getDocuments()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }
    
    public ArrayList getSubdirectories(String path) throws Exception{
    	ArrayList cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_SUBDIR);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_PATH, path);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
        if (SecurityManager.getIdEntidad()!=null)
        	params.put(Const.KEY_ID_ENTIDAD, SecurityManager.getIdEntidad());
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            String dir = null;
            for(;;){
                dir = (String)readObject(ois);
                cRet.add(dir);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getDocuments()" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return cRet;
     }
    
   /** Inserta el ByteInputStream/File del document en la BD y lo asocia a la lista de features */
    public DocumentBean attachDocument(Object[] features,  DocumentBean document) throws Exception{
        if (document != null){
            if (document.getContent() != null)
                return attachDocumentByteStream(features, document);
            if (document.getFileName() != null){
            	
            	if (!document.getFileName().startsWith("http")){
	                File file= new File(document.getFileName());
	                document.setFileName(file.getName());
	                return attachDocument(features, document, file);
            	}
            	else{
            		return attachDocument(features, document, null);
            	}
            }
        }
        throw new Exception("Documento no valido");
    }


    /** Inserta el File en la BD y lo asocia a la lista de features */
    public DocumentBean attachDocument(Object[] features, DocumentBean document, File f) throws Exception{
        DocumentBean doc= null;
        Object[] idFeatures= new Object[features.length];
        Object[] idLayers= new Object[features.length];
        for (int i=0;i<features.length;i++){
            GeopistaFeature feature= (GeopistaFeature)features[i];
            idFeatures[i]= (String)feature.getSystemId();
            idLayers[i]= (String)feature.getLayer().getSystemId();
        }
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_ATTACH_DOCUMENT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
        params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString(), f);
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              doc= (DocumentBean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("attachDocument(Object[] features, DocumentBean document, File f)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return doc;
     }


    /** Inserta el ByteInputStream del document en la BD y lo asocia a la lista de features */
    public DocumentBean attachDocumentByteStream(Object[] features,  DocumentBean document) throws Exception{
        DocumentBean doc= null;
        Object[] idFeatures= new Object[features.length];
        Object[] idLayers= new Object[features.length];
        for (int i=0;i<features.length;i++){
            GeopistaFeature feature= (GeopistaFeature)features[i];
            idFeatures[i]= (String)feature.getSystemId();
            idLayers[i]= (String)feature.getLayer().getSystemId();
        }
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_ATTACH_BYTESTREAM);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
        params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              doc= (DocumentBean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("attachDocument(Object[] features,  DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return doc;
     }


    /** Actualiza el ByteInputStream/File del document en la BD y lo asocia a la lista de features */
    public DocumentBean updateDocument(DocumentBean document) throws Exception{
        if (document != null){
            if (document.getContent() != null)
                return updateDocumentByteStream(document);
            if (document.getFileName() != null){
                File file= new File(document.getFileName());
                if (file.isAbsolute()){
                    /** modificamos el contenido del fichero */
                    document.setFileName(file.getName());
                    return updateDocument(document, file);
                }else return updateDocument(document, null);
            }
        }
        throw new Exception("Documento no valido");
    }
    

    /** Actualiza el File en la BD y lo asocia a la lista de features */
    public DocumentBean updateDocument(DocumentBean document, File f) throws Exception{
        DocumentBean doc= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_UPDATE_DOCUMENT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString(), f);
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              doc= (DocumentBean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("updateDocument(DocumentBean document, File f)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return doc;
     }


    /** Actualiza el ByteInputStream del document en la BD y lo asocia a la lista de features */
    public DocumentBean updateDocumentByteStream(DocumentBean document) throws Exception{
        DocumentBean doc= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_UPDATE_BYTESTREAM);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              doc= (DocumentBean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("updateDocument(DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return doc;
     }

     /** Desvincula un documento de una feature. Si es la última feature con la que está asociado
      * debe eliminar el documento de la base de datos.
      * Para poder desvincular un fichero de una feature esta no debe estar bloqueada. */
     public void detachDocument(GeopistaFeature feature, DocumentBean document) throws Exception{
         ACQuery query= new ACQuery();
         query.setAction(Const.ACTION_DETACH_DOCUMENT);
         Hashtable params= new Hashtable();
         params.put(Const.KEY_ID_LAYER, feature.getLayer().getSystemId());
         params.put(Const.KEY_ID_FEATURE, feature.getSystemId());
         params.put(Const.KEY_DOCUMENT, document);
         if (SecurityManager.getIdMunicipio()!=null)
         	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
        
         query.setParams(params);
         StringWriter swQuery= new StringWriter();
         ByteArrayOutputStream baos= new ByteArrayOutputStream();
         new ObjectOutputStream(baos).writeObject(query);
         Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
         InputStream in= enviarDocuments(sUrl, swQuery.toString());
         ObjectInputStream ois= new ObjectInputStream(in);
         try{
               readObject(ois);
         }catch(OptionalDataException ode){
             if (ode.eof!=true)
                 logger.error("detachDocument(GeopistaFeature feature, DocumentBean document)" + ode.getMessage(), ode);
         }catch (EOFException ee){
         }finally{
             try{ois.close();}catch(Exception e){};
         }
     }

    /** Asocia el documento a la lista de features */
    public void linkDocument(Vector features, DocumentBean document) throws Exception{
        Object[] idFeatures= new Object[features.size()];
        Object[] idLayers= new Object[features.size()];
        for (int i=0;i<features.size();i++){
            GeopistaFeature feature= (GeopistaFeature)features.get(i);
            idFeatures[i]= (String)feature.getSystemId();
            idLayers[i]= (String)feature.getLayer().getSystemId();
        }
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_LINK_DOCUMENT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ARRAYLIST_IDFEATURES, idFeatures);
        params.put(Const.KEY_ARRAYLIST_IDLAYERS, idLayers);
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("linkDocument(Object[] features, DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
     }

    public static InputStream enviarDocuments(String sUrl, String sMensaje) throws Exception{
          return enviar(sUrl, sMensaje, null, null, null);
    }

    public static InputStream enviarDocuments(String sUrl, String sMensaje, File f) throws Exception{
          return enviar(sUrl, sMensaje, null, null, f);
    }

    private static InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword, File f)
            throws Exception{
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else {
			if (com.geopista.security.SecurityManager.getIdSesion() == null){
				if (logger.isDebugEnabled()){
					logger.debug("enviar(String, String, String, String) - Usuario no autienticado");
				}
                creds = new UsernamePasswordCredentials("GUEST", "");
            }else
               creds = new UsernamePasswordCredentials(com.geopista.security.SecurityManager.getIdSesion(), SecurityManager.getIdSesion());
		}
		//create a singular HttpClient object
		org.apache.commons.httpclient.HttpClient client= AppContext.getHttpClient();

		//establish a connection within 5 seconds
		client.setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null) {
			client.getState().setCredentials(null, null, creds);
		}

        /* -- PostMethod --  */
        /*
		HttpMethod method = null;

		//create a method object
		method = new PostMethod(sUrl);
        if (SecurityManager.getIdMunicipio()!=null)
            ((PostMethod) method).addParameter(idMunicipio, SecurityManager.getIdMunicipio());
	    if (sMensaje!=null)
		    ((PostMethod) method).addParameter(mensajeXML, sMensaje);
        if (SecurityManager.getIdApp()!=null)
			((PostMethod) method).addParameter(IdApp, SecurityManager.getIdApp());
		method.setFollowRedirects(true);
        */
        /**/

        /* -- MultipartPostMethod -- */
        org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);

        if (SecurityManager.getIdMunicipio()!=null){
             method.addParameter(idMunicipio, SecurityManager.getIdMunicipio());
        }
        if (SecurityManager.getIdApp()!=null){
			 method.addParameter(IdApp, SecurityManager.getIdApp());
        }
        if (sMensaje!=null){
            //method.addParameter(mensajeXML, sMensaje);
            method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
        }

        /** Documento asociado a una []feature */
        if (f != null){
            /** Debido a que el nombre del fichero puede contener acentos. */
            method.addParameter(URLEncoder.encode(f.getName(),"ISO-8859-1"), f);
        }

        method.setFollowRedirects(false);

		//execute the method
		byte[] responseBody = null;
		try {
			client.executeMethod(method);
			responseBody = method.getResponseBody();
		} catch (HttpException he) {
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
			logger.error(
					"enviar(String, String, String, String) - Unable to connect to '"
							+ sUrl + "'", ioe);
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		if (logger.isDebugEnabled()){
			logger
					.debug("enviar(String, String, String, String) - Request Path: "
							+ method.getPath());
		}
		if (logger.isDebugEnabled()){
			logger
					.debug("enviar(String, String, String, String) - Request Query: "
							+ method.getQueryString());
		}
		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
				if (logger.isDebugEnabled()){
					logger.debug("enviar(String, String, String, String)"
							+ requestHeaders[i]);
				}
		}

		//write out the response headers
		if (logger.isDebugEnabled()){
			logger.debug("enviar(String, String, String, String) - *** Response ***");
		}
		if (logger.isDebugEnabled()){
			logger.debug("enviar(String, String, String, String) - Status Line: "
							+ method.getStatusLine());
		}
        int iStatusCode = method.getStatusCode();
        String sStatusLine=method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			if (logger.isDebugEnabled()){
				logger.debug("enviar(String, String, String, String)"
						+ responseHeaders[i]);
			}
		}

		//clean up the connection resources
		method.releaseConnection();
		method.recycle();
        if (iStatusCode==200){
	        return new GZIPInputStream(new ByteArrayInputStream(responseBody));
        }
        else
            throw new Exception(sStatusLine);
	}


    /*** INVENTARIO */

   

    /** Devuelva un listado de Documentos (DocumentBean) asociados a un objeto.
     *  Cada DocumentBean tendra todos los datos del documento almacenados en la
     *  base de datos excepto el contenido.
     *  @param id
     */
    public Collection getAttachedDocuments(Object obj) throws Exception{
        return getAttached(obj);
    }
    /**
     * Devuelva un listado de Documentos (DocumentBean) asociados a un objeto.
     *  Cada DocumentBean tendra todos los datos del documento almacenados en la
     *  base de datos excepto el contenido.
     * @param id
     */
    private Collection getAttached(Object obj) throws Exception{
        Collection cRet=null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_ATTACHED_INVENTARIO_DOCUMENTS);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_INVENTARIO, obj);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        cRet= new ArrayList();
        try{
            DocumentBean documentBean= null;
            for(;;){
                documentBean= (DocumentBean)readObject(ois);

                cRet.add(documentBean);
            }
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("getAttached(Object obj)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return cRet;
    }

    /**
     * Inserta el ByteInputStream/File del document en la BD y lo asocia a la lista de features
     * @param id del bien
     * @param document
     * */
    public DocumentBean attachInventarioDocument(Object key,  DocumentBean document) throws Exception{
        if (document != null){
            if (document.getContent() != null)
                return attachInventarioDocumentByteStream(key, document);
            if (document.getFileName() != null){
                File file= new File(document.getFileName());
                document.setFileName(file.getName());
                return attachInventarioDocument(key, document, file);
            }
        }
        throw new Exception("Documento no valido");
    }

    /**
     * Inserta el ByteInputStream del document en la BD y lo asocia al bien de inventario con id
     * @param id del bien
     * @param document
     * */
    public DocumentBean attachInventarioDocumentByteStream(Object key,  DocumentBean document) throws Exception{
        DocumentBean doc= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_ATTACH_INVENTARIO_BYTESTREAM);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_INVENTARIO, key);
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              doc= (DocumentBean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("attachInventarioDocumentByteStream(long id,  DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return doc;
     }

    /**
     * Inserta el File en la BD y lo asocia al bien de inventario
     * @param id del bien
     * @param document
     * @param f
     * */
    public DocumentBean attachInventarioDocument(Object key, DocumentBean document, File f) throws Exception{
        DocumentBean doc= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_ATTACH_INVENTARIO_DOCUMENT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_INVENTARIO, key);
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString(), f);
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              doc= (DocumentBean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("attachInventarioDocument(long id, DocumentBean document, File f)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return doc;
     }

    /**
     * Asocia el documento a un bien de inventario
     * @param id del bien
     * @param document
     * */
    public void linkInventarioDocument(Object key, DocumentBean document) throws Exception{
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_LINK_INVENTARIO_DOCUMENT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_INVENTARIO, key);
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("linkInventarioDocument(long id, DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
     }

    /**
     * Desvincula un documento de un bien de inventario. Si es el último bien con el que está asociado
     * debe eliminar el documento de la base de datos.
     * @param id del bien
     * @param document
     */
    public void detachInventarioDocument(Object key, DocumentBean document) throws Exception{
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_DETACH_INVENTARIO_DOCUMENT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_ID_INVENTARIO,key);
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("detachInventarioDocument(long id, DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
    }


    /** Actualiza el mimetype del documento */
    public DocumentBean updateTipo(DocumentBean document) throws Exception{
        DocumentBean doc= null;
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_GET_TIPO_DOCUMENT);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_DOCUMENT, document);
        if (SecurityManager.getIdMunicipio()!=null)
        	params.put(Const.KEY_ID_MUNICIPIO, SecurityManager.getIdMunicipio());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              doc= (DocumentBean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("updateTipo(DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
        return doc;
     }
    
    private static byte[] readFile(File file) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int read = -1;
		while ((read = fis.read(buffer)) != -1) {
			baos.write(buffer, 0, read);
		}
		return baos.toByteArray();
	}


    /** Devuelva un listado de Documentos (DocumentBean) que no están asociados a una feature.
     *  Cada DocumentBean tendra todos los datos del documento almacenados en la
     *  base de datos excepto el contenido */
    public Collection getAttachedDocumentsSinFeature() throws Exception{
    	    	
        Collection cRet=null;


    	ACQuery query= new ACQuery();
    	query.setAction(Const.ACTION_GET_ATTACHED_DOCUMENTS_SIN_FEATURE);
        Hashtable params= new Hashtable();

		if(SecurityManager.getIdEntidad() != null)
			params.put(Const.KEY_ID_ENTIDAD, SecurityManager.getIdEntidad());
       
    	query.setParams(params);
    	StringWriter swQuery= new StringWriter();
    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
    	new ObjectOutputStream(baos).writeObject(query);
    	Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
    	InputStream in= enviarDocuments(sUrl, swQuery.toString());
    	ObjectInputStream ois= new ObjectInputStream(in);
    	cRet= new ArrayList();
    	try{
    		DocumentBean documentBean= null;
    		for(;;){
    			documentBean= (DocumentBean)readObject(ois);

    			cRet.add(documentBean);
    		}
    	}catch(OptionalDataException ode){
    		if (ode.eof!=true)
    			logger.error("getAttachedDocumentsSinFeature()" + ode.getMessage(), ode);
    	}catch (EOFException ee){
    	}finally{
    		try{ois.close();}catch(Exception e){};
    	}
    	return cRet;
    }    
    
    /** Elimina el documento de la base de datos.
     * 
     * @param document
     * @throws Exception
     */
    public void detachDocumentSinFeature(DocumentBean document) throws Exception{
        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_DETACH_DOCUMENT_SIN_FEATURE);
        Hashtable params= new Hashtable();
        params.put(Const.KEY_DOCUMENT, document);
		if(SecurityManager.getIdEntidad() != null)
			params.put(Const.KEY_ID_ENTIDAD, SecurityManager.getIdEntidad());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("detachDocumentSinFeature(DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }
    }

    /** Inserta el ByteInputStream/File del document en la BD*/
    public DocumentBean attachDocumentSinFeature(DocumentBean document) throws Exception{
        if (document != null){
            if (document.getContent() != null)
                return attachDocumentSinFeatureByteStream(document);
            if (document.getFileName() != null){
                File file= new File(document.getFileName());
                document.setFileName(file.getName());
                return attachDocumentSinFeature(document, file);
            }
        }
        throw new Exception("Documento no valido");
    }
    

    /** Inserta el ByteInputStream del document en la BD */
    public DocumentBean attachDocumentSinFeatureByteStream(DocumentBean document) throws Exception{
        DocumentBean doc= null;

        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_ATTACH_BYTESTREAM_SIN_FEATURE);
        Hashtable params= new Hashtable();

        params.put(Const.KEY_DOCUMENT, document);
		if(SecurityManager.getIdEntidad() != null)
			params.put(Const.KEY_ID_ENTIDAD, SecurityManager.getIdEntidad());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              doc= (DocumentBean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("attachDocumentSinFeatureByteStream(DocumentBean document)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return doc;
     }


    /** Inserta el File en la BD y lo asocia a la lista de features */
    public DocumentBean attachDocumentSinFeature(DocumentBean document, File f) throws Exception{
        DocumentBean doc= null;

        ACQuery query= new ACQuery();
        query.setAction(Const.ACTION_ATTACH_DOCUMENT_SIN_FEATURE);
        Hashtable params= new Hashtable();

        params.put(Const.KEY_DOCUMENT, document);
		if(SecurityManager.getIdEntidad() != null)
			params.put(Const.KEY_ID_ENTIDAD, SecurityManager.getIdEntidad());
       
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarDocuments(sUrl, swQuery.toString(), f);
        ObjectInputStream ois= new ObjectInputStream(in);
        try{
              doc= (DocumentBean)readObject(ois);
        }catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("attachDocumentSinFeature(DocumentBean document, File f)" + ode.getMessage(), ode);
        }catch (EOFException ee){
        }finally{
            try{ois.close();}catch(Exception e){};
        }

        return doc;
     }

    
    /**
     * Inicializa la ruta relativa de directorios y su acceso, y devuelve el nodo padre
	 * @param idMunicipality: Identificador de municipio
	 * @param appName: Nombre del tipo de aplicación
	 * @throws Exception
     * @return Node: Nodo padre de la ruta relativa de directorios
     */
	public void initializeRelativeDirectoryPathAndAccess(String idMunicipality, String appName)
			throws Exception {
		Hashtable<Integer, Object> params = new Hashtable<Integer, Object>();
		params.put(AlfrescoConstants.KEY_ID_MUNICIPALITY, idMunicipality);
		params.put(AlfrescoConstants.KEY_APP, appName);		
		getNode(AlfrescoConstants.ACTION_INITIALIZE_RELATIVE_DIRECTORY_PATH_AND_ACCESS,
				params);
	}
	
	 /**
     * Método genérico para las solicitudes del cliente que devuelvan un nodo de Alfresco
	 * @param action: Accion solicitada
	 * @param params: Hashtable con las claves de parámetro y sus valores 
	 * @throws Exception
     * @return Node: Nodo de Alfresco
     */
	public void getNode(int action, Hashtable<Integer, Object> params)
			throws Exception {
		//Node cRet = null;
		ACQuery query = new ACQuery();
		query.setAction(action);
		query.setParams(params);
		StringWriter swQuery = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(query);
		Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
		InputStream in = enviarDocuments(sUrl, swQuery.toString());
		ObjectInputStream ois = new ObjectInputStream(in);
		readObject(ois);				
	}
}
