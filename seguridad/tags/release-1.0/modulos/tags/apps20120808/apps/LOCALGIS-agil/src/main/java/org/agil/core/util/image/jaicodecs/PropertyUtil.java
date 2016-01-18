
package org.agil.core.util.image.jaicodecs;
/*
 * The contents of this file are subject to the  JAVA ADVANCED IMAGING
 * SAMPLE INPUT-OUTPUT CODECS AND WIDGET HANDLING SOURCE CODE  License
 * Version 1.0 (the "License"); You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.sun.com/software/imaging/JAI/index.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is JAVA ADVANCED IMAGING SAMPLE INPUT-OUTPUT CODECS
 * AND WIDGET HANDLING SOURCE CODE.
 * The Initial Developer of the Original Code is: Sun Microsystems, Inc..
 * Portions created by: _______________________________________
 * are Copyright (C): _______________________________________
 * All Rights Reserved.
 * Contributor(s): _______________________________________
 */

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class PropertyUtil {

	private static ResourceBundle b;

	/** Get bundle from .properties files in the current dir. */
	private static ResourceBundle getBundle() {
		ResourceBundle bundle = null;

		InputStream in = null;

	try {
//		in = new FileInputStream("properties");
        URL url = PropertyUtil.class.getResource("/com/ssf/onis/util/jaicodecs/properties");
        if(url==null){
            System.out.println("Fichero de properties no encontrado");
        }
        in = new FileInputStream(url.getFile());

		if (in != null) {
				bundle = new PropertyResourceBundle(in);
				return bundle;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

    public static void main(String[] args){
       b=getBundle();
       System.out.println(b.getString("FileCacheSeekableStream0"));
    }

	public static String getString(String key) {
		if (b == null) {
			b = getBundle();
		}
		return b.getString(key);
   }
}
