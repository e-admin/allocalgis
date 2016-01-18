package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 22-sep-2006
 * Time: 15:05:30
 * To change this template use File | Settings | File Templates.
 */
public class SemovienteBean extends BienBean implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String propiedad;
    private String raza;
    private String conservacion;
    private String identificacion;
    private String especie;
    private String caracteristicas;
    private String destino;
    private Long   cantidad;
    private Double costeAdquisicion;
    private Double valorActual;
    private Date fechaNacimiento;
    
    public SemovienteBean(){
    	super();
    	setTipo(Const.PATRON_SEMOVIENTES);
    }

	public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getConservacion() {
        return conservacion;
    }

    public void setConservacion(String conservacion) {
        this.conservacion = conservacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = new Long(cantidad);
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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    
}
