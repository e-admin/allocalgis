/**
 * Módulo para la creación de Backup en modo consola, se le tienen que pasar 2
 * argumentos:
 * 1. La ruta donde se quieren guardar los ficheros backup que se generan.
 * 2. El codigo INE (id del municipio) del que se quiere hacer el backup, poner
 * -1 si se quiere hacer de toda la Base de Datos. 
 *
*/

package com.geopista.app.backup.consola;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.zip.GZIPOutputStream;

import com.geopista.app.backup.BackupAdapter;
import com.geopista.app.backup.BackupConfig;
import com.geopista.app.backup.BackupOracle;
import com.geopista.app.backup.BackupPostgres;
import com.geopista.app.backup.BackupPreferences;
import com.geopista.app.backup.InformacionTabla;
import com.geopista.app.backup.SecurityManager;
import com.vividsolutions.jump.I18N;

public class BackupConsola {
	class Pair<A,B>{
		A itemA;
		B itemB;
	}
	
	/**
	 * @param args
	 */
    private final BackupPreferences backupPreferences = BackupPreferences.getInstance();
    private BackupAdapter backupAdapter;
    
    /**
     * Main de la aplicacion
     * args[0] --> Usuario BD
     * args[1] --> Password BD
     * args[2] --> Ruta destino de los backup
     * args[3] --> id de entidad o * en caso de backup completo
     * @param args
     */
	public static void main(String[] args) {
		switch  (args.length){
			case 3: 
				backupCompleto(args[0], args[1], args[2]);
                break; 			
			case 4:
				try{
					int idEntidad = Integer.parseInt(args[3]);
					BackupConsola backupConsola = new BackupConsola(args[0],args[1]);
			        backupConsola.execute(args[2],idEntidad, null);
				}catch(Exception ex){
					showHelp();
				}
				break;
			case 5:
				try{
					int idEntidad = Integer.parseInt(args[3]);
					BackupConsola backupConsola = new BackupConsola(args[0],args[1]);
			        backupConsola.execute(args[2],idEntidad, args[4]);
				}catch(Exception ex){
					showHelp();
				}
				break;
			default: 
				showHelp();
			    break;
			
		}
	}
	
	/**
	 * TODO: SI HAY MOVIDAS DE MEMORIA.
	 * 
	 * Si por lo que sea hay problemas de memoria con este modo de proceso, lo mejor va a ser simplemente:
	 * 1.- Ejecutar la consulta que devuelve los objetos entidad+CodAyto.
	 * 2.- Escribir por System.out un string de tokens "id_codAyto" (como la que se usa ahora en el backup.sh -> ENTIDADES).
	 * 3.- En el script de backup hacer algo tipo ENTIDADES=`java ...`
	 * 
	 * 
	 * @param args
	 */
	private static void backupCompleto(String usuarioBD, String passwordBD, String rutaDestinoBackup){
		
		System.out.println("Realizando backup de todos los Ayuntamientos: ");
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE");
		String diaSemana = sdf.format(new java.util.Date());

		BackupConsola backupConsola= new BackupConsola(usuarioBD, passwordBD);
		Hashtable<Integer,String> listaAyuntamientos  = backupConsola.getAyuntamientos();
		if (listaAyuntamientos==null){
			System.exit(1);
		}
		System.out.println("Numero de entidades:"+listaAyuntamientos.size());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date());
		boolean diaDeHoyImpar = (calendar.get(Calendar.DAY_OF_MONTH)%2)==1;
		
