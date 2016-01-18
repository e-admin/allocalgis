package com.geopista.app.cementerios.managers;

import java.io.File;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.AnexoCementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.AnexoFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DocumentoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DocumentoTipoDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.AnexoCementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.AnexoFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DocumentoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DocumentoTipoDAO;
import com.geopista.app.cementerios.business.vo.AnexoCementerio;
import com.geopista.app.cementerios.business.vo.AnexoCementerioExample;
import com.geopista.app.cementerios.business.vo.AnexoFeatureExample;
import com.geopista.app.cementerios.business.vo.AnexoFeatureKey;
import com.geopista.app.cementerios.business.vo.Documento;
import com.geopista.app.cementerios.business.vo.DocumentoExample;
import com.geopista.app.cementerios.business.vo.DocumentoTipo;
import com.geopista.app.cementerios.business.vo.DocumentoTipoExample;


import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.document.DocumentBean;

import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.cementerios.document.DocumentoEnDisco;
import com.geopista.server.database.CPoolDatabase;



public class DocumentManager extends DAO {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DocumentManager.class);

	private static DocumentManager instance;

	private DocumentoDAO documentoDAO;
	private DocumentoTipoDAO documentoTipoDAO;
	private AnexoFeatureDAO anexoFeatureDAO;
	private AnexoCementerioDAO anexoCementerioDAO;
	
	private MappingManager mappingManager;

	public DocumentManager() throws SQLException {

		documentoDAO = new DocumentoDAOImpl();
		documentoTipoDAO = new DocumentoTipoDAOImpl();
		
		anexoFeatureDAO = new AnexoFeatureDAOImpl();
		anexoCementerioDAO = new AnexoCementerioDAOImpl();
		
		mappingManager = MappingManager.getIntance();
	}

	public static DocumentManager getInstance() throws SQLException {
		if (instance == null) {
			instance = new DocumentManager();
		}
		return instance;
	}

	/**************************************************************** TITULAR ****************************************************************************/

	/**
	 * Retorna el conjunto de documentos en BD de tipo imgdoctext
	 * 
	 * @param imgdoctext
	 *            'I' si es imagen, 'T' si es un archivo de texto, 'D' si es un
	 *            documento
	 * @return un conjunto de documentos
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getDocuments(char imgdoctext, String idMunicipio)throws Exception {

		HashMap alRet = new HashMap();
		String imgdoctextstr = new String(imgdoctext + "" );
		List listaDocumentos= null;

		DocumentoExample documentoExample = new DocumentoExample();
		
		if (imgdoctext == DocumentBean.ALL_CODE){
			if (idMunicipio != null) {
				documentoExample.createCriteria().andIdMunicipioEqualTo(Integer.parseInt(idMunicipio));
			}
			else {
				listaDocumentos = documentoDAO.selectAll();
			}
		}else{
			if (idMunicipio != null) {
				documentoExample.createCriteria()
						.andIsImgdoctextEqualTo(imgdoctextstr)
						.andIdMunicipioEqualTo(Integer.parseInt(idMunicipio));
			} else {
				documentoExample.createCriteria().andIsImgdoctextEqualTo(imgdoctextstr);
			}
		}
		if (listaDocumentos == null) {listaDocumentos = documentoDAO.selectByExampleWithBLOBs(documentoExample);}
		
		for (int i = 0; i < listaDocumentos.size(); i++) {
				Documento documentoVo = (Documento) listaDocumentos.get(i);

				DocumentBean documentoBean = mappingManager
						.mapDocumentoVoToBean(documentoVo);
				alRet.put(documentoBean.getId(), documentoBean);
			}
		
		return alRet.values();
	}


	public String getMimeType(int tipo) throws Exception {
		
		String mime_type = null;
		DocumentoTipo tiposDocumento;

		tiposDocumento = documentoTipoDAO.selectByPrimaryKey((short)tipo);
		mime_type = tiposDocumento.getMimeType();
		
		return mime_type;
	}

	public String getExtensionFromMimeType(String mimetype) throws Exception {
		
		DocumentoTipoExample documentoTipoExample = new DocumentoTipoExample();
		documentoTipoExample.createCriteria().andMimeTypeEqualTo(mimetype);
		
		List listaDocumentosTipo = documentoTipoDAO.selectByExample(documentoTipoExample);

		String extension = ((DocumentoTipo)listaDocumentosTipo.get(0)).getExtension();
		
		return extension;
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAttachedDocuments(String idLayer, String idFeature, char imgdoctext) throws Exception{
    	
		HashMap alRet = new HashMap();
		
        Map map = new HashMap();
	        map.put("idLayer",idLayer);
	        map.put("idFeature", idFeature);
	        map.put("imgdoctext", imgdoctext);
	        map.put("publico", 0);
        
        List listaDocumentos = documentoDAO.selectAttached(map);
        for (int i = 0; i < listaDocumentos.size(); i++) {
			Documento documentoVo = (Documento) listaDocumentos.get(i);
			DocumentBean documentoBean = mappingManager.mapDocumentoVoToBean(documentoVo);
				alRet.put(documentoBean.getId(), documentoBean);
			}
		
		return alRet.values();
    }
	

    /**
     * Guia Urbana. Retorna los documentos publicos asociados a una feature
     * @param idLayer layer de la feature
     * @param idFeature
     * @return documentos publicos asociados a una feature
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAttachedPublicDocuments(String idLayer, String idFeature) throws Exception{
    	
		HashMap alRet = new HashMap();
		
        Map map = new HashMap();
	        map.put("idLayer",idLayer);
	        map.put("idFeature", idFeature);
	        map.put("imgdoctext", DocumentBean.DOC_CODE);
	        map.put("publico", 1);
        
        List listaDocumentos = documentoDAO.selectAttached(map);
        for (int i = 0; i < listaDocumentos.size(); i++) {
			Documento documentoVo = (Documento) listaDocumentos.get(i);
			DocumentBean documentoBean = mappingManager.mapDocumentoVoToBean(documentoVo);
				alRet.put(documentoBean.getId(), documentoBean);
			}
		
		return alRet.values();
    }

    /**
     * Guia Urbana. Retorna las imagenes publicas asociadas a una feature
     * @param idLayer layer de la feature
     * @param idFeature
     * @return imagenes publicas asociadas a una feature
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAttachedPublicImages(String idLayer, String idFeature) throws Exception{
    
    	HashMap alRet = new HashMap();
		
        Map map = new HashMap();
	        map.put("idLayer",idLayer);
	        map.put("idFeature", idFeature);
	        map.put("imgdoctext", DocumentBean.IMG_CODE);
	        map.put("publico", 1);
        
        List listaDocumentos = documentoDAO.selectAttached(map);
        for (int i = 0; i < listaDocumentos.size(); i++) {
			Documento documentoVo = (Documento) listaDocumentos.get(i);
			DocumentBean documentoBean = mappingManager.mapDocumentoVoToBean(documentoVo);
				alRet.put(documentoBean.getId(), documentoBean);
			}
		
		return alRet.values();
        
    }
 
    /**
     * Guia Urbana. Retorna los archivos de texto publicos asociados a una feature
     * @param idLayer layer de la feature
     * @param idFeature
     * @return archivos de texto publicos asociados a una feature
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAttachedPublicTextos(String idLayer, String idFeature) throws Exception{
        
    	HashMap alRet = new HashMap();
		
        Map map = new HashMap();
	        map.put("idLayer",idLayer);
	        map.put("idFeature", idFeature);
	        map.put("imgdoctext", DocumentBean.TEXT_CODE);
	        map.put("publico", 1);
        
        List listaDocumentos = documentoDAO.selectAttached(map);
        for (int i = 0; i < listaDocumentos.size(); i++) {
			Documento documentoVo = (Documento) listaDocumentos.get(i);
			DocumentBean documentoBean = mappingManager.mapDocumentoVoToBean(documentoVo);
				alRet.put(documentoBean.getId(), documentoBean);
			}
		
		return alRet.values();

    }
    
    
    private String getExtension(String filename){
        int i= filename.lastIndexOf('.');
        if(i>0 && i<filename.length()-1){
           return filename.substring(i+1).toLowerCase();
        }
        return "*";
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public int getTipo(String extension) throws Exception{
    	
    	DocumentoTipoExample documentoTipoExample = new DocumentoTipoExample();
	    	List values = new ArrayList();
	    	values.add(extension.toUpperCase());
	    	values.add("*");
	    	
    	documentoTipoExample.createCriteria().andExtensionIn(values);
    	
    	List listaDocumentos = documentoTipoDAO.selectByExample(documentoTipoExample);
    	
    	DocumentoTipo documentoTipo;
    	String ext;
    	int tipo= -1;
    	for (int i = 0; i < listaDocumentos.size(); i++) {
    		 documentoTipo = (DocumentoTipo) listaDocumentos.get(i);
    		 ext = documentoTipo.getExtension();
    		 tipo = (int)documentoTipo.getTipo();
    		 if (ext.equalsIgnoreCase(extension)) break;
		}
    	return tipo;
    	
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
     * Asocia un documento a una lista de features
     * @param idLayers lista de layers de la lista de features 1 a 1
     * @param idFeatures lista de features
     * @param document
     * @param userSesion sesion de usuario
     * @return el documento actualizado
     * @throws Exception
     */
    public DocumentBean attachDocument(Object[] idLayers, Object[] idFeatures, DocumentBean document, Sesion userSesion, String idMunicipio) throws PermissionException,LockException,Exception{

    	try {
    		
    		getSqlMapClientTemplate().startTransaction();
    	
	    	/** Se inserta el documento **/
	    	Documento documentoVo = new Documento();
	    	
	    	documentoVo.setIdMunicipio((idMunicipio!=null?Integer.parseInt(idMunicipio):Integer.parseInt(userSesion.getIdMunicipio())));
	    	documentoVo.setNombre (document.getFileName());
	        documentoVo.setFechaAlta(new Date());
	        documentoVo.setFechaModificacion (document.getFechaUltimaModificacion());
	        documentoVo.setTipo((short)getTipo(getExtension(document.getFileName())));
	        documentoVo.setComentario(document.getComentario());
	        documentoVo.setPublico((short) document.getPublico());
	        documentoVo.setTamanio(document.getSize());
	        documentoVo.setIsImgdoctext( new String(""+ document.getIs_imgdoctext()));
	        documentoVo.setThumbnail(document.getThumbnail());
	        documentoVo.setOculto((short)document.getOculto());
	        
	        documentoDAO.insert(documentoVo);
	        
	        int id_documento = documentoDAO.selectByLastSeqKey();
	        String sIdDoc = String.valueOf(id_documento);
	        
	        /** Actualizamos el documento */
	        document.setId(sIdDoc);
	        document.setIdMunicipio((idMunicipio!=null?idMunicipio:userSesion.getIdMunicipio()));
	        document.setFechaEntradaSistema(new Date());
	        document.setTipo(getMimeType(getTipo(getExtension(document.getFileName()))));
	        
            for (int j=0; j<idFeatures.length; j++){
                /** Para vincular un documento a una feature, es necesario que esta
                 * no este bloqueada por otro usuario y tener permisos. */
//                checkPermissionLock((String)idLayers[j], (String)idFeatures[j], userSesion);
	            AnexoFeatureKey anexoFeatureKey = new AnexoFeatureKey();
	            anexoFeatureKey.setIdDocumento(id_documento);
	            anexoFeatureKey.setIdFeature(Integer.parseInt((String)idFeatures[j]));
	            anexoFeatureKey.setIdLayer(getIdLayer((String)idLayers[j]));
	            	
            }

            /** Guardamos el fichero en disco */
            DocumentoEnDisco.guardar(document);

            /** siempre se retorna sin contenido */
            document.setContent(null);
	        
	        getSqlMapClientTemplate().commitTransaction();
	        
    	}catch (Exception e) {
			logger.error("Error AttachDocument" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
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
    	
    	
   	try {
   		
   			//Falta por comprobar los permisos checkPermissionLock & checkPermission mediante  ANEXOFEATURE
    		
    		getSqlMapClientTemplate().startTransaction();
    	
	    	/** Se inserta el documento **/
	    	Documento documentoVo = new Documento();
	    	
	    	documentoVo.setIdDocumento(Integer.valueOf(document.getId()));
	    	documentoVo.setNombre (document.getFileName());
	        documentoVo.setFechaModificacion (new Date());
	        documentoVo.setComentario(document.getComentario());
	        documentoVo.setPublico((short) document.getPublico());
	        documentoVo.setOculto((short)document.getOculto());
	        
            if (document.getContent() != null){
            	documentoVo.setNombre(document.getFileName());
    	        documentoVo.setTipo((short)getTipo(getExtension(document.getFileName())));
    	        documentoVo.setTamanio(document.getSize());
    	        documentoVo.setIsImgdoctext( new String(""+ document.getIs_imgdoctext()));
    	        documentoVo.setThumbnail(document.getThumbnail());

            }


	        documentoDAO.updateByPrimaryKeySelective(documentoVo);
    	
    	
            /** guardamos el fichero en disco */
            if (document.getContent() != null){
                if (!transaccion)
                    DocumentoEnDisco.actualizar(document);
                else DocumentoEnDisco.guardarEnTemporal(document);
            }

            /** actualizamos el fichero */
            document.setFechaUltimaModificacion(new Date());
            document.setTipo(getMimeType(getTipo(getExtension(document.getFileName()))));
            /** siempre se retorna sin contenido */
            document.setContent(null);

          getSqlMapClientTemplate().commitTransaction();

        }finally{

            try{if (!transaccion){
            	getSqlMapClientTemplate().endTransaction();
            }}catch(Exception e){};
        }

        return document;
	     
    }
    
    
    /**
     * Comprueba si un documento esta asociado a otra fetaure distinta de idFeature
     * @param idDocumento
     * @param idLayer al que pertenece la feature
     * @param idFeature
     * @return
     * @throws Exception
     */
    private boolean asociadoAOtraFeature(long idDocumento, int idLayer, String idFeature) throws Exception{

		boolean b= false;
    	try{
    		AnexoFeatureExample anexoFeatureExample = new AnexoFeatureExample();
    		anexoFeatureExample.createCriteria().andIdDocumentoEqualTo((int)idDocumento)
    			.andIdLayerEqualTo(idLayer)
    			.andIdFeatureEqualTo(Integer.parseInt(idFeature));
    		
    		List<AnexoFeatureKey> listaAnexoFeatureKey =  anexoFeatureDAO.selectByExample(anexoFeatureExample);
    		
    		if (listaAnexoFeatureKey.size()>0) b=true;
    		
    	}catch (Exception e) {
    		logger.error("Error comprobando si está asociado a otra feature" + e);
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

    	try{
    		if (document == null) return;
    		
    		getSqlMapClientTemplate().startTransaction();
    		
       		AnexoFeatureExample anexoFeatureExample = new AnexoFeatureExample();
    		anexoFeatureExample.createCriteria().andIdDocumentoEqualTo(Integer.valueOf(document.getId()))
    			.andIdLayerEqualTo(getIdLayer(idLayer))
    			.andIdFeatureEqualTo(Integer.parseInt(idFeature));
    		
    		anexoFeatureDAO.deleteByExample(anexoFeatureExample);

    		documentoDAO.deleteByPrimaryKey(Integer.valueOf(document.getId()));
    		
    		getSqlMapClientTemplate().commitTransaction();
    		
    	}catch (Exception e) {
    		logger.error("error en el borrado del documento" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
				
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
    private boolean existeLink(int idLayer, String idFeature, long idDocumento) throws Exception{
    	
		boolean b= false;
    	try{
    		AnexoFeatureExample anexoFeatureExample = new AnexoFeatureExample();
    		anexoFeatureExample.createCriteria().andIdDocumentoEqualTo((int)idDocumento)
    			.andIdLayerEqualTo(idLayer)
    			.andIdFeatureEqualTo(Integer.parseInt(idFeature));
    		
    		List<AnexoFeatureKey> listaAnexoFeatureKey =  anexoFeatureDAO.selectByExample(anexoFeatureExample);
    		if (listaAnexoFeatureKey.size()>0) b=true;
    		
    	}catch (Exception e) {
    		logger.error("Error comprobando si está asociado a otra feature" + e);
		}
    	return b;	
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

    	try{
    		if (document == null) return;
    		
    		getSqlMapClientTemplate().startTransaction();
    		
            for (int j=0; j<idFeatures.length; j++){
                /* Comprobamos que el documento no este ya vinculado a la misma feature y layer */
                if (existeLink(getIdLayer((String)idLayers[j]), (String)idFeatures[j], Integer.valueOf(document.getId()))) continue;
                
		    	AnexoFeatureKey anexoFeatureKey = new AnexoFeatureKey();
		    	anexoFeatureKey.setIdDocumento(Integer.valueOf(document.getId()));
		    	anexoFeatureKey.setIdFeature((Integer)(idFeatures[j]));
		    	anexoFeatureKey.setIdLayer(getIdLayer((String)idLayers[j]));
		    	
		    	anexoFeatureDAO.insert(anexoFeatureKey);
            }
		    getSqlMapClientTemplate().commitTransaction();
            
    	}catch (Exception e) {
			logger.error("Error en el linkado de documento" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
   }

    
    /**
     * Retorna el conjunto de documentos asociados a un elemento de cementerio
     * @param id del elem del cementerio
     * @return Collection de DocumentBean
     * @throws Exception
     */
    public Collection getAttachedCementerioDocuments(Object key) throws Exception{
        HashMap alRet= new HashMap();

        int idElemCementerio = 0; 
        if (key instanceof ElemCementerioBean){
        	idElemCementerio = (int) ((ElemCementerioBean)key).getId();
        	String superpatron = (String) ((ElemCementerioBean)key).getSuperPatron();
        	String patron = (String) ((ElemCementerioBean)key).getPatron();
        List listaDocumentos = documentoDAO.selectAttachedCementerio(idElemCementerio, superpatron, patron);
        DocumentBean document= null;
        for (int i = 0; i < listaDocumentos.size(); i++) {
			Documento documentoVo = (Documento) listaDocumentos.get(i);

			DocumentBean documentoBean = mappingManager.mapDocumentoVoToBean(documentoVo);
			alRet.put(documentoBean.getId(), documentoBean);
		}
        }
		return alRet.values();

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
    	}else{
	    	if (key instanceof ElemCementerioBean)
	    		aux = (ElemCementerioBean) key;
    	}
    	return attachCementerioDocument((ElemCementerioBean)aux,superpatron, patron, document, userSesion, null, false, idMunicipio);
    }

    /**
     * Asocia un documento a un elemento de cementerio dentro de una transaccion
     * @param id del elemento de cementerio
     * @param document a asociar al elem de cementerio anterior
     * @param userSesion
     * @param conn
     * @param transaccion true si se ejecuta dentro de una transaccion, false en caso contrario
     * @return DocumentBean asociado al elem
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    public DocumentBean attachCementerioDocument(Object key, Object superpatron, Object patron, DocumentBean document, Sesion userSesion, Connection conn, boolean transaccion, String idMunicipio) throws PermissionException,LockException,Exception{

    	try {
    		if (document == null) return null;
    		
    		getSqlMapClientTemplate().startTransaction();
    	
	    	/** Se inserta el documento **/
	    	Documento documentoVo = new Documento();
	    	
	    	documentoVo.setIdMunicipio((idMunicipio!=null?Integer.parseInt(idMunicipio):Integer.parseInt(userSesion.getIdMunicipio())));
	    	documentoVo.setNombre (document.getFileName());
	        documentoVo.setFechaAlta(new Date());
	        documentoVo.setFechaModificacion (document.getFechaUltimaModificacion());
	        documentoVo.setTipo((short)getTipo(getExtension(document.getFileName())));
	        documentoVo.setComentario(document.getComentario());
	        documentoVo.setPublico((short) document.getPublico());
	        documentoVo.setTamanio(document.getSize());
	        documentoVo.setIsImgdoctext( new String(""+ document.getIs_imgdoctext()));
	      //  documentoVo.setThumbnail(document.getThumbnail());
	        documentoVo.setOculto((short)document.getOculto());
	        
	        documentoDAO.insert(documentoVo);
	        
	        int id_documento = documentoDAO.selectByLastSeqKey();
	        String sIdDoc = String.valueOf(id_documento);

	        /** Actualizamos el documento */
	        document.setId(sIdDoc);
	        document.setIdMunicipio((idMunicipio!=null?idMunicipio:userSesion.getIdMunicipio()));
	        document.setFechaEntradaSistema(new Date());
	        document.setTipo(getMimeType(getTipo(getExtension(document.getFileName()))));
    	
	        AnexoCementerio anexoCementerio = new AnexoCementerio();
	        anexoCementerio.setIdDocumento(id_documento);
	        anexoCementerio.setIdElemcementerio((int)((ElemCementerioBean)key).getId());

	        anexoCementerio.setTipo ((String)superpatron);
	        anexoCementerio.setSubtipo((String)patron);
	        
	        anexoCementerioDAO.insert(anexoCementerio);
	        
            /** Guardamos el fichero en disco */
            if (!transaccion)
                DocumentoEnDisco.guardar(document);
            else /** Guardamos el fichero en temporal */
                DocumentoEnDisco.guardarEnTemporal(document);

            /** siempre se retorna sin contenido */
            document.setContent(null);

            getSqlMapClientTemplate().commitTransaction();

    	}catch (Exception e) {
    		logger.error("error AttachCementerioDocument"+ e);
		}finally{
			
			getSqlMapClientTemplate().endTransaction();
		}
	        
       return document;
    }
    
    /**
     * Comprueba si ya existe un enlace del documento para el elem de cemnterio insertados como parametros
     * @param id del elem
     * @param idDocumento
     * @return true si existe el enlace, false en caso contrario
     * @throws Exception
     */
    public boolean existeLink(Object key, Object superpatron, Object patron, long idDocumento) throws Exception{
    	
    	boolean b = false;
    	ElemCementerioBean aux = null;
    	try{
        	if (key instanceof Long){
         	   aux= new ElemCementerioBean();
         	   aux.setId((Long)key);  
        	}else{
    	    	if (key instanceof ElemCementerioBean)
    	    		aux = (ElemCementerioBean) key;
        	}
	    	AnexoCementerioExample anexoCementerioExample = new AnexoCementerioExample();
	    	anexoCementerioExample.createCriteria()
	    		.andIdDocumentoEqualTo((int)idDocumento)
	    		.andIdElemcementerioEqualTo((int)((ElemCementerioBean)aux).getId())
	    		.andTipoEqualTo((String)superpatron)
	    		.andSubtipoEqualTo((String)patron);
	    		
	    	List listaAnexosCementerio = anexoCementerioDAO.selectByExample(anexoCementerioExample);
	    	if (listaAnexosCementerio.size()>0){
	    		b= true;
	    	}
    	}
    	catch (Exception e) {
    		logger.error("Error consultando si existe el link" + e);
		}
    		
    	return b;
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
     * Asocia un documento a un elem de cementerio dentro de una transaccion
     * @param id del elem de cementerio
     * @param document a asociar al elem anterior
     * @param conn
     * @param transaccion tru si se ejecuta dentro de una transaccion, false en caso contrario
     * @throws PermissionException
     * @throws LockException
     * @throws ACException
     * @throws Exception
     */
    public void linkCementerioDocument(Object key, Object superpatron, Object patron, DocumentBean document, Connection conn, boolean transaccion) throws PermissionException,LockException,ACException,Exception{

    	ElemCementerioBean aux = null; 
		try {
			if (document == null) return;
			
			getSqlMapClientTemplate().startTransaction();
			
            /* Comprobamos que el documento no este ya vinculado al mismo elem */
            if (!existeLink(key, superpatron, patron, Integer.valueOf(document.getId()))){
            	
            	if (key instanceof Long){
              	   aux= new ElemCementerioBean();
              	   aux.setId((Long)key);  
             	}else{
         	    	if (key instanceof ElemCementerioBean)
         	    		aux = (ElemCementerioBean) key;
             	}
//        	    AnexoCementerioKey anexoCementerioKey = new AnexoCementerioKey();
//        	    anexoCementerioKey.setIdDocumento((int)document.getId());
//        	    anexoCementerioKey.setIdElemcementerio((int)((ElemCementerioBean)aux).getId());

        	    AnexoCementerio anexoCementerio = new AnexoCementerio();
        	    anexoCementerio.setIdDocumento(Integer.valueOf(document.getId()));
        	    anexoCementerio.setIdElemcementerio((int)((ElemCementerioBean)aux).getId());
        	    anexoCementerio.setTipo((String)superpatron);
        	    anexoCementerio.setSubtipo((String)patron);

        	    anexoCementerioDAO.insert(anexoCementerio);
            }
           
            getSqlMapClientTemplate().commitTransaction();
            
        }catch (Exception e){
        	logger.error("error en el link de documento"+ e);
            throw e;
        }finally{
           getSqlMapClientTemplate().endTransaction();
        }

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
    public void detachCementerioDocument(Object key, Object superpatron, Object patron, DocumentBean document, Connection conn, boolean transaccion) throws PermissionException,LockException,ACException,Exception{

    	ElemCementerioBean aux= null;	
    	try {
			if (document == null) return;
				getSqlMapClientTemplate().startTransaction();
				
            	if (key instanceof Long){
               	   aux= new ElemCementerioBean();
               	   aux.setId((Long)key);  
              	}else{
          	    	if (key instanceof ElemCementerioBean)
          	    		aux = (ElemCementerioBean) key;
              	}
            		
        	    AnexoCementerio anexoCementerio = new AnexoCementerio();
        	    
        	    anexoCementerio.setIdDocumento(Integer.valueOf(document.getId()));
                anexoCementerio.setIdElemcementerio((int) ((ElemCementerioBean)key).getId());
        	    anexoCementerio.setTipo((String)superpatron);
        	    anexoCementerio.setSubtipo((String)patron);
                
      	        anexoCementerioDAO.deleteByPrimaryKey(anexoCementerio);
        	        
        	    if (!asociadoAOtroElem(Integer.valueOf(document.getId()), key)){
        	        	/** borramos el docuemnto de la BD y de disco (si no corresponde a una transaccion de varias operaciones sobre los documentos) */
        	    	documentoDAO.deleteByPrimaryKey(Integer.valueOf(document.getId()));

                 if (!transaccion)
                	 DocumentoEnDisco.borrar(document);
                 }
                    
        	     getSqlMapClientTemplate().commitTransaction();
        	    
    	}catch (Exception e) {
    		logger.error("error borranod un documento" + e);
		}finally{
			
			getSqlMapClientTemplate().endTransaction();
		}
    }
		
   public boolean asociadoAOtroElem(long idDocumento, Object key) throws Exception{
    	
    	boolean b = false;
    	ElemCementerioBean aux = null;
    	try{
	        	if (key instanceof Long){
	           	   aux= new ElemCementerioBean();
	           	   aux.setId((Long)key);  
	          	}else{
	      	    	if (key instanceof ElemCementerioBean)
	      	    		aux = (ElemCementerioBean) key;
	          	}
	    			
	    		AnexoCementerioExample anexoCementerioExample = new AnexoCementerioExample();
	    		anexoCementerioExample.createCriteria()
	    			.andIdDocumentoEqualTo((int)idDocumento);
//	    			.andIdElemcementerioEqualTo((int)((ElemCementerioBean)aux).getId());
	    		
	    		List listaDocumentos = anexoCementerioDAO.selectByExample(anexoCementerioExample);
	    		if (listaDocumentos.size()>0){
	    			b = true;
	    		}
    		
    	}catch (Exception e) {
			logger.error("Error consultando si asociado a otro bien" + e);
		}
    		
    	return b;
    }
    
   

   
}
