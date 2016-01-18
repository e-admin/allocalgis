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

import org.deegree.services.wfs.capabilities.*;
import org.deegree.services.capabilities.*;


/**
 * In the particular case of a Web Map Service, the response of a
 * GetCapabilities request is general information about the service 
 * itself and specific information about the available maps.
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */

class GetCapabilities_Impl implements GetCapabilities{
	
	private ArrayList dCPType = null;
	
	/**
	* default constructor
	*/
	GetCapabilities_Impl()
	{
		dCPType = new ArrayList ();
	}
	
	/**
	* constructor initializing the class with the requests
	*/
	GetCapabilities_Impl(DCPType[] dCPType)
	{
		this();
		setDCPType( dCPType );
	}
	
	/**
    * The only available distributed computing platform is HTTP
    * for which two request methods are defined; GET and POST. 
    * The onlineResource attribute indicates the URL prefix for 
    * HTTP GET requests (everything before the question mark and 
    * query string:http://hostname[:port]/path/scriptname); for HTTP POST 
    * requests, onlineResource is the complete URL.
    */ 
    public DCPType[] getDCPType()
    {
    	return (DCPType[])dCPType.toArray(new DCPType[dCPType.size ()]);
    }
    
    /**
    * adds the DCPType
    */
    public void addDCPType (DCPType dCPType)
    {
    	this.dCPType.add(dCPType);
	}
	
	/**
	* sets the DCPType
	*/
	public void setDCPType (DCPType[] dCPType)
	{
		this.dCPType.clear();
		for (int i= 0; i<dCPType.length; i++){
			this.dCPType.add(dCPType[i]);
		}
	}

	
	public String toString() {
		String ret = null;
		ret = "dCPType = " + dCPType + "\n";
		return ret;
	}
    /*#Request lnkWFS_CapabilitiesResponse;*/
}
/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: GetCapabilities_Impl.java,v $
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
 * Revision 1.1.1.1  2002/09/25 16:01:22  poth
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
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 */
