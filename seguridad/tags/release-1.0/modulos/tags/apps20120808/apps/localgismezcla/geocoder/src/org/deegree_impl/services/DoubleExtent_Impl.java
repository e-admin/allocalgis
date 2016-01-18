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

copyright Emanuele Tajariol / itworks
                  
 ---------------------------------------------------------------------------*/

package org.deegree_impl.services;

import java.util.*;

import org.deegree.services.DoubleExtent;


/**
 * DoubleExtent implements the ISO extended time format
 * The syntax for expressing Time constraints and date / time values is
 * specified in WMS 1.1 Annexes C
 *
 * @author Emanuele Tajariol
 */
public class DoubleExtent_Impl implements DoubleExtent {
    protected ArrayList _multi = null; // arraylist of Double
    protected Double _higher = null;
    protected Double _lower = null;
    protected Double _period = null;
    protected Double _single = null;

    /**
     * Creates a new DoubleExtent_Impl object.
     *
     * @param extent 
     */
    public DoubleExtent_Impl( String extent ) {
        if ( !parseSingle( extent ) ) {
            if ( !parseMulti( extent ) ) {
                if ( !parsePeriodic( extent ) ) {
                    throw new IllegalArgumentException( "Unparsable real extent: " + extent );
                }
            }
        }
    }

    /**
     * Method parsePeriodic
     *
     * @param    extent
     *
     * @return   a  boolean
     */
    private boolean parsePeriodic( String extent ) {
        StringTokenizer st = new StringTokenizer( extent, "/" );

        if ( st.countTokens() != 3 ) {
            return false;
        }

        String sstart = st.nextToken().trim();
        String send = st.nextToken().trim();
        String speriod = st.nextToken().trim();

        try {
            Double start = new Double( sstart );
            Double end = new Double( send );
            Double period = new Double( speriod );

            _lower = start;
            _higher = end;
            _period = period;

            return true;
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "Bad periodic double extent: " + extent );
        }
    }

    /**
     * @param extent a String
     *
     * @return true if extent is parsable as a multi double list
     */
    private boolean parseMulti( String extent ) {
        StringTokenizer st = new StringTokenizer( extent, "," );

        if ( st.countTokens() < 2 ) {
            return false;
        }

        ArrayList list = new ArrayList();

        while ( st.hasMoreElements() ) {
            String item = st.nextToken().trim();

            try {
                Double value = new Double( item );
                list.add( value );
            } catch ( Exception e ) {
                System.out.println( "Bad double in list: " + item + " -- skipped" );
            }
        }

        if ( list.size() == 0 ) {
            throw new IllegalArgumentException( "Illegal double list: " + extent );
        }

        _multi = list;

        return true;
    }

    /**
     * @param    extent             a  String to be parsed
     *
     * @return true if extent is parsable as a single double
     */
    private boolean parseSingle( String extent ) {
        try {
            _single = new Double( extent );
            return true;
        } catch ( Exception e ) {
            return false;
        }
    }

    /**
     *
     *
     * @return 
     */
    public boolean isSingle() {
        return _single != null;
    }

    /**
     *
     *
     * @return 
     */
    public boolean isMulti() {
        return _multi != null;
    }

    /**
     *
     *
     * @return 
     */
    public boolean isPeriodic() {
        return _lower != null;
    }

    /** Used for single valued extents*/
    public Double getSingleValue() {
        return _single;
    }

    /** Used for listed extents*/
    public List getList() {
        return _multi;
    }

    /** Used for listed extents*/
    public int getListLength() {
        return _multi.size();
    }

    /** Used for listed extents*/
    public Double getListItem( int i ) {
        return (Double)_multi.get( i );
    }

    /** Used for periodic extents*/
    public Double getLower() {
        return _lower;
    }

    /** Used for periodic extents*/
    public Double getHigher() {
        return _higher;
    }

    /** Used for periodic extents*/
    public Double getPeriod() {
        return _period;
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append( getClass().getName() ).append( '@' ).append( Integer.toHexString( hashCode() ) )
          .append( "[" );

        if ( isSingle() ) {
            sb.append( "SINGLE, " ).append( _single );
        } else if ( isMulti() ) {
            sb.append( "MULTI" );

            for ( int i = 0, size = _multi.size(); i < size; i++ ) {
                sb.append( ", " ).append( _multi.get( i ) );
            }
        } else if ( isPeriodic() ) {
            sb.append( "PERIODIC, " ).append( _lower ).append( "-->" ).append( _higher )
              .append( " P " ).append( _period );
        }

        sb.append( "]" );

        return sb.toString();
    }
}