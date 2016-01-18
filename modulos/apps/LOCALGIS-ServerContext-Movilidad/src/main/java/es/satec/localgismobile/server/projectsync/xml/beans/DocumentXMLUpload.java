/**
 * DocumentXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.beans;

public class DocumentXMLUpload {

	private SvgXMLUpload svg;
	private ResourcesXMLUpload resources;
	private MetainfoXMLUpload metainfo;
	private String fixedHeader;
	private String fixedFooter;
	private int srid;
	
	public int getSrid() {
		return srid;
	}
	public void setSrid(int srid) {
		this.srid = srid;
	}
	public SvgXMLUpload getSvg() {
		return svg;
	}
	public void setSvg(SvgXMLUpload svg) {
		this.svg = svg;
	}
	public ResourcesXMLUpload getResources() {
		return resources;
	}
	public void setResources(ResourcesXMLUpload resources) {
		this.resources = resources;
	}
	public String getFixedHeader() {
		return fixedHeader;
	}
	public void setFixedHeader(String fixedHeader) {
		this.fixedHeader = fixedHeader;
	}
	public String getFixedFooter() {
		return fixedFooter;
	}
	public void setFixedFooter(String fixedFooter) {
		this.fixedFooter = fixedFooter;
	}
	public MetainfoXMLUpload getMetainfo() {
		return metainfo;
	}
	public void setMetainfo(MetainfoXMLUpload metainfo) {
		this.metainfo = metainfo;
	}	
	
}
