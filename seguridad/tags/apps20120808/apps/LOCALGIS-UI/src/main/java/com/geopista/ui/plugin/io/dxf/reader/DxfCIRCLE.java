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
 *  Representing a DXF CIRCLE.
 *
 *  @version 1.00beta0
 */
public class DxfCIRCLE extends DxfEntity {
  protected Point3D          center = new Point3D();   /* position of center [#10,20,30] */
  protected double            radius;    /* radius [#40] */


  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  10, 20, 30, 40
   *  and anything DxfEntity accepts.
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
      setCoord(center, grpNr/10, fval);
      return true;

    case 40:
      radius = fval;
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }


  /**
   *  Convert this CIRCLE using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    converter.convert(this, dxf, collector);
  }

  /**
   *  Get the radius.
   *  @return radius
   */
  public double getRadius() {
    return radius;
  }

  /**
   *  Get the center. Not cloned for effeciency reasons!
   *  @return center point
   */
  public Point3D getCenter() {
    return center;
  }
}

