/**
 * Point3D.java
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


package org.agil.dxf.math;

/**
 *  3dim double point. A point is transformed different than a vector.
 *  The class is now declared final to allow a more aggresive optimization.
 *
 *  @see       dxfviewer.math.Vector3D;
 *
 *  @version   1.10, 01/13/99
 */
public final class Point3D {
  public double   x, y, z;     // coordinates, allowing direct access

  /**
   *
   */
  public Point3D() {
  }

  /**
   *  Copy constructor.
   *  @param  p    point to copy
   */
  public Point3D(Point3D p) {
	x = p.x;
	y = p.y;
	z = p.z;
  }

  /**
   *  @param   xx    x coord
   *  @param   yy    y coord
   *  @param   zz    z coord
   */
  public Point3D(double xx, double yy, double zz) {
	x = xx;
	y = yy;
	z = zz;
  }

  /**
   *  Scale.
   *  @param   f     scaling factor
   */
  public void scale(double f) {
	if (f != 1f) {
	  x *= f;
	  y *= f;
	  z *= f;
	}
  }

  /**
   *  Add a vector.
   *  @param  v      vector to add
   */
  public void add(Vector3D v) {
	x += v.x;
	y += v.y;
	z += v.z;
  }

  /**
   *  Get sum with vector.
   *  @param  v     vector to add
   *  @return this+v
   */
  public Point3D plus(Vector3D v) {
	Point3D ret = new Point3D(this);
	ret.add(v);
	return ret;
  }

  /**
   *  Substract a vector.
   *  @param  v     vector to substract
   */
  public void sub(Vector3D v) {
	x -= v.x;
	y -= v.y;
	z -= v.z;
  }

  /**
   *  Get difference with vector.
   *  @param  v     vector to substract
   *  @return this-v
   */
  public Point3D minus(Vector3D v) {
	Point3D ret = new Point3D(this);
	ret.sub(v);
	return ret;
  }

  /**
   *  Get difference with point.
   *  @param  p     point to substract
   *  @return this-p
   */
  public Vector3D minus(Point3D p) {
	Vector3D ret = new Vector3D(this);
	ret.sub(new Vector3D(p));
	return ret;
  }

  /**
   *  Output.
   *  @return  output string
   */
  public String toString() {
	return new String(new StringBuffer().append("[").append(x).append(",").append(y).append(",").append(z).append("]"));
  }
}


