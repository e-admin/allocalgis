/**
 * DxfLAYER.java
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

package org.agil.dxf.reader;


/** 
 *  One entry in the layer table.
 *  
 *  @version  1.00beta0 (January 1999)
 */
public class DxfLAYER extends DxfTable {
  // consts
  public static final short LAYER_FROZEN      =     1;  /* layer is frozen */
  public static final short LAYER_AUTO_FROZEN =     2;  /* layer automatically frozen 
						    in all VIEWPORTS */
  public static final short LAYER_LOCKED      =     4;  /* layer is locked */
  public static final short LAYER_XREF        =     8;  /* layer is from XREF */
  public static final short LAYER_XREF_FOUND  =    16;  /* layer is from known XREF */
  public static final short LAYER_USED        =    32;  /* layer was used */
  public static final short LAYER_INVISIBLE   = 16384;  /* (own:) layer is invisible */

  protected short   color     = -1;     /* layer color [#62] */
  protected String  linetype;           /* linetype (as string) [#6] */
  protected boolean visible   = true;   /* is visible? */
  protected boolean is0       = false;  /* is this the layer "0"? */

  /**
   *  Default constructor. Does nothing.
   */
  DxfLAYER() {
  }

  /**
   *  Used to create a layer when one is referenced without being
   *  defined.
   *  @param  name  name of layer
   */  
  DxfLAYER(String name) {
    this.name  = name.toUpperCase();
    this.color = 7;
  }

  /**
   *  Take a short (color) value for a given group code.
   *  Accepted for group code
   *  62
   *  and anything DxfTable accepts.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, short col) {
    if (grpNr == 62) {
      if (col < 0) {
	color   = (short)-col;
	flags  |= LAYER_INVISIBLE;
      }
      else {
	color   = col;
	flags  &= ~LAYER_INVISIBLE;
      }
      return true;
    }
    else {
      return super.setGroup(grpNr, color);
    }
  }


  /**
   *  Take a string for a given group code.
   *  Accepted for group code
   *  6
   *  and anything DxfTable accepts.
   *  @param  grpNr  group code
   *  @param  str    the string value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, String str) {
    if (grpNr == 6) {
      linetype = str;
      return true;
    }
    else {
      return super.setGroup(grpNr, str);
    }
  }

  /**
   *  Do everything what is necessary after the read in of a LAYER has finished.
   *  @return <code>true</code> everything's ok <br>
   *          <code>false</code> error occured
   */
  public boolean finishRead() {
    //    System.out.println("Finishing layer "+name);
    visible = ((flags & LAYER_INVISIBLE) == 0);
    is0 = "0".equals(name);
    return true;
  }

  /**
   *  Get the visibility flag.
   *  @return visibility flag
   */
  public boolean getVisibility() {
    return visible;
  }

  /**
   *  Set the visibility flag.
   *  @param onoff visibility flag
   */
  public void setVisibility(boolean onoff) {
    //    System.out.println("Layer "+name+" now "+ (onoff ? "visible" : "invisible"));
    visible = onoff;
  }

  /**
   *  Is this layer the special layer 0?
   *  @return <code>true</code> yes<br><code>false</code>no
   */
  public boolean isLayer0() {
    return is0;
  }

  /**
   *  Get the DXF color value.
   *  @return color value
   */
  public short getColor() {
    setReferenced();
    return color;
  }

  /**
   *  Get the line type name.
   *  @return line type name
   */
  public String getLineTypeName() {
    return linetype == null ? DxfLTYPE.CONTINUOUS : linetype ;
  }
}

