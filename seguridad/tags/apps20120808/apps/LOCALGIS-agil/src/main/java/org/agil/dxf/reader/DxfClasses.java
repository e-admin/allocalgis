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
