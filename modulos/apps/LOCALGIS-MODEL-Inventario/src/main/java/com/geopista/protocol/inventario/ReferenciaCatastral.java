/**
 * ReferenciaCatastral.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 10-ago-2006
 * Time: 10:23:32
 * To change this template use File | Settings | File Templates.
 */
public class ReferenciaCatastral implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id=-1;
    private String refCatastral;
    private String descripcion;
    private long idBien=-1;
    private long revision=-1;

    public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRefCatastral() {
        return refCatastral;
    }

    public void setRefCatastral(String refCatastral) {
        this.refCatastral = refCatastral;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getIdBien() {
        return idBien;
    }

    public void setIdBien(long idBien) {
        this.idBien = idBien;
    }
}
