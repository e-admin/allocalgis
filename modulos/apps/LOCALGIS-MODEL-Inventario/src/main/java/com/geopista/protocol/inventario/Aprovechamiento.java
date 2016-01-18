/**
 * Aprovechamiento.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 23-ago-2006
 * Time: 10:09:12
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class Aprovechamiento  implements Serializable{
    private long id=-1;
    private long idBien=-1;
    private String aprovechamiento;
    private double superficie=-1;
    private Date fecha;
	private long revision;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdBien() {
        return idBien;
    }

    public void setIdBien(long idBien) {
        this.idBien = idBien;
    }

    public String getAprovechamiento() {
        return aprovechamiento;
    }

    public void setAprovechamiento(String aprovechamiento) {
        this.aprovechamiento = aprovechamiento;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

	public void setRevision(long revision) {
		this.revision = revision;
		
	}
	public long getRevision(){
		return this.revision;
	}

}
