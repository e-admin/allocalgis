/**
 * PublishMaps.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administrador.web;

import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisWMSException;
import com.localgis.web.core.model.BoundingBox;
import com.geopista.server.administrador.web.LocalgisLayer;
import com.localgis.web.core.model.LocalgisStyle;
import com.localgis.web.core.utils.FileUtils;
import com.localgis.web.core.wms.WMSConfigurator;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.umn.gis.mapscript.MS_LAYER_TYPE;
import edu.umn.gis.mapscript.classObj;
import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 03-jun-2004
 * Time: 17:56:40
 */
public class PublishMaps {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PublishMaps.class);
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
    
    private static final int GEOMETRY = 0;
    private static final int POINT = 1;
    private static final int LINESTRING = 3;
    private static final int POLYGON = 5;
    private static final int COLLECTION = 6;
    private static final int MULTIPOINT = 7;
    private static final int MULTILINESTRING = 9;
    private static final int MULTIPOLYGON = 11;
    public static final String MAPSERVER_CONFIG_FILE_SKELETON = "localgis_skeleton_layer.map";

    private static final double INCHES_PER_METER = 39.37;
       /**
        * Constante que contiene la cabecera de un fichero de estilos
        */
       private static final String BEGIN_STYLES = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
       "<sld:StyledLayerDescriptor xmlns:sld=\"http://www.opengis.net/sld\" xmlns:java=\"java\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns=\"http://www.opengis.net/sld\" version=\"1.0.0\">\n";

       private String connectionType;
       /**
        * Constante que contiene el final de un fichero de estilos
        */
       private static final String END_STYLES = "</sld:StyledLayerDescriptor>";
       /**
        * Constante del alias de la geometria
        */
       private static final String ALIAS_GEOMETRY = "transform(\"GEOMETRY\",";
       private static String consultaMunicipios = "select id_municipio from entidades_municipios";
       /**
        * Constante del alias del campo id
        */
       private static final String ALIAS_ID = "id";
       
       public PublishMaps(){
    	   int a = 0;
    	   
       }
       
       /**
        * Método para configurar el fichero de estilos para una entidad supramunicipal, un mapa y una configuracion (publica o privada) determinada
        * @param geopistaEntidadSupramunicipal Entidad para la que se desea configurar el servidor de mapas
        * @param localgisMap Mapa para el que se desea configurar el servidor de mapas
        * @param publicConfiguration Si se desea configurar para los mapas publicos (true) o privados (false)
        * @param mapLocalgisLayers Map donde vamos guardando todas las capas para luego generar un fichero de mapa global
        * @param mapLocalgisMapServerLayers Map donde vamos guardando todas las capas externas para luego generar un fichero de mapa global
        * @param mapDefaultLocalgisStyles Map donde vamos guardando los estilos por defecto de todas las capas
        * @param mapServerConnection Conexion del MapServer
        * @param ortofotoExtension Extension de la ortofoto
        * @param ortofotoSrid SRID asociado a la ortofoto
        * @throws LocalgisWMSException Si sucede algun error con el servidor WMS
        * @throws LocalgisConfigurationException Si sucede algun error de configuracion
        */
       public String configureLayerAndStylesFile(LocalgisLayer localgisLayer) {
    	   logger.error("He entrado");
    	   String urlMapServer = "";
    	   try{
           
           String mapServerConnection = null;
           connectionType = Configuration.getPropertyString(Configuration.PROPERTY_DB_CONNECTIONTYPE);
	       if (!connectionType.equals("postgis")) {
	           mapServerConnection = Configuration.getPropertyString(Configuration.PROPERTY_DB_USERNAME)+"/"+Configuration.getPropertyString(Configuration.PROPERTY_DB_PASSWORD)+"@"+Configuration.getPropertyString(Configuration.PROPERTY_DB_ORACLESERVICE);
	       } else {
	           mapServerConnection = "user="+Configuration.getPropertyString(Configuration.PROPERTY_DB_USERNAME)+" password="+Configuration.getPropertyString(Configuration.PROPERTY_DB_PASSWORD)+" host="+Configuration.getPropertyString(Configuration.PROPERTY_DB_HOST)+" port="+Configuration.getPropertyString(Configuration.PROPERTY_DB_PORT)+" dbname="+Configuration.getPropertyString(Configuration.PROPERTY_DB_NAME);
	       }
           Map mapLocalgisLayers = new HashMap();
           Map mapDefaultLocalgisStyles = new HashMap();
           /*
            * Obtenemos el BoundingBox de la entidad
            */
           BoundingBox bbox = getBoundingBoxSpain(localgisLayer.getSrid());
           String maxExtentMunicipio = bbox.getMinx() + " " + bbox.getMiny() + " " + bbox.getMaxx() + " " + bbox.getMaxy();
           // Creamos un StringBuffer para ir almacenando toda la configuracion de los estilos
           StringBuffer stylesConfiguration = new StringBuffer();
           
           // Creamos un StringBuffer para ir almacenando toda la configuracion del mapa
           StringBuffer mapConfiguration = new StringBuffer();
           
           // Escribimos el inicio del fichero de estilos
           stylesConfiguration.append(BEGIN_STYLES);
           
           //Obtenemos la capa que queremos publicar
           int idMap = 1;
           boolean publicConfiguration = true;

           if (localgisLayer.getStyleName() == null || localgisLayer.getStyleName().equals(""))
        	   localgisLayer.setStyleName(localgisLayer.getLayername()+":_:default:"+localgisLayer.getLayername());
    	   logger.error("localgisLayer.getXml() "+localgisLayer.getXml());

           if (localgisLayer.getXml() == null || localgisLayer.getXml().equals("")){
        	   String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">&#xD;<NamedLayer><Name>";
        	   xml = xml.concat(localgisLayer.getLayername());
        	   xml = xml.concat("</Name><UserStyle><Name>");
        	   xml = xml.concat(localgisLayer.getStyleName());
        	   xml = xml.concat("</Name><Title>Default user style</Title><Abstract>Default user style</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name='stroke'>#007d00</CssParameter><CssParameter name='stroke-linecap'>square</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'>1.0</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>");
        	   localgisLayer.setXml(xml);
           }
        	   
           //Configuramos los estilos
           configureLayerStyle(stylesConfiguration, localgisLayer, idMap, mapDefaultLocalgisStyles);
           configureLayer(mapConfiguration, localgisLayer, maxExtentMunicipio, publicConfiguration, mapServerConnection);
           mapLocalgisLayers.put(localgisLayer.getLayerid(), localgisLayer);
           
           // Escribimos el final del fichero de estilos
           stylesConfiguration.append(END_STYLES);

           // Escribimos el fichero de estilos
           writeStylesFile(localgisLayer.getLayerid(), publicConfiguration, stylesConfiguration.toString());

           // Escribimos el fichero de map
           urlMapServer = writeMapFile(localgisLayer.getIdEntidad(), localgisLayer.getLayerid(), publicConfiguration, mapConfiguration.toString(), maxExtentMunicipio, MAPSERVER_CONFIG_FILE_SKELETON, null, mapServerConnection, localgisLayer.getSrid());
  	       String req = "?REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&STYLES=default&TRANSPARENT=TRUE&FORMAT=image/png&WIDTH=1065&HEIGHT=467&LAYERS=";
  	       urlMapServer = urlMapServer.concat(req);
  	       urlMapServer = urlMapServer.concat(localgisLayer.getLayername());
  	       urlMapServer = urlMapServer.concat("&SRS=EPSG:");
  	       urlMapServer = urlMapServer.concat(localgisLayer.getSrid());
  	       urlMapServer = urlMapServer.concat("&BBOX=");
  	       urlMapServer = urlMapServer.concat(bbox.getMinx() + "," + bbox.getMiny() + "," + bbox.getMaxx() + "," + bbox.getMaxy());
           
           // Aplicamos los estilos al fichero .map
           try {
               applyStylesToMap(localgisLayer.getLayerid(), publicConfiguration);
           } catch (Throwable e) {
        	   e.printStackTrace();
               logger.error("Error grave al configurar el sistema "+e.getMessage());
               urlMapServer = "";
           }
    	   }catch(LocalgisWMSException e){
               logger.error("Error grave al configurar el sistema "+e.getMessage());
        	   e.printStackTrace();
               urlMapServer = "";
    	   }catch(LocalgisConfigurationException e){
               logger.error("Error grave al configurar el sistema "+e.getMessage());
        	   e.printStackTrace();
               urlMapServer = "";
    	   }catch(Exception e){
               logger.error("Error grave al configurar el sistema "+e.getMessage());
        	   e.printStackTrace();
    	   }
           return urlMapServer;
       }

       private String writeMapFile(int idEntidad, int idLayer, Boolean publicConfiguration, String mapConfiguration, String maxExtentMunicipio, String nameResourceConfig, String nameResourceExtraConfig, String mapServerConnection, String ortofotoSrid) throws LocalgisWMSException, LocalgisConfigurationException {
           // Escribimos al fichero la configuracion a partir del skeleton y el fichero extra de capas si lo hubiera
           String skeletonString = null;
           try {
               skeletonString = FileUtils.readContentFromResource(nameResourceConfig);
           } catch (FileNotFoundException e) {
               logger.error("El fichero skeleton del servidor de mapas no existe o no se puede leer.", e);
        	   e.printStackTrace();
               throw new LocalgisWMSException("El fichero skeleton del servidor de mapas no existe o no se puede escribir.");
           } catch (IOException e) {
               logger.error("Error al leer el fichero skeleton de configuracion.", e);
        	   e.printStackTrace();
               throw new LocalgisWMSException("Error al leer el fichero skeleton de configuracion.");
           }

           //En el nombre del fichero .map pongo un 0 delante del identificador de layer para que no se confunda con los .map generador
           //en el publicador de mapas
           String urlMapServer = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER)+Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_DIRECTORY)+"/"; 
           urlMapServer += idLayer+ "/";
           
           if (publicConfiguration != null) {
               if (publicConfiguration.booleanValue()) {
                   urlMapServer += "public";
               } else {
                   urlMapServer += "private";
               }
               urlMapServer += "/";
           }
           urlMapServer += Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_FILE);

           String skeletonExtraString = null;
           if (nameResourceExtraConfig != null) {
               try {
                   skeletonExtraString = FileUtils.readContentFromResource(nameResourceExtraConfig);

                   skeletonExtraString = replaceSkeletonVariables(skeletonExtraString, ortofotoSrid, maxExtentMunicipio,  mapConfiguration != null ? mapConfiguration : "", urlMapServer, String.valueOf(idEntidad), mapServerConnection, ortofotoSrid);
                   
                   if (mapConfiguration != null) {
                       mapConfiguration += "\n" + skeletonExtraString;
                   } else {
                       mapConfiguration = skeletonExtraString;
                   }
               } catch (FileNotFoundException e) {
                   logger.info("El fichero skeleton extra del servidor de mapas no existe o no se puede leer.", e);
               } catch (IOException e) {
                   logger.error("Error al leer el fichero skeleton de configuracion.", e);
               }
           }
           // Si tenemos cosas definidos en el skeletonExtra (debería haber capas), las concatenamos en la configuracion
           
           String fileName = getMapFilename(idLayer, publicConfiguration);
           
           // Reemplazo de las variables del skeleton
           skeletonString = replaceSkeletonVariables(skeletonString, ortofotoSrid, maxExtentMunicipio, mapConfiguration != null ? mapConfiguration : "", urlMapServer, String.valueOf(idEntidad), mapServerConnection, ortofotoSrid);
           
           try {
               FileUtils.writeContentToFile(skeletonString, fileName, null);
           } catch (FileNotFoundException e) {
               logger.error("El fichero de configuracion del servidor de mapas no existe o no se puede escribir.", e);
        	   e.printStackTrace();
               throw new LocalgisWMSException("El fichero de configuracion del servidor de mapas no existe o no se puede escribir.");
           } catch (IOException e) {
               logger.error("Error de entrada/salida al escribir el fichero de configuracion del servidor de mapas.", e);
        	   e.printStackTrace();
              throw new LocalgisWMSException("Error de entrada/salida al escribir el fichero de configuracion del servidor de mapas.");
           }
           return urlMapServer;
       }
       
       private String replaceSkeletonVariables(String skeleton, String srid, String maxExtent, String layers, String urlMapServer, String idMunicipio, String mapServerConnection, String ortofotoSrid) throws LocalgisConfigurationException {
           // Lo primero es reemplazar las variables de queries porque suelen tener nuevas variables a expandir
           if (!connectionType.equals("postgis")) {
               skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_DATA, Configuration.MAPSERVER_CONFIG_FILE_MUNICIPIOS_DATA_ORACLESPATIAL);
               skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_DATA, Configuration.MAPSERVER_CONFIG_FILE_PROVINCIAS_DATA_ORACLESPATIAL);
               skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PARCELAS_DATA, Configuration.MAPSERVER_CONFIG_FILE_PARCELAS_DATA_ORACLESPATIAL);
           } else {
               skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_DATA, Configuration.MAPSERVER_CONFIG_FILE_MUNICIPIOS_DATA_POSTGIS);
               skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_DATA, Configuration.MAPSERVER_CONFIG_FILE_PROVINCIAS_DATA_POSTGIS);
               skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PARCELAS_DATA, Configuration.MAPSERVER_CONFIG_FILE_PARCELAS_DATA_POSTGIS);
           }
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ENCODING, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_ENCODING));
//           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROJECTION, Configuration.getPropertyString("mapserverProjection."+srid));
           StringBuffer proyecciones = new StringBuffer("init=epsg:23029\" \"init=epsg:23030\" \"init=epsg:23031\" \"init=epsg:4230\" ");
           proyecciones.append("\"init=epsg:4258\" \"init=epsg:25829\" \"init=epsg:25830\" \"init=epsg:4326\" \"init=epsg:25831");
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROJECTION,proyecciones.toString());
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_SRID, srid);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MAX_EXTENT, maxExtent);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_LAYERS, layers);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ONLINE_RESOURCE, urlMapServer);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ID_MUNICIPIO, idMunicipio);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_CONNECTION, mapServerConnection);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_CONNECTIONTYPE, Configuration.getPropertyString(Configuration.PROPERTY_DB_CONNECTIONTYPE));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTOS_DIRECTORY, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_ORTOFOTOS_DIRECTORY));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTO_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_ORTOFOTO_LAYER_NAME);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTO_EXTENSION, "");
           /*
            * Si aun no hay ortofoto y el parametro ortofotoSrid es null (o vacio) ponemos la proyeccion del mapa
            */
           String ortofotoProjectionTranslated;
           String ortofotoSridTranslated;
           if (ortofotoSrid == null || ortofotoSrid.trim().equals("")) {
               ortofotoProjectionTranslated = Configuration.getPropertyString("mapserverProjection."+srid);
               ortofotoSridTranslated = srid;
           } else {
               ortofotoProjectionTranslated = Configuration.getPropertyString("mapserverProjection."+ortofotoSrid);
               ortofotoSridTranslated = ortofotoSrid;
           }
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTO_PROJECTION, ortofotoProjectionTranslated);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTO_SRID, ortofotoSridTranslated);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_PROVINCIAS_LAYER_NAME);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_OUTLINECOLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_OUTLINECOLOR));
           String provinciasMaxExtent;
           try {
               BoundingBox boundingBoxSpain = getBoundingBoxSpain(srid);
               provinciasMaxExtent = boundingBoxSpain.getMinx() + " " + boundingBoxSpain.getMiny() + " " + boundingBoxSpain.getMaxx() + " " + boundingBoxSpain.getMaxy();
           } catch (LocalgisConfigurationException e) {
               provinciasMaxExtent = Configuration.DEFAULT_SPAIN_MINX + " " + Configuration.DEFAULT_SPAIN_MINY + " " + Configuration.DEFAULT_SPAIN_MAXX + " " + Configuration.DEFAULT_SPAIN_MAXY;        
           }
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_MAX_EXTENT, provinciasMaxExtent);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_NAME_ATTRIBUTE, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_NAME_ATTRIBUTE));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_NAME_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_NAME_COLOR));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_NAME_SIZE, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_NAME_SIZE));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_MUNICIPIOS_LAYER_NAME);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_OUTLINECOLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_MUNICIPIOS_OUTLINECOLOR));

           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_POLYGON_REPORTS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_POLYGON_REPORTS_LAYER_NAME);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_LINE_REPORTS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_LINE_REPORTS_LAYER_NAME);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_POINT_REPORTS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_POINT_REPORTS_LAYER_NAME);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_TABLE_NAME, Configuration.MAPSERVER_CONFIG_FILE_REPORTS_TABLE_NAME);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_IDENTIFIER_COLUMN_NAME, Configuration.MAPSERVER_CONFIG_FILE_REPORTS_IDENTIFIER_COLUMN_NAME);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_IDENTIFIER_VALUE, Configuration.MAPSERVER_CONFIG_FILE_REPORTS_IDENTIFIER_VALUE);
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_POLYGON_SELECTED_OUTLINE_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_POLYGON_SELECTED_OUTLINE_COLOR));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_POLYGON_SELECTED_FILL_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_POLYGON_SELECTED_FILL_COLOR));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_LINE_SELECTED_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_LINE_SELECTED_COLOR));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_POINT_SELECTED_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_POINT_SELECTED_COLOR));
           
           // Sumamos uno para evitar problemas con los redondeos
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_MIN_SCALE, Double.toString(Configuration.MIN_SCALE_MUNICIPIOS + 1));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_MAX_SCALE, Double.toString(Configuration.MAX_SCALE_MUNICIPIOS - 1));
           // Sumamos uno para evitar problemas con los redondeos
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_MIN_SCALE, Double.toString(Configuration.MIN_SCALE_PROVINCIAS + 1));
           skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_MAX_SCALE, Double.toString(Configuration.MAX_SCALE_PROVINCIAS - 1));
           return skeleton;
       }
       
       /*
        * (non-Javadoc)
        * @see com.localgis.web.core.wms.WMSConfigurator#getBBoxSpain(java.lang.String)
        */
       public BoundingBox getBoundingBoxSpain(String srid) throws LocalgisConfigurationException {
           BoundingBox result = new BoundingBox();
           result.setMinx(new Double(Configuration.getPropertyString(Configuration.PROPERTY_SPAIN_MINX+"."+srid)));
           result.setMiny(new Double(Configuration.getPropertyString(Configuration.PROPERTY_SPAIN_MINY+"."+srid)));
           result.setMaxx(new Double(Configuration.getPropertyString(Configuration.PROPERTY_SPAIN_MAXX+"."+srid)));
           result.setMaxy(new Double(Configuration.getPropertyString(Configuration.PROPERTY_SPAIN_MAXY+"."+srid)));
           return result;
       }
   
       /**
        * Devuelve el nombre del fichero de mapas en funcion del id de la entidad supramunicipal,
        * del mapa y de si el mapa es publico o privado. Tanto el id del mapa como
        * si el mapa es publico o privado pueden ser nulos
        * 
        * @param idEntidad Id de la entidad
        * @param idMap Id del mapa
        * @param publicMap Si el mapa es publico o privado
        * @return El nombre del fichero de mapas
        * @throws LocalgisConfigurationException
        */
       private String getMapFilename(int idLayer, Boolean publicMap) throws LocalgisConfigurationException {
           StringBuffer filename = new StringBuffer(Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_DIRECTORY));
           filename.append(Configuration.MAPSERVER_CONFIG_FILE_DIRECTORY).append("/"); 
           if (publicMap != null) {
               if (publicMap.booleanValue()) {
                   filename.append(Configuration.MAPSERVER_CONFIG_FILE_PREFIX_PUBLIC);
               } else {
                   filename.append(Configuration.MAPSERVER_CONFIG_FILE_PREFIX_PRIVATE);
               }
           } else {
               filename.append(Configuration.MAPSERVER_CONFIG_FILE_PREFIX);
           }
           if (idLayer != -1) {
               filename.append(idLayer);
           }
           filename.append(".").append(Configuration.MAPSERVER_CONFIG_FILE_EXTENSION);
           return filename.toString();
       }
       
       /**
        * Escribe un fichero de estilos
        * @param geopistaEntidadSupramunicipal Entidad a la que pertenece
        * @param localgisMap Mapa al que pertenece
        * @param publicConfiguration Configuracion publica o privada del mapa
        * @param stylesConfiguration Configuracion de los estilos
        * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
        * @throws LocalgisWMSException Si sucede algun error con el servidor WMS
        */
       private void writeStylesFile(int idLayer, Boolean publicConfiguration, String stylesConfiguration) throws LocalgisConfigurationException, LocalgisWMSException {
           String fileNameStyles = getStylesFilename(idLayer, publicConfiguration);
           try {
               FileUtils.writeContentToFile(stylesConfiguration.toString(), fileNameStyles, "ISO-8859-1");
           } catch (FileNotFoundException e) {
               logger.error("El fichero de estilos del servidor de mapas no existe o no se puede escribir.", e);
        	   e.printStackTrace();
               throw new LocalgisWMSException("El fichero de estilos del servidor de mapas no existe o no se puede escribir.");
           } catch (IOException e) {
               logger.error("Error de entrada/salida al escribir el fichero de estilos del servidor de mapas.", e);
        	   e.printStackTrace();
               throw new LocalgisWMSException("Error de entrada/salida al escribir el fichero de estilos del servidor de mapas.");
           }
       }
       /**
        * Aplica los estilos SLD a un fichero .map y lo salva. El localgisMap podra ser nulo para aplicar los estilos al fichero global de mapas
        * @param geopistaEntidadSupramunicipal Entidad a la que pertenece el mapa
        * @param localgisMap Mapa al que se desean aplicar los estilos
        * @param publicConfiguration Si el mapa es publico o privado
        * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
        */
       private void applyStylesToMap(int idLayer, Boolean publicConfiguration) throws LocalgisConfigurationException {
           String filenameMap = getMapFilename(idLayer, publicConfiguration);
           mapObj mapObj = new mapObj(filenameMap);
           /*
            * Obtenemos los tipos de las capas antes de aplicar los estilos para
            * evitar que se nos modifiquen los tipos, ya que se ha detectado que a
            * veces se modifica el tipo y pasa a ser annotation (concretamente
            * cuando tenemos un estilo de siempre visible y otro estilo en una
            * escala de 1 a 1000, por ejemplo)
            */
           int numLayers = mapObj.getNumlayers();
           MS_LAYER_TYPE[] typeLayers = new MS_LAYER_TYPE[numLayers];
           for (int i = 0; i < numLayers; i++) {
               typeLayers[i] = mapObj.getLayer(i).getType();
           }
           String stylesURL = getStylesURL(idLayer, publicConfiguration);
           try{
           mapObj.applySLDURL(stylesURL);
           }catch(Exception e){
           }
           /*
            * Para cada capa reestablecemos el tipos para evitar modificaciones y ordenamos los class
            * segun las escalas, siguiendo estas reglas:
            */
           for (int i = 0; i < numLayers; i++) {
               layerObj layer = mapObj.getLayer(i);
               layer.setType(typeLayers[i]);
               sortClasses(layer);
           }
           
           mapObj.save(filenameMap);
           //Eliminamos el fichero de estilos
           File stylesFile = new File(getStylesFilename(idLayer, publicConfiguration));
           if (stylesFile.exists() && stylesFile.canWrite()) {
               if (stylesFile.delete()) {
                   logger.debug("Fichero de estilos \""+stylesFile+"\" eliminado correctamente");
            	   System.out.println("Fichero de estilos \""+stylesFile+"\" eliminado correctamente");
               } else {
                   logger.error("Error al eliminar el fichero de estilos \""+stylesFile+"\" borrado correctamente");
            	   System.out.println("Error al eliminar el fichero de estilos \""+stylesFile+"\" borrado correctamente");
               }
           } else {
               logger.error("El fichero de estilos \""+stylesFile+"\" no existe o no se puede escribir");
        	   System.out.println("El fichero de estilos \""+stylesFile+"\" no existe o no se puede escribir");
           }
       }
       /**
        * Método para ordenar los class de una capa para que el mapserver se
        * comporte igual que el editor gis. La forma de ordenar es la siguiente:<br>
        * 1º por MAXSCALEDENOM: Irá primero la que menor maxscaledenom tenga.<br>
        * 2º por MINSCALEDENOM: En caso de igualdad en maxscaledenom, irá primero
        * el que mayor minscaledenom tenga.<br>
        * 3º por expresion: En caso de igualdad en minscaledenom y de
        * maxscaledenom, irá primero el que tenga una expresion<br>
        * Ordenamos según el algoritmo "bubble sort" porque tenemos un metodo en el
        * objeto layer que justamente hace un swap entre dos posiciones
        * consecutivas (método moveClassDown)
        * 
        * @param layer
        *            Capa a ordenar
        */
       private void sortClasses(layerObj layer) {
           int numClasses = layer.getNumclasses();
           boolean swapped;
           int i = numClasses;
           do {
               swapped = false;
               i = i - 1;
               for (int j = 0; j < i; j++) {
                   classObj class1 = layer.getClass(j);
                   classObj class2 = layer.getClass(j+1);
                   if ((class2.getMaxscaledenom() < class1.getMaxscaledenom()) ||
                       (class1.getMaxscaledenom() == class2.getMaxscaledenom() && class2.getMinscaledenom() > class1.getMinscaledenom()) ||
                       (class1.getMaxscaledenom() == class2.getMaxscaledenom() && class1.getMinscaledenom() == class2.getMinscaledenom() && class2.getExpressionString() != null && class1.getExpressionString() == null)) {
                       layer.moveClassDown(j);
                       swapped = true;
                   }
               }
           } while (swapped);
       }       
       
       /**
        * Configura el estilo de una capa de un mapa y la almacena en un
        * StringBuffer y, en caso de que sea un estilo por defecto lo almacena en
        * el map pasado como parametro con clave el LocalgisLayer
        * 
        * @param stylesConfiguration
        *            StringBuffer donde se va almacenando la configuracion
        * @param localgisLayer
        *            Capa cuyo estilo se desea a configurar
        * @param idMap
        *            Id del mapa al que pertenece la capa
        * @param mapDefaultLocalgisStyles Map donde vamos guardando los estilos por defecto de cada capa
        * @throws LocalgisConfigurationException
        *             Si ocurre algun error de configuracion
        */
       private void configureLayerStyle(StringBuffer stylesConfiguration, LocalgisLayer localgisLayer, Integer idMap, Map mapDefaultLocalgisStyles) throws LocalgisConfigurationException, LocalgisWMSException {
    	   String layerName = localgisLayer.getLayername().toString();
     	   LocalgisStyle localgisStyle = new LocalgisStyle();
     	   String styleName = localgisLayer.getStyleName();
    	   localgisStyle.setStylename(styleName);
           String xml=localgisLayer.getXml();
           String mapAliasAttributes = localgisLayer.getMapaAtributos();
     	   
           localgisStyle = createLocalgisStyle(xml, styleName, layerName, createAliasMap(mapAliasAttributes));
           logger.error("localgisStyle.getFeaturetypestyles() "+localgisStyle.getFeaturetypestyles());
           /*
            * TODO: Hay que comprobar que esto es correcto cuando se arregle el bug
            * que hay en el editor al modificar estilos por defecto
            * 
            * Si el estilo tiene un nombre del tipo default:nombreCapa lo metemos
            * siempre en el Map y sobreescribimos el que hubiera. Sin embargo, si
            * el estilo tiene un nombre del tipo nombreCapa:_:default:nombreCapa
            * solo lo metemos en el map en el caso de que no hubiera ya un estilo
            * asociado a la capa. En otro caso no hacemos nada
            */
           if (styleName.equals("default:"+layerName)) {
               mapDefaultLocalgisStyles.put(layerName, localgisStyle);
           } else if (styleName.equals(layerName + ":_:default:"+layerName)) {
               if (!mapDefaultLocalgisStyles.containsKey(styleName)) {
                   mapDefaultLocalgisStyles.put(layerName, localgisStyle);
               }
           }
           configureStyle(stylesConfiguration, localgisLayer.getLayername().toString(), localgisStyle);
       }
       
       private Map createAliasMap(String cadenaAlias){
    	   Map mapAlias = new HashMap();
    	   String[] arrayElementos = cadenaAlias.split(";");
    	   int n = arrayElementos.length;
    	   for (int i=0;i<n;i++){
    		   String[] arrayPares = arrayElementos[i].split("=");
    		   if (arrayPares.length<2)
    			   mapAlias.put(arrayPares[0], null);
    		   else
    			   mapAlias.put(arrayPares[0], arrayPares[1]);
    	   }
    	   return mapAlias;
       }
       
       /**
        * Configura el estilo de una capa de un mapa y la almacena en un
        * StringBuffer
        * 
        * @param stylesConfiguration
        *            StringBuffer donde se va almacenando la configuracion
        * @param layerName
        *            Nombre de la capa
        * @param localgisStyle
        *            Estilo a configurar
        * @throws LocalgisConfigurationException
        *             Si ocurre algun error de configuracion
        */
       private void configureStyle(StringBuffer stylesConfiguration, String layerName, LocalgisStyle localgisStyle) throws LocalgisConfigurationException, LocalgisWMSException {
           stylesConfiguration.append("<NamedLayer>\n");
           stylesConfiguration.append("<Name>"+layerName+"</Name>\n");
           stylesConfiguration.append("<UserStyle>\n");
           stylesConfiguration.append(localgisStyle.getFeaturetypestyles());
           stylesConfiguration.append("</UserStyle>\n");
           stylesConfiguration.append("</NamedLayer>\n");
       }
       
       /**
        * Configura una capa y la almacena en un StringBuffer
        * @param configuration StringBuffer donde se va almacenando la configuracion
        * @param localgisLayer Capa a configurar
        * @param geopistaEntidadSupramunicipal Entidad que estamos configurando
        * @param maxExtents Extension del municipio como String
        * @param publicConfiguration Si se desea configurar para los mapas publicos (true) o privados (false)
        * @param mapServerConnection Conexion del MapServer
        * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
        */
       private void configureLayer(StringBuffer configuration, LocalgisLayer localgisLayer, String maxExtents, Boolean publicConfiguration, String mapServerConnection) throws LocalgisConfigurationException {
           configuration.append("LAYER\n");
           configuration.append("\tNAME \"").append(localgisLayer.getLayername()).append("\"\n");   
           configuration.append("\tSTATUS OFF\n");   
           configuration.append("\tDEBUG OFF\n");    
           configuration.append("\tDUMP true\n");  
           int geometrytype = localgisLayer.getGeometrytype();
           switch (geometrytype){
           		case 1:
                    configuration.append("\tTYPE POINT\n");break;   
           		case 3:
                    configuration.append("\tTYPE LINE\n");break;   
           		case 5:
                    configuration.append("\tTYPE POLYGON\n");break;   
           		case 7:
                    configuration.append("\tTYPE POINT\n");break;   
           		case 9:
                    configuration.append("\tTYPE LINE\n");break;   
           		case 11:
                    configuration.append("\tTYPE POLYGON\n");break;   
           		default:
                    configuration.append("\tTYPE POLYGON\n");   
           }
           configuration.append("\tCONNECTIONTYPE "+Configuration.getPropertyString(Configuration.PROPERTY_DB_CONNECTIONTYPE)+"\n");   
           configuration.append("\tCONNECTION \""+mapServerConnection+"\"\n");
           /*
            * Escapamos las comillas simples. Para ello tenemos que poner en el
            * string dos backslashes para que cuando se vuelquen al fichero se
            * mantenga una de ellos. Al tratar con expresiones regulares habrá que
            * poner cuatro backslashes en la expresion a reemplazar para conseguir
            * uno en el resultado
            */
           String layerQuery = localgisLayer.getLayerquery();
           layerQuery = layerQuery.replaceAll("'", "&#39;").replaceAll("\"", "&#34;");
           layerQuery = layerQuery.replaceAll("[?][M]", consultaMunicipios);
           layerQuery = layerQuery.replaceAll("[?][T]", localgisLayer.getSrid());
           if (!connectionType.equals("postgis")) {
               configuration.append("\tDATA 'GEOMETRY from (").append(layerQuery).append(" ) USING UNIQUE "+ALIAS_ID+" SRID ").append(localgisLayer.getSrid()).append("'\n");
           } else {
               configuration.append("\tDATA '").append(ALIAS_GEOMETRY).append(localgisLayer.getSrid()+")").append(" from (").append(layerQuery).append(" ) AS ").append(localgisLayer.getLayername()).append(" USING UNIQUE ").append(ALIAS_ID).append(" USING SRID=").append(localgisLayer.getSrid()).append("'\n");
           }
           configuration.append("\tMETADATA\n"); 
           configuration.append("\t\t\"wms_featureinfoformat\" \"gml\"\n");    
           configuration.append("\t\t\"wms_title\" \"").append(localgisLayer.getLayername()).append("\"\n");    
           configuration.append("\t\t\"wms_srs\" \"23029 23030 23031 4230 4258 4326 25829 25830 25831\"");    
           if (localgisLayer.getTime() != null && !localgisLayer.getTime().equals("")) {
               configuration.append("\t\t\"wms_timeextent\" \"").append(localgisLayer.getTime()).append("\"\n");;
               configuration.append("\t\t\"wms_timeitem\" \"").append(localgisLayer.getColumnTime()).append("\"\n");            
           }
           // Obtenemos los atributos de la capa
           Short mapPublic = ConstantesSQL.FALSE;
           if (publicConfiguration.booleanValue()) {
               mapPublic = ConstantesSQL.TRUE;
           }
/*           Collection attributes = localgisAttributeDAO.selectAttributesByLayer(localgisLayer.getLayerid());
           if (attributes != null && attributes.size() > 0) {
               String includeItems = null;
               Iterator itAttributes = attributes.iterator();
               while (itAttributes.hasNext()) {
                   LocalgisAttribute attribute = (LocalgisAttribute) itAttributes.next();
                   if (includeItems == null) {
                       includeItems = attribute.getAttributename();
                   } else {
                       includeItems += ","+attribute.getAttributename();
                   }
               }
               configuration.append("\t\t\"gml_include_items\" \"").append(includeItems).append("\"\n");
           } else {
               configuration.append("\t\t\"gml_include_items\" \"all\"\n");
           }
           // Obtenemos los atributos restringidos
           Collection restrictedAttributes = localgisAttributeDAO.selectRestrictedAttributesByIdLayerGeopistaAndMapPublic(localgisLayer.getLayeridgeopista(), mapPublic);
           // Siempre restringimos el ID_LOCALGIS, que es el rowid o el oid
           String excludeItems = ALIAS_ID;
           if (restrictedAttributes != null && restrictedAttributes.size() > 0) {
               Iterator itRestrictedAttributes = restrictedAttributes.iterator();
               while (itRestrictedAttributes.hasNext()) {
                   LocalgisAttribute attribute = (LocalgisAttribute) itRestrictedAttributes.next();
                   excludeItems += ","+attribute.getAttributename();
               }
           }*/
           String excludeItems = ALIAS_ID;
           configuration.append("\t\t\"gml_exclude_items\" \"").append(excludeItems).append("\"\n");
           configuration.append("\tEND\n");  
           // Ponemos un simbolo por defecto por si acaso aunque en principio todo
           // los estilos se generan a traves de un SLD especifico.
           configuration.append("\tCLASS\n");
           configuration.append("\t\tCOLOR 0 0 0\n");
           configuration.append("\t\tOUTLINECOLOR 0 0 0\n");
           configuration.append("\tEND\n");
           configuration.append("\tEXTENT ").append(maxExtents).append("\n");
           configuration.append("\tTRANSPARENCY 85\n");
           configuration.append("\tTEMPLATE \"templates/feature.html\"\n");
           configuration.append("\tHEADER \"templates/header_feature.html\"\n");            
           configuration.append("\tFOOTER \"templates/footer_feature.html\"\n");
           configuration.append("\tLABELCACHE ON\n");  
           configuration.append("\tPOSTLABELCACHE FALSE\n");  
           configuration.append("END\n\n");  
       }
       
       /**
        * Devuelve el nombre del fichero de estilos en funcion del id de la entidad
        * supramunicipal, del mapa y de si el mapa es publico o privado. El id del
        * mapa puede ser nulo por si se trata del fichero de estilos global
        * 
        * @param idEntidad
        *            Id de la entidad
        * @param idMap
        *            Id del mapa
        * @param publicMap
        *            Si el mapa es publico o privado
        * @return El nombre del fichero de estilos
        * @throws LocalgisConfigurationException
        */
       private String getStylesFilename(int idLayer, Boolean publicMap) throws LocalgisConfigurationException {
           String fileNameStyles = "";
           if (idLayer != -1) {
               fileNameStyles += idLayer;
           }
           fileNameStyles += "."+Configuration.MAPSERVER_STYLES_FILE_EXTENSION;
           if (publicMap.booleanValue()) {
               fileNameStyles = Configuration.MAPSERVER_STYLES_FILE_PREFIX_PUBLIC + fileNameStyles;
           } else {
               fileNameStyles = Configuration.MAPSERVER_STYLES_FILE_PREFIX_PRIVATE + fileNameStyles;
           }
           fileNameStyles = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_DIRECTORY)+Configuration.MAPSERVER_STYLES_FILE_DIRECTORY+File.separator + fileNameStyles;
           return fileNameStyles;
       }
       
       /*
        * (non-Javadoc)
        * @see com.localgis.web.core.wms.WMSConfigurator#getStylesURL(java.lang.Integer, java.lang.Integer, java.lang.Boolean)
        */
       private String getStylesURL(int idLayer, Boolean publicMap) throws LocalgisConfigurationException {
           String stylesURL = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER) + Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_STYLES_DIRECTORY) + "/";
           if (publicMap != null && publicMap.booleanValue()) {
               stylesURL += Configuration.MAPSERVER_STYLES_FILE_PREFIX_PUBLIC;
           } else {
               stylesURL += Configuration.MAPSERVER_STYLES_FILE_PREFIX_PRIVATE;
           }
           if (idLayer != -1) {
               stylesURL += idLayer;
           }
           stylesURL += "." + Configuration.MAPSERVER_STYLES_FILE_EXTENSION;
           return stylesURL;
       }
       /**
        * Método auxiliar para crear un objeto LocalgisStyle a partir de un xml que contiene una definicion completa de estilos, el nombre del estilo y el nombre de la capa asociada al estilo 
        * @param styleXML Definicion completa de estilo
        * @param styleName Nombre del estilo
        * @param layerName Nombre de la capa para la que se esta creando el estilo
        * @param mapAliasAttributes Mapa para asociar alias de atributos a nombres internos
        * @return Un objeto LocalgisStryle
        * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
        */
       public static LocalgisStyle createLocalgisStyle(String styleXML, String styleName, String layerName, Map mapAliasAttributes) throws LocalgisConfigurationException {
           StringReader sr = new StringReader(styleXML);
           SAXBuilder builder = new SAXBuilder();
           Document doc = null;
           try {
               doc = builder.build(sr);
           } catch (JDOMException e) {
               logger.error("Error al procesar el estilo", e);
               e.printStackTrace();
               return null;
           } catch (IOException e) {
               logger.error("Error de entrada salida al procesar el estilo", e);
               e.printStackTrace();
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

           transformElementsToMapServerFormat(featureTypeStyle, mapAliasAttributes);

           XMLOutputter outputter = new XMLOutputter();
           String featureTypeStyleAsString = outputter.outputString(featureTypeStyle);
           
           LocalgisStyle localgisStyle = new LocalgisStyle();
           localgisStyle.setStylename(userStyleName != null ? userStyleName : "");
           localgisStyle.setStyletitle(userStyleTitle != null ? userStyleTitle : "");
           localgisStyle.setStyleabstract(userStyleAbstract != null ? userStyleAbstract : "");
           localgisStyle.setFeaturetypestyles(featureTypeStyleAsString != null ? featureTypeStyleAsString : "");
           return localgisStyle;
           
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
        * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
        */
       private static void transformElementsToMapServerFormat(Element element, Map mapAliasAttributes) throws LocalgisConfigurationException {
           List children = element.getChildren();
           Iterator itChildren = children.iterator();
           while (itChildren.hasNext()) {
               Element child = (Element) itChildren.next();
               if (child.getNamespace().equals(NAMESPACE_OGC) && child.getName().equals(ELEMENT_PROPERTY_NAME)) {
                   child.setText((String)mapAliasAttributes.get(child.getValue()));
               } else if (child.getNamespace().equals(NAMESPACE_SLD) && child.getName().equals(ELEMENT_ONLINE_RESOURCE)) {
                   String value = child.getAttributeValue(ATTRIBUTE_ONLINE_RESOURCE, NAMESPACE_XLINK);
                   if (value != null) {
                       child.setAttribute(ATTRIBUTE_ONLINE_RESOURCE, transformExternalGraphicToGifFormat(transformUrlFileToUrlHTTP(value)), NAMESPACE_XLINK);
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
               transformElementsToMapServerFormat(child, mapAliasAttributes);
           }
           
       }
       /**
        * Método para transformar una url de tipo file a una url de tipo http
        * @param urlFile La url de tipo file
        * @return La url transformada
        * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
        */
       private static String transformUrlFileToUrlHTTP(String urlFile) throws LocalgisConfigurationException {
           URL url;
           try {
               url = new URL(urlFile);
               if (url.getPath().indexOf(ICONLIB_PATH) != -1) {
                   return Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER)+ STYLES_MAPSERVER_PATH + "/"+url.getPath();
               } else if (url.getPath().indexOf(TEXTURES_PATH) != -1) {
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
}
