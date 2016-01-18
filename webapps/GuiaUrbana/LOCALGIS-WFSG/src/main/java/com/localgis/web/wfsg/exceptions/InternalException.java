/**
 * InternalException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg.exceptions;

/**
 * Internal Exception
 *
 * Exception thrown when an Internal Exception occurs 
 * 
 * @author SATEC Framework Team
 * @since 01.12.2007
 * @version 01.00.00
 */
public class InternalException extends BaseApplicationException {

	private static final long serialVersionUID = 4026509038606279175L;
	public final static String ERROR_CODE = "Internal Error";
	
	public InternalException(String sMessage) {
		super(ERROR_CODE, sMessage);
	}
}
