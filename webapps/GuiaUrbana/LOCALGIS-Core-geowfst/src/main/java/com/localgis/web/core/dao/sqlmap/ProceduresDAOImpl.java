/**
 * ProceduresDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import java.util.List;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.ProceduresDAO;
import com.localgis.web.core.model.Procedure;
import com.localgis.web.core.model.ProcedureProperty;
import com.localgis.web.core.model.ProcedurePropertyKey;

public class ProceduresDAOImpl extends SqlMapDaoTemplate implements ProceduresDAO {

    public ProceduresDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

	@Override
	public void deleteByPrimaryKey(String id) {
		Procedure key = new Procedure();
		key.setId(id);
		delete(
				"geowfst_procedures.ibatorgenerated_deleteByPrimaryKey",
				key);
	}

	@Override
	public void insert(Procedure record) {
		insert("geowfst_procedures.ibatorgenerated_insert",
				record);
	}

	@Override
	public void insertSelective(Procedure record) {
		insert("geowfst_procedures.ibatorgenerated_insertSelective",
				record);
	}

	@Override
	public Procedure selectByPrimaryKey(String id) {
		Procedure key = new Procedure();
		key.setId(id);
		Procedure record = (Procedure) queryForObject(
				"geowfst_procedures.ibatorgenerated_selectByPrimaryKey",
				key);
		return record;
	}

	@Override
	public Procedure selectByLayerName(String layerName) {
		Procedure key = new Procedure();
		key.setLayerName(layerName);
		Procedure record = (Procedure) queryForObject(
				"geowfst_procedures.ibatorgenerated_selectByLayerName",
				key);
		return record;
	}

	@Override
	public Procedure selectByMapName(String mapName) {
		Procedure key = new Procedure();
		key.setMapName(mapName);
		Procedure record = (Procedure) queryForObject(
				"geowfst_procedures.ibatorgenerated_selectByMapName",
				key);
		return record;
	}

	@Override
	public Procedure selectByProcedureType(String procedureType) {
		Procedure key = new Procedure();
		key.setProcedureType(procedureType);
		Procedure record = (Procedure) queryForObject(
				"geowfst_procedures.ibatorgenerated_selectByProcedureType",
				key);
		return record;
	}

	@Override
	public void updateByPrimaryKeySelective(Procedure record) {
		update(
				"geowfst_procedures.ibatorgenerated_updateByPrimaryKeySelective",
				record);
	}

	@Override
	public void updateByPrimaryKey(Procedure record) {
		update(
				"geowfst_procedures.ibatorgenerated_updateByPrimaryKey",
				record);
	}
	
	@Override
    public List<Procedure> selectAll() {
    	List<Procedure> procedures = (List<Procedure>) queryForList("geowfst_procedures.ibatorgenerated_selectAll");
        return procedures;
    }
}