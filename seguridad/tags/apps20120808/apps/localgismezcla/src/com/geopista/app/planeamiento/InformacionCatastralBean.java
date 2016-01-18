/*
 * Created on 15-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.planeamiento;

/**
 * @author dbaeza
 *
 * Clase para recoger la información asocida al informe urbanístico
 */
public class InformacionCatastralBean {

String planoCatastral;
int    numeroInmuebles;
double valorCatastral;
double valorSuelo;
double valorConstruccion;
double supElementosConstruccion;
double supElementosSuelo;

/**
 * @return Returns the numeroInmuebles.
 */
public int getNumeroInmuebles() {
	return numeroInmuebles;
}
/**
 * @param numeroInmuebles The numeroInmuebles to set.
 */
public void setNumeroInmuebles(int numeroInmuebles) {
	this.numeroInmuebles = numeroInmuebles;
}
/**
 * @return Returns the planoCatastral.
 */
public String getPlanoCatastral() {
	return planoCatastral;
}
/**
 * @param planoCatastral The planoCatastral to set.
 */
public void setPlanoCatastral(String planoCatastral) {
	this.planoCatastral = planoCatastral;
}
/**
 * @return Returns the supElementosConstruccion.
 */
public double getSupElementosConstruccion() {
	return supElementosConstruccion;
}
/**
 * @param supElementosConstruccion The supElementosConstruccion to set.
 */
public void setSupElementosConstruccion(double supElementosConstruccion) {
	this.supElementosConstruccion = supElementosConstruccion;
}
/**
 * @return Returns the supElementosSuelo.
 */
public double getSupElementosSuelo() {
	return supElementosSuelo;
}
/**
 * @param supElementosSuelo The supElementosSuelo to set.
 */
public void setSupElementosSuelo(double supElementosSuelo) {
	this.supElementosSuelo = supElementosSuelo;
}
/**
 * @return Returns the valorCatastral.
 */
public double getValorCatastral() {
	return valorCatastral;
}
/**
 * @param valorCatastral The valorCatastral to set.
 */
public void setValorCatastral(double valorCatastral) {
	this.valorCatastral = valorCatastral;
}
/**
 * @return Returns the valorConstruccion.
 */
public double getValorConstruccion() {
	return valorConstruccion;
}
/**
 * @param valorConstruccion The valorConstruccion to set.
 */
public void setValorConstruccion(double valorConstruccion) {
	this.valorConstruccion = valorConstruccion;
}
/**
 * @return Returns the valorSuelo.
 */
public double getValorSuelo() {
	return valorSuelo;
}
/**
 * @param valorSuelo The valorSuelo to set.
 */
public void setValorSuelo(double valorSuelo) {
	this.valorSuelo = valorSuelo;
}
}
