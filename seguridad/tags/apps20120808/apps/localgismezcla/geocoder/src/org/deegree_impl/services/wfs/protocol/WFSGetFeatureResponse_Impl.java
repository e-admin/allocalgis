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

import org.deegree.services.*;
import org.deegree.services.wfs.protocol.*;

import org.w3c.dom.*;


/**
 * The response toaGetFeature request is controlled by the outputFormat
 * attribute. The default value for the outputFormat attribute shall
 * be GML indicating that a WFS must generate a GML document of the
 * result set that conforms to the Geography Markup Language (GML) 2.0
 * specification. Vendor specific output formats can also be generated
 * but they must be declared in the capabilities document.
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class WFSGetFeatureResponse_Impl extends WFSBasicResponse_Impl implements WFSGetFeatureResponse {
    private Object response = null;

    /**
     * constructor initializing the class with the <WFSLockFeatureResponse>
     */
    WFSGetFeatureResponse_Impl(OGCWebServiceRequest request, String[] affectedFeatureTypes, 
                               Document exception, Object response) {
        super(request, exception, affectedFeatureTypes);
        setResponse(response);
    }

    /**
     * returns the request features in a vendor specific format
     */
    public Object getResponse() {
        return response;
    }

    /**
     * sets the request features in a vendor specific format
     */
    public void setResponse(Object response) {
        this.response = response;
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = this.getClass().getName() + ":\n";
        ret = getClass().getName() + ":\n";
        ret += ("response = " + response + "\n");
        return ret;
    }
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSGetFeatureResponse_Impl.java,v $
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
 * Revision 1.3  2004/02/09 08:00:21  poth
 * no message
 *
 * Revision 1.2  2003/04/07 07:26:54  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:26  poth
 * no message
 *
 * Revision 1.8  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.7  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.6  2002/07/19 14:48:20  ap
 * no message
 *
 * Revision 1.5  2002/05/14 14:39:51  ap
 * no message
 *
 * Revision 1.4  2002/05/13 16:11:02  ap
 * no message
 *
 * Revision 1.3  2002/04/26 09:05:36  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 */
