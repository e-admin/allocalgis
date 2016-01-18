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
import com.geopista.app.cementerios.business.dao.interfaces.ComunDAO;
import com.geopista.app.cementerios.business.vo.Bloque;
import com.geopista.app.cementerios.business.vo.Comun;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SQLMAP_cementerioComunDao_Test {

	
    private ComunDAO comunDAO;
    
	public SQLMAP_cementerioComunDao_Test(){

	      comunDAO = new ComunDAOImpl();

	}
	
	
	@Test
	public void testSelectByRegClass() throws SQLException {
		
		Comun comun = new Comun();
		comun.setAttrelid("cementerio.unidadEnterramiento");
		
		
		List listColumns = new ArrayList();
		
		listColumns = comunDAO.selectByRegClass(comun);
		
		for (int i = 0; i < listColumns.size(); i++) {
			Comun elem = (Comun) listColumns.get(i);
			System.out.println(elem.getAttname() + " - " + elem.getFormat_type());
			
		}
		
		System.out.println("finnnm");
	}

}
