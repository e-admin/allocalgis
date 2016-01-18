package com.geopista.protocol.administrador;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

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
 * Time: 11:08:57
 */
public class ListaPermisos implements Serializable, Cloneable{
    //private Logger logger = Logger.getLogger(ListaPermisos.class);
    private Hashtable hPermisos;

    public void sethPermisos(Hashtable hPermisos) {
        this.hPermisos = hPermisos;
    }

    public  ListaPermisos()
    {
          this.hPermisos = new Hashtable();
    }
    public void add(Permiso permiso) {
        //this.hPermisos.put(permiso.getIdPerm(),permiso);
        /** Incidencia [308] - acl distintos con los mismos idperm */
        if ((permiso.getIdPerm() == null) || (permiso.getIdAcl() == null)) return;
        KeyListaPermisos key= new KeyListaPermisos(permiso.getIdPerm(), permiso.getIdAcl());
        this.hPermisos.put(key, permiso);
    }
    /*
    public Permiso get(String sIdPermiso)
    {
        return (Permiso)this.hPermisos.get(sIdPermiso);
    }
    */
    /** Incidencia [308] - acl distintos con los mismos idperm */
    public Permiso get(String idperm, String idacl)
    {
        if ((idperm == null) || (idacl == null)) return null;
        KeyListaPermisos key= new KeyListaPermisos(idperm, idacl);
        return (Permiso)this.hPermisos.get(key);
    }
    /*
    public void remove(String sIdPermiso)
    {
        if (sIdPermiso==null) return;
        hPermisos.remove(sIdPermiso);
    }
    */
    /** Incidencia [308] - acl distintos con los mismos idperm */
    public void remove(String idperm, String idacl)
    {
        if ((idperm == null) || (idacl == null)) return;
        KeyListaPermisos key= new KeyListaPermisos(idperm, idacl);
        hPermisos.remove(key);
    }

    public void set(Vector vPermisos)
    {
        if (vPermisos==null) return;
        for (Enumeration e=vPermisos.elements();e.hasMoreElements();)
        {
            Permiso auxPermiso=(Permiso)e.nextElement();
            add(auxPermiso);
        }
    }

    public Hashtable gethPermisos() {
        return hPermisos;
    }
    public Object clone()
    {
        ListaPermisos obj=null;
        try{
               obj=(ListaPermisos)super.clone();
         }catch(CloneNotSupportedException ex){
           //logger.error("Error al clonar el objeto ListaPermisos. "+ex.toString());
        	 System.out.println("Error al clonar el objeto ListaPermisos. "+ex.toString());
         }
         obj.sethPermisos((Hashtable)this.gethPermisos().clone());
         return obj;
    }

}
