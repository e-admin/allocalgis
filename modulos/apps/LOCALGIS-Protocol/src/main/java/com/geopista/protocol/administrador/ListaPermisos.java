/**
 * ListaPermisos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-may-2004
 * Time: 11:08:57
 */
public class ListaPermisos implements Serializable, Cloneable{
    //private Logger logger = Logger.getLogger(ListaPermisos.class);
    private Hashtable hPermisos;
    private ArrayList hPermisosSorted;

    public void sethPermisos(Hashtable hPermisos) {
        this.hPermisos = hPermisos;
    }
    
    public void sethPermisosSorted(ArrayList hPermisosSorted) {
        this.hPermisosSorted = hPermisosSorted;
    }

    public  ListaPermisos()
    {
          this.hPermisos = new Hashtable();
          this.hPermisosSorted = new ArrayList();
    }
    
    public void add(Permiso permiso) {
        //this.hPermisos.put(permiso.getIdPerm(),permiso);
        /** Incidencia [308] - acl distintos con los mismos idperm */
        if ((permiso.getIdPerm() == null) || (permiso.getIdAcl() == null)) return;
        KeyListaPermisos key= new KeyListaPermisos(permiso.getIdPerm(), permiso.getIdAcl());
        
        boolean duplicado=false;
        if (this.hPermisos.get(key)!=null)
        	duplicado=true;
        
        this.hPermisos.put(key, permiso);
        
        if (!duplicado)
        	this.hPermisosSorted.add(permiso);
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
        hPermisosSorted.remove(hPermisos.get(key));
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
    public ArrayList gethPermisosSorted() {
        return hPermisosSorted;
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
         obj.sethPermisosSorted((ArrayList)this.gethPermisosSorted().clone());
         return obj;
    }

}
