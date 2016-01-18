package com.geopista.app.cementerios.business.dao.implementations;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.interfaces.mapDAO;



public class mapDAOImpl extends DAO implements mapDAO{
	
	public List selectAttachedCementerio (Integer idelemcementerio){
		
		Map map = new HashMap();
		map.put("idelemcementerio", idelemcementerio);
		
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList(
					"documentos.selectAttachedCementerio", map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List selectdocumento (){
		
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList(
					"documentos.selectdocumento", null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List selectanexo (){
		
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList(
					"documentos.selectanexo", null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List selectDifunto(){
		
		List list = null;
		try {
			list = getSqlMapClientTemplate().queryForList(
					"documentos.selectDifunto", null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}