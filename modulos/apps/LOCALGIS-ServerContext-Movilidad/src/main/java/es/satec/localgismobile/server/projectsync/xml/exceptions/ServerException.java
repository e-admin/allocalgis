/**
 * ServerException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.exceptions;

import es.satec.localgismobile.server.projectsync.xml.ConstantsXMLUpload;

/**
 * <upload2localgisresponse code="2">
 * <errorDescription>
 * 			descripcion del error ...
 * </errorDescription>
 * </upload2localgisresponse>
 * @author irodriguez
 *
 */
public class ServerException extends Exception{
	
	private String serverMsg;

	public ServerException(String serverMsg){
		this.serverMsg = serverMsg;
	}
	
	@Override
	public String getMessage() {
		String msg = "<upload2localgisresponse code=\""+ConstantsXMLUpload.RESPONSE_CODE_SERVER_ERROR+"\">\n";
		msg += "<errorDescription>\n";
		msg += serverMsg + "\n";
		msg += "</errorDescription>\n";
		msg += "</upload2localgisresponse>";
		return msg;
	}
}
