package com.geopista.server.cementerios.document;

import com.geopista.server.administradorCartografia.*;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.server.document.DocumentoEnDisco;
import com.geopista.app.cementerios.managers.DocumentManager;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.control.SesionUtils_LCGIII;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaAcl;

import java.io.*;
import java.sql.*;
import java.util.*;


public class CementeriosDocumentConnection{
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(CementeriosDocumentConnection.class);
    private NewSRID srid;
    private Vector docsBorrados= new Vector();
    
    
    private DocumentManager documentManager;

    public CementeriosDocumentConnection() throws SQLException{
    	documentManager = DocumentManager.getInstance();
    }

    public CementeriosDocumentConnection(NewSRID srid) throws SQLException{
        this.srid=srid;
        documentManager = DocumentManager.getInstance();
    }

    public void setSRID(NewSRID srid){
        this.srid=srid;
    }

    /**
     * Retorna el conjunto de documentos de BD de un tipo
     * @param oos buffer donde se deja el resultado
     * @param imgdoctext 'I' si es imagen, 'T' si es un archivo de texto, 'D' si es un documento
     * @throws Exception
     */
    public void returnDocuments(ObjectOutputStream oos, char imgdoctext, String idMunicipio) throws Exception{
           try{
               for (Iterator <DocumentBean>it=documentManager.getDocuments(imgdoctext, idMunicipio).iterator();it.hasNext();){
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
               for (Iterator it=documentManager.getAttachedDocuments(idLayer, idFeature, imgdoctext).iterator();it.hasNext();){
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
               DocumentBean doc= documentManager.attachDocument(idLayers, idFeatures, document, userSesion, idMunicipio);
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
               DocumentBean doc= documentManager.updateDocument(document, userSesion);
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
               documentManager.detachDocument(idLayer, idFeature, document, userSesion);
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
               documentManager.linkDocument(idLayers, idFeatures, document, userSesion);
           }catch(Exception e){
               logger.error("returnLinkDocument: ",e);
               oos.writeObject(new ACException(e));
               throw e;
           }
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
     * Comprueba si en un documento ha pasado de ser publico a privado o viceversa
     * @param idDocumento
     * @param newpublico nuevo valor del campo publico
     * @return true si ha cambiado el valor, false en caso contrario
     * @throws Exception
     */
    public boolean haCambiadoCampoPublico(long idDocumento, int newpublico) throws Exception{
        String sSQL= "SELECT publico FROM documento WHERE id_documento="+idDocumento;

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
        GeopistaAcl acl= SesionUtils_LCGIII.getPerfil(sSesion, lACL);
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


    /** CEMENTERIO */
    
    public void returnAttachedCementerioDocuments(ObjectOutputStream oos, Object obj, Object superPatron, Object patron) throws Exception{
        try{
            if (obj==null) return;
            if (obj instanceof Long){
         	   ElemCementerioBean aux= new ElemCementerioBean();
         	   aux.setId((Long)obj);
         	   aux.setSuperPatron((String) superPatron);
         	   aux.setPatron((String) patron);
         	   for (Iterator it = documentManager.getAttachedCementerioDocuments(aux).iterator();it.hasNext();){;
         		   oos.writeObject((DocumentBean)it.next());
         	   }
             }else{
         	   for (Iterator it = documentManager.getAttachedCementerioDocuments(obj).iterator();it.hasNext();){;
         		   oos.writeObject((DocumentBean)it.next());
         	   }
             }
        }catch(Exception e){
            logger.error("returnAttachedInventarioDocuments: ", e);
            oos.writeObject(new ACException(e));
            throw e;
        }
 }
    
    /**
     * @param oos buffer donde se escribe el resultado de la operacion
     * @param id inventario
     * @throws Exception
     */
    public void returnAttachedCementerioDocuments(ObjectOutputStream oos, Object obj) throws Exception{
           try{
               if (obj==null) return;
               if (obj instanceof Long){
            	   ElemCementerioBean aux= new ElemCementerioBean();
            	   aux.setId((Long)obj);
            	   for (Iterator it = documentManager.getAttachedCementerioDocuments(aux).iterator();it.hasNext();){;
            		   oos.writeObject((DocumentBean)it.next());
            	   }
                }else{
            	   for (Iterator it = documentManager.getAttachedCementerioDocuments(obj).iterator();it.hasNext();){;
            		   oos.writeObject((DocumentBean)it.next());
            	   }
                }
           }catch(Exception e){
               logger.error("returnAttachedInventarioDocuments: ", e);
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    
   
    /**
     * @param oos buffer donde se escribe el resultado de la operacion
     * @param id inventario
     * @param document a asociar al bien de inventario anterior
     * @throws Exception
     */
    public void returnAttachCementerioDocument(ObjectOutputStream oos, Object key, Object superpatron, Object patron, DocumentBean document, Sesion userSesion, String idMunicipio) throws Exception{
           try{
               if (key==null) return;
               DocumentBean doc= documentManager.attachCementerioDocument(key, superpatron, patron, document, userSesion, idMunicipio);
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
    public DocumentBean attachCementerioDocument(Object key, Object superpatron, Object patron, DocumentBean document, Sesion userSesion, String idMunicipio) throws PermissionException,LockException,Exception{
    	ElemCementerioBean aux = null;
        if (key instanceof Long){
     	   	aux= new ElemCementerioBean();
     	   	aux.setId((Long)key);
        }else if (key instanceof ElemCementerioBean){
        	aux = (ElemCementerioBean) key;
        }
        return documentManager.attachCementerioDocument((ElemCementerioBean)aux,  superpatron, patron, document, userSesion, null, false, idMunicipio);
    }

    /**
     *
     * @param oos buffer donde se escribe el resultado de la operacion
     * @param id del bein de inventario
     * @param document a asociar al bien de inventario anterior
     * @throws Exception
     */
    public void returnLinkCementerioDocument(ObjectOutputStream oos, Object key, Object superpatron, Object patron,  DocumentBean document) throws Exception{
           try{
               if (key==null) return;
               linkCementerioDocument(key, superpatron, patron, document);
           }catch(Exception e){
               logger.error("returnLinkCementerioDocument: ",e);
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
    @SuppressWarnings("unused")
	private void linkCementerioDocument(Object key,Object superpatron, Object patron, DocumentBean document) throws PermissionException,LockException,ACException,Exception{
        documentManager.linkCementerioDocument(key, superpatron, patron, document, null, false);
    }
    


    /**
     * Borra el documento del bien pasados como parametros
     * @param oos buffer donde escribe el resultado de la opeacion
     * @param id del bien
     * @param document
     * @throws Exception
     */
    public void returnDetachCementerioDocument(ObjectOutputStream oos, Object key, Object superPatron, Object patron, DocumentBean document) throws Exception{
           try{
               detachCementerioDocument(key,superPatron, patron, document);
           }catch(Exception e){
               logger.error("returnDetachDocument: ",e);
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
    private void detachCementerioDocument(Object key, Object superPatron, Object patron, DocumentBean document) throws PermissionException,LockException,ACException,Exception{
    	ElemCementerioBean aux = null;
        if (key instanceof Long){
     	   	aux= new ElemCementerioBean();
     	   	aux.setId((Long)key);
        }else if (key instanceof ElemCementerioBean){
        	aux = (ElemCementerioBean) key;
        }
     	documentManager.detachCementerioDocument((ElemCementerioBean)aux, superPatron, patron, document, null, false);
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
    public void detachCementerioDocuments(Object key ,Object superPatron, Object patron, Collection documentos, Connection conn, boolean transaccion) throws PermissionException,LockException,ACException,Exception{
        if (documentos == null) return;
        Object[] docs= documentos.toArray();
        for (int i=0; i<docs.length; i++){
            DocumentBean doc= (DocumentBean)docs[i];
            documentManager.detachCementerioDocument(key, superPatron, patron, doc, conn, transaccion);
        }
    }



    private boolean borrado(long id, Object[] documentos){
       for (int i=0; i<documentos.length; i++){
           DocumentBean d= (DocumentBean)documentos[i];
           if (Integer.valueOf(d.getId()) == id) return false;
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
    public void borrarFicherosEnDisco(Object key, Collection c){
        try{
            if (c == null) return;
            Object[] docs= c.toArray();
            for (int i=0; i<docs.length; i++){
                DocumentBean doc= (DocumentBean)docs[i];
                if (!documentManager.asociadoAOtroElem(Integer.valueOf(doc.getId()), key))
                    DocumentoEnDisco.borrar(doc);
            }
        }catch(Exception e){/** si el borrado falla, no damos error. Lo unico que puede pasar es que el fichero se quede descolgado en disco. */}
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
    public Collection updateDocumentsCementerio(Object key,  Object superpatron, Object patron, Object[] documentos, Sesion userSesion, Connection conn, String idMunicipio) throws Exception{
        HashMap alRet= new HashMap();
 		try {
            
 			
            Collection c= null;
            if (key instanceof ElemCementerioBean){
            	c=documentManager.getAttachedCementerioDocuments(key);
            }
            
    		if (documentos == null) {
    			docsBorrados.addAll(c);
    			return alRet.values();
    		}
            /** Comprobamos que ficheros han sido borrados */
 	
            if (c != null){
                Object[] originales= c.toArray();
                for (int i=0; i<originales.length; i++){
                    DocumentBean d= (DocumentBean)originales[i];
                    /** Los ficheros de disco los borramos al final si TODAS las operaciones han ido bien */
                    if (borrado(Integer.valueOf(d.getId()), documentos)){
                    	if (key instanceof ElemCementerioBean)
                    		documentManager.detachCementerioDocument((ElemCementerioBean)key, superpatron, patron, d, conn, true);
                    	docsBorrados.add(d);
                    }
                }
            }
            /** Hacemos las inserciones y actualizaciones en BD y el fichero a disco en un directorio temporal */
            for (int i=0; i<documentos.length; i++){
                DocumentBean doc= (DocumentBean)documentos[i];
                if (doc.getId() == null){  //if (doc.getId() == -1){
                    /** Insertamos documento en BD. Copiamos el fichero en un dir. temporal */
                	documentManager.attachCementerioDocument(key, superpatron, patron, doc, userSesion, conn, true, idMunicipio);
                }else{
                    /** Hacemos una actualizacion del documento en BD. Copiamos el fichero en un dir. temporal */
                	documentManager.updateDocument(doc, userSesion, conn, true);
                    if (!documentManager.existeLink(key, superpatron, patron, Integer.valueOf(doc.getId()))){
                        /** Enlazamos */
                    	documentManager.linkCementerioDocument(key,  superpatron, patron, doc, conn, true);
                    }
                }
                alRet.put(new Long(doc.getId()), doc);
            }

        }catch (Exception e){
            throw e;
        }

        return alRet.values();

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
        document.setTipo(documentManager.getMimeType(documentManager.getTipo(getExtension(file.getName()))));
        if (isImagen(document)) document.setIsImagen();
        else{
            document.setThumbnail(null);
            document.setIsDocument();
        }
        return document;
    }
    
    /**
     * Chequea si el documento es una imagen
     * @param document
     * @return true si es una imagen, false en caso contario
     * @throws Exception
     */
    public boolean isImagen(DocumentBean document) throws Exception{
        try{
            String mimetype= documentManager.getMimeType(documentManager.getTipo(getExtension(document.getFileName())));
            if (mimetype!=null){
                if ((mimetype.toUpperCase().indexOf("IMAGE")) != -1) return true;
            }
        }catch(Exception e){}
        return false;
    }
    
    private String getExtension(String filename){
        int i= filename.lastIndexOf('.');
        if(i>0 && i<filename.length()-1){
           return filename.substring(i+1).toLowerCase();
        }
        return "*";
    }

}
