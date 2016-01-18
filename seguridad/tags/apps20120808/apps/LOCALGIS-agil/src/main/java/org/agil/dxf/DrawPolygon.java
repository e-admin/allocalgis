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
 *  DrawPolygon represents flat drawable faces created from the DXF model.
 *  @see      DE.excape.quincunx.dxf.DrawAble
 *
 */
public class DrawPolygon extends DrawLines {
  private final static  Vector3D light   = new Vector3D(0.5f, 0.4f, 0.76811457f);
  private final static  double    ambient = 0.33f; // percentage of ambient light
  protected Vector3D    normal = null; // normal direction (if known)

  /**
   *
   */
  public DrawPolygon() {
	isClosed = true;
  }


  /**
   *  Construct with a given number of border points.
   *  @param  nr  number of border points
   */
  public DrawPolygon(int nr) {
	super(nr);
	isClosed = true;
  }


  /**
   *  Draw a polygon.
   *  @param  g          graphics context
   *  @param  nrPoints   number of points
   *  @param  px         x coords
   *  @param  py         y coords
   *  @param  pz         z coords
   *  @param  col        color
   */
  protected static void drawPolygon(Graphics g, int nrPoints, int[] px, int[] py, int[] pz, Color col) {
	for (int i = 0;   i < nrPoints;   i++) {
	  if (pz[i] <= 0) {
	return;			// don't draw
	  }
	}
	g.setColor(col);
	g.fillPolygon(px, py, nrPoints);
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public void draw(Graphics g, Matrix4D mat, DxfLAYER insertLayer,
		   DxfColorModel colors, short insertColor) {
	if (!DXFCanvas.calcFaces) {
	  super.draw(g, mat, insertLayer, colors, insertColor);
	}
	else {
	  // --- draw faces ---
	  //    System.out.println("g = "+g+", mat = "+mat);
	  if (!isVisible(insertLayer)) {
	return;
	  }
	  if (normal != null   &&
	  mat.mult(normal).mult(new Vector3D(0.0f, 0.0f, 1.0f)) >= 0.0f) {
	return;
	  }
	  if (nrPoints > 0) {
	short activeColor = calcColor(insertColor, insertLayer);
	int[] px = new int[nrPoints],
		  py = new int[nrPoints],
		  pz = new int[nrPoints];
	mat.transform(line, px, py, pz, nrPoints);

	if (DXFCanvas.calcLight   &&   normal != null) {
	  double cosAlpha = Math.abs(light.mult(normal));
	  double reduce   = (1.0f-ambient)*cosAlpha + ambient;
	  Color original = colors.getColor(activeColor);
	  Color dim      = new Color((int)(reduce*original.getRed()),
					 (int)(reduce*original.getGreen()),
					 (int)(reduce*original.getBlue()));
	  drawPolygon(g, nrPoints, px, py, pz, dim);
	}
	else {
	  drawPolygon(g, nrPoints, px, py, pz, colors.getColor(activeColor));
	}
	  }
	}
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public void transformBy(Matrix4D mat) {
	super.transformBy(mat);
	// calc determinant and switch normal, if det < 0
	if (normal != null) {
	  calcNormal(mat.mult(normal));
	}
  }

  /**
   *  Extrude in z direction.
   *  @param  dist      extrusion length
   *  @param  up        extrusion direction
   *  @return new DrawSet with extruded element
   */
  public DrawAble extrude(double dist, Vector3D up) {
	if (dist == 0) {
	  return this;
	}
	else {
	  boolean     right;
	  Vector3D    ex     = new Vector3D(dist*up.x, dist*up.y, dist*up.z);

	  // --- when extruding the normals have to point to the outside ---
	  if (normal == null) {
	calcNormal();
	  }
	  right = (normal.mult(ex) > 0.0f);
	  if (right) {
	normal.scale(-1.0f);
	  }

	  // --- creat a ladder ---
	  DrawSet     set    = new DrawSet(2+nrPoints);
	  DrawPolygon second = new DrawPolygon(nrPoints);

	  set.setLayer(layer);
	  set.setColor(color);
	  second.setLayer(layer);
	  second.setColor(color);

	  set.addDrawable(this);
	  for (int i = 0;   i < nrPoints;   i++) {
	second.addPoint(line[i].x+ex.x, line[i].y+ex.y, line[i].z+ex.z);
	  }
	  if (normal != null) {
	Vector3D norm = new Vector3D(normal);
	norm.scale(-1.0f);
	second.setNormal(norm);
	  }
	  set.addDrawable(second);


	  for (int i = 0;   i < nrPoints;   i++) {
	DrawPolygon conn = new DrawPolygon(4);   // connection
	conn.setLayer(layer);
	conn.setColor(color);

	conn.addPoint(line[i]);
	if (right) {
	  conn.addPoint(line[(i+1)%nrPoints]);
	  conn.addPoint(second.line[(i+1)%nrPoints]);
	  conn.addPoint(second.line[i]);
	}
	else {
	  conn.addPoint(second.line[i]);
	  conn.addPoint(second.line[(i+1)%nrPoints]);
	  conn.addPoint(line[(i+1)%nrPoints]);
	}
	conn.calcNormal();
	set.addDrawable(conn);
	  }

	  return set;
	}
  }


  /**
   *  Calc the normal direction automatically.
   *  The normal is global for the face, it's not calculated when
   *  there are not at least three points for this polygon and it
   *  is not calculated correctly if the face is not flat.
   */
  public void calcNormal() {
	if (nrPoints >= 3) {
	  int mid = 0;
	  do {
	int left  = (mid+nrPoints-1)%nrPoints;
	int right = (mid+nrPoints+1)%nrPoints;
	Vector3D one = line[left].minus(line[mid]);
	Vector3D two = line[right].minus(line[mid]);

	normal = two.cross(one);

	double len = normal.length();
	if (len != 0.0f) {
	  normal.scale(1.0f/len);
	}
	else {
	  normal = null;
	}
	  } while (normal == null   &&   ++mid < nrPoints);
	}
  }

  /**
   *  Calc the normal direction automatically.
   *  The normal is global for the face, it's not calculated when
   *  there are not at least three points for this polygon and it
   *  is not calculated correctly if the face is not flat.
   *  <p>
   *  The normal is switched if the given normal parameter is
   *  showing into another direction.
   *
   *  @param  norm  the normal of a neighbour face
   */
  public void calcNormal(Vector3D norm) {
	calcNormal();
	if (normal != null   &&   normal.mult(norm) < 0.0f) {
	  normal.scale(-1.0f);
	}
  }

  /**
   *  Set the normal.
   *  @param norm  normal to set
   */
  public void setNormal(Vector3D norm) {
	normal = new Vector3D(norm);
  }


  /**
   *  Get the normal.
   *  @return normal of this face
   */
  public Vector3D getNormal() {
	if (normal != null) {
	  return new Vector3D(normal);
	}
	else {
	  return null;
	}
  }
}










