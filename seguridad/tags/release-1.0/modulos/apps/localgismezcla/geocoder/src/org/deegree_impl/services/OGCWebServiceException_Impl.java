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

package org.deegree_impl.services;

import org.deegree.services.OGCWebServiceException;
import org.deegree.xml.Marshallable;
import org.deegree.xml.XMLTools;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p>A web XXX server has to handle two classes of errors; exceptions that are generated
 * during the processing of a request (such as parsing the request) and exceptions that
 * are generated during the execution of a request (such as a transaction failure). This
 * section deals with exceptions generated during the processing of a request.
 * <p>Exceptions encountered during request execution are dealt with in subsequent sections.
 * In the event that a web feature server encounters an error while processing a request, it
 * shall generate an XML document indicating that an error has occurred.
 * <p>The &lt;OGCWebServiceException&gt; tag can delimit one or more exception.details.
 * Individual exceptions are delimited using the &lt;Exception&gt; tag.
 * <p>The optional &lt;Locator%gt; element is used to indicate where an
 * exception was encountered. A number of elements defined in this
 * document include a handle attribute that can be used to uniquely
 * identify an element. If such a handle exists, its value would be
 * reported using the <Locator> element. If a handle attribute does not
 * exist or is not defined, then the web XXX server can attempt to
 * locate the error using other means such as line numbers, etc...
 * <p>The &lt;Message&gt; element is used to delimit any messages associated
 * with an exception
 *
 * <p>-----------------------------------------------------------------------</p>
 *
 * @author Katharina Lupp <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */

public class OGCWebServiceException_Impl implements OGCWebServiceException, Marshallable {
    
    private String locator = null;
    private String message = null;
    
    /**
     * constructor
     */
    public OGCWebServiceException_Impl(String locator, String message) {
        setLocator(locator);
        setMessage(message);
    }
    
    /**
     * constructor
     */
    public OGCWebServiceException_Impl(Document doc) {
        String me = null;
        String lo = null;
        
        Element mess = (Element)doc.getElementsByTagName( "Message" ).item(0);
        me = mess.getFirstChild().getNodeValue();
        
        NodeList nl = doc.getElementsByTagName( "Locator" );
        if ( nl != null && nl.getLength() > 0 ) {
            lo = nl.item(0).getFirstChild().getNodeValue();
        }
        
        setLocator(lo);
        setMessage(me);
    }
    
    /**
     * returns the class/service that has caused the exception
     */
    public String getLocator() {
        return locator;
    }
    
    /**
     * sets the class/service that has caused the exception
     */
    public void setLocator(String locator) {
        this.locator = locator;
    }
    
    
    /**
     * returns the error message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * sets the error message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * export the exception message and locator as OGC WFS conform
     * XML document.
     */
    public String exportAsXML() {
        StringBuffer sb = new StringBuffer(1000);
        sb.append( "<OGCWebServiceException>" );        
        sb.append( "<Exception>" );
        sb.append( "<Message>" );
        sb.append( XMLTools.validateCDATA( message ) );        
        sb.append( "</Message>" );        
        sb.append( "<Locator>" );
        sb.append( XMLTools.validateCDATA( locator ) );        
        sb.append( "</Locator>" );
        sb.append( "</Exception>" );
        sb.append( "</OGCWebServiceException>" );        
        return sb.toString();
    }
    
    public String toString() {
        return exportAsXML();
    }
    
}
/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: OGCWebServiceException_Impl.java,v $
 * Revision 1.1  2009/07/09 07:25:33  miriamperez
 * Rama única LocalGISDOS
 *
 * Revision 1.1  2009/04/13 10:16:50  miriamperez
 * Meto en LocalGISPrivado el módulo del geocoder al completo
 *
 * Revision 1.1  2006/09/04 11:04:01  angeles
 * no message
 *
 * Revision 1.1.1.1  2006/08/17 11:34:00  angeles
 * no message
 *
 * Revision 1.9  2004/01/08 09:50:23  poth
 * no message
 *
 * Revision 1.8  2003/07/21 07:50:47  poth
 * no message
 *
 * Revision 1.7  2003/06/10 07:52:11  poth
 * no message
 *
 * Revision 1.6  2003/05/26 07:17:17  poth
 * no message
 *
 * Revision 1.5  2003/01/27 12:01:20  poth
 * no message
 *
 * Revision 1.4  2002/12/12 13:11:15  poth
 * no message
 *
 * Revision 1.3  2002/11/11 10:42:22  poth
 * no message
 *
 * Revision 1.2  2002/10/29 10:50:27  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:06  poth
 * no message
 *
 * Revision 1.2  2002/08/15 10:01:06  ap
 * no message
 *
 * Revision 1.1  2002/05/17 15:58:30  ap
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
