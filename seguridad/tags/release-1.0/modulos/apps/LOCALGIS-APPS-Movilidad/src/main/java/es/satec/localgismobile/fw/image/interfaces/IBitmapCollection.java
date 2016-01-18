package es.satec.localgismobile.fw.image.interfaces;

import java.io.IOException;

import es.satec.localgismobile.fw.image.exceptions.IncorrectImageFormatException;

/**
 * Interfaz que representa una colección de bitmaps
 * @author jpolo
 *
 */
public interface IBitmapCollection {
	/**
	 * Obtiene el número de bitmaps de la colección
	 * @return Número de bitmaps de la colección
	 */
	public int getNumberOfBitmaps();
	
	/**
	 * Obtiene el bitmap indicada por el índice
	 * @param index Índice del bitmap que se desea obtener
	 * @return Bitmap correspondiente al índice indicado
	 */
	public IBitmap getBitmap(int index);
	
	/**
	 * Carga la colección de bitmaps en disco
	 * @param path Ruta de donde cargar la colección
	 * @throws IOException Si se produce algún error durante el acceso
	 * a disco
	 * @throws IncorrectImageFormatException Si la colección de bitmaps
	 * no se encontraba en el formato esperado
	 */
	public void loadFromDisk(String path) throws IOException, IncorrectImageFormatException;
	
	/**
	 * Guarda la colección de bitmaps en disco
	 * @param path Ruta de donde guardar la colección
	 * @throws IOException Si se produce algún error durante el acceso
	 * a disco
	 */
	public void saveToDisk(String path) throws IOException;
}
