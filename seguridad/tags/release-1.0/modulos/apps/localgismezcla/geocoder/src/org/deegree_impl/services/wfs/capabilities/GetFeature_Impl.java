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
package org.deegree_impl.services.wfs.capabilities;

import java.util.*;

import org.deegree.services.capabilities.*;
import org.deegree.services.wfs.*;
import org.deegree.services.wfs.capabilities.*;


/**
 * The &lt;GetFeature&gt; tag isused todefine the formats available for
 * expressing the results of a query. The RESULTFORMATS entity defines the
 * mandatory output format of GML but can be redefined to include additional
 * vendor specific formats.
 *
 * <p>----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class GetFeature_Impl implements GetFeature {
    private ArrayList dCPType = null;
    private ArrayList resultFormat = null;
    private HashMap classes = null;

    /**
     * constructor initializing the class with the <FeatureType>
     */
    GetFeature_Impl( String[] resultFormat, String[] classNames, DCPType[] dCPType ) {
        this.dCPType = new ArrayList();
        this.resultFormat = new ArrayList();
        this.classes = new HashMap();
        setDCPType( dCPType );
        setResultFormat( resultFormat );
        setClasses( resultFormat, classNames );
    }

    /**
     * gets the ResultFormat, the ouput format of the GML.
     */
    public String[] getResultFormat() {
        return (String[])resultFormat.toArray( new String[resultFormat.size()] );
    }

    /**
     * adds a result format to the GetFeature operation
     */
    public void addResultFormat( String resultFormat ) {
        this.resultFormat.add( resultFormat );
    }

    /**
     * sets the ResultFormat, the ouput format of the GML.
     */
    public void setResultFormat( String[] resultFormat ) {
        this.resultFormat.clear();

        if ( resultFormat != null ) {
            for ( int i = 0; i < resultFormat.length; i++ ) {
                addResultFormat( resultFormat[i] );
            }
        }
    }

    /**
     * The only available distributed computing platform is HTTP
     * for which two request methods are defined; GET and POST.
     * The onlineResource attribute indicates the URL prefix for
     * HTTP GET requests (everything before the question mark and
     * query string:http://hostname[:port]/path/scriptname); for HTTP POST
     * requests, onlineResource is the complete URL.
     */
    public DCPType[] getDCPType() {
        return (DCPType[])dCPType.toArray( new DCPType[dCPType.size()] );
    }

    /**
     * adds the available Distributed Computing Platforms (DCPs) for a operation.
     * At present, only HTTP (GET & POST) is defined.
     */
    public void addDCPType( DCPType dCPType ) {
        this.dCPType.add( dCPType );
    }

    /**
     * adds the available Distributed Computing Platforms (DCPs) for a operation.
     * At present, only HTTP (GET & POST) is defined.
     */
    public void setDCPType( DCPType[] dCPTypes ) {        
        this.dCPType.clear();

        if ( dCPTypes != null ) {
            for ( int i = 0; i < dCPTypes.length; i++ ) {
                this.dCPType.add( dCPTypes[i] );
            }
        }
    }

    /**
     * the method returns the class that is responsible for handling/creating the
     * submitted format. This enables an implementation of the deegree WFS to add
     * the handling for new formats dynamicly by editing the its capabilities
     * document.
     */
    public GetFeatureResponseHandler getClassForFormat( String format ) {
        return (GetFeatureResponseHandler)classes.get( format );
    }

    /**
     * sets the classes that are responsible for handling/creating the
     * submitted format.
     */
    public void setClasses( String[] formats, String[] classNames ) {
        classes.clear();

        if ( classNames != null ) {
            for ( int i = 0; i < classNames.length; i++ ) {
                addClassByName( formats[i], classNames[i] );
            }
        }
    }

    /**
     * adds a class that is responsible for handling/creating the
     * submitted format.
     */
    public void addClassByName( String format, String className ) {
        try {
            Class class_ = Class.forName( className );
            classes.put( format, class_.newInstance() );
        } catch ( Exception e ) {
            System.out.println( e );
        }
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = null;
        ret = "resultFormat = " + resultFormat + "\n";
        ret += ( "dCPType = " + dCPType + "\n" );
        ret += ( "dCPType = " + classes + "\n" );
        return ret;
    }

    /*#Request lnkWFS_Service;*/
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: GetFeature_Impl.java,v $
 * Revision 1.1  2009/07/09 07:25:33  miriamperez
 * Rama única LocalGISDOS
 *
 * Revision 1.1  2009/04/13 10:16:51  miriamperez
 * Meto en LocalGISPrivado el módulo del geocoder al completo
 *
 * Revision 1.1  2006/08/31 11:15:30  angeles
 * *** empty log message ***
 *
 * Revision 1.1.1.1  2006/08/17 11:34:01  angeles
 * no message
 *
 * Revision 1.2  2003/08/29 08:03:00  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:22  poth
 * no message
 *
 * Revision 1.9  2002/08/19 15:58:51  ap
 * no message
 *
 * Revision 1.8  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.7  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.6  2002/05/13 16:10:47  ap
 * no message
 *
 * Revision 1.5  2002/05/06 16:02:07  ap
 * no message
 *
 * Revision 1.4  2002/04/26 09:05:10  ap
 * no message
 *
 * Revision 1.2  2002/04/25 16:18:47  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 *
 */
