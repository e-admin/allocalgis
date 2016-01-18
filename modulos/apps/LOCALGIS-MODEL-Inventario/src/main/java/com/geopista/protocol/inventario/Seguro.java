/**
 * Seguro.java
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
 * Date: 02-ago-2006
 * Time: 13:06:58
 * To change this template use File | Settings | File Templates.
 */
public class Seguro  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private CompanniaSeguros compannia;
    private String descripcion;
    private Long poliza;
    private Double prima;
    private Date fechaInicio;
    private Date fechaVencimiento;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = new Long(id);
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public CompanniaSeguros getCompannia() {
        return compannia;
    }

    public void setCompannia(CompanniaSeguros compannia) {
        this.compannia = compannia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getPoliza() {
        return poliza;
    }

    public void setPoliza(long poliza) {
        this.poliza = new Long(poliza);
    }
    
    public void setPoliza(Long poliza) {
        this.poliza = poliza;
    }

    public Double getPrima() {
        return prima;
    }

    public void setPrima(double prima) {
        this.prima = new Double(prima);
    }
    public void setPrima(Double prima) {
        this.prima = prima;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
