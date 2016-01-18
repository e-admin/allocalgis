/**
 * IcoFile.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.image.ico;

import java.io.IOException;

import es.satec.localgismobile.fw.image.exceptions.IncorrectImageFormatException;
import es.satec.localgismobile.fw.image.interfaces.IBitmap;
import es.satec.localgismobile.fw.image.interfaces.IBitmapCollection;

/**
 * Clase que representa un icono. Un icono es un archivo que contiene
 * una colección de bitmaps
 * @author jpolo
 *
 */
public class IcoFile implements IBitmapCollection {
	/**
	 * Directorio con la información acerca de las imágenes
	 * presentes en el icono
	 */
	private IcoDirectoryList icoDirectoryList;
	
	public IcoFile(){}
	
	/**
	 * Construye un icono, cargando el fichero indicado
	 * @param path Ruta de disco del icono
	 * @throws IOException Si se produce un error al acceder al fichero
	 * @throws IncorrectImageFormatException Si el fichero no es un icono o el
	 * icono no tiene el formato esperado
	 */
	public IcoFile(String path) throws IOException, IncorrectImageFormatException{
		loadFromDisk(path);
	}
	
	public IcoDirectoryList getIcoDirectoryList() {
		return icoDirectoryList;
	}
	
	public void setIcoDirectoryList(IcoDirectoryList icoDirectoryList) {
		this.icoDirectoryList = icoDirectoryList;
	}
	
	/**
	 * Carga de disco el icono
	 * @param path Ruta de disco del icono
	 * @throws IOException Si se produce un error al acceder al fichero
	 * @throws IncorrectImageFormatException Si el fichero no es un icono o el
	 * icono no tiene el formato esperado
	 */
	public void loadFromDisk(String path) throws IOException, IncorrectImageFormatException{
		IcoFileReader icoFileReader = new IcoFileReader();
		
		icoDirectoryList = icoFileReader.readIcoFile(path);		
	}
	
	/**
	 * Almacena el icono a disco
	 * @param path Ruta de disco del icono
	 * @throws IOException Si se produce un error al acceder al fichero
	 */
	public void saveToDisk(String path) throws IOException{
		IcoFileWriter icoFileWriter = new IcoFileWriter();
		icoFileWriter.writeIcoFile(path, this);	
	}
	
	/**
	 * Obtiene el número de imágenes presentes en el icono
	 * @return Número de imágenes presentes en el icono
	 */
	public int getNumberOfImages(){
		return icoDirectoryList.getNumberOfEntries();
	}
	
	/**
	 * Obtiene la imagen indicada por el índice del icono
	 * @param index Índice de la imagen dentro del icono
	 * @return La imagen presente en el icono
	 */
	public IcoImage getIcoImage(int index){
		IcoDirectoryEntry icoDirectoryEntry = (IcoDirectoryEntry)
			icoDirectoryList.getDirectoryEntries().elementAt(index);
		
		return icoDirectoryEntry.getIcoImage();
	}
	
	/**
	 * Obtiene la altura de la imagen indicada por el índice
	 * @param index Índice de la imagen de la que se desea conocer la
	 * altura
	 * @return Altura de la imagen indicada
	 */
	public int getHeight(int index){
		IcoDirectoryEntry icoDirectoryEntry = (IcoDirectoryEntry)
			icoDirectoryList.getDirectoryEntries().elementAt(index);
		
		return icoDirectoryEntry.getHeight();
	}
	
	/**
	 * Obtiene el ancho de la imagen indicada por el índice
	 * @param index Índice de la imagen de la que se desea conocer
	 * el ancho
	 * @return Ancho de la imagen indicada
	 */
	public int getWidth(int index){
		IcoDirectoryEntry icoDirectoryEntry = (IcoDirectoryEntry)
			icoDirectoryList.getDirectoryEntries().elementAt(index);
		
		return icoDirectoryEntry.getWidth();
	}

	public IBitmap getBitmap(int index) {
		IcoImage image = icoDirectoryList.getDirectoryEntry(index).getIcoImage();
		return image;
	}

	public int getNumberOfBitmaps() {
		return icoDirectoryList.getNumberOfEntries();
	}
}
