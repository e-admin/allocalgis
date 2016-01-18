/**
 * DxfLINE.java
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

package com.geopista.ui.plugin.io.dxf.reader;


import com.geopista.ui.plugin.io.dxf.math.Point3D;


/**
 *  Representing a DXF LINE.
 *
 *  @version 1.00beta0
 */
public class DxfLINE extends DxfEntity {
  protected Point3D[]     end = new Point3D[2]; /* end of line */

  /**
   *  Initialize.
   */
  public DxfLINE() {
    end[0] = new Point3D();
    end[1] = new Point3D();
  }


  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  10, 20, 30 and
   *  11, 21, 31
   *  @param  grpNr  group code
   *  @param  fval   the double value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, double fval) {
    switch (grpNr) {
    case 10:
    case 11:
    case 20:
    case 21:
    case 30:
    case 31:
      setCoord(end[grpNr%10], grpNr/10, fval);
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }


  /**
   *  Convert this LINE using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    converter.convert(this, dxf, collector);
  }

  /**
   *  Get the start point. Not cloned for effeciency reasons!
   *  @return start point
   */
  public final Point3D getStartPoint() {
    return end[0];
  }

  /**
   *  Get the end point. Not cloned for effeciency reasons!
   *  @return end point
   */
  public final Point3D getEndPoint() {
    return end[1];
  }
}


