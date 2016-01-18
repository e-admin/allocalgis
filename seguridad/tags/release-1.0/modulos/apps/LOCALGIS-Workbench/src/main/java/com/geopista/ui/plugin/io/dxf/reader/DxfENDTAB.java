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
 *  ENDTAB group, just to have isTerm return <code>true</code>.
 *  
 *  @version  0.10beta (March 17th, 1997)
 */
public class DxfENDTAB extends DxfTable {

  /**
   *  This is terminating, so return <code>true</code>.
   *  @return <code>true</code>
   */
  public boolean isTerm() {
    return true;
  }
}

