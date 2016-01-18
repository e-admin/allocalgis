package com.geopista.protocol.administrador.dominios;

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
 * Time: 16:14:50
 */
public class Domain {
    private String idDomain;
    private ListaDomainNode listaNodes;
    private String name;
    private String idCategory;

    public Domain(String idDomain, String name) {
        this.idDomain = idDomain;
        this.name = name;
        listaNodes= new ListaDomainNode();
    }

    public Domain() {
        listaNodes= new ListaDomainNode();
    }

    public Domain(String idDomain, ListaDomainNode listaNodes, String name) {
        this.idDomain = idDomain;
        this.listaNodes = listaNodes;
        this.name = name;
    }

    public Domain(String idDomain,  String name,String idCategory) {
        this.idCategory = idCategory;
        this.idDomain = idDomain;
        this.name = name;
        listaNodes= new ListaDomainNode();
    }

    public String getIdDomain() {
        return idDomain;
    }

    public String getName() {
        return name;
    }

    public void setIdDomain(String idDomain) {
        this.idDomain = idDomain;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ListaDomainNode getListaNodes() {
        return listaNodes;
    }

    public void setListaNodes(ListaDomainNode listaNodes) {
        this.listaNodes = listaNodes;

    }
    public void addNode(DomainNode node)
    {
        if (listaNodes==null) listaNodes=new ListaDomainNode();
        if (node!=null)
        listaNodes.add(node);
    }
    public void restructurar()
    {
        listaNodes.restructurar();
    }
    public String print()
    {
        return ("\nDOMINIO: "+ this.getIdDomain() + "NOMBRE DOMINIO: "+this.getName()+
                listaNodes.print());
    }
    public String toString()
    {
        return getName();
    }
    public DomainNode getNode(String sIdNode)
    {
        return listaNodes.busca(sIdNode);
    }
    public void removeNode(DomainNode nodo)
    {
        listaNodes.remove(nodo);
    }
    public int compareTo(Domain valor)
    {

        if (valor==null || valor.getName()==null)return -1;
        return (name.toUpperCase().compareTo(valor.getName().toUpperCase()));
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }
}
