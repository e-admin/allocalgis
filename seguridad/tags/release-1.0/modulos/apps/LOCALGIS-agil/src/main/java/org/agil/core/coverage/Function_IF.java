package org.agil.core.coverage;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

/**
 *  Interfaz que deben implementar todas las funciones asociadas a una
 *  coverage.
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public interface Function_IF {

	/**
	 *@param  geometry
	 *@return           java.util.List
	 *@roseuid          3F6F1F03031C
	 */
	public List evaluate(Geometry geometry);
}
