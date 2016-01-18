/**
 * mapDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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