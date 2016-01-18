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
