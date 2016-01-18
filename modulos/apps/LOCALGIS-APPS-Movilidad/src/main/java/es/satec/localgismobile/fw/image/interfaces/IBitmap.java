/**
 * IBitmap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
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
