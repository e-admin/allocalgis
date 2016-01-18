/**
 * UnidadEnterramientoManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.CementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.CementerioFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DifuntoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.LocalizacionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PlazaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.UnidadEnterramientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.elem_cementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DifuntoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.LocalizacionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PlazaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.UnidadEnterramientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.elem_cementerioDAO;
import com.geopista.app.cementerios.business.vo.Cementerio;
import com.geopista.app.cementerios.business.vo.CementerioFeatureKey;
import com.geopista.app.cementerios.business.vo.Difunto;
import com.geopista.app.cementerios.business.vo.DifuntoExample;
import com.geopista.app.cementerios.business.vo.Localizacion;
import com.geopista.app.cementerios.business.vo.Plaza;
import com.geopista.app.cementerios.business.vo.PlazaExample;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample;
import com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample.Criteria;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.geopista.app.cementerios.business.vo.elem_cementerioExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.LocalizacionBean;
import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.document.DocumentoEnDisco;


public class UnidadEnterramientoManager extends DAO {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(UnidadEnterramientoManager.class);

	private static UnidadEnterramientoManager instance;

	private CementerioDAO cementerioDAO;
	private UnidadEnterramientoDAO unidadEnterramientoDAO;
	private elem_cementerioDAO elemCementerioDAO;
	private LocalizacionDAO localizacionDAO;
	private CementerioFeatureDAO cementerioFeatureDAO;
	private PlazaDAO plazaDAO;
	private DifuntoDAO difuntoDAO;
	private MappingManager mappingManager;

	public static UnidadEnterramientoManager getInstance() {
		if (instance == null) {
			instance = new UnidadEnterramientoManager();
		}
		return instance;
	}

	public UnidadEnterramientoManager() {

		unidadEnterramientoDAO = new UnidadEnterramientoDAOImpl();
		elemCementerioDAO = new elem_cementerioDAOImpl();
		localizacionDAO = new LocalizacionDAOImpl();
		cementerioFeatureDAO = new CementerioFeatureDAOImpl();
		plazaDAO = new PlazaDAOImpl();
		difuntoDAO = new DifuntoDAOImpl();
		cementerioDAO = new CementerioDAOImpl();
		mappingManager = MappingManager.getIntance();

	}

	/******************************************************* UNIDAD ENTERRAMIENTO ****************************************************************************/

	/**
	 * Insertar una unidad de enteramiento en la bbdd
	 */
	public UnidadEnterramientoBean insertUnidadEnterramiento(Object[] idLayers, Object[] idFeatures, UnidadEnterramientoBean elem,
			Integer idCementerio, Sesion userSesion) throws PermissionException, LockException, Exception {

		try {

			getSqlMapClientTemplate().startTransaction();
			
			// Pasos que hay que hacer
			int idUnidadEnterramiento = -1;

			CementerioManager cementerioManager = CementerioManager.getInstance();
			Cementerio cementerio = cementerioManager.getCementerio(idCementerio);

			if (cementerio == null)
				return null;

			// 1.Insert el cementerioElem generico -->
			elem_cementerio elemCementerio = new elem_cementerio();

			elemCementerio.setIdMunicipio(Integer.parseInt(elem.getIdMunicipio()));
			elemCementerio.setNombre(cementerio.getNombre());
			elemCementerio.setEntidad(elem.getEntidad());
			elemCementerio.setTipo(elem.getSuperPatron()); //TODO esto es nuevo antes elem.getTipo()
			elemCementerio.setSubtipo(elem.getPatron());
			elemCementerio.setIdCementerio(idCementerio);

			elemCementerioDAO.insert(elemCementerio);

			// 2. el insert de la localizacion correspondiente
			Localizacion localizacion = new Localizacion();
			
			localizacion.setDescripcion(elem.getLocalizacion().getDescripcion());
			localizacion.setValor(new Float(12));
			
			Integer res = localizacionDAO.insert(localizacion);

			// 3. el insertPropio de la unidad de enterramiento correspondiente
			UnidadEnterramiento unidad = new UnidadEnterramiento();

			unidad.setColumna(elem.getColumna());
			unidad.setEstado(elem.getEstado());
			unidad.setFila(elem.getFila());
			unidad.setFultConstruccion(elem.getFecha_construccion());
			unidad.setFultModificacion(elem.getFecha_UltimaMod());
			unidad.setFreforma(elem.getFecha_UltimaRef());
			unidad.setCodigo(elem.getCodigo());
			unidad.setDescripcion(elem.getDescripcion());
			unidad.setTipoUnidad(elem.getTipo_unidad());

			unidad.setNumplazas(elem.getNumMin_Plazas());

			int idElemCementerio = elemCementerioDAO.selectByLastSeqKey();
			elemCementerio.setId(idElemCementerio);
			unidad.setIdElemcementerio(idElemCementerio);

			int idLocalizacion = localizacionDAO.selectByLastSeqKey();
			localizacion.setId(idLocalizacion);
			unidad.setIdLocalizacion(idLocalizacion);

			// unidad.setNumplazas(elem.getPlazas().size());
			unidad.setNumplazas(elem.getNumMin_Plazas());
			
			res = unidadEnterramientoDAO.insert(unidad);

			idUnidadEnterramiento = unidadEnterramientoDAO.selectByLastSeqKey();
				elem.setIdUe(idUnidadEnterramiento);
				elem.setId(idElemCementerio);


			// 4. insert de las plazas de la unidad enterramiento
			ArrayList<PlazaBean> listaPlazas = elem.getPlazas();
			ArrayList<PlazaBean> nuevalista = new ArrayList<PlazaBean>();
			PlazaBean plazaElem;

			for (int i = 0; i < listaPlazas.size(); i++) {
				plazaElem = listaPlazas.get(i);
				Plaza plazaVO = new Plaza();
				// plazaVO.setDescripcion(plazaElem.getDescripcion());
				if (plazaElem.isEstado()) {
					plazaVO.setEstado(Constantes.TRUE);
				} else {
					plazaVO.setEstado(Constantes.FALSE);
				}

				plazaVO.setIdUnidadenterramiento(idUnidadEnterramiento);
				plazaVO.setModicado(plazaElem.getModificado());
				plazaVO.setSituacion(plazaElem.getSituacion());

				plazaDAO.insert(plazaVO);

				int idPlaza = plazaDAO.selectByLastSeqKey();
				plazaElem.setIdPlaza(idPlaza);
				plazaElem.setIdUnidadEnterramiento(idUnidadEnterramiento);
				nuevalista.add(plazaElem);
			}

			/*
			 * elimino la lista de plazas anterior y añado la nueva lista con
			 * los ids de las plazas insertadas en bbdd
			 */
			elem.getPlazas().clear();
			elem.setPlazas(nuevalista);

			// 4. El insert de la feature asociada a la unidad de enterramiento

			for (int j = 0; j < idFeatures.length; j++) {

				CementerioFeatureKey featureKey = new CementerioFeatureKey();

				featureKey.setIdElem(idUnidadEnterramiento);
				featureKey.setIdFeature(Integer.parseInt((String) idFeatures[j]));
				featureKey.setIdLayer((String) idLayers[j]);

				cementerioFeatureDAO.insert(featureKey);
			}
			elem.setIdFeatures(idFeatures);
			elem.setIdLayers(idLayers);

			/** Actualizamos los ficheros en disco (temporal --> destino) */
			DocumentoEnDisco.actualizarConFicherosDeTemporal(((UnidadEnterramientoBean) elem).getDocumentos());
			
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
	 * Insertar una unidad de enteramiento en la bbdd
	 */
	public UnidadEnterramientoBean updateUnidadEnterramiento(
			UnidadEnterramientoBean elem, Sesion userSesion)
			throws PermissionException, LockException, Exception {

		try {
			
			getSqlMapClientTemplate().startTransaction();

			elem_cementerio elemCementerio = new elem_cementerio();
			elemCementerio.setIdMunicipio(Integer.parseInt(elem
					.getIdMunicipio()));
			elemCementerio.setNombre(elem.getNombreCementerio());
			elemCementerio.setEntidad(elem.getEntidad());
			elemCementerio.setTipo(elem.getTipo());
			elemCementerio.setId((int) elem.getId());

			elemCementerioDAO.updateByPrimaryKeySelective(elemCementerio);

			Localizacion localizacion = new Localizacion();
			localizacion
					.setDescripcion(elem.getLocalizacion().getDescripcion());
			localizacion.setValor(new Float(12));// TODOModificar??
			localizacion.setId((int) elem.getLocalizacion().getId());

			localizacionDAO.updateByPrimaryKeySelective(localizacion);

			// UnidadVO
			UnidadEnterramiento unidad = new UnidadEnterramiento();

			unidad.setColumna(elem.getColumna());
			unidad.setEstado(elem.getEstado());
			unidad.setFila(elem.getFila());
			unidad.setFultConstruccion(elem.getFecha_construccion());
			unidad.setFultModificacion(elem.getFecha_UltimaMod());
			unidad.setFreforma(elem.getFecha_UltimaRef());
			unidad.setCodigo(elem.getCodigo());
			unidad.setDescripcion(elem.getDescripcion());
			unidad.setTipoUnidad(elem.getTipo_unidad());

			unidad.setNumplazas(elem.getNumMin_Plazas());

			unidad.setIdElemcementerio((int) elem.getId());
			unidad.setIdLocalizacion((int) elem.getLocalizacion().getId());
			unidad.setId((int) elem.getIdUe());

			// 1.Recupero las plazas asociadas a la unidad de enterramiento en
			// la bbdd
			PlazaExample plazaExample = new PlazaExample();
			plazaExample.createCriteria().andIdUnidadenterramientoEqualTo((int) elem.getIdUe());
			ArrayList<Plaza> plazasVOList = (ArrayList<Plaza>) plazaDAO.selectByExample(plazaExample);

			// creo una hash con la lista
			Hashtable plazasVOTable = new Hashtable();
			Plaza plazaVO;
			for (int i = 0; i < plazasVOList.size(); i++) {
				plazaVO = plazasVOList.get(i);
				if (!plazasVOTable.containsKey(plazaVO.getId())) {
					plazasVOTable.put(plazaVO.getId(), plazaVO);
				}
			}

			// 2.Lista de plazas
			ArrayList<PlazaBean> listaPlazas = elem.getPlazas();

			ArrayList<PlazaBean> nuevalista = new ArrayList<PlazaBean>();
			PlazaBean plazaElem;

			for (int i = 0; i < listaPlazas.size(); i++) {
				// 1.Pillo un elemento de la lista
				plazaElem = listaPlazas.get(i);

				// 2.Creo la plazaVO
				plazaVO = new Plaza();
				// plazaVO.setDescripcion(plazaElem.getDescripcion());
				if (plazaElem.isEstado()) {
					plazaVO.setEstado(Constantes.TRUE);
				} else {
					plazaVO.setEstado(Constantes.FALSE);
				}
				plazaVO.setIdUnidadenterramiento(plazaElem
						.getIdUnidadEnterramiento());
				plazaVO.setModicado(plazaElem.getModificado());
				plazaVO.setSituacion(plazaElem.getSituacion());
				plazaVO.setId(plazaElem.getIdPlaza());

				// 3. compruebo si está en la lista de bbdd y la elimino de la
				// lista
				// la lista resultado seran las plazas a eliminar de la bbdd

				if (plazasVOTable.containsKey(plazaVO.getId())) {
					// Plaza aux = (Plaza) plazasVOTable.get(plazaVO.getId());
					plazasVOTable.remove(plazaVO.getId());
				}
				// 4. Actualizo en bbdd
				int res = plazaDAO.updateByPrimaryKeySelective(plazaVO);

				// 5. Si no se actualiza ninguna fila es que es nueva y se
				// inserta
				if (res == 0) {
					plazaDAO.insert(plazaVO);
					int idPlaza = plazaDAO.selectByLastSeqKey();
					plazaElem.setIdPlaza(idPlaza);

				}
				Plaza record = new Plaza();
				record.setIdUnidadenterramiento(plazaVO.getIdUnidadenterramiento());
				record.setEstado(Constantes.TRUE);
				int numPlazas = plazaDAO.selectCountPlazasAsignadas(record);

				unidad.setNumplazas(numPlazas);
				unidadEnterramientoDAO.updateByPrimaryKeySelective(unidad);

				plazaElem.setIdPlaza(plazaElem.getIdPlaza());
				plazaElem.setIdUnidadEnterramiento(plazaElem
						.getIdUnidadEnterramiento());
				nuevalista.add(plazaElem);
			}

			/*
			 * elimino la lista de plazas anterior y añado la nueva lista con
			 * los ids de las plazas insertadas en bbdd
			 */
			plazasVOList.clear();
			Enumeration elements = plazasVOTable.elements();

			while (elements.hasMoreElements()) {
				plazaVO = (Plaza) elements.nextElement();
				int id = plazaVO.getId();
				plazaDAO.deleteByPrimaryKey(id);
			}

			elem.getPlazas().clear();
			elem.setPlazas(nuevalista);

			
			/** Actualizamos los ficheros en disco (temporal --> destino) */
			DocumentoEnDisco.actualizarConFicherosDeTemporal(((UnidadEnterramientoBean) elem).getDocumentos());

			
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
	 * deleteUnidadEnterramiento
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
	public UnidadEnterramientoBean deleteUnidadEnterramiento(Object[] idLayers,
			Object[] idFeatures, UnidadEnterramientoBean elem, Sesion userSesion)
			throws PermissionException, LockException, Exception {

		try {
			
			getSqlMapClientTemplate().startTransaction();

			unidadEnterramientoDAO.deleteByPrimaryKey((int) elem.getIdUe());

			localizacionDAO.deleteByPrimaryKey((int) elem.getLocalizacion().getId());

			if (idFeatures == null)
				idFeatures = elem.getIdFeatures();
			if (idLayers == null)
				idLayers = elem.getIdLayers();

			for (int j = 0; j < idFeatures.length; j++) {
				CementerioFeatureKey featureKey = new CementerioFeatureKey();
				featureKey.setIdElem((int) elem.getIdUe());
				featureKey.setIdFeature(Integer.parseInt((String) idFeatures[j]));
				featureKey.setIdLayer((String) idLayers[j]);
				
				cementerioFeatureDAO.deleteByPrimaryKey(featureKey);
				/*
				 * TODO solo mientras no se pueda seleccionar en el mapa una sola
				 * layer.. borrar de cementerio feature las features q se
				 * correspondan con el id de la unidad
				 */
				cementerioFeatureDAO.deleteByElem((int) elem.getIdUe());
	
				// Borrar elem_cementerio
				elemCementerioDAO.deleteByPrimaryKey((int) elem.getId());
			}
			
			getSqlMapClientTemplate().commitTransaction();
			
		}catch (Exception e) {
			logger.error("Error eliminando la unidad de enterramiento"+ e);
			
		}finally{
			
			getSqlMapClientTemplate().endTransaction();
		}
		
		return elem;
	}

	/**
	 * getUnidad
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
	public UnidadEnterramientoBean getUnidad(String superpatron,
			String patron, Object[] filtro, Object[] idLayers,
			Object[] idFeatures, Integer idCementerio, Sesion userSesion)
			throws Exception {

		UnidadEnterramientoBean alRet = null;

		List listaCementerioFeaturesKeys = new ArrayList<CementerioFeatureKey>();

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
				Localizacion localizacion = localizacionDAO
						.selectByPrimaryKey(unidad.getIdLocalizacion());

				elem_cementerioExample elemExample = new elem_cementerioExample();
				elemExample.createCriteria()
						.andIdCementerioEqualTo(idCementerio)
						.andIdEqualTo(unidad.getIdElemcementerio());

				List elem_cementerioList = elemCementerioDAO
						.selectByExample(elemExample);

				if (elem_cementerioList.size() != 1) {
					continue;
				}
				// Se retorna una lista pero solo debe contener un elemento
				elem_cementerio elem = (elem_cementerio) elem_cementerioList
						.get(0);
				if (elem != null) {

					// Recuperamos el nombre del cementerio para hacer el set en
					// la unidad
					Cementerio cementerio = cementerioDAO
							.selectByPrimaryKey(idCementerio);

					// se pasa del objeto que se obtiene de bbdd unidadVo al
					// bean para poder trabajar con el en la aplicacion
					UnidadEnterramientoBean unidadBean = new UnidadEnterramientoBean();
					unidadBean.setNombreCementerio(cementerio.getNombre());
					unidadBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
					unidadBean.setEntidad(elem.getEntidad());
					unidadBean.setId(elem.getId());

					// Parametros comunes
					unidadBean.setEntidad(elem.getEntidad());
					unidadBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
					unidadBean.setNombreCementerio(elem.getNombre());
					unidadBean.setSuperPatron(superpatron);
					unidadBean.setTipo(elem.getTipo());
					unidadBean.setId(elem.getId());

					unidadBean.setColumna(unidad.getColumna());
					unidadBean.setEntidad(elem.getEntidad());
					unidadBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
					unidadBean.setNombreCementerio(elem.getNombre());
					unidadBean.setSuperPatron(superpatron);
					unidadBean.setTipo_unidad(unidad.getTipoUnidad());
					unidadBean.setEstado(unidad.getEstado());
					unidadBean.setFecha_construccion(unidad.getFultConstruccion());
					unidadBean.setFecha_UltimaMod(unidad.getFultModificacion());
					unidadBean.setFecha_UltimaRef(unidad.getFreforma());
					unidadBean.setFila(unidad.getFila());
					unidadBean.setIdUe(unidad.getId());
					unidadBean.setCodigo(unidad.getCodigo());
					unidadBean.setDescripcion(unidad.getDescripcion());

					LocalizacionBean localizacionBean = new LocalizacionBean();
					localizacionBean.setDescripcion(localizacion.getDescripcion());
					localizacionBean.setValor(localizacion.getValor());
					localizacionBean.setId(localizacion.getId());
					unidadBean.setLocalizacion(localizacionBean);

					unidadBean.setNumMin_Plazas(unidad.getNumplazas());

					// Plazas
					PlazaExample example = new PlazaExample();
					example.createCriteria().andIdUnidadenterramientoEqualTo(
							unidad.getId());
					List<Plaza> listaPlazasVO;
					listaPlazasVO = plazaDAO.selectByExample(example);

					ArrayList<PlazaBean> liataPlazasBean = new ArrayList<PlazaBean>();
					Plaza plazaVO;
					for (int m = 0; m < listaPlazasVO.size(); m++) {
						PlazaBean plazaBean = new PlazaBean();
						plazaVO = listaPlazasVO.get(m);

						// plazaBean.setDescripcion(plazaVO.getDescripcion());

						if (plazaVO.getEstado() == Constantes.TRUE) {
							plazaBean.setEstado(true);
						} else {
							plazaBean.setEstado(false);
						}
						plazaBean.setIdPlaza(plazaVO.getId());
						plazaBean.setIdUnidadEnterramiento((int) unidadBean
								.getIdUe());
						plazaBean.setModificado(plazaVO.getModicado());

						DifuntoExample difExample = new DifuntoExample();
						difExample.createCriteria().andIdPlazaEqualTo(
								plazaVO.getId());

						List listDifunto = difuntoDAO
								.selectByExample(difExample);
						Difunto difunto = null;
						for (int l = 0; l < listDifunto.size(); l++) {
							difunto = (Difunto) listDifunto.get(l);
						}
						DifuntoBean difuntoBean = mappingManager
								.mapDifuntoVOToBean(difunto);
						plazaBean.setDifunto(difuntoBean);

						plazaBean.setSituacion(plazaVO.getSituacion());

						liataPlazasBean.add(plazaBean);
					}
					unidadBean.setPlazas(liataPlazasBean);

					// getCementerio_feature where id unidad
					listaCementerioFeaturesKeys = cementerioFeatureDAO
							.selectByElem((int) unidadBean.getIdUe());
					List listafeatures = new ArrayList();
					List listaLayers = new ArrayList();
					for (int k = 0; k < listaCementerioFeaturesKeys.size(); k++) {
						CementerioFeatureKey keyElem = (CementerioFeatureKey) listaCementerioFeaturesKeys
								.get(k);
						listafeatures.add(keyElem.getIdFeature());
						listaLayers.add(keyElem.getIdLayer());
					}

					unidadBean.setIdFeatures(listafeatures);
					unidadBean.setIdLayers(listaLayers);

					alRet = unidadBean;
				}
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
	public Collection getUnidadesByFeature(String superpatron, String patron, Object[] filtro, Object[] idLayers,
				Object[] idFeatures, Integer idCementerio, Sesion userSesion) throws Exception {

		
		HashMap alRet = new HashMap();

		List listaCementerioFeaturesKeys = new ArrayList<CementerioFeatureKey>();

		for (int j = 0; j < idFeatures.length; j++) {

			List lista = new ArrayList<CementerioFeatureKey>();
			CementerioFeatureKey key = new CementerioFeatureKey();
			key.setIdFeature(Integer.parseInt((String) idFeatures[j]));
			key.setIdLayer("unidad_enterramiento");

			lista = cementerioFeatureDAO.selectByFeatureAndLayerUnidad(key);

			for (int i = 0; i < lista.size(); i++) {
				key = (CementerioFeatureKey) lista.get(i);
				UnidadEnterramiento unidad = unidadEnterramientoDAO .selectByPrimaryKey(key.getIdElem());
				Localizacion localizacion = localizacionDAO .selectByPrimaryKey(unidad.getIdLocalizacion());

				elem_cementerioExample elemExample = new elem_cementerioExample();
				elemExample.createCriteria()
						.andIdCementerioEqualTo(idCementerio)
						.andIdEqualTo(unidad.getIdElemcementerio());

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

					// se pasa del objeto que se obtiene de bbdd unidadVo al
					// bean para poder trabajar con el en la aplicacion
					UnidadEnterramientoBean unidadBean = new UnidadEnterramientoBean();
					unidadBean.setNombreCementerio(cementerio.getNombre());
					unidadBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
					unidadBean.setEntidad(elem.getEntidad());
					unidadBean.setId(elem.getId());

					// Parametros comunes
					unidadBean.setEntidad(elem.getEntidad());
					unidadBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
					unidadBean.setNombreCementerio(elem.getNombre());
					unidadBean.setSuperPatron(superpatron);
					unidadBean.setTipo(elem.getTipo());
					unidadBean.setId(elem.getId());

					unidadBean.setColumna(unidad.getColumna());
					unidadBean.setTipo_unidad(unidad.getTipoUnidad());
					unidadBean.setEstado(unidad.getEstado());
					unidadBean.setFecha_construccion(unidad.getFultConstruccion());
					unidadBean.setFecha_UltimaMod(unidad.getFultModificacion());
					unidadBean.setFecha_UltimaRef(unidad.getFreforma());
					unidadBean.setFila(unidad.getFila());
					unidadBean.setIdUe(unidad.getId());
					unidadBean.setCodigo(unidad.getCodigo());
					unidadBean.setDescripcion(unidad.getDescripcion());

					LocalizacionBean localizacionBean = new LocalizacionBean();
					localizacionBean.setDescripcion(localizacion.getDescripcion());
					localizacionBean.setValor(localizacion.getValor());
					localizacionBean.setId(localizacion.getId());
					unidadBean.setLocalizacion(localizacionBean);

					unidadBean.setNumMin_Plazas(unidad.getNumplazas());

					// Plazas
					PlazaExample example = new PlazaExample();
					example.createCriteria().andIdUnidadenterramientoEqualTo(unidad.getId());
					List<Plaza> listaPlazasVO;
					listaPlazasVO = plazaDAO.selectByExample(example);

					ArrayList<PlazaBean> liataPlazasBean = new ArrayList<PlazaBean>();
					Plaza plazaVO;
					for (int m = 0; m < listaPlazasVO.size(); m++) {
						PlazaBean plazaBean = new PlazaBean();
						plazaVO = listaPlazasVO.get(m);

						// plazaBean.setDescripcion(plazaVO.getDescripcion());

						if (plazaVO.getEstado() == Constantes.TRUE) {
							plazaBean.setEstado(true);
						} else {
							plazaBean.setEstado(false);
						}
						plazaBean.setIdPlaza(plazaVO.getId());
						plazaBean.setIdUnidadEnterramiento((int) unidadBean.getIdUe());
						plazaBean.setModificado(plazaVO.getModicado());

						DifuntoExample difExample = new DifuntoExample();
						difExample.createCriteria().andIdPlazaEqualTo(plazaVO.getId());

						List listDifunto = difuntoDAO.selectByExample(difExample);
						Difunto difunto = null;
						for (int l = 0; l < listDifunto.size(); l++) {
							difunto = (Difunto) listDifunto.get(l);
						}
						DifuntoBean difuntoBean = mappingManager.mapDifuntoVOToBean(difunto);
						plazaBean.setDifunto(difuntoBean);
						plazaBean.setSituacion(plazaVO.getSituacion());
						liataPlazasBean.add(plazaBean);
					}
					unidadBean.setPlazas(liataPlazasBean);

					// getCementerio_feature where id unidad
					listaCementerioFeaturesKeys = cementerioFeatureDAO.selectByElem((int) unidadBean.getIdUe());
					List listafeatures = new ArrayList();
					List listaLayers = new ArrayList();
					for (int k = 0; k < listaCementerioFeaturesKeys.size(); k++) {
						CementerioFeatureKey keyElem = (CementerioFeatureKey) listaCementerioFeaturesKeys.get(k);
						listafeatures.add(keyElem.getIdFeature());
						listaLayers.add(keyElem.getIdLayer());
					}

					unidadBean.setIdFeatures(listafeatures);
					unidadBean.setIdLayers(listaLayers);

					
					alRet.put(unidadBean.getIdUe(), unidadBean);
				}
			}
		}
		return alRet.values();
	}
	
	/**
	 * getAllUnidadesEnterramiento()
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection getAllUnidadesEnterramiento() throws Exception {

		List<UnidadEnterramiento> listaUnidades;
		HashMap alRet = new HashMap();

		listaUnidades = unidadEnterramientoDAO.selectAll();

		UnidadEnterramientoBean unidad;
		List listaCementerioFeaturesKeys = new ArrayList<CementerioFeatureKey>();

		for (int i = 0; i < listaUnidades.size(); i++) {
			unidad = new UnidadEnterramientoBean();
			UnidadEnterramiento unidadVO = listaUnidades.get(i);

			unidad.setCodigo(unidadVO.getCodigo());
			unidad.setColumna(unidadVO.getColumna());
			unidad.setFila(unidadVO.getFila());
			unidad.setDescripcion(unidadVO.getDescripcion());
			unidad.setEstado(unidadVO.getEstado());
			unidad.setFecha_construccion(unidadVO.getFultConstruccion());
			unidad.setFecha_UltimaMod(unidadVO.getFultModificacion());
			unidad.setFecha_UltimaRef(unidadVO.getFreforma());
			unidad.setIdUe(unidadVO.getId());
			unidad.setTipo_unidad(unidadVO.getTipoUnidad());
			unidad.setNumMin_Plazas(unidadVO.getNumplazas());

			Localizacion localizacionVo = localizacionDAO
					.selectByPrimaryKey(unidadVO.getIdLocalizacion());
			LocalizacionBean localizacion = new LocalizacionBean();
			localizacion.setId(unidadVO.getIdLocalizacion());
			localizacion.setDescripcion(localizacionVo.getDescripcion());
			localizacion.setValor(localizacionVo.getValor());

			unidad.setLocalizacion(localizacion);

			elem_cementerio elem = elemCementerioDAO
					.selectByPrimaryKey(unidadVO.getIdElemcementerio());

			// Completamos los campos de cementerio que es el bean generico
			unidad.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
			unidad.setNombreCementerio(elem.getNombre());
			unidad.setEntidad(elem.getEntidad());
			unidad.setId(elem.getId());
			unidad.setTipo(elem.getTipo());

			// Plazas
			PlazaExample example = new PlazaExample();
			example.createCriteria().andIdUnidadenterramientoEqualTo(
					unidadVO.getId());
			List<Plaza> listaPlazasVO;
			listaPlazasVO = plazaDAO.selectByExample(example);

			ArrayList<PlazaBean> liataPlazasBean = new ArrayList<PlazaBean>();
			Plaza plazaVO;
			for (int j = 0; j < listaPlazasVO.size(); j++) {
				PlazaBean plazaBean = new PlazaBean();
				plazaVO = listaPlazasVO.get(j);

				// plazaBean.setDescripcion(plazaVO.getDescripcion());

				if (plazaVO.getEstado() == Constantes.TRUE) {
					plazaBean.setEstado(true);
				} else {
					plazaBean.setEstado(false);
				}
				plazaBean.setIdPlaza(plazaVO.getId());
				plazaBean.setIdUnidadEnterramiento((int) unidad.getIdUe());
				plazaBean.setModificado(plazaVO.getModicado());

				DifuntoExample difExample = new DifuntoExample();
				difExample.createCriteria().andIdPlazaEqualTo(plazaVO.getId());

				List listDifunto = difuntoDAO.selectByExample(difExample);
				Difunto difunto = null;
				for (int m = 0; m < listDifunto.size(); m++) {
					difunto = (Difunto) listDifunto.get(m);
				}
				DifuntoBean difuntoBean = mappingManager
						.mapDifuntoVOToBean(difunto);
				plazaBean.setDifunto(difuntoBean);

				plazaBean.setSituacion(plazaVO.getSituacion());

				liataPlazasBean.add(plazaBean);
			}
			unidad.setPlazas(liataPlazasBean);

			// getCementerio_feature where id unidad
			listaCementerioFeaturesKeys = cementerioFeatureDAO
					.selectByElem(unidadVO.getId());
			List listafeatures = new ArrayList();
			List listaLayers = new ArrayList();
			for (int k = 0; k < listaCementerioFeaturesKeys.size(); k++) {
				CementerioFeatureKey keyElem = (CementerioFeatureKey) listaCementerioFeaturesKeys
						.get(k);
				listafeatures.add(keyElem.getIdFeature());
				listaLayers.add(keyElem.getIdLayer());
			}

			unidad.setIdFeatures(listafeatures);
			unidad.setIdLayers(listaLayers);

			alRet.put(unidad.getIdUe(), unidad);
		}

		return alRet.values();
	}

	/**
	 * 
	 * @param idCementerio
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAllUnidadesEnterramientoByCementerio(
			Integer idCementerio) throws Exception {

		List<UnidadEnterramiento> listaUnidades;
		HashMap alRet = new HashMap();

		listaUnidades = unidadEnterramientoDAO.selectAll();

		UnidadEnterramientoBean unidad;
		List listaCementerioFeaturesKeys = new ArrayList<CementerioFeatureKey>();

		for (int i = 0; i < listaUnidades.size(); i++) {
			unidad = new UnidadEnterramientoBean();
			UnidadEnterramiento unidadVO = listaUnidades.get(i);

			unidad.setCodigo(unidadVO.getCodigo());
			unidad.setColumna(unidadVO.getColumna());
			unidad.setFila(unidadVO.getFila());
			unidad.setDescripcion(unidadVO.getDescripcion());
			unidad.setEstado(unidadVO.getEstado());
			unidad.setFecha_construccion(unidadVO.getFultConstruccion());
			unidad.setFecha_UltimaMod(unidadVO.getFultModificacion());
			unidad.setFecha_UltimaRef(unidadVO.getFreforma());
			unidad.setIdUe(unidadVO.getId());
			unidad.setTipo_unidad(unidadVO.getTipoUnidad());
			unidad.setNumMin_Plazas(unidadVO.getNumplazas());

			Localizacion localizacionVo = localizacionDAO
					.selectByPrimaryKey(unidadVO.getIdLocalizacion());
			LocalizacionBean localizacion = new LocalizacionBean();
			localizacion.setId(unidadVO.getIdLocalizacion());
			localizacion.setDescripcion(localizacionVo.getDescripcion());
			localizacion.setValor(localizacionVo.getValor());

			unidad.setLocalizacion(localizacion);

			// elem_cementerio elem =
			// elemCementerioDAO.selectByPrimaryKey(unidadVO.getIdElemcementerio());

			elem_cementerioExample elemExample = new elem_cementerioExample();
			elemExample.createCriteria().andIdCementerioEqualTo(idCementerio)
					.andIdEqualTo(unidadVO.getIdElemcementerio());

			List elem_cementerioList = elemCementerioDAO.selectByExample(elemExample);

			if (elem_cementerioList.size() != 1) {
				continue;
			}
			// Se retorna una lista pero solo debe contener un elemento
			elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
			if (elem != null) {

				// Recuperamos el nombre del cementerio para hacer el set en la
				// unidad
				Cementerio cementerio = cementerioDAO
						.selectByPrimaryKey(idCementerio);

				// Completamos los campos de cementerio que es el bean generico
				unidad.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
				unidad.setNombreCementerio(cementerio.getNombre());
				unidad.setEntidad(elem.getEntidad());
				unidad.setId(elem.getId());
				unidad.setTipo(elem.getTipo());

				// Plazas
				PlazaExample example = new PlazaExample();
				example.createCriteria().andIdUnidadenterramientoEqualTo(
						unidadVO.getId());
				List<Plaza> listaPlazasVO;
				listaPlazasVO = plazaDAO.selectByExample(example);

				ArrayList<PlazaBean> liataPlazasBean = new ArrayList<PlazaBean>();
				Plaza plazaVO;
				for (int j = 0; j < listaPlazasVO.size(); j++) {
					PlazaBean plazaBean = new PlazaBean();
					plazaVO = listaPlazasVO.get(j);

					// plazaBean.setDescripcion(plazaVO.getDescripcion());

					if (plazaVO.getEstado() == Constantes.TRUE) {
						plazaBean.setEstado(true);
					} else {
						plazaBean.setEstado(false);
					}
					plazaBean.setIdPlaza(plazaVO.getId());
					plazaBean.setIdUnidadEnterramiento((int) unidad.getIdUe());
					plazaBean.setModificado(plazaVO.getModicado());

					DifuntoExample difExample = new DifuntoExample();
					difExample.createCriteria().andIdPlazaEqualTo(
							plazaVO.getId());

					List listDifunto = difuntoDAO.selectByExample(difExample);
					Difunto difunto = null;
					for (int m = 0; m < listDifunto.size(); m++) {
						difunto = (Difunto) listDifunto.get(m);
					}
					DifuntoBean difuntoBean = mappingManager
							.mapDifuntoVOToBean(difunto);
					plazaBean.setDifunto(difuntoBean);

					plazaBean.setSituacion(plazaVO.getSituacion());

					liataPlazasBean.add(plazaBean);
				}
				unidad.setPlazas(liataPlazasBean);

				// getCementerio_feature where id unidad
				listaCementerioFeaturesKeys = cementerioFeatureDAO
						.selectByElem(unidadVO.getId());
				List listafeatures = new ArrayList();
				List listaLayers = new ArrayList();
				for (int k = 0; k < listaCementerioFeaturesKeys.size(); k++) {
					CementerioFeatureKey keyElem = (CementerioFeatureKey) listaCementerioFeaturesKeys
							.get(k);
					listafeatures.add(keyElem.getIdFeature());
					listaLayers.add(keyElem.getIdLayer());
				}

				unidad.setIdFeatures(listafeatures);
				unidad.setIdLayers(listaLayers);

				alRet.put(unidad.getIdUe(), unidad);
			}
		}// For

		return alRet.values();
	}

	public Collection getUnidadesByFilter(String superpatron, String patron,
			Object[] filtro, Integer idCementerio, Sesion userSesion)
			throws Exception {

		List<UnidadEnterramiento> listaUnidades;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		UnidadEnterramientoExample unidadExample = new UnidadEnterramientoExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			Criteria criteria = unidadExample.createCriteria();
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = addFilter.addUnidadEnterramientoFilter(criteria,campoFiltro, false);
			}			
			unidadExample.or(criteria);
			listaUnidades = unidadEnterramientoDAO.selectByExample(unidadExample);

			for (int j = 0; j < listaUnidades.size(); j++) {
				UnidadEnterramiento unidad = (UnidadEnterramiento) listaUnidades
						.get(j);
				UnidadEnterramientoBean unidadBean = mappingManager
						.mapUnidadEnterramientoVOToBean(unidad);

				alRet.put(unidadBean.getIdUe(), unidadBean);
			}
		}
		return alRet.values();
	}
	
	public Collection findUnidades(String superpatron,String patron, Object[] filtro, Integer idCementerio, Sesion userSesion) throws Exception {

		List<UnidadEnterramiento> listaUnidades= null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		UnidadEnterramientoExample unidadExample = new UnidadEnterramientoExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			for (int i = 0; i < filtro.length; i++) {
				Criteria criteria = unidadExample.createCriteriaInternal();
				campoFiltro = (CampoFiltro) filtro[i];
				
				criteria = addFilter.addUnidadEnterramientoFilter(criteria,campoFiltro, true);
				
				if (criteria == null) continue;
				
				unidadExample.or(criteria);
				listaUnidades = unidadEnterramientoDAO.selectByExample(unidadExample);

				
				if ((listaUnidades != null) && (listaUnidades.size()>0)) break; 
				unidadExample.clear();
				
			}

			if (listaUnidades!=null){
				for (int j = 0; j < listaUnidades.size(); j++) {
					UnidadEnterramiento unidad = (UnidadEnterramiento) listaUnidades.get(j);
					UnidadEnterramientoBean unidadBean = mappingManager.mapUnidadEnterramientoVOToBean(unidad);
					alRet.put(unidadBean.getIdUe(), unidadBean);
				}
			}
		}

		return alRet.values();
	}
	
	public boolean todasPlazasLibres (UnidadEnterramientoBean unidad){
		boolean todasLibres = true;
		
		ArrayList<PlazaBean> listaPlazas;
		listaPlazas = unidad.getPlazas();
		PlazaBean elem;
		for (int i = 0; i < listaPlazas.size(); i++) {
			elem = (PlazaBean) listaPlazas.get(i);
			if (elem.isEstado()){ //plaza asignada
					todasLibres = false;
					break;
			}
		}
		return todasLibres;
	}
}
