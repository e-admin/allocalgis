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

import java.awt.*;

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


