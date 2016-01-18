/**
 * DxfEntity.java
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

package com.geopista.ui.plugin.io.dxf.reader;


import com.geopista.ui.plugin.io.dxf.math.Matrix4D;
import com.geopista.ui.plugin.io.dxf.math.Point3D;
import com.geopista.ui.plugin.io.dxf.math.Vector3D;


/**
 *  DxfEntity is the base class for a whole bunch classes related to DXF entities.
 *
 *  @version    1.00alpha0 (January 6th, 1999)
 */
public class DxfEntity implements DxfInterface, DxfConvertable {
  /* these are the same for all entities */
  private String       reference;  // reference [#5]
  private String       ltype;      // line type [#6]
  private String       layer;      // layer [#8]
  private double        height;     // height [#38]
  private double        extrusion;  // extrusion [#39] */
  private short        color = -1; // color [#62]
  private boolean      tile;       // tile (true == paper) [#67]
  private Vector3D     upward = new Vector3D(0.0f, 0.0f, 1.0f);    /* upward direction [#210,220,230] */

  private boolean      upwardIsZ; /* upward points in Z direction */
    private DxfXData dxfXData=null;

  /**
   *  Set coord by number. often needed.
   *  @param   pt     the point which coord is to be set
   *  @param   dir    coordinate (1, 2, or 3 (means x, y, or z))
   *  @param   val    value
   */
  protected void setCoord(Point3D pt, int dir, double val) {
    switch (dir) {
    case 1:
      pt.x = val;
      break;

    case 2:
      pt.y = val;
      break;

    case 3:
      pt.z = val;
      break;
    }
  }

