/**
 * Resolucion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 10-jun-2005
 * Time: 10:07:25
 */
public class Resolucion {
    private boolean favorable;
    private java.util.Date fecha;
    private String coletilla;
    private String organo;
    private String asunto;

    public Resolucion() {
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getColetilla() {
        return coletilla;
    }

    public void setColetilla(String coletilla) {
        this.coletilla = coletilla;
    }

    public boolean isFavorable() {
        return favorable;
    }

    public void setFavorable(boolean favorable) {
        this.favorable = favorable;
    }

    public java.util.Date getFecha() {
        return fecha;
    }

    public void setFecha(java.util.Date fecha) {
        this.fecha = fecha;
    }

    public String getOrgano() {
        return organo;
    }

    public void setOrgano(String organo) {
        this.organo = organo;
    }
}
