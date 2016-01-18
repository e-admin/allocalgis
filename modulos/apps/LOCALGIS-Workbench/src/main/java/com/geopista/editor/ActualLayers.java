/**
 * ActualLayers.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.editor;

import java.util.ArrayList;
import java.util.HashMap;

import com.vividsolutions.jump.workbench.model.Layerable;

public class ActualLayers {
	
	private ArrayList capasActual=new ArrayList();
	
	private HashMap categoryList=new HashMap();
	
	
	public ActualLayers(){
		
	}

	public void addLayer(String category,Layerable layer){
		categoryList.put(layer.getName(),category);
		capasActual.add(layer);
	}
	
	public ArrayList getLayers(){
		return capasActual;
	}

	public String getCategory(String name) {
		return (String)categoryList.get(name);
	}
	
	
}

