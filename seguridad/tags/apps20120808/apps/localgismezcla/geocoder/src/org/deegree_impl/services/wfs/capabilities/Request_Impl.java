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


/**
 * The REQUEST parameter indicates which service operation is being invoked.
 * The value shall be the name of one of the operations offered by the OGC 
 * Web Service Instance.
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */

class Request_Impl implements Request {
	
	private GetCapabilities getCapabilities = null;
	private DescribeFeatureType describeFeatureType = null;
	private Transaction transaction = null;
	private GetFeature getFeature = null;
	private GetFeatureWithLock getFeatureWithLock = null;
	private LockFeature lockFeature = null;

	
	/**
	* constructor initializing the class with the <Request>
	*/
	Request_Impl (GetCapabilities getCapabilities,
				  DescribeFeatureType describeFeatureType,
				  Transaction transaction,
				  GetFeature getFeature,
				  GetFeatureWithLock getFeatureWithLock,
				  LockFeature lockFeature)
	{
		setGetCapabilities (getCapabilities);
		setDescribeFeatureType (describeFeatureType);
		setTransaction (transaction);
		setGetFeature (getFeature);
		setGetFeatureWithLock (getFeatureWithLock);
		setLockFeature (lockFeature);
	}

	/**
	 * The &lt;GetCapabilities&gt; element is included to define the
	 * available distributed computing platforms for this
	 * interface.
	 */
	public GetCapabilities getGetCapabilities()
	{
		return getCapabilities;
	}
	
	/**
	* sets the getCapabilities
	*/
	public void setGetCapabilities(GetCapabilities getCapabilities)
	{
		this.getCapabilities = getCapabilities;
	}

	/**
	 * The &lt;DescribeFeatureType&gt; tag isused toindicate what
	 * schema description languages can be used to describe the
	 * schema of a feature type when a client requests such a
	 * description. XMLSCHEMA is the only mandatory
	 * language that must be available. The.SCHEMALANGUAGES entity can be redefined to
	 * include vendor specific languages.
	 */	
	public DescribeFeatureType getDescribeFeatureType()
	{
		return describeFeatureType;
	}
	
	/**
	* sets the <DescribeFeatureType>
	*/
	public void setDescribeFeatureType(DescribeFeatureType describeFeatureType)
	{
		this.describeFeatureType = describeFeatureType;
	}

	/**
	 * The &lt;Transaction&gt; element is included to define the
	 * available distributed computing platforms for this
	 * interface.
	 */	
	public Transaction getTransaction()
	{
		return transaction;
	}
	
	/**
	* sets the <transaction>
	*/
	public void setTransaction(Transaction transaction)
	{
		this.transaction = transaction;
	}

	/**
	 * The &lt;GetFeature&gt; tag isused todefine the formats
	 * available for expressing the results of a query. The
	 * RESULTFORMATS entity defines the mandatory output
	 * format of GML but can be redefined to include additional
	 * vendor specific formats.
	 */	
	public GetFeature getGetFeature()
	{
		return getFeature;
	}
	
	/**
	* sets the <getFeature>
	*/
	public void setGetFeature(GetFeature getFeature)
	{
		this.getFeature = getFeature;
	}

	/**
	 * Get The Feature with Lock. It is the same as GetFeature(),
	 * with a Lock!
	 */	
	public GetFeatureWithLock getGetFeatureWithLock()
	{
		return getFeatureWithLock;
	}
	
	/**
	 * sets the Feature with Lock. 
	 */	
	public void setGetFeatureWithLock(GetFeatureWithLock getFeatureWithLock)
	{
		this.getFeatureWithLock = getFeatureWithLock;
	}
	

	/**
	 * The &lt;LockFeature&gt; element is included to define the
	 * available distributed computing platforms.
	 */	
	public LockFeature getLockFeature()
	{
		return lockFeature;
	}
	
	/**
	* sets the <LockFeature>
	*/
	public void setLockFeature(LockFeature lockFeature)
	{
		this.lockFeature = lockFeature;
	}
	


	public String toString() {
		String ret = null;
		ret = "getCapabilities = " + getCapabilities + "\n";
		ret += "describeFeatureType = " + describeFeatureType + "\n";
		ret += "transaction = " + transaction + "\n";
		ret += "getFeature = " + getFeature + "\n";
		ret += "getFeatureWithLock = " + getFeatureWithLock + "\n";
		ret += "lockFeature = " + lockFeature + "\n";
		return ret;
	}
	
}
/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: Request_Impl.java,v $
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
 * Revision 1.1.1.1  2002/09/25 16:01:21  poth
 * no message
 *
 * Revision 1.8  2002/08/19 15:58:51  ap
 * no message
 *
 * Revision 1.7  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.6  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.5  2002/05/16 15:52:13  ap
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
