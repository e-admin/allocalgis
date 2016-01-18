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


package com.geopista.ui.plugin.io.dxf.reader;



import java.util.Hashtable;
import java.util.Enumeration;

import com.geopista.ui.plugin.io.dxf.utils.Counter;

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
   *  @param  cl  geopistadxf entity class
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

