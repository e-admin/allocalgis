package com.geopista.app.cementerios.business.dao.implementations;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.geopista.app.cementerios.business.dao.interfaces.BloqueDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PlazaDAO;
import com.geopista.app.cementerios.business.vo.Bloque;
import com.geopista.app.cementerios.business.vo.Plaza;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SQLMAP_cementerioPlazaDao_Test {

	
    private PlazaDAO plazaDAO;
    
	public SQLMAP_cementerioPlazaDao_Test(){

//		Reader reader = null;
//		try {
//			reader = Resources.getResourceAsReader("sqlMaps/sqlMapConfig.xml");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//				e.printStackTrace();
//		} 
//	      SqlMapClient sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader); 
//	      
	      plazaDAO = new PlazaDAOImpl();
//	      plazaDAO.setSqlMapClient(sqlMapper);

	}
	
	@Test
	public void testSelectCountPlazasAsingadas() throws SQLException {
		
		Plaza plaza = new Plaza();
		plaza.setIdUnidadenterramiento(16);
		plaza.setEstado(0);
		
		int numPlazas = plazaDAO.selectCountPlazasAsignadas(plaza);
    	System.out.println(numPlazas);
	}

}
