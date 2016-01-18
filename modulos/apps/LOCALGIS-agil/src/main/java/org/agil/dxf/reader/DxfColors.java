/**
 * DxfColors.java
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
 *  This interface is just used to define the color values which are used in
 *  DXF. Allowed color numbers range from 1 to 255.
 *  <p> 
 *  Color numbers 0 and 256 have special meanings:<br>
 *  0 is BYBLOCK (color is taken from INSERT<br>
 *  256 is BYLAYER (color is taken from layer)
 *  <p>
 *  The internal color handling of the reader is handling that transparently, i.e.
 *  you will always see colors between 1 and 255.
 *  <p>
 *  Color components here ranged from 0 (minimum) to 255 (maximum).
 *  <p>
 *  E.g. DXF color number <code>7</code> is <code>(red[7], green[7], blue[7])</code> which is
 *  <code>(255, 255, 255)</code> which is <em>white</em>.
 *  <p>
 *  And yes, it is not possible to have a black color. 
 *
 *  
 *  @version 1.00
 */
public interface DxfColors {
  /** Red components for colors 1 to 255. Don't use index 0, it's just for convenience. */
  public static final int red[] = {
    0, // don't use
    255, 255,   0,   0,   0, 255, 255, 128, 128, 255, 255, 166, 166, 128, 128,  77,
     77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 255, 255, 166,
    166, 128, 128,  77,  77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  77,  38,
     38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 191, 223, 124, 145,  96,
    112,  57,  68,  27,  33, 128, 191,  83, 124,  64,  96,  38,  57,  19,  27,  64,
    159,  41, 104,  32,  80,  19,  48,  10,  24,   0, 128,   0,  83,   0,  64,   0,
     38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  19,   0, 128,   0,
     83,   0,  64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  38,   0,
     19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  19,   0, 128,   0,  83,   0,
     64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  19,   0,
    128,   0,  83,   0,  64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  64,   0,
     38,   0,  19,  64, 159,  41, 104,  32,  80,  19,  48,  10,  24, 128, 191,  83,
    124,  64,  96,  38,  57,  19,  27, 191, 223, 124, 145,  96, 112,  57,  68,  27,
     33, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 255, 255, 166, 166, 128,
    128,  77,  77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 255,
    255, 166, 166, 128, 128,  77,  77,  38,  38,  84, 118, 152, 187, 221, 255
  };
  /** Green components for colors 1 to 255. Don't use index 0, it's just for convenience. */
  public static final int green[] = {
      0, // don't use
      0, 255, 255, 255,   0,   0, 255, 128,   0,   0, 128,   0,  83,   0,  64,   0,
     38,   0,  19,  64, 159,  41, 104,  32,  80,  19,  48,  10,  24, 128, 191,  83,
    124,  64,  96,  38,  57,  19,  27, 191, 223, 124, 145,  96, 112,  57,  68,  27,
     33, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 255, 255, 166, 166, 128,
    128,  77,  77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 255,
    255, 166, 166, 128, 128,  77,  77,  38,  38, 255, 255, 166, 166, 128, 128,  77,
     77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 255, 255, 166,
    166, 128, 128,  77,  77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  
     38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 191, 223, 124, 145,  96,
    112,  57,  68,  41,  33, 128, 191,  83, 124,  64,  96,  38,  57,  19,  27,  64,
    159,  41, 104,  32,  80,  19,  48,  10,  24,   0, 128,   0,  83,   0,  64,   0,
     38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  19,   0, 128,   0,
     83,   0,  64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  38,   0,
     19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  
     64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  19,   0, 
    128,   0,  83,   0,  64,   0,  38,   0,  19,  84, 118, 152, 187, 221, 255
  };
  /** Green components for colors 1 to 255. Don't use index 0, it's just for convenience. */
  public static final int blue[] = {
      0, // don't use
      0,   0,   0, 255, 255, 255, 255, 128,   0,   0, 128,   0,  83,   0,  64,   0,
     38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  19,   0, 128,   0,  
     83,   0,  64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  
     19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  
     64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  38,   0,  19,   0,
    128,   0,  83,   0,  64,   0,  38,   0,  19,   0, 128,   0,  83,   0,  64,   0,  
     38,   0,  19,  64, 159,  41, 104,  32,  80,  19,  48,  10,  24, 128, 191,  83,
    124,  64,  96,  38,  57,  19,  27, 191, 223, 124, 145,  96, 112,  57,  68,  27,
     33, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 255, 255, 166, 166, 128, 
    128,  77,  77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 255, 
    255, 166, 166, 128, 128,  77,  77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  
     77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 255, 255, 166, 
    166, 128, 128,  77,  77,  38,  38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  
     38, 255, 255, 166, 166, 128, 128,  77,  77,  38,  38, 191, 223, 124, 145,  96,
    112,  57,  68,  27,  33, 128, 191,  83, 124,  64,  96,  38,  57,  19,  27,  64,
    159,  41, 104,  32,  80,  19,  48,  96,  24,  84, 118, 152, 187, 221, 255
  };
}
