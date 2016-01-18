package com.geopista.app.cementerios.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.CementerioFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DifuntoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ElemFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.IntervencionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PlazaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.elem_cementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DifuntoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ElemFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.IntervencionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PlazaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.elem_cementerioDAO;
import com.geopista.app.cementerios.business.vo.CementerioFeatureKey;
import com.geopista.app.cementerios.business.vo.Difunto;
import com.geopista.app.cementerios.business.vo.ElemFeature;
import com.geopista.app.cementerios.business.vo.ElemFeatureExample;
import com.geopista.app.cementerios.business.vo.Inhumacion;
import com.geopista.app.cementerios.business.vo.InhumacionExample;
import com.geopista.app.cementerios.business.vo.Intervencion;
import com.geopista.app.cementerios.business.vo.IntervencionExample;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.cementerios.IntervencionBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.document.DocumentoEnDisco;

public class IntervencionManager extends DAO {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(IntervencionManager.class);
	
	private static IntervencionManager instance;
	
    private IntervencionDAO intervencionDAO;
    private ElemFeatureDAO elemFeatureDAO;
    
    private MappingManager mappingManager;
    
    
    public IntervencionManager(){
        intervencionDAO = new IntervencionDAOImpl();
        elemFeatureDAO = new ElemFeatureDAOImpl();
        
        mappingManager = mappingManager.getIntance();
    }
    
    public static IntervencionManager getInstance(){
    	if (instance == null){
    		instance = new IntervencionManager();
    	}
    	return instance;
    }
	
    /*********************************************************** INTERVENCIONES **********************************************************************/
    
