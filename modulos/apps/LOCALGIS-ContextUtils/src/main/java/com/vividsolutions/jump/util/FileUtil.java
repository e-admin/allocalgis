/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI for
 * visualizing and manipulating spatial features with geometry and attributes.
 * 
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * For more information, contact:
 * 
 * Vivid Solutions Suite #1A 2328 Government Street Victoria BC V8T 5G5 Canada
 * 
 * (250)385-6040 www.vividsolutions.com
 */

package com.vividsolutions.jump.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 * File-related utility functions.
 */
public class FileUtil {
    /**
     * Reads a text file.
     * 
     * @param textFileName
     *                   the pathname of the file to open
     * @return the lines of the text file
     * @throws FileNotFoundException
     *                    if the text file is not found
     * @throws IOException
     *                    if the file is not found or another I/O error occurs
     */
    public static List getContents(String textFileName)
            throws FileNotFoundException, IOException {
        List contents = new ArrayList();
        FileReader fileReader = new FileReader(textFileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();

        while (line != null) {
            contents.add(line);
            line = bufferedReader.readLine();
        }

        return contents;
    }

    /**
     * Saves the String to a file with the given filename.
     * 
     * @param textFileName
     *                   the pathname of the file to create (or overwrite)
     * @param contents
     *                   the data to save
     * @throws IOException
     *                    if an I/O error occurs.
     */
    public static void setContents(String textFileName, String contents)
            throws IOException {    	    	
    	
        FileWriter fileWriter = new FileWriter(textFileName, false);
    	BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(contents);
        bufferedWriter.flush();
        bufferedWriter.close();
        fileWriter.close();

    }

    /**
     * Saves the String to a file with the given filename.
     * 
     * @param textFileName
     *                   the pathname of the file to create (or overwrite)
     * @param contents
     *                   the data to save
     * @throws IOException
     *                    if an I/O error occurs.
     */
    public static void setContentsUTF8(String textFileName, String contents)
            throws IOException {
    	
    	FileOutputStream fos = null;
    	OutputStreamWriter w = null;
    	try {
    	// new file and write BOM bytes first
    	fos = new FileOutputStream(textFileName);
    	byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };

    	fos.write(bom);

    	// open UTF8 writer
    	w = new OutputStreamWriter(fos, "UTF-8");
    	w.write(contents);
    	w.flush();
    	w.close();
    	} finally {
    		if (w != null) try { w.close(); } catch (Exception ex) { }
    		if (fos != null) try { fos.close(); } catch (Exception ex) { }
    	}
   
    } 
    
    
    
    
    public static void setContentsISO88591(String textFileName, String contents)
    throws IOException {
		
		FileOutputStream fos = null;
		OutputStreamWriter w = null;
		try {
		// new file and write BOM bytes first
		fos = new FileOutputStream(textFileName);
		byte[] bom = new byte[] { (byte)'ï', (byte)'»', (byte)'¿' };
		
		fos.write(bom);
		
		// open UTF8 writer
		w = new OutputStreamWriter(fos, "iso-8859-1");
		w.write(contents);
		w.flush();
		w.close();
		} finally {
			if (w != null) try { w.close(); } catch (Exception ex) { }
			if (fos != null) try { fos.close(); } catch (Exception ex) { }
		}
		
		} 
    
    

