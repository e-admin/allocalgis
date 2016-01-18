/**
 * CargarPadronMunicipalMain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;



/**
 * Clase main para la carga del padrón municipal de habitantes, nos permite realizar las carga de un listado de ficheros de padrón
 * utilizando el script cargarPadron.sh (presente en src/padron).
 * 
 * La llamada al script sería por ejemplo:
 * 
 * ./cargarPadron.sh syssuperuser satec2009 geopista geopista
 * 
 * Recibiendo como se puede observar: usuario y contraseña para loguearse y usuario y contraseña de la base de datos
 * 
 * 
 * Para realizar la carga tendremos que generar un jar que contenga los ficheros de padrón y un fichero llamado file_names 
 * (que contendrá los nombres de los ficheros de padrón, uno por cada línea)-->tenemos un ejemplo en src/padron. 
 * El jar se llamará geopista_ficheros_padron.jar
 * 
 *  Este proceso sólo permite cargar ficheros en formato txt.
 *  
 */
public class CargarPadronMunicipalMain {
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private String userAplicacion=null;
	private String passwordAplicacion=null;
	private String userBd=null;
	private String passBd=null;
	
	
	public CargarPadronMunicipalMain( String userAplicacion, String passwordAplicacion, 
			String userBd, String passBd) {		
	
		try {
		//file_names es un fichero de texto que contiene los nombres de los ficheros de padrón,
		//en cada línea contiene el nombre de uno de los ficheros
		InputStream is_file_names =  getClass().getResourceAsStream( "/padron"+File.separator+"file_names");
		InputStreamReader isr=new InputStreamReader(is_file_names);
		BufferedReader br=new BufferedReader(isr);
		
		
			while(br.ready()){
				String nombreFichero=br.readLine();
	
				String location = "/padron"+File.separator+nombreFichero;
				InputStream is =  getClass().getResourceAsStream(location);
				InputStream is2 =  getClass().getResourceAsStream(location);
	
				if(is!=null){
					System.out.println("El fichero de entrada: "+nombreFichero+" existe");
				}
	
				CargarFicheroPadron cPadron = new CargarFicheroPadron(nombreFichero);
				ApplicationContext application = AppContext.getApplicationContext();  
	
				this.userAplicacion=userAplicacion;
				this.passwordAplicacion=passwordAplicacion;
				this.userBd=userBd;
				this.passBd=passBd;
				boolean autenticaBD=true;
	
				//nos autenticamos en la base de datos
				try{
					autentificacionBBDD();
					autenticaBD=true;
				}catch(Exception e){
					System.out.println("No hemos podido loguearnos en la base de datos");
					autenticaBD=false;
					e.printStackTrace();
				}

				if(autenticaBD){
					System.out.println("Nos hemos logueado con exito");
					Map dataMap=new HashMap();
					dataMap.put("is", is);
					dataMap.put("is2", is2);
					cPadron.importar(dataMap);
					
					//llamamos este método para cerrar la conexión a la base de datos, que nos ha permitido cargar el padrón de habitantes
					//ImportacionOperations.finalizarCargaPadron();
					
					//cerramos la conexión a la base de datos, que hemos abierto para loguearnos
					aplicacion.closeConnection((Connection)aplicacion.getBlackboard().get("DirectConnection"),null,null,null);
					
					System.out.println("Hemos cerrado la conexión y terminado la carga---------------");
				}//fin if
			}//fin del while
			
			is_file_names.close();
			isr.close();
			br.close();
			
			System.out.println("Hemos terminado la carga de todos los ficheros de padrón");
		} catch (IOException e) {
		System.out.println("Se ha producido un problema en la lectura del fichero que contiene los nombres de los ficheros de padron");
		e.printStackTrace();
		}
	}

	/** Método main encargado de la carga de padrón: La llamada al script sería por ejemplo:
	 * 
	 * ./cargarPadron.sh syssuperuser satec2009 geopista geopista
	 * 
	 * @param nombre usuario
	 * @param password
	 * @param usuario BD
	 * @param password BD
	 * @throws FileNotFoundException 
	 * */
	public static void main(String[] args) throws FileNotFoundException {
		
		if(args.length!=4/*5*/){
			System.out.println("Se requieren 5 parámetros");
			
		}
		
		else{
		//String nombreFichero=args [0];
		String userApplication=args[0];
		String passwordApp=args[1];
		String userBd=args[2];	
		String passBd=args[3];
	
		
		new CargarPadronMunicipalMain(/*nombreFichero,*/ userApplication, passwordApp, userBd, passBd);
		}
		}//fin del main
		
	
	
	
	/**
	 * En este metodo nos autentificamos en el Sistema, tenemos que hacer la autentificacion en la aplicacion y
	 * posteriormente en la BBDD.
	 */
	private void autentificacionBBDD(){
		
		// Comprobamos que se ha introducido correctamente el usuario y password de la aplicación:
		if (!aplicacion.isLogged()) {
			aplicacion.loginNoGrafico(userAplicacion, passwordAplicacion);
		}
		if (!aplicacion.isLogged()) {
			System.exit(1);
		}
		
       	// Para establecer la conexión directa con la BBDD, comprobamos que se ha introducido correctamente el usuario y password de la BBDD:
		
		try {
			aplicacion.getBlackboard().put("USER_BD", userBd);
			aplicacion.getBlackboard().put("PASS_BD", passBd);
			aplicacion.getBlackboard().put("DirectConnection", aplicacion.getJDBCConnection(userBd, passBd));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
}//fin clase
