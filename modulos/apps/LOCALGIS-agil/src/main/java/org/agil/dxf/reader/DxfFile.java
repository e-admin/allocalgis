/**
 * DxfFile.java
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


import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;




/**
 *  DxfFile resembles the sectioned structure of a DXF file (i.e.
 *  the SECTIONs HEADER, TABLES, BLOCKS, ENTITIES and EOF).
 *
 *  @version    1.00beta0 (January 11, 1999)
 *  @see        DxfSection
 */
public class DxfFile  {
  static final int DEBUG_LEVEL = 0;

  protected DxfGroups       grp;         // group reader for file

  protected DxfHeader       header;      // content of HEADER section
  protected DxfTables       tables;      // content of TABLES section
  protected DxfBlocks       blocks;      // content of BLOCKS section
  protected DxfEntities     entities;    // content of ENTITIES section

  // === new since AutoCAD R13 ===
  protected DxfClasses      classes;     // content of CLASSES section
  protected DxfObjects      objects;     // content of OBJECTS section

  /**
   *  Constructor. Read in the given file.
   *  @param     path    path to the DXF file
   *  @see       DxfGroups
   *  @exception DxfException on file i/o and syntax errors
   */
  public DxfFile(String path) throws DxfException {
    grp = new DxfGroups(path);
    read();
    //    System.out.println("After read");
  }


  /**
   *  Constructor. Read in a DXF file from the given stream.
   *  @param     is	input stream with DXF file
   *  @param     report  reporter (for status messages,
   *                               <code>null</code> allowed)
   *  @exception DxfException on file i/o and syntax errors
   *  @see       DxfGroups
   */
  public DxfFile(InputStream is) throws DxfException {
    grp = new DxfGroups(is);
    read();
    //    System.out.println("After read");
  }

  public void close() throws IOException{
        grp.close();
  }


  /**
   *  Read the file.
   *  @exception DxfException if read fails or format is wrong
   */
  private void read() throws DxfException {
    while (true) {
      grp.read();
      String code;
      if (DxfFile.DEBUG_LEVEL > 10) {
	System.out.println("number = "+grp.number);
      }
      if (grp.number == 0) {
	if ((code = grp.valToString()).equals("SECTION")) {
	  /* --- read section name --- */
	  grp.read();
	  if (grp.number != 2) {
	    // error
	    throw new DxfException("err!FpFormat", new String[] { String.valueOf(grp.getLineNr()) });
	  }
	  else {
	    code = grp.valToString();
	    if (code.equals("HEADER")) {
	      /* HEADER section */
	      header = new DxfHeader();
	      header.read(grp);
	    }
	    else if (code.equals("TABLES")) {
	      /* TABLES section */
	      tables = new DxfTables();
	      tables.read(grp);
	    }
	    else if (code.equals("BLOCKS")) {
	      /* BLOCKS section */
	      blocks = new DxfBlocks();
	      blocks.read(grp);
	    }
	    else if (code.equals("ENTITIES")) {
	      /* ENTITIES section */
	      entities = new DxfEntities();
	      entities.read(grp);
	    }
	    // ========= inserted for R13 ==========
	    else if (code.equals("CLASSES")) {
	      // CLASSES section
	      classes = new DxfClasses();
	      classes.read(grp);
	    }
	    else if (code.equals("OBJECTS")) {
	      // OBJECTS section
	      objects = new DxfObjects();
	      objects.read(grp);
	    }
	    // ========= end of insertion ==========
	    else {
	      /* skip any other section */
	      DxfSection foo = new DxfSection();
	      foo.read(grp);
	    }
	  }
	}
	else if (code.equals("EOF")) {
	  // the end

	  // --- looking for layer 0 ---
	  if (getLayer("0", false) == null) {
	    // no layer 0, so create one
	    if (tables == null) {
	      tables = new DxfTables();
	    }
	    tables.createLayer0();
	  }
	  // --- looking for ltype CONTINUOUS ---
	  if (getLtype(DxfLTYPE.CONTINUOUS) == null) {
	    // no ltype CONTINUOUS, so creat one
	    if (tables == null) {
	      tables = new DxfTables();
	    }
	    tables.createLtypeContinuous();
	  }
	  // --- looking for style STANDARD ---
	  if (getStyle(DxfSTYLE.STANDARD) == null) {
	    // no style STANDARD, so creat one
	    if (tables == null) {
	      tables = new DxfTables();
	    }
	    tables.createStyleStd();
	  }
	  // --- we will need a header ---
	  if (header == null) {
	    System.out.println("Warning: no header !!");
	    // create default header
	    header = new DxfHeader();
	  }
	  else if (header.getACADVER() == null) {
	    System.out.println("Warning: no ACADVER !!");
	  }

	  return;
	}
      }
      else {
	// error
	throw new DxfException("err!Syntax", new String[] { String.valueOf(grp.getLineNr()) });
      }
    }
  }


