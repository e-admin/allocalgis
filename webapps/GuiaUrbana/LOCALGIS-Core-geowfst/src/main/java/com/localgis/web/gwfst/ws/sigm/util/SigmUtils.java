/**
 * SigmUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.sigm.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.localgis.web.core.LocalgisManagerBuilderGeoWfst;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.gwfst.util.LocalgisManagerBuilderSingletonFeature;
import com.localgis.web.core.manager.LocalgisGeoFeatureManager;
import com.localgis.web.core.model.ProcedureProperty;
import com.localgis.web.core.model.Procedure;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndName;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndValue;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Utilidades para el Web Service de Sigm
 */
public class SigmUtils {

	/**
	 * Logger
	 */
	private static Log logger = LogFactory.getLog(SigmUtils.class);
	
    /**
	 * Recupera un array con las propiedades configuradas en el XML 
	 * @param propertyAndNameXMLPath: Ruta del XML de configuración de propiedades
	 * @return PropertyAndName []: Array de PropertyAndName
	 */
//	public static PropertyAndName [] getPropertyAndNameFromXML(
//			String propertyAndNameXMLPath) {
//		PropertyAndName[] propertyAndName = null;
//		try {
//			Element xmlFeature = getRootXMLElementByPath(propertyAndNameXMLPath);
//			if(xmlFeature != null){
//				List<Element> propertyAndNameList = xmlFeature.getChildren("propertyandname");
//				if (propertyAndNameList.size() > 0) {
//					propertyAndName = new PropertyAndName[propertyAndNameList
//							.size()];
//					int j = 0;
//					Iterator<Element> propertyAndNameListIt = propertyAndNameList.iterator();
//					while (propertyAndNameListIt.hasNext()) {
//						Element e = propertyAndNameListIt.next();
//						propertyAndName[j] = new PropertyAndName(
//								e.getChildText("grouptitle"),
//								e.getChildText("property"), 
//								e.getChildText("name"),
//								e.getChildText("type"),
//								new Boolean(e.getChildText("searchactive")),
//								new Boolean(e.getChildText("active")));
//						j++;
//					}
//				}
//			}
//			else propertyAndName = new PropertyAndName [0];
//		} catch (Exception e) {
//			logger.error("getPropertyAndNameFromXML() ERROR:" + e.getMessage());
//		}
//		return propertyAndName;
//	}
	
    /**
	 * Recupera un array con las propiedades configuradas en el XML 
	 * @param propertyAndNameXMLPath: Ruta del XML de configuración de propiedades
	 * @return PropertyAndName []: Array de PropertyAndName
     * @throws LocalgisConfigurationException 
     * @throws LocalgisInitiationException 
	 */
	public static PropertyAndName [] getPropertyAndName(String procedureType) throws LocalgisInitiationException, LocalgisConfigurationException{
		PropertyAndName [] propertyAndName = null;
		LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
		LocalgisGeoFeatureManager localgisGeoFeatureManager = localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager();      
		Procedure procedures = localgisGeoFeatureManager.getProceduresByProcedureType(procedureType);
		if(procedures != null && procedures.getId() != null){
			List<ProcedureProperty> procedurePropertiesList = localgisGeoFeatureManager.getProcedureProperties(procedures.getId());
			if(procedurePropertiesList!=null && procedurePropertiesList.size()>0){
				Iterator<ProcedureProperty> iterator = procedurePropertiesList.iterator();
				propertyAndName = new PropertyAndName[procedurePropertiesList.size()];
				PropertyAndName propertyAndNameTemp = null;
				int i=0;
				while(iterator.hasNext()){
					propertyAndNameTemp = new PropertyAndName();
					ProcedureProperty procedureProperties = iterator.next();
					if(procedureProperties.getGrouptitle()!=null)
						propertyAndNameTemp.setGroupTitle(procedureProperties.getGrouptitle());
					if(procedureProperties.getProperty()!=null)
						propertyAndNameTemp.setProperty(procedureProperties.getProperty());
					if(procedureProperties.getName()!=null)
						propertyAndNameTemp.setName(procedureProperties.getName());
					if(procedureProperties.getType()!=null)
						propertyAndNameTemp.setType(procedureProperties.getType());
					if(procedureProperties.getSearchactive()!=null)
						propertyAndNameTemp.setSearchActive(procedureProperties.getSearchactive());
					if(procedureProperties.getActive()!=null)
						propertyAndNameTemp.setActive(procedureProperties.getActive());		
					propertyAndName[i] = propertyAndNameTemp;
					i++;
				}
			}
		}
		return propertyAndName;
	}
	
