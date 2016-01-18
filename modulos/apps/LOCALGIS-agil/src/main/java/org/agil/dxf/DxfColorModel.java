/**
 * DxfColorModel.java
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


