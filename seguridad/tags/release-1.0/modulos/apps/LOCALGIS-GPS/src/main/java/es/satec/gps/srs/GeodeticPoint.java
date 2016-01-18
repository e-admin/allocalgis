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
