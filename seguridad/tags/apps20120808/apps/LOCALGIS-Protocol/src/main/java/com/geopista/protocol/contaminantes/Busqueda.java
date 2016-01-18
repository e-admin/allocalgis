package com.geopista.protocol.contaminantes;

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
 * Date: 21-oct-2004
 * Time: 15:43:10
 */
public class Busqueda
{
    String id_tipo;
    String id_razon;
    String numeroAdm;
    String asunto;
    Date fInicio;
    Date fFin;

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Date getfFin() {
        return fFin;
    }

    public void setfFin(Date fFin) {
        this.fFin = fFin;
    }

    public Date getfInicio() {
        return fInicio;
    }

    public void setfInicio(Date fInicio) {
        this.fInicio = fInicio;
    }

    public String getId_razon() {
        return id_razon;
    }

    public void setId_razon(String id_razon) {
        this.id_razon = id_razon;
    }

    public String getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(String id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getNumeroAdm() {
        return numeroAdm;
    }

    public void setNumeroAdm(String numeroAdm) {
        this.numeroAdm = numeroAdm;
    }
}
