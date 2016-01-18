/**
 * IcoFileWriter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.image.ico;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * Escribe un icono a disco
 * @author jpolo
 *
 */
public class IcoFileWriter {
	/**
	 * Stream que se encarga de escrbir el archivo
	 */
	private FileOutputStream fileWriter;
	
	/**
	 * Escribe un icono a disco
	 * @param path Ruta del fichero a escribir
	 * @param icoFile Icono a guardar
	 * @throws IOException Si se produce un error al escribir a disco
	 */
	public void writeIcoFile(String path, IcoFile icoFile) throws IOException{
		fileWriter = new FileOutputStream(path);
		
		IcoDirectoryList icoDirectoryList = icoFile.getIcoDirectoryList();
		writeDirectoryList(icoDirectoryList);
		
		Vector directoryEntries = icoDirectoryList.getDirectoryEntries();
		for (int i = 0; i < directoryEntries.size(); i++){
			IcoDirectoryEntry icoDirectoryEntry = (IcoDirectoryEntry) directoryEntries.elementAt(i);
			writeDirectoryEntry(icoDirectoryEntry);
		}
		
		for (int i = 0; i < directoryEntries.size(); i++){
			IcoDirectoryEntry icoDirectoryEntry = (IcoDirectoryEntry) directoryEntries.elementAt(i);
			IcoImage icoImage = icoDirectoryEntry.getIcoImage();
			writeImage(icoImage);
		}
		
		fileWriter.close();
	}
	
	/**
	 * Escribe el directorio de entradas del icono
	 * @param icoDirectoryList
	 * @throws IOException Si se produce un error al escribir a disco
	 */
	public void writeDirectoryList(IcoDirectoryList icoDirectoryList) throws IOException {
		writeUInt2(0); // Reserved
		writeUInt2(icoDirectoryList.getType());
		writeUInt2(icoDirectoryList.getNumberOfEntries());
	}
	
	/**
	 * Escribe una entrada de directorio
	 * @param icoDirectoryEntry Entrada de directorio
	 * @throws IOException Si se produce un error al escribir a disco
	 */
	public void writeDirectoryEntry(IcoDirectoryEntry icoDirectoryEntry) throws IOException {
		writeUInt1(icoDirectoryEntry.getWidth());
		writeUInt1(icoDirectoryEntry.getHeight());
		writeUInt1(icoDirectoryEntry.getColourCount());
		writeUInt1(0);
		writeUInt2(icoDirectoryEntry.getNumberOfPlanes());
		writeUInt2(icoDirectoryEntry.getBitsPerPixel());
		writeUInt4(icoDirectoryEntry.getNumberOfBytes());
		writeUInt4(icoDirectoryEntry.getImageOffset());
	}
	
	/**
	 * Escribe una imagen en el icono
	 * @param icoImage Imagen a escribir
	 * @throws IOException Si se produce un error al escribir a disco
	 */
	public void writeImage(IcoImage icoImage) throws IOException{
		writeHeader(icoImage.getHeader());
		
		writeByteArray(icoImage.getXorMask());
		writeByteArray(icoImage.getAndMask());
	}
	
	/**
	 * Escribe la cabecera de una imagen
	 * @param icoHeader Cabecera de la imagen
	 * @throws IOException Si se produce un error al escribir a disco
	 */
	public void writeHeader(IcoHeader icoHeader) throws IOException{
		writeUInt4(icoHeader.getHeaderSize());
		writeUInt4(icoHeader.getWidth());
		writeUInt4(icoHeader.getHeight());
		writeUInt2(icoHeader.getNumberOfPlanes());
		writeUInt2(icoHeader.getBitsPerPixel());
		writeUInt4(icoHeader.getCompression());
		writeUInt4(icoHeader.getImageSize());
		writeUInt4(0); // XpixelsPerM -> NOT USED 0
		writeUInt4(0); // YpixelsPerM -> NOT USED 0
		writeUInt4(0); // ColorsUsed -> NOT USED 0
		writeUInt4(0); // ColorsImportant -> NOT USED 0
	}
	
	/**
	 * Escribe un entero de un byte
	 * @param value Entero del que se escribirá su byte más bajo
	 * @throws IOException Si se produce un error al escribir a disco
	 */
	private void writeUInt1(int value) throws IOException{
		fileWriter.write(value & 0xFF);
	}
	
	/**
	 * Escribe un entero de dos bytes
	 * @param value Entero del que se escribirán sus dos bytes más bajos
	 * @throws IOException Si se produce un error al escribir a disco
	 */
	private void writeUInt2(int value) throws IOException{
		byte[] writeBuffer = new byte[2];
		writeBuffer[0] = (byte) (value & 0x000000FF);
		writeBuffer[1] = (byte) ((value & 0x0000FF00) >>  8);
		fileWriter.write(writeBuffer);
	}
	
	/**
	 * Escribe un entero de 4 bytes
	 * @param value Entero a escribir
	 * @throws IOException Si se produce un error al escribir a disco
	 */
	private void writeUInt4(int value) throws IOException{
		byte[] writeBuffer = new byte[4];
		writeBuffer[0] = (byte) (value & 0x000000FF);
		writeBuffer[1] = (byte) ((value & 0x0000FF00) >>  8);
		writeBuffer[2] = (byte) ((value & 0x00FF0000) >> 16);
		writeBuffer[3] = (byte) ((value & 0xFF000000) >> 24);
		fileWriter.write(writeBuffer);
	}
	
	/**
	 * Escribe un array de bytes en disco
	 * @param byteArray Array de bytes que se desea almacenar
	 * @throws IOException Si se produce un error al escribir a disco
	 */
	private void writeByteArray(byte byteArray[]) throws IOException{
		fileWriter.write(byteArray);
	}
}
