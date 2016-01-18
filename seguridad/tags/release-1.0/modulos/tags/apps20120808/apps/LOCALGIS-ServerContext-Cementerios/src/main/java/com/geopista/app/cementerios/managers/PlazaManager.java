package com.geopista.app.cementerios.managers;

import java.util.List;

import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.business.dao.implementations.DifuntoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PlazaDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.DifuntoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PlazaDAO;
import com.geopista.app.cementerios.business.vo.Difunto;
import com.geopista.app.cementerios.business.vo.DifuntoExample;
import com.geopista.app.cementerios.business.vo.Plaza;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.PermissionException;

public class PlazaManager {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PlazaManager.class);
	
	private static PlazaManager instance;
	
    private PlazaDAO plazaDAO;
    private DifuntoDAO difuntoDAO;
    private MappingManager mappingManager;
    
    public PlazaManager(){
        
        plazaDAO = new PlazaDAOImpl();
        difuntoDAO = new DifuntoDAOImpl();
        mappingManager = MappingManager.getIntance();
    }
    
    public static PlazaManager getInstance(){
    	if (instance == null){
    		instance = new PlazaManager();
    	}
    	return instance;
    }
	
    /**************************************************************** PLAZA ****************************************************************************/
	  
    public PlazaBean insertPlaza (Object[] idLayers, Object[] idFeatures, PlazaBean plazaElem, Sesion userSesion) throws PermissionException,LockException,Exception{
  
    	try{
    	
			//1.Creo la plazaVO
			Plaza plazaVO = new Plaza();
	//		plazaVO.setDescripcion(plazaElem.getDescripcion());
			if (plazaElem.isEstado()){
				plazaVO.setEstado(Constantes.TRUE);
			}else{
				plazaVO.setEstado(Constantes.FALSE);
			}
			plazaVO.setIdUnidadenterramiento(plazaElem.getIdUnidadEnterramiento());
			plazaVO.setModicado(plazaElem.getModificado());
			plazaVO.setSituacion(plazaElem.getSituacion());
			plazaVO.setId(plazaElem.getIdPlaza());
			
			plazaDAO.insert(plazaVO);
	   		int idPlaza = plazaDAO.selectByLastSeqKey();
	   		plazaElem.setIdPlaza(idPlaza);

    	}catch (Exception e) {
    		logger.error("Error en la inserccion de la plaza en bbdd" + e);
		}

    	return plazaElem;
	} 
   
   /**
    *  getPlaza
    * @param id
    * @param userSesion
    * @return
    * @throws Exception
    */
   public PlazaBean getPlaza(Long id,  Sesion userSesion) throws Exception {
    	
    	PlazaBean alRet= null;
    	
    	int id_plaza = id.intValue();
    	
    	Plaza plaza = plazaDAO.selectByPrimaryKey(id_plaza);
    	
    	if (plaza!= null){
    		PlazaBean plazaBean = new PlazaBean();
    		
    		if (plaza.getEstado() == Constantes.TRUE){
    			plazaBean.setEstado(true);
    		}else{
    			plazaBean.setEstado(false);
    		}
    		plazaBean.setIdPlaza(plaza.getId());
    		plazaBean.setModificado(plaza.getModicado());
    		plazaBean.setSituacion(plaza.getSituacion());
    		plazaBean.setIdUnidadEnterramiento(plaza.getIdUnidadenterramiento());
    		
    		DifuntoExample difExample = new DifuntoExample();
    		difExample.createCriteria().andIdPlazaEqualTo(plaza.getId());
			
    		List listDifunto =  difuntoDAO.selectByExample(difExample);
			Difunto difunto = null;
			 for (int m = 0; m < listDifunto.size(); m++) {
				 difunto = (Difunto) listDifunto.get(m);
			 }
			DifuntoBean difuntoBean = mappingManager.mapDifuntoVOToBean(difunto);
    		plazaBean.setDifunto(difuntoBean);
    		
    		alRet= plazaBean;
    	}
    	
        return alRet;
    }
}
