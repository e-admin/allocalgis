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
 *  4x4dim double matrix used for perspective transformations.
 *  The class is now declared final to allow a more aggresive optimization.
 *
 *
 *  @version   1.10, 01/13/99
 */
public final class Matrix4D {
  static private final double DEG2RAD = Math.PI/180.0;  // conversion from degree to radians

  public double xx, xy, xz, xw,     // 1st row
			   yx, yy, yz, yw,     // 2nd row
			   zx, zy, zz, zw,     // 3rd row
			   wx, wy, wz, ww;     // 4th row

  /**
   *  Create identity matrix.
   */
  public Matrix4D() {
	// set to identity mat
	xx = yy = zz = ww = 1f;
  }

  /**
   *  Copy constructor.
   *  @param  m   matrix to copy
   */
  public Matrix4D(Matrix4D m) {
	xx = m.xx;
	xy = m.xy;
	xz = m.xz;
	xw = m.xw;
	yx = m.yx;
	yy = m.yy;
	yz = m.yz;
	yw = m.yw;
	zx = m.zx;
	zy = m.zy;
	zz = m.zz;
	zw = m.zw;
	wx = m.wx;
	wy = m.wy;
	wz = m.wz;
	ww = m.ww;
  }

  /**
   *  @param  mxx     1st elem in 1st row
   *  @param  mxy     2nd elem in 1st row
   *  @param  mxz     3rd elem in 1st row
   *  @param  mxw     4th elem in 1st row
   *  @param  myx     1st elem in 2nd row
   *  @param  myy     2nd elem in 2nd row
   *  @param  myz     3rd elem in 2nd row
   *  @param  myw     4th elem in 2nd row
   *  @param  mzx     1st elem in 3rd row
   *  @param  mzy     2nd elem in 3rd row
   *  @param  mzz     3rd elem in 3rd row
   *  @param  mzw     4th elem in 3rd row
   *  @param  mwx     1st elem in 4th row
   *  @param  mwy     2nd elem in 4th row
   *  @param  mwz     3rd elem in 4th row
   *  @param  mww     4th elem in 4th row
   */
  public Matrix4D(double mxx, double mxy, double mxz, double mxw,
		  double myx, double myy, double myz, double myw,
		  double mzx, double mzy, double mzz, double mzw,
		  double mwx, double mwy, double mwz, double mww) {
	xx = mxx;
	xy = mxy;
	xz = mxz;
	xw = mxw;
	yx = myx;
	yy = myy;
	yz = myz;
	yw = myw;
	zx = mzx;
	zy = mzy;
	zz = mzz;
	zw = mzw;
	wx = mwx;
	wy = mwy;
	wz = mwz;
	ww = mww;
  }

  /**
   *  Reset to identity
   */
  public void identity() {
	xx = yy = zz = ww = 1f;
	xy = xz = xw =
	  yx = yz = yw =
	  zx = zy = zw =
	  wx = wy = wz = 0f;
  }

  /**
   *  Transponize.
   */
  public void transponize() {
	// switch rows and columns
	double t;
	t = xy; xy = yx; yx = t;
	t = xz; xz = zx; zx = t;
	t = xw; xw = wx; wx = t;
	t = yz; yz = zy; zy = t;
	t = yw; yw = wy; wy = t;
	t = zw; zw = wz; wz = t;
  }

  /**
   *  Matrix multiplication of vector.
   *  @param  v   vector to transform
   *  @return transformed vector
   */
  public Vector3D mult(Vector3D v) {
	return new Vector3D(xx*v.x + xy*v.y + xz*v.z,
			yx*v.x + yy*v.y + yz*v.z,
			zx*v.x + zy*v.y + zz*v.z);
  }

  /**
   *  Transformation of 1 vector.
   *  @param  v   vector to transform
   */
  public void transform(Vector3D v) {
	double x = xx*v.x + xy*v.y + xz*v.z,
		  y = yx*v.x + yy*v.y + yz*v.z,
		  z = zx*v.x + zy*v.y + zz*v.z;

	v.x = x;
	v.y = y;
	v.z = z;
  }

  /**
   *  Matrix multiplication of point.
   *  @param  p    point to transform
   *  @return transformed point
   */
  public Point3D mult(Point3D p) {
	Point3D ret = new Point3D(xx*p.x + xy*p.y + xz*p.z + xw,
				  yx*p.x + yy*p.y + yz*p.z + yw,
				  zx*p.x + zy*p.y + zz*p.z + zw);
	ret.scale(1f/(wx*p.x + wy*p.y + wz*p.z + ww));
	return ret;
  }

