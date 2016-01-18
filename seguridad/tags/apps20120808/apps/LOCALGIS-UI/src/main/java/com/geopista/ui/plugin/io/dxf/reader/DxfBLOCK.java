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


import com.geopista.ui.plugin.io.dxf.math.Point3D;


/**
 *  Representing a DXF BLOCK.
 *
 *  @version 1.00beta0
 */
public class DxfBLOCK extends DxfEntity implements DxfEntityCollector {
  protected String        blockName;    /* name of the block [#2] */
  protected String        typeName;     /* name of block type [#3] */
  protected String        external;     /* name of external [#1] */
  protected int           type;         /* type of block [#70] */
  protected Point3D       refPoint = new Point3D();     /* reference point [#10,20,30] */

  protected DxfEntitySet  entitySet = new DxfEntitySet();    /* entities in block */

  /**
   *  Take a string for a given group code.
   *  Accepted for group codes
   *  1, 2, 3
   *  and anything DxfEntity accepts.
   *  @param  grpNr  group code
   *  @param  str    the string value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, String str) {
    switch (grpNr) {
    case 1:
      external = str;
      return true;

    case 2:
      blockName = str;
      return true;

    case 3:
      typeName = str;
      return true;

    default:    /* method of super class */
      return super.setGroup(grpNr, str);
    }
  }

  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  10, 20, 30
   *  and anything dxFentity accepts.
   *  @param  grpNr  group code
   *  @param  fval   the double value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, double fval) {
    switch (grpNr) {
    case 10:
    case 20:
    case 30:
      setCoord(refPoint, grpNr/10, fval);
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }

  /**
   *  Take an int value for a given group code.
   *  Accepted for group code
   *  70
   *  and anything DxfEtity accepts.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
    if (grpNr == 70) {
      type = ival;
      return true;
    }
    else {
      return super.setGroup(grpNr, ival);
    }
  }


  /**
   *  Has this block the given name?
   *  @return the answer
   */
  public boolean isBlockNamed(String name) {
    return (blockName != null  &&  blockName.equals(name));
  }


  /**
   *  Add an entity to this block.
   *  @param  entity  entity to add
   *  @return <code>true</code> if entity is accepted<br>
   *          <code>false</code> if entity terminates this sequence
   *  @exception DxfException if entity is not acceptable
   */
  public boolean addEntity(DxfEntity entity) throws DxfException {
    if (entity instanceof DxfBLOCK) {
      throw new DxfException("err!Nested");
    }
    if (entity instanceof DxfENDBLK) {
      return false;
    }
    else {
      // all others are concatenated
      entitySet.addEntity(entity);
      return true;
    }
  }

  /**
   *  Convert this BLOCK using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    converter.convert(this, dxf, collector);
  }

  /**
   *  Get the block's name.
   *  @return block name
   */
  public final String getBlockName() {
    return blockName;
  }

  /**
   *  Get the type name.
   *  @return type name
   */
  public final String getTypeName() {
    return typeName;
  }

  /**
   *  Get the external file name.
   *  @return file name
   */
  public final String getExternal() {
    return external;
  }

  /**
   *  Get the type.
   *  @return block type
   */
  public final int getType() {
    return type;
  }

  /**
   *  Get the reference point. Not cloned for effeciency reasons!
   *  @return reference point
   */
  public final Point3D getReferencePoint() {
    return refPoint;
  }

  /**
   *  Get the entities. Not cloned for efficiency reasons!
   *  @return entities
   */
  public final DxfEntitySet getEntities() {
    return entitySet;
  }
}



