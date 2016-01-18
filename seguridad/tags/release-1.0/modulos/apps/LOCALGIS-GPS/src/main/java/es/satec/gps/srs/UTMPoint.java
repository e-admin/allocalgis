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
