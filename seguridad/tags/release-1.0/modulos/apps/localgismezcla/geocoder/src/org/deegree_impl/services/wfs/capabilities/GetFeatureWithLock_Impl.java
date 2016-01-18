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

import org.deegree.services.capabilities.*;
import org.deegree.services.wfs.capabilities.*;

/**
 * The &lt;GetFeature&gt; tag isused todefine the formats
 * available for expressing the results of a query. The
 * RESULTFORMATS entity defines the mandatory output
 * format of GML but can be redefined to include additional
 * vendor specific formats.
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:uzs6tr@uni-bonn.de">Axel Schaefer</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */

class GetFeatureWithLock_Impl extends GetFeature_Impl implements GetFeatureWithLock {
	
	
	GetFeatureWithLock_Impl(String[] resultFormat, String[] classes, DCPType[] dCPType)
	{
		super( resultFormat, classes, dCPType );
	}

    /*#Request lnkWFS_Service;*/	
}


/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: GetFeatureWithLock_Impl.java,v $
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
 * Revision 1.4  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.3  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.2  2002/05/06 16:02:07  ap
 * no message
 *
 * Revision 1.1  2002/04/26 09:05:10  ap
 * no message
 *
 *
 *
 */
