/**
 * NoDataFoundException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg.exceptions;

/**
 * No Data Found Exception
 *
 * Exception thrown when no data has been found for the searching criteria
 * 
 * @author SATEC Framework Team
 * @since 01.12.2007
 * @version 01.00.00
 */
public class NoDataFoundException extends BaseApplicationException {

	/**
	 * This exception is thrown when no data is found matching the searching criteria
	 */
	private static final long serialVersionUID = -7406598921236053836L;

	public final static String ERROR_CODE = "No Data Found";
	
	public NoDataFoundException() {
		super(ERROR_CODE, "No Data has been found matching the searching criteria.  ");
	}
}
