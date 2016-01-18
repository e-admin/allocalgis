/**
 * CivilWorkContextReader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.context.rearder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;


/**
 * @author javieraragon
 *
 */
public class CivilWorkContextReader {
	
	private static final String webContentFileName = "WebContent";
	private static final String jarProtocolName = "jar";
	
	
	/**
	 * @param base 	Una ruta base para copiar los archivos de WebContent. Si no se 
	 * 				especifica se hará en el directorio por defecto.
	 * @return
	 */
	public static ConfigurationContext GetWebServiceRampartContext(String base){
		
		String rutaBase = "";
		if (base!=null && !base.equals("")){
			rutaBase = base;
		}

		URL webcontentURL = CivilWorkContextReader.class.getResource(webContentFileName);
		File webContentFile = null;
		
		if (webcontentURL!=null && webcontentURL.getProtocol()!=null && 
				webcontentURL.getProtocol().equals(jarProtocolName)){
			// if the file does not exist into data directory.
			//Read grid file from jar. and copy it to data directory.
			webContentFile = CretateFileFromJar(webcontentURL, rutaBase);
			
			try {
				File webInfFile = new File(webContentFile,"WEB-INF");
				
//				return ConfigurationContextFactory.createConfigurationContextFromURIs(null, CivilWorkContextReader.class.getResource("WEB-INF"));
				return ConfigurationContextFactory.createConfigurationContextFromFileSystem(webInfFile.getPath() ,null);
			} catch (AxisFault e) {
				e.printStackTrace();
			}
		}
		
		try {
			return ConfigurationContextFactory.createConfigurationContextFromFileSystem(webcontentURL.getFile() + "/WEB-INF", null);
//			return ConfigurationContextFactory.createConfigurationContextFromFileSystem(webcontentURL.getFile() + "/WEB-INF", null);
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		
		try {
			return ConfigurationContextFactory.createConfigurationContextFromFileSystem(webcontentURL.getPath(), null);
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		
		try {
			return ConfigurationContextFactory.createConfigurationContextFromFileSystem(webcontentURL.getRef(), null);
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		
		try{
			return ConfigurationContextFactory.createConfigurationContextFromFileSystem("WebContent/WEB-INF", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			return ConfigurationContextFactory.createConfigurationContextFromFileSystem("WEB-INF", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		return null;

	}
	
	
	private static File CretateFileFromJar(URL url, String base){

		try {

			//find "filename" into resources
			String tempUrl = url.toString();
			// the file's path into jar's directory tree
			String insideJarPath = tempUrl.substring(tempUrl.lastIndexOf("!")+2);
			// the jar's path
			String jarPath = tempUrl.substring(0, tempUrl.lastIndexOf("!")+2 );

			// Creates the jar file, with jarUrlConnection.
			URL jarUrl = new URL(jarPath);
			JarURLConnection connectionJarFile = (JarURLConnection)jarUrl.openConnection();
			JarFile jarfile = connectionJarFile.getJarFile();
			
			File lastDirectory = null;
			
			Enumeration<JarEntry> entries = jarfile.entries();
			while(entries.hasMoreElements()){
				JarEntry nextEntrie = entries.nextElement();
				if (nextEntrie.getName().contains(webContentFileName)){
					if (nextEntrie.isDirectory()){
						File directory = new File(base,nextEntrie.getName());
						System.out.println(directory.setReadable(true, false));
						System.out.println(directory.setWritable(true, false));
						System.out.println(directory.setExecutable(true, false));
						if (!directory.exists()){
							directory.mkdirs();
						}
						if (nextEntrie.getName().endsWith(webContentFileName+ "/") ){
							lastDirectory = directory;
						}
					} else{
						File fileToCopy = new File(base,nextEntrie.getName());
						System.out.println(fileToCopy.setReadable(true, false));
						System.out.println(fileToCopy.setWritable(true, false));
						System.out.println(fileToCopy.setExecutable(true, false));
						copyFileFromJarToFile(jarfile, nextEntrie.getName(), fileToCopy);
					}
				}
			}
			
			return lastDirectory;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static boolean copyFileFromJarToFile(JarFile srFile,String pathInsideJar, File dtFile){
		InputStream in = null;
		OutputStream out = null;
		try{

			in = srFile.getInputStream(srFile.getEntry(pathInsideJar));
			out = new FileOutputStream(dtFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			return true;
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.getMessage());
			return false;
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		}
		finally{
			try{
				in.close();
			}catch(Exception e){e.printStackTrace();}
			try{
				out.close();
			}catch(Exception e){e.printStackTrace();}
		}
	}
	
	 /**
     * Copia un directorio con todo y su contendido
     * @param srcDir
     * @param dstDir
     * @throws IOException
     */
    public void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
            
            String[] children = srcDir.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(srcDir, children[i]),
                    new File(dstDir, children[i]));
            }
        } else {
            copy(srcDir, dstDir);
        }
    }
    
    /**
     * Copia un solo archivo
     * @param src
     * @param dst
     * @throws IOException
     */
    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        
        
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    } 

}
