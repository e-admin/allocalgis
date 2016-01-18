/**
 * CardinalVariables.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans;

/**
 * @author javieraragon
 *
 */
public class CardinalVariables {
	// variables estaticas que determinan grados para las orientaciones
	// del primer tramo de la ruta.
	// ï¿½En quï¿½ calle empieza la ruta? ï¿½En quï¿½ Direccion? Norte/Sur... etc
	private static final int NORTH_START = 80;
	private static final int NORTH_END = 100;
	private static final int WEST_START = 170;
	private static final int WEST_END =  190;
	private static final int SOUTH_START = 260;
	private static final int SOUTH_END = 280;
	private static final int EAST_START = 350;
	private static final int EAST_END = 10;

}
