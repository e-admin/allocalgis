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
