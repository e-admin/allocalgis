/**
 * DxfSection.java
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


import java.io.IOException;


/** 
 *  Base class to represent the different section of a DXF file.
 *  
 *  @version  1.00beta0 
 */
public class DxfSection {
  /** 
   *  Read a section. To be overwritten by subclasses. This just 
   *  skips the whole section.
   *  @param      grp             Handle to the groups of the file
   *  @param      reporter        reporter for status messages and warnings
   *  @exception  IOException     Exception during file read
   */
  void read(DxfGroups grp) throws DxfException {

    // --- just skip this section --- 
    skipSection(grp);
  }


  /** 
   *  Skips a section. May be used by subclasses to skip a ection.
   *  @param      grp             Handle to the groups of the file
   *  @param      reporter        reporter for status messages and warnings
   *  @exception  IOException     Exception during file read
   */
  protected void skipSection(DxfGroups grp) throws DxfException {
    do {
      grp.read();
    } while (grp.number != 0   ||   !"ENDSEC".equals(grp.valToString()));
  }
}


















