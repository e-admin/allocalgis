/**
 * FeatureException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.exceptions;

import es.satec.localgismobile.server.projectsync.xml.ConstantsXMLUpload;

/**
 *         <upload2localgisresponse code="1"> 
            <![CDATA[
            <svg> 
                 <g id="errors" l1="errorDescription"> 
                      <path id="3434" v1 ="..." /> 
                </g>
            </svg> 
             ]] 
        </upload2localgisresponse>
 * @author irodriguez
 *
 */
public class FeatureException extends Exception{

	private String featuresMsg;
	private String results;
	private boolean permissionException;

	public FeatureException(String featuresMsg, String results,boolean permissionException){
		this.featuresMsg = featuresMsg;
		this.results = results;
		this.permissionException=permissionException;
	}
	
	@Override
	public String getMessage() {
		String msg="";
		
		//TODO Control mas fino de los permisos
		if (permissionException)
			msg = "<upload2localgisresponse code=\""+ConstantsXMLUpload.RESPONSE_CODE_FEATURES_ERROR+"\">\n";
		else
			msg = "<upload2localgisresponse code=\""+ConstantsXMLUpload.RESPONSE_CODE_FEATURES_ERROR+"\">\n";
		msg += "<![CDATA[\n";
		msg += "<svg>\n";
		msg += "<g id=\"errors\" fill=\"red\" stroke=\"red\" l1=\"errorDescription\">\n";
		msg += featuresMsg;
		msg += "</g>\n";
		msg += "</svg>\n"; 
		msg += "]]>\n";
		msg += results+"\n";
		msg += "</upload2localgisresponse>";
		return msg;
	}

}
