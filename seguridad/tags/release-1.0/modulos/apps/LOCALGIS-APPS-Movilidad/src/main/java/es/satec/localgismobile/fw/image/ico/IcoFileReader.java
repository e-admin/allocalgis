package es.satec.localgismobile.fw.image.ico;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import es.satec.localgismobile.fw.image.exceptions.IncorrectImageFormatException;

/**
 * Clase que lee un icono de disco
 * @author jpolo
 *
 */
public class IcoFileReader {
	/**
	 * Directorio con todas las entradas del icono
	 */
	private IcoDirectoryList icoDirectoryList;
	
	/**
	 * Stream utilizado para leer el fichero del icono
	 */
	private FileInputStream fileReader;

	/**
	 * Lee un icono de disco.
	 * @param path Ruta de disco del icono
	 * @return La entrada de directorios del icono
	 * @throws IOException Si se produce un error al acceder a disco
	 * @throws IncorrectImageFormatException Si el archivo no es un icono
	 * o no se encuentra en el formato esperado
	 */
	public IcoDirectoryList readIcoFile(String path) throws IOException, IncorrectImageFormatException{
		fileReader = new FileInputStream(path);
		
		icoDirectoryList = new IcoDirectoryList();
		
		readDirectoryList();
		
		for (int i = 0; i < icoDirectoryList.getNumberOfEntries(); i++){
			Vector vectorIcoDirEntry = icoDirectoryList.getDirectoryEntries();
			vectorIcoDirEntry.addElement(readDirectoryEntry());
		}
		
		Vector vectorIcoDirEntry = icoDirectoryList.getDirectoryEntries();
		
		for (int i = 0; i < icoDirectoryList.getNumberOfEntries(); i++) {
			IcoDirectoryEntry directoryEntry = (IcoDirectoryEntry) vectorIcoDirEntry.elementAt(i);
			if (directoryEntry.getBitsPerPixel() != 24)
				throw new IncorrectImageFormatException("El icono seleccionado no tiene una profundidad de color de 24 bits");
			if (directoryEntry.getHeight() != 32)
				throw new IncorrectImageFormatException("El icono seleccionado no tiene un tamaño de 32x32");
			if (directoryEntry.getWidth() != 32)
				throw new IncorrectImageFormatException("El icono seleccionado no tiene un tamaño de 32x32");
		}
		
		for (int i = 0; i < icoDirectoryList.getNumberOfEntries(); i++){
			IcoDirectoryEntry icoDirectoryEntry = (IcoDirectoryEntry) icoDirectoryList.getDirectoryEntries().elementAt(i);
			icoDirectoryEntry.setIcoImage(readIcoImage());		
		}
		
		fileReader.close();
		
		return icoDirectoryList;
	}
	
	/**
	 * Lee un entero sin signo de un byte.
	 * @return Entero leído
	 * @throws IOException Si se produce un error al leer de disco
	 */
	protected int readUInt1() throws IOException{
		int readValue = 0;
		readValue = fileReader.read();
		return readValue;
	}
	
	/**
	 * Lee un entero sin signo de 2 bytes
	 * @return Entero leído
	 * @throws IOException Si se produce un error al leer de disco
	 */
	protected int readUInt2() throws IOException{
		int readValue = 0;
		int part1, part2;
		part1 = fileReader.read() ;
		part2 = fileReader.read() << 8;
		readValue = part1 | part2;
		return readValue;
	}
	
	/**
	 * Lee un entero de 4 bytes
	 * @return Entero leído
	 * @throws IOException Si se produce un error al leer de disco
	 */
	protected int readUInt4() throws IOException{
		int readValue = 0;
		byte[] readBuffer = new byte[4];
		fileReader.read(readBuffer);
		
		for(int i = 4 - 1; i >= 0; i--){
			readValue <<= 8;
			readValue += readBuffer[i] & 0xff;
	    }
		
		return readValue;		
	}
	
	/**
	 * Lee un entero del número de bytes indicado, hasta cuatro bytes
	 * como máximo
	 * @param numberOfBytes Número de bytes que se desean leer
	 * @return Entero leído
	 * @throws IOException Si se produce un error al leer de disco
	 */
	protected int readUpTo4Bytes(int numberOfBytes) throws IOException {	
		int readValue = 0;
		
		if (numberOfBytes == 1) {
			readValue = fileReader.read();
			return readValue;
		}
		else if (numberOfBytes == 2) {
			int part1, part2;
			part1 = fileReader.read() ;
			part2 = fileReader.read() << 8;
			readValue = part1 | part2;
			return readValue;
		}
		else if (numberOfBytes == 4) {
			byte[] readBuffer = new byte[4];
			fileReader.read(readBuffer);
			
			for(int i = numberOfBytes - 1; i >= 0; i--){
				readValue <<= 8;
				readValue += readBuffer[i] & 0xff;
		    }
			
			return readValue;
		}
		
		return readValue;
	}
	