    /**
	 * Recupera
	 * @param propertyAndValueHashMap: HashMap con las propiedades y sus valores
	 * @return PropertyAndName []: Array de PropertyAndName
	 */
	public static PropertyAndValue [] getPropertyAndValueFromProcedures(
			HashMap<String, String> propertyAndValueHashMap) {
		ArrayList<PropertyAndValue> propertyAndValueList = new ArrayList<PropertyAndValue>();
		String procedureName = null;
		PropertyAndValue propertyAndValue = null;
		try {
			Iterator<String> propertyAndValueHashMapIt = propertyAndValueHashMap.keySet().iterator();
			while(propertyAndValueHashMapIt.hasNext()){
				procedureName = propertyAndValueHashMapIt.next();
				Element xmlFeature = getRootXMLElementByXML(propertyAndValueHashMap.get(procedureName));
				if(xmlFeature != null){
					List<Element> listItems = xmlFeature.getChildren("item");
					if(listItems.size() > 0){
						int j = 0;
						Iterator<Element> listItemsIt = listItems.iterator();
						while(listItemsIt.hasNext()){
							List<Element> listValues = listItemsIt.next().getChildren("value");
							if (listValues.size() > 0) {													
								Iterator<Element> listValuesIt = listValues.iterator();
								while (listValuesIt.hasNext()) {
									Element e = listValuesIt.next();
									propertyAndValue = new PropertyAndValue("", procedureName + "." + e.getAttributeValue("name"), null);
									propertyAndValueList.add(propertyAndValue);
									if(e.getContent() != null && e.getContent().size() > 0)
										propertyAndValue.setValue(((CDATA) e.getContent().get(0)).getValue());
									j++;
								}
							}
						}
					}
				}			
			}
		} catch (Exception e) {
			logger.error("getPropertyAndNameFromXML() ERROR:" + e.getMessage());
		}
		return propertyAndValueList.toArray(new PropertyAndValue[propertyAndValueList.size()]);
	}
	
    /**
	 * Recupera un array con las propiedades configuradas en el XML 
	 * @param propertyAndValueHashMap: HashMap con las propiedades y sus valores
	 * @return PropertyAndName []: Array de PropertyAndName
	 */
	public static String getSearchXML(PropertyAndValue [] searchPropertyAndValues, PropertyAndName [] propertyAndNames){	
		String generalProperty = null;
		String property = null;
		String operator = null;
		String procedureName = null;		
		PropertyAndName propertyAndName = null;
		String searchXML = "<search>";
		for(int i = 0; i<searchPropertyAndValues.length;i++){
			generalProperty = searchPropertyAndValues[i].getProperty();			
			if(procedureName == null || !procedureName.equals(generalProperty.substring(0, generalProperty.indexOf(".")))){
				if(procedureName != null)
					searchXML += "</entity>";
				procedureName = generalProperty.substring(0, generalProperty.indexOf("."));			
				searchXML += "<entity name=\"" + procedureName.toLowerCase() + "\">";
			}
			property = generalProperty.substring(generalProperty.indexOf(".")+1, generalProperty.length());	
			propertyAndName = getPropertyAndNameByProperty(propertyAndNames, generalProperty);
			if(propertyAndName != null && propertyAndName.getType().equals("string"))
				operator = "Contiene(Like)";
			else
				operator = "=";
			searchXML += "<field>" +
	        "<name>" + property + "</name>" +			
	        "<operator>" + operator + "</operator>" +            
	        "<value>" + searchPropertyAndValues[i].getValue() + "</value>" +
	        "</field>";
		}
		searchXML += "</entity></search>";
		return searchXML;
	}
	
