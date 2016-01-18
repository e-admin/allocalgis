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


package org.agil.dxf;

import java.io.*;

/**
 *  Load a font from a font file.
 *  The font file is a binary file whih contains the font information.
 *  <p>
 *  It starts with the sequence "DVFT". Then the follows the following data:
 *  <p>
 *  1 float value:    font height (vertical advance)
 *  <p>
 *  256 float values: horizontal advances for each character
 *  <p>
 *  repeated {
 *  <p>
 *  1 short value:    character number
 *  <p>
 *  1 short value:    number of lines <code>L</code> for this char
 *  <p>
 *  <code>L</code> times {
 *  <p>
 *  1 short value:    number of points <code>P</code> in this line
 *  <p>
 *  <code>P</code> times 2 floats: point data for the line
 *  <p>
 *  }
 *  <p>
 *  }
 */
public class DxfFontFromStream extends DxfFont {
  private static byte[]   HEAD = new byte[] { (byte)'D', (byte)'V', (byte)'F', (byte)'T' };
  private DrawChar[]      drawables = new DrawChar[NUM_CHARS];
  private float           height;
  private float[]         advance = new float[NUM_CHARS];
  private float[][][][]   chars = new float[NUM_CHARS][][][];

  /**
   *  Constructor. Read the font data from the stream.
   *  @param  input   input stream with font data
   *  @exception  IOException on IO errors or wrong format
   */
  public DxfFontFromStream(InputStream input) throws IOException {
    byte[]  head = new byte[HEAD.length];

    // --- head ---
    input.read(head);
    for (int b = 0;   b < HEAD.length;   b++) {
      if (head[b] != HEAD[b]) {
	throw new IOException("No font file");
      }
    }

    // --- height ---
    height = readFloat(input);

    // --- widths ---
    for (int c = 0;   c < NUM_CHARS;    c++) {
      advance[c] = readFloat(input);
    }

    // --- chars ---
    while (true) {
      int charnum;
      charnum = readUShort(input); // character number
      if (charnum == NUM_CHARS) {  // this impossible char is the end marker
	break;
      }

      int lines = readUShort(input); // number of lines
      chars[charnum] = new float[lines][][];

      // each line
      for (int l = 0;   l < lines;   l++) {
	int points = readUShort(input);	// number of points
	chars[charnum][l] = new float[points][];

	// each point
	for (int p = 0;   p < points;   p++) {
	  float x = readFloat(input);
	  float y = readFloat(input);
	  chars[charnum][l][p] = new float[] { x, y };
	}
      }
    }
  }

  /**
   *  Read an <em>unsigned</em> short from the stream.
   *  @param  input  input stream
   *  @return unsigned short value or <code>-1</code> on EOF
   *  @exception IOException on io errors and EOF
   */
  private static int readUShort(InputStream input) throws IOException {
    int val1 = input.read();
    int val2 = input.read();
    if ((val1 |  val2) < 0) {
      throw new EOFException();
    }
    return val1<<8 | val2;
  }

  /**
   *  Read a float from the stream.
   *  @param  input  input stream
   *  @return float value
   *  @exception IOException on io errors and EOF
   */
  private static float readFloat(InputStream input) throws IOException {
    int val1 = readUShort(input);
    int val2 = readUShort(input);
    if ((val1 | val2) < 0) {
      throw new EOFException();
    }

    return Float.intBitsToFloat(val1<<16 | val2);
  }

  /**
   *  Get the drawable for a char.
   *  @param   charNum  character number
   *  @return  drawable for this character
   */
  public DrawChar getDrawChar(int charNum) {
    charNum = normalize(charNum);
    if (drawables[charNum] == null) {
      drawables[charNum] = new DrawChar(chars[charNum], advance[charNum]);
    }

    return drawables[charNum];
  }

  /**
   *  Get the height (vertical advance).
   *  @return font height
   */
  public float getHeight() {
    return height;
  }

}
//================================== EOF ======================================
