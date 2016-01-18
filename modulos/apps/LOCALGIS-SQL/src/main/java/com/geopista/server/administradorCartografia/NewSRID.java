/**
 * NewSRID.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.server.administradorCartografia;

import java.util.HashMap;
import java.util.Map;

import com.geopista.server.database.COperacionesEntidades;
import com.geopista.server.database.COperacionesMunicipios;


/**
 * @author seilagamo
 *
 */
public class NewSRID {
    
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(NewSRID.class);
    private Map sridTabla = null; 
    
    
    public NewSRID(){        
    }
        
   
    public int getSRID(int id_municipio) {
        int srid = 0;

        if (sridTabla == null) {
            sridTabla = new HashMap();
        }
        if (sridTabla.get(Integer.valueOf(id_municipio)) != null) {
            srid = ((Integer) sridTabla.get(Integer.valueOf(id_municipio))).intValue();
        } else {
            // Consultar a bbdd
            try {
                srid = COperacionesMunicipios.getSRID(id_municipio);
                sridTabla.put(Integer.valueOf(id_municipio), Integer.valueOf(srid));
            } catch (Exception e) {
                java.io.StringWriter sw = new java.io.StringWriter();
                java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al recoger el srid:" + sw.toString());
            }
        }
        return srid;
    }
    
    
    public int getSRIDEntidad(int id_entidad) {
        int srid = 0;

        if (sridTabla == null) {
            sridTabla = new HashMap();
        }
        if (sridTabla.get(Integer.valueOf(id_entidad)) != null) {
            srid = ((Integer) sridTabla.get(Integer.valueOf(id_entidad))).intValue();
        } else {
            // Consultar a bbdd
            try {
                srid = COperacionesEntidades.getSRID(id_entidad);
                sridTabla.put(Integer.valueOf(id_entidad), Integer.valueOf(srid));
            } catch (Exception e) {
                java.io.StringWriter sw = new java.io.StringWriter();
                java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al recoger el srid:" + sw.toString());
            }
        }
        return srid;
    }
    

}
