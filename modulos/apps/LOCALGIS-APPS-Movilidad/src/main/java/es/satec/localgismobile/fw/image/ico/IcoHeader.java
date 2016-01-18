/**
 * IcoHeader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.image.ico;

/**
 * Clase que contiene la cabecera del icono con la información
 * acerca de su contenido
 * @author jpolo
 *
 */
public class IcoHeader {
	/**
	 * Tamaño de la cabecera
	 */
	protected int headerSize; // Siempre 40
	/**
	 * Ancho del icono
	 */
	protected int width;
	/**
	 * Altura del icono
	 */
	protected int height;
	/**
	 * Número de componentes de color del icono
	 */
	protected int numberOfPlanes;
	/**
	 * Número de bits por píxel
	 */
	protected int bitsPerPixel;
	/**
	 * Tamaño del icono en bytes
	 */
	protected int imageSize;
	/**
	 * Tipo de compresión del icono
	 */
	protected int compression;
	
	public int getCompression() {
		return compression;
	}
	public void setCompression(int compression) {
		this.compression = compression;
	}
	public int getBitsPerPixel() {
		return bitsPerPixel;
	}
	public void setBitsPerPixel(int bitsPerPixel) {
		this.bitsPerPixel = bitsPerPixel;
	}
	public int getHeaderSize() {
		return headerSize;
	}
	public void setHeaderSize(int headerSize) {
		this.headerSize = headerSize;
	}
	public int getHeight() {
		return height;
	}
	public int getNumberOfPlanes() {
		return numberOfPlanes;
	}
	public void setNumberOfPlanes(int numberOfPlanes) {
		this.numberOfPlanes = numberOfPlanes;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getImageSize() {
		return imageSize;
	}
	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	
}
