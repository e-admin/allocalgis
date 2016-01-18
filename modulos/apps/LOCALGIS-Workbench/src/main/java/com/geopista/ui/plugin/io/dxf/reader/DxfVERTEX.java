/**
 * DxfVERTEX.java
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
 *  Representing a DXF VERTEX. VERTEX is an entity only used
 *  by the POLYLINE entity.
 *  @see DxfPOLYLINE
 *
 *  @version 1.00beta0
 */
public class DxfVERTEX extends DxfEntity {
  protected Point3D     position = new Point3D();           /* vertex pos [#10,20,30] */
  protected int         type;            /* type of vertex */
  protected double       startWidth = -1.0f, /* starting width [#40] */
                        endWidth = -1.0f;   /* ending width [#41] */
  protected double       rounding;        /* rounding [#42] */
  protected double       tangent;         /* tangent dir [#50] */
  protected int[]       index;           /* reference numbers [#71-74] */

  /* Vertex flags */
  public final static int FIT_POINT    = 0x01;
  public final static int TANGENT      = 0x02;
  public final static int LINE_FIT     = 0x08;
  public final static int CONTROL      = 0x10;
  public final static int LINE_3D      = 0x20;
  public final static int WEB_3D       = 0x40;
  public final static int NET_CONTROL  = 0x80;
  /* sets */
  public final static int APPROX       = FIT_POINT | LINE_FIT;

  /* special value */
  public final static int NO_INDEX     = 0;

  /* constant at least until AutoCAD12 */
  public final static int PFACEVMAX    = 4;

  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  10, 20, 30, 40 & 41
   *  and everything DxfEntity accepts.
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

    case 40:
      startWidth = fval;
      return true;

    case 41:
      endWidth = fval;
      return true;

    case 42:
      rounding = fval;
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }

  /**
   *  Take an int value for a given group code.
   *  Accepted for group codes
   *  70 -- 75
   *  and everything DxfEntity accepts
   *  @param  grpNr  group code
   *  @param  ival   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
    switch (grpNr) {
    case 70:
      type = ival;
      return true;

    case 71:
    case 72:
    case 73:
    case 74:
      if (index == null) {
	/* init */
	index = new int[PFACEVMAX];
	for (int i = 0;   i < PFACEVMAX;   i++) {
	  index[i] = NO_INDEX;
	}
      }
      index[grpNr-71] = ival;
      return true;

    default:
      return super.setGroup(grpNr, ival);
    }
  }

  /**
   *  Convert this VERTEX.
   *  <em>VERTEX should not be converted directly because they make no
   *  sense without their POLYLINE!</em>
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    if (DxfFile.DEBUG_LEVEL > 0) {
      System.err.println("OOPS. Trying to convert VERTEX!");
    }
  }


  /**
   *  Get the position. Not cloned for effeciency reasons!
   *  @return vertex position
   */
  public final Point3D getPosition() {
    return position;
  }

  /**
   *  Get the type flags.
   *  @return type flags
   */
  public final int getType() {
    return type;
  }

  /**
   *  Get the starting width.
   *  @return start width
   */
  public final double getStartWidth() {
    return startWidth;
  }

  /**
   *  Get the ending width.
   *  @return end width
   */
  public final double getEndWidth() {
    return endWidth;
  }

  /**
   *  Sets the start and the end width.
   *  For some obscure reason the first VERTEX in a POLYLINE
   *  has to overtake the settings from the POLYLINE entity.
   *  Sigh!
   *  @param  startWidth   start width to set
   *  @param  endWidth     end width to set
   */
  public void setWidths(double startWidth, double endWidth) {
    this.startWidth = startWidth;
    this.endWidth   = endWidth;
  }

  /**
   *  Sets the start width.
   *  @param  startWidth   start width to set
   */
  public void setStartWidth(double startWidth) {
    this.startWidth = startWidth;
  }

  /**
   *  Sets the end width.
   *  @param  endWidth   end width to set
   */
  public void setEndWidth(double endWidth) {
    this.endWidth = endWidth;
  }

  /**
   *  Get the rounding parameter.
   *  @return rounding parameter
   */
  public final double getRounding() {
    return rounding;
  }

  /**
   *  Get the tangent angle.
   *  @return tangent angle
   */
  public final double getTangent() {
    return tangent;
  }

  /**
   *  Get the reference numbers. Not cloned for efficiency reasons.
   *  @return array of reference numbers (always PFACEMAX=4),
   *          an index of 0 indicates an invalid reference
   */
  public final int[] getReferences() {
    return index == null  ?  new int[PFACEVMAX]  :  index;
  }
}


