/**
 * DrawLines.java
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

import java.awt.Color;
import java.awt.Graphics;

import org.agil.dxf.math.Matrix4D;
import org.agil.dxf.math.Point3D;
import org.agil.dxf.math.Vector3D;
import org.agil.dxf.reader.DxfLAYER;


/**
 *  DrawLines represents the drawable lines builded from the DXF model.
 *  @see      DE.excape.quincunx.dxf.DrawAble
 *
 */
public class DrawLines extends DrawAble {
  /**
   *  AWT has problems with lines which reach far outside the window.
   *  If <code>CLIP_LINES</code> is set to <code>true</code> we try a
   *  simple-minded clipping algorithm here. This slows down execution
   *  a little bit in most cases, but may even speed up it when we
   *  are zoomed in and most lines don't need to be drawn.
   */
  private final static boolean CLIP_LINES = true;

  /** Vertices of the line in model coordinates. */
  protected Point3D[]     line;// the points of the line
  /** Number of vertices in the line. */
  protected int           nrPoints;// number of points in the line
  /** Is the line closed? */
  protected boolean       isClosed;        // is the line closed?

  /**
   *
   */
  public DrawLines() {
  }

  /**
   *  Construct with a given number of points.
   *  @param  nr   number of points
   */
  public DrawLines(int nr) {
	if (nr > 0) {
	  line = new Point3D[nr];
	}
  }

  /**
   *  Draw a simple line.
   *  @param  g      graphics context
   *  @param  x1     x coord of point 1
   *  @param  y1     y coord of point 1
   *  @param  z1     z coord of point 1
   *  @param  x2     x coord of point 2
   *  @param  y2     y coord of point 2
   *  @param  z2     z coord of point 2
   */
  protected static void drawLine(Graphics g, int x1, int y1, int z1, int x2, int y2, int z2) {
	if (z1 < 0   ||   z2 < 0) {
	  // Clipping necessary
	  if (z1 < 0   &&   z2 < 0) {
	// nothing to do
	  }
	  else {
	/* -----------------
	if (z1 < 0) {
	  float f = ((float) z2)/(z2-z1); // scaling factor
	  x1 = x2 + (int)((x1 - x2)*f);
	  y1 = y2 + (int)((y1 - y2)*f);
	}
	else {
	  float f = ((float) z1)/(z1-z2); // scaling factor
	  x2 = x1 + (int)((x2 - x1)*f);
	  y2 = y1 + (int)((y2 - y1)*f);
	}
	g.drawLine(x1, y1, x2, y2);
	------------------- */
	  }
	}
	else {
	  if (CLIP_LINES) {

		java.awt.Rectangle r = g.getClipBounds();
		if (r == null){
			//System.out.println("El bounds es null para drawLines");
		}else{
				int xmin = r.x;
				int ymin = r.y;
				int xmax = r.x + r.width;
				int ymax = r.y + r.height;

				if ((x1 < xmin  &&  x2 < xmin)  ||
					(y1 < ymin  &&  y2 < ymin)  ||
					(x1 > xmax  &&  x2 > xmax)  ||
					(y1 > ymax  &&  y2 > ymax)) {
				  // empty
				}else {
					g.drawLine(x1, y1, x2, y2);
				}
		  }
		}
	}
  }

