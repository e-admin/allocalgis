package com.geopista.protocol.inventario;

import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 27-sep-2006
 * Time: 10:52:26
 * To change this template use File | Settings | File Templates.
 */
public class VehiculoBean extends BienBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String matriculaVieja;
    private String matriculaNueva;
    private String numBastidor;
    private String marca;
    private String motor;
    private String fuerza;
    private String servicio;
    private String destino;
    private String tipoVehiculo;
    private String estadoConservacion;
    private String traccion;
    private String propiedad;
 
    private Double costeAdquisicion;
    private Double valorActual;

    public VehiculoBean (){
    	super();
    	setTipo(Const.PATRON_VEHICULOS);
    }

    public String getMatriculaVieja() {
        return matriculaVieja;
    }

    public void setMatriculaVieja(String matriculaVieja) {
        this.matriculaVieja = matriculaVieja;
    }

    public String getMatriculaNueva() {
        return matriculaNueva;
    }

    public void setMatriculaNueva(String matriculaNueva) {
        this.matriculaNueva = matriculaNueva;
    }

    public String getNumBastidor() {
        return numBastidor;
    }

    public void setNumBastidor(String numBastidor) {
        this.numBastidor = numBastidor;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getFuerza() {
        return fuerza;
    }

    public void setFuerza(String fuerza) {
        this.fuerza = fuerza;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getEstadoConservacion() {
        return estadoConservacion;
    }

    public void setEstadoConservacion(String estadoConservacion) {
        this.estadoConservacion = estadoConservacion;
    }

    public String getTraccion() {
        return traccion;
    }

    public void setTraccion(String traccion) {
        this.traccion = traccion;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
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
        this.valorActual = valorActual;
    }

  
    

}
