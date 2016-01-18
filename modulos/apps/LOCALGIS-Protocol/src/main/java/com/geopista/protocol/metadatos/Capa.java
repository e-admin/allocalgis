/**
 * Capa.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.metadatos;

import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 21-sep-2004
 * Time: 17:30:51
 */
public class Capa {
       private String id;
    private String idDes;
    private Hashtable hDict;
    private String idAcl;

    public Capa() {
        hDict= new Hashtable();
    }

    public Capa(String id, String idDes) {
        this.id = id;
        this.idDes = idDes;
        hDict= new Hashtable();
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
    public String getFirstTerm()
    {
         if (hDict.size()>=1)
              return (String)hDict.elements().nextElement();
         else
              return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdDes() {
        return idDes;
    }

    public void setIdDes(String idDes) {
        this.idDes = idDes;
    }

    public Hashtable gethDict() {
        return hDict;
    }

    public void sethDict(Hashtable hDict) {
        this.hDict = hDict;
    }

    public String getIdAcl() {
        return idAcl;
    }

    public void setIdAcl(String idAcl) {
        this.idAcl = idAcl;
    }
}
