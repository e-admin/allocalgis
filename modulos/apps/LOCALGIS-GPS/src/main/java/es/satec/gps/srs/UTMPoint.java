/**
 * UTMPoint.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.gps.srs;

/**
 * Punto en coordenadas UTM
 * @author jpresa
 */
public class UTMPoint {

	/**
	 * Componente x (Easting)
	 */
	private double x;
	/**
	 * Componente y (Northing)
	 */
	private double y;
	/**
	 * Huso o zona UTM
	 */
	private int zone;
	
	/**
	 * Constructor
	 * @param x Coordenada x
	 * @param y Coordenada y
	 * @param zone Huso o zona UTM
	 */
	public UTMPoint(double x, double y, int zone) {
		this.x = x;
		this.y = y;
		this.zone = zone;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

	public String toString() {
		return "(" + x + ", " + y + " - "+ zone + ")";
	}
	
}
