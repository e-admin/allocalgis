/**
 * AlfrescoNode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.beans;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Bean que contiene el nombre del nodo y la clave unívoca de Alfresco
 */
public class AlfrescoNode extends DefaultMutableTreeNode {

	/**
	 * Variables
	 */
	private String name;
	private String uuid;
	
	/**
	 * Constructor
	 */
	public AlfrescoNode() {		
	}
	
	/**
	 * Constructor
	 * @param name: Nombre del nodo
	 * @param uuid: Clave unívoca
	 */
	public AlfrescoNode(String name, String uuid) {
		this.name = name;
		this.uuid = uuid;
	}
	
	/**
	 * Getter name
	 * @return String: Nombre del nodo
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter name
	 * @param name: Nombre del nodo
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter uuid
	 * @return String: Clave unívoca
	 */
	public String getUuid() {
		return uuid;
	}
	
	/**
	 * Setter uuid
	 * @param uuid: Clave unívoca
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * @override: toString()
	 * @return String: Nombre del nodo
	 */
	@Override
	public String toString() {
		return getName();
	}	
}
