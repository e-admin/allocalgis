/*
 * Created on 29-jul-2004
 */
package com.geopista.style.sld.ui.impl;

/**
 * @author Enxenio S.L.
 */
public class Operator {
		
		public Operator(String name, int id) {
			_name = name;
			_id = id;
		}
		
		public String getName() {
			return _name;
		}
		
		public int getID() {
			return _id;
		}
		
		public String toString() {
			return _name;
		}
		
		private String _name;
		private int _id;

}
