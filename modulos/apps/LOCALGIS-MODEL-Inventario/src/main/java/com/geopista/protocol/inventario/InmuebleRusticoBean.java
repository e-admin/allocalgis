/**
 * InmuebleRusticoBean.java
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
 * Date: 17-jul-2006
 * Time: 10:50:44
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que implementa un objeto de tipo inmueble rustico 
 */
public class InmuebleRusticoBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String poligono;
    private String paraje;
    private String aprovechamiento;
    private String parcela;
    private String subparcela;
    private double sueloLong;
    private double sueloAncho;
    private double sueloMetrosPav;
    private double sueloMetrosNoPav;


    public String getPoligono() {
        return poligono;
    }

    public void setPoligono(String poligono) {
        this.poligono = poligono;
    }

    public String getParaje() {
        return paraje;
    }

    public void setParaje(String paraje) {
        this.paraje = paraje;
    }

    public String getAprovechamiento() {
        return aprovechamiento;
    }

    public void setAprovechamiento(String aprovechamiento) {
        this.aprovechamiento = aprovechamiento;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public String getSubparcela() {
        return subparcela;
    }

    public void setSubparcela(String subparcela) {
        this.subparcela = subparcela;
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
