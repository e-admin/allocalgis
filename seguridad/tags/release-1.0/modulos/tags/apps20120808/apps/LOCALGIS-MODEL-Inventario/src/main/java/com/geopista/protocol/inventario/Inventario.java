package com.geopista.protocol.inventario;

import java.io.Serializable;

public class Inventario implements Serializable{
	private int id;
	private String numInventario;
	
	@Override
	public String toString() {
		return numInventario;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumInventario() {
		return numInventario;
	}
	public void setNumInventario(String numInventario) {
		this.numInventario = numInventario;
	}

	@Override
    public boolean equals(Object compareObj)
    {
	if (this == compareObj) // Are they exactly the same instance?
           return true;
 
	if (compareObj == null) // Is the object being compared null?
	    return false;
 
	if (!(compareObj instanceof Inventario)) // Is the object being compared also a Inventario?
	    return false;
 
	Inventario compareInventario = (Inventario)compareObj; // Convert the object to a Invnetario
 
	return (this.id == compareInventario.getId()); // Are they equal?
    }
}
