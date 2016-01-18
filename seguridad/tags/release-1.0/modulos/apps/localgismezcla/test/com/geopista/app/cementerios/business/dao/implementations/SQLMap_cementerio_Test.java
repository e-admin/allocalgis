package com.geopista.app.cementerios.business.dao.implementations;

import static org.junit.Assert.*;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.junit.Test;

import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.InhumacionDAO;
import com.geopista.app.cementerios.business.vo.Cementerio;

public class SQLMap_cementerio_Test extends TestCase {
	
    private CementerioDAO cementerioDAO;
    
    public SQLMap_cementerio_Test(){
    
    	cementerioDAO = new CementerioDAOImpl();
    }
    
	@Test
	public void testSelectByPrimaryKey() throws SQLException {
		
		Cementerio cementerio = new Cementerio();

		cementerio = cementerioDAO.selectByPrimaryKey(11);
		
		System.out.println(cementerio.getNombre());
		
	}

}
