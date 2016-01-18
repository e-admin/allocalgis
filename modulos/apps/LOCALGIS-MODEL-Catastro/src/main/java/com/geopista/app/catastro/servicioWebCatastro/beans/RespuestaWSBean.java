/**
 * RespuestaWSBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.servicioWebCatastro.beans;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.Expediente;

public class RespuestaWSBean {
	
	private Expediente expediente ;

	private ArrayList<UnidadErrorElementoWSBean> lstUnidadError;
	
	private String estado;
	
	private UDSAWSBean udsa;
	
	
	public UDSAWSBean getUdsa() {
		return udsa;
	}

	public void setUdsa(UDSAWSBean udsa) {
		this.udsa = udsa;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	public ArrayList<UnidadErrorElementoWSBean> getLstUnidadError() {
		return lstUnidadError;
	}

	public void setLstUnidadError(ArrayList<UnidadErrorElementoWSBean> lstUnidadError) {
		this.lstUnidadError = lstUnidadError;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

}
