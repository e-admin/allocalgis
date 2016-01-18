package com.geopista.protocol.administrador;

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

public class Conexion {
    String idUsuario;
    String idApp;
    Date fechaInicio;
    Date fechaFin;
    String idConexion;
    
    public Conexion() {
    }

    public Conexion(String idUsuario,String idApp,Date fechaInicio,Date fechaFin) {
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.idApp = idApp;
        this.idUsuario = idUsuario;
    }

    public Conexion(String idConexion, String idUsuario, String idApp, Date fechaInicio, Date fechaFin) {
        this.idConexion = idConexion;
    	this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.idApp = idApp;
        this.idUsuario = idUsuario;
    }
    
    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getIdApp() {
        return idApp;
    }

    public void setIdApp(String idApp) {
        this.idApp = idApp;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdConexion() {
        return idConexion;
    }

    public void setIdConexion(String idConexion) {
        this.idConexion = idConexion;
    }
}
