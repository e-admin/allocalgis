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
