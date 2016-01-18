/**
 * DataModelUpdater.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import com.localgis.module.updater.database.ConnectionUtilities;
import com.localgis.module.updater.database.OperationsDataBase;
import com.localgis.module.utilitys.UpdaterUtilities;
import com.localgis.tools.modules.exception.DependencyViolationException;

public class DataModelUpdater extends AbstractLocalGISUpdater {
	private static final String LOCALGIS_DATABASE_PASSWORD = "localgis_database_password";
	private static final String LOCALGIS_DATABASE_USERNAME = "localgis_database_username";
	private static final String LOCALGIS_DATABASE_URL = "localgis_database_url";
	
	private static final String LOCALGIS_DATABASE_NAME = "localgis_database_name";
	private static final String LOCALGIS_DATABASE_HOST = "localgis_database_host";
	private static final String LOCALGIS_DATABASE_PORT = "localgis_database_port";
	private static final String LOCALGIS_PSQL_PATH = "localgis_psql_path";
	
	private static final String NAME_INSTALL_SQL_FILE = "install.sql";
	private static final String NAME_UPGRADE_SQL_FILE = "upgrade.sql";
	
	
	private boolean validInstallation = false;
	private boolean validUninstallation = false;

	public DataModelUpdater(){
		
	}
	
	public void install() throws DependencyViolationException {
		String mensajeBase = getMessageResource("text.operacion.install");
		try {
			this.userInterfaceFacade.notifyActivity(mensajeBase + ": " + getFullFinalNameArtifact(getModule()));
			//Carga y ejecucion de sentencias SQL para instalacion
			if(existsPSQLPath()){
				execPSQLFile(NAME_INSTALL_SQL_FILE);
			}			
			else{
				execSQLFile(NAME_INSTALL_SQL_FILE);
			}
			validInstallation = true;
			showMessageSuccess(mensajeBase);
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
	}

	
	public void upgrade() throws DependencyViolationException {
		String mensajeBase = getMessageResource("text.operacion.upgrade");
		try {
			this.userInterfaceFacade.notifyActivity(mensajeBase + ": " + getFullFinalNameArtifact(getModule()));
			//Carga y ejecucion de sentencias SQL para instalacion
			execSQLFile(NAME_UPGRADE_SQL_FILE);
			validInstallation = true;
			showMessageSuccess(mensajeBase);
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
	}

	
	public void uninstall() throws DependencyViolationException {
		// TODO Auto-generated method stub
		validUninstallation = true;
	}

	
	public void installOrUpgrade() {
		// TODO Auto-generated method stub
	}

	
	public boolean checkInstallationValidity() {
		// TODO Auto-generated method stub
		// check script
		// validInstallation=validInstallation&checking();
		return validInstallation;
	}

	
	public boolean checkUninstallationValidity() {
		// TODO Auto-generated method stub
		return validUninstallation;
	}
	
		
	/**
	 * Procesa fichero sql para obtener lista con sentencias eliminando comentarios
	 * ATENCION: 
	 * 	SEPARADOR SENTENCIAS: La obtencion de sentencias se realiza utilizando como delimitador ; por lo que el fichero sql debe estar correctamente escrito delimitando cada sentencia con ;
	 * 	COMENTARIOS: Se eliminan los comentarios en bloque de una o varias lineas y en linea con //, -- y #
	 * 
	 * LIMITACIONES: Para el separador de sentencia (;) NO se interpretan de forma distinta cuando se encuentren dentro de una sentencia de forma textual,
	 *  por lo que sentencia se procesara de forma incorrecta (bien por fallo de ejecucion o por modificacion de la informacion)
	 * 
	 * EJEMPLO:	insert xxxx (campo1, ...) values (value1, ...) Donde value1 sea un campo de texto que contenga dicho caracter
	 * 
	 * @param nameSQLFile
	 * @return
	 * @throws IOException
	 */
	private void execSQLFile(String nameSQLFile) throws IOException, SQLException 
	{
		File jarFile = null;
		Scanner scanner = null;
		InputStream sqlFileInput = null;
		File sqlFileTemp = null;
		String separador = ";";
		
		int countOk = 0;
		int countError = 0;
		int countTotal = 0;
		
		String sentenciaSQL = "";
		Connection conexion = null;
		Exception exception = null;
		String mensajeBase = getMessageResource("text.proceso.procesandoFicheroSQL");
		try
		{
			showMessageInitProcess(mensajeBase);
			//Obtener fichero sql para procesar
			jarFile = getBinaryArtifact().getFile();
			sqlFileInput = UpdaterUtilities.getJarInputStream(nameSQLFile, jarFile);
			if (sqlFileInput != null) {
				//Procesar fichero y obtener temporal sin cometarios de linea
				sqlFileTemp = processFileForRemoveComments(nameSQLFile, sqlFileInput,true);
				if (sqlFileTemp != null) 
				{
					//Procesar fichero para obtener sentencias
					scanner = new Scanner(sqlFileTemp);
					if (scanner != null)  {
						//Establecer delimitador de sentencias
						scanner.useDelimiter("(" + separador + "(\\r|\\n)?)");
						
						//Iniciar proceso y ejecucion de sentencias
						conexion = getConectionDataBase();
						
						String line = "";
						//Procesar y ejecutar sentencias
						while (scanner.hasNext()) {
							line = scanner.next();
							//Procesar linea para obtener sql valido
							sentenciaSQL = procesarLineSQL(line);
							//Ejecutar sentencia si corresponde
							if (sentenciaSQL.trim().length() > 0) {
								//NO detener proceso aunque se produzcan errores: mostrar el error pero continuar el proceso
								try {
									OperationsDataBase.executeQuery(conexion, sentenciaSQL.trim());
								} catch (SQLException e) {
									exception = e;
									
								} catch (Exception e) {
									exception = e;
								}
								
								countTotal++;
								
								if (exception != null) {
									//Mostrar fallo de operacion en sentencia que falla pero continiamos operacion
									showMessageError(getMessageResource("text.proceso.ejecutandoSentenciasSQL") + " ("+ countTotal +") SQL: " + sentenciaSQL + " - [EXCEPTION]: " + exception.getMessage());
									exception = null;
									countError ++;
								}
								else
									countOk ++;
								
								//Mostrar sentencias procesadas
								//showMessageOk(" ("+ count +") SQL: " +  line.trim());
							}
						}
						//Mostrar numero de sentencias ejecutadas
						showMessageOk(MessageFormat.format(getMessageResource("text.proceso.numSentenciasEjecutadas"), countError, countOk, countTotal));
					}
				}
			}
			showMessageSuccess(mensajeBase);
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		} finally {
			if (sqlFileInput != null)
				sqlFileInput.close();
			if (sqlFileTemp != null)
				sqlFileTemp.deleteOnExit();
			//Cerrar conexion
			ConnectionUtilities.closeConnection(conexion);
		}
	}
	
	/**
	 * Procesa fichero sql utilizando psql
	 * @param nameSQLFile
	 * @throws IOException
	 * @throws SQLException
	 */
	private void execPSQLFile(String nameSQLFile) throws IOException, SQLException 
	{
		File jarFile = null;
		InputStream sqlFileInput = null;
		File sqlFileTemp = null;
		File tempDirectory=null;
		
		String mensajeBase = getMessageResource("text.proceso.procesandoFicheroSQL");
		try
		{
			showMessageInitProcess(mensajeBase);
			jarFile = getBinaryArtifact().getFile();
			
			
			tempDirectory=UpdaterUtilities.createTempDirectory();
			UpdaterUtilities.extractAllFilesToDirectory(jarFile,tempDirectory);
			
			sqlFileTemp=new File(tempDirectory+File.separator+nameSQLFile);
			//sqlFileInput = UpdaterUtilities.getJarInputStream(nameSQLFile, jarFile);
			//if (sqlFileInput != null) {
				//sqlFileTemp = processFileForRemoveComments(nameSQLFile, sqlFileInput,false);
				if (sqlFileTemp.exists()) 
				{
					ProcessBuilder pb = getPSQLProcess(sqlFileTemp.getAbsolutePath());
					pb.directory(tempDirectory);
					showMessageOk("Comando PSQL de ejecucion:"+pb.command());
					pb.redirectErrorStream(true);
					Process p = pb.start();
					//System.out.println("Proceso arrancado");
					
					StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
					StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
					outputGobbler.start();
					errorGobbler.start();
								
					int exitVal = p.waitFor();
					showMessageSuccess(print(p.getInputStream()));
				}			
		}catch (Exception e) {
			showMessageFailed(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (sqlFileInput != null)
				sqlFileInput.close();
			if (tempDirectory != null){
				tempDirectory.deleteOnExit();
			}
		}
	}
	
	/**
	 * Generar fichero temporal eliminando comentarios
	 * @param nameFile
	 * @param inputStream
	 * @return
	 */
	private File processFileForRemoveComments (String nameFile, InputStream inputStream,boolean removeComments) {
		String linea = "";
		File resultFile = null;
		OutputStream outputStream = null;
		BufferedReader bufferedReader = null;
		
		String mensajeBase = getMessageResource("text.proceso.eliminandoComentarios");
		try {
			//TODO: Pendiente determinar si puede contener informacion con caracteres acentuados, etc...
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			
			//-------------------------- Eliminar comentarios de linea: --
			resultFile = File.createTempFile(nameFile + "_tmp", "tmp");
			resultFile.deleteOnExit();
			outputStream = new FileOutputStream(resultFile);
			while((linea = bufferedReader.readLine())!=null)
				if (removeComments){			
					if (!linea.startsWith("--")){
						outputStream.write(linea.getBytes());
						outputStream.write("\n".getBytes());
					}
					else{
						outputStream.write(linea.getBytes());
						outputStream.write("\n".getBytes());
					}
				}
				else{
					outputStream.write(linea.getBytes());
					outputStream.write("\n".getBytes());
				}
			outputStream.close();
			
			showMessageOk(mensajeBase);
		} catch (Exception e) {
			showMessageError(mensajeBase);
			throw new RuntimeException("Error al procesar fichero" + e);
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return resultFile;
	}
	
	/**
	 * Obtiene sentencia SQL a ejecutar eliminado comentarios
	 * @param line
	 * @return
	 */
	private String procesarLineSQL (String line) {
		String cadena = line;
		if (line != null) {
			//Eliminar contenido comentario en bloque: /* */ (una o varias lineas)
			cadena = cadena.replaceAll("/\\*(?:.|[\\n\\r])*?\\*/","");
			//Remplazar valores comentarios dentro de elementos textuales: --,  // y #
			cadena = cadena.replaceAll("'((?:.)*)-{2,}((?:.)*)'", "'$1[COMMENT_1]$2'");
			cadena = cadena.replaceAll("'((?:.)*)/{2,}((?:.)*)'", "'$1[COMMENT_2]$2'");
			cadena = cadena.replaceAll("'((?:.)*)#{1,}((?:.)*)'", "'$1[COMMENT_3]$2'");
			//Eliminar comentarios:  --,  // y #
			cadena = cadena.replaceAll("(\r|\n)*(/{2,}|#{1,}|-{2,})(?:.)*(\\r|\\n)*","");
			//Restaurar valores textuales
			cadena = cadena.replaceAll("\\[COMMENT_1\\]","--");
			cadena = cadena.replaceAll("\\[COMMENT_2\\]","//");
			cadena = cadena.replaceAll("\\[COMMENT_3\\]","#");
		}
		if (cadena == null)
			cadena = "";
		
		return cadena;
	}
	
	/**
	 * Pinta el contenido de un InputStream
	 * @param inputStream
	 * @throws IOException
	 */
	private String print(InputStream inputStream) throws IOException{
		String result = "";
	    String line;
	    BufferedReader input=new BufferedReader(new InputStreamReader(inputStream));
	    while ((line = input.readLine()) != null) {        
	    	result += line;
	    }
	    return result;
	}
	
	/**
	 * Obtiene conexion con BdD
	 * @return
	 */
	private Connection getConectionDataBase () {
		Connection conn = null;
		try {
			String dbURL = this.properties.getProperty(LOCALGIS_DATABASE_URL);
			String dbUserName = this.properties.getProperty(LOCALGIS_DATABASE_USERNAME);
			String  dbPassword = this.properties.getProperty(LOCALGIS_DATABASE_PASSWORD);
			
			conn = OperationsDataBase.getConnection(dbURL, dbUserName, dbPassword);
		} catch (SQLException e1) {
			throw new RuntimeException(e1);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return conn;
	}
	
	private String getPSQLCommand(String sqlFilePath) {
		String command = "";
		try {
			String psqlPath = this.properties.getProperty(LOCALGIS_PSQL_PATH);
			String dbHost = this.properties.getProperty(LOCALGIS_DATABASE_HOST);
			String dbPort = this.properties.getProperty(LOCALGIS_DATABASE_PORT);
			String dbName = this.properties.getProperty(LOCALGIS_DATABASE_NAME);
			String dbUserName = this.properties.getProperty(LOCALGIS_DATABASE_USERNAME);
			String dbPassword = this.properties.getProperty(LOCALGIS_DATABASE_PASSWORD);
						
			command = "\""+psqlPath+"\"" + " -h " + dbHost + " -p " + dbPort + " -d " + dbName + " -U " + dbUserName + " -W -f " + sqlFilePath;
			
			//command = "\"" + psqlPath + "\" -h " + dbHost + " -p " + dbPort + " -d " + dbName + " -U " + dbUserName + " -f \"" + sqlFilePath + "\"";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return command;
	}
	
	private ProcessBuilder getPSQLProcess(String sqlFilePath) {
		ArrayList list =new ArrayList();
		ProcessBuilder pb=null;
		try {
			String psqlPath = this.properties.getProperty(LOCALGIS_PSQL_PATH);
			String dbHost = this.properties.getProperty(LOCALGIS_DATABASE_HOST);
			String dbPort = this.properties.getProperty(LOCALGIS_DATABASE_PORT);
			String dbName = this.properties.getProperty(LOCALGIS_DATABASE_NAME);
			String dbUserName = this.properties.getProperty(LOCALGIS_DATABASE_USERNAME);
			String dbPassword = this.properties.getProperty(LOCALGIS_DATABASE_PASSWORD);
					
			
			list.add(psqlPath);
			list.add("-h");
			list.add(dbHost);
			list.add("-p");
			list.add(dbPort);
			list.add("-d");
			list.add(dbName);
			list.add("-U");
			list.add(dbUserName);			
			//list.add("-W");
			list.add("-f");			
			list.add(sqlFilePath);
			
			pb = new ProcessBuilder(list);			
			Map<String, String> env = pb.environment();
			env.put("PGPASSWORD", dbPassword);
		} catch (Exception e) {	
			list.add("c:\\Program Files (x86)\\pgAdmin III\\1.16\\psql");
			list.add("-h");
			list.add("elearning.satec.es");			
			pb = new ProcessBuilder(list);
			Map<String, String> env = pb.environment();
			env.put("PGPASSWORD", "yoursecretpassword");
		}
		return pb;
	}
	
	private boolean existsPSQLPath(){
		try{
			if(this.properties.getProperty(LOCALGIS_PSQL_PATH)!=null){
				return true;
			}
		}
		catch(Exception e){		
			e.printStackTrace();
		}
		return false;
	}
	
	public void startSQL(){
		try {

			/*ArrayList list=new ArrayList();
			list.add("c:\\Program Files (x86)\\pgAdmin III\\1.16\\psql");
			list.add("-h");
			list.add("elearning.satec.es");			
			ProcessBuilder pb = new ProcessBuilder(list);
			*/
			
			ProcessBuilder pb =getPSQLProcess("XXX");
			//Map<String, String> env = pb.environment();
			//env.put("PGPASSWORD", "yoursecretpassword");

			Process p = pb.start();
			System.out.println("Proceso arrancado");
			
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
			StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");

			// start gobblers
			outputGobbler.start();
			errorGobbler.start();

			
			int exitVal = p.waitFor();
			System.out.println("Finalizando");
			
			/*Runtime rt = Runtime.getRuntime();
			String sql="\"c:\\Program Files (x86)\\pgAdmin III\\1.16\\psql\" -h elearning.satec.es -p 5432 -d geopista_probar_actualizado -U postgres -W -f C:\\Users\\fjgarcia\\AppData\\Local\\Temp\\install.sql_tmp7887926049962415332tmp";
			Process proc = rt.exec(sql);
			int exitVal = proc.waitFor();*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String args[]){
		new DataModelUpdater().startSQL();
	}
	
	private class StreamGobbler extends Thread {
	    InputStream is;
	    String type;

	    private StreamGobbler(InputStream is, String type) {
	        this.is = is;
	        this.type = type;
	    }

	    @Override
	    public void run() {
	        try {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line = null;
	            while ((line = br.readLine()) != null)
	                System.out.println(type + "> " + line);
	        }
	        catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	}
}
