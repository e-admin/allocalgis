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
import java.util.zip.*;

import com.geopista.ui.plugin.io.dxf.utils.Utility;


/**
 *  Reading a file linewise fast and be able to always get the current line.
 *  Uses buffering and a fast byte-to-String conversion asserting that the
 *  input stream is ASCII.
 *  <p>
 *  It should also be possible to read binary DXF but this feature has never
 *  been tested due to lack of test data.
 *
 *  @version 1.00beta0
 */
public class DedicatedDxfFileInputStream {
  private static final byte[] binDxfTag = new byte[] {
    // A     u     t     o     C     A     D   ´ ´
    0x41, 0x75, 0x74, 0x6f, 0x43, 0x41, 0x44, 0x20,
    // B     i     n     a     r     y    ´ ´
    0x42, 0x69, 0x6e, 0x61, 0x72, 0x79, 0x20,
    // D     X     F    \r    \n    ^Z    \0
    0x44, 0x58, 0x46, 0x0d, 0x0a, 0x1a, 0x00
  };
  private static final byte[] shxTag = new byte[] {
    // A     u     t     o     C     A     D     -     8     6   ' '
    0x41, 0x75, 0x74, 0x6f, 0x43, 0x41, 0x44, 0x2d, 0x38, 0x36, 0x20,
    // s     h     a     p     e     s   ' '     1     .     0    \r
    0x73, 0x68, 0x61, 0x70, 0x65, 0x73, 0x20, 0x31, 0x2e, 0x30, 0x0d,
    //\n    ^Z    \0
    0x0a, 0x1a, 0x00
  };
  private static final int BUFFER_SIZE = 0x10000;

  public static final int TYPE_UNKNOWN = 0x00;
  public static final int TYPE_DXF     = 0x01;
  public static final int TYPE_SHX     = 0x02;
  public static final int TYPE_ZIP     = 0x10;
  public static final int TYPE_GZIP    = 0x20;
  public static final int TYPE_PACKED  = TYPE_ZIP | TYPE_GZIP;
  public static final int TYPE_BINARY  = 0x80;

  static final int typeLen[] = {
    -1,				// length of NOTHING
    0,				// length of string is special
    2,				// length of DXF int is 2
    8,				// length of double is 8
    2,				// length of color is 2
    0,				// length of byte array is special
    4				// length of DXF long integer is 4
  };

  protected int    pos      = 0;                      // pos in buffer
  protected long   markpos  = -1;                     // marked pos
  protected byte[] buffer   = new byte[BUFFER_SIZE];  // buffer
  protected long   bufpos   = 0;                      // pos of buf
  protected int    bufused  = 0;                      // used size of buffer
  protected int    filetype = TYPE_UNKNOWN;

  protected long   fileLength = -1;
  private   char[] linebuf  = new char[256];          // initial line buffer
  private   int    lines    = 0;                      // DXF "lines"
  private   InputStream iStream;
  private   DxfStreamReader reader;


  /**
   *  Private base class for Stream readers knowing about DXF stuff.
   */
  private abstract class DxfStreamReader {
    protected short group;

    /**
     *  Read the group number and the data from the given stream.
     *  @return  group number
     *  @exception NumberFormatException  on conversion errors
     *  @exception IOException  on i/o errors
     *  @exception DxfException on other errors
     */
    abstract short readChunk() throws NumberFormatException, IOException, DxfException;

    /**
     *  Get the group number.
     *  @return group number
     */
    short getGroup() {
      return group;
    }

    /**
     *  Get the data as string.
     *  @return string
     *  @exception DxfException on conversion errors
     */
    abstract String getStringData() throws DxfException;

    /**
     *  Get the data as integer (16bit) value
     *  @return integer value
     *  @exception DxfException on conversion errors
     */
    abstract int getIntData() throws DxfException;

    /**
     *  Get the data as double value.
     *  @return double value
     *  @exception DxfException on conversion errors
     */
    abstract double getDoubleData() throws DxfException;

