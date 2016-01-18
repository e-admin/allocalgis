package com.geopista.server.administradorCartografia;

import com.geopista.server.administradorCartografia.GeopistaConnection;
import com.geopista.server.administradorCartografia.SRID;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 18-oct-2005
 * Time: 18:55:14
 * To change this template use File | Settings | File Templates.
 */
public class GeopistaConnectionFactory {

    public static GeopistaConnection getInstance(NewSRID srid, boolean isOracle) throws Exception{

        String geoClass= "";

        if (!isOracle){
            geoClass= "com.geopista.server.administradorCartografia.PostGISConnection";
        }else{
            geoClass= "com.geopista.server.administradorCartografia.OracleConnection";
        }

        GeopistaConnection geoConn= (GeopistaConnection)Class.forName(geoClass).newInstance();
        geoConn.setSRID(srid);
        
        return geoConn;
    }
}
