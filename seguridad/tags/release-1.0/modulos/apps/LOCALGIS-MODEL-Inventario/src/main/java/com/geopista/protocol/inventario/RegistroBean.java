package com.geopista.protocol.inventario;

import java.io.Serializable;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 08-sep-2006
 * Time: 15:02:53
 */
public class RegistroBean  implements Serializable{
    private String tomo;
    private String folio;
    private String libro;
    private String finca;
    private String inscripcion;
    private String protocolo;
    private String notario;
   // private String registro;
    private String propiedad;

   /* public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }*/

    public String getFinca() {
        return finca;
    }

    public void setFinca(String finca) {
        this.finca = finca;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(String inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getNotario() {
        return notario;
    }

    public void setNotario(String notario) {
        this.notario = notario;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getTomo() {
        return tomo;
    }

    public void setTomo(String tomo) {
        this.tomo = tomo;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }
}
