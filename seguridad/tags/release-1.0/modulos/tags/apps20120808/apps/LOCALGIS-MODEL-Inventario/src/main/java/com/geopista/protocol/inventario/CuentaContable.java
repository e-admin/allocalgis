package com.geopista.protocol.inventario;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 31-jul-2006
 * Time: 11:37:24
 * To change this template use File | Settings | File Templates.
 */
public class CuentaContable  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long id=-1;
    String descripcion;
    String cuenta;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String toString()
    {
        return (cuenta==null?"":cuenta+" - ")+(descripcion==null?"          ":descripcion);
    }

}
