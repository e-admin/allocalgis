package es.satec.localgismobile.server.projectsync.xml.beans;

/**
 * 	<resource type="image">
		<url>http://www.spanien-bilder.com/data/media/56/wappen-real-sporting-de-gijon.gif</url> 
		<body>R0lGODlhVAFHAvd9AO0bI5CPi+jKU+GPEc3Jt82QkvTlqq1laMe1a+dTHO7LzOR0F+WusOjn5drJ</body> 
	</resource>
 * @author irodriguez
 *
 */
public class ResourceXMLUpload {

	private String type;
	private String url;
	private String body;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
	
}
