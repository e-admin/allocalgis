/**
 * AlfrescoKey.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.manager.beans;

import java.io.Serializable;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Bean con la clave unívoca (path o uuid) de un elemento de Alfresco
 */
public class AlfrescoKey implements Serializable{

	/**
	 * Constantes
	 */
	public static final int KEY_UUID = 1;
	public static final int KEY_PATH = 2;
	
	/**
	 * Variables
	 */
	private String key;		
	private int keyType;

	/**
	 * Constructor
	 * @param key: Clave unívoca
	 * @param keyType: Tipo de clave unívoca 
	 */
	public AlfrescoKey(String key, int keyType) {
		this.key = key;
		this.keyType = keyType;
	}

	/**
	 * Getter key
	 * @return String: Clave unívoca
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Setter key
	 * @param key: Clave unívoca
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Getter keyType
	 * @return int: Tipo de clave unívoca (KEY_UUID o KEY_PATH)
	 */
	public int getKeyType() {
		return keyType;
	}

	/**
	 * Setter keyType
	 * @param int: Tipo de clave unívoca (KEY_UUID o KEY_PATH)
	 */
	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}

	/**
	 * @override: toString()
	 * @return String: Clave unívoca
	 */
	@Override
	public String toString() {
		return key;
	}
	
}