  /**
   *  Transformation of 1 point.
   *  @param  p   point to transform
   */
  public void transform(Point3D p) {
	double  x = xx*p.x + xy*p.y + xz*p.z + xw,
		   y = yx*p.x + yy*p.y + yz*p.z + yw,
		   z = zx*p.x + zy*p.y + zz*p.z + zw,
		   w = wx*p.x + wy*p.y + wz*p.z + ww;

	p.x = x/w;
	p.y = y/w;
	p.z = z/w;
  }

  /**Transformacion de un punto considerando solo el plano horizontal
   */
   public void transformXY(Point3D p){
		double  x = xx*p.x + xy*p.y+ xw,
		y = yx*p.x + yy*p.y + yw,
		w = wx*p.x + wy*p.y + ww;
		p.x = x/w;
		p.y = y/w;
   }

  /**
   *  Matrix multiplication.
   *  @param  m   matrix to multiply with
   *  @return this * m
   */
  public Matrix4D mult(Matrix4D m) {
	if (m != null) {
	  return new Matrix4D(xx*m.xx + xy*m.yx + xz*m.zx + xw*m.wx,
			  xx*m.xy + xy*m.yy + xz*m.zy + xw*m.wy,
			  xx*m.xz + xy*m.yz + xz*m.zz + xw*m.wz,
			  xx*m.xw + xy*m.yw + xz*m.zw + xw*m.ww,
			  yx*m.xx + yy*m.yx + yz*m.zx + yw*m.wx,
			  yx*m.xy + yy*m.yy + yz*m.zy + yw*m.wy,
			  yx*m.xz + yy*m.yz + yz*m.zz + yw*m.wz,
			  yx*m.xw + yy*m.yw + yz*m.zw + yw*m.ww,
			  zx*m.xx + zy*m.yx + zz*m.zx + zw*m.wx,
			  zx*m.xy + zy*m.yy + zz*m.zy + zw*m.wy,
			  zx*m.xz + zy*m.yz + zz*m.zz + zw*m.wz,
			  zx*m.xw + zy*m.yw + zz*m.zw + zw*m.ww,
			  wx*m.xx + wy*m.yx + wz*m.zx + ww*m.wx,
			  wx*m.xy + wy*m.yy + wz*m.zy + ww*m.wy,
			  wx*m.xz + wy*m.yz + wz*m.zz + ww*m.wz,
			  wx*m.xw + wy*m.yw + wz*m.zw + ww*m.ww);
	}
	else {
	  return new Matrix4D(this);
	}
  }

  /**
   *  Matrix multiplication.
   *  @param  m  matrix to multply with
   */
  public void multBy(Matrix4D m) {
	double x = xx*m.xx + xy*m.yx + xz*m.zx + xw*m.wx,
		  y = xx*m.xy + xy*m.yy + xz*m.zy + xw*m.wy,
		  z = xx*m.xz + xy*m.yz + xz*m.zz + xw*m.wz,
		  w = xx*m.xw + xy*m.yw + xz*m.zw + xw*m.ww;
	xx = x;
	xy = y;
	xz = z;
	xw = w;

	x = yx*m.xx + yy*m.yx + yz*m.zx + yw*m.wx;
	y = yx*m.xy + yy*m.yy + yz*m.zy + yw*m.wy;
	z = yx*m.xz + yy*m.yz + yz*m.zz + yw*m.wz;
	w = yx*m.xw + yy*m.yw + yz*m.zw + yw*m.ww;
	yx = x;
	yy = y;
	yz = z;
	yw = w;

	x = zx*m.xx + zy*m.yx + zz*m.zx + zw*m.wx;
	y = zx*m.xy + zy*m.yy + zz*m.zy + zw*m.wy;
	z = zx*m.xz + zy*m.yz + zz*m.zz + zw*m.wz;
	w = zx*m.xw + zy*m.yw + zz*m.zw + zw*m.ww;
	zx = x;
	zy = y;
	zz = z;
	zw = w;

	x = wx*m.xx + wy*m.yx + wz*m.zx + ww*m.wx;
	y = wx*m.xy + wy*m.yy + wz*m.zy + ww*m.wy;
	z = wx*m.xz + wy*m.yz + wz*m.zz + ww*m.wz;
	w = wx*m.xw + wy*m.yw + wz*m.zw + ww*m.ww;
	wx = x;
	wy = y;
	wz = z;
	ww = w;
  }

