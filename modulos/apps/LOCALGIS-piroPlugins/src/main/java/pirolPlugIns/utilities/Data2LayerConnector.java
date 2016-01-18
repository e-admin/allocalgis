/**
 * Data2LayerConnector.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 06.01.2005
 *
 * CVS header information:
 *  $RCSfile: Data2LayerConnector.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/Data2LayerConnector.java,v $s
 */
package pirolPlugIns.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pirolPlugIns.utilities.Delaunay.DelaunayPunkt;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.Layer;
/**
 * 
 * Class to easily exchange JTS geometry types and PIROL geometry
 * types, like punkt or Kante.
 * Offers methods e.g. to convert layers into lists of punkt objects
 * or lists of Kante objects to LinearRing objects.
 * 
 * @author orahn
 *
 * FH Osnabrück - University of Applied Sciences Osnabrück
 * Project PIROL 2005
 * Daten- und Wissensmanagement
 * 
 * @see pirolPlugIns.utilities.punkt
 * @see pirolPlugIns.utilities.Kante
 */
public class Data2LayerConnector {
    protected List shapesData = null;
    
    public static final int SHAPETYPE_UNKNOWN = -1;
    public static final int SHAPETYPE_POINT = 0;
    public static final int SHAPETYPE_LINE  = 1;
    public static final int SHAPETYPE_POLYGON = 2;
    
    protected int shapeType;
    
    public Data2LayerConnector(List shapesData) {
        this.shapesData = shapesData;
    }
    
    protected void setupShapeType(Object o){
        this.shapeType = Data2LayerConnector.SHAPETYPE_UNKNOWN;
        try {
            // check if casting Exception occurs
            punkt p = (punkt)o;
            this.shapeType = Data2LayerConnector.SHAPETYPE_POINT;
            return;
        } catch (RuntimeException e) {
            System.out.println(o.toString() + " ... wasn't a point!");
        }
        try {
            // check if casting Exception occurs
            Kante k = (Kante)o;
            this.shapeType = Data2LayerConnector.SHAPETYPE_LINE;
            return;
        } catch (RuntimeException e) {
            System.out.println(o.toString() + " ... wasn't a line either!");
        }
    }
    
    protected void setupFeatureSchemaAccordingToShapeType(FeatureSchema fs){
        fs.addAttribute("Geometry", AttributeType.GEOMETRY);
        if (this.shapeType == Data2LayerConnector.SHAPETYPE_POINT){
            fs.addAttribute( "Index", AttributeType.INTEGER );
        } else if (this.shapeType == Data2LayerConnector.SHAPETYPE_LINE){
            fs.addAttribute( "Index P1", AttributeType.INTEGER );
            fs.addAttribute( "Index P2", AttributeType.INTEGER );
            fs.addAttribute( "Index PA", AttributeType.INTEGER );
            fs.addAttribute( "Index PB", AttributeType.INTEGER );
        }
    }
    
    /**
     * Converts a geometry (with more than 1 Coordinate) to an array of Kante objects.
     *@param poly a Geometry like a LinearRing, a Polygon or a LineString
     *@return array with 0 to n Kante objects
     *@see Kante
     */
    public static Kante[] polygon2KanteList(Geometry poly){
        List kanten = new ArrayList();
        
        Coordinate[] coords = poly.getCoordinates();
        Coordinate oldCoordinate = coords[0], currentCoordinate;
        
        for (int i=1; i<coords.length; i++){
            currentCoordinate = coords[i];
            kanten.add( new Kante(Data2LayerConnector.coordinate2Punkt(oldCoordinate, punkt.class),Data2LayerConnector.coordinate2Punkt(currentCoordinate, punkt.class)) );
            oldCoordinate = currentCoordinate;
        }
        
        return (Kante[])kanten.toArray(new Kante[0]);
    }
    
    public static Coordinate punkt2Coordinate( punkt p ) throws Exception{
        if (p.getDimension()==2)
            return new Coordinate(p.getX(), p.getY());

        return new Coordinate(p.getX(), p.getY(), p.getZ());
    }
    
    public static Geometry punkt2Geometry( punkt p, GeometryFactory factory ) throws Exception{
        return factory.createPoint(Data2LayerConnector.punkt2Coordinate(p));
    }
    
    public static LinearRing kantenList2LinearRing( List kanten, GeometryFactory gf ) throws Exception{
        Iterator iter = kanten.iterator();
        Coordinate[] coords = new Coordinate[kanten.size()+1];
        Kante k;
        
        for (int i=0; iter.hasNext(); i++){
            k = (Kante)iter.next();
            coords[i] = Data2LayerConnector.punkt2Coordinate(k.getAnfang());
        }
        coords[kanten.size()] = coords[0];
        // ggf. die erste Koordinate nochmals mit reinnehmen
        LinearRing lr = gf.createLinearRing(coords);
        Assert.isTrue(lr.isRing());
        return lr;
    }
    
