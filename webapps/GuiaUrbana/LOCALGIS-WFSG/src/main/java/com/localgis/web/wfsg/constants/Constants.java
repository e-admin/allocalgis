/**
 * Constants.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg.constants;

public final class Constants {
	/**
	 * private Constructor.
	 */
	private Constants() {
		// empty
	}
    
    /** Empty String */
    public static final String EMPTY_STRING = "";
    
    /** Test server used at the beginning of the project. The XML that it is devolving are different from the other styles */
    public static final int STYLE_PRUEBA = 0;
    
    /** Constant used to set the style of the IDEE Server. The XML have a different format than the others */
    public static final int STYLE_IDEE = 1;
    
    /** Constant used to set the style of a WFSG World standard Server. The XML have a different format than the others */
    public static final int STYLE_STANDARD = 2;
    
    /** Part of the URL that throw the getCapabilities on the WFSG Server */
    public static final String URL_GETCAPABILITIES = "REQUEST=GetCapabilities&version=1.1.0&SERVICE=WFS";
    
    /** First part of the URL that throw the getDescribeFeatureType on the WFSG Server */
    public static final String URL_GETDESCRIBEFEATURE_PART1 = "REQUEST=DescribeFeatureType&version=1.1.0&SERVICE=WFS&TYPENAME=";
    
    /** Second part of the URL that throw the getDescribeFeatureType on the WFSG Server */
    public static final String URL_GETDESCRIBEFEATURE_PART2 = "&NAMESPACE=xmlns(app=http://www.deegree.org/app)";
    
    /** First part of the URL that throw the getFeatures command on a standard Server */
    public static final String URL_STANDARD_GETFEATURE_PART1="REQUEST=GetFeature&version=1.1.0&SERVICE=WFS&TYPENAME=";

    /** Second part of the URL that throw the getFeatures command on a standard Server */
    public static final String URL_STANDARD_GETFEATURE_PART2="&NAMESPACE=xmlns(app=http://www.deegree.org/app)";
    
    /** before filtering on a standard Server*/
    public static final String URL_STANDARD_GETFEATURE_STARTFILTER = "&FILTER=<Filter>";
    
    /** after filtering on a standard Server*/
    public static final String URL_STANDARD_GETFEATURE_ENDFILTER = "</Filter>";
    
    /** defining "property is like" on an IDEE Server*/
    public static final String URL_IDEE_PROPERTYISLIKE_START = "<PropertyIsLike wildCard=\"*\" singleChar=\"_\" escapeChar=\"|\">";
    
    /** after defining "property is like" on an IDEE Server*/
    public static final String URL_IDEE_PROPERTYISLIKE_END = "</PropertyIsLike>";
    
    /** defining "property is like" on a WFSG compatible Server*/
    public static final String URL_STANDARD_PROPERTYISLIKE_START = "<PropertyIsLike wildCard=\"*\" singleChar=\"#\" escapeChar=\"!\">";
    
    /** after defining "property is like" on a WFSG compatible Server*/
    public static final String URL_STANDARD_PROPERTYISLIKE_END = "</PropertyIsLike>";
    
    /** defining property name on a STANDARD Server*/
    public static final String URL_STANDARD_PROPERTYNAME_BEFORE = "<PropertyName>";
    
    /** after defining property name on a STANDARD Server*/
    public static final String URL_STANDARD_PROPERTYNAME_AFTER = "</PropertyName>";
    
    /** And Start Tag used on the URL for the getFeature on a STANDARD Server */
    public static final String URL_STANDARD_AND_START = "<And>";
    
    /** And Start Tag used on the URL for the getFeature on a STANDARD Server */
    public static final String URL_STANDARD_AND_END = "</And>";
    
    /** Property is equal to tag used on the URL for the getFeature on a STANDARD Server */
    public static final String URL_STANDARD_PROPERTYISEQUALTO_START = "<PropertyIsEqualTo>";
    
    /** Property is equal to tag used on the URL for the getFeature on a STANDARD Server */
    public static final String URL_STANDARD_PROPERTYISEQUALTO_END = "</PropertyIsEqualTo>";
    
    /** defining literal on a STANDARD Server*/
    public static final String URL_STANDARD_LITERAL_BEFORE = "<Literal>";
    
    /** after defining literal on a STANDARD Server*/
    public static final String URL_STANDARD_LITERAL_AFTER = "</Literal>";

    /** XML part used in the XML query posted to the standard Server */
    public static final String URL_STANDARD_XML_POST_ENCODING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    /** XML part used in the XML query posted to the standard Server */
    public static final String URL_STANDARD_XML_POST_GETFEATURE_START = "<wfs:GetFeature version=\"1.1.0\" outputFormat=\"text/xml; subtype=gml/3.1.1\"";
    
    /** XML part used in the XML query posted to the standard Server */
    public static final String URL_STANDARD_XML_POST_NAMESPACE_1 = " xmlns:wfs=\"http://www.opengis.net/wfs\">";
    
    /** XML part used in the XML query posted to the standard Server */
    public static final String URL_STANDARD_XML_POST_NAMESPACE_2 = " xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\"";
    
    /** XML part used in the XML query posted to the standard Server */
    public static final String URL_STANDARD_XML_POST_NAMESPACE_3 = " xmlns:wfs=\"http://www.opengis.net/wfs\">";
    
    /** XML part used in the XML query posted to the standard Server */
    public static final String URL_STANDARD_XML_POST_QUERY_START = " <wfs:Query xmlns:app=\"http://www.deegree.org/app\" typeName=";
    
    /** XML part used in the XML query posted to the standard Server */
    public static final String URL_STANDARD_XML_POST_FILTER_START = " <Filter xmlns=\"http://www.opengis.net/ogc\">";
    
    /** XML part used in the XML query posted to the standard Server */
    public static final String URL_STANDARD_XML_POST_QUERY_END = " </wfs:Query>";
    
    /** XML part used in the XML query posted to the standard Server */
    public static final String URL_STANDARD_XML_POST_GETFEATURE_END = " </wfs:GetFeature>";
    
    /** Content type */
    public static final String CONTENT_TYPE = "Content-type";
    
    /** Text XML */
    public static final String TEXT_XML = "text/xml";
    
    /** Searched entity for an IDEE Server */
    public static final String SEARCHED_ENTITY_IDEE = "Entidad";
    
    /** Searched entity for a TEST Server */
    public static final String SEARCHED_ENTITY_PRUEBA = "app:Via";
    
    /** Searched entity for a WFSG Specific Server */
    public static final String SEARCHED_ENTITY_STANDARD = "app:Municipalities";
    
    /** Portal for a TEST Server */
    public static final String ENTIDAD_PORTAL = "app:Portal";
    
    /** property numero for a TEST server on a portal request*/
    public static final String PORTAL_NUMERO = "nombreEntidad_nombre";
    
    /** property numero for a TEST server on a portal request*/

    public static final String PORTAL_VIA = "entidadRelacionada_idEntidad";
    
    /** Tag Operation Metadata used in the getCapabilities request */
    public static final String TAG_OPERATION_METADATA = "OperationsMetadata";
    
    /** Tag Operation used in the getCapabilities request */
    public static final String TAG_OPERATION = "Operation";
    
    /** Tag FeatureTypeList used in the getCapabilities request */
    public static final String TAG_FEATURE_TYPE_LIST = "FeatureTypeList";
    
    /** Tag FeatureType used in the getCapabilities request */
    public static final String TAG_FEATURE_TYPE = "FeatureType";
    
    /** Tag Name used in the getCapabilities request */
    public static final String TAG_NAME = "Name";
    
    /** Tag SRS used in the getCapabilities request for a STANDARD server */
    public static final String TAG_STANDARD_SRS = "DefaultSRS";
    
    /** Tag SRS used in hte getCapabilities request for an IDEE server */
    public static final String TAG_IDEE_SRS = "SRS";
    
    
    /** Tag sequence used in the getDescribeFeatureType request */
    public static final String TAG_SEQUENCE = "sequence";
    
    /** Tag extension used in the getDescribeFeatureType request */
    public static final String TAG_EXTENSION = "extension";
    
    /** Tag complexContent used in the getDescribeFeatureType request */
    public static final String TAG_COMPLEXCONTENT = "complexContent";
    
    /** Tag element used in the getDescribeFeatureType request */
    public static final String TAG_ELEMENT = "element";
    
    /** Tag complexType used in the getDescribeFeatureType request */
    public static final String TAG_COMPLEXTYPE = "complexType";
    
    /** Tag featureMember used in the getFeature request */
    public static final String TAG_FEATURE_MEMBER = "featureMember";
    
    /** Tag Entidad used in the getFeature request */
    public static final String TAG_ENTIDAD = "Entidad";
    
    /** Tag nombre used in the getFeature request */
    public static final String TAG_NOMBRE = "nombre";
    
    /** Tag nombreEntidad used in the getFeature request */
    public static final String TAG_NOMBRE_ENTIDAD = "nombreEntidad";
    
    /** Tag claseNombre used in the getFeature request */
    public static final String TAG_CLASENOMBRE = "claseNombre";
    
    /** Tag preferente used in the getFeature request */
    public static final String PREFERENTE = "Preferente";
    
    /** Tag X used in the getFeature request */
    public static final String TAG_POS_X = "X";
    
    /** Tag Y used in the getFeature request */
    public static final String TAG_POS_Y = "Y";
    
    /** Tag Type used in the getFeature request */
    public static final String TAG_TYPE = "tipo";
    
    /** Tag Town used in the getFeature request */
    public static final String TAG_TOWN = "municipio";
    
    /** Tag Town used in the getFeature request */
    public static final String TAG_COUNTY = "provincia";
    
    /** Tag Language used in the getFeature request */
    public static final String TAG_LANGUAGE = "idioma";
    
    /** Tag MultiLineString used in the getFeature request */
    public static final String TAG_MULTILINE_STRING = "MultiLineString";
    
    /** Tag LineString used in the getFeature request */
    public static final String TAG_LINE_STRING = "LineString";
    
    /** Tag fid used in the getFeature request */
    public static final String TAG_FID = "fid";
    
    /** Tag gid used in the getFeature request */
    public static final String TAG_GID = "gid";
    
    /** Tag geographicIdentifier used in the getFeature request */
    public static final String TAG_GEOGRAPHIC_IDENTIFIER = "geographicIdentifier"; 
    
    /** Tag geographicIdentifier used in the getFeature request */
    public static final String TAG_GEOGRAPHIC_EXTENT = "geographicExtent";
    
    
    /** Tag position used in the getFeature request */
    public static final String TAG_POSITION = "position"; 
    
    /** Tag coordinates used in the getFeature request */
    public static final String TAG_COORDINATES = "coordinates";
    
    /** Attribute name used to get name attribute in some tags */
    public static final String ATT_NAME = "name";
    
    /** Attribute type used to get type attribute in some tags */
    public static final String ATT_TYPE = "type";
    
    /** Star Wildcard */
    public static final String STAR_WILDCARD = "*";
    
    /** Percent Wildcard */
    public static final String PERCENT_WILDCARD = "%";
    
    
    /** Idioma: Español */
    public static final String LANG_ESP = "esp";
    
    /** <ServiceExceptionReport> exception */
    public static final String SERVICE_EXCEPTION_REPORT = "<ServiceExceptionReport>";
    
    /** idee Exception */
    public static final String IDEE_SERVICE_EXCEPTION_REPORT = "<OGCWebServiceException>";
    
    /** app prefix used for Namespace creation */
    public static final String NAMESPACE_APP_PREFIX = "app";
    
    /** app prefix used for Namespace creation */
    public static final String NAMESPACE_APP_URI = "http://www.deegree.org/app";
    
    /** gml prefix used for Namespace creation */
    public static final String NAMESPACE_GML_PREFIX = "gml";
    
    /** gml prefix used for Namespace creation */
    public static final String NAMESPACE_GML_URI = "http://www.opengis.net/gml";
    
    /** colon caracter used in String manipulations */
    public static final String COLON = ":";
    
    /** full-stop caracter used in String manipulations */
    public static final String FULL_STOP = ".";
    
    /** comma caracter used in String manipulations */
    public static final String COMMA = ",";
    
    /** SPACE caracter used in String manipulations */
    public static final String SPACE = " ";
    
    /** Quotation mark used in String manipulations */
    public static final String QUOTATION_MARK = "\"";
    
    /** slash used in String manipulations */
    public static final String SLASH = "/";
    
    /** Timeout used for server calls */
    public static final int TIMEOUT_WFS_SERVER = 20000;

}
