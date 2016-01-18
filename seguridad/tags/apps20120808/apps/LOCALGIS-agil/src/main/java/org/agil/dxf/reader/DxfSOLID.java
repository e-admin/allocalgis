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

import org.agil.dxf.math.*;


/**
 *  Representing a DXF SOLID.
 *
 *  @verson 1.00beta0 (January 1999)
 */
public class DxfSOLID extends DxfEntity {
  protected Point3D[]      corner = new Point3D[4]; /* corners [#10,20,30,11,21,31,12,22,32,13,23,33] */
  /**
   *  Used as a workaround for buggy DXF output filters which don't set
   *  all points to indicate that there are not all 4 points used.
   *  Instead the surplus points had to be set to the same value as
   *  the last.
   */
  protected short          maxPointUsed = -1;

  /**
   *  Initializes the 4 corner points.
   */
  public DxfSOLID() {
    super();
    corner[0] = new Point3D();
    corner[1] = new Point3D();
    corner[2] = new Point3D();
    corner[3] = new Point3D();
  }


  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  10, 20, 30,
   *  11, 21, 31,
   *  12, 22, 32,
   *  13, 23, 33
   *  and everything DxfEntity accepts.
   *  @param  grpNr  group code
   *  @param  fval   the double value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, double fval) {
    switch (grpNr) {
    case 10:
    case 11:
    case 12:
    case 13:
    case 20:
    case 21:
    case 22:
    case 23:
    case 30:
    case 31:
    case 32:
    case 33:
      setCoord(corner[grpNr%10], grpNr/10, fval);
      if (grpNr%(short)10 > maxPointUsed) {
	maxPointUsed = (short)(grpNr%(short)10);
      }
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }


  /**
   *  Take an int value for a given group code.
   *  Accepted for any group code
   *  DxfEntity accepts.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
    return super.setGroup(grpNr, ival);
  }


  /**
   *  Run this before conversion.
   *  It tests wether all points are used (some creators don't
   *  do that erroneously) and normalizes otherwise.
   */
  protected void preConvert() {
    if (maxPointUsed < 3) {
      if (DxfFile.DEBUG_LEVEL > 0) {
	System.out.println("Working around too view points in SOLID/3DFACE");
      }
      if (maxPointUsed > 0) {
	for (int p = maxPointUsed+1;   p < corner.length;   p++) {
	  corner[p] = corner[maxPointUsed];
	}
	maxPointUsed = 3;	// to avoid multiple invocations
      }
    }
  }


  /**
   *  Convert this SOLID using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    preConvert();
    converter.convert(this, dxf, collector);
  }


  /**
   *  Get the corners. Not cloned for efficiency reasons.
   *  @return the corners (always an array of 4 points, don't change!)
   */
  public final Point3D[] getCorners() {
    return corner;
  }
}


