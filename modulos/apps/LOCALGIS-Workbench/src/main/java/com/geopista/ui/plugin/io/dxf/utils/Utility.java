/**
 * Utility.java
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

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageProducer;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *  Utility contains some helpful functionality.
 */  
public class Utility {
  private static Component preparer = new Canvas();        // used for access to prepareImage method
  private static boolean   weAreOnDOS = (File.separatorChar == '\\'); // (too?) simple
  private static String    resourceDir   = "resources/"; // directory to search for images and other resources
  private static Applet	   applet;	// applet if we are running in one

  /**
   *  Load an image and prepare a representation. Used for static images to be loaded
   *  in an very early stage of program execution.
   *  @param   path   path of the image file
   *  @return  the loaded image
   */
  public static Image loadImage(String path) {
    return loadImage(path, preparer);
  }

  /**
   *  Load an image and prepare a representation. Used for static images to be loaded
   *  in an very early stage of program execution.
   *  @param   path      path of the image file
   *  @return  the loaded image
   */
  public static Image loadImage(String path, Component renderer) {
    if (resourceDir != null) {
      path = resourceDir + /*File.separator +*/ path;
    }
    return (new Utility()).loadAnImage(path, renderer);
  }

  /**
   *  Loads an image from a jar file. Be careful to always use /
   *  for dirs packed in jar!
   *  @param   path   path of file (e.g.. images/icon.gif)
   *  @param   renderer  component used for image rendering
   *  @return  the image
   */
  private Image loadAnImage(String path, Component renderer) {
    Image img = null;
    try {
      URL url = getClass().getResource(path);
      if (url == null) {
	// workaround for netscape problem
	if (applet != null) {
	  url = new URL(applet.getDocumentBase(), "dxfviewer/utils/"+path);
	}
	else {
	  return null;
	}
      }

      if (applet != null) {
	img = applet.getImage(url);
      }
      else {
	img = (Image) Toolkit.getDefaultToolkit().createImage( (ImageProducer) url.getContent() );                                                      
      }
    } catch (Exception x) {
      // do nothing for now
      //      System.out.println("Exception: "+x.getMessage()+", "+x);
    }
          
    if (img != null) {
      /* --- load it NOW --- */
      renderer.prepareImage(img, null);
    }

    return img;
  } 


  /**
   *  Load a text file into a string. 
   *  @param   path   name of the text file
   *  @return  the loaded text
   */
  public static String loadText(String path) {
    if (resourceDir != null) {
      path = resourceDir + /*File.separator +*/ path;
    }
    return (new Utility()).loadAText(path);
  }

  /**
   *  Loads a text file from a jar file. Be careful to always use /
   *  for dirs packed in jar!
   *  @param   path   path of file (e.g.. images/foo.txt)
   *  @return  the text
   */
  private String loadAText(String path) {
    String txt = "";
    try {
      String line;
      //      System.out.println("Loading "+path);
      
      //      System.out.println("URL = "+url);
      BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
      while ((line = reader.readLine()) != null) {
	txt += line +"\n";
      }
      reader.close();
    } catch (Exception x) {
      // do nothing for now
      //      System.out.println("Exception: "+x.getMessage()+", "+x);
    }
          
    return txt;
  } 


  /**
   *  Test wether our System is a DOS.
   *  @return   true   we are on DOS
   */
  public static boolean areWeOnDOS() {
    return weAreOnDOS;
  }


  /**
   *  Set the resource directory.
   *  @param   dir   the image drirectory
   */
  public static void setResourceDir(String dir) {
    resourceDir = dir;
  }


  /**
   *  Compile a formatted string with maximum 10 args.
   *  <pre>
   *  Special signs:
   *     %#  where hash is a digit from 0 to 9 means insert arg #
   *     &#64;#  where hash is a digit from 0 to 9 means insert localized arg #
   *     %%  means %
   *     &#64;&@64;  means &#64; 
   *  </pre>
   *  @param   tag    resource tag for format string
   *  @param   args   arguments for insertion
   *  @param   res    active resource bundle
   *  @return  String with inserted args.
   */
  public static String compileString(String tag, String[] args, ResourceBundle res) {
    String       format  = res.getString(tag);
    StringBuffer ret     = new StringBuffer(format.length());
    int          i;
    char         c;

    for (i = 0;   i < format.length();   i++) {
      c = format.charAt(i);
      
      if (c == '%'   ||   c == '@') {
	int argNum = -1;

        if (i < format.length()-1) {
	  // this implies that there are never more than 10 args
	  switch (format.charAt(i+1)) {
	  case '%':  
	    if (c == '%') { // "%%" means "%"
	      ret.append('%');
	      i++;
	    }
	    break;

	  case '@':
	    if (c == '@') { // "@@" means "@"
	      ret.append("@");
	      i++;
	    }
	    break;

	  case '0':
	    argNum = 0;
	    break;

	  case '1':
	    argNum = 1;
	    break;

	  case '2':
	    argNum = 2;
	    break;

	  case '3':
	    argNum = 3;
	    break;
	    
	  case '4':
	    argNum = 4;
	    break;

	  case '5':
	    argNum = 5;
	    break;

	  case '6':
	    argNum = 6;
	    break;

	  case '7':
	    argNum = 7;
	    break;
	    
	  case '8':
	    argNum = 8;
	    break;

	  case '9':
	    argNum = 9;
	    break;

	  default:
	    break;
	  }
	}
	if (argNum >= 0   &&   argNum < args.length) {
	  if (c == '%') {
	    // arg is a non-localized string
	    ret.append(args[argNum]);
	  }
	  else { // c == '@'
	    // arg is a tag for localization
	    ret.append(res.getString(args[argNum]));
	  }
	  i++;
	}
      }
      else {
	ret.append(c);
      }
    }

    return new String(ret);
  }


  /**
   *  Method to get the frame parent of any component.
   *  @param   comp   the component to search the frame for
   *  @return  the frame parent of the component
   */
  public static Frame getFrame(Component comp) {
    for (   ;  comp != null;   comp = comp.getParent()) {
      if (comp instanceof Frame) {
	return (Frame)comp;
      }
    }
    /* --- Not found. Ugly workaround: --- */
    return new Frame();
  }


  /**
   *  Compare two byte arrays.
   *  Compare <code>len</code> bytes from array 1 starting with offset 1 
   *  with <code>len</code> bytes from array 2 starting with offset 2.
   *  Will return always <code>true</code> for <code>len &le;= 0</code>
   *  @param arr1    array 1
   *  @param off1    offset 1
   *  @param arr2    array 2
   *  @param off2    offset 2
   *  @param len     length to compare
   *  @return <code>true</code> if both chunks are equal<br>
   *          <code>false</code> otherwise
   */
  static public boolean equalBytes(byte[] arr1, int off1, byte[] arr2, int off2, int len) {
    while (len-- > 0) {
      //      System.out.println(arr1[off1] + " == "+arr2[off2]);
      if (arr1[off1++] != arr2[off2++]) {
	//	System.out.println();
	return false;		// not equal
      }
    }
    return true;		// equal
  }


  /**
   *  Set the applet we are running in (if any).
   *  @param applet   applet we are running in (if <code>null</code> then we
   *                  are running in an application
   */
  public static void setApplet(Applet applet) {
    Utility.applet = applet;
  }

  /**
   *  Are we running an applet?
   *  @return the answer
   */
  public static boolean areWeInAnApplet() {
    return applet != null;
  }
}











