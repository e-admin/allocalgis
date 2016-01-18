/**
 * LocalgisUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.NoResourceAvailableException;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisLayerExt;
import com.localgis.web.core.model.LocalgisMapServerLayer;
import com.localgis.web.core.model.LocalgisStyle;
import com.localgis.web.core.model.Scale;

public class LocalgisUtils {
        
	private static String ELEMENT_PROPERTY_NAME = "PropertyName";
    private static String ELEMENT_ONLINE_RESOURCE = "OnlineResource";
    private static String ELEMENT_CSS_PARAMETER = "CssParameter";
    private static String ELEMENT_SIZE_PREFIX = "";
    private static String ELEMENT_SIZE = "Size";
    private static String ELEMENT_LITERAL = "Literal";
    private static String ELEMENT_FORMAT = "Format";
    
    private static String ATTRIBUTE_NAME = "name";
    private static String VALUE_FONT_FAMILY = "font-family";
    private static String VALUE_STROKE_DASHARRAY = "stroke-dasharray";
    private static String VALUE_STROKE_WIDTH = "stroke-width";

    private static Namespace NAMESPACE_SLD = Namespace.getNamespace("sld", "http://www.opengis.net/sld");
    private static Namespace NAMESPACE_OGC = Namespace.getNamespace("ogc", "http://www.opengis.net/ogc");
    private static Namespace NAMESPACE_XLINK = Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");

    private static String ATTRIBUTE_ONLINE_RESOURCE = "href";
    private static String ICONLIB_PATH = "iconlib";
    private static String TEXTURES_PATH = "textures";
    private static String STYLES_MAPSERVER_PATH = "/mapserverstyles";
    
    private static final double INCHES_PER_METER = 39.37;
      
    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisUtils.class);
    
    /**
     * Método auxiliar para crear un objeto LocalgisStyle a partir de un xml que contiene una definicion completa de estilos, el nombre del estilo y el nombre de la capa asociada al estilo 
     * @param styleXML Definicion completa de estilo
     * @param styleName Nombre del estilo
     * @param layerName Nombre de la capa para la que se esta creando el estilo
     * @param mapAliasAttributes Mapa para asociar alias de atributos a nombres internos
     * @return Un objeto LocalgisStryle
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
    public static LocalgisStyle createLocalgisStyle(String styleXML, String styleName, String layerName, Map mapAliasAttributes) throws LocalgisConfigurationException,NoResourceAvailableException {
        StringReader sr = new StringReader(styleXML);
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(sr);
        } catch (JDOMException e) {
            logger.error("Error al procesar el estilo", e);
            return null;
        } catch (IOException e) {
            logger.error("Error de entrada salida al procesar el estilo", e);
            return null;
        }
        /*Parseamos el SLD que codifica el/los estilos asociados a la capa*/
        /*Si el documento SLD tuviera varios elementos <UserStyle>, nos quedamos con aquel cuyo nombre 
         * coincida con el parámetro styleName*/
        Element userStyle = getUserStyle(doc,styleName,layerName,NAMESPACE_SLD);
        Element userStyleNameElement = userStyle.getChild("Name",NAMESPACE_SLD);
        String userStyleName = userStyleNameElement.getText();
        Element userStyleTitleElement = userStyle.getChild("Title",NAMESPACE_SLD);
        String userStyleTitle = null; 
        if (userStyleTitleElement != null) {
            userStyleTitle = userStyleTitleElement.getText();
        }
        Element userStyleAbstractElement = userStyle.getChild("Abstract",NAMESPACE_SLD);
        String userStyleAbstract = null; 
        if (userStyleAbstractElement != null) {
            userStyleAbstract = userStyleAbstractElement.getText();
        }   
        
        Element featureTypeStyle = userStyle.getChild("FeatureTypeStyle",NAMESPACE_SLD);
        
        featureTypeStyle.addNamespaceDeclaration(NAMESPACE_OGC);
            
        StringBuffer errorRecursos=new StringBuffer();
        
        transformElementsToMapServerFormat(featureTypeStyle, mapAliasAttributes,errorRecursos);

        if (errorRecursos.length()>0){
        	throw new NoResourceAvailableException("Layer:"+layerName+"->"+errorRecursos.toString());
        }
        
        XMLOutputter outputter = new XMLOutputter();
        String featureTypeStyleAsString = outputter.outputString(featureTypeStyle);
        
        LocalgisStyle localgisStyle = new LocalgisStyle();
        localgisStyle.setStylename(userStyleName != null ? userStyleName : "");
        localgisStyle.setStyletitle(userStyleTitle != null ? userStyleTitle : "");
        localgisStyle.setStyleabstract(userStyleAbstract != null ? userStyleAbstract : "");
        localgisStyle.setFeaturetypestyles(featureTypeStyleAsString != null ? featureTypeStyleAsString : "");
        return localgisStyle;
        
    }
    
    /**
     * Método para transformar el elemento featureTypeStyle. Se modificaran lo siguiente <br>
     * los elementos <ogc:PropertyName> cambiando el alias del atributo por el
     * nombre interno del mismo.<br>
     * Las imagenes pasando de file:/imagen a una URL y cambiando el formato a gif si el formato origen es distinto de gif o png.<br>
     * El size para eliminar el nodo Literal y que directamente tenga el tamaño. Es decir de <size><literal>5</literal></size> a <size>5</size>
     * Los nombres de las fuentes.<br>
     * El estilo stroke-dasharray para sustituir las ',' por ' '. Ejemplo: "2.0,4.0" a "2.0 4.0" 
     * 
     * @param featureTypeStyle Elemento a transformar
     * @param mapAliasAttributes Mapa para asociar alias de atributos a nombres internos
     * @param errorRecursos 
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
 
    
    private static void transformElementsToMapServerFormat(Element element, Map mapAliasAttributes, StringBuffer errorRecursos) throws LocalgisConfigurationException {
        List children = element.getChildren();
        Iterator itChildren = children.iterator();
        while (itChildren.hasNext()) {
            Element child = (Element) itChildren.next();
            if (child.getNamespace().equals(NAMESPACE_OGC) && child.getName().equals(ELEMENT_PROPERTY_NAME)) {
                child.setText((String)mapAliasAttributes.get(child.getValue()));
            } else if (child.getNamespace().equals(NAMESPACE_SLD) && child.getName().equals(ELEMENT_ONLINE_RESOURCE)) {
                String value = child.getAttributeValue(ATTRIBUTE_ONLINE_RESOURCE, NAMESPACE_XLINK);
                try {
					if (value != null) {
					    child.setAttribute(ATTRIBUTE_ONLINE_RESOURCE, transformExternalGraphicToGifFormat(transformUrlFileToUrlHTTP(value)), NAMESPACE_XLINK);					   
					}
				} catch (NoResourceAvailableException e) {
					if (errorRecursos==null)
						errorRecursos=new StringBuffer();
					 errorRecursos.append(e.getMessage()+"\n");
				}
            } else if ((child.getNamespace().getPrefix().equals(ELEMENT_SIZE_PREFIX) && child.getName().equals(ELEMENT_SIZE)) ||
                       (child.getNamespace().equals(NAMESPACE_SLD) && child.getName().equals(ELEMENT_CSS_PARAMETER) && child.getAttributeValue(ATTRIBUTE_NAME) != null && child.getAttributeValue(ATTRIBUTE_NAME).equals(VALUE_STROKE_WIDTH))) {
                Element literalElement = child.getChild(ELEMENT_LITERAL, NAMESPACE_OGC);
                if (literalElement != null) {
                    child.removeChild(ELEMENT_LITERAL, NAMESPACE_OGC);
                    child.setText(literalElement.getValue());
                }
            } else if (child.getNamespace().equals(NAMESPACE_SLD) && child.getName().equals(ELEMENT_CSS_PARAMETER) && child.getAttributeValue(ATTRIBUTE_NAME) != null && child.getAttributeValue(ATTRIBUTE_NAME).equals(VALUE_FONT_FAMILY)) {
                String fontOriginal = child.getText();
                if (fontOriginal != null && fontOriginal.equalsIgnoreCase("Courier New")) {
                    child.setText("Courier");
                } else if (fontOriginal != null && fontOriginal.equalsIgnoreCase("Times New Roman")) {
                    child.setText("Times");
                } else if (fontOriginal != null && fontOriginal.equalsIgnoreCase("Verdana")) {
                    child.setText("Verdana");
                } else if (fontOriginal != null && fontOriginal.equalsIgnoreCase("Arial")) {
                    child.setText("Arial");
                } else {
                    child.setText("Arial");
                }
            } else if (child.getNamespace().equals(NAMESPACE_SLD) && child.getName().equals(ELEMENT_CSS_PARAMETER) && child.getAttributeValue(ATTRIBUTE_NAME) != null && child.getAttributeValue(ATTRIBUTE_NAME).equals(VALUE_STROKE_DASHARRAY)) {
                String valueOriginal = child.getText();
                if (valueOriginal != null) {
                    child.setText(valueOriginal.replaceAll(",", " "));
                }
            } else if (child.getNamespace().equals(NAMESPACE_SLD) && child.getName().equals(ELEMENT_FORMAT)) {
                // Solo se admite formato png y gif. Si viene un formato diferente lo transformamos en gif
                String value = child.getValue();
                if (!value.equalsIgnoreCase("image/png") && !value.equalsIgnoreCase("image/gif") && 
                        !value.equalsIgnoreCase("png") && !value.equalsIgnoreCase("gif")) {
                    child.setText("image/gif");
                }
            }
            transformElementsToMapServerFormat(child, mapAliasAttributes,errorRecursos);
        }
        
    }

    /**
     * Método para transformar una url de tipo file a una url de tipo http
     * @param urlFile La url de tipo file
     * @return La url transformada
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
    private static String transformUrlFileToUrlHTTP(String urlFile) throws LocalgisConfigurationException,NoResourceAvailableException {
        URL url;
        try {
            url = new URL(urlFile);
            if (url.getPath().indexOf(ICONLIB_PATH) != -1) {
            	//Verificamos si el recurso esta accesible
            	
            	String path=Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_DIRECTORY)+File.separator+"htdocs"+File.separator+url.getPath();
            	File f=new File(path);
            	if (!f.exists()){
            		logger.error("No existe el recurso:"+path);
            		System.out.println("No existe el recurso:"+path);

            		throw new NoResourceAvailableException(url.getPath());
            	}
                return Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER)+ STYLES_MAPSERVER_PATH + "/"+url.getPath();
            } else if (url.getPath().indexOf(TEXTURES_PATH) != -1) {
            	String path=Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_DIRECTORY)+File.separator+"htdocs"+File.separator+url.getPath();
            	File f=new File(path);
            	if (!f.exists()){
            		throw new NoResourceAvailableException(url.getPath());
            	}
                return Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER)+ STYLES_MAPSERVER_PATH + "/"+url.getPath().substring(url.getPath().indexOf(TEXTURES_PATH));
            }
        } catch (MalformedURLException e) {
            logger.error("La url ["+urlFile+"]no es válida. No se transforma");
        }
        return urlFile;
    }

    /**
     * Método para transformar una url de un elemento ExternalGraphic de forma que si no es un gif o un png se transforma a gif
     * @param urlFile La url a transformar
     * @return La url transformada
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
    private static String transformExternalGraphicToGifFormat(String url) throws LocalgisConfigurationException {
        // Obtenemos la extension para que en caso de que sea JPG lo pasemos a GIF
        int indexOfExtension = url.lastIndexOf(".");
        if (indexOfExtension != -1) {
            String extension = url.substring(indexOfExtension);
            if (!extension.equalsIgnoreCase(".png") && !extension.equalsIgnoreCase(".gif")) {
                url = url.substring(0, indexOfExtension) + ".gif";
            }
        }
        return url;
    }

    /*
     * Metodo auxiliar utilizado por createLocalgisStyle  
     */
    private static Element getUserStyle(Document sldDoc,String styleName,String layerName,Namespace sldNS) {
        
        Element sldDocRoot = sldDoc.getRootElement();
        Element userStyle = null;
        
        List namedLayerElements = sldDocRoot.getChildren("NamedLayer",sldNS);
        boolean layerFound = false;
        Iterator namedLayerIterator = namedLayerElements.iterator();
        /*Si el SLD tuviera más de un elemento <NamedLayer>, nos quedamos con aquel cuyo nombre sea 
         * igual al del parámetro layerName*/
        while ((namedLayerIterator.hasNext())&&!layerFound) {
            Element namedLayerElement = (Element)namedLayerIterator.next();
            Element nameElement = namedLayerElement.getChild("Name",sldNS);
            if (nameElement.getText().equalsIgnoreCase(layerName)) {
                layerFound = true;
                List userStyleElements = namedLayerElement.getChildren("UserStyle",sldNS);
                boolean styleFound = false;
                Iterator userStyleIterator = userStyleElements.iterator();
                /*Recorremos los elementos <UserStyle> en busca del estilo deseado, si tuviera más de uno*/
                Element userStyleElement = null;
                while ((userStyleIterator.hasNext())&&!styleFound) {
                     userStyleElement = (Element)userStyleIterator.next();
                    Element userStyleName = userStyleElement.getChild("Name",sldNS);
                    if (userStyleName.getText().equalsIgnoreCase(styleName)) {
                        styleFound = true;
                        userStyle = userStyleElement;
                    }
                }
                if(userStyle==null) userStyle = userStyleElement;
            }   
        }
        
        
        return userStyle;
    }

	/**
	 * Método para cambiar la query de la select y poner correctamente los ids
	 * de los municipios y la transformacion al SRID. La query será algo así
	 * como:<br>
	 * select transform("GEOMETRY", ?T) as "GEOMETRY" from Tabla where
	 * Tabla.idMunicipio in (?M)
	 * 
	 * @param selectQuery
	 *            La query a modificar
	 * @param idMunicipios
	 *            Los identificadores de municipios a establecer como una
	 *            coleccion de enteros. Al menos deberia llegar un id de un
	 *            municipio ya que si no la querie seria invalida
	 * @return La query modificada
	 */
    public static String changeSelectQuery(String selectQuery, Collection<Integer> idsMunicipios) {
    	selectQuery = selectQuery.replaceAll("\\?T", "\\$SRID\\$");
    	StringBuffer ids = new StringBuffer();
    	int i = 0;
    	Iterator<Integer> it = idsMunicipios.iterator();
    	while (it.hasNext()) {
			Integer idMunicipio = (Integer) it.next();
			if (i != 0) {
				ids.append(", ");
			}
			ids.append(idMunicipio);
			i++;
		}
    	selectQuery = selectQuery.replaceAll("\\?M", ids.toString());
        return selectQuery;
    }

    /**
     * Método que devuelve si el tipo pasado como parametro es un tipo geometria
     * @param typeAsInt Tipo que se desea comprobar
     * @return Si es un tipo geometria o no
     */
    public static boolean isGeometryType(Short typeAsInt) {
        if (typeAsInt == null) {
            return false;
        } else {
            return typeAsInt.shortValue() == (short)1;
        }
    }
    
    /**
     * Método que devuelve el tipo de geometria como String y en formato GML a partir de un tipo geometria short
     * @param geometryType El tipo de geometria como short
     * @return El tipo de geometria como String y en formato GML
     */
    public static String getGeometryType(Short geometryType) {
        if (geometryType == null) {
            return "";
        } else {
            switch (geometryType.shortValue()) {
            case 0:
                return "gml:AbstractGeometryType";
            case 1:
                return "gml:PointPropertyType";
            case 2:
                return "gml:CurvePropertyType";
            case 3:
                return "gml:LineStringPropertyType";
            case 4:
                return "gml:SurfacePropertyType";
            case 5:
                return "gml:PolygonPropertyType";
            case 6:
                return "gml:MultiGeometryPropertyType";
            case 7:
                return "gml:MultiPointPropertyType";
            case 8:
                return "gml:MultiCurvePropertyType";
            case 9:
                return "gml:MultiLineStringPropertyType";
            case 10:
                return "gml:MultiSurfacePropertyType";
            case 11:
                return "gml:MultiPolygonPropertyType";
            default:
                return "";
            }
        }
    }
    
    /**
     * Método que obtiene un bounding box a partir de una escala, una x, una y, un ancho y un alto de imagen 
     * @param scale Escala
     * @param x X de centrado
     * @param y Y de centrado
     * @param width Ancho de la imagen en pixeles
     * @param height Alto de la imagen en pixeles
     * @return Bounding box que se corresponde con la imagen
     */
    public static BoundingBox getBoundingBox(Scale scale, double x, double y, int width, int height) {
        // Obtenemos la escala normalizada, es decir, numerador entre denominador
        double scaleNormalized = (double) scale.getNumerator()/(double) scale.getDenominator();
        // Obtenemos la resolucion a partir de la escala, las pulgadas por unidad (metros de momento) y los puntos por pulgada
        double resolution = 1.0 / (scaleNormalized * Configuration.INCHES_PER_UNIT * Configuration.DOTS_PER_INCH);

        // Obtenemos el ancho y el alto real de la imagen en metros
        double widthRealImage = (double)width * resolution;
        double heightRealImage = (double)height * resolution;

        // Obtenemos el bounding box para la imagen que queremos devolver
        double minX = x - widthRealImage / 2.0;
        double minY = y - heightRealImage / 2.0;
        double maxX = x + widthRealImage / 2.0;
        double maxY = y + heightRealImage / 2.0;

        return new BoundingBox(new Double(minX), new Double(minY), new Double(maxX), new Double(maxY));
    }
    
    /**
     * Método que devuelve una escala de manera que el bounding box indicado sea visible
     * dentro de una imagen con el ancho y alto indicado. 
     * 
     * @param boundingBox Bounding Box que se desea visualizar
     * @param width Ancho en pixels
     * @param height Altura en pixels
     * @return Escala a la que se debe mostrar la imagen
     */
    public static Scale getScale(BoundingBox boundingBox, int width, int height){
    	
    	// Obtenemos el ancho y el alto en las dimensiones reales
    	double surfaceHeightReal = boundingBox.getMaxy().doubleValue() - boundingBox.getMiny().doubleValue();
    	double surfaceWidthReal = boundingBox.getMaxx().doubleValue() - boundingBox.getMinx().doubleValue();
    	
    	// Obtenemos el ancho y el alto de la imagen en las unidades reales
    	double imageWidthReal = width / Configuration.DOTS_PER_INCH / INCHES_PER_METER;
    	double imageHeightReal = height / Configuration.DOTS_PER_INCH / INCHES_PER_METER;
    	
    	Scale scale;
    	
    	// Calculamos cuanto habría que reducir la imagen para que entre en horizontal
    	double horizontalScale = surfaceWidthReal/imageWidthReal;
    	double verticalScale = surfaceHeightReal/imageHeightReal;
    	
    	// Nos quedamos con la mayor escala, es decir, la escala mas alejada para que entre tanto en horizontal como en vertical
    	scale = new Scale (1, new Double(Math.floor(Math.max(horizontalScale, verticalScale))).intValue());
    	return scale;
    	
    	// Comprobamos la proporcion entre el alto y el ancho de las imagenes
    	// Se añade un 5% mas a la proporcion de la escala, para asegurarse de que se vean
    	// los bordes de la parcela, puesto que en el proceso de conversion se pueden
    	// producir errores de redondeo
//    	if (surfaceWidthReal > surfaceHeightReal){
//    		scale = new Scale(1, new Double(surfaceWidthReal/imageWidthReal + (surfaceWidthReal/imageWidthReal)*0.05).intValue());    		
//    	}
//    	else {
//    		scale = new Scale(1, new Double(surfaceHeightReal/imageHeightReal + (surfaceHeightReal/imageHeightReal)*0.05).intValue());
//    	}
//    	
//    	return scale;
    }
    
    /**
     * Método para ordenar las capas por posicion
     * @param layers Una lista de capas
     * @param wmsLayers Una lista de cpas de servidores externos
     * @return Un conjunto ordenado por posicion de objetos {@link LocalgisLayer} y {@link LocalgisMapServerLayer}
     */
    public static SortedSet joinAndSortLayers(List layers, List wmsLayers) {
        SortedSet result = new TreeSet(new Comparator() {
            public int compare(Object obj1, Object obj2) {
                Integer positionObj1 = new Integer(-1);
                if (obj1 != null && obj1 instanceof LocalgisLayerExt) {
                    positionObj1 = ((LocalgisLayerExt)obj1).getPosition();
                } else if (obj1 != null && obj1 instanceof LocalgisMapServerLayer) {
                    positionObj1 = ((LocalgisMapServerLayer)obj1).getPosition();
                }
                Integer positionObj2 = new Integer(-1);
                if (obj2 != null && obj2 instanceof LocalgisLayerExt) {
                    positionObj2 = ((LocalgisLayerExt)obj2).getPosition();
                } else if (obj2 != null && obj2 instanceof LocalgisMapServerLayer) {
                    positionObj2 = ((LocalgisMapServerLayer)obj2).getPosition();
                }
                return positionObj1.compareTo(positionObj2);
            }
            
            public boolean equals(Object obj) {
                return super.equals(obj);
            }
        });
        
        if (layers != null) {
            Iterator itLayers = layers.iterator();
            while (itLayers.hasNext()) {
                LocalgisLayerExt localgisLayer = (LocalgisLayerExt) itLayers.next();
                result.add(localgisLayer);
            }
        }
        if (wmsLayers != null) {
            Iterator itWMSLayers = wmsLayers.iterator();
            while (itWMSLayers.hasNext()) {
                LocalgisMapServerLayer localgisMapServerLayer = (LocalgisMapServerLayer) itWMSLayers.next();
                result.add(localgisMapServerLayer);
            }
        }
        return result;
    }


}
