package com.geopista.protocol.administrador.dominios;

import java.util.Enumeration;
import java.util.Hashtable;

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
 * Time: 16:20:43
 */
public class ListaDomainNode implements java.io.Serializable,Cloneable{

    private Hashtable hDom;
	
	
    public ListaDomainNode() {
        hDom= new Hashtable();
    }

    public void sethDom(Hashtable hDom) {
        this.hDom = hDom;
    }
    public void add(DomainNode node)
    {
        this.hDom.put(node.getIdNode(),node);
   	 
    }

    public DomainNode get(String sIdNode)
    {
        return (DomainNode)this.hDom.get(sIdNode);
        
    }

    public Hashtable gethDom() {
        return hDom;
    }

    public void restructurar()
    {
        Hashtable auxHashNode=new Hashtable();
        for (Enumeration e=hDom.elements();e.hasMoreElements();)
        {
            DomainNode nodeAux= (DomainNode)e.nextElement();
            if (nodeAux.getIdParent()==null)
               auxHashNode.put(nodeAux.getIdNode(),nodeAux);
            else
            {
                DomainNode nodePadre= (DomainNode)hDom.get(nodeAux.getIdParent());
                if (nodePadre==null)
                	 System.err.println("El nodo padre no se encuentra en la lista: "+nodeAux.getIdParent()+
                     " posible inconsistencia de datos");
                else
                   nodePadre.addHijo(nodeAux);

            }
        }
        hDom=auxHashNode;
    }
    public void remove(DomainNode node)
    {
        hDom.remove(node.getIdNode());
    }
    public String print()
    {
        return print("    ",this);
    }
    public String print(String sTab, ListaDomainNode lista)
    {
        StringBuffer sbCadena=new StringBuffer();
        for (Enumeration e=lista.gethDom().elements(); e.hasMoreElements();)
        {
            DomainNode node=(DomainNode)e.nextElement();
            sbCadena.append(sTab+"\nNodo ID: "+node.getIdNode()+ " Descripcion; "+node.getFirstTerm());
            print(sTab+"     ",node.getlHijos());
        }
        return sbCadena.toString();
    }
    public DomainNode busca(String sIdNode)
    {
        return busca(sIdNode,this);
    }
    public DomainNode busca(String sIdNode, ListaDomainNode lista)
    {
        if (lista.get(sIdNode)!=null) return lista.get(sIdNode);
        for (Enumeration e=lista.gethDom().elements(); e.hasMoreElements();)
        {
            DomainNode node=(DomainNode)e.nextElement();
            DomainNode buscado=busca(sIdNode, node.getlHijos());
            if (buscado!=null) return buscado;
        }
        return null;

    }
    public DomainNode getFirst()
    {
         if (hDom.size()==0) return null;
         Enumeration e=hDom.elements();
        return (DomainNode)e.nextElement();
    }

     public Object clone()
     {
        ListaDomainNode obj=null;
        try{
                   obj=(ListaDomainNode)super.clone();
             }catch(CloneNotSupportedException ex){
             }
             obj.sethDom((Hashtable)this.hDom.clone());
             return obj;
        }
     
}



