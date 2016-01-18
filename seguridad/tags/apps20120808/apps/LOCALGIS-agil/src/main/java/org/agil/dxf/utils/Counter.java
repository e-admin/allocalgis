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

 
package org.agil.dxf.utils;

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
