/**
 * PeticionBusquedaHistorico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.contaminantes;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 19-abr-2005
 * Time: 12:18:28
 */
public class PeticionBusquedaHistorico {
    Integer accion;
    Integer tipo;
    Date fechaDesde;
    Date fechaHasta;

    public PeticionBusquedaHistorico() {
    }

    public Integer getAccion() {
        return accion;
    }

    public void setAccion(Integer accion) {
        this.accion = accion;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

}
