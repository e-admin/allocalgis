package org.agil.core.coverage;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

/**
 *  Par ordenado donde el primer elemento es un Punto del dominio de la
 *  funcion a la que está asociado y el segundo es un valor (vector) que
 *  expresa el valor de la función
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public class PointValuePair
		 extends GeometryValuePair {

	/**
	 *  punto del dominio discreto de la funcion (variable independiente)
	 */
	private Point point;

	/**
	 *  Valor asociado al punto (vector de valores)
	 */
	private List value;


	/**
	 *@roseuid    3F6EA267006D
	 */
	public PointValuePair() {

	}
}
