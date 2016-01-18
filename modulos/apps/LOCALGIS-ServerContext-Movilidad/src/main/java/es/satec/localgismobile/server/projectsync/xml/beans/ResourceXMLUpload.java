/**
 * ResourceXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
