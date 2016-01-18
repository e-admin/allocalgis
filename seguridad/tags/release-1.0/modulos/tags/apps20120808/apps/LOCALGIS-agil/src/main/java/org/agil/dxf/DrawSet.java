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
import java.awt.Color;

import org.agil.*;
import org.agil.dxf.math.*;
import org.agil.dxf.reader.*;


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