    /**
     *  Get the data as byte array.
     *  @return byte array value
     *  @exception DxfException on conversion errors
     */
    abstract byte[] getByteArrayData() throws DxfException;

    /**
     *  Get the data as long integer (32bit) value
     *  @return integer value
     *  @exception DxfException on conversion errors
     */
    abstract int getLongIntData() throws DxfException;
  }

  /**
   *  Class to read an ASCII DXF stream.
   */
  private class AsciiDxfStreamReader extends DxfStreamReader {
    private String value;

    /**
     *  Read a complete line containing only a short int and maybe white space.
     *  @return     the short
     *  @exception  IOException  on read errors
     *  @exception  NumberFormatException  when there's something else on the line
     *  @exception DxfException on other errors like unexpected EOF
     */
    private short readGroupLine() throws IOException, NumberFormatException, DxfException {
      int   ret = 0;
      byte  b;

      // skip white space
      while ((b = read()) == (byte)' '   ||   b == (byte)'\t') {
	// empty
      }

      if (b < (byte)'0'  ||   b > (byte)'9') {
	if (b == -1) {
	  throw new DxfException("err!EOF", new String[] { String.valueOf(lines) });
	}
	else {
	  throw new NumberFormatException();
	}
      }

      do {
	ret = 10*ret + (b - (byte)'0');
	b = read();
      } while (b >= (byte)'0'   &&   b <= (byte)'9');

      // skip white space
      while (b == (byte)' '   ||   b == (byte)'\t') {
	b = read();
      }

      // skip newline char(s)
      if (b == (byte)10   ||   b == (byte)13) {
	// skip [cr][lf] or [lf][cr] combination?
	if (b+read() != (byte)23) {
	  // no combination: reset pos in buffer
	  pos--;
	}
      }
      else {
	throw new NumberFormatException();
      }

      if (ret > Short.MAX_VALUE) {
	throw new NumberFormatException();
      }

      return (short)ret;
    }


    /**
     *  Read the group number and the data from the given stream.
     *  @return  group number
     *  @exception NumberFormatException  on conversion errors
     *  @exception IOException  on i/o errors
     *  @exception DxfException on other errors
     */
    short readChunk() throws NumberFormatException, IOException, DxfException {
      // === read the group number ===
      lines++;
      group = readGroupLine();
      lines++;
      value = readLine().trim();
      return group;
    }


    /**
     *  Get the data as string.
     *  @return string
     *  @exception DxfException on conversion errors
     */
    String getStringData() throws DxfException {
      if (DxfGroups.type(group) == DxfGroups.STRING) {
	return value;
      }
      else {
	throw new DxfException("err!Format", new String[] { String.valueOf(lines) });
      }
    }

    /**
     *  Get the data as double value.
     *  @return double value
     *  @exception DxfException on conversion errors
     */
    double getDoubleData() throws DxfException {
      if (DxfGroups.type(group) == DxfGroups.FLOAT) {
	return Double.valueOf(value).doubleValue();
      }
      else {
	throw new DxfException("err!Format", new String[] { String.valueOf(lines) });
      }
    }


    /**
     *  Get the data as integer (16bit) value
     *  @return integer value
     *  @exception DxfException on conversion errors
     */
    int getIntData() throws DxfException {
      switch (DxfGroups.type(group)) {
      case DxfGroups.INT:
      case DxfGroups.COLOR:
      case DxfGroups.LONGINT:
	return (int)Integer.parseInt(value);

      default:
	throw new DxfException("err!Format", new String[] { String.valueOf(lines) });
      }
    }

    /**
     *  Get the data as byte array.
     *  @return byte array value
     *  @exception DxfException on conversion errors
     */
    byte[] getByteArrayData() throws DxfException {
      throw new DxfException("AsciiDxfStreamReader.getByteArrayData(): Not yet implemented!");
    }

