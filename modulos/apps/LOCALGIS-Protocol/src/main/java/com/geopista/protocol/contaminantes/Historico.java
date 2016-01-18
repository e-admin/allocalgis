/**
 * Historico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.contaminantes;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 18-abr-2005
 * Time: 14:55:14
 */
public class Historico {
  long id_historico;
  int id_elemento;
  Date fecha;
  String nombre_Usuario;
  int accion;
  int tipo_medioambiental;
  String apunte;
  int sistema=1;
  boolean borrar=false;

    public Historico()
    {
       id_historico=-1;
    }

    public Historico(int accion, String apunte, Date fecha, int id_elemento, long id_historico, String nombre_Usuario, int sistema, int tipo_medioambiental) {
        this.accion = accion;
        this.apunte = apunte;
        this.fecha = fecha;
        this.id_elemento = id_elemento;
        this.id_historico = id_historico;
        this.nombre_Usuario = nombre_Usuario;
        this.sistema = sistema;
        this.tipo_medioambiental = tipo_medioambiental;
    }

    public Historico(int id_elemento, int accion, int tipo_medioambiental) {
        this.id_historico=-1;
        this.id_elemento = id_elemento;
        this.accion = accion;
        this.tipo_medioambiental = tipo_medioambiental;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public String getApunte() {
        return apunte;
    }

    public void setApunte(String apunte) {
        this.apunte = apunte;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId_elemento() {
        return id_elemento;
    }

    public void setId_elemento(int id_elemento) {
        this.id_elemento = id_elemento;
    }

    public long getId_historico() {
        return id_historico;
    }

    public void setId_historico(long id_historico) {
        this.id_historico = id_historico;
    }

    public String getNombre_Usuario() {
        return nombre_Usuario;
    }

    public void setNombre_Usuario(String nombre_Usuario) {
        this.nombre_Usuario = nombre_Usuario;
    }

    public int getSistema() {
        return sistema;
    }

    public void setSistema(int sistema) {
        this.sistema = sistema;
    }

    public int getTipo_medioambiental() {
        return tipo_medioambiental;
    }

    public void setTipo_medioambiental(int tipo_medioambiental) {
        this.tipo_medioambiental = tipo_medioambiental;
    }

    public boolean isBorrar() {
        return borrar;
    }

    public void setBorrar(boolean borrar) {
        this.borrar = borrar;
    }
    public boolean esSistema() {
        return (sistema==1);
    }
}