  /**
   *  Set coord by number. Often needed.
   *  @param   v      the vector which coord is to be set
   *  @param   dir    coordinate (1, 2, or 3 (means x, y, or z))
   *  @param   val    value
   */
  protected void setCoord(Vector3D v, int dir, double val) {
    switch (dir) {
    case 1:
      v.x = val;
      break;

    case 2:
      v.y = val;
      break;

    case 3:
      v.z = val;
      break;
    }
  }

  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  38, 39, 210, 220, 230
   *  @param  grpNr  group code
   *  @param  fval   the double value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, double fval) {
    switch (grpNr) {
    case 38:
      height = fval;
      return true;

    case 39:
      extrusion = fval;
      return true;

    case 210:
    case 220:
    case 230:
      setCoord(upward, (grpNr-200)/10, fval);
      return true;
    }
    /* otherwise: */
    return false;
  }

  /**
   *  Take a short (color) value for a given group code.
   *  Accepted for group code
   *  62
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, short color) {
    if (grpNr == 62) {
      this.color = color;
      //      System.out.println("Setting color to "+color);
      return true;
    }
    else {
      return false;
    }
  }

  /**
   *  Take an int value for a given group code.
   *  Accepted for group code
   *  67
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
    if (grpNr == 67) {
      /* only allowed value is 1, but we don't care... */
      tile = (ival == 0) ? false : true;
      return true;
    }
    else {
      return false;
    }
  }


    public DxfXData getXData() {
        return dxfXData;
    }

    /**
   *  Take a string for a given group code.
   *  Accepted for group codes
   *  5 and 8.
   *  @param  grpNr  group code
   *  @param  str    the string value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, String str) {
      switch (grpNr) {
          case 5:
              reference = str;
              return true;

          case 6:
              ltype = str;
              return true;

          case 8:
        	  if (layer==null){
        		  layer = str;
        	  }
              return true;

          case 100: // new since Acad R13
              // discard
              return true;

          case 1001:
              if (str.equals("GEOPISTA"))
                  this.dxfXData=new DxfXDataAttributes();
              else if (str.equals("GEOPISTA_SCHEMA"))
                  this.dxfXData=new DxfXDataSchema();
              else if (str.equals("GEOPISTA_LAYER"))
                this.dxfXData=new DxfXDataLayer();
              return true;
          case 1000:
          case 1002:
              //return this.dxfXData.setGroup(grpNr,str);
              /* NOTA: incidencia [0000202] - Fichero que no tiene formato GEOPISTA dxf y contiene cadenas 1002,
                       por lo que estas no corresponden a XData (datos extendidos).*/
              if (this.dxfXData != null){
                return this.dxfXData.setGroup(grpNr,str);
              }else return true;
          default:
              return false;
      }
  }

    

  /**
   *  Do everything what is necessary after the read in of an entity has finished.
   *  @return <code>true</code> everything's ok <br>
   *          <code>false</code> error occured
   */
  public boolean finishRead() {
    /* --- normalize upward --- */
    double len = upward.length();

    if (len != 1.0f) {
      if (len != 0.0f) {
	upward.scale(1.0f/len);
      }
      else {
	return false;
      }
    }
    upwardIsZ = (upward.x == 0.0f   &&   upward.y == 0.0f   &&   upward.z == 1.0f);

    /* Layer needs to be defined! */
    /* no longer because some DXF output filters forget this!
    if (layer == null) {
      return false;
    }
    */

    /* --- ok --- */
    return true;
  }

  /**
   *  Tell if this class is terminating a sequence.
   *  To be overwritten by subclasses.
   *  @return   <code>true</code> for terminating classes,
   *            otherwise <code>false</code>
   */
  boolean isTerm() {
    return false;
  }

  /**
   *  Tell if this class is a ENDSEC.
   *  @return   <code>true</code> for ENDSEC,
   *            otherwise <code>false</code>
   */
  boolean isENDSEC() {
    return false;
  }

  /**
   *  Tell if this class is a BLOCK named name.
   *  @param    name   of the BLOCK
   *  @return   <code>true</code> for BLOCK named name,
   *            otherwise <code>false</code>
   */
  boolean isBlockNamed(String name) {
    /* is neither block nor has name */
    return false;
  }

  /**
   *  Calculate the <em>arbitrary transformation matrix</em>
   *  given by the upward vector and the height. This implements
   *  a special algorithm used by AutoDesk to store transformeted
   *  coordinate systems.
   *  @return  new Matrix4D, if upward/height are
   *           defining a none-identity trafo mat,
   *           otherwise <code>null</code>
   */
  public final Matrix4D calcArbitMat() {
    if (upwardIsZ  &&  height == 0) {
      return null;
    }
    else {
      /* arbitrary axis algorithm */
      Vector3D row1,
	       row2;

      if (((upward.x < 0.0f) ? -upward.x : upward.x) < 1.0f/64.0f   &&
	  ((upward.y < 0.0f) ? -upward.y : upward.y) < 1.0f/64.0f) {
	/* [0,1,0] x upward */
	row1 = new Vector3D(upward.z, 0.0f, -upward.x);
      }
      else {
	/* [0,0,1] x upward */
	row1 = new Vector3D(-upward.y, upward.x, 0.0f);
      }
      double len = row1.length();
      row1.scale(1.0f/len);

      /* upward x row1 */
      row2 = upward.cross(row1);

      return new Matrix4D(row1.x, row2.x, upward.x, height*upward.x,
			  row1.y, row2.y, upward.y, height*upward.y,
			  row1.z, row2.z, upward.z, height*upward.z,
			  0.0f,   0.0f,   0.0f,     1.0f);
    }
  }

  /**
   *  Get the reference of this entity.
   *  @return reference as String
   */
  public final String getReference() {
    return reference;
  }

  /**
   *  Get the layer name of this entity.
   *  @return layer name
   */
  public final String getLayerName() {
    return layer;
  }

  /**
   *  Get the linetype name of this entity.
   *  @return ltype name
   */
  public final String getLtypeName() {
    return ltype == null  ?  DxfLTYPE.BYLAYER  :  ltype;
  }

  /**
   *  Get the height for this entity.
   *  @return height
   */
  public final double getHeight() {
    return height;
  }

  /**
   *  Get the extrusion for this entity.
   *  @return  extrusion
   */
  public final double getExtrusion() {
    return extrusion;
  }

  /**
   *  Get the color of this entity.
   *  @return DXF color value
   */
  public short getColor() {
    return color;
  }

  /**
   *  Get the space of this entity.
   *  @return <code>true</code>  if entity lies in paper space<br>
   *          <code>false</code> if entity lies in model space
   */
  public boolean getTile() {
    return tile;
  }

  /**
   *  Return the upward vector of this entity.
   *  Not cloned for efficiency reasons!
   *  @return  upward vector
   */
  public final Vector3D getUpwardVector() {
    return upward;
  }

  /**
   *  Is the upward vector equal to [0,0,1]?
   *  @return  upward vector equals z vector?
   */
  public final boolean upwardIsZ() {
    return upwardIsZ;
  }


  /**
   *  This method is to be overwritten by subclasses with the
   *  following piece of code:<br>
   *  <code>
   *  public void convert(DxfConverter converter, DxfFile geopistadxf, Object collector) {
   *    converter.convert(this, geopistadxf, collector);
   *  }
   *  </code>
   *  <p>
   *  Entities themselves are not converted.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    if (DxfFile.DEBUG_LEVEL > 0) {
      System.out.println("Converting simple ENTITY!");
    }
  }
}




















