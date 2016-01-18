package com.geopista.server.administradorCartografia;

/** Alternativa serializable a las clases de ...protocol.dominios */
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

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getIdNode()
	 */
    
	public String getIdNode() {
        return idNode;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getIdDes()
	 */
    
	public String getIdDes() {
        return idDes;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getType()
	 */
    
	public int getType() {
        return type;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getIdParent()
	 */
    
	public String getIdParent() {
        return idParent;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getIdMuni()
	 */
    
	public String getIdMuni() {
        return idMuni;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#setIdNode(java.lang.String)
	 */
    
	public void setIdNode(String idNode) {
        this.idNode = idNode;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#setIdDes(java.lang.String)
	 */
    
	public void setIdDes(String idDes) {
        this.idDes = idDes;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#setType(int)
	 */
    
	public void setType(int type) {
        this.type = type;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#setIdParent(java.lang.String)
	 */
    
	public void setIdParent(String idParent) {
        this.idParent = idParent;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#setIdMuni(java.lang.String)
	 */
    
	public void setIdMuni(String idMuni) {
        this.idMuni = idMuni;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#addTerm(java.lang.String, java.lang.String)
	 */
    
	public void addTerm(String sLocale, String sTerm)
    {
        if ((sTerm==null) || (sLocale==null)) return;
        hDict.put(sLocale, sTerm);
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getTerm(java.lang.String)
	 */
    
	public String getTerm(String sLocale)
    {
        String sTerm= (String) hDict.get(sLocale);
        if (sTerm!=null) return sTerm;
        return getFirstTerm();
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#setTerm(java.lang.String, java.lang.String)
	 */
    
	public void setTerm(String sLocale, String sTerm)
    {
        if ((sTerm==null)||sTerm.length()==0)
            hDict.remove(sLocale);
        else
            hDict.put(sLocale,sTerm);
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#gethDict()
	 */
    
	public Hashtable gethDict() {
        return hDict;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#sethDict(java.util.Hashtable)
	 */
    
	public void sethDict(Hashtable hDict) {
        this.hDict = hDict;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#addHijo(com.geopista.server.administradorCartografia.DomainNode)
	 */
    
	public void addHijo(DomainNode node)
    {
        lHijos.add(node);
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#removeHijo(com.geopista.server.administradorCartografia.DomainNode)
	 */
    
	public void removeHijo(DomainNode node)
    {
        lHijos.remove(node);
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getFirstTerm()
	 */
    
	public String getFirstTerm()
    {
        if (hDict.size()>=1)
            return (String)hDict.elements().nextElement();
        else
            return null;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getlHijos()
	 */
    
	public ListaDomainNode getlHijos() {
        return lHijos;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#setlHijos(com.geopista.server.administradorCartografia.ListaDomainNode)
	 */
    
	public void setlHijos(ListaDomainNode lHijos) {
        this.lHijos = lHijos;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#toString()
	 */
    
	public String toString() {
            return getFirstTerm();
        }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getIdDomain()
	 */
    
	public String getIdDomain() {
           return idDomain;
       }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#setIdDomain(java.lang.String)
	 */
    
	public void setIdDomain(String idDomain) {
           this.idDomain = idDomain;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#getPatron()
	 */
    
	public String getPatron() {
        return patron;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#setPatron(java.lang.String)
	 */
    
	public void setPatron(String patron) {
        this.patron = patron;
    }
    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IDomainNode#clone()
	 */
    
	public Object clone()
        {
            DomainNode obj=null;
            try{
                   obj=(DomainNode)super.clone();
             }catch(CloneNotSupportedException ex){
             }
             obj.sethDict((Hashtable)this.hDict.clone());
             //no Clono los hijos
             return obj;
        }

}
