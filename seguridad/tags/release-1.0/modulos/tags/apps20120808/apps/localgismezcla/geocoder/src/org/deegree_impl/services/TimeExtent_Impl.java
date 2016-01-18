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
import org.deegree.services.TimeExtent;
import org.deegree_impl.tools.TimeTools;


/**
 * TimeExtent implements the ISO extended time format
 * The syntax for expressing Time constraints and date / time values is
 * specified in WMS 1.1 Annexes B
 */
public class TimeExtent_Impl implements TimeExtent {
    protected ArrayList _multidate = null; // arraylist of Calendars
    protected Calendar _enddate = null;
    protected Calendar _singledate = null;
    protected Calendar _startdate = null;
    protected TimePeriod _period = null;
    protected boolean _endIsNow = false;

    /**
     * Creates a new TimeExtent object.
     *
     * @param isoTime 
     */
    public TimeExtent_Impl(String isoTime) {
        if ( !parseSingleDate( isoTime ) ) {
            if ( !parseMultiDate( isoTime ) ) {
                if ( !parsePeriodicDate( isoTime ) ) {
                    throw new IllegalArgumentException( "Unparsable ISO date: " + isoTime );
                }
            }
        }
    }

    /**
     * Method parsePeriodicDate
     *
     * @param    isoTime             a  String
     *
     * @return   a  boolean
     */
    private boolean parsePeriodicDate( String isoTime ) {
        StringTokenizer st = new StringTokenizer( isoTime, "/" );

        if ( st.countTokens() != 3 ) {
            return false;
        }

        String sstart = st.nextToken().trim();
        String send = st.nextToken().trim();
        String speriod = st.nextToken().trim();

        try {
            Calendar start = TimeTools.createCalendar( sstart );
            Calendar end = null;

            if ( send.equalsIgnoreCase( "NOW" ) || send.equalsIgnoreCase( "CURRENT" ) ) {
                _endIsNow = true;
            } else {
                end = TimeTools.createCalendar( send );
            }

            TimePeriod period = new TimePeriod( speriod );

            _startdate = start;
            _enddate = end;
            _period = period;

            return true;
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "Bad periodic date: " + isoTime );
        }
    }

    /**
     * @param    isoTime             a  String
     *
     * @return true if isoTime is parsable as a multi date list
     */
    private boolean parseMultiDate( String isoTime ) {
        StringTokenizer st = new StringTokenizer( isoTime, "," );

        if ( st.countTokens() < 2 ) {
            return false;
        }

        ArrayList dates = new ArrayList();

        while ( st.hasMoreElements() ) {
            String sdate = st.nextToken().trim();

            try {
                Calendar date = TimeTools.createCalendar( sdate );
                dates.add( date );
            } catch ( Exception e ) {
                System.out.println( "Bad date in list: " + sdate + " -- skipped" );
            }
        }

        if ( dates.size() == 0 ) {
            throw new IllegalArgumentException( "Illegal date list: " + isoTime );
        }

        _multidate = dates;

        return true;
    }

    /**
     * @param    isoTime             a  String to be parsed
     *
     * @return true if isoTime is parsable as a single date
     */
    private boolean parseSingleDate( String isoTime ) {
        try {
            _singledate = TimeTools.createCalendar( isoTime );
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
        return _singledate != null;
    }

    /**
     *
     *
     * @return 
     */
    public boolean isMulti() {
        return _multidate != null;
    }

    /**
     *
     *
     * @return 
     */
    public boolean isPeriodic() {
        return _startdate != null;
    }

    /** Used for single valued time extents*/
    public Calendar getDate() {
        return _singledate;
    }

    /** Used for listed time extents*/
    public List getList() {
        return _multidate;
    }

    /**
     *
     *
     * @return 
     */
    public int getListLength() {
        return _multidate.size();
    }

    /** Used for listed time extents*/
    public Calendar getDate( int i ) {
        return (Calendar)_multidate.get( i );
    }

    /** Used for periodic time extents*/
    public TimePeriod getPeriod() {
        return _period;
    }

    /** Used for periodic time extents*/
    public Calendar getStartDate() {
        return _startdate;
    }

    /** Used for periodic time extents*/
    public Calendar getEndDate() {
        return _endIsNow ? Calendar.getInstance() : _enddate;
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
            sb.append( "SINGLE, " ).append( _singledate );
        } else if ( isMulti() ) {
            sb.append( "LIST" );

            for ( int i = 0, size = _multidate.size(); i < size; i++ ) {
                sb.append( ", " ).append( _multidate.get( i ) );
            }
        } else if ( isPeriodic() ) {
            sb.append( "PERIODIC, " ).append( _startdate ).append( "-->" ).append( _enddate )
              .append( " P " ).append( _period );
        }

        sb.append( "]" );

        return sb.toString();
    }

    
}