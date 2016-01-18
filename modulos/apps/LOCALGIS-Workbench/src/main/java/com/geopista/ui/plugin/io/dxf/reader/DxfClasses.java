/**
 * DxfClasses.java
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

package com.geopista.ui.plugin.io.dxf.reader;


import java.io.IOException;

/** 
 *  DxfClasses resembles the CLASSES section of the DXF file.
 *  This section first occured in AutoCAD R13.
 *  At the moment everything is skipped!
 *  
 *  @version  1.04 (April 2000)
 */
public class DxfClasses extends DxfSection {
  /** 
   *  Read a section. This just skips the whole section.
   *  @param      grp             Handle to the groups of the file
   *  @param      reporter        reporter for status messages and warnings
   *  @exception  IOException     Exception during file read
   */
  void read(DxfGroups grp) throws DxfException {
    // --- just skip this section --- 
    skipSection(grp);
  }
}
