/**
 * Coverage.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 *  <p>
 *
 *  Segun la especificacion OpenGIS, es un origen de datos cartografico cuya
 *  principal caracteristica es es generar un valor (aislado, o conjunto de
 *  ellos en forma de vector) para cualquiera de los puntos que cae en su
 *  dominio. <br>
 *  Ejemplos de coberturas son:<br>
 *  una triangulacion de delaunay (TIN) una teselacion mediante poligonos de
 *  Thiessen un modelo digital de elevaciones, temperatura, etc (GRID) una
 *  funcion matematica (KRIGING) etc. </p>
 *
 *@author     alvaro zabala
 *@created    19 de septiembre de 2003
 *@version    1.1
 */
public abstract class Coverage {

	/**
	 *  Funcion que permite evaluar cualquier punto de la cobertura
	 */
	protected C_Function function;


	/**
	 *@roseuid    3F6EA26D01B5
	 */
	public Coverage() {

	}


	/**
	 *  Permite fijar la funcion matematica que evaluara los puntos de la
	 *  cobertura
	 *
	 *@param  function
	 *@roseuid          3F70385302CE
	 */
	public void setFunction(C_Function function) {
		this.function = function;
	}


	/**
	 *  The number of sample dimensions in the coverage. For grid coverages, a
	 *  sample dimension is a band.
	 *
	 *@return     the number of sample dimensions in the coverage.
	 *@roseuid    3F6EA26D01D4
	 */
	public abstract int getNumSampleDimensions();


	/**
	 *  Number of grid coverages which the grid coverage was derived from. This
	 *  implementation specification does not include interfaces for creating
	 *  collections of coverages therefore this value will usually be one
	 *  indicating an adapted grid coverage, or zero indicating a raw grid
	 *  coverage.
	 *
	 *@return     the number of grid coverages which the grid coverage was
	 *      derived from.
	 *@roseuid    3F6EA26D0242
	 */
	public abstract int getNumSources();


	/**
	 *  Devuelve el MRE de la cobertura.
	 *
	 *@return     minimo rectangulo encuadrante de la cobertura
	 *@roseuid    3F6EA26D029F
	 */
	public abstract Envelope getEnvelope();


	/**
	 *  The names of each dimension in the coverage. Typically these names are
	 *  <var>x</var> , <var>y</var> , <var>z</var> and <var>t</var> . The number
	 *  of items in the sequence is the number of dimensions in the coverage.
	 *  Grid coverages are typically 2D (<var>x</var> , <var>y</var> ) while
	 *  other coverages may be 3D (<var>x</var> , <var>y</var> , <var>z</var> )
	 *  or 4D (<var>x</var> , <var>y</var> , <var>z</var> , <var>t</var> ). The
	 *  number of dimensions of the coverage is the number of entries in the list
	 *  of dimension names.
	 *
	 *@return                   the names of each dimension in the coverage.
	 *@throws  RemoteException  if a remote method call failed.
	 *@roseuid                  3F6EB49802AF
	 */
	public abstract String[] getDimensionNames();


	/**
	 *  List of metadata keywords for a coverage. If no metadata is available,
	 *  the sequence will be empty.
	 *
	 *@return     the list of metadata keywords for a coverage.
	 *@roseuid    3F6EB498030D
	 */
	public abstract String[] getMetadataNames();


	/**
	 *  Returns the source data for a grid coverage. If the {@link
	 *  GC_GridCoverage} was produced from an underlying dataset (by <code>createFromName</code>
	 *  or <code>createFromSubName</code> for instance) the {@link
	 *  #getNumSources} method should returns zero, and this method should not be
	 *  called. If the <code>GC_GridCoverage}</code> was produced using {link
	 *  org.opengis.gp.GP_GridCoverageProcessor} then it should return the source
	 *  grid coverage of the one used as input to <code>GP_GridCoverageProcessor</code>
	 *  . In general the source() method is intended to return the original
	 *  <code>GC_GridCoverage</code> on which it depends. This is intended to
	 *  allow applications to establish what <code>GC_GridCoverage</code>s will
	 *  be affected when others are updated, as well as to trace back to the "raw
	 *  data".
	 *
	 *@param  sourceDataIndex   Source grid coverage index. Indexes start at 0.
	 *@return                   the source data for a grid coverage.
	 *@throws  RemoteException  if a remote method call failed.
	 *@roseuid                  3F6EB498038A
	 */
	public abstract Coverage getSource(int sourceDataIndex);


