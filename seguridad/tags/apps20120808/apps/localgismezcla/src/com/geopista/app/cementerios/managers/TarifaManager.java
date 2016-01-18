package com.geopista.app.cementerios.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.BloqueDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.CementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.TarifaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.elem_cementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.BloqueDAO;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.TarifaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.elem_cementerioDAO;
import com.geopista.app.cementerios.business.vo.Bloque;
import com.geopista.app.cementerios.business.vo.Cementerio;
import com.geopista.app.cementerios.business.vo.Inhumacion;
import com.geopista.app.cementerios.business.vo.InhumacionExample;
import com.geopista.app.cementerios.business.vo.Tarifa;
import com.geopista.app.cementerios.business.vo.TarifaExample;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.geopista.app.cementerios.business.vo.elem_cementerioExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.cementerios.TarifaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;

public class TarifaManager extends DAO {

	
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TarifaManager.class);
	
	private static TarifaManager instance;

	private TarifaDAO tarifaDAO;
	private CementerioDAO cementerioDAO;
	private elem_cementerioDAO elemCementerioDAO;
    private AddFilter addFilter;
    private MappingManager mappingManager;
    
    public TarifaManager(){
        tarifaDAO = new TarifaDAOImpl();
        cementerioDAO = new CementerioDAOImpl();
        elemCementerioDAO = new elem_cementerioDAOImpl();
        
        addFilter = AddFilter.getInstance();
        mappingManager = MappingManager.getIntance();
    }
    
    public static TarifaManager getInstance(){
    	if (instance == null){
    		instance = new TarifaManager();
    	}
    	return instance;
    }
	
	
    /**************************************************************** TARIFA ****************************************************************************/
    
    /**
     * getAllBloquesByCementerio
     */
   public Collection getAllTarifasByCementerio(Integer idCementerio) throws Exception {
    	
    	List<Tarifa> listaTarifas;
    	HashMap alRet= new HashMap();

    	listaTarifas = tarifaDAO.selectAllByCementery(idCementerio);
    	
    	Tarifa tarifa;

    	for (int i = 0; i < listaTarifas.size(); i++) {
    		tarifa = (Tarifa) listaTarifas.get(i);
			elem_cementerioExample elemExample = new elem_cementerioExample();
			elemExample.createCriteria().andIdCementerioEqualTo(idCementerio);
			List elem_cementerioList = elemCementerioDAO.selectByExample(elemExample);

			if (elem_cementerioList.size() < 1) {
				continue;
			}
			// Se retorna una lista pero solo debe contener un elemento
			elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
			if (elem != null) {

				// Recuperamos el nombre del cementerio para hacer el set en la
				// unidad
				Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
    		
	    		TarifaBean tarifaBean = new TarifaBean();
	    		tarifaBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
	    		tarifaBean.setNombreCementerio(cementerio.getNombre());
	    		tarifaBean.setEntidad(elem.getEntidad());
	    		tarifaBean.setId(elem.getId());
	    		tarifaBean.setTipo(elem.getTipo());
	    		tarifaBean.setConcepto(tarifa.getConcepto());
	    		tarifaBean.setTipo_tarifa(tarifa.getTipoTarifa());
	    		tarifaBean.setId_tarifa(tarifa.getId());
	    		tarifaBean.setPrecio(tarifa.getPrecio());
	    		tarifaBean.setTipo_calculo(tarifa.getTipoCalculo());
	    		tarifaBean.setTipo_unidad(tarifa.getCategoria());
	    		
    	    alRet.put(tarifaBean.getId_tarifa(), tarifaBean);
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
   public Collection getAllTarifasByCementerioAndType(Integer idCementerio, Integer tipoTarifa) throws Exception {
   	
   	List<Tarifa> listaTarifas;
   	HashMap alRet= new HashMap();

   	listaTarifas = tarifaDAO.selectByCementerioAndType(idCementerio, tipoTarifa);
   	
   	Tarifa tarifa;

   	for (int i = 0; i < listaTarifas.size(); i++) {
   		tarifa = (Tarifa) listaTarifas.get(i);
			elem_cementerioExample elemExample = new elem_cementerioExample();
			elemExample.createCriteria().andIdCementerioEqualTo(idCementerio);
			List elem_cementerioList = elemCementerioDAO.selectByExample(elemExample);

			if (elem_cementerioList.size() < 1) {
				continue;
			}
			// Se retorna una lista pero solo debe contener un elemento
			elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
			if (elem != null) {

				// Recuperamos el nombre del cementerio para hacer el set en la
				// unidad
				Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
   		
	    		TarifaBean tarifaBean = new TarifaBean();
	    		tarifaBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
	    		tarifaBean.setNombreCementerio(cementerio.getNombre());
	    		tarifaBean.setEntidad(elem.getEntidad());
	    		tarifaBean.setId(elem.getId());
	    		tarifaBean.setTipo(elem.getTipo());
	    		tarifaBean.setConcepto(tarifa.getConcepto());
	    		tarifaBean.setTipo_tarifa(tarifa.getTipoTarifa());
	    		tarifaBean.setId_tarifa(tarifa.getId());
	    		tarifaBean.setPrecio(tarifa.getPrecio());
	    		tarifaBean.setTipo_calculo(tarifa.getTipoCalculo());
	    		tarifaBean.setTipo_unidad(tarifa.getCategoria());
	    		
   	    alRet.put(tarifaBean.getId_tarifa(), tarifaBean);
			}
   	}
   	return alRet.values();
  }
   
	/**
	 * Insertar una tarifa en la bbdd
	 */
	public TarifaBean insertTarifa(Object[] idLayers, Object[] idFeatures, TarifaBean elem, Integer idCementerio, Sesion userSesion)
			throws PermissionException, LockException, Exception {

		try {

			getSqlMapClientTemplate().startTransaction();
			
			// Pasos que hay que hacer
			int idTarifa = -1;
			
			Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
			if (cementerio == null) return null;
			
			Tarifa tarifaVo = new Tarifa();
			
			tarifaVo.setCategoria(elem.getTipo_unidad());
			tarifaVo.setConcepto(elem.getConcepto());
			tarifaVo.setTipoTarifa(elem.getTipo_tarifa());
			tarifaVo.setIdCementerio(cementerio.getId());
			tarifaVo.setPrecio(elem.getPrecio());
			tarifaVo.setTipoCalculo(elem.getTipo_calculo());
			
			tarifaDAO.insert(tarifaVo);
			
			idTarifa = tarifaDAO.selectByLastSeqKey();
			
			elem.setId_tarifa(idTarifa);
			elem.setIdCementerio(idCementerio);
			
			getSqlMapClientTemplate().commitTransaction();
			
		}catch (Exception e) {
			logger.error("Error al insertar la tarifa" + e);
		}finally{
			
			getSqlMapClientTemplate().endTransaction();
		}
			
		return elem;
	}
		
 
	/**
	 * actualizar una tarifa en la bbdd
	 */
	public TarifaBean updateTarifa(TarifaBean elem, Sesion userSesion)
			throws PermissionException, LockException, Exception {

		try {

			getSqlMapClientTemplate().startTransaction();
			
			Tarifa tarifaVo = new Tarifa();
			
			tarifaVo.setCategoria(elem.getTipo_unidad());
			tarifaVo.setConcepto(elem.getConcepto());
			tarifaVo.setTipoTarifa(elem.getTipo_tarifa());
			tarifaVo.setPrecio(elem.getPrecio());
			tarifaVo.setTipoCalculo(elem.getTipo_calculo());
			tarifaVo.setId(elem.getId_tarifa());
			
			tarifaDAO.updateByPrimaryKeySelective(tarifaVo);
						
			getSqlMapClientTemplate().commitTransaction();
			
		}catch (Exception e) {
			logger.error("Error al actualizar la tarifa" + e);
		}finally{
			
			getSqlMapClientTemplate().endTransaction();
		}
			
		return elem;
	}

	/**
	 * Borrar una tarifa en la bbdd
	 */
	public TarifaBean deleteTarifa(Object[] idLayers, Object[] idFeatures, TarifaBean elem, Sesion userSesion)
			throws PermissionException, LockException, Exception {

		try {

			getSqlMapClientTemplate().startTransaction();
			
			Tarifa tarifaVo = new Tarifa();
			
			tarifaVo.setCategoria(elem.getTipo_unidad());
			tarifaVo.setConcepto(elem.getConcepto());
			tarifaVo.setTipoTarifa(elem.getTipo_tarifa());
			tarifaVo.setIdCementerio(elem.getIdCementerio());
			tarifaVo.setPrecio(elem.getPrecio());
			tarifaVo.setTipoCalculo(elem.getTipo_calculo());
			
			tarifaDAO.deleteByPrimaryKey(elem.getId_tarifa());
			
			getSqlMapClientTemplate().commitTransaction();
			
		}catch (Exception e) {
			logger.error("Error al borrar la tarifa" + e);
		}finally{
			
			getSqlMapClientTemplate().endTransaction();
		}
			
		return elem;
	}

	/**
	 * Borrar una tarifa en la bbdd
	 */
	public Collection getAllTarifasByCementerio(Object[] idLayers, Object[] idFeatures, TarifaBean elem, Integer idCementerio, Sesion userSesion)
			throws PermissionException, LockException, Exception {

		List<Tarifa>  listaTarifas;
		HashMap alRet = new HashMap();
		
		try {
			Tarifa tarifaVo;
			TarifaBean tarifaBean;
			listaTarifas = tarifaDAO.selectAllByCementery(idCementerio);
			
			for (int i = 0; i < listaTarifas.size(); i++) {
				tarifaVo = (Tarifa)listaTarifas.get(i);
				tarifaBean = mappingManager.mapTarifaVoToBean(tarifaVo);
				
				alRet.put(tarifaBean.getId_tarifa(), tarifaBean);
			}
			
		}catch (Exception e) {
			logger.error("Error al borrar la tarifa" + e);
		}
			
		return alRet.values();
	}

	/**
	 * getTarifasDifuntoByFilter
	 * @param superpatron
	 * @param patron
	 * @param filtro
	 * @param idCementerio
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	public Collection getTarifasDifuntoByFilter(String superpatron,String patron, Object[] filtro, Integer idCementerio, Integer tipoTarifa,
			Sesion userSesion) throws Exception {

		List<Tarifa> listaTarifas;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		TarifaExample tarifaExample = new TarifaExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.TarifaExample.Criteria criteria = tarifaExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.TarifaExample.Criteria) 
				addFilter.addTarifaFilter(criteria, campoFiltro, false);
			}
			if (tipoTarifa == Constantes.TARIFA_GDIFUNTOS){
				criteria.andTipoTarifaEqualTo(tipoTarifa);
			}else{
				tarifaExample = new TarifaExample();
				criteria = tarifaExample.createCriteria();
				criteria.andIdCementerioEqualTo(idCementerio);
			}
		
			tarifaExample.or(criteria);
			listaTarifas = tarifaDAO.selectByExample(tarifaExample); 

			for (int j = 0; j < listaTarifas.size(); j++) {
				Tarifa tarifa = (Tarifa) listaTarifas.get(j);
				TarifaBean tarifaBean = mappingManager.mapTarifaVoToBean(tarifa);
				alRet.put(tarifaBean.getId_tarifa(), tarifaBean);
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
	 * @param tipoTarifa
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	public Collection getTarifasPropiedadByFilter(String superpatron,String patron, Object[] filtro, Integer idCementerio, Integer tipoTarifa,
			Sesion userSesion) throws Exception {

		List<Tarifa> listaTarifas;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		TarifaExample tarifaExample = new TarifaExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.TarifaExample.Criteria criteria = tarifaExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.TarifaExample.Criteria) 
				addFilter.addTarifaFilter(criteria, campoFiltro, false);
			}
			if (tipoTarifa == Constantes.TARIFA_GPROPIEDAD){
				criteria.andTipoTarifaEqualTo(tipoTarifa);
			}else{
				tarifaExample.clear();
				criteria = tarifaExample.createCriteria();
				criteria.andIdCementerioEqualTo(idCementerio);
			}
				
			tarifaExample.or(criteria);
			listaTarifas = tarifaDAO.selectByExample(tarifaExample); 

			for (int j = 0; j < listaTarifas.size(); j++) {
				Tarifa tarifa = (Tarifa) listaTarifas.get(j);
				TarifaBean tarifaBean = mappingManager.mapTarifaVoToBean(tarifa);
				alRet.put(tarifaBean.getId_tarifa(), tarifaBean);
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
	 * @param tipoTarifa
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	public Collection findTarifasPropiedad(String superpatron,String patron, Object[] filtro, Integer idCementerio, Integer tipoTarifa,
			Sesion userSesion) throws Exception {

		List<Tarifa> listaTarifas = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		TarifaExample tarifaExample = new TarifaExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.TarifaExample.Criteria criteria = tarifaExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.TarifaExample.Criteria) 
				addFilter.addTarifaFilter(criteria, campoFiltro, false);
			
				if (tipoTarifa == Constantes.TARIFA_GPROPIEDAD){
					criteria.andTipoTarifaEqualTo(tipoTarifa);
				}else{
					tarifaExample.clear();
					criteria = tarifaExample.createCriteria();
					criteria.andIdCementerioEqualTo(idCementerio);
				}
					
				tarifaExample.or(criteria);
				listaTarifas = tarifaDAO.selectByExample(tarifaExample); 
	
				if ((listaTarifas != null) && (listaTarifas.size()>0)) break; 
				tarifaExample.clear();
				
			}
			for (int j = 0; j < listaTarifas.size(); j++) {
				Tarifa tarifa = (Tarifa) listaTarifas.get(j);
				TarifaBean tarifaBean = mappingManager.mapTarifaVoToBean(tarifa);
				alRet.put(tarifaBean.getId_tarifa(), tarifaBean);
			}
		}

		return alRet.values();
	}
	
	public Collection findTarifasDifunto(String superpatron,String patron, Object[] filtro, Integer idCementerio, Integer tipoTarifa,
			Sesion userSesion) throws Exception {

		List<Tarifa> listaTarifas = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		TarifaExample tarifaExample = new TarifaExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.TarifaExample.Criteria criteria = tarifaExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.TarifaExample.Criteria) 
				addFilter.addTarifaFilter(criteria, campoFiltro, false);
				
				if (tipoTarifa == Constantes.TARIFA_GDIFUNTOS){
					criteria.andTipoTarifaEqualTo(tipoTarifa);
				}else{
					tarifaExample.clear();
					criteria = tarifaExample.createCriteria();
					criteria.andIdCementerioEqualTo(idCementerio);
				}
			
				tarifaExample.or(criteria);
				listaTarifas = tarifaDAO.selectByExample(tarifaExample); 
				
				if ((listaTarifas != null) && (listaTarifas.size()>0)) break; 
				tarifaExample.clear();

			}
			for (int j = 0; j < listaTarifas.size(); j++) {
				Tarifa tarifa = (Tarifa) listaTarifas.get(j);
				TarifaBean tarifaBean = mappingManager.mapTarifaVoToBean(tarifa);
				alRet.put(tarifaBean.getId_tarifa(), tarifaBean);
			}
		}

		return alRet.values();
	}
	
	

}