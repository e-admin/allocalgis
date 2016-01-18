/**
 * ParcelaOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;

import java.util.ArrayList;

public class ParcelaOT {

	private String refCatastral = null;
	private Double superficie = null;
	private Double superficieConstruida = null;
		
	private LocalizacionOT direccion = new LocalizacionOT();
	private ArrayList lstBienesInmuebles = new ArrayList();

	public String getRefCatastral() {
		return refCatastral;
	}

	public void setRefCatastral(String refCatastral) {
		this.refCatastral = refCatastral;
	}

	public Double getSuperficie() {
		return superficie;
	}

	public void setSuperficie(Double superficie) {
		this.superficie = superficie;
	}

	public double getSuperficieConstruida() {
		return superficieConstruida;
	}

	public void setSuperficieConstruida(double superficieConstruida) {
		this.superficieConstruida = superficieConstruida;
	}

	public LocalizacionOT getDireccion() {
		return direccion;
	}

	public void setDireccion(LocalizacionOT direccion) {
		this.direccion = direccion;
	}

	public ArrayList getLstBienesInmuebles() {
		return lstBienesInmuebles;
	}

	public void setLstBienesInmuebles(ArrayList lstBienesInmuebles) {
		this.lstBienesInmuebles = lstBienesInmuebles;
	}

	public void setSuperficieConstruida(Double superficieConstruida) {
		this.superficieConstruida = superficieConstruida;
	}

	
}
