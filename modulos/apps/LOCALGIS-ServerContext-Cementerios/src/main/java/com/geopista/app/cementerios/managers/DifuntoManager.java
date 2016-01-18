/**
 * DifuntoManager.java
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
import com.geopista.app.cementerios.business.dao.implementations.CementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.CementerioFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DatosFallecimientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DifuntoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PersonaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PlazaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.UnidadEnterramientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.elem_cementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DatosFallecimientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DifuntoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PersonaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PlazaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.UnidadEnterramientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.elem_cementerioDAO;
import com.geopista.app.cementerios.business.vo.Cementerio;
import com.geopista.app.cementerios.business.vo.CementerioFeatureKey;
import com.geopista.app.cementerios.business.vo.DatosFallecimiento;
import com.geopista.app.cementerios.business.vo.DatosFallecimientoExample;
import com.geopista.app.cementerios.business.vo.Difunto;
import com.geopista.app.cementerios.business.vo.DifuntoExample;
import com.geopista.app.cementerios.business.vo.Persona;
import com.geopista.app.cementerios.business.vo.PersonaExample;
import com.geopista.app.cementerios.business.vo.Plaza;
import com.geopista.app.cementerios.business.vo.PlazaExample;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.geopista.app.cementerios.business.vo.elem_cementerioExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.DatosFallecimientoBean;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;

public class DifuntoManager extends DAO {
	
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DifuntoManager.class);
	
	private static DifuntoManager instance;
	
    private PlazaDAO plazaDAO;
    private DifuntoDAO difuntoDAO;
    private DatosFallecimientoDAO datosFallecimientoDAO;
    private PersonaDAO personaDAO;
    private UnidadEnterramientoDAO unidadEnterramientoDAO;
    private CementerioFeatureDAO cementerioFeatureDAO;
    private elem_cementerioDAO elemCementerioDAO;
    private CementerioDAO cementerioDAO;
    
    private MappingManager mappingManager;
    
    public DifuntoManager(){
        
        plazaDAO = new PlazaDAOImpl();
        difuntoDAO = new DifuntoDAOImpl();
        datosFallecimientoDAO = new DatosFallecimientoDAOImpl();
        personaDAO = new PersonaDAOImpl();
        unidadEnterramientoDAO = new UnidadEnterramientoDAOImpl();
        cementerioFeatureDAO = new CementerioFeatureDAOImpl();
        elemCementerioDAO = new elem_cementerioDAOImpl(); 
        cementerioDAO = new CementerioDAOImpl();
        
        mappingManager = MappingManager.getIntance();
    }
    
    public static DifuntoManager getInstance(){
    	if (instance == null){
    		instance = new DifuntoManager();
    	}
    	return instance;
    }
	
	
    /*************************************************************** DIFUNTO ******************************************************************************/
    /**
     * insertDifunto
     */
    public DifuntoBean insertDifunto (Object[] idLayers, Object[] idFeatures, DifuntoBean elem, Sesion userSesion) throws PermissionException,LockException,Exception{
    	
    	try{
    		
    		getSqlMapClientTemplate().startTransaction();
    	
    		DatosFallecimientoBean datosFallecimiento = elem.getDatosFallecimiento();
    		//Configuramos el VO
    		DatosFallecimiento datosFallecimientoVO = new DatosFallecimiento();
	    		datosFallecimientoVO.setCausaFundamental(datosFallecimiento.getCausa_fundamental());
	    		datosFallecimientoVO.setCausaInmediata(datosFallecimiento.getCausa_inmediata());
	    		datosFallecimientoVO.setFecha(datosFallecimiento.getFecha());
	    		datosFallecimientoVO.setLugar(datosFallecimiento.getLugar());
	    		datosFallecimientoVO.setMedico(datosFallecimiento.getMedico());
	    		datosFallecimientoVO.setNumColegiado(datosFallecimiento.getNumColegiado());
	    		datosFallecimientoVO.setPoblacion(datosFallecimiento.getPoblacion());
	    		datosFallecimientoVO.setReferencia(datosFallecimiento.getReferencia_fallecimiento());
	    		datosFallecimientoVO.setRegistroCivill(datosFallecimiento.getRegistro_civil());
	    		
	    		
	    		
	    	datosFallecimientoDAO.insert(datosFallecimientoVO);

	    	int id_datosFallecimiento = datosFallecimientoDAO.selectByLastSeqKey();
	    	datosFallecimiento.setId(id_datosFallecimiento);
	    	
	    	elem.setDatosFallecimiento(datosFallecimiento);

	    	//1.Insert la persona/ 
	    	PersonaBean persona = elem.getPersona();
	        Persona personaVo = new Persona();
	        	personaVo.setApellido1(persona.getApellido1());
	        	personaVo.setApellido2(persona.getApellido2());
	        	personaVo.setDni(persona.getDNI());
	        	personaVo.setDomicilio(persona.getDomicilio());
	        	personaVo.setEstadoCivil(persona.getEstado_civil());
	        	personaVo.setFechaNacimiento(persona.getFecha_nacimiento());
	        	personaVo.setNombre(persona.getNombre());
	        	personaVo.setPoblacion(persona.getPoblacion());
	        	personaVo.setSexo(persona.getSexo());
	        	personaVo.setTelefono("1234");

	        	int actualizado = personaDAO.updateByPrimaryKeySelective(personaVo);	
	        
	        	if (actualizado!= 1){
	        		personaDAO.insert(personaVo);
	        	}
	    	Difunto difunto = new Difunto();
		    	difunto.setDniPersona(persona.getDNI());
		    	difunto.setEdadDifunto(elem.getEdad());
		    	difunto.setFechaDefuncion(elem.getFecha_defuncion());
		    	difunto.setGrupo(elem.getGrupo());
		    	difunto.setIdPlaza(elem.getId_plaza());
		    	difunto.setIdDatosfallecimiento(id_datosFallecimiento);
		    	difunto.setCodigo(elem.getCodigo());
		    	
		    try{	
		    	difuntoDAO.insert(difunto);
		    }catch (Exception e) {
		    	System.out.println(e);
			}
		    int id_difunto = difuntoDAO.selectByLastSeqKey();
		    
		    elem.setId(id_difunto);
		    
		    //update los campos de la plaza... 
		    Plaza plazaVO = plazaDAO.selectByPrimaryKey(elem.getId_plaza());
			    plazaVO.setEstado(Constantes.TRUE);
			    plazaVO.setModicado(new Date());
			    plazaVO.setSituacion("Asignada");
			    
			plazaDAO.updateByPrimaryKeySelective(plazaVO);
			
		    //actualizo en bbdd en numero de plazas minimo
			UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(plazaVO.getIdUnidadenterramiento());
			Plaza record = new Plaza();
			record.setIdUnidadenterramiento(plazaVO.getIdUnidadenterramiento());
			record.setEstado(Constantes.TRUE);
			int numPlazas = plazaDAO.selectCountPlazasAsignadas(record);
			
			unidadVO.setNumplazas(numPlazas);
			unidadEnterramientoDAO.updateByPrimaryKeySelective(unidadVO);
			
			getSqlMapClientTemplate().commitTransaction();
			
    	}catch (Exception e) {
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
    	
    	return elem;
    }
    

    /**
     * updateDifunto
     * @param idLayers
     * @param idFeatures
     * @param elem
     * @param userSesion
     * @return
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    public DifuntoBean updateDifunto (Object[] idLayers, Object[] idFeatures, DifuntoBean elem, Sesion userSesion) throws PermissionException,LockException,Exception{
    	
    	try{
    		/**Comienza transacción**/
    		getSqlMapClientTemplate().startTransaction();

    		DatosFallecimientoBean datosFallecimiento = elem.getDatosFallecimiento();
    		//Configuramos el VO
    		DatosFallecimiento datosFallecimientoVO = new DatosFallecimiento();
	    		datosFallecimientoVO.setCausaFundamental(datosFallecimiento.getCausa_fundamental());
	    		datosFallecimientoVO.setCausaInmediata(datosFallecimiento.getCausa_inmediata());
	    		datosFallecimientoVO.setFecha(datosFallecimiento.getFecha());
	    		datosFallecimientoVO.setLugar(datosFallecimiento.getLugar());
	    		datosFallecimientoVO.setMedico(datosFallecimiento.getMedico());
	    		datosFallecimientoVO.setNumColegiado(datosFallecimiento.getNumColegiado());
	    		datosFallecimientoVO.setPoblacion(datosFallecimiento.getPoblacion());
	    		datosFallecimientoVO.setReferencia(datosFallecimiento.getReferencia_fallecimiento());
	    		datosFallecimientoVO.setRegistroCivill(datosFallecimiento.getRegistro_civil());
	    		
	    	datosFallecimientoDAO.updateByPrimaryKeySelective(datosFallecimientoVO);

	    	int id_datosFallecimiento = datosFallecimientoDAO.selectByLastSeqKey();
	    	datosFallecimiento.setId(id_datosFallecimiento);

	    	//1.Insert la persona/ 
	    	PersonaBean persona = elem.getPersona();
	        Persona personaVo = new Persona();
	        	personaVo.setApellido1(persona.getApellido1());
	        	personaVo.setApellido2(persona.getApellido2());
	        	personaVo.setDni(persona.getDNI());
	        	personaVo.setDomicilio(persona.getDomicilio());
	        	personaVo.setEstadoCivil(persona.getEstado_civil());
	        	personaVo.setFechaNacimiento(persona.getFecha_nacimiento());
	        	personaVo.setNombre(persona.getNombre());
	        	personaVo.setPoblacion(persona.getPoblacion());
	        	personaVo.setSexo(persona.getSexo());
	        	personaVo.setTelefono("1234");
	        	
	        	personaDAO.updateByPrimaryKeySelective(personaVo);
	        	
	    	if (elem.getServicio()!= null){
	    		
	    		//TODO insertSErvicio
	    	}
		  
	    	Difunto difunto = new Difunto();
	    	difunto.setDniPersona(persona.getDNI());
	    	difunto.setEdadDifunto(elem.getEdad());
	    	difunto.setFechaDefuncion(elem.getFecha_defuncion());
	    	difunto.setGrupo(elem.getGrupo());
	    	difunto.setIdPlaza(elem.getId_plaza());
	    	difunto.setIdDatosfallecimiento(id_datosFallecimiento);
	    	difunto.setCodigo(elem.getCodigo());
	    	
	    	difuntoDAO.updateByPrimaryKeySelective(difunto);
	    
	    	int id_difunto = difuntoDAO.selectByLastSeqKey();
	    
	    	elem.setId(id_difunto);
	    	
		    Plaza plazaVO = plazaDAO.selectByPrimaryKey(elem.getId_plaza());
			    plazaVO.setEstado(Constantes.TRUE);
			    plazaVO.setModicado(new Date());
			    plazaVO.setSituacion("Asignada");
			    
			plazaDAO.updateByPrimaryKeySelective(plazaVO);
			
		    //actualizo en bbdd en numero de plazas minimo
			UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(plazaVO.getIdUnidadenterramiento());
			Plaza record = new Plaza();
			record.setIdUnidadenterramiento(plazaVO.getIdUnidadenterramiento());
			record.setEstado(Constantes.TRUE);
			int numPlazas = plazaDAO.selectCountPlazasAsignadas(record);
			
			unidadVO.setNumplazas(numPlazas);
			unidadEnterramientoDAO.updateByPrimaryKeySelective(unidadVO);

			/**se hace commit**/
			getSqlMapClientTemplate().commitTransaction();
			
    	}catch (Exception e) {
    		logger.error("Error en el update del difunto" + e);
    		
		}finally{
			/**Se termina la transaccion**/
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
    //no es posible devolver un difunto solo si no hay plaza seleccionada
    private Collection getDifuntos (String superpatron, String patron, Object[] filtro, Object[] idLayers, Object[] idFeatures, Integer idCementerio, Sesion userSesion) throws Exception {
    	
    	DifuntoBean difunto = null;
    	List<Difunto> listaDifuntos = new ArrayList<Difunto>();
    	HashMap alRet= new HashMap();
    	
    	
    	try{
    		getSqlMapClientTemplate().startTransaction();
    		
        	List listaCementerioFeaturesKeys = new ArrayList<CementerioFeatureKey>();
        	
            for (int j=0; j<idFeatures.length; j++){
            	
            	List lista = new ArrayList<CementerioFeatureKey>();
        		CementerioFeatureKey key = new CementerioFeatureKey();
        		key.setIdFeature(Integer.parseInt((String)idFeatures[j]));
        		key.setIdLayer("unidad_enterramiento");
        		
        		//lista de elementos que hay en la feature
        		lista = cementerioFeatureDAO.selectByFeatureAndLayerUnidad(key);
            	for (int i = 0; i < lista.size(); i++) {
            		key = (CementerioFeatureKey) lista.get(i);
            		UnidadEnterramiento unidad = unidadEnterramientoDAO.selectByPrimaryKey(key.getIdElem());
            		
        	    	elem_cementerioExample elemExample = new  elem_cementerioExample();
        	    	elemExample.createCriteria().andIdCementerioEqualTo(idCementerio).andIdEqualTo(unidad.getIdElemcementerio());
        	    	List elem_cementerioList =  elemCementerioDAO.selectByExample(elemExample);
                	if (elem_cementerioList.size() != 1){
                		continue;
                	}
                	//Se retorna una lista pero solo debe contener un elemento
                	elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0); 
//                	if (elem != null){
//                	//Recuperamos el nombre del cementerio para hacer el set en la unidad
//                		Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
//                	}
                	//recupero todas las plazas de esa unidad
                	List listPlazas = new ArrayList();
                	PlazaExample plazaExample = new PlazaExample();
                	plazaExample.createCriteria().andIdUnidadenterramientoEqualTo(unidad.getId());
                	listPlazas = plazaDAO.selectByExample(plazaExample);
                	Plaza plazaelem;
                	Difunto difuntoelem;
                	List listaDifuntosAux;
            		for (int k = 0; k < listPlazas.size(); k++) {
						plazaelem = (Plaza) listPlazas.get(k);
						if (plazaelem.getEstado() == Constantes.TRUE){ //plaza asiganda a un difunto
							DifuntoExample difuntoExample = new DifuntoExample();
							difuntoExample.createCriteria().andIdPlazaEqualTo(plazaelem.getId());
							listaDifuntosAux = difuntoDAO.selectByExample(difuntoExample);
							listaDifuntos.addAll(listaDifuntosAux);
						}
					}
                	}
            	}
            getSqlMapClientTemplate().commitTransaction();
    	}catch (Exception e) {
    		logger.error("Error obteniendo el difunto " + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
    	
		return listaDifuntos;
    	
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
    @SuppressWarnings("rawtypes")
	public Collection getDifuntosByFeature (String superpatron, String patron, Object[] filtro, Object[] idLayers, Object[] idFeatures, Integer idCementerio, Sesion userSesion) throws Exception {
    	
    	DifuntoBean difunto = null;
    	List<Difunto> listaDifuntos = new ArrayList<Difunto>();
    	
    	HashMap alRet= new HashMap();
    	
    	try{
    		getSqlMapClientTemplate().startTransaction();
    		
        	List listaCementerioFeaturesKeys = new ArrayList<CementerioFeatureKey>();
        	
            for (int j=0; j<idFeatures.length; j++){
            	
            	List lista = new ArrayList<CementerioFeatureKey>();
        		CementerioFeatureKey key = new CementerioFeatureKey();
        		key.setIdFeature(Integer.parseInt((String)idFeatures[j]));
        		key.setIdLayer("unidad_enterramiento");
        		
        		//lista de elementos que hay en la feature
        		lista = cementerioFeatureDAO.selectByFeatureAndLayerUnidad(key);
            	for (int i = 0; i < lista.size(); i++) {
            		key = (CementerioFeatureKey) lista.get(i);
            		UnidadEnterramiento unidad = unidadEnterramientoDAO.selectByPrimaryKey(key.getIdElem());
            		UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidad);
            		
        	    	elem_cementerioExample elemExample = new  elem_cementerioExample();
        	    	elemExample.createCriteria().andIdCementerioEqualTo(idCementerio).andIdEqualTo(unidad.getIdElemcementerio());
        	    	
        	    	List elem_cementerioList =  elemCementerioDAO.selectByExample(elemExample);
                	
        	    	if (elem_cementerioList.size() != 1){
                		continue;
                	}

        	    	// Se retorna una lista pero solo debe contener un elemento
    				elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
    				if (elem != null) {
    					
    					// Recuperamos el nombre del cementerio para hacer el set en
    					// la unidad
    					Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);

    					
                    	//recupero todas las plazas de esa unidad
                    	List listPlazas = new ArrayList();
                    	PlazaExample plazaExample = new PlazaExample();
                    	plazaExample.createCriteria().andIdUnidadenterramientoEqualTo(unidad.getId());
                    	listPlazas = plazaDAO.selectByExample(plazaExample);

                    	Plaza plazaelem;
                    	Difunto difuntoelem;
                    	List listaDifuntosAux;
                		for (int k = 0; k < listPlazas.size(); k++) {
    						plazaelem = (Plaza) listPlazas.get(k);
    						if (plazaelem.getEstado() == Constantes.TRUE){ //plaza asiganda a un difunto
    							DifuntoExample difuntoExample = new DifuntoExample();
    							difuntoExample.createCriteria().andIdPlazaEqualTo(plazaelem.getId());
    							listaDifuntosAux = difuntoDAO.selectByExample(difuntoExample);
    							listaDifuntos.addAll(listaDifuntosAux);
    						}
    					}
    					for (int k = 0; k < listaDifuntos.size(); k++) {
							Difunto difuntoVO = (Difunto) listaDifuntos.get(k);
							DifuntoBean difuntoBean = mappingManager.mapDifuntoVOToBean(difuntoVO);
							difuntoBean.setNombreCementerio(cementerio.getNombre());
							difuntoBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
							difuntoBean.setEntidad(elem.getEntidad());
							difuntoBean.setId(elem.getId());
							difuntoBean.setIdFeatures(unidadBean.getIdFeatures());
							difuntoBean.setIdLayers(unidadBean.getIdLayers());
							
							alRet.put(difuntoBean.getId_difunto(), difuntoBean);
						}
                	}
            	}
            }
            
            getSqlMapClientTemplate().commitTransaction();

    	}catch (Exception e) {
    		logger.error("Error obteniendo el difunto " + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
    	
		return alRet.values();
    	
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
    public Collection getAllDifuntosByCemetery (Integer idCementerio) throws Exception {
    	
    	DifuntoBean difunto = null;
    	List<Difunto> listaDifuntos = new ArrayList<Difunto>();
    	HashMap alRet= new HashMap();
    	
    	
    	try{
    		getSqlMapClientTemplate().startTransaction();
    		
    		
    		listaDifuntos = difuntoDAO.selectAll();
    		Difunto difuntoElem;
    		for (int i = 0; i < listaDifuntos.size(); i++) {
				difuntoElem = listaDifuntos.get(i);
				
				//obtengo la plaza donde esta el difunto para obtener la unidad y comprobar si esta o no en el cementerio
				Plaza plaza = plazaDAO.selectByPrimaryKey(difuntoElem.getIdPlaza());
				
				UnidadEnterramiento unidad = unidadEnterramientoDAO.selectByPrimaryKey(plaza.getIdUnidadenterramiento());
				UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidad);
				
    	    	elem_cementerioExample elemExample = new  elem_cementerioExample();
    	    	elemExample.createCriteria().andIdCementerioEqualTo(idCementerio).andIdEqualTo(unidad.getIdElemcementerio());
    	    	
    	    	List elem_cementerioList =  elemCementerioDAO.selectByExample(elemExample);
            	if (elem_cementerioList.size() != 1){
            		continue;
            	}
            	
				// Se retorna una lista pero solo debe contener un elemento
				elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
				if (elem != null) {
					// Recuperamos el nombre del cementerio para hacer el set en
					// la unidad
					Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);

					// si esta correcto me compongo el bean difunto y lo meto en la hash
					difunto = mappingManager.mapDifuntoVOToBean(difuntoElem);
					
					difunto.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
					difunto.setNombreCementerio(elem.getNombre());
					difunto.setEntidad(elem.getEntidad());
					difunto.setId(elem.getId());
					difunto.setTipo(elem.getTipo());

					difunto.setIdFeatures(unidadBean.getIdFeatures());
					difunto.setIdLayers(unidadBean.getIdLayers());
					
					alRet.put(difunto.getId_difunto(), difunto);
				}
    		}
    		
            getSqlMapClientTemplate().commitTransaction();
            
    	}catch (Exception e) {
    		
    		logger.error("Error obteniendo el difunto " + e);
		}finally{
			
			getSqlMapClientTemplate().endTransaction();
		}
    	
		return alRet.values();
    	
    }
 

   /**Métodos Join de las listas de resultados obtenidas sobre las tablas tras aplicar los diferentes filtros**/
   
    /**
     * Join de los resultados ente datos de fallecimiento y lista de difuntos
     * @param listaDatosFallecimiento
     * @param listaDifuntosTemp
     * @return
     */
    private List joinDifuntosDatosFallecimiento(List listaDatosFallecimiento, List listaDifuntosTemp){
    	List<Difunto> listaDifuntos = new ArrayList<Difunto>();
    	for (int j = 0; j < listaDifuntosTemp.size(); j++) {
			Difunto difuntoElem = (Difunto) listaDifuntosTemp.get(j);
			for (int i = 0; i < listaDatosFallecimiento.size(); i++) {
				DatosFallecimiento datosElem = (DatosFallecimiento) listaDatosFallecimiento.get(i);
				if (difuntoElem.getIdDatosfallecimiento().intValue() == datosElem.getId().intValue()){
					listaDifuntos.add(difuntoElem);
					break;
				}
			}
    	}
    	return listaDifuntos;
    }    
    
    /**
     * Join de los resultados ente datos de personas y lista de difuntos
     * @param listaPersonas
     * @param listaDifuntosTemp
     * @return
     */
    private List joinDifuntosPersonas(List listaPersonas, List listaDifuntosTemp){
    	
    	List<Difunto> listaDifuntos = new ArrayList<Difunto>();
    	for (int j = 0; j < listaDifuntosTemp.size(); j++) {
			Difunto difuntoElem = (Difunto) listaDifuntosTemp.get(j);
			for (int i = 0; i < listaPersonas.size(); i++) {
				Persona personaElem = (Persona) listaPersonas.get(i);
				if (difuntoElem.getDniPersona().equalsIgnoreCase(personaElem.getDni())){
					listaDifuntos.add(difuntoElem);
					break;
				}
			}
    	}
    	return listaDifuntos;
    }
    
    /**
     * 
     * @param superpatron
     * @param patron
     * @param filtro
     * @param idCementerio
     * @param userSesion
     * @return
     * @throws Exception
     */
	public Collection getDifuntosByFilter(String superpatron, String patron,
				Object[] filtro, Integer idCementerio, Sesion userSesion) throws Exception {
	
		List<Difunto> listaDifuntos;
		List<Difunto> listaDifuntosAux = null;
		List<Difunto> listaDifuntosTemp;
		List<Persona> listaPersonas = null;
		List<DatosFallecimiento> listaDatosFallecimiento = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();
	
		DifuntoExample difuntoExample = new DifuntoExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria criteria = difuntoExample.createCriteria();
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
				}else{
					criteria = addFilter.addDifuntoFilter(criteria,campoFiltro, false);
				}
			}
			difuntoExample.or(criteria);
			listaDifuntosTemp = difuntoDAO.selectByExample(difuntoExample);
			
			/**se hace el join de las listas obtenidas de las diferentes tablas tras aplicar los filtros correspondientes**/
			if ((listaPersonas!=null) || (listaDatosFallecimiento!= null)){
				if (listaPersonas != null){
					listaDifuntosAux = joinDifuntosPersonas(listaPersonas, listaDifuntosTemp);
				}
				if (listaDatosFallecimiento != null){
					if (listaDifuntosAux!= null) listaDifuntosTemp = listaDifuntosAux;
					listaDifuntosAux = joinDifuntosDatosFallecimiento(listaDatosFallecimiento, listaDifuntosTemp);
				}
				listaDifuntos = listaDifuntosAux;
			}
			else{
				listaDifuntos = listaDifuntosTemp;
			}
			
			for (int j = 0; j < listaDifuntos.size(); j++) {
				Difunto difuntoElem = (Difunto) listaDifuntos.get(j);

				//obtengo la plaza donde esta el difunto para obtener la unidad y comprobar si esta o no en el cementerio
				Plaza plaza = plazaDAO.selectByPrimaryKey(difuntoElem.getIdPlaza());
				
				UnidadEnterramiento unidad = unidadEnterramientoDAO.selectByPrimaryKey(plaza.getIdUnidadenterramiento());
				UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidad);
				
    	    	elem_cementerioExample elemExample = new  elem_cementerioExample();
    	    	elemExample.createCriteria().andIdCementerioEqualTo(idCementerio).andIdEqualTo(unidad.getIdElemcementerio());
    	    	
    	    	List elem_cementerioList =  elemCementerioDAO.selectByExample(elemExample);
            	if (elem_cementerioList.size() != 1){
            		continue;
            	}
            	
				// Se retorna una lista pero solo debe contener un elemento
				elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
				if (elem != null) {
					// Recuperamos el nombre del cementerio para hacer el set en
					// la unidad
					Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);

					// si esta correcto me compongo el bean difunto y lo meto en la hash
					DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoElem);
					
					difunto.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
					difunto.setNombreCementerio(elem.getNombre());
					difunto.setEntidad(elem.getEntidad());
					difunto.setId(elem.getId());
					difunto.setTipo(elem.getTipo());

					difunto.setIdFeatures(unidadBean.getIdFeatures());
					difunto.setIdLayers(unidadBean.getIdLayers());
					
					alRet.put(difunto.getId_difunto(), difunto);
				
				}
			}
		}
		return alRet.values();
	}
	
	
	/**
	 * 
	 * @param superpatron
	 * @param patron
	 * @param filtro
	 * @param idCementerio
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	public Collection findDifuntos(String superpatron, String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<Difunto> listaDifuntos = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		DifuntoExample difuntoExample = new DifuntoExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			for (int i = 0; i < filtro.length; i++) {
				com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria criteria = difuntoExample.createCriteriaInternal();
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = addFilter.addDifuntoFilter(criteria,campoFiltro, true);
				if (criteria!=null){
					difuntoExample.or(criteria);
					listaDifuntos = difuntoDAO.selectByExample(difuntoExample);
				}
				
				if ((listaDifuntos != null) && (listaDifuntos.size()>0)) break; 
				difuntoExample.clear();
			}
			if (listaDifuntos!=null){
				for (int j = 0; j < listaDifuntos.size(); j++) {
					Difunto difuntoElem = (Difunto) listaDifuntos.get(j);
	
					//obtengo la plaza donde esta el difunto para obtener la unidad y comprobar si esta o no en el cementerio
					Plaza plaza = plazaDAO.selectByPrimaryKey(difuntoElem.getIdPlaza());
					
					UnidadEnterramiento unidad = unidadEnterramientoDAO.selectByPrimaryKey(plaza.getIdUnidadenterramiento());
					UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidad);
					
	    	    	elem_cementerioExample elemExample = new  elem_cementerioExample();
	    	    	elemExample.createCriteria().andIdCementerioEqualTo(idCementerio).andIdEqualTo(unidad.getIdElemcementerio());
	    	    	
	    	    	List elem_cementerioList =  elemCementerioDAO.selectByExample(elemExample);
	            	if (elem_cementerioList.size() != 1){
	            		continue;
	            	}
	            	
					// Se retorna una lista pero solo debe contener un elemento
					elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
					if (elem != null) {
						// Recuperamos el nombre del cementerio para hacer el set en
						// la unidad
						Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
	
						// si esta correcto me compongo el bean difunto y lo meto en la hash
						DifuntoBean difunto = mappingManager.mapDifuntoVOToBean(difuntoElem);
						
						difunto.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
						difunto.setNombreCementerio(elem.getNombre());
						difunto.setEntidad(elem.getEntidad());
						difunto.setId(elem.getId());
						difunto.setTipo(elem.getTipo());
	
						difunto.setIdFeatures(unidadBean.getIdFeatures());
						difunto.setIdLayers(unidadBean.getIdLayers());
						
						alRet.put(difunto.getId_difunto(), difunto);
					
					}
				}
			}
		}

		return alRet.values();
	}
    
    /**
     * deleteDifunto
     * @param idLayers
     * @param idFeatures
     * @param elem
     * @param userSesion
     * @return
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    public DifuntoBean deleteDifunto (Object[] idLayers, Object[] idFeatures, DifuntoBean elem, Sesion userSesion)
   	throws PermissionException,LockException,Exception{
    	
    	DifuntoBean difunto; 
    	try{
    		
    		getSqlMapClientTemplate().startTransaction();
    		
    		//1. Eliminar los datos de fallecimiento
    		datosFallecimientoDAO.deleteByPrimaryKey(elem.getDatosFallecimiento().getId_datosFallecimiento());

    		//2. se elimina el difunto
    		difuntoDAO.deleteByPrimaryKey(elem.getId_difunto());
    		
    		//3.Eliminar los datos de persona
    		personaDAO.deleteByPrimaryKey(elem.getPersona().getDNI());
    		
    		//4.Actualizar la plaza para dejarla libre (no se debe borrar)
    		Plaza plazadifunto = plazaDAO.selectByPrimaryKey(elem.getId_plaza());
    		plazadifunto.setEstado(Constantes.FALSE); //ya no esta asignada
    		plazadifunto.setModicado(new Date());
    		plazadifunto.setSituacion("Liberado");
    		
    		
    		plazaDAO.updateByPrimaryKey(plazadifunto);
    		
    		//5.Actualizo en numero de plazas asinadas en la unidad de enterramiento
			Plaza record = new Plaza();
			record.setIdUnidadenterramiento(plazadifunto.getIdUnidadenterramiento());
			record.setEstado(Constantes.TRUE);
			int numPlazas = plazaDAO.selectCountPlazasAsignadas(record);
			
			UnidadEnterramiento unidad = unidadEnterramientoDAO.selectByPrimaryKey(plazadifunto.getIdUnidadenterramiento());
			unidad.setNumplazas(numPlazas);
			unidadEnterramientoDAO.updateByPrimaryKeySelective(unidad);
    		
    		
    		getSqlMapClientTemplate().commitTransaction();
    		
    	}catch (Exception e) {
			logger.error("Error eliminando un difunto" + e);
			
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
		
    	return elem;
    }

}
