package com.geopista.app.utilidades;

import java.util.*;

/**
 * Clase CMap para manejar objetos Maps.
 *
 * @author SATEC
 * @version 1.0
 */

public class Map extends Dictionary {

	/**
	 * Constructor de la clase Map
	 *
	 * @return Este método no devuelve ningún valor.
	 */
	public Map() {
		_fld02A0 = new Vector();
		_fld02A1 = new Vector();
		_fld02A2 = 0;
	}

	/**
	 * Este metodo devuelve una enumeracion de los elementos
	 * del Map.
	 *
	 * @return Este método devuelve una enumeracion de
	 *         los elementos del map.
	 */
	public Enumeration elements() {
		return _fld02A1.elements();
	}


	/**
	 * Este metodo devuelve un objeto del Map.
	 *
	 * @return Este método devuelve un objeto del Map.
	 */
	public Object get(Object obj) {
		int i = _fld02A0.indexOf(obj);
		if (i == -1 || i > _fld02A2)
			return null;
		else
			return _fld02A1.elementAt(i);
	}


	/**
	 * Este metodo indica si el map esta vacio.
	 *
	 * @return Este método devuelve true si el map esta vacio.
	 */
	public boolean isEmpty() {
		return _fld02A2 == 0;
	}


	/**
	 * Este metodo devuelve las keys del Map.
	 *
	 * @return Este método devuelve las keys del Map.
	 */
	public Enumeration keys() {
		return _fld02A0.elements();
	}


	/**
	 * Este metodo pone un objeto en el map.
	 *
	 * @param obj  seccion
	 * @param obj1 objeto 2.
	 * @return Este método devuelve el objeto que se
	 *         ha puesto.
	 */
	public Object put(Object obj, Object obj1) {
		int i = _fld02A0.indexOf(obj);
		Object obj2 = null;
		if (i == -1) {
			_fld02A0.addElement(obj);
			_fld02A1.addElement(obj1);
			_fld02A2++;
			return null;
		}
		if (i >= 0 && i < _fld02A2) {
			Object obj3 = _fld02A1.elementAt(i);
			_fld02A1.setElementAt(obj1, i);
			return obj3;
		} else {
			return null;
		}
	}

	/**
	 * Este metodo elimina un objeto en el map.
	 *
	 * @param obj objeto a eliminar
	 * @return Este método devuelve el objeto que se
	 *         ha eliminado.
	 */
	public Object remove(Object obj) {
		int i = _fld02A0.indexOf(obj);
		Object obj1 = null;
		if (i == -1)
			return null;
		if (i >= 0 && i < _fld02A2) {
			Object obj2 = _fld02A1.elementAt(i);
			_fld02A1.removeElementAt(i);
			_fld02A0.removeElementAt(i);
			_fld02A2--;
			return obj2;
		} else {
			return null;
		}
	}

	/**
	 * Este metodo devuelve el tamaño del Map
	 *
	 * @return Este método devuelve el tamaño del Map.
	 */
	public int size() {
		return _fld02A2;
	}

	/**
	 * Vector 1.
	 */
	private Vector _fld02A0;

	/**
	 * Vector 2.
	 */
	private Vector _fld02A1;

	/**
	 * Indicador de posicion.
	 */
	private int _fld02A2;
}
