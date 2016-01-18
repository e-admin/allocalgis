package com.geopista.server.administradorCartografia;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SRID {
    Properties properties=new Properties();
    String sFileName=null;

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
            return Integer.parseInt((String)properties.get(Const.SRID_PROP_PREFIX+iMunicipio));
        }catch(Exception e)
        {
            throw new ACException(new ObjectNotFoundException("Municipio "+iMunicipio+" no se encuentra en el sistema"));
        }
    }
}
