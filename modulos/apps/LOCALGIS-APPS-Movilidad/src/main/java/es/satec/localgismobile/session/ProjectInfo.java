/**
 * ProjectInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.session;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.japisoft.fastparser.document.Document;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.utils.PropertiesReader;
import es.satec.svgviewer.localgis.sld.StyledLayerDescriptor;
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
