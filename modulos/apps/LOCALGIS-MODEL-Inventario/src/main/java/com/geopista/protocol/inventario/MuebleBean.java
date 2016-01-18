/**
 * MuebleBean.java
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
 * Date: 28-ago-2006
 * Time: 11:45:40
 * To change this template use File | Settings | File Templates.
 */
public class MuebleBean extends BienBean implements Serializable, Cloneable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String direccion;
    private String ubicacion;
    private String caracteristicas;
    private String material;
    private String artista;
    private String destino;
    private String propiedad;
    private String estadoConservacion;
    private Double costeAdquisicion;
    private Double valorActual;
  
    private String marca;
    private String modelo;
    private String numSerie;
    private Lote lote;
    private Date fechaFinGarantia;
    private String clase;
    

	public MuebleBean() {
		super();
	}

	public MuebleBean(BienBean bien) {
		this.setId(bien.getId());
	}
	
	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public Date getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    public void setFechaFinGarantia(Date fechaFinGarantia) {
        this.fechaFinGarantia = fechaFinGarantia;
    }

   

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public String getEstadoConservacion() {
        return estadoConservacion;
    }

    public void setEstadoConservacion(String estadoConservacion) {
        this.estadoConservacion = estadoConservacion;
    }

    public Double getCosteAdquisicion() {
        return costeAdquisicion;
    }

    public void setCosteAdquisicion(double costeAdquisicion) {
        this.costeAdquisicion = new Double(costeAdquisicion);
    }

    public Double getValorActual() {
        return valorActual;
    }

    public void setValorActual(double valorActual) {
        this.valorActual = new Double(valorActual);
    }

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}

    public MuebleBean clone(){
    	MuebleBean newMueble = new MuebleBean();
    	super.clone(newMueble);
    	newMueble.setDireccion(this.direccion);
    	newMueble.setUbicacion(this.ubicacion);
    	newMueble.setCaracteristicas(this.caracteristicas);
    	newMueble.setMaterial(this.material);
    	newMueble.setArtista(this.artista);
    	newMueble.setDestino(this.destino);
    	newMueble.setPropiedad(this.propiedad);
    	newMueble.setEstadoConservacion(this.estadoConservacion);
    	if (this.costeAdquisicion!=null)
    		newMueble.setCosteAdquisicion(this.costeAdquisicion);
    	if (this.valorActual!=null)
    		newMueble.setValorActual(this.valorActual);
    	newMueble.setMarca(this.marca);
    	newMueble.setModelo(this.modelo);
    	newMueble.setNumSerie(this.numSerie);
    	newMueble.setLote(this.lote);
    	newMueble.setFechaFinGarantia(this.fechaFinGarantia);
	
    	return newMueble;
    	
	}
    

 }
