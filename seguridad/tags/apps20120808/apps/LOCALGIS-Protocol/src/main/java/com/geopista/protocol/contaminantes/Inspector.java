package com.geopista.protocol.contaminantes;

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
 * Date: 14-oct-2004
 * Time: 15:25:52
 */
public class Inspector {
    String id;
    String apellido1;
    String apellido2;
    String nombre;
    String empresa;
    String puesto;
    String direccion;
    String telefono;
    String otrosdatos;

    public Inspector() {
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOtrosdatos() {
        return otrosdatos;
    }

    public void setOtrosdatos(String otrosdatos) {
        this.otrosdatos = otrosdatos;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

      public boolean copy(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inspector)) return false;

        final Inspector inspector = (Inspector) o;
        id=inspector.getId();
        apellido1=inspector.getApellido1();
        apellido2=inspector.getApellido2();
        nombre=inspector.getNombre();
        empresa=inspector.getEmpresa();
        puesto=inspector.getPuesto();
        direccion=inspector.getDireccion();
        telefono=inspector.getTelefono();
        otrosdatos=inspector.getOtrosdatos();
        return true;
    }
    public String toString()
    {
         return nombre + " "+ apellido1 + " "+apellido2+ " "+"["+empresa+"]";
    }
}
