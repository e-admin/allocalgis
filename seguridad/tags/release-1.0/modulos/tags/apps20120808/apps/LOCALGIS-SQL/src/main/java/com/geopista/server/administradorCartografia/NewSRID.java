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
