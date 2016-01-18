/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.utils;

/**
 * @author javieraragon
 *
 */
import java.io.File;
import javax.swing.filechooser.*;

import com.vividsolutions.jump.I18N;

public class ImageFilter extends FileFilter {
    final static String jpeg = "jpeg";
    final static String jpg = "jpg";
    final static String gif = "gif";
    final static String tiff = "tiff";
    final static String tif = "tif";
    final static String bmp = "bmp";
    
    public ImageFilter(){
    	super();
    	UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
    }
    
    // Accept all directories and all gif, jpg, or tiff files.
    public boolean accept(File f) {

        if (f.isDirectory()) {
            return true;
        }

        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            String extension = s.substring(i+1).toLowerCase();
            if (tiff.equals(extension) ||
                tif.equals(extension) ||
                gif.equals(extension) ||
                jpeg.equals(extension) ||
                jpg.equals(extension) ||
                bmp.equals(extension)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }
    
    // The description of this filter
    public String getDescription() {
        return I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.membrete.image.select.dialog.filter");
    }
}