		for(Enumeration<Integer> e=listaAyuntamientos.keys();e.hasMoreElements();){
			Integer idEntidad=(Integer)e.nextElement();
			if ((idEntidad.intValue()%2==1 && !diaDeHoyImpar) || 
			    (idEntidad.intValue()%2==0 && diaDeHoyImpar)) continue;
			
			String municipio=(String)listaAyuntamientos.get(idEntidad);

			String rutaDestinoBackupEntidad=rutaDestinoBackup + File.separator + idEntidad + "_" + municipio + "_" + diaSemana;
			
			System.out.println(getLogHeader()+ "[INICIO] Realizando backup sobre: "+ rutaDestinoBackupEntidad);
			try{
		        //BackupConsola backupConsola = new BackupConsola();
		        backupConsola.execute(rutaDestinoBackupEntidad, idEntidad,null);
		        //backupConsola = null; //Ayudar GC
				System.out.println(getLogHeader()+ "[FIN] OK: "+ rutaDestinoBackupEntidad);
				
			}catch(Throwable th){
				System.out.println(getLogHeader()+ "[FIN] ERROR: "+ rutaDestinoBackupEntidad);
				th.printStackTrace();
			}
		}
		System.out.println("Backup finalizaco con EXITO");
        backupConsola = null; //Ayudar GC
        System.exit(0);
	}
	
	/**Este método devolverá una lista de objetos PAR, donde el itemA será la ENTIDAD y el itemB el CODIGO_AYUNTAMIENTO*/
	private  Hashtable<Integer,String> getAyuntamientos() {
		Connection connection = getConnection();
    	if (connection==null) {
    		System.out.println(getLogHeader()+"No hay Cnx con bbdd.");
    		return null;
    	}
    	try{
    	    return backupAdapter.obtenerEntidadesYMunicipiosBackup(connection);
    	}catch(Exception ex){
    		System.out.println("Error al devolver la lista de municipios:");
    		ex.printStackTrace();
    	    return null;	
    	}finally{
    		try { connection.close(); } catch(Exception e){};
    	}
	}

	private static void showHelp(){
		System.out.println("USO: \n");
		System.out.println("		BackupConsola DBUsr DBPwd Path Entidad --> Para hacer el backup de una entidad\n ");
		System.out.println("		BackupConsola DBUsr DBPwd Path --> Para hacer el backup de todas las entidades del sistema\n ");
		System.out.println(" Si es un BackupCompleto, Colgando del Path se crearan directorios IdEntidad_CodMunicipio_DiaSemana");
		System.exit(1);
	}

	private BackupConsola(String usuarioBD, String passwordBD) {
		backupPreferences.setUser(usuarioBD);
		backupPreferences.setPassword(passwordBD);
		String url = backupPreferences.getUrl();
		/*
		 *  Determinamos a traves de la información de preferencia 
		 *  el tipo de base de datos de geopista y en funcion de 
		 * 	ello instanciamos un tipo de backup u otro
		 *  
		 */
    
		if (url.indexOf("jdbc:postgresql")>=0) {
			backupAdapter = new BackupPostgres();
		}else { 
			if (url.indexOf("jdbc:oracle")>=0) {
				backupAdapter = new BackupOracle();
                
            }
		}	
		/*
         * Cargamos la clase del driver que vamos a usar para
         * la conexión jdbc
         * 
         */
        try {
            Class.forName(backupAdapter.obtenerClaseDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

	}

    private Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(backupPreferences.getUrl(),backupPreferences.getUser(),backupPreferences.getPassword());
            return connection;
        } catch (SQLException e) {          
            e.printStackTrace();
        }
        return null;
    }
	
    public void execute( String rutaDestinoBackup, int idEntidad,String rutaPostgres) throws IOException, InterruptedException {
    	//*******************************
        //Cargamos los recursos
        //*******************************
    	if(rutaPostgres!=null){
    	//	String rutaarchivo="C:\\Documents and Settings\\borja.caveda\\Mis documentos\\backup eclipe Illas\\";
		//	Process miProceso2 = Runtime.getRuntime().exec("cmd.exe / "+rutaPostgres+" /pg_dump -U postgres -s geopista > "+rutaarchivo+"esquema.sql"); 
			//cmd.exe /E:1900 /C copy C:\\this.txt D:\\that.txt
		//	System.out.println(miProceso2.getInputStream());
			//miProceso2.waitFor();
    	}
    	Locale loc = I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.backup.ui.language.BackupSupraMunicipali18n", loc, this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("BackupSupraMunicipal", bundle);
        
        boolean isAuthorized = false;
        isAuthorized = SecurityManager.isAuthorized(backupPreferences.getUser(),backupPreferences.getPassword());
	    if (!isAuthorized) {
	    	System.out.println(getLogHeader()+"No se ha autentificado correctamente.");
	    	return;
	    }
    	PrintStream logger = null;
        PrintStream errorLog = null;
        GZIPOutputStream salidaEntidadSql = null;
        GZIPOutputStream salidaComunSql = null;
        File backupComun =null;
    	File backupEntidad =null;
    	GZIPOutputStream salidaEntidadSql_UTF8 = null;
    	GZIPOutputStream salidaComunSql_UTF8 = null;
        File backupComun_UTF8 =null;
    	File backupEntidad_UTF8 =null;
        Connection connection = null;
        try {
      	
    		String directorio = "";

            directorio = rutaDestinoBackup.replace("\\", "\\\\");
        
            File directorioBase= new File(directorio);
           deleteFiles(directorioBase);
            try{directorioBase.mkdirs();}catch(Exception ex){}
    	
        		
    		File logBackup = new File(directorio, BackupConfig.FILELOG);
    		logBackup.createNewFile();
    		logger = new PrintStream(new FileOutputStream(logBackup, false), true);
		
    		File error = new File(directorio, BackupConfig.FILEERROR);
    		error.createNewFile();
            errorLog = new PrintStream(new FileOutputStream(error, false), true);

    		backupEntidad = new File(directorio, BackupConfig.FILEENTIDAD+BackupConfig.FILEEXTENSION);
    		backupEntidad.createNewFile();
            salidaEntidadSql = new GZIPOutputStream(new FileOutputStream(backupEntidad, false));
            /**
             * Creacion de archivos utf8
             */
            
            backupEntidad_UTF8 = new File(directorio, BackupConfig.FILEENTIDAD_UTF8+BackupConfig.FILEEXTENSION);
    		backupEntidad_UTF8.createNewFile();
            salidaEntidadSql_UTF8 = new GZIPOutputStream(new FileOutputStream(backupEntidad_UTF8, false));
            
            backupComun_UTF8 = new File(directorio, BackupConfig.FILECOMUN_UTF8+BackupConfig.FILEEXTENSION);
            backupComun_UTF8.createNewFile();
            salidaComunSql_UTF8 = new GZIPOutputStream(new FileOutputStream(backupComun_UTF8, false)); 
            
            
            /**
             * Creacion de archivos utf8
             */
    		
            backupComun = new File(directorio, BackupConfig.FILECOMUN+BackupConfig.FILEEXTENSION);
            backupComun.createNewFile();
            salidaComunSql = new GZIPOutputStream(new FileOutputStream(backupComun, false));

            errorLog.println(getLogHeader()+"Realizando Backup...");
            errorLog.println(getLogHeader()+"Ruta: "+rutaDestinoBackup);
            errorLog.println(getLogHeader()+"Id Entidad: "+idEntidad);

    		logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.realizandoBackup"));
    		logger.println(getLogHeader()+BackupConfig.MSGLINEA1);
    		errorLog.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.realizandoBackup"));
    		errorLog.println(getLogHeader()+BackupConfig.MSGLINEA1);


        	connection = getConnection();
        	if (connection==null) {
        		errorLog.println(getLogHeader()+"No hay Cnx con bbdd.");
        		return;
        	}
		
    		
    		String municipios = backupAdapter.obtenerStringMunicipiosEntidad(connection, idEntidad);

    		/*
             * 1. Obtenemos las tablas comunes de las que hay que hacer backup
             */
logger.println(getLogHeader()+"Paso 1");
        	InformacionTabla[] tablasComunes = backupAdapter.obtenerTablasComunes(connection);
			logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasComunes"));
			
	        /*
	         * 2. Obtenemos las tablas dependientes del municipio de las que hay que
	         * hacer backup
	         */
logger.println(getLogHeader()+"Paso 2");
			InformacionTabla[] tablasMunicipio = backupAdapter.obtenerTablasMunicipio(connection);
			logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasMunicipio"));
	  	 	writeFile(salidaComunSql,BackupConfig.SQLBEGIN);
	  	 	writeFile(salidaEntidadSql,BackupConfig.SQLBEGIN);
	  	 	writeFile(salidaComunSql_UTF8,BackupConfig.SQLBEGIN);
	  	 	writeFile(salidaEntidadSql_UTF8,BackupConfig.SQLBEGIN);
	  	 	
	  	 	/*
	  	 	 * 3. Creamos el script de cosas comunes que puedan hacer falta para realizar el resto de las operaciones 
	  	 	 */
logger.println(getLogHeader()+"Paso 3");
	        backupAdapter.crearScriptInicial(connection, salidaComunSql, errorLog);
	        logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.creando"));
	        if ((tablasComunes == null )||(tablasMunicipio == null)) {
	        	errorLog.println(getLogHeader()+"(tablasComunes == null )||(tablasMunicipio == null)");
			            return;
			        }
logger.println(getLogHeader()+"Paso 3a");

	        logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.scriptTablasComunes"));
	        for (int i = 0; i < tablasComunes.length; i++) {
	            InformacionTabla tabla = tablasComunes[i];
	            String deshabilitarConstraints = backupAdapter.obtenerSentenciaDeshabilitarConstraints(tabla,connection);
	            logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.deshabilitandoConstraint")+ " " + i +" " + I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasComunes"));
			            writeFile(salidaComunSql, deshabilitarConstraints);  
			            writeFile(salidaComunSql_UTF8, deshabilitarConstraints); 
			        }
