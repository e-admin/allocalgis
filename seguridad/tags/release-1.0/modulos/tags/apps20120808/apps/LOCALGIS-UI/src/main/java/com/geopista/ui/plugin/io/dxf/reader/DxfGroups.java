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

import java.util.Hashtable;

/**
 *  DxfGroups resembles the structure of an DXF file, where two lines
 *  together will form a group, with the first line containing the group
 *  number and the second the group code. All file input is handled by this
 *  class.
 *
 *  @version  1.03 (February, 1997)
 */
public class DxfGroups {
  // constants
  static final byte    COMMENT    = -1; // comment (not converted)
  static final byte    NOTHING    = 0;  // not converted
  static final byte    STRING     = 1;  // string value
  static final byte    VARNAME    = 1;  // a variable name is actually a string
  static final byte    LAYER      = 1;  // a layer is actually a string
  static final byte    LTYPE      = 1;  // a linetype is actually a string
  static final byte    ENTITY     = 1;  // an entity is actually a string
  static final byte    CLASS      = 1;  // a class (>=Acad13) is a string, too
  static final byte    INT        = 2;  // integer value (16 bit)
  static final byte    COUNTER    = 2;  // a counter is actually an int
  static final byte    FLOAT      = 3;  // float value
  static final byte    XVAL       = 3;  // a X value is actually a double
  static final byte    YVAL       = 3;  // a Y value is actually a double
  static final byte    ZVAL       = 3;  // a Z value is actually a double
  static final byte    COLOR      = 4;  // a color (actually a short)
  static final byte    BYTEARR    = 5;  // a byte array (EED)
  static final byte    LONGINT    = 6;  // long integer (32 bit)

  static final int     COMMENT_NUM = 999; // group number of comments
  static final int     EED_START  = 1000; // start value for extended entity data

  // Types from numbers
  static private final byte[]  typeArr = {
	ENTITY,  STRING,  STRING,  STRING,  STRING,  STRING,  STRING,  STRING,  LAYER,   VARNAME,  //   0 --   9
	XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,     //  10 --  19
	YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,     //  20 --  29
	ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,     //  30 --  39
	FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,    //  40 --  49
	FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,    //  50 --  59
	INT,     INT,     COLOR,   INT,     INT,     INT,     INT,     INT,     INT,     INT,      //  60 --  69
	COUNTER, COUNTER, COUNTER, COUNTER, COUNTER, COUNTER, COUNTER, COUNTER, COUNTER, COUNTER,  //  70 --  79
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  //  80 --  89
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  //  90 --  99

	CLASS,   NOTHING, STRING,  NOTHING, NOTHING, STRING,  NOTHING, NOTHING, NOTHING, NOTHING,  // 100 -- 109
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 110 -- 119
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 120 -- 129
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 130 -- 139
	FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   NOTHING, NOTHING,  // 140 -- 149
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 150 -- 159
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 160 -- 169
	INT,     INT,     INT,     INT,     INT,     INT,     INT,     INT,     INT,     NOTHING,  // 170 -- 179
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 180 -- 189
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 190 -- 199

	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 200 -- 209
	XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,    XVAL,     // 210 -- 219
	YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,    YVAL,     // 220 -- 229
	ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,    ZVAL,     // 230 -- 239
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 240 -- 249
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 250 -- 259
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 260 -- 269
	INT,     INT,     INT,     INT,     INT,     INT,     INT,     NOTHING, NOTHING, NOTHING,  // 270 -- 279
	INT,     INT,     INT,     INT,     INT,     INT,     INT,     INT,     INT,     NOTHING,  // 280 -- 289
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 290 -- 299
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 300 -- 309
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 310 -- 319
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 320 -- 329
	STRING,  NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 330 -- 339
	STRING,  NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 340 -- 349
	STRING,  NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,   // 350 -- 359
	NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 360 -- 369
	INT,     NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 370 -- 379
	INT,     NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING,  // 380 -- 389
	STRING,  NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING   // 390 -- 399
  };

