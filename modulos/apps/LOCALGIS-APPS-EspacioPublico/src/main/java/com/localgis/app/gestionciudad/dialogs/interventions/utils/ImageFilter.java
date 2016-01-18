/**
 * ImageFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