    /**
	 * Recupera el mapa y los atributos necesarias para el visor
	 * @param mapping: Mapeo de la acción
	 * @param form: Formulario asociado a la acción
	 * @param request: Objeto petición de la acción
	 * @param response: Objeto respuesta de la acción
	 * @exception Exception
	 * @throws ActionForward: Devuelve la siguiente acción
	 */
	public static String [] getSearchIdFeatures(String idFeaturesXML){
		String [] id_features = null;
		try {
			Element xmlFeature = getRootXMLElementByXML(idFeaturesXML);
			if(xmlFeature != null){
				List<Element> listItems = xmlFeature.getChildren("item");
				if (listItems.size() > 0) {
					id_features = new String[listItems.size()];
					int j = 0;
					Iterator<Element> listItemsIt = listItems.iterator();
					while (listItemsIt.hasNext()) {
						List<Element> listValues = listItemsIt.next().getChildren("value");
						if (listValues.size() > 0) {
							Iterator<Element> listValuesIt = listValues.iterator();
							while (listValuesIt.hasNext()) {
								Element e = (Element) listValuesIt.next();
								if(e.getContent()!= null && e.getContent().size()>0){
									id_features[j] = ((CDATA) e.getContent().get(0)).getValue();
									j++;
								}
							}
						}
					}
				}
			}
			else id_features = new String [0];
		} catch (Exception e) {
			logger.error("getSearchIdFeatures() ERROR:" + e);
		}		
		return id_features;
	}
	
    /**
	 * Recupera el mapa y los atributos necesarias para el visor
	 * @param mapping: Mapeo de la acción
	 * @param form: Formulario asociado a la acción
	 * @param request: Objeto petición de la acción
	 * @param response: Objeto respuesta de la acción
	 * @exception Exception
	 * @throws ActionForward: Devuelve la siguiente acción
	 */
	public static PropertyAndName getPropertyAndNameByProperty(PropertyAndName [] propertyAndNames, String property){
		if(propertyAndNames != null && propertyAndNames.length > 0){
			for(int i=0;i<propertyAndNames.length;i++){
				if(propertyAndNames[i].getProperty().equals(property))
					return propertyAndNames[i];
			}
		}
		return null;
	}
	
    /**
	 * Recupera el mapa y los atributos necesarias para el visor
	 * @param mapping: Mapeo de la acción
	 * @param form: Formulario asociado a la acción
	 * @param request: Objeto petición de la acción
	 * @param response: Objeto respuesta de la acción
	 * @exception Exception
	 * @throws ActionForward: Devuelve la siguiente acción
	 */
	public static Element getRootXMLElementByPath(String propertyAndNameXMLPath) throws JDOMException, IOException{
		return (new SAXBuilder(false).build(SigmUtils.class.getResourceAsStream(propertyAndNameXMLPath))).getRootElement();		
	}
		
    /**
	 * Recupera el mapa y los atributos necesarias para el visor
	 * @param mapping: Mapeo de la acción
	 * @param form: Formulario asociado a la acción
	 * @param request: Objeto petición de la acción
	 * @param response: Objeto respuesta de la acción
	 * @exception Exception
	 * @throws ActionForward: Devuelve la siguiente acción
	 */
	public static Element getRootXMLElementByXML(String xml) throws JDOMException, IOException{
		return (new SAXBuilder().build(new StringReader(xml))).getRootElement();			
	}
	
}
