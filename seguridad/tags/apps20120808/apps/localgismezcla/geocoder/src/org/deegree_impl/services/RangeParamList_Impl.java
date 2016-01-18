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

import org.deegree.services.RangeParamList;
import org.deegree.tools.Parameter;


/**
 * The list of ranges in a URL request.
 *
 * @author ETj
 */
public final class RangeParamList_Impl implements RangeParamList {
    private HashMap params = new HashMap();

    /**
     * Builds an empty ParameterList.
     */
    public RangeParamList_Impl() {
    }

    /**
     * Builds a ParameterList from the given model request,
     * fetching time, elevation and all other declared ranges.
     *
     * TODO: only time and elevation are retrieved.
     */
    public RangeParamList_Impl(HashMap model) {
        // retrieve special ranges
        String time = (String)model.get( "TIME" );

        if ( time != null ) {
            addParameter( "time", time );
        }

        String elev = (String)model.get( "ELEVATION" );

        if ( elev != null ) {
            addParameter( "elevation", elev );
        }
        // TODO: insert all other ranges
    }

    /**
     * Builds a ParameterList from the given URL request,
     * fetching time, elevation and all other declared ranges.
     *
     * TODO: this constructor is a pure stub!
     */
    public RangeParamList_Impl(String request) {
        // TODO: insert all other ranges
    }

    /**
     * returns the parameter that matches the submitted name. if no parameter can
     * be found <tt>null</tt> will be returned.
     */
    public Parameter getParameter( String name ) {
        return (Parameter)params.get( name );
    }

    /**
     * adds a new <tt>= new Parameter_Impl( name, value );
         addParameter( p );
     }
    
    /**
     * adds a new <tt>Parameter</tt> to the list
     */
    public void addParameter( Parameter param ) {
        params.put( param.getName(), param );
    }

    /**
     *
     *
     * @param name 
     * @param value 
     */
    public void addParameter( String name, String value ) {
        Parameter rp = RangeParamFactory.createRangeParam( name, value );
        addParameter( rp );
    }

    /**
     * returns all <tt>Parameter</tt>s contained within the list as array.
     * it is guarenteered that the arrays isn't <tt>null</tt>
     */
    public Parameter[] getParameters() {
        Parameter[] p = new Parameter[params.size()];
        return (Parameter[])params.values().toArray( p );
    }

    /**
     *
     *
     * @return 
     */
    public Set getNames() {
        return params.keySet();
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = null;
        ret = "params = " + params + "\n";
        return ret;
    }
}