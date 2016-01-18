/**
 * Context.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database;

import java.sql.Date;
import java.sql.Timestamp;


	public abstract class Context {

		/**
		 * Comment for <code>facade</code>
		 */
		protected XMLFacade facade;

		/**
		 * Documento xml
		 */
		private String xml;

		/**
		 * Nombre del nodo que se toma como raiz del documento
		 */
		protected String root;
		
		/**
		 * Construye un nuevo Context inicializando.
		 * @param xml
		 */
		public Context(String xml, String root){
		    this.xml = xml;
		    this.root = root;
		    if (xml != null)
		        facade = new XMLFacade(xml);
		}
		
		protected abstract int getInt(String property);
		protected abstract Date getDate(String property);
		protected abstract Timestamp getTimestamp(String property);
		protected abstract String get(String property);
		
		public String toString(){
		    return xml;
		}	
}
