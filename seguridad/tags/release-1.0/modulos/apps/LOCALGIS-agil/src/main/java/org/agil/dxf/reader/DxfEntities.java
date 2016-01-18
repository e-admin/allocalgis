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

import java.io.*;

/** 
 *  DxfEntities resembles the ENTITIES section of the DXF file.
 *  
 *  @version   1.00beta0
 */
public class DxfEntities extends DxfSection implements DxfConvertable {
  protected DxfEntitySet     entitySet = new DxfEntitySet();

  /** 
   *  Read the ENTITIES section.
   *  @param  grp  group code reader
   *  @param  reporter  reporter for status and error messages
   *  @exception DxfException on i/o and syntax errors
   */
  public void read(DxfGroups grp) throws DxfException {
    grp.readEntities(entitySet);
  }


  /**
   *  Convert these section using the given DxfConverter.
   *  @param  converter  the DXF converter
   *  @param  dxf        DXF file for references
   *  @param  collector  collector for results
   */
  public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
    converter.convert(entitySet, dxf, collector);
  }

  /**
   *  Get the entity set with the entities. Not cloned for efficiency reasons!
   *  @return the netity set
   */
  public DxfEntitySet getEntitySet() {
    return entitySet;
  }
}




