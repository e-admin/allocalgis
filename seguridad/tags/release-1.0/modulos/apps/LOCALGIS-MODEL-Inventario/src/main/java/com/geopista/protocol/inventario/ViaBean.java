package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 26-sep-2006
 * Time: 12:44:52
 * To change this template use File | Settings | File Templates.
 */
public class ViaBean  extends BienBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String categoria;
    private String codigo;
    private String nombreVia;
    private String inicioVia;
    private String finVia;
    private String destino;
    private Long numApliques;
    private Long numBancos;
    private Long numPapeleras;
    private Double metrosPavimentados;
    private Double metrosNoPavimentados;
    private Double zonasVerdes;
    private Double longitud;
    private Double ancho;
    private Double valorActual;
    private Collection referenciasCatastrales;
    private String clase;
    
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public String getInicioVia() {
        return inicioVia;
    }

    public void setInicioVia(String inicioVia) {
        this.inicioVia = inicioVia;
    }

    public String getFinVia() {
        return finVia;
    }

    public void setFinVia(String finVia) {
        this.finVia = finVia;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Long getNumApliques() {
        return numApliques;
    }

    public void setNumApliques(long numApliques) {
        this.numApliques = new Long(numApliques);
    }

    public Long getNumBancos() {
        return numBancos;
    }

    public void setNumBancos(long numBancos) {
        this.numBancos = new Long(numBancos);
    }

    public Long getNumPapeleras() {
        return numPapeleras;
    }

    public void setNumPapeleras(long numPapeleras) {
        this.numPapeleras = new Long(numPapeleras);
    }

    public Double getMetrosPavimentados() {
        return metrosPavimentados;
    }

    public void setMetrosPavimentados(double metrosPavimentados) {
        this.metrosPavimentados = new Double(metrosPavimentados);
    }

    public Double getMetrosNoPavimentados() {
        return metrosNoPavimentados;
    }

    public void setMetrosNoPavimentados(double metrosNoPavimentados) {
        this.metrosNoPavimentados = new Double(metrosNoPavimentados);
    }

    public Double getZonasVerdes() {
        return zonasVerdes;
    }

    public void setZonasVerdes(double zonasVerdes) {
        this.zonasVerdes = new Double(zonasVerdes);
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = new Double(longitud);
    }

    public Double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = new Double(ancho);
    }

    public Double getValorActual() {
        return valorActual;
    }

    public void setValorActual(double valorActual) {
        this.valorActual = new Double(valorActual);
    }

    public Collection getReferenciasCatastrales() {
        return referenciasCatastrales;
    }

    public void setReferenciasCatastrales(Collection referenciasCatastrales) {
        this.referenciasCatastrales = referenciasCatastrales;
    }
 
    public boolean isUrbana(){
        if ((getTipo() != null) && (getTipo().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS))) return true;
        return false;
    }
    public boolean isRustica(){
        if ((getTipo() != null) && (getTipo().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS))) return true;
        return false;
    }
    
}
