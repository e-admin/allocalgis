/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geopista.reports.parameters;

import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.JRProperty;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.gui.MainFrame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author jpolo
 */
public class IReportParameterManager {
	
    public static final String MAP_PARAMETER_CLASS_TYPE = "java.lang.String";
    public static final String MAP_FIELD_CLASS_TYPE = "java.lang.String";    
    public static final String IS_MAP_PROPERTY = "isMap";    
    public static final String MAP_NAME_PROPERTY = "mapName";
    
    public static Vector getAvailableParameters(){
        SubDataset subDataset = MainFrame.getMainInstance().getActiveReportFrame().getReport();
        Vector availableParameters = subDataset.getParameters();
        if (availableParameters == null){
            availableParameters = new Vector();
        }

        return availableParameters;
    }
    
    public static Vector getAvailableFields(){
        SubDataset subDataset = MainFrame.getMainInstance().getActiveReportFrame().getReport();
        Vector availableFields = subDataset.getFields();
        if (availableFields == null){
            availableFields = new Vector();
        }

        return availableFields;
    }

    public static Vector getAvailableMapIdParameters(){
        Vector availableParameters = getAvailableParameters();
        if (availableParameters.size() == 0){
            return availableParameters;
        }

        Vector availableMapParameters = new Vector();
        int numberOfParameters = availableParameters.size();
        for (int i = 0; i < numberOfParameters; i++){
            JRParameter parameter = (JRParameter) availableParameters.get(i);
            List properties = parameter.getProperties();
            if (parameter.getClassType().equals(MAP_PARAMETER_CLASS_TYPE) &&
                    properties != null && properties.size() != 0){
            	for (int j = 0; j < properties.size(); j++){
            		JRProperty property = (JRProperty) properties.get(j);
            		if (property.getName().equals(IS_MAP_PROPERTY)){
            			availableMapParameters.add(parameter);
            		}
            	}
            }            
        }

        return availableMapParameters;
    }
    
    public static Vector getAvailableMapIdFields(){
    	Vector availableFields = getAvailableFields();
    	
    	if (availableFields.size() == 0){
    		return availableFields;
    	}
    	
    	Vector availableMapFields = new Vector();
    	int numberOfFielfs = availableFields.size();
    	for (int i = 0; i < availableFields.size(); i++){
    		JRField field = (JRField) availableFields.get(i);
    		if (field.getClassType().equals(MAP_FIELD_CLASS_TYPE)){
    			availableMapFields.add(field);
    		}
    	}
    	
    	return availableMapFields;
    }
    
    public static Vector getAvailableStringParameters(){
    	Vector availableParameters = getAvailableParameters();
        if (availableParameters.size() == 0){
            return availableParameters;
        }

        Vector availableStringParameters = new Vector();
        int numberOfParameters = availableParameters.size();
        for (int i = 0; i < numberOfParameters; i++){
            JRParameter parameter = (JRParameter) availableParameters.get(i);            
            if (parameter.getClassType().equals(String.class.getName())){
            	availableStringParameters.add(parameter);
            }            
        }

        return availableStringParameters;
    }

    public static void addMapParameter(String name){
        SubDataset subDataset = MainFrame.getMainInstance().getActiveReportFrame().getReport();
        JRParameter parameter = newMapParameter(name);
        subDataset.addParameter(parameter);        
    }
   
    public static boolean isMapImageExpression(String imageExpression, String expressionClassType) {
        if (!expressionClassType.equals(MAP_PARAMETER_CLASS_TYPE)) {
            return false;
        }

        if (imageExpression.startsWith("com.geopista")) {
            return true;
        }
        return false;
    }

    private static JRParameter newMapParameter(String name){
    	String parameterName = name;
    	
    	JRParameter parameter = new JRParameter(parameterName, MAP_PARAMETER_CLASS_TYPE,
                false);    	
   
        String isMap = "true";
    	String mapName = name;  
        
        ArrayList properties = new ArrayList();
        
        JRProperty mapNameProperty = new JRProperty();
        mapNameProperty.setName(MAP_NAME_PROPERTY);
        mapNameProperty.setValue(mapName);
        properties.add(mapNameProperty);
        
        JRProperty isMapProperty = new JRProperty();
        isMapProperty.setName(IS_MAP_PROPERTY);
        isMapProperty.setValue(isMap);
        properties.add(isMapProperty);
        
        parameter.setProperties(properties);
           
    	return parameter;
    }
    
    public static void addDefaultGeopistaParameters(Report report){
    	JRParameter idMunicipioParameter = 	new JRParameter("ID_ENTIDAD", String.class.getName());   
    	Iterator it =report.getParameters().iterator();
    	while (it.hasNext()){
    		JRParameter parameter=(JRParameter)it.next();
    		if (parameter.getName().equals("ID_ENTIDAD"))
    			return;
    	}
    	System.out.println("Adding ID_ENTIDAD Parameter");
   		report.addParameter(idMunicipioParameter);
    }

    public static String getQuery(){
        SubDataset subDataset = MainFrame.getMainInstance().getActiveReportFrame().getReport();
        return subDataset.getQuery();
    }
}
