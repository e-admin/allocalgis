package com.geopista.ui.plugin.profiles3D.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfree.data.xy.XYSeries;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.feature.Feature;
/**
 * 
 * @author jvaca
 *
 */
public class ProfilesGeometry3D
{
    //contendra Features
    Feature FeatureSelect;
    //contendra todas las LineString que se saquen de la seleccion del feature
    LineString lineString;
    //contiene las uniones de los LineString que su endPoint y su startPoint coinciden
    MultiLineString multiLine;
    //almacena todos los puntos de cada LineString
    List pointLine= new ArrayList();
    //almacena todos los lineString
    List lineStringMaster= new ArrayList();
    //Esta lista almacena xy series
    XYSeries series;
    
    Coordinate coordenada;
    Double maxalturas;
    Double minalturas;
    Double maxDistancias;
    Double minDistancias;
    Point punto;
    
    
    //distancia de un punto de la feature a los puntos mas cercanos de la isolinea
    double distance;
    //altura indicada por la cota de la isolinea
    double altitud;    
    
    public ProfilesGeometry3D(){}
    
    
    /**
     * @return Returns the featureSelect.
     */
    public Feature getFeatureSelect()
    {
        return FeatureSelect;
    }
    
    /**
     * @param featureSelect The featureSelect to set.
     */
    public void setFeatureSelect(Feature featureSelect)
    {
        FeatureSelect = featureSelect;
    }
    
    
    /**
     * @return Returns the lineString.
     */
    public LineString getLineString()
    {
        return lineString;
    }
    
    
    /**
     * @param lineString The lineString to set.
     */
    public void setLineString(LineString lineString)
    {
        this.lineString = lineString;
    }
    
    
    /**
     * @return Returns the altitud.
     */
    public double getAltitud()
    {
        return altitud;
    }
    
    
    /**
     * @param altitud The altitud to set.
     */
    public void setAltitud(double altitud)
    {
        this.altitud = altitud;
    }
    
    
    /**
     * @return Returns the distance.
     */
    public double getDistance()
    {
        return distance;
    }
    
    
    /**
     * @param distance The distance to set.
     */
    public void setDistance(double distance)
    {
        this.distance = distance;
    }
    
    
    /**
     * @return Returns the coordenada.
     */
    public Coordinate getCoordenada()
    {
        return coordenada;
    }
    
    
    /**
     * @param coordenada The coordenada to set.
     */
    public void setCoordenada(Coordinate coordenada)
    {
        this.coordenada = coordenada;
    }
    
    
    /**
     * @return Returns the pointLine.
     */
    public List getPointLine()
    {
        return pointLine;
    }
    
    
    /**
     * @param pointLine The pointLine to set.
     */
    public void setPointLine(List pointLine)
    {
        this.pointLine = pointLine;
    }
    
    
    
    /**
     * @return Returns the multiLine.
     */
    public MultiLineString getMultiLine()
    {
        return multiLine;
    }
    
    
    /**
     * @param multiLine The multiLine to set.
     */
    public void setMultiLine(MultiLineString multiLine)
    {
        this.multiLine = multiLine;
    }
    
    
    /**
     * @return Returns the punto.
     */
    public Point getPunto()
    {
        return punto;
    }
    
    /**
     * @param punto The punto to set.
     */
    public void setPunto(Point punto)
    {
        this.punto = punto;
    }
    
    /**
     * @return Returns the lineStringMaster.
     */
    public List getLineStringMaster()
    {
        return lineStringMaster;
    }
    
    
    /**
     * @param lineStringMaster The lineStringMaster to set.
     */
    public void setLineStringMaster(List lineStringMaster)
    {
        this.lineStringMaster = lineStringMaster;
    }
    
    /**
     * @return Returns the maxalturas.
     */
    public Double getMaxalturas()
    {
        return maxalturas;
    }
    
    /**
     * @param maxalturas The maxalturas to set.
     */
    public void setMaxalturas(Double maxalturas)
    {
        this.maxalturas = maxalturas;
    }
    
    /**
     * @return Returns the maxDistancias.
     */
    public Double getMaxDistancias()
    {
        return maxDistancias;
    }
    
    /**
     * @param maxDistancias The maxDistancias to set.
     */
    public void setMaxDistancias(Double maxDistancias)
    {
        this.maxDistancias = maxDistancias;
    }
    
    /**
     * @return Returns the minalturas.
     */
    public Double getMinalturas()
    {
        return minalturas;
    }
    
    /**
     * @param minalturas The minalturas to set.
     */
    public void setMinalturas(Double minalturas)
    {
        this.minalturas = minalturas;
    }
    
    /**
     * @return Returns the minDistancias.
     */
    public Double getMinDistancias()
    {
        return minDistancias;
    }
    
    /**
     * @param minDistancias The minDistancias to set.
     */
    public void setMinDistancias(Double minDistancias)
    {
        this.minDistancias = minDistancias;
    }
    
    /**
     * @return Returns the series.
     */
    public XYSeries getSeries()
    {
        return series;
    }
    
    /**
     * @param series The series to set.
     */
    public void setSeries(XYSeries series)
    {
        this.series = series;
    }
    
    public boolean equals(ProfilesGeometry3D obj) 
    {    
        
        if (obj.getLineStringMaster().size()!= this.lineStringMaster.size())
            return false;
        
        
        Iterator itMaster = lineStringMaster.iterator();
        Iterator itComp = obj.getLineStringMaster().iterator();
        
        //Si sigue es que tienen la misma longitud, asi que el while puede hacerse
        //sobre cualquiera de los iteradores
        while (itMaster.hasNext())
        {
            if (!itMaster.next().equals(itComp.next()))
                return false;
        }
        return true;
        
    }
    
    /**
     * Comprueba si el lineStringMaster del objeto contiene al de otro objeto dado
     * @param obj
     * @return
     */
    public boolean contains(ProfilesGeometry3D obj) 
    {   
        if (obj.getLineStringMaster().size() > this.lineStringMaster.size())
            return false;
        
        //Si sigue es que el objeto actual contiene más lineas que el que viene como parametro
        //Habrá que comprobar si TODOS los elementos del lineStringMaster corto estan en el largo
        if(lineStringMaster.containsAll(obj.getLineStringMaster()))
            return true;
        else 
            return false;
        
    }
    
}
