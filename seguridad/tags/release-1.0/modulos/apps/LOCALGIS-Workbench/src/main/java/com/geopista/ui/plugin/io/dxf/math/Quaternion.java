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
 *  double quaternions used for rotation calculations.
 *  The class is now declared final to allow a more aggresive optimization.
 *
 *
 *  @version   1.10, 01/27/99
 */
public final class Quaternion {
  private double x, y, z, w;


  /**
   *  Create unit quaternion from vector difference.
   *  Vectors are normalized to unit sphere.
   *  @param  from  start vector
   *  @param  to    end vector
   */
  public Quaternion(Vector3D from, Vector3D to) {
    from.normalize();
    to.normalize();

    x = from.y*to.z - from.z*to.y;
    y = from.z*to.x - from.x*to.z;
    z = from.x*to.y - from.y*to.x;
    w = from.x*to.x + from.y*to.y + from.z*to.z;
  }

  /**
   *  Copy constructor.
   *  @param quat  quaternion to copy
   */
  public Quaternion(Quaternion quat) {
    this(quat.x, quat.y, quat.z, quat.w);
  }

  /**
   *  Construct from coords.
   *  @param x   x coord
   *  @param y   y coord
   *  @param z   z coord
   *  @param w   w coord
   */
  public Quaternion(double x, double y, double z, double w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  /**
   *  Default constructor. Create quaternion with w=1, else zero.
   */
  public Quaternion() {
    this(0.0f, 0.0f, 0.0f, 1.0f);
  }

  /**
   *  Create rotation matrix from this Quaternion.
   *  @return  rotation matrix
   */
  public Matrix4D getMatrix() {
    double norm  = x*x + y*y + z*z + w*w;
    double scale = norm > 0.0f  ?  2.0f/norm  :  0.0f;

    double xs = x*scale,	      ys = y*scale,	  zs = z*scale;
    double wx = w*xs,	      wy = w*ys,	  wz = w*zs;
    double xx = x*xs,	      xy = x*ys,	  xz = x*zs;
    double yy = y*ys,	      yz = y*zs,	  zz = z*zs;

    return new Matrix4D(1.0f - yy - zz, xy - wz,        xz + wy,        0.0f,
			xy + wz,        1.0f - xx - zz, yz - wx,        0.0f,
			xz - wy,        yz + wx,        1.0f - xx - yy, 0.0f,
			0.0f,           0.0f,           0.0f,           1.0f);
  }

  /**
   *  Multiply with another quaternion. Does not change this.
   *  @param  quat   Quaternion to multiply with
   *  @return  result of multiplication of this with <code>quat</code>
   */
  public Quaternion mult(Quaternion quat) {
    return new Quaternion(w*quat.x + x*quat.w + y*quat.z - z*quat.y,
			  w*quat.y + y*quat.w + z*quat.x - x*quat.z,
			  w*quat.z + z*quat.w + x*quat.y - y*quat.x,
			  w*quat.w - x*quat.x - y*quat.y - z*quat.z);
  }

  /**
   *  Multiply this quaternion with another. Changes this.
   *  @param  quat   Quaternion to multiply with
   */
  public void multBy(Quaternion quat) {
    double nw = w*quat.w - x*quat.x - y*quat.y - z*quat.z;
    double nx = w*quat.x + x*quat.w + y*quat.z - z*quat.y;
    double ny = w*quat.y + y*quat.w + z*quat.x - x*quat.z;
    double nz = w*quat.z + z*quat.w + x*quat.y - y*quat.x;
    w = nw;
    x = nx;
    y = ny;
    z = nz;
  }

  /**
   *  Get a string representation for this Qaternion.
   *  @return string representation
   */
  public String toString() {
    return "[#"+x+","+y+","+z+","+w+"#]";
  }
}
