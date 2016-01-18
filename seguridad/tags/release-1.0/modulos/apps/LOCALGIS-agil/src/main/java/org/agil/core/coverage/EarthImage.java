package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 *  Función que representa una imagen de satelite, que mide la luminancia
 *  (función de distribución bidimensional de la iluminación)
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public class EarthImage extends C_Function {

	/**
	 *  Se encarga de acceder a los datos de la imagen almacenados
	 *  persistentemente (o de solicitarlos a un servidor de datos remoto)
	 */
	private ImageDataAccesor dataAccesor;


	/**
	 *@roseuid    3F6F098303C8
	 */
	public EarthImage() {
	}


	/**
	 *  Establece la clase encargada de leer datos raster de su fuente de
	 *  almacenamiento.
	 *
	 *@param  accesor
	 *@roseuid         3F70387101A5
	 */
	public void setDataAccesor(ImageDataAccesor accesor) {
		dataAccesor = accesor;
		setDomain(dataAccesor.getEnvelope());
	}


	/**
	 *@param  width     - ancho de la imagen que se quiere obtener
	 *@param  height    - alto de la imagen que se quiere obtener
	 *@param  envelope  - ventana geografica de la que se quiere obtener una
	 *      imagen
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F09BB0177
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope) {
		return dataAccesor.getImagen(envelope, width, height);
	}


	/**
	 *  Devuelve la instancia de DataAccesor que lee los datos de imagenes
	 *
	 *@return     implementacion de la interfaz ImageDataAccesor
	 *@roseuid    3F7038710138
	 */
	public ImageDataAccesor getDataAccesor() {
		return dataAccesor;
	}


	/**
	 *@param  punto
	 *@return        java.util.List
	 *@roseuid       3F6F098400DA
	 */
	public List evaluate(Point punto) {
		return null;
	}
}
