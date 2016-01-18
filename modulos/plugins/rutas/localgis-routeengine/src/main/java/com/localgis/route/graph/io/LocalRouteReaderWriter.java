/**
 * LocalRouteReaderWriter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.route.graph.io.FileRouteEngineReaderWriter;

import com.localgis.route.network.NetworkProperty;


public class LocalRouteReaderWriter extends FileRouteEngineReaderWriter implements NetworkPropertiesReaderWriter 
{
	
	private static Logger LOGGER = Logger.getLogger(LocalRouteReaderWriter.class);
	
	
	public LocalRouteReaderWriter(String filename)
	{
	super(filename + ".network");
	}
	

	/* (non-Javadoc)
	 * @see com.localgis.route.graph.io.NetworkPropertiesReaderWriter#writeNetworkProperties(java.util.Map)
	 */
	@Override
	public void writeNetworkProperties( Map<String, Object> networkProperties){
		LocalNetworkDAO propDAO = new LocalNetworkDAO();
		String key = "savedDate";
		networkProperties.put(key, new NetworkProperty(key,DateFormat.getDateTimeInstance().format(new Date())));
		propDAO.setNetworkProperties((String) getProperty(FILENAME), networkProperties);
	}	  
	
	/* (non-Javadoc)
	 * @see com.localgis.route.graph.io.NetworkPropertiesReaderWriter#readNetworkProperties()
	 */
	@Override
	public  Map<String, NetworkProperty> readNetworkProperties(){
		LocalNetworkDAO propDAO = new LocalNetworkDAO();
		HashMap<String, NetworkProperty> networkProperties = new HashMap<String, NetworkProperty>();
		try {
			propDAO.getNetworkProperties((String) getProperty(FILENAME), networkProperties);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}
		return networkProperties;
	}


	

	@Override
	public Graph read() throws IOException
	{
	    String filename=getFileName();
	    File file=new File(filename);
	    File parentFile = file.getParentFile();
	    if (!file.exists() 
		    && parentFile.exists()) // aun no se ha guardado por primera vez
		{
		    return (Graph) new BasicGraph(Collections.EMPTY_LIST,Collections.EMPTY_LIST);
		}
	    else
		{
		    return super.read();
		}
	}


	@Override
	public void deleteAll()
	{
	    File file=new File(getFileName());
	    file.delete();
	}
	
	

}