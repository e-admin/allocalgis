/**
 * Residuo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.contaminantes;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 19-oct-2004
 * Time: 9:40:48
 */
public class Residuo {
    String id;
    String tipo;
    Float ratio;
	String situacion;
	Long diaria;
	Long anual;

    public Residuo() {
    }

    public Long getAnual() {
        return anual;
    }

    public void setAnual(Long anual) {
        this.anual = anual;
    }

    public Long getDiaria() {
        return diaria;
    }

    public void setDiaria(Long diaria) {
        this.diaria = diaria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String toString()
    {
        return "";
    }
}
