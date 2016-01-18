/**
 * LocalGIS_WFSG_XML_Analysis.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.localgis.web.wfsg.constants.Constants;
import com.localgis.web.wfsg.domain.ElementEntity;
import com.localgis.web.wfsg.domain.Entity;
import com.localgis.web.wfsg.domain.EntityField;
import com.localgis.web.wfsg.exceptions.IncorrectArgumentException;
import com.localgis.web.wfsg.exceptions.InternalException;


public class LocalGIS_WFSG_XML_Analysis {

    protected static Logger logger = Logger.getLogger(LocalGIS_WFSG_XML_Analysis.class);
 
	protected static LocalGIS_WFSG_Resultados getCapabilities (String sXMLBody, LocalGIS_WFSG_Resultados lwr_Resultados) throws Exception, IncorrectArgumentException {
		
		
		//sXMLBody=sXMLBody.replaceAll("xmlns", "wfs-mne");
		//StringReader sr = new StringReader(sXMLBody);
		//SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		String sEntityName = null;
		int iIndex;
		
		/*try {
			//builder.setFeature("http://xml.org/sax/features/namespaces", false);
			//builder.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
			//builder.setFeature("http://apache.org/xml/features/validation/schema",false);
			//builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking",false);
			doc = builder.build(sr);
		} catch (JDOMException e) {
			throw new Exception("LocalGIS_WFSG_XML_Analysis getCapabilities(): cannot process XML Style:"+e.toString());
		} catch (IOException e) {
			throw new Exception("LocalGIS_WFSG_XML_Analysis getCapabilities(): Error while processing XML Style:"+e.toString());
	    }
		catch (Exception e){
			throw new Exception("LocalGIS_WFSG_XML_Analysis getCapabilities(): Error while processing XML :"+e.toString());
		}
		
		*/
		
		org.w3c.dom.Document docw3c = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);


		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			docw3c=db.parse(new InputSource(new StringReader(sXMLBody)));
			
			DOMBuilder dombuilder = new DOMBuilder();
			doc=dombuilder.build(docw3c);
			
		}
		catch (Exception e){
			throw new Exception("LocalGIS_WFSG_XML_Analysis getCapabilities(): DOMPARSER Error while processing XML :"+e.toString());
		}	
		
		try {
		
			Element sldDocRoot = doc.getRootElement();
			Element myOperations = null;
			Element myFeatureTypes = null;
			List arlTags = sldDocRoot.getChildren();
			Iterator itTag = arlTags.iterator();
			
			/** If server is responding with a Service Report exception, we 
			 * transmit it as an Incorrect Argument Exception
			 */
			if (sXMLBody.indexOf(Constants.SERVICE_EXCEPTION_REPORT)!=-1){
				throw new IncorrectArgumentException("URL Server");
			}
			
			logger.info("execution of LocalGIS_WFSG_Resultados getCapabilities");
			while (itTag.hasNext()){
				Element anElement = (Element)itTag.next();
				
				if (anElement.getName().equals(Constants.TAG_OPERATION_METADATA)){
					logger.info("LocalGIS_WFSG_XML_Analysis getCapabilities(): Operation Metadata found");
					myOperations = anElement;
					List arlOperations = myOperations.getChildren(Constants.TAG_OPERATION, myOperations.getNamespace());

					Iterator operationIterator = arlOperations.iterator();
		    		while (operationIterator.hasNext()){
		    			Element operationElement = (Element)operationIterator.next();
		    			Attribute anOperationName = operationElement.getAttribute(Constants.ATT_NAME);
		    			lwr_Resultados.addOperation(anOperationName.getValue());
		    		}
				}
				
				/** processing feature type */
				if (anElement.getName().equals(Constants.TAG_FEATURE_TYPE_LIST)){
					myFeatureTypes = anElement;
					List arlFeatureTypes = myFeatureTypes.getChildren(Constants.TAG_FEATURE_TYPE, myFeatureTypes.getNamespace());
					Iterator itFeatureTypes = arlFeatureTypes.iterator();
					while (itFeatureTypes.hasNext()){
						
						Element aFeatureTypeElement = (Element) itFeatureTypes.next();
						Entity anEntity = new Entity();
						Element aFeatureTypeNameElement = aFeatureTypeElement.getChild(Constants.TAG_NAME, aFeatureTypeElement.getNamespace());
						if (aFeatureTypeNameElement!=null){
							sEntityName = aFeatureTypeNameElement.getText();
							anEntity.setName(sEntityName);
						}
						
						Element aFeatureTypeSRSElement = null;
						if (lwr_Resultados.getStyle()==Constants.STYLE_IDEE && lwr_Resultados.getSRS()==null){
							aFeatureTypeSRSElement = aFeatureTypeElement.getChild(Constants.TAG_IDEE_SRS, aFeatureTypeElement.getNamespace());
						}else if ((lwr_Resultados.getStyle()==Constants.STYLE_STANDARD) && lwr_Resultados.getSRS()==null){
							aFeatureTypeSRSElement = aFeatureTypeElement.getChild(Constants.TAG_STANDARD_SRS, aFeatureTypeElement.getNamespace());
						}else if (lwr_Resultados.getStyle()==Constants.STYLE_PRUEBA && lwr_Resultados.getSRS()==null && sEntityName!=null){
							if (sEntityName.equals(Constants.SEARCHED_ENTITY_PRUEBA)){
								aFeatureTypeSRSElement = aFeatureTypeElement.getChild(Constants.TAG_STANDARD_SRS, aFeatureTypeElement.getNamespace());
							}
						}
						if (aFeatureTypeSRSElement!=null){
							String sSRS = aFeatureTypeSRSElement.getText();
							iIndex = sSRS.indexOf(Constants.COLON);
							sSRS = sSRS.substring((iIndex+1), sSRS.length());
							logger.info("LocalGIS_WFSG_XML_Analysis (getCapabilities) SRS equals: " + sSRS);
							lwr_Resultados.setSRS(sSRS);
						}
						lwr_Resultados.addEntityTypes(anEntity);
					}
				}
			}
			return lwr_Resultados;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	protected static Entity getDescribeFeatureType (String sXMLBody, String sEntityName, LocalGIS_WFSG_Resultados lwr_Resultados) 
	throws IncorrectArgumentException, Exception {
		StringReader sr = new StringReader(sXMLBody);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		Entity myEntity = null;
		
		try {
			doc = builder.build(sr);
		} catch (JDOMException e) {
			throw new Exception("LocalGIS_WFSG_XML_Analysis getDescribeFeatureType(): cannot process XML Style");
		} catch (IOException e) {
			throw new Exception("LocalGIS_WFSG_XML_Analysis getDescribeFeatureType(): Error while processing XML Style");
	    }
		
		try {
		
			Element sldDocRoot = doc.getRootElement();
			Element myElement = null;
			Namespace myNamespace = sldDocRoot.getNamespace();
			String sEntityTypeName = "";
			String sComplexTypeName;
			int iIndex;
			
			
			/** If server is responding with a Service Report exception, we 
			 * transmit it as an Incorrect Argument Exception
			 */
			if (sXMLBody.indexOf(Constants.SERVICE_EXCEPTION_REPORT)!=-1){
				throw new IncorrectArgumentException("URL Server");
			}
			
			String sEntityNameCleaned = sEntityName;
    		iIndex = sEntityName.indexOf(Constants.COLON);
    		if (iIndex>=0){
    			sEntityNameCleaned = sEntityName.substring((iIndex+1), sEntityName.length());
    		}
			
			List arlElements = sldDocRoot.getChildren(Constants.TAG_ELEMENT, myNamespace);
			Iterator itElements = arlElements.iterator();
			
			
			while (itElements.hasNext()){
				
				Element anElement = (Element)itElements.next();
				if (anElement!=null && anElement.getAttribute(Constants.ATT_NAME)!=null && anElement.getAttribute(Constants.ATT_NAME).getValue().equals(sEntityNameCleaned)){
					myEntity = new Entity();
					myEntity.setName(sEntityName);
					
					myElement = anElement;
					
					/** Find the type */
					if (myElement.getAttribute(Constants.ATT_TYPE)!=null){
						sEntityTypeName = myElement.getAttributeValue(Constants.ATT_TYPE);
						myEntity.setTypeName(sEntityTypeName);
						
						/** Now that we have the type, we need to find the complex type */
						/** We remove the app: of the type that we have found */
						String sEntityTypeNameCleaned = "";
			    		iIndex = sEntityTypeName.indexOf(Constants.COLON);
			    		sEntityTypeNameCleaned = sEntityTypeName.substring((iIndex+1), sEntityTypeName.length());
			    		
			    		/** We get the node(s) that starts as "ComplexType" */
			    		List arlComplexTypes = sldDocRoot.getChildren(Constants.TAG_COMPLEXTYPE, myNamespace);
			    		Iterator itComplexTypes = arlComplexTypes.iterator();
						
						/** We go through the node(s) */
						while (itComplexTypes.hasNext()){
							Element aComplexTypeElement = (Element) itComplexTypes.next();
							if (aComplexTypeElement!=null && aComplexTypeElement.getAttributeValue(Constants.ATT_NAME)!=null){
								sComplexTypeName = aComplexTypeElement.getAttributeValue(Constants.ATT_NAME);
								if (sComplexTypeName!=null && sComplexTypeName.equals(sEntityTypeNameCleaned)){
									/** We found the complex type we were looking for */
									
									/** We go directly to the descendant element */
									Iterator itXSDElements = aComplexTypeElement.getDescendants(new ElementFilter(Constants.TAG_ELEMENT));
									if (itXSDElements!=null){
										while (itXSDElements.hasNext()){
											Element aXSDElement = (Element) itXSDElements.next();
											EntityField aField = new EntityField();
											
											String sFieldName = aXSDElement.getAttributeValue(Constants.ATT_NAME);
											if (sFieldName!=null && !sFieldName.equals(Constants.EMPTY_STRING)){
												aField.setFieldName(sFieldName);
											}
											
											String sFieldType = aXSDElement.getAttributeValue(Constants.ATT_TYPE);
											if (sFieldType!=null && !sFieldType.equals(Constants.EMPTY_STRING)){
												iIndex = sFieldType.indexOf(Constants.COLON);
												sFieldType = sFieldType.substring((iIndex+1), sFieldType.length());
												aField.setFieldType(sFieldType);
											}
											
											myEntity.addFields(aField);
										}
									}	
								}else{
									logger.info("LocalGIS_WFSG_XML_Analysis getDescribeFeatureType(): We did not find the complex type");
								}
							}else{
								logger.info("LocalGIS_WFSG_XML_Analysis getDescribeFeatureType(): aComplexTypeElement is null or its attribute name is null");
							}
						}
						
					}else{
						logger.info("LocalGIS_WFSG_XML_Analysis getDescribeFeatureType(): myElement.getAttribute(\"type\") is null");
					}
				}else{
					logger.info("LocalGIS_WFSG_XML_Analysis getDescribeFeatureType(): we cannot find the attribute name");
				}
				
			}
			if (myEntity!=null){
				return myEntity;
			}else{
				logger.info("Error: Entity has not been found");
				throw new Exception();
			}
			
		}catch (Exception ex){
			logger.info("An exception has occured");
			throw ex;
		}
	}

	
	protected static Entity getFeatureMNE (String sXMLBody, Entity entityReference, LocalGIS_WFSG_Resultados lwr_Resultados) throws Exception {
		StringReader sr = new StringReader(sXMLBody);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		Entity myEntity = null;
		ElementEntity anElementEntity;
		Element sldDocRoot;
		String sEntityElementName = "";
		
		try {
			doc = builder.build(sr);
		} catch (JDOMException e) {
			logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): Error al procesar el estilo");
			throw new Exception();
		} catch (IOException e) {
			logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): Error de entrada salida al procesar el estilo");
			throw new Exception();
	    }
		
		try {
		
			sldDocRoot = doc.getRootElement();
			myEntity = entityReference;
			Iterator itFeatureMember = sldDocRoot.getDescendants(new ElementFilter(Constants.TAG_FEATURE_MEMBER));
			
			/** If server is responding with a Service Report exception, we 
			 * transmit it as an Internal Exception. This checking is just done in the getFeature of the 
			 * idee server. For the other server there is not this checking as we check the URL before. 
			 */
			if (sXMLBody.indexOf(Constants.IDEE_SERVICE_EXCEPTION_REPORT)!=-1){
				throw new InternalException("Exception reported for GetFeature");
			}
			
			
			while (itFeatureMember.hasNext()){
				Element aFeatureMember = (Element) itFeatureMember.next();
				anElementEntity = new ElementEntity();
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): new Feature Member");
				
				/** get ID */
				Iterator itEntityID = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_ENTIDAD));
				while (itEntityID.hasNext()){
					Element anEntityID = (Element) itEntityID.next();
					if (anEntityID.getAttributeValue(Constants.TAG_FID)!=null){
						String sEntityID = anEntityID.getAttributeValue(Constants.TAG_FID);
						int iIndex = sEntityID.indexOf(Constants.FULL_STOP);
						sEntityID = sEntityID.substring((iIndex+1), sEntityID.length());
						anElementEntity.setId(sEntityID);
					}
				}
				
				/** get Name */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): is going to get the name");
				Iterator itEntity = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_NOMBRE_ENTIDAD));
				while (itEntity.hasNext()){
					Element anEntity = (Element) itEntity.next();
				
					Iterator itEntityClaseNombre = anEntity.getDescendants(new ElementFilter(Constants.TAG_CLASENOMBRE));
					Iterator itEntityName = anEntity.getDescendants(new ElementFilter(Constants.TAG_NOMBRE));
					logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): is bMNECompatible");
					while (itEntityClaseNombre.hasNext()){
						Element anEntityLanguage = (Element) itEntityClaseNombre.next();
						Element anEntityName = (Element) itEntityName.next();
						if (anEntityLanguage.getValue().equals(Constants.PREFERENTE)){
							sEntityElementName = anEntityName.getValue();
						}else if (sEntityElementName.equals(Constants.EMPTY_STRING)){
							/** it is not the prefered name, but we keep it in case we don't find any prefered one (We only keep it if sEntityElementName is empty) */
							sEntityElementName = anEntityName.getValue();
						}
						/** we set it */
						anElementEntity.setName(sEntityElementName);
					}
				
				}
			
				/** get Element Type */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): is going to get the Type");
				Iterator itEntityType = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_TYPE));
				while (itEntityType.hasNext()){
					Element anEntityType = (Element) itEntityType.next();
					if (anEntityType.getValue()!=null){
						anElementEntity.setType(anEntityType.getValue().trim());
					}
				}
				
				/** get Element Town */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): is going to get the Town");
				Iterator itEntityTown = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_TOWN));
				while (itEntityTown.hasNext()){
					Element anEntityTown = (Element) itEntityTown.next();
					if (anEntityTown.getValue()!=null){
						anElementEntity.setTown(anEntityTown.getValue());
					}
				}
				
				/** get Element County */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): is going to get the County");
				Iterator itEntityCounty = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_COUNTY));
				while (itEntityCounty.hasNext()){
					Element anEntityCounty = (Element) itEntityCounty.next();
					if (anEntityCounty.getValue()!=null){
						anElementEntity.setCounty(anEntityCounty.getValue());
					}
				}
				
				/** get coordinates */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): is going to get the Y point");
				Iterator itEntityPositions = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_COORDINATES, aFeatureMember.getNamespace()));
				while (itEntityPositions.hasNext()){
					Element anEntityPosition = (Element) itEntityPositions.next();
					String sPosition = anEntityPosition.getValue();
					int iIndexPunctuation = 0;
					iIndexPunctuation = sPosition.indexOf(Constants.COMMA);
					
					String sPositionX = sPosition.substring(0, iIndexPunctuation);
					anElementEntity.setPosX(sPositionX);
					
					String sPositionY = sPosition.substring((iIndexPunctuation+1), sPosition.length());
					anElementEntity.setPosY(sPositionY);
				}
				
				myEntity.addElement(anElementEntity);
			}
			
			if (myEntity!=null){
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): myEntity is not null, we throw it back");
				return myEntity;
			}else{
				logger.info("Error: Entity has not been found");
				throw new Exception();
			}
		}catch (Exception ex){
			logger.info("LocalGIS_WFSG_XML_Analysis getFeatureMNE(): An exception has occured");
			throw ex;
		}
	}
	
	
	protected static Entity getFeatureStandardWFSG (String sXMLBody, Entity entityReference, LocalGIS_WFSG_Resultados lwr_Resultados) throws Exception {
		StringReader sr = new StringReader(sXMLBody);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		Entity myEntity = null;
		ElementEntity anElementEntity;
		Element sldDocRoot;
		
		try {
			doc = builder.build(sr);
		} catch (JDOMException e) {
			logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStandardWFSG(): Error al procesar el estilo");
			throw new Exception();
		} catch (IOException e) {
			logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStandardWFSG(): Error de entrada salida al procesar el estilo");
			throw new Exception();
	    }
		
		try {
		
			sldDocRoot = doc.getRootElement();
			myEntity = entityReference;
			Iterator itFeatureMember = sldDocRoot.getDescendants(new ElementFilter(Constants.TAG_FEATURE_MEMBER));
			
			while (itFeatureMember.hasNext()){
				Element aFeatureMember = (Element) itFeatureMember.next();
				anElementEntity = new ElementEntity();
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStandardWFSG(): new Feature Member");
				
				/** get ID */
				Iterator itEntityID = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_GID, Namespace.getNamespace(Constants.NAMESPACE_APP_PREFIX, Constants.NAMESPACE_APP_URI)));
				while (itEntityID.hasNext()){
					Element anEntityID = (Element) itEntityID.next();
					if (anEntityID.getValue()!=null){
						anElementEntity.setId(anEntityID.getValue());
					}
				}
				/** get Name */
				Iterator itEntityName = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_GEOGRAPHIC_IDENTIFIER, Namespace.getNamespace(Constants.NAMESPACE_APP_PREFIX, Constants.NAMESPACE_APP_URI)));
				while (itEntityName.hasNext()){
					Element anEntityName = (Element) itEntityName.next();
					if (anEntityName.getValue()!=null){
						anElementEntity.setName(anEntityName.getValue());
					}
				}
				
				/** get Position */
				Iterator itPosition = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_POSITION, Namespace.getNamespace(Constants.NAMESPACE_APP_PREFIX, Constants.NAMESPACE_APP_URI)));
				while (itPosition.hasNext()){
					Element aPosition = (Element) itPosition.next();
					Iterator itCoordinates = aPosition.getDescendants(new ElementFilter(Constants.TAG_COORDINATES, Namespace.getNamespace(Constants.NAMESPACE_GML_PREFIX, Constants.NAMESPACE_GML_URI)));
				
					while (itCoordinates.hasNext()){
						Element anEntityPosition = (Element) itCoordinates.next();
						String sPosition = anEntityPosition.getValue();
						int iIndexPunctuation = 0;
						iIndexPunctuation = sPosition.indexOf(Constants.COMMA);
						
						String sPositionX = sPosition.substring(0, iIndexPunctuation);
						anElementEntity.setPosX(sPositionX);
						
						String sPositionY = sPosition.substring((iIndexPunctuation+1), sPosition.length());
						anElementEntity.setPosY(sPositionY);
					}
				}
				
				Iterator itGeographicExtent = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_GEOGRAPHIC_EXTENT, Namespace.getNamespace(Constants.NAMESPACE_APP_PREFIX, Constants.NAMESPACE_APP_URI)));
				while (itGeographicExtent.hasNext()){
					Element aPosition = (Element) itGeographicExtent.next();
					Iterator itCoordinates = aPosition.getDescendants(new ElementFilter(Constants.TAG_COORDINATES, Namespace.getNamespace(Constants.NAMESPACE_GML_PREFIX, Constants.NAMESPACE_GML_URI)));
				
					while (itCoordinates.hasNext()){
						Element anEntityPosition = (Element) itCoordinates.next();
						String sPosition = anEntityPosition.getValue();
						
						anElementEntity.setGeometria(sPosition);
					}
				}
				myEntity.addElement(anElementEntity);
			}
			
			if (myEntity!=null){
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): myEntity is not null, we throw it back");
				return myEntity;
			}else{
				logger.info("Error: Entity has not been found");
				throw new Exception();
			}
		}catch (Exception ex){
			logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStandardWFSG(): An exception has occured");
			throw ex;
		}
	}
	
	
	protected static Entity getFeatureStreet (String sXMLBody, Entity entityReference, LocalGIS_WFSG_Resultados lwr_Resultados) throws Exception {
		StringReader sr = new StringReader(sXMLBody);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		Entity myEntity = null;
		ElementEntity anElementEntity;
		Element sldDocRoot;
		
		try {
			doc = builder.build(sr);
		} catch (JDOMException e) {
			logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): Error al procesar el estilo");
			throw new Exception();
		} catch (IOException e) {
			logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): Error de entrada salida al procesar el estilo");
			throw new Exception();
	    }
		
		try {
		
			sldDocRoot = doc.getRootElement();
			myEntity = entityReference;
			Iterator itFeatureMember = sldDocRoot.getDescendants(new ElementFilter(Constants.TAG_FEATURE_MEMBER));
			
			while (itFeatureMember.hasNext()){
				Element aFeatureMember = (Element) itFeatureMember.next();
				anElementEntity = new ElementEntity();
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): new Feature Member");
				
				anElementEntity.setExactResult(false);
				
				/** get ID */
				Iterator itEntityID = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_ENTIDAD));
				while (itEntityID.hasNext()){
					Element anEntityID = (Element) itEntityID.next();
					if (anEntityID.getAttributeValue(Constants.TAG_FID)!=null){
						anElementEntity.setId(anEntityID.getAttributeValue(Constants.TAG_FID));
					}
				}
				
				/** get Name */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): is going to get the name");
				Iterator itEntityName = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_NOMBRE));
				while (itEntityName.hasNext()){
					Element anEntityName = (Element) itEntityName.next();
					if (anEntityName.getValue()!=null){
						anElementEntity.setName(anEntityName.getValue());
					}
				}
				
			
				/** get Element Type */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): is going to get the Type");
				Iterator itEntityType = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_TYPE));
				while (itEntityType.hasNext()){
					Element anEntityType = (Element) itEntityType.next();
					if (anEntityType.getValue()!=null){
						anElementEntity.setType(anEntityType.getValue());
					}
				}
				
				/** get Element Town */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): is going to get the Town");
				Iterator itEntityTown = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_TOWN));
				while (itEntityTown.hasNext()){
					Element anEntityTown = (Element) itEntityTown.next();
					if (anEntityTown.getValue()!=null){
						anElementEntity.setTown(anEntityTown.getValue());
					}
				}
				
				/** get Element County */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): is going to get the County");
				Iterator itEntityCounty = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_COUNTY));
				while (itEntityCounty.hasNext()){
					Element anEntityCounty = (Element) itEntityCounty.next();
					if (anEntityCounty.getValue()!=null){
						anElementEntity.setCounty(anEntityCounty.getValue());
					}
				}
				
				/** get coordinates */
				/** get X Point */

				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): is going to get the coordinate point ");
				Iterator itEntityMultiline = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_MULTILINE_STRING, aFeatureMember.getNamespace()));
				if (itEntityMultiline.hasNext()){
					Element anEntityGeometry = (Element) itEntityMultiline.next();
					Iterator itEntityPositions = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_COORDINATES, aFeatureMember.getNamespace()));
					if (itEntityPositions.hasNext()){
						Element anEntityPosition = (Element) itEntityPositions.next();
						String sPosition = anEntityPosition.getValue();
						int iIndexComma = sPosition.indexOf(Constants.COMMA);
						int iIndexSpace = sPosition.indexOf(Constants.SPACE);
						
						String sPositionX = sPosition.substring(0, iIndexComma);
						anElementEntity.setPosX(sPositionX);
						logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): X set to: " + sPositionX);
						
						String sPositionY = sPosition.substring((iIndexComma+1), iIndexSpace);
						anElementEntity.setPosY(sPositionY);
						logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): Y set to: " + sPositionY);
					}
				}
				
				/** get MultilineString */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): is going to get the MultiLineString ");
				Iterator itEntityGeometry = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_MULTILINE_STRING, aFeatureMember.getNamespace()));
				while (itEntityGeometry.hasNext()){
					Element anEntityGeometry = (Element) itEntityGeometry.next();
					if (anEntityGeometry.getValue()!=null){
						anElementEntity.setGeometria(anEntityGeometry.getValue());
					}
				}
				
				myEntity.addElement(anElementEntity);
			}
			
			if (myEntity!=null){
				logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): myEntity is not null, we throw it back");
				return myEntity;
			}else{
				logger.info("Error: Entity has not been found");
				throw new Exception();
			}
		}catch (Exception ex){
			logger.info("LocalGIS_WFSG_XML_Analysis getFeatureStreet(): An exception has occured");
			throw ex;
		}
	}
	
	protected static ElementEntity getFeaturePortal (String sXMLBody, ElementEntity elementEntity, LocalGIS_WFSG_Resultados lwr_Resultados) throws Exception {
		StringReader sr = new StringReader(sXMLBody);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		ElementEntity anElementEntity = null;
		Element sldDocRoot;
		
		try {
			doc = builder.build(sr);
		} catch (JDOMException e) {
			logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): Error al procesar el estilo");
			throw new Exception();
		} catch (IOException e) {
			logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): Error de entrada salida al procesar el estilo");
			throw new Exception();
	    }
		
		try {
			logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal() start of method");
			sldDocRoot = doc.getRootElement();
			Iterator itFeatureMember = sldDocRoot.getDescendants(new ElementFilter(Constants.TAG_FEATURE_MEMBER));
			
			anElementEntity = elementEntity;
			
			if (itFeatureMember.hasNext()){
				Element aFeatureMember = (Element) itFeatureMember.next();
				logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): new Feature Member");
				
				/** get ID */
				Iterator itEntityID = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_ENTIDAD));
				while (itEntityID.hasNext()){
					Element anEntityID = (Element) itEntityID.next();
					if (anEntityID.getAttributeValue(Constants.TAG_FID)!=null){
						anElementEntity.setId(anEntityID.getAttributeValue(Constants.TAG_FID));
					}
				}
				
				/** get Name & número*/
				logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): is going to get the name");
				Iterator itEntityName = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_NOMBRE));
				logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): got the Iterator");
				while (itEntityName.hasNext()){
					logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): has next");
					Element anEntityName = (Element) itEntityName.next();
					logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): got next");
					if (anEntityName.getValue()!=null){
						logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): anEntityName.getValue() is not null: " + anEntityName.getValue());
						logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): anElementEntity.getName(): " + anElementEntity.getName());
						anElementEntity.setNumero(anEntityName.getValue());
						logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): we have set the numero: " + anEntityName.getValue());
						anElementEntity.setExactResult(true);
					}
				}
				
				/** get Element Type */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): is going to get the Type");
				Iterator itEntityType = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_TYPE));
				while (itEntityType.hasNext()){
					Element anEntityType = (Element) itEntityType.next();
					if (anEntityType.getValue()!=null){
						anElementEntity.setType(anEntityType.getValue());
					}
				}
				
				/** get Element Town */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): is going to get the Town");
				Iterator itEntityTown = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_TOWN));
				while (itEntityTown.hasNext()){
					Element anEntityTown = (Element) itEntityTown.next();
					if (anEntityTown.getValue()!=null){
						anElementEntity.setTown(anEntityTown.getValue());
					}
				}
				
				/** get Element County */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): is going to get the County");
				Iterator itEntityCounty = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_COUNTY));
				while (itEntityCounty.hasNext()){
					Element anEntityCounty = (Element) itEntityCounty.next();
					if (anEntityCounty.getValue()!=null){
						anElementEntity.setCounty(anEntityCounty.getValue());
					}
				}
				
				/** get coordinates */
				/** get X Point */
