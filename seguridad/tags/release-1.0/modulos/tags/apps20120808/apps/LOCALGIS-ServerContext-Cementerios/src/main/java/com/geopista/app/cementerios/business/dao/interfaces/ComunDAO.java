package com.geopista.app.cementerios.business.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.geopista.app.cementerios.business.vo.Comun;

public interface ComunDAO {
	
	/**
	 * selectByRegClass
	 * @param comun
	 * @return la lista de las columnas y tipos de la tabla dada.
	 */
	List selectByRegClass(Comun comun) throws SQLException;
	
	/**
	 * Set the iBATIS Database Layer SqlMapClient to work with.
	 * @param sqlMapper
	 */
//	void setSqlMapClient(SqlMapClient sqlMapper);
	
	
}
