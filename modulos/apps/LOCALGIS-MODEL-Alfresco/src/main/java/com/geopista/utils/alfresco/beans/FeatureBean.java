/**
 * FeatureBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.utils.alfresco.beans;

import java.io.Serializable;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Bean de un elemento  geométrico LocalGIS
 */
public class FeatureBean implements Serializable{

	/**
	 * Constantes
	 */
	private int idLayer;
	private String idFeature;
	
	/**
	 * Constructor
	 * @param idLayer: Identificador de capa
	 * @param idFeature: Identificador de elemento geométrico
	 */
	public FeatureBean(int idLayer, String idFeature){
		this.idLayer = idLayer;
		this.idFeature = idFeature;
	}
	
	/**
	 * Getter idLayer
	 * @return int: Identificador de capa
	 */
	public int getIdLayer() {
		return idLayer;
	}
	
	/**
	 * Setter idLayer
	 * @param idLayer: Identificador de capa
	 */
	public void setIdLayer(int idLayer) {
		this.idLayer = idLayer;
	}
	
	/**
	 * Getter idFeature
	 * @return String: Identificador de elemento geométrico
	 */
	public String getIdFeature() {
		return idFeature;
	}
	
	/**
	 * Setter idFeature
	 * @param idFeature: Identificador de elemento geométrico
	 */
	public void setIdFeature(String idFeature) {
		this.idFeature = idFeature;
	}		
	
}
