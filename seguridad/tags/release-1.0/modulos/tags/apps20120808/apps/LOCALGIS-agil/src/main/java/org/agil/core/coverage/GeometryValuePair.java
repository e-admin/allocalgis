package org.agil.core.coverage;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

/**
 *  Clase abstracta cuyas implementaciones son elementos de listas que definen
 *  las relaciones de los elementos de una funcion discreta (variables
 *  dependientes e independientes)
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public abstract class GeometryValuePair {

	/**
	 *@roseuid    3F6AD0910271
	 */
	public GeometryValuePair() {

	}


	/**
	 *  devuelve la geometria -variable independiente- de la funcion
	 *
	 *@return     com.vividsolutions.jts.geom.Geometry
	 *@roseuid    3F6ACBED01D4
	 */
	public Geometry geom() {
		return null;
	}


	/**
	 *  devuelve el vector de valores asociados a una geometria
	 *
	 *@return     java.util.List
	 *@roseuid    3F6ACC1401A5
	 */
	public List value() {
		return null;
	}
}