  // Same for extended entity data
  static private final byte[]  eedTypeArr = {
	STRING,  STRING,  STRING,  STRING,  BYTEARR, STRING,  STRING,  STRING,  STRING,  STRING,   // 1000--1009
	FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,    // 1010--1019
	FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,    // 1020--1029
	FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,    // 1030--1039
	FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,    // 1040--1049
	FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,   FLOAT,    // 1050--1059
	INT,     INT,     INT,     INT,     INT,     INT,     INT,     INT,     INT,     INT,      // 1060--1069
	INT,     LONGINT, INT,     INT,     INT,     INT,     INT,     INT,     INT,     INT       // 1070--1079
  };

  private static Hashtable       dxfClassTable = null;
  private static String          entityClassHead = null; // fully qualified prefix of loadable classes
  private static Class           dxfVertexClass;

  // elements
  private   DedicatedDxfFileInputStream dxfStream; // input file

  protected short              number;  // the group number
  //  protected String             code;    // the code

  protected long               fileLength = -1;

  /**
   *  Common initialisations for all constuctors.
   */
  private void init() {
	if (entityClassHead == null) {
	  // --- determine correct prefix for loadable classes ---
	  entityClassHead = getClass().getName();
	  int dot = entityClassHead.lastIndexOf('.');

	  if (dot == -1) {
	entityClassHead = "Dxf";
	  }
	  else {
	// should be "de.escape.quincunx.geopistadxf.Dxf", but maybe this changes
	entityClassHead = entityClassHead.substring(0, dot+1) + "Dxf";
	  }
	  try {
	dxfVertexClass = Class.forName(entityClassHead+"VERTEX");
	  } catch (Exception x) {
	dxfVertexClass = null;
	  }
	}
  }


  /**
   *  Constructor.
   *  Now also accepts zipped files (the first entry)
   *  and gzipped files.
   *  @param     fileName     name of the DXF file
   *  @param     report	       for progress indication and status messages
   *  @exception DxfException  when file open fails
   */
  public DxfGroups(String fileName) throws DxfException {
	dxfStream = new DedicatedDxfFileInputStream(fileName);
	fileLength = dxfStream.getFileLength();

	// System.out.println("DxfGroups("+fileName+") --> "+iStream);
	init();
  }


  /**
   *  Constructor.
   *  Now accepts packed streams.
   *  @param      is          stream to DXF file
   *  @exception  DxfException on i/o errors
   */
  public DxfGroups(InputStream is) throws DxfException {
	dxfStream = new DedicatedDxfFileInputStream(is);
	init();
  }


  public void close() throws IOException{
    dxfStream.close();
  }



  /**
   *  Get type of group.
   *  @param     num         group number
   *  @return    type of num
   */
  static byte type(short num) {
	if (num >= 0) {
		if (num < typeArr.length) {
			/*
			if (typeArr[num] == NOTHING) {
			System.out.println("NOTHING: "+num);
			}
			*/
			return typeArr[num];
		}else if (num < COMMENT_NUM) {
			if (DxfFile.DEBUG_LEVEL > 0) {
			  System.out.println("Strange group number: "+num);
			}
			// just return index into first hundred for a quick solution
			return typeArr[num % 100];
	  }else if (num == COMMENT_NUM) {
			return COMMENT;
	  }else if (num < EED_START + eedTypeArr.length) {
			if (DxfFile.DEBUG_LEVEL > 10) {
			  System.out.println("EED: "+num);
			}
			return eedTypeArr[num-EED_START];
	  }
	}//num >=0
	return NOTHING;
  }

  /**
   *  Get type of this group.
   *  @return     type of this group number
   */
  public byte type() {
	return type(number);
  }

