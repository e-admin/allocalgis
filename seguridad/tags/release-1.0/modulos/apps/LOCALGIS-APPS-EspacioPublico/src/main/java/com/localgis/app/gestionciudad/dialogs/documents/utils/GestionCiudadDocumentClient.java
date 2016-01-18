package com.localgis.app.gestionciudad.dialogs.documents.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
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
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.document.Const;
import com.localgis.app.gestionciudad.beans.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.localgis.app.gestionciudad.beans.LocalGISNote;

public class GestionCiudadDocumentClient extends DocumentClient {

	private static final Log logger= LogFactory.getLog(GestionCiudadDocumentClient.class);
	private String sUrl=null;

	public GestionCiudadDocumentClient(String url) {
		super(url);
		this.sUrl = url;
	}
	
	 private Object readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException,ACException{
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
	        return getGestionCiudadDocuments(tipo);
	    }
	    /** Devuelva todos los documentos (DocumentBean) del sistema */
	    private Collection getDocuments(char is_imgdoctext) throws Exception{
	        Collection cRet=null;
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_GET_DOCUMENTS);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_IS_IMGDOCTEXT, new String(""+is_imgdoctext));
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
	    
	    private Collection getGestionCiudadDocuments(char is_imgdoctext) throws Exception{
	        Collection cRet=null;
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_GET_GESTIONCIUDAD_DOCUMENTS);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_IS_IMGDOCTEXT, new String(""+is_imgdoctext));
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
	        
	        if (feature.getLayer()!= null && feature.getLayer() instanceof GeopistaLayer && !((GeopistaLayer)feature.getLayer()).isLocal()){

	        	ACQuery query= new ACQuery();
	        	query.setAction(Const.ACTION_GET_ATTACHED_DOCUMENTS);
	        	Hashtable params= new Hashtable();
	        	params.put(Const.KEY_ID_LAYER, feature.getLayer().getSystemId());
	        	params.put(Const.KEY_ID_FEATURE, feature.getSystemId());
	        	params.put(Const.KEY_IS_IMGDOCTEXT, new String(""+is_imgdoctext));
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

	    /** Inserta el ByteInputStream/File del document en la BD y lo asocia a la lista de features */
	    public DocumentBean attachDocument(Object[] features,  DocumentBean document) throws Exception{
	        if (document != null){
	            if (document.getContent() != null)
	                return attachDocumentByteStream(features, document);
	            if (document.getFileName() != null){
	                File file= new File(document.getFileName());
	                document.setFileName(file.getName());
	                return attachDocument(features, document, file);
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


	    /*** GESTION CIUDAD */    
	    public Collection getAttachedDocuments(LocalGISNote note) throws Exception{
	        return getAttached(note);
	    }

	   
	    private Collection getAttached(LocalGISNote note) throws Exception{
	        Collection cRet=null;
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_GET_ATTACHED_GESTIONCIUDAD_DOCUMENTS);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_ID_GESTIONCIUDADNOTE, new Long(note.getId()));
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

	    public DocumentBean attachDocument(LocalGISNote note,  DocumentBean document) throws Exception{
	        if (document != null){
	            if (document.getContent() != null)
	                return attachInventarioDocumentByteStream(note, document);
	            if (document.getFileName() != null){
	                File file= new File(document.getFileName());
	                document.setFileName(file.getName());
	                return attachInventarioDocument(note, document, file);
	            }
	        }
	        throw new Exception("Documento no valido");
	    }


	    public DocumentBean attachInventarioDocumentByteStream(LocalGISNote note,  DocumentBean document) throws Exception{
	        DocumentBean doc= null;
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_ATTACH_GESTIONCIUDAD_BYTESTREAM);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_ID_GESTIONCIUDADNOTE, new Long(note.getId()));
	        params.put(Const.KEY_DOCUMENT, document);
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


	    public DocumentBean attachInventarioDocument(LocalGISNote note, DocumentBean document, File f) throws Exception{
	        DocumentBean doc= null;
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_ATTACH_GESTIONCIUDAD_DOCUMENT);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_ID_GESTIONCIUDADNOTE, new Long(note.getId()));
	        params.put(Const.KEY_DOCUMENT, document);
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


	    public void linkGestionCiudadDocument(LocalGISNote note, DocumentBean document) throws Exception{
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_LINK_GESTIONCIUDAD_DOCUMENT);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_ID_GESTIONCIUDADNOTE, new Long(note.getId()));
	        params.put(Const.KEY_DOCUMENT, document);
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
	    public void detachDocument(LocalGISNote note, DocumentBean document) throws Exception{
	        ACQuery query= new ACQuery();
	        query.setAction(Const.ACTION_DETACH_GESTIONCIUDAD_DOCUMENT);
	        Hashtable params= new Hashtable();
	        params.put(Const.KEY_ID_GESTIONCIUDADNOTE, new Long(note.getId()));
	        params.put(Const.KEY_DOCUMENT, document);
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

}
