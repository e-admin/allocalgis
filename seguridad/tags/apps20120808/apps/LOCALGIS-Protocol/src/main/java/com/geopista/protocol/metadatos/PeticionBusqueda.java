package com.geopista.protocol.metadatos;

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
 * Date: 30-ago-2004
 * Time: 14:37:22
 */
public class PeticionBusqueda {
     String id_capa;
     String id_metadato;
     String id_contact;
     String titulo;
     Date f_desde;
     Date f_hasta;

    public PeticionBusqueda() {
    }

    public Date getF_desde() {
        return f_desde;
    }

    public void setF_desde(Date f_desde) {
        this.f_desde = f_desde;
    }

    public Date getF_hasta() {
        return f_hasta;
    }

    public void setF_hasta(Date f_hasta) {
        this.f_hasta = f_hasta;
    }

    public String getId_capa() {
        return id_capa;
    }

    public void setId_capa(String id_capa) {
        this.id_capa = id_capa;
    }

    public String getId_contact() {
        return id_contact;
    }

    public void setId_contact(String id_contact) {
        this.id_contact = id_contact;
    }

    public String getId_metadato() {
        return id_metadato;
    }

    public void setId_metadato(String id_metadato) {
        this.id_metadato = id_metadato;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