	/**
	 * Lee un array de bytes de disco
	 * @param numberOfBytes Número de bytes a leer
	 * @return Bytes leídos
	 * @throws IOException Si se produce un error al leer de disco
	 */
	protected byte[] readByteArray(int numberOfBytes) throws IOException{
		byte readBytes[] = new byte[numberOfBytes];
		
		fileReader.read(readBytes);
		
		return readBytes;
	}
	
	/**
	 * Lee el directorio de entradas del icono
	 * @throws IOException Si se produce un error al leer de disco
	 */
	private void readDirectoryList() throws IOException{
		icoDirectoryList = new IcoDirectoryList();
		
		readUpTo4Bytes(2); // Reserved
		icoDirectoryList.setType(readUpTo4Bytes(2));
		icoDirectoryList.setNumberOfEntries(readUpTo4Bytes(2));
	}
	
	/**
	 * Lee el contenido de una entrada de directorio
	 * @return Entrada de directorio
	 * @throws IOException
	 */
	private IcoDirectoryEntry readDirectoryEntry() throws IOException {
		IcoDirectoryEntry icoDirectoryEntry = new IcoDirectoryEntry();
		
		icoDirectoryEntry.setWidth(readUpTo4Bytes(1));
		icoDirectoryEntry.setHeight(readUpTo4Bytes(1));
		icoDirectoryEntry.setColourCount(readUpTo4Bytes(1));
		readUpTo4Bytes(1); // reserved
		icoDirectoryEntry.setNumberOfPlanes(readUpTo4Bytes(2));
		icoDirectoryEntry.setBitsPerPixel(readUpTo4Bytes(2));
		icoDirectoryEntry.setNumberOfBytes(readUpTo4Bytes(4));
		icoDirectoryEntry.setImageOffset(readUpTo4Bytes(4));
		
		return icoDirectoryEntry;
	}
	
	/**
	 * Lee una imagen del icono
	 * @return Una imagen del icono
	 * @throws IOException Si se produce un error al leer de disco
	 */
	private IcoImage readIcoImage() throws IOException{
		IcoImage icoImage = new IcoImage();
		
		IcoHeader icoHeader = readIcoHeader();
		icoImage.setHeader(icoHeader);
		
//		byte colors[] = readBytes(icoHeader.getWidth() * icoHeader.getHeight() * 4);		
//		icoImage.setColors(colors);
		
		int numberOfBytesOfXorMask = getNumberOfBytesOfMask(icoHeader.getBitsPerPixel()
				,icoHeader.getHeight() / 2, icoHeader.getWidth());
		byte xorMask[] = readByteArray(numberOfBytesOfXorMask);
		icoImage.setXorMask(xorMask);
		
		int numberOfBytesOfAndMask = getNumberOfBytesOfMask(1, icoHeader.getHeight() / 2, icoHeader.getWidth());
		byte andMask[] = readByteArray(numberOfBytesOfAndMask);
		icoImage.setAndMask(andMask);
		
		return icoImage;
	}
	
	/**
	 * Lee la cabecera de la imagen de un icono
	 * @return La cabecera de la imagen
	 * @throws IOException Si se produce un error al leer de disco
	 */
	private IcoHeader readIcoHeader() throws IOException{
		IcoHeader icoHeader = new IcoHeader();
		
		icoHeader.setHeaderSize(readUpTo4Bytes(4));
		icoHeader.setWidth(readUpTo4Bytes(4));
		icoHeader.setHeight(readUpTo4Bytes(4));
		icoHeader.setNumberOfPlanes(readUpTo4Bytes(2));
		icoHeader.setBitsPerPixel(readUpTo4Bytes(2));
		icoHeader.setCompression(readUpTo4Bytes(4));
		icoHeader.setImageSize(readUpTo4Bytes(4));
		readUpTo4Bytes(4); // XpixelsPerM -> NOT USED 0
		readUpTo4Bytes(4); // YpixelsPerM -> NOT USED 0
		readUpTo4Bytes(4); // ColorsUsed -> NOT USED 0
		readUpTo4Bytes(4); // ColorsImportant -> NOT USED 0
				
		return icoHeader;
	}
	
	/**
	 * Calcula el número de bytes de la máscara xor de una imagen
	 * de un icono
	 * @param bitsPerPixel Número de bits por pixel de la imagen
	 * @param height Altura de la imagen
	 * @param width Anchura de la imagen
	 * @return Número de bytes de la máscara
	 */
	private int getNumberOfBytesOfMask(int bitsPerPixel, int height, int width){
		double numberOfBits = bitsPerPixel * height * width;
		double numberOfBytes = Math.ceil(numberOfBits / 32.0) * 4;
		return (new Double(numberOfBytes)).intValue();
	}
}
