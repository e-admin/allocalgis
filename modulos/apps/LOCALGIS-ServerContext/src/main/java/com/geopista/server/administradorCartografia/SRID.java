/**
 * SRID.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.geopista.consts.config.ConfigConstants;
import com.geopista.server.ServerContext;

public class SRID {
    Properties properties=new Properties();
    String sFileName=null;

    public SRID()throws FileNotFoundException, IOException{
    }
    
    public SRID(String sFileName)throws FileNotFoundException, IOException{
        this.sFileName=sFileName;
        load();
    }

    public void load()throws FileNotFoundException, IOException{
        FileInputStream in=new FileInputStream(sFileName);
        properties.load(in);
        try{in.close();}catch(Exception e){};
    }

    public int getSRID(int iMunicipio) throws ACException{
        try
        {
//        	return Integer.parseInt(properties.getProperty(Const.SRID_PROP_PREFIX + iMunicipio));
            return Integer.parseInt(ServerContext.getConfig().get(ConfigConstants.SRID_PROP_PREFIX + iMunicipio));
        }catch(Exception e)
        {
            throw new ACException(new ObjectNotFoundException("Municipio "+iMunicipio+" no se encuentra en el sistema"));
        }
    }
}
