/**
 * ReportsContext.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;

import java.io.File;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.config.UserPreferenceStore;

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

		String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);

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
		String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);

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