    /**
     *  Get the data as long integer (32bit) value
     *  @return integer value
     *  @exception DxfException on conversion errors
     */
    int getLongIntData() throws DxfException {
      if (DxfGroups.type(group) == DxfGroups.LONGINT) {
	return Integer.parseInt(value);
      }
      else {
	throw new DxfException("err!Format", new String[] { String.valueOf(lines) });
      }
    }
  }

  /**
   *  Class to read a binary DXF stream.
   *  <p>
   *  <em>BEWARE!</em> Due to lack of test data this has never been tested
   *  and will probably crash.
   */
  private class BinaryDxfStreamReader extends DxfStreamReader {
    private byte[]  value            = new byte[256];
    private byte[]  groupNumberBytes = new byte[2];
    private int     length;
    private boolean shortGroups      = false;


    /**
     *  Create a reader which knows about binary DXF.
     *  @exception IOException  on i/o errors
     *  @exception DxfException on other errors
     */
    BinaryDxfStreamReader() throws DxfException {
      // Life could be so simple if only...
      // It seems that the DXF binary format has changed slighly
      // between AutoCAD R12 and AutoCAD R13.
      // In newer versions group numbers are always 2 bytes
      // (and not 1 or three).
      // The only way I´ve found so far to get a grip on the difference
      // is to read the first two bytes after the header and look whether
      // they are both 0x00 (which is the case in newer releases).
      mark(2);
      try {
	byte b1 = read();
	byte b2 = read();
	if (b1 != 0x00) {
	  throw new DxfException("err!Format", new String[] { String.valueOf(lines) });
	}

	shortGroups = (b2==0x00);

	reset();

	if (DxfFile.DEBUG_LEVEL > 10) {
	  System.out.println("shortGroups = "+shortGroups);
	}
      } catch (IOException x) {
	// do nothing (will crash later)
      }
    }


    /**
     *  Read the group number and the data from the given stream.
     *  @return  group number
     *  @exception NumberFormatException  on conversion errors
     *  @exception IOException  on i/o errors
     *  @exception DxfException on other errors
     */
    short readChunk() throws NumberFormatException, IOException, DxfException {
      byte b;

      // === read the group number ===
      lines++;
      if (shortGroups) {
	// all group numbers are short (= 2 byte)
	read(groupNumberBytes);
	group = (short)(unsignedShort(groupNumberBytes[1]) << 8 |
			unsignedShort(groupNumberBytes[0]));
      }
      else {
	// group numbers are 1 or 3 bytes (depending on first byte)
	b = read();
	if (b == (byte)0xFF) {
	  // EED
	  read(groupNumberBytes);
	  group = (short)(unsignedShort(groupNumberBytes[1]) << 8 |
			  unsignedShort(groupNumberBytes[0]));

	}
	else {
	  group = unsignedShort(b);
	}
      }

      lines++;
      int type = DxfGroups.type(group);
      length = typeLen[type];
      if (DxfFile.DEBUG_LEVEL > 10) {
	System.out.println("group  = "+group+",type   = "+type+",length = "+length);
      }
      if (length > 0) {		// simple item
	read(value, 0, length);
      }
      else if (length == 0) {	// String or byte array
	if (type == DxfGroups.STRING) {
	  // read until 0 byte
	  int  i = 0;
	  while ((b = read()) != (byte)0) {
	    value[i++] = b;
	  }
	  length = i;
	}
	else if (type == DxfGroups.BYTEARR) {
	  // length byte
	  length = read();
	  read(value, 0, length);
	}
	else {
	  throw new DxfException("err!Group", new String[] { String.valueOf(lines-1) });
	}
	if (DxfFile.DEBUG_LEVEL > 10) {
	  System.out.println("length (correct) = "+length);
	}
      }
      else {
	throw new DxfException("err!Group", new String[] { String.valueOf(lines-1) });
      }

      return group;
    }


