/**
 * CuentaAmortizacion.java
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
 * Date: 31-jul-2006
 * Time: 11:37:43
 * To change this template use File | Settings | File Templates.
 */
public class CuentaAmortizacion  implements Serializable{
    long id=-1;
    String descripcion;
    String cuenta;
    double porcentaje=-1;
    int annos=-1;
    private String tipoAmortizacion;
    private double totalAmortizado= -1;

    public String getTipoAmortizacion() {
        return tipoAmortizacion;
    }

    public void setTipoAmortizacion(String tipoAmortizacion) {
        this.tipoAmortizacion = tipoAmortizacion;
    }

    public double getTotalAmortizado() {
        return totalAmortizado;
    }

    public void setTotalAmortizado(double totalAmortizado) {
        this.totalAmortizado = totalAmortizado;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getAnnos() {
        return annos;
    }

    public void setAnnos(int annos) {
        this.annos = annos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String toString()
       {
           return (cuenta==null?"":cuenta+" - ")+(descripcion==null?"          ":descripcion);
       }

}