  /**
   *  Draw a polyline.
   *  @param  g          graphics context
   *  @param  nrPoints   number of points
   *  @param  px         x coords
   *  @param  py         y coords
   *  @param  pz         z coords
   *  @param  col        color
   *
   * Este metodo se la pega cuando es llamado desde Netscape
   * Da un NullPointException
   *
   */
  public static void drawPoly(Graphics g, int nrPoints, int[] px, int[] py, int[] pz, Color col) {



		boolean drawSingle = false;
		int xmin = 0;
		int xmax = 0;
		int ymin = 0;
		int ymax = 0;


		if (CLIP_LINES) {

		  java.awt.Rectangle r = g.getClipBounds();
		  if (r == null){
			//  System.out.println("El bounds es NULL");
		  }else{

			  xmin = r.x;
			  ymin = r.y;
			  xmax = r.x + r.width;
			  ymax = r.y + r.height;
		  }
		}


		for (int i = 0;   i < nrPoints;   i++) {

			if (pz[i] <= 0) {
				drawSingle = true;
				break;
			}

			if (CLIP_LINES) {
				if ((px[i] < xmin)  ||(py[i] < ymin)  || (px[i] > xmax)  || (py[i] > ymax)) {
					  // empty
					  drawSingle = true;
					  break;
				}
			}
		}

		g.setColor(col);
		if (drawSingle) {
		  /* draw segments */
		  for (int i = nrPoints-1;   i > 0;   i--) {
			drawLine(g, px[i], py[i], pz[i], px[i-1], py[i-1], pz[i-1]);
		  }
		}
		else {
		  /* draw fast as polyline */
		  g.drawPolyline(px, py, nrPoints);
		}
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public void draw(Graphics g, Matrix4D mat, DxfLAYER insertLayer,
		   DxfColorModel colors, short insertColor) {
	//    System.out.println("g = "+g+", mat = "+mat);
	if (!isVisible(insertLayer)) {
	  return;
	}
	if (nrPoints > 0) {
	  short activeColor = calcColor(insertColor, insertLayer);
	  int[] px = new int[nrPoints + (isClosed ? 1 : 0)],
		py = new int[nrPoints + (isClosed ? 1 : 0)],
		pz = new int[nrPoints + (isClosed ? 1 : 0)];
	  mat.transform(line, px, py, pz, nrPoints);

	  if (isClosed) {
	px[nrPoints] = px[0];
	py[nrPoints] = py[0];
	pz[nrPoints] = pz[0];
	drawPoly(g, nrPoints+1, px, py, pz, colors.getColor(activeColor));
	//	  DrawLine(g, tv[0], tv[1], tv[2], tv[3*nrPoints-3], tv[3*nrPoints-2], tv[3*nrPoints-1], colors.getColor(activeColor));
	  }
	  else {
	drawPoly(g, nrPoints, px, py, pz, colors.getColor(activeColor));
	  }
	}
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public void draw(Graphics g, Matrix4D mat, DxfLAYER insLayer,
		   DxfColorModel colors, short insColor, int nr, int maxNr) {
	if (nr == 0) {
	  draw(g, mat, insLayer, colors, insColor);
	}
  }

  /**
   *  Add another pount to the line.
   *  @param  pt    new point
   */
  public void addPoint(Point3D pt) {
	addPoint(pt.x, pt.y, pt.z);
  }

  /**
   *  Add another pount to the line.
   *  @param  x     x coord of new point
   *  @param  y     y coord of new point
   *  @param  z     z coord of new point
   */
  public void addPoint(double x, double y, double z) {
	if (line == null) {
	  // --- init ---
	  line = new Point3D[2];
	}
	else if (nrPoints == line.length) {
	  // --- expand ---
	  Point3D[]  nLine = new Point3D[2*line.length];
	  System.arraycopy(line, 0, nLine, 0, line.length);
	  line = nLine;
	}
	line[nrPoints++] = new Point3D(x, y, z);
  }

  /**
   *  Close the line.
   */
  public void close() {
	if (nrPoints > 0) {
	  isClosed = true;
	}
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public int calcBB(Point3D min, Point3D max, Matrix4D mat,
			DxfLAYER insertLayer, boolean onlyVisible) {
	if (onlyVisible  &&  !isVisible(insertLayer)) { // is invisible?
	  return 0;
	}

	for (int i = 0;   i < nrPoints;   i++) {
	  Point3D p = (mat == null) ? line[i] : mat.mult(line[i]);
	  if (p.x < min.x) {
	min.x = p.x;
	  }
	  if (p.y < min.y) {
	min.y = p.y;
	  }
	  if (p.z < min.z) {
	min.z = p.z;
	  }
	  if (p.x > max.x) {
	max.x = p.x;
	  }
	  if (p.y > max.y) {
	max.y = p.y;
	  }
	  if (p.z > max.z) {
	max.z = p.z;
	  }
	}
	// return nr of lines or 1, if point
	return (nrPoints > 1) ? nrPoints-(isClosed ? 0 : 1) : nrPoints;
  }

  /**
   *  @see de.escape.quincunx.dxf.DrawAble
   */
  public void transformBy(Matrix4D mat) {
	for (int i = 0;   i < nrPoints;   i++) {
	  mat.transform(line[i]);
	}
  }

  /**
   *  Extrude in arbitrary direction.
   *  @param  dist      extrusion length
   *  @param  up        extrusion direction
   *  @return new DrawSet with extruded element
   */
  public DrawAble extrude(double dist, Vector3D up) {
	if (dist == 0) {
	  return this;
	}
	else {
	  // --- create a ladder ---
	  Vector3D  ex     = new Vector3D(dist*up.x, dist*up.y, dist*up.z);
	  DrawSet   set    = new DrawSet(2+nrPoints);
	  DrawLines second = new DrawLines(nrPoints);

	  set.setLayer(layer);
	  set.setColor(color);
	  second.setLayer(layer);
	  second.setColor(color);

	  set.addDrawable(this);
	  for (int i = 0;   i < nrPoints;   i++) {
	second.addPoint(line[i].x+ex.x, line[i].y+ex.y, line[i].z+ex.z);

	DrawLines conn = new DrawLines(2);   // connection
	conn.setLayer(layer);
	conn.setColor(color);
	conn.addPoint(line[i]);
	conn.addPoint(second.line[i]);
	set.addDrawable(conn);
	  }
	  if (isClosed) {
	second.close();
	  }
	  set.addDrawable(second);

	  return set;
	}
  }
}

