/**
 * CreadorCargador.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.creador;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.geopista.app.AppContext;
import com.geopista.cargador.LayerLoader;
import com.geopista.feature.Table;
import com.vividsolutions.jump.util.commandline.CommandLine;
import com.vividsolutions.jump.util.commandline.OptionSpec;
import com.vividsolutions.jump.util.commandline.ParseException;

/**
 * Clase para Crear capas y cargar datos a partir de SHP'S. Para que funcione correctamente tenemos que pasarle una serie de parámetros:
 *   - -userAplicacion syssuperuser (Usuario de la Aplicación)
 *   - -passwordAplicacion sysgeopass (Password de la Aplicación)
 *   - -userBBDD geopista (Usuario de la BBDD)
 *   - -passwordBBDD geopista (Password de la BBDD)
 *   - -rutaFicheroConfig C:\desarrollo\java\proyectos\LOCALGIS.BaseSatec\src\config\ejemploConfig.txt (Fichero que contiene la configuración
 *     de los SHP's que se van a cargar) 
 *   - -rutaFicherosSHP C:\Orense\CartografiaAdicional\ (Ruta donde se encuentran los SHP's que se van a cargar) 
 *   - -rutaAdmCart http://localhost:8081/geopista/AdministradorCartografiaServlet (Opcional: Ruta del Administrador de Cartografía)
 *   
 * IMPORTANTE: Para la correcta visualización de las capas que creemos en la GU:
 * En el fichero de configuración se indican entre otras cosas el nombre de las capas que deseamos cargar y crear, 
 * ese nombre no sólo será el nombre de las capas, sino su identificador. Si ese nombre/id tiene espacios no podremos publicar 
 * dicha capa en la GU. Posibles solusiones:
 *	1. No incluir en el fichero de configuración nombres de capas con espacios.
 *	2. Tras crear y cargar una capa con el CreadorCargador nos vamos al gestor de capas y modificamos su id a mano.
 *

 @author fjcastro
 */
public class CreadorCargador {
	
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private static Logger logger = Logger.getLogger("com.geopista.creador");
	protected String userAplicacion = null;
	protected String passwordAplicacion = null;
	protected String userBBDD = null;
	protected String passwordBBDD = null;	
	protected String rutaFicheroConfig = null;
	protected String rutaFicherosSHP = null;
	protected String rutaAdmCart = "http://localhost:8081/geopista/AdministradorCartografiaServlet";
	
	public final static String USER_APLICACION = "userAplicacion";
	public final static String PASSWORD_APLICACION = "passwordAplicacion";
	public final static String USER_BBDD = "userBBDD";
	public final static String PASSWORD_BBDD = "passwordBBDD";
	public final static String RUTA_FICHERO_CONFIG = "rutaFicheroConfig";
	public final static String RUTA_FICHEROS_SHP = "rutaFicherosSHP";
	public final static String RUTA_ADMIN_CART = "rutaAdmCart";
	
	protected static CommandLine commandLine;
	
	/**
	 * Clase Principal de la aplicacion
	 * @param args
	 */
	public static void main(String[] args) {
		CreadorCargador creadorCargador = new CreadorCargador();
		if (creadorCargador.start(args))
			creadorCargador.execute();			
	}   

