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
