/**
 * UtilsResource.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.protocol.document.DocumentBean;

public class UtilsResource {

    private static final Log logger = LogFactory.getLog(UtilsResource.class);
	
	public static byte[] getBytesFromResource(DocumentBean document){
    	InputStream is = null;
    	URL url=null;
    	byte[] imageBytes=null;
    	try {    			    	  
          url = new URL(document.getFileName());
    	  is = url.openStream ();
    	  imageBytes = IOUtils.toByteArray(is);
    	}
    	catch (Exception e) {
    	   if (url!=null)
    		   logger.error("Error al leer los bytes de la imagen"+url.toExternalForm(),e);
    	   else
    		   logger.error("Formato de URL incorreta"+document.getFileName(),e);
    	}
    	finally {
    	  try {
			if (is != null) { is.close(); }
		} catch (IOException e) {
		}
    	}
    	return imageBytes;
    }
}
