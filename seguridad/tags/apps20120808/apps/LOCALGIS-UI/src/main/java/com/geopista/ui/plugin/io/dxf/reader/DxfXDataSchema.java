package com.geopista.ui.plugin.io.dxf.reader;

import com.geopista.feature.GeopistaSchema;
import com.vividsolutions.jump.util.java2xml.XML2Java;

public class DxfXDataSchema implements DxfXData{
    StringBuffer sbXml=null;
    private GeopistaSchema schema=null;

    public boolean setGroup(short sGroup, String sData){
        switch (sGroup){
            case 1002:
                if (sData.equals("{"))
                    sbXml=new StringBuffer();
                else if (sData.equals("}"))
                    try{
                        //System.out.println(sbXml.toString());
                        schema=(GeopistaSchema)new XML2Java().read(sbXml.toString(),GeopistaSchema.class);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                break;
            case 1000:
                sbXml.append(sData.substring(1,sData.length()-1));
        }
        return true;
    }

    public GeopistaSchema getSchema(){
        return this.schema;
    }

    public Object[] getAttributes() {
        return null;
    }

    public String getAppID() {
        return "GEOPISTA_SCHEMA";
    }

    public static void main(String s[])throws Exception{

    }

}
