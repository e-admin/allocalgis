/**
 * FontFinder.java
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

import org.agil.dxf.reader.DxfException;

/**
 *  Interface for classes which know how to find fonts.
 *  
 *  @version 1.02
 */
public interface FontFinder {
  /**
   *  Get the font for a given name. Maybe a similar or default font
   *  if the correct font cannot be determined.
   *  @param   fontName  font's name
   *  @return  font
   *  @exception DxfException if no suitable font is found
   */
  public DxfFont getFont(String fontName) throws DxfException;

  /**
   *  Are there any fonts available?
   *  @return  fonts available?
   */
  public boolean fontsAvailable();

}
  
