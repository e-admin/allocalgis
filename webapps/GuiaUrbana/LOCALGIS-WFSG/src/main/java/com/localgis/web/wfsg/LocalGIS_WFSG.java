/**
 * LocalGIS_WFSG.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import com.localgis.web.wfsg.constants.Constants;
import com.localgis.web.wfsg.domain.ElementEntity;
import com.localgis.web.wfsg.domain.Entity;
import com.localgis.web.wfsg.exceptions.ConnectionException;
import com.localgis.web.wfsg.exceptions.IncorrectArgumentException;
import com.localgis.web.wfsg.exceptions.InternalException;
import com.localgis.web.wfsg.exceptions.NoDataFoundException;
import com.localgis.web.wfsg.util.Utils;

/**
 * This class contains 
 * 		* the three main procedures called (protected) 
 * 		* the public getPlaceNameInformation that is called when using the idee
 * 		* the public getStreetInformation that is called when using the server 213
 *
 * Exception thrown when no data has been found for the searching criteria
 * 
 * @author SATEC Framework Team
 * @since 01.12.2007
 * @version 01.00.00
 */

public class LocalGIS_WFSG {
	 
    protected static Logger logger = Logger.getLogger(LocalGIS_WFSG.class);
    
    
    /**
	 * This method is the main method of the class and does the different requests to the server that is to be 
	 * a WFSG standard server
	 * 
	 * @param URLServer URL of the server that will be called by the application
	 * @param properties Hashtable containing all the different properties that will be filtered in the getFeature request
	 * @return a LocalGIS_WFSG_Resultados that contains all the results found through the three different requests
	 * @throws IncorrectArgumentException if any arguments is incorrect
	 * @throws NoDataFoundException if no data is found
	 * @throws ConnectionException if any connection problem arrises. 
	 * @throws InternalException
	 */
    public static LocalGIS_WFSG_Resultados getStandardWFSGInformation (String URLServer, Hashtable properties, String featureType) 
    	throws IncorrectArgumentException, NoDataFoundException, ConnectionException, InternalException {
    	
    	try {
			
			/** We check that the URLServer passed in argument is correct */
			if (URLServer==null || URLServer.trim().equals(Constants.EMPTY_STRING)){
				throw new IncorrectArgumentException("URLServer");
			}
			
			/** We check the Hashtable */
			if (properties==null || properties.size()==0){
				throw new IncorrectArgumentException("Properties");
			}
			
			/** We check featureType */
			if (featureType==null || featureType.trim().equals(Constants.EMPTY_STRING)){
				throw new IncorrectArgumentException("featureType");
			}
			logger.info("execution of getStandardWFSGInformation");
    		LocalGIS_WFSG_Resultados lwr_Resultados = new LocalGIS_WFSG_Resultados();
    		Entity myEntity = new Entity();
    		ElementEntity myElementEntity = new ElementEntity();
    		/** We set the Server URL */
    		lwr_Resultados.setURLServer(URLServer);
    		/** We set the boolean to whether or not the Server is MNE-Specific */
    		lwr_Resultados.setStyle(Constants.STYLE_STANDARD);

    		myEntity.setName(featureType);
    		
    		lwr_Resultados.setEntityReference(myEntity);
    		lwr_Resultados.setElementEntityReference(myElementEntity);
    		/** We do the three different request to the server. The order of the different request is important */

    		lwr_Resultados = LocalGIS_WFSG.getCapabilities(lwr_Resultados);
    		
    		//lwr_Resultados = LocalGIS_WFSG.getDescribeFeatureType(lwr_Resultados);
    		
    		lwr_Resultados = LocalGIS_WFSG.getFeatureStandardWFSG(lwr_Resultados, properties);
    		
    		
    		
    		
    		/** We return the object created */
    		return lwr_Resultados;
    	
    	/** Catching the different possible errors */
		}catch (IncorrectArgumentException iae){
			throw iae;
		}catch (ConnectionException ce){
			throw ce;
		}catch (NoDataFoundException ndfe){
			throw ndfe;
    	}catch (Exception e) {
    		throw new InternalException(e.getMessage());
    	}
    }
    
