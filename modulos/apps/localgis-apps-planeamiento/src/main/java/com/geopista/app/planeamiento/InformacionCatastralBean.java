/**
 * InformacionCatastralBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