    /**
     *  Get the data as string.
     *  @return string
     *  @exception DxfException on conversion errors
     */
    String getStringData() throws DxfException {
      if (DxfGroups.type(group) == DxfGroups.STRING) {
	if (DxfFile.DEBUG_LEVEL > 10) {
	  System.out.println("\""+byteToString(value, 0, length)+"\"");
	}
	return byteToString(value, 0, length);
      }
      else {
	throw new DxfException("err!Format", new String[] { String.valueOf(lines) });
      }
    }

    /**
     *  Make an unsigned long value from a signed byte.
     *  @param  b  signed byte
     *  @return unsigned value
     */
    private final long unsignedLong(byte b) {
      long l = b;
      return l >= 0  ?  l  :  l+256;
    }

    /**
     *  Make an unsigned int value from a signed byte.
     *  @param  b  signed byte
     *  @return unsigned value
     */
    private final int unsignedInt(byte b) {
      int i = b;
      return i >= 0  ?  i  :  i+256;
    }

    /**
     *  Make an unsigned short value from a signed byte.
     *  @param  b  signed byte
     *  @return unsigned value
     */
    private final short unsignedShort(byte b) {
      short s = b;
      return s >= 0  ?  s  :  (short)(s+256);
    }

    /**
     *  Get the data as double value.
     *  @return double value
     *  @exception DxfException on conversion errors
     */
    double getDoubleData() throws DxfException {
      if (DxfGroups.type(group) == DxfGroups.FLOAT) {
	long dummy = unsignedLong(value[7]) << 56  |
	             unsignedLong(value[6]) << 48  |
	             unsignedLong(value[5]) << 40  |
	             unsignedLong(value[4]) << 32  |
                     unsignedLong(value[3]) << 24  |
                     unsignedLong(value[2]) << 16  |
                     unsignedLong(value[1]) <<  8  |
                     unsignedLong(value[0]);
	if (DxfFile.DEBUG_LEVEL > 10) {
	  System.out.println(Double.longBitsToDouble(dummy));
	}
	return Double.longBitsToDouble(dummy);
      }
      else {
	throw new DxfException("err!Format", new String[] { String.valueOf(lines) });
      }
    }

    /**
     *  Get the data as integer (16bit) value
     *  @return integer value
     *  @exception DxfException on conversion errors
     */
    int getIntData() throws DxfException {
      switch (DxfGroups.type(group)) {
      case DxfGroups.INT:
      case DxfGroups.COLOR:
      case DxfGroups.LONGINT:
	return unsignedInt(value[1]) << 8  |
	       unsignedInt(value[0]);

      default:
	throw new DxfException("err!Format", new String[] { String.valueOf(lines) });
      }
    }

    /**
     *  Get the data as byte array.
     *  @return byte array value
     *  @exception DxfException on conversion errors
     */
    byte[] getByteArrayData() throws DxfException {
      byte[] ret = new byte[length];

      for (int i = length-1;   i >= 0;   i--) {
	ret[i] = value[i];
      }
      return ret;
    }

