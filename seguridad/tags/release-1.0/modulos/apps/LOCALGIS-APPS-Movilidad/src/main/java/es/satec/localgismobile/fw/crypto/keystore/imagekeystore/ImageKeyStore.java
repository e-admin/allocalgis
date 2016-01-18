package es.satec.localgismobile.fw.crypto.keystore.imagekeystore;

import java.io.File;
import java.io.IOException;

import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.crypto.keystore.exceptions.KeyNotFoundException;
import es.satec.localgismobile.fw.crypto.keystore.exceptions.KeyStoreAccessException;
import es.satec.localgismobile.fw.crypto.keystore.imagekeystore.pixelpath.IPixelPath;
import es.satec.localgismobile.fw.crypto.keystore.imagekeystore.pixelpath.RandomPixelPath;
import es.satec.localgismobile.fw.crypto.keystore.interfaces.IKeyStore;
import es.satec.localgismobile.fw.image.exceptions.IncorrectImageFormatException;
import es.satec.localgismobile.fw.image.ico.IcoFile;
import es.satec.localgismobile.fw.image.interfaces.IBitmap;
import es.satec.localgismobile.fw.image.interfaces.IBitmapCollection;

/**
 * Almacena una clave dentro de una imagen mediante el uso de técnicas
 * de esteganografía
 * @author jpolo
 *
 */
public class ImageKeyStore implements IKeyStore{
	
	/**
	 * Bitmap que almacena la clave
	 */
	private IBitmap keyBitmap;
	/**
	 * Coleccion de bitmaps donde se almacena la clave
	 */
	private IBitmapCollection keyBitmapCollection;
	/**
	 * Patrón de presencia que indica que en la imagen
	 * hay una clave almacenada
	 */
	private final String presencePattern = "en un lugar de la mancha de cuyo nombre no quiero acordarme.";
	/**
	 * Path donde se encuentra la imagen que almacena
	 * la clave
	 */
	private String iconPath = "";
	/**
	 * Recorrido por los pixeles de la imagen
	 */
	private IPixelPath pixelPath;
	
	/**
	 * Constructor por defecto de la clase
	 *
	 */
	public ImageKeyStore (){
		iconPath = Global.APP_PATH + File.separator + "resources" + File.separator + "open.ico";
		pixelPath = new RandomPixelPath();
	}
	
	/**
	 * Carga la clave almacenada en la imagen que se utiliza como
	 * almacen
	 * @return Array de Bytes que contiene la clave recuperada
	 * @throws KeyNotFoundException Si no se ha encontrado ninguna clave
	 * en la imagen
	 * @throws KeyStoreAccessException Si se ha producido algun error
	 * al acceder al almacen de claves
	 */
	public byte[] loadKey() throws KeyStoreAccessException, KeyNotFoundException {
		try {
			keyBitmapCollection = new IcoFile(iconPath);
			keyBitmap = keyBitmapCollection.getBitmap(0);
		
			pixelPath.resetPath();
		
			if (testPresencePattern() == false)
				throw new KeyNotFoundException();
		
			int keyLength = (decodeByteFromImage() & 0xFF);		
			byte byteArrayKey[] = new byte[keyLength];
		
			for (int i = 0; i < byteArrayKey.length; i++){
				byte currentByte = decodeByteFromImage();
				byteArrayKey[i] = currentByte;
			}
			
			return byteArrayKey;
		}catch (IOException e){
			throw new KeyStoreAccessException();
		}catch (IncorrectImageFormatException e){
			throw new KeyStoreAccessException();
		}
	}
	
	/**
	 * Almacena la clave indicada en la imagen utilizada como almacén
	 * @param password Array de bytes que contiene la clave a guardar.
	 * @throws KeyStoreAccessException Si se produce algun error al
	 * acceder al almacen de claves
	 */
	public void saveKey(byte[] password) throws KeyStoreAccessException{
		try {
			keyBitmapCollection = new IcoFile(iconPath);
			keyBitmap = keyBitmapCollection.getBitmap(0);
		
			pixelPath.resetPath();
		
			encodePresencePatternInImage();		
		
			byte keyLength = (byte) password.length;
		
			encodeByteInImage(keyLength);
			encodeByteArrayInImage(password);
			
			keyBitmapCollection.saveToDisk(iconPath);
		}catch (IOException e){
			throw new KeyStoreAccessException();
		}catch (IncorrectImageFormatException e){
			throw new KeyStoreAccessException();
		}
	}
	
