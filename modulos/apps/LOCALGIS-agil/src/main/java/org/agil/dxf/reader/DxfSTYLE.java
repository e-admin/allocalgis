/**
 * DxfSTYLE.java
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
 *  One entry in the style table
 *
 *  @version  1.00beta0 (January, 1999)
 */
public class DxfSTYLE extends DxfTable {
  // consts
  public static final short STYLE_VERTICAL    = 4;      // vertical font
  public static final short STYLE_USED        = 64;     // style was referenced

  public static final String STANDARD         = "STANDARD";
  public static final String DEFAULT_FONT     = "TXT8";

  protected double   height;         // text height [#40]
  protected double   aspect = 1.0f;  // aspect ratio [#41]
  protected double   slant;          // slant [#50]
  protected double   lastHeight;     // last height [#42]
  protected short   direction;      // write dir [#71]
  protected String  fontName;       // font file name [#3]
  protected String  bigFontName;    // big font file name [#4]


  /**
   *  Default constructor.
   */
  public DxfSTYLE() {
    name     = STANDARD;
    fontName = DEFAULT_FONT;
  }

  /**
   *  Take an int value for a given group code.
   *  Accepted for group code
   *  71
   *  and everything DxfTable accepts.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
    switch (grpNr) {
    case 71:
      direction = (short)ival;
      return true;

    default:
      return super.setGroup(grpNr, ival);
    }
  }


  /**
   *  Take a string for a given group code.
   *  Accepted for group codes
   *  3 and 4
   *  and everything DxfTable accepts.
   *  @param  grpNr  group code
   *  @param  str    the string value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, String str) {
    switch (grpNr) {
    case 3:
      fontName = str.toUpperCase();
      int dot = fontName.lastIndexOf('.');
      if (dot != -1) {
	fontName = fontName.substring(0, dot);
      }
      return true;

    case 4:
      bigFontName = str;
      return true;

    default:
      return super.setGroup(grpNr, str);
    }
  }



  /**
   *  Take a double value for a given group code.
   *  Accepted for group codes
   *  40, 41, 50
   *  and everything DxfTable accepts.
   *  @param  grpNr  group code
   *  @param  fval   the double value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, double fval) {
    switch (grpNr) {
    case 40:
      height = fval;
      return true;

    case 41:
      aspect = fval;
      return true;

    case 50:
      slant = (double)((fval/180.0)*Math.PI);
      return true;

    default:
      return super.setGroup(grpNr, fval);
    }
  }


  /**
   *  Do everything what is necessary after the read in of a STYLE has finished.
   *  @return <code>true</code> everything's ok <br>
   *          <code>false</code> error occured
   */
  public boolean finishRead() {
    //    System.out.println("Finishing style "+name);
    return true;
  }

  /**
   *  Get the name of the font used.
   *  @return  font name
   */
  public final String getFontName() {
    return fontName;
  }

}

