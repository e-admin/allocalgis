/**
 * ECEFPoint.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.gps.srs;

/**
 * Punto en coordenadas geocéntricas
 * @author jpresa
 */
public class ECEFPoint {
	/**
	 * Componente x
	 */
	private double x;
	/**
	 * Componente y
	 */
	private double y;
	/**
	 * Componente z
	 */
	private double z;
	
	/**
	 * Constructor
	 * @param x Coordenada x
	 * @param y Coordenada y
	 */
	public ECEFPoint(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0.0;
	}

	/**
	 * Constructor
	 * @param x Coordenada x
	 * @param y Coordenada y
	 * @param z Coordenada z
	 */
	public ECEFPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	
	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public String toString() {
		return "(" + x + ", " + y + ", "+ z + ")";
	}
	

}
