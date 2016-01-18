/**
 * GeoserverWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.geoserver;

import java.net.MalformedURLException;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder.ProjectionPolicy;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;

public class GeoserverWS {

	public static final String WORKSPACE_CITE = "cite";
	
	private GeoServerRESTReader reader = null;
	private GeoServerRESTPublisher publisher = null;
	
	public GeoserverWS(String url, String user, String password) throws MalformedURLException{
		reader = new GeoServerRESTReader(url, user, password);
		publisher = new GeoServerRESTPublisher(url, user, password);
	}
	
	public boolean addDataStore(String datasourceName, String datasourceHost, Integer datasourcePort, String datasourceDatabase, String datasourceSchema, String datasourceUser, String datasourcePassword){
		if(reader.getDatastore(WORKSPACE_CITE, datasourceName) == null){
			GSPostGISDatastoreEncoder gsPostGISDatastoreEncoder = new GSPostGISDatastoreEncoder();
			gsPostGISDatastoreEncoder.setName(datasourceName);
			gsPostGISDatastoreEncoder.setHost(datasourceHost);
			gsPostGISDatastoreEncoder.setPort(datasourcePort);
			gsPostGISDatastoreEncoder.setDatabase(datasourceDatabase);
			gsPostGISDatastoreEncoder.setSchema(datasourceSchema);
			gsPostGISDatastoreEncoder.setUser(datasourceUser);
			gsPostGISDatastoreEncoder.setPassword(datasourcePassword);				
			gsPostGISDatastoreEncoder.setEnabled(true);
			return publisher.createPostGISDatastore(WORKSPACE_CITE, gsPostGISDatastoreEncoder);
		}
		return true;
	}
	
	public boolean publishLayer(String datasourceName, String layerName, String srid, String layerSldStyle, Integer idMap){	
		if(reader.getLayer(layerName) == null){
			GSFeatureTypeEncoder gsFeatureTypeEncoder = new GSFeatureTypeEncoder();
			gsFeatureTypeEncoder.setProjectionPolicy(ProjectionPolicy.FORCE_DECLARED);
			gsFeatureTypeEncoder.setSRS("EPSG:" + srid);
			gsFeatureTypeEncoder.setEnabled(true);
			gsFeatureTypeEncoder.setName(layerName);
			
			GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();
			String styleName = layerName;
			if(idMap != null)
				styleName += "(" + idMap + ")";
			if(layerSldStyle!=null && layerSldStyle!="" && publishLayerSldStyle(styleName, layerSldStyle)){
				gsLayerEncoder.setDefaultStyle(styleName);
			}
			gsLayerEncoder.setEnabled(true);
			
			return publisher.publishDBLayer(WORKSPACE_CITE, datasourceName, gsFeatureTypeEncoder, gsLayerEncoder);
		}
		return true;
	}
	
	public boolean publishLayerSldStyle(String layerName, String layerSldStyle){	
		if(reader.getSLD(layerName) == null){						
			return publisher.publishStyle(layerSldStyle, layerName);						
		}
		return true;
	}
	
}
