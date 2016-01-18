/**
 * GeopistaDatosImportarPadronConDomicilio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inforeferencia;

import java.util.ArrayList;

import com.geopista.app.catastro.GeopistaDatosImportarPadron;

public class GeopistaDatosImportarPadronConDomicilio {
	private ArrayList<Integer> idDomicilio;
	private GeopistaDatosImportarPadron data;
	public ArrayList<Integer> getIdDomicilio() {
		return idDomicilio;
	}
	public void setIdDomicilio(ArrayList<Integer> idDomicilio) {
		this.idDomicilio = idDomicilio; 
	} 
	public GeopistaDatosImportarPadron getData() {
		return data;
	}
	public void setData(GeopistaDatosImportarPadron data) {
		this.data = data;
	}
	public void addIdDomicilio(Integer idDomicilio){
		if(this.idDomicilio == null)
			this.idDomicilio = new ArrayList<Integer>();
		if(!this.idDomicilio.contains(idDomicilio))
			this.idDomicilio.add(idDomicilio);
	}
	
}
