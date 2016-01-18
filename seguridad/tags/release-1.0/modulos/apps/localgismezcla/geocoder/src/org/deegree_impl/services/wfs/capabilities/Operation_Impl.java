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

import org.deegree.services.wfs.capabilities.Operation;

/**
 * 
 * <p>----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version 2002-03-01
 */
class Operation_Impl implements Operation {
	
	private String name = null;
	
	
   /**
    * individual constructor receiving operation name 
    */	
	Operation_Impl(String name)
	{
		setName( name );
	}
	
		
   /**
    * returns the name of the operation
    */
    public String getName()
    {
    	return name;
    }
    
   /**
    * sets the name of the operation
    */ 
    public void setName(String name)
    {
    	this.name = name;
    }           
	

	public String toString() {
		String ret = null;
		ret = getClass().getName() + ":\n" ;
		ret += "name = " + name + "\n";
		return ret;
	}

	public boolean equals (Object that) {
		if (that instanceof Operation) {
			return ((Operation) that).getName().equals (getName ());
		}
		return false;
	}
}
