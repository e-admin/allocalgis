/**
 * Sugerencia.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geopista.app.sugerencias;

/**
 *
 * @author osantos
 */
public class Sugerencia {
	
	private String usuario;
    private String password;
    
    private String tipo;
    private String asunto;
    private String descripcion;
    private String entorno;
    private String ficheroAdjunto;
    /** Detalles adicionales de la incidencia, como usuario LocalGIS*/
    private String detallesAdicionales;
    private String proyecto;
    
    public String getDetallesAdicionales() {
		return detallesAdicionales;
	}

	public void setDetallesAdicionales(String detallesAdicionales) {
		this.detallesAdicionales = detallesAdicionales;
	}
	
	public void addDetallesAdicionales(String detallesAdicionales) {
		this.detallesAdicionales=this.detallesAdicionales+ " " + detallesAdicionales;
		
	}

	public String getFicheroAdjunto() {
		return ficheroAdjunto;
	}

	public void setFicheroAdjunto(String ficheroAdjunto) {
		this.ficheroAdjunto = ficheroAdjunto;
	}



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEntorno() {
        return entorno;
    }

    public void setEntorno(String entorno) {
        this.entorno = entorno;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

	public String getProyecto() {
		return proyecto;
	}

	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}

}
