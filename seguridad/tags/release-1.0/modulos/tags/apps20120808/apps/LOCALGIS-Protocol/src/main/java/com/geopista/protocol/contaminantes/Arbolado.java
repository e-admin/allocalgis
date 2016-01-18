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
 * Date: 13-oct-2004
 * Time: 16:27:56
 */
public class Arbolado {
    String id;
    String obs;
    float ext;
    Date fechaPlanta;
    Date fechaUltimaTala;
    String plantadoPor;
    String idTipo;


    public Arbolado() {
    }

    public Arbolado(String id, String obs, float ext) {
        this.ext = ext;
        this.id = id;
        this.obs = obs;
    }

    public float getExt() {
        return ext;
    }

    public void setExt(float ext) {
        this.ext = ext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Date getFechaPlanta() {
        return fechaPlanta;
    }

    public void setFechaPlanta(Date fechaPlanta) {
        this.fechaPlanta = fechaPlanta;
    }

    public Date getFechaUltimaTala() {
        return fechaUltimaTala;
    }

    public void setFechaUltimaTala(Date fechaUltimaTala) {
        this.fechaUltimaTala = fechaUltimaTala;
    }

    public String getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }

    public String getPlantadoPor() {
        return plantadoPor;
    }

    public void setPlantadoPor(String plantadoPor) {
        this.plantadoPor = plantadoPor;
    }
}
