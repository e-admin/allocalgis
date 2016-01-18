package com.geopista.protocol.catastro;

import java.util.Date;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 22-dic-2004
 * Time: 13:13:47
 */
public class Parcela {
    private Via via;
    private String id;
    private String referenciaCatastral;
    private String tipo;
    private String primerNumero;
    private String primeraLetra;
    private String segundoNumero;
    private String segundaLetra;
    private String direccionNoEstructurada;
    private String codigoPostal;
    private String municipio;
    private String provincia;
    private String distritoCensal;
    Double superficieSolar;
    Double superficieConstruidaTotal;
    Double superficieConstruidaSobrerasante;
    Double superficieConstruidaBajorasante;
    Double superficieCubierta;
    String annoAprobacion;
    String annoExpediente;
    String referenciaExpediente;
    Date fechaAlta;
    Date fechaBaja;
    Float area;
    Float length;



    public Via getVia() {
        return via;
    }

    public void setVia(Via via) {
        this.via = via;
    }

    public void setId(String id){
        this.id= id;
    }

    public String getId(){
        return id;
    }

    public void setReferenciaCatastral(String ref){
        this.referenciaCatastral= ref;
    }

    public String getReferenciaCatastral(){
        return referenciaCatastral;
    }

    public void setTipo(String tipo){
        this.tipo= tipo;
    }

    public String getTipo(){
        return tipo;
    }

    public void setPrimerNumero(String primerNumero){
        this.primerNumero= primerNumero;
    }

    public String getPrimerNumero(){
        return primerNumero;
    }

    public void setSegundoNumero(String segundoNumero){
        this.segundoNumero= segundoNumero;
    }

    public String getSegundoNumero(){
        return segundoNumero;
    }


    public void setPrimeraLetra(String primeraLetra){
        this.primeraLetra= primeraLetra;
    }

    public String getPrimeraLetra(){
        return primeraLetra;
    }

    public void setSegundaLetra(String segundaLetra){
        this.segundaLetra= segundaLetra;
    }

    public String getSegundaLetra(){
        return segundaLetra;
    }

    public void setDireccionNoEstructurada(String dir){
        this.direccionNoEstructurada= dir;
    }

    public String getDireccionNoEstructurada(){
        return direccionNoEstructurada;
    }

    public void setCodigoPostal(String codigoPostal){
        this.codigoPostal= codigoPostal;
    }

    public String getCodigoPostal(){
        return codigoPostal;
    }

    public void setMunicipio(String municipio){
         this.municipio= municipio;
     }

     public String getMunicipio(){
         return municipio;
     }

    public void setProvincia(String provincia){
         this.provincia= provincia;
     }

     public String getProvincia(){
         return provincia;
     }

    public void setDistritoCensal(String distrito){
         this.distritoCensal= distrito;
     }

     public String getDistritoCensal(){
         return distritoCensal;
     }    

    public void setSuperficieSolar(Double superficie){
        this.superficieSolar= superficie;
    }

    public Double getSuperficieSolar(){
        return superficieSolar;
    }

    public void setSuperficieConstruidaTotal(Double superficie){
        this.superficieConstruidaTotal= superficie;
    }

    public Double getSuperficieConstruidaTotal(){
        return superficieConstruidaTotal;
    }

    public void setSuperficieConstruidaSobrerasante(Double superficie){
        this.superficieConstruidaSobrerasante= superficie;
    }

    public Double getSuperficieConstruidaSobrerasante(){
        return superficieConstruidaSobrerasante;
    }

    public void setSuperficieConstruidaBajorasante(Double superficie){
        this.superficieConstruidaBajorasante= superficie;
    }

    public Double getSuperficieConstruidaBajorasante(){
        return superficieConstruidaBajorasante;
    }

    public void setSuperficieCubierta(Double superficie){
        this.superficieCubierta= superficie;
    }

    public Double getSuperficieCubierta(){
        return superficieCubierta;
    }

    public void setAnnoAprobacion(String anno){
        this.annoAprobacion= anno;
    }

    public String getAnnoAprobacion(){
        return annoAprobacion;
    }

    public void setAnnoExpediente(String anno){
        this.annoExpediente= anno;
    }

    public String getAnnoExpediente(){
        return annoExpediente;
    }

    public void setReferenciaExpediente(String ref){
        this.referenciaExpediente= ref;
    }

    public String getReferenciaExpediente(){
        return referenciaExpediente;
    }

    public void setFechaAlta(Date date){
        this.fechaAlta= date;
    }

    public Date getFechaAlta(){
        return fechaAlta;
    }

    public void setFechaBaja(Date date){
        this.fechaBaja= date;
    }

    public Date getFechaBaja(){
        return fechaBaja;
    }

    public void setArea(Float area){
        this.area= area;
    }

    public Float getArea(){
        return area;
    }

    public void setLength(Float lon){
        this.length= lon;
    }

    public Float getLength(){
        return length;
    }

}
