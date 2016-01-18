package com.geopista.app.reports.parameters;

import java.util.HashSet;
import java.util.Set;

import net.sf.jasperreports.engine.JRParameter;

import com.geopista.app.AppContext;
import com.geopista.app.reports.ReportsContext;

public class GeopistaPredefinedParametersManager {
	
	private AppContext geopistaContext = 
		(AppContext) AppContext.getApplicationContext();  
	
	private HashSet predefinedParameters;
	
	private static GeopistaPredefinedParametersManager instance = null;
	
	protected GeopistaPredefinedParametersManager() {
		predefinedParameters = new HashSet();
		
		predefinedParameters.add("idMunicipio");
		predefinedParameters.add("nombreMunicipio");
		predefinedParameters.add("SUBREPORT_DIR");
	}
	
	public static GeopistaPredefinedParametersManager getInstance(){
		if (instance != null){
			return instance;
		}
		
		instance = new GeopistaPredefinedParametersManager();
		
		return instance;
	}
	
	public Set getGeopistaPredefinedParametersList(){
		return predefinedParameters;
	}
	
	public Object fillGeopistaPredefinedParameter(JRParameter reportParameter){
		String parameterName = reportParameter.getName();
		
		if (parameterName.equalsIgnoreCase("idMunicipio")){
			return fillIdMunicipio();
		}
		else if (parameterName.equalsIgnoreCase("nombreMunicipio")){
			return fillNombreMunicipio();
		}
		else if (parameterName.equalsIgnoreCase("SUBREPORT_DIR")){
			return fillSubreportDir();
		}
		else {
			return null;
		}
	}
	
	private Integer fillIdMunicipio(){
		return new Integer(geopistaContext.getString("geopista.DefaultCityId"));
	}
	
	private String fillNombreMunicipio(){
		return "TODO: MUNICIPIO";
	}
	
	private String fillSubreportDir(){
		return ReportsContext.getInstance().getBaseCompiledReportsPath();
	}

	public boolean isGeopistaPredefinedParameter(JRParameter parameter) {
		return predefinedParameters.contains(parameter.getName());
	}
}
