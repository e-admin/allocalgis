import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;


public class RemoveSign {

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
		try {
			jarInputStream = getJarInputStream(fileToExtract, jarFile);

			File fileToExtractFile = new File(fileToExtract);

			File outputFile = new File(targetDirectory,
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
			//List<List<String>> filteringFiles = loadDataFilteringFiles ();
			
			//Plantilla para sustitucion
			//VelocityFilter velocityFilter = new VelocityFilter(properties);
			
			//Abrir fichero original para proceso y creamos temporal para resultados
			jarFile = new JarFile(sourceJarFile);
			tempJarFile = File.createTempFile(sourceJarFile.getName(), "tmp");
			//tempJarFile =sourceJarFile;
			//tempJarFile.deleteOnExit();
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
				//System.out.println("Entrada:"+nameEntry);
				if (nameEntry!=null && (!nameEntry.equalsIgnoreCase("META-INF/GEOPISTA.DSA")) && (!nameEntry.equalsIgnoreCase("META-INF/GEOPISTA.SF"))){
						
					outEntry = new ZipEntry(nameEntry);
	
					tempJarStream.putNextEntry(outEntry);
	
					//Insertar conversion de patrones
					/*if (needFiltering(nameEntry, filteringFiles)) {
						filtered = new StringWriter((int) entry.getSize());
						velocityFilter.translateStream(new InputStreamReader(entryStream), filtered);
						tempJarStream.write(filtered.toString().getBytes());
					}
					else {*/
						// Copia binaria
						while ((bytesRead = entryStream.read(buffer)) > 0) {
							tempJarStream.write(buffer, 0, bytesRead);
						}
					/*}*/
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
			//tempJarFile.close();
	
		}
		return tempJarFile;
	}
	
	
	private static File removeSign(JarFile originaljarFile, JarEntry entry) throws Exception{
		
		File tempJarFile = null;
		JarFile jarFile=null;
		File file=null;
		
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
			if (file!=null) file.delete();
		}
		
		return tempJarFile;
	}

	 public static void main(String args[]){		
	 
		try{
			String fichero=args[0];
			File tempFile=RemoveSign.replaceJarFiles(new File(fichero),null);		

			new File(fichero).delete();
			boolean isRenamed=tempFile.renameTo(new File(fichero));
			if (isRenamed)
				System.out.println("Reemplazado de fichero:"+fichero+" terminado");
		}
		catch (Exception e){
		}
		
		
	}
}
