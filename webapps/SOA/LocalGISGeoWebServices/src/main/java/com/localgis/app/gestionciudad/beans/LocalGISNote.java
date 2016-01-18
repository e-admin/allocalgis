/**
 * LocalGISNote.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.beans;

import java.io.Serializable;
import java.util.Calendar;

import javax.naming.ConfigurationException;

public class LocalGISNote implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String description;
	private Calendar startWarning;
	private Document[] listaDeDocumentos;
	private LayerFeatureBean[] featureRelation;
	private Integer userCreator;
	private Integer idMunicipio;
	private Integer idEntidad;
	public LocalGISNote(){
		super();
	}
	
	public LocalGISNote(Integer userCreator,Integer idMunicipio, Integer idEntidad) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null)
			throw new ConfigurationException("UserCreator and idMunicipio Must be not null");
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
		this.idEntidad = idEntidad;
	}
	
	public LocalGISNote(Integer userCreator,Integer idMunicipio) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null)
			throw new ConfigurationException("UserCreator and idMunicipio Must be not null");
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
	}
	
	public LocalGISNote(Integer userCreator,Integer idMunicipio, Calendar startWarning, Integer idEntidad) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null)
			throw new ConfigurationException("UserCreator and idMunicipio Must be not null");
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
		this.startWarning = startWarning;
		this.idEntidad = idEntidad;
	}
	
	public LocalGISNote(Integer userCreator,Integer idMunicipio, Calendar startWarning) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null)
			throw new ConfigurationException("UserCreator and idMunicipio Must be not null");
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
		this.startWarning = startWarning;
	}
	public LocalGISNote(Integer userCreator,Integer idMunicipio,String description,Calendar startWarning,Document[] listaDeDocumentos,LayerFeatureBean[] featureRelation, Integer idEntidad) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null)
			throw new ConfigurationException("UserCreator and idMunicipio Must be not null");
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
		this.description = description;
		this.startWarning = startWarning;
		this.listaDeDocumentos = listaDeDocumentos;
		this.featureRelation = featureRelation;
		this.idEntidad = idEntidad;
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
	public LocalGISNote(Integer id,Integer userCreator,Integer idMunicipio,String description,Calendar startWarning,Document[] listaDeDocumentos,LayerFeatureBean[] featureRelation, Integer idEntidad) throws ConfigurationException{
		if(userCreator == null || idMunicipio == null || id == null)
			throw new ConfigurationException("UserCreator ,idMunicipio and id Must be not null");
		this.id = id;
		this.userCreator = userCreator;
		this.idMunicipio = idMunicipio;
		this.description = description;
		this.startWarning = startWarning;
		this.listaDeDocumentos = listaDeDocumentos;
		this.featureRelation = featureRelation;
		this.idEntidad = idEntidad;
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
	public Integer getIdEntidad() {
		return idEntidad;
	}
	public void setIdEntidad(Integer idEntidad) {
		this.idEntidad = idEntidad;
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
	
	
	// TODO Eliminar esta funcion si no es necesaria, usada para log de pruebas
	/*public String listaDocumentosToString(){
		String resultado = "";
		if(this.listaDeDocumentos!=null && this.listaDeDocumentos.length != 0){
			ArrayList<Document> lista = new ArrayList<Document>(Arrays.asList(listaDeDocumentos));
			Iterator<Document> it = lista.iterator();
			Document doc = it.next(); 
			resultado = "Documentos Asociados: " + doc.toString();
			int i = 1;
			while(it.hasNext()){
				doc = it.next();
				if (i == 3){
					resultado = resultado + "/n     ";
					i = 0;
				}
				i++;
				resultado = resultado + ", " + doc.toString();
			}
		}
		return resultado;
	}*/
	
	/*public String DocumentListToParsedString(){
		String resultado = "";
		if(this.listaDeDocumentos!=null && this.listaDeDocumentos.length != 0){
			ArrayList<Document> lista = new ArrayList<Document>(Arrays.asList(listaDeDocumentos));
			Iterator<Document> it = lista.iterator();
			Document doc = it.next(); 
			resultado = doc.toString();
			while(it.hasNext()){
				doc = it.next();
				resultado = resultado + "; " + doc.toString();
			}
		}
		return resultado;
	}*/
}
