package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 *  Red de poligonos de Thiessen. Es el dual de un TIN.
 *
 *@author     alvaro zabala
 *@created    22 de septiembre de 2003
 *@version    1.1
 */
public class ThiessenPolygonNetwork
		 extends C_Function {

	/**
	 *  Description of the Field
	 */
	public PointValuePair thePointValuePair;

	/**
	 *  Description of the Field
	 */
	public ThiessenPolygon theThiessenPolygon;

	private String INTERPOLATION_TYPE;
	private String TRIANGULATION_TYPE;


	/**
	 *@roseuid    3F6EA28800BB
	 */
	public ThiessenPolygonNetwork() {

	}


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F0A92005D
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope) {
		return null;
	}


	/**
	 *@param  punto
	 *@return        java.util.List
	 *@roseuid       3F6F067201E4
	 */
	public List evaluate(Point punto) {
		return null;
	}
}