logger.println(getLogHeader()+"Paso 3b");			        
	        for (int i = 0; i < tablasComunes.length; i++) {
	            InformacionTabla tabla = tablasComunes[i];
	            backupAdapter.crearScriptUTF8(connection, salidaComunSql, errorLog, tabla, false, municipios,salidaComunSql_UTF8);
	            logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.geopista.app.backup.creandoScript")+ " " + i +" " + I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasComunes"));
			        }
		
logger.println(getLogHeader()+"Paso 3c");			        
	        for (int i = 0; i < tablasComunes.length; i++) {
	            InformacionTabla tabla = tablasComunes[i];
	            String habilitarConstraints = backupAdapter.obtenerSentenciaHabilitarConstraints(tabla,connection);
	            logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.geopista.app.backup.habilitandoConstraint")+ " " + i +" " + I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasComunes"));
	             writeFile(salidaComunSql,habilitarConstraints);  
	             writeFile(salidaComunSql_UTF8,habilitarConstraints);
	        }

	        /*
	         * Actualizamos las secuencias de las tablas comunes a los ultimos valores
	         */
	    	
	        logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.actualizandoTablasComunes"));
logger.println(getLogHeader()+"Paso 3d");			        
	        for (int i = 0; i < tablasComunes.length; i++) {
	            String sentencia = backupAdapter.obtenerSentenciaActualizarSecuencia(tablasComunes[i]);
	            if (sentencia != null) {
	                writeFile(salidaComunSql,sentencia);
	                writeFile(salidaComunSql_UTF8,sentencia);
	            }
	        }
	        
	        logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.scriptTablasMunicipio"));
