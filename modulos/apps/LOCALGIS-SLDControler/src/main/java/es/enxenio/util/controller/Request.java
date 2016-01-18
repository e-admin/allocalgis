/**
 * Request.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 10-jun-2004
 *
 */
package es.enxenio.util.controller;

/**
 * @author enxenio s.l.
 *
 */
public interface Request {
	
	/**Este método recuperará el valor de un parámetro almacenado en el Mapa
	 * @param paramKey La clave que identifica al parámetro
	 * @return El objeto que contiene el valor del parámetro
	 */
	public Object getAttribute(String paramKey);
	
	/**Este método añadirá una nueva entrada al HashMap
	 * @param paramKey La clave que identificará a la nueva entrada
	 * @param paramValue El objeto que queremos almacenar en el HashMap
	 */
	public void setAttribute(String paramKey, Object paramValue);
	
}
