/**
 * DxfDIMENSION.java
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
 *  Representing a DXF DIMENSION.
 *
 *  @version 1.00beta0
 */
public class DxfDIMENSION extends DxfEntity {
  protected Point3D    insertPoint = new Point3D(); // point of insertion [#12,22,32]
  protected Point3D    reference0  = null;          // reference point 0 [#10,20,30]
  protected Point3D    textMiddle  = null;          // middle of text [#11,21,31]
  protected String     blockName   = null;          // name of block [#2]
  protected String     style       = null;          // name of style [#3]
  protected short      type        = 0;             // type of dimension [#70]
  protected String     text        = null;          // dimension text [#1]
  protected Point3D    reference3  = null;          // reference point 3 [#13,23,33]
  protected Point3D    reference4  = null;          // reference point 4 [#14,24,34]
  protected Point3D    reference5  = null;          // reference point 5 [#15,25,35]
  protected Point3D    reference6  = null;          // reference point 6 [#16,26,36]
  protected double      lineLength  = 0f;            // length of dim line [#40]
  protected double      rotation    = 0f;            // rotation angle of dim line [#50]
  protected double      horizontal  = 0f;            // horizontal dir [#51]
  protected double      helpRot     = 0f;            // rotation angle of help lines [#52]
  protected double      textRot     = 0f;            // rotation angle of text [#53]

  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  10, 20, 30,
   *  11, 21, 31,
   *  12, 22, 32,
   *  13, 23, 33,
   *  14, 24, 34,
   *  15, 25, 35,
   *  16, 26, 36,
   *  40, 50, 51, 52, 53
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
      if (reference0 == null) {
	reference0 = new Point3D();
      }
      setCoord(reference0, grpNr/10, fval);
      return true;

    case 11:
    case 21:
    case 31:
      if (textMiddle == null) {
	textMiddle = new Point3D();
      }
      setCoord(textMiddle, grpNr/10, fval);
      return true;

    case 12:
    case 22:
    case 32:
      setCoord(insertPoint, grpNr/10, fval);
      return true;

    case 13:
    case 23:
    case 33:
      if (reference3 == null) {
	reference3 = new Point3D();
      }
      setCoord(reference3, grpNr/10, fval);
      return true;

    case 14:
    case 24:
    case 34:
      if (reference4 == null) {
	reference4 = new Point3D();
      }
      setCoord(reference4, grpNr/10, fval);
      return true;

    case 15:
    case 25:
    case 35:
      if (reference5 == null) {
	reference5 = new Point3D();
      }
      setCoord(reference5, grpNr/10, fval);
      return true;

    case 16:
    case 26:
    case 36:
      if (reference6 == null) {
	reference6 = new Point3D();
      }
      setCoord(reference6, grpNr/10, fval);
      return true;

    case 40:
      lineLength = fval;
      return true;

    case 50:
      rotation = (double)((fval/180.0)*Math.PI);
      return true;

    case 51:
      horizontal = (double)((fval/180.0)*Math.PI);
      return true;

    case 52:
      helpRot = (double)((fval/180.0)*Math.PI);
      return true;

    case 53:
      textRot = (double)((fval/180.0)*Math.PI);
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }


  /**
   *  Take an int value for a given group code.
   *  Accepted for group code
   *  70
   *  and anything DxfEntity accepts.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
    switch (grpNr) {
    case 70:
      type = (short)ival;
      return true;

    default:
      return super.setGroup(grpNr, ival);
    }
  }

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
      text = str;
      return true;

    case 2:
      blockName = str;
      return true;

    case 3:
      style = str;
      return true;

    default:
      return super.setGroup(grpNr, str);
    }
  }


  /**
   *  Convert this DIMENSION using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    converter.convert(this, dxf, collector);
  }

  /**
   *  Get the insertion point. Not cloned for effeciency reasons!
   *  @return insertion point
   */
  public final Point3D getInsertPoint() {
    return insertPoint;
  }

  /**
   *  Get the reference point 0. Not cloned for effeciency reasons!
   *  @return reference point 0
   */
  public final Point3D getReferencePoint0() {
    return reference0;
  }

  /**
   *  Get the text middle. Not cloned for effeciency reasons!
   *  @return text middle point
   */
  public final Point3D getTextMiddle() {
    return textMiddle;
  }

  /**
   *  Get the block name.
   *  @return block name
   */
  public final String getBlockName() {
    return blockName;
  }

  /**
   *  Get the style name.
   *  @return style name
   */
  public final String getStyleName() {
    return style;
  }

  /**
   *  Get the type
   *  @return type
   */
  public final short getType() {
    return type;
  }

  /**
   *  Get the dimension text.
   *  @return dimension text
   */
  public final String getText() {
    return text;
  }

  /**
   *  Get the reference point 3. Not cloned for effeciency reasons!
   *  @return reference point 3
   */
  public final Point3D getReferencePoint3() {
    return reference3;
  }

  /**
   *  Get the reference point 4. Not cloned for effeciency reasons!
   *  @return reference point 4
   */
  public final Point3D getReferencePoint4() {
    return reference4;
  }

  /**
   *  Get the reference point 5. Not cloned for effeciency reasons!
   *  @return reference point 5
   */
  public final Point3D getReferencePoint5() {
    return reference5;
  }

  /**
   *  Get the reference point 6. Not cloned for effeciency reasons!
   *  @return reference point 6
   */
  public final Point3D getReferencePoint6() {
    return reference6;
  }

  /**
   *  Get the dimension line length.
   *  @return line length
   */
  public final double getLineLength() {
    return lineLength;
  }

  /**
   *  Get the rotation angle.
   *  @return rotation angle
   */
  public final double getRotation() {
    return rotation;
  }

  /**
   *  Get the horizontal direction angle.
   *  @return horizontal direction angle
   */
  public final double getHorizontal() {
    return horizontal;
  }

  /**
   *  Get the help line rotation angle.
   *  @return rotation angle
   */
  public final double getHelpRotation() {
    return helpRot;
  }

  /**
   *  Get the text rotation angle.
   *  @return rotation angle
   */
  public final double getTextRotation() {
    return textRot;
  }

}