  /**
   *  Get a LAYER from the TABLES section of the file.
   *  @param  name   layer's name
   *  @param  refer  set referenced flag of layer?
   *  @return LAYER or <code>null</code>, if no layer is found
   */
  public DxfLAYER getLayer(String name, boolean refer) {
    return (tables != null) ? tables.getLayer(name, refer) : null;
  }

  /**
   *  Get a LAYER from the TABLES section of the file.
   *  Set's the referenced flag of the LAYER.
   *  @param  name   layer's name
   *  @return LAYER or <code>null</code>, if no layer is found
   */
  public DxfLAYER getLayer(String name) {
    return (tables != null) ? tables.getLayer(name, true) : null;
  }


  /**
   *  Get a LTYPE from the TABLES section of the file.
   *  @param  name   ltype's name
   *  @return LTYPE or <code>null</code>, if no matching linetype is found
   */
  public DxfLTYPE getLtype(String name) {
    return (tables != null) ? tables.getLtype(name) : null;
  }


  /**
   *  Get all LTYPEs from the TABLES section of the file.
   *  @return LTYPEs or <code>null</code>
   */
  public DxfLTYPE[] getLtypes() {
    return (tables != null) ? tables.getLtypes() : null;
  }


  /**
   *  Get a BLOCK from the BLOCKS section of the file.
   *  @param  name   block's name
   *  @return BLOCK or <code>null</code>, if no block is found
   */
  public DxfBLOCK getBlock(String name) {
    return (blocks != null) ? (DxfBLOCK)blocks.getBlock(name) : null;
  }


  /**
   *  Get a STYLE from the TABLES section of the file.
   *  @param  name   style's name
   *  @return STYLE or <code>null</code>, if no style is found
   */
  public DxfSTYLE getStyle(String name) {
    return (tables != null) ? tables.getStyle(name) : null;
  }


  /**
   *  Get the LAYER table. The LAYER table is a Hashtable
   *  where the keys are the layer names and the values
   *  are the corresponding DxfLAYER classes.
   *  @return Hashtable or <code>null</code>, if there's no
   *          layer defined in the file
   */
  public Hashtable getLayerTable() {
    return (tables != null) ? tables.getLayerTable() : null;
  }

  /**
   *  Get the length of the LAYER table.
   *  @return length of layer table (= number of layers in file)
   */
  public int getLayerTableLength() {
    return (tables != null) ? tables.getLayerTableLength() : 0;
  }

  /**
   *  Convert this file using a dedicated converter.
   *  @see DxfConverter
   *  @param converter  converter used to convert file
   */
  public void convert(DxfConverter converter) {
    //    System.out.println("DxfFile::convert(), entities = "+entities);
    converter.convert(this);
  }

  /**
   *  Get the code page (a DXF HEADER variable) defined in this
   *  file. The default code page is DOS437, so the default return value
   *  here is UC-DOS437, when there is no DWGCODEPAGE variable defined
   *  in the DXF file.
   *  @see DxfHeader
   *  @return code page number
   */
  public int getCodePage() {
    return header.getCodePage();
  }


  /**
   *  Return the header of this file. Not cloned for efficiency reasons.
   *  @return header
   */
  public DxfHeader getHeader() {
    return header;
  }

  /**
   *  Return the tables of this file. Not cloned for efficiency reasons.
   *  @return tables
   */
  public DxfTables getTables() {
    return tables;
  }

  /**
   *  Return the blocks of this file. Not cloned for efficiency reasons.
   *  @return blocks
   */
  public DxfBlocks getBlocks() {
    return blocks;
  }

  /**
   *  Return the entities of this file. Not cloned for efficiency reasons!
   *  @return entities
   */
  public DxfEntities getEntities() {
    return entities;
  }

}

