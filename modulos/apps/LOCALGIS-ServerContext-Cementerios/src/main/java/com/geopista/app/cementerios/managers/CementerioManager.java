/**
 * CementerioManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
