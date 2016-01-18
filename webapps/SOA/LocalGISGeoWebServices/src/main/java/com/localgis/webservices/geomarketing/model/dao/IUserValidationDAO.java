/**
 * IUserValidationDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing.model.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.geopista.server.administradorCartografia.ACException;


public interface IUserValidationDAO
{
	public boolean obtenerDatosGenerales(Connection connection,Integer userId) throws SQLException;
	public boolean obtenerDatosGeomarketing(Connection connection,Integer userId) throws SQLException;
	public boolean readLayer(Connection connection,Integer userId,Integer idLayer)throws SQLException;
	public boolean hasPerm(Connection connection,Integer userId,Integer idAcl,Integer idPerm) throws SQLException;
	public Integer getAclIdGeomarketing(Connection connection) throws SQLException;
	public Integer getIdPerm(Connection connection,String permName) throws SQLException;
	public Integer getUserId(Connection connection,String userName,Integer idMunicipio) throws SQLException;
	public Integer getUserId(Connection connection,String userName) throws SQLException;
	public Integer getIdAclFromIdLayer(Connection connection, Integer idLayer) throws SQLException;
	public Integer getActiveAndValidatedUserId(Connection connection,String userName,String password) throws SQLException, ACException;
}


