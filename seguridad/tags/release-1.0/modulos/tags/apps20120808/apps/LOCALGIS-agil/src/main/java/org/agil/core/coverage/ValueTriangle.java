package org.agil.core.coverage;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

/**
 *  Triangulo -es decir, tres puntos no colineales que forman parte de un
 *  dominio 2D de la funcion- que a su vez tienen asociados un vector de
 *  valores. Esto permite dar valor mediante tecnicas interpolatorias (o
 *  algebra lineal: ecuacion del plano) a cualquier punto interior al
 *  triangulo
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public class ValueTriangle {
	/**
	 *  Description of the Field
	 */
	public PointValuePair vertices;

	/**
	 *  Cadena de texto con el tipo de interpolacion del triangulo
	 */
	private String INTERPOLATION_TYPE;


	/**
	 *@roseuid    3F6AD0920157
	 */
	public ValueTriangle() {

	}


	/**
	 *  Sets the value of the vertices property.
	 *
	 *@param  aVertices  the new value of the vertices property
	 */
	public void setVertices(PointValuePair aVertices) {
		vertices = aVertices;
	}


	/**
	 *  Access method for the vertices property.
	 *
	 *@return    the current value of the vertices property
	 */
	public PointValuePair getVertices() {
		return vertices;
	}


	/**
	 *  Permite obtener un valor de la funcion -por tecnicas interpolatorias- del
	 *  punto especificado
	 *
	 *@param  point  - punto del que se quiere obtener un vector de valores ({z},
	 *      {dx, dy del viento}, etc)
	 *@return        java.util.List
	 *@roseuid       3F6ACFC5004E
	 */
	public List evaluatePoint(Point point) {
		return null;
	}
}
