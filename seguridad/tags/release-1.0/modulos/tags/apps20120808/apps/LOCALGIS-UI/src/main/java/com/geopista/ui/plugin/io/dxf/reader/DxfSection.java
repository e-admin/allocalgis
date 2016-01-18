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


import java.io.*;


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


