    /**
     * 
     */
    public IntervencionBean insertIntervencion (Object[] idLayers, Object[] idFeatures, IntervencionBean elem, Integer idCementerio, Sesion userSesion) throws PermissionException,LockException,Exception{
    	
		try {

            for (int j=0; j<idFeatures.length; j++){

            	/**Start transaccion**/
				getSqlMapClientTemplate().startTransaction();
				
				int idIntervencion = -1;
    		
				Intervencion intervencion = new Intervencion();
				intervencion.setLocalizacion(elem.getLocalizacion());
				intervencion.setCodigo(elem.getCodigo());
				intervencion.setEstado(elem.getEstado());
				intervencion.setFechaInicio(elem.getFechaInicio());
				intervencion.setFechaFin(elem.getFechaFin());
				intervencion.setInforme(elem.getInforme());
				
				intervencionDAO.insert(intervencion);
		
				idIntervencion = intervencionDAO.selectByLastSeqKey();

				elem.setId_intervencion(idIntervencion);
				
	    		//1.Insert el cementerioElem generico --> insertElemEnterramiento(elem);
				ElemFeature elemFeature = new ElemFeature();
					elemFeature.setIdCementerio(idCementerio);
					elemFeature.setIdFeature(Integer.parseInt((String)idFeatures[j]));
					elemFeature.setIdLayer((String)idLayers[j]);
					elemFeature.setIdCementerio(idCementerio);
					elemFeature.setIdElem(idIntervencion);
					elemFeature.setEntidad(elem.getEntidad());
					elemFeature.setIdMunicipio(Integer.parseInt(elem.getIdMunicipio()));
					elemFeature.setNombre(elem.getNombreCementerio());
					elemFeature.setTipo(Const.SUPERPATRON_GINTERVENCIONES);
					elemFeature.setSubtipo(Const.PATRON_INTERVENCION);
			
					elemFeatureDAO.insert(elemFeature);
			
					int idElemFeature = elemFeatureDAO.selectByLastSeqKey();
					elemFeature.setId(idElemFeature);

					/** Actualizamos los ficheros en disco (temporal --> destino) */
					DocumentoEnDisco.actualizarConFicherosDeTemporal(((IntervencionBean) elem).getDocumentos());
					
					getSqlMapClientTemplate().commitTransaction();
					
	        }

		}catch (Exception e) {
			logger.error("Error en la insercion de una inhumacion" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
		
		return elem;
	}
    
    
    /**
     * updateIntervencion
     * @param idLayers
     * @param idFeatures
     * @param elem
     * @param idCementerio
     * @param userSesion
     * @return
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    
    public IntervencionBean updateIntervencion (IntervencionBean elem, Sesion userSesion)
	throws PermissionException, LockException, Exception {

		try {
			/**Start transaccion**/
			getSqlMapClientTemplate().startTransaction();
			
			Intervencion intervencion = new Intervencion();

				intervencion.setLocalizacion(elem.getLocalizacion());
				intervencion.setCodigo(elem.getCodigo());
				intervencion.setEstado(elem.getEstado());
				intervencion.setFechaInicio(elem.getFechaInicio());
				intervencion.setFechaFin(elem.getFechaFin());
				intervencion.setInforme(elem.getInforme());
				intervencion.setId(elem.getId_intervencion());
				
				intervencionDAO.updateByPrimaryKeySelective(intervencion);
		
				/** Actualizamos los ficheros en disco (temporal --> destino) */
				DocumentoEnDisco.actualizarConFicherosDeTemporal(((IntervencionBean) elem).getDocumentos());

				getSqlMapClientTemplate().commitTransaction();

		}catch (Exception e) {
			logger.error("Error en la insercion de una inhumacion" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
		
		return elem;
	}
    
    /**
     * deleteIntervencion
     * @param idLayers
     * @param idFeatures
     * @param elem
     * @param idCementerio
     * @param userSesion
     * @return
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
	public IntervencionBean deleteIntervencion(Object[] idLayers, Object[] idFeatures, IntervencionBean elem, Integer idCementerio, Sesion userSesion)
	throws PermissionException, LockException, Exception {

		try {
			/**Start transaccion**/
			getSqlMapClientTemplate().startTransaction();
			
			//Tengo que eliminar la entrada de elem_feature
			ElemFeatureExample elemFeatureExample = new ElemFeatureExample();
			elemFeatureExample.createCriteria().andIdElemEqualTo(elem.getId_intervencion());
			
			elemFeatureDAO.deleteByExample(elemFeatureExample);
			
			intervencionDAO.deleteByPrimaryKey(elem.getId_intervencion());
			
			getSqlMapClientTemplate().commitTransaction();
			
		}catch (Exception e) {
			logger.error("Error en el borrado de una intervencion" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
		
		return elem;
	}
    
	/**
	 * getAllIntervencionesByCementerio
	 * @param idCementerio
	 * @return
	 * @throws SQLException
	 */
    public Collection getAllIntervencionesByCementerio (Integer idCementerio) throws SQLException{
    	
    	Intervencion intervencionVO = null;
    	List listaIntervenciones = new ArrayList<Intervencion>();
    	
    	
    	HashMap alRet= new HashMap();
    	
    	try{
    		
    		listaIntervenciones = intervencionDAO.selectAll();
    		ElemFeature elemFeature;
    		IntervencionBean intervencion;
    		
    		for (int i = 0; i < listaIntervenciones.size(); i++) {
				intervencionVO = (Intervencion) listaIntervenciones.get(i);
				
				elemFeature = elemFeatureDAO.selectByElemAndCementerio(intervencionVO.getId(), idCementerio);
    			if (elemFeature!= null){
    				intervencion  = mappingManager.mapIntervencionVOToBean(intervencionVO);
    				
    		
    				intervencion.setIdMunicipio(String.valueOf(elemFeature.getIdMunicipio()));
    				intervencion.setEntidad(elemFeature.getEntidad());
    				intervencion.setNombreCementerio(elemFeature.getNombre());
    				intervencion.setId((int)elemFeature.getId());
    				intervencion.setIdCementerio(idCementerio);
    				
    				Collection c = new ArrayList(); 
    				c.add(elemFeature.getIdFeature()); 
    				intervencion.setIdFeatures( c);
    				Collection c2 = new ArrayList(); 
    				c2.add(elemFeature.getIdLayer());
    				intervencion.setIdLayers(c2);
    				alRet.put(intervencion.getId_intervencion(), intervencion);
    			}
    		}
    	}catch (Exception e) {
    		logger.error("Error al obtener las intervenciones"+ e);
		}
    	
    	return alRet.values();
    }  
    
    
    /**
     * getIntervencionesByFeature
     * @param superpatron
     * @param patron
     * @param filtro
     * @param idLayers
     * @param idFeatures
     * @param idCementerio
     * @param userSesion
     * @return
     * @throws Exception
     */
	public Collection getIntervencionesByFeature(String superpatron, String patron,Object[] filtro, Object[] idLayers, Object[] idFeatures,
			Integer idCementerio, Sesion userSesion) throws Exception {

    	Intervencion intervencionVO = null;
    	List listaIntervenciones = new ArrayList<Intervencion>();

		HashMap alRet = new HashMap();
		
    	try{
    		
    		listaIntervenciones = intervencionDAO.selectAll();
    		ElemFeature elemFeature;
    		IntervencionBean intervencion;
    		
    		for (int i = 0; i < listaIntervenciones.size(); i++) {
				intervencionVO = (Intervencion) listaIntervenciones.get(i);
				
				for (int j = 0; j < idLayers.length; j++) {

					for (int k = 0; k < idFeatures.length; k++) {
						
					elemFeature = elemFeatureDAO.selectByElemCementerioLayerAndFeature(intervencionVO.getId(), idCementerio, 
							(String)idLayers[j], (String)idFeatures[k]);
					
					if (elemFeature!= null){
						intervencion  = mappingManager.mapIntervencionVOToBean(intervencionVO);
						
	    				intervencion.setIdMunicipio(String.valueOf(elemFeature.getIdMunicipio()));
	    				intervencion.setEntidad(elemFeature.getEntidad());
	    				intervencion.setNombreCementerio(elemFeature.getNombre());
	    				intervencion.setId((int)elemFeature.getId());
	    				intervencion.setIdCementerio(idCementerio);
	    				
	    				Collection c = new ArrayList(); 
	    				c.add(elemFeature.getIdFeature()); 
	    				intervencion.setIdFeatures( c);
	    				Collection c2 = new ArrayList(); 
	    				c2.add(elemFeature.getIdLayer());
	    				intervencion.setIdLayers(c2);
	    				alRet.put(intervencion.getId_intervencion(), intervencion);
						
						}
					}
				}
    		}
    	}catch (Exception e) {
    		logger.error("Error obteniendo intervenciones en feature" + e);
		}
    	
    	return alRet.values();
	}
	
	
	public Collection getIntervencionesByFilter(String superpatron,String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<Intervencion> listaIntervenciones;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		IntervencionExample intervencionExample = new IntervencionExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.IntervencionExample.Criteria criteria = intervencionExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.IntervencionExample.Criteria) 
				addFilter.addIntervencionFilter(criteria, campoFiltro, false);
			}
			intervencionExample.or(criteria);
			listaIntervenciones = intervencionDAO.selectByExample(intervencionExample);

			for (int j = 0; j < listaIntervenciones.size(); j++) {
				Intervencion intervencion = (Intervencion) listaIntervenciones.get(j);
				IntervencionBean intervencionBean = mappingManager.mapIntervencionVOToBean(intervencion);

				ElemFeature elemFeature = elemFeatureDAO.selectByElemAndCementerio(intervencion.getId(), idCementerio);
    			if (elemFeature!= null){
    				
    				intervencionBean.setIdMunicipio(String.valueOf(elemFeature.getIdMunicipio()));
    				intervencionBean.setEntidad(elemFeature.getEntidad());
    				intervencionBean.setId((int)elemFeature.getId());
    				intervencionBean.setIdCementerio(idCementerio);
				
					Collection c = new ArrayList(); 
					c.add(elemFeature.getIdFeature()); 
					intervencionBean.setIdFeatures( c);
					Collection c2 = new ArrayList(); 
					c2.add(elemFeature.getIdLayer());
					intervencionBean.setIdLayers(c2);
    			}
				alRet.put(intervencionBean.getId_intervencion(), intervencionBean);
			}
		}
		
