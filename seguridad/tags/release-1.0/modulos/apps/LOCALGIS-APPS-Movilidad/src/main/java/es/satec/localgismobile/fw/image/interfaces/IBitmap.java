package es.satec.localgismobile.fw.image.interfaces;

/**
 * Interfaz que representa una imagen de tipo bitmap
 * @author jpolo
 *
 */
public interface IBitmap {
	
	/**
	 * Obtiene el byte indicado por el índice
	 * @param index Índice del que se desea obtener el byte del bitmap
	 * @return Byte del bitmap indicado por el índice
	 */
	public byte getByte(int index);

	/**
	 * Fija el valor del byte indicado por el índice
	 * @param index Índice del byte cuyo valor se desea fijar
	 * @param value Valor que se desea para ese byte
	 */
	public void setByte(int index, byte value);	
	
	/**
	 * Obtiene el número de bytes del bitmap
	 * @return Número de bytes del bitmap
	 */
	public int getNumberOfBytes();
}
