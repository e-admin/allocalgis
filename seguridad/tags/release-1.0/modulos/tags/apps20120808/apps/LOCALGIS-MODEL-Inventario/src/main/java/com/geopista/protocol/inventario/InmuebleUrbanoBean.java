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
