/**
 * DxfTable.java
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
 *  Base class to represent one table element in the DXF tables section.
 *
 *  @version  1.00beta0 (January, 1999)
 */
public class DxfTable implements DxfInterface {
  protected String    name;         /* name of the table elem [#2] */
  protected short     flags;        /* flags [#70] */
  private   boolean   isUsed;       /* is this layer etc. used? */


  /**
   *  Take a double value for a given group code.
   *  No accepted codes.
   *  @param  grpNr  group code
   *  @param  fval   the double value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, double fval) {
    // no use
    return false;
  }

  /**
   *  Take a string for a given group code.
   *  Accepted for group code
   *  2.
   *  @param  grpNr  group code
   *  @param  str    the string value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, String str) {
    switch (grpNr) {
    case 2:
      // only uppercase symbol names are allowed!
      name = str.toUpperCase();
      return true;

    case 100: // new since Acad R13
      // discard
      return true;

    default:
      return false;
    }
  }


  /**
   *  Take an int value for a given group code.
   *  Accepted for group code
   *  70.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival) {
    if (grpNr == 70) {
      flags |= (short)ival;
      return true;
    }
    return false;
  }


  /**
   *  Take a short (color) value for a given group code.
   *  No accepted codes.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, short color) {
    // no use
    return false;
  }


  /**
   *  Do everything what is necessary after the read in of a table has finished.
   *  @return <code>true</code> everything's ok <br>
   *          <code>false</code> error occured
   */
  public boolean finishRead() {
    // nothing to do
    return true;
  }


  /**
   *  Is this a terminating element?
   *  @return terminating?
   */
  boolean isTerm() {
    return false;
  }


  /**
   *  Is this table element referenced in the file?
   *  @return the answer
   */
  public final boolean isReferenced() {
    return isUsed;
  }

  /**
   *  Call when this table element is referenced.
   */
  public final void setReferenced() {
    isUsed = true;
  }

  /**
   *  Get the name of this table element.
   *  @return element's name
   */
  public final String getName() {
    return name;
  }

  /**
   *  Get the flags of this element.
   *  @return element's flags
   */
  public final short getFlags() {
    return flags;
  }

}

