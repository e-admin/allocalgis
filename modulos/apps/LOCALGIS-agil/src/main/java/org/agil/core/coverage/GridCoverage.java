/**
 * GridCoverage.java
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
 *  Implementacion de Coverage que se caracteriza por tener asociado un
 *  sistema de coordenadas reticular (Grid) y que subdivide el espacio en
 *  celdas. Cada una de las celdas de un GridCoverage está centrada en los
 *  puntos del sistema de coordenadas reticular. </p>
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public class GridCoverage
		 extends Coverage {
	/**
	 *  identificador numerico del origen de datos
	 */
	private long identificador;
	/**
	 *  funcion espacial que permite evaluar cualquier punto R2 del dominio de la
	 *  cobertura.
	 */
	private C_Function function;

	/**
	 *  Minimo rectangulo encuadrante de la cobertura
	 */
	private Envelope envelope;


	/**
	 *  Constructor: recibe la funcion espacial que evalua los puntos de la
	 *  cobertura
	 *
	 *@param  function
	 *@roseuid          3F6F1F74007D
	 */
	public GridCoverage(C_Function function) {
		setFunction(function);
	}


	/**
	 *@roseuid    3F6EA27D0399
	 */
	public GridCoverage() {

	}


	/**
	 *  Setea la funcion que evalua los puntos de la cobertura
	 *
	 *@param  function
	 *@roseuid          3F7038630203
	 */
	public void setFunction(C_Function function) {
		this.function = function;
		envelope = function.domain();
	}


	/**
	 *  Establece el envelope (minimo rectangulo contenedor) de la cobertura
	 *
	 *@param  envelope
	 *@roseuid          3F7038630399
	 */
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}


	/**
	 *  setea el identificador del origen de datos
	 *
	 *@param  l  clave entera que identifica el origen de datos
	 */
	public void setIdentificador(long l) {
		identificador = l;
	}


	/**
	 *  Devuelve el numero de bandas que forman el GridCoverage. En el caso de
	 *  imagenes, lo normal son 1 (GS o LookupTable) y 3 (RGB). Los gridCoverages
	 *  geofisicos pueden tener mas bandas.
	 *
	 *@return     int
	 *@author     alvaro zabala 24-sep-2003
	 *@roseuid    3F6EAE7F0203
	 */
	public int getNumSampleDimensions() {
		return 0;
	}


	/**
	 *@return     int
	 *@roseuid    3F6EAE7F0232
	 */
	public int getNumSources() {
		return 0;
	}


	/**
	 *@return     com.vividsolutions.jts.geom.Envelope
	 *@roseuid    3F6EAE7F0251
	 */
	public Envelope getEnvelope() {
		return this.envelope;
	}


	/**
	 *  <p>
	 *
	 *  Devuelve el numero de 'overviews' (imagenes en piramide que pueden
	 *  simplificar la representacion de la cobertura) asociado a la cobertura.
	 *  </p>
	 *
	 *@return     int
	 *@roseuid    3F6F014C0109
	 */
	public int getNumOverviews() {
		return 0;
	}


	/**
	 *@return     java.lang.String[]
	 *@roseuid    3F6EAE8000CB
	 */
	public String[] getDimensionNames() {
		return null;
	}


	/**
	 *  <p>
	 *
	 *  Un GridCoverage puede tener una serie de 'overviews' (representaciones
	 *  simplificadas) previamente calculadas. Estas representaciones están
	 *  ordenadas en forma de piramide, de mayor a menor detalle (cuanto menor
	 *  sea el detalle, menos precisas serán pero las operaciones con ellas serán
	 *  mas rapidas) </p>
	 *
	 *@return     org.agil.kernel.jump.coverage.GridCoverage
	 *@roseuid    3F6F018B01A5
	 */
	public GridCoverage getOverview() {
		return null;
	}


	/**
	 *@return     java.lang.String[]
	 *@roseuid    3F6EAE800109
	 */
	public String[] getMetadataNames() {
		return null;
	}


	/**
	 *@param  sourceDataIndex
	 *@return                  org.agil.kernel.jump.coverage.Coverage
	 *@roseuid                 3F6EAE800128
	 */
	public Coverage getSource(int sourceDataIndex) {
		return null;
	}


	/**
	 *@param  name
	 *@return       java.lang.String
	 *@roseuid      3F6EAE800196
	 */
	public String getMetadataValue(String name) {
		return null;
	}


	/**
	 *  Obtiene la funcion asociada a la cobertura
	 *
	 *@return     Funcion que permite evaluar cualquier punto en R2 del dominio
	 *      de la cobertura
	 *@roseuid    3F7038630138
	 */
	public C_Function getFunction() {
		return function;
	}


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F719497029F
	 */
	public BufferedImage getBufferedImage(int width, int height,
			Envelope envelope) {
		return function.getImage(width, height, envelope);
	}


	/**
	 *  devuelve el identificador del origen de datos
	 *
	 *@return    clave entera que identifica el origen de datos
	 */
	public long getIdentificador() {
		return identificador;
	}


	/**
	 *@param  point
	 *@return        java.util.List
	 *@roseuid       3F6EAE7F0271
	 */
	public List evaluate(Point point) {
		return function.evaluate(point);
	}


	/**
	 *@param  point
	 *@return        boolean[]
	 *@roseuid       3F6EAE7F02FD
	 */
	public boolean[] evaluateAsBoolean(Point point) {
		boolean[] solucion = null;
		List values = evaluate(point);
		solucion = new boolean[values.size()];
		for (int i = 0; i < solucion.length; i++) {
			solucion[i] = ((Boolean) values.get(i)).booleanValue();
		}
		return solucion;
	}


	/**
	 *@param  point
	 *@return        byte[]
	 *@roseuid       3F6EAE7F036B
	 */
	public byte[] evaluateAsByte(Point point) {
		byte[] solucion = null;
		List values = evaluate(point);
		solucion = new byte[values.size()];
		for (int i = 0; i < solucion.length; i++) {
			solucion[i] = ((Byte) values.get(i)).byteValue();
		}
		return solucion;
	}


	/**
	 *@param  point
	 *@return        int[]
	 *@roseuid       3F6EAE800000
	 */
	public int[] evaluateAsInteger(Point point) {
		int[] solucion = null;
		List values = evaluate(point);
		solucion = new int[values.size()];
		for (int i = 0; i < solucion.length; i++) {
			solucion[i] = ((Integer) values.get(i)).intValue();
		}
		return solucion;
	}


	/**
	 *@param  point
	 *@return        double[]
	 *@roseuid       3F6EAE80007D
	 */
	public double[] evaluateAsDouble(Point point) {
		double[] solucion = null;
		List values = evaluate(point);
		solucion = new double[values.size()];
		for (int i = 0; i < solucion.length; i++) {
			solucion[i] = ((Double) values.get(i)).doubleValue();
		}
		return solucion;
	}

}
