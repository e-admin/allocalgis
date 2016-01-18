/**
 * LocalGIS_WFSG_Resultados.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg;

import java.util.ArrayList;
import java.util.List;

import com.localgis.web.wfsg.domain.ElementEntity;
import com.localgis.web.wfsg.domain.Entity;
/*****************************************************************************************
 *  File		: LocalGIS_WFSG_Resultados.java
 *  Package		: com.localgis.web.wfsg
 *  Desc		: object that contains all the results we need from the server 
 *  Author		: SATEC Framework Team			  
 *  Date		: 01-12-2007
 * 	Initial Version
 ******************************************************************************************/

public class LocalGIS_WFSG_Resultados {
	/** URL of the server we are working with */
	private String URLServer;
	/** SRS of the server */
	private String SRS;
	/** The Entity Reference (the one we are looking for) */
	private Entity entityReference;
	/** The element of the reference we are looking for */
	private ElementEntity elementEntityReference;
	/** The different operations the Server is allowing */
	private List operations = new ArrayList();
	/** The different entityTypes defined on the server */
	private List entityTypes = new ArrayList();
	/** Server Style, that give us an information about what kind of WFSG server we are working with */
	private int style;
	
	public LocalGIS_WFSG_Resultados(){
		this.SRS=null;
	}
	
	/**
	 * Getter for property URLServer
	 * 
	 * @return String value of URLServer
	 */
	public String getURLServer() {
		return this.URLServer;
	}
	
	/**
	 * Setter for property URLServer
	 * 
	 * @param server new value for property URLServer
	 */
	public void setURLServer(String server) {
		this.URLServer = server;
	}
	
	/**
	 * Getter for property SRS
	 * 
	 * @return String value of SRS
	 */
	public String getSRS() {
		return this.SRS;
	}

	/**
	 * Setter for property SRS
	 * 
	 * @param srs new value for property SRS
	 */
	public void setSRS(String srs) {
		this.SRS = srs;
	}
	
	/**
	 * Getter for property entityReference
	 * 
	 * @return Entity value of entityReference
	 */
	public Entity getEntityReference() {
		return this.entityReference;
	}

	/**
	 * Setter for property entityReference
	 * 
	 * @param entityReference new value for property entityReference
	 */
	public void setEntityReference(Entity entityReference) {
		this.entityReference = entityReference;
	}
	
	/**
	 * Getter for property elementEntityReference
	 * 
	 * @return ElementEntity value of elementEntityReference
	 */
	public ElementEntity getElementEntityReference() {
		return this.elementEntityReference;
	}

	/**
	 * Setter for property elementEntityReference
	 * 
	 * @param elementEntityReference new ElementEntity for property elementEntityReference
	 */
	public void setElementEntityReference(ElementEntity elementEntityReference) {
		this.elementEntityReference = elementEntityReference;
	}
	
	/**
	 * Getter for property operations
	 * 
	 * @return list of String of the different operations found
	 */
	public List getOperations() {
		return this.operations;
	}
	
	/**
	 * Setter for property operations
	 * 
	 * @param operations new list (of strings) that contains the different operations found on the server
	 */
	public void setOperations(List operations) {
		this.operations = operations;
	}
	
	/**
	 * Allows the user to add a new operation
	 * 
	 * @param sOperationName new operation found on the server
	 */
	public void addOperation(String sOperationName){
		this.operations.add(sOperationName);
	}
	
	/**
	 * Getter for property entityTypes
	 * 
	 * @return list of String of the different entity types found
	 */
	public List getEntityTypes() {
		return this.entityTypes;
	}

	/**
	 * Setter for property entityTypes
	 * 
	 * @param entityTypes new list (of strings) that contains the different entity types found on the server
	 */
	public void setEntityTypes(List entityTypes) {
		this.entityTypes = entityTypes;
	}
	
	/**
	 * Allows the user to add a new entity types. 
	 * 
	 * @param anEntityType string that contains the new entity type to add
	 */
	public void addEntityTypes(Entity anEntityType){
		this.entityTypes.add(anEntityType);
	}

	/**
	 * Getter for property style
	 * 
	 * @return value of style as an int. 
	 */
	public int getStyle() {
		return style;
	}
	/**
	 * Setter for property style
	 * 
	 * @param style integer that permits to know what Server-style we are facing
	 */
	public void setStyle(int style) {
		this.style = style;
	}	
}
