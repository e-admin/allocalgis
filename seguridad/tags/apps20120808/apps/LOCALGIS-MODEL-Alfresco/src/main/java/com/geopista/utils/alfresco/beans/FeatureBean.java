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
