/**
 * 
 */
package com.geopista.server.administradorCartografia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.server.administradorCartografia.ObjectNotFoundException;
import com.geopista.server.database.CPoolDatabase;


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
            return Integer.parseInt((String) properties.get(Const.SRID_DEFECTO_PROP_PREFIX));
        } catch (Exception e) {
            throw new ACException(new ObjectNotFoundException("No se encuentra el srid por defecto en el sistema"));
        }
    }

}
