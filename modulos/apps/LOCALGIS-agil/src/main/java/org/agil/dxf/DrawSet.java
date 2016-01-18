/**
 * DrawSet.java
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


package org.agil.dxf;

import java.awt.Graphics;

import org.agil.dxf.math.Matrix4D;
import org.agil.dxf.math.Point3D;
import org.agil.dxf.math.Vector3D;
import org.agil.dxf.reader.DxfLAYER;


/**
 *  A set of DrawAbles.
 *  @see  de.escape.quincunx.dxf.DrawAble
 */
public class DrawSet extends DrawAble {
  Matrix4D      trafo;         // own trafo
  DrawAble[]    drawables;     // drawables
  int           nrDrawables;   // nr of drawables

  /**
   *  @param  mat   trafo matrix for this set
   *  @param  nr    number of drawables expected
   */
  public DrawSet(Matrix4D mat, int nr) {
	trafo     = mat;
	if (nr > 0) {
	  drawables = new DrawAble[nr];
	}
  }

  /**
   *
   */
  public DrawSet() {
  }

  /**
   *  @param  mat   trafo matrix for this set
   */
  public DrawSet(Matrix4D mat) {
	trafo = mat;
  }

  /**
   *  @param  nr    number of drawables expected
   */
  public DrawSet(int nr) {
	if (nr > 0) {
	  drawables = new DrawAble[nr];
	}
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public void draw(Graphics g, Matrix4D mat, DxfLAYER insLayer,
		   DxfColorModel colors, short insColor) {
	// System.out.println("nrDrawables = "+nrDrawables);
	Matrix4D    tmat;
	if (trafo == null) {
	  tmat = mat;
	}
	else {
	  tmat = new Matrix4D(trafo);
	  if (mat != null) {
	tmat.multLeftBy(mat);
	  }
	}

	for (int i = 0;   i < nrDrawables;   i++) {
	  drawables[i].draw(g, tmat, (layer == null || layer.isLayer0()) ? insLayer : layer,
			colors, (color != -1) ? color : insColor);
	}
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public void draw(Graphics g, Matrix4D mat, DxfLAYER insLayer,
		   DxfColorModel colors, short insColor, int nr, int maxNr) {
	Matrix4D    tmat;
	if (trafo == null) {
	  tmat = mat;
	}
	else {
	  tmat = new Matrix4D(trafo);
	  if (mat != null) {
	tmat.multLeftBy(mat);
	  }
	}

	for (int i = 0;   i < nrDrawables;   i++) {
	  drawables[i].draw(g, tmat, (layer == null  ||  layer.isLayer0()) ? insLayer : layer,
			colors, (color != -1) ? color : insColor, nr, maxNr);

	  if (--nr < 0) {
	nr = maxNr;
	  }
	}
  }

  /**
   *  Add a drawable to the set.
   *  @param  d   drawable to add
   */
  public void addDrawable(DrawAble d) {
	if (d == null) {
	  // --- no need to go any further ---
	  return;
	}
	if (drawables == null) {
	  // --- init ---
	  drawables = new DrawAble[4];
	}
	else if (nrDrawables == drawables.length) {
	  // --- expand ---
	  DrawAble[] newd = new DrawAble[2*drawables.length];
	  System.arraycopy(drawables, 0, newd, 0, drawables.length);
	  drawables = newd;
	}
	// --- add ---
	drawables[nrDrawables++] = d;
  }

  /**
   *  Set the transformation.
   *  @param  mat   transformation matrix
   */
  public void setTrafo(Matrix4D mat) {
	trafo = mat;
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public int calcBB(Point3D min, Point3D max, Matrix4D mat,
			DxfLAYER insertLayer, boolean onlyVisible)
  {
	if (onlyVisible  &&  !isVisible(insertLayer)) { // is invisible?
	  return 0;
	}

	Matrix4D  tmp;
	int       nrLines = 0;

	if (mat == null) {
	  tmp = trafo;
	}
	else if (trafo == null) {
	  tmp = mat;
	}
	else {
	  tmp = mat.mult(trafo);
	}

	for (int i = 0;   i < nrDrawables;   i++) {
	  nrLines += drawables[i].calcBB(min, max, tmp,
					 (layer == null  ||  layer.isLayer0()) ? insertLayer : layer,
					 onlyVisible);
	}

	return nrLines;
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public void transformBy(Matrix4D mat) {
	if (trafo != null) {
	  trafo.multLeftBy(mat);
	}
	else {
	  trafo = mat;
	}
  }

  /**
   *  Extrude in arbitrary direction.
   *  @param  dist      extrusion length
   *  @param  up        extrusion direction
   *  @return new DrawSet with extruded element
   */
  public DrawAble extrude(double dist, Vector3D up) {
	if (DXFCanvas.debugLevel > 0) {
	  System.err.println("Cannot extrude DrawSet...");
	}
	return this;
  }

}




