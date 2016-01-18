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
 *  DxfBlocks resembles the BLOCKS section of the DXF file.
 *  
 *  @version   1.00beta0 (January 1999)
 */
public class DxfBlocks extends DxfSection {
  protected DxfEntitySet    blockSet = new DxfEntitySet();   /* the BLOCKs as an entity set */

  /** 
   *  Read the BLOCKS section.
   *  @param  grp  group code reader
   *  @exception DxfException on i/o and syntax errors
   */
  public void read(DxfGroups grp) throws DxfException {
    grp.readEntities(blockSet);
  }

  /** 
   *  Find a BLOCK by name.
   *  @see    DxfEntitySet#getBlock
   *  @param  name  the name of the BLOCK
   *  @return reference to the BLOCK (when found) or <code>null</code>
   */
  public DxfEntity getBlock(String name) {
    return blockSet.getBlock(name);
  }
}

