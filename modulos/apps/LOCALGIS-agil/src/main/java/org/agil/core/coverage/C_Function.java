/**
 * C_Function.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 *  <p>
 *
 *  Funcion que tiene asociado un dominio en un espacio vectorial R2. Es
 *  decir, asocia a un punto (x,y) un vector (ej: {Rojo, Verde, Azul}) </p>
 *
 *@author     alvaro zabala
 *@created    22 de septiembre de 2003
 *@version    1.1
 */
public abstract class C_Function
		 implements C_FunctionIF {
	private Envelope domain;


	/**
	 *@roseuid    3F6EA27300FA
	 */
	public C_Function() {
	}


	/**
	 *  <p>
	 *
	 *  Permite fijar el dominio de la funcion. En el caso de funciones continuas
	 *  (C_Function) el dominio será su extension (el Envelope) </p>
	 *
	 *@param  domain          The new Domain value
	 *@roseuid                3F70693003A9
	 */
	public void setDomain(Envelope domain) {
		this.domain = domain;
	}


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F0A460251
	 */
	public abstract BufferedImage getImage(int width, int height,
			Envelope envelope);


	/**
	 *  Evalua el punto especificado
	 *
	 *@param  punto  - Punto para el que se evalua la funcion
	 *@return        java.util.List
	 *@roseuid       3F6EA27300FB
	 */
	public abstract List evaluate(Point punto);


	/**
	 *  Devuelve el dominio de la funcion (geometria que establece su dominio)
	 *  Por tratarse de una funcion continua, devuelve un Envelope
	 *
	 *@return     com.vividsolutions.jts.geom.Envelope
	 *@roseuid    3F6EA2730149
	 */
	public Envelope domain() {
		return domain;
	}
}
