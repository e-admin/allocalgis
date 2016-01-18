/**
 * GetFeatureInfoProxy.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.model.LocalgisAttributeTranslated;
import com.localgis.web.core.model.LocalgisAttributeValueTranslated;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;

class ComparatorAttributeAlias implements Comparator {

    private String locale;
    
    public ComparatorAttributeAlias(String locale) {
        this.locale = locale;
    }
    
    public int compare(Object obj1, Object obj2) {
        String str1 = (String)obj1;
        String str2 = (String)obj2;
        final Collator collator = Collator.getInstance (new Locale(locale)); 
        return collator.compare(str1, str2);
    }
    
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

public class GetFeatureInfoProxy extends HttpServlet {

    private static final Logger logger = Logger.getLogger(GetFeatureInfoProxy.class);
    
    private static String ERROR_PAGE = "/empty.jsp";

    private static int TIMEOUT_WFS_SERVER = 20000;
    
    private static String QUERY_LAYERS_PARAM = "QUERY_LAYERS";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtenemos los parametros
    	String layerName = null;
        String urlWFSServer = request.getParameter("urlWFSServer");
        String language = request.getParameter("language");
        String accion = request.getParameter("accion");
        
        // Hacemos la peticion
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_WFS_SERVER);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_WFS_SERVER);
        HttpMethod httpMethod = new GetMethod(urlWFSServer);
        byte[] responseBody;
        try {
            int statusCode = httpClient.executeMethod(httpMethod);
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Error al realizar la peticion a \""+urlWFSServer+"\". Codigo de la respuesta = ["+statusCode+"]");
                request.setAttribute("errorMessageKey", "getFeatureInfo.error.badStatusCode");
                request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
                return;
            }
            responseBody = httpMethod.getResponseBody();
        } catch (HttpException e) {
            logger.error("Error al realizar la peticion a \""+urlWFSServer+"\"", e);
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        } catch (IOException e) {
            logger.error("Error de E/S al realizar la peticion a \""+urlWFSServer+"\"", e);
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        } finally {
            httpMethod.releaseConnection();
        }
        
        
        /* Obtenemos el contenido, que debería ser algo de esta forma:
         * 
         * <?xml version="1.0" encoding="ISO-8859-1"?>
         * <msGMLOutput
         *        xmlns:gml="http://www.opengis.net/gml"
         *        xmlns:xlink="http://www.w3.org/1999/xlink"
         *        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
         *   <parcelas_layer>
         *     <parcelas_feature>
         *       <gml:boundedBy>
         *         <gml:Box srsName="EPSG:XXXX">
         *           <gml:coordinates>XXXX</gml:coordinates>
         *         </gml:Box>
         *       </gml:boundedBy>
         *       <id_localgis>XXXX</id_localgis>
         *       <id>XXXXX</id>
         *       <referencia_catastral>XXXX</referencia_catastral>
         *       <id_municipio>XXXX</id_municipio>
         *       <GEOMETRY>POLYGON((XXXX))</GEOMETRY>
         *     </parcelas_feature>
         *   </parcelas_layer>
         * </msGMLOutput>
         */
        ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBody);
        boolean isXMLResponse = true;
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        } catch (SAXException e) {
            logger.error("Error al parsear la respuesta del map server", e);
            isXMLResponse = false;
        } catch (IOException e) {
            logger.error("Error de entrada salida al parsear la respuesta del map server", e);
            isXMLResponse = false;
        } catch (ParserConfigurationException e) {
            logger.error("Error de configuracion en el parser de la respuesta del map server", e);
            isXMLResponse = false;
        }
        if (!isXMLResponse) {
            logger.error("La respuesta recibida no tiene formato XML");
            // Escribimos los bytes directamente en el response
            response.getOutputStream().write(responseBody);
            response.getOutputStream().flush();
            return;
        }
        
        /*
         * El nodo raiz debera ser un nodo msGMLOutput, en otro caso es que no
         * hemos obtenido correctamente la feature
         */
        Element root = document.getDocumentElement();
        if (!root.getNodeName().equals("msGMLOutput")) {
            logger.error("Error al realizar la peticion a \""+urlWFSServer+"\". La raiz del documento es ["+root.getNodeName()+"]");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }
        
        /*
         * Creamos un map para poder traducir los atributos. En el caso de que
         * estemos haciendo la peticion al map server nuestro tendremos
         * traducciones. En caso de que sea una peticion a un servidor externo
         * lo tendremos vacio y no traduciremos nada
         */
        try {
            String urlMapServer = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER);
            Map attributesTranslation = new HashMap();
            Hashtable attributesValues= null;
            if (urlWFSServer.startsWith(urlMapServer)) {
                LocalgisLayerManager localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
                String configurationLocalgisWeb = (String) request.getAttribute("configurationLocalgisWeb");
                boolean publicMaps;
                if (configurationLocalgisWeb != null && ((configurationLocalgisWeb.equals("public"))||(configurationLocalgisWeb.equals("incidencias")))) {
                    publicMaps = true;
                } else if (configurationLocalgisWeb != null && configurationLocalgisWeb.equals("private")) {
                    publicMaps = false;
                } else {
                    logger.debug("El parámetro de configuracion \"configurationLocalgisWeb\" no esta bien definido");
                    request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
                    return;
                }
                layerName = getQueryLayersFromURL(urlWFSServer);
                List attributes = localgisLayerManager.getAttributesLayer(layerName, new Boolean(publicMaps), language);
                Iterator itAttributes = attributes.iterator();
                while (itAttributes.hasNext()) {
                    LocalgisAttributeTranslated attribute = (LocalgisAttributeTranslated) itAttributes.next();
                    attributesTranslation.put(attribute.getAttributename(), attribute.getAlias());
                }
                
                //Obtenemos la informacion sobre las traducciones disponibles
                //para los atributos que tienen dominio en la tabla especifica. El objetivo
                //es posteriormente en lugar de presentar el valor se presenta la traduccion
                //por ejemplo en lugar de presentar MT se prense Metro.
                attributesValues=(Hashtable)getServletContext().getAttribute(layerName);
                if (attributesValues==null)
                	attributesValues=getAtributeValues(localgisLayerManager,layerName,language);
                
                if (attributesValues!=null)
                	getServletContext().setAttribute(layerName, attributesValues);
                
            }
            Collection features = getFeatures(root, attributesTranslation, attributesValues,language, accion);

            if (features == null || features.isEmpty()) {
                request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
                return;
            }
            if ((accion!=null) && (accion.equals("incidencia")))
            	incidencia(request, response, features, new String(responseBody), layerName);
            else
            	sendResponse(request, response, features, new String(responseBody));
            return;
        } catch (LocalgisConfigurationException e) {
            logger.error("Error de configuración.", e);
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        } catch (LocalgisInitiationException e) {
            logger.error("Error de inicializacion.", e);
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        } catch (LocalgisDBException e) {
            logger.error("Error de de base de datos.", e);
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }
    }
    
    private void incidencia(HttpServletRequest request, HttpServletResponse response, Collection features, String gml, String layerName) throws IOException {
        response.setContentType("text/xml");

        PrintWriter printWriter = response.getWriter();

        Iterator itFeatures = features.iterator();
        while (itFeatures.hasNext()) {
        	printWriter.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            printWriter.println("<incidencia>");
            //SortedMap feature = (SortedMap) itFeatures.next();
            LinkedHashMap feature = (LinkedHashMap) itFeatures.next();
            Iterator itFeature = feature.keySet().iterator();
            while (itFeature.hasNext()) {
                String attributeName = (String) itFeature.next();
                String attributeValue = (String) feature.get(attributeName);
                if ((attributeName!=null)&& ((attributeName.equals("id")))||(attributeName.equals("id_municipio"))||(attributeName.equals("nombre")) ){
                	printWriter.println("<"+attributeName+">");
                	printWriter.println(attributeValue);
                	printWriter.println("</"+attributeName+">");
                }
            }
            printWriter.println("<layer_name>");
        	printWriter.println(layerName);
        	printWriter.println("</layer_name>");
            printWriter.println("</incidencia>");
        }
        try {
            String encodedGML = URLEncoder.encode(gml.replaceAll("\\c+", " "), "ISO-8859-1");

        } catch (UnsupportedEncodingException e) {
            logger.error("Error al codificar la respuesta GML");
        }
	}

	/**
     * Obtenemos la lista de valores de atributos (dominios) traducidos
     * @param localgisLayerManager
     * @param layername
     * @param language
     * @return
     */
    private Hashtable getAtributeValues(LocalgisLayerManager localgisLayerManager,String layername,String language){
    	
    	Hashtable atributos=null;
    	
		try {
			List attributesValues=localgisLayerManager.getAttributesValuesLayer(layername,  language);
			Iterator itAttributesValues = attributesValues.iterator();
			atributos=new Hashtable();
			while (itAttributesValues.hasNext()) {
			    LocalgisAttributeValueTranslated attributeValue = (LocalgisAttributeValueTranslated) itAttributesValues.next();
			    String attributeName=attributeValue.getAttributename();
			    String pattern=attributeValue.getPattern();
			    String traduccion=attributeValue.getTraduccion();
			    String locale=attributeValue.getLocale();
			    //System.out.println("Datos:"+attributeName+" "+pattern+" "+traduccion);
			    
			    //Obtenemos la lista de traducciones de un atributo
			    Hashtable traduccionesValues=(Hashtable)atributos.get(attributeName);
			    if (traduccionesValues==null){
			    	traduccionesValues=new Hashtable();
			    	atributos.put(attributeName,traduccionesValues);
			    }                
			    //Si la  traduccion ya existe y el idioma del dominio no es el especificado no la
			    //insertamos. Con esto queremos conseguir que si hay traducciones para el idioma 
			    //elegido nos quedemos con esta y en caso contrario con la castellana.
			    String traduccionRecuperada=(String)traduccionesValues.get(pattern);
			    if (traduccionRecuperada==null)
			    	traduccionesValues.put(pattern, traduccion);                
			    else  
			    	if ((traduccionRecuperada!=null) && (attributeValue.getLocale().equals(language)))
			        	traduccionesValues.put(pattern, traduccion);                
			    	
			}
		} catch (LocalgisDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
        return atributos;
    	
    }
    
    private void sendResponse(HttpServletRequest request, HttpServletResponse response, Collection features, String gml) throws IOException {
        response.setContentType("text/html; charset=ISO-8859-1");
        response.setCharacterEncoding("ISO-8859-1");

        PrintWriter printWriter = response.getWriter();

        Iterator itFeatures = features.iterator();
        while (itFeatures.hasNext()) {
            printWriter.println("<table width=\"95%\"");
            //SortedMap feature = (SortedMap) itFeatures.next();
            LinkedHashMap feature = (LinkedHashMap) itFeatures.next();
            Iterator itFeature = feature.keySet().iterator();
            while (itFeature.hasNext()) {
                String attributeName = (String) itFeature.next();
                String attributeValue = (String)feature.get(attributeName);
                printWriter.println("<tr>");
                printWriter.println("<td align=\"left\"><b>"+attributeName+"</b></td>");
                printWriter.println("<td align=\"left\">"+attributeValue+"</td>");
                printWriter.println("</tr>");
            }
            printWriter.println("</table>");
        }
        try {
            String encodedGML = URLEncoder.encode(gml.replaceAll("\\c+", " "), "ISO-8859-1");
            printWriter.print("<div id=\"gmlResponse\" style=\"display: none;\">");
            printWriter.print(encodedGML);
            printWriter.print("</div>");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error al codificar la respuesta GML");
        }
    }
    
    /**
     * Método que obtiene el valor del parametro QUERY_LAYERS a partir de una URL
     * @param url URL donde se quiere buscar el valor del parametro
     * @return El valor del parametro QUERY_LAYERS
     */
    private String getQueryLayersFromURL(String url) {
        
        int indexOfParams = url.indexOf("?");
        int indexOfParamQueryLayers = url.indexOf(QUERY_LAYERS_PARAM+"=", indexOfParams);
        int indexOfEndParamQueryLayers = url.indexOf("&", indexOfParamQueryLayers);
        if (indexOfEndParamQueryLayers != -1) {
            return url.substring(indexOfParamQueryLayers + QUERY_LAYERS_PARAM.length() + 1, indexOfEndParamQueryLayers);
        } else {
            return url.substring(indexOfParamQueryLayers + QUERY_LAYERS_PARAM.length() + 1);
        }
    }
    
    /**
     * Método que procesa un documento XML y devuelve una coleccion de features,
     * donde cada feature sera una lista ordenada de pares atributo-valor. Las
     * features se intentaran traducir utilzando el Map pasado como parametro.
     * Las features se ordenaran segun el idioma pasado como parametro
     * 
     * @param root
     *            Nodo raiz del XML
     * @param attributesTranslation
     *            Map para realizar las traducciones
     * @param locale
     *            Locales a las que se desea traducir
     * @return Una coleccion de features
     */
    private Collection getFeatures(Element root, Map attributesTranslation, Hashtable attributesValues,String locale, String accion) {
        /*
         * Creamos una coleccion donde meteremos todas las features por si la
         * consulta devolviera varias
         */
        Collection features = new ArrayList();
        NodeList layersNodeList = root.getChildNodes();
        int layersNodeListLength = layersNodeList.getLength();
        for (int i = 0; i < layersNodeListLength; i++) {
            /*
             * Buscamos el primer nodo que sea un elemento, que será el nodo
             * correpondiente a la capa. Solo debería haber un elemento de este
             * nivel
             */
            Node layerNode = layersNodeList.item(i);
            if (layerNode.getNodeType() == Node.ELEMENT_NODE) {
                NodeList featuresNodeList = ((Element)layerNode).getChildNodes();
                int featuresNodeListLength = featuresNodeList.getLength();
                /*
                 * Buscamos el primer nodo que sea un elemento, que será el nodo
                 * correpondiente a la feature. Pude haber varios
                 */
                for (int j = 0; j < featuresNodeListLength; j++) {
                    Node featureNode = featuresNodeList.item(j);
                    if (featureNode.getNodeType() == Node.ELEMENT_NODE) {
                        NodeList attributesNodeList = ((Element)featureNode).getChildNodes();
                        int attributesNodeListLength = attributesNodeList.getLength();
                        
                        //Cambiamos la estructura para que la ordenacion depenga del orden definido
                        //en el gestor de capas.
                        //SortedMap feature = new TreeMap(new ComparatorAttributeAlias(locale));
                        LinkedHashMap feature = new LinkedHashMap();
                        
                        /*
                         * Insertamos en un SortedMap todos los atributos que encontremos, previamente traducidos, salvo:
                         * id_localgis, GEOMETRY y gml:boundedBy
                         */
                        for (int k = 0; k < attributesNodeListLength; k++) {
                            Node attributeNode = attributesNodeList.item(k);
                            if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
                                if (!(attributeNode.getNodeName().equals("gml:boundedBy") ||
                                        attributeNode.getNodeName().equals("id_localgis") || attributeNode.getNodeName().equals("GEOMETRY"))) {
                                	String nameAttributeTranslated = null;
                                	if ((accion!=null) && (accion.equals("incidencia")))
                                		nameAttributeTranslated = (String) attributeNode.getNodeName();                                		
                                	else
                                		nameAttributeTranslated = (String)attributesTranslation.get(attributeNode.getNodeName());
                                    if (nameAttributeTranslated == null) {
                                        nameAttributeTranslated = attributeNode.getNodeName();
                                    }
                                    //En lugar de añadir simplemente el contenido sin traducir lo traducimos
                                    //por si pertenece a un dominio
                                    String contenido=attributeNode.getFirstChild().getNodeValue();
                                    try {
                                    	if (attributesValues==null)
                                    		attributesValues=new Hashtable();
										Hashtable attributeValuesDomain=(Hashtable)attributesValues.get(attributeNode.getNodeName());
										if (attributeValuesDomain!=null){
											String traduccionDominio=(String)attributeValuesDomain.get(contenido);
											if (traduccionDominio!=null)
												contenido=traduccionDominio;
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
                                    feature.put(nameAttributeTranslated, contenido);
                                }
                            }
                        }
                        features.add(feature);
                    }
                }
                // Como solo vamos a tratar una capa salimos del bucle
                break;
            }
        }
        return features;
    }

}
