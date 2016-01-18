package com.geopista.app;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 20-abr-2004
 * Time: 11:23:57
 * To change this template use File | Settings | File Templates.
 */

/**
   * Ejemplo - creacion de un nuevo FileFilter para los ficheros gif y jpg:
   *
   *     JFileChooser chooser = new JFileChooser();
   *     AnexosFileFilter filter = new AnexosFileFilter(
   *                   new String{"gif", "jpg"}, "JPEG & GIF Images")
   *     chooser.addChoosableFileFilter(filter);
   *     chooser.showOpenDialog(this);
   *
   */
  public class GeoPistaFileFilter extends FileFilter {

      private static String TYPE_UNKNOWN = "Type Unknown";
      private static String HIDDEN_FILE = "Hidden File";

      private Hashtable filters = null;
      private String description = null;
      private String fullDescription = null;
      private boolean useExtensionsInDescription = true;

      /**
       * Crea un FileFilter. Si no se especifica un filtro,
       * se aceptan todos los ficheros
       *
       */
      public GeoPistaFileFilter() {
          this.filters = new Hashtable();
      }

      /**
       * Crea un FileFilter que acepta los ficheros con una determinada extension.
       * Ejemplo: new AnexosFileFilter("jpg");
       *
       */
      public GeoPistaFileFilter(String extension) {
      this(extension,null);
      }

      /**
       * Crea un FileFilter que acepta el tipo de fichero dado.
       * Ejemplo: new AnexosFileFilter("jpg", "JPEG Image Images");
       *
       * Note that the "." before the extension is not needed. If
       * provided, it will be ignored.
       *
       */
     public GeoPistaFileFilter(String extension, String description) {
        this();
        if(extension!=null) addExtension(extension);
        if(description!=null) setDescription(description);
    }

     /**
      * Creates a file filter from the given string array.
      * Example: new AnexosFileFilter(String {"gif", "jpg"});
      *
      * Note that the "." before the extension is not needed adn
      * will be ignored.
      *      
      */
     public GeoPistaFileFilter(String[] filters) {
         this(filters, null);
    }

     /**
      * Creates a file filter from the given string array and description.
      * Example: new AnexosFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
      *
      * Note that the "." before the extension is not needed and will be ignored.
      *
      * @see #addExtension
      */
     public GeoPistaFileFilter(String[] filters, String description) {
        this();
        for (int i = 0; i < filters.length; i++) {
            // add filters one by one
            addExtension(filters[i]);
        }
        if(description!=null) setDescription(description);
     }

     /**
      * Return true if this file should be shown in the directory pane,
      * false if it shouldn't.
      *
      * Files that begin with "." are ignored.
      *
      * @see #getExtension
      * @see FileFilter#accepts
      */
     public boolean accept(File f) {
        if(f != null) {
            if(f.isDirectory()) {
            return true;
            }
            String extension = getExtension(f);
            if(extension != null && filters.get(getExtension(f)) != null) {
                return true;
            }
        }
        return false;
     }

     /**
      * Return the extension portion of the file's name .
      *
      * @see #getExtension
      * @see FileFilter#accept
      */
      public String getExtension(File f) {
        if(f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if(i>0 && i<filename.length()-1) {
                return filename.substring(i+1).toLowerCase();
            };
        }
        return null;
     }

     /**
      * Adds a filetype "dot" extension to filter against.
      *
      * For example: the following code will create a filter that filters
      * out all files except those that end in ".jpg" and ".tif":
      *
      *   ExampleFileFilter filter = new ExampleFileFilter();
      *   filter.addExtension("jpg");
      *   filter.addExtension("tif");
      *
      * Note that the "." before the extension is not needed and will be ignored.
      */
     public void addExtension(String extension) {
         if(filters == null) {
             filters = new Hashtable(5);
        }
        filters.put(extension.toLowerCase(), this);
        fullDescription = null;
     }


     /**
      * Returns the human readable description of this filter. For
      * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
      *
      * @see setDescription
      * @see setExtensionListInDescription
      * @see isExtensionListInDescription
      * @see FileFilter#getDescription
      */
     public String getDescription() {
     if(fullDescription == null) {
         if(description == null || isExtensionListInDescription()) {
         fullDescription = description==null ? "(" : description + " (";
         // build the description from the extension list
         Enumeration extensions = filters.keys();
         if(extensions != null) {
             fullDescription += "." + (String) extensions.nextElement();
             while (extensions.hasMoreElements()) {
             fullDescription += ", ." + (String) extensions.nextElement();
             }
         }
         fullDescription += ")";
         } else {
         fullDescription = description;
         }
     }
     return fullDescription;
     }

     /**
      * Sets the human readable description of this filter. For
      * example: filter.setDescription("Gif and JPG Images");
      *
      * @see setDescription
      * @see setExtensionListInDescription
      * @see isExtensionListInDescription
      */
     public void setDescription(String description) {
     this.description = description;
     fullDescription = null;
     }

     /**
      * Determines whether the extension list (.jpg, .gif, etc) should
      * show up in the human readable description.
      *
      * Only relevent if a description was provided in the constructor
      * or using setDescription();
      *
      * @see getDescription
      * @see setDescription
      * @see isExtensionListInDescription
      */
     public void setExtensionListInDescription(boolean b) {
     useExtensionsInDescription = b;
     fullDescription = null;
     }

     /**
      * Returns whether the extension list (.jpg, .gif, etc) should
      * show up in the human readable description.
      *
      * Only relevent if a description was provided in the constructor
      * or using setDescription();
      *
      * @see getDescription
      * @see setDescription
      * @see setExtensionListInDescription
      */
     public boolean isExtensionListInDescription() {
     return useExtensionsInDescription;
     }
 }

