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
  

