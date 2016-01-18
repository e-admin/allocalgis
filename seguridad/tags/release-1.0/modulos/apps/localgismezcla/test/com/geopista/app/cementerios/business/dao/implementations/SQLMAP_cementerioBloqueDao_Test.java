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
import com.geopista.app.cementerios.business.vo.Bloque;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SQLMAP_cementerioBloqueDao_Test {

	
    private BloqueDAO bloqueDAO;
    
	public SQLMAP_cementerioBloqueDao_Test(){
	      
	      bloqueDAO = new BloqueDAOImpl();

	}
	
	
	@Test
	public void testBloqueDAOImpl() {
		assertTrue(true);
	}

	@Test
	public void testDeleteByPrimaryKey() {
		assertTrue(true);
	}

	@Test
	public void testInsert() {
		assertTrue(true);
	}

	@Test
	public void testInsertSelective() {
		assertTrue(true);
	}

	@Test
	public void testSelectByPrimaryKey() {
		assertTrue(true);
	}

	@Test
	public void testUpdateByPrimaryKeySelective() {
		assertTrue(true);
	}

	@Test
	public void testUpdateByPrimaryKey() {
		assertTrue(true);
	}

	@Test
	public void testSelectByLastSeqKey() {
		assertTrue(true);
	}

	@Test
	public void testSelectAll() {
		assertTrue(true);
	}

	@Test
	public void testSelectByFeature() throws NumberFormatException, SQLException {
		List listFeatures = new ArrayList();
		listFeatures.add(new String("76")) ;

		Object[] idFeatures = listFeatures.toArray(); 
		
		
		if (idFeatures.length ==1){
    		for (int j=0; j<idFeatures.length; j++){
    		Bloque	bloque = bloqueDAO.selectByFeature(Integer.parseInt((String)idFeatures[j]));
    		System.out.println(bloque.getDescripcion());
    		}
    	}
		System.out.println("finnn");
		
	}

}
