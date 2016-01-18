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
import java.util.*;

/** 
 *  DxfTables resembles the TABLES section of the DXF file.
 *  
 *  @version   1.00beta0
 */
public class DxfTables extends DxfSection implements DxfTableCollector {
  // constants
  static final byte    NOT_ACTIVE  = -1;   // no active table
  static final byte    LAYER       = 0;    // layer table
  static final byte    LTYPE       = 1;    // ltype table
  static final byte    STYLE       = 2;    // style table
  static final byte    MAX_TABLES  = 3;    // always last

  protected byte       activeTable = NOT_ACTIVE;
  protected Hashtable  table[] = new Hashtable[MAX_TABLES];

  /** 
   *  Read the TABLES section.
   *  @param  grp       DXF groups reader
   *  @exception DxfException on i/o and syntax errors
   */
  public void read(DxfGroups grp) throws DxfException {
    while (true) {
      int nr = 0;
      // always 0\nTABLE
      grp.read();
      //      System.out.println("DxfTables.read: grp = "+grp);
      String code = grp.valToString();
      if (grp.number != 0  ||  !"TABLE".equals(code)) {
	if ("ENDSEC".equals(code)) {
	  // ready with this section
	  break;
	}
	else {		
	  throw new DxfException("err!GrpInTables", new String[] { String.valueOf(grp.getLineNr()) });
	}
      }
      // 2\nNAME_OF_TABLE
      grp.read();
      code = grp.valToString();
      if (grp.number != 2) {	  	
	  throw new DxfException("err!GrpInTables", new String[] { String.valueOf(grp.getLineNr()) });
      }
      if ("LAYER".equals(code)) {
	/* layer table */
	activeTable = LAYER;
      }
      else if ("LTYPE".equals(code)) {
	activeTable = LTYPE;
      }
      else if ("STYLE".equals(code)) {
	activeTable = STYLE;
      }
      else {
	activeTable = NOT_ACTIVE;
      }
      
      // table length
      if (false) { // old version before support for Acad R13/14
	grp.read();
      }
      else {
	// === skip special Acad R13/R14 stuff ===
	// (this is a quick hack and longs for further investigation)
		do {
	  	  grp.read();
		} while (grp.number == 5    || 
	      	     grp.number == 100  ||
		    	 grp.number == 330  ||
			 	 // RRI
			 	 grp.number == 102  ||  // 102\n{ACAD_XDICTIONARY o 102\n}
			 	 grp.number == 360);    // 360\nhandle of dictionary
      }
      if (grp.number != 70) {	  	
	throw new DxfException("err!GrpInTables", new String[] { String.valueOf(grp.getLineNr()) });
      }
      int tableLen = grp.valToInt(); 
      if (tableLen > 0  &&  activeTable != NOT_ACTIVE) {
	table[activeTable] = new Hashtable(tableLen);
      }
      else { 
	activeTable = NOT_ACTIVE;
      }
      grp.readTables(this);
    }    
  }


  /**
   *  Create the default LAYER 0 if this has not already happened during reading the
   *  file. This is necessary because some DXF exporter filters don't write correct
   *  TABLES sections.
   */
  void createLayer0() {
    //    System.out.println("createLayer0");
    if (table[LAYER] == null) {
      table[LAYER] = new Hashtable(1);
    }
    DxfLAYER layer0 = new DxfLAYER("0");
    layer0.finishRead();

    activeTable = LAYER;
    try {
      addTable(layer0);
    } catch(DxfException x) {
      // cannot happen because activeTable is set!
    }
    //    System.out.println("/createLayer0");
  }


  /**
   *  Create the default LTYPE CONTINUOUS if this has not already happened during reading the
   *  file. This is necessary because some DXF exporter filters don't write correct
   *  TABLES sections.
   */
  void createLtypeContinuous() {
    //    System.out.println("createLtypeContinuous");
    if (table[LTYPE] == null) {
      table[LTYPE] = new Hashtable(1);
    }
    DxfLTYPE conti = new DxfLTYPE();
    conti.finishRead();

    activeTable = LTYPE;
    try {
      addTable(conti);
    } catch(DxfException x) {
      // cannot happen because activeTable is set!
    }
    //    System.out.println("/createLtypeContinuous");
  }


  /**
   *  Create the default STYLE STD if this has not already happened during reading the
   *  file. This is necessary because some DXF exporter filters don't write correct
   *  TABLES sections.
   */
  void createStyleStd() {
    //    System.out.println("createStyleStd");
    if (table[STYLE] == null) {
      table[STYLE] = new Hashtable(1);
    }
    DxfSTYLE std = new DxfSTYLE();
    std.finishRead();

    activeTable = STYLE;
    try {
      addTable(std);
    } catch(DxfException x) {
      // cannot happen because activeTable is set!
    }
    //    System.out.println("/createStyleStd");
  }


