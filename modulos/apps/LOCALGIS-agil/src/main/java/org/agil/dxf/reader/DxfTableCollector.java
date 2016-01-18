/**
 * DxfTableCollector.java
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


package org.agil.dxf.reader;


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