  /**
   *  Matrix multiplication from left.
   *  @param  m  matrix to multiply with.
   */
  public void multLeftBy(Matrix4D m) {
	double x = m.xx*xx + m.xy*yx + m.xz*zx + m.xw*wx,
		  y = m.yx*xx + m.yy*yx + m.yz*zx + m.yw*wx,
		  z = m.zx*xx + m.zy*yx + m.zz*zx + m.zw*wx,
		  w = m.wx*xx + m.wy*yx + m.wz*zx + m.ww*wx;
	xx = x;
	yx = y;
	zx = z;
	wx = w;

	x = m.xx*xy + m.xy*yy + m.xz*zy + m.xw*wy;
	y = m.yx*xy + m.yy*yy + m.yz*zy + m.yw*wy;
	z = m.zx*xy + m.zy*yy + m.zz*zy + m.zw*wy;
	w = m.wx*xy + m.wy*yy + m.wz*zy + m.ww*wy;
	xy = x;
	yy = y;
	zy = z;
	wy = w;

	x = m.xx*xz + m.xy*yz + m.xz*zz + m.xw*wz;
	y = m.yx*xz + m.yy*yz + m.yz*zz + m.yw*wz;
	z = m.zx*xz + m.zy*yz + m.zz*zz + m.zw*wz;
	w = m.wx*xz + m.wy*yz + m.wz*zz + m.ww*wz;
	xz = x;
	yz = y;
	zz = z;
	wz = w;

	x = m.xx*xw + m.xy*yw + m.xz*zw + m.xw*ww;
	y = m.yx*xw + m.yy*yw + m.yz*zw + m.yw*ww;
	z = m.zx*xw + m.zy*yw + m.zz*zw + m.zw*ww;
	w = m.wx*xw + m.wy*yw + m.wz*zw + m.ww*ww;
	xw = x;
	yw = y;
	zw = z;
	ww = w;
  }

  /**
   *  Translate the origin.
   *  @param  x   translation in x
   *  @param  y   translation in y
   *  @param  z   translation in z
   */
  public void translate(double x, double y, double z) {
	xw += x*xx+y*xy+z*xz;
	yw += x*yx+y*yy+z*yz;
	zw += x*zx+y*zy+z*zz;
	ww += x*wx+y*wy+z*wz;
  }

  /**
   *  Translate the origin
   *  @param  v   translation vector
   */
  public void translate(Vector3D v) {
	translate(v.x, v.y, v.z);
  }

  /**
   *  Translate.
   *  @param  x   translation in x
   *  @param  y   translation in y
   *  @param  z   translation in z
   */
  public void translateLeft(double x, double y, double z) {
	if (x != 0f) {
	  xx += x*wx;  xy += x*wy;  xz += x*wz;  xw += x*ww;
	}
	if (y != 0f) {
	  yx += y*wx;  yy += y*wy;  yz += y*wz;  yw += y*ww;
	}
	if (z != 0f) {
	  zx += z*wx;  zy += z*wy;  zz += z*wz;  zw += z*ww;
	}
  }

  /**
   *  Translate the origin
   *  @param  v   tranbslation vector
   */
  public void translateLeft(Vector3D v) {
	translateLeft(v.x, v.y, v.z);
  }


  /**
   *  Move the stuff.
   *  @param  x   translation in x
   *  @param  y   translation in y
   *  @param  z   translation in z
   */
  public void moveBy(double x, double y, double z) {
	if (x != 0f) {
	  xx += x*xw;  yx += x*yw;  zx += x*zw;  wx += x*ww;
	}
	if (y != 0f) {
	  xy += y*xw;  yy += y*yw;  zy += y*zw;  wy += y*ww;
	}
	if (z != 0f) {
	  xz += z*xw;  yz += z*yw;  zz += z*zw;  wz += z*ww;
	}
  }

  /**
   *  Translate the origin
   *  @param  v   translation vector
   */
  public void moveBy(Vector3D v) {
	moveBy(v.x, v.y, v.z);
  }


  /**
   *  Rotate theta degrees about the y axis.
   *  @param  theta  rotation angle in rad
   */
  public void yrot(double theta) {
	if (theta == 0.0) {
	  return;
	}
	double ct = Math.cos(theta);
	double st = -Math.sin(theta);

	double Nx = (double) (xx * ct - zx * st);
	double Nz = (double) (xx * st + zx * ct);
	xx = Nx;
	zx = Nz;

	Nx = (double) (xy * ct - zy * st);
	Nz = (double) (xy * st + zy * ct);
	xy = Nx;
	zy = Nz;

	Nx = (double) (xz * ct - zz * st);
	Nz = (double) (xz * st + zz * ct);
	xz = Nx;
	zz = Nz;

	Nx = (double) (xw * ct - zw * st);
	Nz = (double) (xw * st + zw * ct);
	xw = Nx;
	zw = Nz;
  }

