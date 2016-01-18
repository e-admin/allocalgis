/**
 * IcoImage.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.image.ico;

import es.satec.localgismobile.fw.image.interfaces.IBitmap;

/**
 * Clase que representa una imagen de un icono. Es una imagen
 * de tipo bitmap
 * @author jpolo
 *
 */
public class IcoImage implements IBitmap {
	/**
	 * Cabecera con la información del bitmap
	 */
	private IcoHeader header;
	/**
	 * Array de bytes que contiene la paleta de colores
	 * del bitmap
	 */
	private byte colors[];
	/**
	 * Máscara que contiene la información sobre el color
	 * de los píxeles. Es el bitmap en si.
	 */
	private byte xorMask[];
	/**
	 * Máscara que define la forma del icono. Usada para transparencia
	 * e inversiones del color
	 */
	private byte andMask[];
	
	public byte[] getAndMask() {
		return andMask;
	}
	public void setAndMask(byte[] andMask) {
		this.andMask = andMask;
	}
	public byte[] getColors() {
		return colors;
	}
	public void setColors(byte[] colors) {
		this.colors = colors;
	}
	public IcoHeader getHeader() {
		return header;
	}
	public void setHeader(IcoHeader header) {
		this.header = header;
	}
	public byte[] getXorMask() {
		return xorMask;
	}
	public void setXorMask(byte[] xorMask) {
		this.xorMask = xorMask;
	}
	
	public int getWidth(){
		return header.getWidth();
	}
	
	public int getHeight(){
		return header.getWidth();
	}
	
	public byte[] getPixel(int x, int y){
		byte pixel[] = new byte[3];
		pixel[0] = xorMask[x * header.getWidth() * 3 + y * 3];
		pixel[1] = xorMask[x * header.getWidth() * 3 + y * 3 + 1];
		pixel[2] = xorMask[x * header.getWidth() * 3 + y * 3 + 2];
		
		return pixel;
	}
	
	public void setPixel(int x, int y, byte pixel[]){
		xorMask[x * header.getWidth() * 3 + y * 3] = pixel[0];
		xorMask[x * header.getWidth() * 3 + y * 3 + 1] = pixel[1];
		xorMask[x * header.getWidth() * 3 + y * 3 + 2] = pixel[2];
	}
	
	public byte getXorMask(int index){
		return xorMask[index];
	}
	
	public void setXorMask(int index, byte value){
		xorMask[index] = value;
	}
	
	public byte getByte(int index){
		return getXorMask(index);
	}
	
	public void setByte(int index, byte value){
		setXorMask(index, value);
	}
	
	public int getNumberOfBytes(){
		return xorMask.length;
	}
}
