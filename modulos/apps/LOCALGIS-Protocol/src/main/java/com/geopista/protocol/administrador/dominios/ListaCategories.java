/**
 * ListaCategories.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador.dominios;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 30-sep-2004
 * Time: 10:24:48
 */
public class ListaCategories {
    private Hashtable hCat;
       public ListaCategories() {
           hCat=new Hashtable();
       }

      public void setCat(Hashtable hCat) {
           this.hCat = hCat;
       }
       public void add(Category category)
       {
           hCat.put(category.getId(),category);
       }

       public Category get(String sId)
       {
           return (Category)this.hCat.get(sId);
       }


      public synchronized void remove(String key)
      {
          hCat.remove(key);
    }

       public Hashtable getCat() {
               return hCat;
       }
        public String print()
       {
           StringBuffer sbAux= new  StringBuffer();
           for (Enumeration e=hCat.elements();e.hasMoreElements();)
           {
               Category aux= (Category)e.nextElement();
               sbAux.append(aux.print());
           }
           return sbAux.toString();
       }

}
