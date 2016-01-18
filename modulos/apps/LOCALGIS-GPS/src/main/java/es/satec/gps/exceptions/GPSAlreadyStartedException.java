/**
 * GPSAlreadyStartedException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.gps.exceptions;

/**
 * Indica que el GPS ya ha sido iniciado.
 * User: Hugo
 * Date: 02-ago-2007
 * Time: 12:26:34
 */
public class GPSAlreadyStartedException extends GPSException {
	private static final long serialVersionUID = -6262879498509449036L;

	public GPSAlreadyStartedException(String string) {
        super(string);
    }
}
