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
 *  Representing a DXF TRACE. There's no real difference between TRACE and 
 *  SOLID.
 *  
 *  @version 1.00beta0 (January 1999)
 */
public class DxfTRACE extends DxfSOLID {
  /**
   *  Convert this TRACE.
   *  Because TRACE and SOLID are essentially the same the typical
   *  TRACE convert method in the DxfConverter class should loke like:<br>
   *  <code>
   *  public void convert(DxfTRACE trace, DxfFile dxf, Object collector) {
   *    convert((DxfSOLID)trace, dxf, collector);
   *  }
   *  </code>
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    preConvert();		// work around for erroneaous DXF files
    converter.convert(this, dxf, collector);
  }
}


