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
package org.deegree_impl.services.capabilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.deegree.services.capabilities.HTTP;
import org.deegree.xml.Marshallable;
import org.deegree_impl.tools.NetWorker;

/**
 * The HTTP-Protocol, which extends the super-interface
 * Protocol.java. It defines the GetOnlineResource and
 * the getPostOnlineResource methods.
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class HTTP_Impl implements HTTP, Marshallable {
    private ArrayList getOnlineResource = null;
    private ArrayList postOnlineResource = null;

    /**
    * default constructor
    */
    public HTTP_Impl() {
        getOnlineResource = new ArrayList();
        postOnlineResource = new ArrayList();
    }

    /**
    * constructor initializing the class with get and post URL
    */
    public HTTP_Impl( URL[] getOnlineResource, URL[] postOnlineResource ) {
        this();

        setGetOnlineResources( getOnlineResource );
        setPostOnlineResources( postOnlineResource );
    }

    /**
    * returns URL prefix for get HTTP request method.    
    */
    public URL[] getGetOnlineResources() {
        return (URL[])getOnlineResource.toArray( new URL[getOnlineResource.size()] );
    }

    /**
     * adds a get-online resource to the HTTP protocol class
     */
    public void addGetOnlineResource( URL getOnlineResource ) {
        this.getOnlineResource.add( getOnlineResource );
    }

    /**
    * sets URL prefix for get HTTP request method.
    */
    public void setGetOnlineResources( URL[] getOnlineResource ) {
        this.getOnlineResource.clear();

        if ( getOnlineResource != null ) {
            for ( int i = 0; i < getOnlineResource.length; i++ ) {
                addGetOnlineResource( getOnlineResource[i] );
            }
        }
    }

    /**
    * returns URL prefix for post HTTP request method.
    */
    public URL[] getPostOnlineResources() {
        return (URL[])postOnlineResource.toArray( new URL[postOnlineResource.size()] );
    }

    /**
    * sets URL prefix for post HTTP request method.
    */
    public void setPostOnlineResources( URL[] postOnlineResource ) {
        this.postOnlineResource.clear();

        if ( postOnlineResource != null ) {
            for ( int i = 0; i < postOnlineResource.length; i++ ) {
                addPostOnlineResource( postOnlineResource[i] );
            }
        }
    }

    /**
    * adds a post-online resource to the HTTP protocol class
    */
    public void addPostOnlineResource( URL postOnlineResource ) {
        this.postOnlineResource.add( postOnlineResource );
    }

    /**
     * Returns an XML representation of this object.
     */
    public String exportAsXML () {
        StringBuffer sb = new StringBuffer ();

        sb.append ("<HTTP>");

        if (getOnlineResource != null && getOnlineResource.size () > 0) {
            sb.append ("<Get>");
            Iterator it = getOnlineResource.iterator ();            
            while (it.hasNext ()) {
                sb.append ("<OnlineResource ")
                  .append ("xmlns:xlink=\"http://www.w3.org/1999/xlink\" ")
                  .append ("xlink:type=\"simple\" xlink:href=\"")
                  .append (NetWorker.url2String (((URL) it.next ())))
                  .append ("\"/>");
            }
            sb.append ("</Get>");
        }

        if (postOnlineResource != null && postOnlineResource.size () > 0) {
            sb.append ("<Post>");
            Iterator it = postOnlineResource.iterator ();            
            while (it.hasNext ()) {
                sb.append ("<OnlineResource ")
                  .append ("xmlns:xlink=\"http://www.w3.org/1999/xlink\" ")
                  .append ("xlink:type=\"simple\" xlink:href=\"")
                  .append (NetWorker.url2String (((URL) it.next ())))
                  .append ("\"/>");
            }
            sb.append ("</Post>");
        }        
        
        sb.append ("</HTTP>");

        return sb.toString ();
    }        
}