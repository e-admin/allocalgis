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
    
    
    
    
}
