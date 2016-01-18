package test;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;
public class ConfiguraLocalGISMobile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean done = false;
		String ipHost="";
		String portHost="";
		String gpsPuerto="";
		String locale="";
		Scanner in = new Scanner(System.in);
		while (!done) {
			System.out.println("**************** Configurador de LocalGISMobile ****************");
			System.out.println("...");
			System.out.println("Introduzca direccion IP o nombre de host servidor");
			
			ipHost = in.nextLine();
			ipHost = ipHost.trim();
			
			System.out.println("Introduzca puerto de conexion del host servidor");
			
			portHost = in.nextLine();
			portHost = portHost.trim();
			

			boolean puertoDone = false;
			while (!puertoDone) {
				System.out.println("...");
				System.out.println("Introduzca numero de puerto de GPS en la pda [1..99]");
	
				gpsPuerto = in.nextLine();
				gpsPuerto = gpsPuerto.trim();
				try {
					Integer.parseInt(gpsPuerto);
					puertoDone = true;
				}
				catch (Exception e) {
					System.out.println("Puerto incorrecto");
					
				}
				
			}
			boolean localeDone = false;
			while (!localeDone) {
				System.out.println("...");
				System.out.println("Introduzca idioma [es,ca,gl,va]:[es]");
	
				locale = in.nextLine();
				locale = locale.trim();
				if (locale.equals(""))
					locale="es";

				System.out.println("LOCALE:"+locale);
				if (!((locale.equals("es"))||
							(locale.equals("ca"))||
							(locale.equals("gl"))||
							(locale.equals("va")))){
						System.out.println("Locale incorrecto");
				}
				else
					localeDone=true;
				
			}
			
			System.out.println("HOST:"+ipHost);
			System.out.println("PORT:"+portHost);
			System.out.println("PUERTO GPS:"+gpsPuerto);
			System.out.println("LOCALE:"+locale);
			String salida="";
			while (! (salida.equalsIgnoreCase ("s") ||
					salida.equalsIgnoreCase ("n") )) {

				System.out.println("...");
				System.out.println("Es correcto? S/N");

				salida = in.nextLine();
			}	
			salida = salida.toUpperCase();
			done = salida.equals("S")? true:false;

		}
		//System.out.println("A modificar ficheros ..");
		gpsPuerto= "COM"+gpsPuerto +":";
		try {
			modificaPropiedad ("localgis.properties", "server.host", ipHost);
			modificaPropiedad ("localgis.properties", "server.port", portHost);
			modificaPropiedad ("gps.properties", "port", gpsPuerto);
			modificaPropiedad ("localgis.properties", "locale.language", locale);

			System.out.println("Ficheros modificados correctamente");
			System.out.println("Realizando cab de configuracion ...");
			Runtime.getRuntime().exec("makecab.bat");
			System.out.println("Cab de configuracion configLocalGIS.cab realizado..");
			System.out.println("Proceda a copiarlo a la pda y ejecutarlo para realizar la configuracion final..");
			String f = in.nextLine();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void modificaPropiedad(String fichero, String propiedad,
			String valor) throws IOException {
		Properties props = new Properties();
		java.io.FileInputStream fis = new java.io.FileInputStream
		(new java.io.File(fichero));
		props.load(fis);
		props.put(propiedad, valor);

		java.io.FileOutputStream fos = new java.io.FileOutputStream (new java.io.File(fichero));
		props.store(fos, "Ficheros modificados para su configuracion en la PDA");



	}

}
