package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 *
 */
public interface C_FunctionIF {

	/**
	 *  Evalua el punto especificado
	 *
	 *@param  punto  - Punto para el que se evalua la funcion
	 *@return        java.util.List
	 *@roseuid       3F6F1D17000F
	 */
	public List evaluate(Point punto);


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F1D170138
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope);
}
