/**
 * GeopistaConnectionFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;


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
