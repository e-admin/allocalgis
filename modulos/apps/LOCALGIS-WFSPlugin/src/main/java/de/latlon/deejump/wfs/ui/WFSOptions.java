/**
 * WFSOptions.java
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
package de.latlon.deejump.wfs.ui;

/**
 * <code>WFSOptions</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: satec $
 * 
 * @version $Revision: 1.1 $, $Date: 2012/09/12 10:52:04 $
 */
public class WFSOptions {

    private int maxFeatures;

    private String[] outputFormats;

    private String selectedOutputFormat;

    private String[] protocols;

    private String selectedProtocol;

    /**
     * @param maxFeatures
     * @param outputFormats
     * @param protocols
     */
    public WFSOptions( int maxFeatures, String[] outputFormats, String[] protocols ) {
        setMaxFeatures( maxFeatures );
        setOutputFormats( outputFormats );
        setProtocols( protocols );
    }

    /**
     * 
     */
    public WFSOptions() {
        this( 1000, new String[] { "GML2", "text/xml; subtype=gml/3.1.1" }, new String[] { "GET", "POST" } );
    }

    /**
     * @return the max features setting to be used in the request
     */
    public int getMaxFeatures() {
        return maxFeatures;
    }

    /**
     * @param maxFeatures
     */
    public void setMaxFeatures( int maxFeatures ) {
        if ( maxFeatures < 1 ) {
            throw new IllegalArgumentException( "maxFeatures must be a number greater than 1." );
        }
        this.maxFeatures = maxFeatures;
    }

    /**
     * @return the list of output formats
     */
    public String[] getOutputFormats() {

        return outputFormats;
    }

    /**
     * @param outputFormats
     */
    public void setOutputFormats( String[] outputFormats ) {
        if ( outputFormats == null || outputFormats.length == 0 ) {
            throw new IllegalArgumentException( "outputFormats cannot be null or have zero length" );
        }
        this.outputFormats = outputFormats;
        // the selected is the first one in the list
        setSelectedOutputFormat( this.outputFormats[1] );
    }

    /**
     * @return the list of protocols (unused?)
     */
    public String[] getProtocols() {
        return protocols;
    }

    /**
     * @param protocols
     */
    public void setProtocols( String[] protocols ) {
        if ( protocols == null || protocols.length == 0 ) {
            throw new IllegalArgumentException( "outputFormats cannot be null or have zero length" );
        }
        this.protocols = protocols;
        // the selected is the first one in the list
        setSelectedProtocol( this.protocols[0] );

    }

    /**
     * @return the selected output format
     */
    public String getSelectedOutputFormat() {
        return selectedOutputFormat;
    }

    /**
     * @param selectedOutputFormat
     */
    public void setSelectedOutputFormat( String selectedOutputFormat ) {
        if ( selectedOutputFormat == null ) {
            throw new IllegalArgumentException( "selectedOutputFormat cannot be null or have zero length" );
        }
        this.selectedOutputFormat = selectedOutputFormat;
    }

    /**
     * @return the selected protocol (unused?)
     */
    public String getSelectedProtocol() {
        return selectedProtocol;
    }

    /**
     * @param selectedProtocol
     */
    public void setSelectedProtocol( String selectedProtocol ) {
        if ( selectedProtocol == null ) {
            throw new IllegalArgumentException( "selectedProtocol cannot be null or have zero length" );
        }
        this.selectedProtocol = selectedProtocol;
    }

}
