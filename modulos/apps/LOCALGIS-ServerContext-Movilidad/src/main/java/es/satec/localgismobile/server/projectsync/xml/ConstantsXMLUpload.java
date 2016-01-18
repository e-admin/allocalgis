/**
 * ConstantsXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml;

public class ConstantsXMLUpload {
	
	public static final String PROP_GEOPISTA_CON_SERVER = "localgis.server.admcar";
	
	public static final String NUM_EXP_PREFIX = "E_";
	public static final String LOCALE = "es_ES";
	
	//atributos del XML
	public static final String ATT_ID = "id";
	public static final String ATT_ID_FEATURE = "v1";
	public static final String ATT_ID_LAYER = "l1";
	public static final String ATT_ID_MUNICIPIO_FEATURE = "v2";
	public static final String ATT_SYSTEM_ID_LAYER = "systemId";
	public static final String ATT_CHANGE_TYPE = "changeType";
	public static final String ATT_IMAGE_URLS = "imageURLs";
	public static final String ATT_DESPX = "despX";
	public static final String ATT_DESPY = "despY";
	public static final String ATT_CHANGE_TIMESTAMP = "changeTimestamp";
	public static final String ATT_OPACITY = "opacity";
	public static final String ATT_VIEWBOX = "viewBox";
	public static final String ATT_GEOTYPE_PATH = "path";
	public static final String ATT_GEOTYPE_POLYLINE = "polyline";
	public static final String ATT_GEOTYPE_ELLIPSE = "ellipse";
	public static final String ATT_GEOTYPE_CIRCLE = "circle";
	
	//códigos de respuesta
	public static final int RESPONSE_CODE_OK = 0;
	public static final int RESPONSE_CODE_FEATURES_ERROR = 1;
	public static final int RESPONSE_CODE_SERVER_ERROR = 2;
	public static final int RESPONSE_CODE_FEATURES_PERMISSION_ERROR = 3;
	
	//máscaras de comparación para saber el tipo de operación
	public static final int MASK_CHANGE_TYPE_INS = 1;
    public static final int MASK_CHANGE_TYPE_MOD = 1<<1;
    public static final int MASK_CHANGE_TYPE_DEL = 1<<2;
    public static final int MASK_CHANGE_TYPE_METADATA = 1<<3;
	public static final String ATT_REFLECTMETHOD = "rm";
	public static final String ATT_UPDATABLEMETHOD = "updatable";
	public static final String ATT_CLASSID = "classId";
	public static final String TAG_ITEM = "item";
	public static final String TAG_ITEMLIST = "itemlist";
	public static final String TAG_SUBITEM = "subitem";

    //otras constantes
    public static final String SPLIT_CHAR_ATT_URL = " ";
    public static final String RESOURCE_IMAGE_TYPE = "image";
    
}
