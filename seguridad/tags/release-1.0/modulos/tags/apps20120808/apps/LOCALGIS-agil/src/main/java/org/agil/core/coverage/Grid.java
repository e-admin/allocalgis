package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 *  Funcion definida por una coleccion finita de PointValuePairs cuyos puntos
 *  cubre el espacio formando un grid. El valor de cualquier punto interior al
 *  Grid es calculado con un GridEvaluator (que implementa un algoritmo
 *  determinado)
 *
 *@author     alvaro zabala
 *@created    22 de septiembre de 2003
 *@version    1.1
 */
public class Grid
		 extends C_Function {

	/**
	 *  Description of the Field
	 */
	public GridEvaluator theGridEvaluator;

	/**
	 *  Description of the Field
	 */
	public GridMatrixValues theGridMatrixValues;

	/**
	 *  Tipo de interpolacion a utilizar
	 */
	private String INTERPOLATION_TYPE;


	/**
	 *@roseuid    3F6EA273007D
	 */
	public Grid() {

	}


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F0A9003C8
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope) {
		return null;
	}


	/**
	 *@param  punto
	 *@return        java.util.List
	 *@roseuid       3F6F0671033C
	 */
	public List evaluate(Point punto) {
		return null;
	}
}
