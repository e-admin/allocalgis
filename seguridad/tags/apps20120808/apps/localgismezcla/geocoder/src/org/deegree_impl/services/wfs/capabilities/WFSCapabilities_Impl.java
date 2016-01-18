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

import org.deegree.model.geometry.GM_Envelope;
import org.deegree.services.capabilities.HTTP;
import org.deegree.services.capabilities.MetadataURL;
import org.deegree.services.capabilities.Service;
import org.deegree.services.wfs.capabilities.Capability;
import org.deegree.services.wfs.capabilities.FeatureType;
import org.deegree.services.wfs.capabilities.FeatureTypeList;
import org.deegree.services.wfs.capabilities.Operation;
import org.deegree.services.wfs.capabilities.Request;
import org.deegree.services.wfs.capabilities.WFSCapabilities;
import org.deegree_impl.services.capabilities.OGCWebServiceCapabilities_Impl;
import org.w3c.dom.Document;

/**
 * The parent element of the Capabilities document includes as children a Service
 * element with general information about the server, a Capability element with
 * specific information about the kinds of functionality offered by the server
 * and a featureTypeList element defining the list of all feature types available
 * from this server.
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class WFSCapabilities_Impl extends OGCWebServiceCapabilities_Impl
                                                    implements WFSCapabilities {
    
    private Capability capability           = null;
    private FeatureTypeList featureTypeList = null;
    
    
    /**
     * constructor initializing the class with the <Request>
     */
    protected WFSCapabilities_Impl(Capability capability,
                        FeatureTypeList featureTypeList,
                        String version,String updateSequence,
                        Service service) {
        super( version, updateSequence, service );
        setCapability(capability);
        setFeatureTypeList(featureTypeList);
    }
    
    /**
     * A Capability lists available request types, how exceptions may be reported,
     * and whether any vendor-specific capabilities are defined. It also lists all
     * the feature types available from this feature server.
     */
    public Capability getCapability() {
        return capability;
    }
    
    /**
     * sets the <capability>
     */
    public void setCapability(Capability capability) {
        this.capability = capability;
    }
    
    /**
     * The optional VendorSpecificCapabilities element lists any capabilities
     * unique to a particular server. Because the information is not known a
     * priori, it cannot be constrained by this particular DTD. A vendor-specific
     * DTD fragment must be supplied at the start of the XML capabilities document,
     * after the reference to the general WFS_Capabilities DTD.
     */
    public FeatureTypeList getFeatureTypeList() {
        return featureTypeList;
    }
    
    /**
     * sets the <FeatureTypeList>
     */
    public void setFeatureTypeList(FeatureTypeList featureTypeList) {
        this.featureTypeList = featureTypeList;
    }
    
    /**
     * exports the capabilities as OGC WFS conform XML document
     */ 
    public String exportAsXML() {
        StringBuffer sb = new StringBuffer(60000);
        Document doc = null;
        
        try {
            
            sb.append( "<WFS_Capabilities version=\"" + getVersion() + "\" " );
            sb.append( "updateSequence=\"" + getUpdateSequence() + "\">" );
            
            // service section
            sb.append( "<Service><Name>" + getService().getName() + "</Name>" );
            if ( getService().getTitle() != null ) {
                sb.append( "<Title>" + getService().getTitle() + "</Title>" );
            }
            if ( getService().getAbstract() != null ) {
                sb.append( "<Abstract>" + getService().getAbstract() + "</Abstract>" );
            }

			String [] keywords = getService ().getKeywordList();
			if (keywords.length != 0) {
				sb.append ("<Keywords>");
			}
			for (int i = 0; i < keywords.length; i++) {
				sb.append (keywords [i]);
				if (i != keywords.length - 1) {
					sb.append (";");
				}
			}
			if (keywords.length != 0) {
				sb.append ("</Keywords>");
			}
            
            if ( getService().getOnlineResource() != null ) {
                String s = getService().getOnlineResource().getProtocol() + "://";
                s += getService().getOnlineResource().getHost();
                s += ":" + getService().getOnlineResource().getPort();
                s += getService().getOnlineResource().getPath();
                sb.append( "<OnlineResource>" + s + "</OnlineResource>" );
            }
            if ( getService().getFees() != null ) {
                sb.append( "<Fees>" + getService().getFees() + "</Fees>" );
            }
            if ( getService().getAccessConstraints() != null ) {
                sb.append( "<AccessConstraints>" +
                getService().getAccessConstraints() +
                "</AccessConstraints>" );
            }
            sb.append( "</Service>" );
            
            // Capability section
            Request req = getCapability().getRequest();
            sb.append( "<Capability><Request><GetCapabilities><DCPType><HTTP>" );
            HTTP http = (HTTP)req.getGetCapabilities().getDCPType()[0].getProtocol();
            if ( http.getGetOnlineResources() != null ) {
                for (int i = 0; i < http.getGetOnlineResources().length; i++) {
                    sb.append( "<Get onlineResource=\"" +
                    http.getGetOnlineResources()[i].getProtocol() + "://" +
                    http.getGetOnlineResources()[i].getHost() + ":" + 
                    http.getGetOnlineResources()[i].getPort() +
                    http.getGetOnlineResources()[i].getPath() +
                    "\" />" );
                }
            }
            if ( http.getPostOnlineResources() != null ) {
                for (int i = 0; i < http.getPostOnlineResources().length; i++) {
                    sb.append( "<Post onlineResource=\"" +
                    http.getPostOnlineResources()[i].getProtocol() + "://" +
                    http.getPostOnlineResources()[i].getHost() + ":" + 
                    http.getPostOnlineResources()[i].getPort() +
                    http.getPostOnlineResources()[i].getPath() +
                    "\" />" );
                }
            }
            sb.append( "</HTTP></DCPType></GetCapabilities>" );
            
            sb.append( "<DescribeFeatureType><SchemaDescriptionLanguage>" );
            sb.append( "<XMLSCHEMA/></SchemaDescriptionLanguage><DCPType><HTTP>" );
            http = (HTTP)req.getDescribeFeatureType().getDCPType()[0].getProtocol();
            if ( http.getGetOnlineResources() != null ) {
                for (int i = 0; i < http.getGetOnlineResources().length; i++) {
                    sb.append( "<Get onlineResource=\"" +
                    http.getGetOnlineResources()[i].getProtocol() + "://" +
                    http.getGetOnlineResources()[i].getHost()  + ":" + 
                    http.getGetOnlineResources()[i].getPort() +
                    http.getGetOnlineResources()[i].getPath() +
                    "\" />" );
                }
            }
            if ( http.getPostOnlineResources() != null ) {
                for (int i = 0; i < http.getPostOnlineResources().length; i++) {
                    sb.append( "<Post onlineResource=\"" +
                    http.getPostOnlineResources()[i].getProtocol() + "://" +
                    http.getPostOnlineResources()[i].getHost()  + ":" + 
                    http.getPostOnlineResources()[i].getPort() +
                    http.getPostOnlineResources()[i].getPath() +
                    "\" />" );
                }
            }
            sb.append( "</HTTP></DCPType></DescribeFeatureType>" );
            
            if ( req.getTransaction() != null ) {
                sb.append( "<Transaction><DCPType><HTTP>" );
                http = (HTTP)req.getTransaction().getDCPType()[0].getProtocol();
                if ( http.getGetOnlineResources() != null ) {
                    for (int i = 0; i < http.getGetOnlineResources().length; i++) {
                        sb.append( "<Get onlineResource=\"" +
                        http.getGetOnlineResources()[i].getProtocol() + "://" +
                        http.getGetOnlineResources()[i].getHost()  + ":" + 
                        http.getGetOnlineResources()[i].getPort() +
                        http.getGetOnlineResources()[i].getPath() +
                        "\" />" );
                    }
                }
                if ( http.getPostOnlineResources() != null ) {
                    for (int i = 0; i < http.getPostOnlineResources().length; i++) {
                        sb.append( "<Post onlineResource=\"" +
                        http.getPostOnlineResources()[i].getProtocol() + "://" +
                        http.getPostOnlineResources()[i].getHost()  + ":" + 
                        http.getPostOnlineResources()[i].getPort() +
                        http.getPostOnlineResources()[i].getPath() +
                        "\" />" );
                    }
                }
                sb.append( "</HTTP></DCPType></Transaction>" );
            }
            
            if ( req.getGetFeature() != null ) {
                sb.append( "<GetFeature><ResultFormat><XML/><GML2/><FEATURECOLLECTION/>" );
                sb.append( "</ResultFormat><DCPType><HTTP>" );
                http = (HTTP)req.getGetFeature().getDCPType()[0].getProtocol();
                if ( http.getGetOnlineResources() != null ) {
                    for (int i = 0; i < http.getGetOnlineResources().length; i++) {
                        sb.append( "<Get onlineResource=\"" +
                        http.getGetOnlineResources()[i].getProtocol() + "://" +
                        http.getGetOnlineResources()[i].getHost()  + ":" + 
                        http.getGetOnlineResources()[i].getPort() +
                        http.getGetOnlineResources()[i].getPath() +
                        "\" />" );
                    }
                }
                if ( http.getPostOnlineResources() != null ) {
                    for (int i = 0; i < http.getPostOnlineResources().length; i++) {
                        sb.append( "<Post onlineResource=\"" +
                        http.getPostOnlineResources()[i].getProtocol() + "://" +
                        http.getPostOnlineResources()[i].getHost()  + ":" + 
                        http.getPostOnlineResources()[i].getPort() +
                        http.getPostOnlineResources()[i].getPath() +
                        "\" />" );
                    }
                }
                sb.append( "</HTTP></DCPType></GetFeature>" );
            }
            
            if ( req.getGetFeatureWithLock() != null ) {
                sb.append( "<GetFeatureWithLock><ResultFormat><XML/><GML2/><FEATURECOLLECTION/>" );
                sb.append( "</ResultFormat><DCPType><HTTP>" );
                http = (HTTP)req.getGetFeatureWithLock().getDCPType()[0].getProtocol();
                if ( http.getGetOnlineResources() != null ) {
                    for (int i = 0; i < http.getGetOnlineResources().length; i++) {
                        sb.append( "<Get onlineResource=\"" +
                        http.getGetOnlineResources()[i].getProtocol() + "://" +
                        http.getGetOnlineResources()[i].getHost()  + ":" + 
                        http.getGetOnlineResources()[i].getPort() +
                        http.getGetOnlineResources()[i].getPath() +
                        "\" />" );
                    }
                }
                if ( http.getPostOnlineResources() != null ) {
                    for (int i = 0; i < http.getPostOnlineResources().length; i++) {
                        sb.append( "<Post onlineResource=\"" +
                        http.getPostOnlineResources()[i].getProtocol() + "://" +
                        http.getPostOnlineResources()[i].getHost()  + ":" + 
                        http.getPostOnlineResources()[i].getPort() +
                        http.getPostOnlineResources()[i].getPath() +
                        "\" />" );
                    }
                }
                sb.append( "</HTTP></DCPType></GetFeatureWithLock>" );
            }
            
            if ( req.getLockFeature() != null ) {
                sb.append( "<LockFeature><DCPType><HTTP>" );
                http = (HTTP)req.getLockFeature().getDCPType()[0].getProtocol();
                if ( http.getGetOnlineResources() != null ) {
                    for (int i = 0; i < http.getGetOnlineResources().length; i++) {
                        sb.append( "<Get onlineResource=\"" +
                        http.getGetOnlineResources()[i].getProtocol() + "://" +
                        http.getGetOnlineResources()[i].getHost()  + ":" + 
                        http.getGetOnlineResources()[i].getPort() +
                        http.getGetOnlineResources()[i].getPath() +
                        "\" />" );
                    }
                }
                if ( http.getPostOnlineResources() != null ) {
                    for (int i = 0; i < http.getPostOnlineResources().length; i++) {
                        sb.append( "<Post onlineResource=\"" +
                        http.getPostOnlineResources()[i].getProtocol() + "://" +
                        http.getPostOnlineResources()[i].getHost()  + ":" + 
                        http.getPostOnlineResources()[i].getPort() +
                        http.getPostOnlineResources()[i].getPath() +
                        "\" />" );
                    }
                }
                sb.append( "</HTTP></DCPType></LockFeature>" );
            }
            
            sb.append( "</Request></Capability>" );
            
            // FeatureTypeList section
            sb.append( "<FeatureTypeList>" );
            Operation[] operations = getFeatureTypeList().getOperations();
            if ( operations != null ) {
                sb.append( "<Operations>" );
                for (int i = 0; i < operations.length; i++) {
                    sb.append( "<" + operations[i].getName() + "/>" );
                }
                sb.append( "</Operations>" );
            }
            
            FeatureType[] types = getFeatureTypeList().getFeatureTypes();
            
            for (int i = 0; i < types.length; i++) {
                sb.append( "<FeatureType>" );
                sb.append( "<Name>" + types[i].getName() +"</Name>" );
                if ( types[i].getTitle() != null ) {
                    sb.append( "<Title>" + types[i].getTitle() +"</Title>" );
                }
                if ( types[i].getAbstract() != null ) {
                    sb.append( "<Abstract>" + types[i].getAbstract() +"</Abstract>" );
                }
                if ( types[i].getKeywords() != null ) {
                    sb.append( "<Keywords>" );
                    for (int j = 0; j < types[i].getKeywords().length-1; j++) {
                        if ( j != types[i].getKeywords().length-1 ) {
                            sb.append( types[i].getKeywords()[j] + "," );
                        } else {
                            sb.append( types[i].getKeywords()[j] );
                        }
                    }
                    sb.append( "</Keywords>" );
                }
                sb.append( "<SRS>" + types[i].getSrs() +"</SRS>" );
                GM_Envelope bb = types[i].getLatLonBoundingBox();
                sb.append( "<LatLonBoundingBox " );
                sb.append( "minx=\"" + bb.getMin().getX() + "\" " );
                sb.append( "miny=\"" + bb.getMin().getY() + "\" " );
                sb.append( "maxx=\"" + bb.getMax().getX() + "\" " );
                sb.append( "maxy=\"" + bb.getMax().getY() + "\" >" );
                sb.append( "</LatLonBoundingBox>" );
                
                operations = types[i].getOperations();
                if ( operations != null ) {
                    sb.append( "<Operations>" );
                    for (int j = 0; j < operations.length; j++) {
                        sb.append( "<" + operations[j].getName() + "/>" );
                    }
                    sb.append( "</Operations>" );
                }
                
                MetadataURL[] meta = types[i].getMetadataURL();
                if ( meta != null ) {
                    for (int j = 0; j < meta.length; j++) {
                        sb.append( "<MetadataURL  type=\"" + meta[j].getType() + "\" " );
                        sb.append( "format=\"" + meta[j].getFormat() + "\">" );
                        sb.append( meta[j].getOnlineResource().getProtocol() + "://" +
                        meta[j].getOnlineResource().getHost()  + ":" + 
                        meta[j].getOnlineResource().getPort() +
                        meta[j].getOnlineResource().getPath() );
                        sb.append( "</MetadataURL>" );
                    }
                }
                
                sb.append( "</FeatureType>" );
                
            }
            
            sb.append( "</FeatureTypeList></WFS_Capabilities>" );
                        
        } catch(Exception e) {
            System.out.println( e );
        }
        
        return sb.toString();
    }
    
    public String toString() {        
        return exportAsXML();
    }
    
}
