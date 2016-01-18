/**
 * CTipoAnexo.java
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
 *          $RCSfile: CTipoAnexo.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CTipoAnexo implements java.io.Serializable{

	private int idTipoAnexo;
	private String descripcion;
	private String observacion;

	public CTipoAnexo() {
	}

	public CTipoAnexo(int idTipoAnexo, String descripcion, String observacion) {
		this.idTipoAnexo = idTipoAnexo;
		this.descripcion = descripcion;
		this.observacion = observacion;
	}

	public int getIdTipoAnexo() {
		return idTipoAnexo;
	}

	public void setIdTipoAnexo(int idTipoAnexo) {
		this.idTipoAnexo = idTipoAnexo;
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
