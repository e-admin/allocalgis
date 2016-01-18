package com.geopista.app.backup;

public class BackupConfig {

	// Nombres de los ficheros:
	
	// Nombre del fichero de error:
	public static final String FILEERROR = "backupError.log";
	
	// Nombre del fichero con los datos comunes:
	public static final String FILECOMUN = "backupComun.sql";
	public static final String FILECOMUN_UTF8 = "backupComun_UTF8.sql";
	
	// Nombre del fichero con los datos del municipio:
	public static final String FILEENTIDAD = "backupEntidad.sql";
	public static final String FILEENTIDAD_UTF8 = "backupEntidad_UTF8.sql";
	
	// Nombre del fichero con el log generado en modo consola:
	public static final String FILELOG = "LogGeopista.log";
	
	//Nombre de la extension para la compresion
	public static final String FILEEXTENSION = ".gz";
	
	public static final String MSGLINEA1 = "--------------------";
	public static final String MSGLINEA2 = "---------------------------------------";
	
	// Linea de inicio para insertar al comienzo de los ficheros de backup: fileComun y fileMunicipio:
	public static final String SQLBEGIN = "begin;";
	
	// Linea de fin para insertar al final de los ficheros de backup: fileComun y fileMunicipio:
	public static final String SQLCOMMIT = "commit;";
	
	// Para insertar un salto de linea despues de cada línea del backup:
	public static final String NUEVALINEA = "\n";
}
