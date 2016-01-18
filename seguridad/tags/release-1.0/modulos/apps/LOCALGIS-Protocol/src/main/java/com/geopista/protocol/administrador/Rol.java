package com.geopista.protocol.administrador;

import org.apache.log4j.Logger;

import java.util.Vector;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-may-2004
 * Time: 9:23:23
 */
public class Rol implements java.io.Serializable, Cloneable{
    private Logger logger = Logger.getLogger(Rol.class);
    String id;
    String nombre;
    String descripcion;
    ListaPermisos permisos;

    public Rol(){
        permisos = new ListaPermisos();
    }
    public Rol(String sId,String sNombre, String sDescripcion)
    {
        id=sId;
        nombre=sNombre;
        descripcion=sDescripcion;
        permisos = new ListaPermisos();
    }
    public Rol(String sId,String sNombre, String sDescripcion, ListaPermisos listaPermisos)
    {
        id=sId;
        nombre=sNombre;
        descripcion=sDescripcion;
        permisos=listaPermisos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPermisos(ListaPermisos listaPermisos) {
        this.permisos = listaPermisos;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ListaPermisos getPermisos() {
        return permisos;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void addPermiso(String sIdPerm, String sIdAcl)
    {
        if (sIdPerm==null) return;
        Permiso auxPermiso=new Permiso(sIdPerm, null, null, sIdAcl);
        permisos.add(auxPermiso);
    }
    public void deletePermiso(String sIdPerm, String sIdAcl)
    {
        if ((sIdPerm==null)||(sIdAcl==null)) return;

        /*
        Permiso auxPermiso= permisos.get(sIdPerm);
        if ((auxPermiso!=null)&&(sIdAcl.equals(auxPermiso.getIdAcl())))
        {
            permisos.remove(sIdPerm);
        }
        */
        /** Incidencia [308] - acl distintos con los mismos idperm */
        Permiso auxPermiso= permisos.get(sIdPerm, sIdAcl);
        if (auxPermiso!=null){
            permisos.remove(sIdPerm, sIdAcl);
        }

    }

    public boolean containsPermiso(String sIdPerm, String sIdAcl)
    {
        if ((sIdPerm==null)||(sIdAcl==null)) return false;
        /*
        Permiso auxPermiso= permisos.get(sIdPerm);
        if ((auxPermiso!=null)&&(sIdAcl.equals(auxPermiso.getIdAcl())))
            return true;
        return false;
        */
        /** Incidencia [308] - acl distintos con los mismos idperm */
        Permiso auxPermiso= permisos.get(sIdPerm, sIdAcl);
        if (auxPermiso!=null)
            return true;
        return false;

    }
    public void addPermiso(Permiso auxPermiso)
    {
        if (auxPermiso==null) return;
        permisos.add(auxPermiso);
    }
    public Object clone()
    {
        Rol obj=null;
        try{
               obj=(Rol)super.clone();
         }catch(CloneNotSupportedException ex){
           logger.error("Error al clonar el objeto ListaPermisos. "+ex.toString());
         }
         obj.setPermisos((ListaPermisos)this.getPermisos().clone());
         return obj;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
