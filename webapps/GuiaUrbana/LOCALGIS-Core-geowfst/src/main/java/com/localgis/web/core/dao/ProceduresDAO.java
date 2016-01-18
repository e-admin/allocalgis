/**
 * ProceduresDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.localgis.web.core.model.Procedure;

public interface ProceduresDAO {
	
	void deleteByPrimaryKey(String id);
	void insert(Procedure record);
	void insertSelective(Procedure record);
	Procedure selectByPrimaryKey(String id);
	Procedure selectByLayerName(String layerName);
	Procedure selectByMapName(String mapName);
	Procedure selectByProcedureType(String procedureType);
	void updateByPrimaryKeySelective(Procedure record);
	void updateByPrimaryKey(Procedure record);
	List<Procedure> selectAll();
	
}