    /**
     *  Get the data as long integer (32bit) value
     *  @return integer value
     *  @exception DxfException on conversion errors
     */
    int getLongIntData() throws DxfException {
      if (DxfGroups.type(group) == DxfGroups.LONGINT) {
	int ret = unsignedInt(value[3]) << 24  |
	          unsignedInt(value[2]) << 16  |
	          unsignedInt(value[1]) <<  8  |
	          unsignedInt(value[0]);
	return ret;
      }
      else {
	throw new DxfException("err!Format", new String[] { String.valueOf(lines) });
      }
    }
  }
  /**
   *  Stuff common to all Constructors.
   *  @param  is  input stream with geopistadxf file content
   *  @exception  DxfException on certain errors
   */
  private void init(InputStream is) throws DxfException {
    if (!is.markSupported()) {
      is = new BufferedInputStream(is);
    }
    is.mark(256);
    try {
      try {
	ZipInputStream zip = new ZipInputStream(is);
	// get stream for 1st entry
	ZipEntry    entry   = zip.getNextEntry();
	if (entry == null) {	// seems to happen
	  if (DxfFile.DEBUG_LEVEL > 10) {
	    System.out.println("First ZipEntry is null");
	  }
	  throw new ZipException(); // escape form here
	}
	// if this is ok it seems to be a zip file
	fileLength = entry.getSize();
	iStream    = zip;
	filetype  |= TYPE_ZIP;
      } catch (ZipException z) {
	// doesn't seem to be a ZipFile
	is.reset();
	is.mark(256);
	if (DxfFile.DEBUG_LEVEL > 10) {
	  System.out.println("ZipException (probably ok): "+z);
	}
	// --- maybe a gzip? ---
	try {
	  iStream = new GZIPInputStream(is);
	  fileLength = -1;
	  filetype |= TYPE_GZIP;
	} catch (IOException io) {
	  if (DxfFile.DEBUG_LEVEL > 10) {
	    System.out.println("IOException (probably ok): "+io);
	  }
	  // hopefully this IOException is "Not in GZIP format"
	  is.reset();
	  iStream = is;
	}
      }

      byteAvailable();		// fill buffer to allow mark & reset handled internally!

      // === test for binary file types ===

      // --- binary DXF? ---
      mark(binDxfTag.length);	// mark for reset
      byte[] tag = new byte[binDxfTag.length];
      read(tag);
      if (Utility.equalBytes(tag, 0, binDxfTag, 0, binDxfTag.length)) {
	filetype |= TYPE_BINARY | TYPE_DXF;	// indeed: binary DXF
      }
      else {
	reset();		// no binary DXF

	// --- binary SHX? ---
	mark(shxTag.length);	// mark for reset
	tag = new byte[shxTag.length];
	read(tag);
	if (Utility.equalBytes(tag, 0, shxTag, 0, shxTag.length)) {
	  filetype |= TYPE_BINARY | TYPE_SHX; // indeed: binary SHX
	}
	else {
	  reset();		// no binary stuff, has to be ASCII
	  filetype |= TYPE_DXF;
	}
      }
    } catch (IOException iox) {
      throw new DxfException("err!IO", new String[]
			     { "[input stream]", iox.getMessage() });

    }
    if (DxfFile.DEBUG_LEVEL > 10) {
      System.out.println("File type: "+filetype);
    }

    if ((filetype & TYPE_BINARY) != 0) {
      reader = new BinaryDxfStreamReader();
      filetype |= TYPE_BINARY;
    }
    else {
      reader = new AsciiDxfStreamReader();
    }
  }


  /**
   *  Constructor.
   *  @param  fileName  path of file to read
   *  @exception  DxfException on certain errors
   */
  public DedicatedDxfFileInputStream(String fileName) throws DxfException {
    try {
      fileLength = new File(fileName).length();
      init(new FileInputStream(fileName));
    } catch (FileNotFoundException x) {
      throw new DxfException("err!FileNotFound", new String[] { fileName });
    }
  }


  /**
   *  Constructor.
   *  @param  is  input stream with geopistadxf file content
   *  @exception  DxfException on certain errors
   */
  public DedicatedDxfFileInputStream(InputStream is) throws DxfException {
    init(is);
  }


  public void close() throws IOException{
        iStream.close();
  }


  /**
   *  Destructor.
   *  Close the stream.
   *  @exception Throwable never thrown
   */
  protected void finalize() throws Throwable {
    // System.out.println("Ending read in line "+lines);
    try {
      if (iStream != null) {
	iStream.close();
	iStream = null;
      }
    } catch (Exception e) {
    }
  }

  /**
   *  Are there any more bytes available in the buffer?
   *  If necessary and possible, fill buffer.
   *  @return bytes available?
   *  @exception  IOException on certain read errors
   */
  private final boolean byteAvailable() throws IOException {
    if (pos < bufused) {
      // buffered something
      return true;
    }
    if (bufused == -1) {
      return false;
    }
    // --- fill buffer ---
    bufpos += bufused;
    bufused = iStream.read(buffer);
    pos     = 0;

    //    System.out.println("New buffer: \""+(char)buffer[0]+(char)buffer[1]+(char)buffer[2]+(char)buffer[3]+"...\"");
    return (bufused != -1);
  }


