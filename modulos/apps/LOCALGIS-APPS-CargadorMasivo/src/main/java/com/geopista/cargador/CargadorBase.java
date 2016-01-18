/**
 * CargadorBase.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.cargador;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.commandline.CommandLine;
import com.vividsolutions.jump.util.commandline.OptionSpec;
import com.vividsolutions.jump.util.commandline.ParseException;

public class CargadorBase {
	
	private static final Log	logger	= LogFactory.getLog(CargadorBase.class);
	
	protected AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	protected GeopistaEditor geopistaEditor1 = null;
	protected Blackboard blackboard = aplicacion.getBlackboard();
	
	protected static CommandLine commandLine;
	
	protected String codProvinciaOption=null;
	protected String codMunicipioOption=null;
	/* 
	 * codCartografiaPathOption should be the Path until the Provincia Code (not included) 
	 * Please give an absolute path
	 */
	protected String codCartografiaPathOption=null;
	/* 
	 * codResultadosPathOption should be the path until the Provincia Code (not included). 
	 * Please give an Absolute Path 
	 */
	protected String codResultadosPathOption=null;
	
	public static String user="syssuperuser";
	public static String pass="sysgeopass";
	
	public final static int PROVINCIA_LENGTH=2;
	public final static int MUNICIPIO_LENGTH=5;
	
	public final static String PROVINCIA_OPTION = "provincia";
	public final static String MUNICIPIO_OPTION = "municipio";
	public final static String DATOS_CARTOGRAFIA_PATH_OPTION = "datos_cartografia_path";
	public final static String RESULTADOS_PATH_OPTION = "resultados_path";

	
	/**
	 * Parseo de los argumentos
	 * 
	 * @param args
	 */
	protected static void parseCommandLine(String[] args) {
		commandLine = new CommandLine('-');
		commandLine.addOptionSpec(new OptionSpec(PROVINCIA_OPTION, 1));
		commandLine.addOptionSpec(new OptionSpec(MUNICIPIO_OPTION, 1));
		commandLine.addOptionSpec(new OptionSpec(DATOS_CARTOGRAFIA_PATH_OPTION, 1));
		commandLine.addOptionSpec(new OptionSpec(RESULTADOS_PATH_OPTION, 1));
		try {
			commandLine.parse(args);
		} catch (ParseException e) {
		}
	}
	
	
	public void pause(){
		System.out.println("Pulse una tecla....");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	    try {
	        bufferedReader.readLine();
	    } catch (IOException e1) {
	    }		
	}
	
	/**
	 * Método para determinar si el fichero existe
	 * 
	 * @param String
	 *            fich, ruta del fichero a comprobar que sea correcto
	 * @return boolean true si es correcto, false en caso contrario
	 */
	public static boolean revisarDirectorio(String fich) {
		try {
			File directorio = new File(fich);
			return directorio.exists();

		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Login into the system.
	 * @param aplicacion 
	 * @param user
	 * @param pass
	 */
	public static boolean login(AppContext aplicacion, String user, String pass) {
		// TODO Auto-generated method stub
		
		//comprobar login
		if (aplicacion.isLogged()){	
			aplicacion.relogin(user, pass);
			
		}		
		//comprobar login
		if (!aplicacion.isLogged()) {
			if (!aplicacion.loginNoGrafico(user, pass)){
				logger.info("Error al autenticar al usuario");
				return false;
			}
		}
		if (!aplicacion.isLogged()) {
			logger.info("Error en la conexión con la BBDD.");
			System.exit(1);
		}	
		return true;
	}
	
	
	protected boolean start(String[] args){
		/**
		 * Parseo de la linea de comandos.
		 */
		parseCommandLine(args);
		if (commandLine.hasOption(PROVINCIA_OPTION)) {
			codProvinciaOption = commandLine.getOption(PROVINCIA_OPTION)
					.getArg(0);
		}
		if (commandLine.hasOption(MUNICIPIO_OPTION)) {
			codMunicipioOption = commandLine.getOption(MUNICIPIO_OPTION)
					.getArg(0);
		}
		if (commandLine.hasOption(DATOS_CARTOGRAFIA_PATH_OPTION)) {
			codCartografiaPathOption = commandLine.getOption(DATOS_CARTOGRAFIA_PATH_OPTION)
					.getArg(0);
		}
		if (commandLine.hasOption(RESULTADOS_PATH_OPTION)) {
			codResultadosPathOption = commandLine.getOption(RESULTADOS_PATH_OPTION)
					.getArg(0);
		}
		return true;

	}
	
	
}
