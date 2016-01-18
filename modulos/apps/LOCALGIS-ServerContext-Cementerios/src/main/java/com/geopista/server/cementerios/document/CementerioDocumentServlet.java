/**
 * CementerioDocumentServlet.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.cementerios.document;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
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

import com.geopista.global.WebAppConstants;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.document.Const;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.Thumbnail;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.administradorCartografia.NewSRID;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.administradorCartografia.SystemMapException;
import com.geopista.utils.alfresco.config.ConstantesAlfresco;
import com.localgis.server.SessionsContextShared;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 24-abr-2006
 * Time: 15:14:36
 */
public class CementerioDocumentServlet extends LoggerHttpServlet {
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(CementerioDocumentServlet.class);
    //GeopistaConnection geoConn;
//    private static SRID srid=null;
    private static NewSRID srid = null;

    static boolean verificadoAlfresco=false;
    
    public void init(){
        try{
            if (srid==null)
//                srid=new SRID(com.geopista.server.administradorCartografia.Const.SRID_PROPERTIES);
                srid = new NewSRID();
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	super.doPost(request);
        ObjectOutputStream oos=null;
        try{
            JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
            Sesion userSesion = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

          //Recuperamos informacion sobre si Alfresco esta o no activado.Esto lo fija server-alfresco a traves
        	//del fichero alfresco.properties y el resto de las aplicaciones solo necesitan saber si alfresco esta
        	//o no activo.
        	if (!verificadoAlfresco){
	        	if (SessionsContextShared.getContextShared()!=null){
	        		Object configObject=SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), 
	        						WebAppConstants.ALFRESCO_WEBAPP_NAME,"ConfigAlfresco");
	        		if(configObject!= null){	
	        			HashMap<String,String> config=(HashMap)configObject;
	    				//Sustituir en codigo Constantes..... por config.get(ConfigConstants.....)
	    				String alfrescoActive=config.get(ConstantesAlfresco.keyAlfrescoActive);
	    				if ((alfrescoActive!=null) && (alfrescoActive.equals("true"))){
	    					ConstantesAlfresco.alfrescoActive=true;
	    					logger.info("Fijando alfresco como activo");
	    					verificadoAlfresco=true;
	    					ConstantesAlfresco.sesionData=true;
	    				}
	    				else
	    					logger.info("Fijando alfresco como no activo");
	    					verificadoAlfresco=true;
	    					ConstantesAlfresco.sesionData=true;
	        		}	
	        		else{
	        			logger.error("El contexto para el DocumentServlet no esta disponible");
	        		}
	        	}
        	}
            
            CementeriosDocumentConnection geoConn=null;
            ACQuery query= new ACQuery();
            DocumentBean documentFile= null;

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
                    }else {
                        /** Leemos el fichero que se envia. Generamos un DocumentBean auxiliar para guardar la informacion */
                        documentFile= new DocumentBean();
                        /** Debido a que el nombre del fichero puede contener acentos. */
                        documentFile.setFileName(URLDecoder.decode(item.getFieldName(),"ISO-8859-1"));
                        documentFile.setContent(item.get());
                        documentFile.setSize(item.getSize());
                    }
                }
            }

            geoConn= new CementeriosDocumentConnection(srid);

            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));
            //De aquí hay que obtener los parametros necesarios
            Hashtable params= query.getParams();
            Sesion sesion= sesion(request);
            int iUserID= Integer.parseInt(sesion.getIdUser());
            DocumentBean document= null;
            String idMunicipio=((String)params.get(Const.KEY_ID_MUNICIPIO));
            switch (query.getAction()){
                case Const.ACTION_GET_DOCUMENTS:
                    geoConn.returnDocuments(oos, ((String)params.get(Const.KEY_IS_IMGDOCTEXT)).charAt(0), idMunicipio);
                    break;
                case Const.ACTION_ATTACH_DOCUMENT:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    if (documentFile != null){
                        document.setSize(documentFile.getSize());
                        document.setContent(documentFile.getContent());
                        if (document.isImagen())
                            document.setThumbnail(Thumbnail.createThumbnail(documentFile.getContent()));
                    }
                    geoConn.returnAttachDocument(oos, (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), document, userSesion, idMunicipio);
                    break;
                case Const.ACTION_ATTACH_BYTESTREAM:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    if (document.isImagen())
                        document.setThumbnail(Thumbnail.createThumbnail(document.getContent()));
                    geoConn.returnAttachDocument(oos, (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), document, userSesion, idMunicipio);
                    break;
                case Const.ACTION_GET_ATTACHED_DOCUMENTS:
                    geoConn.returnAttachedDocuments(oos, (String)params.get(Const.KEY_ID_LAYER), (String)params.get(Const.KEY_ID_FEATURE), ((String)params.get(Const.KEY_IS_IMGDOCTEXT)).charAt(0));
                    break;
                case Const.ACTION_GET_ATTACHED_BYTESTREAM:
                    geoConn.returnGetAttachedByteStream(oos, (DocumentBean)params.get(Const.KEY_DOCUMENT));
                    break;
                case Const.ACTION_UPDATE_DOCUMENT:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    if (documentFile != null){
                        document.setSize(documentFile.getSize());
                        document.setContent(documentFile.getContent());
                        if (document.isImagen())
                            document.setThumbnail(Thumbnail.createThumbnail(documentFile.getContent()));
                    }
                    geoConn.returnUpdateDocument(oos, document, userSesion);
                    break;
                case Const.ACTION_UPDATE_BYTESTREAM:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    if (document.isImagen())
                        document.setThumbnail(Thumbnail.createThumbnail(documentFile.getContent()));                    
                    geoConn.returnUpdateDocument(oos, document, userSesion);
                    break;
                case Const.ACTION_DETACH_DOCUMENT:
                    geoConn.returnDetachDocument(oos, (String)params.get(Const.KEY_ID_LAYER), (String)params.get(Const.KEY_ID_FEATURE), (DocumentBean)params.get(Const.KEY_DOCUMENT), userSesion);
                    break;
                case Const.ACTION_LINK_DOCUMENT:
                    geoConn.returnLinkDocument(oos, (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), (DocumentBean)params.get(Const.KEY_DOCUMENT), userSesion);
                    break;
                    
    /********************************* CEMENTERIO ************************/
                    
                case Const.ACTION_GET_ATTACHED_CEMENTERIO_DOCUMENTS:
                    geoConn.returnAttachedCementerioDocuments(oos, params.get(Const.KEY_ID_CEMENTERIO), params.get(Const.KEY_ID_SUPERPATRON), params.get(Const.KEY_ID_PATRON) );
                    break;
                case Const.ACTION_ATTACH_CEMENTERIO_BYTESTREAM:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    
                    if (geoConn.isImagen(document)) document.setIsImagen();
                    if (document.isImagen()){
                        document.setThumbnail(Thumbnail.createThumbnail(document.getContent()));
                    }
                    geoConn.returnAttachCementerioDocument(oos, params.get(Const.KEY_ID_CEMENTERIO),params.get(Const.KEY_ID_SUPERPATRON), params.get(Const.KEY_ID_PATRON), document, userSesion, idMunicipio);
                    break;
                case Const.ACTION_ATTACH_CEMENTERIO_DOCUMENT:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    if (documentFile != null){
                        document.setSize(documentFile.getSize());
                        document.setContent(documentFile.getContent());
                        if (geoConn.isImagen(document)) document.setIsImagen();
                        if (document.isImagen())
                            document.setThumbnail(Thumbnail.createThumbnail(documentFile.getContent()));
                    }
                    geoConn.returnAttachCementerioDocument(oos, params.get(Const.KEY_ID_CEMENTERIO), params.get(Const.KEY_ID_SUPERPATRON), params.get(Const.KEY_ID_PATRON), document, userSesion, idMunicipio);
                    break;
                case Const.ACTION_LINK_CEMENTERIO_DOCUMENT:
                    geoConn.returnLinkCementerioDocument(oos, params.get(Const.KEY_ID_CEMENTERIO),params.get(Const.KEY_ID_SUPERPATRON), params.get(Const.KEY_ID_PATRON), (DocumentBean)params.get(Const.KEY_DOCUMENT));
                    break;
                case Const.ACTION_DETACH_CEMENTERIO_DOCUMENT:
                    geoConn.returnDetachCementerioDocument(oos, params.get(Const.KEY_ID_CEMENTERIO),params.get(Const.KEY_ID_SUPERPATRON), params.get(Const.KEY_ID_PATRON), (DocumentBean)params.get(Const.KEY_DOCUMENT));
                    break;
                case Const.ACTION_GET_TIPO_DOCUMENT:
                    geoConn.returnUpdateTipoDocument(oos, (DocumentBean)params.get(Const.KEY_DOCUMENT));
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
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            try{oos.writeObject(new ACException(e));}catch(Exception ex){};
        }finally{
            try{oos.close();}catch(Exception e){};
        }

    }
    private Sesion sesion(HttpServletRequest request){
        Sesion sRet=null;
        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
        if (userPrincipal!=null){
            String  sIdSesion= userPrincipal.getName();
            sRet=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
        }
        return sRet;
    }
}

