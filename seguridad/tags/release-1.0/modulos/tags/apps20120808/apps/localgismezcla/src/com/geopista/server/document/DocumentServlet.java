package com.geopista.server.document;

import com.geopista.app.AppContext;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administradorCartografia.*;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.document.Const;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.Thumbnail;

import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.gui.JReportFrame;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.net.URLDecoder;

import org.exolab.castor.xml.Unmarshaller;
import org.mortbay.jaas.JAASUserPrincipal;
import admcarApp.PasarelaAdmcar;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 24-abr-2006
 * Time: 15:14:36
 */
public class DocumentServlet extends LoggerHttpServlet {
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(DocumentServlet.class);
    GeopistaConnection geoConn;
//    private static SRID srid=null;
    private static NewSRID srid = null;

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

            DocumentConnection geoConn=null;
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

                           //logger.debug("MENSAJE XML:"+item.getString("ISO-8859-1"));
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

            geoConn= new DocumentConnection(srid);

            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));
            //De aquí hay que obtener los parametros necesarios
            Hashtable params= query.getParams();
            Sesion sesion= sesion(request);
            int iUserID= Integer.parseInt(sesion.getIdUser());
            DocumentBean document= null;
            String idMunicipio=((String)params.get(Const.KEY_ID_MUNICIPIO));   
            
            int idEntidad=idEntidadSupramunicipal(request);
            switch (query.getAction()){
            	case Const.ACTION_GET_DIR:
            		geoConn.returnDirectories(oos,idEntidad);
            		break;
            	case Const.ACTION_GET_SUBDIR:
            		geoConn.returnSubdirectories(oos, idEntidad,((String)params.get(Const.KEY_PATH)));
            		break;
            	case Const.ACTION_GET_FILES:
            		geoConn.returnFiles(oos,idEntidad,((String)params.get(Const.KEY_PATH)));
            		break;
	            case Const.ACTION_SEND_FILE:
	            	geoConn.returnSendFile(((String)params.get(Const.KEY_ID_ENTIDAD)), 
	            			(String)params.get(Const.KEY_DOCUMENT_FILE),
	            			(DocumentBean)params.get(Const.KEY_DOCUMENT),
	            			((String)params.get(Const.KEY_PATH)), idMunicipio, ((String)params.get(Const.KEY_REPORT_NAME)));
	            	break;
                case Const.ACTION_GET_DOCUMENTS:
                	 geoConn.returnDocuments(oos, ((String)params.get(Const.KEY_IS_IMGDOCTEXT)).charAt(0), idMunicipio);
                     break;
                case Const.ACTION_FIND_DOCUMENTS:
                    geoConn.returnFindDocuments(oos, (DocumentBean)params.get(Const.KEY_DOCUMENT), idMunicipio);
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
                /** Inventario */
                case Const.ACTION_GET_ATTACHED_INVENTARIO_DOCUMENTS:
                    geoConn.returnAttachedInventarioDocuments(oos, params.get(Const.KEY_ID_INVENTARIO));
                    break;
                case Const.ACTION_ATTACH_INVENTARIO_BYTESTREAM:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    if (geoConn.isImagen(document)) document.setIsImagen();
                    if (document.isImagen()){
                        document.setThumbnail(Thumbnail.createThumbnail(document.getContent()));
                    }
                    geoConn.returnAttachInventarioDocument(oos, params.get(Const.KEY_ID_INVENTARIO), document, userSesion, idMunicipio);
                    break;
                case Const.ACTION_ATTACH_INVENTARIO_DOCUMENT:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    if (documentFile != null){
                        document.setSize(documentFile.getSize());
                        document.setContent(documentFile.getContent());
                        if (geoConn.isImagen(document)) document.setIsImagen();
                        if (document.isImagen())
                            document.setThumbnail(Thumbnail.createThumbnail(documentFile.getContent()));
                    }
                    geoConn.returnAttachInventarioDocument(oos, params.get(Const.KEY_ID_INVENTARIO), document, userSesion, idMunicipio);
                    break;
                case Const.ACTION_LINK_INVENTARIO_DOCUMENT:
                    geoConn.returnLinkInventarioDocument(oos, params.get(Const.KEY_ID_INVENTARIO), (DocumentBean)params.get(Const.KEY_DOCUMENT));
                    break;
                case Const.ACTION_DETACH_INVENTARIO_DOCUMENT:
                    geoConn.returnDetachInventarioDocument(oos, params.get(Const.KEY_ID_INVENTARIO), (DocumentBean)params.get(Const.KEY_DOCUMENT));
                    break;
                case Const.ACTION_GET_TIPO_DOCUMENT:
                    geoConn.returnUpdateTipoDocument(oos, (DocumentBean)params.get(Const.KEY_DOCUMENT));
                    break;
                case Const.ACTION_GET_ATTACHED_GESTIONCIUDAD_DOCUMENTS:
                    geoConn.returnAttachedGestionCiudadDocuments(oos, (Long)params.get(Const.KEY_ID_GESTIONCIUDADNOTE));
                    break;
                case Const.ACTION_ATTACH_GESTIONCIUDAD_BYTESTREAM:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    if (geoConn.isImagen(document)) document.setIsImagen();
                    if (document.isImagen()){
                        document.setThumbnail(Thumbnail.createThumbnail(document.getContent()));
                    }
                    geoConn.returnAttachInventarioDocument(oos, (Long)params.get(Const.KEY_ID_GESTIONCIUDADNOTE), document, userSesion, idMunicipio);
                    break;
                case Const.ACTION_ATTACH_GESTIONCIUDAD_DOCUMENT:
                    document= (DocumentBean)params.get(Const.KEY_DOCUMENT);
                    if (documentFile != null){
                        document.setSize(documentFile.getSize());
                        document.setContent(documentFile.getContent());
                        if (geoConn.isImagen(document)) document.setIsImagen();
                        if (document.isImagen())
                            document.setThumbnail(Thumbnail.createThumbnail(documentFile.getContent()));
                    }
                    geoConn.returnAttachGestionCiudadDocument(oos, (Long)params.get(Const.KEY_ID_GESTIONCIUDADNOTE), document, userSesion);
                    break;
                case Const.ACTION_LINK_GESTIONCIUDAD_DOCUMENT:
                    geoConn.returnLinkGestionCiudadDocument(oos, (Long)params.get(Const.KEY_ID_GESTIONCIUDADNOTE), (DocumentBean)params.get(Const.KEY_DOCUMENT));
                    break;
                case Const.ACTION_DETACH_GESTIONCIUDAD_DOCUMENT:
                    geoConn.returnDetachGestionCiudadDocument(oos, (Long)params.get(Const.KEY_ID_GESTIONCIUDADNOTE), (DocumentBean)params.get(Const.KEY_DOCUMENT));
                    break;
                case Const.ACTION_GET_GESTIONCIUDAD_DOCUMENTS:
                    geoConn.returnGestionCiudadDocuments(oos, ((String)params.get(Const.KEY_IS_IMGDOCTEXT)).charAt(0));
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
    
    //Devuelve la entidad supramunicipal relacionada con la sesión
    private int idEntidadSupramunicipal(HttpServletRequest request){
        int iRet=-1;
        try{
            JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
            if (userPrincipal!=null){
                String  sIdSesion= userPrincipal.getName();
                ISesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
                iRet=Integer.parseInt(sSesion.getIdEntidad());
            }
        }catch (Exception e){
        }
        return iRet;
    }

}

