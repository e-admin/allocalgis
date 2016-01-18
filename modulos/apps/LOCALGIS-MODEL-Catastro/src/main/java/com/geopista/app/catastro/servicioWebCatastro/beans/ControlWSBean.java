/**
 * ControlWSBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.servicioWebCatastro.beans;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.EntidadGeneradora;

public class ControlWSBean {

	
	private EntidadGeneradora entidadGeneradora;
	
	private InformacionWSFichero informacionFichero;
	
	private String codigoEnvio;
	
	private ArrayList lstIdentificadoresDialogo;

	public ArrayList getLstIdentificadoresDialogo() {
		return lstIdentificadoresDialogo;
	}

	public void setLstIdentificadoresDialogo(ArrayList liddf) {
		this.lstIdentificadoresDialogo = liddf;
	}

	public InformacionWSFichero getInformacionFichero() {
		return informacionFichero;
	}

	public void setInformacionFichero(InformacionWSFichero informacionFichero) {
		this.informacionFichero = informacionFichero;
	}

	public String getCodigoEnvio() {
		return codigoEnvio;
	}

	public void setCodigoEnvio(String codigoEnvio) {
		this.codigoEnvio = codigoEnvio;
	}

	public EntidadGeneradora getEntidadGeneradora() {
		return entidadGeneradora;
	}

	public void setEntidadGeneradora(EntidadGeneradora entidadGeneradora) {
		this.entidadGeneradora = entidadGeneradora;
	}
}