  /**
   *  Rotate theta degrees about the x axis.
   *  @param  theta  rotation angle in rad
   */
  public void xrot(double theta) {
	if (theta == 0.0) {
	  return;
	}
	double ct = Math.cos(theta);
	double st = Math.sin(theta);

	double Ny = (double) (yx * ct - zx * st);
	double Nz = (double) (yx * st + zx * ct);
	yx = Ny;
	zx = Nz;

	Ny = (double) (yy * ct - zy * st);
	Nz = (double) (yy * st + zy * ct);
	yy = Ny;
	zy = Nz;

	Ny = (double) (yz * ct - zz * st);
	Nz = (double) (yz * st + zz * ct);
	yz = Ny;
	zz = Nz;

	Ny = (double) (yw * ct - zw * st);
	Nz = (double) (yw * st + zw * ct);
	yw = Ny;
	zw = Nz;
  }

  /**
   *  Rotate theta degrees about the z axis.
   *  @param  theta  rotation angle in rad
   */
  public void zrot(double theta) {
	if (theta == 0.0) {
	  return;
	}
	double ct = Math.cos(theta);
	double st = Math.sin(theta);

	double Nx = (double) (xx * ct - yx * st);
	double Ny = (double) (xx * st + yx * ct);
	xx = Nx;
	yx = Ny;

	Nx = (double) (xy * ct - yy * st);
	Ny = (double) (xy * st + yy * ct);
	xy = Nx;
	yy = Ny;

	Nx = (double) (xz * ct - yz * st);
	Ny = (double) (xz * st + yz * ct);
	xz = Nx;
	yz = Ny;

	Nx = (double) (xw * ct - yw * st);
	Ny = (double) (xw * st + yw * ct);
	xw = Nx;
	yw = Ny;
  }

  /**
   *  Scale.
   *  @param  f   factor to scale with.
   */
  public void scale(double f) {
	xx *= f;
	yx *= f;
	zx *= f;
	wx *= f;
	xy *= f;
	yy *= f;
	zy *= f;
	wy *= f;
	xz *= f;
	yz *= f;
	zz *= f;
	wz *= f;
  }

  /**
   *  Scale different in x,y,z.
   *  @param  fx   scaling factor in x
   *  @param  fy   scaling factor in y
   *  @param  fz   scaling factor in z
   */
  public void scale(double fx, double fy, double fz) {
	xx *= fx;
	yx *= fx;
	zx *= fx;
	wx *= fx;
	xy *= fy;
	yy *= fy;
	zy *= fy;
	wy *= fy;
	xz *= fz;
	yz *= fz;
	zz *= fz;
	wz *= fz;
  }


  private static final double MIN_INT = (double)Short.MIN_VALUE;
  private static final double MAX_INT = (double)Short.MAX_VALUE;

  /**
   *  Transform some points.
   *  @param  v       points to transform
   *  @param  tx      transformed points x coord
   *  @param  ty      transformed points y coord
   *  @param  tz      transformed points z coord
   *  @param  npoints nr of points to transform
   */
  public void transform(Point3D v[], int tx[], int ty[], int tz[], int npoints) {
	Point3D tmp;

	for (int i = npoints; --i >= 0;) {
	  tmp = mult(v[i]);

	  if (tmp.x < MIN_INT) {
	tmp.x = MIN_INT;
	  }
	  else if (tmp.x > MAX_INT) {
	tmp.x = MAX_INT;
	  }
	  if (tmp.y < MIN_INT) {
	tmp.y = MIN_INT;
	  }
	  else if (tmp.y > MAX_INT) {
	tmp.y = MAX_INT;
	  }
	  if (tmp.z < MIN_INT) {
	tmp.z = MIN_INT;
	  }
	  else if (tmp.z > MAX_INT) {
	tmp.z = MAX_INT;
	  }

	  tx[i] = (int) tmp.x;
	  ty[i] = (int) tmp.y;
	  tz[i] = (int) tmp.z;
	}
  }

  /**
   *  Output
   *  @return String representing this.
   */
  public String toString() {
	return new String(new StringBuffer().append("{{").
	  append(xx).append(",").append(xy).append(",").append(xz).append(",").append(xw).
	  append("},{").
	  append(yx).append(",").append(yy).append(",").append(yz).append(",").append(yw).
	  append("},{").
	  append(zx).append(",").append(zy).append(",").append(zz).append(",").append(zw).
	  append("},{").
	  append(wx).append(",").append(wy).append(",").append(wz).append(",").append(ww).
	  append("}}"));
  }
}


