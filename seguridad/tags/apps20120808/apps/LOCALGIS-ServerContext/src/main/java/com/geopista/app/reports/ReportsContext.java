package com.geopista.app.reports;

import java.io.File;

import com.geopista.app.AppContext;

public class ReportsContext {

	private static AppContext geopistaContext;  
	
	private static ReportsContext instance = null;
	
	protected ReportsContext(){
		geopistaContext = (AppContext) AppContext.getApplicationContext();
	}
	
	public static ReportsContext getInstance(){
		if (instance != null){
			return instance;
		}
		
		instance = new ReportsContext();
		
		return instance;
	}
	
	public String getBaseReportsPath(String idAppType){
		AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  

		String localPath = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH, true);

		String baseReportsPath = localPath + File.separator + ReportsConstants.REPORTS_DIR + File.separator;
		
		if (idAppType!=null){
			baseReportsPath = baseReportsPath+idAppType;
		}
		return baseReportsPath;
	}
	
	public String getBaseReportsPath(){
		return getBaseReportsPath(null);
	}
	
	public String getBaseCompiledReportsPath(String idAppType){
		AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
		String localPath = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH, true);

		String baseReportsPath = localPath + File.separator +
			ReportsConstants.COMPILED_REPORTS_DIR + File.separator;
		if (idAppType!=null){
			baseReportsPath = baseReportsPath+idAppType;
		}
		return baseReportsPath;
	}
	
	public String getBaseCompiledReportsPath(){
		return getBaseCompiledReportsPath(null);
	}
}
