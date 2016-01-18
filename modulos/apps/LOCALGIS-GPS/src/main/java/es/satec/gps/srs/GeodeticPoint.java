/**
 * GeodeticPoint.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.gps.srs;

/**
 * Punto en coordenadas geodésicas o geográficas
 * @author jpresa
 */
public class GeodeticPoint {

	/**
	 * Longitud
	 */
	private double longitude;
	/**
	 * Latitud
	 */
	private double latitude;
	/**
	 * Altitud elipsoidal
	 */
	private double altitude;
	
	public GeodeticPoint(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = 0;
	}
	
	public GeodeticPoint(double longitude, double latitude, double altitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public String toString() {
		return "(" + longitude + ", " + latitude + ", " + altitude + ")";
	}

}
