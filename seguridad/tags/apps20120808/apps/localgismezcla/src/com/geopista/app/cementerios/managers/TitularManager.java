package com.geopista.app.cementerios.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.geopista.app.catastro.ListadoHabitantesPanel;
import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.CementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ConcesionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PersonaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.RelTitularDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.UnidadEnterramientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.elem_cementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ConcesionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PersonaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.RelTitularDAO;
import com.geopista.app.cementerios.business.dao.interfaces.UnidadEnterramientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.elem_cementerioDAO;
import com.geopista.app.cementerios.business.vo.Bloque;
import com.geopista.app.cementerios.business.vo.BloqueExample;
import com.geopista.app.cementerios.business.vo.Cementerio;
import com.geopista.app.cementerios.business.vo.Concesion;
import com.geopista.app.cementerios.business.vo.Persona;
import com.geopista.app.cementerios.business.vo.PersonaExample;
import com.geopista.app.cementerios.business.vo.RelTitular;
import com.geopista.app.cementerios.business.vo.RelTitularKey;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.geopista.app.cementerios.business.vo.elem_cementerioExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;

public class TitularManager extends DAO {

	
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TitularManager.class);
	
	private static TitularManager instance;
	
	private RelTitularDAO relTitularDAO;
	private PersonaDAO personaDAO;
	private UnidadEnterramientoDAO unidadEnterramientoDAO;
	private ConcesionDAO concesionDAO;
	private elem_cementerioDAO elemCementerioDAO;
	private CementerioDAO cementerioDAO;
	
    private AddFilter addFilter;
    private MappingManager mappingManager;
   
	
    public TitularManager(){
    	
    	relTitularDAO = new RelTitularDAOImpl();
    	personaDAO = new PersonaDAOImpl();    	
    	unidadEnterramientoDAO = new UnidadEnterramientoDAOImpl();
    	concesionDAO = new ConcesionDAOImpl();
    	elemCementerioDAO = new elem_cementerioDAOImpl();
    	cementerioDAO = new CementerioDAOImpl();

    	
        addFilter = AddFilter.getInstance();
        mappingManager = MappingManager.getIntance();
    }
    
    public static TitularManager getInstance(){
    	if (instance == null){
    		instance = new TitularManager();
    	}
    	return instance;
    }
	
	
    /**************************************************************** TITULAR ****************************************************************************/
    
    public PersonaBean updateTitular (PersonaBean persona, Sesion userSesion) throws PermissionException, LockException, Exception {
    
	try {
		
		getSqlMapClientTemplate().startTransaction();

		// 1.Insert la persona/ titular principal de la concesion

		Persona titular = new Persona();
		titular.setApellido1(persona.getApellido1());
		titular.setApellido2(persona.getApellido2());
		titular.setDni(persona.getDNI());
		titular.setDomicilio(persona.getDomicilio());
		titular.setEstadoCivil(persona.getEstado_civil());
		titular.setFechaNacimiento(persona.getFecha_nacimiento());
		titular.setNombre(persona.getNombre());
		titular.setPoblacion(persona.getPoblacion());
		titular.setSexo(persona.getSexo());
		titular.setTelefono(persona.getTelefono());

		personaDAO.updateByPrimaryKeySelective(titular);
	
		getSqlMapClientTemplate().commitTransaction();
		
		}catch (Exception e) {
			logger.error("Error actualizando el titular" + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
		return persona;
    }
    
    public Collection getAllTitularesByCementerio(Integer idCementerio) throws Exception {
       	
    	List<RelTitular> listaRelTitulares;
       	HashMap alRet= new HashMap();
       	
       	listaRelTitulares = relTitularDAO.selectAll();
       	
       	PersonaBean personaBean = null;
       	Persona persona;
       	RelTitular elem;
       	
       	for (int i = 0; i < listaRelTitulares.size(); i++) {
       		elem = listaRelTitulares.get(i);
       		if (elem.getEsprincipal()){
       			
       			persona = personaDAO.selectByPrimaryKey(listaRelTitulares.get(i).getDniPersona());
       			personaBean = mappingManager.mapPersonaVoToBean(persona);
       			
       			Concesion concesion = concesionDAO.selectByPrimaryKey(listaRelTitulares.get(i).getIdConcesion());

       			//Completamos el bean de persona con la informacion de concesion
       			
				/** Unidad de enterramiento**/
				UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(concesion.getIdUnidad());
				UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidadVO);
				
				// Completamos los campos de cementerio que es el bean generico
				elem_cementerioExample elemExample = new elem_cementerioExample();
				elemExample.createCriteria()
						.andIdCementerioEqualTo(idCementerio)
						.andIdEqualTo(unidadVO.getIdElemcementerio());

				List elem_cementerioList = elemCementerioDAO.selectByExample(elemExample);

				if (elem_cementerioList.size() != 1) {
					continue;
				}
				// Se retorna una lista pero solo debe contener un elemento
				elem_cementerio elemCementerio = (elem_cementerio) elem_cementerioList.get(0);
				if (elemCementerio != null) {
					// Recuperamos el nombre del cementerio para hacer el set en
					// la unidad
					Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);

					personaBean.setIdMunicipio(String.valueOf(elemCementerio.getIdMunicipio()));
					personaBean.setNombreCementerio(elemCementerio.getNombre());
					personaBean.setEntidad(elemCementerio.getEntidad());
					personaBean.setId(elemCementerio.getId());
					personaBean.setTipo(elemCementerio.getTipo());

					personaBean.setIdFeatures(unidadBean.getIdFeatures());
					personaBean.setIdLayers(unidadBean.getIdLayers());
				}
       			
       		}
			
       		alRet.put(personaBean.getDNI(), personaBean);
		}
       	
       	return alRet.values();
    }
    
    public Collection getTitularesByConcesion (Integer idConcesion) throws Exception {

    	List<RelTitular> listaRelTitulares;
       	HashMap alRet= new HashMap();
       	
       	listaRelTitulares = relTitularDAO.selectByConcesion(idConcesion);
       	
       	PersonaBean personaBean = null;
       	Persona persona;
       	RelTitular elem;
       	
       	for (int i = 0; i < listaRelTitulares.size(); i++) {
       		elem = listaRelTitulares.get(i);
       		if (elem.getEsprincipal()){
       			persona = personaDAO.selectByPrimaryKey(listaRelTitulares.get(i).getDniPersona());
       			personaBean = mappingManager.mapPersonaVoToBean(persona);
       			
       			Concesion concesion = concesionDAO.selectByPrimaryKey(listaRelTitulares.get(i).getIdConcesion());

       			//Completamos el bean de persona con la informacion de concesion
       			
				/** Unidad de enterramiento**/
				UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(concesion.getIdUnidad());
				UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidadVO);
				
				// Completamos los campos de cementerio que es el bean generico
				elem_cementerioExample elemExample = new elem_cementerioExample();
				elemExample.createCriteria()
						.andIdEqualTo(unidadVO.getIdElemcementerio());

				List elem_cementerioList = elemCementerioDAO.selectByExample(elemExample);

				if (elem_cementerioList.size() != 1) {
					continue;
				}
				// Se retorna una lista pero solo debe contener un elemento
				elem_cementerio elemCementerio = (elem_cementerio) elem_cementerioList.get(0);
				if (elemCementerio != null) {
					// Recuperamos el nombre del cementerio para hacer el set en
					// la unidad
					Cementerio cementerio = cementerioDAO.selectByPrimaryKey(elemCementerio.getIdCementerio());

					personaBean.setIdMunicipio(String.valueOf(elemCementerio.getIdMunicipio()));
					personaBean.setNombreCementerio(elemCementerio.getNombre());
					personaBean.setEntidad(elemCementerio.getEntidad());
					personaBean.setId(elemCementerio.getId());
					personaBean.setTipo(elemCementerio.getTipo());

					personaBean.setIdFeatures(unidadBean.getIdFeatures());
					personaBean.setIdLayers(unidadBean.getIdLayers());
				}

       		}
			
       		alRet.put(personaBean.getDNI(), personaBean);
		}
       	
       	return alRet.values();

    }
    
    
    public PersonaBean getTitularPrincipalByConcesion (Integer idConcesion) throws Exception {

    	List<RelTitular> listaRelTitulares;
       //	HashMap alRet= new HashMap();
       	
       	listaRelTitulares = relTitularDAO.selectByConcesion(idConcesion);
       	
       	PersonaBean personaBean = null;
       	Persona persona;
       	RelTitular elem;
       	
       	for (int i = 0; i < listaRelTitulares.size(); i++) {
       		elem = listaRelTitulares.get(i);
       		if (elem.getEsprincipal()){
       			persona = personaDAO.selectByPrimaryKey(listaRelTitulares.get(i).getDniPersona());
       			personaBean = mappingManager.mapPersonaVoToBean(persona);
       			
       			Concesion concesion = concesionDAO.selectByPrimaryKey(listaRelTitulares.get(i).getIdConcesion());

       			//Completamos el bean de persona con la informacion de concesion
       			
				/** Unidad de enterramiento**/
				UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(concesion.getIdUnidad());
				UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidadVO);
				
				// Completamos los campos de cementerio que es el bean generico
				elem_cementerioExample elemExample = new elem_cementerioExample();
				elemExample.createCriteria()
						.andIdEqualTo(unidadVO.getIdElemcementerio());

				List elem_cementerioList = elemCementerioDAO.selectByExample(elemExample);

				if (elem_cementerioList.size() != 1) {
					continue;
				}
				// Se retorna una lista pero solo debe contener un elemento
				elem_cementerio elemCementerio = (elem_cementerio) elem_cementerioList.get(0);
				if (elemCementerio != null) {
					// Recuperamos el nombre del cementerio para hacer el set en
					// la unidad
					Cementerio cementerio = cementerioDAO.selectByPrimaryKey(elemCementerio.getIdCementerio());

					personaBean.setIdMunicipio(String.valueOf(elemCementerio.getIdMunicipio()));
					personaBean.setNombreCementerio(elemCementerio.getNombre());
					personaBean.setEntidad(elemCementerio.getEntidad());
					personaBean.setId(elemCementerio.getId());
					personaBean.setTipo(elemCementerio.getTipo());

					personaBean.setIdFeatures(unidadBean.getIdFeatures());
					personaBean.setIdLayers(unidadBean.getIdLayers());
				}

       		}
			
       		//alRet.put(personaBean.getDNI(), personaBean);
       		break;
		}
       	
       	//return alRet.values();
       	return personaBean;

    }
    
    
    public PersonaBean deleteTitular (PersonaBean elem, Sesion userSesion)
   	throws PermissionException,LockException,Exception{
    	
    	
    	try{
    		getSqlMapClientTemplate().startTransaction();
    		
    		//1. Eliminar los datos de fallecimiento
    		personaDAO.deleteByPrimaryKey(elem.getDNI());
    	
            getSqlMapClientTemplate().commitTransaction();
    
    	}catch (Exception e) {
    		logger.error("Error eliminando el titular " + e);
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
    
		return elem;
    }
    
  public Collection getTitularesByFilter(String superpatron, String patron, Object[] filtro,  Integer idCementerio, Sesion userSesion) throws Exception {
	  	
	  	List<Persona> listaPersonas;
	  	HashMap alRet= new HashMap();
	  	
	  	PersonaExample personaExample = new PersonaExample();
	  	if (filtro!= null){
	  		CampoFiltro campoFiltro;
	  		com.geopista.app.cementerios.business.vo.PersonaExample.Criteria criteria = personaExample.createCriteria();
	  		for (int i = 0; i < filtro.length; i++) {
					campoFiltro = (CampoFiltro) filtro[i];
					criteria = (com.geopista.app.cementerios.business.vo.PersonaExample.Criteria) addFilter.addPersonaFilter (criteria, campoFiltro, false);
	  		}
	  		personaExample.or(criteria);
	  		listaPersonas = personaDAO.selectByExample(personaExample);
	  			
	  		for (int j = 0; j < listaPersonas.size(); j++) {
	  			Persona persona = (Persona) listaPersonas.get(j);
	  			
	  			Collection c = relTitularDAO.selectByTitular(persona.getDni());
	  			Object[] arrayElems = c.toArray();
	            int n = arrayElems.length;
	  			
	  			//if (!c.isEmpty()){
	  			for (int k = 0; k < arrayElems.length; k++) {
		  			PersonaBean personaBean = mappingManager.mapPersonaVoToBean(persona);
		  			
		  			/**añadimos la informacion generica**/
	       			Concesion concesion = concesionDAO.selectByPrimaryKey(((RelTitularKey) arrayElems[k]).getIdConcesion());

	       			//Completamos el bean de persona con la informacion de concesion
	       			
					/** Unidad de enterramiento**/
					UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(concesion.getIdUnidad());
					UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidadVO);
					
					// Completamos los campos de cementerio que es el bean generico
					elem_cementerioExample elemExample = new elem_cementerioExample();
					elemExample.createCriteria()
							.andIdEqualTo(unidadVO.getIdElemcementerio());

					List elem_cementerioList = elemCementerioDAO.selectByExample(elemExample);

					if (elem_cementerioList.size() != 1) {
						continue;
					}
					// Se retorna una lista pero solo debe contener un elemento
					elem_cementerio elemCementerio = (elem_cementerio) elem_cementerioList.get(0);
					if (elemCementerio != null) {
						// Recuperamos el nombre del cementerio para hacer el set en
						// la unidad
						Cementerio cementerio = cementerioDAO.selectByPrimaryKey(elemCementerio.getIdCementerio());

						personaBean.setIdMunicipio(String.valueOf(elemCementerio.getIdMunicipio()));
						personaBean.setNombreCementerio(elemCementerio.getNombre());
						personaBean.setEntidad(elemCementerio.getEntidad());
						personaBean.setId(elemCementerio.getId());
						personaBean.setTipo(elemCementerio.getTipo());

						personaBean.setIdFeatures(unidadBean.getIdFeatures());
						personaBean.setIdLayers(unidadBean.getIdLayers());
					}
		  			
		  			alRet.put(personaBean.getDNI(), personaBean);
	  			}
			}
			}
	      return alRet.values();
	  }
    
	public Collection findTitulares(String superpatron, String patron, Object[] filtro, Integer idCementerio, Sesion userSesion) throws Exception {

	  	ArrayList<Persona> listaPersonas = new ArrayList<Persona>();
	  	HashMap alRet= new HashMap();
	  	
	  	PersonaExample personaExample = new PersonaExample();
	  	if (filtro!= null){
			CampoFiltro campoFiltro;
			for (int i = 0; i < filtro.length; i++) {
				com.geopista.app.cementerios.business.vo.PersonaExample.Criteria criteria = personaExample.createCriteria();
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.PersonaExample.Criteria) addFilter.addPersonaFilter (criteria, campoFiltro, true);				
				
		  		personaExample.or(criteria);
		  		listaPersonas = (ArrayList<Persona>) personaDAO.selectByExample(personaExample);
				
				if ((listaPersonas != null) && (listaPersonas.size()>0)) break; 
				personaExample.clear();
				
			}
			
	  		for (int j = 0; j < listaPersonas.size(); j++) {
	  			Persona persona = (Persona) listaPersonas.get(j);
	  			Collection c = relTitularDAO.selectByTitular(persona.getDni());
	  			
	  			//if (!c.isEmpty()){
	  			Object[] arrayElems = c.toArray();
	            int n = arrayElems.length;
	  			
	  			//if (!c.isEmpty()){
	  			for (int k = 0; k < arrayElems.length; k++) {
		  			PersonaBean personaBean = mappingManager.mapPersonaVoToBean(persona);
		  			
	       			Concesion concesion = concesionDAO.selectByPrimaryKey(((RelTitularKey) arrayElems[k]).getIdConcesion());

	       			//Completamos el bean de persona con la informacion de concesion
	       			
					/** Unidad de enterramiento**/
					UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(concesion.getIdUnidad());
					UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidadVO);
					
					// Completamos los campos de cementerio que es el bean generico
					elem_cementerioExample elemExample = new elem_cementerioExample();
					elemExample.createCriteria()
							.andIdEqualTo(unidadVO.getIdElemcementerio());

					List elem_cementerioList = elemCementerioDAO.selectByExample(elemExample);

					if (elem_cementerioList.size() != 1) {
						continue;
					}
					// Se retorna una lista pero solo debe contener un elemento
					elem_cementerio elemCementerio = (elem_cementerio) elem_cementerioList.get(0);
					if (elemCementerio != null) {
						// Recuperamos el nombre del cementerio para hacer el set en
						// la unidad
						Cementerio cementerio = cementerioDAO.selectByPrimaryKey(elemCementerio.getIdCementerio());

						personaBean.setIdMunicipio(String.valueOf(elemCementerio.getIdMunicipio()));
						personaBean.setNombreCementerio(elemCementerio.getNombre());
						personaBean.setEntidad(elemCementerio.getEntidad());
						personaBean.setId(elemCementerio.getId());
						personaBean.setTipo(elemCementerio.getTipo());

						personaBean.setIdFeatures(unidadBean.getIdFeatures());
						personaBean.setIdLayers(unidadBean.getIdLayers());
					}
		  			
		  			alRet.put(personaBean.getDNI(), personaBean);
	  			}
			}
		}

		return alRet.values();
	}
}
