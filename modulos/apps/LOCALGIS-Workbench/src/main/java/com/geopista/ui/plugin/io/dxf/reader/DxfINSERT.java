/**
 * DxfINSERT.java
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
 *  Representing a DXF INSERT entity.
 *  <em>INSERTs can be followed by ATTRIB entities but this
 *  is not implemented yet!</em>
 *
 *  @version 1.00beta0
 */
public class DxfINSERT extends DxfEntity {
  protected Point3D    insertPoint = new Point3D(); // point of insertion [#10,20,30]
  protected String     blockName;                   // name of inserted block [#2]
  protected double      scaleX = 1.0f,               // scaling factor in x [#41]
					   scaleY = 1.0f,               // scaling factor in y [#42]
					   scaleZ = 1.0f;               // scaling factor in z [#43]
  protected double      rotAngle;                    // rotation angle [#50]
  protected int        nrRows = 1,                  // # of rows [#71]
					   nrCols = 1;                  // # of columns [#72]
  protected double      rowDist,                     // distance between rows [#44]
			   colDist;                     // distance between columns [#45]
  protected int        attribFollows = 0;           // attribute follows flag [#66]


  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  10, 20, 30,
   *  41, 42, 43,
   *  44, 45, 50
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
	  setCoord(insertPoint, grpNr/10, fval);
	  return true;

	case 41:
	  scaleX = fval;
	  return true;

	case 42:
	  scaleY = fval;
	  return true;

	case 43:
	  scaleZ = fval;
	  return true;

	case 44:
	  rowDist = fval;
	  return true;

	case 45:
	  colDist = fval;
	  return true;

	case 50:
	  rotAngle = (double)((fval/180.0)*Math.PI);
	  return true;

	default:
	  return super.setGroup(grpNr, fval);
	}
  }


  /**
   *  Take an int value for a given group code.
   *  Accepted for group code
   *  66, 71, 72
   *  and anything DxfEntity accepts.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
	switch (grpNr) {
	case 66:
	  attribFollows = ival;
	  return true;

	case 71:
	  nrRows = ival;
	  return true;

	case 72:
	  nrCols = ival;
	  return true;

	default:
	  return super.setGroup(grpNr, ival);
	}
  }

  /**
   *  Take a string for a given group code.
   *  Accepted for group code
   *  2
   *  and anything DxfEntity accepts.
   *  @param  grpNr  group code
   *  @param  str    the string value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, String str) {
	switch (grpNr) {
	case 2:
	  blockName = str;
	  return true;

	default:    /* method of super class */
	  return super.setGroup(grpNr, str);
	}
  }


  /**
   *  Not used yet.
   */
  public boolean addEntity(DxfEntity entity) {
	//    System.out.println("DxfINSERT with follow flag = "+(attribFollows != 0));
	return (attribFollows != 0);
  }


  /**
   *  Convert this INSERT using the given DxfConverter.
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
   *  Get the inserted block name.
   *  @return block name
   */
  public final String getBlockName() {
	return blockName;
  }

  /**
   *  Get the scaling in X direction.
   *  @return scaling in X
   */
  public final double getScaleX() {
	return scaleX;
  }

  /**
   *  Get the scaling in Y direction.
   *  @return scaling in Y
   */
  public final double getScaleY() {
	return scaleY;
  }

  /**
   *  Get the scaling in Z direction.
   *  @return scaling in Z
   */
  public final double getScaleZ() {
	return scaleZ;
  }

  /**
   *  Get the rotation angle.
   *  @return rotation angle
   */
  public final double getRotation() {
	return rotAngle;
  }

  /**
   *  Get the number of rows.
   *  @return rumber of rows
   */
  public final int getRows() {
	return nrRows;
  }

  /**
   *  Get the number of columns.
   *  @return rumber of columns
   */
  public final int getColumns() {
	return nrCols;
  }

  /**
   *  Get the distance betweem rows.
   *  @return row distance
   */
  public final double getRowDistance() {
	return rowDist;
  }

  /**
   *  Get the distance betweem columns.
   *  @return column distance
   */
  public final double getColumnDistance() {
	return colDist;
  }

  /**
   *  Are the attributes following?
   *  @return attributes following
   */
  public final boolean attributesFollowing() {
	return attribFollows != 0;
  }
}


