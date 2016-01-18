/**
 * WFServiceWrapper_1_1_0.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * (c) 2007 by lat/lon GmbH
 *
 * @author Ugo Taddei (taddei@latlon.de)
 *
 * This program is free software under the GPL (v2.0)
 * Read the file LICENSE.txt coming with the sources for details.
 */

package de.latlon.deejump.wfs.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.deegreewfs.datatypes.QualifiedName;
import org.deegreewfs.framework.xml.DOMPrinter;
import org.deegreewfs.ogcwebservices.getcapabilities.DCPType;
import org.deegreewfs.ogcwebservices.getcapabilities.HTTP;
import org.deegreewfs.ogcwebservices.getcapabilities.InvalidCapabilitiesException;
import org.deegreewfs.ogcwebservices.wfs.capabilities.WFSCapabilities;
import org.deegreewfs.ogcwebservices.wfs.capabilities.WFSCapabilitiesDocument;
import org.deegreewfs.ogcwebservices.wfs.capabilities.WFSFeatureType;
import org.xml.sax.SAXException;

import de.latlon.deejump.wfs.DeeJUMPException;
import de.latlon.deejump.wfs.auth.UserData;

/**
 * This class represents a WFService. It handles connection with the server behind the given URL. It
 * also caches Feature Information such as which propertis belong to a FeatueType
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * 
 */
public class WFServiceWrapper_1_1_0 extends AbstractWFSWrapper {

    private static Logger LOG = Logger.getLogger( WFServiceWrapper_1_1_0.class );

    private WFSCapabilities wfsCapabilities;

    private String[] featureTypes;

    private String getFeatureUrl;

    private String capsString;

    /**
     * @param logins
     * @param wfsURL
     * @throws DeeJUMPException
     */
    public WFServiceWrapper_1_1_0( UserData logins, String wfsURL ) throws DeeJUMPException {
        super( logins, wfsURL );
        init();
    }

    private void init()
                            throws DeeJUMPException {

        createHttpClient();

        WFSCapabilitiesDocument wfsCapsDoc = new WFSCapabilitiesDocument();
        try {
            String caps = getCapabilitiesURL();
            LOG.debug( "Sending capabilities request: " + caps );
            wfsCapsDoc.load( new URL( caps ) );
        } catch ( MalformedURLException e ) {
            LOG.error( "Invalid URL", e );
            throw new DeeJUMPException( e );
        } catch ( IOException e ) {
            LOG.error( "IOException when opening: " + getCapabilitiesURL(), e );
            throw new DeeJUMPException( e );
        } catch ( SAXException e ) {
            throw new DeeJUMPException( e );
        }

        capsString = DOMPrinter.nodeToString( wfsCapsDoc.getRootElement(), "" );

        try {

            wfsCapabilities = (WFSCapabilities) wfsCapsDoc.parseCapabilities();
        } catch ( InvalidCapabilitiesException e ) {
            LOG.error( "Could not initialize WFS capabilities", e );
            throw new DeeJUMPException( e );
        }
    }

    private synchronized String[] extractFeatureTypes() {

        String[] fts = null;

        WFSFeatureType[] featTypes = wfsCapabilities.getFeatureTypeList().getFeatureTypes();
        ftNameToWfsFT = new HashMap<String, WFSFeatureType>();
        fts = new String[featTypes.length];
        for ( int i = 0; i < fts.length; i++ ) {
            QualifiedName qn = featTypes[i].getName();
            fts[i] = qn.getLocalName();
            // well, putting prefix + : + simple name
            // should consider to put simple name only!
            /*
             * ftNameToWfsFT.put( qn.getPrefix() + ":" + qn.getLocalName(), featTypes[i] );
             */
            ftNameToWfsFT.put( qn.getLocalName(), featTypes[i] );
        }

        return fts;
    }

    @Override
    public String[] getFeatureTypes() {
        if ( featureTypes == null ) {
            featureTypes = extractFeatureTypes();
        }
        return featureTypes;
    }

    @Override
    public String getGetFeatureURL() {

        org.deegreewfs.ogcwebservices.getcapabilities.Operation[] ops = this.wfsCapabilities.getOperationsMetadata().getOperations();
        getFeatureUrl = null;

        for ( int i = 0; i < ops.length && getFeatureUrl == null; i++ ) {

            if ( ops[i].getName().equals( "GetFeature" ) ) {
                DCPType[] dcps = ops[i].getDCPs();
                if ( dcps.length > 0 ) {
                    getFeatureUrl = ( (HTTP) dcps[0].getProtocol() ).getPostOnlineResources()[0].toString();
                }
            }
        }

        if ( getFeatureUrl == null ) {
            throw new RuntimeException( "Service does not have a GetFeature operation accessible by HTTP POST." );
        }

        return getFeatureUrl;
    }

    @Override
    public String getCapabilitesAsString() {
        return this.capsString;
    }

    @Override
    public String getServiceVersion() {
        return "1.1.0";
    }

    @Override
    protected String createDescribeFTOnlineResource() {
        org.deegreewfs.ogcwebservices.getcapabilities.Operation[] ops = this.wfsCapabilities.getOperationsMetadata().getOperations();
        String descrFtUrl = null;
        for ( int i = 0; i < ops.length && descrFtUrl == null; i++ ) {

            if ( ops[i].getName().equals( "DescribeFeatureType" ) ) {
                DCPType[] dcps = ops[i].getDCPs();
                if ( dcps.length > 0 ) {
                    descrFtUrl = ( (HTTP) dcps[0].getProtocol() ).getGetOnlineResources()[0].toString();
                }

                if ( descrFtUrl == null ) {
                    descrFtUrl = ( (HTTP) dcps[0].getProtocol() ).getPostOnlineResources()[0].toString();
                }
            }
        }
        return descrFtUrl;
    }

    protected String createCapabilitiesOnlineResource() {
        throw new UnsupportedOperationException( "TODO" );
    }

}
