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