//				logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): is going to get the X point");
//				Iterator itEntityPosX = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_POS_X, aFeatureMember.getNamespace()));
//				while (itEntityPosX.hasNext()){
//					Element anEntityPosX = (Element) itEntityPosX.next();
//					if (anEntityPosX.getValue()!=null){
//						anElementEntity.setPosX(anEntityPosX.getValue());
//					}
//				}
//				
//				/** get Y Point */
//				Iterator itEntityPosY = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_POS_Y, aFeatureMember.getNamespace()));
//				while (itEntityPosY.hasNext()){
//					Element anEntityPosY = (Element) itEntityPosY.next();
//					if (anEntityPosY.getValue()!=null){
//						anElementEntity.setPosY(anEntityPosY.getValue());
//					}
//				}
				
				Iterator itEntityPositions = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_COORDINATES, aFeatureMember.getNamespace()));
				while (itEntityPositions.hasNext()){
					Element anEntityPosition = (Element) itEntityPositions.next();
					String sPosition = anEntityPosition.getValue();
					int iIndexPunctuation = 0;
					iIndexPunctuation = sPosition.indexOf(Constants.COMMA);
					
					String sPositionX = sPosition.substring(0, iIndexPunctuation);
					anElementEntity.setPosX(sPositionX);
					
					String sPositionY = sPosition.substring((iIndexPunctuation+1), sPosition.length());
					anElementEntity.setPosY(sPositionY);
				}
				
				/** get MultilineString */
				logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): is going to get the MultiLineString ");
				Iterator itEntityGeometry = aFeatureMember.getDescendants(new ElementFilter(Constants.TAG_MULTILINE_STRING, aFeatureMember.getNamespace()));
				anElementEntity.setGeometria(null);
				while (itEntityGeometry.hasNext()){
					Element anEntityGeometry = (Element) itEntityGeometry.next();
					if (anEntityGeometry.getValue()!=null){
						anElementEntity.setGeometria(anEntityGeometry.getValue());
					}
				}
			}
			logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(); is going to return anElementEntity");
			return anElementEntity;
			
		}catch (Exception ex){
			logger.info("LocalGIS_WFSG_XML_Analysis getFeaturePortal(): An exception has occured");
			throw ex;
		}
	}
	
}
