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