    public static List getContents(InputStream inputStream) throws IOException {
        ArrayList contents = new ArrayList();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            try {
                String line = bufferedReader.readLine();
                while (line != null) {
                    contents.add(line);
                    line = bufferedReader.readLine();
                }
            } finally {
                bufferedReader.close();
            }
        } finally {
            inputStreamReader.close();
        }
        return contents;
    }

    /**
     * Saves the List of Strings to a file with the given filename.
     * 
     * @param textFileName
     *                   the pathname of the file to create (or overwrite)
     * @param lines
     *                   the Strings to save as lines in the file
     * @throws IOException
     *                    if an I/O error occurs.
     */
    public static void setContents(String textFileName, List lines)
            throws IOException {
        String contents = "";

        for (Iterator i = lines.iterator(); i.hasNext(); ) {
            String line = (String) i.next();
            contents += (line + System.getProperty("line.separator"));
        }

        setContents(textFileName, contents);
    }

    public static void zip(Collection files, File zipFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFile);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            try {
                ZipOutputStream zos = new ZipOutputStream(bos);
                try {
                    for (Iterator i = files.iterator(); i.hasNext(); ) {
                        File file = (File) i.next();
                        zos.putNextEntry(new ZipEntry(file.getName()));
                        FileInputStream fis = new FileInputStream(file);
                        try {
                            BufferedInputStream bis = new BufferedInputStream(
                                    fis);
                            try {
                                while (true) {
                                    int j = bis.read();
                                    if (j == -1) {
                                        break;
                                    }
                                    zos.write(j);
                                }
                            } finally {
                                bis.close();
                            }
                        } finally {
                            fis.close();
                            zos.closeEntry();
                        }
                    }
                } finally {
                    zos.close();
                }
            } finally {
                bos.close();
            }
        } finally {
            fos.close();
        }
    }

    public static File addExtensionIfNone(File file, String extension) {
        if (GUIUtil.getExtension(file).length() > 0) {
            return file;
        }
        String path = file.getAbsolutePath();
        if (!path.endsWith(".")) {
            path += ".";
        }
        path += extension;
        return new File(path);
    }
    
    public static String parseISToStringUTF8(java.io.InputStream is) throws UnsupportedEncodingException{
	     
    	StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
               
        try{
            String line = null;
            while((line=reader.readLine()) != null){
                sb.append(line+"\n");
            }
        }catch(Exception ex){
            ex.getMessage();
        }finally{
            try{
                is.close();
            }catch(Exception ex){}
        }
        
        String bom = getBOM("UTF-8");
        
        if (sb.toString().startsWith(bom)){
        	int index = sb.toString().indexOf(bom);
        	return sb.toString().substring(index + bom.length());
        }
        
        
        return sb.toString();
    }
    
    
    
    
    
    public static String parseISO88591(java.io.InputStream is) throws UnsupportedEncodingException{
	     
    	StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
        String completeFileContent="";   
        
        try{
            String line = null;
            while((line=reader.readLine()) != null){
            	completeFileContent+=line+"\r\n";
                sb.append(line+"\n");
            }
        }catch(Exception ex){
            ex.getMessage();
        }finally{
            try{
                is.close();
            }catch(Exception ex){}
        }
        
        String bom = getBOM("iso-8859-1");
        
        if (sb.toString().startsWith(bom)){
        	int index = sb.toString().indexOf(bom);
        	return sb.toString().substring(index + bom.length());
        }
        
        
        return sb.toString();
    }
    
    
    
    
    
    
    public static String getBOM(String enc) throws UnsupportedEncodingException { 
    	if ("UTF-8".equals(enc)) {
    		byte[] bom = new byte[3]; 
    		bom[0] = (byte) 0xEF;
    		bom[1] = (byte) 0xBB;
    		bom[2] = (byte) 0xBF;
    		return new String(bom, enc);
    	} 
    	else if ("UTF-16BE".equals(enc)) { 
    		byte[] bom = new byte[2];          
    		bom[0] = (byte) 0xFE;           
    		bom[1] = (byte) 0xFF;           
    		return new String(bom, enc);     
    	} 
    	else if ("UTF-16LE".equals(enc)) {  
    		byte[] bom = new byte[2];          
    		bom[0] = (byte) 0xFF;         
    		bom[1] = (byte) 0xFE;           
    		return new String(bom, enc);  
    	} 
    	else if ("UTF-32BE".equals(enc)) {  
    		byte[] bom = new byte[4];  
    		bom[0] = (byte) 0x00;        
    		bom[1] = (byte) 0x00;        
    		bom[2] = (byte) 0xFE;        
    		bom[3] = (byte) 0xFF;       
    		return new String(bom, enc);  
    	}
    	else if ("UTF-32LE".equals(enc)) {  
    		byte[] bom = new byte[4];         
    		bom[0] = (byte) 0x00;          
    		bom[1] = (byte) 0x00;          
    		bom[2] = (byte) 0xFF;          
    		bom[3] = (byte) 0xFE;          
    		return new String(bom, enc);      
    	} 	
    	else if ("iso-8859-1".equals(enc)) {  
    		byte[] bom = new byte[3];         
    		bom[0]=(byte)'ï';
    		bom[1]=(byte)'»';
    		bom[2]=(byte)'¿';         
    		return new String(bom, enc);      
    	} 
    	
    	else {          
    		return null;    
    	}  
    }
    
    
    
    
    

}
