package org.agil.core.coverage;

import java.io.File;
import java.io.FilenameFilter;
import java.util.StringTokenizer;

/**
 *  <p>
 *
 *  Segun la especificacion, esta clase permite construir GridCoverages a
 *  partir de almacenamientos persistentes, a la vez que permite guardarlos en
 *  dichos almacenamientos. <br>
 *  Debe distinguir distintos tipos de formatos:
 * 
 *  multi-earth-image|ecw|RGB|pathDirectorio (todos los mosaicos son el local
 *  -transparentes en remoto-) 
 * <b>no he pensado si se pueden crear mosaicos
 *  heterogeneos (earth con krigeage, etc)</b>
 * 
 *  earth-image|local|ecw|GS|pathImagen earth-image|local|tiff|RGB|pathImagen
 * 
 *  earth-image|remote|tcpimageserver|RGB|ipServer|portServer|idcoverage
 * 
 *  earth-image|remote|servletimageserver|RGB|urlServlet|portHttpServer|idcoverage
 * 
 *  dem|grid|txt| dem|grid|remote| dem|tin|txt| dem|tin|shp| kriging|shp| etc
 * 
 *  etc </p>
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public class GridCoverageExchange {
	
	public static final String ECW = "ECW";
	public static final String TIF = "TIF";
	public static final String TIFF = "TIFF";
	public static final String PTIF = "PTIF";
	public static final String PTIFF = "PTIFF";
	public static final String BMP = "BMP";
	public static final String PNG = "PNG";
	public static final String JAI_IMAGES = "JAI_IMAGES";
	
	private static GridCoverageExchange _instance = new GridCoverageExchange();

	/**
	 *  Constructor privado para impedir su instanciación. Esta clase sigue el
	 *  patrón de diseño Singleton.
	 *
	 *@author     alvaro zabala 23-sep-2003
	 *@roseuid    3F6F054D033C
	 */
	private GridCoverageExchange() {

	}


	/**
	 *  <p>
	 *
	 *  Recibe un identificador de origen de datos -URN propia- y devuelve la
	 *  instancia de GridCoverage almacenada en el origen referenciado. </p>
	 *
	 *@param  name      - Idenfificador -uri según un formato propio- de la
	 *      cobertura a la que se desea acceder. El formato debe ser tal que
	 *      identifique univocamente el tipo de cobertura -Raster de satelite,
	 *      TIN, MDE, Nube de puntos para Kriging, etc.-
	 *@return           org.agil.kernel.jump.coverage.GridCoverage
	 *@roseuid          3F6F03530232
	 */
	public GridCoverage createFromName(String name) throws TfwNoAvailableException {
		GridCoverage solucion = null;
		StringTokenizer tratadorUrnCoverage = new StringTokenizer(name, "|");
		String tipoGridCoverage = tratadorUrnCoverage.nextToken();

		if (tipoGridCoverage.equalsIgnoreCase("earth-image")) {
			String ambitoImagen = tratadorUrnCoverage.nextToken();
			//local o remoto
			String tipoImagen = tratadorUrnCoverage.nextToken();
			//ecw-tif, servlet-tcp/ip
			String colorImagen = tratadorUrnCoverage.nextToken();
			//GS o RGB
			String urlImagen = tratadorUrnCoverage.nextToken();
			//TODO quitar esto. Lo que hace es pasar el final de la cadena (hacerlo con substring)
			while (tratadorUrnCoverage.hasMoreTokens()) {
				urlImagen += ("|" + tratadorUrnCoverage.nextToken());
			}
			solucion = leeEarthCoverage(ambitoImagen, tipoImagen, colorImagen, urlImagen);
		}
		
		else if (tipoGridCoverage.equalsIgnoreCase("multi-earth-image")) {
			String ambitoImagen = tratadorUrnCoverage.nextToken();
			String tipoImagen = tratadorUrnCoverage.nextToken();
			//ecw-tif, servlet-tcp/ip
			String colorImagen = tratadorUrnCoverage.nextToken();
			//GS o RGB
			String urlImagen = tratadorUrnCoverage.nextToken();

			//TODO quitar esto. Lo que hace es pasar el final de la cadena (hacerlo con substring)
			while (tratadorUrnCoverage.hasMoreTokens()) {
				urlImagen += ("|" + tratadorUrnCoverage.nextToken());
			}
			solucion = leeMultiEarthCoverage(tipoImagen, colorImagen, urlImagen);
		}
		
		else {
			throw new UnsupportedOperationException("Tipo de Coverage no contemplado!");
		}
//			    }else if (tipoGridCoverage.equalsIgnoreCase("dem")){
//
//
//				}else if (tipoGridCoverage.equalsIgnoreCase("kriging")) {
//
//				}else{
//
//				}
		return solucion;
	}


	/**
	 *  <p>
	 *
	 *  Guarda la cobertura según el formato especificado </p>
	 *
	 *@param  coverage
	 *@param  pathFile  - ruta donde se guardara de forma persistente la
	 *      cobertura
	 *@roseuid          3F6F042A0271
	 */
	public void exportTo(GridCoverage coverage, String pathFile) {
		throw new UnsupportedOperationException("Operacion no soportada!");
	}


	/**
	 *  Recibe un tipo de imagen (ecw, tiff, etc), si es RGB o GS, y el path de
	 *  un directorio, y devuelve un GridCoverageCollection con tantas coverages
	 *  como archivos del tipo especificado hay colgando en ese directorio.
	 *
	 *@param  tipoImagen   si es ecw, tiff, jpg, bmp.
	 *@param  colorImagen  RGB o GS
	 *@param  urlImagen    ruta del directorio donde leer las imagenes
	 *@return
	 */
	private GridCoverage leeMultiEarthCoverage(String tipoImagen, String colorImagen, 
		String urlImagen) throws TfwNoAvailableException {
		GridCoverageCollection solucion = new GridCoverageCollection();
		String[] fileExtension = null;

		if (tipoImagen.equalsIgnoreCase("ECW")) {//Ecw
			String[] extension = {ImageDataAccesorFactory.ECW};
			fileExtension = extension;
		}else if(tipoImagen.equalsIgnoreCase("TIFF") || tipoImagen.equalsIgnoreCase("TIF") ){//Tiff
			String[] extension = {ImageDataAccesorFactory.TIFF};
			fileExtension = extension;
		}else if(tipoImagen.equalsIgnoreCase("PTIF") || tipoImagen.equalsIgnoreCase("PTIFF") ){//Multipage Tiff
			String[] extension = {ImageDataAccesorFactory.PTIFF};
			fileExtension = extension;
		}else if(tipoImagen.equalsIgnoreCase("JAI_IMAGES")){
			String[] extension = {ImageDataAccesorFactory.PTIFF, 
								ImageDataAccesorFactory.JPG, 
								ImageDataAccesorFactory.PNG, 
								ImageDataAccesorFactory.BMP,
								ImageDataAccesorFactory.TIFF};
			fileExtension = extension;
		}else {
			System.out.println("FORMATO " + tipoImagen + " NO CONTEMPLADO");
			return null;
		}
		final String[] finalExtension = fileExtension;
		//para acceder desde la clase anonima

		File directory = new File(urlImagen);
		String[] files = directory.list(
					new FilenameFilter() {
						public boolean accept(File dir, String name) {
							for(int i = 0; i < finalExtension.length; i++){
								if(name.endsWith(finalExtension[i]))
									return true;
							}//for		
							return false;			
						}
					});
		for (int i = 0; i < files.length; i++) {
			String ecwFile = files[i];
			GridCoverage coverage = leeEarthCoverage("LOCAL", tipoImagen, colorImagen, urlImagen + "/" + ecwFile);
			solucion.addGridCoverage(coverage, ecwFile);
		}

		return solucion;
	}


	/**
	 *  Construye una GridCoverage que tenga asociada una imagen raster.
	 *
	 *@param  ambitoImagen  Si se trata de una imagen cuyo origen es local o
	 *      remoto.
	 *@param  tipoImagen    Si es ECW, TIFF, BMP, etc para imagenes locales, o el
	 *      tipo de servidor en el caso de imagenes remotas (TCP/IP, un Servlet
	 *      HTTP, etc)
	 *@param  colorImagen   Si es escala de grises (GS) o color (RGB)
	 *@param  urlImagen     Para imagenes locales el path, para remotas una URL.
	 *@return
	 *@roseuid              3F70386D030D
	 */
	private GridCoverage leeEarthCoverage(String ambitoImagen, String tipoImagen, 
				String colorImagen, String urlImagen) throws TfwNoAvailableException {
		GridCoverage solucion = new GridCoverage();
		ImageDataAccesor dataAccesor =
				ImageDataAccesorFactory.createDataAccesor(ambitoImagen, tipoImagen,
				colorImagen, urlImagen);
		EarthImage function = new EarthImage();
		function.setDataAccesor(dataAccesor);
		solucion.setFunction(function);
		solucion.setEnvelope(dataAccesor.getEnvelope());
		return solucion;
	}


	/**
	 *@return     org.agil.kernel.jump.coverage.GridCoverageExchange
	 *@roseuid    3F70386D00AB
	 */
	public static GridCoverageExchange getInstance() {
		return _instance;
	}
}
