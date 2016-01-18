/**
 * ExhumacionManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.DatosFallecimientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DifuntoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ElemFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ExhumacionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.HistoricoDifuntosDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PersonaDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.DatosFallecimientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DifuntoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ElemFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ExhumacionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.HistoricoDifuntosDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PersonaDAO;
import com.geopista.app.cementerios.business.vo.DatosFallecimiento;
import com.geopista.app.cementerios.business.vo.DatosFallecimientoExample;
import com.geopista.app.cementerios.business.vo.Difunto;
import com.geopista.app.cementerios.business.vo.DifuntoExample;
import com.geopista.app.cementerios.business.vo.ElemFeature;
import com.geopista.app.cementerios.business.vo.ElemFeatureExample;
import com.geopista.app.cementerios.business.vo.Exhumacion;
import com.geopista.app.cementerios.business.vo.ExhumacionExample;
import com.geopista.app.cementerios.business.vo.HistoricoDifuntos;
import com.geopista.app.cementerios.business.vo.Persona;
import com.geopista.app.cementerios.business.vo.PersonaExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.ExhumacionBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;

public class ExhumacionManager  extends DAO {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExhumacionManager.class);
	
	private static ExhumacionManager instance;
	
	private ExhumacionDAO exhumacionDAO;
	private ElemFeatureDAO elemFeatureDAO;
	private DifuntoDAO difuntoDAO;
	private HistoricoDifuntosDAO historicoDAO;
	private PersonaDAO personaDAO;
    private DatosFallecimientoDAO datosFallecimientoDAO;
    
    private MappingManager mappingManager;
    private DifuntoManager difuntoManager;
    
    public ExhumacionManager(){
        
    	exhumacionDAO = new ExhumacionDAOImpl();
    	elemFeatureDAO = new ElemFeatureDAOImpl();
    	difuntoDAO = new DifuntoDAOImpl();
    	historicoDAO = new HistoricoDifuntosDAOImpl();
    	personaDAO = new PersonaDAOImpl();
   	    datosFallecimientoDAO = new DatosFallecimientoDAOImpl();
    	
        mappingManager = MappingManager.getIntance();
        difuntoManager = DifuntoManager.getInstance();
        
    }
    
    public static ExhumacionManager getInstance(){
    	if (instance == null){
    		instance = new ExhumacionManager();
    	}
    	return instance;
    }
		
	
	/**************************************************************** Exhumacion ****************************************************************************/
	
	/**
	 * insertExhumacion
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
	 *  Esto se realiza para que sea mas fácil la recuperación posterior de las Exhumaciones realizadas en una feature
	 *  Sin esto la manera de realizarlo sería: 
	 	* 1.select de cada cementerioFeaturekey de cada idfeature--> se obtiene el id de la unidad de enterarramiento
	 	* 2.comprobar si el cementerio se corresponde con el cementerio en el que estamos
	 	* 3.Recuepar la informacion del cementerio 
	 	* 4.Recuperar las plazas de la unidad de enterramiento obtenida
	 	* 5.Recuperar el difunto que pertence a cada una de las plazas
	 	* 6.Recuperar la Exhumacion sobre cada uno de los difuntos**/ 
    
	public ExhumacionBean insertExhumacion(Object[] idLayers, Object[] idFeatures, ExhumacionBean elem, Integer idCementerio, Sesion userSesion)
	throws PermissionException, LockException, Exception {

		try {

            for (int j=0; j<idFeatures.length; j++){

            	/**Start transaccion**/
				getSqlMapClientTemplate().startTransaction();
			
				Exhumacion exhumacion = new Exhumacion();
					exhumacion.setBonificacion(elem.getBonificacion());
					exhumacion.setCodigo(elem.getCodigo());
					exhumacion.setContenedor(elem.getTipo_contenedor());
					exhumacion.setFechaExhumacion(elem.getFecha_exhumacion());
					exhumacion.setIdDifunto(elem.getDifunto().getId_difunto());
					exhumacion.setInforme(elem.getInforme_exhumacion());
					exhumacion.setPrecioFinal(elem.getPrecio_final());
					exhumacion.setTipoExhumacion(elem.getTipo_exhumacion());
					exhumacion.setIdTarifa(elem.getTarifa().getId_tarifa());
					if (elem.isRed_restos()){
						exhumacion.setRedRestos(Constantes.TRUE);
					}else{
						exhumacion.setRedRestos(Constantes.FALSE);
					}
					if (elem.isTraslado()){
						exhumacion.setTraslado(Constantes.TRUE);
					}else{
						exhumacion.setTraslado(Constantes.FALSE);
					}
					exhumacionDAO.insert(exhumacion);
				
				int idExhumacion = exhumacionDAO.selectByLastSeqKey();
				elem.setId_exhumacion(idExhumacion);
				
				ElemFeature elemFeature = new ElemFeature();
					elemFeature.setIdCementerio(idCementerio);
					elemFeature.setIdFeature(Integer.parseInt((String)idFeatures[j]));
					elemFeature.setIdLayer((String)idLayers[j]);
					elemFeature.setIdCementerio(idCementerio);
					elemFeature.setIdElem(idExhumacion);
					elemFeature.setEntidad(elem.getEntidad());
					elemFeature.setIdMunicipio(Integer.parseInt(elem.getIdMunicipio()));
					elemFeature.setNombre(elem.getNombreCementerio());
					elemFeature.setTipo(Const.SUPERPATRON_GDIFUNTOS);
					elemFeature.setSubtipo(Const.PATRON_EXHUMACION);
				
				elemFeatureDAO.insert(elemFeature);
				
				int idElemFeature = elemFeatureDAO.selectByLastSeqKey();
				elemFeature.setId(idElemFeature);
				
				HistoricoDifuntos historicoDifuntos = new HistoricoDifuntos();
				historicoDifuntos.setFechaOperacion(new Date());
				historicoDifuntos.setComentario(exhumacion.getInforme());
				historicoDifuntos.setIdDifunto(elem.getDifunto().getId_difunto());
				historicoDifuntos.setIdElem(idExhumacion);
				//historicoDifuntos.setTipo(Integer.parseInt(Const.PATRON_EXHUMACION));
				historicoDifuntos.setTipo(Const.PATRON_EXHUMACION);
			
				historicoDAO.insert(historicoDifuntos);
				
				getSqlMapClientTemplate().commitTransaction();
				
	        }

		}catch (Exception e) {
			logger.error("Error en la insercion de una Exhumacion" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
		
		return elem;
	}

	/**
	 * updateExhumacion
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
	public ExhumacionBean updateExhumacion(ExhumacionBean elem, Sesion userSesion)
	throws PermissionException, LockException, Exception {

		try {
			/**Start transaccion**/
			getSqlMapClientTemplate().startTransaction();
			
			Exhumacion exhumacion = new Exhumacion();
			
			exhumacion.setBonificacion(elem.getBonificacion());
			exhumacion.setCodigo(elem.getCodigo());
			exhumacion.setContenedor(elem.getTipo_contenedor());
			exhumacion.setFechaExhumacion(elem.getFecha_exhumacion());
			exhumacion.setIdDifunto(elem.getDifunto().getId_difunto());
			exhumacion.setInforme(elem.getInforme_exhumacion());
			exhumacion.setPrecioFinal(elem.getPrecio_final());
			exhumacion.setTipoExhumacion(elem.getTipo_exhumacion());
			exhumacion.setIdTarifa(elem.getTarifa().getId_tarifa());
			exhumacion.setId(elem.getId_exhumacion());
			if (elem.isRed_restos()){
				exhumacion.setRedRestos(Constantes.TRUE);
			}else{
				exhumacion.setRedRestos(Constantes.FALSE);
			}
			if (elem.isTraslado()){
				exhumacion.setTraslado(Constantes.TRUE);
			}else{
				exhumacion.setTraslado(Constantes.FALSE);
			}
			
			exhumacionDAO.updateByPrimaryKeySelective(exhumacion);
			
			getSqlMapClientTemplate().commitTransaction();
			
		}catch (Exception e) {
			logger.error("Error en la actualización de una Exhumacion" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
		
		return elem;
	}
		
	/**
	 * deleteExhumacion
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
	public ExhumacionBean deleteExhumacion(Object[] idLayers, Object[] idFeatures, ExhumacionBean elem, Integer idCementerio, Sesion userSesion)
	throws PermissionException, LockException, Exception {

		try {
			/**Start transaccion**/
			getSqlMapClientTemplate().startTransaction();
			
			//Tengo que eliminar la entrada de elem_feature
			ElemFeatureExample elemFeatureExample = new ElemFeatureExample();
			elemFeatureExample.createCriteria().andIdElemEqualTo(elem.getId_exhumacion());
			
			elemFeatureDAO.deleteByExample(elemFeatureExample);
			
			exhumacionDAO.deleteByPrimaryKey(elem.getId_exhumacion());
			
			getSqlMapClientTemplate().commitTransaction();
			
		}catch (Exception e) {
			logger.error("Error en el borrado de una Exhumacion" + e);
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
	public Collection getExhumacionesByFeature(String superpatron, String patron,Object[] filtro, Object[] idLayers, Object[] idFeatures,
			Integer idCementerio, Sesion userSesion) throws Exception {

		HashMap alRet = new HashMap();
		

		Collection colDifuntos = difuntoManager.getDifuntosByFeature(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,userSesion);
		List listaExhumaciones = exhumacionDAO.selectAll();
		
		Exhumacion exhumacionVO;

		for (int i = 0; i < listaExhumaciones.size(); i++) {
			exhumacionVO = (Exhumacion) listaExhumaciones.get(i);
			
			DifuntoBean difunto = perteneceExhumacionADifuntosEnFeature(colDifuntos, exhumacionVO.getIdDifunto()); 
			if (difunto!= null){
				ExhumacionBean exhumacion = mappingManager.mapExhumacionVOToBean(exhumacionVO);
//				DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoVO);	
				exhumacion.setDifunto(difunto);
				exhumacion.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
				exhumacion.setEntidad(difunto.getEntidad());
				exhumacion.setId((int)difunto.getId());
				
				Collection c = new ArrayList(); 
				for (int j = 0; j < idFeatures.length; j++) {
					c.add(idFeatures[j]);
				}
				exhumacion.setIdFeatures(c);
				Collection c2 = new ArrayList(); 
				for (int j = 0; j < idLayers.length; j++) {
					c2.add(idLayers[j]);
				}
				exhumacion.setIdLayers(c2);
				alRet.put(exhumacion.getId_exhumacion(), exhumacion);
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
    public Collection getAllExhumacionesByCemetery (Integer idCementerio) throws Exception {
    	
    	ExhumacionBean exhumacion = null;
    	List<Exhumacion> listaExhumaciones = new ArrayList<Exhumacion>();
    	HashMap alRet= new HashMap();
    	
    	
    	try{
    		
    		listaExhumaciones = exhumacionDAO.selectAll();
    		Exhumacion exhumacionVO;
    		ElemFeature elemFeature;
    		for (int i = 0; i < listaExhumaciones.size(); i++) {
				exhumacionVO = listaExhumaciones.get(i);
				
				elemFeature = elemFeatureDAO.selectByElemAndCementerio(exhumacionVO.getId(), idCementerio);
    			if (elemFeature!= null){
    				exhumacion = mappingManager.mapExhumacionVOToBean(exhumacionVO);
    				
    				Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(exhumacionVO.getIdDifunto());
    				DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoVO);
    				
    				exhumacion.setDifunto(difunto);
    				exhumacion.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
    				exhumacion.setEntidad(difunto.getEntidad());
    				exhumacion.setId((int)difunto.getId());
    				exhumacion.setIdCementerio(idCementerio);
    				
    				Collection c = new ArrayList(); 
    				c.add(elemFeature.getIdFeature()); 
    				exhumacion.setIdFeatures( c);
    				Collection c2 = new ArrayList(); 
    				c2.add(elemFeature.getIdLayer());
    				exhumacion.setIdLayers(c2);
    				alRet.put(exhumacion.getId_exhumacion(), exhumacion);
    			}
			}
            
    	}catch (Exception e) {
    		
    		logger.error("Error obteniendo el difunto " + e);
		}    	
		return alRet.values();
    }
	
	
	private DifuntoBean perteneceExhumacionADifuntosEnFeature (Collection cdifuntos, Integer idDifunto){
		
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
	 * getExhumacionesByFilter
	 * @param superpatron
	 * @param patron
	 * @param filtro
	 * @param idCementerio
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
//	public Collection getExhumacionesByFilter(String superpatron,String patron, Object[] filtro, Integer idCementerio,
//			Sesion userSesion) throws Exception {
//
//		List<Exhumacion> listaExhumaciones;
//		HashMap alRet = new HashMap();
//		AddFilter addFilter = AddFilter.getInstance();
//
//		ExhumacionExample exhumacionExample = new ExhumacionExample();
//		if (filtro != null) {
//			CampoFiltro campoFiltro;
//			com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria criteria = exhumacionExample.createCriteria();
//			
//			for (int i = 0; i < filtro.length; i++) {
//				campoFiltro = (CampoFiltro) filtro[i];
//				criteria = (com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria) 
//				addFilter.addExhumacionFilter(criteria, campoFiltro, false);
//			}
//			exhumacionExample.or(criteria);
//			listaExhumaciones = exhumacionDAO.selectByExample(exhumacionExample);
//
//			for (int j = 0; j < listaExhumaciones.size(); j++) {
//				Exhumacion exhumacion = (Exhumacion) listaExhumaciones.get(j);
//				ExhumacionBean exhumacionBean = mappingManager.mapExhumacionVOToBean(exhumacion);
//				
//				Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(exhumacion.getIdDifunto());
//				DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoVO);
//				
//				exhumacionBean.setDifunto(difunto);
//				exhumacionBean.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
//				exhumacionBean.setEntidad(difunto.getEntidad());
//				exhumacionBean.setId((int)difunto.getId());
//				exhumacionBean.setIdCementerio(idCementerio);
//				
//				ElemFeature elemFeature = elemFeatureDAO.selectByElemAndCementerio(exhumacion.getId(), idCementerio);
//				if (elemFeature!= null){
//					Collection c = new ArrayList(); 
//					c.add(elemFeature.getIdFeature()); 
//					exhumacionBean.setIdFeatures( c);
//					Collection c2 = new ArrayList(); 
//					c2.add(elemFeature.getIdLayer());
//					exhumacionBean.setIdLayers(c2);
//				}
//    			alRet.put(exhumacionBean.getId_exhumacion(), exhumacionBean);
//			}
//		}
//		return alRet.values();
//	}		
	
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
	
	public Collection getExhumacionesByFilter(String superpatron,String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<Exhumacion> listaExhumaciones;

		List<Persona> listaPersonas = null;
		List<DatosFallecimiento> listaDatosFallecimiento = null;
		List<Difunto> listaDifuntos = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		ExhumacionExample exhumacionExample = new ExhumacionExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria criteria = exhumacionExample.createCriteria();
			
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
				
					criteria = (com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria) 
					addFilter.addExhumacionFilter(criteria, campoFiltro, false);
				}
			}
			
			exhumacionExample.or(criteria);
			listaExhumaciones = exhumacionDAO.selectByExample(exhumacionExample);

			for (int j = 0; j < listaExhumaciones.size(); j++) {
				Exhumacion exhumacion = (Exhumacion) listaExhumaciones.get(j);
				ExhumacionBean exhumacionBean = mappingManager.mapExhumacionVOToBean(exhumacion);
				
				Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(exhumacion.getIdDifunto());
				
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
				
				exhumacionBean.setDifunto(difunto);
				exhumacionBean.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
				exhumacionBean.setEntidad(difunto.getEntidad());
				exhumacionBean.setId((int)difunto.getId());
				exhumacionBean.setIdCementerio(idCementerio);
				
				ElemFeature elemFeature = elemFeatureDAO.selectByElemAndCementerio(exhumacion.getId(), idCementerio);
				if (elemFeature!= null){
					Collection c = new ArrayList(); 
					c.add(elemFeature.getIdFeature()); 
					exhumacionBean.setIdFeatures( c);
					Collection c2 = new ArrayList(); 
					c2.add(elemFeature.getIdLayer());
					exhumacionBean.setIdLayers(c2);
				}
    			alRet.put(exhumacionBean.getId_exhumacion(), exhumacionBean);
			}
		}
		return alRet.values();
	}	
		
	/**
	 * 	findExhumaciones
	 * @param superpatron
	 * @param patron
	 * @param filtro
	 * @param idCementerio
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	public Collection findExhumaciones(String superpatron,String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<Exhumacion> listaExhumaciones = null;
		
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		ExhumacionExample exhumacionExample = new ExhumacionExample();
		
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria criteria = exhumacionExample.createCriteriaInternal();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria) 
				addFilter.addExhumacionFilter(criteria, campoFiltro, true);
			
			if (criteria == null) continue;	
				
			exhumacionExample.or(criteria);
			listaExhumaciones = exhumacionDAO.selectByExample(exhumacionExample);

			if ((listaExhumaciones != null) && (listaExhumaciones.size()>0)) break; 
			exhumacionExample.clear();
			
			}
			
			if (listaExhumaciones!=null){
				for (int j = 0; j < listaExhumaciones.size(); j++) {
					Exhumacion exhumacion = (Exhumacion) listaExhumaciones.get(j);
					
					ExhumacionBean exhumacionBean = mappingManager.mapExhumacionVOToBean(exhumacion);
					
					Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(exhumacion.getIdDifunto());
					DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoVO);
					
					exhumacionBean.setDifunto(difunto);
					exhumacionBean.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
					exhumacionBean.setEntidad(difunto.getEntidad());
					exhumacionBean.setId((int)difunto.getId());
					exhumacionBean.setIdCementerio(idCementerio);
					
					ElemFeature elemFeature = elemFeatureDAO.selectByElemAndCementerio(exhumacion.getId(), idCementerio);
					if (elemFeature!= null){
						Collection c = new ArrayList(); 
						c.add(elemFeature.getIdFeature()); 
						exhumacionBean.setIdFeatures( c);
						Collection c2 = new ArrayList(); 
						c2.add(elemFeature.getIdLayer());
						exhumacionBean.setIdLayers(c2);
					}
	    			alRet.put(exhumacionBean.getId_exhumacion(), exhumacionBean);
				}
			}
		}
		return alRet.values();
	}		
		
		
		
		
		
}
