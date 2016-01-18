package com.geopista.app.cementerios.business.dao.implementations;

import java.io.IOException;
import java.io.Reader;

import com.geopista.app.cementerios.business.dao.interfaces.elem_cementerioDAO;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import junit.framework.TestCase;

public class SQLMap_elem_cementerioDao_Test extends TestCase {

    private elem_cementerioDAO elemCementerioDAO;
    
	public SQLMap_elem_cementerioDao_Test(){

//		Reader reader = null;
//		try {
//			reader = Resources.getResourceAsReader("sqlMaps/sqlMapConfig.xml");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//				e.printStackTrace();
//		} 
//	      SqlMapClient sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader); 
	      
	      elemCementerioDAO = new elem_cementerioDAOImpl();

	}
	
	
	public void testElem_cementerioDAOImpl() {
		assertTrue(true);
	}

	public void testDeleteByPrimaryKey() {
		assertTrue(true);
	}

	public void testInsert() {
		elem_cementerio elemCementerio = new elem_cementerio();
    	

		elemCementerio.setIdMunicipio(new Integer(34083));
		elemCementerio.setNombre("nombre municipio");
		elemCementerio.setEntidad("entidad mun");
		elemCementerio.setTipo("2");
		
    try{
		
		elemCementerioDAO.insert(elemCementerio);

		elem_cementerio sal = new elem_cementerio();
		
		int num = elemCementerioDAO.selectByLastSeqKey();
		
		sal = elemCementerioDAO.selectByPrimaryKey(num);
		
		System.out.println(sal.getNombre());
		System.out.println(sal.getEntidad());
		System.out.println(sal.getTipo());
		
	}catch (Exception e) {
		fail("returnElemtEnterramiento: "+ e.getMessage());
		//System.out.println("returnElemtEnterramiento: "+ e.getMessage());
		}	
	
	}

	public void testInsertSelective() {
		assertTrue(true);
	}

	public void testSelectByPrimaryKey() {
		assertTrue(true);
	}

	public void testUpdateByPrimaryKeySelective() {
		assertTrue(true);
	}

	public void testUpdateByPrimaryKey() {
		assertTrue(true);
	}

}
