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

import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.geometry.GM_Object;
import org.deegree.services.wfs.filterencoding.Expression;
import org.deegree.services.wfs.filterencoding.FilterConstructionException;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree.xml.XMLTools;
import org.w3c.dom.Element;


/**
 * Encapsulates the information of a <PropertyName> element as defined in the
 * FeatureId DTD.
 *
 * @author Markus Schneider
 * @version 07.08.2002
 */
public class PropertyName extends Expression_Impl {
    /** The PropertyName's value (as an XPATH expression). */
    private String value;

    /** Constructs a new PropertyName. */
    public PropertyName( String value ) {
        id = ExpressionDefines.PROPERTYNAME;
        setValue( value );
    }

    /**
     * Given a DOM-fragment, a corresponding Expression-object is built.
     * @throws FilterConstructionException if the structure of the DOM-fragment is invalid
     */
    public static Expression buildFromDOM( Element element ) throws FilterConstructionException {
        // check if root element's name equals 'PropertyName'
        if ( !element.getLocalName().toLowerCase().equals( "propertyname" ) ) {
            throw new FilterConstructionException( "Name of element does not equal 'PropertyName'!" );
        }

        return new PropertyName( XMLTools.getValue( element ) );
    }

    /**
     * Returns the last two parts of the XPATH-Expression in the format
     * TABLENAME.VALUE.
     */
    public String getSQLFieldQualifier() {
        return value;
    }

    /**
     * Returns the PropertyName's value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @see #getValue
     */
    public void setValue( String value ) {
        this.value = value;

        if ( value.startsWith( "/" ) ) {
            this.value = value.substring( 1 );
        }
    }

    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML() {
        StringBuffer sb = new StringBuffer(200);
        sb.append( "<ogc:PropertyName>" ).append( value ).append( "</ogc:PropertyName>" );
        return sb;
    }

    /**
     * Returns the <tt>PropertyName</tt>'s value (to be used in the evaluation
     * of a complexer <tt>Expression</tt>). If the value is a geometry, an
     * instance of <tt>GM_Object</tt> is returned, if it appears to be
     * numerical, a <tt>Double</tt>, else a <tt>String</tt>.
     * <p>
     * TODO: Improve datatype handling.
     * <p>
     * @param feature that determines the value of this <tt>PropertyName</tt>
     * @return the resulting value
     * @throws FilterEvaluationException if the <Feature> has no
     *         <tt>Property</tt> with a matching name
     */
    public Object evaluate( Feature feature ) throws FilterEvaluationException {
        //        FeatureTypeProperty[] ftp = feature.getFeatureType().getProperties();
        Object object = getProperty( feature, value );

        //        if (feature.getFeatureType ().getProperty (value) == null) {
        //            throw new FilterEvaluationException (
        //                "FeatureType '" + feature.getFeatureType ().getName () +
        //                "' has no property with name '" + value + "'!");
        //        }
        //        Object object = feature.getProperty (value);
        if ( object == null ) {
            return null;
        }

        if ( object instanceof Number ) {
            return object;
        }

        return object.toString();
    }

    /**
    * Method getProperty
    *
    * @param    feature             a  Feature
    * @param    value               a  String
    *
    * @return   an Object
    */
    private Object getProperty( Feature feature, String value ) throws FilterEvaluationException {
        if ( feature.getFeatureType().getProperty( value ) != null ) {
            return feature.getProperty( value );
        }

        Feature found = findFeature( feature, value );
        System.out.println( "Feature" + found ); //MMDEBUG

        if ( found != null ) {
            return found.getProperty( value );
        } else {
            throw new FilterEvaluationException( "FeatureType '" + 
                                                 feature.getFeatureType().getName() + 
                                                 "' has no property with name '" + value + "'!" );
        }
    }

    /**
    * Method getNames
    *
    * @param    rootfeat feature where starting the search
    * @param    name name of the feature to search for
    *
    * @return   ArrayList<String>
    */
    private Feature findFeature( Feature rootfeat, String name ) {
        Object[] fp = rootfeat.getProperties();

        // find out subcollections
        for ( int i = 0; i < fp.length; i++ ) {
            Object o = fp[i];

            if ( o instanceof FeatureCollection ) {
                FeatureCollection fc = (FeatureCollection)o;
                Feature[] feats = fc.getAllFeatures();

                for ( int j = 0; j < feats.length; j++ ) // foreach feature in the collection
                {
                    if ( feats[j].getFeatureType().getProperty( name ) != null ) // if it's ok, return it

                    {
                        return feats[j];
                    }

                    // else perform a subsearch
                    Feature find = findFeature( feats[j], name );

                    if ( find != null ) {
                        return find;
                    }
                }
            }
        }

        return null;
    }
}