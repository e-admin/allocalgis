/**
 * ListaContactos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.metadatos;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 23-jul-2004
 * Time: 10:32:43
 */
public class ListaContactos  implements Cloneable
 {
    private Logger logger = Logger.getLogger(ListaContactos.class);
    private Hashtable hContactos;

    public void sethContactos(Hashtable hContactos) {
        this.hContactos = hContactos;
    }

    public  ListaContactos()
    {
          this.hContactos = new Hashtable();
    }
    public void add(CI_ResponsibleParty contacto) {
        this.hContactos.put(contacto.getId(),contacto);
    }

    public CI_ResponsibleParty get(String sIdContacto)
    {
        return (CI_ResponsibleParty)this.hContactos.get(sIdContacto);
    }
    public void remove(String sIdContacto)
    {
        if (sIdContacto==null) return;
        hContactos.remove(sIdContacto);
    }
    public void remove(CI_ResponsibleParty contacto)
       {
           if ((contacto==null)||(contacto.getId()==null)) return;
           hContactos.remove(contacto.getId());
       }

    public void set(Vector vContactos)
    {
        if (vContactos==null) return;
        for (Enumeration e=vContactos.elements();e.hasMoreElements();)
        {
            CI_ResponsibleParty auxContacto=(CI_ResponsibleParty)e.nextElement();
            add(auxContacto);
        }
    }

    public Hashtable gethContactos() {
        return hContactos;
    }
    public Object clone()
    {
        ListaContactos obj=null;
        try{
               obj=(ListaContactos)super.clone();
         }catch(CloneNotSupportedException ex){
           logger.error("Error al clonar el objeto ListaContactos. "+ex.toString());
         }
         obj.sethContactos((Hashtable)this.gethContactos().clone());
         return obj;
    }

}