	/**
	 * Elimina la clave del almacen de claves
	 * @throws KeyStoreAccessException
	 */
	public void reset() throws KeyStoreAccessException{
		try {
			keyBitmapCollection = new IcoFile(iconPath);
			keyBitmap = keyBitmapCollection.getBitmap(0);
		
			pixelPath.resetPath();
			
			removePresencePatternFromImage();
			
			keyBitmapCollection.saveToDisk(iconPath);
		}catch (IOException e){
			throw new KeyStoreAccessException();
		}catch (IncorrectImageFormatException e){
			throw new KeyStoreAccessException();
		}
	}
	
	/**
	 * Comprueba si se encuentra el patron de presencia en la imagen
	 * @return Verdadero si se encontro el patron de presencia en la imagen,
	 * falso en caso contrario
	 */
	private boolean testPresencePattern(){
		byte readPatternArray[] = decodeByteArrayFromImage(presencePattern.length());
		String readPattern = new String(readPatternArray);
		
		if (readPattern.equals(presencePattern))
			return true;
		else		
			return false;
	}
	
	/**
	 * Introduce el patron de presencia de clave en la imagen
	 *
	 */
	private void encodePresencePatternInImage(){
		byte bytePattern[] = presencePattern.getBytes();
		
		encodeByteArrayInImage(bytePattern);
	}
	
	/**
	 * Elimina el patron de presencia de clave de la imagen
	 *
	 */
	private void removePresencePatternFromImage(){
		byte bytePattern[] = "Presence pattern removed".getBytes();
		
		encodeByteArrayInImage(bytePattern);
	}
	
	/**
	 * Codifica un array de bytes en una imagen mediante el uso
	 * de técnicas de esteganografía
	 * @param byteArray Array de bytes con los datos que se desean
	 * guardar en la imagen
	 */
	private void encodeByteArrayInImage (byte byteArray[]){		
		for (int i = 0; i < byteArray.length; i++)
			encodeByteInImage(byteArray[i]);
	}
	
	/**
	 * Descodifica un array de bytes de una imagen mediante el
	 * uso de técnicas de esteganografía
	 * @param numberOfBytes Número de bytes a decodificar
	 * @return Array con los bytes descodificados a partir de la imagen
	 */
	private byte[] decodeByteArrayFromImage (int numberOfBytes){
		byte byteArray[] = new byte[numberOfBytes];
		
		for (int i = 0; i < byteArray.length; i++){
			byte currentByte = decodeByteFromImage();
			byteArray[i] = currentByte;
		}
		
		return byteArray;
	}
	
	/**
	 * Codifica un byte en una imagen mediante el uso de técnicas
	 * de esteganografía
	 * @param originalByte Byte que se desea codificar
	 */
	private void encodeByteInImage(byte originalByte){
		for (int j = 0; j < 8; j++){
			int index = pixelPath.getNextPathIndex();
			byte mask = (byte) (0x80 >> j);
			byte bit = (byte) (originalByte & mask);
			
			bit = (byte) (bit >> 7-j);
			byte pixelValue = keyBitmap.getByte(index);
			mask = (byte) 0xFE;
			pixelValue &= mask;
			pixelValue |= bit;
			keyBitmap.setByte(index ,pixelValue);
		}
	}
	
	/**
	 * Descodifica un byte a partir de una imagen mediante el uso
	 * de técnicas de esteganografía
	 * @return Byte obtenido de la imagen
	 */
	private byte decodeByteFromImage(){
		byte currentByte = 0;
		for (int j = 0; j < 8; j++){
			int index = pixelPath.getNextPathIndex();
			byte mask = (byte) (0x01);
			byte pixelValue =  keyBitmap.getByte(index);
			byte bit = (byte) (pixelValue & mask);
			bit = (byte) (bit << 7-j);			
			currentByte = (byte) (currentByte | bit);
		}
		return currentByte;
	}
}
