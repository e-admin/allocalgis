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
 *  DxfTableCollector -- simple interface to add tables.
 *  
 *  @version 1.00beta0 (January 1999)
 */
public interface DxfTableCollector {

  /**
   *  Add a table to the collector.
   *  @param  table  table to add
   *  @return <code>true</code> everything's ok <br>
   *          <code>false</code> table not excepted
   *  @exception DxfException on other errors
   */
  public boolean addTable(DxfTable table) throws DxfException;
}

