/**
 * DxfEntityCounter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */


package org.agil.dxf.reader;



import java.util.Enumeration;
import java.util.Hashtable;

import org.agil.dxf.utils.Counter;

/**
 *  Count DXF entities per type.
 *  Keep track of the numbers of entities for each type of entities.
 */
public final class DxfEntityCounter {
  private static     Class entityClass = new DxfEntity().getClass();

  private Hashtable  class2name  = new Hashtable(23);
  private Hashtable  counts      = new Hashtable(23);
  private int        allEntities = 0;

  /**
   *  Get the entity name for a DxfEntity class.
   *  @param  cl  dxf entity class
   */
  private String getDxfEntityName(Class cl) {
    if (cl == entityClass) {
      return null;
    }
    String className = cl.getName();
    int index = className.lastIndexOf(".Dxf");
    if (index == -1) {
      // oops, should do something, but what?
      return null;
    }
    return className.substring(index+4);
  }

  /**
   *  Add an entity.
   *  @param  ent entity to add
   */
  public void add(DxfEntity ent) {
    String name;
    Class cl = ent.getClass();

    name = (String)class2name.get(cl);

    if (name == null) {
      name = getDxfEntityName(cl);

      if (name == null) {
	return;
      }
      class2name.put(cl, name);
    }

    add(name);
  }


  /**
   *  Add an entity type by name.
   *  @param  name  entity name to add
   */
  public void add(String name) {
    Counter cnt = (Counter)counts.get(name);
    if (cnt == null) {
      cnt = new Counter(1);
      counts.put(name, cnt);
    }
    else {
      cnt.add1();
    }
  }


  /**
   *  Get the number of entities for this entity type.
   *  @param  ent entity type for which to get the count
   *  @return  count for this type of entity
   */
  public int getCount(DxfEntity ent) {
    Class cl = ent.getClass();

    String name = (String)class2name.get(cl);

    if (name == null) {
      name = getDxfEntityName(cl);
    }
    return   getCount(name);
  }


  /**
   *  Get the number of entities for this entity type.
   *  @param  name entity type for which to get the count
   *  @return  count for this type of entity
   */
  public int getCount(String name) {
    if (name == null) {
      return 0;
    }
    else {
      Counter cnt = (Counter)counts.get(name);
      
      return cnt == null   ?   0   :   cnt.getValue();
    }
  }


  /**
   *  Get an Enumeration of known entity names.
   *  @return enumeration of entity names with non-zero counters.
   */
  public Enumeration getEntityNames() {
    return counts.keys();
  }  
}

