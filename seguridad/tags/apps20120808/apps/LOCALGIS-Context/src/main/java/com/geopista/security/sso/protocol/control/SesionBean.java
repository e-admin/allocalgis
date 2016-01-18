package com.geopista.security.sso.protocol.control;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.geopista.protocol.Version;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 04-may-2004
 * Time: 11:51:50
 * To change this template use Options | File Templates.
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
    private Version version=null;

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

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}
    
	
	
}
