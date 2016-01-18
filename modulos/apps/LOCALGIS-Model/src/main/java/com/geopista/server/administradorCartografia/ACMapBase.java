/**
 * ACMapBase.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Hashtable;

import com.geopista.model.IGeopistaMap;



/** Datos de un mapa para el administrador de cartografia */
public class ACMapBase implements Serializable{
      


	private String name;
	private byte[] image;
	private String id;
	private String xml;
	private int idMunicipio;
	private Hashtable layerFamilies;
	private Collection mapServerLayers ;
	private Timestamp timestamp;
	private String locale;
	private int idEntidad;

	public ACMapBase(){
    }

	public ACMapBase(IGeopistaMap map) {
		
	}

	public void setName(String name) {
		this.name=name;	
	}

	public String getName() {
		return name;
	}

	public void setImage(byte[] image) {
		this.image=image;
		
	}

	public byte[] getImage() {
		return image;
	}

	public void setId(String id) {
		this.id=id;		
	}

	public String getId() {
		return id;
	}

	public void setXml(String xml) {
		this.xml=xml;
		
	}

	public String getXml() {
		return xml;
	}

	public void setIdMunicipio(int idMunicipio) {
		this.idMunicipio=idMunicipio;
	}

	public int getIdMunicipio() {
		return idMunicipio;
	}

	public void setLayerFamilies(Hashtable layerFamilies) {
		this.layerFamilies=layerFamilies;
		
	}

	public Hashtable getLayerFamilies() {
		return layerFamilies;
	}

	public void setMapServerLayers(Collection mapServerLayers) {
		this.mapServerLayers =mapServerLayers;
	}

	public Collection getMapServerLayers() {
		return mapServerLayers;
	}

	public void setTimeStamp(Timestamp timestamp) {
		this.timestamp=timestamp;		
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setLocale(String locale) {
		this.locale=locale;
	}

	public String getLocale() {
		return locale;
	}

	public void setIdEntidad(int idEntidad) {
		this.idEntidad=idEntidad;
		
	}

	public int getIdEntidad() {
		return idEntidad;
	}

    
}
