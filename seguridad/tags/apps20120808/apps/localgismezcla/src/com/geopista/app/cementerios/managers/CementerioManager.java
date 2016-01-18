package com.geopista.app.cementerios.managers;

import com.geopista.app.cementerios.business.dao.implementations.CementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.vo.Cementerio;

public class CementerioManager {
	
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BloqueManager.class);
		

	private static CementerioManager instance;
    private CementerioDAO cementerioDAO;
	
	public static CementerioManager getInstance(){
		if(instance == null){
			instance = new CementerioManager();
		}
		return instance;
	}
	
	public CementerioManager(){
		cementerioDAO = new CementerioDAOImpl();
		
	}
	
    /*************************************************************** CEMENTERIO **********************************************************************/

    public Cementerio getCementerio(Integer idCementerio){

    	Cementerio cementerio = null;
    	try{
    		cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
    	}catch (Exception e) {
			logger.error("Error obteniendo el cementerio" + e);
		}

		return cementerio;
    }

}
