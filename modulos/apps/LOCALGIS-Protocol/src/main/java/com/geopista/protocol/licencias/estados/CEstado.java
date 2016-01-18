/**
 * CEstado.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias.estados;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:05 $
 *          $Name:  $
 *          $RCSfile: CEstado.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CEstado implements java.io.Serializable{

	private int idEstado;
	private String descripcion;
	private String observacion;
	private int step;
    private long idPermiso= -1;
    private String descPermiso;

	public CEstado() {
	}

    public CEstado(int idEstado)
    {
        this.idEstado=idEstado;
    }
	public CEstado(int idEstado, String descripcion, String observacion, int step) {
		this.idEstado = idEstado;
		this.descripcion = descripcion;
		this.observacion = observacion;
		this.step = step;
	}

	public int getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
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

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

    public long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getDescPermiso() {
        return descPermiso;
    }

    public void setDescPermiso(String descripcion) {
        this.descPermiso = descripcion;
    }


}
