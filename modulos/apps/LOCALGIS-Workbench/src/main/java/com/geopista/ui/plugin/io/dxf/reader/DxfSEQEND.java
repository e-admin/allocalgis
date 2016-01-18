/**
 * DxfSEQEND.java
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


/**
 *  Representing a DXF SEQEND.
 *  This is only a sort of pseudo entity, and is used to end
 *  sequences in the DXF file.
 *  
 *  @version 1.00beta0
 */
public class DxfSEQEND extends DxfEntity {

  /**
   *  This is a terminating entity so it returns <code>true</code>.
   *  @return  always <code>true</code>
   */
  public boolean isTerm() {
    return true; 
  }



  /**
   *  SEQENDs are not converted.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    if (DxfFile.DEBUG_LEVEL > 0) {
      System.err.println("OOPS. Trying to convert SEQEND!");
    }
  }
}
  

