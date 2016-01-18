/**
 * DomainNode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.util;

import java.util.Hashtable;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 07-jun-2004
 * Time: 16:15:35
 */
public class DomainNode  implements java.io.Serializable, Cloneable{
    private String idNode;
    private String idDes;
    private int type;
    private String idParent;
    private String idMuni;
    private Hashtable hDict;
    private ListaDomainNode lHijos;
    private String idDomain;
    private String patron;



    public DomainNode() {
         hDict= new Hashtable();
         lHijos= new ListaDomainNode();
    }

    public DomainNode(String idNode, String idDes, int type, String idParent, String idMuni, String idDomain,
                      String patron) {
        this.idNode = idNode;
        this.idDes = idDes;
        this.type = type;
        this.idParent = idParent;
        this.idMuni = idMuni;
        hDict= new Hashtable();
        this.idDomain=idDomain;
        this.patron=patron;
        lHijos= new ListaDomainNode();
    }

    public String getIdNode() {
        return idNode;
    }

    public String getIdDes() {
        return idDes;
    }

    public int getType() {
        return type;
    }

    public String getIdParent() {
        return idParent;
    }

    public String getIdMuni() {
        return idMuni;
    }

    public void setIdNode(String idNode) {
        this.idNode = idNode;
    }
    public void setIdDes(String idDes) {
        this.idDes = idDes;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }
    public void setIdMuni(String idMuni) {
        this.idMuni = idMuni;
    }
    public void addTerm(String sLocale, String sTerm)
    {
        if ((sTerm==null) || (sLocale==null)) return;
        hDict.put(sLocale, sTerm);
    }
    public String getTerm(String sLocale)
    {
        String sTerm= (String) hDict.get(sLocale);
        if (sTerm!=null) return sTerm;
        return getFirstTerm();
    }

    public void setTerm(String sLocale, String sTerm)
    {
        if ((sTerm==null)||sTerm.length()==0)
            hDict.remove(sLocale);
        else
            hDict.put(sLocale,sTerm);
    }

    public Hashtable gethDict() {
        return hDict;
    }

    public void sethDict(Hashtable hDict) {
        this.hDict = hDict;
    }
    public void addHijo(DomainNode node)
    {
        lHijos.add(node);
    }
    public void removeHijo(DomainNode node)
    {
        lHijos.remove(node);
    }
    public String getFirstTerm()
    {
        if (hDict.size()>=1)
            return (String)hDict.elements().nextElement();
        else
            return null;
    }

    public ListaDomainNode getlHijos() {
        return lHijos;
    }

    public void setlHijos(ListaDomainNode lHijos) {
        this.lHijos = lHijos;
    }
    public String toString() {
            return getFirstTerm();
        }
    public String getIdDomain() {
           return idDomain;
       }

    public void setIdDomain(String idDomain) {
           this.idDomain = idDomain;
    }

    public String getPatron() {
        return patron;
    }

    public void setPatron(String patron) {
        this.patron = patron;
    }
    public Object clone()
        {
            DomainNode obj=null;
            try{
                   obj=(DomainNode)super.clone();
             }catch(CloneNotSupportedException ex){
             }
             obj.sethDict((Hashtable)this.hDict.clone());
             obj.setlHijos((ListaDomainNode)this.lHijos.clone());

             return obj;
        }

    public boolean copy(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainNode)) return false;

        final DomainNode domainNode = (DomainNode) o;

        type = domainNode.type;
        hDict = domainNode.hDict;
        idDes = domainNode.idDes;
        idDomain = domainNode.idDomain;
        idMuni = domainNode.idMuni;
        idNode  = domainNode.idNode;
        idParent = domainNode.idParent;
        lHijos =domainNode.lHijos;
        patron = domainNode.patron ;

        return true;
    }
    public int compareTo(DomainNode valor, String locale)
   {

          if (valor==null || valor.getTerm(locale)==null)return -1;
         return (getTerm(locale).toUpperCase().compareTo(valor.getTerm(locale).toUpperCase()));
   }




}
