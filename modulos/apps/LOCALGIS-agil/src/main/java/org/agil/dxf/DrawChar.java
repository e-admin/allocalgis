/**
 * DrawChar.java
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

/**
 *  Information about the lines of a char.
 */
public class DrawChar {
  Point3D[][] lines;
  float       advance;
  int         maxLineLen = 0;
  int         nrLines = 0;

  /**
   *  Constructor. Create a character from the given lines
   *  and the advance.
   *  @param charlines  lines of the character as linenr, point, coord triples
   *  @param xadvance   advancement in x direction
   */
  public DrawChar(float[][][] charlines, float xadvance) {
    if (charlines != null   &&   charlines.length > 0) {
      lines = new Point3D[charlines.length][];
      
      for (int l = 0;   l < charlines.length;   l++) {
	int p;
	lines[l] = new Point3D[charlines[l].length];
	for (p = 0;   p < charlines[l].length;   p++) {
          lines[l][p] = new Point3D(charlines[l][p][0], 
				    charlines[l][p][1],
				    0f);
	}
	if (p > maxLineLen) {
	  maxLineLen = p;
	}
	nrLines += p-1;
      }
    }

    advance = xadvance;
  }

  /**
   *  Draw this char with the given transformation and color.
   *  @param  g      graphics context
   *  @param  mat    transformation to use
   *  @param  color  color to use
   */
  public void draw(Graphics g, Matrix4D mat, Color color) {
    if (lines != null) {
      int[] px = new int[maxLineLen],
	    py = new int[maxLineLen],
            pz = new int[maxLineLen];
      for (int l = 0;   l < lines.length;  l++) {
	mat.transform(lines[l], px, py, pz, lines[l].length);

	DrawLines.drawPoly(g, lines[l].length, px, py, pz, color);
      }
    }

    //    mat.translate(advance, 0f, 0f);   not here
  }

  /**
   *  Calculate the bounding box.
   *  @param min  minimum point so far
   *  @param max  maximum point so far
   *  @param mat  transfformation to use
   */
  public int calcBB(Point3D min, Point3D max, Matrix4D mat) {
    int nrPoints = 0;
    if (lines != null) {
      for (int l = 0;   l < lines.length;   l++) {
	nrPoints += lines.length;
	for (int i = 0;   i < lines[l].length;   i++) {
	  Point3D p = (mat == null) ? lines[l][i] : mat.mult(lines[l][i]);
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
      }
    }
    // return nr of points
    return nrPoints;
  }

}