    public static Geometry kante2Geometry( Kante k, GeometryFactory factory ) throws Exception{
        Coordinate[] coords = new Coordinate[2];
        coords[0] = Data2LayerConnector.punkt2Coordinate(k.getAnfang());
        coords[1] = Data2LayerConnector.punkt2Coordinate(k.getEnde());
        
        return factory.createLineString(coords);
    }
    
    public static punkt coordinate2Punkt( Coordinate coord, Class punktKlasse ){
        punkt p = null;
        try {
            p = (punkt)punktKlasse.getClassLoader().loadClass(punktKlasse.getName()).newInstance();
            p.setCoordinates(new double[]{coord.x,coord.y});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return p;
    }
    
    public static punkt[] layer2Punktefeld(Layer layer, Class punktKlasse ) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
        return  Data2LayerConnector.featureList2Punktefeld( FeatureCollectionTools.FeatureCollection2FeatureArray(layer.getFeatureCollectionWrapper().getUltimateWrappee()), punktKlasse );
    }
    
    public static punkt[] featureList2Punktefeld(Feature[] featureList, Class punktKlasse ) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
        ArrayList punktefeld = new ArrayList();
        Feature[] featureArray = featureList;
        int numFeats = featureArray.length;
        Feature feat;
        punkt p;
        Geometry geom;
        
        for ( int i=0; i<numFeats; i++ ){
            feat = featureArray[i];
            geom = feat.getGeometry();
            
            p = Data2LayerConnector.coordinate2Punkt(geom.getCoordinate(), punktKlasse);
            p.setIndex(feat.getID());
            
            punktefeld.add(p);            
        }
        
        if (punktKlasse.equals(DelaunayPunkt.class)){
            return (DelaunayPunkt[])punktefeld.toArray(new DelaunayPunkt[0]);
        } else if ( punktKlasse.equals(clusterPoint.class)){
            return (clusterPoint[])punktefeld.toArray(new clusterPoint[0]);
        }
        
        return (punkt[])punktefeld.toArray(new punkt[0]);
    }
    /* added by oster */
    /** Converts a Layer into a field of DataPoints.
     *  @param AttributeIndex - The Index of the Attribute which will be stored into the
     * 							DataPoint. 
     * */
    public static List layer2DataPointField(Layer layer, int AttributeIndex) throws Exception{
        
        ArrayList punktefeld = new ArrayList();
        
        FeatureCollection featColl = layer.getFeatureCollectionWrapper().getUltimateWrappee();
        
        Iterator iter = featColl.iterator();
        Feature feat;
        DataPoint p;
        Geometry geom;
        Object o;
        
        while( iter.hasNext() ){
            feat = (Feature)iter.next();
            geom = feat.getGeometry();
            p = new DataPoint();
            p.setCoordinates(new double[]{geom.getCoordinate().x,geom.getCoordinate().y});
            p.setIndex(feat.getID());
            // TODO: make sure that attribute-type is double!
            // see FeatureCoolectionTools.isAttributeTypeNumeric()!
            o = feat.getAttribute(AttributeIndex);
            /* adds the attribute as an value: */
            p.setValue(((Double)o).doubleValue());
            punktefeld.add(p);            
        }
        
        return punktefeld;
    }
    /* end added by oster */

    public FeatureCollection putShapesIntoALayer() throws Exception{
        if (this.shapesData == null || this.shapesData.isEmpty()) return null;
        
        FeatureSchema featureSchema = new FeatureSchema();
        
        this.setupShapeType(this.shapesData.get(0));
        this.setupFeatureSchemaAccordingToShapeType(featureSchema);
        
        FeatureDataset featureDataset = new FeatureDataset(featureSchema);
        
        Iterator iter = this.shapesData.iterator();
        Feature feat;
        Object obj;
        punkt p;
        Kante k;
        
        GeometryFactory geomFact = new GeometryFactory();
        
        while ( iter.hasNext() ){
            obj = iter.next();
            feat = new BasicFeature(featureSchema);
            
            switch (this.shapeType){
            	case Data2LayerConnector.SHAPETYPE_POINT:
            	    p = (punkt)obj;
            	    feat.setAttribute("Index", new Integer(p.getIndex()));
            	    feat.setGeometry(Data2LayerConnector.punkt2Geometry(p, geomFact));
            	    break;
            	case Data2LayerConnector.SHAPETYPE_LINE:
            	    k = (Kante)obj;
            	    feat.setAttribute("Index P1", new Integer(k.getAnfang().getIndex()));
            	    feat.setAttribute("Index P2", new Integer(k.getEnde().getIndex()));
            	    feat.setAttribute("Index PA", new Integer(k.getPunktIndexA()));
            	    feat.setAttribute("Index PB", new Integer(k.getPunktIndexB()));
            	    feat.setGeometry(Data2LayerConnector.kante2Geometry(k, geomFact));
            	    break;
            	default:
            	    throw new Exception("Type " + obj.getClass().getName() + " not (yet) supported by "+this.getClass().getName());
            }
    	    featureDataset.add(feat);
        }
        
        //layer = new Layer( this.layerTitel, Color.red, featureDataset, this.layerManager );
        
        return featureDataset;
    }
}
