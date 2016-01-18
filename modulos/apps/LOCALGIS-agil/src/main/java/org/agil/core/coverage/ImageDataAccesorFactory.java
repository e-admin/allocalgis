/**
 * ImageDataAccesorFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

import java.util.StringTokenizer;

import org.agil.vectorialserver.SpatialEngineConnection;

/**
 *  Esta clase se encarga de instanciar implementaciones de la interfaz
 *  ImageDataAccesor, en funcion del tipo de origen de datos al que se vaya
 *  acceder.
 *
 *@author     alvaro zabala
 *@created    23 de septiembre de 2003
 */
public class ImageDataAccesorFactory {
	/**
	 *  Constante de cadena con la extension del formato ECW
	 */
	public static final String ECW = ".ecw";
	/**
	 *  Constante de cadena con la extension del formato TIF
	 */
	public static final String TIFF = ".tif";
	/**
	 *  Constante de cadena con la extension del formato JPEG
	 */
	public static final String JPG = ".jpg";
	/**
	 *  Constante de cadena con la extension del formato MultiPageTiff
	 */
	public static final String PTIFF = ".ptif";
	
	public static final String BMP = ".bmp";
	
	public static final String PNG = ".png";
	
	/**
	 *  Construye un objeto de acceso a datos raster para un gridCoverage.
	 *
	 *@param  ambitoImagen  ambito local o remoto
	 *
	 *@param  tipoImagen    tipo de imagen: ecw, tiff, bmp, tcp-ip,
	 *      serverservlet, etc
	 * 
	 *@param  colorImagen   RS o RGB
	 *
	 *@param  urlImagen     uri de la imagen (c:/imagen.ecw,
	 *      172.16.1.22:2222|imagen
	 * 
	 *@return               objeto que permite acceder a los pixeles de la imagen
	 *      
	 */
	public static ImageDataAccesor createDataAccesor(String ambitoImagen,
													 String tipoImagen, 
													 String colorImagen,
													  String urlImagen) throws TfwNoAvailableException {
		ImageDataAccesor solucion = null;
		if (ambitoImagen.equalsIgnoreCase("LOCAL")) {
			if (tipoImagen.equalsIgnoreCase(GridCoverageExchange.ECW)) {
				//Creamos un DataAccesor para imagenes ECW
			//	solucion = new ECWImageDataAccesor(urlImagen, colorImagen);
				throw new UnsupportedOperationException("Se ha desactivado manejo de ErMapper.(Consulte equipo de desarrollo.)");
			}
			else if (tipoImagen.equalsIgnoreCase(GridCoverageExchange.TIF) 
								|| tipoImagen.equalsIgnoreCase(GridCoverageExchange.TIFF)) {
				solucion = new AgilImageDataAccesor(urlImagen);
			}
			else if (tipoImagen.equalsIgnoreCase(GridCoverageExchange.JAI_IMAGES)) {
				solucion = new AgilImageDataAccesor(urlImagen);
			}

		}
		else if (ambitoImagen.equalsIgnoreCase("REMOTE")) {

			StringTokenizer analizadorURL = new StringTokenizer(urlImagen, "|");

			if (tipoImagen.equalsIgnoreCase("TCPIMAGESERVER")) {
				String hostServer = analizadorURL.nextToken();
				int port = Integer.parseInt(analizadorURL.nextToken());
				long idCoverage = Integer.parseInt(analizadorURL.nextToken());
				SpatialEngineConnection connection = new SpatialEngineConnection(hostServer, port);
				solucion = new TCPImageDataAccesor(idCoverage, connection);

			}
			else if (tipoImagen.equalsIgnoreCase("SERVLETIMAGESERVER")) {
				String hostServer = analizadorURL.nextToken();
				long idCoverage = Integer.parseInt(analizadorURL.nextToken());
				solucion = new ServletImageDataAccesor(urlImagen, idCoverage);
			}
			//if
		}
		//if
		return solucion;
	}

}
