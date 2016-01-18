package com.geopista.protocol.inventario;

import java.util.Date;
import java.io.Serializable;

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
