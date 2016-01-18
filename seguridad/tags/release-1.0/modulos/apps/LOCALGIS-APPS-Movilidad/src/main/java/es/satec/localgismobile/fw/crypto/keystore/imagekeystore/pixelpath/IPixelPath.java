package es.satec.localgismobile.fw.crypto.keystore.imagekeystore.pixelpath;

/**
 * Genera un recorrido a través de los píxeles de una imagen
 * @author jpolo
 *
 */
public interface IPixelPath {
	/**
	 * Obtiene el índice del siguiente pixel
	 * @return El índice del siguiente pixel
	 */
	public int getNextPathIndex();
	
	/**
	 * Resetea el recorrido, situándose al comienzo
	 * del mismo
	 *
	 */
	public void resetPath();
	
	/**
	 * Obtiene la posición enésima del recorrido de píxeles
	 * @param index Índice de la posición del recorrido de la imagen
	 * de la que se quiere conocer el píxel correspondiente
	 * @return El píxel correspondiente a la posición indicada
	 * del recorrido de la imagen
	 */
	public int getPathIndex(int index);
}
