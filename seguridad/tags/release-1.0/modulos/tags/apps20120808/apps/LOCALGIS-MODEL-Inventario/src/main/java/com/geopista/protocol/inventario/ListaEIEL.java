package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Hashtable;


public class ListaEIEL implements Serializable{
	
    private Hashtable hEIEL;
    
    public  ListaEIEL()
    {
          this.hEIEL = new Hashtable();
    }
    public void add(InventarioEIELBean  eiel) {
        this.hEIEL.put(eiel.getUnionClaveEIEL(), eiel);
    }

    public InventarioEIELBean get(String union_clave_eiel)
    {
        return (InventarioEIELBean)this.hEIEL.get(union_clave_eiel);
    }
    public void remove(InventarioEIELBean eiel)
    {
        this.hEIEL.remove(eiel.getUnionClaveEIEL());
    }

    public Hashtable gethEIEL() {
        return hEIEL;
    }

    public void sethEIEL(Hashtable hEIEL) {
        this.hEIEL = hEIEL;
    }
}

