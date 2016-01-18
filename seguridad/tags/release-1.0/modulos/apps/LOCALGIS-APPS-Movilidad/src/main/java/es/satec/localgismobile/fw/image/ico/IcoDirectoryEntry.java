package es.satec.localgismobile.fw.image.ico;

/**
 * Clase que almacena la información relativa a una imagen dentro
 * de la lista de imagenes que puede contener un icono
 * @author jpolo
 *
 */
public class IcoDirectoryEntry {
	/**
	 * Ancho de la imagen
	 */
	private int width;
	/**
	 * Altura de la imagen
	 */
	private int height;
	/**
	 * Número de colores de la imagen
	 */
	private int colourCount;
	/**
	 * Número de planos de la imagen (Componentes de color)
	 */
	private int numberOfPlanes;
	/**
	 * Número de bits por pixel
	 */
	private int bitsPerPixel;
	/**
	 * Número de bytes de la imagen
	 */
	private int numberOfBytes;
	/**
	 * Desplazamiento desde el comienzo del archivo del icono
	 * a partir del cual se encuentra la imagen
	 */
	private int imageOffset;	
	/**
	 * Contenido de la imagen
	 */
	private IcoImage icoImage;
	
	public IcoImage getIcoImage() {
		return icoImage;
	}
	public void setIcoImage(IcoImage icoImage) {
		this.icoImage = icoImage;
	}
	public int getBitsPerPixel() {
		return bitsPerPixel;
	}
	public void setBitsPerPixel(int bitsPerPixel) {
		this.bitsPerPixel = bitsPerPixel;
	}
	public int getColourCount() {
		return colourCount;
	}
	public void setColourCount(int colourCount) {
		this.colourCount = colourCount;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getImageOffset() {
		return imageOffset;
	}
	public void setImageOffset(int imageOffset) {
		this.imageOffset = imageOffset;
	}
	public int getNumberOfBytes() {
		return numberOfBytes;
	}
	public void setNumberOfBytes(int numberOfBytes) {
		this.numberOfBytes = numberOfBytes;
	}
	public int getNumberOfPlanes() {
		return numberOfPlanes;
	}
	public void setNumberOfPlanes(int numberOfPlanes) {
		this.numberOfPlanes = numberOfPlanes;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
}
