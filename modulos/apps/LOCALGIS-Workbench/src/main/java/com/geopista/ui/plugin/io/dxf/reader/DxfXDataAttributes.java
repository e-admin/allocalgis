/**
 * DxfXDataAttributes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.dxf.reader;

import java.util.ArrayList;

import com.geopista.feature.GeopistaSchema;


public class DxfXDataAttributes implements DxfXData{
    ArrayList alValues=new ArrayList();
    ArrayList alTypes=new ArrayList();
    String sSystemId=null;
    String sGeometryClass=null;
    int iGeomNum=0;
    int iRingNum=0;

    public int getGeomNum() {
        return iGeomNum;
    }

    public int getRingNum() {
        return iRingNum;
    }

    int iState=BEGIN;

    private static final int BEGIN=0;
    private static final int ID=1;
    private static final int GEOM=2;
    private static final int GEOM_NUM=3;
    private static final int RING_NUM=4;
    private static final int ATTS=5;
    private static final int ATT=6;
    private static final int POS=7;
    private static final int TYPE=8;
    private static final int VAL=9;
    private static final int ATT_END=10;
    private static final int END=11;
    private static final int ERR=12;

    private static final int GROUP_BEGIN=0;
    private static final int GROUP_STRING=1;
    private static final int GROUP_END=2;

    private static final int[][] aStates={// BEGIN   VALUE      END
                                            {ID,    ERR,        ERR}, //BEGIN
                                            {ERR,   GEOM,       ERR}, //ID
                                            {ERR,   GEOM_NUM,   ERR}, //GEOM
                                            {ERR,   RING_NUM,   ERR}, //GEOM_NUM
                                            {ERR,   ATTS,       ERR}, //RING_NUM
                                            {POS,   ERR,        ERR}, //ATTS
                                            {POS,   ERR,        END}, //ATT
                                            {ERR,   TYPE,       ERR}, //POS
                                            {ERR,   VAL,        ERR}, //TYPE
                                            {ERR,   ATT_END,    ERR}, //VAL
                                            {ERR,   ERR,        ATT}  //ATT_END
                                         };


    public String getGeometryClass() {
        return sGeometryClass;
    }

    public String getSystemId() {
        return sSystemId;
    }

    public boolean setGroup(short sGroup, String sData) {
        int iToken=-1;
        switch (sGroup){
            case 1002:
                iToken=sData.equals("{")?GROUP_BEGIN:GROUP_END;
                break;
            case 1000:
                iToken=GROUP_STRING;
        }
        runState(iState,sData.trim());
        if (iState<END)
            iState=aStates[iState][iToken];
        return (iState!=ERR);
    }

    public void runState(int iState,String sData){
        switch (iState){
            case ID:
                sSystemId=sData;
                break;
            case GEOM:
                sGeometryClass=sData;
                break;
            case GEOM_NUM:
                iGeomNum=Integer.parseInt(sData);
                break;
            case RING_NUM:
                iRingNum=Integer.parseInt(sData);
                break;
            case POS:
                int iPos=Integer.parseInt(sData);
                while (alValues.size()<iPos)
                    alValues.add(null);
                break;
            case TYPE:
                alTypes.add(sData);
                break;
            case VAL:
                alValues.add(sData);
                break;
            case BEGIN:
            case ATTS:
            case ATT:
            case ATT_END:
            case END:
        }
    }

    public GeopistaSchema getSchema() {
        return null;
    }

    public Object[] getAttributes() {
        return alValues.toArray();
    }

    public Object[] getAttributeTypes() {
        return alTypes.toArray();
    }

    public String getAppID() {
        return "GEOPISTA";
    }
}
