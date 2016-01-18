package com.geopista.protocol.administrador;

import com.geopista.protocol.administrador.Acl;
import com.geopista.protocol.administrador.ListaAcl;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 16-mar-2012
 * Time: 11:13:04
 */
public class App implements java.io.Serializable{
    String id;
    String nombre;
    ListaAcl acls;

    public App(){
    	acls= new ListaAcl();
    }
    public App(String sId,String sNombre)
    {
        id=sId;
        nombre=sNombre;
        acls=new ListaAcl();
    }
    public App(String sId,String sNombre,  ListaAcl listaAcls)
    {
        id=sId;
        nombre=sNombre;
        acls=listaAcls;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setPermisos(ListaAcl listaAcls) {
        this.acls = listaAcls;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ListaAcl getAcls() {
        return acls;
    }

    
    public void addAcls(Acl auxAcl)
    {
        if (auxAcl==null) return;
        acls.add(auxAcl);
    }
}


