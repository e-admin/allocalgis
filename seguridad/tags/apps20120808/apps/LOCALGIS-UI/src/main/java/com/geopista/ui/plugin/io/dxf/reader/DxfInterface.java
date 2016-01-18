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
 *  Common interface to DXF related classes to allow reading of the
 *  groups code related information.
 *
 *  @version    1.00beta0 (January 1999)
 */
public interface DxfInterface {

  /**
   *  Take a double value for a given group code.
   *  @param  grpNr  group code
   *  @param  fval   the double value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, double fval);


  /**
   *  Take a short (color) value for a given group code.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, String str);


  /**
   *  Take an int value for a given group code.
   *  @param  grpNr  group code
   *  @param  fval   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, int ival);


  /**
   *  Take a string for a given group code.
   *  @param  grpNr  group code
   *  @param  str    the string value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, short color);


  /**
   *  Do everything what is necessary after the read in of an entity has finished.
   *  @return <code>true</code> everything's ok <br>
   *          <code>false</code> error occured
   */
  public boolean finishRead();
}


