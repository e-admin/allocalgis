package com.geopista.app.cementerios.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.DatosFallecimientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DifuntoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ElemFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.HistoricoDifuntosDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.InhumacionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PersonaDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.DatosFallecimientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DifuntoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ElemFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.HistoricoDifuntosDAO;
import com.geopista.app.cementerios.business.dao.interfaces.InhumacionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PersonaDAO;
import com.geopista.app.cementerios.business.vo.Concesion;
import com.geopista.app.cementerios.business.vo.ConcesionExample;
import com.geopista.app.cementerios.business.vo.DatosFallecimiento;
import com.geopista.app.cementerios.business.vo.DatosFallecimientoExample;
import com.geopista.app.cementerios.business.vo.Difunto;
import com.geopista.app.cementerios.business.vo.DifuntoExample;
import com.geopista.app.cementerios.business.vo.ElemFeature;
import com.geopista.app.cementerios.business.vo.ElemFeatureExample;
import com.geopista.app.cementerios.business.vo.HistoricoDifuntos;
import com.geopista.app.cementerios.business.vo.Inhumacion;
import com.geopista.app.cementerios.business.vo.InhumacionExample;
import com.geopista.app.cementerios.business.vo.Persona;
import com.geopista.app.cementerios.business.vo.PersonaExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;

