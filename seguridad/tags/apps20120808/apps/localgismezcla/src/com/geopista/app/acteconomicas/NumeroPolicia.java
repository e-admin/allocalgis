package com.geopista.app.acteconomicas;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.WKTReader;

import java.io.StringReader;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 06-jun-2006
 * Time: 13:13:34
 */
public class NumeroPolicia {
    int id;
    String rotulo;
    Geometry geometria;

    public NumeroPolicia(int id, String rotulo, Geometry geometria)
    {
        this.id=id;
        this.rotulo=rotulo;
        this.geometria=geometria;
    }
    public NumeroPolicia(int id, String rotulo, String geometria)
    {
        try
        {
            if (geometria!=null)
            {
                WKTReader wktReader=new WKTReader();
                FeatureCollection fc=wktReader.read(new StringReader(geometria));
                Feature jumpFeature=(Feature)fc.iterator().next();
                this.geometria=jumpFeature.getGeometry();
            }
            this.id=id;
            this.rotulo=rotulo;
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Geometry getGeometria() {
        return geometria;
    }

    public void setGeometria(Geometry geometria) {
        this.geometria = geometria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }
    public String toString()
    {
        return rotulo!=null?rotulo:"";
    }
}
