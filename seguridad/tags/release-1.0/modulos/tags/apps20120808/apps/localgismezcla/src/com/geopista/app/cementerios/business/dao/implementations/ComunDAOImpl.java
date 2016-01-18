package com.geopista.app.cementerios.business.dao.implementations;

import java.sql.SQLException;
import java.util.List;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.interfaces.ComunDAO;
import com.geopista.app.cementerios.business.vo.Comun;

public class ComunDAOImpl extends DAO implements ComunDAO {


	public List selectByRegClass(Comun comun) throws SQLException {
		List list = getSqlMapClientTemplate()
		.queryForList(
				"cementerio_comun.selectByRegClass",
				comun);
		return list;
	}
	
	

}
