/**
 * Dxf3DFACE.java
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


/**
 *  Representing a DXF 3DFACE.
 *  
 *  @version 1.00beta0
 */
public class Dxf3DFACE extends DxfSOLID {
  protected int      invisible;     /* invisible lines [#70] */


  /**
   *  Take an int value for a given group code.
   *  Accepted for group code
   *  70 and anything DxfSOLID accepts.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
    switch (grpNr) {
    case 70:
      invisible = ival;
      return true;

    default:
      return super.setGroup(grpNr, ival);
    }
  }


  /**
   *  Convert this 3DFACE using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile file, Object collector) {
    preConvert();
    converter.convert(this, file, collector);
  }


  /**
   *  Get the invisible line flags.
   *  @return invisible line flags
   */
  public final int getInvisible() {
    return invisible;
  }
}



