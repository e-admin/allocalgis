/**
 * LonLat.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.coordsys.ed50toetrs89.info;



/**
 * This class defines a record Type that contains 2 fields:
 * <ul>
 * 	<li>Longitude.
 *  <li>Latitude.
 * </ul>
 *  
 * Also, constructors, getters and setters methods for this class are well defined. 
 * @author javieraragon
 *
 */
public class LonLat {
	
	private Double latitude;
	private Double longitude;
	
	
	/**
	 * Constructor.
	 */
	public LonLat(){
		super();
	}
	
	/**
	 * Creates a LonLat coordinate from the specified longitude and latitude.
	 * @param longi longitude.
	 * @param lati latitude.
	 */
	public LonLat (Double longi, Double lati){
		super();
		this.latitude = lati;
		this.longitude = longi;
	}

	/**
	 * Gets the coordinate latitude.
	 * @return latitude.
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the coordinate latitude.
	 * @param latitude latitude.
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the coordinate longitude.
	 * @return longitude.
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Gets the coordinate longitude.
	 * @param longitude longitude.
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
