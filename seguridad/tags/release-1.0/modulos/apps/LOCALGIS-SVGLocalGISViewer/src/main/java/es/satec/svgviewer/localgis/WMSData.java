package es.satec.svgviewer.localgis;

/**
 * Datos básicos de un servidor de mapas.
 * @author jpresa
 */
public class WMSData {

	private String baseURL;
	private String version;
	private String layers;
	private String styles;
	private String srs;
	private String format;
	
	private boolean active;
	
	public WMSData() {
	}
	
	/**
	 * @param baseURL
	 * @param version
	 * @param layers
	 * @param styles
	 * @param srs
	 * @param format
	 * @param active
	 */
	public WMSData(String baseURL, String version, String layers, String styles, String srs, String format, boolean active) {
		this.baseURL = baseURL;
		this.version = version;
		this.layers = layers;
		this.styles = styles;
		this.srs = srs;
		this.format = format;
		this.active = active;
	}

	/**
	 * @param baseURL
	 * @param version
	 * @param layers
	 * @param styles
	 * @param srs
	 * @param format
	 */
	public WMSData(String baseURL, String version, String layers, String styles, String srs, String format) {
		this(baseURL, version, layers, styles, srs, format, false);
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getLayers() {
		return layers;
	}

	public void setLayers(String layers) {
		this.layers = layers;
	}

	public String getSrs() {
		return srs;
	}

	public void setSrs(String srs) {
		this.srs = srs;
	}

	public String getStyles() {
		return styles;
	}

	public void setStyles(String styles) {
		this.styles = styles;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public String toString() {
		return "Base URL: " + baseURL + ", version: " + version + ", layers: " + layers +
			", styles: " + styles + ", srs: " + srs + ", format: " + format + ", active: " + active;
	}
}
