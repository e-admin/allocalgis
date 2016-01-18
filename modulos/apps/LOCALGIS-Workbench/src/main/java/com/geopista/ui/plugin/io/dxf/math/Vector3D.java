/**
 * Vector3D.java
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

package com.geopista.ui.plugin.io.dxf.math;

/**
 *  3dim double vector. A vector is transformed different than a point.
 *  The class is now declared final to allow a more aggresive optimization.
 *
 *  @see       dxfviewer.math.Point3D;
 *
 *  @version   1.10, 01/13/99
 */
public final class Vector3D {
  public double x, y, z;       // coordinates, allowing direct access

  /**
   *
   */
  public Vector3D() {
  }

  /**
   *  Copy constructor.
   *  @param  v    vector to copy
   */
  public Vector3D(Vector3D v) {
    x = v.x;
    y = v.y;
    z = v.z;
  }

  /**
   *  Copy from a point.
   *  @param  p   point to copy
   */
  public Vector3D(Point3D p) {
    x = p.x;
    y = p.y;
    z = p.z;
  }

  /**
   *  @param   xx    x coord
   *  @param   yy    y coord
   *  @param   zz    z coord
   */
  public Vector3D(double xx, double yy, double zz) {
    x = xx;
    y = yy;
    z = zz;
  }

  /**
   *  Calculate the length.
   *  @return  length of vector
   */
  public double length() {
    return (double)Math.sqrt(x*x+y*y+z*z);
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
   *  Normalize. Scale vector so it has length 1.
   */
  public void normalize() {
    scale(1.0f/length());
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
   *  Get sum of vectors.
   *  @param  v     vector to add
   *  @return this+v
   */
  public Vector3D plus(Vector3D v) {
    Vector3D ret = new Vector3D(this);
    ret.add(v);
    return ret;
  }
  /**
   *  Get sum of this vector and point.
   *  @param  p     point to add
   *  @return this+p
   */
  public Point3D plus(Point3D p) {
    Point3D ret = new Point3D(p);
    ret.add(this);
    return ret;
  }

  /**
   *  Substract a vector from this.
   *  @param  v     vector to substract
   */
  public void sub(Vector3D v) {
    x -= v.x;
    y -= v.y;
    z -= v.z;
  }

  /**
   *  Get difference with point.
   *  @param  p     point to substract
   *  @return this-p = -(p-this)
   */
  public Point3D minus(Point3D p) {
    Point3D ret = new Point3D(p);
    ret.sub(this);
    ret.scale(-1f);
    return ret;
  }

  /**
   *  Get difference with vector.
   *  @param  v     vector to substract
   *  @return this-v
   */
  public Vector3D minus(Vector3D v) {
    Vector3D ret = new Vector3D(this);
    ret.sub(v);
    return ret;
  }

  /**
   *  Scalar product.
   *  @param  v     vector to multiply
   *  @return this*v
   */
  public double mult(Vector3D v) {
    return x*v.x+y*v.y+z*v.z;
  }

  /**
   *  Cross product.
   *  @param  v     vector to multiply
   *  @return this x v
   */
  public Vector3D cross(Vector3D v) {
    return new Vector3D(y*v.z - z*v.y,
			z*v.x - x*v.z,
			x*v.y - y*v.x);
  }

  /**
   *  Output.
   *  @return  string representation
   */
  public String toString() {
    return new String(new StringBuffer().append("<").append(x).append(",").append(y).append(",").append(z).append(">"));
  }
}