public class InhumacionManager  extends DAO {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InhumacionManager.class);
	
	private static InhumacionManager instance;
	
	private InhumacionDAO inhumacionDAO;
	private ElemFeatureDAO elemFeatureDAO;
	private DifuntoDAO difuntoDAO;
	private HistoricoDifuntosDAO historicoDAO;
	private PersonaDAO personaDAO;
    private DatosFallecimientoDAO datosFallecimientoDAO;
    
    private MappingManager mappingManager;
    private DifuntoManager difuntoManager;
    
    public InhumacionManager(){
        
    	inhumacionDAO = new InhumacionDAOImpl();
    	elemFeatureDAO = new ElemFeatureDAOImpl();
    	difuntoDAO = new DifuntoDAOImpl();
    	historicoDAO = new HistoricoDifuntosDAOImpl();
    	personaDAO = new PersonaDAOImpl();
    	 datosFallecimientoDAO = new DatosFallecimientoDAOImpl();
    	
        mappingManager = MappingManager.getIntance();
        difuntoManager = DifuntoManager.getInstance();
        
    }
    
    public static InhumacionManager getInstance(){
    	if (instance == null){
    		instance = new InhumacionManager();
    	}
    	return instance;
    }
		
	
	/**************************************************************** INHUMACION ****************************************************************************/
	
	/**
	 * insertInhumacion
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
    
	/** NOTA: Insert en cementerioFeature
	 *  Esto se realiza para que sea mas fácil la recuperación posterior de las inhumaciones realizadas en una feature
	 *  Sin esto la manera de realizarlo sería: 
	 	* 1.select de cada cementerioFeaturekey de cada idfeature--> se obtiene el id de la unidad de enterarramiento
	 	* 2.comprobar si el cementerio se corresponde con el cementerio en el que estamos
	 	* 3.Recuepar la informacion del cementerio 
	 	* 4.Recuperar las plazas de la unidad de enterramiento obtenida
	 	* 5.Recuperar el difunto que pertence a cada una de las plazas
	 	* 6.Recuperar la inhumacion sobre cada uno de los difuntos**/ 
    
	public InhumacionBean insertInhumacion(Object[] idLayers, Object[] idFeatures, InhumacionBean elem, Integer idCementerio, Sesion userSesion)
	throws PermissionException, LockException, Exception {

		try {

            for (int j=0; j<idFeatures.length; j++){

            	/**Start transaccion**/
				getSqlMapClientTemplate().startTransaction();
			
				Inhumacion inhumacion = new Inhumacion();
					inhumacion.setBonificacion(elem.getBonificacion());
					inhumacion.setCodigo(elem.getCodigo());
					inhumacion.setContenedor(elem.getTipo_contenedor());
					inhumacion.setFechaInhumacion(elem.getFecha_inhumacion());
					inhumacion.setIdDifunto(elem.getDifunto().getId_difunto());
					inhumacion.setInforme(elem.getInforme_inhumacion());
					inhumacion.setPrecioFinal(elem.getPrecio_final());
					inhumacion.setTipoInhumacion(elem.getTipo_inhumacion());
					inhumacion.setIdTarifa(elem.getTarifa().getId_tarifa());
				
				inhumacionDAO.insert(inhumacion);
				
				int idInhumacion = inhumacionDAO.selectByLastSeqKey();
				elem.setId_inhumacion(idInhumacion);
				
				ElemFeature elemFeature = new ElemFeature();
					elemFeature.setIdCementerio(idCementerio);
					elemFeature.setIdFeature(Integer.parseInt((String)idFeatures[j]));
					elemFeature.setIdLayer((String)idLayers[j]);
					elemFeature.setIdCementerio(idCementerio);
					elemFeature.setIdElem(idInhumacion);
					elemFeature.setEntidad(elem.getEntidad());
					elemFeature.setIdMunicipio(Integer.parseInt(elem.getIdMunicipio()));
					elemFeature.setNombre(elem.getNombreCementerio());
					elemFeature.setTipo(Const.SUPERPATRON_GDIFUNTOS);
					elemFeature.setSubtipo(Const.PATRON_INHUMACION);
				
				elemFeatureDAO.insert(elemFeature);
				
				int idElemFeature = elemFeatureDAO.selectByLastSeqKey();
				elemFeature.setId(idElemFeature);
				
				
				HistoricoDifuntos historicoDifuntos = new HistoricoDifuntos();
					historicoDifuntos.setFechaOperacion(new Date());
					historicoDifuntos.setComentario(inhumacion.getInforme());
					historicoDifuntos.setIdDifunto(elem.getDifunto().getId_difunto());
					historicoDifuntos.setIdElem(idInhumacion);
					historicoDifuntos.setTipo(Integer.parseInt(Const.PATRON_INHUMACION));
				
					historicoDAO.insert(historicoDifuntos);
					
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
	 * updateInhumacion
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
	public InhumacionBean updateInhumacion(InhumacionBean elem, Sesion userSesion)
	throws PermissionException, LockException, Exception {

		try {
			/**Start transaccion**/
			getSqlMapClientTemplate().startTransaction();
			
			Inhumacion inhumacion = new Inhumacion();
			
			inhumacion.setBonificacion(elem.getBonificacion());
			inhumacion.setCodigo(elem.getCodigo());
			inhumacion.setContenedor(elem.getTipo_contenedor());
			inhumacion.setFechaInhumacion(elem.getFecha_inhumacion());
			inhumacion.setIdDifunto(elem.getDifunto().getId_difunto());
			inhumacion.setInforme(elem.getInforme_inhumacion());
			inhumacion.setPrecioFinal(elem.getPrecio_final());
			inhumacion.setTipoInhumacion(elem.getTipo_inhumacion());
			
			inhumacion.setIdTarifa(elem.getTarifa().getId_tarifa());
			inhumacion.setId(elem.getId_inhumacion());
			
			inhumacionDAO.updateByPrimaryKeySelective(inhumacion);
			
			getSqlMapClientTemplate().commitTransaction();
			
		}catch (Exception e) {
			logger.error("Error en la actualización de una inhumacion" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
		
		return elem;
	}
		
	/**
	 * deleteInhumacion
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
	public InhumacionBean deleteInhumacion(Object[] idLayers, Object[] idFeatures, InhumacionBean elem, Integer idCementerio, Sesion userSesion)
	throws PermissionException, LockException, Exception {

		try {
			/**Start transaccion**/
			getSqlMapClientTemplate().startTransaction();
			
			//Tengo que eliminar la entrada de elem_feature
			ElemFeatureExample elemFeatureExample = new ElemFeatureExample();
			elemFeatureExample.createCriteria().andIdElemEqualTo(elem.getId_inhumacion());
			
			elemFeatureDAO.deleteByExample(elemFeatureExample);
			
			inhumacionDAO.deleteByPrimaryKey(elem.getId_inhumacion());
			
			getSqlMapClientTemplate().commitTransaction();
			
		}catch (Exception e) {
			logger.error("Error en el borrado de una inhumacion" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
		
		return elem;
	}
		
	/**
	 * 	
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
	public Collection getInhumacionesByFeature(String superpatron, String patron,Object[] filtro, Object[] idLayers, Object[] idFeatures,
			Integer idCementerio, Sesion userSesion) throws Exception {

		HashMap alRet = new HashMap();
		

		Collection colDifuntos = difuntoManager.getDifuntosByFeature(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,userSesion);
		List listaInhumaciones = inhumacionDAO.selectAll();
		
		Inhumacion inhumacionVO;

		for (int i = 0; i < listaInhumaciones.size(); i++) {
			inhumacionVO = (Inhumacion) listaInhumaciones.get(i);
			
			DifuntoBean difunto = perteneceInhumacionADifuntosEnFeature(colDifuntos, inhumacionVO.getIdDifunto()); 
			if (difunto!= null){
				InhumacionBean inhumacion = mappingManager.mapInhumacionVOToBean(inhumacionVO);
				inhumacion.setDifunto(difunto);
				inhumacion.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
				inhumacion.setEntidad(difunto.getEntidad());
				inhumacion.setId((int)difunto.getId());
				
				Collection c = new ArrayList(); 
				for (int j = 0; j < idFeatures.length; j++) {
					c.add(idFeatures[j]);
				}
				inhumacion.setIdFeatures(c);
				Collection c2 = new ArrayList();
				for (int j = 0; j < idLayers.length; j++) {
					c2.add(idLayers[j]);
				}
				inhumacion.setIdLayers(c2);
				alRet.put(inhumacion.getId_inhumacion(), inhumacion);
			}
		}
		
	return alRet.values();
	}

	/**
	 * 
	 * @param idCementerio
	 * @return
	 * @throws Exception
	 */
    public Collection getAllInhumacionesByCemetery (Integer idCementerio) throws Exception {
    	
    	InhumacionBean inhumacion = null;
    	List<Inhumacion> listaInhumaciones = new ArrayList<Inhumacion>();
    	HashMap alRet= new HashMap();
    	
    	
    	try{
    		
    		listaInhumaciones = inhumacionDAO.selectAll();
    		Inhumacion inhumacionVO;
    		ElemFeature elemFeature;
    		for (int i = 0; i < listaInhumaciones.size(); i++) {
				inhumacionVO = listaInhumaciones.get(i);
				
				elemFeature = elemFeatureDAO.selectByElemAndCementerio(inhumacionVO.getId(), idCementerio);
    			if (elemFeature!= null){
    				inhumacion = mappingManager.mapInhumacionVOToBean(inhumacionVO);
    				
    				Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(inhumacionVO.getIdDifunto());
    				DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoVO);
    				
    				inhumacion.setDifunto(difunto);
    				inhumacion.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
    				inhumacion.setEntidad(difunto.getEntidad());
    				inhumacion.setId((int)difunto.getId());
    				inhumacion.setIdCementerio(idCementerio);
    				
    				Collection c = new ArrayList(); 
    				c.add(elemFeature.getIdFeature()); 
    				inhumacion.setIdFeatures( c);
    				Collection c2 = new ArrayList(); 
    				c2.add(elemFeature.getIdLayer());
    				inhumacion.setIdLayers(c2);
    				alRet.put(inhumacion.getId_inhumacion(), inhumacion);
    			}
			}
            
    	}catch (Exception e) {
    		
    		logger.error("Error obteniendo el difunto " + e);
		}    	
		return alRet.values();
    }
	
	
	private DifuntoBean perteneceInhumacionADifuntosEnFeature (Collection cdifuntos, Integer idDifunto){
		
		boolean pertenece = false;
		Object[] arrayElems = cdifuntos.toArray();
		
		DifuntoBean difunto = null;
		for (int i = 0; i < arrayElems.length; i++) {
			difunto = (DifuntoBean) arrayElems[i];
			if (difunto.getId_difunto() == idDifunto.intValue()){
				pertenece = true;
				break;
			}
			difunto = null;
		}
		return difunto;
	}
		
	/**
	 * getInhumacionesByFilter
	 * @param superpatron
	 * @param patron
	 * @param filtro
	 * @param idCementerio
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
//	public Collection getInhumacionesByFilter(String superpatron,String patron, Object[] filtro, Integer idCementerio,
//			Sesion userSesion) throws Exception {
//
//		List<Inhumacion> listaInhumaciones;
//		HashMap alRet = new HashMap();
//		AddFilter addFilter = AddFilter.getInstance();
//
//		InhumacionExample inhumacionExample = new InhumacionExample();
//		if (filtro != null) {
//			CampoFiltro campoFiltro;
//			com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria criteria = inhumacionExample.createCriteria();
//			
//			for (int i = 0; i < filtro.length; i++) {
//				campoFiltro = (CampoFiltro) filtro[i];
//				criteria = (com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria) 
//				addFilter.addInhumacionFilter(criteria, campoFiltro, false);
//			}
//			inhumacionExample.or(criteria);
//			listaInhumaciones = inhumacionDAO.selectByExample(inhumacionExample);
//
//			for (int j = 0; j < listaInhumaciones.size(); j++) {
//				Inhumacion inhumacion = (Inhumacion) listaInhumaciones.get(j);
//				InhumacionBean inhumacionBean = mappingManager.mapInhumacionVOToBean(inhumacion);
//				
//				Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(inhumacion.getIdDifunto());
//				DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoVO);
//				
//				inhumacionBean.setDifunto(difunto);
//				inhumacionBean.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
//				inhumacionBean.setEntidad(difunto.getEntidad());
//				inhumacionBean.setId((int)difunto.getId());
//				inhumacionBean.setIdCementerio(idCementerio);
//				
//				ElemFeature elemFeature = elemFeatureDAO.selectByElemAndCementerio(inhumacion.getId(), idCementerio);
//    			if (elemFeature!= null){
//					Collection c = new ArrayList(); 
//					c.add(elemFeature.getIdFeature()); 
//					inhumacionBean.setIdFeatures( c);
//					Collection c2 = new ArrayList(); 
//					c2.add(elemFeature.getIdLayer());
//					inhumacionBean.setIdLayers(c2);
//    			}
//				alRet.put(inhumacionBean.getId_inhumacion(), inhumacionBean);
//			}
//		}
//		
//		return alRet.values();
//	}	
		
	
    /**
     * 
     * @param difunto
     * @param listaPersonas
     * @return
     */
    private boolean perteneceDifuntoListapersonas (Difunto difunto, List listaPersonas){
    	boolean pertenece = false;
		for (int i = 0; i < listaPersonas.size(); i++) {
			Persona personaElem = (Persona) listaPersonas.get(i);
			if (difunto.getDniPersona().equalsIgnoreCase(personaElem.getDni())){
				pertenece = true;
				break;
			}
		}
		return pertenece;	
    }
    
    private boolean perteneceDifuntoListaDatosFallecimiento (Difunto difunto, List listaDatosFallecimiento){
    	boolean pertenece = false;
		for (int i = 0; i < listaDatosFallecimiento.size(); i++) {
			DatosFallecimiento datosFallecimientoElem = (DatosFallecimiento) listaDatosFallecimiento.get(i);
			if (difunto.getIdDatosfallecimiento().intValue() == difunto.getIdDatosfallecimiento().intValue()){ 
				pertenece = true;
				break;
			}
		}
		return pertenece;	
    	
    }

    
    private boolean perteneceDifuntoListaDifuntos (Difunto difunto, List listaDifuntos){
    	boolean pertenece = false;
		for (int i = 0; i < listaDifuntos.size(); i++) {
			Difunto difuntoElem = (Difunto) listaDifuntos.get(i);
			if (difunto.getDniPersona().equalsIgnoreCase(difuntoElem.getDniPersona())){ 
				pertenece = true;
				break;
			}
		}
		return pertenece;	
    	
    }

    
    
	public Collection getInhumacionesByFilter(String superpatron,String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<Inhumacion> listaInhumaciones;
		List<Persona> listaPersonas = null;
		List<DatosFallecimiento> listaDatosFallecimiento = null;
		List<Difunto> listaDifuntos = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		InhumacionExample inhumacionExample = new InhumacionExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria criteria = inhumacionExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				
				if (campoFiltro.getValorId() != null){
					String filter = campoFiltro.getValorId();
					if (filter.equalsIgnoreCase("persona")){
						PersonaExample personaExample = new PersonaExample();
						com.geopista.app.cementerios.business.vo.PersonaExample.Criteria personaCriteria = personaExample.createCriteria();
						personaCriteria = addFilter.addPersonaFilter(personaCriteria, campoFiltro, false);
						
						personaExample.or(personaCriteria);
						listaPersonas = personaDAO.selectByExample(personaExample);
					}
					else if (filter.equalsIgnoreCase("datosfallecimiento")){
						DatosFallecimientoExample datosFallecimientoExample = new DatosFallecimientoExample();
						com.geopista.app.cementerios.business.vo.DatosFallecimientoExample.Criteria datosFallecimientoCriteria =
							datosFallecimientoExample.createCriteria();
						datosFallecimientoCriteria = addFilter.addDatosFallecimientoFilter(datosFallecimientoCriteria, campoFiltro, false);
						datosFallecimientoExample.or(datosFallecimientoCriteria);
						listaDatosFallecimiento = datosFallecimientoDAO.selectByExample(datosFallecimientoExample);
					}
					else if (filter.equalsIgnoreCase("difunto")){
						DifuntoExample difuntoExample = new DifuntoExample();
						com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria difuntoCriteria = difuntoExample.createCriteria();
						difuntoCriteria = addFilter.addDifuntoFilter(difuntoCriteria, campoFiltro, false);
						
						difuntoExample.or(difuntoCriteria);
						listaDifuntos = difuntoDAO.selectByExample(difuntoExample);
					}					
				}else{
					criteria = (com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria) 
					addFilter.addInhumacionFilter(criteria, campoFiltro, false);
				}
			}
			inhumacionExample.or(criteria);
			listaInhumaciones = inhumacionDAO.selectByExample(inhumacionExample);

			for (int j = 0; j < listaInhumaciones.size(); j++) {
				Inhumacion inhumacion = (Inhumacion) listaInhumaciones.get(j);
				InhumacionBean inhumacionBean = mappingManager.mapInhumacionVOToBean(inhumacion);
				
				Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(inhumacion.getIdDifunto());
				
				if (listaPersonas != null){
					if (!perteneceDifuntoListapersonas(difuntoVO, listaPersonas)){
						continue;
					}
				}
				if (listaDatosFallecimiento != null){
					if (!perteneceDifuntoListaDatosFallecimiento(difuntoVO, listaDatosFallecimiento)){
						continue;
					}
				}
				if (listaDifuntos != null){
					if (!perteneceDifuntoListaDifuntos(difuntoVO, listaDifuntos)){
						continue;
					}
				}
				DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoVO);
				
				inhumacionBean.setDifunto(difunto);
				inhumacionBean.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
				inhumacionBean.setEntidad(difunto.getEntidad());
				inhumacionBean.setId((int)difunto.getId());
				inhumacionBean.setIdCementerio(idCementerio);
				
				ElemFeature elemFeature = elemFeatureDAO.selectByElemAndCementerio(inhumacion.getId(), idCementerio);
    			if (elemFeature!= null){
					Collection c = new ArrayList(); 
					c.add(elemFeature.getIdFeature()); 
					inhumacionBean.setIdFeatures( c);
					Collection c2 = new ArrayList(); 
					c2.add(elemFeature.getIdLayer());
					inhumacionBean.setIdLayers(c2);
    			}
				alRet.put(inhumacionBean.getId_inhumacion(), inhumacionBean);
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
	public Collection findInhumaciones(String superpatron,String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<Inhumacion> listaInhumaciones = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		InhumacionExample inhumacionExample = new InhumacionExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria criteria = inhumacionExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria) 
				addFilter.addInhumacionFilter(criteria, campoFiltro, true);
			
			if (criteria == null) continue;	
				
			inhumacionExample.or(criteria);
			listaInhumaciones = inhumacionDAO.selectByExample(inhumacionExample);
			
			if ((listaInhumaciones != null) && (listaInhumaciones.size()>0)) break; 
			inhumacionExample.clear();
			}
			for (int j = 0; j < listaInhumaciones.size(); j++) {
				Inhumacion inhumacion = (Inhumacion) listaInhumaciones.get(j);
				InhumacionBean inhumacionBean = mappingManager.mapInhumacionVOToBean(inhumacion);
				
				Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(inhumacion.getIdDifunto());
				DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoVO);
				
				inhumacionBean.setDifunto(difunto);
				inhumacionBean.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
				inhumacionBean.setEntidad(difunto.getEntidad());
				inhumacionBean.setId((int)difunto.getId());
				inhumacionBean.setIdCementerio(idCementerio);
				
				ElemFeature elemFeature = elemFeatureDAO.selectByElemAndCementerio(inhumacion.getId(), idCementerio);
    			if (elemFeature!= null){
					Collection c = new ArrayList(); 
					c.add(elemFeature.getIdFeature()); 
					inhumacionBean.setIdFeatures( c);
					Collection c2 = new ArrayList(); 
					c2.add(elemFeature.getIdLayer());
					inhumacionBean.setIdLayers(c2);
    			}
				alRet.put(inhumacionBean.getId_inhumacion(), inhumacionBean);
			}
		}

		return alRet.values();
	}	
		
		
		
		
		
}
