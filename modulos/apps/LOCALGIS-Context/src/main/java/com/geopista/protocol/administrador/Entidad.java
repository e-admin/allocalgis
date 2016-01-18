/**
 * Entidad.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import java.io.Serializable;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 29-nov-2004
 * Time: 9:05:33
 */
public class Entidad implements Serializable{
    String id;
    String nombre;
    String idprovincia;
    String provincia;
    String srid;
    boolean backup=true;
    int aviso;
    int periodicidad;
    int intentos;
    String entidadExt;
    
    Set<Municipio> municipios;
    
    
    public Entidad(String id, String nombre, String srid) {
    	this.id = id;
		this.nombre = nombre;
		this.srid = srid;
	}
    
	public Entidad(String id, String nombre, String srid, int backup) {
        this.id = id;
        this.nombre = nombre;
        this.srid = srid;
        this.backup = backup==0?false:true;
    }

	public Entidad(String id, String nombre, String srid, boolean backup) {
        this.id = id;
        this.nombre = nombre;
        this.srid = srid;
        this.backup = backup;
    }

	public Entidad(String id, String nombre, String srid, boolean backup, int aviso, int periodicidad, int intentos) {
        this.id = id;
        this.nombre = nombre;
        this.srid = srid;
        this.backup = backup;
        this.aviso = aviso;
        this.periodicidad = periodicidad;
        this.intentos = intentos;
    }
	
	public Entidad(String id, String nombre, String srid, int backup, int aviso, int periodicidad, int intentos) {
        this.id = id;
        this.nombre = nombre;
        this.srid = srid;
        this.backup = backup==0?false:true;
        this.aviso = aviso;
        this.periodicidad = periodicidad;
        this.intentos = intentos;
    }
	
	public Entidad(String id, String nombre, String srid, boolean backup, int aviso, int periodicidad, int intentos, String entidadExt) {
        this.id = id;
        this.nombre = nombre;
        this.srid = srid;
        this.backup = backup;
        this.aviso = aviso;
        this.periodicidad = periodicidad;
        this.intentos = intentos;
        this.entidadExt = entidadExt;
    }
	
	public Entidad(String id, String nombre, String srid, int backup, int aviso, int periodicidad, int intentos, String entidadExt) {
        this.id = id;
        this.nombre = nombre;
        this.srid = srid;
        this.backup = backup==0?false:true;
        this.aviso = aviso;
        this.periodicidad = periodicidad;
        this.intentos = intentos;
        this.entidadExt = entidadExt;
    }

	public Entidad() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdprovincia() {
        return idprovincia;
    }

    public void setIdprovincia(String idprovincia) {
        this.idprovincia = idprovincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getSrid() {
        return srid;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }

	public boolean isBackup() {
		return backup;
	}

	public void setBackup(boolean backup) {
		this.backup = backup;
	}
    
    public int getAviso() {
		return aviso;
	}

	public void setAviso(int aviso) {
		this.aviso = aviso;
	}

	public int getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(int periodicidad) {
		this.periodicidad = periodicidad;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public void setMunicipios(Set<Municipio> municipios){
		this.municipios=municipios;
	}
	
	public Set<Municipio> getMunicipios(){
		return municipios;
	}
	
	public String getEntidadExt() {
		return entidadExt;
	}

	public void setEntidadExt(String entidadExt) {
		this.entidadExt = entidadExt;
	}
}
