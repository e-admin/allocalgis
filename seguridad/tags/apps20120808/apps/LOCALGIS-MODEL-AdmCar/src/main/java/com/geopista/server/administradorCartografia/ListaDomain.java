package com.geopista.server.administradorCartografia;

import java.util.Hashtable;
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
 * Date: 07-jun-2004
 * Time: 17:22:26
 */
public class ListaDomain {
    private Hashtable hDom;
       public ListaDomain() {
           hDom=new Hashtable();
       }

       public void sethDom(Hashtable hDom) {
           this.hDom = hDom;
       }
       public void add(Domain domain)
       {
           this.hDom.put(domain.getIdDomain(),domain);
       }

       public Domain get(String sIdNode)
       {
           return (Domain)this.hDom.get(sIdNode);
       }

       public Hashtable gethDom() {
               return hDom;
       }
       public String print()
       {
           StringBuffer sbAux= new  StringBuffer();
           for (Enumeration e=hDom.elements();e.hasMoreElements();)
           {
               Domain auxDomain= (Domain)e.nextElement();
               sbAux.append(auxDomain.print());
           }
           return sbAux.toString();
       }
       public DomainNode getNode(String sIdDomain, String sIdNode)
       {
            Domain auxDomain = get(sIdDomain);
            if (auxDomain==null) return null;
            return auxDomain.getNode(sIdNode);
       }
       public boolean hasFamily(DomainNode nodo, int iType)
       {
           if (nodo.getIdParent()==null) return false;
           DomainNode padreNode= getNode(nodo.getIdDomain(),nodo.getIdParent());
           if (padreNode.getType()==iType) return true;
           else return hasFamily(padreNode, iType);
       }
}
