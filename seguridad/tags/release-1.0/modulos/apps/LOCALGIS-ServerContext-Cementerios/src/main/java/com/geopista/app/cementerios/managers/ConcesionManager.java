package com.geopista.app.cementerios.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jj2000.j2k.codestream.HeaderInfo.COC;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.CementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.CementerioFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ConcesionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.HistoricoPropiedadDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PersonaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.RelTitularDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.TarifaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.UnidadEnterramientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.elem_cementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ConcesionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.HistoricoDifuntosDAO;
import com.geopista.app.cementerios.business.dao.interfaces.HistoricoPropiedadDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PersonaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.RelTitularDAO;
import com.geopista.app.cementerios.business.dao.interfaces.TarifaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.UnidadEnterramientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.elem_cementerioDAO;
import com.geopista.app.cementerios.business.vo.Cementerio;
import com.geopista.app.cementerios.business.vo.CementerioFeatureKey;
import com.geopista.app.cementerios.business.vo.Concesion;
import com.geopista.app.cementerios.business.vo.ConcesionExample;
import com.geopista.app.cementerios.business.vo.HistoricoDifuntos;
import com.geopista.app.cementerios.business.vo.HistoricoPropiedad;
import com.geopista.app.cementerios.business.vo.Persona;
import com.geopista.app.cementerios.business.vo.RelTitular;
import com.geopista.app.cementerios.business.vo.RelTitularKey;
import com.geopista.app.cementerios.business.vo.Tarifa;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.geopista.app.cementerios.business.vo.elem_cementerioExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.cementerios.TarifaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;

