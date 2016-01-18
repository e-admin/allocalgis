/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.encapsulacionSW;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter extends XmlAdapter<MapElements[], Map<Object, Object>> {
    public MapElements[] marshal(Map<Object, Object> arg0) throws Exception {
        MapElements[] mapElements = new MapElements[arg0.size()];
        int i = 0;
        for (Map.Entry<Object, Object> entry : arg0.entrySet())
            mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());

        return mapElements;
    }

    public Map<Object, Object> unmarshal(MapElements[] arg0) throws Exception {
        Map<Object, Object> r = new HashMap<Object, Object>();
        for (MapElements mapelement : arg0)
            r.put(mapelement.key, mapelement.value);
        return r;
    }
}
