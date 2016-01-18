package com.geopista.server.administradorCartografia;

import com.geopista.feature.GeopistaFeature;
import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;

/** Clase de utilidad para subir features modificados al servidor
 * sin arrastrar los objetos del esquema.
  */
public class ACFeatureUpload implements Serializable{
    String id;
    Object[] attValues;
    public final static int DIRTY=1;
    public final static int NEW=2;
    public final static int DELETED=3;
    private int iStatus=0;
    private int geometryIndex=-1;

    public int getGeometryIndex() {
        return geometryIndex;
    }

    public ACFeatureUpload(){
    }

    public boolean isNew(){
        return iStatus==2;
    }

    public boolean isDirty(){
        return iStatus==1;
    }

    public boolean isDeleted(){
        return iStatus==3;
    }

    public ACFeatureUpload(GeopistaFeature gf){
        Object[] oAtts=gf.getAttributes();
        attValues=new Object[oAtts.length];
        for (int i=0;i<oAtts.length;i++){
            if (oAtts[i] instanceof Geometry){
                attValues[i]=((Geometry)oAtts[i]).toText();
                this.geometryIndex=i;
            }else
                attValues[i]=oAtts[i];
        }
        this.id=gf.getSystemId();
        this.iStatus=gf.isNew()?     NEW
                    :gf.isDeleted()? DELETED
                    :gf.isDirty()?   DIRTY : -1;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object[] getAttValues() {
        return attValues;
    }

    public void setAttValues(Object[] attValues) {
        this.attValues = attValues;
    }
}