/**
 * CTipoLicencia.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias.tipos;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:05 $
 *          $Name:  $
 *          $RCSfile: CTipoLicencia.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CTipoLicencia implements java.io.Serializable{

	private int idTipolicencia;
	public Double getImpuestoConstruccion() {
		return impuestoConstruccion;
	}

	public void setImpuestoConstruccion(Double impuestoConstruccion) {
		this.impuestoConstruccion = impuestoConstruccion;
	}

	public Double getTasaLicencia() {
		return tasaLicencia;
	}

	public void setTasaLicencia(Double tasaLicencia) {
		this.tasaLicencia = tasaLicencia;
	}

	public String getCondicionesEspeciales() {
		return condicionesEspeciales;
	}

	public void setCondicionesEspeciales(String condicionesEspeciales) {
		this.condicionesEspeciales = condicionesEspeciales;
	}

	private String descripcion;
	private String observacion;
	private Double impuestoConstruccion;
	private Double tasaLicencia;
	private String condicionesEspeciales;

	public CTipoLicencia() {
	}

	public CTipoLicencia(int idTipolicencia, String descripcion, String observacion) {
		this.idTipolicencia = idTipolicencia;
		this.descripcion = descripcion;
		this.observacion = observacion;
	}
	
	public CTipoLicencia(int idTipolicencia, String descripcion, String observacion, Double impuesto, Double tasa, String condicionesEspeciales) {
		this.idTipolicencia = idTipolicencia;
		this.descripcion = descripcion;
		this.observacion = observacion;
		this.impuestoConstruccion = impuesto;
		this.tasaLicencia = tasa;
		this.condicionesEspeciales = condicionesEspeciales;
	}

	public int getIdTipolicencia() {
		return idTipolicencia;
	}

	public void setIdTipolicencia(int idTipolicencia) {
		this.idTipolicencia = idTipolicencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
