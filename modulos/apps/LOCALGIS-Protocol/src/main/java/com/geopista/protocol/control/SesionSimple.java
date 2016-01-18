/**
 * SesionSimple.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.control;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 17-jun-2004
 * Time: 9:33:32
 * Esta clase solo sirve para el paso de datos
 */
public class SesionSimple {
     private String idSesion;
    private String idApp;
    private String idUser;
    private String sName;
    private Date fechaConexion;

    public SesionSimple() {
    }
    
    public SesionSimple(String id,String name,String tipo, Date fecha)
    {
       this.idSesion=id;
       this.idApp=tipo;
       this.sName=name;
       this.fechaConexion=fecha;
    }
    
    public SesionSimple(Sesion sesion)
    {
        idSesion=sesion.getIdSesion();
        idApp=sesion.getIdApp();
        idUser=sesion.getIdUser();
        sName=sesion.getUserPrincipal().getName();
        fechaConexion=sesion.getFechaConexion();
    }

    public String getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public String getIdApp() {
        return idApp;
    }

    public void setIdApp(String idApp) {
        this.idApp = idApp;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public Date getFechaConexion() {
        return fechaConexion;
    }

    public void setFechaConexion(Date fechaConexion) {
        this.fechaConexion = fechaConexion;
    }
}
