/**
 * InsertUserStylesAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 22-jul-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.sql.Connection;
import java.util.List;

import es.enxenio.util.exceptions.InternalErrorException;

/**
 * @author enxenio s.l.
 *
 */
public class InsertUserStylesAction {

	private static String sldNS = "http://www.opengis.net/sld";

	private String _layerName;
	private List _userStyles;
	
	public InsertUserStylesAction(String layerName,List userStyles) {
		
		_layerName = layerName;
		_userStyles = userStyles;
	}

	/* (non-Javadoc)
	 * @see es.enxenio.util.sql.PlainAction#execute(java.sql.Connection)
	 */
	public Object execute(Connection connection) throws InternalErrorException {
		try {
			//TODO: This method will update the database to reflect the changes in the SLD style
			//TODO: It is not yet implemented!			
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
		return null;
	}
}
