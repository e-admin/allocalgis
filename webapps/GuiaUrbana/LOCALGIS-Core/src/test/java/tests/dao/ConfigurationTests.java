/**
 * ConfigurationTests.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import java.math.BigDecimal;

import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.model.LocalgisAttribute;
import com.localgis.web.core.model.LocalgisCSS;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisLegend;
import com.localgis.web.core.model.LocalgisLegendKey;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.LocalgisMapLayer;
import com.localgis.web.core.model.LocalgisMapServerLayer;
import com.localgis.web.core.model.LocalgisMarker;
import com.localgis.web.core.model.LocalgisRestrictedAttribute;
import com.localgis.web.core.model.LocalgisStyle;

public class ConfigurationTests {

    public static final String LOCALE = "es_ES";
    public static final String USER_VALID = "sysadmin";
    public static final String PASSWORD_USER_VALID = "sysgeopass";
    public static final String USER_INVALID = "syssuperuser";
    public static final String PASSWORD_USER_INVALID = "Sysgeopass";
    public static final String ROL_USER_VALID = "Geopista.Visualizador.Login";
    public static final String ROL_USER_INVALID = "Geopista.Visualizador.Login";
    public static final Integer ID_ENTIDAD = new Integer(1);
    public static final Integer ID_MUNICIPIO = new Integer(34083);
    public static final String SRID = "23030";
    public static final Integer ID_MAP_GEOPISTA = new Integer(1);
    public static final Integer ID_MAP_LOCALGIS = new Integer(1);
    public static final Integer ID_ATTRIBUTE_ALIAS= new Integer(1);
    public static final Integer ID_ATTRIBUTE_GEOPISTA = new Integer(1);
    public static final Integer ID_ATTRIBUTE_LOCALGIS = new Integer(1);
    public static final Integer ID_LAYER_GEOPISTA = new Integer(1);
    public static final Integer ID_NUMERO_POLICIA = new Integer(1);
    public static final Integer ID_VIA = new Integer(1);
    public static final String REFERENCIA_CATASTRAL = "";
    public static final Integer ID_LAYER_LOCALGIS = new Integer(1);
    public static final Short PUBLIC_MAP_SHORT = ConstantesSQL.TRUE;
    public static final Boolean PUBLIC_MAP_BOOLEAN = new Boolean(PUBLIC_MAP_SHORT.equals(ConstantesSQL.TRUE));
    public static final byte[] LEGEND_CONTENT = new byte [] {2, 2, 2};
    public static final Integer POSITION_LAYER_LOCALGIS = new Integer(0);
    public static final Integer POSITION_LAYER_FAMILY_LOCALGIS = new Integer(0);
    public static final Integer ID_STYLE_LOCALGIS = new Integer(1);
    public static final Integer ID_MARKER_LOCALGIS = new Integer(1);
    
    public static final LocalgisAttribute LOCALGIS_ATTRIBUTE = new LocalgisAttribute(ID_ATTRIBUTE_LOCALGIS, "atributo1", ID_ATTRIBUTE_ALIAS, ID_ATTRIBUTE_GEOPISTA, ID_LAYER_LOCALGIS);
    public static final LocalgisLayer LOCALGIS_LAYER = new LocalgisLayer(ID_LAYER_LOCALGIS, ID_LAYER_GEOPISTA, "layer1", "select * from parcelas", " ");
    public static final LocalgisCSS LOCALGIS_CSS = new LocalgisCSS(ID_ENTIDAD, "Contenido");
    public static final LocalgisLegendKey LOCALGIS_LEGEND_KEY = new LocalgisLegendKey(ID_LAYER_GEOPISTA, ID_ENTIDAD, PUBLIC_MAP_SHORT);
    public static final LocalgisLegend LOCALGIS_LEGEND = new LocalgisLegend(LOCALGIS_LEGEND_KEY.getLayeridgeopista(), LOCALGIS_LEGEND_KEY.getIdentidad(), LOCALGIS_LEGEND_KEY.getMappublic(), LEGEND_CONTENT);
    public static final LocalgisMap LOCALGIS_MAP = new LocalgisMap("mapa1", ID_MAP_LOCALGIS, ID_MAP_GEOPISTA, new Double(0), new Double(0), new Double(0), new Double(0), ID_ENTIDAD, PUBLIC_MAP_SHORT, SRID, ConstantesSQL.TRUE);
    public static final LocalgisMapLayer LOCALGIS_MAP_LAYER = new LocalgisMapLayer(ID_MAP_LOCALGIS, ID_LAYER_LOCALGIS, ID_STYLE_LOCALGIS, POSITION_LAYER_LOCALGIS, POSITION_LAYER_FAMILY_LOCALGIS);
    public static final LocalgisMapServerLayer LOCALGIS_MAP_SERVER_LAYER = new LocalgisMapServerLayer(ID_LAYER_LOCALGIS, "wms", "http://localhost", "layers", "23030", "image/png", "1.1.1", new BigDecimal(1), new BigDecimal(1), ID_MAP_LOCALGIS, POSITION_LAYER_LOCALGIS, ID_LAYER_GEOPISTA);
    public static final LocalgisStyle LOCALGIS_STYLE = new LocalgisStyle(ID_STYLE_LOCALGIS, "style1", "style1", "style1", "style1");
    public static final LocalgisMarker LOCALGIS_MARKER = new LocalgisMarker(ID_MARKER_LOCALGIS, ID_MAP_LOCALGIS, USER_VALID, new Double(0), new Double(0), new Double(0), "mark1", "descriptionMark1");
    public static final LocalgisRestrictedAttribute LOCALGIS_RESTRICTED_ATTRIBUTE = new LocalgisRestrictedAttribute(ID_LAYER_GEOPISTA, ID_ENTIDAD , ID_ATTRIBUTE_GEOPISTA, PUBLIC_MAP_SHORT, ID_ATTRIBUTE_ALIAS, "alias1");
    
}
