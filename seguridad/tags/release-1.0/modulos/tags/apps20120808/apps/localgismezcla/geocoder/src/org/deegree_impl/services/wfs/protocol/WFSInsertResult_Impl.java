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

import org.deegree.services.wfs.protocol.*;
import org.deegree.xml.Marshallable;
import org.deegree_impl.tools.Debug;


/**
* The <InsertResult> element is used to delimit one or more 
* feature identifiers of newly created features. The insert results 
* are reported in the order in which the <Insert> operations were 
* specified in the <Transaction> request. Additionally, they can 
* be correlated using the handle attribute.     
*
* <p>--------------------------------------------------------</p>
*
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
* @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
*/
class WFSInsertResult_Impl implements WFSInsertResult, Marshallable {
    private ArrayList featureIds = null;
    private String handle = null;

    /**
     * Creates a new WFSInsertResult_Impl object.
     *
     * @param handle 
     * @param featureIds 
     */
    WFSInsertResult_Impl(String handle, String[] featureIds) {
        this.featureIds = new ArrayList();
        setFeatureIds(featureIds);
        setHandle(handle);
    }

    /**
     * The handle attribute is included to allow a server to associate 
     * any text to the response. The purpose of the handle  attribute 
     * is to provide an error handling mechanism for locating 
     * a statement that might fail. Or to identify an InsertResult.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * @see getHandle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Returns the ids of the inserted features 
     */
    public String[] getFeatureIds() {
        return (String[]) featureIds.toArray(new String[featureIds.size()]);
    }

    /**
     * @see getFeatureIds
     */
    public void addFeatureId(String featureId) {
        featureIds.add(featureId);
    }

    /**
     * @see getFeatureIds
     */
    public void setFeatureIds(String[] featureIds) {
        this.featureIds.clear();

        if (featureIds != null) {
            for (int i = 0; i < featureIds.length; i++) {
                addFeatureId(featureIds[i]);
            }
        }
    }
    
    /**
     * returns the XML reprsentation of the wfs Insert result
     */
    public String exportAsXML() {
        Debug.debugMethodBegin();
        
        StringBuffer ins = new StringBuffer(500);
        ins.append( "<InsertResult>" );
        for (int i = 0; i < featureIds.size(); i++) {
            ins.append( "<FeatureId>").append( featureIds.get(i) ).append( "</FeatureId>" );       
        }
		ins.append( "</InsertResult>" );
	    
        Debug.debugMethodEnd();
        return ins.toString();
    }
    
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSInsertResult_Impl.java,v $
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
 * Revision 1.4  2004/02/09 08:00:22  poth
 * no message
 *
 * Revision 1.3  2003/12/05 09:34:17  poth
 * no message
 *
 * Revision 1.2  2003/04/07 07:26:55  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:26  poth
 * no message
 *
 * Revision 1.3  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.2  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.1  2002/07/10 14:18:26  ap
 * no message
 *
 * Revision 1.3  2002/04/26 09:02:51  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 */