	/**
	 *  Retrieve the metadata value for a given metadata name.
	 *
	 *@param  name              Metadata keyword for which to retrieve data.
	 *@return                   the metadata value for a given metadata name.
	 *@throws  RemoteException  if a remote method call failed.
	 *@roseuid                  3F6EB499002E
	 */
	public abstract String getMetadataValue(String name);


	/**
	 *  <p>
	 *
	 *  Devuelve una vista 2D del GridCoverage en forma de BufferedImage. Este
	 *  metodo permite interoperabilidad con Java2D </p>
	 *
	 *@param  width     - ancho en pixeles de la imagen que se desea obtener
	 *@param  height    - alto en pixels de la imagen que se desea proporcionar
	 *@param  envelope  - sub-regíon de la cobertura de la que se quiere obtener
	 *      una representacion en forma de imagen.
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6EBF9201F4
	 */
	public abstract BufferedImage getBufferedImage(int width, int height,
			Envelope envelope);


	/**
	 *  Ofrece acceso a la funcion que evalua los puntos de la cobertura
	 *
	 *@return     C_Function que evalua los puntos de la cobertura
	 *@roseuid    3F70385301E4
	 */
	public C_Function getFunction() {
		return function;
	}


	/**
	 *  Return the value vector for a given point in the coverage. A value for
	 *  each sample dimension is included in the vector. The default
	 *  interpolation type used when accessing grid values for points which fall
	 *  between grid cells is nearest neighbor. The coordinate system of the
	 *  point is the same as the grid coverage coordinate system (specified by
	 *  the {@link #getCoordinateSystem}).
	 *
	 *@param  point             Point at which to find the grid values.
	 *@return                   the value vector for a given point in the
	 *      coverage.
	 *@throws  RemoteException  if a remote method call failed.
	 *@roseuid                  3F6EA26E0000
	 */
	public abstract List evaluate(Point point);


	/**
	 *  Return a sequence of Boolean values for a given point in the coverage. A
	 *  value for each sample dimension is included in the sequence. The default
	 *  interpolation type used when accessing grid values for points which fall
	 *  between grid cells is nearest neighbor. The coordinate system of the
	 *  point is the same as the grid coverage coordinate system.
	 *
	 *@param  point             Point at which to find the coverage values.
	 *@return                   a sequence of boolean values for a given point in
	 *      the coverage.
	 *@throws  RemoteException  if a remote method call failed.
	 *@roseuid                  3F6EA26E00CB
	 */
	public abstract boolean[] evaluateAsBoolean(Point point);


	/**
	 *  Return a sequence of unsigned byte values for a given point in the
	 *  coverage. A value for each sample dimension is included in the sequence.
	 *  The default interpolation type used when accessing grid values for points
	 *  which fall between grid cells is nearest neighbor. The coordinate system
	 *  of the point is the same as the grid coverage coordinate system.
	 *
	 *@param  point             Point at which to find the coverage values.
	 *@return                   a sequence of unsigned byte values for a given
	 *      point in the coverage.
	 *@throws  RemoteException  if a remote method call failed.
	 *@roseuid                  3F6EA26E0157
	 */
	public abstract byte[] evaluateAsByte(Point point);


	/**
	 *  Return a sequence of integer values for a given point in the coverage. A
	 *  value for each sample dimension is included in the sequence. The default
	 *  interpolation type used when accessing grid values for points which fall
	 *  between grid cells is nearest neighbor. The coordinate system of the
	 *  point is the same as the grid coverage coordinate system.
	 *
	 *@param  point             Point at which to find the grid values.
	 *@return                   a sequence of integer values for a given point in
	 *      the coverage.
	 *@throws  RemoteException  if a remote method call failed.
	 *@roseuid                  3F6EA26E01F4
	 */
	public abstract int[] evaluateAsInteger(Point point);


	/**
	 *  Return a sequence of double values for a given point in the coverage. A
	 *  value for each sample dimension is included in the sequence. The default
	 *  interpolation type used when accessing grid values for points which fall
	 *  between grid cells is nearest neighbor. The coordinate system of the
	 *  point is the same as the grid coverage coordinate system.
	 *
	 *@param  point             Point at which to find the grid values.
	 *@return                   a sequence of double values for a given point in
	 *      the coverage.
	 *@throws  RemoteException  if a remote method call failed.
	 *@roseuid                  3F6EA26E0271
	 */
	public abstract double[] evaluateAsDouble(Point point);
}