  /**
   *  Read another byte.
   *  @return the next byte or -1 on eof
   *  @exception  IOException on certain read errors
   *  @exception  DxfException on unexpected EOF
   */
  protected byte read() throws IOException, DxfException {
    if (!byteAvailable()) {
      throw new DxfException("err!EOF", new String[] { String.valueOf(lines) });
    }
    return buffer[pos++];
  }


  /**
   *  Fast routine for conversion of byte arrays to String.
   *  Assertion: the bytes are ISO8859-1 chars (or ASCII, which is a subset).
   *  No argument checking.
   *  <p>
   *  This is not declared private due to changes in handling of
   *  inner classes which caused 1.2 JVMs to refuse loading this
   *  class when compiled with 1.1.
   *
   *  @param  buf   byte buffer
   *  @param  off   offset in buffer
   *  @param  len   length of bytes to convert
   *  @return String with length len with corresponding chars.
   */
  String byteToString(byte[] buf, int off, int len) {
    if (len > linebuf.length) {
      // make big enough
      linebuf = new char[2*len];
    }
    for (int i = 0;   i < len;   i++) {
      int b = buf[off+i];
      linebuf[i] = (char)(b < 0  ?  b+256  :  b);
    }
    return new String(linebuf, 0, len);
  }


  /**
   *  Read <code>b.length</code> bytes into the given buffer.
   *  @param  b   byte buffer
   *  @return number of bytes read or -1 on EOF
   *  @exception  IOException  on read errors
   *  @exception  DxfException on unexpected EOF
   */
  protected int read(byte b[]) throws IOException, DxfException {
    return read(b, 0, b.length);
  }

  /**
   *  Read <code>len</code> bytes into the given buffer at position
   *  <code>off</code>.
   *  @param  cbuf  byte buffer
   *  @param  off   offset in buffer
   *  @param  len   length of bytes to convert
   *  @return number of bytes read or -1 on EOF
   *  @exception  IOException  on read errors
   *  @exception  DxfException on unexpected EOF
   */
  protected int read(byte cbuf[], int off, int len) throws IOException, DxfException {
    if (!byteAvailable()) {
      throw new DxfException("err!EOF", new String[] { String.valueOf(lines) });
    }

    if (len < bufused - pos) {
      // easy
      System.arraycopy(buffer, pos, cbuf, off, len);
      pos += len;
      return len;
    }
    else {
      // copy buffered bytes
      int rest = (bufused-pos);
      System.arraycopy(buffer, pos, cbuf, off, rest);
      bufpos += bufused;
      pos = bufused = 0;
      // get other bytes direct
      int chunk = iStream.read(cbuf, off+rest, len-rest);
      if (chunk >= 0) {
	bufpos += chunk;
	return rest+chunk;
      }
      else {
	bufused = -1;
	return rest;
      }
    }
  }

  /**
   *  Read a line from the file (only used for ASCII).
   *  <p>
   *  This is not declared private due to changes in handling of
   *  inner classes which caused 1.2 JVMs to refuse loading this
   *  class when compiled with 1.1.
   *
   *  @return  next line (w/o \n,\r etc.) or <code>null</code> on EOF
   *  @exception  IOException on certain read errors
   *  @exception  DxfException on unexpected EOF
   */
  String readLine() throws IOException, DxfException {
    int   start;
    byte  b = (byte)0;

    for (start = pos;
	 pos < bufused   &&   (b = buffer[pos]) != (byte)10   &&   b != (byte)13;
	 pos++) {
      // empty
    }
    if (b == (byte)10   ||   b == (byte)13) {
      pos++;
      // create string
      String ret = byteToString(buffer, start, pos-start-1);
      // tricky: read next char & put it back when necessary by resetting pos!
      try {
	byte c = read();

	// skip [cr][lf] or [lf][cr] combination?
	if (c+b != 23   &&   pos > 0) {
	  pos--;
	}
      } catch (DxfException x) {
	// nothing
      }
      //      System.out.println("1. Returning \""+ret+"\"");
      return ret;
    }
    else {
      // eob: not enough buffered
      //      System.out.println("start = "+start+" ,pos = "+pos+", bufused = "+bufused);
      if (pos-start > 0) {
	String ret = byteToString(buffer, start, pos-start);
	if (byteAvailable()) {
	  //	  System.out.println("2. Returning \""+ret+"\" + ...");
	  return ret + readLine();
	}
	else {
	  return ret;
	}
      }
      else {
	if (!byteAvailable()) {
	  return null;
	}
	else {
	  //	  System.out.println("5. Rereading...");
	  return readLine();
	}
      }
    }
  }


