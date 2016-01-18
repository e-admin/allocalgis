package es.satec.svgviewer.localgis;

/**
 * Representa una región de la tierra mediante dos esquinas opuestas de un rectángulo
 * expresadas en coordenadas geodésicas.
 * @author jpresa
 */
public class BoundingBox {

	/**
	 * Longitud mínima de la región.
	 */
	private double minLongitude;
	/**
	 * Latitud mínima de la región.
	 */
	private double minLatitude;
	/**
	 * Longitud máxima de la región.
	 */
	private double maxLongitude;
	/**
	 * Latitud máxima de la región.
	 */
	private double maxLatitude;
	
	/**
	 * @param minLongitude Longitud mínima de la región.
	 * @param minLatitude Latitud mínima de la región.
	 * @param maxLongitude Longitud máxima de la región.
	 * @param maxLatitude Latitud máxima de la región.
	 */
	public BoundingBox(double minLongitude, double minLatitude, double maxLongitude, double maxLatitude) {
		this.minLongitude = minLongitude;
		this.minLatitude = minLatitude;
		this.maxLongitude = maxLongitude;
		this.maxLatitude = maxLatitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}
	
	/**
	 * Indica si un punto geodésico está contenido en esta región.
	 * @param longitude Longitud del punto.
	 * @param latitude Latitud del punto.
	 * @return true si el punto está contenido; false en caso contrario.
	 */
	public boolean contains(double longitude, double latitude) {
		if (minLongitude>longitude) return false;
		if (minLatitude>latitude) return false;
		if (maxLongitude<longitude) return false;
		if (maxLatitude<latitude) return false;
		return true;
	}

	/**
	 * Indica si otra región está totalmente contenida en esta región.
	 * @param other La otra región.
	 * @return true si la región está contenida; false en caso contrario.
	 */
	public boolean contains(BoundingBox other) {
		if (minLongitude>other.minLongitude) return false;
		if (minLatitude>other.minLatitude) return false;
		if (maxLongitude<other.maxLongitude) return false;
		if (maxLatitude<other.maxLatitude) return false;
		return true;
	}

	/** Indica si otra región coincide parcial o totalmente con esta región.
	 * @param other La otra región.
	 * @return true si hay intersección; false si son disjuntas.
	 */
	public boolean intersects(BoundingBox other) {
		if (minLongitude > other.maxLongitude) return false; // sale por la izquierda
		if (maxLongitude < other.minLongitude) return false; // sale por la derecha
		if (minLatitude > other.maxLatitude) return false; // sale por abajo
		if (maxLatitude < other.minLatitude) return false; // sale por arriba
		return true;
	}
	/**
	 * Indica si el objeto proporcionado es equivalente al actual.
	 * @param obj El objeto proporcionado.
	 * @return true si el objeto es una instancia de BoundingBox y sus coordenadas límite son las mismas.
	 */
	public boolean equals(Object obj) {
		if (obj instanceof BoundingBox) {
			BoundingBox another = (BoundingBox) obj;
			return another.minLongitude==this.minLongitude && another.minLatitude==this.minLatitude
				&& another.maxLongitude==this.maxLongitude && another.maxLatitude==this.maxLatitude;
		}
		else return false;
	}

	/**
	 * Devuelve una representación del objeto como una cadena de texto, con el formato
	 * "longitud mínima, latitud mínima, longitud máxima, latitud máxima".
	 * @return Representación del objeto como una cadena de texto.
	 */
	public String toString() {
		return minLongitude + "," + minLatitude + "," + maxLongitude + "," + maxLatitude;
	}

}
