/**
 * SRIDDefecto.java
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import com.geopista.consts.config.ConfigConstants;
import com.geopista.server.ServerContext;


/**
 * @author seilagamo
 *
 */
public class SRIDDefecto {
    
    Properties properties=new Properties();
    String sFileName=null;

    public SRIDDefecto()throws FileNotFoundException, IOException{
/*        this.sFileName = Const.SRID_DEFECTO;
        load();*/
    }

    public SRIDDefecto(String fileName)throws FileNotFoundException, IOException{
        this.sFileName = fileName;
        load();
    }

    public void load()throws FileNotFoundException, IOException{
        properties.load(SRIDDefecto.class.getResourceAsStream(sFileName));
    }

    public int getSRID(Connection conn, int idEntidad) throws ACException {
    	PreparedStatement preparedStatement = null;
    	ResultSet rsSQL = null;
        try {
			preparedStatement = conn.prepareStatement("select srid from entidad_supramunicipal where id_entidad=?");
			preparedStatement.setInt(1, idEntidad);
            rsSQL = preparedStatement.executeQuery();
            if (rsSQL.next())
            	return rsSQL.getInt("srid");
        	
            return -1;
        } catch (Exception e) {
            throw new ACException(new ObjectNotFoundException("No se encuentra el srid por defecto en el sistema"));
        }finally{
			try{rsSQL.close();}catch(Exception e){new ACException(new ObjectNotFoundException(e.getMessage()));};
			try{preparedStatement.close();}catch(Exception e){new ACException(new ObjectNotFoundException(e.getMessage()));};
		}

    }
    
    public int getSRID() throws ACException {
        try {
//        	return Integer.parseInt(properties.getProperty(Const.SRID_DEFECTO_PROP_PREFIX));
            return Integer.parseInt(ServerContext.getConfig().get(ConfigConstants.SRID_DEFECTO_PROP_PREFIX));
        } catch (Exception e) {
            throw new ACException(new ObjectNotFoundException("No se encuentra el srid por defecto en el sistema"));
        }
    }

}
