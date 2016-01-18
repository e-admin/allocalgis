package com.geopista.app.cementerios.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.BloqueDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.CementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.CementerioFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ConcesionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DatosFallecimientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DifuntoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.IntervencionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.LocalizacionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PersonaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PlazaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.RelTitularDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.TarifaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.UnidadEnterramientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.elem_cementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.BloqueDAO;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ConcesionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DatosFallecimientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DifuntoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.IntervencionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.LocalizacionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PersonaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PlazaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.RelTitularDAO;
import com.geopista.app.cementerios.business.dao.interfaces.TarifaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.UnidadEnterramientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.elem_cementerioDAO;
import com.geopista.app.cementerios.business.vo.Bloque;
import com.geopista.app.cementerios.business.vo.BloqueExample;
import com.geopista.app.cementerios.business.vo.Cementerio;
import com.geopista.app.cementerios.business.vo.Concesion;
import com.geopista.app.cementerios.business.vo.ConcesionExample;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.geopista.app.cementerios.business.vo.elem_cementerioExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.cementerios.CementerioConnection;
import com.geopista.server.cementerios.document.DocumentoEnDisco;

public class BloqueManager extends DAO {

	
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BloqueManager.class);
	
	private static BloqueManager instance;
	
    private CementerioDAO cementerioDAO;
    private elem_cementerioDAO elemCementerioDAO;
    private BloqueDAO bloqueDAO;
    private AddFilter addFilter;
    private MappingManager mappingManager;
    
    public BloqueManager(){
    	bloqueDAO = new BloqueDAOImpl();
        elemCementerioDAO = new elem_cementerioDAOImpl();
        bloqueDAO = new BloqueDAOImpl();
        cementerioDAO = new CementerioDAOImpl();
        
        addFilter = AddFilter.getInstance();
        mappingManager = MappingManager.getIntance();
    }
    
    public static BloqueManager getInstance(){
    	if (instance == null){
    		instance = new BloqueManager();
    	}
    	return instance;
    }
	
	
    /**************************************************************** BLOQUE ****************************************************************************/
    
    
    /**
     * InsertBloque
     */
    public BloqueBean insertBloque (Object[] idLayers, Object[] idFeatures, BloqueBean elem, Integer idCementerio, Sesion userSesion) throws PermissionException,LockException,Exception{
    	
    	try{
    		/**Start de la transacción**/
    		getSqlMapClientTemplate().startTransaction();
    		
    		CementerioManager cementerioManager = CementerioManager.getInstance();
    		
       		//Pasos que hay que hacer 
    		int idBloque = -1;
    		
       		Cementerio cementerio = cementerioManager.getCementerio(idCementerio);

       		if (cementerio == null) return null;
       		
       		//1.Insert el cementerioElem generico --> insertElemEnterramiento(elem);
       		elem_cementerio elemCementerio = new elem_cementerio();

           	elemCementerio.setIdMunicipio(Integer.parseInt(elem.getIdMunicipio()));
           	elemCementerio.setNombre(cementerio.getNombre());
       		elemCementerio.setEntidad(elem.getEntidad());
       		elemCementerio.setTipo(elem.getTipo());
       		elemCementerio.setIdCementerio(idCementerio);
    		
    		elemCementerioDAO.insert(elemCementerio);

    		int idElemCementerio = elemCementerioDAO.selectByLastSeqKey();
    		elemCementerio.setId(idElemCementerio);
    		
    		Bloque bloque = new Bloque();
    		//2. El insert de la feature asociada al bloque
    		//Aunque en teoria debe de ser solo 1 feature
    		for (int j=0; j<idFeatures.length; j++){
    			
        		bloque.setDescripcion(elem.getDescripcion());
        		bloque.setNumColumnas(elem.getNumColumnas());
        		bloque.setNumFilas(elem.getNumFilas());
        		bloque.setTipoBloque(elem.getTipo_unidades());
        	    bloque.setIdElemcementerio(idElemCementerio);
    			bloque.setIdFeature(Integer.parseInt((String)idFeatures[j]));
        		
        		bloqueDAO.insert(bloque);
        		idBloque = bloqueDAO.selectByLastSeqKey();
        		
        		elem.setId_bloque(idBloque);
    		}
    		
    		elem.setIdFeatures(idFeatures);
    		elem.setIdLayers(idLayers);
    		
			/** Actualizamos los ficheros en disco (temporal --> destino) */
			DocumentoEnDisco.actualizarConFicherosDeTemporal(((BloqueBean) elem).getDocumentos());
    		
    		/**Si no hay problemas commit de las operaciones realizadas**/
    		getSqlMapClientTemplate().commitTransaction();

    	}catch (Exception e) {
			logger.error("Inserccion del bloque" + e);
			
		}finally{
			
			getSqlMapClientTemplate().endTransaction();
		}
    	return elem;
    }

    /**
     * UpdateBloque
     * @param idLayers
     * @param idFeatures
     * @param elem
     * @param userSesion
     * @return
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    public BloqueBean updateBloque (BloqueBean elem, Sesion userSesion) throws PermissionException,LockException,Exception{
    	
    	try{
    		
    		getSqlMapClientTemplate().startTransaction();
    		
    		elem_cementerio elemCementerio = new elem_cementerio();
    		
        	elemCementerio.setIdMunicipio(Integer.parseInt(elem.getIdMunicipio()));
        	elemCementerio.setNombre(elem.getNombreCementerio());
    		elemCementerio.setEntidad(elem.getEntidad());
    		elemCementerio.setTipo(elem.getTipo());
    		elemCementerio.setId((int)elem.getId());
//    		elemCementerio.setIdCementerio(elem.getIdCementerio()); --> no se puede mover del cementerio...
    		
    		elemCementerioDAO.updateByPrimaryKeySelective(elemCementerio);
    			
    		//2. update el bloque especifico
    		Bloque bloque = new Bloque();
    		
    		bloque.setDescripcion(elem.getDescripcion());
    		bloque.setNumColumnas(elem.getNumColumnas());
    		bloque.setNumFilas(elem.getNumFilas());
    		bloque.setTipoBloque(elem.getTipo_unidades());
    		bloque.setId(elem.getId_bloque());
    		bloque.setIdElemcementerio((int)elem.getId());
    		
    		bloqueDAO.updateByPrimaryKeySelective(bloque);
    			
			/** Actualizamos los ficheros en disco (temporal --> destino) */
			DocumentoEnDisco.actualizarConFicherosDeTemporal(((BloqueBean) elem).getDocumentos());
			
    		getSqlMapClientTemplate().commitTransaction();
    		
    	}catch (Exception e) {
			logger.error("Inserccion del bloque" + e);
			
		}finally{
			
			getSqlMapClientTemplate().endTransaction();
		}
    	
    	return elem;
    }
    
    /**
     * deleteBloque
     * @param idLayers
     * @param idFeatures
     * @param elem
     * @param userSesion
     * @return
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    public BloqueBean deleteBloque (BloqueBean elem, Sesion userSesion) throws PermissionException,LockException,Exception{
    	
       try{
    	   getSqlMapClientTemplate().startTransaction();
    	   
    	   bloqueDAO.deleteByPrimaryKey((int)elem.getId_bloque());
    	   
       	   elemCementerioDAO.deleteByPrimaryKey((int)elem.getId());
       	   
       	   getSqlMapClientTemplate().commitTransaction();
       		
        }catch (Exception e) {
        	logger.error("returnDeleteBloque: "+ e.getMessage());
        	throw e;
        	
        }finally{
        	
        	getSqlMapClientTemplate().endTransaction();
        }

       return elem;
   }
    
    /**
     * GETBLOQUE
     * @param superpatron
     * @param patron
     * @param filtro
     * @param idLayers
     * @param idFeatures
     * @param userSesion
     * @return
     * @throws Exception
     */
    public BloqueBean getBloque(String superpatron, String patron, Object[] filtro, Object[] idLayers, Object[] idFeatures, Integer idCementerio, Sesion userSesion) throws Exception {
    	
    	BloqueBean alRet = null;
    	Bloque bloque = null;

    	if (idFeatures.length ==1){
    		for (int j=0; j<idFeatures.length; j++){
    			bloque = bloqueDAO.selectByFeature(Integer.parseInt((String)idFeatures[j]));
    		}
    	}
		if (bloque != null){	
			
    	elem_cementerioExample elemExample = new  elem_cementerioExample();
    	elemExample.createCriteria().andIdCementerioEqualTo(idCementerio).andIdEqualTo(bloque.getIdElemcementerio());

    	List elem_cementerioList =  elemCementerioDAO.selectByExample(elemExample);

	    if (elem_cementerioList.size() != 1){
	    	return alRet;
	    }
	    //Se retorna una lista pero solo debe contener un elemento
	    elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0); 
	    if (elem != null){
	    	
	    //Recuperamos el nombre del cementerio para hacer el set en la unidad
	    Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
			
    	
    	BloqueBean bloqueBean = new BloqueBean ();
	    	bloqueBean.setNombreCementerio(cementerio.getNombre());
	    	bloqueBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
	    	bloqueBean.setEntidad(elem.getEntidad());
	    	bloqueBean.setId(elem.getId());
	    	
	    	bloqueBean.setId_bloque(bloque.getId());
	    	
	    	bloqueBean.setDescripcion(bloque.getDescripcion());
	    	bloqueBean.setIdElemCementerio(bloque.getIdElemcementerio());
	    	bloqueBean.setId_feature(bloque.getIdFeature());
	    	bloqueBean.setNumColumnas(bloque.getNumColumnas());
	    	bloqueBean.setNumFilas(bloque.getNumFilas());
	    	bloqueBean.setTipo_unidades(bloque.getTipoBloque());
	    	
	    	alRet = bloqueBean;
		}
		}
        return alRet;
    	
    }    
    
    /**
     * getAllBloques
     */
   public Collection getAllBloques() throws Exception {
    	
    	List<Bloque> listaBloques;
    	HashMap alRet= new HashMap();

    	listaBloques = bloqueDAO.selectAll();
    	Bloque bloque;

    	for (int i = 0; i < listaBloques.size(); i++) {
    		
    		bloque = (Bloque) listaBloques.get(i);
    
        	elem_cementerio elem = elemCementerioDAO.selectByPrimaryKey(bloque.getIdElemcementerio());
      	
        	BloqueBean bloqueBean = new BloqueBean ();
    	    	bloqueBean.setNombreCementerio(elem.getNombre());
    	    	bloqueBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
    	    	bloqueBean.setEntidad(elem.getEntidad());
    	    	bloqueBean.setId(elem.getId());
    	    	
    	    	Collection features = new Vector();
    	    	features.add(bloque.getIdFeature());
    	    	bloqueBean.setIdFeatures(features);
    	    	
    	    	bloqueBean.setId_bloque(bloque.getId());
    	    	
    	    	bloqueBean.setDescripcion(bloque.getDescripcion());
    	    	bloqueBean.setIdElemCementerio(bloque.getIdElemcementerio());
    	    	bloqueBean.setId_feature(bloque.getIdFeature());
    	    	bloqueBean.setNumColumnas(bloque.getNumColumnas());
    	    	bloqueBean.setNumFilas(bloque.getNumFilas());
    	    	bloqueBean.setTipo_unidades(bloque.getTipoBloque());
    	    	
    	    	

    	    alRet.put(bloqueBean.getId_bloque(), bloqueBean);
		}
    	return alRet.values();
   }
   
   /**
    * getAllBloquesByCementerio
    */
  public Collection getAllBloquesByCementerio(Integer idCementerio) throws Exception {
   	
   	List<Bloque> listaBloques;
   	HashMap alRet= new HashMap();

   	listaBloques = bloqueDAO.selectAll();
   	Bloque bloque;

   	for (int i = 0; i < listaBloques.size(); i++) {
   		
   		bloque = (Bloque) listaBloques.get(i);
   
//       	elem_cementerio elem = elemCementerioDAO.selectByPrimaryKey(bloque.getIdElemcementerio());
   		
    	elem_cementerioExample elemExample = new  elem_cementerioExample();
    	elemExample.createCriteria().andIdCementerioEqualTo(idCementerio).andIdEqualTo(bloque.getIdElemcementerio());

    	List elem_cementerioList =  elemCementerioDAO.selectByExample(elemExample);
    	
    	if (elem_cementerioList.size() != 1){
    		continue;
    	}
    	//Se retorna una lista pero solo debe contener un elemento
    	elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0); 
    	if (elem != null){
    	
    	//Recuperamos el nombre del cementerio para hacer el set en la unidad
    	Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
    		
       	BloqueBean bloqueBean = new BloqueBean ();
   	    	bloqueBean.setNombreCementerio(cementerio.getNombre());
   	    	bloqueBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
   	    	bloqueBean.setEntidad(elem.getEntidad());
   	    	bloqueBean.setId(elem.getId());
   	    	
   	    	Collection features = new Vector();
   	    	features.add(bloque.getIdFeature());
   	    	bloqueBean.setIdFeatures(features);
   	    	
   	    	bloqueBean.setId_bloque(bloque.getId());
   	    	
   	    	bloqueBean.setDescripcion(bloque.getDescripcion());
   	    	bloqueBean.setIdElemCementerio(bloque.getIdElemcementerio());
   	    	bloqueBean.setId_feature(bloque.getIdFeature());
   	    	bloqueBean.setNumColumnas(bloque.getNumColumnas());
   	    	bloqueBean.setNumFilas(bloque.getNumFilas());
   	    	bloqueBean.setTipo_unidades(bloque.getTipoBloque());
   	    	
   	    alRet.put(bloqueBean.getId_bloque(), bloqueBean);
    	}
	}
   	return alRet.values();
  }
  
  public Collection getBloquesByFilter(String superpatron, String patron, Object[] filtro,  Integer idCementerio, Sesion userSesion) throws Exception {
	  	
	  	List<Bloque> listaBloques;
	  	HashMap alRet= new HashMap();
	  	
	  	BloqueExample bloqueExample = new BloqueExample();
	  	if (filtro!= null){
	  		CampoFiltro campoFiltro;
	  		com.geopista.app.cementerios.business.vo.BloqueExample.Criteria criteria = bloqueExample.createCriteria();
	  		for (int i = 0; i < filtro.length; i++) {
					campoFiltro = (CampoFiltro) filtro[i];
					criteria = (com.geopista.app.cementerios.business.vo.BloqueExample.Criteria) addFilter.addBloqueFilter (criteria, campoFiltro, false);
	  		}
	  		bloqueExample.or(criteria);
	  		listaBloques = bloqueDAO.selectByExample(bloqueExample);
	  			
	  		for (int j = 0; j < listaBloques.size(); j++) {
	  			Bloque bloque = (Bloque) listaBloques.get(j);
	  			BloqueBean bloqueBean = mappingManager.mapBloqueVOToBean(bloque, idCementerio);
	  			alRet.put(bloqueBean.getId_bloque(), bloqueBean);
			}
			}
	      return alRet.values();
	  }
	
  
	public Collection findBloques(String superpatron, String patron, Object[] filtro, Integer idCementerio, Sesion userSesion) throws Exception {

	  	List<Bloque> listaBloques = null;
	  	HashMap alRet= new HashMap();
	  	boolean buscar = true;
	  	
	  	BloqueExample bloqueExample = new BloqueExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			for (int i = 0; i < filtro.length; i++) {
				com.geopista.app.cementerios.business.vo.BloqueExample.Criteria criteria = bloqueExample.createCriteria();
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.BloqueExample.Criteria) addFilter.addBloqueFilter (criteria, campoFiltro,buscar);				
				
		  		bloqueExample.or(criteria);
		  		listaBloques = bloqueDAO.selectByExample(bloqueExample);
				
				if ((listaBloques != null) && (listaBloques.size()>0)) break; 
				bloqueExample.clear();
				
			}

	  		for (int j = 0; j < listaBloques.size(); j++) {
	  			Bloque bloque = (Bloque) listaBloques.get(j);
	  			BloqueBean bloqueBean = mappingManager.mapBloqueVOToBean(bloque, idCementerio);
	  			alRet.put(bloqueBean.getId_bloque(), bloqueBean);
			}
		}

		return alRet.values();
	}
  
}
