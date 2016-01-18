package com.geopista.protocol.inventario;

import java.util.Date;
import java.io.Serializable;

public class ValorMobiliarioBean extends BienBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String emitidoPor;
    private String depositadoEn;
    private String clase;
    private Double costeAdquisicion;
    private Double valorActual;
    private String numero;
    private String serie;
    private Integer numTitulos;
    private Double precio;
    private Double capital;
    private Date fechaAcuerdo;
    private String destino;
    
    public ValorMobiliarioBean(){
    	super();
    	setTipo(Const.PATRON_VALOR_MOBILIARIO);
    }

	public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }


    public String getEmitidoPor() {
        return emitidoPor;
    }

    public void setEmitidoPor(String emitidoPor) {
        this.emitidoPor = emitidoPor;
    }

    public String getDepositadoEn() {
        return depositadoEn;
    }

    public void setDepositadoEn(String depositadoEn) {
        this.depositadoEn = depositadoEn;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getNumTitulos() {
        return numTitulos;
    }

    public void setNumTitulos(int numTitulos) {
        this.numTitulos = new Integer(numTitulos);
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = new Double(precio);
    }

    public Double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = new Double(capital);
    }

    public Date getFechaAcuerdo() {
        return fechaAcuerdo;
    }

    public void setFechaAcuerdo(Date fechaAcuerdo) {
        this.fechaAcuerdo = fechaAcuerdo;
    }

   



}
