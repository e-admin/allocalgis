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
