/**
 * 
 */
package com.geopista.ui.plugin.plantasignificativa.info;

import com.geopista.ui.plugin.plantasignificativa.PlantaSignificativaPlugIn;

/**
 * Esta clase se usa para recoger la información que se pide en el dialogo {@link PlantaSignificativaFormDialog} y que sirve para 
 * intercambiar la informacion de la planta significativa entre el diálogo y la clase principal del plugin {@link PlantaSignificativaPlugIn} 
 *
 * @author javieraragon
 */
public class PlantaSignificativaInfo {
	
	
	/**
	 * numero de plantas que representa la planta significativa 
	 */
	private int numPlantas = -1;
	/**
	 * nombre de la planta significativa
	 */
	private String nombrePlantas = "";
	
	/**
	 * Constructor por defecto
	 */
	public PlantaSignificativaInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con los campos introducidos como parametros.
	 * @param numPlantas el número de plantas que representa la planta significativa.
	 * @param nombrePlantas el nombre de la planta significativa.
	 */
	public PlantaSignificativaInfo(int numPlantas, String nombrePlantas) {
		super();
		this.numPlantas = numPlantas;
		this.nombrePlantas = nombrePlantas;
	}

	/**
	 * @return the numPlantas
	 */
	public int getNumPlantas() {
		return numPlantas;
	}

	/**
	 * @param numPlantas the numPlantas to set
	 */
	public void setNumPlantas(int numPlantas) {
		this.numPlantas = numPlantas;
	}

	/**
	 * @return the nombrePlantas
	 */
	public String getNombrePlantas() {
		return nombrePlantas;
	}

	/**
	 * @param nombrePlantas the nombrePlantas to set
	 */
	public void setNombrePlantas(String nombrePlantas) {
		this.nombrePlantas = nombrePlantas;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * Convierte a string la información de la planta significativa.
	 */
	public String toString(){
		String result = nombrePlantas + ", " + numPlantas ; 
		
		return result;
		
	}

}
