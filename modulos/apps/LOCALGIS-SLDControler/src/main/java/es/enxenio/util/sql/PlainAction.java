/**
 * PlainAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.enxenio.util.sql;

import java.sql.Connection;

import es.enxenio.util.exceptions.InternalErrorException;
import es.enxenio.util.exceptions.ModelException;

/**
 * The base interface of all plain actions. A plain action is an bussiness
 * action executed with plain Java classes, using JDBC as database accesss
 * technology.
 */ 
public interface PlainAction {

	Object execute(Connection connection) throws ModelException,
		InternalErrorException;

}
