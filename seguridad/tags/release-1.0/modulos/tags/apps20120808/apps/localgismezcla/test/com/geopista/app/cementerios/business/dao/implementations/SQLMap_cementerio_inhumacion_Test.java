package com.geopista.app.cementerios.business.dao.implementations;

import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import com.geopista.app.cementerios.business.dao.interfaces.ExhumacionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.InhumacionDAO;
import com.geopista.app.cementerios.business.vo.Inhumacion;
import com.geopista.app.cementerios.business.vo.ExhumacionExample;
import com.geopista.app.cementerios.business.vo.InhumacionExample;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample;
import com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample.Criteria;


public class SQLMap_cementerio_inhumacion_Test extends TestCase {

    private InhumacionDAO inhumacionDAO;
    
	public SQLMap_cementerio_inhumacion_Test(){

		super();
		
		inhumacionDAO = new InhumacionDAOImpl();
				
}

	
	public void testSelectAll() throws SQLException{
		
		List<Inhumacion> listaunidad;
		listaunidad = inhumacionDAO.selectAll();
		
		 for (int i = 0; i < listaunidad.size(); i++) {
				Inhumacion unidad = listaunidad.get(i);
				System.out.println(unidad.toString());
			}
		
	}
}