	/**
	 * Clase que ejecuta el resto de clases necesarias para la aplicacion
	 * 
	 */	
    private void execute() {
		logger.log(Level.INFO,"***** INICIO DEL CREADOR Y CARGADOR DE SHP'S *****");
		CreaCapa creaCapa = new CreaCapa(); 
		
		List filas = creaCapa.devuelveFilasFichero(rutaFicheroConfig);

		Iterator it = filas.iterator();
		autentificacionBBDD();
		while (it.hasNext()){
			boolean error = Boolean.FALSE;
			String fila = (String)it.next();
			int codigoINE = new Integer(fila.split(";")[0]);
//			System.out.println("codigo: "+codigoINE);
			String digitosControl = fila.split(";")[1];
//			System.out.println("digitos: "+digitosControl);
			String familia =  fila.split(";")[2];
//			System.out.println("familia: "+familia);
			String nombreCapa = fila.split(";")[3];
//			System.out.println("capa: "+nombreCapa);
			String nombreTabla =  fila.split(";")[4];
//			System.out.println("tabla: "+nombreTabla);
			String sSHPFilePath = rutaFicherosSHP+fila.split(";")[5];			
//			System.out.println("shp: "+sSHPFilePath);
			// Nos autentificamos en el sistema y en la BBDD:
			
    		Table newTable = new Table();
			newTable.setDescription(nombreTabla);
			File fichero = new File(sSHPFilePath);
			logger.log(Level.INFO, "  **** INICIO CAPA: "+nombreCapa+" ("+codigoINE+") ****");	
			if (fichero.exists()){
				newTable.setGeometryType(creaCapa.devuelveGeometria(aplicacion, sSHPFilePath));
	
				logger.log(Level.INFO, "  - Opción: "+digitosControl);
				Boolean existeTabla = Boolean.FALSE;
				long miliseg = 0;
				// SI SE HA ESCOGIDO LA OPCIÓN DE RENOMBRAR LA TABLA Y LA CAPA (EN CASO DE QUE EXISTA):
				if (digitosControl.equals("RECREATE_AND_INSERT_DATA")){
					miliseg = System.currentTimeMillis();
					String nuevoNombreTabla = nombreTabla+"_"+miliseg;
					String nuevoNombreCapa = nombreCapa+"_"+miliseg;
					Table tabla = creaCapa.renombraTabla(aplicacion, nombreTabla, nuevoNombreTabla);
					creaCapa.renombraCapa(aplicacion, tabla, nuevoNombreCapa);
					
				}
				
				if ((digitosControl.equals("RECREATE_AND_INSERT_DATA")) || (digitosControl.equals("CREATE_AND_INSERT_DATA"))){
					
					// 1. Creamos la tabla:
					logger.log(Level.INFO, "    1. Creando la tabla...");
					
					// Comprobamos que no existe la tabla previamente en la BBDD para continuar con la creacion de la capa:
					if (!creaCapa.creaTabla(newTable)){
		
						// 2. Añadimos las columnas:
						logger.log(Level.INFO, "    2. Añadiendo las columnas a la tabla...");
						creaCapa.insertaColumnas(newTable, sSHPFilePath, aplicacion);
		
						// 3. Creamos la familia:
						logger.log(Level.INFO, "    3. Creando la familia...");
						String idFamilia = creaCapa.crearFamilia(familia);
						
						Integer idLayerFamily = new Integer(idFamilia);
						// 4. Creamos la capa:
					
						logger.log(Level.INFO, "    4. Creando la capa...");
						
						creaCapa.crearCapa(aplicacion, newTable, nombreCapa, idLayerFamily, digitosControl, miliseg);
											
					}
					else{
						error = Boolean.TRUE;
						logger.log(Level.INFO, "    No se ha podido crear correctamente la capa "+nombreCapa+", la tabla "+nombreTabla+" ya existe en el sistema.");
					}
				}
	/*			else{ // Aquí leemos el SHP y añadimos las columnas que falten en la tabla:
					if (digitosControl.equals("CREATE_COLUMN_AND_INSERT_DATA")){
						// 1. Añadiendo columnas:
						logger.log(Level.INFO, "    1. Actualizando columnas en la tabla...");
						Table tabla = creaCapa.obtenerTabla(aplicacion, nombreTabla);
						creaCapa.actualizaColumnasTabla(tabla, sSHPFilePath, aplicacion);
						
						logger.log(Level.INFO, "    2. Actualizando atributos en la capa...");
						creaCapa.actualizaAtributosCapa(aplicacion, newTable, nombreCapa);
					}
				}
	*/
				// Si no se ha producido ningún error al crear la capa, inserta los datos:
				if (!error){
					// CARGA DE DATOS UTILIZANDO LO DE LILIAN:
					LayerLoader layerLoader = new LayerLoader();
					URL urlAdministradorCartografia;
					try {
						urlAdministradorCartografia = new URL(rutaAdmCart);
						logger.log(Level.INFO, "    5. Cargando el SHP...");
						logger.log(Level.INFO, "    Nueva configuración ");
						layerLoader.load(aplicacion, codigoINE, nombreCapa, sSHPFilePath, urlAdministradorCartografia, userAplicacion, passwordAplicacion);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
			else{
				logger.log(Level.INFO, "    NO EXISTE EL FICHERO "+sSHPFilePath);	
			}
			logger.log(Level.INFO, "  **** FIN CAPA: "+nombreCapa+" ("+codigoINE+") ****");
		}
		logger.log(Level.WARNING, "***** FIN DEL CREADOR Y CARGADOR DE SHP'S *****");			
	}

    
	/**
	 * En este metodo nos autentificamos en el Sistema, tenemos que hacer la autentificacion en la aplicacion y
	 * posteriormente en la BBDD, ya que los metodos utilizados para crear la capa establecen una conexion directa
	 * con la BBDD.
	 * 
	 */
	private void autentificacionBBDD(){
		
		// Comprobamos que se ha introducido correctamente el usuario y password de la aplicación:
		if (!aplicacion.isLogged()) {
			aplicacion.loginNoGrafico(userAplicacion, passwordAplicacion);
		}
		if (!aplicacion.isLogged()) {
			logger.log(Level.WARNING, "Error en la conexión con la BBDD.");
			System.exit(1);
		}
		
       	// Para establecer la conexión directa con la BBDD, comprobamos que se ha introducido correctamente el usuario y password de la BBDD:
		
		try {
			aplicacion.getBlackboard().put("USER_BD", userBBDD);
			aplicacion.getBlackboard().put("PASS_BD", passwordBBDD);
			aplicacion.getBlackboard().put("DirectConnection", aplicacion.getJDBCConnection(userBBDD, passwordBBDD));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	protected boolean start(String[] args){
		/**
		 * Parseo de la linea de comandos.
		 */
    	if ((args.length==12)||(args.length==14)){
    		parseCommandLine(args);
    		if (commandLine.hasOption(USER_APLICACION)) {
    			userAplicacion = commandLine.getOption(USER_APLICACION).getArg(0);
    		}		
			if (commandLine.hasOption(PASSWORD_APLICACION)) {
				passwordAplicacion = commandLine.getOption(PASSWORD_APLICACION).getArg(0);
			}
			if (commandLine.hasOption(USER_BBDD)) {
				userBBDD = commandLine.getOption(USER_BBDD).getArg(0);
			}
			if (commandLine.hasOption(PASSWORD_BBDD)) {
				passwordBBDD = commandLine.getOption(PASSWORD_BBDD).getArg(0);
			}
			if (commandLine.hasOption(RUTA_ADMIN_CART)) {
				rutaAdmCart = commandLine.getOption(RUTA_ADMIN_CART).getArg(0);
			}
			if (commandLine.hasOption(RUTA_FICHERO_CONFIG)) {
				rutaFicheroConfig = commandLine.getOption(RUTA_FICHERO_CONFIG).getArg(0);
			}
			if (commandLine.hasOption(RUTA_FICHEROS_SHP)) {
				rutaFicherosSHP = commandLine.getOption(RUTA_FICHEROS_SHP).getArg(0);
			}
			return true;
    	}
    	else{
    		logger.log(Level.INFO, "Tiene que introducir 6 o 7 valores: -userAplicacion usuarioAplicacion -passwordAplicacion passwordAplicacion -userBBDD usuarioBBDD -passwordBBDD passwordBBDD -rutaFicheroConfig ficheroConfiguracion -rutaFicherosSHP rutaFicherosSHP [-rutaAdmCart http://localhost:8081/geopista/AdministradorCartografiaServlet]");
    		return false;
    	}
	}
	
	/**
	 * Parseo de los argumentos
	 * 
	 * @param args
	 */
	protected static void parseCommandLine(String[] args) {
		commandLine = new CommandLine('-');
		commandLine.addOptionSpec(new OptionSpec(USER_APLICACION, 1));
		commandLine.addOptionSpec(new OptionSpec(PASSWORD_APLICACION, 1));
		commandLine.addOptionSpec(new OptionSpec(USER_BBDD, 1));
		commandLine.addOptionSpec(new OptionSpec(PASSWORD_BBDD, 1));
		commandLine.addOptionSpec(new OptionSpec(RUTA_ADMIN_CART, 1));
		commandLine.addOptionSpec(new OptionSpec(RUTA_FICHERO_CONFIG, 1));
		commandLine.addOptionSpec(new OptionSpec(RUTA_FICHEROS_SHP, 1));
		try {
			commandLine.parse(args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
