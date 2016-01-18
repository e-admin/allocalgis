/**
 * ACMapSimple.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.geopista.model.IGeopistaMap;

public class ACMapSimple implements IACMap,Serializable {

	private String id;
	private String name;
	private String xml;
	private byte[] image;
	private Date timestamp;
	private int idMunicipio;
	private int idEntidad;
	private Collection layerStyles;
	private Collection mapServerLayers;
	private Hashtable layerFamilies;
	private String idEntidadSeleccionada;
	private String locale;

	public ACMapSimple() {

	}

	public ACMapSimple(IGeopistaMap gpMap, Collection mapServerLayers,
			Hashtable layerFamilies, byte[] image,String locale) {

		this.id = gpMap.getSystemId();
		this.name = gpMap.getName();
		this.xml = gpMap.getDescriptor();
		this.image = image;
		
		this.timestamp = gpMap.getTimeStamp();
		this.layerStyles = gpMap.getLayersStylesRelation();
		this.idMunicipio = gpMap.getIdMunicipio();
		this.idEntidad = gpMap.getIdEntidad();
		this.idEntidadSeleccionada=gpMap.getIdEntidadSeleccionada();
		this.locale=locale;
		
		/*Iterator<E> it=mapServerLayers.iterator();
		while (it.hasNext()){
			
		}*/
		this.mapServerLayers=mapServerLayers;

		this.layerFamilies=new Hashtable();
		
		Enumeration e = layerFamilies.keys();
		while( e.hasMoreElements() ){
		  Integer clave = (Integer)e.nextElement();
		  IACLayerFamily acLayerFamily=(IACLayerFamily)layerFamilies.get(clave);
		  ACLayerFamilySimple acLayerFamilySimple=new ACLayerFamilySimple(acLayerFamily);
		  this.layerFamilies.put(clave, acLayerFamilySimple);
		}
		
		
		//this.layerFamilies=layerFamilies;
		

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getXml() {
		return xml;
	}

	public void setImage(byte[] image) {
		this.image = image;

	}

	public byte[] getImage() {
		return image;
	}

	public void settimeStamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Date gettimeStamp() {
		return timestamp;
	}

	public void setIdMunicipio(int idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public int getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdEntidad(int idEntidad) {
		this.idEntidad = idEntidad;
	}

	public int getIdEntidad() {
		return idEntidad;
	}

	public Collection getLayerStyles() {
		return layerStyles;
	}

	public Collection getMapServerLayers() {
		return mapServerLayers;
	}

	public void setMapServerLayers(Collection mapServerLayers) {
		this.mapServerLayers = mapServerLayers;
	}

	@Override
	public Hashtable getLayerFamilies() {
		return layerFamilies;
	}

	@Override
	public void setLayerFamilies(Hashtable layerFamilies) {
		this.layerFamilies=layerFamilies;
		
	}
	@Override
	public String getIdEntidadSeleccionada() {
		return idEntidadSeleccionada;
	}

	@Override
	public void setIdEntidadSeleccionada(String idEntidadSeleccionada) {
		this.idEntidadSeleccionada=idEntidadSeleccionada;
		
	}
	
	@Override
	public String getLocale() {
		return locale;
	}

	@Override
	public void setLocale(String locale) {
		this.locale=locale;
		
	}
	

	@Override
	public void setLayerStyleData(Collection layerStyleData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IGeopistaMap convert(Hashtable htLayers, Hashtable htStyleXMLs,
			Date hora, String sLocale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMapProjection(String projection) {
		// TODO Auto-generated method stub
		return null;
	}




}
