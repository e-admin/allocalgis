/**
 * FileUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.wm.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtil {
	
	private static Log log = LogFactory.getLog(FileUtil.class);
	
	private static final String testString = "This is a test string";
	
	private static final String msErrorPath = "/tmp/ms_error.txt";
	
	public static String newLine = System.getProperty("line.separator");
	
	public static boolean activeErrorFile(){
		try{
			File file = new File(msErrorPath);			
			if(createWritableFileIfNotExists(file)){
				if(writeInFile(file)){
					return true;
				}
			}
		}
		catch(Exception ex){
			log.error("FileUtil - activeErrorFile(): ",ex);
		}
		return false;
	}
	
	public static boolean createWritableFileIfNotExists(File file) throws IOException{
		file.getParentFile().mkdirs();
			if(!file.exists()){
				file.setExecutable(true);
				file.setReadable(true);
				file.setWritable(true);
				if (file.createNewFile()){
					return file.exists();
				}
			}
			else{
				return true;
			}
		
		return false;
	}
	
	public static boolean writeInFile(File file) throws IOException{
		if(file.canWrite()){
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			//FileWriter outFile = new FileWriter(file);
			//PrintWriter out = new PrintWriter(outFile);			 
			out.append(getCurrentDateTime() + ": " + testString + newLine);  
			out.close();
			return true;
		}
//		else{
//			 if(createWritableFileIfNotExists(file));
//		}
		return false;
	}
	
	public static String getCurrentDateTime(){
		 Calendar cal = Calendar.getInstance();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 return sdf.format(cal.getTime());
	}
	
}
