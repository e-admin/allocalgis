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
 *  Interface for DXF classes which can be converted.
 *  
 *  @version 1.00beta0
 */
public interface DxfConvertable {
  /**
   *  Convert this DXF object.
   *  @param  converter   converter used to convert entities
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector);
}  
