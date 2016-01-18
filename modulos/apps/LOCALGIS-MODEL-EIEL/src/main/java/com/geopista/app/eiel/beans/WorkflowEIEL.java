/**
 * WorkflowEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.geopista.app.eiel.ConstantesLocalGISEIEL;

public class WorkflowEIEL extends InventarioEIEL implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3607365627234226280L;
	
	private int estadoValidacion;
	private int estadoValidacionAnterior;
	
	/*private boolean estadoTemporal;
	private boolean estadoPublicable;
	private boolean estadoValido=true;*/
	
	private long revisionActual;
	private long revisionExpirada;
	
	private String bloqueado = null;
	protected String codINEProvincia = null;
	protected String codINEMunicipio = null;
	
	protected long idelemento;

	protected LinkedHashMap elementos;

	
	public void setEstadoValidacion(int estadoValidacion){
		this.estadoValidacion=estadoValidacion;
	}
	
	public void setEstadoValidacionAnterior(int estadoValidacionAnterior){
		this.estadoValidacionAnterior=estadoValidacionAnterior;
	}
	
	public boolean isEstadoTemporal() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_TEMPORAL);
	}
	
	public boolean isEstadoValido() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_VALIDO);
	}

	public boolean isEstadoAutoPublicable() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_PUBLICABLE_MOVILIDAD);
	}
	
	public boolean isEstadoPublicable() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_PUBLICABLE);
	}

	public boolean isEstadoBorrable() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_BORRABLE);
	}
	
	
	public boolean isEstadoABorrar() {
		return (estadoValidacion==ConstantesLocalGISEIEL.ESTADO_A_BORRAR);
	}
	
	public long getRevisionActual() {
		return revisionActual;
	}

	public void setRevisionActual(long revisionActual) {
		this.revisionActual = revisionActual;
	}

	public long getRevisionExpirada() {
		return revisionExpirada;
	}

	public void setRevisionExpirada(long revisionExpirada) {
		this.revisionExpirada = revisionExpirada;
	}
	
	public String getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	public String getCodINEProvincia() {
		return codINEProvincia;
	}
	public void setCodINEProvincia(String codINEProvincia) {
		this.codINEProvincia = codINEProvincia;
	}
	
	public String getCodINEMunicipio() {
		return codINEMunicipio;
	}

	
	public void setCodINEMunicipio(String codINEMunicipio) {
		this.codINEMunicipio = codINEMunicipio;
	}
		
	
	public String getEstadoValidacion(){
		if (isEstadoTemporal())
			return ConstantesLocalGISEIEL.TEMPORAL;
		else if (isEstadoPublicable())
			return ConstantesLocalGISEIEL.PUBLICABLE;
		else if (isEstadoAutoPublicable())
			return ConstantesLocalGISEIEL.AUTO_PUBLICABLE;
		else if (isEstadoBorrable())
			return ConstantesLocalGISEIEL.BORRABLE;
		else
			return ConstantesLocalGISEIEL.VALIDO;			
	}
	
	public int getEstadoValidacionAnterior(){
		return estadoValidacionAnterior;
	}

	public void setDatosAdicionales(LinkedHashMap elementos) {
		this.elementos=elementos;
		
	}
	
	public LinkedHashMap getDatosAdicionales(){
		return this.elementos;
	}

	public long getIdElemento() {
		return idelemento;
	}

	public void setIdElemento(long idelemento) {
		this.idelemento = idelemento;
	}
	
	

}
