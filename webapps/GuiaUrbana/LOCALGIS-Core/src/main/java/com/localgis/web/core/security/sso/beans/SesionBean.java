/**
 * SesionBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.security.sso.beans;

import java.util.List;
import java.util.Date;
import java.util.Calendar;

/*
 * @Author dcaaveiro
 */
public class SesionBean {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SesionBean.class);

    private String idSesion;
    private String idApp;
    private String idUser;
    private List alMunicipios;
    private String idEntidad;
    private String idMunicipio;
    private String sName;
    private Date fechaConexion=null;

    public SesionBean() {
    	
    }

    public SesionBean(String sIdSesion, String sIdApp) 
    {
        idSesion=sIdSesion;
        idApp=sIdApp;      
        fechaConexion=Calendar.getInstance().getTime();
    }

    public Date getFechaConexion() {
        return fechaConexion;
    }

    public void setFechaConexion(Date fechaConexion) {
        this.fechaConexion = fechaConexion;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public void setIdApp(String idApp) {
        this.idApp = idApp;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdSesion()
    {
        return idSesion;
    }
  
    public String getIdApp()
    {
        return idApp;
    }

    public List getAlMunicipios() {
        return alMunicipios;
    }

    public void setAlMunicipios(List alMunicipios) {
        this.alMunicipios = alMunicipios;
    }

    public String getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(String idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(String idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}
    
	
	
}
