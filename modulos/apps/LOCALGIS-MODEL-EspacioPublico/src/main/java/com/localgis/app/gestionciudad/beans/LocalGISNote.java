/**
 * LocalGISNote.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.beans;

import java.util.Calendar;


import javax.naming.ConfigurationException;

public class LocalGISNote {
	private Integer id;
	private String description;
	private Calendar startWarning;
	private Document[] listaDeDocumentos;
	private LayerFeatureBean[] featureRelation;
	private Integer userCreator;
	private Integer idMunicipio;
	
	public LocalGISNote(){
		super();
	}
	public LocalGISNote(Integer userCreator,Integer idMunicipio) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null)
			throw new ConfigurationException("UserCreator and idMunicipio Must be not null");
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
	}
	public LocalGISNote(Integer userCreator,Integer idMunicipio, Calendar startWarning) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null)
			throw new ConfigurationException("UserCreator and idMunicipio Must be not null");
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
		this.startWarning = startWarning;
	}
	public LocalGISNote(Integer userCreator,Integer idMunicipio,String description,Calendar startWarning,Document[] listaDeDocumentos,LayerFeatureBean[] featureRelation) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null)
			throw new ConfigurationException("UserCreator and idMunicipio Must be not null");
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
		this.description = description;
		this.startWarning = startWarning;
		this.listaDeDocumentos = listaDeDocumentos;
		this.featureRelation = featureRelation;
	}
	public LocalGISNote(Integer id,Integer userCreator,Integer idMunicipio,String description,Calendar startWarning,Document[] listaDeDocumentos,LayerFeatureBean[] featureRelation) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null || id == null)
			throw new ConfigurationException("UserCreator ,idMunicipio and id Must be not null");
		this.id = id;
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
		this.description = description;
		this.startWarning = startWarning;
		this.listaDeDocumentos = listaDeDocumentos;
		this.featureRelation = featureRelation;
	}
	public Integer getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public Integer getUserCreator() {
		return userCreator;
	}
	public void setUserCreator(Integer userCreator) {
		this.userCreator = userCreator;
	}
	public LayerFeatureBean[] getFeatureRelation() {
		return featureRelation;
	}
	public void setFeatureRelation(LayerFeatureBean[] featureRelation) {
		this.featureRelation = featureRelation;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Calendar getStartWarning() {
		return startWarning;
	}
	public void setStartWarning(Calendar startWarning) {
		this.startWarning = startWarning;
	}
	public Document[] getListaDeDocumentos() {
		return listaDeDocumentos;
	}
	public void setListaDeDocumentos(Document[] listaDeDocumentos) {
		this.listaDeDocumentos = listaDeDocumentos;
	}
	

}
