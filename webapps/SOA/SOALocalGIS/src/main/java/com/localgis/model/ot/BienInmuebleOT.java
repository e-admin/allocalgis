/**
 * BienInmuebleOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;

import java.util.ArrayList;

public class BienInmuebleOT {

	private String referencia_catastral = null;
	private LocalizacionOT direccionLocalizacion = new LocalizacionOT();
	private String claseBienInmueble = null;
	private String claseUso = null;
	private Double superficie = null;
	private ArrayList lstConstrucciones = new ArrayList();
	private ArrayList lstCultivos = new ArrayList();
	
	public String getReferencia_catastral() {
		return referencia_catastral;
	}
	public void setReferencia_catastral(String referencia_catastral) {
		this.referencia_catastral = referencia_catastral;
	}
	public LocalizacionOT getDireccionLocalizacion() {
		return direccionLocalizacion;
	}
	public void setDireccionLocalizacion(LocalizacionOT direccionLocalizacion) {
		this.direccionLocalizacion = direccionLocalizacion;
	}
	public String getClaseBienInmueble() {
		return claseBienInmueble;
	}
	public void setClaseBienInmueble(String claseBienInmueble) {
		this.claseBienInmueble = claseBienInmueble;
	}
	public String getClaseUso() {
		return claseUso;
	}
	public void setClaseUso(String claseUso) {
		this.claseUso = claseUso;
	}
	public ArrayList getLstConstrucciones() {
		return lstConstrucciones;
	}
	public void setLstConstrucciones(ArrayList lstConstrucciones) {
		this.lstConstrucciones = lstConstrucciones;
	}
	public ArrayList getLstCultivos() {
		return lstCultivos;
	}
	public void setLstCultivos(ArrayList lstCultivos) {
		this.lstCultivos = lstCultivos;
	}
	public Double getSuperficie() {
		return superficie;
	}
	public void setSuperficie(Double superficie) {
		this.superficie = superficie;
	}
	
	
}
