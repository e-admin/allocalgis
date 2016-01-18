package com.geopista.app.cementerios.business.dao.implementations;

import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import com.geopista.app.cementerios.business.dao.interfaces.UnidadEnterramientoDAO;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample;
import com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample.Criteria;

public class SQLMap_unidadEnterramientoDao_Test extends TestCase {

    private UnidadEnterramientoDAO unidadEnterramientoDAO;
    
	public SQLMap_unidadEnterramientoDao_Test(){

		super();
		
		unidadEnterramientoDAO = new UnidadEnterramientoDAOImpl();
		
//		Reader reader = null;
//		try {
//			reader = Resources.getResourceAsReader("sqlMaps/sqlMapConfig.xml");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//				e.printStackTrace();
//		} 
//	      SqlMapClient sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader); 
//	      
//	      unidadEnterramientoDAO = new UnidadEnterramientoDAOImpl();
//	      unidadEnterramientoDAO.setSqlMapClient(sqlMapper);

	}
	
	
	public void testSelectByPrimaryKey() throws SQLException {
		
		UnidadEnterramientoExample example = new UnidadEnterramientoExample();
		example.createCriteria().andColumnaBetween(1, 10);
		
		
		List<UnidadEnterramiento> listaunidad;
		 listaunidad = unidadEnterramientoDAO.selectByExample(example);
		
		 for (int i = 0; i < listaunidad.size(); i++) {
			UnidadEnterramiento unidad = listaunidad.get(i);
			System.out.println(unidad.getColumna());
		}
		
	}
	
	public void testSelect() throws SQLException {
		
		List<UnidadEnterramiento> listaunidad;
		 listaunidad = unidadEnterramientoDAO.selectAll();
		
		 for (int i = 0; i < listaunidad.size(); i++) {
			UnidadEnterramiento unidad = listaunidad.get(i);
			System.out.println(unidad.toString());
		}
		
	}


	public void testSelectExample() throws SQLException{
		
		List<UnidadEnterramiento> listaunidad;
		
		UnidadEnterramientoExample unidadExample = new UnidadEnterramientoExample();
		Criteria criteria = unidadExample.createCriteria();
		
		criteria.andCodigoLike("%UE%");
		
		unidadExample.or(criteria);
		
		listaunidad = unidadEnterramientoDAO.selectByExample(unidadExample);
		
		 for (int i = 0; i < listaunidad.size(); i++) {
				UnidadEnterramiento unidad = listaunidad.get(i);
				System.out.println(unidad.toString());
			}
		
	}
	
	public void testUpdateByPrimaryKeySelective() {
		
		assertTrue(true);
		
		
	}

	public void testUpdateByPrimaryKey() {
		assertTrue(true);
	}

}
