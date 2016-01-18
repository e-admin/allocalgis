package com.geopista.app.cementerios.business.dao.implementations;

import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import com.geopista.app.cementerios.business.dao.interfaces.ExhumacionDAO;
import com.geopista.app.cementerios.business.vo.Exhumacion;
import com.geopista.app.cementerios.business.vo.ExhumacionExample;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample;
import com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample.Criteria;


public class SQLMap_cementerio_exhumacion_Test extends TestCase {

    private ExhumacionDAO exhumacionDAO;
    
	public SQLMap_cementerio_exhumacion_Test(){

		super();
		
		exhumacionDAO = new ExhumacionDAOImpl();
				
}

	
	public void testSelectExample() throws SQLException{
		
		List<Exhumacion> listaunidad;
		
		ExhumacionExample example = new ExhumacionExample();
		
		com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria criteria = example.createCriteria();
		
		
		criteria.andCodigoLike("%Exh%");
		
		example.or(criteria);
		
		listaunidad = exhumacionDAO.selectByExample(example);
		
		 for (int i = 0; i < listaunidad.size(); i++) {
				Exhumacion unidad = listaunidad.get(i);
				System.out.println(unidad.toString());
			}
		
	}
}