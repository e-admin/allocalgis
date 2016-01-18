/**
 * SessionInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.session;

import java.util.Vector;

import es.satec.localgismobile.fw.validation.bean.ValidationBean;
import es.satec.svgviewer.localgis.GPSProvider;

public class SessionInfo {
	
	private static SessionInfo session;
	
	private Vector currentDefinedSearchs = new Vector();
	private ProjectInfo projectInfo = null;
	private CellInfo cellInfo;
	private ValidationBean validationBean;
	
	private GPSProvider gpsProvider;
 
	public static SessionInfo getInstance() {
		
		if (session == null) {
			session = new SessionInfo();
		}
		return session;
	}
	
	public void setCurrentDefinedSearchs(Vector currentDefinedSearchs) {
		this.currentDefinedSearchs = currentDefinedSearchs;
	}
 	
	public Vector getCurrentDefinedSearchs(){
		return currentDefinedSearchs;
	}
	
	public void setProjectInfo (ProjectInfo projectInf) {
		projectInfo = projectInf;
	}

	public  ProjectInfo getProjectInfo() {
		
		return projectInfo;
	}

	public CellInfo getCellInfo() {
		return cellInfo;
	}
 
	public void setCellInfo(CellInfo cellInfo) {
		this.cellInfo = cellInfo;
	}
	
	public GPSProvider getGPSProvider() {
		return gpsProvider;
	}
	
	public void setGPSProvider(GPSProvider gpsProvider) {
		this.gpsProvider = gpsProvider;
	}

	public ValidationBean getValidationBean() {
		return validationBean;
	}
	
	public void setValidationBean(ValidationBean validationBean) {
		this.validationBean = validationBean;
	}
	
}
