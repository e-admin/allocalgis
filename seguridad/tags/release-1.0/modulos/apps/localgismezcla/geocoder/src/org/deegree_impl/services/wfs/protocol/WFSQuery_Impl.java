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
package org.deegree_impl.services.wfs.protocol;

import java.util.*;

import org.deegree.services.wfs.filterencoding.*;
import org.deegree.services.wfs.protocol.*;
import org.deegree_impl.tools.*;


/**
 * Each individual query packaged in a GetFeature request is defined
 * using the query value. The query value defines which feature type
 * to query, what properties to retrieve and what constraints (spatial
 * and non-spatial) to apply to those properties.
 *
 * <p>----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class WFSQuery_Impl implements WFSQuery {
    private ArrayList propertyNames = null;
    private Filter filter = null;
    private String handle = null;
    private String typeName = null;
    private String version = null;
 
    /**
     * default constructor
     */
    public WFSQuery_Impl() {
        propertyNames = new ArrayList();
    }

    /**
     * constructor initializing the class with the <WFSGetFeatureRequest>
     */
    public WFSQuery_Impl(String[] propertyNames, String handle, String version, String typeName, 
                         Filter filter) {
        this();
        setPropertyNames(propertyNames);
        setHandle(handle);
        setVersion(version);
        setTypeName(typeName);
        setFilter(filter);
    }

    /**
     * The property names is used to enumerate the feature properties
     * or attributes that should be selected. If no property names are
     * specified then all properties should be fetched.
     */
    public String[] getPropertyNames() {
        return (String[]) propertyNames.toArray(new String[propertyNames.size()]);
    }

    /**
     * adds the <PropertyNames>
     */
    public void addPropertyNames(String propertyNames) {
        this.propertyNames.add(propertyNames);
    }

    /**
     * sets the <PropertyNames>
     */
    public void setPropertyNames(String[] propertyNames) {
        this.propertyNames.clear();

        if (propertyNames != null) {
            for (int i = 0; i < propertyNames.length; i++) {
                this.propertyNames.add(propertyNames[i]);
            }
        }
    }

    /**
     * The handle attribute is included to allow a client to associate a
     * mnemonic name to the <Query> request. The purpose of the handle attribute
     * is to provide an error handling mechanism for locating a statement that
     * might fail.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * sets the <handle>
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * The version attribute is included in order to accommodate systems that
     * support feature versioning. A value of ALL indicates that all versions
     * of a feature should be fetched. Otherwise an integer can be specified
     * to return the n th version of a feature. The version numbers start at '1'
     * which is the oldest version. If a version value larger than the largest
     * version is specified then the latest version is return. The default action
     * shall be for the query to return the latest version. Systems that do not
     * support versioning can ignore the parameter and return the only version
     * that they have.
     */
    public String getVersion() {
        return version;
    }

    /**
     * sets the <version>
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * The typeName attribute is used to indicate the name of the feature type
     * or class to be queried.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * sets the <TypeName>
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * returns the filter that limits the query
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * sets the filter that limits the query
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    
     /** 
     * exports the <tt>WFSQuery</tt> as XML expression
     */
    public String exportAsXML() {
        Debug.debugMethodBegin( this, "exportAsXML" );
        
        StringBuffer sb = new StringBuffer( 5000 );
        sb.append( "<wfs:Query xmlns:wfs=\"http://www.opengis.net/wfs\" " );
        sb.append( "typeName=\"" + getTypeName() + "\">" );
        String[] pn = getPropertyNames();
        for (int i = 0; i < pn.length; i++) {
            sb.append( "<wfs:PropertyName>" + pn[i] + "</wfs:PropertyName>" );
        }
        if ( getFilter() != null ) {
            sb.append( getFilter().toXML( ) );
        }
        sb.append( "</wfs:Query>" );
        
        Debug.debugMethodEnd();
        return sb.toString();
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = null;
        ret = "propertyNames = " + propertyNames + "\n";
        ret += ("handle = " + handle + "\n");
        ret += ("version = " + version + "\n");
        ret += ("typeName = " + typeName + "\n");
        ret += ("filter = " + filter + "\n");
        return ret;
    }
    
   
    
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSQuery_Impl.java,v $
 * Revision 1.1  2009/07/09 07:25:33  miriamperez
 * Rama única LocalGISDOS
 *
 * Revision 1.1  2009/04/13 10:16:50  miriamperez
 * Meto en LocalGISPrivado el módulo del geocoder al completo
 *
 * Revision 1.1  2006/08/31 11:15:30  angeles
 * *** empty log message ***
 *
 * Revision 1.1.1.1  2006/08/17 11:34:01  angeles
 * no message
 *
 * Revision 1.5  2004/02/09 08:00:22  poth
 * no message
 *
 * Revision 1.4  2003/08/18 07:23:26  poth
 * no message
 *
 * Revision 1.3  2003/04/10 07:32:35  poth
 * no message
 *
 * Revision 1.2  2003/04/07 07:26:56  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:24  poth
 * no message
 *
 * Revision 1.6  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.5  2002/05/29 16:09:38  ap
 * no message
 *
 * Revision 1.4  2002/05/14 14:39:51  ap
 * no message
 *
 * Revision 1.3  2002/04/26 09:05:36  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 
 */
