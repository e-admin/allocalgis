/**
 * ZIPCreatorUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.server.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class ZIPCreatorUtils {

	/**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ZIPCreatorUtils.class);
  
	public static byte[] createZip(String zipName, File rootFile,boolean canonical){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
		try {
	    	zos.setLevel(Deflater.DEFAULT_COMPRESSION);
	    	zos.setMethod(Deflater.DEFLATED);
	    	String contextPath=null;
	    	if (canonical)
	    		contextPath = rootFile.getCanonicalPath().replace(rootFile.getName(), "");
	    	else
	    		contextPath=rootFile.getCanonicalPath();
	    	
    		addFolderToZip(rootFile, contextPath, zos);	    	    	
			zos.flush();
	        baos.flush();
	        zos.close();
	        baos.close();
		} catch (FileNotFoundException e) {
			logger.error(e);
			System.out.println(e);
		} catch (IOException e) {			
			logger.error(e);
			System.out.println(e);			
		}
 
        return baos.toByteArray();
    }
	
	public static void addFolderToZip(File folder, String contextPath, ZipOutputStream zip) throws IOException {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addFolderToZip(file, contextPath, zip);
			} else {
				String name = file.getCanonicalPath().replace(contextPath, "");
				ZipEntry zipEntry = new ZipEntry(name);
				zip.putNextEntry(zipEntry);
				IOUtils.copy(new FileInputStream(file), zip);
				zip.closeEntry();
			}
		}
	}
    
}