		return alRet.values();
	}	
		
	/**
	 * findInhumaciones
	 * @param superpatron
	 * @param patron
	 * @param filtro
	 * @param idCementerio
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	public Collection findIntervenciones(String superpatron,String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<Intervencion> listaIntervenciones = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		IntervencionExample intervencionExample = new IntervencionExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.IntervencionExample.Criteria criteria = intervencionExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.IntervencionExample.Criteria) 
				addFilter.addIntervencionFilter(criteria, campoFiltro, true);
			
			if (criteria == null) continue;	
				
			intervencionExample.or(criteria);
			listaIntervenciones = intervencionDAO.selectByExample(intervencionExample);

			
			if ((listaIntervenciones != null) && (listaIntervenciones.size()>0)) break; 
			intervencionExample.clear();
			}
			for (int j = 0; j < listaIntervenciones.size(); j++) {
				Intervencion intervencion = (Intervencion) listaIntervenciones.get(j);
				IntervencionBean intervencionBean = mappingManager.mapIntervencionVOToBean(intervencion);
				

				ElemFeature elemFeature = elemFeatureDAO.selectByElemAndCementerio(intervencion.getId(), idCementerio);
    			if (elemFeature!= null){
    				
    				intervencionBean.setIdMunicipio(String.valueOf(elemFeature.getIdMunicipio()));
    				intervencionBean.setEntidad(elemFeature.getEntidad());
    				intervencionBean.setId((int)elemFeature.getId());
    				intervencionBean.setIdCementerio(idCementerio);
				
					Collection c = new ArrayList(); 
					c.add(elemFeature.getIdFeature()); 
					intervencionBean.setIdFeatures( c);
					Collection c2 = new ArrayList(); 
					c2.add(elemFeature.getIdLayer());
					intervencionBean.setIdLayers(c2);
    			}
    			alRet.put(intervencionBean.getId_intervencion(), intervencionBean);
			}
		}

		return alRet.values();
	}	
		

}


