/**
 * LocalNetworkDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.localgis.route.network.NetworkProperty;


public class LocalNetworkDAO {
	private static Logger LOGGER = Logger.getLogger(LocalNetworkDAO.class);
	public void getNetworkProperties(String networkName,Map<String,NetworkProperty> networkProperties) throws FileNotFoundException {
		
		Scanner scanner = new Scanner(new File(networkName+".properties"));

	    try {
	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Consultando las propiedades de la red "+ networkName);
		    while(scanner.hasNextLine()){ 
		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Propiedades encontradas");
		    	String[] line = scanner.nextLine().split(";");
		    	String name = line[0];
		    	NetworkProperty property = null;
		    	
		    	if(property != networkProperties.get(name)){
		    		property = (NetworkProperty)networkProperties.get(name);
		    	}else{
		    		property = new NetworkProperty();
		    	}
		    	property.setNetworkManagerProperty(line[1], line[2]);
		    	networkProperties.put(name, property);
		    }
		    scanner.close();
	    	
		} catch (Exception e) {
			LOGGER.error("Network Properties not found!",e);
		} 
		
	}
	
	public void setNetworkProperties(String networkName,Map<String,Object> networkProperties) {
		
		Iterator<String> it = networkProperties.keySet().iterator();
		String networkPropertyName = null;
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Guardando propiedades de la red " + networkName + " en base de datos ");
		try {
		FileWriter fstream = new FileWriter(networkName+".properties");
        BufferedWriter out = new BufferedWriter(fstream);
		while (it.hasNext()){
			
			networkPropertyName = it.next();
			NetworkProperty netProp =  (NetworkProperty) networkProperties.get(networkPropertyName);
			
			for(int i=0; i < netProp.getKeys().size(); i++){
						
					 String textProperty =
						 	networkPropertyName +";"+
						 	netProp.getKeys().get(i) + ";" + 
						 	netProp.getValue(netProp.getKeys().get(i));
							out.write(textProperty);
							out.newLine();
			}
		}
	    out.close();
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	
	public boolean existsNetworkInLocal(String networkFilePath){
		boolean resultado = false;
		File f = new File(networkFilePath);
		if (f.exists()){
			resultado = true;
		} else{
			resultado = false;
		}
		return resultado;
	}
		
	
}
