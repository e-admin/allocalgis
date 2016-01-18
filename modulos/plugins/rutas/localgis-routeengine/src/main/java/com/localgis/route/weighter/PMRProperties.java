/**
 * PMRProperties.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.weighter;

/**
 * Recoge las propiedades de una acera
 * @author miriamperez
 *
 */
public class PMRProperties {
	
	private String disabilityType;
	private double minWidth = 1.5;	//Anchura
	private double maxTransversalSlope = 0.02;	//Pendiente transversal
	private double maxLongitudinalSlope = 0.05;	//Pendiente longitudinal
	private boolean considerateIrregularPaving = false;	//Pavimento irregular
	private boolean considerateNonDifferentiatedPaving = false;	//Pavimento no diferenciado
	private double obstacleHeight = 0;	//Obstaculo en altura

	public PMRProperties(){
		
	}
  
	
	public double getObstacleHeight() {
		return obstacleHeight;
	}

	public void setObstacleHeight(double obstacleHeight) {
		this.obstacleHeight = obstacleHeight;
	}
	public double getMinWidth() {
		return minWidth;
	}
	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
	}
	public double getMaxTransversalSlope() {
		return maxTransversalSlope;
	}
	public void setMaxTransversalSlope(double maxTransversalSlope) {
		this.maxTransversalSlope = maxTransversalSlope;
	}
	public double getMaxLongitudinalSlope() {
		return maxLongitudinalSlope;
	}
	public void setMaxLongitudinalSlope(double maxLongitudinalSlope) {
		this.maxLongitudinalSlope = maxLongitudinalSlope;
	}
	public boolean isConsiderateIrregularPaving() {
		return considerateIrregularPaving;
	}
	public void setConsiderateIrregularPaving(boolean considerateIrregularPaving) {
		this.considerateIrregularPaving = considerateIrregularPaving;
	}
	public boolean isConsiderateNonDifferentiatedPaving() {
		return considerateNonDifferentiatedPaving;
	}
	public void setConsiderateNonDifferentiatedPaving(
			boolean considerateNonDifferentiatedPaving) {
		this.considerateNonDifferentiatedPaving = considerateNonDifferentiatedPaving;
	}	
	public String getDisabilityType() {
		return disabilityType;
	}
	public void setDisabilityType(String disabilityType) {
		this.disabilityType = disabilityType;
	}
}
