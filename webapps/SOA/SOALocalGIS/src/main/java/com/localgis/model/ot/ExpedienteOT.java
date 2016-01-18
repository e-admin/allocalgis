/**
 * ExpedienteOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;


public class ExpedienteOT 
{
	private int anioExpediente = 0;
	private String refExpediente = null;
	
	private String codigoEntidadColaboradora = null;

	public int getAnioExpediente() {
		return anioExpediente;
	}

	public void setAnioExpediente(int anioExpediente) {
		this.anioExpediente = anioExpediente;
	}

	public String getRefExpediente() {
		return refExpediente;
	}

	public void setRefExpediente(String refExpediente) {
		this.refExpediente = refExpediente;
	}

	public String getCodigoEntidadColaboradora() {
		return codigoEntidadColaboradora;
	}

	public void setCodigoEntidadColaboradora(String codigoEntidadColaboradora) {
		this.codigoEntidadColaboradora = codigoEntidadColaboradora;
	}
	
}
