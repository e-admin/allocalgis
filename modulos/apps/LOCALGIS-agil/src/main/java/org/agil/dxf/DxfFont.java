/**
 * DxfFont.java
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


/**
 *  Simple abstract base class to hold the information of a DXF font.
 *
 *  @version 1.02
 */
public abstract class DxfFont {
  public static final int      NUM_CHARS = 256;

  /**
   *  Get the drawable for a char.
   *  @param charNum	character number
   *  @return  a drawable representation of this character
   */
  public abstract DrawChar getDrawChar(int charNum);

  /**
   *  Get the height the vertical advance) of the font.
   *  @return the height
   */
  public abstract float getHeight();

  /**
   *  Get a standard char for all chars out of bounds.
   *  @param character number
   *  @return normalized char
   */
  protected int normalize(int charNum) {
    if (charNum < 0   ||   charNum >= NUM_CHARS) {
      return (int)'?';
    }
    else {
      return charNum;
    }
  }
}


