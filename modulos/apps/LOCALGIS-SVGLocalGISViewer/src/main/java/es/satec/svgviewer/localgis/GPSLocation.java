/**
 * GPSLocation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis;

public class GPSLocation {

	private double longitude;
	private double latitude;
	private double altitude;
	private String speed;
	private String direction;
	private String quality;
	private int numberOfSatellites;

	/**
	 * @param longitude
	 * @param latitude
	 * @param altitude
	 * @param speed
	 * @param direction
	 * @param quality
	 * @param numberOfSatellites
	 */
	public GPSLocation(double longitude, double latitude, double altitude, String speed, String direction, String quality, int numberOfSatellites) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.speed = speed;
		this.direction = direction;
		this.quality = quality;
		this.numberOfSatellites = numberOfSatellites;
	}

	/**
	 * @param longitude
	 * @param latitude
	 */
	public GPSLocation(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		altitude = 0.0;
		speed = "";
		direction = "";
		quality = "";
		numberOfSatellites = 0;
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getNumberOfSatellites() {
		return numberOfSatellites;
	}

	public void setNumberOfSatellites(int numberOfSatellites) {
		this.numberOfSatellites = numberOfSatellites;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}
}
