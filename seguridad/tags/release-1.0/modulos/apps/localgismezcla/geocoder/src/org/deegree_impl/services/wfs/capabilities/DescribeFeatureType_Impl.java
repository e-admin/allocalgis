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

import org.deegree.services.wfs.capabilities.*;
import org.deegree.services.capabilities.*;

import java.util.*;
/**
 * The %lt;DescribeFeatureType&gt; tag isused to indicate what
 * schema description languages can be used to describe the
 * schema of a feature type when a client requests such a
 * description. XMLSCHEMA is the only mandatory
 * language that must be available. The
 * SCHEMALANGUAGES entity can be redefined to
 * include vendor specific languages.
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */

class DescribeFeatureType_Impl implements DescribeFeatureType  {
	
	private ArrayList schemaDescriptionLanguage = null;
	private ArrayList dCPType = null;
	
	/**
	* default constructor
	*/
	DescribeFeatureType_Impl()
	{
		dCPType = new ArrayList ();
		schemaDescriptionLanguage = new ArrayList();
	}
	
	/**
	* constructor initializing the class with the <DescribeFeatureType>
	*/
	DescribeFeatureType_Impl(String[] schemaDescriptionLanguage, DCPType[] dCPType)
	{
		this();
		setDCPType( dCPType );
		setSchemaDescriptionLanguage(schemaDescriptionLanguage);
		
	}
	
	/**
	 * This entity can be redefined to include vendor specific languages.
	 * XMLSCHEMA is the only mandatory language that must be available.
	 */
	public String[] getSchemaDescriptionLanguage()
	{
		int c = schemaDescriptionLanguage.size();
		return (String[])schemaDescriptionLanguage.toArray( new String[c] );
	}
	
   /**
    * adds a schema description language to the describe feature type object
    */	
	public void addSchemaDescriptionLanguage(String schemaDescriptionLanguage)
	{
		this.schemaDescriptionLanguage.add( schemaDescriptionLanguage );
	}
	
	/**
	* sets the <SchemaDescriptionLanguage>
	*/
	public void setSchemaDescriptionLanguage(String[] schemaDescriptionLanguage)
	{
		this.schemaDescriptionLanguage.clear();
		if ( schemaDescriptionLanguage != null ) {
			for (int i = 0; i < schemaDescriptionLanguage.length; i++) {
				addSchemaDescriptionLanguage( schemaDescriptionLanguage[i] );
		    }
		}
	}

	/**
     * adds the available Distributed Computing Platforms (DCPs) for a operation.
     * At present, only HTTP (GET & POST) is defined.
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
     	if ( dCPType != null ) {
	     	for (int i = 0; i<dCPTypes.length; i++){
	     		this.dCPType.add(dCPTypes[i]);
	     	}
	    }
     }

	
	public String toString() {
		String ret = null;
		ret = "schemaDescriptionLanguage = " + schemaDescriptionLanguage + "\n";
		ret += "dCPType = " + dCPType + "\n";
		return ret;
	}

    /*#Request lnkWFS_Service;*/	
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: DescribeFeatureType_Impl.java,v $
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
 * Revision 1.1.1.1  2002/09/25 16:01:23  poth
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
