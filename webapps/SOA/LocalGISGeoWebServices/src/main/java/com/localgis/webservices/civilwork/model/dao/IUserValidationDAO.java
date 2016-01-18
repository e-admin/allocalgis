/**
 * IUserValidationDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.localgis.webservices.civilwork.util.LocalGISUser;


public interface IUserValidationDAO
{
	
	public Integer getIdAclNotes(Connection connection) throws SQLException;
	
	public Integer getIdPermFromName(Connection connection,String description) throws SQLException;
	
	public boolean login(Connection connection,Integer userId) throws SQLException;
	
	public boolean administration(Connection connection,Integer userId) throws SQLException;
	
	public boolean notesQuery(Connection connection,Integer userId) throws SQLException;
	
	public boolean notesNew(Connection connection,Integer userId) throws SQLException;
	
	public boolean notesDelete(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsQuery(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsNew(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsModify(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsPostpone(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsDiscard(Connection connection,Integer userId) throws SQLException;

	public boolean warningsDelete(Connection connection,Integer userId) throws SQLException;
	
	public boolean planesObraQuery(Connection connection,Integer userId) throws SQLException;
	
	public boolean planesObraNew(Connection connection,Integer userId) throws SQLException;
	
	public boolean planesObraDelete(Connection connection,Integer userId) throws SQLException;
	
	public ArrayList<LocalGISUser> getAdminUserList(Connection connection,Integer idMunicipio) throws SQLException;
	
	public ArrayList<LocalGISUser> getUser(Connection connection,Integer idMunicipio,Integer userId) throws SQLException;
	
}


