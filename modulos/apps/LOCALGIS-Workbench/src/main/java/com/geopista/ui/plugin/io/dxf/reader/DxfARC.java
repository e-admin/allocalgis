/**
 * DxfARC.java
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



/**
 *  Representing a DXF ARC.
 *
 *  @version 1.00beta0
 */
public class DxfARC extends DxfCIRCLE {
  protected double      startAngle, /* starting angle */
                       endAngle;   /* ending angle */

  /**
   *  Take a float value for a given group code.
   *  Accepted for group codes
   *  50, 51
   *  and anything DxFCIRCLE accepts.
   *  @param  grpNr  group code
   *  @param  fval   the float value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, double fval) {
    switch (grpNr) {
    case 50:
      startAngle = (double)(fval*Math.PI/180.0);
      return true;

    case 51:
      endAngle = (double)(fval*Math.PI/180.0);
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }

  /**
   *  Convert this ARC using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    converter.convert(this, dxf, collector);
  }

  /**
   *  Get the start angle.
   *  @return  start angle
   */
  public final double getStartAngle() {
    return startAngle;
  }

  /**
   *  Get the end angle.
   *  @return  end angle
   */
  public final double getEndAngle() {
    return endAngle;
  }
}


