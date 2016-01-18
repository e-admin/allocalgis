package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 *@author     alvaro zabala
 *@version    1.1
 */
public class GridMatrixValues
		 extends DiscreteC_Function {

	/**
	 *  numero de filas
	 */
	private int numRows;

	/**
	 *  numero de columnas
	 */
	private int numColumns;

	/**
	 *  Array de vectores formado por numFilas * numColumnas
	 */
	private java.util.List values[];

	/**
	 *  Coordenadas del origen del grid
	 */
	private Point origin;


	/**
	 *@roseuid    3F6EA269007D
	 */
	public GridMatrixValues() {

	}


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F702480035B
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope) {
		return null;
	}


	/**
	 *  Devuelve todos los puntos que forman el grid
	 *
	 *@return     com.vividsolutions.jts.geom.Point[]
	 *@roseuid    3F6EE65C03B9
	 */
	public Point[] points() {
		return null;
	}


	/**
	 *@return     int
	 *@roseuid    3F70248001B5
	 */
	public int num() {
		return 0;
	}


	/**
	 *@param  geometry
	 *@return           java.util.List
	 *@roseuid          3F70248001F4
	 */
	public List evaluate(Geometry geometry) {
		return null;
	}


	/**
	 *@return     java.util.List[]
	 *@roseuid    3F7024800271
	 */
	public List[] values() {
		return null;
	}


	/**
	 *@return     com.vividsolutions.jts.geom.Geometry
	 *@roseuid    3F702480029F
	 */
	public Geometry[] domain() {
		return (Geometry[]) null;
	}


	/**
	 *@param  punto
	 *@return        java.util.List
	 *@roseuid       3F70248002CE
	 */
	public List evaluate(Point punto) {
		return null;
	}
}