  /**
   *  Read two lines from file.
   *  @exception  DxfException  if read fails
   */
  void read() throws DxfException {
	try {
	  // read random access
	  do {
		// read first line (group number)
		number = dxfStream.read2Lines();
	  } while (type(number) <= NOTHING);

	  if (DxfFile.DEBUG_LEVEL > 20) {
		System.out.println("line: "+dxfStream.getLines()+"\t"+number);
	  }

	} catch (IOException x) {
	  // add line nr to exception and rethrow
	  throw new DxfException(x.getMessage()+" in line "+dxfStream.getLines());

	} catch (NumberFormatException x) {
	  // give more detailed message
	  if (dxfStream.getLines() == 1) {
		// very first line: possibly no DXF file
		throw new DxfException("err!DxfFile");
	  }else {
		// later in file...
		throw new DxfException("err!Group", new String[] { String.valueOf(dxfStream.getLines()) });
	  }
	}//catch
  }

  /**
   *  Return the value as string.
   *  @return     new String from value
   *  @exception  DxfException  if format is wrong
   */
  String valToString() throws DxfException {
	return dxfStream.getString();
  }

  /**
   *  Return the value as float.
   *  @return    value as float
   *  @exception DxfException  if format is wrong
   */
  double valToDouble() throws DxfException {
	return dxfStream.getDouble();
  }

  /**
   *  Return value as int.
   *  @return    value as int
   *  @exception DxfException  if format is wrong
   */
  int valToInt() throws DxfException {
	return dxfStream.getInt();
  }


  /**
   *  Return value as short.
   *  @return    value as short
   *  @exception DxfException  if format is wrong
   */
  short valToColor() throws DxfException {
	return dxfStream.getColor();
  }



  /**
   *  Get a specific class for an entity.
   *  @param     name        name of entity
   *  @return    entity class for name
   */
  static DxfEntity entityByName(String name) {
	if (dxfClassTable == null) {
	  /* --- initialize table --- */
	  dxfClassTable = new Hashtable(32);
	}

	//    System.out.println("getting entity "+name);
	Class c = (Class)dxfClassTable.get(name);
	if (c == null) {
	  /* not found */
	  //      System.out.println("\t-->new");
	  try {
	try {
	  c = Class.forName(entityClassHead+name);
	} catch (Exception x) {
	  c = Class.forName(entityClassHead+"Entity");
	}
	dxfClassTable.put(name, c);
	  } catch (Exception x) {
	return new DxfEntity();
	  }
	}
	try {
	  return (DxfEntity)c.newInstance();
	} catch (Exception x) {
	  return new DxfEntity();
	}
  }

  /**
   *  Get a specific class for a table.
   *  @param     name        name of table
   *  @return    table for name
   */
  static DxfTable tableByName(String name) {
	if (dxfClassTable == null) {
	  /* --- initialize table --- */
	  dxfClassTable = new Hashtable(32);
	}

	//    System.out.println("getting table "+name);
	Class c = (Class)dxfClassTable.get(name);
	if (c == null) {
	  /* not found */
	  //      System.out.println("\t-->new");
	  try {
	try {
	  c = Class.forName(entityClassHead+name);
	} catch (Exception x) {
	  c = Class.forName(entityClassHead+"Table");
	}
	dxfClassTable.put(name, c);
	  } catch (Exception x) {
	return new DxfTable();
	  }
	}
	try {
	  return (DxfTable)c.newInstance();
	} catch (Exception x) {
	  return new DxfTable();
	}
  }

