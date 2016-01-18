/**
 * IdentificadorDerecho.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.servicioWebCatastro;

import com.geopista.app.catastro.model.beans.Derecho;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Persona;

public class IdentificadorDerecho {
	
	private Expediente expediente ;

	
	private Persona representante;
	
	private Derecho derecho;
	
	private String codigoDelegacion;
	
	private String codigoMunicipio;

	public String getCodigoDelegacion() {
		return codigoDelegacion;
	}

	public void setCodigoDelegacion(String codigoDelegacion) {
		this.codigoDelegacion = codigoDelegacion;
	}

	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Persona getRepresentante() {
		return representante;
	}

	public void setRepresentante(Persona representante) {
		this.representante = representante;
	}

	public Derecho getDerecho() {
		return derecho;
	}

	public void setDerecho(Derecho derecho) {
		this.derecho = derecho;
	}


}
