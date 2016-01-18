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


import java.awt.Color;

import org.agil.dxf.reader.DxfColors;

/** 
 *  Static color model for DXF files. 
 *  
 *  @version 1.00beta0
 */
public class DxfColorModel implements DxfColors {
  /* already defined AWT colors */
  static protected Color colorArray[] = null;

  static protected DxfColorModel normalColors = new DxfColorModel(false);
  static protected DxfColorModel invertColors = new DxfColorModel(true);


  /**
   *  Return a set of colors for normal usage.
   *  @return  colors for normal usage
   */  
  static public DxfColorModel getNormalColors() {
    return normalColors;
  }

  /**
   *  Return a set of colors for iverted usage (e.g. for printing)
   *  @return  colors for normal usage
   */  
  static public DxfColorModel getInvertColors() {
    return invertColors;
  }

  private boolean invertWhite;

  /**
   *  Create new color set.
   *  @param invert  create inverted set?
   */
  private DxfColorModel(boolean invert) {
    invertWhite = invert;
  }

  /**
   *  Get the color for the given DXF color index.
   *  @param  nr  DXF color index
   *  @return AWT color for this index
   */
  public Color getColor(short nr) {
    if (nr < 1   ||   nr > 255) {
      nr = 7;
    }

    if (invertWhite  &&   nr == 7) {
      nr = 0;
    }
      
    if (colorArray == null) {
      colorArray = new Color[256];
    }
    if (colorArray[nr] == null) {
      colorArray[nr] = new Color(red[nr], green[nr], blue[nr]);
    }
    return colorArray[nr];
  }
}


