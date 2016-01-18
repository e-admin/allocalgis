/**
 * ExternalLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model;



import java.awt.Color;

import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;


/**Representa una capa externa o capa de la EIEL
 * @author sgrodriguez
 *
 */
public class ExternalLayer extends Layer implements Cloneable,IExternalLayer{
	 private String externalId; 
	 private String tabla;
	 private String id_column;
	 private String geometry_column;
	 private String URL;
	 
	 
	 /**Crea una capa externa
	  * @param name Nombre de la capa
	  * @param fillColor 
	  * @param collection
	  * @param layerManager Gestor de capas 
	  * @param externalId Id de la capa en la BD de la EIEL
	  * @param tabla Tabla de la BD de la EIEL en la que se almacena la información de la capa
	  * @param id_column Columna id de la tabla de la BD de la EIEL en la que se almacena la información de la capa
	  * @param geometry_column Columna geométrica de la tabla de la BD de la EIEL en la que se almacena la información de la capa
	  * @param URL Url que nos permite el acceso a la capa (URL del Web service). 
	  */
	 public ExternalLayer(String name,Color fillColor,FeatureCollection collection,LayerManager layerManager, String externalId,
			 String tabla, String id_column, String geometry_column, String URL){
		 super(name,
	           fillColor,
	           collection,
	           layerManager);
		 
		 this.externalId=externalId;
		 this.tabla=tabla;
		 this.id_column=id_column;
		 this.geometry_column=geometry_column;
		 this.URL=URL;
	 }//fin del constructor
	 
	 
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	public String getId_column() {
		return id_column;
	}
	public void setId_column(String id_column) {
		this.id_column = id_column;
	}
	public String getGeometry_column() {
		return geometry_column;
	}
	public void setGeometry_column(String geometry_column) {
		this.geometry_column = geometry_column;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String url) {
		URL = url;
	}
	

	 
}
