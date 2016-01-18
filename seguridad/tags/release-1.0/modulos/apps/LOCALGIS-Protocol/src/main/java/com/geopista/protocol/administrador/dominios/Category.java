package com.geopista.protocol.administrador.dominios;

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
 * Date: 30-sep-2004
 * Time: 10:16:34
 */
public class Category {

        private String id;
        private String idDes;
        private Hashtable hDict;
        public Category() {
            hDict= new Hashtable();
        }

        public Category(String id, String idDes) {
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
        public String toString()
        {
            return getFirstTerm();
        }
        public String print()
        {
             return ("\nCatetgoria: "+ this.getId() + " Vocable: "+this.getFirstTerm());
       }


}
