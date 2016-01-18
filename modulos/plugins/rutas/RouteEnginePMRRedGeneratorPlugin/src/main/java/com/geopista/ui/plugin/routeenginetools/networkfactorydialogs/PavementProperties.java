/**
 * PavementProperties.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networkfactorydialogs;


/**
 * Recoge las propiedades de una acera
 * @author miriamperez
 *
 */
public class PavementProperties {
	
	private double width = 1.5;	//Anchura
	private double transversalSlope = 0.02;	//Pendiente transversal
	private double longitudinalSlope = 0.05;	//Pendiente longitudinal
	private boolean irregularPaving = false;	//Pavimento irregular
	private boolean nonDifferentiatedPaving = false;	//Pavimento no diferenciado
	
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getTransversalSlope() {
		return transversalSlope;
	}
	public void setTransversalSlope(double transversalSlope) {
		this.transversalSlope = transversalSlope;
	}
	public double getLongitudinalSlope() {
		return longitudinalSlope;
	}
	public void setLongitudinalSlope(double longitudinalSlope) {
		this.longitudinalSlope = longitudinalSlope;
	}
	public boolean isIrregularPaving() {
		return irregularPaving;
	}
	public void setIrregularPaving(boolean irregularPaving) {
		this.irregularPaving = irregularPaving;
	}
	public boolean isNonDifferentiatedPaving() {
		return nonDifferentiatedPaving;
	}
	public void setNonDifferentiatedPaving(boolean nonDifferentiatedPaving) {
		this.nonDifferentiatedPaving = nonDifferentiatedPaving;
	}
}