logger.println(getLogHeader()+"Paso 3e");			        
	        for (int i = 0; i < tablasMunicipio.length; i++) {
	            InformacionTabla tabla = tablasMunicipio[i];
	            String deshabilitarConstraints = backupAdapter.obtenerSentenciaDeshabilitarConstraints(tabla,connection);
	            logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.deshabilitandoConstraint")+ " " + i +" " + I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasMunicipio"));
			            writeFile(salidaEntidadSql, deshabilitarConstraints); 
			            writeFile(salidaEntidadSql_UTF8, deshabilitarConstraints);
			        }
			        
logger.println(getLogHeader()+"Paso 3f");			        
	        for (int i = 0; i < tablasMunicipio.length; i++) {
	            logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.deshabilitandoConstraint")+ " " + i +" " + I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasMunicipio"));
			            InformacionTabla tabla = tablasMunicipio[i];
			            backupAdapter.crearScriptUTF8(connection, salidaEntidadSql, errorLog, tabla, true, municipios,salidaEntidadSql_UTF8);    
			        }      
logger.println(getLogHeader()+"Paso 3g");
	        for (int i = 0; i < tablasMunicipio.length; i++) {
	            InformacionTabla tabla = tablasMunicipio[i];
	            String habilitarConstraints = backupAdapter.obtenerSentenciaHabilitarConstraints(tabla,connection);
	            logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.habilitandoConstraint")+ " " + i +" " + I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasMunicipio"));
	            writeFile(salidaEntidadSql,habilitarConstraints);
	            writeFile(salidaEntidadSql_UTF8, habilitarConstraints);

	        }
	        
	        logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.actualizandoTablasMunicipio"));
