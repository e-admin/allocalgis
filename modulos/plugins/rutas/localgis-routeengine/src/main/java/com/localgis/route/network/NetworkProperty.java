/**
 * NetworkProperty.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class NetworkProperty implements Serializable {
	private HashMap<String,String> propertyList = new HashMap<String, String>();
	public static final String BASIC_CONFIGURATION = "basic";
	public static final String LAYER_CONFIGURATION = "layer";
	public static final String DESCRIPTION_CONFIGURATION = "description";
	
	public NetworkProperty()
	{
		
	}
	public NetworkProperty(String key, String value)
	{
	    this();
	    this.setNetworkManagerProperty(key, value);
	}
	public String getValue(String key){
		return propertyList.get(key);
	}
	public void setNetworkManagerProperty(String key,String value){
		propertyList.put(key, value);
	}
	public ArrayList<String> getKeys(){
		Set<String> keys = propertyList.keySet();
		Iterator<String> it = keys.iterator();
		ArrayList<String> list = new ArrayList<String>();
		while (it.hasNext()){
			list.add(it.next());
		}
		return list;
	}
}
