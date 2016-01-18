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