logger.println(getLogHeader()+"Paso 3h");
	        for (int i = 0; i < tablasMunicipio.length; i++) {
	            String sentencia = backupAdapter.obtenerSentenciaActualizarSecuencia(tablasMunicipio[i]);
	            if (sentencia != null) {
	                writeFile(salidaEntidadSql,sentencia);  
		            writeFile(salidaEntidadSql_UTF8, sentencia);

	            }
	        }    
	        
	        writeFile( salidaEntidadSql,BackupConfig.SQLCOMMIT);
	        writeFile(salidaComunSql,BackupConfig.SQLCOMMIT); 
	        writeFile( salidaEntidadSql_UTF8,BackupConfig.SQLCOMMIT);
	        writeFile(salidaComunSql_UTF8,BackupConfig.SQLCOMMIT); 
	        logger.println(getLogHeader()+BackupConfig.MSGLINEA2);
	        logger.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.exito"));

	        errorLog.println(getLogHeader()+BackupConfig.MSGLINEA2);
	        errorLog.println(getLogHeader()+I18N.get("BackupSupraMunicipal", "geopista.app.backup.exito"));
	       // generarFicherosBackupUTF8(directorioBase);
	       // convertFile();
		} catch (Throwable e) {
			if(errorLog!=null) {
				errorLog.println(getLogHeader()+"Excepcion: "+e.toString());
				e.printStackTrace(errorLog);
			} else {
				e.printStackTrace();
			}
		} finally {
	        logger.println(getLogHeader()+BackupConfig.MSGLINEA2);
	        errorLog.println(getLogHeader()+BackupConfig.MSGLINEA2);

	        try { logger.flush(); } catch(Exception e){}
			try { logger.close(); logger=null; } catch(Exception e){}
			
			try { errorLog.flush(); } catch(Exception e){}
			try { errorLog.close();errorLog=null; } catch(Exception e){}

			try { salidaEntidadSql.flush(); } catch(Exception e){}
			try { salidaEntidadSql.close();salidaEntidadSql=null; } catch(Exception e){}
			
			try { salidaEntidadSql_UTF8.flush(); } catch(Exception e){}
			try { salidaEntidadSql_UTF8.close();salidaEntidadSql_UTF8=null; } catch(Exception e){}

			try { salidaComunSql.flush(); } catch(Exception e){}
			try { salidaComunSql.close(); salidaComunSql=null;} catch(Exception e){}
			
			try { salidaComunSql_UTF8.flush(); } catch(Exception e){}
			try { salidaComunSql_UTF8.close(); salidaComunSql_UTF8=null;} catch(Exception e){}
			
			try { connection.close(); connection=null;} catch(Exception e){};
			
			//Para limpiar la memoria
			System.gc();
			
		}
    	return;
	 }
    
    public static void writeFile(FilterOutputStream out, String cadena){
    	try{
    		if (cadena==null || out==null) return;
    		out.write(cadena.getBytes());
    		
    		out.write('\n');
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    private static final SimpleDateFormat SDF = new SimpleDateFormat("[yyyy/MM/dd_HH:mm:ss.SSS]");
    public static String getLogHeader(){
    	Runtime rt = Runtime.getRuntime();
    	long total = rt.totalMemory();
    	long libre = rt.freeMemory();
    	long ocupada = total - libre;
    	return SDF.format(new java.util.Date()) + "[MemOcup:"+(ocupada/1024/1024)+"Mb] ";
    }
    
    /* public boolean deleteDirectory(File path) {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
               deleteDirectory(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return( path.delete() );
      }
    public boolean createDirectory(File path) {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
               deleteDirectory(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return( path.delete() );
      } */
    /**
     * 
     */
    private void generarFicherosBackupUTF8(File directorySelected){
		try {
			//File backupMunicipio = new File(directorySelected, BackupConfig.FILEENTIDAD);			
			//File backupMunicipio_UTF8 = new File(directorySelected, BackupConfig.FILEENTIDAD_UTF8);
			//backupMunicipio_UTF8.createNewFile();
			File backupComun = new File(directorySelected, BackupConfig.FILECOMUN+BackupConfig.FILEEXTENSION);
			File backupComun_UTF8 = new File(directorySelected, BackupConfig.FILECOMUN_UTF8);
			backupComun_UTF8.createNewFile();
			//convertFile(backupMunicipio,backupMunicipio_UTF8);			        
			convertFile(backupComun,backupComun_UTF8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			        
    }
    
    /**
     * Lo convertimos a UTF-8
     * @param backupFile
     * @param backupFileUTF8
     */
    private void convertFile(File backupFile,File backupFileUTF8){
    	
    	try {
			BufferedReader reader= new BufferedReader(new FileReader(backupFile));
			
			Writer out = new BufferedWriter(new OutputStreamWriter(
		            new FileOutputStream(backupFileUTF8), "UTF8"));

			String linea= reader.readLine();
			while(linea!=null) { 
				out.write(linea+"\n");
				linea= reader.readLine(); 
			}
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
    /**
     * 
     *

     * Borramos archivos previos de backup en el mismo direrctorio.
     */
    private void deleteFiles(File ruta){
    	
              
        //Borramos archivo BackupComun
        File comun = new File(ruta, BackupConfig.FILECOMUN+BackupConfig.FILEEXTENSION);
        if(comun.exists()){
    	   comun.delete();
        }
       	//Borramos archivo BackupEntidad
        File entidad = new File(ruta, BackupConfig.FILEENTIDAD+BackupConfig.FILEEXTENSION);           
        if(entidad.exists()){
    	   entidad.delete();
        }
        //Borramos archivo backupError
        File logError = new File(ruta, BackupConfig.FILEERROR);
        if(logError.exists()){
    	   logError.delete();
        }
        //Borramos archivo backUpComun_UTF8
        File comunUTF8 = new File(ruta, BackupConfig.FILECOMUN_UTF8+BackupConfig.FILEEXTENSION);
        if(comunUTF8.exists()){
    	   comunUTF8.delete();
        }
        //Borramos archivo backUpEntidad_UTF8
        File entidadUTF8 = new File(ruta, BackupConfig.FILEENTIDAD_UTF8+BackupConfig.FILEEXTENSION);
        if(entidadUTF8.exists()){
    	   entidadUTF8.delete();
        }
        //Borramos archivo log
        File log = new File(ruta, BackupConfig.FILELOG);
        if(log.exists()){
    	   log.delete();
        }
    }
}

