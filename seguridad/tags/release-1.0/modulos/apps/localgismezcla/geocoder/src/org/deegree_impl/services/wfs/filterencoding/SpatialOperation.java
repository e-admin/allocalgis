/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
http://www.lat-lon.de

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact:

Andreas Poth
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de

                 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.services.wfs.filterencoding;

import org.deegree.gml.GMLBox;
import org.deegree.gml.GMLException;
import org.deegree.gml.GMLGeometry;
import org.deegree.model.feature.Feature;
import org.deegree.model.geometry.GM_Envelope;
import org.deegree.model.geometry.GM_Exception;
import org.deegree.model.geometry.GM_Object;
import org.deegree.services.wfs.filterencoding.FilterConstructionException;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree.services.wfs.filterencoding.Operation;
import org.deegree.xml.ElementList;
import org.deegree.xml.XMLTools;
import org.deegree_impl.gml.GMLFactory;
import org.deegree_impl.gml.GMLGeometry_Impl;
import org.deegree_impl.model.geometry.GMLAdapter;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Encapsulates the information of a spatial_ops entity (as defined in the
 * Filter DTD).
 * <p>
 * @author <a href="mailto:mschneider@lat-lon.de>Markus Schneider</a>
 * @author <a href="mailto:luigimarinucci@yahoo.com">Luigi Marinucci<a>
 * @version $Id: SpatialOperation.java,v 1.1 2009/07/09 07:25:31 miriamperez Exp $
 */
public class SpatialOperation extends AbstractOperation {
    private static GeometryFactory factory = new GeometryFactory();
    private GMLGeometry gmlGeometry;
    private GM_Object geometryLiteral;
    private PropertyName propertyName;
    //calvin  added on 10/21/2003
    private double distance = -1;

    /**
     * Constructs a new SpatialOperation.
     * @see OperationDefines
     */
    public SpatialOperation( int operatorId, PropertyName propertyName, GMLGeometry gmlGeometry ) {
        super( operatorId );
        this.propertyName = propertyName;
        this.gmlGeometry = gmlGeometry;
    }

    /**
       * Constructs a new SpatialOperation.
       * @see OperationDefines
       * Calvin added on 10/21/2003
       *
       */
    public SpatialOperation( int operatorId, PropertyName propertyName, GMLGeometry gmlGeometry, 
                             double d ) {
        super( operatorId );
        this.propertyName = propertyName;
        this.gmlGeometry = gmlGeometry;
        this.distance = d;
    }
    
    /**
     * returns the distance for geo spatial comparsions such as DWithin or Beyond
     * @return the distance for geo spatial comparsions such as DWithin or Beyond
     */
    public double getDistance()
    {
      return distance ;
    }

    /**
     * Given a DOM-fragment, a corresponding Operation-object is built. This method
     * recursively calls other buildFromDOM () - methods to validate the structure
     * of the DOM-fragment.
     * @throws FilterConstructionException if the structure of the DOM-fragment is invalid
     */
    public static Operation buildFromDOM( Element element ) throws FilterConstructionException {
        SpatialOperation operation = null;

        // check if root element's name is a spatial operator
        String name = element.getLocalName();
        int operatorId = OperationDefines.getIdByName( name );

        // every spatial operation has exactly 2 elements
        ElementList children = XMLTools.getChildElements( element );

         if ( (children.getLength() != 2)   &&  (operatorId != OperationDefines.DWITHIN)) {
            throw new FilterConstructionException( "'" + name + "' requires exactly 2 elements!" );
        }


        // first element must be a PropertyName-Element
        Element child1 = children.item( 0 );
        Element child2 = children.item( 1 );

        if ( !child1.getLocalName().toLowerCase().equals( "propertyname" ) ) {
            throw new FilterConstructionException( "First element of every '" + name + 
                                                   "'-operation must be a " + 
                                                   "'PropertyName'-element!" );
        }

        PropertyName propertyName = (PropertyName)PropertyName.buildFromDOM( child1 );
        GMLGeometry gmlGeometry = null;

        try {
            gmlGeometry = GMLFactory.createGMLGeometry( child2 );
        } catch ( GMLException e ) {
            throw new FilterConstructionException( "GMLGeometry definition in '" + name + 
                                                   "'-operation is erroneous: " + e.getMessage() );
        }

        if ( gmlGeometry == null ) {
            throw new FilterConstructionException( "Unable to parse GMLGeometry definition in '" + 
                                                   name + "'-operation!" );
        }

        //calvin added on 10/21/2003
        double dist = 0;

        if ( operatorId == OperationDefines.DWITHIN ) {
            if ( children.getLength() != 3 ) {
                throw new FilterConstructionException( "'" + name + 
                                                       "' requires exactly 3 elements!" );
            }

            Element child3 = children.item( 2 );

            if ( !child3.getLocalName().toLowerCase().equals( "distance" ) ) {
                throw new FilterConstructionException( "Name of element does not equal 'Distance'!" );
            }

            try { // assume the unit can be only metre
                dist = Double.parseDouble( XMLTools.getValue( child3 ) );

                if ( dist < 0 ) {
                    throw new FilterConstructionException( "value of  Distance can't be negative:" + 
                                                           XMLTools.getValue( element ) );
                }
            } catch ( Exception e ) {
                throw new FilterConstructionException( "value of  Distance is error:" + 
                                                       XMLTools.getValue( element ) );
            }
        }

        switch ( operatorId ) {
            case OperationDefines.CROSSES:
            case OperationDefines.BEYOND:
                throw new FilterConstructionException( "Spatial operator '" + name + 
                                                       "' not implemented!" );
            case OperationDefines.EQUALS:
            case OperationDefines.OVERLAPS:
            case OperationDefines.TOUCHES:
            case OperationDefines.DISJOINT:
            case OperationDefines.INTERSECTS:
            case OperationDefines.WITHIN:
            case OperationDefines.CONTAINS:
            //calvin added on 10/21/2003
            case OperationDefines.DWITHIN:
                // every GMLGeometry is allowed as Literal-argument here
                break;
            case OperationDefines.BBOX:
            {
                if ( !( gmlGeometry instanceof GMLBox ) ) {
                    throw new FilterConstructionException( "'" + name + 
                                                           "' can only be used with a 'Box'-geometry!" );
                }

                break;
            }
            default:
                throw new FilterConstructionException( "'" + name + 
                                                       "' is not a spatial operator!" );
        }

        return new SpatialOperation( operatorId, propertyName, gmlGeometry, dist );
    }