  /**
   *  Skip the given number of bytes.
   *  @param   n   number of bytes to skip
   *  @return  number of bytes skipped or -1 on EOF
   *  @exception  IOException  on read errors
   */
  protected long skip(long n) throws IOException {
    if (n < bufused-pos) {
      pos += (int)n;
      return n;
    }
    else {
      int rest = bufused - pos;
      n  -= rest;
      pos = bufused = 0;

      // skip other bytes directly
      long skipped = iStream.skip(n);
      bufpos += skipped;
      return rest+skipped;
    }
  }


  /**
   *  Set a mark.
   *  @param readLimit maximum number of bytes to read until this mark is useless
   */
  synchronized void mark(int readlimit) {
    if (readlimit >= bufused - pos) {
      iStream.mark(readlimit);
    }
    // no exception:
    markpos = bufpos+pos;
    if (DxfFile.DEBUG_LEVEL > 10) {
      System.out.println("mark pos = "+markpos);
    }
  }

  /**
   *  Reset to a previously marked position.
   *  @exception IOException on certain errors or problems with mark
   */
  synchronized void reset() throws IOException {
    if (markpos < bufpos) {
      iStream.reset();
      bufused = 0;
    }
    // no exception:
    pos = (int)(markpos - bufpos);
    if (DxfFile.DEBUG_LEVEL > 10) {
      System.out.println("resetting to "+markpos);
    }
    //pos = 0;
  }

  /**
   *  What we desperately need: The file position.
   *  @return actual  file position
   */
  long getFilePointer() {
    return bufpos + pos;
  }

  /**
   *  Get the length of the file (or -1 if unknown).
   *  @return length of file
   */
  long getFileLength() {
    return fileLength;
  }

  /**
   *  Return number of lines read so far.
   *  @return number of lines
   */
  int getLines() {
    return lines;
  }

  /**
   *  Return the last data as a String.
   *  @return String
   */
  String getString() throws DxfException {
    return reader.getStringData();
  }

  /**
   *  Return the last data as a float.
   *  @return float
   */
  double getDouble() throws DxfException {
    return reader.getDoubleData();
  }

  /**
   *  Return the last data as a int.
   *  @return int value
   */
  int getInt() throws DxfException {
    return reader.getIntData();
  }

  /**
   *  Return the last data as color value.
   *  @return short value
   */
  short getColor() throws DxfException {
    return (short)reader.getIntData();
  }

  /**
   *  Read the typical two line chunk.
   *  @return group code
   *  @exception NumberFormatException on conversion errors
   *  @exception IOException on i/o errors
   *  @exception DxfException on further errors
   */
  short read2Lines() throws NumberFormatException, IOException, DxfException {
    return reader.readChunk();
  }

  /**
   *  Is this reader reading a binary DXF file?
   *  @return the answer
   */
  boolean isBinary() {
    return (filetype & TYPE_BINARY) != 0;
  }

  /**
   *  Get the type of this reader.
   *  @return combination of <tt>TYPE_?</tt> flags
   */
  int getType() {
    return filetype;
  }
}

