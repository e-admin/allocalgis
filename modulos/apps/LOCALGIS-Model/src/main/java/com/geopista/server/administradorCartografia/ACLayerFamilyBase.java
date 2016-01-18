/**
 * ACLayerFamilyBase.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.Hashtable;



public class ACLayerFamilyBase implements Serializable{
    int id;
    String name;
    String description;
    Hashtable layers;


    
    public ACLayerFamilyBase(){
    }

   
	public int getId() {
        return id;
    }

	public void setId(int id) {
        this.id = id;
    }

	public String getName() {
        return name;
    }

	public void setName(String name) {
        this.name = name;
    }

	
	public Hashtable getLayers() {
        return layers;
    }

	public void setLayers(Hashtable layers) {
        this.layers = layers;
    }
	/*

	public String getDescription() {
        return description;
    }

	public void setDescription(String description) {
        this.description = description;
    }   
*/	
    
}
