package com.geopista.ui.plugin.io.dxf.reader;

import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.util.java2xml.XML2Java;

public class DxfXDataLayer implements DxfXData{
    StringBuffer sbXml=null;
    private GeopistaLayer layer=null;

    public boolean setGroup(short sGroup, String sData){
        switch (sGroup){
            case 1002:
                if (sData.equals("{"))
                    sbXml=new StringBuffer();
                else if (sData.equals("}"))
                    try{
                        System.out.println(sbXml.toString());
                        layer=(GeopistaLayer)new XML2Java().read(sbXml.toString(),GeopistaLayer.class);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                break;
            case 1000:
                sbXml.append(sData.substring(1,sData.length()-1));
        }
        return true;
    }

    public GeopistaLayer getLayer(){
        return layer;
    }

    public GeopistaSchema getSchema(){
        return null;
    }

    public Object[] getAttributes() {
        return null;
    }

    public String getAppID() {
        return "GEOPISTA_LAYER";
    }

    public static void main(String s[])throws Exception{

    }

}