    /**
	 * This method is the main method of the class and does the different requests to the server to get
	 * all the information needed when using the server www.idee.es
	 * 
	 * @param URLServer URL of the server that will be called by the application
	 * @param properties Hashtable containing all the different properties that will be filtered in the getFeature request
	 * @return a LocalGIS_WFSG_Resultados that contains all the results found through the three different requests
	 * @throws IncorrectArgumentException if any arguments is incorrect
	 * @throws NoDataFoundException if no data is found
	 * @throws ConnectionException if any connection problem arrises. 
	 * @throws InternalException
	 */
    public static LocalGIS_WFSG_Resultados getPlaceNameInformation (String URLServer, Hashtable properties) 
    	throws IncorrectArgumentException, NoDataFoundException, ConnectionException, InternalException {
    	
    	try {
			
			/** We check that the URLServer passed in argument is correct */
			if (URLServer==null || URLServer.trim().equals(Constants.EMPTY_STRING)){
				throw new IncorrectArgumentException("URLServer");
			}
			
			/** We check the Hashtable */
			if (properties==null || properties.size()==0){
				throw new IncorrectArgumentException("Properties");
			}
			
    		LocalGIS_WFSG_Resultados lwr_Resultados = new LocalGIS_WFSG_Resultados();
    		Entity myEntity = new Entity();
    		ElementEntity myElementEntity = new ElementEntity();
    		/** We set the Server URL */
    		lwr_Resultados.setURLServer(URLServer);
    		/** We set the boolean to whether or not the Server is MNE-Specific */
    		lwr_Resultados.setStyle(Constants.STYLE_IDEE);

    		myEntity.setName(Constants.SEARCHED_ENTITY_IDEE);
    		
    		lwr_Resultados.setEntityReference(myEntity);
    		lwr_Resultados.setElementEntityReference(myElementEntity);
    		/** We do the three different request to the server. The order of the different request is important */
    		lwr_Resultados = LocalGIS_WFSG.getCapabilities(lwr_Resultados);
    		//lwr_Resultados = LocalGIS_WFSG.getDescribeFeatureType(lwr_Resultados);
    		lwr_Resultados = LocalGIS_WFSG.getFeature(lwr_Resultados, properties, "ISO-8859-1");
    		
    		
    		
    		
    		/** We return the object created */
    		return lwr_Resultados;
    	
    	/** Catching the different possible errors */
		}catch (IncorrectArgumentException iae){
			throw iae;
		}catch (ConnectionException ce){
			throw ce;
		}catch (NoDataFoundException ndfe){
			throw ndfe;
    	}catch (Exception e) {
    		throw new InternalException(e.getMessage());
    	}
    }
    
