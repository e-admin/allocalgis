/**
 * InmuebleUrbanoBean.java
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
 * Date: 13-jul-2006
 * Time: 13:19:33
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que implementa un objeto de tipo inmueble urbano
 */
public class InmuebleUrbanoBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
    private String manzana;
    private String parcela;
    private double sueloLong;
    private double sueloAncho;
    private double sueloMetrosPav;
    private double sueloMetrosNoPav;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

	public double getSueloLong() {
		return sueloLong;
	}

	public void setSueloLong(double sueloLong) {
		this.sueloLong = sueloLong;
	}

	public double getSueloAncho() {
		return sueloAncho;
	}

	public void setSueloAncho(double sueloAncho) {
		this.sueloAncho = sueloAncho;
	}

	public double getSueloMetrosPav() {
		return sueloMetrosPav;
	}

	public void setSueloMetrosPav(double sueloMetrosPav) {
		this.sueloMetrosPav = sueloMetrosPav;
	}

	public double getSueloMetrosNoPav() {
		return sueloMetrosNoPav;
	}

	public void setSueloMetrosNoPav(double sueloMetrosNoPav) {
		this.sueloMetrosNoPav = sueloMetrosNoPav;
	}

}
