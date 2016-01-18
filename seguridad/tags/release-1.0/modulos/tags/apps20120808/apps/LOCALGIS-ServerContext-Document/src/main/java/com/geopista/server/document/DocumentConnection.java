package com.geopista.server.document;

import com.geopista.server.administrador.init.Constantes;
import com.geopista.server.administradorCartografia.*;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.protocol.document.Const;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.control.SesionUtils_LCGIII;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaAcl;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportReader;
import it.businesslogic.ireport.gui.JReportFrame;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 28-abr-2006
 * Time: 14:19:45
 * To change this template use File | Settings | File Templates.
 */
public class DocumentConnection{
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(DocumentConnection.class);
//    private SRID srid;
    private NewSRID srid;
    private Vector docsBorrados= new Vector();

    public DocumentConnection(){
    }

    public DocumentConnection(NewSRID srid){
        this.srid=srid;
    }

    public void setSRID(NewSRID srid){
        this.srid=srid;
    }

    /**
     * Retorna el conjunto de documentos den BD de un tipo
     * @param oos buffer donde se deja el resultado
     * @param imgdoctext 'I' si es imagen, 'T' si es un archivo de texto, 'D' si es un documento
     * @throws Exception
     */
    public void returnDocuments(ObjectOutputStream oos, char imgdoctext, String idMunicipio) throws Exception{
           try{
               for (Iterator <DocumentBean>it=getDocuments(imgdoctext, idMunicipio).iterator();it.hasNext();){
                   oos.writeObject((DocumentBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnDocuments: ", e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    
    public void returnGestionCiudadDocuments(ObjectOutputStream oos, char imgdoctext) throws Exception{
        try{
            for (Iterator it=getGestionCiudadDocuments(imgdoctext).iterator();it.hasNext();){
                oos.writeObject((DocumentBean)it.next());
            }
        }catch(Exception e){
            logger.error("returnDocuments: ", e);
            oos.writeObject(new ACException(e));
            throw e;
        }
 }

    /**
     * Retorna el conjunto de documentos asocidos a una featura de un layer
     * @param oos buffer donde se deja el resultado
     * @param idLayer
     * @param idFeature
     * @param imgdoctext 'I' si es imagen, 'T' si es un archivo de texto, 'D' si es un documento
     * @throws Exception
     */
    public void returnAttachedDocuments(ObjectOutputStream oos, String idLayer, String idFeature, char imgdoctext) throws Exception{
           try{
               try{new Long(idFeature);}catch(Exception e){return;} //Cuando una feature se crea el id es NoInicializado xxxx
               for (Iterator it=getAttachedDocuments(idLayer, idFeature, imgdoctext).iterator();it.hasNext();){
                   oos.writeObject((DocumentBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnAttachedDocuments: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Devuelve el contenido de un docuemnto (byte[])
     * @param oos buffer donde deja el resultado de la operacion
     * @param document
     * @throws Exception
     */
    public void returnGetAttachedByteStream(ObjectOutputStream oos, DocumentBean document) throws Exception{
        try{
            byte[] content= getAttachedByteStream(document);

            oos.writeObject(content);
        }catch(Exception e){
            logger.error("returnGetAttachedByteStream: ",e);
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    public void returnSendFile(String idEntidad, 
    		String fileName, DocumentBean document,String typepath, String idMunicipio, String name) throws Exception{
    	//String path= Const.KEY_TEMPLATES + typepath;	
    	String path= ConstantesLocalGISPlantillas.PATH_PLANTILLAS+File.separator+idEntidad+File.separator+typepath;
        if (!new File(path).exists())
            new File(path).mkdirs();

        try{
        	System.out.println("Escribiendo el informe en :"+path + File.separator+name);
        	FileOutputStream out = new FileOutputStream(path + File.separator+name);
            out.write(document.getContent());
            out.close();
        	
        }catch (IOException ex){
        	logger.error("Error guardando fichero: ",ex);
        }finally{        	
        }
    }
    
    public void returnDirectories(ObjectOutputStream oos,int idEntidad) throws Exception{
        try{
        	//String path= Const.KEY_TEMPLATES;
        	
        	String path= ConstantesLocalGISPlantillas.PATH_PLANTILLAS+File.separator+idEntidad;
        	
        	crearDirectorios(path);

        	 
        	 
        	File[] col = new File(path).listFiles();
        	if (col!=null){
	        	for(int i=0;i<col.length;i++)
	        		if(col[i].isDirectory())
	        			oos.writeObject(col[i].getName());            
        	}
        }catch(Exception e){
            logger.error("returnGetAttachedByteStream: ",e);
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    private void crearDirectorios(String pathBase){
    	String []aplicaciones={"actividad","contaminantes","eiel","espaciopublico","inventario","licencias","ocupacion","routes","impresion"};
    	for (int i=0;i<aplicaciones.length;i++){
    		String app=aplicaciones[i];
        	String path=pathBase+File.separator+app+File.separator+"img";
          	if (!new File(path).exists())
                new File(path).mkdirs();
          	if(app.equals("impresion")) continue;
         	path=pathBase+File.separator+app+File.separator+"subreports";
         	if (!new File(path).exists())
               new File(path).mkdirs();
       	 }
    		
    }

    

    public void returnSubdirectories(ObjectOutputStream oos, int idEntidad,String type) throws Exception{
        try{
        	//String path= Const.KEY_TEMPLATES + File.separator + type;
        	String path= ConstantesLocalGISPlantillas.PATH_PLANTILLAS+File.separator+idEntidad + File.separator + type;
        	File[] col = new File(path).listFiles();
        	if (col!=null){
	        	for(int i=0;i<col.length;i++)
	        		if(col[i].isDirectory())
	        			oos.writeObject(col[i].getName());    
        	}
        }catch(Exception e){
            logger.error("returnGetAttachedByteStream: ",e);
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    public void returnFiles(ObjectOutputStream oos, int idEntidad,String type) throws Exception{
        try{
        	String path=null;
        	if(!type.equals("")){
        		path= ConstantesLocalGISPlantillas.PATH_PLANTILLAS+File.separator+idEntidad + File.separator + type;
        		//path= Const.KEY_TEMPLATES + File.separator + type;
        	}
        	File[] col = new File(path).listFiles();
        	if (col!=null){
	        	for(int i=0;i<col.length;i++)
	        		if(col[i].isFile() && col[i].getName().contains(".jrxml"))
	        			oos.writeObject(col[i].getName());            
        	}
        }catch(Exception e){
            logger.error("returnGetAttachedByteStream: ",e);
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    /**
     * Asocia un documento a una lista de features
     * @param oos buffer donde deja el resultado
     * @param idLayers lista de layers de la lista de features 1 a 1
     * @param idFeatures lista de features
     * @param document
     * @param userSesion sesion de usuario
     * @throws Exception
     */
    public void returnAttachDocument(ObjectOutputStream oos, Object[] idLayers, Object[] idFeatures, DocumentBean document, Sesion userSesion, String idMunicipio) throws Exception{
           try{
               DocumentBean doc= attachDocument(idLayers, idFeatures, document, userSesion, idMunicipio);
               oos.writeObject(doc);
           }catch(Exception e){
               logger.error("returnAttachDocument: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Actualiza un documento
     * @param oos buffer donde deja el resultado de la operacion
     * @param document
     * @param userSesion sesion de usuario
     * @throws Exception
     */
    public void returnUpdateDocument(ObjectOutputStream oos, DocumentBean document, Sesion userSesion) throws Exception{
           try{
               DocumentBean doc= updateDocument(document, userSesion);
               oos.writeObject(doc);
           }catch(Exception e){
               logger.error("returnUpdateDocument: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Borra un documento de una feature asociada a un layer
     * @param oos buffer donde escribe el resultado de la operacion
     * @param idLayer
     * @param idFeature
     * @param document
     * @param userSesion ssesion de usuario
     * @throws Exception
     */
    public void returnDetachDocument(ObjectOutputStream oos, String idLayer, String idFeature, DocumentBean document, Sesion userSesion) throws Exception{
           try{
               detachDocument(idLayer, idFeature, document, userSesion);
           }catch(Exception e){
               logger.error("returnDetachDocument: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Enlaza un documento ya existente en BD a un conjunto de features
     * @param oos buffer donde escribe el resultado
     * @param idLayers lista de layers de la lista de features 1 a 1
     * @param idFeatures lista de features
     * @param document
     * @param userSesion sesion de usuario
     * @throws Exception
     */
    public void returnLinkDocument(ObjectOutputStream oos, Object[] idLayers, Object[] idFeatures, DocumentBean document, Sesion userSesion) throws Exception{
           try{
               linkDocument(idLayers, idFeatures, document, userSesion);
           }catch(Exception e){
               logger.error("returnLinkDocument: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Retorna el conjunto de documentos en BD de tipo imgdoctext
     * @param imgdoctext 'I' si es imagen, 'T' si es un archivo de texto, 'D' si es un documento
     * @return un conjunto de documentos
     * @throws Exception
     */
    public Collection getDocuments(char imgdoctext, String idMunicipio) throws Exception{
        HashMap alRet= new HashMap();
        String sSQL="";
        if (imgdoctext==DocumentBean.ALL_CODE){
            /** Inventario */
            sSQL= "SELECT d.id_documento, d.nombre, d.tamanio, " +
                    "d.tipo, d.fecha_alta, d.fecha_modificacion, " +
                    "d.comentario, d.publico, d.id_municipio, " +
                    "d.is_imgdoctext, d.thumbnail, d.oculto " +
                    "FROM documento d ";
            if (idMunicipio!=null)
            	sSQL+=" where d.id_municipio='"+idMunicipio+"' ";
            sSQL+="ORDER BY nombre ASC";
                    // "WHERE d.id_documento=a.id_documento ORDER BY nombre ASC";
                   // "FROM documento d, anexo_inventario a " +
                   // "WHERE d.id_documento=a.id_documento ORDER BY nombre ASC";
        }else{
            sSQL= "SELECT d.id_documento, d.nombre, d.tamanio, " +
                    "d.tipo, d.fecha_alta, d.fecha_modificacion, " +
                    "d.comentario, d.publico, d.id_municipio, " +
                    "d.is_imgdoctext, d.thumbnail, d.oculto " +
                    " FROM documento d  " +
                    " WHERE d.is_imgdoctext=? ";
            if (idMunicipio!=null)
            	sSQL+=" and d.id_municipio='"+idMunicipio+"' ";
            sSQL+="ORDER BY nombre ASC";
                    
                    //"FROM documento d, anexofeature a " +
                    //"WHERE d.id_documento=a.id_documento AND d.is_imgdoctext=? ORDER BY nombre ASC";
        }

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            if (imgdoctext!=DocumentBean.ALL_CODE){
                ps.setString(1, new String(""+imgdoctext));
            }
            rs= ps.executeQuery();
            DocumentBean document= null;
            while (rs.next()){
                /** documentos sin contenido */
                document= new DocumentBean();
                document.setId(rs.getString("ID_DOCUMENTO"));
                document.setFileName(rs.getString("NOMBRE"));
                document.setSize(rs.getLong("TAMANIO"));
                document.setTipo(getMimeType(rs.getInt("TIPO")));
                document.setFechaEntradaSistema(rs.getTimestamp("FECHA_ALTA"));
                document.setFechaUltimaModificacion(rs.getTimestamp("FECHA_MODIFICACION"));
                document.setComentario(rs.getString("COMENTARIO"));
                document.setPublico(rs.getInt("PUBLICO"));
                document.setOculto(rs.getInt("OCULTO"));
                document.setIdMunicipio(rs.getString("ID_MUNICIPIO"));
                document.setIs_imgdoctext(rs.getString("IS_IMGDOCTEXT").charAt(0));
                if (CPoolDatabase.isPostgres(conn))
                    document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                else{
                    /** Oracle */
                    java.sql.Blob blob= rs.getBlob("THUMBNAIL");
                    if (blob!=null){
                        document.setThumbnail((byte[])blob.getBytes(1,new Long(blob.length()).intValue()));
                    }
                }

                alRet.put(document.getId(), document);
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        return alRet.values();
    }
    /**
     * Retorna un documento por su id
     * @return un documento o null si no lo encuentra
     * @throws Exception
     */
    private DocumentBean getDocumentById(Connection conn, String id, String idMunicipio) throws Exception{
        String sSQL= "SELECT d.id_documento, d.nombre, d.tamanio, " +
                    "d.tipo, d.fecha_alta, d.fecha_modificacion, " +
                    "d.comentario, d.publico, d.id_municipio, " +
                    "d.is_imgdoctext, d.thumbnail, d.oculto " +
                    "FROM documento d where d.id_documento='"+ id+"' ";
            if (idMunicipio!=null)
            	sSQL+=" and d.id_municipio='"+idMunicipio+"' ";
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            DocumentBean document= null;
            if (rs.next()){
                /** documentos sin contenido */
                document= new DocumentBean();
                document.setId(rs.getString("ID_DOCUMENTO"));
                document.setFileName(rs.getString("NOMBRE"));
                document.setSize(rs.getLong("TAMANIO"));
                document.setTipo(getMimeType(rs.getInt("TIPO")));
                document.setFechaEntradaSistema(rs.getTimestamp("FECHA_ALTA"));
                document.setFechaUltimaModificacion(rs.getTimestamp("FECHA_MODIFICACION"));
                document.setComentario(rs.getString("COMENTARIO"));
                document.setPublico(rs.getInt("PUBLICO"));
                document.setOculto(rs.getInt("OCULTO"));
                document.setIdMunicipio(rs.getString("ID_MUNICIPIO"));
                document.setIs_imgdoctext(rs.getString("IS_IMGDOCTEXT").charAt(0));
                if (CPoolDatabase.isPostgres(conn))
                    document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                else{
                    /** Oracle */
                    java.sql.Blob blob= rs.getBlob("THUMBNAIL");
                    if (blob!=null){
                        document.setThumbnail((byte[])blob.getBytes(1,new Long(blob.length()).intValue()));
                    }
                }
                return document;
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return null;
    }
    
    
    public Collection getGestionCiudadDocuments(char imgdoctext) throws Exception{
        HashMap alRet= new HashMap();
        String sSQL="";
        if (imgdoctext==DocumentBean.ALL_CODE){
            /** Inventario */
            sSQL= "SELECT d.id_documento, d.nombre, d.tamanio, " +
                    "d.tipo, d.fecha_alta, d.fecha_modificacion, " +
                    "d.comentario, d.publico, d.id_municipio, " +
                    "d.is_imgdoctext, d.thumbnail, d.oculto " +
                    "FROM documento d, civil_work_documents a " +
                    "WHERE d.id_documento=a.id_document ORDER BY nombre ASC";
        }else{
            sSQL= "SELECT d.id_documento, d.nombre, d.tamanio, " +
                    "d.tipo, d.fecha_alta, d.fecha_modificacion, " +
                    "d.comentario, d.publico, d.id_municipio, " +
                    "d.is_imgdoctext, d.thumbnail, d.oculto " +
                    "FROM documento d, civil_work_documents a " +
                    "WHERE d.id_documento=a.id_document AND d.is_imgdoctext=? ORDER BY nombre ASC";
        }

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            if (imgdoctext!=DocumentBean.ALL_CODE){
                ps.setString(1, new String(""+imgdoctext));
            }
            rs= ps.executeQuery();
            DocumentBean document= null;
            while (rs.next()){
                /** documentos sin contenido */
                document= new DocumentBean();
                document.setId(rs.getString("ID_DOCUMENTO"));
                document.setFileName(rs.getString("NOMBRE"));
                document.setSize(rs.getLong("TAMANIO"));
                document.setTipo(getMimeType(rs.getInt("TIPO")));
                document.setFechaEntradaSistema(rs.getTimestamp("FECHA_ALTA"));
                document.setFechaUltimaModificacion(rs.getTimestamp("FECHA_MODIFICACION"));
                document.setComentario(rs.getString("COMENTARIO"));
                document.setPublico(rs.getInt("PUBLICO"));
                document.setOculto(rs.getInt("OCULTO"));
                document.setIdMunicipio(rs.getString("ID_MUNICIPIO"));
                document.setIs_imgdoctext(rs.getString("IS_IMGDOCTEXT").charAt(0));
                if (CPoolDatabase.isPostgres(conn))
                    document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                else{
                    /** Oracle */
                    java.sql.Blob blob= rs.getBlob("THUMBNAIL");
                    if (blob!=null){
                        document.setThumbnail((byte[])blob.getBytes(1,new Long(blob.length()).intValue()));
                    }
                }

                alRet.put(document.getId(), document);
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        return alRet.values();
    }


    private String getSQLAttached(boolean publiconly){
        String sSQL= "SELECT d.id_documento, d.nombre, d.tamanio, " +
                "d.tipo, d.fecha_alta, d.fecha_modificacion, " +
                "d.comentario, d.publico, d.id_municipio, " +
                "d.is_imgdoctext, d.thumbnail " +
                "FROM documento d, anexofeature a " +
                "WHERE a.id_documento=d.id_documento AND a.id_layer=? AND a.id_feature=? AND d.is_imgdoctext=? ";
                if (publiconly)  sSQL+= "AND d.publico=1 ";
                sSQL+="ORDER BY nombre ASC";
        return sSQL;
    }

    /**
     * Retorna los documentos de tipo imgdoctext asociados a una feature
     * @param idLayer
     * @param idFeature
     * @param imgdoctext 'I' si es imagen, 'T' si es un archivo de texto, 'D' si es un documento
     * @return documentos de tipo imgdoctext asociados a una feature
     * @throws Exception
     */
    public Collection getAttachedDocuments(String idLayer, String idFeature, char imgdoctext) throws Exception{
        return getAttached(idLayer, idFeature, imgdoctext, getSQLAttached(false));
    }

    /**
     * Guia Urbana. Retorna los documentos publicos asociados a una feature
     * @param idLayer layer de la feature
     * @param idFeature
     * @return documentos publicos asociados a una feature
     * @throws Exception
     */
    public Collection getAttachedPublicDocuments(String idLayer, String idFeature) throws Exception{
        return getAttached(idLayer, idFeature, DocumentBean.DOC_CODE, getSQLAttached(true));
    }

    /**
     * Guia Urbana. Retorna las imagenes publicas asociadas a una feature
     * @param idLayer layer de la feature
     * @param idFeature
     * @return imagenes publicas asociadas a una feature
     * @throws Exception
     */
    public Collection getAttachedPublicImages(String idLayer, String idFeature) throws Exception{
        return getAttached(idLayer, idFeature, DocumentBean.IMG_CODE, getSQLAttached(true));
    }

    /**
     * Guia Urbana. Retorna los archivos de texto publicos asociados a una feature
     * @param idLayer layer de la feature
     * @param idFeature
     * @return archivos de texto publicos asociados a una feature
     * @throws Exception
     */
    public Collection getAttachedPublicTextos(String idLayer, String idFeature) throws Exception{
        return getAttached(idLayer, idFeature, DocumentBean.TEXT_CODE, getSQLAttached(true));
    }

    /**
     * Retorna los archivos asociados a una feature de tipo imgdoctext
     * @param idLayer later de la feature
     * @param idFeature
     * @param imgdoctext 'I' si es imagen, 'T' si es un archivo de texto, 'D' si es un documento
     * @param sSQL a ejecutar
     * @return
     * @throws Exception
     */
    public Collection getAttached(String idLayer, String idFeature, char imgdoctext, String sSQL) throws Exception{
        HashMap alRet= new HashMap();
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        DocumentBean document= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            ps.setInt(1, getIdLayer(idLayer));
            ps.setString(2, idFeature);
            ps.setString(3, new String(""+imgdoctext));
            rs= ps.executeQuery();
            while (rs.next()){
                /** documentos sin contenido */
                document= new DocumentBean();
                document.setId(rs.getString("ID_DOCUMENTO"));
                document.setFileName(rs.getString("NOMBRE"));
                document.setSize(rs.getLong("TAMANIO"));
                document.setTipo(getMimeType(rs.getInt("TIPO")));
                document.setFechaEntradaSistema(rs.getTimestamp("FECHA_ALTA"));
                document.setFechaUltimaModificacion(rs.getTimestamp("FECHA_MODIFICACION"));
                document.setComentario(rs.getString("COMENTARIO"));
                document.setPublico(rs.getInt("PUBLICO"));
                document.setIdMunicipio(rs.getString("ID_MUNICIPIO"));
                document.setIs_imgdoctext(rs.getString("IS_IMGDOCTEXT").charAt(0));
                /** GuiaUrbana: conn instanceof org.postgresql.jdbc3.Jdbc3Connection.
                 *  No se puede hacer el casting a java.sql.Connection, que es lo que espera como parametro CPoolDatabase.isPostgres() */
                if (conn instanceof org.postgresql.PGConnection){
                    document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                }else{
                    if (CPoolDatabase.isPostgres(conn))
                        document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                    else{
                        /** Oracle */
                        java.sql.Blob blob= rs.getBlob("THUMBNAIL");
                        if (blob!=null){
                            document.setThumbnail((byte[])blob.getBytes(1,new Long(blob.length()).intValue()));
                        }
                    }
                }

                alRet.put(document.getId(), document);
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return alRet.values();
    }


    /**
     * Retorna el contenido de un documento
     * @param document
     * @return contenido del docuemnto en disco
     * @throws Exception
     */
    public byte[] getAttachedByteStream(DocumentBean document) throws Exception{
        return DocumentoEnDisco.get(document);
    }


    /**
     * Asocia un documento a una lista de features
     * @param idLayers lista de layers de la lista de features 1 a 1
     * @param idFeatures lista de features
     * @param document
     * @param userSesion sesion de usuario
     * @return el documento actualizado
     * @throws Exception
     */
    public DocumentBean attachDocument(Object[] idLayers, Object[] idFeatures, DocumentBean document, Sesion userSesion, String idMunicipio) throws PermissionException,LockException,Exception{

        String sSQLDoc= "INSERT INTO DOCUMENTO (ID_DOCUMENTO, ID_MUNICIPIO, NOMBRE, FECHA_ALTA, FECHA_MODIFICACION, TIPO, COMENTARIO,  PUBLICO, TAMANIO, IS_IMGDOCTEXT, THUMBNAIL) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        String sSQLAFeature= "INSERT INTO ANEXOFEATURE (ID_DOCUMENTO, ID_LAYER, ID_FEATURE ) VALUES (?,?,?)";

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (document == null) return null;
			
			//Recuperacion del codigo de documento
			//Lo cambiamos para que sea un hash del documento y lo ponemos el identificador
			//de municipio para que luego sea muy sencillo borrarlo del disco.
            //long idDoc= CPoolDatabase.getNextValue("documento", "id_documento");
            
            String idDoc=getCodigoHash(document.getContent(),document.getFileName());
            
            
            document.setIdMunicipio((idMunicipio!=null?idMunicipio:userSesion.getIdMunicipio()));
            
            /** Actualizamos el documento */
            //NUEVO
            if(!AlfrescoManagerUtils.isAlfrescoActive())
            	document.setId(document.getIdMunicipio()+"_"+idDoc);
            //FIN NUEVO
            
            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);
            
            /** Buscamos si ya existe el documento*/
            DocumentBean documentoEnBD=getDocumentById(conn, document.getId(), idMunicipio);
            if (documentoEnBD==null){
            	/** Comprobamos que tenga permisos de publicacion de documentos */
            	if (!checkPermission(userSesion, com.geopista.protocol.document.Const.ACL_GENERAL, GeopistaPermission.PUBLICAR_DOCUMENTO)){
            		if (document.getPublico() == 1)
            			throw new PermissionException("PermissionException: " + GeopistaPermission.PUBLICAR_DOCUMENTO);
            	}
            	java.util.Date fecha = new java.util.Date();
            	ps= conn.prepareStatement(sSQLDoc);
            	ps.setString(1, document.getId());
            	ps.setInt(2, Integer.valueOf(idMunicipio!=null?idMunicipio:userSesion.getIdMunicipio()));
            	ps.setString(3, document.getFileName());
            	ps.setTimestamp(4, new Timestamp(fecha.getTime()));
            	ps.setNull(5, java.sql.Types.TIMESTAMP);
            	ps.setInt(6, getTipo(getExtension(document.getFileName())));
            	ps.setString(7, document.getComentario());
            	/** El contenido lo guardamos en disco: Problemas con java.lang.OutOfMemoryError */
                //ps.setBytes(8, null);
            	ps.setInt(8, document.getPublico());
            	ps.setLong(9, document.getSize());
            	ps.setString(10, new String(""+document.getIs_imgdoctext()));
            	ps.setBytes(11, document.getThumbnail());
            	ps.execute();
            	ps.close();
            	document.setFechaEntradaSistema(fecha);
            	document.setTipo(getMimeType(getTipo(getExtension(document.getFileName()))));
            }else{
            	document=documentoEnBD;
            }
            for (int j=0; j<idFeatures.length; j++){
                //Si el documento está en bd puede que ya este asociado al bien
            	int idLayer=getIdLayer((String)idLayers[j]);
                if ((documentoEnBD==null) || !existeLink(idLayer, (String)idFeatures[j], document.getId())) {
                	/** Para vincular un documento a una feature, es necesario que esta
                     * no este bloqueada por otro usuario y tener permisos. */
                    checkPermissionLock((String)idLayers[j], (String)idFeatures[j], userSesion);
                    ps=conn.prepareStatement(sSQLAFeature);
                    ps.setString(1, document.getId());
                    ps.setInt(2, idLayer);
                    ps.setInt(3, Integer.valueOf((String)idFeatures[j]));
                    ps.execute();
                    ps.close();
                }
            }
            
            //NUEVO
            if (!AlfrescoManagerUtils.isAlfrescoActive() || documentoEnBD==null){
            	/** Guardamos el fichero en disco */
            	DocumentoEnDisco.guardar(document);
            }  
            //FIN NUEVO
            /** siempre se retorna sin contenido */
            document.setContent(null);

            conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return document;
    }

    /**
     * Actualiza un documento
     * @param document
     * @param userSesion
     * @return el documento actualizado
     * @throws Exception
     */
    public DocumentBean updateDocument(DocumentBean document, Sesion userSesion) throws Exception{
        return updateDocument(document, userSesion, null, false);
    }

    /**
     * Actualiza un documento dentro de una transaccion
     * @param document
     * @param userSesion
     * @param conn
     * @param transaccion true si se realiza dentro de una transaccion, false en caso contrario
     * @return
     * @throws Exception
     */
    public DocumentBean updateDocument(DocumentBean document, Sesion userSesion, Connection conn, boolean transaccion) throws Exception{

        String sSQL= "UPDATE DOCUMENTO SET FECHA_MODIFICACION=?, COMENTARIO=?, PUBLICO=?, OCULTO=?";

        if (document.getContent() != null){
            /** Se modifica el contenido */
            sSQL+= ", NOMBRE=?,  TIPO=?, TAMANIO=?, IS_IMGDOCTEXT=?, THUMBNAIL=?";
        }
        sSQL+= " WHERE ID_DOCUMENTO='"+document.getId()+"'";

        String sSQLLF= "SELECT * FROM ANEXOFEATURE WHERE ID_DOCUMENTO='"+document.getId()+"'";

        PreparedStatement ps= null;
        ResultSet rs= null;
		try {
			if (document == null) return null;

            if (conn == null){
                conn= CPoolDatabase.getConnection();
                conn.setAutoCommit(false);
            }

            ps=conn.prepareStatement(sSQLLF);
            rs=ps.executeQuery();
            while (rs.next()){
                String idLayer= rs.getString("ID_LAYER");
                String idFeature= rs.getString("ID_FEATURE");
                /** Es necesario que la feature no este bloqueada por otro usuario y tener permisos. */
                checkPermissionLock(idLayer, idFeature, userSesion);
            }
            rs.close();
            ps.close();

            /** Comprobamos que ademas tenga permisos de publicacion de documentos */
            if (!checkPermission(userSesion, com.geopista.protocol.document.Const.ACL_GENERAL, GeopistaPermission.PUBLICAR_DOCUMENTO)){
                if (haCambiadoCampoPublico(document.getId(), document.getPublico()))
                    throw new PermissionException("PermissionException: " + GeopistaPermission.PUBLICAR_DOCUMENTO);
            }

            java.util.Date date= (java.util.Date)new Timestamp(new java.util.Date().getTime());
            ps= conn.prepareStatement(sSQL);
            ps.setTimestamp(1, new Timestamp(date.getTime()));
            ps.setString(2, document.getComentario());
            ps.setInt(3, document.getPublico());
            ps.setInt(4, document.getOculto());
            if (document.getContent() != null){
                ps.setString(5, document.getFileName());
                ps.setInt(6, getTipo(getExtension(document.getFileName())));
                /** guardamos el documento en disco */
                //ps.setBytes(7, null);
                ps.setLong(7, document.getSize());
                ps.setString(8, new String(""+document.getIs_imgdoctext()));
                ps.setBytes(9, document.getThumbnail());
            }
            ps.execute();

            /** guardamos el fichero en disco */
            if (AlfrescoManagerUtils.isAlfrescoActive() && document.getContent() != null){
                if (!transaccion)
                    DocumentoEnDisco.actualizar(document);
                else DocumentoEnDisco.guardarEnTemporal(document);
            }

            /** actualizamos el fichero */
            document.setFechaUltimaModificacion(date);
            document.setTipo(getMimeType(getTipo(getExtension(document.getFileName()))));
            /** siempre se retorna sin contenido */
            document.setContent(null);

            if (!transaccion) conn.commit();

        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{if (!transaccion){
                conn.close();CPoolDatabase.releaseConexion();
            }}catch(Exception e){};
        }

        return document;
    }
     
    /**
     * Comprueba si en un documento ha pasado de ser publico a privado o viceversa
     * @param idDocumento
     * @param newpublico nuevo valor del campo publico
     * @return true si ha cambiado el valor, false en caso contrario
     * @throws Exception
     */
    public boolean haCambiadoCampoPublico(String idDocumento, int newpublico) throws Exception{
        String sSQL= "SELECT publico FROM documento WHERE id_documento='"+idDocumento+"'";

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        boolean b= false;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            if (rs.next()){
                int oldpublico= rs.getInt("publico");
                if (oldpublico != newpublico) b= true;
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return b;
    }

    /**
     * Borra un documento de una feature
     * @param idLayer
     * @param idFeature
     * @param document
     * @param userSesion
     * @throws PermissionException
     * @throws LockException
     * @throws ACException
     * @throws Exception
     */
    public void detachDocument(String idLayer, String idFeature, DocumentBean document, Sesion userSesion) throws PermissionException,LockException,ACException,Exception{

        String sSQLDoc= "DELETE FROM DOCUMENTO WHERE ID_DOCUMENTO=?";
        String sSQLAFeature= "DELETE FROM ANEXOFEATURE " +
                             "WHERE ID_LAYER=? AND ID_FEATURE=? AND ID_DOCUMENTO=?";

        Connection conn=null;
        PreparedStatement ps= null;
		try {
			if (document == null) return;
            /** Para desvincular un documento de una feature, es necesario que esta
             * no este bloqueada por otro usuario y tener permisos. */
            checkPermissionLock(idLayer, idFeature, userSesion);

            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            ps= conn.prepareStatement(sSQLAFeature);
            ps.setInt(1, getIdLayer(idLayer));
            ps.setString(2, idFeature);
            ps.setString(3, document.getId());
            ps.execute();

            if (!asociadoAOtraFeature(document.getId(), getIdLayer(idLayer), idFeature) &&
            		asociadoAOtroBien(document.getId(),null)){
                /** borramos el docuemnto de la BD y de disco */
                ps.close();
                ps= conn.prepareStatement(sSQLDoc);
                ps.setString(1, document.getId());
                ps.execute();

                DocumentoEnDisco.borrar(document);

            }
            conn.commit();
        }finally{
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }


    /**
     * Comprueba si un documento esta asociado a otra fetaure distinta de idFeature
     * @param idDocumento
     * @param idLayer al que pertenece la feature
     * @param idFeature
     * @return
     * @throws Exception
     */
    private boolean asociadoAOtraFeature(String idDocumento, int idLayer, String idFeature) throws Exception{
        String sSQL= "SELECT * FROM ANEXOFEATURE WHERE ID_DOCUMENTO='" +idDocumento+
                     "' AND ID_LAYER<>"+idLayer+ " AND ID_FEATURE<>"+idFeature;
        boolean b= false;
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            if (rs.next()) b= true;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return b;
    }

    /**
     * Comprueba si una feature esta bloqueada por otro usuario
     * @param iMunicipio
     * @param sLayer al que pertenece la feature
     * @param iFeature
     * @return -1 si no bloqueado, id_usuario si hay bloqueo
     * @throws Exception
     */
    public int featureLocked(int iMunicipio, String sLayer, int iFeature) throws Exception{
        int iRet=-1;
        String sSQL="select user_id,ts from locks_feature " +
                    "where " +
                        "municipio=? and " +
                        "layer=? and " +
                        "feature_id=?";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=CPoolDatabase.getConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setInt(1,iMunicipio);
            ps.setString(2,sLayer);
            ps.setInt(3,iFeature);
            rs=ps.executeQuery();
            if (rs.next()){
                iRet=rs.getInt("user_id");
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return iRet;
    }

    /**
     * Comprueba si el usuario tiene permisos de bloqueo sobre la feature
     * @param idLayer al que pertenece la feature
     * @param idFeature
     * @param userSesion sesion de usuario
     * @throws Exception
     */
    public void checkPermissionLock(String idLayer, String idFeature, Sesion userSesion) throws Exception{


        String sSQL= "select l.name as name,l.acl as acl " +
                     "from layers l ";

        try{
            Integer.parseInt(idLayer);
            sSQL+= "where l.id_layer= ?";
        }catch(java.lang.NumberFormatException e){
            sSQL+= "where l.name= ?";
        }

        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        long aclLayer= -1;
        String nameLayer= "";
        try{
            conn=CPoolDatabase.getConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setString(1,idLayer);
            rs=ps.executeQuery();
            if (rs.next()){
                aclLayer=rs.getLong("acl");
                nameLayer=rs.getString("name");
            }
            rs.close();
            ps.close();
            /** Permisos sobre el layer */
            if (!checkPermission(userSesion,aclLayer,com.geopista.server.administradorCartografia.Const.PERM_LAYER_WRITE))
                    throw new PermissionException(com.geopista.server.administradorCartografia.Const.PERM_LAYER_WRITE);

            /** Bloqueo sobre la feature */
            //int iLock=canLockFeature(Integer.parseInt(userSesion.getIdMunicipio()),idLayer,Integer.parseInt(idFeature),Integer.parseInt(userSesion.getIdUser()));
            int iLock=canLockFeature(Integer.parseInt(userSesion.getIdMunicipio()),nameLayer,Integer.parseInt(idFeature),Integer.parseInt(userSesion.getIdUser()));
            if (!(iLock==AdministradorCartografiaClient.LOCK_FEATURE_OWN ||
                  iLock==AdministradorCartografiaClient.LOCK_LAYER_OWN   ||
                  iLock==AdministradorCartografiaClient.LOCK_LAYER_LOCKED)){
                String sMsg=null;
                switch (iLock){
                    case AdministradorCartografiaClient.LOCK_LAYER_OTHER:
                        sMsg="locked: layer "+idLayer;
                        break;
                    case AdministradorCartografiaClient.LOCK_FEATURE_OTHER:
                        sMsg="locked: feature";
                        break;
                    default:
                        sMsg="Lock error";
                }
                throw new LockException(sMsg+" ("+iLock+")");
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }

    /**
     * Comprueba si el usuario tiene permisos sobre el layer
     * @param sSesion
     * @param lACL
     * @param sPerm
     * @return
     * @throws Exception
     */
    public boolean checkPermission(Sesion sSesion,long lACL,String sPerm) throws Exception{
        GeopistaAcl acl= SesionUtils_LCGIII.getPerfil(sSesion,lACL);
        if (acl==null) return false;
        return acl.checkPermission(new GeopistaPermission(sPerm));
    }

    /**
     * Comprueba si tiene permisos de bloque sobre la feature
     * @param iMunicipio
     * @param sLayer
     * @param iFeature
     * @param iUser
     * @return
     * @throws Exception
     */
    private int canLockFeature(int iMunicipio, String sLayer, int iFeature, int iUser) throws Exception{
        int iRet=0;
        // Comparar ids de Feature y bloqueos de layer con la geometria de iFeature...
        int iFeatureLock=featureLocked(iMunicipio,sLayer,iFeature);
        if(iFeatureLock!=-1)
            return (iFeatureLock==iUser?AdministradorCartografiaClient.LOCK_FEATURE_OWN
                                       :AdministradorCartografiaClient.LOCK_FEATURE_OTHER);
        //Obtener la tabla donde esta la geometria...
        String sSQLTable="select t.name as table,l.id_layer from tables t,columns c,attributes a,layers l " +
                         "where a.id_layer=l.id_layer and l.name=? and c.id_table=t.id_table and c.name='GEOMETRY' and a.id_column=c.id";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=CPoolDatabase.getConnection();
            ps=conn.prepareStatement(sSQLTable);
            ps.setString(1,sLayer);
            rs=ps.executeQuery();
            String sTable=null;
            if (rs.next()){
                sTable=rs.getString("table");
            }
            rs.close();
            ps.close();
            String sSQLGeom="";
            if (CPoolDatabase.isPostgres(conn))
                /*
                sSQLGeom="select ll.user_id from locks_layer ll,"+sTable+" t " +
                            "where t.id=? and ll.municipio=? and ll.layer=? " +
                            " and t.\"GEOMETRY\" && setsrid(locks_layer.\"GEOMETRY\","+srid.getSRID(iMunicipio)+");";
                */
                sSQLGeom="select ll.user_id from locks_layer ll,"+sTable+" t " +
                            "where t.id=? and ll.municipio=? and ll.layer=? " +
                            " and t.\"GEOMETRY\" && setsrid(ll.\"GEOMETRY\","+srid.getSRID(iMunicipio)+");";
            else /** Oracle */
                sSQLGeom="select ll.user_id from locks_layer ll,"+sTable+" t " +
                           "where t.id=? and ll.municipio=? and ll.layer=? " +
                           " and sdo_relate(t.geometry,ll.geometry, 'mask=anyinteract querytype=window') = 'TRUE'";

            ps=conn.prepareStatement(sSQLGeom);
            ps.setInt(1,iFeature);
            ps.setInt(2,iMunicipio);
            ps.setString(3,sLayer);
            rs=ps.executeQuery();
            if (rs.next())
                iRet=(rs.getInt("user_id")!=iUser)? AdministradorCartografiaClient.LOCK_LAYER_OTHER
                                                  : AdministradorCartografiaClient.LOCK_LAYER_OWN;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return iRet;
    }

    /**
     *
     * @param sLayer nombre del layer
     * @return el identificador del layer
     * @throws Exception
     */
    public int getIdLayer(String sLayer) throws Exception{
        int idLayer= -1;
        try{
            /** Se trata del identificador del layer */
            idLayer= Integer.parseInt(sLayer);
        }catch(NumberFormatException ex){
            /** Se trata del nombre del layer */
            String sSQL="select id_layer " +
                       "from layers " +
                       "where name= ?";

            Connection conn=null;
            PreparedStatement ps=null;
            ResultSet rs=null;
            try{
                conn=CPoolDatabase.getConnection();
                ps=conn.prepareStatement(sSQL);
                ps.setString(1,sLayer);
                rs=ps.executeQuery();
                if (rs.next()){
                    idLayer=rs.getInt("id_layer");
                }
            }finally{
                try{rs.close();}catch(Exception e){};
                try{ps.close();}catch(Exception e){};
                try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            }
        }

        return idLayer;
    }

    /**
     * Asocia un documento ya existente en BD a una lista de features
     * @param idLayers a los que pertenecen la lista de features 1 a 1
     * @param idFeatures
     * @param document
     * @param userSesion
     * @throws PermissionException
     * @throws LockException
     * @throws ACException
     * @throws Exception
     */
    public void linkDocument(Object[] idLayers, Object[] idFeatures, DocumentBean document, Sesion userSesion) throws PermissionException,LockException,ACException,Exception{

        String sSQL= "INSERT INTO ANEXOFEATURE (ID_DOCUMENTO, ID_LAYER, ID_FEATURE ) VALUES (?,?,?)";

        Connection conn=null;
        PreparedStatement ps= null;
		try {
			if (document == null) return;

            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            for (int j=0; j<idFeatures.length; j++){
                /* Comprobamos que el documento no este ya vinculado a la misma feature y layer */
                if (existeLink(getIdLayer((String)idLayers[j]), (String)idFeatures[j], document.getId())) continue;

                /** Para vincular un documento a una feature, es necesario que esta
                 * no este bloqueada por otro usuario y tener permisos. */
                checkPermissionLock((String)idLayers[j], (String)idFeatures[j], userSesion);

                ps=conn.prepareStatement(sSQL);
                ps.setString(1, document.getId());
                ps.setInt(2, getIdLayer((String)idLayers[j]));
                ps.setString(3, (String)idFeatures[j]);
                ps.execute();
                ps.close();
            }
            conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

    }

    /**
     * Comprueba si ya existe el enlace del docuemnto con la feature
     * @param idLayer al que pertenece la feature
     * @param idFeature
     * @param idDocumento
     * @return
     * @throws Exception
     */
    private boolean existeLink(int idLayer, String idFeature, String idDocumento) throws Exception{
        String sSQL= "SELECT * FROM anexofeature WHERE id_documento=? AND id_layer=? AND id_feature=?";

        boolean b= false;
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            ps.setString(1, idDocumento);
            ps.setInt(2, idLayer);
            ps.setString(3, idFeature);
            rs= ps.executeQuery();
            if (rs.next()) b= true;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return b;
    }


    private String getExtension(String filename){
        int i= filename.lastIndexOf('.');
        if(i>0 && i<filename.length()-1){
           return filename.substring(i+1).toLowerCase();
        }
        return "*";
    }

    public int getTipo(String extension) throws Exception{
        String sSQL= "SELECT TIPO, EXTENSION FROM DOCUMENTO_TIPOS WHERE upper(EXTENSION)=upper('"+extension+"') OR upper(EXTENSION)=upper('*')";
        int tipo= -1;
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            while (rs.next()){
                String ext= rs.getString("EXTENSION");
                tipo= rs.getInt("TIPO");
                if (ext.equalsIgnoreCase(extension)) break;
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return tipo;
    }


    public String getMimeType(int tipo) throws Exception{
        String sSQL= "SELECT MIME_TYPE FROM DOCUMENTO_TIPOS WHERE TIPO="+tipo;
        String mime_type= null;
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            if (rs.next()) mime_type= rs.getString("MIME_TYPE");
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return mime_type;
    }

    public String getExtensionFromMimeType(String mimetype) throws Exception{
        String sSQL= "SELECT EXTENSION FROM DOCUMENTO_TIPOS WHERE MIME_TYPE="+mimetype;
        String extension= null;
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            if (rs.next()) extension= rs.getString("EXTENSION");
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return extension;
    }

    /** INVENTARIO */
    /**
     * @param oos buffer donde se escribe el resultado de la operacion
     * @param id inventario
     * @throws Exception
     */
    public void returnAttachedInventarioDocuments(ObjectOutputStream oos, Object obj) throws Exception{
           try{
               if (obj==null) return;
               if (obj instanceof Long){
            	   BienBean aux= new BienBean();
            	   aux.setId((Long)obj);
            	   for (Iterator <DocumentBean>it=getAttachedInventarioDocuments(aux).iterator();it.hasNext();){
            		   oos.writeObject((DocumentBean)it.next());
            	   }
                }else{
            	   for (Iterator <DocumentBean> it=getAttachedInventarioDocuments(obj).iterator();it.hasNext();){
            		   oos.writeObject((DocumentBean)it.next());
            	   }
                }
           }catch(Exception e){
               logger.error("returnAttachedInventarioDocuments: ", e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    
    public void returnAttachedGestionCiudadDocuments(ObjectOutputStream oos, Long id) throws Exception{
        try{
            if (id==null) return;
            for (Iterator it=getAttachedGestionCiudadDocuments(id.longValue()).iterator();it.hasNext();){
                oos.writeObject((DocumentBean)it.next());
            }
        }catch(Exception e){
            logger.error("returnAttachedInventarioDocuments: ", e);
            oos.writeObject(new ACException(e));
            throw e;
        }
 }

    /**
     * Retorna el conjunto de documentos asociados a un bien de inventario
     * @param id del inventario
     * @return Collection de DocumentBean
     * @throws Exception
     */
    public Collection getAttachedInventarioDocuments(Object key) throws Exception{
        HashMap alRet= new HashMap();

        String sSQLBien= "SELECT d.id_documento, d.nombre, d.tamanio, " +
                "d.tipo, d.fecha_alta, d.fecha_modificacion, " +
                "d.comentario, d.publico, d.id_municipio, " +
                "d.is_imgdoctext, d.thumbnail, d.oculto " +
                "FROM documento d, anexo_inventario a " +
                "WHERE a.id_documento=d.id_documento AND " +
                "a.id_bien=? " +
                "ORDER BY nombre ASC";
        String sSQLLote= "SELECT d.id_documento, d.nombre, d.tamanio, " +
        		"d.tipo, d.fecha_alta, d.fecha_modificacion, " +
	        	"d.comentario, d.publico, d.id_municipio, " +
	        	"d.is_imgdoctext, d.thumbnail, d.oculto " +
	        	"FROM documento d, anexo_lote a " +
		        "WHERE a.id_documento=d.id_documento AND " +
		        "a.id_lote=? " +
		        "ORDER BY nombre ASC";
        String sSQLBienRevertible= "SELECT d.id_documento, d.nombre, d.tamanio, " +
				"d.tipo, d.fecha_alta, d.fecha_modificacion, " +
				"d.comentario, d.publico, d.id_municipio, " +
				"d.is_imgdoctext, d.thumbnail, d.oculto " +
				"FROM documento d, anexo_bien_revertible a " +
				"WHERE a.id_documento=d.id_documento AND " +
				"a.id_bien_revertible=? " +
				"ORDER BY nombre ASC";

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        DocumentBean document= null;
        try{
        	conn= CPoolDatabase.getConnection();
            if (key instanceof BienBean){
            	ps= conn.prepareStatement(sSQLBien);
            	ps.setLong(1, ((BienBean)key).getId());
            }else if (key instanceof Lote){
            	ps= conn.prepareStatement(sSQLLote);
            	ps.setLong(1, ((Lote)key).getId_lote());
            }else if (key instanceof BienRevertible){
            	ps= conn.prepareStatement(sSQLBienRevertible);
            	ps.setLong(1, ((BienRevertible)key).getId());
            }else 
            	return null;
            
            rs= ps.executeQuery();
            while (rs.next()){
                /** documentos sin contenido */
                document= new DocumentBean();
                document.setId(rs.getString("ID_DOCUMENTO"));
                document.setFileName(rs.getString("NOMBRE"));
                document.setSize(rs.getLong("TAMANIO"));
                document.setTipo(getMimeType(rs.getInt("TIPO")));
                document.setFechaEntradaSistema(rs.getTimestamp("FECHA_ALTA"));
                document.setFechaUltimaModificacion(rs.getTimestamp("FECHA_MODIFICACION"));
                document.setComentario(rs.getString("COMENTARIO"));
                document.setPublico(rs.getInt("PUBLICO"));
                document.setOculto(rs.getInt("OCULTO"));
                document.setIdMunicipio(rs.getString("ID_MUNICIPIO"));
                document.setIs_imgdoctext(rs.getString("IS_IMGDOCTEXT").charAt(0));
                /** GuiaUrbana: conn instanceof org.postgresql.jdbc3.Jdbc3Connection.
                 *  No se puede hacer el casting a java.sql.Connection, que es lo que espera como parametro CPoolDatabase.isPostgres() */
                if (conn instanceof org.postgresql.PGConnection){
                    document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                }else{
                    if (CPoolDatabase.isPostgres(conn))
                        document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                    else{
                        /** Oracle */
                        java.sql.Blob blob= rs.getBlob("THUMBNAIL");
                        if (blob!=null){
                            document.setThumbnail((byte[])blob.getBytes(1,new Long(blob.length()).intValue()));
                        }
                    }
                }

                alRet.put(document.getId(), document);
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return alRet.values();
    }
    
   
    
    public Collection getAttachedGestionCiudadDocuments(long id) throws Exception{
        HashMap alRet= new HashMap();

        String sSQL= "SELECT d.id_documento, d.nombre, d.tamanio, " +
                "d.tipo, d.fecha_alta, d.fecha_modificacion, " +
                "d.comentario, d.publico, d.id_municipio, " +
                "d.is_imgdoctext, d.thumbnail, d.oculto " +
                "FROM documento d, civil_work_documents a " +
                "WHERE a.id_document=d.id_documento AND " +
                "a.id_warning=? " +
                "ORDER BY nombre ASC";

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        DocumentBean document= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, id);
            rs= ps.executeQuery();
            while (rs.next()){
                /** documentos sin contenido */
                document= new DocumentBean();
                document.setId(rs.getString("ID_DOCUMENTO"));
                document.setFileName(rs.getString("NOMBRE"));
                document.setSize(rs.getLong("TAMANIO"));
                document.setTipo(getMimeType(rs.getInt("TIPO")));
                document.setFechaEntradaSistema(rs.getTimestamp("FECHA_ALTA"));
                document.setFechaUltimaModificacion(rs.getTimestamp("FECHA_MODIFICACION"));
                document.setComentario(rs.getString("COMENTARIO"));
                document.setPublico(rs.getInt("PUBLICO"));
                document.setOculto(rs.getInt("OCULTO"));
                document.setIdMunicipio(rs.getString("ID_MUNICIPIO"));
                document.setIs_imgdoctext(rs.getString("IS_IMGDOCTEXT").charAt(0));
                /** GuiaUrbana: conn instanceof org.postgresql.jdbc3.Jdbc3Connection.
                 *  No se puede hacer el casting a java.sql.Connection, que es lo que espera como parametro CPoolDatabase.isPostgres() */
                if (conn instanceof org.postgresql.PGConnection){
                    document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                }else{
                    if (CPoolDatabase.isPostgres(conn))
                        document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                    else{
                        /** Oracle */
                        java.sql.Blob blob= rs.getBlob("THUMBNAIL");
                        if (blob!=null){
                            document.setThumbnail((byte[])blob.getBytes(1,new Long(blob.length()).intValue()));
                        }
                    }
                }

                alRet.put(document.getId(), document);
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return alRet.values();
    }

    /**
     * @param oos buffer donde se escribe el resultado de la operacion
     * @param id inventario
     * @param document a asociar al bien de inventario anterior
     * @throws Exception
     */
    public void returnAttachInventarioDocument(ObjectOutputStream oos, Object key, DocumentBean document, Sesion userSesion, String idMunicipio) throws Exception{
           try{
               if (key==null) return;
               DocumentBean doc= attachInventarioDocument(key, document, userSesion, idMunicipio);
               oos.writeObject(doc);
           }catch(Exception e){
               logger.error("returnAttachInventarioDocument: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    

    public void returnAttachGestionCiudadDocument(ObjectOutputStream oos, Long id, DocumentBean document, Sesion userSesion) throws Exception{
        try{
            if (id==null) return;
            DocumentBean doc= attachGestionCiudadDocument(id.longValue(), document, userSesion);
            oos.writeObject(doc);
        }catch(Exception e){
            logger.error("returnAttachInventarioDocument: ",e);
            oos.writeObject(new ACException(e));
            throw e;
        }
 }

    /**
     * Asocia un documento a un bien de inventario
     * @param id del bien de inventario
     * @param document a asociar al bien de inventario anterior
     * @param userSesion
     * @return DocumentBean asociado al bien de inventario
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    public DocumentBean attachInventarioDocument(Object key, DocumentBean document, Sesion userSesion, String idMunicipio) throws PermissionException,LockException,Exception{
    	  if (document.getServerPath()!=null && document.getServerPath().length()>0){
    		 cargarDocumentServer(document); 
    	  }
          return attachInventarioDocument(key, document, userSesion, null, false, idMunicipio);
    }
    /**
     * Carga el documento de un path dentro del servidor.
     * Esto unicamente puede pasar cuando se realice una carga masiva del inventario
     * @param document
     * @return
     */
    public DocumentBean cargarDocumentServer(DocumentBean document) throws Exception{
    	try{
    		File documentFile=new File(document.getServerPath()+File.separator+document.getFileName());
    		document.setContent(DocumentoEnDisco.getContenido(documentFile));
    		document.setSize(document.getContent().length);
    		if (isImagen(document))
    			document.setThumbnail(com.geopista.protocol.document.Thumbnail.createThumbnail(documentFile.getAbsolutePath(), 20, 20));
    		return document;
    	}catch (Exception ex){
    		logger.info("Error al cargar el fichero: "+document.getFileName());
    		throw ex;
    	}
    }
    public DocumentBean attachGestionCiudadDocument(long id, DocumentBean document, Sesion userSesion) throws PermissionException,LockException,Exception{
        return attachGestionCiudadDocument(id, document, userSesion, null, false);
  }

    /**
     * Asocia un documento a un bien de inventario dentro de una transaccion
     * @param id del bien de inventario
     * @param document a asociar al bien de inventario anterior
     * @param userSesion
     * @param conn
     * @param transaccion true si se ejecuta dentro de una transaccion, false en caso contrario
     * @return DocumentBean asociado al bien de inventario
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    public DocumentBean attachInventarioDocument(Object key, DocumentBean document, Sesion userSesion, Connection conn, boolean transaccion, String idMunicipio) throws PermissionException,LockException,Exception{

        String sSQLDoc= "INSERT INTO DOCUMENTO (ID_DOCUMENTO, ID_MUNICIPIO, NOMBRE, FECHA_ALTA, FECHA_MODIFICACION, TIPO, COMENTARIO,  PUBLICO, TAMANIO, IS_IMGDOCTEXT, THUMBNAIL, OCULTO) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        String sSQLAnexoBien= "INSERT INTO ANEXO_INVENTARIO (ID_DOCUMENTO, ID_BIEN) VALUES (?,?)";
        String sSQLAnexoLote= "INSERT INTO ANEXO_LOTE (ID_DOCUMENTO, ID_LOTE) VALUES (?,?)";
        String sSQLAnexoBienRevertible= "INSERT INTO ANEXO_BIEN_REVERTIBLE (ID_DOCUMENTO, ID_BIEN_REVERTIBLE) VALUES (?,?)";
 

        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (document == null) return null;
            if (conn == null){
                conn= CPoolDatabase.getConnection();
                conn.setAutoCommit(false);
            }
			//Recuperacion del codigo de documento
			//Lo cambiamos para que sea un hash del documento y lo ponemos el identificador
			//de municipio para que luego sea muy sencillo borrarlo del disco.
            //long idDoc= CPoolDatabase.getNextValue("documento", "id_documento");
            
            String idDoc=getCodigoHash(document.getContent(),document.getFileName());
            /** Actualizamos el documento */
            //document.setId(idDoc);
            
            document.setIdMunicipio((idMunicipio!=null?idMunicipio:userSesion.getIdMunicipio()));
            //NUEVO           
            if(!AlfrescoManagerUtils.isAlfrescoActive())
            	document.setId(document.getIdMunicipio()+"_"+idDoc);
            //FIN NUEVO
             
            
            /** Buscamos si ya existe el documento*/
            DocumentBean documentoEnBD=getDocumentById(conn, document.getId(), idMunicipio);
            
            if (documentoEnBD==null){
            	/** Comprobamos que tenga permisos de publicacion de documentos */
            	if (!checkPermission(userSesion, com.geopista.protocol.document.Const.ACL_GENERAL, GeopistaPermission.PUBLICAR_DOCUMENTO)){
            		if (document.getPublico() == 1)
            			throw new PermissionException("PermissionException: " + GeopistaPermission.PUBLICAR_DOCUMENTO);
            	}
            
            	java.util.Date date= new java.util.Date();
            	ps= conn.prepareStatement(sSQLDoc);
            	ps.setString(1, document.getId());
            	 //BUG. PG8.4. Casting de tipos
            	//ps.setString(2, (idMunicipio!=null ? idMunicipio:userSesion.getIdMunicipio()));
               	ps.setInt(2, Integer.parseInt(idMunicipio!=null ? idMunicipio:userSesion.getIdMunicipio()));
            	ps.setString(3, document.getFileName());
            	ps.setTimestamp(4, new Timestamp(date.getTime()));
            	ps.setNull(5, java.sql.Types.TIMESTAMP);
            	ps.setInt(6, getTipo(getExtension(document.getFileName())));
            	ps.setString(7, document.getComentario());
            	/** El contenido lo guardamos en disco: Problemas con java.lang.OutOfMemoryError */
            	ps.setInt(8, document.getPublico());
            	ps.setLong(9, document.getSize());
            	ps.setString(10, new String(""+document.getIs_imgdoctext()));
            	ps.setBytes(11, document.getThumbnail());
            	ps.setInt(12, document.getOculto());
            	ps.execute();
            	ps.close();
            
                document.setFechaEntradaSistema(date);
                document.setTipo(getMimeType(getTipo(getExtension(document.getFileName()))));
            }else{
            	document=documentoEnBD;
            }
        	//Si el documento está en bd puede que ya este asociado al bien
            if ((documentoEnBD==null) || !existeLink(key, document.getId())){ 
            	if (key instanceof BienBean){
            		ps=conn.prepareStatement(sSQLAnexoBien);
            		ps.setString(1, document.getId());
            		ps.setLong(2, ((BienBean)key).getId());
            	} else if (key instanceof Lote){
            		ps=conn.prepareStatement(sSQLAnexoLote);
            		ps.setString(1, document.getId());
            		ps.setLong(2, ((Lote)key).getId_lote());
            	}else if (key instanceof BienRevertible){
            		ps=conn.prepareStatement(sSQLAnexoBienRevertible);
            		ps.setString(1, document.getId());
            		ps.setLong(2, ((BienRevertible)key).getId());
            	}
            	ps.execute();
            	ps.close();
            }
            
            //NUEVO           
            if(!AlfrescoManagerUtils.isAlfrescoActive() && documentoEnBD==null){
            	/** Guardamos el fichero en disco */
            	if (!transaccion)
            		DocumentoEnDisco.guardar(document);
            	else /** Guardamos el fichero en temporal */
            		DocumentoEnDisco.guardarEnTemporal(document);
            }    
            //FIN NUEVO
            /** siempre se retorna sin contenido */
            document.setContent(null);

            if (!transaccion) conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{if (!transaccion){
                conn.close();CPoolDatabase.releaseConexion();}
            }catch(Exception e){}
        }

        return document;
    }
    
     public DocumentBean attachGestionCiudadDocument(long id, DocumentBean document, Sesion userSesion, Connection conn, boolean transaccion) throws PermissionException,LockException,Exception{

        String sSQLDoc= "INSERT INTO DOCUMENTO (ID_DOCUMENTO, ID_MUNICIPIO, NOMBRE, FECHA_ALTA, FECHA_MODIFICACION, TIPO, COMENTARIO,  PUBLICO, TAMANIO, IS_IMGDOCTEXT, THUMBNAIL, OCULTO) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        String sSQLAnexo= "INSERT INTO civil_work_documents (id_document, id_warning,document_type,document_extension,document_name) VALUES (?,?,?,?,?)";

        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (document == null) return null;
            //String sIdSQL= "";
            if (conn == null){
                conn= CPoolDatabase.getConnection();
                conn.setAutoCommit(false);
            }

            /** Comprobamos que tenga permisos de publicacion de documentos */
            if (!checkPermission(userSesion, com.geopista.protocol.document.Const.ACL_GENERAL, GeopistaPermission.PUBLICAR_DOCUMENTO)){
                if (document.getPublico() == 1)
                    throw new PermissionException("PermissionException: " + GeopistaPermission.PUBLICAR_DOCUMENTO);
            }
            
			//Recuperacion del codigo de documento
			//Lo cambiamos para que sea un hash del documento y lo ponemos el identificador
			//de municipio para que luego sea muy sencillo borrarlo del disco.
            //long idDoc= CPoolDatabase.getNextValue("documento", "id_documento");
            
            String idDoc=getCodigoHash(document.getContent(),document.getFileName());
            
            /** Actualizamos el documento */
            //document.setId(idDoc);
            document.setIdMunicipio(userSesion.getIdMunicipio());
            //NUEVO           
            if(!AlfrescoManagerUtils.isAlfrescoActive())
            	document.setId(document.getIdMunicipio()+"_"+idDoc);
            //FIN NUEVO
                
            java.util.Date date= new java.util.Date();
            ps= conn.prepareStatement(sSQLDoc);
            ps.setString(1, document.getId());
            ps.setInt(2, Integer.parseInt(userSesion.getIdMunicipio()));
            ps.setString(3, document.getFileName());
            ps.setTimestamp(4, new Timestamp(date.getTime()));
            ps.setNull(5, java.sql.Types.TIMESTAMP);
//                ps.setInt(6, getTipo(getExtension(document.getFileName())));
            ps.setInt(6, getTipo(getExtension(document.getFileName())));
            
            ps.setString(7, document.getComentario());
            /** El contenido lo guardamos en disco: Problemas con java.lang.OutOfMemoryError */
            //ps.setBytes(8, null);
            ps.setInt(8, document.getPublico());
            ps.setLong(9, document.getSize());
            ps.setString(10, new String(""+document.getIs_imgdoctext()));
            ps.setBytes(11, document.getThumbnail());
            ps.setInt(12, document.getOculto());
            ps.execute();
            ps.close();

            
            
            document.setFechaEntradaSistema(date);
            document.setTipo(getMimeType(getTipo(getExtension(document.getFileName()))));
            
           
            ps=conn.prepareStatement(sSQLAnexo);
            ps.setString(1, document.getId());
            ps.setLong(2, id);
            ps.setString(3, "");
            ps.setString(4, "");
            ps.setString(5, document.getFileName());
            ps.execute();
            ps.close();

            /** Guardamos el fichero en disco */
            //NUEVO           
            if(!AlfrescoManagerUtils.isAlfrescoActive()){
	            if (!transaccion)
	                DocumentoEnDisco.guardar(document);
	            else /** Guardamos el fichero en temporal */
	                DocumentoEnDisco.guardarEnTemporal(document);
            }
            //FIN NUEVO

            /** siempre se retorna sin contenido */
            document.setContent(null);

            if (!transaccion) conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{if (!transaccion){
                conn.close();CPoolDatabase.releaseConexion();}
            }catch(Exception e){}
        }

        return document;
    }

    /**
     *
     * @param oos buffer donde se escribe el resultado de la operacion
     * @param id del bein de inventario
     * @param document a asociar al bien de inventario anterior
     * @throws Exception
     */
    public void returnLinkInventarioDocument(ObjectOutputStream oos, Object key, DocumentBean document) throws Exception{
           try{
               if (key==null) return;
               linkInventarioDocument(key, document);
           }catch(Exception e){
               logger.error("returnLinkInventarioDocument: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    
    public void returnLinkGestionCiudadDocument(ObjectOutputStream oos, Long id, DocumentBean document) throws Exception{
        try{
            if (id==null) return;
            linkGestionCiudadDocument(id.longValue(), document);
        }catch(Exception e){
            logger.error("returnLinkInventarioDocument: ",e);
            oos.writeObject(new ACException(e));
            throw e;
        }
 }

    /**
     * Asocia un documento a un bien de inventario
     * @param id del bien de inventario
     * @param document a asociar al bien anterior
     * @throws PermissionException
     * @throws LockException
     * @throws ACException
     * @throws Exception
     */
    private void linkInventarioDocument(Object key, DocumentBean document) throws PermissionException,LockException,ACException,Exception{
        linkInventarioDocument(key, document, null, false);
    }
    
    private void linkGestionCiudadDocument(long id, DocumentBean document) throws PermissionException,LockException,ACException,Exception{
        linkGestionCiudadDocument(id, document, null, false);
    }

    /**
     * Asocia un documento a un bien de inventario dentro de una transaccion
     * @param id del bien de inventario
     * @param document a asociar al bien anterior
     * @param conn
     * @param transaccion tru si se ejecuta dentro de una transaccion, false en caso contrario
     * @throws PermissionException
     * @throws LockException
     * @throws ACException
     * @throws Exception
     */
    private void linkInventarioDocument(Object key, DocumentBean document, Connection conn, boolean transaccion) throws PermissionException,LockException,ACException,Exception{

        String sSQLBien= "INSERT INTO ANEXO_INVENTARIO (ID_DOCUMENTO, ID_BIEN) VALUES (?,?)";
        String sSQLLote= "INSERT INTO ANEXO_LOTE (ID_DOCUMENTO, ID_LOTE) VALUES (?,?)";
        String sSQLBienRevertible= "INSERT INTO ANEXO_BIEN_REVERTIBLE (ID_DOCUMENTO, ID_BIEN_REVERTIBLE) VALUES (?,?)";

        PreparedStatement ps= null;
		try {
			if (document == null) return;
            if (conn == null){
                conn= CPoolDatabase.getConnection();
                conn.setAutoCommit(false);
            }

            /* Comprobamos que el documento no este ya vinculado al mismo bien de inventario */
            if (!existeLink(key, document.getId())){
            	if (key instanceof BienBean){
            		ps=conn.prepareStatement(sSQLBien);
                	ps.setString(1, document.getId());
                	ps.setLong(2, ((BienBean)key).getId());
                }else if (key instanceof Lote){
            		ps=conn.prepareStatement(sSQLLote);
                	ps.setString(1, document.getId());
                	ps.setLong(2, ((Lote)key).getId_lote());
                }else if (key instanceof BienRevertible){
            		ps=conn.prepareStatement(sSQLBienRevertible);
                	ps.setString(1, document.getId());
                	ps.setLong(2, ((BienRevertible)key).getId());
                }
                ps.execute();
                ps.close();
            }

            if (!transaccion) conn.commit();
            
        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
            try{if (!transaccion){
                conn.close();CPoolDatabase.releaseConexion();
            }}catch(Exception e){};
        }

    }
    
    private void linkGestionCiudadDocument(long id, DocumentBean document, Connection conn, boolean transaccion) throws PermissionException,LockException,ACException,Exception{

        String sSQL= "INSERT INTO civil_work_documents (id_document, id_warning) VALUES (?,?)";

        PreparedStatement ps= null;
		try {
			if (document == null) return;
            if (conn == null){
                conn= CPoolDatabase.getConnection();
                conn.setAutoCommit(false);
            }

            /* Comprobamos que el documento no este ya vinculado al mismo bien de inventario */
            if (!existeLink(id, document.getId())){
                ps=conn.prepareStatement(sSQL);
                ps.setString(1, document.getId());
                ps.setLong(2, id);
                ps.execute();
                ps.close();
            }

            if (!transaccion) conn.commit();
            
        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
            try{if (!transaccion){
                conn.close();CPoolDatabase.releaseConexion();
            }}catch(Exception e){};
        }

    }

    /**
     * Comprueba si ya existe un enlace del documento para el bien insertados como parametros
     * @param id del bien
     * @param idDocumento
     * @return true si existe el enlace, false en caso contrario
     * @throws Exception
     */
    private boolean existeLink(Object key, String idDocumento) throws Exception{
        String sSQLBien= "SELECT * FROM anexo_inventario WHERE id_documento=? AND id_bien=?";
        String sSQLLote= "SELECT * FROM anexo_lote WHERE id_documento=? AND id_lote=?";
        String sSQLBienRevertible= "SELECT * FROM anexo_bien_revertible WHERE id_documento=? AND id_bien_revertible=?";

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            if (key instanceof BienBean){
            	ps= conn.prepareStatement(sSQLBien);
            	ps.setString(1, idDocumento);
            	ps.setLong(2, ((BienBean)key).getId());
            }else if (key instanceof Lote){
            	ps= conn.prepareStatement(sSQLLote);
            	ps.setString(1, idDocumento);
            	ps.setLong(2, ((Lote)key).getId_lote());
            }else if (key instanceof BienRevertible){
            	ps= conn.prepareStatement(sSQLBienRevertible);
            	ps.setString(1, idDocumento);
            	ps.setLong(2, ((BienRevertible)key).getId());
            }
            rs= ps.executeQuery();
            if (rs.next()) return true;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return false;
    }

    /**
     * Chequea si el documento es una imagen
     * @param document
     * @return true si es una imagen, false en caso contario
     * @throws Exception
     */
    public boolean isImagen(DocumentBean document) throws Exception{
        try{
            String mimetype= getMimeType(getTipo(getExtension(document.getFileName())));
            if (mimetype!=null){
                if ((mimetype.toUpperCase().indexOf("IMAGE")) != -1) return true;
            }
        }catch(Exception e){}
        return false;
    }

    /**
     * Actualiza el tipo de un documento
     * @param oos buffer donde se escribe el resultado de la operacion
     * @param document
     * @throws Exception
     */
    public void returnUpdateTipoDocument(ObjectOutputStream oos, DocumentBean document) throws Exception{
           try{
               DocumentBean doc= updateTipoDocument(document);
               oos.writeObject(doc);
           }catch(Exception e){
               logger.error("returnUpdateTipoDocument: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Actualiza el tipo de un documento
     * @param document
     * @return el documento actualizado
     * @throws Exception
     */
    private DocumentBean updateTipoDocument(DocumentBean document) throws Exception{
        File file= new File(document.getFileName());
        document.setTipo(getMimeType(getTipo(getExtension(file.getName()))));
        if (isImagen(document)) document.setIsImagen();
        else{
            document.setThumbnail(null);
            document.setIsDocument();
        }
        return document;
    }

    /**
     * Borra el documento del bien pasados como parametros
     * @param oos buffer donde escribe el resultado de la opeacion
     * @param id del bien
     * @param document
     * @throws Exception
     */
    public void returnDetachInventarioDocument(ObjectOutputStream oos, Object key, DocumentBean document) throws Exception{
           try{
               detachInventarioDocument(key, document);
           }catch(Exception e){
               logger.error("returnDetachInventarioDocument: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    
    public void returnDetachGestionCiudadDocument(ObjectOutputStream oos, Long id, DocumentBean document) throws Exception{
        try{
            detachGestionCiudadDocument(id.longValue(), document);
        }catch(Exception e){
            logger.error("returnDetachInventarioDocument: ",e);
            oos.writeObject(new ACException(e));
            throw e;
        }
 }

    /**
     * Borra el documento del bien
     * @param id del bien
     * @param document
     * @throws PermissionException
     * @throws LockException
     * @throws ACException
     * @throws Exception
     */
    private void detachInventarioDocument(Object key, DocumentBean document) throws PermissionException,LockException,ACException,Exception{
    	detachInventarioDocument(key, document, null, false);
    }
    
    private void detachGestionCiudadDocument(long id, DocumentBean document) throws PermissionException,LockException,ACException,Exception{
    	BienBean aux= new BienBean();
    	aux.setId(id);
        detachInventarioDocument(aux, document, null, false);
    }

    /**
     * Borra la coleccion de documentos de un bien dentro de una transaccion
     * @param id del bien
     * @param documentos
     * @param conn
     * @param transaccion true si se realiza dentro de una transaccion, false en caso contrario
     * @throws PermissionException
     * @throws LockException
     * @throws ACException
     * @throws Exception
     */
    public void detachInventarioDocuments(Object key, Collection<DocumentBean> documentos, Connection conn, boolean transaccion) throws PermissionException,LockException,ACException,Exception{
        if (documentos == null) return;
        Object[] docs= documentos.toArray();
        for (int i=0; i<docs.length; i++){
            DocumentBean doc= (DocumentBean)docs[i];
            detachInventarioDocument(key, doc, conn, transaccion);
        }
    }
    
    public void detachGestionCiudadDocuments(long id, Collection<DocumentBean> documentos, Connection conn, boolean transaccion) throws PermissionException,LockException,ACException,Exception{
        if (documentos == null) return;
        Object[] docs= documentos.toArray();
        for (int i=0; i<docs.length; i++){
            DocumentBean doc= (DocumentBean)docs[i];
            detachGestionCiudadDocument(id, doc, conn, transaccion);
        }
    }

    /**
     * Borra un documento de un bien dentro de una transaccion
     * @param id del bien
     * @param document
     * @param conn
     * @param transaccion
     * @throws PermissionException
     * @throws LockException
     * @throws ACException
     * @throws Exception
     */
    public void detachInventarioDocument(Object key, DocumentBean document, Connection conn, boolean transaccion) throws PermissionException,LockException,ACException,Exception{

        String sSQLDoc= "DELETE FROM DOCUMENTO WHERE ID_DOCUMENTO=?";
        String sSQLAnexoBien= "DELETE FROM ANEXO_INVENTARIO " +
                             "WHERE ID_BIEN=? AND ID_DOCUMENTO=?";
        String sSQLAnexoLote= "DELETE FROM ANEXO_LOTE " +
        					"WHERE ID_LOTE=? AND ID_DOCUMENTO=?";
        String sSQLAnexoBienRevertible= "DELETE FROM ANEXO_BIEN_REVERTIBLE " +
							"WHERE ID_BIEN_REVERTIBLE=? AND ID_DOCUMENTO=?";
        boolean cerrarConexion=false;

        PreparedStatement ps= null;
		try {
			if (document == null) return;

            if (conn == null){
                conn= CPoolDatabase.getConnection();
                conn.setAutoCommit(false);
                cerrarConexion=true;
            }
            Long id= new Long(0);
            if (key instanceof BienBean){
            	ps= conn.prepareStatement(sSQLAnexoBien);
            	ps.setLong(1, ((BienBean)key).getId());
            	ps.setString(2, document.getId());
            	id=((BienBean)key).getId();
            }else if (key instanceof Lote){
            	ps= conn.prepareStatement(sSQLAnexoLote);
            	ps.setLong(1, ((Lote)key).getId_lote());
            	ps.setString(2, document.getId());
            	id=((Lote)key).getId_lote();
            }else if (key instanceof BienRevertible){
            	ps= conn.prepareStatement(sSQLAnexoBienRevertible);
            	ps.setLong(1, ((BienRevertible)key).getId());
            	ps.setString(2, document.getId());
            	id=((BienRevertible)key).getId();
            }
            else{
            	logger.error("El objeto no corresponde a un tipo fijo");
            }
            ps.execute();

            if (!asociadoAOtroBien(document.getId(), id)&& 
            		!asociadoAOtraFeature(document.getId(), -1, null)){
                /** borramos el docuemnto de la BD y de disco (si no corresponde a una transaccion de varias operaciones sobre los documentos) */
                ps.close();
                ps= conn.prepareStatement(sSQLDoc);
                ps.setString(1, document.getId());
                ps.execute();

                if (!transaccion)
                    DocumentoEnDisco.borrar(document);
            }
            if (!transaccion) conn.commit();
        }finally{
            try{ps.close();}catch(Exception e){};
            try{if (cerrarConexion){
                conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
        }
    }
    
   
    public void detachGestionCiudadDocument(long id, DocumentBean document, Connection conn, boolean transaccion) throws PermissionException,LockException,ACException,Exception{

        String sSQLDoc= "DELETE FROM DOCUMENTO WHERE ID_DOCUMENTO=?";
        String sSQLAnexo= "DELETE FROM civil_work_documents " +
                             "WHERE id_warning=? AND id_document=?";

        PreparedStatement ps= null;
		try {
			if (document == null) return;

            if (conn == null){
                conn= CPoolDatabase.getConnection();
                conn.setAutoCommit(false);
            }

            ps= conn.prepareStatement(sSQLAnexo);
            ps.setLong(1, id);
            ps.setString(2, document.getId());
            ps.execute();
     
            if (!asociadoAOtroBien(document.getId(),id ) && 
            		!asociadoAOtraFeature(document.getId(),-1,null)){
                /** borramos el docuemnto de la BD y de disco (si no corresponde a una transaccion de varias operaciones sobre los documentos) */
                ps.close();
                ps= conn.prepareStatement(sSQLDoc);
                ps.setString(1, document.getId());
                ps.execute();

                if (!transaccion)
                    DocumentoEnDisco.borrar(document);
            }
            if (!transaccion) conn.commit();
        }finally{
            try{ps.close();}catch(Exception e){};
            try{if (!transaccion){
                conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
        }
    }

    /**
     * Comprueba si un documento esta asociado a otro bien distinto al idBien pasado como parametro
     * @param idDocumento
     * @param idBien
     * @return true si el docuemnto esta asociado a otro bien, false en cado contrario
     * @throws Exception
     */
    private boolean asociadoAOtroBien(String idDocumento, Long id) throws Exception{
        String sSQLBien= "SELECT * FROM ANEXO_INVENTARIO WHERE ID_DOCUMENTO='" +idDocumento +
                     "' AND ID_BIEN<> ?";
        String sSQLLote= "SELECT * FROM ANEXO_LOTE WHERE ID_DOCUMENTO='" +idDocumento +
        "' AND ID_LOTE<> ?";
        String sSQLBienRevertible= "SELECT * FROM ANEXO_BIEN_REVERTIBLE WHERE ID_DOCUMENTO='" +idDocumento +
        "' AND ID_BIEN_REVERTIBLE<> ?";
        String sSQLFeature= "SELECT * FROM ANEXOFEATURE WHERE ID_DOCUMENTO='" +idDocumento +"'";

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQLBien);
            //Falla con el borrado con NULL (el por defecto)
            //NUEVO
            if(id!=null)
            	ps.setLong(1,id);
            else
            	ps.setNull(1, Types.INTEGER);
            //FIN NUEVO
            rs= ps.executeQuery();
            if (rs.next()) return true;
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            
            ps= conn.prepareStatement(sSQLLote);
            //NUEVO
            if(id!=null)
            	ps.setLong(1,id);
            else
            	ps.setNull(1, Types.INTEGER);
            //FIN NUEVO
            rs= ps.executeQuery();
            if (rs.next()) return true;
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            
            ps= conn.prepareStatement(sSQLBienRevertible);
            //NUEVO
            if(id!=null)
            	ps.setLong(1,id);
            else
            	ps.setNull(1, Types.INTEGER);
            //FIN NUEVO
            rs= ps.executeQuery();
            if (rs.next()) return true;
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            
            ps= conn.prepareStatement(sSQLFeature);
            rs= ps.executeQuery();
            if (rs.next()) return true;
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
          
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return false;
    }
    
    private boolean asociadoAOtraNota(long idDocumento, long idBien) throws Exception{
        String sSQL= "SELECT * FROM civil_work_documents WHERE id_document=" +idDocumento +
                     " AND id_warning<>"+idBien;
        boolean b= false;
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            if (rs.next()) b= true;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return b;
    }

    /**
     * Asocia los documentos a un bien dentro de una transaccion
     * @param id del bien
     * @param documentos
     * @param userSesion
     * @param conn
     * @return
     * @throws Exception
     */
    public Collection updateDocumentsInventario(Object key, Object[] documentos, Sesion userSesion, Connection conn, String idMunicipio) throws Exception{
        HashMap alRet= new HashMap();
		try {
            
			
            Collection c=getAttachedInventarioDocuments(key);
            
    		if (documentos == null) {
    			if (c!=null) docsBorrados.addAll(c);
    			return alRet.values();
    		}
            /** Comprobamos que ficheros han sido borrados */
	
            if (c != null){
                Object[] originales= c.toArray();
                for (int i=0; i<originales.length; i++){
                    DocumentBean d= (DocumentBean)originales[i];
                    /** Los ficheros de disco los borramos al final si TODAS las operaciones han ido bien */
                    if (borrado(d.getId(), documentos)){
                        detachInventarioDocument(key, d, conn, true);
                    	docsBorrados.add(d);
                    }
                }
            }
            /** Hacemos las inserciones y actualizaciones en BD y el fichero a disco en un directorio temporal */
            for (int i=0; i<documentos.length; i++){
                DocumentBean doc= (DocumentBean)documentos[i];
                if (doc.getId()==null){
                    /** Insertamos documento en BD. Copiamos el fichero en un dir. temporal */
                	//TODO. No entiendo bien porque no hay que comprobar nada.
                	//if (doc.getContent()!=null) IMP-->No poner esto porque si no no se sabe si ha ido bien
                	//if (doc.getContent()!=null)
                		doc=attachInventarioDocument(key, doc, userSesion, conn, true, idMunicipio);
                }else{
                    /** Hacemos una actualizacion del documento en BD. Copiamos el fichero en un dir. temporal */
                	//if (doc.getContent()!=null)
                		updateDocument(doc, userSesion, conn, true);
                    if (!existeLink(key, doc.getId())){
                        /** Enlazamos */
                        linkInventarioDocument(key, doc, conn, true);
                    }
                }
                alRet.put(doc.getId(), doc);
                documentos[i]=doc;
            }

        }catch (Exception e){
            throw e;
        }

        return alRet.values();

    }
    
    public Collection updateDocumentsGestionCiudad(long id, Object[] documentos, Sesion userSesion, Connection conn, String idMunicipio) throws Exception{
        HashMap alRet= new HashMap();
		try {
            
			if (documentos == null) return null;
            /** Comprobamos que ficheros han sido borrados */
			BienBean bien=new BienBean();
			bien.setId(id);
			
            Collection c= getAttachedInventarioDocuments(bien);
            if (c != null){
                Object[] originales= c.toArray();
                for (int i=0; i<originales.length; i++){
                    DocumentBean d= (DocumentBean)originales[i];
                    /** Los ficheros de disco los borramos al final si TODAS las operaciones han ido bien */
                    if (borrado(d.getId(), documentos)){
                        detachInventarioDocument(bien, d, conn, true);
                        docsBorrados.add(d);
                    }
                }
            }
            /** Hacemos las inserciones y actualizaciones en BD y el fichero a disco en un directorio temporal */
            for (int i=0; i<documentos.length; i++){
                DocumentBean doc= (DocumentBean)documentos[i];
                if (doc.getId() == null){
                    /** Insertamos documento en BD. Copiamos el fichero en un dir. temporal */
                   attachInventarioDocument(bien, doc, userSesion, conn, true, idMunicipio);
                }else{
                    /** Hacemos una actualizacion del documento en BD. Copiamos el fichero en un dir. temporal */
                    updateDocument(doc, userSesion, conn, true);
                    if (!existeLink(bien, doc.getId())){
                        /** Enlazamos */
                        linkInventarioDocument(bien, doc, conn, true);
                    }
                }
                alRet.put(doc.getId(), doc);
            }

        }catch (Exception e){
            throw e;
        }

        return alRet.values();

    }

    private boolean borrado(String id, Object[] documentos){
       for (int i=0; i<documentos.length; i++){
           DocumentBean d= (DocumentBean)documentos[i];
           if (d.getId().equals(id)) return false;
       }
        return true;
    }

    public Vector getDocsBorrados() {
        return docsBorrados;
    }

    /**
     * Borra los ficheros del los documentos asociados al bien de disco
     * @param idBien
     * @param c de documentos a borrar. Sólo se borrara si no esta asociado a otro bien
     */
    public void borrarFicherosEnDisco(Long id, Collection c){
        try{
            if (c == null) return;
            Object[] docs= c.toArray();
            for (int i=0; i<docs.length; i++){
                DocumentBean doc= (DocumentBean)docs[i];
                if (!asociadoAOtroBien(doc.getId(), id) && 
                		(!asociadoAOtraFeature(doc.getId(), -1, null)))
                    DocumentoEnDisco.borrar(doc);
            }
        }catch(Exception e){/** si el borrado falla, no damos error. Lo unico que puede pasar es que el fichero se quede descolgado en disco. */}
    }

    /**
     * Retorna el conjunto de documentos den BD de un tipo
     * @param oos buffer donde se deja el resultado
     * @param imgdoctext 'I' si es imagen, 'T' si es un archivo de texto, 'D' si es un documento
     * @throws Exception
     */
    public void returnFindDocuments(ObjectOutputStream oos, DocumentBean documento, String idMunicipio) throws Exception{
           try{
               for (Iterator <DocumentBean>it=findDocuments(documento, idMunicipio).iterator();it.hasNext();){
                   oos.writeObject((DocumentBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnDocuments: ", e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Retorna el conjunto de documentos en BD de tipo imgdoctext
     * @param imgdoctext 'I' si es imagen, 'T' si es un archivo de texto, 'D' si es un documento
     * @return un conjunto de documentos
     * @throws Exception
     */
    public Collection findDocuments(DocumentBean documentFound, String idMunicipio) throws Exception{
        HashMap alRet= new HashMap();
        String sSQL="";
            /** Inventario */
            sSQL= "SELECT d.id_documento, d.nombre, d.tamanio, " +
                    "d.tipo, d.fecha_alta, d.fecha_modificacion, " +
                    "d.comentario, d.publico, d.id_municipio, " +
                    "d.is_imgdoctext, d.thumbnail, d.oculto " +
                    "FROM documento d ";
            String sSQLFinal="";
            if (idMunicipio!=null)
            	sSQLFinal+=" where d.id_municipio='"+idMunicipio+"' ";
            if (documentFound.getFileName()!=null){
            	String documento=documentFound.getFileName().replaceAll("'", "''");
            	sSQLFinal+= (sSQLFinal.length()==00?" where ":" and ")+" upper(d.nombre) like upper('"+documento+"%') ";
            
            }
            
            if (documentFound.getSize()>0)
            	sSQLFinal+= (sSQLFinal.length()==00?" where ":" and ") +" d.tamanio='"+documentFound.getSize()+"' ";
            sSQL+=sSQLFinal+ "  ORDER BY nombre ASC";
        
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            DocumentBean document= null;
            while (rs.next()){
                /** documentos sin contenido */
                document= new DocumentBean();
                document.setId(rs.getString("ID_DOCUMENTO"));
                document.setFileName(rs.getString("NOMBRE"));
                document.setSize(rs.getLong("TAMANIO"));
                document.setTipo(getMimeType(rs.getInt("TIPO")));
                document.setFechaEntradaSistema(rs.getTimestamp("FECHA_ALTA"));
                document.setFechaUltimaModificacion(rs.getTimestamp("FECHA_MODIFICACION"));
                document.setComentario(rs.getString("COMENTARIO"));
                document.setPublico(rs.getInt("PUBLICO"));
                document.setOculto(rs.getInt("OCULTO"));
                document.setIdMunicipio(rs.getString("ID_MUNICIPIO"));
                document.setIs_imgdoctext(rs.getString("IS_IMGDOCTEXT").charAt(0));
                if (CPoolDatabase.isPostgres(conn))
                    document.setThumbnail((byte[])rs.getBytes("THUMBNAIL"));
                else{
                    /** Oracle */
                    java.sql.Blob blob= rs.getBlob("THUMBNAIL");
                    if (blob!=null){
                        document.setThumbnail((byte[])blob.getBytes(1,new Long(blob.length()).intValue()));
                    }
                }

                alRet.put(document.getId(), document);
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        return alRet.values();
    }
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    
    
	public String getCodigoHash(String path)
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		File f = new File(path);
		
		String nombreFichero=f.getName();
		//System.out.println("Nombre de fichero:"+nombreFichero);
		InputStream is = new FileInputStream(f);
		byte[] buffer = new byte[(int) f.length()];
		int read = 0;
		while ((read = is.read(buffer)) > 0) {
			digest.update(buffer, 0, read);
		}
		digest.update(nombreFichero.getBytes(),0,nombreFichero.length());
		
		byte[] md5sum = digest.digest();
		BigInteger bigInt = new BigInteger(1, md5sum);
		String output = bigInt.toString(16);
		is.close();
		return output;
	}
	
	public String getCodigoHash(byte[] data,String nombreFichero)
								throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		
		if (data==null)
			return String.valueOf(System.currentTimeMillis());
		digest.update(data,0,data.length);
		digest.update(nombreFichero.getBytes(),0,nombreFichero.length());

		byte[] md5sum = digest.digest();
		BigInteger bigInt = new BigInteger(1, md5sum);
		String output = bigInt.toString(16);
		return output;
	}
	
    public static void main(String args[]){
    	String fichero="c:\\tmp\\27997";
    	try {
    		
    		DocumentConnection doc=new DocumentConnection();
    		
    		File f1=new File("c:\\tmp\\27996");
    		File f2=new File("c:\\tmp\\27996");
    		File f3=new File("c:\\temp\\27996");
    		byte b1[]=doc.getBytesFromFile(f1);
    		byte b2[]=doc.getBytesFromFile(f2);
    		byte b3[]=doc.getBytesFromFile(f3);
    		
			String info=doc.getCodigoHash(b1,f1.getName());
			String info2=doc.getCodigoHash(b2,f2.getName());
			String info3=doc.getCodigoHash(b3,f3.getName());
			System.out.println("Info:"+info);
			System.out.println("Info:"+info2);
			System.out.println("Info:"+info3);
			if (info.equals(info2)){
				System.out.println("Iguales");
			}
			else
				System.out.println("Diferentes");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
