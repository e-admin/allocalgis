/**
 * DxfXDataSchema.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
