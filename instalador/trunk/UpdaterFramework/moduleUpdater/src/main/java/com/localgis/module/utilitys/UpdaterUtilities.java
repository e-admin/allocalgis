/**
 * UpdaterUtilities.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.utilitys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;


public class UpdaterUtilities {

	public static InputStream getJarInputStream(String fileToExtract,
			File jarFile) throws IOException {
		JarFile jar = new JarFile(jarFile);
		JarEntry entry = jar.getJarEntry(fileToExtract);
		InputStream inputFileStream = jar.getInputStream(entry);
		return inputFileStream;
	}

	public static void extractJarFileToDirectory(String fileToExtract,
			File jarFile, File targetDirectory) throws IOException {

		InputStream jarInputStream = null;

		OutputStream fileOutputStream = null;
		
		File outputFile=null;
		try {
			jarInputStream = getJarInputStream(fileToExtract, jarFile);

			File fileToExtractFile = new File(fileToExtract);

			outputFile = new File(targetDirectory,
					fileToExtractFile.getName());
			fileOutputStream = new FileOutputStream(outputFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = jarInputStream.read(buf)) > 0) {
				fileOutputStream.write(buf, 0, len);
			}

		} finally {
			try {
				jarInputStream.close();
			} catch (Exception e) {

			}
			try {
				fileOutputStream.close();
			} catch (Exception e) {

			}
		}
		
		//Quitamos la firma si existiera en el fichero jar que se copia
		try{	
			System.out.println("Eliminado firma del fichero a distribuir: " + outputFile.getAbsolutePath());		
			if (outputFile!=null){
				File resultadoSinFirma=clearSign(outputFile.getAbsolutePath(),false);
				//System.out.println("Fichero sin firma: " + resultadoSinFirma.getAbsolutePath());						
				Files.copy(resultadoSinFirma.toPath(), outputFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
				//tempFile=removeSign(jarFile,entry);
			}
		}
		catch(Exception e){			
		}
		
		

	}
	
	public static File createTempDirectory()
		    throws IOException
		{
		    final File temp;

		    temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

		    if(!(temp.delete()))
		    {
		        throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
		    }

		    if(!(temp.mkdir()))
		    {
		        throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
		    }

		    return (temp);
		}
	
	public static void extractAllFilesToDirectory(File jarFile, File targetDirectory) throws IOException {

		InputStream jarInputStream = null;

		OutputStream fileOutputStream = null;
		try {
			
			JarFile jar = new JarFile(jarFile);
			Enumeration enumeration=jar.entries();
			for (Enumeration e = jar.entries(); e.hasMoreElements(); ) {  
				 JarEntry entry=( JarEntry)e.nextElement();
			    //System.out.println("Fichero:"+entry);
			    if ((entry.getName().endsWith("/")) || (entry.getName().endsWith("\\"))){
			    	File fileToExtractFile = new File(entry.getName());
					fileToExtractFile.mkdirs();
					fileToExtractFile.mkdir();
			    }
			    else{
			    	File fileToExtractFile = new File(entry.getName());

					File outputFile = new File(targetDirectory,fileToExtractFile.getPath());
					
					File parent = outputFile.getParentFile();
					if(!parent.exists() && !parent.mkdirs()){
						System.out.println("No se puede crear el path de descompresion:"+parent.getAbsolutePath());
					}
					if (!outputFile.exists()) {
						outputFile.createNewFile();
					}
					fileOutputStream = new FileOutputStream(outputFile);

					jarInputStream = jar.getInputStream(entry);
					
					byte[] buf = new byte[1024];
					int len;
					while ((len = jarInputStream.read(buf)) > 0) {
						fileOutputStream.write(buf, 0, len);
					}
				
					if (fileOutputStream!=null)
						fileOutputStream.close();
			    }
				
			}
		}
		catch (Exception e){
			e.printStackTrace();
			
		}
		finally {		
			try {
				if (jarInputStream!=null)
					jarInputStream.close();
			} catch (Exception e) {				
			}
		}

	}

	public static File replaceJarFiles(File sourceJarFile, Properties properties) throws Exception 
	{
		File tempJarFile = null;
		JarFile jarFile = null;

		JarEntry entry = null;
		InputStream entryStream = null;
		StringWriter filtered = null;
		ZipEntry outEntry = null;
		String nameEntry = null;

		int bytesRead;
		byte[] buffer = new byte[1024];
		try {
			//Carga datos para filtro de ficheros
			List<List<String>> filteringFiles = loadDataFilteringFiles ();
			
			//Plantilla para sustitucion
			VelocityFilter velocityFilter = new VelocityFilter(properties);
			
			//Abrir fichero original para proceso y creamos temporal para resultados
			jarFile = new JarFile(sourceJarFile);
			tempJarFile = File.createTempFile(sourceJarFile.getName(), "tmp");
			tempJarFile.deleteOnExit();
			JarOutputStream tempJarStream = new JarOutputStream(new FileOutputStream(tempJarFile));

			//Proceso de cada entrada: generamos nuevo jar con sustituciones correspondientes
			for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();)
			{
				//Obtener entrada, incluir en jar resultado y procesar conversion de patrones segun corresponda
				entry = (JarEntry) entries.nextElement();
				
				File tempFile=null;
				if (entry.getName().endsWith(".jar")){
				//if (entry.getName().contains(("AbsoluteL"))){
					tempFile=removeSign(jarFile,entry);
					entryStream = new java.io.FileInputStream(tempFile);

				}
				else{
					entryStream = jarFile.getInputStream(entry);
				}
	
				
				// Read the entry and write it to the temp jar.
				nameEntry = entry.getName();
				
				//Evitamos incluir la entrada de Firma en el propio fichero WAR
				if (nameEntry!=null && (!nameEntry.equalsIgnoreCase("META-INF/GEOPISTA.DSA")) && (!nameEntry.equalsIgnoreCase("META-INF/GEOPISTA.SF"))){
						
					outEntry = new ZipEntry(nameEntry);
	
					tempJarStream.putNextEntry(outEntry);
	
					//Insertar conversion de patrones
					if (needFiltering(nameEntry, filteringFiles)) {
						filtered = new StringWriter((int) entry.getSize());
						velocityFilter.translateStream(new InputStreamReader(entryStream), filtered);
						tempJarStream.write(filtered.toString().getBytes());
					}
					else {
						// Copia binaria
						while ((bytesRead = entryStream.read(buffer)) > 0) {
							tempJarStream.write(buffer, 0, bytesRead);
						}
					}
				}
				entryStream.close();
				tempJarStream.closeEntry();		
				
				if(tempFile!=null){
					tempFile.delete();
				}
			}
			tempJarStream.close();

		 }catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			jarFile.close();
		}
		return tempJarFile;
	}
	
	
	private static File removeSign(JarFile originaljarFile, JarEntry entry) throws Exception{
		
		
		java.io.InputStream inStream;
		
		InputStream entryStreamOriginal = originaljarFile.getInputStream(entry);	
		
		int indice=entry.getName().lastIndexOf("/");		
		String nombreFicheroTemporal=entry.getName().substring(indice+1);
		OutputStream os = new FileOutputStream(nombreFicheroTemporal);  
		byte[] buffer = new byte[4096];  
		int bytesRead;  
		while ((bytesRead = entryStreamOriginal.read(buffer)) != -1) {  
		  os.write(buffer, 0, bytesRead);  
		}  
		entryStreamOriginal.close();  
		os.close();
		
		
		return clearSign(nombreFicheroTemporal,true);
		
	}
	
	private static File clearSign(String nombreFicheroTemporal,boolean deleteFile) throws Exception{
		
		File file=null;
		JarFile jarFile = null;
		File tempJarFile = null;
		int bytesRead; 
		byte[] buffer = new byte[4096];  
		
		try {
			file=new File(nombreFicheroTemporal);
			jarFile=new JarFile(file);
			JarEntry entrada;
			ZipEntry outEntry = null;
			String nameEntry = null;
			InputStream entryStream = null;
			
			tempJarFile = File.createTempFile(file.getName(), "tmp");
			//tempJarFile.deleteOnExit();
			JarOutputStream tempJarStream = new JarOutputStream(new FileOutputStream(tempJarFile));
			
			//System.out.println("Eliminado la firma del fichero:"+file.getName());
			
			for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();){
				entrada= (JarEntry) entries.nextElement();
				
				
				entryStream = jarFile.getInputStream(entrada);

				// Read the entry and write it to the temp jar.
				nameEntry = entrada.getName();
				outEntry = new ZipEntry(nameEntry);


				//Insertar conversion de patrones
				if (nameEntry!=null && (!nameEntry.equalsIgnoreCase("META-INF/GEOPISTA.DSA")) && (!nameEntry.equalsIgnoreCase("META-INF/GEOPISTA.SF"))){
					tempJarStream.putNextEntry(outEntry);
					// Copia binaria
					while ((bytesRead = entryStream.read(buffer)) > 0) {
						tempJarStream.write(buffer, 0, bytesRead);
					}
				}
			
				entryStream.close();
				tempJarStream.closeEntry();
			}
			tempJarStream.close();	
		} finally {
			
			
			if (jarFile!=null)jarFile.close();
			if (deleteFile)
				if (file!=null) file.delete();
		}
		
		return tempJarFile;
	}
	
	

	
	/**
	 * Carga datos de elementos de filtrado
	 * @return
	 * @throws IOException 
	 */
	private static List<List<String>> loadDataFilteringFiles () throws IOException {

		//FIXME Identificadores a utilizar: Se pueden definir como constantes de proceso
		String NAME_FILTERING_FILES = "/filtering_files.properties";
		String PATTERN_FILTER = "pattern_filter";
		String FILE_FILTER = "file_filter";

		String cadena = "";
		List<List<String>> lstResult = new ArrayList<List<String>>();

		//Cargamos datos del fichero de propiedades
		Properties propertiesFiltering= new Properties();
		InputStream resourceAsStream = UpdaterUtilities.class.getResourceAsStream(NAME_FILTERING_FILES);
		propertiesFiltering.load(resourceAsStream);

		//Generar listas con elementos a procesar
		//Elementos por extension
		cadena = propertiesFiltering.getProperty(PATTERN_FILTER);
		if (cadena != null)
			lstResult.add(0, Arrays.asList(cadena.split(";")));
		//Elementos concretos
		cadena = propertiesFiltering.getProperty(FILE_FILTER);
		if (cadena != null)
			lstResult.add(1, Arrays.asList(cadena.split(";")));

		return lstResult;
	}
	
	/**
	 * Determina si el fichero debe ser procesado o no segun configuraciones de fitro enviadas
	 * @param nameFile
	 * @param filteringFiles
	 * @return
	 */
	private static boolean needFiltering(String nameFile, List<List<String>> filteringFiles) {
		boolean filter = false;
		//Determinar si realizar reemplazo de patrones: entrada existe en conjunto de eltos por extension o por nombre completo
		if (filteringFiles != null) {
			String pattern = ((nameFile.lastIndexOf(".") != -1)? nameFile.substring(nameFile.lastIndexOf("."), nameFile.length()) : "");
			//Filtrar si: entrada existe en conjunto de eltos por extension o por nombre completo
			filter = (((List<String>)filteringFiles.get(0)).contains(pattern) || ((List<String>)filteringFiles.get(1)).contains(nameFile));
		}
		return filter;
	}

}
