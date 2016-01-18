package com.geopista.coordsys.ed50toetrs89.info;

/**
 * This class defines a record Type that contains 2 fields:
 * <ul>
 * <li>Longitude.
 * <li>Latitude.
 * </ul>
 * 
 * Also, constructors, getters and setters methods for this class are well
 * defined.
 * 
 * @author javieraragon
 * 
 */
public class LonLat {

	private Double latitude;
	private Double longitude;

	/**
	 * Constructor.
	 */
	public LonLat() {
		super();
	}

	/**
	 * Creates a LonLat coordinate from the specified longitude and latitude.
	 * 
	 * @param longi
	 *            longitude.
	 * @param lati
	 *            latitude.
	 */
	public LonLat(Double longi, Double lati) {
		super();
		this.latitude = lati;
		this.longitude = longi;
	}

	/**
	 * Gets the coordinate latitude.
	 * 
	 * @return latitude.
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the coordinate latitude.
	 * 
	 * @param latitude
	 *            latitude.
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the coordinate longitude.
	 * 
	 * @return longitude.
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Gets the coordinate longitude.
	 * 
	 * @param longitude
	 *            longitude.
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
