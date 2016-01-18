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

package org.agil.dxf.reader;

import org.agil.dxf.math.*;


/**
 *  Representing a DXF POLYLINE.
 *  A POLYLINE collects the VERTEX entities which belongs to it.
 */
public class DxfPOLYLINE extends DxfEntity implements DxfEntityCollector {
  protected int         vertexFlag;      /* vertex flag (always 1) [#66] */
  protected Point3D     basis = new Point3D(); /* basis point [#10,20,30] */
  protected int         type;            /* type of polyline [#70] */
  protected double       startWidth,      /* starting width [#40] */
                        endWidth;        /* ending width [#41] */
  protected int         nrCtrlU,         /* number of control points in U dir [#71] */
                        nrCtrlV;         /* number of control points in V dir [#72] */
  protected int         nrApproxM,       /* approximation points in M dir [#73] */
                        nrApproxN;       /* approximation points in N dir [#74] */
  protected int         smooth;          /* type of smoothing [#75] */

  protected DxfEntitySet vertices = new DxfEntitySet();        /* vertices of this polyline */

  private   boolean     prepared = false; /* is this prepared for conversion? */
  private   boolean     hasWidth = false; /* is this a polyline with width? */

  /* polyline types */
  public final static int CLOSED       = 0x01;
  public final static int CURVE_FIT    = 0x02;
  public final static int B_SPLINE     = 0x04;
  public final static int THREE_DIM    = 0x08;
  public final static int WEB          = 0x10;
  public final static int WEB_CLOSED   = 0x20;
  public final static int NET          = 0x40;
  public final static int CONT_LTYPE   = 0x80;
  /* as sets: */
  public final static int TWO_DIM      = 0x06;
  public final static int ANY_WEB      = 0x30;

  /* smooth types */
  public final static int NO_SMOOTH    = 0;
  public final static int B_SPLINE_2   = 5;
  public final static int B_SPLINE_3   = 6;
  public final static int BEZIER       = 8;


  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  10, 20, 30, 40 and 41
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
      setCoord(basis, grpNr/10, fval);
      return true;

    case 40:
      startWidth = fval;
      return true;

    case 41:
      endWidth = fval;
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }

  /**
   *  Take an int value for a given group code.
   *  Accepted for group codes
   *  70 -- 75
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
    switch (grpNr) {
    case 70:
      type = ival;
      return true;

    case 71:
      nrCtrlU = ival;
      return true;

    case 72:
      nrCtrlV = ival;
      return true;

    case 73:
      nrApproxM = ival;
      return true;

    case 74:
      nrApproxN = ival;
      return true;

    case 75:
      smooth = ival;
      return true;

    default:
      return super.setGroup(grpNr, ival);
    }
  }

  /**
   *  Is this POLYLINE using line width?
   *  return  using width?
   */
  public boolean hasLineWidth() {
    if (!prepared) {
      prepare();
    }
    return hasWidth;
  }

  /**
   *  Add a VERTEX to this POLYLINE.
   *  @param  vertex  must be a DxfVERTEX! or a DxfSEQEND
   *  @return <code>true</code> keep on reading <br>
   *          <code>false</code> this sequence is ended
   *  @exception DxfException when other entities are added
   */
  public boolean addEntity(DxfEntity vertex) throws DxfException {
    if (vertex instanceof DxfVERTEX) {
      vertices.addEntity(vertex);
      return true;
    }
    if (vertex.isTerm()  &&  vertex instanceof DxfSEQEND) {
      return false;
    }

    throw new DxfException("err!VertexAfterPolyline");
  }

  /**
   *  Convert this POLYLINE using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    if (!prepared) {
      prepare();
    }
    converter.convert(this, dxf, collector);
  }



  /**
   *  Get the vertex flag. (This should <em>always</em> return <code>true</code>.
   *  @return vertex following?
   */
  public final boolean vertexFollowing() {
    return vertexFlag != 0;
  }

  /**
   *  Get the base point. Not cloned for effeciency reasons.
   *  @return base point
   */
  public final Point3D getBasis() {
    return basis;
  }

  /**
   *  Get the type flags.
   *  @return type flags
   */
  public final int getType() {
    return type;
  }

  /**
   *  Get the starting width.
   *  @return start width
   */
  public final double getStartWidth() {
    return startWidth;
  }

  /**
   *  Get the end width.
   *  @return end width
   */
  public final double getEndWidth() {
    return endWidth;
  }

  /**
   *  Get the number of control points in u direction.
   *  @return number of control points in u direction
   */
  public final int getNrCtrlU() {
    return nrCtrlU;
  }

  /**
   *  Get the number of control points in v direction.
   *  @return number of control points in v direction
   */
  public final int getNrCtrlV() {
    return nrCtrlV;
  }

  /**
   *  Get the number of approximation points in m direction.
   *  @return number of approximation points in m direction
   */
  public final int getNrApproxM() {
    return nrApproxM;
  }

  /**
   *  Get the number of approximation points in n direction.
   *  @return number of approximation points in n direction
   */
  public final int getNrApproxN() {
    return nrApproxN;
  }

  /**
   *  Get the smoothing type flags
   *  @return smoothing type
   */
  public final int getSmoothType() {
    return smooth;
  }

  /**
   *  Get the vertices. Not cloned for effeciency reasons!
   *  @return vertices
   */
  public final DxfEntitySet getVertices() {
    return vertices;
  }

  /**
   *  Prepare before conversion.
   *  Sets the hasWidth flag.
   */
  private void prepare() {
    if (!prepared) {
      prepared = true;

      // --- Obscure but true:
      // --- the first vertex has to take the widths from the POLYLINE!
      DxfVERTEX first = (DxfVERTEX)vertices.getEntity(0);
      first.setWidths(getStartWidth(), getEndWidth());

      // look whether the width is set anywhere, thereby setting the
      // start width to the previous end width if it is unset
      double width;
      double lastEndWidth = ((getType() & DxfPOLYLINE.CLOSED) != 0) ?
	((DxfVERTEX)vertices.getEntity(vertices.getNrOfEntities()-1)).getEndWidth() :
	0.0f;

      for (java.util.Enumeration e = vertices.getEntities();   e.hasMoreElements();   ) {
	DxfVERTEX v = (DxfVERTEX)e.nextElement();
	if ((width = v.getStartWidth()) > 0.0f) {
	  hasWidth = true;
	}
	else if (width < 0.0f) {
	  v.setStartWidth(lastEndWidth);
	}
	if ((width = v.getEndWidth()) > 0.0f) {
	  hasWidth = true;
	}
	else if (width < 0.0f) {
	  v.setEndWidth(v.getStartWidth());
	}
	if ((lastEndWidth = v.getEndWidth()) > 0.0f) {
	  hasWidth = true;
	}
      }
    }
  }
}



