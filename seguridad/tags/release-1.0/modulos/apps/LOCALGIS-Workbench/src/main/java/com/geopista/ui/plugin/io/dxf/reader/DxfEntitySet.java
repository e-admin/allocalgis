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

import java.util.*;

/** 
 *  A set of DXF entities. Needed for SECTION ENTITIES, BLOCK, INSERT,
 *  POLYLINE.
 *  
 *  @version    1.03beta0 (January 1999)
 */
public class DxfEntitySet implements DxfConvertable, DxfEntityCollector {
  protected Vector   entities = new Vector();      // array to hold entities 
  protected DxfEntityCounter counter = new DxfEntityCounter(); // counter for entities


  /**
   *  Convert this entity set using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    converter.convert(this, dxf, collector);
  }


  /**
   *  Add an entity to the set.
   *  @param  entity entity to add
   *  @return <code>true</code> if the entity is added<br>
   *          <code>false</code> if entity is a terminator
   */
  public boolean addEntity(DxfEntity entity) {
    if (entity instanceof DxfENDSEC) {
      // this is the end
      return false;
    }
    entities.addElement(entity);
    counter.add(entity);
    return true;
  }
  
  /**
   *  Get a BLOCK with the given name.
   *  @return the BLOCK or <code>null</code>
   */
  public DxfEntity getBlock(String name) {
    for (Enumeration e = entities.elements();   e.hasMoreElements();  ) {
      DxfEntity ent = (DxfEntity)e.nextElement();

      if (ent.isBlockNamed(name)) {
	return ent;
      }
    }
    /* nothing found */
    return null;
  }

  /**
   *  Get the number of entities collected here.
   *  @return number of entities
   */
  public final int getNrOfEntities() {
    return entities.size();
  }


  /**
   *  Return an enumeration of entities.
   *  @return enumeration of collected entities
   */
  public Enumeration getEntities() {
    return entities.elements();
  }

  /**
   *  Get a Enumeration of collected entity types.
   *  @return enumeration of collected entity types
   */
  public Enumeration getCollectedEntityTypes() {
    return counter.getEntityNames();
  }

  /**
   *  Get the number of entities for a given type.
   *  @param  entType entity type  (as got from getCollectedEntityTypes)
   *  @return number of entities of this type collected here
   */
  public int getNumberOf(String entType) {
    return counter.getCount(entType);
  }

  /**
   *  Get a special entity.
   *  @param  index  index of entity to return
   */
  public DxfEntity getEntity(int index) {
    return (DxfEntity)entities.elementAt(index);
  }
}


