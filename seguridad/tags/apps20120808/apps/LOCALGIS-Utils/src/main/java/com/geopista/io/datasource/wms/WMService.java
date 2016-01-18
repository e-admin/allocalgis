



/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.geopista.io.datasource.wms;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.vividsolutions.wms.Capabilities;
import com.vividsolutions.wms.MapRequest;

/**
 * Represents a remote WMS Service.
 *
 * @author Chris Hodgson chodgson@refractions.net
 */
public class WMService extends com.vividsolutions.wms.WMService{
  private Capabilities cap;

  /**
   * Constructs a WMService object from a server URL.
   * @param serverUrl the URL of the WMS server
   */
  public WMService( String serverUrl ) {
  	super(serverUrl);
  }

  /**
   * Connect to the service and get the capabilities.
   * This must be called before anything else is done with this service.
   */
  public void initialize() throws IOException {
    String requestUrlString = getServerUrl() + "SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities";
    URL requestUrl = new URL( requestUrlString );
    InputStream inStream = requestUrl.openStream();
    Parser p = new Parser();
    cap = p.parseCapabilities( this, inStream );
  }


  /**
   * Gets the title of the map service.
   * The service must have previously been initialized, otherwise null is returned.
   * @return the title of the WMService
   */
  public String getTitle() {
    return cap.getTitle();
  }

  /**
   * Gets the Capabilities for this service.
   * The service must have previously been initialized, otherwise null is returned.
   * @return a copy of the MapDescriptor for this service
   */
  public com.vividsolutions.wms.Capabilities getCapabilities() {
    return cap;
  }

  /**
   * Creates a new MapRequest object which can be used to retrieve a Map
   * from this service.
   * @return a MapRequest object which can be used to retrieve a map image
   *         from this service
   */
  public MapRequest createMapRequest() {
    return new MapRequest( this );
  }

}
