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

import org.deegree.services.wfs.capabilities.Capability;
import org.deegree.services.wfs.capabilities.Request;
import org.w3c.dom.Document;


/**
 * The capabilities section specifies the list of requests that the WFS 
 * can handle. Two classes of web feature servers, based on the capabilities 
 * they support, are defined in the Overview 
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */

class Capability_Impl implements Capability{
	
	private Document vendorSpecificCapabilities = null;
	private Request request = null;
	
	/**
	* default constructor
	*/
	Capability_Impl()
	{
	}
	
	/**
	* constructor initializing the class with the requests
	*/
	Capability_Impl(Request request, Document vendorSpecificCapabilities )
	{
		setVendorSpecificCapabilities (vendorSpecificCapabilities);
		setRequest (request);
	}
	
   /**
    * A basic WFS would include the GetCapabilities, DescribeFeatureType 
    * and the GetFeature interfaces. A transactional WFS would also include
    * the Transaction interface, possibly the LockFeature interface and/or 
    * the GetFeatureWithLock interface.
    */	
	public Request getRequest()
	{
		return request;
	}
	
	/**
	* sets the request
	*/ 
	public void setRequest(Request request)
	{
		this.request = request;
	}
	

   /**
    * returns vendor specific capabilities of the wfs
    */ 
	public Document getVendorSpecificCapabilities()
	{
		return vendorSpecificCapabilities;
	}
	
	/**
    * sets vendor specific capabilities of the wfs
    */ 
	public void setVendorSpecificCapabilities(Document vendorSpecificCapabilities)
	{
		this.vendorSpecificCapabilities = vendorSpecificCapabilities;
	}
	


	public String toString() {
		String ret = null;
		ret = "vendorSpecificCapabilities = " + vendorSpecificCapabilities + "\n";
		ret += "request = " + request + "\n";
		return ret;
	}

    /*#WFS_CapabilitiesResponse lnkWFS_CapabilitiesResponse;*/
}
/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: Capability_Impl.java,v $
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
 * Revision 1.2  2004/01/14 08:23:26  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:21  poth
 * no message
 *
 * Revision 1.6  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.5  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.4  2002/04/26 09:05:10  ap
 * no message
 *
 * Revision 1.2  2002/04/25 16:18:47  ap
 * no message
 *
 */
