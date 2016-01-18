/**
 * DxfPOINT.java
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

import org.agil.dxf.math.Point3D;


/**
 *  Representing a DXF POINT.
 *
 *  @version 1.00beta0
 */
public class DxfPOINT extends DxfEntity {
  protected Point3D     position = new Point3D();   /* position of point [#10,20,30] */
  protected double       flatDir;    /* angle of x axis [#50] */


  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  10, 20, 30 & 50
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
      setCoord(position, grpNr/10, fval);
      return true;

    case 50:
      flatDir = (double)((fval/180.0)*Math.PI);
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }


  /**
   *  Convert this POINT using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    converter.convert(this, dxf, collector);
  }

  /**
   *  Get the point's position. Not cloned for efficiency reasons!
   *  @return  point position
   */
  public final Point3D getPosition() {
    return position;
  }

  /**
   *  Return the X direction angle.
   *  @return X direction angle
   */
  public double getXDirection() {
    return flatDir;
  }
}





