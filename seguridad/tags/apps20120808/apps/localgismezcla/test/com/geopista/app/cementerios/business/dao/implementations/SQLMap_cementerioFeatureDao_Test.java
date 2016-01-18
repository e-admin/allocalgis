package com.geopista.app.cementerios.business.dao.implementations;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.geopista.app.cementerios.business.dao.interfaces.CementerioFeatureDAO;
import com.geopista.app.cementerios.business.vo.CementerioFeatureKey;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SQLMap_cementerioFeatureDao_Test {
	
	
    private CementerioFeatureDAO cementerioFeatureDAO;
    
	public SQLMap_cementerioFeatureDao_Test(){

	      cementerioFeatureDAO = new CementerioFeatureDAOImpl();

	}

	@Test
	public void testInsert() {
		assertTrue(true);
	}

//	@Test
//	public void testSelectByExample() {
//		 
//		List lista = new ArrayList<CementerioFeatureKey>();
//	    
//		lista = cementerioFeatureDAO.selectByFeature(6527);
//		
//		for (int i = 0; i < lista.size(); i++) {
//			CementerioFeatureKey key = (CementerioFeatureKey) lista.get(i);
//			
//			System.out.println(key.getIdLayer());
//			System.out.println(key.getIdElemcementerio());
//			System.out.println(key.getIdFeature());
//			
//		}
//		
//		assertTrue(true);
//	}
	
	@Test
	public void testSelectByFeatureAndLayerUnidad() throws SQLException {
		 
		List lista = new ArrayList<CementerioFeatureKey>();
		CementerioFeatureKey key = new CementerioFeatureKey();
		key.setIdFeature(80);
		key.setIdLayer("unidad_enterramiento");
	    
		lista = cementerioFeatureDAO.selectByFeatureAndLayerUnidad(key);
		
		for (int i = 0; i < lista.size(); i++) {
			key = (CementerioFeatureKey) lista.get(i);
			
			System.out.println(key.getIdLayer());
			System.out.println(key.getIdElem());
			System.out.println(key.getIdFeature());
			
		}
		
		assertTrue(true);
	}

}













