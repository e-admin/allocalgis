package es.satec.localgismobile.session;

import com.japisoft.fastparser.document.Document;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.utils.PropertiesReader;
import es.satec.svgviewer.localgis.sld.AbstractLayer;
import es.satec.svgviewer.localgis.sld.AbstractStyle;
import es.satec.svgviewer.localgis.sld.FeatureTypeStyle;
import es.satec.svgviewer.localgis.sld.Rule;
import es.satec.svgviewer.localgis.sld.StyledLayerDescriptor;
import es.satec.svgviewer.localgis.sld.UserStyle;


import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
public class ProjectInfo {

	private static Logger logger = Global.getLoggerFor(ProjectInfo.class);

	private CellPermissionsBean permisosCeldas;
	private Document document;
	private URL urlGridFile;
	private String path;
	private Hashtable hashSLD;
	private PropertiesReader prWMS;
	private String nombreMapa;
	private String nombreMunicipio;
	private Vector enabledApplications;
	private int srid;
	
	public String getNombreMapa() {
		return nombreMapa;
	}
	public void setNombreMapa(String nombreMapa) {
		this.nombreMapa = nombreMapa;
	}
	public String getNombreMunicipio() {
		return nombreMunicipio;
	}
	public void setNombreMunicipio(String nombreMunicipio) {
		this.nombreMunicipio = nombreMunicipio;
	}
	public Hashtable getHashSLD() {
		return hashSLD;
	}
	public void setHashSLD(Hashtable hashSLD) {
		this.hashSLD = hashSLD;
	}
	public Hashtable getHashSCH() {
		return hashSCH;
	}
	public void setHashSCH(Hashtable hashSCH) {
		this.hashSCH = hashSCH;
	}
	private Hashtable hashSCH;
	
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public URL getUrlGridFile() {
		return urlGridFile;
	}
	public void setUrlGridFile(URL urlGridFile) {
		this.urlGridFile = urlGridFile;
	}
	public String getGridName() {
		return gridName;
	}
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}
	private String gridName;

	private String numFicherosLicencias;

	public CellPermissionsBean getPermisosCeldas() {
		return permisosCeldas;
	}
	public void setPermisosCeldas(CellPermissionsBean permisosCeldas) {
		this.permisosCeldas = permisosCeldas;
	}
	public PropertiesReader getWMSProperties() {
		return prWMS;
	}
	public void setWMSProperties(PropertiesReader prWMS) {
		this.prWMS = prWMS;
	}
	
 	public Vector getEnabledApplications() {
 		return enabledApplications;
 	}
 	
 	public void setEnabledApplications(Vector enabledApplications) {
 		this.enabledApplications = enabledApplications;
 	}
 	
	public int getSrid() {
		return srid;
	}
	public void setSrid(int srid) {
		this.srid = srid;
	}
	public String getNumFicherosLicencias(){
		if (numFicherosLicencias==null)
			return Config.DEFAULT_NUM_FICHEROS_LICENCIAS;
		else
			return numFicherosLicencias;
	}
	public void setNumFicherosLicencias(String numFicherosLicencias) {
		this.numFicherosLicencias=numFicherosLicencias;
		
	}
	
	//Borramos la informacio de SLD que pudiera tener disponible.
	public void dispose() {
		
		// Cargar los estilos y esquemas
		Hashtable hashSLD =getHashSLD();
		Enumeration e = hashSLD.keys();
		while (e.hasMoreElements()) {
			String layer = (String) e.nextElement();
			StyledLayerDescriptor sld = (StyledLayerDescriptor) hashSLD.get(layer);
			if (sld!=null)
				sld.dispose();			
		}
		
	}

 	
}
