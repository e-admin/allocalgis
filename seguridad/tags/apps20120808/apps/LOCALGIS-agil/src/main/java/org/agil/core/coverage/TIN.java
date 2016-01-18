package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 *  Triangulated Irregular Network. Es una funcion de dominio continuo en R2
 *  que une todos los puntos de una nube de puntos mediante triangulos que no
 *  se intersectan y que cumplen una serie de caracteristicas. DE MOMENTO LA
 *  DEFINIMOS PERO ESTO VA PARA EL FINAL
 *
 *@author     alvaro zabala
 *@created    19 de septiembre de 2003
 *@version    1.1
 */
public class TIN
		 extends C_Function {

	/**
	 *  Description of the Field
	 */
	public ValueTriangle teselas;

	/**
	 *  Description of the Field
	 */
	public DiscretePointC_Function function;


	/**
	 *@roseuid    3F6EA27302BF
	 */
	public TIN() {

	}


	/**
	 *  Sets the value of the function property.
	 *
	 *@param  aFunction  the new value of the function property
	 *@roseuid           3F6EE66E0251
	 */
	public void setFunction(DiscretePointC_Function aFunction) {
		function = aFunction;
	}


	/**
	 *  Access method for the function property.
	 *
	 *@return     the current value of the function property
	 *@roseuid    3F6EE66E0203
	 */
	public DiscretePointC_Function getFunction() {
		return function;
	}


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F0A93008C
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope) {
		return null;
	}


	/**
	 *@param  point  - Punto del que se quiere obtener el triangulo que lo
	 *      contiene
	 *@return        org.agil.kernel.jump.coverage.functions.ValueTriangle
	 *@roseuid       3F6EE66E02BF
	 */
	public ValueTriangle locate(Point point) {
		return null;
	}


	/**
	 *@param  punto
	 *@return        java.util.List
	 *@roseuid       3F6F06730148
	 */
	public List evaluate(Point punto) {
		return null;
	}
}
