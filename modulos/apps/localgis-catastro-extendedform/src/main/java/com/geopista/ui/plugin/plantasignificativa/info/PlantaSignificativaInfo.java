/**
 * PlantaSignificativaInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.plantasignificativa.info;

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