  /**
   *  Read a set of entities.
   *  @param     cool        collector for entities
   *  @exception DxfException if read fails
   */
  public void readEntities(DxfEntityCollector cool) throws DxfException {

	DxfEntity   e = (cool instanceof DxfEntity)  ?  (DxfEntity)cool  :  null;
	if (DxfFile.DEBUG_LEVEL > 20) {
	  System.out.println("Entering readEntities, lines = "+dxfStream.getLines()+", cool = "+cool);
	}
	while (true) {

	  try {
		read();//LEEMOS 2 LINEAS DEL FICHERO, QUE DEFINEN UN GRUPO
	  } catch (Exception x) {
		String msg = x.getMessage();
		throw new DxfException("err!Read", new String[] { (msg == null  ?  x.toString()  :  msg), String.valueOf(dxfStream.getLines()) } );
	  }

	  if (number == 0) {
		//Estamos en el comienzo de una Entidad (group code==0=
		if (e != null) {
			// finish last entity
			e.finishRead();
		}

		/* new entity */
		//Creamos una entidad a partir de su nombre
		//     0
		//     Nombre de la entidad
		e = entityByName(dxfStream.getString());

		if (DxfFile.DEBUG_LEVEL > 20) {
		  System.out.println("cool = "+cool+", e = "+e);
		}

	try {
	  if (DxfFile.DEBUG_LEVEL > 20) {
		System.out.println("Adding "+e+" to "+cool);
	  }
	  if (!cool.addEntity(e)) {
		if (DxfFile.DEBUG_LEVEL > 20) {
		  System.out.println("Returning from readEntities, cool = "+cool);
		}
		return;
	  }
	} catch (DxfUnsupportedException x) {
	  String msg = x.getMessage();
	  throw new DxfException("err!Read", new String[] { (msg == null  ?  x.toString()  :  msg), String.valueOf(dxfStream.getLines()) } );
	}
	if (e instanceof DxfEntityCollector) {
	  /* lastEnt is a collector */
	  readEntities((DxfEntityCollector)e);
	  if (DxfFile.DEBUG_LEVEL > 20) {
		System.out.println("cool = "+cool+", e = "+e);
	  }
	}
	  }
	  else {
	set(e);
	  }
	}
  }

  /**
   *  Set groups of an entity/table.
   *  @param     inter   interface used for setting
   *  @exception DxfException if read fails
   */
  private void set(DxfInterface inter) throws DxfException {
	// System.out.println("Setting "+inter);
	if (inter != null  &&  number > 0) {
	  switch (type(number)) {
	  case STRING:
		inter.setGroup(number, valToString());
	break;

	  case INT:
	inter.setGroup(number, valToInt());
	break;

	  case FLOAT:
	inter.setGroup(number, valToDouble());
	break;

	  case COLOR:
	inter.setGroup(number, valToColor());
	break;
	  }
	}
  }

  /**
   *  Read a set of table elems.
   *  @param     cool        collector for tables
   *  @exception DxfException if read fails
   */
  void readTables(DxfTableCollector cool) throws DxfException {
	DxfTable t = null;
	//    System.out.println("Entering readTables, lines = "+dxfStream.getLines());

	while (true) {
	  read();
	  if (number == 0) {
	/* new table */
	if (t != null) {
	  t.finishRead();
	  try {
		cool.addTable(t);
	  } catch (Exception x) {
		// hm?
	  }
	  t = null;
	}

	t = tableByName(dxfStream.getString());

	if (t.isTerm()) {
	  /* end reached */
	  // System.out.println("Leaving readTables, lines = "+dxfStream.getLines());
	  return;
	}
	// System.out.println("cool = "+cool+", t = "+t);
	  }
	  else {
	set(t);
	  }
	}
  }

  /**
   *  Return a string.
   *  @return  stringified version of this
   */
  public String toString() {
	try {
	  return "#"+number+":\t'"+dxfStream.getString()+"'";
	} catch (DxfException x) {
	  return "#"+number+":\t???";
	}
  }

  /**
   *  Get the current line number.
   *  @return  current line number
   */
  public int getLineNr() {
	return dxfStream.getLines();
  }


  /* only for tests
  public static void main(String argv[]) {
	try {
	  DxfGroups grp = new DxfGroups(argv[0]);
	  do {
	grp.read();
	  } while (grp.number != 0   ||   !grp.code.equals("EOF"));
	} catch (Exception e) {
	  System.out.println(e);
	}
  }
  */
}