    /**
     * Returns the geometry property used in the operation and one concrete
     * feature.
     * <p>
     * @param feature
     * @return the property as a <tt>GM_Object</tt>-object.
     * @throws FilterEvaluationException if the PropertyName does not denote
     *         a GM_Object
     */
    public GM_Object getGeometryProperty( Feature feature ) throws FilterEvaluationException {
        Object o = feature.getProperty( propertyName.getValue() );

        if ( !( o instanceof GM_Object ) ) {
            throw new FilterEvaluationException( "Specified PropertyName: '" + 
                                                 propertyName.getValue() + 
                                                 "' does not denote a geometry object!" );
        }

        return (GM_Object)o;
    }

    /**
     * Returns the geometry literal used in the operation.
     * <p>
     * @return the literal as a <tt>GM_Object</tt>-object.
     * @throws FilterEvaluationException if the Literal can not be converted to
     *         a GM_Object
     */
    public GM_Object getGeometryLiteral() throws FilterEvaluationException {
        if ( geometryLiteral == null ) {
            try {
                geometryLiteral = GMLAdapter.wrap( gmlGeometry );
            } catch ( GM_Exception e ) {
                throw new FilterEvaluationException( "Construction of GM_Object from " + 
                                                     "SpatialOperation literal failed: '" + 
                                                     e.getMessage() + "'!" );
            }
        }

        return geometryLiteral;
    }

    /**
     * Returns the geometry literal used in the operation.
     * @return the literal as a <tt>GMLGeometry</tt>-object.
     */
    public GMLGeometry getGeometry() {
        return gmlGeometry;
    }

    /**
     * Returns the (bounding) box of a BBOX operation.
     * @deprecated replaced by {@link #getGeometry()}
     */
    public GM_Envelope getBoundingBox() {
        GM_Envelope box = GMLAdapter.createGM_Envelope( (GMLBox)gmlGeometry );

        return box;
    }

    /**
     * returns the name of the (spatial) property that shall be use
     * for geo spatial comparsions
     */
    public PropertyName getPropertyName() {
        return propertyName;
    }

    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML() {
        StringBuffer sb = new StringBuffer(2000);
        sb.append( "<ogc:" ).append( getOperatorName() );
        sb.append( " xmlns:gml='http://www.opengis.net/gml' " ).append( ">" );
        sb.append( propertyName.toXML() );

        Document doc = XMLTools.create();
        Element element = ( (GMLGeometry_Impl)gmlGeometry ).getAsElement();
        XMLTools.appendNode( element, "", sb );
        sb.append( "</ogc:" ).append( getOperatorName() ).append( ">" );

        return sb;
    }

    /**
     * Calculates the <tt>SpatialOperation</tt>'s logical value based on the
     * property values of the given <tt>Feature</tt>.
     * <p>
     * TODO: Implement operations: CROSSES, BEYOND, OVERLAPS AND TOUCHES.
     * <p>
     * @param feature that determines the property values
     * @return true, if the <tt>SpatialOperation</tt> evaluates to true,
     *         else false
     * @throws FilterEvaluationException if the evaluation fails
     */
    public boolean evaluate( Feature feature ) throws FilterEvaluationException {
        boolean value = false;

        switch ( operatorId ) {
            case OperationDefines.EQUALS:
                value = getGeometryProperty( feature ).equals( getGeometryLiteral() );
            case OperationDefines.DISJOINT:
            {
                value = !getGeometryProperty( feature ).intersects( getGeometryLiteral() );
                break;
            }
            case OperationDefines.WITHIN:
            {
                value = getGeometryLiteral().contains( getGeometryProperty( feature ) );
                break;
            }
            case OperationDefines.CONTAINS:
            {
                value = getGeometryProperty( feature ).contains( getGeometryLiteral() );
                break;
            }
            case OperationDefines.INTERSECTS:
            case OperationDefines.BBOX:
            {
                value = getGeometryProperty( feature ).intersects( getGeometryLiteral() );
                break;
            }
            //calvin added on 10/21/2003
            case OperationDefines.DWITHIN:
            {
                value = getGeometryProperty( feature )
                            .isWithinDistance( getGeometryLiteral(), distance );
                break;
            }
            case OperationDefines.CROSSES:
            case OperationDefines.BEYOND:
            case OperationDefines.OVERLAPS:
            case OperationDefines.TOUCHES:
                throw new FilterEvaluationException( "Evaluation for spatial " + "operation '" + 
                                                     OperationDefines.getNameById( operatorId ) + 
                                                     "' is not implemented yet!" );
            default:
                throw new FilterEvaluationException( "Encountered unexpected " + "operatorId: " + 
                                                     operatorId + 
                                                     " in SpatialOperation.evaluate ()!" );
        }

        return value;
    }
}