    /**
	 * This method is the main method of the class and does the different requests to the server to get
	 * all the information needed when using the server 213.164.33.69
	 * 
	 * @param URLServer URL of the server that will be called by the application
	 * @param properties Hashtable containing all the different properties that will be filtered in the getFeature request
	 * @param streetNumber streetNumber searched for (can be initialized to null if no portal is looked for). 
	 * @return a LocalGIS_WFSG_Resultados that contains all the results found through the three different requests
	 * @throws IncorrectArgumentException if any arguments is incorrect
	 * @throws Exception
	 */
    public static LocalGIS_WFSG_Resultados getStreetInformation (String URLServer, Hashtable properties, String streetNumber) 
	throws IncorrectArgumentException, NoDataFoundException, ConnectionException, InternalException {
	try {
		
		/** We check that the URLServer passed in argument is correct */
		if (URLServer==null || URLServer.trim().equals(Constants.EMPTY_STRING)){
			throw new IncorrectArgumentException("URLServer");
		}
		
		/** We check the Hashtable */
		if (properties==null || properties.size()==0){
			throw new IncorrectArgumentException("Properties");
		}
		
		LocalGIS_WFSG_Resultados lwr_Resultados = new LocalGIS_WFSG_Resultados();
		Entity myEntity = new Entity();
		ElementEntity myElementEntity = new ElementEntity();
		/** We set the Server URL */
		lwr_Resultados.setURLServer(URLServer);
		/** We set the boolean to whether or not the Server is MNE-Specific */
		lwr_Resultados.setStyle(Constants.STYLE_PRUEBA);

		myEntity.setName(Constants.SEARCHED_ENTITY_PRUEBA);

		/** if and only if a portal is passed through the arguments, we set the field "numero" 
		 *	of the entity reference with the numero of portal
		 */
		if (streetNumber!=null && !streetNumber.trim().equals(Constants.EMPTY_STRING)){
			logger.info("LocalGIS_WFSG (getStreetInformation): setting numero to: " + streetNumber);
			myElementEntity.setNumero(streetNumber.trim());
		}
		
		
		lwr_Resultados.setEntityReference(myEntity);
		lwr_Resultados.setElementEntityReference(myElementEntity);
		/** We do the three different request to the server. The order of the different request is important */
		lwr_Resultados = LocalGIS_WFSG.getCapabilities(lwr_Resultados);
		//lwr_Resultados = LocalGIS_WFSG.getDescribeFeatureType(lwr_Resultados);
		lwr_Resultados = LocalGIS_WFSG.getFeature(lwr_Resultados, properties, null);
		
		/** We return the object created */
		return lwr_Resultados;
	
	/** Catching the different possible errors */
	}catch (IncorrectArgumentException iae){
		throw iae;
	}catch (ConnectionException ce){
		throw ce;
	}catch (NoDataFoundException ndfe){
		throw ndfe;
	}catch (Exception e) {
		throw new InternalException(e.getMessage());
	}
}
    
	
	/**
	 * Method that does the request getCapabilities on the selected server. 
	 * 
	 * @param aLWR_Resultados the object that contains the information needed
	 * @return aLWR_Resultados updated
	 * @throws NoDataFoundException if no data is found on the server
	 * @throws IncorrectArgumentException if any argument is incorrect
	 * @throws ConnectionException if any connection error appears. 
	 * @throws Exception any other exception
	 */
	protected static LocalGIS_WFSG_Resultados getCapabilities (LocalGIS_WFSG_Resultados aLWR_Resultados) 
		throws NoDataFoundException, IncorrectArgumentException, ConnectionException, Exception {
		
		StringBuffer sbURLFinal = new StringBuffer();
		String strGetResponseBody;
		
		
		try {
			if (aLWR_Resultados!=null){
				String sURL = aLWR_Resultados.getURLServer();

				if (sURL!=null && !sURL.trim().equals(Constants.EMPTY_STRING)){
					/** configuring the URL */
					sbURLFinal.append(sURL);
					sbURLFinal.append(Constants.URL_GETCAPABILITIES);
					
					HttpClientParams httpClientParams = new HttpClientParams();
					httpClientParams.setSoTimeout(Constants.TIMEOUT_WFS_SERVER);
					httpClientParams.setConnectionManagerTimeout(Constants.TIMEOUT_WFS_SERVER);
					HttpClient client = new HttpClient(httpClientParams);
					GetMethod get = new GetMethod(sbURLFinal.toString());
					logger.info("getCapabilities() : sURLFinal: " + sbURLFinal);
					get.setFollowRedirects(false);
					try {	
						int statusCode = client.executeMethod(get);
						if (statusCode != HttpStatus.SC_OK) {
							throw new ConnectionException();
						}
						strGetResponseBody = get.getResponseBodyAsString();
					}finally{
						get.releaseConnection();
					}
		    		
		    		
		    		if (!strGetResponseBody.trim().equals(Constants.EMPTY_STRING) || strGetResponseBody.indexOf("ServiceExceptionReport")!=-1){
		    			/** Analysis of the data received from the server */
		    			return LocalGIS_WFSG_XML_Analysis.getCapabilities(strGetResponseBody, aLWR_Resultados);
		    		}else{
		    			throw new NoDataFoundException();
		    		}
				}else {
					throw new IncorrectArgumentException ("URLServer");
				}
			}else{
				throw new Exception ("getCapabilities() LocalGIS_WFSG_Resultados is null");
			}
			
			
		}catch (IllegalArgumentException ex){
			throw new IncorrectArgumentException ("URLServer:"+ex.toString());
		}catch (IllegalStateException is){
			throw new IncorrectArgumentException ("URLServer protocol:"+is.toString());
		}catch (IOException ie){
			throw new ConnectionException ();
		}catch (NoDataFoundException ndfe){
			throw ndfe;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	
	/**
	 * Method that does the request getDescribeFeatureType on the selected server. 
	 * 
	 * @param aLWR_Resultados the object that contains the information needed
	 * @return aLWR_Resultados updated
	 * @throws NoDataFoundException if no data is found on the server
	 * @throws IncorrectArgumentException if any argument is incorrect
	 * @throws ConnectionException if any connection error appears. 
	 * @throws Exception any other exception
	 */
	protected static LocalGIS_WFSG_Resultados getDescribeFeatureType (LocalGIS_WFSG_Resultados aLWR_Resultados) 
		throws NoDataFoundException, IncorrectArgumentException, ConnectionException, Exception {
		StringBuffer sURLFinal;
		String sGetResponseFeatureTypes;
		
		try {
			if (aLWR_Resultados!=null){
				String sURL = aLWR_Resultados.getURLServer();

				if (sURL!=null && !sURL.trim().equals(Constants.EMPTY_STRING)){
					/** We check that there are Entity Types */
					if (aLWR_Resultados.getEntityTypes()!=null && aLWR_Resultados.getEntityTypes().size()>0){
						/** We go through the list o entity types */
						for (int i=0; i<aLWR_Resultados.getEntityTypes().size(); i++){
							Entity anEntity = (Entity) aLWR_Resultados.getEntityTypes().get(i);
							/** We set the URL we are going to throw */
							sURLFinal = new StringBuffer();
							sURLFinal.append(sURL);
							sURLFinal.append(Constants.URL_GETDESCRIBEFEATURE_PART1);
							sURLFinal.append(anEntity.getName());
							sURLFinal.append(Constants.URL_GETDESCRIBEFEATURE_PART2);
							
							/** sURLFinal is set to the URL that will be thrown to the server*/
							/** We prepare the request to the server */
							HttpClientParams httpClientParams = new HttpClientParams();
							httpClientParams.setSoTimeout(Constants.TIMEOUT_WFS_SERVER);
							httpClientParams.setConnectionManagerTimeout(Constants.TIMEOUT_WFS_SERVER);
							HttpClient client = new HttpClient(httpClientParams);
							GetMethod get = new GetMethod(sURLFinal.toString());
							get.setFollowRedirects(false);
							logger.info("LocalGIS_WFSG getDescribeFeatureType() sURLFinal: " + sURLFinal);
							try {
								int statusCode = client.executeMethod(get);
								if (statusCode != HttpStatus.SC_OK) {
									throw new ConnectionException();
								}
				    			sGetResponseFeatureTypes = get.getResponseBodyAsString();
							}finally{
								get.releaseConnection();
							}
							
							if (!sGetResponseFeatureTypes.trim().equals(Constants.EMPTY_STRING) || sGetResponseFeatureTypes.indexOf("ServiceExceptionReport")!=-1){
								anEntity = LocalGIS_WFSG_XML_Analysis.getDescribeFeatureType(sGetResponseFeatureTypes, anEntity.getName(), aLWR_Resultados);
								aLWR_Resultados.getEntityTypes().set(i, anEntity);
				    		}else{
				    			throw new NoDataFoundException();
				    		}
						}
					}
				}else {
					throw new IncorrectArgumentException ("URLServer");
				}
			}else{
				throw new Exception ("getCapabilities() LocalGIS_WFSG_Resultados is null");
			}
			
			
			return aLWR_Resultados;
			
			
		}catch (IllegalArgumentException ex){
			throw new IncorrectArgumentException ("URLServer");
		}catch (IllegalStateException is){
			throw new IncorrectArgumentException ("URLServer protocol");
		}catch (IOException ie){
			throw new ConnectionException ();
		}catch (NoDataFoundException ndfe){
			throw ndfe;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	/**
	 * Method that does the request getFeature on the standard WFSG. 
	 * 
	 * @param aLWR_Resultados the object that contains the information needed
	 * @return aLWR_Resultados updated
	 * @throws NoDataFoundException if no data is found on the server
	 * @throws IncorrectArgumentException if any argument is incorrect
	 * @throws ConnectionException if any connection error appears. 
	 * @throws Exception any other exception
	 */
	
	protected static LocalGIS_WFSG_Resultados getFeatureStandardWFSG (LocalGIS_WFSG_Resultados aLWR_Resultados, Hashtable properties)
	throws NoDataFoundException, IncorrectArgumentException, ConnectionException, Exception {
		String strGetResponseFeature;
		
		try {
			if (aLWR_Resultados!=null){
				
				String sURL = aLWR_Resultados.getURLServer();
				logger.info("LocalGIS_WFSG getFeatureStandardWFSG(): execution of getFeatureStandardWFSG()");
				
				if (aLWR_Resultados.getEntityReference()!=null && aLWR_Resultados.getEntityReference().getName()!=null){
					if (sURL!=null && !sURL.trim().equals(Constants.EMPTY_STRING)){
						/** We set the URL we are going to throw */
						Entity anEntity = aLWR_Resultados.getEntityReference();
						
						HttpClientParams httpClientParams = new HttpClientParams();
						httpClientParams.setSoTimeout(Constants.TIMEOUT_WFS_SERVER);
					    httpClientParams.setConnectionManagerTimeout(Constants.TIMEOUT_WFS_SERVER);
					    HttpClient client = new HttpClient(httpClientParams);
						logger.info("LocalGIS_WFSG getFeatureStandardWFSG(): new HttpClient created");
						PostMethod post = new PostMethod(sURL);
						logger.info("LocalGIS_WFSG getFeatureStandardWFSG(): created postMethod");		

						StringBuffer sbXML = new StringBuffer();
						sbXML.append(Constants.URL_STANDARD_XML_POST_ENCODING);
						sbXML.append(Constants.URL_STANDARD_XML_POST_GETFEATURE_START);
						sbXML.append(Constants.URL_STANDARD_XML_POST_NAMESPACE_1);
						sbXML.append(Constants.URL_STANDARD_XML_POST_QUERY_START);
						sbXML.append(Constants.QUOTATION_MARK);
						sbXML.append(anEntity.getName());
						sbXML.append(Constants.QUOTATION_MARK);
						sbXML.append(Constants.URL_STANDARD_XML_POST_NAMESPACE_2);
						sbXML.append(Constants.URL_STANDARD_XML_POST_NAMESPACE_3);
						sbXML.append(Constants.URL_STANDARD_XML_POST_FILTER_START);
						sbXML.append((properties.size()>1)?Constants.URL_STANDARD_AND_START:"");
						Enumeration en = properties.keys();
						while (en.hasMoreElements()){
							String sPropertyName = (String) en.nextElement();
							String sPropertyValue;
							
							if (properties.get(sPropertyName) instanceof Integer){
								Integer ivalue = (Integer) properties.get(sPropertyName);
								sPropertyValue=String.valueOf(ivalue);						
							}	
							else{
								sPropertyValue = (String) properties.get(sPropertyName);
							}
							
							sbXML.append((Utils.isNumeric(sPropertyValue))?Constants.URL_STANDARD_PROPERTYISEQUALTO_START:Constants.URL_STANDARD_PROPERTYISLIKE_START);
							sbXML.append(Constants.URL_STANDARD_PROPERTYNAME_BEFORE);
							sbXML.append(sPropertyName);
							sbXML.append(Constants.URL_STANDARD_PROPERTYNAME_AFTER);
							sbXML.append(Constants.URL_STANDARD_LITERAL_BEFORE);
							sbXML.append((!Utils.isNumeric(sPropertyValue) && sPropertyValue!=null && !sPropertyValue.trim().equals(Constants.EMPTY_STRING))?Constants.STAR_WILDCARD:"");
							sbXML.append((sPropertyValue!=null && !sPropertyValue.trim().equals(Constants.EMPTY_STRING))?sPropertyValue:Constants.STAR_WILDCARD);
							sbXML.append((!Utils.isNumeric(sPropertyValue) && sPropertyValue!=null && !sPropertyValue.trim().equals(Constants.EMPTY_STRING))?Constants.STAR_WILDCARD:"");
							sbXML.append(Constants.URL_STANDARD_LITERAL_AFTER);
							sbXML.append((Utils.isNumeric(sPropertyValue))?Constants.URL_STANDARD_PROPERTYISEQUALTO_END:Constants.URL_IDEE_PROPERTYISLIKE_END);
						
						}
						sbXML.append((properties.size()>1)?Constants.URL_STANDARD_AND_END:"");
						sbXML.append(Constants.URL_STANDARD_GETFEATURE_ENDFILTER);
						sbXML.append(Constants.URL_STANDARD_XML_POST_QUERY_END);
						sbXML.append(Constants.URL_STANDARD_XML_POST_GETFEATURE_END);
				
						logger.info("LocalGIS_WFSG getFeatureStandardWFSG(): sXML: " + sbXML.toString());
				
						try {
							InputStream anInputStream = new java.io.ByteArrayInputStream(sbXML.toString().getBytes());
							InputStreamRequestEntity ISRE = new InputStreamRequestEntity(anInputStream, InputStreamRequestEntity.CONTENT_LENGTH_AUTO);
							post.setRequestEntity(ISRE);
							post.setRequestHeader(Constants.CONTENT_TYPE,Constants.TEXT_XML);
							int statusCode = client.executeMethod(post);
							if (statusCode != HttpStatus.SC_OK) {
								throw new ConnectionException();
							}
							strGetResponseFeature = post.getResponseBodyAsString();
						}finally{
							post.releaseConnection();
						}
						
						if (!strGetResponseFeature.trim().equals(Constants.EMPTY_STRING)){
							anEntity = LocalGIS_WFSG_XML_Analysis.getFeatureStandardWFSG(strGetResponseFeature, anEntity, aLWR_Resultados);
							
						}else{
			    			logger.info("EXCEPTION: Error initialization strGetResponseFeature");
			    			throw new Exception("EXCEPTION: Error initialization strGetResponseFeature");
			    		}
						
						aLWR_Resultados.setEntityReference(anEntity);
					}else {
						throw new Exception("User didn't enter a proper URL for the server");	
					}
				}
				return aLWR_Resultados;
				
			}else{
				throw new Exception ("LocalGIS_WFSG_Resultados is null");
			}
		}catch (IllegalArgumentException ex){
			throw new IncorrectArgumentException ("URLServer");
		}catch (IllegalStateException is){
			throw new IncorrectArgumentException ("URLServer protocol");
		}catch (IOException ie){
			throw new ConnectionException ();
		}catch (NoDataFoundException ndfe){
			throw ndfe;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	
	/**
	 * Method that does the request getFeature on the selected server. 
	 * 
	 * @param aLWR_Resultados the object that contains the information needed
	 * @param charset Charset to encode the URLs
	 * @return aLWR_Resultados updated
	 * @throws NoDataFoundException if no data is found on the server
	 * @throws IncorrectArgumentException if any argument is incorrect
	 * @throws ConnectionException if any connection error appears. 
	 * @throws Exception any other exception
	 */
	
	protected static LocalGIS_WFSG_Resultados getFeature (LocalGIS_WFSG_Resultados aLWR_Resultados, Hashtable properties, String charset)
	throws NoDataFoundException, IncorrectArgumentException, ConnectionException, Exception {
		StringBuffer sbURLFinal;
		URI aURI = null;
		String strGetResponseFeature;
		
		try {
			if (aLWR_Resultados!=null){
				
				String sURL = aLWR_Resultados.getURLServer();
				logger.info("LocalGIS_WFSG getFeature(): execution of LocalGIS_WFSG_Resultados getFeature()");
				
				if (aLWR_Resultados.getEntityReference()!=null && aLWR_Resultados.getEntityReference().getName()!=null){
					if (sURL!=null && !sURL.trim().equals(Constants.EMPTY_STRING)){
						/** We set the URL we are going to throw */
						Entity anEntity = aLWR_Resultados.getEntityReference();
						sbURLFinal = new StringBuffer();
						sbURLFinal.append(sURL);
						sbURLFinal.append(Constants.URL_STANDARD_GETFEATURE_PART1);
						sbURLFinal.append(anEntity.getName());
						sbURLFinal.append(Constants.URL_STANDARD_GETFEATURE_PART2);
						sbURLFinal.append(Constants.URL_STANDARD_GETFEATURE_STARTFILTER);
						sbURLFinal.append((properties.size()>1)?Constants.URL_STANDARD_AND_START:"");
						Enumeration en = properties.keys();
						while (en.hasMoreElements()){
							String sPropertyName = (String) en.nextElement();
							String sPropertyValue = (String) properties.get(sPropertyName);
							
							if (sPropertyName.equals("entidadLocal_municipio"))
								sbURLFinal.append(Utils.isNumeric(sPropertyValue)?Constants.URL_STANDARD_PROPERTYISEQUALTO_START:Constants.URL_IDEE_PROPERTYISLIKE_START);
							else
								sbURLFinal.append((Utils.isNumeric(sPropertyValue) && aLWR_Resultados.getStyle()!=Constants.STYLE_PRUEBA)?Constants.URL_STANDARD_PROPERTYISEQUALTO_START:Constants.URL_IDEE_PROPERTYISLIKE_START);
							sbURLFinal.append(Constants.URL_STANDARD_PROPERTYNAME_BEFORE);
							sbURLFinal.append(sPropertyName);
							sbURLFinal.append(Constants.URL_STANDARD_PROPERTYNAME_AFTER);
							sbURLFinal.append(Constants.URL_STANDARD_LITERAL_BEFORE);
							sbURLFinal.append((!Utils.isNumeric(sPropertyValue) && sPropertyValue!=null && !sPropertyValue.trim().equals(Constants.EMPTY_STRING))?Constants.STAR_WILDCARD:"");
							sbURLFinal.append((sPropertyValue!=null && !sPropertyValue.trim().equals(Constants.EMPTY_STRING))?sPropertyValue:Constants.STAR_WILDCARD);
							sbURLFinal.append((!Utils.isNumeric(sPropertyValue) && sPropertyValue!=null && !sPropertyValue.trim().equals(Constants.EMPTY_STRING))?Constants.STAR_WILDCARD:"");
							sbURLFinal.append(Constants.URL_STANDARD_LITERAL_AFTER);
							if (sPropertyName.equals("entidadLocal_municipio"))
								sbURLFinal.append(Utils.isNumeric(sPropertyValue)?Constants.URL_STANDARD_PROPERTYISEQUALTO_END:Constants.URL_IDEE_PROPERTYISLIKE_END);
							else
								sbURLFinal.append((Utils.isNumeric(sPropertyValue) && aLWR_Resultados.getStyle()!=Constants.STYLE_PRUEBA)?Constants.URL_STANDARD_PROPERTYISEQUALTO_END:Constants.URL_IDEE_PROPERTYISLIKE_END);
						}
						sbURLFinal.append((properties.size()>1)?Constants.URL_STANDARD_AND_END:"");
						sbURLFinal.append(Constants.URL_STANDARD_GETFEATURE_ENDFILTER);	
						
						aURI = new URI(sbURLFinal.toString(), false, charset);
						
						logger.info("LocalGIS_WFSG getFeature(): sURLFinal: " + sbURLFinal.toString());
						HttpClientParams httpClientParams = new HttpClientParams();
						httpClientParams.setSoTimeout(Constants.TIMEOUT_WFS_SERVER);
						httpClientParams.setConnectionManagerTimeout(Constants.TIMEOUT_WFS_SERVER);
						HttpClient client = new HttpClient(httpClientParams);
						logger.info("LocalGIS_WFSG getFeature(): URI: " + aURI.getEscapedURI());
							
						GetMethod get = new GetMethod(aURI.getEscapedURI());
							
						get.setFollowRedirects(false);
						
						try {
							int statusCode = client.executeMethod(get);
							if (statusCode != HttpStatus.SC_OK) {
								throw new ConnectionException();
							}
							logger.info("LocalGIS_WFSG getFeature(): Method executed");
				    		strGetResponseFeature = get.getResponseBodyAsString();
						}finally{
							get.releaseConnection();
						}
						
						if (!strGetResponseFeature.trim().equals(Constants.EMPTY_STRING)){
			    			/** Analysis of the data received from the server */
							if (aLWR_Resultados.getStyle()==Constants.STYLE_IDEE){
								anEntity = LocalGIS_WFSG_XML_Analysis.getFeatureMNE(strGetResponseFeature, anEntity, aLWR_Resultados);
							}else if (aLWR_Resultados.getStyle()==Constants.STYLE_PRUEBA){
								anEntity = LocalGIS_WFSG_XML_Analysis.getFeatureStreet(strGetResponseFeature, anEntity, aLWR_Resultados);
								logger.info("aLWR_Resultados.getElementEntityReference().getNumero(): " + aLWR_Resultados.getElementEntityReference().getNumero());
								if (aLWR_Resultados.getElementEntityReference()!=null && !aLWR_Resultados.getElementEntityReference().getNumero().equals(Constants.EMPTY_STRING)){
									String sNumeroPortal = aLWR_Resultados.getElementEntityReference().getNumero();
									anEntity = getFeaturePortal (aLWR_Resultados, sNumeroPortal, anEntity);
								}
							}
						}else{
			    			logger.info("EXCEPTION: Error initialization strGetResponseFeature");
			    			throw new Exception("EXCEPTION: Error initialization strGetResponseFeature");
			    		}
				    	
				    	aLWR_Resultados.setEntityReference(anEntity);
					}else {
						throw new Exception("User didn't enter a proper URL for the server");	
					}
				}
		    	return aLWR_Resultados;
				
			}else{
				throw new Exception ("LocalGIS_WFSG_Resultados is null");
			}
			
			
			
		}catch (IllegalArgumentException ex){
			throw new IncorrectArgumentException ("URLServer");
		}catch (IllegalStateException is){
			throw new IncorrectArgumentException ("URLServer protocol");
		}catch (IOException ie){
			throw new ConnectionException ();
		}catch (NoDataFoundException ndfe){
			throw ndfe;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	/**
	 * Method that does the request getFeature on the selected server. 
	 * 
	 * @param aLWR_Resultados the object that contains the information needed
	 * @return aLWR_Resultados updated
	 * @throws NoDataFoundException if no data is found on the server
	 * @throws IncorrectArgumentException if any argument is incorrect
	 * @throws ConnectionException if any connection error appears. 
	 * @throws Exception any other exception
	 */
	
	protected static Entity getFeaturePortal (LocalGIS_WFSG_Resultados aLWR_Resultados, String iPortal, Entity anEntity)
	throws NoDataFoundException, IncorrectArgumentException, ConnectionException, Exception {
		StringBuffer sbURLFinal;
		URI aURI = null;
		String strGetResponseFeature;
		
		try {
			if (aLWR_Resultados!=null){
				
				String sURL = aLWR_Resultados.getURLServer();
				logger.info("LocalGIS_WFSG getFeaturePortal(): execution of getFeaturePortal()");
				
				for (int i=0; i<anEntity.getElements().size(); i++){
					ElementEntity anElementEntity = (ElementEntity) anEntity.getElements().get(i);
					
					
					if (sURL!=null && !sURL.trim().equals(Constants.EMPTY_STRING)){
						/** We set the URL we are going to throw */
						sbURLFinal = new StringBuffer();
						sbURLFinal.append(sURL);
						sbURLFinal.append(Constants.URL_STANDARD_GETFEATURE_PART1);
						sbURLFinal.append(Constants.ENTIDAD_PORTAL);
						sbURLFinal.append(Constants.URL_STANDARD_GETFEATURE_PART2);
						sbURLFinal.append( Constants.URL_STANDARD_GETFEATURE_STARTFILTER);
						sbURLFinal.append(Constants.URL_STANDARD_AND_START);
						sbURLFinal.append(Constants.URL_STANDARD_PROPERTYISEQUALTO_START);					
						sbURLFinal.append(Constants.URL_STANDARD_PROPERTYNAME_BEFORE);
						sbURLFinal.append(Constants.PORTAL_NUMERO);	
						sbURLFinal.append(Constants.URL_STANDARD_PROPERTYNAME_AFTER);
						sbURLFinal.append(Constants.URL_STANDARD_LITERAL_BEFORE);
						sbURLFinal.append(iPortal);
						sbURLFinal.append(Constants.URL_STANDARD_LITERAL_AFTER);
						sbURLFinal.append(Constants.URL_STANDARD_PROPERTYISEQUALTO_END);
						sbURLFinal.append(Constants.URL_STANDARD_PROPERTYISEQUALTO_START);					
						sbURLFinal.append(Constants.URL_STANDARD_PROPERTYNAME_BEFORE);
						sbURLFinal.append(Constants.PORTAL_VIA);
						sbURLFinal.append(Constants.URL_STANDARD_PROPERTYNAME_AFTER);
						sbURLFinal.append(Constants.URL_STANDARD_LITERAL_BEFORE);
						sbURLFinal.append(anElementEntity.getId());
						sbURLFinal.append(Constants.URL_STANDARD_LITERAL_AFTER);
						sbURLFinal.append(Constants.URL_STANDARD_PROPERTYISEQUALTO_END);
						sbURLFinal.append(Constants.URL_STANDARD_AND_END);
						sbURLFinal.append( Constants.URL_STANDARD_GETFEATURE_ENDFILTER);	
						
						
						aURI = new URI(sbURLFinal.toString(), false);
						
						logger.info("LocalGIS_WFSG getFeaturePortal(): sURLFinal: " + sbURLFinal.toString());
						HttpClientParams httpClientParams = new HttpClientParams();
						httpClientParams.setSoTimeout(Constants.TIMEOUT_WFS_SERVER);
						httpClientParams.setConnectionManagerTimeout(Constants.TIMEOUT_WFS_SERVER);
						HttpClient client = new HttpClient(httpClientParams);
						logger.info("LocalGIS_WFSG getFeaturePortal(): URI: " + aURI.getEscapedURI());
								
						GetMethod get = new GetMethod(aURI.getEscapedURI());
								
						get.setFollowRedirects(false);
							
						try {
							int statusCode = client.executeMethod(get);
							if (statusCode != HttpStatus.SC_OK) {
								throw new ConnectionException();
							}
							logger.info("LocalGIS_WFSG getFeaturePortal(): Method executed");
					    	strGetResponseFeature = get.getResponseBodyAsString();
						}finally{
							get.releaseConnection();
						}
							
						if (!strGetResponseFeature.trim().equals(Constants.EMPTY_STRING)){
							anElementEntity = LocalGIS_WFSG_XML_Analysis.getFeaturePortal(strGetResponseFeature, anElementEntity, aLWR_Resultados);
							anEntity.getElements().set(i, anElementEntity);
							
						}else{
				    		logger.info("EXCEPTION: Error initialization strGetResponseFeature");
				    		throw new Exception("EXCEPTION: Error initialization strGetResponseFeature");
				    	}
					}else {
						throw new Exception("User didn't enter a proper URL for the server");	
					}
				
				}

				return anEntity;
				
			}else{
				throw new Exception ("LocalGIS_WFSG_Resultados is null");
			}
			
		}catch (IllegalArgumentException ex){
			throw new IncorrectArgumentException ("URLServer");
		}catch (IllegalStateException is){
			throw new IncorrectArgumentException ("URLServer protocol");
		}catch (IOException ie){
			throw new ConnectionException ();
		}catch (NoDataFoundException ndfe){
			throw ndfe;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	
}
