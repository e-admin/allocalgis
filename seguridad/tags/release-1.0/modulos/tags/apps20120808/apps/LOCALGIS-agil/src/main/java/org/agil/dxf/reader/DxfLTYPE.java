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

import java.util.Vector;

/**
 *  One entry in the linetype table.
 *
 *  @version  1.04beta0 (December 1999)
 */
public class DxfLTYPE extends DxfTable {
  // consts
  public static final short LTYPE_XREF        =    16;  // ltype is defined in external reference
  public static final short LTYPE_XREF_FOUND  =    32;  // ltype is external and found
  public static final short LTYPE_USED        =    64;  // layer was used

  public static final String CONTINUOUS      = "CONTINUOUS";
  public static final String BYBLOCK         = "BYBLOCK";
  public static final String BYLAYER         = "BYLAYER";

  private boolean isConti      = false;	// is this the special ltype CONTINUOUS?
  private String  description;          // ltype description
  private short   adjust;		// usually 65
  private double   length;		// length of pattern
  private Vector  pattern;		// Vector with ltype pattern

  /**
   *  Default constructor. Does nothing.
   */
  DxfLTYPE() {
    this(CONTINUOUS);
  }

  /**
   *  Used to create a ltype when one is referenced without being
   *  defined.
   *  @param  name  name of ltype
   */
  DxfLTYPE(String name) {
    this.name  = name.toUpperCase();
  }

  /**
   *  Take a string for a given group code.
   *  Accepted for group code
   *  3
   *  and anything DxfTable accepts.
   *  @param  grpNr  group code
   *  @param  str    the string value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this table
   */
  public boolean setGroup(short grpNr, String str) {
    if (grpNr == 3) {
      description = str;
      return true;
    }
    else {
      return super.setGroup(grpNr, str);
    }
  }



  /**
   *  Take a int value for a given group code.
   *  Accepted for group code
   *  72, 73
   *  and anything DxfTable accepts.
   *  @param  grpNr  group code
   *  @param  ival   the integer value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this table
   */
  public boolean setGroup(short grpNr, int ival) {
    switch (grpNr) {
    case 72:
      adjust = (short)ival;
      break;

    case 73:  // length of pattern
      if (pattern == null  &&  ival > 0) {
	pattern = new Vector(ival);
      }
      break;

    default:
      return super.setGroup(grpNr, ival);
    }

    return true;
  }


  /**
   *  Take a double value for a given group code.
   *  Accepted for group code
   *  40, 49
   *  and anything DxfTable accepts.
   *  @param  grpNr  group code
   *  @param  fval   the double value
   *  @return <code>true</code> group code is accepted for this entity<br>
   *          <code>false</code> unknown group code for this entity
   */
  public boolean setGroup(short grpNr, double fval) {
    switch (grpNr) {
    case 40: // length of pattern
      length = fval;
      break;

    case 49:
      if (pattern == null) {
	pattern = new Vector(5);
      }
      pattern.addElement(new Double(fval));
      break;

    default:
      return super.setGroup(grpNr, fval);
    }

    return true;
  }


  /**
   *  Do everything what is necessary after the read in of a LTYPE has finished.
   *  @return <code>true</code> everything's ok <br>
   *          <code>false</code> error occured
   */
  public boolean finishRead() {
    if (DxfFile.DEBUG_LEVEL > 0) {
      System.out.println("Finishing ltype "+name+": "+description);
    }

    // correct the length if necessary
    double[] segments   = getPattern();
    double   reallength = 0.0f;

    if (segments != null) {
      for (int s = segments.length-1;   s >= 0;   --s) {
	reallength += Math.abs(segments[s]);
      }
    }
    if (reallength != length) {
      if (Math.abs(length-reallength)/(reallength + length) > 1e-3f) {
	// seems to be an error
      }
      // force length to be correct
      length = reallength;
    }
    isConti = (CONTINUOUS.equals(name)  ||  length == 0.0f);

    return true;
  }

  /**
   *  Is this ltype the special ltype CONTINOUS?
   *  @return <code>true</code> yes<br><code>false</code>no
   */
  public boolean isContinuous() {
    return isConti;
  }


  /**
   *  Get the complete pattern length.
   *  @return  pattern length
   */
  public double getPatternLength() {
    return length;
  }

  /**
   *  Get the line segments. Lines are indicated by positive length, holes
   *  by negative length. Points have length 0.
   *  @return the pattern
   */
  public double[] getPattern() {
    if (isConti   ||   pattern == null) {
      return null;
    }
    else {
      double[] ret = new double[pattern.size()];
      int pos = 0;

      for (java.util.Enumeration e = pattern.elements();   e.hasMoreElements();  ) {
	ret[pos++] = ((Double)e.nextElement()).doubleValue();
      }

      return ret;
    }
  }

  /**
   *  Get the description string.
   *  @return description string
   */
  public String getDescription() {
    return description;
  }

  /**
   *  Get the adjustment flags.
   *  @return adjustment flags
   */
  public short getAdjustmentFlags() {
    return adjust;
  }
}

