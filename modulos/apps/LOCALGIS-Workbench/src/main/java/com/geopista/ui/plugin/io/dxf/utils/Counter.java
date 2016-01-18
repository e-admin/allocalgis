/**
 * Counter.java
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

 
package com.geopista.ui.plugin.io.dxf.utils;

/**
 *  Changeable integer wrapper, to be used as counter especially in Hashtables.
 */
public final class Counter {
  private int value;

  /**
   *  Default constructor. Initialize value with 0.
   */
  public Counter() {
    value = 0;
  }

  /**
   *  Constructor. Initialize value with <code>val</code>.
   *  @param   val  init value
   */
  public Counter(int val) {
    value = val;
  }

  /**
   *  Copy constructor. Initialize value with given counter.
   *  @param  count  counter to be copied
   */
  public Counter(Counter count) {
    value = count.value;
  }

  /**
   *  Add something to the counter.
   *  @param  add  value to add
   */
  public void add(int add) {
    value += add;
  }

  /**
   *  Add 1 to counter.
   */
  public void add1() {
    value++;
  }

  /**
   *  Substract something from the counter.
   *  @param  sub  value to substract
   */
  public void substract(int sub) {
    value -= sub;
  }

  /**
   *  Substract 1 from counter.
   */
  public void substract1() {
    value--;
  }

  /**
   *  Get the value.
   *  @return  accumulated value
   */
  public int getValue() {
    return value;
  }
}