  /**
   *  Add a table entry to the active table.
   *  @param  tab  table entry to add
   *  @return always <code>true</code>
   *  @exception DxfException when no active table is defined
   */
  public boolean addTable(DxfTable tab) throws DxfException {
    if (activeTable == NOT_ACTIVE) {
      throw new DxfException("err!Internal");
    }
    //    System.out.println("Adding table item "+tab.name);
    table[activeTable].put(tab.name, tab);
    return true;
  }

  /**
   *  Get the LAYER with the given name.
   *  @param name  name of LAYER 
   *  @param refer set use reference for this LAYER when <code>true</code>
   *  @return the LAYER with the given name or <code>null</code> when the
   *          LAYER is unknown
   */
  public DxfLAYER getLayer(String name, boolean refer) {
    if (name != null  &&  table[LAYER] != null) {
      // only uppercase symbol names are allowed!
      name = name.toUpperCase();
      DxfLAYER l = (DxfLAYER)table[LAYER].get(name);
      if (l == null  &&  !refer) {
	//	showWarning("warnUnknownLayer", new String[] { name } );
	l = new DxfLAYER(name);
	table[LAYER].put(name, l);
      }
      if (refer   &&   l != null) {
	l.setReferenced();
      }
      return l;
    }
    else {
      return null;
    }
  }

  /**
   *  Get the LAYER with the given name. Don't set the use reference for this
   *  LAYER.
   *  @param name  name of LAYER 
   *  @return the LAYER with the given name or <code>null</code> when the
   *          LAYER is unknown
   */
  public DxfLAYER getLayer(String name) {
    return getLayer(name, false);
  }


  /**
   *  Get the LTYPE with the given name.
   *  @param name  name of LTYPE (when null, return LTYPE CONTINUOUS) 
   *  @return the linetype with the given name or <code>null</code> when the
   *          linetype is unknown
   */
  public DxfLTYPE getLtype(String name) {
    if (name == null) {
      name = DxfLTYPE.CONTINUOUS;
    }
    else {
      // only uppercase symbol names are allowed!
      name = name.toUpperCase();
    }
    if (table[LTYPE] != null) {
      DxfLTYPE s = (DxfLTYPE)table[LTYPE].get(name);
      if (s != null) {
	s.setReferenced();
	return s;
      }
      else if (name.equals(DxfLTYPE.CONTINUOUS)) {
	return null;
      }
      else {
	return getLtype(DxfLTYPE.CONTINUOUS);
      }
    }
    else {
      return null;
    }
  }


  /**
   *  Get all LTYPEs.
   *  @return all LTYPEs or <code>null</code>
   */
  public DxfLTYPE[] getLtypes() {
    if (table[LTYPE] != null) {
      DxfLTYPE[] ret = new DxfLTYPE[table[LTYPE].size()];
      int l = 0;

      for (Enumeration e = table[LTYPE].elements();   e.hasMoreElements();  ) {
	ret[l++] = (DxfLTYPE)e.nextElement();
      }

      return ret;
    }
    return null;
  }

  /**
   *  Get the STYLE with the given name.
   *  @param name  name of STYLE (when null, return STYLE STANDARD) 
   *  @return the style with the given name or <code>null</code> when the
   *          style is unknown
   */
  public DxfSTYLE getStyle(String name) {
    if (name == null) {
      name = DxfSTYLE.STANDARD;
    }
    else {
      // only uppercase symbol names are allowed!
      name = name.toUpperCase();
    }
    if (table[STYLE] != null) {
      DxfSTYLE s = (DxfSTYLE)table[STYLE].get(name);
      if (s != null) {
	s.setReferenced();
	return s;
      }
      else if (name.equals(DxfSTYLE.STANDARD)) {
	return null;
      }
      else {
	return getStyle(DxfSTYLE.STANDARD);
      }
    }
    else {
      return null;
    }
  }

  /**
   *  Get the LAYER table. The LAYER table is a Hashtable
   *  where the keys are the layer names and the values
   *  are the corresponding DxfLAYER classes.
   *  @return Hashtable or <code>null</code>, if there's no
   *          layer defined in the file
   */
  Hashtable getLayerTable() {
    return table[LAYER] != null  ?  table[LAYER]  :  null;
  }


  /**
   *  Get the length of the LAYER table.
   *  @return length of layer table (= number of layers in file)
   */
  int getLayerTableLength() {
    return table[LAYER] != null  ?  table[LAYER].size()  :  0;
  }

}


