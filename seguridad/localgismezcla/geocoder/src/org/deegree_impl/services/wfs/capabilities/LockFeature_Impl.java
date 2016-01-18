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
 * Defines the access to the elements/attributes of the LockFeature
 * tag of a capabilities response .
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class LockFeature_Impl implements LockFeature {
    /*#Request lnkWFS_Service_Cap;*/
    
	private ArrayList dCPType = null;
	
	/**
	* default constructor
	*/
	LockFeature_Impl ()
	{
		dCPType = new ArrayList();
	}
	
	/**
	* constructor initializing the class with the <FeatureType>
	*/
	LockFeature_Impl(DCPType[] dCPType)
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
     	return (DCPType[])dCPType.toArray(new DCPType [dCPType.size ()]);
     }
     
     /**
     * adds the available Distributed Computing Platforms (DCPs) for a operation.
     * At present, only HTTP (GET & POST) is defined.
     */
     public void addDCPType(DCPType dCPType)
     {
     	this.dCPType.add (dCPType);
     }
     
     /**
     * adds the available Distributed Computing Platforms (DCPs) for a operation.
     * At present, only HTTP (GET & POST) is defined.
     */
     public void setDCPType (DCPType[] dCPTypes)
     {
     	this.dCPType.clear();
     	for (int i = 0; i<dCPTypes.length; i++){
     		this.dCPType.add(dCPTypes[i]);
     	}
     }
     
	public String toString() {
		String ret = null;
		ret = "dCPType = " + dCPType + "\n";
		return ret;
	}
    
}