public class ConcesionManager extends DAO {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ConcesionManager.class);
	
	private static ConcesionManager instance;
	
    private CementerioDAO cementerioDAO;
    private UnidadEnterramientoDAO unidadEnterramientoDAO;
    private elem_cementerioDAO elemCementerioDAO;
    private CementerioFeatureDAO cementerioFeatureDAO;
    private PersonaDAO personaDAO;
    private TarifaDAO tarifaDAO;
    private ConcesionDAO concesionDAO;
    private RelTitularDAO relTitularDAO;
	private HistoricoPropiedadDAO historicoDAO;

    
    
    private MappingManager mappingManager;
    private UnidadEnterramientoManager unidadManager;
    
    public ConcesionManager(){
        
        unidadEnterramientoDAO = new UnidadEnterramientoDAOImpl();
        elemCementerioDAO = new elem_cementerioDAOImpl();
        cementerioFeatureDAO = new CementerioFeatureDAOImpl();
        personaDAO = new PersonaDAOImpl();
        tarifaDAO = new TarifaDAOImpl();
        concesionDAO =  new ConcesionDAOImpl();
        relTitularDAO = new RelTitularDAOImpl();
        cementerioDAO = new CementerioDAOImpl();
        historicoDAO = new HistoricoPropiedadDAOImpl();
        
        mappingManager = MappingManager.getIntance();
        unidadManager = UnidadEnterramientoManager.getInstance();
    }
    
    public static ConcesionManager getInstance(){
    	if (instance == null){
    		instance = new ConcesionManager();
    	}
    	return instance;
    }
		
	
	/**************************************************************** CONCESION ****************************************************************************/

	public ConcesionBean insertConcesion(Object[] idLayers, Object[] idFeatures, ConcesionBean elem, Integer idCementerio, Sesion userSesion)
			throws PermissionException, LockException, Exception {

		try {
			/**Start transaccion**/
			getSqlMapClientTemplate().startTransaction();
			
			// 1.Insert la persona/ titular principal de la concesion
			PersonaBean persona = elem.getTitular();

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
			
			personaDAO.insert(titular);

			// 3. La unidad de enterramiento no hay que añadirla puesto que debe
			// estar creada

			Concesion concesion = new Concesion();
			concesion.setEstado(elem.getEstado());
			concesion.setFechaFin(elem.getFecha_fin());
			concesion.setFechaIni(elem.getFecha_ini());
			concesion.setIdTarifa(elem.getTarifa().getId_tarifa());
			concesion.setIdUnidad((int) elem.getUnidad().getIdUe());
			concesion.setLocalizado(elem.getLocalizacion());
			concesion.setCodigo(elem.getCodigo());			
			concesion.setTipoConcesion(elem.getTipo_concesion());
			concesion.setUltimaRenova(elem.getFecha_ultRenovacion());
			concesion.setUltimoTitular("Concesión inicial");
			concesion.setDescripcion(elem.getDescripcion());
			concesion.setBonificacion(elem.getBonificacion());
			concesion.setPrecioFinal(elem.getPrecio_final());
			concesion.setEstado(elem.getEstado());

			concesionDAO.insert(concesion);

			// 5. Se recupera la id de la concesion
			int id_concesion = concesionDAO.selectByLastSeqKey();
			elem.setId_concesion(id_concesion);

			// 6. Se inserta en la tabla relacion
			RelTitular relTitular = new RelTitular();
			relTitular.setDniPersona(persona.getDNI());
			relTitular.setIdConcesion(id_concesion);
			relTitular.setEsprincipal(true);

			relTitularDAO.insert(relTitular);
			
			HistoricoPropiedad historicoPropiedad = new HistoricoPropiedad();
			historicoPropiedad.setFechaOperacion(new Date());
			historicoPropiedad.setComentario(concesion.getDescripcion());
			historicoPropiedad.setDniTitular(persona.getDNI());
			historicoPropiedad.setIdElem(id_concesion);
			//historicoPropiedad.setTipo(Integer.parseInt(Const.PATRON_CONCESION));
			historicoPropiedad.setTipo(Const.PATRON_CONCESION);
		
			historicoDAO.insert(historicoPropiedad);

			// 7. Inserto los cotitulares dados de alta...

			List<PersonaBean> cotitulares = elem.getCotitulares();
			if (cotitulares != null) {
				PersonaBean cotitularesElem;
				Persona cotitular;
				for (int i = 0; i < cotitulares.size(); i++) {
					cotitularesElem = cotitulares.get(i);
					cotitular = new Persona();

					cotitular.setApellido1(cotitularesElem.getApellido1());
					cotitular.setApellido2(cotitularesElem.getApellido2());
					cotitular.setDni(cotitularesElem.getDNI());
					cotitular.setDomicilio(cotitularesElem.getDomicilio());
					cotitular.setEstadoCivil(cotitularesElem.getEstado_civil());
					cotitular.setFechaNacimiento(cotitularesElem
							.getFecha_nacimiento());
					cotitular.setNombre(cotitularesElem.getNombre());
					cotitular.setPoblacion(cotitularesElem.getPoblacion());
					cotitular.setSexo(cotitularesElem.getSexo());
					cotitular.setTelefono(cotitularesElem.getTelefono());

					// 8. Inserto en persona --> los datos de la nueva alta de
					// persona como cotitular de la concesion
					personaDAO.insert(cotitular);
					// 9.Insertar la relacion en la tabla de relacion de
					// titulares, con principal a false
					relTitular = new RelTitular();
					relTitular.setDniPersona(cotitular.getDni());
					relTitular.setIdConcesion(id_concesion);
					relTitular.setEsprincipal(false);

					relTitularDAO.insert(relTitular);

				}
			}
			
			/**si todo ha ido correctamente hacemos el commit**/
			getSqlMapClientTemplate().commitTransaction();
			
			return elem;

		} catch (Exception e) {
			logger.error("returnUnidadEnterramiento: " + e.getMessage());
			throw e;
		}finally{
			getSqlMapClientTemplate().endTransaction();
		}
	}

	/**
	 * updateConcesion
	 * 
	 * @param elem
	 * @param userSesion
	 * @return
	 * @throws PermissionException
	 * @throws LockException
	 * @throws Exception
	 */
	public ConcesionBean updateConcesion(ConcesionBean elem, Sesion userSesion) throws PermissionException, LockException, Exception {

		try {
			
			getSqlMapClientTemplate().startTransaction();

			// 1.Insert la persona/ titular principal de la concesion
			PersonaBean persona = elem.getTitular();

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

			// 4. Tenemos que generar el vo de concesion e insertarlo
			Concesion concesion = new Concesion();

			concesion.setEstado(elem.getEstado());
			concesion.setFechaFin(elem.getFecha_fin());
			concesion.setFechaIni(elem.getFecha_ini());
			concesion.setIdUnidad((int) elem.getUnidad().getIdUe());
			concesion.setLocalizado(elem.getLocalizacion());
			concesion.setCodigo(elem.getCodigo());
			concesion.setEstado(elem.getEstado());
			concesion.setTipoConcesion(elem.getTipo_concesion());
			concesion.setUltimaRenova(elem.getFecha_ultRenovacion());
			concesion.setUltimoTitular("titularnateior");
			concesion.setId((int) elem.getId_concesion());
			concesion.setDescripcion(elem.getDescripcion());
			concesion.setBonificacion(elem.getBonificacion());
			concesion.setPrecioFinal(elem.getPrecio_final());
			
			concesionDAO.updateByPrimaryKeySelective(concesion);

			// //5. Se recupera la id de la concesion
			// int id_concesion = concesionDAO.selectByLastSeqKey();
			//
			// //6. Se inserta en la tabla relacion
			// RelTitular relTitular = new RelTitular();
			// relTitular.setDniPersona(persona.getDNI());
			// relTitular.setIdConcesion(id_concesion);
			// relTitular.setEsprincipal(true);
			//
			// try{
			// relTitularDAO.insert(relTitular);
			// }catch (Exception e) {
			// logger.error("insert reltitular: "+ e.getMessage());
			// throw e;
			// }
			//
			// //7. Inserto los cotitulares dados de alta...
			// List<PersonaBean> cotitulares = elem.getCotitulares();
			// PersonaBean cotitularesElem;
			// Persona cotitular;
			// for (int i = 0; i < cotitulares.size(); i++) {
			// cotitularesElem = cotitulares.get(i);
			// cotitular = new Persona();
			//
			// cotitular.setApellido1(cotitularesElem.getApellido1());
			// cotitular.setApellido2(cotitularesElem.getApellido2());
			// cotitular.setDni(cotitularesElem.getDNI());
			// cotitular.setDomicilio(cotitularesElem.getDomicilio());
			// cotitular.setEstadoCivil(cotitularesElem.getEstado_civil());
			// cotitular.setFechaNacimiento(cotitularesElem.getFecha_nacimiento());
			// cotitular.setNombre(cotitularesElem.getNombre());
			// cotitular.setPoblacion(cotitularesElem.getPoblacion());
			// cotitular.setSexo(cotitularesElem.getSexo());
			// cotitular.setTelefono(cotitularesElem.getTelefono());
			//
			// //8. Inserto en persona --> los datos de la nueva alta de persona
			// como cotitular de la concesion
			// try{
			// personaDAO.insert(cotitular);
			// //9.Insertar la relacion en la tabla de relacion de titulares,
			// con principal a false
			// relTitular = new RelTitular();
			// relTitular.setDniPersona(cotitular.getDni());
			// relTitular.setIdConcesion(id_concesion);
			// relTitular.setEsprincipal(false);
			//
			// relTitularDAO.insert(relTitular);
			//
			// }catch (Exception e) {
			// logger.error("insert cotitular + reltitular: "+ e.getMessage());
			// continue;
			// }
			// }

			getSqlMapClientTemplate().commitTransaction();
			
			return elem;

		} catch (Exception e) {
			logger.error("returnUnidadEnterramiento: " + e.getMessage());
			throw e;
			
		}finally{
				
			getSqlMapClientTemplate().endTransaction();
		}

	}

	/**
	 * deleteConcesion
	 * 
	 * @param idLayers
	 * @param idFeatures
	 * @param elem
	 * @param userSesion
	 * @return
	 * @throws PermissionException
	 * @throws LockException
	 * @throws Exception
	 */
	public ConcesionBean deleteConcesion(ConcesionBean elem, Sesion userSesion)
			throws PermissionException, LockException, Exception {

		try {
			
			getSqlMapClientTemplate().startTransaction();
			
			RelTitularKey key = new RelTitularKey();
			key.setDniPersona(elem.getTitular().getDNI());
			key.setIdConcesion((int) elem.getId_concesion());

			Integer res = relTitularDAO.deleteByPrimaryKey(key);

			List<PersonaBean> cotitulares = elem.getCotitulares();
			for (int j = 0; j < cotitulares.size(); j++) {

				res = personaDAO
						.deleteByPrimaryKey(cotitulares.get(j).getDNI());

				key = new RelTitularKey();
				key.setDniPersona(cotitulares.get(j).getDNI());
				key.setIdConcesion((int) elem.getId_concesion());
				res = relTitularDAO.deleteByPrimaryKey(key);
			}

			res = concesionDAO.deleteByPrimaryKey((int) elem.getId_concesion());

//			// 1.Elimino la tarifa asociada y creada para esa concesion
//			res = tarifaDAO.deleteByPrimaryKey((int) elem.getTarifa().getId());

			// 2. Eliminar el titular principal y los cotitulares así como la
			// relacion en la tabla.
			res = personaDAO.deleteByPrimaryKey(elem.getTitular().getDNI());

			getSqlMapClientTemplate().commitTransaction();
			
		} catch (Exception e) {
			logger.error("deleteConcesion:" + e.getMessage());
			throw e;
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
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	public ConcesionBean getConcesion(String superpatron, String patron,
			Object[] filtro, Object[] idLayers, Object[] idFeatures,
			Integer idCementerio, Sesion userSesion) throws Exception {

		ConcesionBean alRet = null;

		for (int j = 0; j < idFeatures.length; j++) {
			List lista = new ArrayList<CementerioFeatureKey>();

			CementerioFeatureKey key = new CementerioFeatureKey();
			key.setIdFeature(Integer.parseInt((String) idFeatures[j]));
			key.setIdLayer("unidad_enterramiento");
			lista = cementerioFeatureDAO.selectByFeatureAndLayerUnidad(key);

			for (int i = 0; i < lista.size(); i++) {
				key = (CementerioFeatureKey) lista.get(i);
				UnidadEnterramiento unidad = unidadEnterramientoDAO
						.selectByPrimaryKey(key.getIdElem());
				elem_cementerio elem = elemCementerioDAO
						.selectByPrimaryKey(unidad.getIdElemcementerio());

				Concesion concesion = concesionDAO.selectByUnidad(unidad
						.getId());

				if ((unidad == null) || (elem == null) || (concesion == null)) {
					continue;
				}

				// 1.se crea el bean de unidad de enterramiento asociado a la
				// concesion..
				UnidadEnterramientoBean unidadBean = unidadManager.getUnidad(superpatron, patron, filtro, idLayers, idFeatures,
						idCementerio, userSesion);

				// 2.creo la concesion
				ConcesionBean concesionBean = new ConcesionBean();
				concesionBean.setEntidad(elem.getEntidad());
				concesionBean.setEstado(concesion.getEstado());
				concesionBean.setFecha_fin(concesion.getFechaFin());
				concesionBean.setFecha_ini(concesion.getFechaIni());
				concesionBean.setFecha_ultRenovacion(concesion.getUltimaRenova());
				concesionBean.setId_concesion(concesion.getId());
				concesionBean.setId(elem.getId());
				concesionBean.setIdFeatures(unidadBean.getIdFeatures());
				concesionBean.setIdLayers(unidadBean.getIdLayers());
				concesionBean.setIdMunicipio(unidadBean.getIdMunicipio());
				concesionBean.setNombreCementerio(elem.getNombre());
				concesionBean.setSuperPatron(superpatron);
				concesionBean.setTipo_concesion(concesion.getTipoConcesion());
				concesionBean.setTipo(elem.getTipo());
				concesionBean.setUnidad(unidadBean);
				concesionBean.setBonificacion(concesion.getBonificacion());
				concesionBean.setPrecio_final(concesion.getPrecioFinal());
				concesionBean.setDescripcion(concesion.getDescripcion());
				concesionBean.setLocalizacion(concesion.getLocalizado());
				concesionBean.setCodigo(concesion.getCodigo());
				concesionBean.setEstado(concesion.getEstado());

				// Titulares
				ArrayList<RelTitular> relTitularList = (ArrayList<RelTitular>) relTitularDAO
						.selectByConcesion((int) concesionBean
								.getId_concesion());

				RelTitular relTitular;
				ArrayList<PersonaBean> cotitulares = new ArrayList<PersonaBean>();

				for (int k = 0; k < relTitularList.size(); k++) {
					relTitular = relTitularList.get(k);

					Persona persona = personaDAO.selectByPrimaryKey(relTitular
							.getDniPersona());

					PersonaBean titular = new PersonaBean();
					titular.setApellido1(persona.getApellido1());
					titular.setApellido2(persona.getApellido2());
					titular.setDNI(persona.getDni());
					titular.setDomicilio(persona.getDomicilio());
					titular.setEstado_civil(persona.getEstadoCivil());
					titular.setFecha_nacimiento(persona.getFechaNacimiento());
					titular.setNombre(persona.getNombre());
					titular.setPoblacion(persona.getPoblacion());
					titular.setSexo(persona.getSexo());
					titular.setTelefono(persona.getTelefono());

					if (relTitular.getEsprincipal()) {
						concesionBean.setTitular(titular);
					} else {
						cotitulares.add(titular);
					}

				}

				concesionBean.setCotitulares(cotitulares);

				// 3.Recupero la tarifa asociada a la concesion
				Tarifa tarifa = tarifaDAO.selectByPrimaryKey(concesion.getIdTarifa());
				// tengo que hacer el mapeo de Tarifa a tarifabean
				TarifaBean tarifaBean = new TarifaBean();
				tarifaBean.setConcepto(tarifa.getConcepto());
				tarifaBean.setTipo_tarifa(tarifa.getTipoTarifa());
				tarifaBean.setPrecio(tarifa.getPrecio());
				tarifaBean.setTipo_calculo(tarifa.getTipoCalculo());
				tarifaBean.setId(tarifa.getId());

				concesionBean.setTarifa(tarifaBean);

				alRet = concesionBean;
			}
		}
		return alRet;
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
	public Collection getConcesionesByFeature(String superpatron, String patron,Object[] filtro, Object[] idLayers, Object[] idFeatures,
				Integer idCementerio, Sesion userSesion) throws Exception {

//		ConcesionBean alRet = null;
		HashMap alRet = new HashMap();

		for (int j = 0; j < idFeatures.length; j++) {
			List lista = new ArrayList<CementerioFeatureKey>();

			CementerioFeatureKey key = new CementerioFeatureKey();
			key.setIdFeature(Integer.parseInt((String) idFeatures[j]));
			key.setIdLayer("unidad_enterramiento");
			lista = cementerioFeatureDAO.selectByFeatureAndLayerUnidad(key);

			for (int i = 0; i < lista.size(); i++) {
				key = (CementerioFeatureKey) lista.get(i);
				UnidadEnterramiento unidad = unidadEnterramientoDAO.selectByPrimaryKey(key.getIdElem());
				elem_cementerio elem = elemCementerioDAO.selectByPrimaryKey(unidad.getIdElemcementerio());

				Concesion concesion = concesionDAO.selectByUnidad(unidad.getId());

				if ((unidad == null) || (elem == null) || (concesion == null)) {
					continue;
				}

				// 1.se crea el bean de unidad de enterramiento asociado a la
				// concesion..
				UnidadEnterramientoBean unidadBean = unidadManager.getUnidad(superpatron, patron, filtro, idLayers, idFeatures,
						idCementerio, userSesion);

				// 2.creo la concesion
				ConcesionBean concesionBean = new ConcesionBean();
				 concesionBean.setDescripcion(concesion.getDescripcion());
				concesionBean.setEntidad(elem.getEntidad());
				concesionBean.setEstado(concesion.getEstado());
				concesionBean.setFecha_fin(concesion.getFechaFin());
				concesionBean.setFecha_ini(concesion.getFechaIni());
				concesionBean.setFecha_ultRenovacion(concesion.getUltimaRenova());
				concesionBean.setId_concesion(concesion.getId());
				concesionBean.setId(elem.getId());
				concesionBean.setIdFeatures(unidadBean.getIdFeatures());
				concesionBean.setIdLayers(unidadBean.getIdLayers());
				concesionBean.setIdMunicipio(unidadBean.getIdMunicipio());
				concesionBean.setNombreCementerio(elem.getNombre());
				concesionBean.setSuperPatron(superpatron);
				concesionBean.setTipo_concesion(concesion.getTipoConcesion());
				concesionBean.setTipo(elem.getTipo());
				concesionBean.setUnidad(unidadBean);
				concesionBean.setBonificacion(concesion.getBonificacion());
				concesionBean.setPrecio_final(concesion.getPrecioFinal());
				concesionBean.setLocalizacion(concesion.getLocalizado());
				concesionBean.setCodigo(concesion.getCodigo());
				concesionBean.setEstado(concesion.getEstado());

				// Titulares
				ArrayList<RelTitular> relTitularList = (ArrayList<RelTitular>) relTitularDAO
						.selectByConcesion((int) concesionBean
								.getId_concesion());

				RelTitular relTitular;
				ArrayList<PersonaBean> cotitulares = new ArrayList<PersonaBean>();

				for (int k = 0; k < relTitularList.size(); k++) {
					relTitular = relTitularList.get(k);

					Persona persona = personaDAO.selectByPrimaryKey(relTitular
							.getDniPersona());

					PersonaBean titular = new PersonaBean();
					titular.setApellido1(persona.getApellido1());
					titular.setApellido2(persona.getApellido2());
					titular.setDNI(persona.getDni());
					titular.setDomicilio(persona.getDomicilio());
					titular.setEstado_civil(persona.getEstadoCivil());
					titular.setFecha_nacimiento(persona.getFechaNacimiento());
					titular.setNombre(persona.getNombre());
					titular.setPoblacion(persona.getPoblacion());
					titular.setSexo(persona.getSexo());
					titular.setTelefono(persona.getTelefono());

					if (relTitular.getEsprincipal()) {
						concesionBean.setTitular(titular);
					} else {
						cotitulares.add(titular);
					}

				}

				concesionBean.setCotitulares(cotitulares);

				// 3.Recupero la tarifa asociada a la concesion
				Tarifa tarifa = tarifaDAO.selectByPrimaryKey(concesion.getIdTarifa());
				// tengo que hacer el mapeo de Tarifa a tarifabean
				TarifaBean tarifaBean = new TarifaBean();
				tarifaBean.setConcepto(tarifa.getConcepto());
				tarifaBean.setTipo_tarifa(tarifa.getTipoTarifa());
				tarifaBean.setPrecio(tarifa.getPrecio());
				tarifaBean.setTipo_calculo(tarifa.getTipoCalculo());
				tarifaBean.setId(tarifa.getId());

				concesionBean.setTarifa(tarifaBean);

				//alRet = concesionBean;
				alRet.put(concesionBean.getId_concesion(), concesionBean);
			}
		}
		return alRet.values();
	}
	
	/**
	 * getAllConcesiones
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection getAllConcesiones() throws Exception {

		List<Concesion> listaConcesiones;
		HashMap alRet = new HashMap();

		// 1.Obtengo todas las concesiones que existen en el sistema
		listaConcesiones = concesionDAO.selectAll();

		// 2.Esta lista es una lista de vo --> mapeo de cada uno de los
		// elementos al bean concesion
		ConcesionBean concesionBean = new ConcesionBean();
		Concesion concesionVO;

		for (int i = 0; i < listaConcesiones.size(); i++) {
			concesionVO = listaConcesiones.get(i);

			concesionBean.setEstado(concesionVO.getEstado());
			concesionBean.setFecha_fin(concesionVO.getFechaFin());
			concesionBean.setFecha_ini(concesionVO.getFechaIni());
			concesionBean.setFecha_ultRenovacion(concesionVO.getUltimaRenova());
			concesionBean.setId_concesion(concesionVO.getId());
			concesionBean.setTipo_concesion(concesionVO.getTipoConcesion());
			concesionBean.setBonificacion(concesionVO.getBonificacion());
			concesionBean.setPrecio_final(concesionVO.getPrecioFinal());
			concesionBean.setDescripcion(concesionVO.getDescripcion());
			concesionBean.setLocalizacion(concesionVO.getLocalizado());
			concesionBean.setCodigo(concesionVO.getCodigo());
			concesionBean.setEstado(concesionVO.getEstado());
			// Recupero los elementos asociados

			// Tarifa
			Tarifa tarifaVO = tarifaDAO.selectByPrimaryKey(concesionVO.getIdTarifa());
			TarifaBean tarifaBean = new TarifaBean();
			tarifaBean.setId(tarifaVO.getId());
			tarifaBean.setConcepto(tarifaVO.getConcepto());
			tarifaBean.setTipo_tarifa(tarifaVO.getTipoTarifa());
			tarifaBean.setPrecio(tarifaVO.getPrecio());
			tarifaBean.setTipo_calculo(tarifaVO.getTipoCalculo());
			concesionBean.setTarifa(tarifaBean);

			// Unidad de enterramiento
			UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(concesionVO.getIdUnidad());
			UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidadVO);
			concesionBean.setUnidad(unidadBean);

			// Completamos los campos de cementerio que es el bean generico
			elem_cementerio elem = elemCementerioDAO.selectByPrimaryKey(unidadVO.getIdElemcementerio());
			
			concesionBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
			concesionBean.setNombreCementerio(elem.getNombre());
			concesionBean.setEntidad(elem.getEntidad());
			concesionBean.setId(elem.getId());
			concesionBean.setTipo(elem.getTipo());

			concesionBean.setIdFeatures(unidadBean.getIdFeatures());
			concesionBean.setIdLayers(unidadBean.getIdLayers());

			// Titulares
			ArrayList<RelTitular> relTitularList = (ArrayList<RelTitular>) relTitularDAO
					.selectByConcesion((int) concesionBean.getId_concesion());

			RelTitular relTitular;
			ArrayList<PersonaBean> cotitulares = new ArrayList<PersonaBean>();

			for (int k = 0; k < relTitularList.size(); k++) {
				relTitular = relTitularList.get(k);

				Persona persona = personaDAO.selectByPrimaryKey(relTitular
						.getDniPersona());

				PersonaBean titular = new PersonaBean();
				titular.setApellido1(persona.getApellido1());
				titular.setApellido2(persona.getApellido2());
				titular.setDNI(persona.getDni());
				titular.setDomicilio(persona.getDomicilio());
				titular.setEstado_civil(persona.getEstadoCivil());
				titular.setFecha_nacimiento(persona.getFechaNacimiento());
				titular.setNombre(persona.getNombre());
				titular.setPoblacion(persona.getPoblacion());
				titular.setSexo(persona.getSexo());
				titular.setTelefono(persona.getTelefono());

				if (relTitular.getEsprincipal()) {
					concesionBean.setTitular(titular);
				} else {
					cotitulares.add(titular);
				}

			}

			concesionBean.setCotitulares(cotitulares);
			alRet.put(concesionBean.getId_concesion(), concesionBean);
		}
		return alRet.values();
	}

	/**
	 * getAllConcesiones
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection getAllConcesionesByCementerio(Integer idCementerio)
			throws Exception {

		HashMap alRet = new HashMap();
		try {

			List<Concesion> listaConcesiones;

			// 1.Obtengo todas las concesiones que existen en el sistema
			listaConcesiones = concesionDAO.selectAll();

			// 2.Esta lista es una lista de vo --> mapeo de cada uno de los
			Concesion concesionVO;

			for (int i = 0; i < listaConcesiones.size(); i++) {
				concesionVO = listaConcesiones.get(i);

				ConcesionBean concesionBean = new ConcesionBean();

				concesionBean.setEstado(concesionVO.getEstado());
				concesionBean.setFecha_fin(concesionVO.getFechaFin());
				concesionBean.setFecha_ini(concesionVO.getFechaIni());
				concesionBean.setFecha_ultRenovacion(concesionVO.getUltimaRenova());
				concesionBean.setId_concesion(concesionVO.getId());
				concesionBean.setTipo_concesion(concesionVO.getTipoConcesion());
				concesionBean.setBonificacion(concesionVO.getBonificacion());
				concesionBean.setPrecio_final(concesionVO.getPrecioFinal());
				concesionBean.setDescripcion(concesionVO.getDescripcion());
				concesionBean.setLocalizacion(concesionVO.getLocalizado());
				concesionBean.setCodigo(concesionVO.getCodigo());
				concesionBean.setEstado(concesionVO.getEstado());
				// Recupero los elementos asociados

				/**Tarifa**/
				Tarifa tarifaVO = tarifaDAO.selectByPrimaryKey(concesionVO.getIdTarifa());
				
				TarifaBean tarifaBean = new TarifaBean();
				tarifaBean.setId(tarifaVO.getId());
				tarifaBean.setConcepto(tarifaVO.getConcepto());
				tarifaBean.setTipo_tarifa(tarifaVO.getTipoTarifa());
				tarifaBean.setPrecio(tarifaVO.getPrecio());
				tarifaBean.setTipo_calculo(tarifaVO.getTipoCalculo());
				concesionBean.setTarifa(tarifaBean);

				/** Unidad de enterramiento**/
				UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(concesionVO.getIdUnidad());
				UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidadVO);
				concesionBean.setUnidad(unidadBean);

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
				elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
				if (elem != null) {
					// Recuperamos el nombre del cementerio para hacer el set en
					// la unidad
					Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);

					concesionBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
					concesionBean.setNombreCementerio(elem.getNombre());
					concesionBean.setEntidad(elem.getEntidad());
					concesionBean.setId(elem.getId());
					concesionBean.setTipo(elem.getTipo());

					concesionBean.setIdFeatures(unidadBean.getIdFeatures());
					concesionBean.setIdLayers(unidadBean.getIdLayers());

					/**Titulares**/
					ArrayList<RelTitular> relTitularList = (ArrayList<RelTitular>) 
							relTitularDAO.selectByConcesion((int) concesionBean.getId_concesion());

					RelTitular relTitular;
					ArrayList<PersonaBean> cotitulares = new ArrayList<PersonaBean>();

					for (int k = 0; k < relTitularList.size(); k++) {
						relTitular = relTitularList.get(k);

						Persona persona = personaDAO.selectByPrimaryKey(relTitular.getDniPersona());

						PersonaBean titular = new PersonaBean();
						titular.setApellido1(persona.getApellido1());
						titular.setApellido2(persona.getApellido2());
						titular.setDNI(persona.getDni());
						titular.setDomicilio(persona.getDomicilio());
						titular.setEstado_civil(persona.getEstadoCivil());
						titular.setFecha_nacimiento(persona.getFechaNacimiento());
						titular.setNombre(persona.getNombre());
						titular.setPoblacion(persona.getPoblacion());
						titular.setSexo(persona.getSexo());
						titular.setTelefono(persona.getTelefono());
						
						//Se completan los datos de elemcementerio para el titular
						titular.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
						titular.setNombre(elem.getNombre());
						titular.setEntidad(elem.getEntidad());
						titular.setId(elem.getId());
						titular.setTipo(elem.getTipo());

						titular.setIdFeatures(unidadBean.getIdFeatures());
						titular.setIdLayers(unidadBean.getIdLayers());

						if (relTitular.getEsprincipal()) {
							concesionBean.setTitular(titular);
						} else {
							cotitulares.add(titular);
						}

					}

					concesionBean.setCotitulares(cotitulares);
					alRet.put(concesionBean.getId_concesion(), concesionBean);
				}
			}

		} catch (Exception e) {
			logger.error("Error obteniendo la concesion:" + e);
		}
		return alRet.values();
	}

	public Collection getConcesionesByFilter(String superpatron,
			String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<Concesion> listaConcesiones;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		ConcesionExample concesionExample = new ConcesionExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.ConcesionExample.Criteria criteria = concesionExample.createCriteria();
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.ConcesionExample.Criteria) 
											addFilter.addConcesionFilter(criteria, campoFiltro, false);
			}
			concesionExample.or(criteria);
			listaConcesiones = concesionDAO.selectByExample(concesionExample);

			for (int j = 0; j < listaConcesiones.size(); j++) {
				Concesion concesion = (Concesion) listaConcesiones.get(j);
				ConcesionBean concesionBean = mappingManager.mapConcesionVoToBean(concesion);
				alRet.put(concesionBean.getId_concesion(), concesionBean);
			}
		}

		return alRet.values();
	}
	
	public Collection getConcesionesByTitular(String dni_persona,
			Sesion userSesion) throws Exception {

		List<Concesion> listaConcesiones;
		HashMap alRet = new HashMap();

		ArrayList<RelTitular> relTitularList = (ArrayList<RelTitular>) relTitularDAO.selectByTitular(dni_persona);
		
		for (int i = 0; i < relTitularList.size(); i++) {
			RelTitular elem = relTitularList.get(i);
			Concesion concesion = concesionDAO.selectByPrimaryKey(elem.getIdConcesion());
			if (concesion!= null){
				ConcesionBean concesionBean = mappingManager
				.mapConcesionVoToBean(concesion);
						alRet.put(concesionBean.getId_concesion(), concesionBean);
			}
		}
		return alRet.values();
	}
	
	
	public Collection findConcesiones(String superpatron,
			String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<Concesion> listaConcesiones = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		ConcesionExample concesionExample = new ConcesionExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			for (int i = 0; i < filtro.length; i++) {
				com.geopista.app.cementerios.business.vo.ConcesionExample.Criteria criteria = concesionExample.createCriteria();
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.ConcesionExample.Criteria) addFilter
						.addConcesionFilter(criteria, campoFiltro, true);
				concesionExample.or(criteria);
				listaConcesiones = concesionDAO.selectByExample(concesionExample);
				
				if ((listaConcesiones != null) && (listaConcesiones.size()>0)) break; 
				concesionExample.clear();
				
			}

			for (int j = 0; j < listaConcesiones.size(); j++) {
				Concesion concesion = (Concesion) listaConcesiones.get(j);
				ConcesionBean concesionBean = mappingManager
						.mapConcesionVoToBean(concesion);
				alRet.put(concesionBean.getId_concesion(), concesionBean);
			}
		}

		return alRet.values();
	}
	
	public boolean estaEnUso(Integer id_tarifa) throws SQLException{
		boolean estaenuso= false;
		
		List<Concesion> listaConcesiones = null;
		listaConcesiones = concesionDAO.selectAll();
		Concesion concesion;
		for (int i = 0; i < listaConcesiones.size(); i++) {
			
			concesion = listaConcesiones.get(i);
			if (concesion.getIdTarifa().intValue() == id_tarifa.intValue()){
				estaenuso= true;
				break;
			}
		}
		
		return estaenuso;
	}
}
