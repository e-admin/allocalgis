package com.geopista.server.catastro.servicioWebCatastroApp;

import java.util.Date;

/**
 * Clase que implementa el bean cabecera de la creacion del xml de finEntrada de catastro. Se crea el objeto y
 * con el java2xml se parsea facilmente a xml.
 * */

public class CabeceraFinEntrada
{
    private String tipoEntidadGeneradora;
    private int codigoDelegacion;
    private String MunicipioODiputacion;
    private String nombreEntidadGeneradora;
    private Date fechaGeneracionFichero;
    private String horaGeneracionFichero;
    private String tipoFichero;
    private String descripcionDelFichero;
    private String nombreFicheroAsignadoPorDGC;
    private int codigoEntidadDestinataria;
    private Date fechaInicioPeriodo;
    private Date fechaFinalizacionPeriodo;
    private int numeroExpedientes;
    private int numeroFincas;
    private int numeroBienesInmuebles;
    private int numeroTitulares;
    
    /** Constante tipo de fichero que en el caso de fin entrada simpre es CFIE*/
    public static String TIPO_FICHERO_FIN_ENTRADA = "CFIE";

    /** Constante tipo de fichero que en el caso de VARPAD simpre es CVPE*/
    public static String TIPO_FICHERO_VARPAD = "CVPE";

    /**
     * Constructor de la clase.
     * */
    public CabeceraFinEntrada(){

    }

    public String getTipoEntidadGeneradora() {
        return tipoEntidadGeneradora;
    }

    public void setTipoEntidadGeneradora(String tipoEntidadGeneradora) {
        this.tipoEntidadGeneradora = tipoEntidadGeneradora;
    }

    public int getCodigoDelegacion(){
        return codigoDelegacion;
    }

    public void setCodigoDelegacion(int codigoDelegacion) {
        this.codigoDelegacion = codigoDelegacion;
    }

    public String getMunicipioODiputacion() {
        return MunicipioODiputacion;
    }

    public void setMunicipioODiputacion(String municipioODiputacion) {
        MunicipioODiputacion = municipioODiputacion;
    }

    public String getNombreEntidadGeneradora() {
        return nombreEntidadGeneradora;
    }

    public void setNombreEntidadGeneradora(String nombreEntidadGeneradora){
        this.nombreEntidadGeneradora = nombreEntidadGeneradora;
    }

    public Date getFechaGeneracionFichero() {
        return fechaGeneracionFichero;
    }

    public void setFechaGeneracionFichero(Date fechaGeneracionFichero) {
        this.fechaGeneracionFichero = fechaGeneracionFichero;
    }

    public String getHoraGeneracionFichero() {
        return horaGeneracionFichero;
    }

    public void setHoraGeneracionFichero(String horaGeneracionFichero)  {
        this.horaGeneracionFichero = horaGeneracionFichero;
    }

    public String getTipoFichero(){
        return tipoFichero;
    }

    public void setTipoFichero(String tipoFichero){
        this.tipoFichero = tipoFichero;
    }

    public String getDescripcionDelFichero(){
        return descripcionDelFichero;
    }

    public void setDescripcionDelFichero(String descripcionDelFichero) {
        this.descripcionDelFichero = descripcionDelFichero;
    }

    public String getNombreFicheroAsignadoPorDGC() {
        return nombreFicheroAsignadoPorDGC;
    }

    public void setNombreFicheroAsignadoPorDGC(String nombreFicheroAsignadoPorDGC) {
        this.nombreFicheroAsignadoPorDGC = nombreFicheroAsignadoPorDGC;
    }

    public int getCodigoEntidadDestinataria() {
        return codigoEntidadDestinataria;
    }

    public void setCodigoEntidadDestinataria(int codigoEntidadDestinataria) {
        this.codigoEntidadDestinataria = codigoEntidadDestinataria;
    }

    public Date getFechaInicioPeriodo() {
        return fechaInicioPeriodo;
    }

    public void setFechaInicioPeriodo(Date fechaInicioPeriodo){
        this.fechaInicioPeriodo = fechaInicioPeriodo;
    }

    public Date getFechaFinalizacionPeriodo() {
        return fechaFinalizacionPeriodo;
    }

    public void setFechaFinalizacionPeriodo(Date fechaFinalizacionPeriodo) {
        this.fechaFinalizacionPeriodo = fechaFinalizacionPeriodo;
    }

    public int getNumeroExpedientes(){
        return numeroExpedientes;
    }

    public void setNumeroExpedientes(int numeroExpedientes) {
        this.numeroExpedientes = numeroExpedientes;
    }

    public int getNumeroFincas(){
        return numeroFincas;
    }

    public void setNumeroFincas(int numeroFincas){
        this.numeroFincas = numeroFincas;
    }

    public int getNumeroBienesInmuebles(){
        return numeroBienesInmuebles;
    }

    public void setNumeroBienesInmuebles(int numeroBienesInmuebles) {
        this.numeroBienesInmuebles = numeroBienesInmuebles;
    }

    public int getNumeroTitulares(){
        return numeroTitulares;
    }

    public void setNumeroTitulares(int numeroTitulares) {
        this.numeroTitulares = numeroTitulares;
    }
}
