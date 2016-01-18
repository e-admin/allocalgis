/**
 * IcoDirectoryEntry.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
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
