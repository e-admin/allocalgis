package com.geopista.protocol.inventario;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 25-sep-2006
 * Time: 9:45:44
 * To change this template use File | Settings | File Templates.
 */
public class CompanniaSeguros implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private String nombre;
    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = new Long(id);
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
